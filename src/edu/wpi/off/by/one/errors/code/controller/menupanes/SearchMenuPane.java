package edu.wpi.off.by.one.errors.code.controller.menupanes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.wpi.off.by.one.errors.code.controller.MainPane;
import edu.wpi.off.by.one.errors.code.controller.customcontrols.AutoCompleteTextField;

/**
 * Created by jules on 11/28/2015.
 */
public class SearchMenuPane extends BorderPane {
	
	@FXML AutoCompleteTextField searchField;
	
    public SearchMenuPane(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/menupanes/SearchMenuPane.fxml"));

        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
            
        }catch(IOException excpt){
            throw new RuntimeException(excpt);
        }
        
        SortedSet<String> entries = new TreeSet<String>();
        ;
    }

	public void setMainPane(MainPane mainPane) {
		// TODO Auto-generated method stub
		
	}
}
