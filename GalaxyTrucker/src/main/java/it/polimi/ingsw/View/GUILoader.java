package it.polimi.ingsw.View;

import javafx.application.Application;
import javafx.stage.Stage;

public class GUILoader extends Application {
    GeneralView view;
    public GUILoader(GeneralView view) {
        this.view = view;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        /*
        Parent root = FXMLLoader.load(getClass().getResource(view.getFXML()));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Galaxy Trucker");
        primaryStage.show();

         */
    }


}
