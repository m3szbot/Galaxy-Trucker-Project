package it.polimi.ingsw.Connection.ServerSide.messengers;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.socket.SocketDataExchanger;
import it.polimi.ingsw.Controller.FlightPhase.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;

/**
 * A playerMessenger which does nothing. Its only purpose is to prevent
 * the server from crashing in case the server gets the playerMessenger of
 * a disconnected player.
 *
 * @author carlo
 */

public class DummyPlayerMessenger extends PlayerMessenger{


    public DummyPlayerMessenger(Player player, SocketDataExchanger socketDataExchanger, int gameCode) {
        super(player, socketDataExchanger, gameCode);
    }
    @Override
    public void setGamePhase(GamePhase gamePhase) {
    }

    private void sendDataContainer() {

    }

    @Override
    public void endGame() {
    }

    public void clearPlayerResources() {
    }
    public void unblockUserInputGetterCall() {
    }

    public String getPlayerString() throws PlayerDisconnectedException {
        return getPlayerInput().trim();
    }
    private String getPlayerInput() throws PlayerDisconnectedException {
        return "dummy";
    }

    public int getPlayerInt() throws PlayerDisconnectedException {
        return -1;
    }

    @Override
    public void printMessage(String message) {
    }

    @Override
    public void printComponent(Component component) {

    }

    @Override
    public synchronized void printShipboard(ShipBoard shipBoard) {

    }

    @Override
    public void printCard(Card card) {
    }

    @Override
    public void printFlightBoard(FlightBoard flightBoard) {
    }

    public int[] getPlayerCoordinates() throws PlayerDisconnectedException {
        return new int[]{0, 0};
    }

    public boolean getPlayerBoolean() throws PlayerDisconnectedException {
        return false;
    }
}
