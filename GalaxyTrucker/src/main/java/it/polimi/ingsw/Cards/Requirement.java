package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Shipboard.Player;

/**
 * Interface that defines a method which returns true or false whether
 * the player satisfies or not the specified requirement. Note that
 * the requirement can only be 2.
 *
 * @author carlo
 */

public interface Requirement {

    default boolean isSatisfying(Player player, ElementType requirementType, int quantity) {

        if (requirementType == ElementType.CrewMember) {

            if (player.getShipBoard().shipBoardAttributes.getCrewMembers() >= quantity) {
                return true;
            }

            return false;
        } else {

            if (player.getShipBoard().shipBoardAttributes.getFirePower() >= quantity) {
                return true;
            }

            return false;
        }

    }

}
