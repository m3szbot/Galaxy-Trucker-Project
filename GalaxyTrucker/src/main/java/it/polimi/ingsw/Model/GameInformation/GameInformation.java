package it.polimi.ingsw.Model.GameInformation;

/**
 * main class of the model
 *
 * @author Ludo
 */

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.GameMessenger;
import it.polimi.ingsw.Controller.Cards.Blow;
import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Controller.Cards.CardBuilder;
import it.polimi.ingsw.Controller.Cards.ElementType;
import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameInformation {
    private GameType gameType;
    private List<Card> cardsList;
    private List<Component> componentList;
    private List<Player> connectedPlayerList;
    private List<Player> disconnectedPlayerList;
    private int maxNumberOfPlayers;
    private FlightBoard flightBoard;
    private int gameCode;
    private GamePhase gamePhase;

    /**
     * Creates and sets up gameInformation.
     */
    public GameInformation() {
        cardsList = new ArrayList<>();
        componentList = new ArrayList<>();
        connectedPlayerList = new ArrayList<>();
        disconnectedPlayerList = new ArrayList<>();
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    /**
     * Sets the given gamePhase and sends messages of it for both server and client.
     */
    public void setGamePhaseServerClient(GamePhase gamePhase) {
        GameMessenger gameMessenger = ClientMessenger.getGameMessenger(getGameCode());
        setGamePhase(gamePhase);
        System.out.printf("%s phase is starting...\n", gamePhase);
        gameMessenger.setGamePhaseToAll(gamePhase);
        gameMessenger.sendMessageToALl(String.format("%s phase is starting...\n", gamePhase));

    }

    public int getGameCode() {
        return this.gameCode;
    }

    public void setGameCode(int gameCode) {
        this.gameCode = gameCode;
    }

    /**
     * Sets gameType, maxNumberOfPlayers and initializes gameInformation models:
     * cards, components, flightBoard
     *
     * @author Boti
     */
    public void setUpGameInformation(GameType gameType, int maxNumberOfPlayers) {
        setGameType(gameType);
        setMaxNumberOfPlayers(maxNumberOfPlayers);
        try {
            setUpCards();
            setUpComponents();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setUpFlightBoard();
    }

    /**
     * creates the complete list of cards based on the type of game
     */
    private void setUpCards() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File("src/main/resources/Cards.json"));
        CardBuilder cardBuilder = new CardBuilder();

        ElementType blowType, requirementType, lossType;
        int daysLost, gainedCredit, requirementNumber, cardLevel, lossNumber;
        int[] goods = new int[0], planet1 = new int[0], planet2 = new int[0], planet3 = new int[0], planet4 = new int[0];
        Blow[] blows = new Blow[0];
        Blow blow;
        String cardName, elementType;
        JsonNode tempValues;

        for (JsonNode node : rootNode) {

            // only level 1 cards for TestGame
            if (gameType == GameType.NORMALGAME || (gameType == GameType.TESTGAME && node.get("level").asInt() == 1)) {

                // present for all
                cardLevel = node.get("level").asInt();
                cardName = node.get("cardName").asText();
                // optional (can be null)
                daysLost = getSafeInt(node, "daysLost");
                gainedCredit = getSafeInt(node, "gainedCredit");
                requirementNumber = getSafeInt(node, "requirementNumber");
                lossNumber = getSafeInt(node, "lossNumber");
                blowType = getSafeElementType(node, "blowType");
                requirementType = getSafeElementType(node, "requirementType");
                lossType = getSafeElementType(node, "lossType");

                tempValues = node.get("goods");

                if (tempValues != null && tempValues.isArray()) {

                    goods = new int[tempValues.size()];

                    for (int i = 0; i < tempValues.size(); i++) {

                        goods[i] = tempValues.get(i).asInt();

                    }

                }

                tempValues = node.get("planet1");

                if (tempValues != null && tempValues.isArray()) {

                    planet1 = new int[tempValues.size()];

                    for (int i = 0; i < tempValues.size(); i++) {

                        planet1[i] = tempValues.get(i).asInt();

                    }

                }


                tempValues = node.get("planet2");

                if (tempValues != null && tempValues.isArray()) {

                    planet2 = new int[tempValues.size()];

                    for (int i = 0; i < tempValues.size(); i++) {

                        planet2[i] = tempValues.get(i).asInt();

                    }

                }


                tempValues = node.get("planet3");

                if (tempValues != null && tempValues.isArray()) {

                    planet3 = new int[tempValues.size()];

                    for (int i = 0; i < tempValues.size(); i++) {

                        planet3[i] = tempValues.get(i).asInt();

                    }

                }


                tempValues = node.get("planet4");

                if (tempValues != null && tempValues.isArray()) {

                    planet4 = new int[tempValues.size()];

                    for (int i = 0; i < tempValues.size(); i++) {

                        planet4[i] = tempValues.get(i).asInt();

                    }

                }

                tempValues = node.get("blows");

                if (tempValues != null && tempValues.isArray()) {

                    int direction;
                    boolean big;
                    blows = new Blow[tempValues.size()];
                    int i = 0;

                    for (JsonNode obj : tempValues) {

                        direction = obj.get("direction").asInt();
                        big = obj.get("big").asBoolean();
                        blow = new Blow(direction, big);
                        blows[i] = blow;
                        i++;

                    }

                }

                cardsList.add(cardBuilder.buildCardLevel(cardLevel).buildCardName(cardName).buildBlowType(blowType).buildRequirementType(requirementType).buildLossType(lossType).buildDaysLost(daysLost).buildGainedCredit(gainedCredit).buildRequirementNumber(requirementNumber).buildLossNumber(lossNumber).buildGoods(goods).buildBlows(blows).buildPlanets(planet1, planet2, planet3, planet4).getBuiltCard());

            }

        }

    }

    /**
     * creates component objects
     */
    private void setUpComponents() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Serve ad ignorare le proprietà(campi) sconosciuti degli oggetti json con campi aggiuntivi
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Registrazione esplicita dei subtypes
        mapper.registerSubtypes(
                AlienSupport.class,
                Battery.class,
                Cabin.class,
                Cannon.class,
                Component.class,
                Engine.class,
                Shield.class,
                Storage.class
        );

        componentList = mapper.readValue(new File("src/main/resources/Components.json"),
                mapper.getTypeFactory().constructCollectionType(List.class, Component.class)
        );

    }

    /**
     * creates the flight board based on the game type
     */
    private void setUpFlightBoard() {
        this.flightBoard = new FlightBoard(gameType, cardsList);
    }

    /**
     * Returns the field value as int if the field exists, default value otherwise
     *
     * @param node  Json node
     * @param field field of Json node
     * @return field value or default value
     * @author Boti
     */
    private int getSafeInt(JsonNode node, String field) {
        // set defaultValue
        int defaultValue = 0;
        if (node.has(field)) {
            return node.get(field).asInt();
        } else {
            return defaultValue;
        }
    }

    /**
     * Return ElementType of the given type field or default if field not present
     *
     * @param node
     * @param field
     * @return
     * @author Boti
     */
    private ElementType getSafeElementType(JsonNode node, String field) {
        if (node.has(field)) {
            return ElementType.valueOf(node.get(field).asText());
        } else {
            return ElementType.Default;
        }
    }

    public List<Card> getCardsList() {
        return cardsList;
    }

    public List<Component> getComponentList() {
        return componentList;
    }

    /**
     * @return connectedPlayerList
     */
    public List<Player> getPlayerList() {
        return connectedPlayerList;
    }

    public List<Player> getDisconnectedPlayerList() {
        return this.disconnectedPlayerList;
    }

    public int getMaxNumberOfPlayers() {
        return maxNumberOfPlayers;
    }

    public void setMaxNumberOfPlayers(int maxNumberOfPlayers) {
        this.maxNumberOfPlayers = maxNumberOfPlayers;
    }

    public FlightBoard getFlightBoard() {
        return flightBoard;
    }

    public GameType getGameType() {
        return gameType;
    }

    /**
     * asks the creator of the game which mode to play
     */
    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }


    /**
     * adds a player to the connectedPlayerList
     *
     * @param player
     */
    public void addPlayers(Player player) {

        if (connectedPlayerList.size() < maxNumberOfPlayers) {
            connectedPlayerList.add(player);

        } else {
            System.out.println("Cannot join match, max number of players reached");
        }
    }

    /**
     * removes a player from the connectedPlayerList
     *
     * @param player
     */
    public void removePlayers(Player player) {
        connectedPlayerList.remove(player);
    }

    /**
     * Moves player from connectedPlayerList to disconnectedPlayerList. It
     * also clears the player resources (carlo).
     *
     * @author Boti
     */
    public void disconnectPlayer(Player player) {
        connectedPlayerList.remove(player);
        disconnectedPlayerList.add(player);
        ClientMessenger.getGameMessenger(gameCode).clearPlayerResources(player);
    }

    /**
     * Moves player from disconnectedPlayerList to connectedPlayerList.
     *
     * @author Boti
     */
    public void reconnectPlayer(Player player) {
        // TODO send current phase to reconnected player
        // TODO notify suspended player threads or launch new thread
        disconnectedPlayerList.remove(player);
        connectedPlayerList.add(player);
    }


}