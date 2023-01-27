package controllers;

import Main.Main;
import Main.TransferObject;
import client.ClientHandler;
import controllers.Usability.Methods;
import entity.Company;
import entity.User;
import enums.Commands;
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

import java.io.IOException;

public class SignUpWindowController implements Methods {

    @FXML
    private Button SignUpButton;

    @FXML
    private Button backButton;

    @FXML
    private TextField companyField;

    @FXML
    private TextField emailField;

    @FXML
    private Text errortextGlobal;

    @FXML
    private Text errortextLogin;

    @FXML
    private TextField loginField;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    private ClientHandler connection;

    @FXML
    private void initialize(){
        errortextLogin.setVisible(false);
        errortextGlobal.setVisible(false);
        try {
            connection = ClientHandler.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void SignUpButtonClicked(ActionEvent event) throws IOException {
        errortextLogin.setVisible(false);
        errortextGlobal.setVisible(false);
        boolean flag =false;
        if (loginField.getText().length() == 0) {
            flag = true;
        }
        if (passwordField.getText().length() == 0) {
            flag = true;
        }
        if (nameField.getText().length() == 0) {
            flag = true;
        }
        if (companyField.getText().length() == 0) {
            flag = true;
        }
        if (emailField.getText().length() == 0) {
            flag = true;
        }

        if(flag){
            errorText(errortextGlobal, "Поля не могут быть пустыми!", "-fx-text-fill: red; -fx-font-size: 12px;");
            return;
        }

        User user = new User(loginField.getText(), passwordField.getText(), "User", nameField.getText(), emailField.getText());
        user.setImage(9);

        connection.sendObject(user, Commands.isLoginEXIST);

        TransferObject transferObject = connection.getObject();
        if(transferObject.getCommand() == Commands.isEXIST){
            errortextGlobal.setVisible(false);
            errorText(errortextLogin, "Данный логин уже существует!", "-fx-text-fill: red; -fx-font-size: 12px;");
            return;
        }
        else {
            connection.sendObject(user, Commands.REGISTER);
            connection.sendObject(new Company(user, companyField.getText()), Commands.ADD);

            errorText(errortextGlobal, "Регистрация прошла успешно!", "-fx-text-fill:green; -fx-font-size: 12px;");

            transferObject = connection.getObject();
            user = (User) transferObject.getObject();



            try {
                Stage stage = Main.getPrimaryStage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/forms/UserWindow.fxml"));
                Parent root = loader.load();

                UserWindowController ucw = loader.getController();
                user.setCurrentCompany(0);
                ucw.setData(user);
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }

    @FXML
    void backButtonClicked(ActionEvent event) {
        try {
            Stage stage = Main.getPrimaryStage();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/forms/SignInWindow.fxml")));
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}