package main;

import database.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Launches scheduler app. */
public class Main extends Application {
    /** Opens log in page on launch.
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/logInScreen.fxml"));
        primaryStage.setTitle("Turbo Scheduler 3000");
        primaryStage.setScene(new Scene(root, 380, 300));
        primaryStage.show();
    }

    /** Connects to database on launch and terminates connection on close.
     *
     * @param args
     */
    public static void main(String[] args) {
        JDBC.makeConnection();
        launch(args);
        JDBC.closeConnection();
    }
}
