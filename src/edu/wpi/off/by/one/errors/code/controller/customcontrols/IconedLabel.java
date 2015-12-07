package edu.wpi.off.by.one.errors.code.controller.customcontrols;

import java.io.IOException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 * Created by jules on 11/30/2015.
 */
public class IconedLabel extends GridPane {
    //region FXML attributes
    @FXML private Label label;

    @FXML private ImageView image;
    //endregion

    public StringProperty imageURLProperty = new SimpleStringProperty();

    public IconedLabel() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/customcontrols/IconedLabel.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException excpt) {
            throw new RuntimeException(excpt);
        }
    }

    public StringProperty textProperty(){
        return label.textProperty();
    }

    public String getText(){
        return textProperty().get();
    }

    public void setText(String text){
        textProperty().set(text);
    }

    public ObjectProperty<Image> imageProperty(){
        return image.imageProperty();
    }

    public Image getImage(){
        return imageProperty().get();
    }

    public void setImage(String url){
        imageProperty().set(new Image(url));
    }
}
