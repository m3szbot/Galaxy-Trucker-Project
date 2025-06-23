package it.polimi.ingsw.View.GUI.utils;

import it.polimi.ingsw.Model.Components.Battery;
import it.polimi.ingsw.Model.Components.Cabin;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.Components.Storage;
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

    public void populateShipBoardTiles(ShipBoard shipBoard) {

        int rows = ShipBoard.ROWS;
        int cols = ShipBoard.COLS;
        Component[][] shipStructure = shipBoard.getComponentMatrix();

        int indexRow = 3;
        int indexColumn = 2;

        for (int i = ShipBoard.FIRST_REAL_COL; i <= ShipBoard.LAST_REAL_COL; i++) {
            for (int j = ShipBoard.FIRST_REAL_ROW; j <= ShipBoard.LAST_REAL_ROW; j++) {

                Component component = shipStructure[i][j];

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

                    AnchorPane cellPane = createTileWithIndicators(new Image(component.getImagePath()), numbers, colors);
                    gridPane.add(cellPane, i - 3, j - 2);

                }

            }
        }

    }

    private AnchorPane createTileWithIndicators(Image componentImagePath, int[] numbers, Color[] colors) {

        AnchorPane cellPane = new AnchorPane();
        cellPane.setPrefSize(TILE_SIZE, TILE_SIZE);

        // component image
        ImageView imageView = new ImageView(componentImagePath);
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

}
