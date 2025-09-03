package de.uniks.stp24.service;

import de.uniks.stp24.dto.GameEmpireDto;
import de.uniks.stp24.dto.TechnologyDto;
import de.uniks.stp24.model.TechnologieNode;
import de.uniks.stp24.model.game.Empire;
import de.uniks.stp24.model.game.Game;
import de.uniks.stp24.model.game.User;
import de.uniks.stp24.rest.EmpireApiService;
import de.uniks.stp24.rest.PresetsApiService;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import retrofit2.Response;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TechnologiesServicesTest {

    @Mock
    private GameService gameService;

    @Mock
    private PresetsApiService presetsApiService;

    @Mock
    private EmpireApiService empireApiService;

    @InjectMocks
    private TechnologiesServices technologiesServices;

    @BeforeEach
    public void setUp() {
        // Initialize mocks and test class
        technologiesServices = new TechnologiesServices();
        technologiesServices.gameService = gameService;
        technologiesServices.presetsApiService = presetsApiService;
        technologiesServices.empireApiService = empireApiService;

        // Mock GameService
        Game mockGame = new Game();
        mockGame.setId("game1");
        User mockUser = new User();
        Empire mockEmpire = new Empire();
        mockEmpire.set_id("empire1");
        mockUser.setEmpire(mockEmpire);
        lenient().when(gameService.getGame()).thenReturn(mockGame);
        lenient().when(gameService.getOwnUser()).thenReturn(mockUser);
    }

    @Test
    public void testLoadUnlockedTechnologies() {
        // Mock responses
        when(empireApiService.getById(anyString(), anyString())).thenReturn(Observable.just(mockEmpireResponse()));

        // Call the method and verify the result
        List<String> unlockedTechs = technologiesServices.loadUnlockedTechnologies().blockingFirst();
        assertNotNull(unlockedTechs);
        assertTrue(unlockedTechs.contains("tech1"));
    }

    @Test
    public void testLoadTechnologies() {
        // Mock responses
        when(presetsApiService.getTechnologies()).thenReturn(Observable.just(mockPresetsResponse()));

        // Call the method and verify the result
        Collection<TechnologyDto> technologies = technologiesServices.loadTechnologies().blockingFirst();
        assertNotNull(technologies);
        assertEquals(2, technologies.size());
    }

    @Test
    public void testLoadRoots() {
        // Populate cache
        technologiesServices.technologiesCache.put("tech1", new TechnologyDto("tech1", null, null, null, null, null));
        technologiesServices.technologiesCache.put("tech2", new TechnologyDto("tech2", null, null, null, Collections.singletonList("tech1"), null));

        // Call the method and verify the result
        List<TechnologieNode> roots = technologiesServices.loadRoots().blockingFirst();
        assertNotNull(roots);
        assertEquals(1, roots.size());
        assertEquals("tech1", roots.get(0).getTechnology().id());
    }

    @Test
    public void testLoadTree() {
        // Mock responses
        when(presetsApiService.getTechnologies()).thenReturn(Observable.just(mockPresetsResponse()));
        when(empireApiService.getById(anyString(), anyString())).thenReturn(Observable.just(mockEmpireResponse()));

        // Call the method and verify the result
        List<TechnologieNode> tree = technologiesServices.loadTree().blockingFirst();
        assertNotNull(tree);
        assertFalse(tree.isEmpty());
    }


    // Helper methods to mock responses
    private GameEmpireDto mockEmpireResponse() {
        return new GameEmpireDto(
                LocalDateTime.now(),
                LocalDateTime.now(),
                "empire1",
                "game1",
                "user1",
                "Empire Name",
                "Empire Description",
                "blue",
                1,
                1,
                "homeSystem1",
                new String[]{"trait1", "trait2"},
                new HashMap<>(),
                Arrays.asList("tech1", "tech2")
        );
    }

    private Response<List<TechnologyDto>> mockPresetsResponse() {
        List<TechnologyDto> techs = new ArrayList<>();
        techs.add(new TechnologyDto("tech1", null, null, null, null, null));
        techs.add(new TechnologyDto("tech2", null, null, null, Collections.singletonList("tech1"), null));
        return Response.success(techs);
    }
}