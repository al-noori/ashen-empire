package de.uniks.stp24.controller;

import de.uniks.stp24.ControllerTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FractionDetailsControllerTest extends ControllerTest {
    /*
    EventListener eventListener = mock(EventListener.class);

    @Spy
    BuildingService buildingService = spy(BuildingService.class);
    @Spy
    Provider<BuildingComponent> buildingsComponentProvider = spy(new Provider<>() {
        @Override
        public BuildingComponent get() {
            final BuildingComponent bcomp = new BuildingComponent();
            bcomp.buildingService = buildingService;
            bcomp.buildingInformationComponent = buildingInformationComponent;
            return bcomp;
        }
    });
    @InjectMocks
    BuildingComponent buildingComponent;
    @InjectMocks
    BuildingInformationComponent buildingInformationComponent;
    @Spy
    Subscriber subscriber = spy(new Subscriber());
    @Spy
    PresetsApiService presetsApiService = spy(PresetsApiService.class);
    @InjectMocks
    FractionDetailsController fractionDetailsController;
    final Subject<Event<Fraction>> subject = BehaviorSubject.create();


    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);

        fractionDetailsController.buildingComponent = buildingComponent;
        fractionDetailsController.buildingComponent.buildingInformationComponent = buildingInformationComponent;

        doReturn(subject).when(eventListener).listen("games." + "1" + ".systems." +  "1.*", Fraction.class);
        String[] testBuildings = new String[]{"power_plant", "factory", "foundry","mine"};
        app.show(fractionDetailsController, Map.of(
                "fraction",
                new Fraction(null,null,"1","1","Germany",0,null,
                        testBuildings,0,0,0,"1",Map.of(), "b"),
                "game",
                new Game(null, null, "1", "1", "testGame", true, 1, 1, new GameSettingsDto(50), 1, "testUser")));
    }

    @Test
    void testBuildings() {
        waitForFxEvents();

        System.out.println(fractionDetailsController.currentbuildings);
        System.out.println(fractionDetailsController.buildingsList.getItems());



        ListView<String> blist = lookup("#buildingsList").query();
        System.out.println(buildingService.buildingIDtoName.get("power_plant"));
        sleep(5000);

    }
    */
}
