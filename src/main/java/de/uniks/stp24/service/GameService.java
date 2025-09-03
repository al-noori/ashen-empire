package de.uniks.stp24.service;

import de.uniks.stp24.dto.*;
import de.uniks.stp24.model.game.*;
import de.uniks.stp24.rest.*;
import de.uniks.stp24.ws.Event;
import de.uniks.stp24.ws.EventListener;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import org.fulib.fx.controller.Subscriber;
import retrofit2.Response;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static de.uniks.stp24.constants.Constants.VARIABLES;

@Singleton
public class GameService {
    private final Game game = new Game();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private CompositeDisposable websocketDisposable = new CompositeDisposable();
    private final ConcurrentHashMap<String, Fraction> fractions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Empire> empires = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Empire> homeSystemEmpireMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Jobs> jobMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Fleet> fleetMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Ship> shipsMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, War> warsMap = new ConcurrentHashMap<>();
    private String owner = "";
    User ownUser;
    private boolean isSpectator = false;
    private final BehaviorSubject<Map<String, VariableDto>> variablesObservable = BehaviorSubject.create();

    private final List<String> variables = new ArrayList<>(VARIABLES);
    @Inject
    GamesApiService gamesApiService;
    @Inject
    UserService userService;
    @Inject
    GameSystemsService gameSystemsService;
    @Inject
    EventListener eventListener;
    @Inject
    GameMembersApiService gameMembersApiService;
    @Inject
    EmpireApiService empireApiService;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    JobApiService jobApiService;
    @Inject
    VariablesApiService variablesApiService;
    @Inject
    Subscriber subscriber;
    @Inject
    FleetsApiService fleetsApiService;
    @Inject
    ShipApiService shipApiService;
    @Inject
    WarsApiService warsApiService;
    @Inject
    ClientChangeService clientChangeService;


    @Inject
    public GameService() {
        game.setId("");
    }

    synchronized public Observable<Game> loadGame(String gameId) {
        /* 1. Get Game by id
         * 2. Get Members by ids
         * 3. Get Users by ids
         * 4. Get Empires by ids
         * 5. Get Fractions by ids
         * */
        websocketDisposable.dispose();
        game.removeYou();
        users.clear();
        empires.clear();
        fractions.clear();
        homeSystemEmpireMap.clear();
        fleetMap.clear();
        jobMap.clear();
        return gamesApiService.getGameById(gameId)
                .subscribeOn(Schedulers.io())
                .flatMap(
                        gameResponseDto -> {
                            CreateGameResponseDto gameResponse = gameResponseDto.body();
                            assert gameResponse != null;
                            game.setId(gameResponse._id());
                            game.setCreatedAt(gameResponse.createdAt());
                            game.setUpdatedAt(gameResponse.updatedAt());
                            game.setName(gameResponse.name());
                            game.setStarted(gameResponse.started());
                            game.setSpeed(gameResponse.speed());
                            game.setPeriod(gameResponse.period());
                            game.setSize(gameResponse.settings().size());
                            game.setPlayersCount(gameResponse.members());
                            game.setMaxPlayers(gameResponse.maxMembers());
                            owner = gameResponse.owner();
                            return gameMembersApiService.getPlayer(game.getId());
                        })
                .flatMap(
                        membersDto -> {
                            assert membersDto.body() != null;
                            Set<String> members = membersDto
                                    .body()
                                    .stream()
                                    .map(GameMembersDto::user)
                                    .filter(user -> !users.containsKey(user))
                                    .collect(Collectors.toSet());
                            return userService.decodeUsers(members).flatMap(
                                    users -> {
                                        synchronized (this.users) {
                                            membersDto.body().forEach(member -> {
                                                if (this.users.containsKey(member.user())) {
                                                    return;
                                                }
                                                User user = new User();
                                                this.users.put(member.user(), user);
                                                user.set_id(member.user());
                                                user.setCreatedAt(member.createdAt());
                                                user.setUpdatedAt(member.updatedAt());
                                                user.setReady(member.ready());
                                                user.setGame(game);
                                                user.setName(users.get(member.user()));
                                            });
                                            game.setOwner(this.users.get(owner));
                                            ownUser = this.users.get(tokenStorage.getUserId());
                                            return empireApiService.getAll(gameId);
                                        }
                                    });
                        })
                .map(empiresDto -> {
                    assert empiresDto.body() != null;
                    empiresDto.body().forEach(
                            empireDto -> {
                                synchronized (empires) {
                                    if (!users.containsKey(empireDto.user())) {
                                        return;
                                    }
                                    Empire empire = new Empire()
                                            .setName(empireDto.name())
                                            .setColor(empireDto.color())
                                            .setDescription(empireDto.description())
                                            .setFlag(empireDto.flag())
                                            .setPortrait(empireDto.portrait())
                                            .setOwner(users.get(empireDto.user()))
                                            .set_id(empireDto._id());
                                    if (empireDto.resources() != null) empire.setResources(empireDto.resources());
                                    if(empireDto.technologies() != null) {
                                        List<Technology> technologies = new ArrayList<>();
                                        empireDto.technologies().forEach(tech -> {
                                            Technology technology = new Technology();
                                            technology.set_id(tech);
                                            technologies.add(technology);
                                        });
                                        empire.withTechnologies(technologies);
                                    }
                                    homeSystemEmpireMap.put(empireDto.homeSystem(), empire);
                                    empires.put(empireDto._id(), empire);
                                }
                            }
                    );
                    setupWebsocket();
                    return game;
                });
    }

