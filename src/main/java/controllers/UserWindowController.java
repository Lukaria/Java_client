package controllers;

import Main.*;
import client.ClientHandler;

import controllers.Usability.Methods;

import entity.Company;
import entity.User;
import enums.Commands;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class UserWindowController implements Methods {

    @FXML
    private TableColumn<Company, String> companiesColumn;
    @FXML
    private Button addCompanyButton;

    @FXML
    private Button updateCompanyButton;

    @FXML
    private Button deleteCompanyButton;

    @FXML
    private TableView<Company> companiesTable;

    @FXML
    private TextField companyName;

    @FXML
    private Button editButton;

    @FXML
    private Label emailField;

    @FXML
    private Text errortextCompany;

    @FXML
    private Text errortextEdit;

    @FXML
    private Button exitButton;

    @FXML
    private Label loginField;

    @FXML
    private Label nameField;

    @FXML
    private Label passwordField;

    @FXML
    private TextField editPassword;
    @FXML
    private TextField editEmail;
    @FXML
    private TextField editName;

    @FXML
    private ToolBar toolbar;

    @FXML
    private Button toolbarAccount;

    @FXML
    private Button ImageButton;
    @FXML
    private ChoiceBox<String> toolbarCompanies;

    @FXML
    private Button toolbarInfo;

    @FXML
    private ImageView UserImage;

    @FXML
    private Label toolbarUsername;

    private User thisUser;

    private ClientHandler connection;

    private List<Company> companiesList = new ArrayList<>();

    boolean edit = true;

    private int lastSelected = 0;


    private void edit(Company company) {

    }

    private void setImage() {
        File file = new File("src/main/resources/forms/images/avatar.png");
        if (thisUser.getImage() != 9)
            file = new File("src/main/resources/forms/images/icon" + thisUser.getImage() + ".png");

        Image image = new Image(file.toURI().toString());
        UserImage.setImage(image);
        UserImage.setFitWidth(284);
        UserImage.setFitHeight(284);
    }

    @FXML
    private void initialize() {
        UserImage.setVisible(true);
        try {
            connection = ClientHandler.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        companiesColumn.setCellValueFactory(new PropertyValueFactory<Company, String>("name"));

        companiesTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> edit(newValue));

        companiesTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if(newValue == null) {
                        lastSelected = 0;
                        return;
                    }
                    if(lastSelected != newValue.getCompanyId())
                        EditEvent(newValue);
                });
    }

    private void EditEvent(Company newValue) {
        lastSelected = newValue.getCompanyId();
        companyName.setText(newValue.getName());
        addCompanyButton.setVisible(false);
        updateCompanyButton.setVisible(true);
        deleteCompanyButton.setVisible(true);
    }


    public void setData(User user) {
        thisUser = user;
        companiesList = user.getCompanies();
        loginField.setText(user.getLogin());
        passwordField.setText(user.getPassword());
        nameField.setText(user.getName());
        emailField.setText(user.getEmail());
        toolbarUsername.setText("| " + user.getName());
        toolbarUsername.setStyle("-fx-font-size: 24px");
        buttonChoosed(toolbarAccount);

        setImage();
        refreshToolbarCompanies();

        toolbarCompanies.setOnAction((ActionEvent event) -> {
            String name = toolbarCompanies.getValue();
            int index = 0;
            for (Company company : companiesList) {
                if (company.getName().equals(name)) {

                    name = company.getName();
                    break;
                }
                index++;
            }

            thisUser.setCurrentCompany(index);
            toolbarCompanies.setValue(name);

        });
    }

    @FXML
    void AccountButtonClicked(ActionEvent event) {

    }

    @FXML
    void InfoButtonClicked(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/forms/InfoWindow.fxml"));
        ScrollPane page = null;
        try {
            page = (ScrollPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Справка");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(Main.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            dialogStage.showAndWait();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void ExitButtonClicked(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Выход из приложения");
        alert.setHeaderText("Вы уверены?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            Stage stage = Main.getPrimaryStage();
            stage.close();
        }

    }

    @FXML
    void LiquidityButtonClicked(ActionEvent event) {
        try {
            Stage stage = Main.getPrimaryStage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/forms/LiquidityWindow.fxml"));
            Parent root = loader.load();

            LiquidityWindowController lwc = loader.getController();
            lwc.setData(thisUser);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void ProfitButtonClicked(ActionEvent event) {
        try {
            Stage stage = Main.getPrimaryStage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/forms/ProfitWindow.fxml"));
            Parent root = loader.load();

            ProfitWindowController pwc = loader.getController();
            pwc.setData(thisUser);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void addCompanyClicked(ActionEvent event) {
        errortextCompany.setVisible(false);
        if (companyName.getText().length() == 0) {
            errorText(errortextCompany, "Поле не может быть пустым!", "-fx-text-fill: red; -fx-font-size: 12px;");
            return;
        }

        for (Company compare : companiesTable.getItems()) {
            if (compare.getName().equals(companyName.getText())) {
                errorText(errortextCompany, "Такая компания уже существует!", "-fx-text-fill: red; -fx-font-size: 12px;");
                return;
            }
        }

        errortextCompany.setVisible(false);

        Company company = new Company(thisUser, companyName.getText());

        connection.sendObject(company, Commands.NEWCOMPANY);
        connection.sendObject(thisUser, Commands.NEWCOMPANY);
        TransferObject to = connection.getObject();
        company = (Company) to.getObject();

        thisUser.addCompany(company);

        refreshToolbarCompanies();
        companiesList = thisUser.getCompanies();
    }

    @FXML
    void UpdateCompanyButtonClicked(ActionEvent event) {
        errortextCompany.setVisible(false);

        if (companyName.getText().length() == 0) {
            errorText(errortextCompany, "Поле не может быть пустым!", "-fx-text-fill: red; -fx-font-size: 12px;");
            return;
        }

        for (Company compare : companiesTable.getItems()) {
            if (compare.getName().equals(companyName.getText()) && compare.getCompanyId() != lastSelected) {
                errorText(errortextCompany, "Такая компания уже существует!", "-fx-text-fill: red; -fx-font-size: 12px;");
                return;
            }
        }

        Company company = new Company();
        company.setName(companyName.getText());
        company.setCompanyId(lastSelected);
        for(int i = 0; i < companiesList.size(); i++){
            if(companiesList.get(i).getCompanyId() == lastSelected){
                companiesList.get(i).setName(company.getName());
                break;
            }
        }
        connection.sendObject(company, Commands.UPDATE_COMPANY);
        connection.sendObject(thisUser, Commands.UPDATE_COMPANY);

        refreshToolbarCompanies();

        companyName.setText(null);
        companyName.setPromptText("Название компании");



        addCompanyButton.setVisible(true);
        deleteCompanyButton.setVisible(false);
        updateCompanyButton.setVisible(false);
    }

    @FXML
    void DeleteCompanyButtonClicked(ActionEvent event) {
        errortextCompany.setVisible(false);
        if(companiesList.size() == 1){
            errorText(errortextCompany, "Должна быть хотя бы одна компания!", "-fx-text-fill: red; -fx-font-size: 12px;");
            return;
        }
        Company company = new Company();
        company.setCompanyId(lastSelected);
        for(int i=0; i< companiesList.size(); i++){
            if(companiesList.get(i).getCompanyId() == lastSelected){
                companiesList.remove(i);
                break;
            }
        }
        connection.sendObject(company, Commands.DELETE);
        refreshToolbarCompanies();

        companyName.setText(null);
        companyName.setPromptText("Название компании");

        addCompanyButton.setVisible(true);
        deleteCompanyButton.setVisible(false);
        updateCompanyButton.setVisible(false);
        errortextCompany.setVisible(false);
    }

    private void refreshToolbarCompanies() {
        companiesList.sort(Comparator.comparing(Company::getName));
        toolbarCompanies.getItems().clear();
        for (Company company : companiesList) {
            toolbarCompanies.getItems().add(company.getName());
        }
        assert companiesList != null;
        toolbarCompanies.setValue(companiesList.get(0).getName());
        companiesTable.getItems().clear();
        companiesTable.setItems(FXCollections.observableArrayList(companiesList));
    }

    @FXML
    void editButtonClicked(ActionEvent event) {
        if (edit) {
            disableEdit(false);

            editPassword.setText(passwordField.getText());
            editName.setText(nameField.getText());
            editEmail.setText(emailField.getText());


            editButton.setText("Подветрдить");
            editButton.setStyle("-fx-background-color:#f58632");
            edit = !edit;

        } else {
            boolean flag = false;
            if (editPassword.getText().length() == 0) {
                flag = true;
            }
            if (editName.getText().length() == 0) {
                flag = true;
            }
            if (editEmail.getText().length() == 0) {
                flag = true;
            }

            if (flag) {
                errorText(errortextEdit, "Поля не могут быть пустыми!", "-fx-text-fill: red; -fx-font-size: 14px;");
                return;
            }


            errortextEdit.setVisible(false);

            thisUser.setPassword(editPassword.getText());
            thisUser.setName(editName.getText());
            thisUser.setEmail(editEmail.getText());
            User user = new User();

            user.setUserId(thisUser.getUserId());
            user.setLogin(thisUser.getLogin());
            user.setPassword(thisUser.getPassword());
            user.setName(thisUser.getName());
            user.setEmail(thisUser.getEmail());
            user.setImage(thisUser.getImage());
            user.setRole("User");
            connection.sendObject(user, Commands.UPDATE);

            passwordField.setText(thisUser.getPassword());
            nameField.setText(thisUser.getName());
            emailField.setText(thisUser.getEmail());
            toolbarUsername.setText("| " + thisUser.getName());


            disableEdit(true);

            editButton.setText("Редактировать");
            editButton.setStyle("-fx-background-color: #f9b331;");
            edit = !edit;

            errortextCompany.setVisible(false);
        }

    }

    @FXML
    void ImageButtonClicked(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/forms/IconWindow.fxml"));
        FlowPane page = null;
        try {
            page = (FlowPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Иконки");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(Main.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            IconWindowController controller = loader.getController();
            controller.setData(dialogStage, UserImage, thisUser.getImage());

            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            dialogStage.showAndWait();

            thisUser.setImage(controller.getIcon());
            setImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private void disableEdit(boolean enable) {
        passwordField.setVisible(enable);
        emailField.setVisible(enable);
        nameField.setVisible(enable);

        editPassword.setDisable(enable);
        editName.setDisable(enable);
        editEmail.setDisable(enable);
        ImageButton.setDisable(enable);

        editPassword.setVisible(!enable);
        editName.setVisible(!enable);
        editEmail.setVisible(!enable);
    }
}
