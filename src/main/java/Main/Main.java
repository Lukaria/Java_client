package Main;
import java.io.*;
import java.util.Objects;

import client.ClientHandler;
import javafx.application.Application;

import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import static javafx.fxml.FXMLLoader.*;

public class Main extends Application {

    public static Stage mainStage;

    static public Stage getPrimaryStage() {
        return Main.mainStage;
    }


    public static void main(String[] arg) throws IOException {

        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;
        ClientHandler client = ClientHandler.getInstance();
        try{
            client.getClientSocket().isConnected();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        mainStage.setOnCloseRequest(windowEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Выход из приложения");
            alert.setHeaderText("Вы уверены?");
            if(alert.showAndWait().get() == ButtonType.OK){
                Stage stage = Main.getPrimaryStage();
                stage.close();
            }
            else{
                alert.close();
            }
        });

        Scene primaryScene = new Scene(load(Objects.requireNonNull(getClass().getResource("/forms/SignInWindow.fxml"))));

        mainStage.setTitle("Coursework");
        mainStage.setScene(primaryScene);

        mainStage.show();
    }
}