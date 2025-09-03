package de.uniks.stp24.service;

import de.uniks.stp24.dto.AchievementDto;
import de.uniks.stp24.dto.AchievementUpdateDto;
import de.uniks.stp24.model.Achievement;
import de.uniks.stp24.rest.AchievementsApiService;
import de.uniks.stp24.util.response.ResponseEnum;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.AsyncSubject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static de.uniks.stp24.constants.AchievementsConstants.ACHIEVEMENTS;

@Singleton
public class AchievementsService {
    @Inject
    AchievementsApiService achievementsApiService;
    @Inject
    TokenStorage tokenStorage;

    @Inject
    public AchievementsService() {

    }

    public Observable<Boolean> addAllAchievementsToUserIfNotHappenedYet() {
        AsyncSubject<Boolean> subject = AsyncSubject.create();
        CompositeDisposable disposables = new CompositeDisposable();
        disposables.add(
                achievementsApiService.getAchievementsByUserId(tokenStorage.getUserId()).subscribe(
                        result -> {
                            if (result.isSuccessful()) {
                                final List<AchievementDto> achievementsList = result.body();
                                for (String achievementId : ACHIEVEMENTS.keySet()) {
                                    if (achievementsList == null || achievementsList.stream().noneMatch(achievementDto -> achievementDto.id().equals(achievementId))) {
                                        disposables.add(
                                                updateAchievement(achievementId, ACHIEVEMENTS.get(achievementId)).subscribe(
                                                        passed -> {
                                                        },
                                                        error -> {
                                                            subject.onNext(false);
                                                            subject.onComplete();
                                                        }
                                                )
                                        );
                                    }
                                }
                            }
                            if (!subject.hasComplete()) {
                                subject.onNext(true);
                                subject.onComplete();
                            }
                        }, error -> {
                            subject.onError(error);
                            subject.onComplete();
                        }
                )
        );
        return subject.doOnDispose(disposables::dispose);
    }

    public Observable<List<Achievement>> getAchievementsFromUser() {
        AsyncSubject<List<Achievement>> subject = AsyncSubject.create();
        CompositeDisposable disposables = new CompositeDisposable();
        disposables.add(
                addAllAchievementsToUserIfNotHappenedYet().subscribe(
                        passed -> disposables.add(
                                achievementsApiService.getAchievementsByUserId(tokenStorage.getUserId()).subscribe(
                                        result -> {
                                            final List<AchievementDto> achievementsList = result.body();
                                            final List<Achievement> achievements = new ArrayList<>();
                                            if (achievementsList != null) {
                                                for (AchievementDto achievementDto : achievementsList) {
                                                    final Achievement achievement = ACHIEVEMENTS.get(achievementDto.id());
                                                    if (achievement != null) {
                                                        achievements.add(new Achievement(
                                                                achievement.getName(),
                                                                achievement.getDescription(),
                                                                achievement.getMinProgress(),
                                                                achievement.getMaxProgress(),
                                                                achievementDto.progress(),
                                                                achievementDto.unlockedAt()));
                                                    }
                                                }
                                            }
                                            subject.onNext(achievements);
                                            subject.onComplete();
                                        }, error -> {
                                            subject.onError(error);
                                            subject.onComplete();
                                        }
                                )
                        ),
                        error -> {
                            subject.onError(error);
                            subject.onComplete();
                        }
                )
        );
        return subject.doOnDispose(disposables::dispose);
    }

    public Observable<ResponseEnum> updateAchievement(String id, Achievement achievement) {
        AsyncSubject<ResponseEnum> subject = AsyncSubject.create();
        Disposable subscribed = achievementsApiService.updateAchievementByUserId(tokenStorage.getUserId(), id, new AchievementUpdateDto(
                (achievement.getProgress() >= achievement.getMaxProgress()) ? LocalDateTime.now().toString() : null
                , achievement.getProgress())).subscribe(
                result -> {
                    if (result.isSuccessful()) {
                        subject.onNext(ResponseEnum.SUCCESS);
                    } else {
                        subject.onNext(ResponseEnum.fromInt(result.code()));
                    }
                    subject.onComplete();
                }, error -> {
                    subject.onError(error);
                    subject.onComplete();
                }
        );
        return subject.doOnDispose(subscribed::dispose);
    }
    public Observable<Boolean> isAchievementUnlocked(String id) {
        AsyncSubject<Boolean> subject = AsyncSubject.create();
        CompositeDisposable disposables = new CompositeDisposable();
        disposables.add(
                achievementsApiService.getAchievementByUserId(tokenStorage.getUserId(), id).subscribe(
                        result -> {
                            if (result.isSuccessful()) {
                                assert result.body() != null;
                                if(!result.body().isEmpty()) {
                                    AchievementDto achievementDto = result.body().getFirst();
                                    if(achievementDto.unlockedAt() != null) {
                                        subject.onNext(true);
                                    } else {
                                        subject.onNext(false);
                                    }
                                }

                            } else {
                                subject.onNext(false);
                            }
                            subject.onComplete();
                        }, error -> {
                            subject.onError(error);
                            subject.onComplete();
                        }
                )
        );
        return subject.doOnDispose(disposables::dispose);
    }

