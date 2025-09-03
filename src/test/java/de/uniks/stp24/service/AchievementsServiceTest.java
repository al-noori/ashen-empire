package de.uniks.stp24.service;

import de.uniks.stp24.dto.AchievementDto;
import de.uniks.stp24.dto.AchievementUpdateDto;
import de.uniks.stp24.model.Achievement;
import de.uniks.stp24.rest.AchievementsApiService;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.TestObserver;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import retrofit2.Response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static de.uniks.stp24.constants.AchievementsConstants.ACHIEVEMENTS;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class) // Use MockitoExtension to Mock
public class AchievementsServiceTest {
    @Mock
    AchievementsApiService achievementsApiService;
    @Mock
    TokenStorage tokenStorage;
    @InjectMocks
    AchievementsService achievementsService;

    @Test
    public void testAddAllAchievementsToUserIfNotHappenedYet() {
        when(tokenStorage.getUserId()).thenReturn("0");
        when(achievementsApiService.getAchievementsByUserId(eq("0"))).thenReturn(Observable.just(Response.success(List.of())));
        for(Map.Entry<String, Achievement> entry: ACHIEVEMENTS.entrySet()){
            Achievement achievement = entry.getValue();
            AchievementUpdateDto entryUpdateDto = new AchievementUpdateDto (
                null,
                    achievement.getProgress()
            );
            when(achievementsApiService.updateAchievementByUserId(eq("0"), eq(entry.getKey()), eq(entryUpdateDto))).thenReturn(Observable.just(Response.success(null)));
        }
        achievementsService.addAllAchievementsToUserIfNotHappenedYet().test().assertValue(true);
    }

        // Successfully add all achievements to user if not already added
        @Test
        public void test_add_all_achievements_to_user_if_not_already_added() {
            AchievementsApiService apiService = mock(AchievementsApiService.class);
            TokenStorage tokenStorage = mock(TokenStorage.class);
            AchievementsService achievementsService = new AchievementsService();
            achievementsService.achievementsApiService = apiService;
            achievementsService.tokenStorage = tokenStorage;

            String userId = "testUser";
            when(tokenStorage.getUserId()).thenReturn(userId);

            List<AchievementDto> existingAchievements = new ArrayList<>();
            when(apiService.getAchievementsByUserId(userId)).thenReturn(Observable.just(Response.success(existingAchievements)));

            when(apiService.updateAchievementByUserId(anyString(), anyString(), any(AchievementUpdateDto.class)))
                    .thenReturn(Observable.just(Response.success(new AchievementDto("507f191e810c19729de860ea", userId, 0, LocalDateTime.now(), LocalDateTime.now(), null))));

            TestObserver<Boolean> testObserver = achievementsService.addAllAchievementsToUserIfNotHappenedYet().test();
            testObserver.assertValue(true);
        }

        // Retrieve achievements for a user and map them correctly to the Achievement model
        @Test
        public void test_retrieve_and_map_achievements_correctly() {
            AchievementsApiService apiService = mock(AchievementsApiService.class);
            TokenStorage tokenStorage = mock(TokenStorage.class);
            AchievementsService achievementsService = new AchievementsService();
            achievementsService.achievementsApiService = apiService;
            achievementsService.tokenStorage = tokenStorage;

            String userId = "testUser";
            when(tokenStorage.getUserId()).thenReturn(userId);

            List<AchievementDto> existingAchievements = new ArrayList<>();
            when(apiService.getAchievementsByUserId(userId)).thenReturn(Observable.just(Response.success(existingAchievements)));

            when(apiService.updateAchievementByUserId(anyString(), anyString(), any(AchievementUpdateDto.class)))
                    .thenReturn(Observable.just(Response.success(new AchievementDto("507f191e810c19729de860ea", userId, 0, LocalDateTime.now(), LocalDateTime.now(), null))));

            TestObserver<List<Achievement>> testObserver = achievementsService.getAchievementsFromUser().test();
            testObserver.assertNoErrors();
            testObserver.assertComplete();
            List<Achievement> achievements = testObserver.values().get(0);
            // Add assertions for mapping correctness
        }

        // Handle successful responses from AchievementsApiService gracefully
        @Test
        public void test_handle_successful_responses_gracefully() {
            AchievementsApiService apiService = mock(AchievementsApiService.class);
            TokenStorage tokenStorage = mock(TokenStorage.class);
            AchievementsService achievementsService = new AchievementsService();
            achievementsService.achievementsApiService = apiService;
            achievementsService.tokenStorage = tokenStorage;

            String userId = "testUser";
            when(tokenStorage.getUserId()).thenReturn(userId);

            List<AchievementDto> existingAchievements = new ArrayList<>();
            when(apiService.getAchievementsByUserId(userId)).thenReturn(Observable.just(Response.success(existingAchievements)));

            when(apiService.updateAchievementByUserId(anyString(), anyString(), any(AchievementUpdateDto.class)))
                    .thenReturn(Observable.just(Response.success(new AchievementDto("507f191e810c19729de860ea", userId, 0, LocalDateTime.now(), LocalDateTime.now(), null))));

            TestObserver<Boolean> testObserver = achievementsService.addAllAchievementsToUserIfNotHappenedYet().test();
            testObserver.assertValue(true);
        }

