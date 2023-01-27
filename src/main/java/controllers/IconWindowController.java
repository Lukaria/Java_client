package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;

public class IconWindowController {
    private int icon = 9;
    private Stage stage;
    private ImageView imageView;

    public int getIcon() {
        return icon;
    }

    public void setData(Stage stage, ImageView image, int icon) {
        this.imageView = image;
        this.stage = stage;
        this.icon = icon;
    }

    @FXML
    void icon1Clicked(ActionEvent event) {
        icon = 1;
        File file = new File("src/main/resources/forms/icon1.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
        stage.close();
    }

    @FXML
    void icon2Clicked(ActionEvent event) {
        icon = 2;
        File file = new File("src/main/resources/forms/icon1.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
        stage.close();
    }

    @FXML
    void icon3Clicked(ActionEvent event) {
        icon = 3;
        File file = new File("src/main/resources/forms/icon3.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
        stage.close();
    }

    @FXML
    void icon4Clicked(ActionEvent event) {
        icon = 4;
        File file = new File("src/main/resources/forms/icon4.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
        stage.close();
    }

    @FXML
    void icon5Clicked(ActionEvent event) {
        icon = 5;
        File file = new File("src/main/resources/forms/icon5.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
        stage.close();
    }

    @FXML
    void icon6Clicked(ActionEvent event) {
        icon = 6;
        File file = new File("src/main/resources/forms/icon6.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
        stage.close();
    }

    @FXML
    void icon7Clicked(ActionEvent event) {
        icon = 7;
        File file = new File("src/main/resources/forms/icon7.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
        stage.close();
    }

    @FXML
    void icon8Clicked(ActionEvent event) {
        icon = 8;
        File file = new File("src/main/resources/forms/icon8.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
        stage.close();
    }

    @FXML
    void icon9Clicked(ActionEvent event) {
        icon = 9;
        File file = new File("src/main/resources/forms/avatar.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
        stage.close();
    }

}
