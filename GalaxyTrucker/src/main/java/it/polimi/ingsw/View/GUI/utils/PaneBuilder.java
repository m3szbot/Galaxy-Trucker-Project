package it.polimi.ingsw.View.GUI.utils;

import it.polimi.ingsw.Controller.FlightPhase.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Utility class which build the pane/imageView for each relevant element
 * of the game
 *
 * @author carlo
 */

public final class PaneBuilder {

    private static ShipBoardController shipBoardController;
    private static FlightBoardController flightBoardController;

    public static void setShipBoardController(ShipBoardController controller) {

        shipBoardController = controller;

    }

    public static void setFlightBoardController(FlightBoardController controller){
        flightBoardController = controller;
    }

    public static ImageView buildCardImage(Card card){

        Image cardImage = new Image(card.getCardImage());
        ImageView cardImageView = new ImageView(cardImage);
        return cardImageView;
    }

    //to update to handle the rotation of the components
    public static ImageView buildComponentImage(Component component){
        Image componentImage = new Image(component.getImagePath());
        ImageView componentImageView = new ImageView(componentImage);

        componentImageView.setRotate(90 * component.getRotations());

        return componentImageView;
    }

    public static void buildShipBoardPane(ShipBoard shipBoard){

        shipBoardController.populateShipBoardTiles(shipBoard);
        shipBoardController.addStellarCredits(shipBoard);

    }

    public static void buildFlightBoardPane(FlightBoard flightBoard){

        flightBoardController.populateFlightBoard(flightBoard);
        flightBoardController.setRemainingCards(flightBoard);

    }

}
