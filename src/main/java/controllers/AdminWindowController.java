package controllers;

import Main.TransferObject;
import client.ClientHandler;

import controllers.Usability.Methods;

import entity.Company;
import entity.Currency;
import entity.User;
import enums.Commands;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AdminWindowController implements Methods {
    @FXML
    private TableView<User> UserTable;

    @FXML
    private TreeView<String> UserTree;

    @FXML
    private Button addButton;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private TableColumn<Currency, Integer> currencyCode;

    @FXML
    private TableColumn<Currency, String> currencyAbbreviation;

    @FXML
    private TableColumn<Currency, Float> currencyCoeff;

    @FXML
    private TableColumn<Currency, Integer> currencyID;

    @FXML
    private TableColumn<Currency, Character> currencySymbol;


    @FXML
    private TableView<Currency> CurrencyTable;

    @FXML
    private Button deleteButton;

    @FXML
    private Text errortext;

    @FXML
    private Button fileButton;

    @FXML
    private Button filterButton;

    @FXML
    private TextField textField1;

    @FXML
    private TextField textField2;

    @FXML
    private TextField textField3;

    @FXML
    private TextField textField4;

    @FXML
    private Button updateButton;

    @FXML
    private TableColumn<User, Integer> userID;

    @FXML
    private TableColumn<User, String> userEmail;

    @FXML
    private TableColumn<User, String> userLogin;

    @FXML
    private TableColumn<User, String> userName;

    @FXML
    private TableColumn<User, String> userPassword;

    @FXML
    private TableColumn<User, String> userRole;

    private ClientHandler connection = null;

    private enum Window {
        USERS,
        CURRENCY,
        TREE,
    }

    private enum Scenario {
        ADD,
        UPDATE
    }

    private enum Sort{
        ASCENDING,
        DESCENDING
    }

    private Sort sortType = Sort.ASCENDING;

    private Window currentWindow = Window.USERS;
    private Scenario currentScenrio = Scenario.ADD;

    private List<User> users = new ArrayList<>();
    private List<Currency> currencies = new ArrayList<>();

    private int lastSelected = 0;
    private List<Company> lastComp = null;


    void setColumns() {
        userID.setCellValueFactory(new PropertyValueFactory<User, Integer>("userId"));
        userLogin.setCellValueFactory(new PropertyValueFactory<User, String>("login"));
        userPassword.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        userRole.setCellValueFactory(new PropertyValueFactory<User, String>("role"));
        userName.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        userEmail.setCellValueFactory(new PropertyValueFactory<User, String>("email"));

        currencyID.setCellValueFactory(new PropertyValueFactory<Currency, Integer>("currencyId"));
        currencyCode.setCellValueFactory(new PropertyValueFactory<Currency, Integer>("currencyCode"));
        currencySymbol.setCellValueFactory(new PropertyValueFactory<Currency, Character>("symbol"));
        currencyCoeff.setCellValueFactory(new PropertyValueFactory<Currency, Float>("coeff"));
        currencyAbbreviation.setCellValueFactory(new PropertyValueFactory<Currency, String>("abbreviation"));
    }

    private void EditEventUser(User user) {
        refreshButtons(Scenario.UPDATE);

        lastSelected = user.getUserId();
        textField1.setText(user.getLogin());
        textField2.setText(user.getPassword());
        textField3.setText(user.getName());
        textField4.setText(user.getEmail());

        textField1.setDisable(true);
        textField2.setDisable(true);
        lastComp = user.getCompanies();
    }

    private void EditEventCurrency(Currency currency) {
        refreshButtons(Scenario.UPDATE);

        lastSelected = currency.getCurrencyId();
        textField1.setText(Integer.toString(currency.getCurrencyCode()));
        textField2.setText(currency.getAbbreviation());
        textField3.setText(Character.toString(currency.getSymbol()));
        textField4.setText(Float.toString(currency.getCoeff()));
    }


    @FXML
    private void initialize() {
        try {
            connection = ClientHandler.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setColumns();

        List<String> array = new ArrayList<>();
        array.add("Пользователи");
        array.add("Валюты");
        array.add("Компании");

        choiceBox.setItems(FXCollections.observableArrayList(array));

        choiceBox.setOnAction((ActionEvent event) -> {
            String choice = choiceBox.getValue();
            errortext.setVisible(false);
            lastSelected = 0;
            refreshButtons(Scenario.ADD);
            switch (choice) {
                case "Пользователи":
                    currentWindow = Window.USERS;
                    refreshScene(Window.USERS);
                    refreshFields(Window.USERS);
                    refreshButtons(Scenario.ADD);
                    break;
                case "Валюты":
                    currentWindow = Window.CURRENCY;
                    refreshScene(Window.CURRENCY);
                    refreshFields(Window.CURRENCY);
                    refreshButtons(Scenario.ADD);
                    break;
                case "Компании":
                    currentWindow = Window.TREE;
                    refreshScene(Window.TREE);
                    refreshFields(Window.TREE);
                    refreshButtons(null);
                    break;
            }
        });
        choiceBox.setValue(array.get(0));
        errortext.setVisible(false);

        UserTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    System.out.println("calledUser");
                    System.out.println(lastSelected);
                    if(newValue == null){
                        lastSelected = 0;
                        return;
                    }
                    System.out.println(newValue.getUserId());
                    if (lastSelected != newValue.getUserId()){
                        EditEventUser(newValue);
                    }

                });

        CurrencyTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    System.out.println("calledCurrency");
                    System.out.println(lastSelected);
                    if(newValue == null){
                        lastSelected = 0;
                        return;
                    }
                    System.out.println(newValue.getCurrencyId());
                    if (lastSelected != newValue.getCurrencyId()){
                        EditEventCurrency(newValue);
                    }

                });

        refreshLists();
        refreshButtons(Scenario.ADD);
        refreshScene(Window.USERS);
        refreshTables();
    }

    @FXML
    void AddButtonClicked(ActionEvent event) {
        errortext.setVisible(false);
        switch (currentWindow) {
            case USERS: {
                boolean flag = false;

                User user = new User();
                if (textField1.getText().length() == 0) {
                    flag = true;
                }
                if (textField2.getText().length() == 0) {
                    flag = true;
                }
                if (textField3.getText().length() == 0) {
                    flag = true;
                }
                if (textField4.getText().length() == 0) {
                    flag = true;
                }

                if (flag) {
                    errorText(errortext, "Поля не могут быть пустыми!", "-fx-text-fill: red; -fx-font-size: 12px;");
                    return;
                }

                for (User compare : users) {
                    if (compare.getLogin().equals(textField1.getText())) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    errorText(errortext, "Пользователь с данным логином уже существует!", "-fx-text-fill: red; -fx-font-size: 12px;");
                    return;
                }

                user.setLogin(textField1.getText());
                user.setPassword(textField2.getText());
                user.setName(textField3.getText());
                user.setEmail(textField4.getText());
                user.setRole("User");
                user.setImage(9);

                Company company = new Company(user, "FirstCompany");
                connection.sendObject(user, Commands.REGISTER);
                connection.sendObject(company, Commands.REGISTER);

                TransferObject transferObject = connection.getObject();
//                user = (User) transferObject.getObject();
//                user.setCompanies(null);
//                user.addCompany(company);
//                users.add(user);

                refreshLists();
                refreshTables();
                refreshFields(Window.USERS);
            }
            break;
            case CURRENCY: {
                boolean flag = false;
                Currency currency = new Currency();
                if (textField1.getText().length() == 0) {
                    flag = true;
                }
                if (textField2.getText().length() != 3) {
                    errorText(errortext, "Соркащение валюты введено неправильно!", "-fx-text-fill: red; -fx-font-size: 12px;");
                    return;
                }
                if (textField3.getText().length() != 1) {

                    errorText(errortext, "Символ валюты введен непрвильно!", "-fx-text-fill: red; -fx-font-size: 12px;");
                    return;
                }
                if (textField4.getText().length() == 0) {
                    flag = true;
                }

                if (flag) {
                    errorText(errortext, "Поля не могут быть пустыми!", "-fx-text-fill: red; -fx-font-size: 12px;");
                    return;
                }

                int code = 0;
                float coeff = 0;
                try {
                    code = Integer.parseInt(textField1.getText());
                    coeff = Float.parseFloat(textField4.getText());
                } catch (NumberFormatException ex) {
                    errorText(errortext, "Поля должны содержать числа!", "-fx-text-fill: red; -fx-font-size: 12px;");
                    return;
                }

                String str = textField3.getText();
                char symbol = str.charAt(0);

                for (Currency currency1 : currencies) {
                    if (currency1.getSymbol() == symbol && currency1.getAbbreviation().equals(textField2.getText())) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    errorText(errortext, "Данная валюта уже существует!", "-fx-text-fill: red; -fx-font-size: 12px;");
                    return;
                }
                currency.setCurrencyCode(code);
                currency.setSymbol(symbol);
                currency.setCoeff(coeff);
                currency.setCurrencyId(lastSelected);
                currency.setAbbreviation(textField2.getText().toUpperCase());


                connection.sendObject(currency, Commands.ADD_CURRENCY);

                TransferObject transferObject = connection.getObject();
                currency = (Currency) transferObject.getObject();
                currencies.add(currency);
                refreshFields(Window.CURRENCY);
                refreshTables();
            }
            break;
            default:
                break;

        }

    }

    @FXML
    void DeleteButtonClicked(ActionEvent event) {
        errortext.setVisible(false);
        switch (currentWindow) {
            case USERS:
                User user = new User();
                user.setUserId(lastSelected);
                if(user.getUserId() == 1){
                    errorText(errortext, "Аккаунт админа удалить невозможно!", "-fx-text-fill: red; -fx-font-size: 12px;");
                    return;
                }
                connection.sendObject(user, Commands.DELETE);
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getUserId() == lastSelected) {
                        users.remove(i);
                        break;
                    }
                }

                refreshFields(currentWindow);
                refreshButtons(Scenario.ADD);
                refreshTables();
                break;
            case CURRENCY:
                Currency currency = new Currency();
                currency.setCurrencyId(lastSelected);
                connection.sendObject(currency, Commands.DELETE);
                for (int i = 0; i < currencies.size(); i++) {
                    if (currencies.get(i).getCurrencyId() == lastSelected) {
                        currencies.remove(i);
                        break;
                    }
                }

                refreshFields(currentWindow);
                refreshButtons(Scenario.ADD);
                refreshTables();
                break;
        }

    }

    @FXML
    void FileButtonClicked(ActionEvent event) {
        errortext.setVisible(false);
        switch (currentWindow) {
            case USERS: {
                boolean flag = false;

                User user = new User();
                if (textField1.getText().length() == 0) {
                    flag = true;
                }
                if (textField2.getText().length() == 0) {
                    flag = true;
                }
                if (textField3.getText().length() == 0) {
                    flag = true;
                }
                if (textField4.getText().length() == 0) {
                    flag = true;
                }

                if (flag) {
                    errorText(errortext, "Поля не могут быть пустыми!", "-fx-text-fill: red; -fx-font-size: 12px;");
                    return;
                }

                user.setLogin(textField1.getText());
                user.setPassword(textField2.getText());
                user.setName(textField3.getText());
                user.setEmail(textField4.getText());
                user.setRole("User");
                user.setUserId(lastSelected);
                for(User user1: users){
                    if(user1.getLogin().equals(textField1.getText())){
                        user = user1;
                        break;
                    }
                }

                connection.sendObject(user, Commands.RECORD);

                refreshFields(Window.USERS);
                refreshButtons(Scenario.ADD);
            }
            break;
            case CURRENCY: {
                boolean flag = false;
                Currency currency = new Currency();
                if (textField1.getText().length() == 0) {
                    flag = true;
                }
                if (textField2.getText().length() != 3) {
                    errorText(errortext, "Соркащение валюты введено неправильно!", "-fx-text-fill: red; -fx-font-size: 12px;");
                    return;
                }
                if (textField3.getText().length() != 1) {

                    errorText(errortext, "Символ валюты введен непрвильно!", "-fx-text-fill: red; -fx-font-size: 12px;");
                    return;
                }
                if (textField4.getText().length() == 0) {
                    flag = true;
                }

                if (flag) {
                    errorText(errortext, "Поля не могут быть пустыми!", "-fx-text-fill: red; -fx-font-size: 12px;");
                    return;
                }

                int code = 0;
                float coeff = 0;
                try {
                    code = Integer.parseInt(textField1.getText());
                    coeff = Float.parseFloat(textField4.getText());
                } catch (NumberFormatException ex) {
                    errorText(errortext, "Поля должны содержать числа!", "-fx-text-fill: red; -fx-font-size: 12px;");
                    return;
                }

                String str = textField3.getText();
                char symbol = str.charAt(0);

                currency.setCurrencyCode(code);
                currency.setSymbol(symbol);
                currency.setCoeff(coeff);
                currency.setCurrencyId(lastSelected);
                currency.setAbbreviation(textField2.getText().toUpperCase());


                connection.sendObject(currency, Commands.RECORD);

                refreshButtons(Scenario.ADD);
                refreshFields(Window.CURRENCY);
            }
            break;
            default:
                break;

        }


    }

    @FXML
    void FilterButtonClicked(ActionEvent event) {
        if(sortType == Sort.ASCENDING){
            sortType = Sort.DESCENDING;
            filterButton.setText("Убывание");
        }
        else{
            filterButton.setText("Возрастание");
            sortType = Sort.ASCENDING;
        }
        refreshTables();
    }

    @FXML
    void UpdateButtonClicked(ActionEvent event) {
        errortext.setVisible(false);
        switch (currentWindow) {
            case USERS: {
                boolean flag = false;

                User user = new User();
                if (textField3.getText().length() == 0) {
                    flag = true;
                }
                if (textField4.getText().length() == 0) {
                    flag = true;
                }

                if (flag) {
                    errorText(errortext, "Поля не могут быть пустыми!", "-fx-text-fill: red; -fx-font-size: 12px;");
                    return;
                }

                user.setLogin(textField1.getText());
                user.setPassword(textField2.getText());
                user.setName(textField3.getText());
                user.setEmail(textField4.getText());
                user.setRole("User");
                user.setUserId(lastSelected);
                user.setImage(9);

                connection.sendObject(user, Commands.UPDATE);

                //user.setCompanies(lastComp);

//                for(int i=0; i<users.size();i++){
//                    if(users.get(i).getUserId() == user.getUserId()){
//                        users.set(i, user);
//                        break;
//                    }
//                }
                refreshButtons(Scenario.ADD);
                refreshLists();
                refreshFields(Window.USERS);
                refreshTables();

            }
            break;
            case CURRENCY: {
                System.out.println(textField3.getText());
                System.out.println(textField3.getText().length());

                boolean flag = false;

                Currency currency = new Currency();
                if (textField1.getText().length() == 0) {
                    flag = true;
                }
                if (textField2.getText().length() != 3) {
                    errorText(errortext, "Соркащение валюты введено неправильно!", "-fx-text-fill: red; -fx-font-size: 12px;");
                    return;
                }
                if (textField3.getText().length() != 1) {

                    errorText(errortext, "Символ валюты введен непрвильно!", "-fx-text-fill: red; -fx-font-size: 12px;");
                    return;
                }
                if (textField4.getText().length() == 0) {
                    flag = true;
                }

                if (flag) {
                    errorText(errortext, "Поля не могут быть пустыми!", "-fx-text-fill: red; -fx-font-size: 12px;");
                    return;
                }

                int code = 0;
                float coeff = 0;
                try {
                    code = Integer.parseInt(textField1.getText());
                    coeff = Float.parseFloat(textField4.getText());
                } catch (NumberFormatException ex) {
                    errorText(errortext, "Поля должны содержать числа!", "-fx-text-fill: red; -fx-font-size: 12px;");
                    return;
                }

                String str = textField3.getText();
                char symbol = str.charAt(0);

                for (Currency currency1 : currencies) {
                    if (currency1.getSymbol() == symbol && currency1.getAbbreviation().equals(textField2.getText())
                            && currency1.getCurrencyId() != lastSelected) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    errorText(errortext, "Данная валюта уже существует!", "-fx-text-fill: red; -fx-font-size: 12px;");
                    return;
                }
                currency.setCurrencyCode(code);
                currency.setSymbol(symbol);
                currency.setCoeff(coeff);
                currency.setCurrencyId(lastSelected);
                currency.setAbbreviation(textField2.getText().toUpperCase());

                connection.sendObject(currency, Commands.UPDATE);

                refreshLists();
                refreshFields(Window.CURRENCY);
                refreshTables();
                refreshButtons(Scenario.ADD);
            }
            break;
        }

    }

    void refreshButtons(Scenario type) {
        addButton.setDisable(true);
        filterButton.setDisable(true);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
        fileButton.setDisable(true);
        if (type == null) return;
        switch (type) {
            case ADD:
                addButton.setDisable(false);
                filterButton.setDisable(false);

                updateButton.setDisable(true);
                deleteButton.setDisable(true);
                fileButton.setDisable(true);
                break;
            case UPDATE:
                addButton.setDisable(true);
                filterButton.setDisable(true);

                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                fileButton.setDisable(false);
                break;
        }


    }

    void refreshScene(Window type) {
        UserTable.setVisible(false);
        UserTree.setVisible(false);
        CurrencyTable.setVisible(false);
        switch (type) {
            case USERS:
                UserTable.setVisible(true);
                break;
            case CURRENCY:
                CurrencyTable.setVisible(true);
                break;
            case TREE:
                UserTree.setVisible(true);
                break;
        }
    }

    void refreshFields(Window type) {
        textField1.setDisable(false);
        textField2.setDisable(false);
        textField3.setDisable(false);
        textField4.setDisable(false);
        textField1.clear();
        textField2.clear();
        textField3.clear();
        textField4.clear();
        if (type == null) return;
        switch (type) {
            case USERS:
                textField1.setPromptText("Логин");
                textField2.setPromptText("Пароль");
                textField3.setPromptText("Имя");
                textField4.setPromptText("Почта");
                break;
            case CURRENCY:
                textField1.setPromptText("Код валюты");
                textField2.setPromptText("Сокращение");
                textField3.setPromptText("Символ");
                textField4.setPromptText("Коэффициент");
                break;
            case TREE:
                textField1.setDisable(true);
                textField2.setDisable(true);
                textField3.setDisable(true);
                textField4.setDisable(true);
                break;
        }

    }

    void refreshTables() {
        if(sortType == Sort.ASCENDING){
            users.sort(Comparator.comparing(User::getUserId));
            currencies.sort(Comparator.comparing(Currency::getCurrencyId));
        }
        else {
            users.sort(Comparator.comparing(User::getUserId).reversed());
            currencies.sort(Comparator.comparing(Currency::getCurrencyId).reversed());
        }


        UserTable.setItems(FXCollections.observableArrayList(users));
        CurrencyTable.setItems(FXCollections.observableArrayList(currencies));

        TreeItem<String> rootItem = new TreeItem<>();

        for (User user : users) {
            TreeItem<String> item = new TreeItem<String>(user.getName());
            rootItem.getChildren().add(item);
            for (Company company : user.getCompanies()) {
                TreeItem<String> itm = new TreeItem<String>(company.getName());
                item.getChildren().add(itm);
            }
        }

        UserTree.setRoot(rootItem);
    }

    void refreshLists() {
        users.clear();
        currencies.clear();

        connection.sendObject(new User(), Commands.READ_ALL);
        List<TransferObject> transferList = connection.getList();
        for (TransferObject to : transferList) {
            User user = (User) to.getObject();
            for(Company company: user.getCompanies()) company.nullAll();
            users.add((User) to.getObject());
        }

        connection.sendObject(new Currency(), Commands.READ_ALL);
        transferList = connection.getList();
        for (TransferObject to : transferList) {
            Currency currency = (Currency) to.getObject();
            currency.setLiquidities(null);
            currencies.add(currency);
        }
    }
}