    private void setupWebsocket() {
        websocketDisposable.dispose();
        websocketDisposable = new CompositeDisposable();
        websocketDisposable.add(
                eventListener.listen("games." + game.getId() + ".*", CreateGameResponseDto.class)
                        .subscribe(this::handleGameChanges));
        websocketDisposable.add(
                eventListener.listen("games." + game.getId() + ".members.*.*",
                                GameMembersDto.class)
                        .subscribe(this::handleMemberChanges)
        );
        websocketDisposable.add(
                eventListener.listen("games." + game.getId() + ".empires.*.*",
                                GameEmpireDto.class)
                        .subscribe(this::handleEmpireChanges)
        );
        websocketDisposable.add(
                eventListener.listen("games." + game.getId() + ".systems.*.*",
                                FractionDto.class)
                        .subscribe(this::handleFractionsChanges)
        );
    }

    private void initWebsocketsPostJoin() {
        websocketDisposable.add(
                eventListener.listen("games." + game.getId() + ".fleets.*.*",
                                FleetDto.class)
                        .subscribe(this::handleFleetChanges)
        );
        websocketDisposable.add(
                eventListener.listen("games." + game.getId() + ".fleets.*.ships.*.*",
                                ShipDto.class)
                        .subscribe(this::handleShipChanges)
        );
        websocketDisposable.add(
                eventListener.listen("games." + game.getId() + ".wars.*.*",
                                WarsDto.class)
                        .subscribe(this::handleWarChanges)
        );
        if(ownUser == null) return;
        if (ownUser.getEmpire() == null) return;
        websocketDisposable.add(
                eventListener.listen("games." + game.getId() + ".empires." + getOwnUser().getEmpire().get_id() + ".jobs.*.*",
                                JobDto.class)
                        .subscribe(this::handleJobChanges)
        );
    }

    private void handleWarChanges(Event<WarsDto> warsDtoEvent) {
        synchronized (warsMap) {
            if(warsDtoEvent.suffix().equals("deleted")) {
                War war = warsMap.get(warsDtoEvent.data()._id());
                if (war != null) {
                    war.removeYou();
                    warsMap.remove(warsDtoEvent.data()._id());
                }
            } else {
                WarsDto warsDto = warsDtoEvent.data();
                handleWar(warsDto);
            }
        }
    }

    private void handleWar(WarsDto warsDto) {
        War war = warsMap.computeIfAbsent(warsDto._id(), com -> {
            War newWar = new War();
            return newWar;
        });
        war.set_id(warsDto._id());
        war.setCreatedAt(warsDto.createdAt());
        war.setUpdatedAt(warsDto.updatedAt());
        war.setAttacker(empires.get(warsDto.attacker()));
        war.setDefender(empires.get(warsDto.defender()));
        war.setName(warsDto.name());
    }

    private void handleShipChanges(Event<ShipDto> shipDtoEvent) {
        synchronized (shipsMap) {
            Ship ship;
            ShipDto shipDto = shipDtoEvent.data();
            if (shipsMap.get(shipDtoEvent.data()._id()) == null) {
                ship = new Ship();
                shipsMap.put(shipDtoEvent.data()._id(), ship);
            }
            else ship = shipsMap.get(shipDtoEvent.data()._id());
            if(shipDtoEvent.suffix().equals("deleted")) {
                ship.removeYou();
                shipsMap.remove(shipDtoEvent.data()._id());
            } else {
                ship.set_id(shipDto._id());
                ship.setCreatedAt(shipDto.createdAt());
                ship.setUpdatedAt(shipDto.updatedAt());
                ship.setType(shipDto.type());
                ship.setFleet(fleetMap.get(shipDto.fleet()));
                ship.setHealth(shipDto.health());
                ship.setExperience(shipDto.experience());
            }
        }
    }