    private Observable<ResponseEnum> unlockAchievement(String id) {
        AsyncSubject<Boolean> subject = AsyncSubject.create();
        CompositeDisposable disposables = new CompositeDisposable();
        disposables.add(achievementsApiService.updateAchievementByUserId(tokenStorage.getUserId(), id, new AchievementUpdateDto(LocalDateTime.now().toString(), 0)).subscribe(
                result -> {
                    if (result.isSuccessful()) {
                        subject.onNext(true);
                    } else {
                        subject.onNext(false);
                    }
                    subject.onComplete();
                }, error -> {
                    subject.onError(error);
                    subject.onComplete();
                }
        ));
        return subject.map(passed -> passed ? ResponseEnum.SUCCESS : ResponseEnum.ERROR).doOnDispose(disposables::dispose);
    }
    private Observable<Boolean> isFirstStepAchievementUnlocked() {
        return isAchievementUnlocked("507f191e810c19729de860ec");
    }
    private void unlockFirstStepAchievement() {
        unlockAchievement("507f191e810c19729de860ec").subscribe();
    }
    private Observable<Boolean> isBeginnersLuckUnlocked() {
        return isAchievementUnlocked("507f191e810c19729de860ed");
    }
    private void unlockBeginnersLuck() {
        unlockAchievement("507f191e810c19729de860ed").subscribe();
    }
    private Observable<Boolean> isDoubleWinUnlocked() {
        return isAchievementUnlocked("507f191e810c19729de860ee");
    }
    private void unlockDoubleWin() {
        unlockAchievement("507f191e810c19729de860ee").subscribe();
    }
    private Observable<Boolean> isTripleThreatUnlocked() {
        return isAchievementUnlocked("507f191e810c19729de860ef");
    }
    private void unlockTripleThreat() {
        unlockAchievement("507f191e810c19729de860ef").subscribe();
    }
    private Observable<Boolean> isQuadrupleVictoryUnlocked() {
        return isAchievementUnlocked("507f191e810c19729de860f0");
    }
    private void unlockQuadrupleVictory() {
        unlockAchievement("507f191e810c19729de860f0").subscribe();
    }
    private Observable<Boolean> isChampionsStreakUnlocked() {
        return isAchievementUnlocked("507f191e810c19729de860f1");
    }
    private void unlockChampionsStreak() {
        unlockAchievement("507f191e810c19729de860f1").subscribe();
    }
    private Observable<Boolean> isMasterOfCeremoniesUnlocked() {
        return isAchievementUnlocked("507f191e810c19729de860f2");
    }
    private void unlockMasterOfCeremonies() {
        unlockAchievement("507f191e810c19729de860f2").subscribe();
    }
    private void unlock(String id) {
        CompositeDisposable disposables = new CompositeDisposable();
        disposables.add(
        isAchievementUnlocked(id).subscribe( isUnlocked -> {
            if (!isUnlocked) {
                unlockAchievement(id).subscribe();
            }
        }));
    }
    public void unlockTechTitan() {
        unlock("507f191e810c19729de860f3");
    }
    public void unlockEnergyHoarder() {
        unlock("507f191e810c19729de860f4");
    }
    public void unlockTreasureTrove() {
        unlock("507f191e810c19729de860f5");
    }
    public void checkAndUnlockAchievements(boolean amIHost) {
        CompositeDisposable disposables = new CompositeDisposable();
        disposables.add(isBeginnersLuckUnlocked().subscribe(unlock->{
            if(!unlock){
                unlockBeginnersLuck();
            } else  {
                disposables.add(isDoubleWinUnlocked().subscribe(unlock2->{
                    if(!unlock2){
                        unlockDoubleWin();
                    } else {
                        disposables.add(isTripleThreatUnlocked().subscribe(unlock3->{
                            if(!unlock3){
                                unlockTripleThreat();
                            } else {
                                disposables.add(isQuadrupleVictoryUnlocked().subscribe(unlock4->{
                                    if(!unlock4){
                                        unlockQuadrupleVictory();
                                    } else {
                                        disposables.add(isChampionsStreakUnlocked().subscribe(unlock5->{
                                            if(!unlock5){
                                                unlockChampionsStreak();
                                            }
                                        }));
                                    }
                                }));
                            }
                        }));
                    }
                }));
            }
        }));
        if(amIHost){
            disposables.add(
            isMasterOfCeremoniesUnlocked().subscribe(unlock6->{
                if(!unlock6){
                    unlockMasterOfCeremonies();
                }
            }));
        }
        disposables.add(isFirstStepAchievementUnlocked().subscribe(unlock->{
            if(!unlock){
                unlockFirstStepAchievement();
            }
        }));
    }


}