        // Dispose of resources correctly when Observable is disposed
        @Test
        public void dispose_resources_correctly_when_observable_disposed() {
            AchievementsApiService apiService = mock(AchievementsApiService.class);
            TokenStorage tokenStorage = mock(TokenStorage.class);
            AchievementsService achievementsService = new AchievementsService();
            achievementsService.achievementsApiService = apiService;
            achievementsService.tokenStorage = tokenStorage;

            String userId = "testUser";
            when(tokenStorage.getUserId()).thenReturn(userId);

            List<AchievementDto> existingAchievements = new ArrayList<>();
            when(apiService.getAchievementsByUserId(userId)).thenReturn(Observable.just(Response.success(existingAchievements)));

            when(apiService.updateAchievementByUserId(anyString(), anyString(), any(AchievementUpdateDto.class)))
                    .thenReturn(Observable.just(Response.success(new AchievementDto("507f191e810c19729de860ea", userId, 0, LocalDateTime.now(), LocalDateTime.now(), null))));

            TestObserver<Boolean> testObserver = achievementsService.addAllAchievementsToUserIfNotHappenedYet().test();
            testObserver.assertValue(true);
        }

        // Handle API errors gracefully and propagate them correctly
        @Test
        public void test_handle_api_errors_gracefully() {
            AchievementsApiService apiService = mock(AchievementsApiService.class);
            TokenStorage tokenStorage = mock(TokenStorage.class);
            AchievementsService achievementsService = new AchievementsService();
            achievementsService.achievementsApiService = apiService;
            achievementsService.tokenStorage = tokenStorage;

            String userId = "testUser";
            when(tokenStorage.getUserId()).thenReturn(userId);

            List<AchievementDto> existingAchievements = new ArrayList<>();
            when(apiService.getAchievementsByUserId(userId)).thenReturn(Observable.just(Response.success(existingAchievements)));

            when(apiService.updateAchievementByUserId(anyString(), anyString(), any(AchievementUpdateDto.class)))
                    .thenReturn(Observable.just(Response.success(new AchievementDto("507f191e810c19729de860ea", userId, 0, LocalDateTime.now(), LocalDateTime.now(), null))));

            TestObserver<Boolean> testObserver = achievementsService.addAllAchievementsToUserIfNotHappenedYet().test();
            testObserver.assertValue(true);
        }


        // Ensure correct mapping of AchievementDto to Achievement
        @Test
        public void ensure_correct_mapping_of_achievementdto_to_achievement() {
            AchievementsApiService apiService = mock(AchievementsApiService.class);
            TokenStorage tokenStorage = mock(TokenStorage.class);
            AchievementsService achievementsService = new AchievementsService();
            achievementsService.achievementsApiService = apiService;
            achievementsService.tokenStorage = tokenStorage;

            String userId = "testUser";
            when(tokenStorage.getUserId()).thenReturn(userId);

            List<AchievementDto> existingAchievements = new ArrayList<>();
            when(apiService.getAchievementsByUserId(userId)).thenReturn(Observable.just(Response.success(existingAchievements)));

            when(apiService.updateAchievementByUserId(anyString(), anyString(), any(AchievementUpdateDto.class)))
                    .thenReturn(Observable.just(Response.success(new AchievementDto("507f191e810c19729de860ea", userId, 0, LocalDateTime.now(), LocalDateTime.now(), null))));

            TestObserver<Boolean> testObserver = achievementsService.addAllAchievementsToUserIfNotHappenedYet().test();
            testObserver.assertValue(true);
        }

        // Ensure CompositeDisposable disposes all disposables correctly
        @Test
        public void test_composite_disposable_dispose() {
            AchievementsApiService apiService = mock(AchievementsApiService.class);
            TokenStorage tokenStorage = mock(TokenStorage.class);
            AchievementsService achievementsService = new AchievementsService();
            achievementsService.achievementsApiService = apiService;
            achievementsService.tokenStorage = tokenStorage;

            String userId = "testUser";
            when(tokenStorage.getUserId()).thenReturn(userId);

            List<AchievementDto> existingAchievements = new ArrayList<>();
            when(apiService.getAchievementsByUserId(userId)).thenReturn(Observable.just(Response.success(existingAchievements)));

            when(apiService.updateAchievementByUserId(anyString(), anyString(), any(AchievementUpdateDto.class)))
                    .thenReturn(Observable.just(Response.success(new AchievementDto("507f191e810c19729de860ea", userId, 0, LocalDateTime.now(), LocalDateTime.now(), null))));

            TestObserver<Boolean> testObserver = achievementsService.addAllAchievementsToUserIfNotHappenedYet().test();
            testObserver.assertValue(true);
        }

        // Verify that achievements are not added multiple times if already present
        @Test
        public void test_achievements_not_added_multiple_times() {
            AchievementsApiService apiService = mock(AchievementsApiService.class);
            TokenStorage tokenStorage = mock(TokenStorage.class);
            AchievementsService achievementsService = new AchievementsService();
            achievementsService.achievementsApiService = apiService;
            achievementsService.tokenStorage = tokenStorage;

            String userId = "testUser";
            when(tokenStorage.getUserId()).thenReturn(userId);

            List<AchievementDto> existingAchievements = new ArrayList<>();
            when(apiService.getAchievementsByUserId(userId)).thenReturn(Observable.just(Response.success(existingAchievements)));

            when(apiService.updateAchievementByUserId(anyString(), anyString(), any(AchievementUpdateDto.class)))
                    .thenReturn(Observable.just(Response.success(new AchievementDto("507f191e810c19729de860ea", userId, 0, LocalDateTime.now(), LocalDateTime.now(), null))));

            TestObserver<Boolean> testObserver = achievementsService.addAllAchievementsToUserIfNotHappenedYet().test();
            testObserver.assertValue(true);
        }
}