    private void handleFleetChanges(Event<FleetDto> fleetDtoEvent) {
        synchronized (fleetMap) {
            Fleet fleet;
            FleetDto fleetDto = fleetDtoEvent.data();
            if (fleetMap.get(fleetDtoEvent.data()._id()) == null) {
                fleet = new Fleet();
                fleetMap.put(fleetDtoEvent.data()._id(), fleet);
            }
            else fleet = fleetMap.get(fleetDtoEvent.data()._id());
            if(fleetDtoEvent.suffix().equals("deleted")) {
                if(fleet.getMoving() != null) {
                    subscriber.subscribe(jobApiService.deleteJob(game.getId(), ownUser.getEmpire().get_id(), fleet.getMoving().get_id()));
                    fleet.getMoving().removeYou();
                }
                fleet.removeYou();
                fleetMap.remove(fleetDtoEvent.data()._id());
            } else {
                loadFleet(fleet, fleetDto);
            }
        }
    }

    private void loadFleet(Fleet fleet, FleetDto fleetDto) {
        fleet.set_id(fleetDto._id());
        fleet.setCreatedAt(fleetDto.createdAt());
        fleet.setUpdatedAt(fleetDto.updatedAt());
        fleet.setName(fleetDto.name());
        if(fleetDto.empire() != null) {
            fleet.setEmpire(empires.get(fleetDto.empire()));
        }
        if(fleetDto.location() != null) {
            fleet.setLocation(fractions.get(fleetDto.location()));
        }
        fleet.setSize(fleetDto.size());
    }

    private void handleFractionsChanges(Event<FractionDto> event) {
        synchronized (fractions) {
            if (event.suffix().equals("updated")) {
                Fraction fraction;
                if (fractions.containsKey(event.data()._id())) {
                    fraction = fractions.get(event.data()._id());
                } else {
                    fraction = new Fraction();
                    fraction.setGame(game);
                    fraction.set_id(event.data()._id());
                    fraction.setY(event.data().y());
                    fraction.setX(event.data().x());
                    fraction.setHealth(event.data().health());
                }
                if (event.data().owner() != null && empires.containsKey(event.data().owner())) {
                    Empire empire = empires.get(event.data().owner());
                    fraction.setEmpire(empire);
                }
                if (event.data().homeSystem() != null && homeSystemEmpireMap.containsKey(event.data().homeSystem())) {
                    fraction.setHomeEmpire(homeSystemEmpireMap.get(event.data().homeSystem()));
                }
                if (event.data().buildings() != null) {
                    fraction.withoutBuildings(fraction.getBuildings());
                    fraction.withBuildings(event.data().buildings());
                }
                fraction.setDistricts(event.data().districts());
                fraction.setDistrictSlots(event.data().districtSlots());
                fraction.setPopulation(event.data().population());
                fraction.setCapacity(event.data().capacity());
                if (event.data().upgrade() != null) fraction.setUpgrade(event.data().upgrade());
                fraction.setCreatedAt(event.data().createdAt());
                fraction.setUpdatedAt(event.data().updatedAt());
                fraction.setType(event.data().type());
                fraction.setHealth(event.data().health());
                if (!fractions.containsKey(event.data()._id())) {
                    fractions.put(event.data()._id(), fraction);
                }
            }
        }
    }

