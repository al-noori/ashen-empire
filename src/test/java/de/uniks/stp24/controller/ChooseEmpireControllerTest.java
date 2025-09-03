package de.uniks.stp24.controller;

import de.uniks.stp24.ControllerTest;
import de.uniks.stp24.component.EmpireComponent;
import de.uniks.stp24.dto.EmpireDto;
import de.uniks.stp24.dto.GameDto;
import de.uniks.stp24.dto.GameEmpireDto;
import de.uniks.stp24.dto.GameSettingsDto;
import de.uniks.stp24.rest.GameEmpiresApiService;
import de.uniks.stp24.service.TokenStorage;
import de.uniks.stp24.ws.Event;
import de.uniks.stp24.ws.EventListener;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;
import javafx.stage.Stage;
import org.fulib.fx.controller.Subscriber;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Provider;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Use MockitoExtension to Mock
public class ChooseEmpireControllerTest extends ControllerTest {
    GameEmpiresApiService gameEmpiresApiService = mock(GameEmpiresApiService.class);
    EventListener eventListener = mock(EventListener.class);
    @Spy
    Subscriber subscriber = spy(new Subscriber());
    @InjectMocks
    ChooseEmpireController chooseEmpireController;
    @InjectMocks
    EmpireComponent empireComponent;
    Provider<EmpireComponent> empireComponentProvider = mock(Provider.class);
    @Spy
    TokenStorage tokenStorage = spy(TokenStorage.class);
    @Spy
    ResourceBundle resources = spy(ResourceBundle.getBundle("de/uniks/stp24/lang/main", Locale.ENGLISH));

    final Subject<Event<EmpireDto>> subject = BehaviorSubject.create();

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        doReturn(new EmpireComponent()).when(empireComponentProvider).get();
        chooseEmpireController.empireComponent = empireComponent;

        doReturn(Observable.just(List.of(new GameEmpireDto(LocalDateTime.of(1,1,1,1,1), LocalDateTime.of(1,1,1,1,1), "62fc9b33773277d12d28929b", "ashen empire",
                        "test", "Test", "description", "blue", 16, 16, "homesystem", null,
                    Map.of(),null),
                new GameEmpireDto(LocalDateTime.of(1,1,1,1,1), LocalDateTime.of(1,1,1,1,1), "62fc9b33773277d12d28929b", "ashen empire",
                        "test", "Test2", "description", "blue", 16, 16, "homesystem", null,
                        Map.of(),null))))
                .when(gameEmpiresApiService).getAll("62fc9b33773277d12d28929b");
        doReturn(subject).when(eventListener).listen("games." + "62fc9b33773277d12d28929b" + ".empires.*.*", GameEmpireDto.class);

        app.show(chooseEmpireController, Map.of("game", new GameDto(LocalDateTime.of(1, 1,1,1,1), LocalDateTime.of(1, 1,1,1,1), "62fc9b33773277d12d28929b", "Test",
                "ashen empire", false, 16, 16, new GameSettingsDto(16), 12 , "theoneandonly"), "ready", true));
    }
    // TODO: FIX
    /*
    @Test
    void saveEmpire(){
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("Choose Empire", stage.getTitle());

        //assertEquals(null,);
        assertNull( chooseEmpireController.empiresBox.getItems().size());
    }
     */
    @AfterEach
    public void stop() throws Exception {
        Mockito.reset(subscriber, gameEmpiresApiService, eventListener, empireComponentProvider, tokenStorage);
        subscriber = null;
        gameEmpiresApiService = null;
        eventListener = null;
        empireComponentProvider = null;
        tokenStorage = null;
        chooseEmpireController = null;
        empireComponent = null;
        super.stop();
    }
}
