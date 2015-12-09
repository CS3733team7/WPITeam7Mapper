package edu.wpi.off.by.one.errors.code.controller.customcontrols;

import java.io.IOException;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

/**
 * Created by jules on 11/30/2015.
 */
public class IconedLabel extends BorderPane {
    //region FXML attributes
    @FXML private Label label;

    @FXML private ImageView imageView;
    //endregion

    //region Constructor/s
    public IconedLabel() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/customcontrols/IconedLabel.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException excpt) {
            throw new RuntimeException(excpt);
        }

        imageView.fitWidthProperty().bind(imageView.fitHeightProperty());

    }
    //endregion

    //region Getter and Setters
    public StringProperty textProperty(){
        return label.textProperty();
    }

    public String getText(){
        return textProperty().get();
    }

    public void setText(String text){
        textProperty().set(text);
    }
    //endregion

    public ObjectProperty<Image> imageProperty(){
        return imageView.imageProperty();
    }

    public Image getImage(){
        return imageProperty().get();
    }

    public void setImage(String image){
        imageProperty().set(new Image(image));
    }

    @FXML
    public DoubleProperty imageWidthProperty(){
        return imageView.fitWidthProperty();
    }
    @FXML
    public double getImageWidth(){
        return imageWidthProperty().get();
    }
    @FXML
    public void setImageWidth(Number imageWidth){
        imageWidthProperty().setValue(imageWidth);
    }
}