    private void handleEmpireChanges(Event<GameEmpireDto> empireDtoEvent) {
        synchronized (empires) {
            switch (empireDtoEvent.suffix()) {
                case "created" -> {
                    Empire empire = new Empire()
                            .setName(empireDtoEvent.data().name())
                            .setColor(empireDtoEvent.data().color())
                            .setDescription(empireDtoEvent.data().description())
                            .setFlag(empireDtoEvent.data().flag())
                            .setPortrait(empireDtoEvent.data().portrait())
                            .set_id(empireDtoEvent.data()._id());
                    if(empireDtoEvent.data().technologies() != null) {
                        List<Technology> technologies = new ArrayList<>();
                        empireDtoEvent.data().technologies().forEach(tech -> {
                            Technology technology = new Technology();
                            technology.set_id(tech);
                            technologies.add(technology);
                        });
                        empire.withTechnologies(technologies);
                    }
                    if (users.containsKey(empireDtoEvent.data().user())) {
                        empire.setOwner(users.get(empireDtoEvent.data().user()));
                    }
                    if (empireDtoEvent.data().homeSystem() != null)
                        homeSystemEmpireMap.put(empireDtoEvent.data().homeSystem(), empire);
                    if (empireDtoEvent.data().resources() != null) empire.setResources(empireDtoEvent.data().resources());
                    empires.put(empireDtoEvent.data()._id(), empire);
                }
                case "updated" -> {
                    Empire empire = empires.get(empireDtoEvent.data()._id());
                    if (empire == null) {
                        handleEmpireChanges(new Event<>("created", empireDtoEvent.data()));
                    } else {
                        empire
                                .setName(empireDtoEvent.data().name())
                                .setColor(empireDtoEvent.data().color())
                                .setDescription(empireDtoEvent.data().description())
                                .setFlag(empireDtoEvent.data().flag())
                                .setPortrait(empireDtoEvent.data().portrait());
                        if (empireDtoEvent.data().resources() != null) {
                            empire.setResources(empireDtoEvent.data().resources());
                        }
                        if (empireDtoEvent.data().technologies() != null) {
                            empireDtoEvent.data().technologies().forEach(tech -> {
                                if(empire.getTechnologies().stream().noneMatch(technology -> technology.get_id().equals(tech))) {
                                    Technology technology = new Technology();
                                    technology.set_id(tech);
                                    empire.withTechnologies(technology);
                                }

                            });
                        }
                    }
                }
                case "deleted" -> {
                    Empire empire = empires.get(empireDtoEvent.data()._id());
                    if (empire != null) {
                        empire.removeYou();
                        empires.remove(empireDtoEvent.data()._id());
                    }
                }
            }
        }
    }

    private void handleMemberChanges(Event<GameMembersDto> event) {
        synchronized (users) {
            switch (event.suffix()) {
                case "created" -> {
                    if (users.containsKey(event.data().user())) {
                        handleMemberChanges(new Event<>("updated", event.data()));
                        return;
                    }
                    websocketDisposable.add(
                            userService.getUserNameById(event.data().user()).subscribe(name -> {
                                        User user = new User();
                                        users.put(event.data().user(), user);
                                        user.set_id(event.data().user());
                                        user.setName(name);
                                        user.setCreatedAt(event.data().createdAt());
                                        user.setUpdatedAt(event.data().updatedAt());
                                        user.setReady(event.data().ready());
                                        user.setGame(game);
                                    }
                            )
                    );
                }
                case "updated" -> {
                    User user = users.get(event.data().user());
                    if (user == null) {
                        handleMemberChanges(new Event<>("created", event.data()));
                    } else {
                        user
                                .setUpdatedAt(event.data().updatedAt())
                                .setReady(event.data().ready());
                    }
                }
                case "deleted" -> {
                    users.get(event.data().user()).setGame(null).setEmpire(null);
                    users.remove(event.data().user());
                }
            }
        }
    }

    private void handleGameChanges(Event<CreateGameResponseDto> event) {
        if (event.suffix().equals("updated")) {
            if (!game.isStarted() && event.data().started()) {
                game.setStarted(true);
            }
            game.setName(event.data().name());
            game.setUpdatedAt(event.data().updatedAt());
            game.setMaxPlayers(event.data().maxMembers());
            game.setPeriod(event.data().period());
            game.setSpeed(event.data().speed());
            game.setSize(event.data().settings().size());
        }
    }

