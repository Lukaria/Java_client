package controllers;


import Main.Main;
import Main.TransferObject;
import client.ClientHandler;

import controllers.Usability.Methods;
import entity.*;
import enums.Commands;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LiquidityWindowController implements Methods {

    @FXML
    private TextField A1;

    @FXML
    private TableColumn<Apives, Float> A1ColumnActives;
    @FXML
    private TableColumn<Apives, Float> A2ColumnActives;
    @FXML
    private TableColumn<Apives, Float> A3ColumnActives;
    @FXML
    private TableColumn<Apives, Float> A4ColumnActives;

    @FXML
    private TableColumn<Apives, Float> P1ColumnPassives;

    @FXML
    private TableColumn<Apives, Float> P2ColumnPassives;
    @FXML
    private TableColumn<Apives, Float> P3ColumnPassives;
    @FXML
    private TableColumn<Apives, Float> P4ColumnPassives;

    @FXML
    private TableColumn<Apives, Integer> yearColumnPassives;

    @FXML
    private TableColumn<Apives, Integer> yearColumnActives;

    @FXML
    private TableColumn<Liquidity, Integer> yearLiquidityColumn;
    @FXML
    private TableColumn<Liquidity, Float> currentLiquidityColumn;
    @FXML
    private TableColumn<Liquidity, Float> expectedLiquidityColumn;
    @FXML
    private TableColumn<Liquidity, Float> absoluteLiquidityColumn;

    @FXML
    private TableColumn<Liquidity, Float> quickLiquidityColumn;
    @FXML
    private TableColumn<Liquidity, Float> coefLiquidityColumn;
    @FXML
    private TableColumn<Liquidity, String> SOBLiquidityColumn;

    @FXML
    private TableColumn<Liquidity, String> currencyColumn;

    @FXML
    private Text activesSum;

    @FXML
    private Text passivesSum;
    @FXML
    private TextField A2;

    @FXML
    private TextField A3;

    @FXML
    private TextField A4;

    @FXML
    private TextField P1;

    @FXML
    private TextField P2;

    @FXML
    private TextField P3;

    @FXML
    private TextField P4;

    @FXML
    private Button updateButton;

    @FXML
    private PieChart activesChart;

    @FXML
    private TableView<Apives> activesTable;

    @FXML
    private Text errortextActives;

    @FXML
    private Text errortextPassives;

    @FXML
    private Text errorTextHidePiePane;

    @FXML
    private Text errorTextHidePane;

    @FXML
    private LineChart<Liquidity, Integer> liquidityChart;

    @FXML
    private TableView<Liquidity> liquidityTable;

    @FXML
    private Button deleteButton;

    @FXML
    private Button liquidityButton;

    @FXML
    private Button getLiquidityButton;
    @FXML
    private PieChart passivesChart;

    @FXML
    private TableView<Apives> passivesTable;

    @FXML
    private ToolBar toolbar;

    @FXML
    private Button toolbarAccount;

    @FXML
    private ChoiceBox<String> toolbarCompanies;

    @FXML
    private ChoiceBox<String> currencyBox;

    @FXML
    private Button toolbarInfo;

    @FXML
    private Label toolbarUsername;

    @FXML
    private ChoiceBox<Integer> yearAP;

    @FXML
    private ChoiceBox<Integer> yearActives;

    @FXML
    private ChoiceBox<Integer> yearPassives;

    private ClientHandler connection = ClientHandler.getInstance();

    private List<Apives> activesList = FXCollections.observableArrayList();
    private List<Apives> passivesList = FXCollections.observableArrayList();
    private List<Apives> apivesList = FXCollections.observableArrayList();
    private List<Liquidity> liquidityList = FXCollections.observableArrayList();

    private List<Currency> currencies = new ArrayList<>();

    private int lastSelected = 0;
    User thisUser = new User();

    public LiquidityWindowController() throws IOException {
    }

    private void initColumns() {
        yearColumnActives.setCellValueFactory(new PropertyValueFactory<Apives, Integer>("year"));
        A1ColumnActives.setCellValueFactory(new PropertyValueFactory<Apives, Float>("AP1"));
        A2ColumnActives.setCellValueFactory(new PropertyValueFactory<Apives, Float>("AP2"));
        A3ColumnActives.setCellValueFactory(new PropertyValueFactory<Apives, Float>("AP3"));
        A4ColumnActives.setCellValueFactory(new PropertyValueFactory<Apives, Float>("AP4"));

        yearColumnPassives.setCellValueFactory(new PropertyValueFactory<Apives, Integer>("year"));
        P1ColumnPassives.setCellValueFactory(new PropertyValueFactory<Apives, Float>("AP1"));
        P2ColumnPassives.setCellValueFactory(new PropertyValueFactory<Apives, Float>("AP2"));
        P3ColumnPassives.setCellValueFactory(new PropertyValueFactory<Apives, Float>("AP3"));
        P4ColumnPassives.setCellValueFactory(new PropertyValueFactory<Apives, Float>("AP4"));

        yearLiquidityColumn.setCellValueFactory(new PropertyValueFactory<Liquidity, Integer>("year"));
        currentLiquidityColumn.setCellValueFactory(new PropertyValueFactory<Liquidity, Float>("currentRatio"));
        expectedLiquidityColumn.setCellValueFactory(new PropertyValueFactory<Liquidity, Float>("expectedRatio"));
        absoluteLiquidityColumn.setCellValueFactory(new PropertyValueFactory<Liquidity, Float>("absoluteRatio"));
        quickLiquidityColumn.setCellValueFactory(new PropertyValueFactory<Liquidity, Float>("quickRatio"));
        coefLiquidityColumn.setCellValueFactory(new PropertyValueFactory<Liquidity, Float>("currentRatioCoeff"));
        SOBLiquidityColumn.setCellValueFactory(new PropertyValueFactory<Liquidity, String>("stateOfBalance"));
        currencyColumn.setCellValueFactory(liquidity -> new SimpleObjectProperty<>(liquidity.getValue().getCurrency().getAbbreviation()));
    }

    @FXML
    private void initialize() {
        initColumns();

        ObservableList<Integer> years = FXCollections.observableArrayList();
        for (int i = 2000; i <= 2023; i++) {
            years.add(i);
        }
        yearActives.setItems(years);
        yearActives.setOnAction((ActionEvent event) -> {
            yearPassives.setValue(yearActives.getValue());
        });


        activesTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue == null) {
                        lastSelected = 0;
                        return;
                    }
                    if (lastSelected != newValue.getYear())
                        EditEvent(newValue, newValue.isActive());
                });
        passivesTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue == null) {
                        lastSelected = 0;
                        return;
                    }
                    if (lastSelected != newValue.getYear())
                        EditEvent(newValue, newValue.isActive());
                });

        connection.sendObject(new Currency(), Commands.READ_ALL);
        List<TransferObject> toList = connection.getList();

        for (TransferObject to : toList)
            currencies.add((Currency) to.getObject());

        for (Currency currency : currencies)
            currencyBox.getItems().add(currency.getAbbreviation());

        currencyBox.setValue(currencies.get(0).getAbbreviation());


    }

    private void EditEvent(Apives newValue, boolean isActive) {
        lastSelected = newValue.getYear();
        if (isActive) {
            A1.setText(Float.toString(newValue.getAP1()));
            A2.setText(Float.toString(newValue.getAP2()));
            A3.setText(Float.toString(newValue.getAP3()));
            A4.setText(Float.toString(newValue.getAP4()));

            Apives passive = new Apives();
            for (Apives apive : passivesList) {
                if (newValue.getYear() == apive.getYear()) {
                    passive = apive;
                    break;
                }
            }

            P1.setText(Float.toString(passive.getAP1()));
            P2.setText(Float.toString(passive.getAP2()));
            P3.setText(Float.toString(passive.getAP3()));
            P4.setText(Float.toString(passive.getAP4()));
        } else {
            P1.setText(Float.toString(newValue.getAP1()));
            P2.setText(Float.toString(newValue.getAP2()));
            P3.setText(Float.toString(newValue.getAP3()));
            P4.setText(Float.toString(newValue.getAP4()));

            Apives active = new Apives();
            for (Apives apive : activesList) {
                if (newValue.getYear() == apive.getYear()) {
                    active = apive;
                    break;
                }
            }

            A1.setText(Float.toString(active.getAP1()));
            A2.setText(Float.toString(active.getAP2()));
            A3.setText(Float.toString(active.getAP3()));
            A4.setText(Float.toString(active.getAP4()));
        }

        yearActives.setValue(newValue.getYear());
        yearPassives.setValue(newValue.getYear());
        yearActives.setDisable(true);
        yearPassives.setDisable(true);

        getLiquidityButton.setVisible(false);
        updateButton.setVisible(true);
        deleteButton.setVisible(true);
    }


    public void setData(User user) {
        thisUser = user;
        toolbarUsername.setText("| " + user.getName());
        toolbarUsername.setStyle("-fx-font-size: 24px");
        buttonChoosed(liquidityButton);

        List<String> names = new ArrayList<>();
        for (Company company : user.getCompanies()) {
            names.add(company.getName());
        }
        assert names != null;
        toolbarCompanies.getItems().addAll(names);
        toolbarCompanies.setValue(names.get(thisUser.getCurrenctCompany()));
        toolbarCompanies.setOnAction((ActionEvent event) -> {
            String name = toolbarCompanies.getValue();

            int index = 0;
            for (Company company : user.getCompanies()) {
                if (company.getName().equals(name)) {
                    break;
                }
                index++;
            }

            thisUser.setCurrentCompany(index);
            try {
                Stage stage = Main.getPrimaryStage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/forms/LiquidityWindow.fxml"));
                Parent root = loader.load();

                LiquidityWindowController pwc = loader.getController();
                pwc.setData(thisUser);
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


        connection.sendObject(user.getCompanies().get(user.getCurrenctCompany()), Commands.LIQUIDITY);
        List<TransferObject> toList = connection.getList();
        for(TransferObject transferObject: toList){
            apivesList.add((Apives) transferObject.getObject());
        }
        toList = connection.getList();
        for(TransferObject transferObject: toList){
            liquidityList.add((Liquidity) transferObject.getObject());
        }
        List<Currency> currencyList = new ArrayList<>();
        toList = connection.getList();
        for(TransferObject transferObject: toList){
            currencyList.add((Currency) transferObject.getObject());
        }
        for(int i = 0; i < liquidityList.size(); i++)
            liquidityList.get(i).setCurrency(currencyList.get(i));

        activesList = new ArrayList<>();
        passivesList = new ArrayList<>();

        if (apivesList != null) {
            Collections.sort(apivesList, new Comparator<Apives>() {
                public int compare(Apives o1, Apives o2) {
                    return o1.getYear() - o2.getYear();
                }
            });
            for (Apives apives : apivesList) {
                if (apives.isActive()) {
                    activesList.add(apives);
                    yearAP.getItems().add(apives.getYear());
                } else {
                    passivesList.add(apives);
                }
            }
        }




        activesTable.setItems(FXCollections.observableArrayList(activesList));
        passivesTable.setItems(FXCollections.observableArrayList(passivesList));
        liquidityTable.setItems(FXCollections.observableArrayList(liquidityList));

        refreshTables();
        refreshChart();


        if (activesList.size() > 0 && passivesList.size() > 0)
            yearAP.setValue(apivesList.get(0).getYear());


        yearAP.setOnAction((ActionEvent event) -> {
            if(yearAP.getValue() == null) return;
            int year = yearAP.getValue();
            int index = 0;
            for (Apives apive : activesList) {
                if (apive.getYear() == year) {
                    break;
                }
                index++;
            }
            //yearAP.setValue(year);
            refreshPieCharts(index);

        });

        refreshPieCharts(0);
    }

    @FXML
    void AccountButtonClicked(ActionEvent event) throws IOException {
        Stage stage = Main.getPrimaryStage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/forms/UserWindow.fxml"));
        Parent root = loader.load();
        UserWindowController ucw = loader.getController();
        ucw.setData(thisUser);
        stage.setScene(new Scene(root));
        stage.show();
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
    void UpdateButtonClicked(ActionEvent event) {
        boolean flag = false;
        Apives active = new Apives();
        {
            if (yearActives.getValue() == null) {
                flag = true;
            }
            if (A1.getText().length() == 0) {
                flag = true;
            }
            if (A2.getText().length() == 0) {
                flag = true;
            }
            if (A3.getText().length() == 0) {
                flag = true;
            }
            if (A4.getText().length() == 0) {
                flag = true;
            }

            if (flag) {
                errorText(errortextActives, "Поля не могут быть пустыми!", "-fx-text-fill: red; -fx-font-size: 12px;");
            }
            Float a1, a2, a3, a4;
            try {
                a1 = Float.parseFloat(A1.getText());
                a2 = Float.parseFloat(A2.getText());
                a3 = Float.parseFloat(A3.getText());
                a4 = Float.parseFloat(A4.getText());
                if (a1 < 0.0 || a2 < 0.0 || a3 < 0.0 || a4 < 0.0) {
                    errorText(errortextActives, "Значение полей не может быть меньше нуля!", "-fx-text-fill: red; -fx-font-size: 12px;");
                    return;
                }
            } catch (NumberFormatException ex) {
                errorText(errortextActives, "Поля должны содержать числа!", "-fx-text-fill: red; -fx-font-size: 12px;");
                return;
            }

            active.setActive(true);
            active.setAP1(a1);
            active.setAP2(a2);
            active.setAP3(a3);
            active.setAP4(a4);
            active.setYear(yearActives.getValue());

            for(Apives apive: activesList){
                if(apive.getYear() == lastSelected){
                    active.setOperationId(apive.getOperationId());
                    break;
                }
            }

        }
        flag = false;
        Apives passive = new Apives();
        {

            if (yearPassives.getValue() == null) {
                flag = true;
            }
            if (P1.getText().length() == 0) {
                flag = true;
            }
            if (P2.getText().length() == 0) {
                flag = true;
            }
            if (P3.getText().length() == 0) {
                flag = true;
            }
            if (P4.getText().length() == 0) {
                flag = true;
            }

            if (flag) {
                errorText(errortextActives, "Поля не могут быть пустыми!", "-fx-text-fill: red; -fx-font-size: 12px;");
            }
            Float p1, p2, p3, p4;
            try {
                p1 = Float.parseFloat(P1.getText());
                p2 = Float.parseFloat(P2.getText());
                p3 = Float.parseFloat(P3.getText());
                p4 = Float.parseFloat(P4.getText());
                if (p1 < 0.0 || p2 < 0.0 || p3 < 0.0 || p4 < 0.0) {
                    errorText(errortextActives, "Значение полей не может быть меньше нуля!", "-fx-text-fill: red; -fx-font-size: 12px;");
                    return;
                }
            } catch (NumberFormatException ex) {
                errorText(errortextActives, "Поля должны содержать числа!", "-fx-text-fill: red; -fx-font-size: 12px;");
                return;
            }

            passive.setActive(false);
            passive.setAP1(p1);
            passive.setAP2(p2);
            passive.setAP3(p3);
            passive.setAP4(p4);
            passive.setYear(yearPassives.getValue());
            for(Apives apive: passivesList){
                if(apive.getYear() == lastSelected){
                    passive.setOperationId(apive.getOperationId());
                    break;
                }
            }
        }

        Currency currency = new Currency();
        for (Currency currency1: currencies){
            if(currencyBox.getValue().equals(currency1.getAbbreviation())){
                currency = currency1;
                break;
            }
        }
        connection.sendObject(active, Commands.UPDATE_APIVES);
        connection.sendObject(passive, Commands.UPDATE_APIVES);
        connection.sendObject(currency, Commands.UPDATE_APIVES);

        connection.sendObject(thisUser.getCompanies().get(thisUser.getCurrenctCompany()), Commands.UPDATE_APIVES);

        Liquidity liquidity = (Liquidity) connection.getObject().getObject();

        for (int i = 0; i < activesList.size(); i++) {
            if (activesList.get(i).getYear() == yearActives.getValue()) {
                activesList.set(i, active);
                break;
            }
        }

        for (int i = 0; i < passivesList.size(); i++) {
            if (passivesList.get(i).getYear() == yearPassives.getValue()) {
                passivesList.set(i, passive);
                break;
            }
        }

        for (int i = 0; i < liquidityList.size(); i++) {
            if (liquidityList.get(i).getOperationId() == liquidity.getOperationId()) {
                liquidity.setCurrency(currency);
                liquidityList.set(i, liquidity);
                break;
            }
        }

        refreshField();
        refreshTables();
        refreshPieCharts(0);
        refreshChart();

        yearActives.setDisable(false);
        getLiquidityButton.setVisible(true);
        updateButton.setVisible(false);
        deleteButton.setVisible(false);
    }

    @FXML
    void DeleteButtonClicked(ActionEvent event) {
        Apives apive = new Apives();
        apive.setYear(lastSelected);

        for (int i = 0; i < activesList.size(); i++) {
            if (activesList.get(i).getYear() == apive.getYear()) {
                apive = activesList.get(i);
                break;
            }
        }
        connection.sendObject(apive, Commands.DELETE);
        activesList.remove(apive);

        for (int i = 0; i < passivesList.size(); i++) {
            if (passivesList.get(i).getYear() == apive.getYear()) {
                apive = passivesList.get(i);
                break;
            }
        }
        connection.sendObject(apive, Commands.DELETE);
        passivesList.remove(apive);

        Liquidity liquidity = new Liquidity();
        for (int i = 0; i < liquidityList.size(); i++) {
            if (liquidityList.get(i).getYear() == apive.getYear()) {
                liquidity = liquidityList.get(i);
                break;
            }
        }
        connection.sendObject(liquidity, Commands.DELETE);
        liquidityList.remove(liquidity);


        refreshField();
        refreshTables();
        refreshPieCharts(0);
        refreshChart();

        getLiquidityButton.setVisible(true);
        updateButton.setVisible(false);
        deleteButton.setVisible(false);
        yearActives.setDisable(false);
    }

    @FXML
    void LiquidityButtonClicked(ActionEvent event) {


    }

    @FXML
    void getLiquidityButtonClicked(ActionEvent event) {
        boolean flag = false;
        Apives active = new Apives();
        {
            if (yearActives.getValue() == null) {
                flag = true;
            }
            if (A1.getText().length() == 0) {
                flag = true;
            }
            if (A2.getText().length() == 0) {
                flag = true;
            }
            if (A3.getText().length() == 0) {
                flag = true;
            }
            if (A4.getText().length() == 0) {
                flag = true;
            }

            if (flag) {
                errorText(errortextActives, "Поля не могут быть пустыми!", "-fx-text-fill: red; -fx-font-size: 12px;");
            }
            Float a1, a2, a3, a4;
            try {
                a1 = Float.parseFloat(A1.getText());
                a2 = Float.parseFloat(A2.getText());
                a3 = Float.parseFloat(A3.getText());
                a4 = Float.parseFloat(A4.getText());
                if (a1 < 0.0 || a2 < 0.0 || a3 < 0.0 || a4 < 0.0) {
                    errorText(errortextActives, "Значение полей не может быть меньше нуля!", "-fx-text-fill: red; -fx-font-size: 12px;");
                    return;
                }
            } catch (NumberFormatException ex) {
                errorText(errortextActives, "Поля должны содержать числа!", "-fx-text-fill: red; -fx-font-size: 12px;");
                return;
            }

            active.setActive(true);
            active.setAP1(a1);
            active.setAP2(a2);
            active.setAP3(a3);
            active.setAP4(a4);
            active.setYear(yearActives.getValue());

            for (Apives compare : activesList) {
                if (active.getYear() == compare.getYear()) {
                    errorText(errortextActives, "Поле с данной датой уже существует!", "-fx-text-fill: red; -fx-font-size: 12px;");
                    return;
                }
            }
        }
        flag = false;
        Apives passive = new Apives();
        {

            if (yearPassives.getValue() == null) {
                flag = true;
            }
            if (P1.getText().length() == 0) {
                flag = true;
            }
            if (P2.getText().length() == 0) {
                flag = true;
            }
            if (P3.getText().length() == 0) {
                flag = true;
            }
            if (P4.getText().length() == 0) {
                flag = true;
            }

            if (flag) {
                errorText(errortextActives, "Поля не могут быть пустыми!", "-fx-text-fill: red; -fx-font-size: 12px;");
            }
            Float p1, p2, p3, p4;
            try {
                p1 = Float.parseFloat(P1.getText());
                p2 = Float.parseFloat(P2.getText());
                p3 = Float.parseFloat(P3.getText());
                p4 = Float.parseFloat(P4.getText());
                if (p1 < 0.0 || p2 < 0.0 || p3 < 0.0 || p4 < 0.0) {
                    errorText(errortextActives, "Значение полей не может быть меньше нуля!", "-fx-text-fill: red; -fx-font-size: 12px;");
                    return;
                }
            } catch (NumberFormatException ex) {
                errorText(errortextActives, "Поля должны содержать числа!", "-fx-text-fill: red; -fx-font-size: 12px;");
                return;
            }

            passive.setActive(false);
            passive.setAP1(p1);
            passive.setAP2(p2);
            passive.setAP3(p3);
            passive.setAP4(p4);
            passive.setYear(yearPassives.getValue());
        }

        String str = currencyBox.getValue();
        Currency currency = new Currency();
        for (Currency currency1 : currencies) {
            if (currency1.getAbbreviation().equals(str)) {
                currency = currency1;
                break;
            }
        }

        connection.sendObject(active, Commands.ADD_APIVES);
        connection.sendObject(passive, Commands.ADD_APIVES);
        connection.sendObject(thisUser.getCompanies().get(thisUser.getCurrenctCompany()), Commands.ADD_APIVES);
        connection.sendObject(currency, Commands.ADD_APIVES);
        List<TransferObject> transferObject = connection.getList();
        activesList.add((Apives) transferObject.get(0).getObject());
        passivesList.add((Apives) transferObject.get(1).getObject());
        TransferObject to = connection.getObject();
        Liquidity liquidity = (Liquidity) to.getObject();
        liquidity.setCurrency(currency);
        liquidityList.add(liquidity);
        liquidityList.sort(Comparator.comparing(Liquidity::getYear));

        errortextActives.setVisible(false);
        errortextPassives.setVisible(false);

        refreshField();
        refreshTables();
        refreshChart();
        refreshPieCharts(0);
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

    private void refreshChart() {
        liquidityChart.getData().clear();
        hideChart(liquidityChart, errorTextHidePane, true);
        if (liquidityList.size() > 1) {
            boolean flag = false;

            for (int i = 0; i < liquidityList.size() - 1; i++) {
                if (liquidityList.get(i).getYear() + 1 != liquidityList.get(i + 1).getYear()) {
                    flag = true;
                    break;
                }
            }
            if (flag) return;

            hideChart(liquidityChart, errorTextHidePane, false);

            XYChart.Series cr = new XYChart.Series();
            cr.setName("Текущая ликвидность");

            XYChart.Series er = new XYChart.Series();
            er.setName("Перспективная ликвидность");

            XYChart.Series ar = new XYChart.Series();
            ar.setName("Коэффициент абсолютной ликвидности");

            XYChart.Series qr = new XYChart.Series();
            qr.setName("Коэффициент быстрой ликвидности");

            XYChart.Series crc = new XYChart.Series();
            crc.setName("Коэффициент текущей ликвидности");

            for (Liquidity liquidity : liquidityList) {
                cr.getData().add(new XYChart.Data(Integer.toString(liquidity.getYear()), liquidity.getCurrentRatio()));
                er.getData().add(new XYChart.Data(Integer.toString(liquidity.getYear()), liquidity.getExpectedRatio()));
                ar.getData().add(new XYChart.Data(Integer.toString(liquidity.getYear()), liquidity.getAbsoluteRatio()));

                qr.getData().add(new XYChart.Data(Integer.toString(liquidity.getYear()), liquidity.getQuickRatio()));
                crc.getData().add(new XYChart.Data(Integer.toString(liquidity.getYear()), liquidity.getCurrentRatioCoeff()));

            }

            liquidityChart.getData().add(cr);
            liquidityChart.getData().add(er);
            liquidityChart.getData().add(ar);
            liquidityChart.getData().add(qr);
            liquidityChart.getData().add(crc);

            setChartLineStyle(cr, Color.RED);
            setChartLineStyle(er, Color.GREEN);
            setChartLineStyle(ar, Color.BLUE);
            setChartLineStyle(qr, Color.BLACK);
            setChartLineStyle(crc, Color.CORAL);
        }
    }

    private void refreshPieCharts(int index) {
        if(activesList.size() == 0 || passivesList.size() == 0) {
            hideChart(activesChart, errorTextHidePiePane, true);
            hideChart(passivesChart, errorTextHidePiePane, true);
            return;
        }
        else{
            hideChart(activesChart, errorTextHidePiePane, false);
            hideChart(passivesChart, errorTextHidePiePane, false);
        }
        yearAP.getItems().clear();
        for (Apives apives : activesList) {
            if(apives.isActive())
                yearAP.getItems().add(apives.getYear());
        }
        //yearAP.setValue(activesList.get(0).getYear());

        Apives apive = activesList.get(index);
        ObservableList<PieChart.Data> pieDataActives = FXCollections.observableArrayList(
                new PieChart.Data("A1", apive.getAP1()),
                new PieChart.Data("A2", apive.getAP2()),
                new PieChart.Data("A3", apive.getAP3()),
                new PieChart.Data("A4", apive.getAP4())
        );
        activesSum.setText("Cумма всех активов: " + Float.toString(apive.getAP1() + apive.getAP2() + apive.getAP3() + apive.getAP4()));

        apive = passivesList.get(index);
        ObservableList<PieChart.Data> pieDataPassives = FXCollections.observableArrayList(
                new PieChart.Data("П1", apive.getAP1()),
                new PieChart.Data("П2", apive.getAP2()),
                new PieChart.Data("П3", apive.getAP3()),
                new PieChart.Data("П4", apive.getAP4())
        );
        passivesSum.setText("Cумма всех пассивов: " + Float.toString(apive.getAP1() + apive.getAP2() + apive.getAP3() + apive.getAP4()));

        passivesChart.getData().clear();
        activesChart.getData().clear();

        activesChart.setData(pieDataActives);
        passivesChart.setData(pieDataPassives);

    }

    private void refreshTables() {
        if (activesList != null)
            activesList.sort(Comparator.comparing(Apives::getYear));
        if (passivesList != null)
            passivesList.sort(Comparator.comparing(Apives::getYear));
        if (liquidityList != null)
            liquidityList.sort(Comparator.comparing(Liquidity::getYear));

        activesTable.getItems().clear();
        passivesTable.getItems().clear();
        liquidityTable.getItems().clear();

        activesTable.setItems(FXCollections.observableArrayList(activesList));
        passivesTable.setItems(FXCollections.observableArrayList(passivesList));
        liquidityTable.setItems(FXCollections.observableArrayList(liquidityList));
    }

    private void refreshField(){
        A1.setText(null);
        A2.setText(null);
        A3.setText(null);
        A4.setText(null);
        P1.setText(null);
        P2.setText(null);
        P3.setText(null);
        P4.setText(null);

        A1.setPromptText("");
        A2.setPromptText("");
        A3.setPromptText("");
        A4.setPromptText("");
        P1.setPromptText("");
        P2.setPromptText("");
        P3.setPromptText("");
        P4.setPromptText("");
    }


}
