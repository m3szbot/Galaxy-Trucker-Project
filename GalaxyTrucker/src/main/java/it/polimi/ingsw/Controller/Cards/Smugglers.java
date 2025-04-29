package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.View.FlightView.FlightView;

/**
 * class that represent the card smugglers
 *
 * @author carlo
 */

public class Smugglers extends Card implements Movable, GoodsGain, TokenLoss, FirePowerChoice {

    private int daysLost;
    private ElementType lossType;
    private int requirementNumber;
    private int lossNumber;
    private int[] goods;

    public Smugglers(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.getCardLevel();
        this.cardName = cardBuilder.getCardName();
        this.daysLost = cardBuilder.getDaysLost();
        this.lossType = cardBuilder.getLossType();
        this.lossNumber = cardBuilder.getLossNumber();
        this.goods = cardBuilder.getGoods();
        this.requirementNumber = cardBuilder.getRequirementNumber();


    }

    @Override

    public void resolve(FlightBoard flightBoard, FlightView flightView) {
        int numberOfPlayers = flightBoard.getPlayerOrderList().size(), i;
        float chosenFirePower;
        String message;
        AttackStates[] results = new AttackStates[numberOfPlayers];

        for (i = 0; i < numberOfPlayers; i++) {

            chosenFirePower = chooseFirePower(flightBoard.getPlayerOrderList().get(i), flightView);

            if (chosenFirePower > requirementNumber) {

                message = "Player " + flightBoard.getPlayerOrderList().get(i).getNickName() + " has defeated the" +
                        "enemies!";
                flightView.sendMessageToAll(message);
                results[i] = AttackStates.EnemyDefeated;
                break;

            } else if (chosenFirePower == requirementNumber) {

                message = "Player " + flightBoard.getPlayerOrderList().get(i).getNickName() + " equalized the" +
                        "enemies!";
                results[i] = AttackStates.Equalized;
                flightView.sendMessageToAll(message);

            } else {

                message = "Player " + flightBoard.getPlayerOrderList().get(i).getNickName() + " has been" +
                        " defeated by the enemies!";
                results[i] = AttackStates.PlayerDefeated;
                flightView.sendMessageToAll(message);

            }

        }

        for (i = 0; i < numberOfPlayers; i++) {

            if (results[i] == AttackStates.EnemyDefeated) {

                message = "Would you like to collect the reward for defeating the enemies ?";

                if (flightView.askPlayerGenericQuestion(flightBoard.getPlayerOrderList().get(i), message)) {

                    message = "Player " + flightBoard.getPlayerOrderList().get(i).getNickName() +
                            "has collected the reward!";

                    giveGoods(flightBoard.getPlayerOrderList().get(i), goods, flightBoard, flightView);
                    changePlayerPosition(flightBoard.getPlayerOrderList().get(i), daysLost, flightBoard);
                    flightView.sendMessageToAll(message);

                } else {

                    message = "Player " + flightBoard.getPlayerOrderList().get(i).getNickName() +
                            "hasn't collected the reward!";

                    flightView.sendMessageToAll(message);
                }

                break;
            } else if (results[i] == AttackStates.PlayerDefeated) {

                inflictLoss(flightBoard.getPlayerOrderList().get(i), lossType, lossNumber, flightBoard, flightView);

            }

        }
    }
}
