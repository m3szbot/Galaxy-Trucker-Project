package it.polimi.ingsw.View.GUI.utils;

import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller of the flightBoard fxml file. Its role is to
 * correctly set up the flight board pane according to the model
 * received from the server
 *
 * @author carlo
 */

public class FlightBoardController {

    @FXML
    Pane pane;

    @FXML
    Label cardsRemaining;

    private Map<Integer, TilePane> positionMap = new HashMap<>();

    private GameType gameType;

    public void setUpTilesMap(GameType gameType) {

        this.gameType = gameType;

        if (gameType == GameType.TESTGAME) {

            for (int i = 1; i <= 18; i++) {
                TilePane tilePane = (TilePane) pane.lookup("#pos" + i);
                positionMap.put(i, tilePane);
            }

        } else {

            for (int i = 1; i <= 24; i++) {
                TilePane tilePane = (TilePane) pane.lookup("#pos" + i);
                positionMap.put(i, tilePane);
            }

        }

    }

    public void populateFlightBoard(FlightBoard flightBoard) {

        //clearing the flightboard

        for (TilePane tilePane : positionMap.values()) {
            tilePane.getChildren().clear();
        }

        for (Player player : flightBoard.getPlayerOrderList()) {

            int pos = flightBoard.getPlayerTile(player);
            int tiles = flightBoard.getNumberOfTiles();

            while (pos <= 0) {
                pos += tiles;
            }

            while (pos > tiles) {
                pos -= tiles;
            }

            TilePane tilePane = positionMap.get(pos);

            // Creo il triangolino indicatore
            Polygon triangle = new Polygon();
            triangle.getPoints().addAll(0.0, 0.0, 10.0, 20.0, 20.0, 0.0);

            triangle.setFill(Color.valueOf(player.getColor().toString()));
            triangle.setStroke(Color.web("#ffd93d"));
            triangle.setStrokeWidth(1.5);

            // add shadow
            DropShadow shadow = new DropShadow();
            shadow.setOffsetX(0);
            shadow.setOffsetY(0);
            shadow.setColor(Color.BLACK);
            shadow.setRadius(2);
            triangle.setEffect(shadow);

            tilePane.getChildren().add(triangle);
        }
    }

    public void setRemainingCards(FlightBoard flightBoard) {
        cardsRemaining.setText(flightBoard.getCardsNumber() + " cards remaining");
    }

}
