package it.polimi.ingsw.View.GUI.utils;

import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.HashMap;
import java.util.Map;

public class FlightBoardController {

    @FXML
    Pane pane;

    @FXML
    Label cardsRemaining;

    private Map<Integer, TilePane> positionMap = new HashMap<>();

    private GameType gameType;

    public void initialize(){

        for(int i = 1; i <= 18; i++){
            TilePane tilePane = (TilePane) pane.lookup("#pos" + i);
            positionMap.put(i, tilePane);
        }

    }

    public void setUpTilesMap(GameType gameType){

        this.gameType = gameType;

        if(gameType == GameType.TESTGAME){

            for(int i = 1; i <= 18; i++){
                TilePane tilePane = (TilePane) pane.lookup("#pos" + i);
                positionMap.put(i, tilePane);
            }

        }
        else{

            for(int i = 1; i <= 24; i++){
                TilePane tilePane = (TilePane) pane.lookup("#pos" + i);
                positionMap.put(i, tilePane);
            }

        }

    }

    public void populateFlightBoard(FlightBoard flightBoard){

        for (Player player : flightBoard.getPlayerOrderList()) {
            //TODO: CHANGE THE CORRISPONDANCE

            int pos = flightBoard.getPlayerTile(player);

            if(gameType == GameType.NORMALGAME){

                while(pos <= 0){
                    pos += 24;
                }

                while(pos > 24){
                    pos -= 24;
                }

            }
            else{

                while(pos <= 0){
                    pos += 18;
                }

                while(pos > 18){
                    pos -= 18;
                }
            }

            TilePane tilePane = positionMap.get(pos);

            // Creo il triangolino indicatore
            Polygon triangle = new Polygon();
            triangle.getPoints().addAll(0.0, 0.0, 10.0, 20.0, 20.0, 0.0);

            triangle.setFill(Color.valueOf(player.getColor().toString()));

            tilePane.getChildren().add(triangle);
        }
    }

    public void setRemainingCards(FlightBoard flightBoard){
        cardsRemaining.setText(flightBoard.getCardsNumber() + " cards remaining");
    }

}
