module it.polimi.ingsw {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.rmi;

    exports it.polimi.ingsw.Connection;
    exports it.polimi.ingsw.Model;
    exports it.polimi.ingsw.View;
    exports it.polimi.ingsw.Controller;
}