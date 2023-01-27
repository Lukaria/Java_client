package controllers;

import Main.*;
import client.ClientHandler;

import controllers.Usability.Methods;
import entity.*;
import enums.Commands;

import javafx.collections.FXCollections;
import javafx.scene.chart.XYChart;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;

import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class ProfitWindowController implements Methods {

    @FXML
    private TableColumn<Profit, Float> ROAColumn;

    @FXML
    private TableColumn<Profit, Float> ROEColumn;

    @FXML
    private TableColumn<Profit, Float> ROIColumn;

    @FXML
    private TableColumn<Profit, Float> ROSColumn;

    @FXML
    private TextField RWVAT;

    @FXML
    private TableColumn<Capital, Float> RWVATColumn;

    @FXML
    private TextField actives;

    @FXML
    private TableColumn<Capital, Float> activesColumn;

    @FXML
    private Button capitalButton;

    @FXML
    private TableView<Capital> capitalTable;

    @FXML
    private Text errortextCapital;

    @FXML
    private Text errortextCapital1;

    @FXML
    private TextField investedFunds;

    @FXML
    private TableColumn<Capital, Float> investedFundsColumn;

    @FXML
    private Text errorTextHidePane;

    @FXML
    private Button liquidityButton;

    @FXML
    private TextField netProfit;

    @FXML
    private TableColumn<Capital, Float> netProfitColumn;

    @FXML
    private TextField netWorth;

    @FXML
    private TableColumn<Capital, Float> netWorthColumn;

    @FXML
    private Button profitButton;

    @FXML
    private LineChart<Integer, Float> profitChart;

    @FXML
    private TableView<Profit> profitTable;

    @FXML
    private TableColumn<Profit, Integer> profitYearColumn;

    @FXML
    private ToolBar toolbar;

    @FXML
    private Button toolbarAccount;

    @FXML
    private ChoiceBox<String> toolbarCompanies;

    @FXML
    private Button toolbarInfo;

    @FXML
    private Button editCapitalButton;

    @FXML
    private Button deleteCapitalButton;

    @FXML
    private Label toolbarUsername;

    @FXML
    private ChoiceBox<Integer> year;

    @FXML
    private TableColumn<Capital, Float> yearColumn;

    private User thisUser;

    private ClientHandler connection = ClientHandler.getInstance();

    private List<Capital> capitalList = new ArrayList<>();
    private List<Profit> profitList = new LinkedList<>();
    private int lastSelected = 0;

    int capitalId = 0;

    public ProfitWindowController() throws IOException {
    }

    private void EditEvent(Capital selectedCapital) {

        if (selectedCapital != null) {
            lastSelected = selectedCapital.getYear();
            capitalButton.setVisible(false);
            capitalButton.setDisable(true);

            editCapitalButton.setVisible(true);
            editCapitalButton.setDisable(false);
            deleteCapitalButton.setVisible(true);
            deleteCapitalButton.setDisable(false);

            capitalId = capitalTable.getSelectionModel().getSelectedIndex();
            year.setValue(selectedCapital.getYear());
            netProfit.setText(Float.toString(selectedCapital.getNetProfit()));
            RWVAT.setText(Float.toString(selectedCapital.getRWVAT()));
            actives.setText(Float.toString(selectedCapital.getActives()));
            netWorth.setText(Float.toString(selectedCapital.getNetWorth()));
            investedFunds.setText(Float.toString(selectedCapital.getInvestedFunds()));
            year.setDisable(true);
        } else {
            capitalButton.setVisible(true);
            capitalButton.setDisable(false);

            editCapitalButton.setVisible(false);
            editCapitalButton.setDisable(true);
            deleteCapitalButton.setVisible(false);
            deleteCapitalButton.setDisable(true);

            year.setValue(null);
            netProfit.setText(null);
            RWVAT.setText(null);
            actives.setText(null);
            netWorth.setText(null);
            investedFunds.setText(null);
            year.setDisable(false);
        }
    }

    @FXML
    private void initialize() {
        //EditEvent(null);
        ObservableList<Integer> years = FXCollections.observableArrayList();
        for (int i = 2000; i <= 2023; i++) {
            years.add(i);
        }
        hideChart(profitChart, errorTextHidePane, true);

        year.setItems(years);

        yearColumn.setCellValueFactory(new PropertyValueFactory<Capital, Float>("year"));
        netProfitColumn.setCellValueFactory(new PropertyValueFactory<Capital, Float>("netProfit"));
        RWVATColumn.setCellValueFactory(new PropertyValueFactory<Capital, Float>("RWVAT"));
        netWorthColumn.setCellValueFactory(new PropertyValueFactory<Capital, Float>("netWorth"));
        activesColumn.setCellValueFactory(new PropertyValueFactory<Capital, Float>("actives"));
        investedFundsColumn.setCellValueFactory(new PropertyValueFactory<Capital, Float>("investedFunds"));

        ROAColumn.setCellValueFactory(new PropertyValueFactory<Profit, Float>("ROA"));
        ROEColumn.setCellValueFactory(new PropertyValueFactory<Profit, Float>("ROE"));
        ROSColumn.setCellValueFactory(new PropertyValueFactory<Profit, Float>("ROS"));
        ROIColumn.setCellValueFactory(new PropertyValueFactory<Profit, Float>("ROI"));
        profitYearColumn.setCellValueFactory(new PropertyValueFactory<Profit, Integer>("year"));

        capitalTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if(newValue == null){
                        lastSelected = 0;
                        return;
                    }
                    if(lastSelected != newValue.getYear())
                        EditEvent(newValue);
                });
    }

    public void setData(User user) {
        thisUser = user;

        toolbarUsername.setText("| " + user.getName());
        toolbarUsername.setStyle("-fx-font-size: 24px");
        buttonChoosed(profitButton);

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
                if (company.getName() == name) {
                    break;
                }
                index++;
            }
            capitalList = null;
            profitList = null;
            thisUser.setCurrentCompany(index);
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
        });

        connection.sendObject(user.getCompanies().get(user.getCurrenctCompany()), Commands.PROFIT);
        List<TransferObject> toList = connection.getList();
        for(TransferObject transferObject: toList){
            capitalList.add((Capital) transferObject.getObject());
        }
        toList = connection.getList();
        for(TransferObject transferObject: toList){
            profitList.add((Profit) transferObject.getObject());
        }

        refreshChart();
        refreshTables();
    }

    @FXML
    void AccountButtonClicked(ActionEvent event) {
        try {
            Stage stage = Main.getPrimaryStage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/forms/UserWindow.fxml"));
            Parent root = loader.load();

            UserWindowController ucw = loader.getController();
            ucw.setData(thisUser);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
    void CapitalButtonClicked(ActionEvent event) {

        boolean flag = false;
        errortextCapital.setVisible(false);
        Capital capital = new Capital();
        if (year.getValue() == null) {
            flag = true;
        }
        if (netWorth.getText().length() == 0) {
            flag = true;
        }
        if (netProfit.getText().length() == 0) {
            flag = true;
        }
        if (RWVAT.getText().length() == 0) {
            flag = true;
        }
        if (investedFunds.getText().length() == 0) {
            flag = true;
        }
        if (actives.getText().length() == 0) {
            flag = true;
        }

        if (flag) {
            errorText(errortextCapital, "Поля не могут быть пустыми!", "-fx-text-fill: red; -fx-font-size: 12px;");
            return;
        }
        Float networth, investedfunds, rWVAT, netprofit, active;
        try {
            networth = Float.parseFloat(netWorth.getText());
            investedfunds = Float.parseFloat(investedFunds.getText());
            rWVAT = Float.parseFloat(RWVAT.getText());
            netprofit = Float.parseFloat(netProfit.getText());
            active = Float.parseFloat(actives.getText());
            if (networth < 0.0 || investedfunds < 0.0 || rWVAT < 0.0 || netprofit < 0.0) {
                errorText(errortextCapital, "Значение полей не может быть меньше нуля!", "-fx-text-fill: red; -fx-font-size: 12px;");
                return;
            }
        } catch (NumberFormatException ex) {
            errorText(errortextCapital, "Поля должны содержать числа!", "-fx-text-fill: red; -fx-font-size: 12px;");
            return;
        }
        capital.setNetProfit(netprofit);
        capital.setRWVAT(rWVAT);
        capital.setYear(year.getValue());
        capital.setInvestedFunds(investedfunds);
        capital.setNetWorth(networth);
        capital.setActives(active);


        for (Capital compare : capitalList) {
            if (capital.getYear() == compare.getYear()) {
                errorText(errortextCapital, "Поле с данной датой уже существует!", "-fx-text-fill: red; -fx-font-size: 12px;");
                return;
            }
        }


        Company companyObj = thisUser.getCompanies().get(thisUser.getCurrenctCompany());
        connection.sendObject(companyObj, Commands.CAPITALS);
        connection.sendObject(capital, Commands.CAPITALS);

        TransferObject profit = connection.getObject();


        capitalList.add(capital);
        profitList.add((Profit) profit.getObject());


        capitalTable.setItems(FXCollections.observableArrayList(capitalList));
        profitTable.setItems(FXCollections.observableArrayList(profitList));

        refreshChart();
        refreshTables();
    }

    @FXML
    void EditCapitalButtonClicked(ActionEvent event) {

        boolean flag = false;
        errortextCapital.setVisible(false);
        Capital capital = new Capital();
        if (year.getValue() == null) {
            flag = true;
        }
        if (netWorth.getText().length() == 0) {
            flag = true;
        }
        if (netProfit.getText().length() == 0) {
            flag = true;
        }
        if (RWVAT.getText().length() == 0) {
            flag = true;
        }
        if (investedFunds.getText().length() == 0) {
            flag = true;
        }
        if (actives.getText().length() == 0) {
            flag = true;
        }

        if (flag) {
            errorText(errortextCapital, "Поля не могут быть пустыми!", "-fx-text-fill: red; -fx-font-size: 12px;");
        }
        Float networth, investedfunds, rWVAT, netprofit, active;
        try {
            networth = Float.parseFloat(netWorth.getText());
            investedfunds = Float.parseFloat(investedFunds.getText());
            rWVAT = Float.parseFloat(RWVAT.getText());
            netprofit = Float.parseFloat(netProfit.getText());
            active = Float.parseFloat(actives.getText());
            if (networth < 0.0 || investedfunds < 0.0 || rWVAT < 0.0 || netprofit < 0.0) {
                errorText(errortextCapital, "Значение полей не может быть меньше нуля!", "-fx-text-fill: red; -fx-font-size: 12px;");
                return;
            }
        } catch (NumberFormatException ex) {
            errorText(errortextCapital, "Поля должны содержать числа!", "-fx-text-fill: red; -fx-font-size: 12px;");
            return;
        }
        capital.setNetProfit(netprofit);
        capital.setRWVAT(rWVAT);
        capital.setYear(year.getValue());
        capital.setInvestedFunds(investedfunds);
        capital.setNetWorth(networth);
        capital.setActives(active);

        for (Capital compare : capitalList) {
            if (capital.getYear() == compare.getYear() && capital.getYear() != year.getValue()) {
                errorText(errortextCapital, "Поле с данной датой уже существует!", "-fx-text-fill: red; -fx-font-size: 12px;");
                return;
            }
        }

        connection.sendObject(capital, Commands.CAPITALS_UPDATE);
        connection.sendObject(thisUser.getCompanies().get(thisUser.getCurrenctCompany()), Commands.CAPITALS_UPDATE);


        for (int i = 0; i < capitalList.size(); i++) {
            if (capital.getYear() == capitalList.get(i).getYear()) {
                capitalList.set(i, capital);
                break;
            }
        }

        TransferObject profit = connection.getObject();
        Profit prft = (Profit) profit.getObject();

        for (int i = 0; i < profitList.size(); i++) {
            if (prft.getYear() == profitList.get(i).getYear()) {
                profitList.set(i, prft);
                break;
            }
        }

        EditEvent(null);

        refreshChart();
        refreshTables();
    }

    @FXML
    void DeleteCapitalButtonClicked(ActionEvent event) {
        Capital capital = new Capital();
        capital.setYear(lastSelected);

        for (int i = 0; i < capitalList.size(); i++) {
            if (capitalList.get(i).getYear() == capital.getYear()) {
                capital = capitalList.get(i);
                break;
            }
        }
        connection.sendObject(capital, Commands.DELETE);
        capitalList.remove(capital);

        Profit profit = new Profit();
        profit.setYear(lastSelected);
        for (int i = 0; i < profitList.size(); i++) {
            if (profitList.get(i).getYear() == profit.getYear()) {
               profit = profitList.get(i);
                break;
            }
        }
        connection.sendObject(profit, Commands.DELETE);
        profitList.remove(profit);


        EditEvent(null);

        refreshChart();
        refreshTables();


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

    }

    void refreshTables() {
        if (capitalList != null) {
            capitalList.sort(Comparator.comparing(Capital::getYear));
            capitalTable.setItems(FXCollections.observableArrayList(capitalList));
        }
        if (profitList != null) {
            profitList.sort(Comparator.comparing(Profit::getYear));
            profitTable.setItems(FXCollections.observableArrayList(profitList));
        }
    }

    void refreshChart() {
        profitChart.getData().clear();
        hideChart(profitChart, errorTextHidePane, true);
        if (profitList != null && profitList.size() > 1) {
            boolean flag = false;
            Collections.sort(profitList, new Comparator<Profit>() {
                public int compare(Profit o1, Profit o2) {
                    return o1.getYear() - o2.getYear();
                }
            });
            for (int i = 0; i < profitList.size() - 1; i++) {
                if (profitList.get(i).getYear() + 1 != profitList.get(i + 1).getYear()) {
                    flag = true;
                    break;
                }
            }
            if (flag) return;

            hideChart(profitChart, errorTextHidePane, false);

            Series roa = new Series();
            roa.setName("Рентабельность активов");

            Series roe = new Series();
            roe.setName("Рентабельность собственного капитала");


            Series roi = new Series();
            roi.setName("Рентабельность инвестиций");

            Series ros = new Series();
            ros.setName("Рентабельность продаж");

            for (Profit profit : profitList) {
                roi.getData().add(new XYChart.Data(Integer.toString(profit.getYear()), profit.getROI()));
                ros.getData().add(new XYChart.Data(Integer.toString(profit.getYear()), profit.getROS()));
                roe.getData().add(new XYChart.Data(Integer.toString(profit.getYear()), profit.getROE()));
                roa.getData().add(new XYChart.Data(Integer.toString(profit.getYear()), profit.getROA()));
            }


            profitChart.getData().add(roa);
            profitChart.getData().add(roi);
            profitChart.getData().add(roe);
            profitChart.getData().add(ros);

            setChartLineStyle(roi, Color.RED);
            setChartLineStyle(roa, Color.GREEN);
            setChartLineStyle(ros, Color.BLUE);
            setChartLineStyle(roe, Color.BLACK);


        }

    }


}
