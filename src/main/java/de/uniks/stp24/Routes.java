package de.uniks.stp24;

import de.uniks.stp24.controller.*;
import org.fulib.fx.annotation.Route;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class Routes {
    @Route("login")
    @Inject
    Provider<LoginController> login;

    @Route("gameManager")
    @Inject
    Provider<GameManagerController> gameManager;

    @Route("gameManager/deletionScreen")
    @Inject
    Provider<DeletionScreenController> deletionScreen;

    @Route("signup")
    @Inject
    Provider<SignupController> signup;

    @Route("inGame")
    @Inject
    Provider<InGameController> inGame;

    @Route("gameOverview")
    @Inject
    Provider<GameOverviewController> gameOverview;

    @Route("chooseEmpire")
    @Inject
    Provider<ChooseEmpireController> chooseEmpire;

    @Route("lobby")
    @Inject
    Provider<LobbyController> lobby;

    @Route("editUser")
    @Inject
    Provider<EditUserController> editUser;

    @Route("editUser/accountDeletionConfirmation")
    @Inject
    Provider<AccountDeletionConfirmationController> accountDeletionConfirmation;

    @Route("createGame")
    @Inject
    Provider<CreateGameController> createGame;

    @Route("createEmpire")
    @Inject
    Provider<CreateEmpireController> createEmpire;

    @Route("licenses")
    @Inject
    Provider<LicensesController> licenses;

    @Route("achievements")
    @Inject
    Provider<AchievementsController> achievements;

    @Route("inGame/FractionDetails")
    @Inject
    Provider<FractionDetailsController> FractionDetails;

    @Route("joinTheGame")
    @Inject
    Provider<JoinTheGameController> joinTheGame;

    @Inject
    public Routes() {
    }
}