    private void handleJobChanges(Event<JobDto> event) {
        synchronized (jobMap) {
            switch (event.suffix()) {
                case "created" -> {
                    if (jobMap.containsKey(event.data()._id())) {
                        handleJobChanges(new Event<>("updated", event.data()));
                        return;
                    }
                    Jobs job = new Jobs();
                    job.set_id(event.data()._id());
                    job.setCreatedAt(event.data().createdAt());
                    job.setUpdatedAt(event.data().updatedAt());
                    job.setProgress(event.data().progress());
                    job.setTotal(event.data().total());
                    job.setPriority(event.data().priority());
                    job.setType(event.data().type());
                    job.setBuilding(event.data().building());
                    job.setDistrict(event.data().district());
                    job.setTechnology(event.data().technology());
                    job.setShip(event.data().ship());
                    job.setCost(event.data().cost());
                    job.setResult(event.data().result());
                    JobDto jobDto = event.data();
                    List<String> path = jobDto.path();
                    if(jobDto.fleet() != null) {
                        Fleet fleet = fleetMap.get(jobDto.fleet());
                        if (fleet == null) break;
                        job.setFleet(fleet);
                        if(path != null && !path.isEmpty()) {
                            path = path.subList(path.indexOf(fleet.getLocation().get_id()), path.size());
                        }
                    }
                    if (path != null && !path.isEmpty()) {
                        if (!job.getPath().isEmpty()) job.withoutPath(job.getPath());
                        path.forEach(
                                fraction -> job.withPath(fractions.get(fraction))
                        );
                    }
                    jobMap.put(event.data()._id(), job);
                    job.setEmpire(empires.get(event.data().empire()));
                    if (event.data().system() != null) {
                        job.setFraction(fractions.get(event.data().system()));
                    }
                }
                case "updated" -> {
                    if (!jobMap.containsKey(event.data()._id())) {
                        handleJobChanges(new Event<>("created", event.data()));
                        return;
                    }
                    Jobs job = jobMap.get(event.data()._id());
                    if (job == null) {
                        handleJobChanges(new Event<>("created", event.data()));
                    } else {
                        job.setUpdatedAt(event.data().updatedAt());
                        job.setProgress(event.data().progress());
                        job.setTotal(event.data().total());
                        job.setPriority(event.data().priority());
                        job.setType(event.data().type());
                        job.setBuilding(event.data().building());
                        job.setDistrict(event.data().district());
                        job.setTechnology(event.data().technology());
                        job.setShip(event.data().ship());
                        job.setCost(event.data().cost());
                        job.setResult(event.data().result());
                        JobDto jobDto = event.data();
                        List<String> path = jobDto.path();
                        if(jobDto.fleet() != null) {
                            Fleet fleet = fleetMap.get(jobDto.fleet());
                            if (fleet == null) break;
                            job.setFleet(fleet);
                            if(path != null && !path.isEmpty()) {
                                path = path.subList(path.indexOf(fleet.getLocation().get_id()), path.size());
                            }
                        }
                        if (path != null && !path.isEmpty()) {
                            if (!job.getPath().isEmpty()) job.withoutPath(job.getPath());
                            path.forEach(
                                    fraction -> job.withPath(fractions.get(fraction))
                            );
                        }
                    }
                }
                case "deleted" -> {
                    Jobs job = jobMap.get(event.data()._id());
                    if(job != null) {
                        job.removeYou();
                    }
                    jobMap.remove(event.data()._id());
                }
            }
        }
    }

    public Fraction getFraction(String id) {
        return fractions.get(id);
    }

    public List<Fraction> getFractions() {
        return List.copyOf(fractions.values());
    }
    public List<Jobs> getJobs() {
        return List.copyOf(jobMap.values());
    }

    public void destroy() {
        compositeDisposable.dispose();
        websocketDisposable.dispose();
    }

    public Game getGame() {
        return game;
    }

    public User getUser(String id) {
        return users.get(id);
    }

    private void initMap(Response<List<FractionDto>> systems) {
        synchronized (fractions) {
            List<FractionDto> fractions = systems.body();
            assert fractions != null;
            fractions.forEach(fractionDto -> {
                Fraction fraction;
                if (this.fractions.containsKey(fractionDto._id())) {
                    fraction = this.fractions.get(fractionDto._id());
                } else {
                    fraction = new Fraction();
                    fraction.setGame(game);
                    fraction.set_id(fractionDto._id());
                }
                if (fractionDto.owner() != null && empires.containsKey(fractionDto.owner())) {
                    fraction.setEmpire(empires.get(fractionDto.owner()));
                }
                fraction.setName(fractionDto.name());
                fraction.setPopulation(fractionDto.population());
                fraction.setCapacity(fractionDto.capacity());
                if (fractionDto.upgrade() != null) fraction.setUpgrade(fractionDto.upgrade());
                fraction.setX(fractionDto.x());
                fraction.setY(fractionDto.y());
                fraction.setCreatedAt(fractionDto.createdAt());
                fraction.setUpdatedAt(fractionDto.updatedAt());
                fraction.setHealth(fractionDto.health());
                fraction.withoutBuildings(fraction.getBuildings());
                fraction.withBuildings(fractionDto.buildings());
                fraction.setDistricts(fractionDto.districts());
                fraction.setDistrictSlots(fractionDto.districtSlots());
                fraction.setHealth(fractionDto.health());
                fraction.setType(fractionDto.type());
                if (fractionDto.homeSystem() != null && homeSystemEmpireMap.containsKey(fractionDto.homeSystem())) {
                    fraction.setHomeEmpire(homeSystemEmpireMap.get(fractionDto.homeSystem()));
                }
                this.fractions.put(fractionDto._id(), fraction);
            });
            for (int i = 0; i < fractions.size(); i++) {
                for (int j = i + 1; j < fractions.size(); j++) {
                    if (fractions.get(i).links().containsKey(fractions.get(j)._id())) {
                        this.fractions.get(fractions.get(i)._id())
                                .withLinks(this.fractions.get(fractions.get(j)._id()));
                    }
                }
            }
        }
    }

