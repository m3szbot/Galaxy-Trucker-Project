package it.polimi.ingsw.View.GUI.utils;

import it.polimi.ingsw.Model.Components.Battery;
import it.polimi.ingsw.Model.Components.Cabin;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.Components.Storage;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ShipBoardController {

    public static final double TILE_SIZE = 79.8;

    @FXML
    private Pane pane;

    @FXML
    private GridPane gridPane;

    @FXML
    private TilePane stellarCredits;

    public void populateShipBoardTiles(ShipBoard shipBoard) {

        Component[][] shipStructure = shipBoard.getComponentMatrix();

        gridPane.getChildren().clear();

        for (int realCol = ShipBoard.FIRST_REAL_COL; realCol <= ShipBoard.LAST_REAL_COL; realCol++) {
            for (int realRow = ShipBoard.FIRST_REAL_ROW; realRow <= ShipBoard.LAST_REAL_ROW; realRow++) {

                Component component = shipStructure[realCol][realRow];

                if (component != null) {

                    int[] numbers;
                    Color[] colors;

                    if (component.getComponentName().equals("Storage")) {

                        numbers = ((Storage) component).getGoods();
                        colors = new Color[]{Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE};


                    } else if (component.getComponentName().equals("Battery")) {

                        numbers = new int[]{((Battery) component).getBatteryPower()};
                        colors = new Color[]{Color.GREEN};

                    } else if (component.getComponentName().equals("Cabin")) {

                        numbers = new int[]{((Cabin) component).getCrewMembers()};
                        colors = new Color[]{Color.WHITE};

                    } else {

                        numbers = null;
                        colors = null;

                    }

                    AnchorPane cellPane = createTileWithIndicators(new Image(component.getImagePath()), numbers, colors, component.getRotations());


                    // NORMALGAME
                    if (shipBoard.getGameType().equals(GameType.NORMALGAME)) {
                        // gridCol 0 - visibleCol 4 - realCol 3
                        gridPane.add(cellPane, realCol - ShipBoard.FIRST_REAL_COL, realRow - ShipBoard.FIRST_REAL_ROW);
                    }
                    // TESTGAME
                    else {
                        // skip first and last columns (no components)
                        if (realCol >= ShipBoard.FIRST_REAL_COL + 1 && realCol <= ShipBoard.LAST_REAL_COL - 1)
                            // gridCol 0 - visibleCol 5 - realCol 4
                            gridPane.add(cellPane, realCol - ShipBoard.FIRST_REAL_COL - 1, realRow - ShipBoard.FIRST_REAL_ROW);
                    }

                }

            }
        }

    }

    private AnchorPane createTileWithIndicators(Image componentImagePath, int[] numbers, Color[] colors, int rotations) {

        AnchorPane cellPane = new AnchorPane();
        cellPane.setPrefSize(TILE_SIZE, TILE_SIZE);

        // component image
        ImageView imageView = new ImageView(componentImagePath);
        imageView.setRotate(90 * rotations);
        imageView.setFitWidth(TILE_SIZE);
        imageView.setFitHeight(TILE_SIZE);
        imageView.setPreserveRatio(true);
        AnchorPane.setTopAnchor(imageView, 0.0);
        AnchorPane.setLeftAnchor(imageView, 0.0);

        // VBox for the indicators
        if (numbers != null && colors != null && numbers.length == colors.length && numbers.length > 0) {
            VBox indicatorsBox = new VBox(5); // spacing tra indicatori
            indicatorsBox.setPadding(new Insets(5));
            indicatorsBox.setAlignment(Pos.TOP_LEFT);
            AnchorPane.setTopAnchor(indicatorsBox, 2.0);
            AnchorPane.setLeftAnchor(indicatorsBox, 2.0);

            //cycling through the number of indicators
            for (int i = 0; i < numbers.length; i++) {
                HBox hBox = new HBox(3); // spacing tra quadratino e numero
                hBox.setAlignment(Pos.CENTER_LEFT);

                //creating the colored rectangle
                Rectangle square = new Rectangle(10, 10, colors[i]);
                square.setStroke(Color.BLACK);

                //number on the side
                Label numberLabel = new Label(String.valueOf(numbers[i]));
                numberLabel.setTextFill(Color.BLACK);
                numberLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 11;");

                //formatting the hbox
                hBox.getChildren().addAll(square, numberLabel);

                //adding the hbox to the vbox
                indicatorsBox.getChildren().add(hBox);
            }

            //putting teh vbox on the top of the image
            cellPane.getChildren().add(indicatorsBox);
        }

        //Adding the image to the background
        cellPane.getChildren().add(imageView);

        return cellPane;
    }

    public void addStellarCredits(ShipBoard shipBoard) {

        int stellarCreditsArray[] = shipBoard.getShipBoardAttributes().getCreditsInBankNotes();
        stellarCredits.getChildren().clear();

        HBox hBox = new HBox(8);
        hBox.setPrefWidth(299);
        hBox.setPrefHeight(53);
        hBox.setStyle("-fx-padding: 5; -fx-alignment: center-left;");

        for (int i = 0; i < 5; i++) {

            Image image;

            switch (i) {
                case 0 -> {

                    image = new Image("/Polytechnic/cardboard/cardboard-9.png");

                }
                case 1 -> {

                    image = new Image("/Polytechnic/cardboard/cardboard-8.png");
                }
                case 2 -> {

                    image = new Image("/Polytechnic/cardboard/cardboard-7.png");
                }
                case 3 -> {

                    image = new Image("/Polytechnic/cardboard/cardboard-6.png");
                }
                case 4 -> {

                    image = new Image("/Polytechnic/cardboard/cardboard-10.png");
                }
                default -> {
                    image = null;
                    System.err.println("Cosmic credit image in shipboard controller has not been correctly initialized");
                }
            }

            ImageView imageView = new ImageView(image);

            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            imageView.setPreserveRatio(true);

            Label label = new Label(String.valueOf(stellarCreditsArray[i]));
            label.setStyle("-fx-font-size: 14px; -fx-text-fill: yellow;");

            HBox imgBox = new HBox(3);
            imgBox.getChildren().addAll(imageView, label);
            imgBox.setStyle("-fx-alignment: center;");

            hBox.getChildren().add(imgBox);
        }

        stellarCredits.getChildren().add(hBox);

    }

}
