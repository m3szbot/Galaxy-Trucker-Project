package it.polimi.ingsw.Application;

/**
 * main class of the model
 *
 * @author Ludo
 */

import it.polimi.ingsw.components.*;
import it.polimi.ingsw.cards.*;
import it.polimi.ingsw.Bank.*;
import it.polimi.ingsw.shipboard.*;
import it.polimi.ingsw.FlightBoard.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.*;

public class GameInformation {
    private List<Card> cardsList;
    private List<Component> componentList;
    private List<Player> playerList;
    private int maxNumberOfPlayers;
    private Bank bank;
    private FlightBoard flightBoard;
    private GameType gameType;
    private ViewType viewType;

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

    public Bank getBank() {
        return bank;
    }

    public FlightBoard getFlightBoard() {
        return flightBoard;
    }

    public GameType getGameType() {
        return gameType;
    }

    public ViewType getViewType() {
        return viewType;
    }

    /**
     * creates the complete list of cards based on the type of game
     *
     * @param gameType
     */
    public void setUpCards(GameType gameType) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File("Cards.json"));
        CardBuilder cardBuilder = new CardBuilder();

        ElementType blowType, requirementType, lossType;
        int daysLost, gainedCredit, requirementNumber, cardLevel, lossNumber;
        int[] goods = new int[0], planet1 = new int[0], planet2 = new int[0], planet3 = new int[0], planet4 = new int[0];
        Blow blows[] = new Blow[0], blow;
        String cardName, elementType;
        JsonNode tempValues;

        if (gameType == gameType.TestGame) {

            for (JsonNode node : rootNode) {

                if (node.get("level").asInt() == 1) {


                    cardLevel = node.get("level").asInt();
                    cardName = node.get("cardName").asText();
                    daysLost = node.get("daysLost").asInt();
                    gainedCredit = node.get("gainedCredit").asInt();
                    requirementNumber = node.get("requirementNumber").asInt();
                    lossNumber = node.get("lossNumber").asInt();
                    elementType = node.get("blowType").asText();
                    blowType = ElementType.valueOf(elementType);
                    elementType = node.get("requirementType").asText();
                    requirementType = ElementType.valueOf(elementType);
                    elementType = node.get("lossType").asText();
                    lossType = ElementType.valueOf(elementType);

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

                        blows = new Blow[tempValues.size()];
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

        } else {
            for (JsonNode node : rootNode) {

                cardLevel = node.get("level").asInt();
                cardName = node.get("cardName").asText();
                daysLost = node.get("daysLost").asInt();
                gainedCredit = node.get("gainedCredit").asInt();
                requirementNumber = node.get("requirementNumber").asInt();
                lossNumber = node.get("lossNumber").asInt();
                elementType = node.get("blowType").asText();
                blowType = ElementType.valueOf(elementType);
                elementType = node.get("requirementType").asText();
                requirementType = ElementType.valueOf(elementType);
                elementType = node.get("lossType").asText();
                lossType = ElementType.valueOf(elementType);

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
    public void setUpComponents() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        componentList = mapper.readValue(new File("Components.json"),
                mapper.getTypeFactory().constructCollectionType(List.class, Component.class));

    }

    /**
     * creates playerList and adds new player
     *
     * @param player
     * @param maxNumberOfPlayers
     */
    public void setUpPlayers(Player player, int maxNumberOfPlayers) {
        if (playerList.isEmpty()) {

            this.maxNumberOfPlayers = maxNumberOfPlayers;
            playerList = new ArrayList<Player>();
            playerList.add(player);

        }
    }

    /**
     * adds a player to the playerList
     *
     * @param player
     */
    public void addPlayers(Player player) {

        if (playerList.contains(player) && playerList.size() < maxNumberOfPlayers) {
            playerList.add(player);

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
     * creates bank
     */
    public void setUpBank() {
        this.bank = new Bank(gameType);
    }

    /**
     * creates the flight board based on the game type
     */
    public void setUpFlightBoard() {
        this.flightBoard = new FlightBoard(gameType);
    }

    /**
     * asks the creator of the game which mode to play
     */
    public void setGameType() {
        this.gameType = askGameType();
    }

    /**
     * asks each player which view type they want to play with
     */
    public void setViewType() {
        this.viewType = askViewType();
    }
}