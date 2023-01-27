package controllers;

import client.ClientHandler;
import controllers.Usability.Methods;
import enums.Commands;
import Main.TransferObject;
import entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import Main.Main;

import java.io.IOException;

public class SignInWindowController implements Methods {
    @FXML
    private Button SignInButton;

    @FXML
    private Button SignUpButton;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Text errortext;
    private ClientHandler connection;


    @FXML
    void SignInButtonClicked(ActionEvent event) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, IOException {
        boolean flag = false;

        if (passwordField.getText().length() == 0) {
            flag = true;
        }
        if (loginField.getText().length() == 0) {
            flag = true;
        }

        if (flag) {
            errorText(errortext, "Поля не могут быть пустыми!", "-fx-text-fill: red; -fx-font-size: 12px;");
            return;
        }

        errorText(errortext, "Выполняется вход...", "-fx-text-fill:green; -fx-font-size: 12px;");

        User user = new User(loginField.getText(), passwordField.getText());
        connection = ClientHandler.getInstance();
        connection.sendObject(user, Commands.isEXIST);
        TransferObject transferObject = connection.getObject();
        if (transferObject.getCommand() != Commands.isEXIST) {
            errorText(errortext, "Неверный логин или пароль", "-fx-text-fill: red; -fx-font-size: 12px;");

        } else {


            user = (User) transferObject.getObject();
            user.setCurrenctCompany(0);
            if (user.getRole().equals("Admin")) {
                Stage stage = Main.getPrimaryStage();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/forms/AdminWindow.fxml")));
                stage.setScene(scene);
                stage.show();

            } else {
                Stage stage = Main.getPrimaryStage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/forms/UserWindow.fxml"));
                Parent root = loader.load();
                UserWindowController ucw = loader.getController();
                user.setCurrentCompany(0);
                ucw.setData(user);
                stage.setScene(new Scene(root));
                stage.show();
            }
        }
    }

    @FXML
    void SignUpButtonClicked(ActionEvent event) {
        try {
            Stage stage = Main.getPrimaryStage();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/forms/SignUpWindow.fxml")));
            stage.setScene(scene);

            stage.show();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

}
