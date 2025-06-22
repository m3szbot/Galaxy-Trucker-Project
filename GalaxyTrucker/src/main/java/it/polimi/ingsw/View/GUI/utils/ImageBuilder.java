package it.polimi.ingsw.View.GUI.utils;

import it.polimi.ingsw.Controller.FlightPhase.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Utility class which build the image for each relevant element
 * of the game
 *
 * @author carlo
 */

public final class ImageBuilder {

    public static ImageView buildCardImage(Card card){

        Image cardImage = new Image(card.getCardImage());
        ImageView cardImageView = new ImageView(cardImage);
        return cardImageView;
    }

    public static ImageView buildComponentImage(Component component){
        Image componentImage = new Image(component.getImagePath());
        ImageView componentImageView = new ImageView(componentImage);
        return componentImageView;
    }

    public static Pane buildShipBoardImage(ShipBoard shipBoard){

        if(shipBoard.getGameType() == GameType.NORMALGAME){



        }
        else{

        }

    }

}
