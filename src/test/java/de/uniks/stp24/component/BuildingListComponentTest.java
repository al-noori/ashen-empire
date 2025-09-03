package de.uniks.stp24.component;

import de.uniks.stp24.ControllerTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BuildingListComponentTest extends ControllerTest {
    /*
    EventListener eventListener = mock(EventListener.class);
    @Spy
    BuildingService buildingService = spy(BuildingService.class);
    @Spy
    Subscriber subscriber = spy(new Subscriber());
    @Spy
    PresetsApiService presetsApiService = spy(PresetsApiService.class);
    @Spy
    ImageCache imageCache = spy(ImageCache.class);

    Provider<BuildingComponent> buildingComponentProvider = new Provider<>() {
        @Override
        public BuildingComponent get() {
            final BuildingComponent bcomp = new BuildingComponent();
            bcomp.buildingService = buildingService;
            bcomp.buildingInformationComponent = buildingInformationComponent;
            bcomp.presetsApiService = presetsApiService;
            bcomp.subscriber = subscriber;
            bcomp.app = app;
            bcomp.imageCache = imageCache;
            return bcomp;
        }
    };

    Provider<BuildingResourceComponent> buildingResourceComponentProvider = new Provider<>() {
        @Override
        public BuildingResourceComponent get() {
            final BuildingResourceComponent bResComp = new BuildingResourceComponent();
            bResComp.imageCache = imageCache;
            return bResComp;
        }
    };
    @InjectMocks
    BuildingInformationComponent buildingInformationComponent;
    @InjectMocks
    BuildingListComponent buildingListComponent;
    final Subject<Event<Fraction>> subject = BehaviorSubject.create();

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);


        buildingListComponent.buildingComponentProvider = buildingComponentProvider;
        buildingInformationComponent.buildingResourceComponentProvider = buildingResourceComponentProvider;
        buildingInformationComponent.imageCache = imageCache;

        Map<String, Integer> cost = Map.of("minerals", 75);
        Map<String, Integer> production = Map.of("energy", 50);
        Map<String, Integer> upkeep = Map.of("energy", 50, "consumer_goods", 50);
        buildingListComponent.currentbuildings = FXCollections.observableArrayList(new Building("power_plant", cost, production, upkeep),
                new Building("foundry", cost, production, upkeep),
                new Building("factory", cost, production, upkeep),
                new Building("mine", cost, production, upkeep));
        Mockito.doReturn(subject).when(eventListener).listen("games.1.systems.1.*", Fraction.class);

        Mockito.doReturn(Observable.just(new Building("power_plant", cost, production, upkeep))).when(presetsApiService).getBuildingInfo("power_plant");
        Mockito.doReturn(Observable.just(new Building("foundry", cost, production, upkeep))).when(presetsApiService).getBuildingInfo("foundry");
        Mockito.doReturn(Observable.just(new Building("factory", cost, production, upkeep))).when(presetsApiService).getBuildingInfo("factory");
        Mockito.doReturn(Observable.just(new Building("mine", cost, production, upkeep))).when(presetsApiService).getBuildingInfo("mine");
        String[] testBuildings = new String[]{"power_plant", "factory", "foundry", "mine"};

        app.show(buildingListComponent, Map.of(
                "fraction",
                new Fraction(null, null, "1", "1", "Germany", null, 0, "test",
                        testBuildings, 0, 0, 0, "1", Map.of()),
                "game",
                new Game(null, null, "1", "1", "testGame", true, 1, 1, new GameSettingsDto(50), 1, "testUser")));

    }

    @Test
    void testBuildings() {
        System.out.println(this.buildingListComponent.buildingList.getItems().size());

        sleep(10000);

    }
     */

}
