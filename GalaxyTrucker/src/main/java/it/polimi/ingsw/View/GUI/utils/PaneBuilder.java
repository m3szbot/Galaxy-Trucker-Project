package it.polimi.ingsw.View.GUI.utils;

import it.polimi.ingsw.Controller.FlightPhase.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

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

    public static StackPane buildCardImage(Card card){

        Image cardImage = new Image(card.getCardImage());
        ImageView cardImageView = new ImageView(cardImage);
        StackPane wrapper = new StackPane(cardImageView);
        wrapper.setStyle("""
        -fx-border-color: #007FFF;
        -fx-border-width: 2px;
        -fx-border-style: solid;
        -fx-border-radius: 0;
        -fx-background-radius: 0;
    """);
        return wrapper;
    }

    //to update to handle the rotation of the components
    public static ImageView buildComponentImage(Component component){
        Image componentImage = new Image(component.getImagePath());
        ImageView componentImageView = new ImageView(componentImage);

        componentImageView.setRotate(90 * component.getRotations());
        componentImageView.setFitWidth(200);
        componentImageView.setFitHeight(200);
        componentImageView.setPreserveRatio(true);
        componentImageView.setSmooth(true);
        componentImageView.setCache(true);

        return componentImageView;
    }

    public static void buildShipBoardPane(ShipBoard shipBoard){

        if(shipBoardController == null){
            throw new IllegalArgumentException("Shipboard controller is null!");
        }

        shipBoardController.populateShipBoardTiles(shipBoard);
        shipBoardController.addStellarCredits(shipBoard);

    }

    public static void buildFlightBoardPane(FlightBoard flightBoard){

        if(flightBoardController == null){
            throw new IllegalArgumentException("Flightboard controller is null!");
        }

        flightBoardController.populateFlightBoard(flightBoard);
        flightBoardController.setRemainingCards(flightBoard);

    }

}
