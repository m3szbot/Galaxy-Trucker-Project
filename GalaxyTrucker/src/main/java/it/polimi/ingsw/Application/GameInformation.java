package it.polimi.ingsw.Application;

/**
 * main class of the model
 *
 * @author Ludo
 */

import it.polimi.ingsw.Components.*;
import it.polimi.ingsw.Cards.*;
import it.polimi.ingsw.Shipboard.*;
import it.polimi.ingsw.FlightBoard.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.*;

public class GameInformation {
    private List<Card> cardsList;
    private List<Component> componentList;
    private List<Player> playerList = new ArrayList<>();
    private int maxNumberOfPlayers;
    private FlightBoard flightBoard;
    private GameType gameType;
    private HashMap<Player, ViewType> playerViewMap;
    private HashMap<Player, ConnectionType> playerConnectionMap;

    public GameInformation() {
        cardsList = new ArrayList<>();
        componentList = new ArrayList<>();
        playerList = new ArrayList<>();
        playerViewMap = new HashMap<>();
    }

    public List<Card> getCardsList() {
        return cardsList;
    }

    public List<Component> getComponentList() {
        return componentList;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public int getMaxNumberOfPlayers() {
        return maxNumberOfPlayers;
    }

    public FlightBoard getFlightBoard() {
        return flightBoard;
    }

    public GameType getGameType() {
        return gameType;
    }

    public ViewType getPlayerViewType(Player player) {
        return playerViewMap.get(player);
    }

    public ConnectionType getPlayerConnectionType(Player player) {return playerConnectionMap.get(player);}

    /**
     * creates the complete list of cards based on the type of game
     *
     * @param gameType
     */
    public void setUpCards(GameType gameType) throws IOException {

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
            if (gameType == GameType.NormalGame || (gameType == GameType.TestGame && node.get("level").asInt() == 1)) {

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

    /**
     * creates component objects
     */
    public void setUpComponents() throws IOException {
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


    public void setMaxNumberOfPlayers(int maxNumberOfPlayers) {
        this.maxNumberOfPlayers = maxNumberOfPlayers;
    }

    /**
     * adds a player to the playerList
     *
     * @param player
     */
    public void addPlayers(Player player) {

        if (!playerList.isEmpty() && playerList.size() < maxNumberOfPlayers) {
            playerList.add(player);

        } else {
            System.out.println("Cannot join match, max number of players reached");
        }
    }

    /**
     * removes a player from the playerList
     *
     * @param player
     */
    public void removePlayers(Player player) {
        playerList.remove(player);
    }

    /**
     * creates the flight board based on the game type
     */
    public void setUpFlightBoard() {
        this.flightBoard = new FlightBoard(gameType, cardsList);
    }

    /**
     * asks the creator of the game which mode to play
     */
    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    /**
     * asks each player which view type they want to play with
     */
    public void setPlayerViewType(Player player, ViewType viewType) {
        playerViewMap.put(player, viewType);
    }

    /**
     * set up the connection type for the specified player
     * @param player
     * @param connectionType
     *
     * @author carlo
     */

    public void setPlayerConnectionType(Player player, ConnectionType connectionType) {playerConnectionMap.put(player, connectionType);}
}