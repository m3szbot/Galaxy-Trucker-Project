import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.Model.Components.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ComponentTemporaryImageTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        List<Component> componentList = getJSONComponents();
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        for (Component component : componentList) {

            HBox hBox = new HBox(10);
            hBox.setAlignment(Pos.CENTER_LEFT);

            System.out.println(component.getImagePath() + " | rotations: " + component.getRotations());
            ComponentVisitor<String, RuntimeException> visitor = new ComponentStringGetterVisitor();
            String componentString = component.accept(visitor);
            System.out.printf(componentString);

            Image image = new Image(component.getImagePath());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setPreserveRatio(true);
            imageView.setRotate(90 * component.getRotations());

            // label nome
            Label label = new Label(component.getImagePath() + " | rotations: " + component.getRotations());

            hBox.getChildren().addAll(imageView, label);
            root.getChildren().add(hBox);
        }

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 600, 800);
        primaryStage.setTitle("Component Image Test");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private List<Component> getJSONComponents() {

        ObjectMapper mapper = new ObjectMapper();

        // Serve ad ignorare le proprietà(campi) sconosciuti degli oggetti json con campi aggiuntivi
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Explicit registration of all subtypes
        mapper.registerSubtypes(
                AlienSupport.class,
                Battery.class,
                Cabin.class,
                Cannon.class,
                Component.class,
                Engine.class,
                Shield.class,
                Storage.class
        );

        try {

            return mapper.readValue(new File("src/main/resources/Components.json"),
                    mapper.getTypeFactory().constructCollectionType(List.class, Component.class)

            );

        } catch (IOException e) {
            System.err.println("Error while reading components from the JSON file");
            return null;
        }
    }
}
