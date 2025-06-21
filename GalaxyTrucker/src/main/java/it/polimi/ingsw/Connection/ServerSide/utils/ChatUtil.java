package it.polimi.ingsw.Connection.ServerSide.utils;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.List;

/**
 * Class which handles the chat
 *
 * @author carlo
 */

public class ChatUtil {
    private int gameCode;
    private PlayerMessenger playerMessenger;

    public ChatUtil(PlayerMessenger playerMessenger) {

        this.playerMessenger = playerMessenger;
        this.gameCode = playerMessenger.getGameCode();

    }

    public String startPrivateMessageHandler() throws PlayerDisconnectedException {

        Player targetPlayer = null;
        boolean playerPresentFlag = false;

        List<Player> connectedPlayers = ClientMessenger.getGameMessenger(gameCode).getConnectedPlayers();

        if (connectedPlayers.size() == 1) {
            playerMessenger.printMessage("Nobody is in game!");
            return "repeat";
        }

        playerMessenger.printMessage("To which player do you want to send a message ?");

        for (Player player : ClientMessenger.getGameMessenger(gameCode).getConnectedPlayers()) {

            if (!player.getNickName().equals(playerMessenger.getPlayer().getNickName())) {

                playerMessenger.printMessage(player.getColouredNickName());

            }

        }

        String input = playerMessenger.getPlayerString();

        if (input.equals("unblock")) {
            return "unblocked";
        }

        for (Player player : ClientMessenger.getGameMessenger(gameCode).getConnectedPlayers()) {

            if (input.equals(player.getNickName()) && !input.equals(playerMessenger.getPlayer().getNickName())) {
                targetPlayer = player;
                playerPresentFlag = true;
                break;
            }
        }

        if (playerPresentFlag) {


            playerMessenger.printMessage("Type your intergalactic message: ");

            String message = playerMessenger.getPlayerString();

            if (message.equals("unblock")) {
                return "unblocked";
            }
            StringBuilder stringBuilder = new StringBuilder("[Private intergalactic message from " + playerMessenger.getPlayer().getColouredNickName() + "]: ");
            stringBuilder.append(message);
            String parsedMessage = stringBuilder.toString();

            ClientMessenger.getGameMessenger(gameCode).getPlayerMessenger(targetPlayer).printMessage(parsedMessage);
            playerMessenger.printMessage("The intergalactic message was sent");
        } else {

            playerMessenger.printMessage("The player you entered is currently not in game!");

        }

        return "repeat";
    }

    public String startPublicMessageHandler() throws PlayerDisconnectedException {

        List<Player> connectedPlayers = ClientMessenger.getGameMessenger(gameCode).getConnectedPlayers();

        if (connectedPlayers.size() == 1) {
            playerMessenger.printMessage("Nobody is in game!");
            return "repeat";
        }

        playerMessenger.printMessage("Type your intergalactic message: ");

        String message = playerMessenger.getPlayerString();

        if (message.equals("unblock")) {
            return "unblocked";
        }
        StringBuilder stringBuilder = new StringBuilder("[Public intergalactic message from " + playerMessenger.getPlayer().getColouredNickName() + "]: ");
        stringBuilder.append(message);
        String parsedMessage = stringBuilder.toString();

        for (Player player : ClientMessenger.getGameMessenger(gameCode).getConnectedPlayers()) {

            if (player.getNickName().equals(playerMessenger.getPlayer().getNickName())) {
                continue;
            }

            ClientMessenger.getGameMessenger(gameCode).getPlayerMessenger(player).printMessage(parsedMessage);

        }

        playerMessenger.printMessage("The intergalactic message was sent");

        return "repeat";
    }


}
