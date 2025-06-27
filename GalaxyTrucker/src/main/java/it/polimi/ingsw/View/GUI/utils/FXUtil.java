package it.polimi.ingsw.View.GUI.utils;

import javafx.application.Platform;

/**
 * Util which must be used every time you have to update the GUI.
 * This is caused by the fact that the modification you want to do
 * MUST be done in the thread of the gui application. The method checks
 * whether the current thread is the application gui thread or not. If not, it inserts
 * the modification into the queue of the application gui thread, which will run
 * it when ready.
 *
 * @author carlo
 */

public final class FXUtil {

    public static void runOnFXThread(Runnable runnable){
        if(Platform.isFxApplicationThread()){
            runnable.run();
        }
        else{
            Platform.runLater(runnable);
        }
    }

}
