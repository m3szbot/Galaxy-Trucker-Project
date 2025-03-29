package it.polimi.ingsw.Application;

import it.polimi.ingsw.shipboard.Player;

public class SetUpPhase {
    Player player;

    public void SetUpPhase(Player player) {
    }

    public void start(GameInformation gameInformation) {
        if (gameInformation.getPlayerList().isEmpty()) {
            int maxNumberOfPlayers = askMaxNumberOfPlayers();
            gameInformation.setUpPlayers(player, maxNumberOfPlayers);
            GameType gameType = askGameType();
        } else {
            gameInformation.addPlayers(player);
        }
        ViewType viewType = askViewType(setUpView);
        gameInformation.setGameType(gameType);
        gameInformation.setViewType(viewType);

    }

    public int askMaxNumberOfPlayers(SetUpView setUpView) {
        return setUpView.askMaxNumberOfPlayers();
    }

    public GameType askGameType(SetUpView setUpView) {
        return setUpView.askGameType();
    }

    public ViewType askViewType(SetUpView setUpView) {
        return setUpView.askViewType();
    }

}