    public Observable<Game> startGame() {
        return gamesApiService.updateGame(game.getId(), true, new CreateGameDto(
                        null,
                        null,
                        true,
                        null,
                        null
                ))
                .subscribeOn(Schedulers.io())
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        game.setStarted(response.body().started());
                    }
                    return joinGame();
                });
    }

    public Observable<Boolean> amIMember(String gameId) {
        if (this.game.getId().equals(gameId)) {
            return Observable.just(this.game.getMembers().stream().anyMatch(user -> user.get_id().equals(tokenStorage.getUserId())));
        }
        return loadGame(gameId).map(game -> game.getMembers().stream().anyMatch(user -> user.get_id().equals(tokenStorage.getUserId())));

    }
    public boolean amIPlayer() {
        return game.getMembers().stream().anyMatch(user -> user.get_id().equals(tokenStorage.getUserId()));
    }

    public Observable<Game> updateUserEmpire(EmpireDto gameEmpireDto) {
        return gameMembersApiService.updateGameMembership(game.getId(), tokenStorage.getUserId(),
                new ChangeMemberDto(
                        null,
                        gameEmpireDto
                )).map(
                response -> {
                    if (response.isSuccessful()) {
                        synchronized (empires) {
                            Empire empire = new Empire();
                            empire.setPortrait(gameEmpireDto.portrait());
                            empire.setColor(gameEmpireDto.color());
                            empire.setDescription(gameEmpireDto.description());
                            empire.setName(gameEmpireDto.name());
                            empire.setFlag(gameEmpireDto.flag());
                            empire.setOwner(ownUser);
                            if (gameEmpireDto.technologies() != null) {
                                List<Technology> technologies = new ArrayList<>();
                                gameEmpireDto.technologies().forEach(tech -> {
                                    Technology technology = new Technology();
                                    technology.set_id(tech.id());
                                    technologies.add(technology);
                                });
                                empire.withTechnologies(technologies);
                            }
                        }
                    }
                    return game;
                }
        );
    }

    public User getOwnUser() {
        return ownUser;
    }

    public Observable<Game> leaveGame() {
        websocketDisposable.dispose();
        subscriber.dispose();
        if (game.isStarted()) {
            return Observable.just(game);
        }
        if (game.getOwner() == null || game.getOwner().get_id().equals(tokenStorage.getUserId())) {
            return Observable.just(game);
        }
        return gameMembersApiService.leaveGame(game.getId(), tokenStorage.getUserId())
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        game.setId("");
                        return Observable.just(game);
                    }
                    return Observable.error(new RuntimeException("Failed to leave game"));
                });
    }

    public Observable<Game> joinGame() {
        return empireApiService.getAll(game.getId())
                .flatMap(res -> {
                    if (res.body() != null) {
                        res.body().forEach(
                                empireDto -> {
                                    synchronized (this.empires) {
                                        if (!users.containsKey(empireDto.user())) {
                                            return;
                                        }
                                        Empire empire = users.get(empireDto.user()).getEmpire();
                                        if (empire == null) {
                                            empire = new Empire();
                                            empire.setOwner(users.get(empireDto.user()));
                                        }
                                        empire.set_id(empireDto._id());
                                        empire.setName(empireDto.name());
                                        empire.setColor(empireDto.color());
                                        empire.setDescription(empireDto.description());
                                        empire.setFlag(empireDto.flag());
                                        empire.setPortrait(empireDto.portrait());
                                        empire.setUpdatedAt(empireDto.updatedAt());
                                        empire.setCreatedAt(empireDto.createdAt());
                                        if (empireDto.resources() != null) empire.setResources(empireDto.resources());
                                        this.empires.put(empireDto._id(), empire);
                                        if(empireDto.technologies() != null) {
                                            List<Technology> technologies = new ArrayList<>();
                                            empireDto.technologies().forEach(tech -> {
                                                Technology technology = new Technology();
                                                technology.set_id(tech);
                                                technologies.add(technology);
                                            });
                                            empire.withTechnologies(technologies);
                                        }
                                    }
                                }
                        );
                    }
                    if (ownUser != null && ownUser.getEmpire() != null) {
                        return empireApiService.getById(game.getId(), ownUser.getEmpire().get_id()).flatMap(emp -> {
                            ownUser.getEmpire().setResources(emp.resources());
                            return gameSystemsService.getSystems(game.getId());
                        });
                    }
                    return gameSystemsService.getSystems(game.getId());
                })
                .flatMap(systems -> {
                    initMap(systems);
                    if(ownUser == null) return Observable.just(new ArrayList<WarsDto>());
                    return warsApiService.getWars(game.getId());
                })
                .flatMap(wars -> {
                    synchronized (warsMap) {
                        wars.forEach(this::handleWar);
                    }
                    return fleetsApiService.getFleets(game.getId());
                })
                .flatMap(fleets -> {
                    List<Observable<List<ShipDto>>> shipObservables = new ArrayList<>();
                    synchronized (fleetMap) {
                        for (FleetDto fleetDto: fleets) {
                            Fleet fleet;
                            if(fleetMap.containsKey(fleetDto._id())) {
                                fleet = fleetMap.get(fleetDto._id());
                            } else {
                                fleet = new Fleet();
                                fleetMap.put(fleetDto._id(), fleet);
                            }
                            loadFleet(fleet, fleetDto);
                            shipObservables.add(shipApiService.getShips(game.getId(), fleet.get_id()));
                        }
                    }
                    Observable<Response<List<JobDto>>> jobsObservable;
                    if (ownUser == null || ownUser.getEmpire() == null) {
                        jobsObservable = Observable.just(Response.success(new ArrayList<>()));
                    } else {
                        jobsObservable = jobApiService.getJobList(game.getId(), ownUser.getEmpire().get_id());
                    }
                    return jobsObservable
                            .flatMap(jobs -> {
                                synchronized (jobMap) {
                                    if (jobs.body() != null) {
                                        jobs.body().forEach(jobDto -> {
                                            Jobs job = new Jobs();
                                            job.setEmpire(empires.get(jobDto.empire()));
                                            if (jobDto.system() != null) {
                                                job.setFraction(fractions.get(jobDto.system()));
                                            }
                                            job.set_id(jobDto._id());
                                            job.setCreatedAt(jobDto.createdAt());
                                            job.setUpdatedAt(jobDto.updatedAt());
                                            job.setProgress(jobDto.progress());
                                            job.setTotal(jobDto.total());
                                            job.setPriority(jobDto.priority());
                                            job.setType(jobDto.type());
                                            job.setBuilding(jobDto.building());
                                            job.setDistrict(jobDto.district());
                                            job.setTechnology(jobDto.technology());
                                            job.setShip(jobDto.ship());
                                            job.setCost(jobDto.cost());
                                            job.setResult(jobDto.result());
                                            List<String> path = jobDto.path();
                                            if(jobDto.fleet() != null) {
                                                Fleet fleet = fleetMap.get(jobDto.fleet());
                                                job.setFleet(fleet);
                                                if(path != null && !path.isEmpty() && fleet != null) {
                                                    System.out.println(path);
                                                    System.out.println(fleet.getLocation());
                                                    path = path.subList(path.indexOf(fleet.getLocation().get_id()), path.size());
                                                }
                                            }
                                            if (path != null  && !path.isEmpty()) {
                                                if (!job.getPath().isEmpty()) job.withoutPath(job.getPath());
                                                path.forEach(
                                                        fraction -> job.withPath(fractions.get(fraction))
                                                );
                                            }
                                            jobMap.put(jobDto._id(), job);
                                        });
                                    }
                                }
                                if (shipObservables.isEmpty()) return Observable.just(game);
                                return Observable.zip(shipObservables, shipsMaps ->
                                        {
                                            List<ShipDto> allShips = new ArrayList<>();
                                            for (Object ships : shipsMaps) {
                                                allShips.addAll((List<ShipDto>) ships);
                                            }
                                            return allShips;
                                        })
                                        .map(ships -> {
                                            synchronized (shipsMap) {
                                                for (ShipDto shipDto : ships) {
                                                    Ship ship = shipsMap.getOrDefault(shipDto._id(), new Ship());
                                                    ship.set_id(shipDto._id());
                                                    ship.setType(shipDto.type());
                                                    ship.setCreatedAt(shipDto.createdAt());
                                                    ship.setUpdatedAt(shipDto.updatedAt());
                                                    ship.setHealth(shipDto.health());
                                                    ship.setExperience(shipDto.experience());
                                                    if (shipDto.empire() != null) ship.setEmpire(empires.get(shipDto.empire()));
                                                    ship.setFleet(fleetMap.get(shipDto.fleet()));
                                                    shipsMap.put(shipDto._id(), ship);
                                                }
                                            }
                                            return game;
                                        });
                        });
                })
                .doOnComplete(() -> {
                    initWebsocketsPostJoin();
                    if (game.getMembers().stream().noneMatch(user -> user.get_id().equals(tokenStorage.getUserId()))) {
                        this.isSpectator = true;
                    }
                    if(ownUser != null && ownUser.getEmpire() != null) {
                        updateVariables();
                        subscriber.listen(game.listeners(), Game.PROPERTY_PERIOD, evt -> {
                            clientChangeService.triggerClientChange(game.getPeriod(), game.getId());
                            updateVariables();
                        });
                    }
                });

    }

    private void updateVariables() {
        compositeDisposable.add(updateVariablesREST().subscribe());
    }
    public void addVariable(String variable) {
        variables.add(variable);
        updateVariables();
    }
    public Observable<Game> changeReadiness(boolean ready) {
        if (ownUser.getEmpire() == null) {
            return Observable.error(new RuntimeException("You need to choose an empire first"));
        }
        return gameMembersApiService.updateGameMembership(game.getId(), tokenStorage.getUserId(), new ChangeMemberDto(ready, null))
                .map(r -> {
                    if (r.isSuccessful()) {
                        assert r.body() != null;
                        ownUser.setReady(r.body().ready());
                        return game;
                    } else {
                        throw new RuntimeException("Failed to change readiness");
                    }
                });
    }

    public Observable<Game> setReadinessToFalse() {
        return gameMembersApiService.updateGameMembership(game.getId(), tokenStorage.getUserId(), new ChangeMemberDto(false, null))
                .map(r -> {
                    if (r.isSuccessful()) {
                        assert r.body() != null;
                        ownUser.setReady(r.body().ready());
                        return game;
                    } else {
                        throw new RuntimeException("Failed to change readiness");
                    }
                });
    }

    public boolean amIHost() {
        User owner = game.getOwner();
        /*if (owner == null) {
            // Handle the case where the game has no owner
            System.out.println("The game has no owner.");
            return false;
        }*/
        if (owner == null) return false;
        String ownerId = owner.get_id();
        String currentUserId = tokenStorage.getUserId();
        return ownerId.equals(currentUserId);
    }

    public Observable<Game> setUserAsSpectator(String gameId) {
        this.isSpectator = true;
        return loadGame(gameId);
    }

    public boolean getSpectator() {
        return this.isSpectator;
    }

    public boolean amISpectator() {
        return this.isSpectator || ownUser == null || ownUser.getEmpire() == null;
    }

    public void disableSpectator() {
        this.isSpectator = false;
    }

    public Observable<Map<String, VariableDto>> getVariables(){
        return variablesObservable;
    }
    public Observable<VariableDto> getVariable(String variable) {
        if(!variables.contains(variable)){
            addVariable(variable);
            return getVariables()
                    .filter(variables -> variables.containsKey(variable))
                    .map(variables -> variables.get(variable));
        }
        return getVariables()
                .filter(variables -> variables.containsKey(variable))
                .map(variables -> variables.get(variable));
    }
    public Observable<Map<String, VariableDto>> getVariables(Collection<String> variables){
        boolean needsUpdate = false;
        for (String variable : variables) {
            if(!this.variables.contains(variable)){
                this.variables.add(variable);
                needsUpdate = true;
            }
        }
        if(needsUpdate) updateVariables();
        return getVariables().map(variablesMap -> variablesMap.entrySet().stream().filter(entry ->
                variables.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }
    private Observable<Map<String, VariableDto>> updateVariablesREST() {
        if (game.getId().isEmpty() || ownUser == null || ownUser.getEmpire() == null) return Observable.just(new HashMap<>());
        return variablesApiService.getVariables(
                game.getId(),
                getOwnUser().getEmpire().get_id(),
                variables
        ).map(list -> {
            Map<String, VariableDto> variables = list.stream().collect(
                    Collectors.toMap(VariableDto::variable, v -> v)
            );
            variablesObservable.onNext(variables);
            return variables;
        });
    }
}
