package de.uniks.stp24.service;

import de.uniks.stp24.dto.*;
import de.uniks.stp24.model.game.Empire;
import de.uniks.stp24.model.game.Game;
import de.uniks.stp24.rest.EmpireApiService;
import de.uniks.stp24.rest.GameMembersApiService;
import de.uniks.stp24.rest.GamesApiService;
import de.uniks.stp24.ws.Event;
import de.uniks.stp24.ws.EventListener;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.AsyncSubject;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import org.fulib.fx.controller.Subscriber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import retrofit2.Response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {
    @InjectMocks
    GameService gameService;
    @Mock
    GamesApiService gamesApiService;
    @Mock
    UserService userService;
    @Mock
    GameSystemsService gameSystemsService;
    @Mock
    EventListener eventListener;
    @Mock
    GameMembersApiService gameMembersApiService;
    @Mock
    EmpireApiService empireApiService;
    @Mock
    TokenStorage tokenStorage;

    @Spy
    Subscriber subscriber = new Subscriber();

    @Test
    void loadGameTest() {
        LocalDateTime time = LocalDateTime.of(1,1,1,1,1);
        CreateGameResponseDto dto = new CreateGameResponseDto(
                time,
                time,
                "12345678",
                "TestGame",
                "12345678",
                1,
                5,
                false,
                1,
                1,
                new GameSettingsDto(100)
        );
        when(tokenStorage.getUserId()).thenReturn("12345678");
        when(gamesApiService.getGameById("12345678")).thenReturn(
                Observable.just(
                        Response.success(dto)
                )
        );
        when(gameMembersApiService.getPlayer("12345678")).thenReturn(
                Observable.just(
                        Response.success(
                        List.of(new GameMembersDto(
                                "12345678",
                                "12345678",
                                false,
                                time,
                                time,
                                null))
                )
                )
        );
        when(userService.decodeUsers(Set.of("12345678"))).thenReturn(
                Observable.just(
                        new java.util.HashMap<>() {{
                            put("12345678", "TestUser");
                        }}
                )
        );
        when(empireApiService.getAll("12345678")).thenReturn(
                Observable.just(
                        Response.success(
                                List.of()
                        )
                )
        );
        when(eventListener.listen(anyString(), any())).thenReturn(
                Observable.just(new Event<>("", ""))
        );
        Game game = gameService.loadGame("12345678").blockingFirst();
        assertEquals("12345678", game.getId());
        assertEquals("TestGame", game.getName());
        assertEquals(1, game.getSpeed());
        assertEquals(5, game.getMaxPlayers());
        assertEquals(1, game.getPeriod());
        assertEquals(100, game.getSize());
        assertEquals(1, game.getPlayersCount());
        assertEquals(0, game.getFractions().size());
        assertEquals(1, game.getMembers().size());
        assertEquals("TestUser", game.getMembers().getFirst().getName());
        assertEquals(game.getOwner(), game.getMembers().getFirst());
        assertNull(game.getOwner().getEmpire());
    }
    @Test
    void testNewMemberChanges(){
        LocalDateTime time = LocalDateTime.of(1,1,1,1,1);
        BehaviorSubject<Event<GameMembersDto>> subject = BehaviorSubject.create();
        CreateGameResponseDto dto = new CreateGameResponseDto(
                time,
                time,
                "12345678",
                "TestGame",
                "12345678",
                1,
                5,
                false,
                1,
                1,
                new GameSettingsDto(100)
        );
        when(eventListener.listen(anyString(), any())).thenReturn(AsyncSubject.create());
        when(eventListener.listen("games.12345678.members.*.*" , GameMembersDto.class)).thenReturn(subject);
        when(tokenStorage.getUserId()).thenReturn("12345678");
        when(gamesApiService.getGameById("12345678")).thenReturn(
                Observable.just(
                        Response.success(dto)
                )
        );
        when(gameMembersApiService.getPlayer("12345678")).thenReturn(
                Observable.just(
                        Response.success(
                                List.of(new GameMembersDto(
                                        "12345678",
                                        "12345678",
                                        false,
                                        time,
                                        time,
                                        null))
                        )
                )
        );
        when(userService.decodeUsers(Set.of("12345678"))).thenReturn(
                Observable.just(
                        new java.util.HashMap<>() {{
                            put("12345678", "TestUser");
                        }}
                )
        );
        when(userService.getUserNameById("123456789")).thenReturn(
                Observable.just(
                            "TestUser2"
                )
        );
        when(empireApiService.getAll("12345678")).thenReturn(
                Observable.just(
                        Response.success(
                                List.of()
                        )
                )
        );
        Game game = gameService.loadGame("12345678").blockingFirst();
        subject.onNext(new Event<>("games.12345678.members.*.created", new GameMembersDto(
                "123456789",
                "123456789",
                false,
                time,
                time,
                null)));
        assertEquals(2, game.getMembers().size());
        assertEquals("TestUser2", game.getMembers().getLast().getName());
    }
    @Test
    void testMemberChanges(){
        LocalDateTime time = LocalDateTime.of(1,1,1,1,1);
        BehaviorSubject<Event<GameMembersDto>> subject = BehaviorSubject.create();
        CreateGameResponseDto dto = new CreateGameResponseDto(
                time,
                time,
                "12345678",
                "TestGame",
                "12345678",
                1,
                5,
                false,
                1,
                1,
                new GameSettingsDto(100)
        );
        when(eventListener.listen(anyString(), any())).thenReturn(AsyncSubject.create());
        when(eventListener.listen("games.12345678.members.*.*" , GameMembersDto.class)).thenReturn(subject);
        when(tokenStorage.getUserId()).thenReturn("12345678");
        when(gamesApiService.getGameById("12345678")).thenReturn(
                Observable.just(
                        Response.success(dto)
                )
        );
        when(gameMembersApiService.getPlayer("12345678")).thenReturn(
                Observable.just(
                        Response.success(
                                List.of(new GameMembersDto(
                                        "12345678",
                                        "12345678",
                                        false,
                                        time,
                                        time,
                                        null))
                        )
                )
        );
        when(userService.decodeUsers(Set.of("12345678"))).thenReturn(
                Observable.just(
                        new java.util.HashMap<>() {{
                            put("12345678", "TestUser");
                        }}
                )
        );
        when(empireApiService.getAll("12345678")).thenReturn(
                Observable.just(
                        Response.success(
                                List.of()
                        )
                )
        );
        Game game = gameService.loadGame("12345678").blockingFirst();
        subject.onNext(new Event<>("games.12345678.members.*.updated", new GameMembersDto(
                "12345678",
                "12345678",
                true,
                time,
                time,
                null)));
        assertTrue(game.getMembers().getFirst().isReady());
    }
    @Test
    void testMemberDeletedChanges(){
        LocalDateTime time = LocalDateTime.of(1,1,1,1,1);
        BehaviorSubject<Event<GameMembersDto>> subject = BehaviorSubject.create();
        CreateGameResponseDto dto = new CreateGameResponseDto(
                time,
                time,
                "12345678",
                "TestGame",
                "12345678",
                1,
                5,
                false,
                1,
                1,
                new GameSettingsDto(100)
        );
        when(eventListener.listen(anyString(), any())).thenReturn(AsyncSubject.create());
        when(eventListener.listen("games.12345678.members.*.*" , GameMembersDto.class)).thenReturn(subject);
        when(tokenStorage.getUserId()).thenReturn("12345678");
        when(gamesApiService.getGameById("12345678")).thenReturn(
                Observable.just(
                        Response.success(dto)
                )
        );
        when(gameMembersApiService.getPlayer("12345678")).thenReturn(
                Observable.just(
                        Response.success(
                                List.of(new GameMembersDto(
                                        "12345678",
                                        "12345678",
                                        false,
                                        time,
                                        time,
                                        null),
                                        new GameMembersDto(
                                                "12345678",
                                                "123456789",
                                                false,
                                                time,
                                                time,
                                                null)
                                        )
                        )
                )
        );
        when(userService.decodeUsers(Set.of("12345678", "123456789"))).thenReturn(
                Observable.just(
                        new java.util.HashMap<>() {{
                            put("12345678", "TestUser");
                            put("123456789", "TestUser2");
                        }}
                )
        );
        when(empireApiService.getAll("12345678")).thenReturn(
                Observable.just(
                        Response.success(
                                List.of()
                        )
                )
        );
        Game game = gameService.loadGame("12345678").blockingFirst();
        subject.onNext(new Event<>("games.12345678.members.*.deleted", new GameMembersDto(
                "123456789",
                "123456789",
                false,
                time,
                time,
                null)));
        assertEquals(1, game.getMembers().size());
    }
    @Test
    void testGameChanged(){
        LocalDateTime time = LocalDateTime.of(1,1,1,1,1);
        BehaviorSubject<Event<CreateGameResponseDto>> subject = BehaviorSubject.create();
        CreateGameResponseDto dto = new CreateGameResponseDto(
                time,
                time,
                "12345678",
                "TestGame",
                "12345678",
                1,
                5,
                false,
                1,
                1,
                new GameSettingsDto(100)
        );
        when(eventListener.listen(anyString(), any())).thenReturn(AsyncSubject.create());
        when(eventListener.listen("games.12345678.*" , CreateGameResponseDto.class)).thenReturn(subject);
        when(tokenStorage.getUserId()).thenReturn("12345678");
        when(gamesApiService.getGameById("12345678")).thenReturn(
                Observable.just(
                        Response.success(dto)
                )
        );
        when(gameMembersApiService.getPlayer("12345678")).thenReturn(
                Observable.just(
                        Response.success(
                                List.of(new GameMembersDto(
                                                "12345678",
                                                "12345678",
                                                false,
                                                time,
                                                time,
                                                null),
                                        new GameMembersDto(
                                                "12345678",
                                                "123456789",
                                                false,
                                                time,
                                                time,
                                                null)
                                )
                        )
                )
        );
        when(userService.decodeUsers(Set.of("12345678", "123456789"))).thenReturn(
                Observable.just(
                        new java.util.HashMap<>() {{
                            put("12345678", "TestUser");
                            put("123456789", "TestUser2");
                        }}
                )
        );
        when(empireApiService.getAll("12345678")).thenReturn(
                Observable.just(
                        Response.success(
                                List.of()
                        )
                )
        );
        Game game = gameService.loadGame("12345678").blockingFirst();
        subject.onNext(new Event<>("games.12345678.updated", new CreateGameResponseDto(
                time,
                time,
                "12345678",
                "TestGame",
                "12345678",
                1,
                5,
                true,
                1,
                1,
                new GameSettingsDto(100)
        )));
        assertTrue(game.isStarted());
    }
    @Test
    void testEmpireAdded(){
        LocalDateTime time = LocalDateTime.of(1,1,1,1,1);
        BehaviorSubject<Event<GameEmpireDto>> subject = BehaviorSubject.create();
        CreateGameResponseDto dto = new CreateGameResponseDto(
                time,
                time,
                "12345678",
                "TestGame",
                "12345678",
                1,
                5,
                false,
                1,
                1,
                new GameSettingsDto(100)
        );
        when(eventListener.listen(anyString(), any())).thenReturn(AsyncSubject.create());
        when(eventListener.listen("games.12345678.empires.*.*" , GameEmpireDto.class)).thenReturn(subject);
        when(tokenStorage.getUserId()).thenReturn("12345678");
        when(gamesApiService.getGameById("12345678")).thenReturn(
                Observable.just(
                        Response.success(dto)
                )
        );
        when(gameMembersApiService.getPlayer("12345678")).thenReturn(
                Observable.just(
                        Response.success(
                                List.of(new GameMembersDto(
                                                "12345678",
                                                "12345678",
                                                false,
                                                time,
                                                time,
                                                null),
                                        new GameMembersDto(
                                                "12345678",
                                                "123456789",
                                                false,
                                                time,
                                                time,
                                                null)
                                )
                        )
                )
        );
        when(userService.decodeUsers(Set.of("12345678", "123456789"))).thenReturn(
                Observable.just(
                        new java.util.HashMap<>() {{
                            put("12345678", "TestUser");
                            put("123456789", "TestUser2");
                        }}
                )
        );
        when(empireApiService.getAll("12345678")).thenReturn(
                Observable.just(
                        Response.success(
                                List.of()
                        )
                )
        );
        Game game = gameService.loadGame("12345678").blockingFirst();
        subject.onNext(new Event<>("games.12345678.empires.*.created", new GameEmpireDto(
                time,
                time,
                "123456789",
                "12345678",
                "12345678",
                "TestEmpire",
                "TestEmpire",
                "FFFFFF",
                1,
                1,
                "",
                null,
                null,
                null

        )));
        assertNotNull(game.getMembers().getFirst().getEmpire());
        Empire empire = game.getMembers().getFirst().getEmpire();
        assertEquals("123456789", empire.get_id());
        assertEquals("TestEmpire", empire.getName());
        assertEquals("FFFFFF", empire.getColor());
        assertEquals(1, empire.getFlag());
        assertEquals(1, empire.getPortrait());
    }
    @Test
    void testEmpireChanged(){
        LocalDateTime time = LocalDateTime.of(1,1,1,1,1);
        BehaviorSubject<Event<GameEmpireDto>> subject = BehaviorSubject.create();
        CreateGameResponseDto dto = new CreateGameResponseDto(
                time,
                time,
                "12345678",
                "TestGame",
                "12345678",
                1,
                5,
                false,
                1,
                1,
                new GameSettingsDto(100)
        );
        when(eventListener.listen(anyString(), any())).thenReturn(AsyncSubject.create());
        when(eventListener.listen("games.12345678.empires.*.*" , GameEmpireDto.class)).thenReturn(subject);
        when(tokenStorage.getUserId()).thenReturn("12345678");
        when(gamesApiService.getGameById("12345678")).thenReturn(
                Observable.just(
                        Response.success(dto)
                )
        );
        when(gameMembersApiService.getPlayer("12345678")).thenReturn(
                Observable.just(
                        Response.success(
                                List.of(new GameMembersDto(
                                                "12345678",
                                                "12345678",
                                                false,
                                                time,
                                                time,
                                                null),
                                        new GameMembersDto(
                                                "12345678",
                                                "123456789",
                                                false,
                                                time,
                                                time,
                                                null)
                                )
                        )
                )
        );
        when(userService.decodeUsers(Set.of("12345678", "123456789"))).thenReturn(
                Observable.just(
                        new java.util.HashMap<>() {{
                            put("12345678", "TestUser");
                            put("123456789", "TestUser2");
                        }}
                )
        );
        when(empireApiService.getAll("12345678")).thenReturn(
                Observable.just(
                        Response.success(
                                List.of(
                                        new GameEmpireDto(
                                                time,
                                                time,
                                                "123456789",
                                                "12345678",
                                                "12345678",
                                                "TestEmpire",
                                                "TestEmpire",
                                                "000000",
                                                1,
                                                1,
                                                "",
                                                null,
                                                null,
                                                null

                                        )
                                )
                        )
                )
        );
        Game game = gameService.loadGame("12345678").blockingFirst();
        subject.onNext(new Event<>("games.12345678.empires.*.updated", new GameEmpireDto(
                time,
                time,
                "123456789",
                "12345678",
                "12345678",
                "TestEmpire",
                "TestEmpire",
                "FFFFFF",
                1,
                1,
                "",
                null,
                null,
                null

        )));
        assertNotNull(game.getMembers().getFirst().getEmpire());
        Empire empire = game.getMembers().getFirst().getEmpire();
        assertEquals("123456789", empire.get_id());
        assertEquals("TestEmpire", empire.getName());
        assertEquals("FFFFFF", empire.getColor());
        assertEquals(1, empire.getFlag());
        assertEquals(1, empire.getPortrait());
    }
    @Test
    void testEmpireDeleted(){
        LocalDateTime time = LocalDateTime.of(1,1,1,1,1);
        BehaviorSubject<Event<GameEmpireDto>> subject = BehaviorSubject.create();
        CreateGameResponseDto dto = new CreateGameResponseDto(
                time,
                time,
                "12345678",
                "TestGame",
                "12345678",
                1,
                5,
                false,
                1,
                1,
                new GameSettingsDto(100)
        );
        when(eventListener.listen(anyString(), any())).thenReturn(AsyncSubject.create());
        when(eventListener.listen("games.12345678.empires.*.*" , GameEmpireDto.class)).thenReturn(subject);
        when(tokenStorage.getUserId()).thenReturn("12345678");
        when(gamesApiService.getGameById("12345678")).thenReturn(
                Observable.just(
                        Response.success(dto)
                )
        );
        when(gameMembersApiService.getPlayer("12345678")).thenReturn(
                Observable.just(
                        Response.success(
                                List.of(new GameMembersDto(
                                                "12345678",
                                                "12345678",
                                                false,
                                                time,
                                                time,
                                                null),
                                        new GameMembersDto(
                                                "12345678",
                                                "123456789",
                                                false,
                                                time,
                                                time,
                                                null)
                                )
                        )
                )
        );
        when(userService.decodeUsers(Set.of("12345678", "123456789"))).thenReturn(
                Observable.just(
                        new java.util.HashMap<>() {{
                            put("12345678", "TestUser");
                            put("123456789", "TestUser2");
                        }}
                )
        );
        when(empireApiService.getAll("12345678")).thenReturn(
                Observable.just(
                        Response.success(
                                List.of(
                                        new GameEmpireDto(
                                                time,
                                                time,
                                                "123456789",
                                                "12345678",
                                                "12345678",
                                                "TestEmpire",
                                                "TestEmpire",
                                                "FFFFFF",
                                                1,
                                                1,
                                                "",
                                                null,
                                                null,
                                                null

                                        )
                                )
                        )
                )
        );
        Game game = gameService.loadGame("12345678").blockingFirst();
        subject.onNext(new Event<>("games.12345678.empires.*.deleted", new GameEmpireDto(
                time,
                time,
                "123456789",
                "12345678",
                "12345678",
                "TestEmpire",
                "TestEmpire",
                "FFFFFF",
                1,
                1,
                "",
                null,
                null,
                null

        )));
        assertNull(game.getMembers().getFirst().getEmpire());
    }
    @Test
    void testFractionUpdated(){
        LocalDateTime time = LocalDateTime.of(1,1,1,1,1);
        BehaviorSubject<Event<FractionDto>> subject = BehaviorSubject.create();
        CreateGameResponseDto dto = new CreateGameResponseDto(
                time,
                time,
                "12345678",
                "TestGame",
                "12345678",
                1,
                5,
                false,
                1,
                1,
                new GameSettingsDto(100)
        );
        when(eventListener.listen(anyString(), any())).thenReturn(AsyncSubject.create());
        when(eventListener.listen("games.12345678.systems.*.*" , FractionDto.class)).thenReturn(subject);
        when(tokenStorage.getUserId()).thenReturn("12345678");
        when(gamesApiService.getGameById("12345678")).thenReturn(
                Observable.just(
                        Response.success(dto)
                )
        );
        when(gameMembersApiService.getPlayer("12345678")).thenReturn(
                Observable.just(
                        Response.success(
                                List.of(new GameMembersDto(
                                                "12345678",
                                                "12345678",
                                                false,
                                                time,
                                                time,
                                                null),
                                        new GameMembersDto(
                                                "12345678",
                                                "123456789",
                                                false,
                                                time,
                                                time,
                                                null)
                                )
                        )
                )
        );
        when(userService.decodeUsers(Set.of("12345678", "123456789"))).thenReturn(
                Observable.just(
                        new java.util.HashMap<>() {{
                            put("12345678", "TestUser");
                            put("123456789", "TestUser2");
                        }}
                )
        );
        when(empireApiService.getAll("12345678")).thenReturn(
                Observable.just(
                        Response.success(
                                List.of()
                        )
                )
        );
        Game game = gameService.loadGame("12345678").blockingFirst();
        subject.onNext(new Event<>("games.12345678.systems.*.updated", new FractionDto(
                time,
                time,
                "123456789",
                "12345678",
                "12345678",
                null,
                null,
                3,
                null,
                "developed",
                1,
                Map.of(),
                1,
                1,
                "",
                "",
                "",
                100
        )));
        assertEquals(1, game.getFractions().size());
        assertEquals("123456789", game.getFractions().getFirst().get_id());
        assertEquals("developed", game.getFractions().getFirst().getUpgrade());
        assertEquals(3, game.getFractions().getFirst().getCapacity());
        assertEquals(1, game.getFractions().getFirst().getX());
        assertEquals(1, game.getFractions().getFirst().getY());
    }
}
