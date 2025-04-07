package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Shipboard.Player;

/**
 * Interface that defines a default method which returns true or false whether
 * the player satisfies or not the specified requirement. Note that
 * the requirement can only be 2.
 *
 * @author carlo
 */

public interface Requirement {

    /**
     *
     * @param player target player
     * @param requirementType requirement to verify
     * @param quantity quantity of the specified requiriment that you want to verify
     * @return true if the player has met the requirement in the specified quantity,
     * false otherwise
     *
     * @author Carlo
     */

    //need to add the possibility of increasing firePower of enginePower with batteries.

    default boolean isSatisfying(Player player, ElementType requirementType, int quantity) {
        int addedPower = 0;

        if (requirementType == ElementType.CrewMember) {

            if (player.getShipBoard().getShipBoardAttributes().getCrewMembers() >= quantity) {
                return true;
            }

            return false;
        } else if (requirementType == ElementType.FirePower){

            if (player.getShipBoard().getShipBoardAttributes().getFirePower() >= quantity) {
                return true;
            }

            return false;
        }
        else{


        }

    }

}
