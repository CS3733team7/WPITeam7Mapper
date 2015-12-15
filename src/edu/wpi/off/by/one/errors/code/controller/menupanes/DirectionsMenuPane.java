package edu.wpi.off.by.one.errors.code.controller.menupanes;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import edu.wpi.off.by.one.errors.code.controller.customcontrols.AutoCompleteNameTextField;
import edu.wpi.off.by.one.errors.code.controller.customcontrols.AutoCompleteTextField;
import edu.wpi.off.by.one.errors.code.controller.customcontrols.ClearableTextField;
import edu.wpi.off.by.one.errors.code.controller.ControllerSingleton;
import edu.wpi.off.by.one.errors.code.controller.MapRootPane;
import edu.wpi.off.by.one.errors.code.model.GoogleMail;
import edu.wpi.off.by.one.errors.code.model.Id;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import edu.wpi.off.by.one.errors.code.model.Node;
import edu.wpi.off.by.one.errors.code.model.TagMap;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;

/**
 * Created by jules on 11/28/2015.
 */
public class DirectionsMenuPane extends BorderPane {
	@FXML private AutoCompleteNameTextField originTextField;
    @FXML private AutoCompleteNameTextField destinationTextField;
	@FXML Button routeButton;
	@FXML Button foodButton;
	@FXML Button mensRoomButton;
	@FXML Button womensRoomButton;
	@FXML Button genderNeutralRestroomButton;
    @FXML private ListView<String> directionsListView;
    @FXML CheckBox accessibleCheckbox;
    @FXML Button emailButton;
    @FXML Button smsButton;
    @FXML ComboBox carrierChoiceBox;

    private SettingsMenuPane settingsMenuPane;
    Node originNode;
    Node destinationNode;
	
    public DirectionsMenuPane(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/menupanes/DirectionsMenuPane.fxml"));

        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
            setListeners();
        }catch(IOException excpt){
            throw new RuntimeException(excpt);
        }
        settingsMenuPane = ControllerSingleton.getInstance().getSettingsMenuPane();
        this.getStylesheets().add(getClass().getResource("../../resources/stylesheets/menupanes/DirectionsPaneStyleSheet.css").toExternalForm());
    }

	private void setListeners(){ 
		this.routeButton.setOnAction(e -> {
			setDirectionsFromNode();
			setDirectionsToNode();
			ControllerSingleton.getInstance().getMapRootPane().placeStartMarker(originNode);
			ControllerSingleton.getInstance().getMapRootPane().drawPath(originNode.getId(), destinationNode.getId());
		});
		this.foodButton.setOnAction(e-> {
			//ControllerSingleton.getInstance().getMapRootPane().placeMarker(originNode);
			ControllerSingleton.getInstance().getMapRootPane().drawFoodPath();
		});
		this.mensRoomButton.setOnAction(e-> {
			//ControllerSingleton.getInstance().getMapRootPane().placeMarker(originNode);
			ControllerSingleton.getInstance().getMapRootPane().drawMensRoomPath();
		});
		this.womensRoomButton.setOnAction(e-> {
			//ControllerSingleton.getInstance().getMapRootPane().placeMarker(originNode);
			ControllerSingleton.getInstance().getMapRootPane().drawWomensRoomPath();
		});
		this.genderNeutralRestroomButton.setOnAction(e-> {
			//ControllerSingleton.getInstance().getMapRootPane().placeMarker(originNode);
			ControllerSingleton.getInstance().getMapRootPane().drawGenderNeutralRestroomPath();
		});
	}
	
	@FXML private void selectAccessible(){
		ControllerSingleton.getInstance().getMapRootPane().isAccessibleMode = accessibleCheckbox.isSelected() ? true : false;
		System.out.println("Accessibility: " + ControllerSingleton.getInstance().getMapRootPane().isAccessibleMode);
	}
	
	public Node getDirectionsToNode() { return null; }
	public Node getDirectionsFromNode() { return null; }
	
	public void setDirectionsToNode() {
		Id toId = TagMap.getTagMap().findName(destinationTextField.getText());
		destinationNode = ControllerSingleton.getInstance().getMapRootPane().getDisplay().getGraph().returnNodeById(toId);
	}
	
	public void setDirectionsFromNode(){
		Id fromId = TagMap.getTagMap().findName(originTextField.getText());
		originNode = ControllerSingleton.getInstance().getMapRootPane().getDisplay().getGraph().returnNodeById(fromId);
	}
	
	public void setDirectionsToNode(Node n){
		destinationTextField.setText(n.getName());
		destinationNode = n;
		//TODO Should take an input (String? NodeDisplay? Node?)
		//Put String name in the To box
		//set directionsTo variable (NOT MADE YET)
	}
	
	public void setDirectionsFromNode(Node n){
		originTextField.setText(n.getName());
		originNode = n;
		//TODO Should take an input (String? NodeDisplay? Node?)
		//Put String name in the From box
		//set directionsFrom variable (NOT MADE YET)
	}

    public ListView<String> getdirectionsListView(){
        return this.directionsListView;
    }

    @FXML private void onSwitchDirectionsButtonClick(){
        String originContent = originTextField.getText();
        originTextField.setText(destinationTextField.getText());
        destinationTextField.setText(originContent);
        Node buffer = originNode;
        originNode = destinationNode;
        destinationNode = buffer;
    }

    public void saveAsPng() {
    	MapRootPane mrp = new MapRootPane();
        WritableImage image = mrp.snapshot(new SnapshotParameters(), null);

        // TODO: probably use a file chooser here
        File file = new File("screenshot.png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            System.out.println("shoted");
        } catch (IOException e) {
            // TODO: handle exception here
        }
    }
    
    @FXML private void onEmailButtonClick(){
        settingsMenuPane = ControllerSingleton.getInstance().getSettingsMenuPane();
        String userEmail = settingsMenuPane.getUserEmail();
        List<String> directions = ControllerSingleton.getInstance().getMapRootPane().getPath().getTextual();
        String userName = settingsMenuPane.getUserName();
        String body = "Hello "+ userName + "!\n";
        for (String s : directions){
            body += (s + "\n");
        }
        saveAsPng();
        GoogleMail googleMail = new GoogleMail();
        googleMail.send(userEmail, "Directions from goatThere()", body);
    }
    public void disableEmailButton(boolean b){
        emailButton.setDisable(b);
    }
    public void disableSMSButton(boolean b){
    	smsButton.setDisable(b);
    }
    
    @FXML private void onSMSButtonClick(){

        settingsMenuPane = ControllerSingleton.getInstance().getSettingsMenuPane();
        String userName = settingsMenuPane.getUserName();

        List<String> directions = ControllerSingleton.getInstance().getMapRootPane().getPath().getTextual();

        String body = "Hello "+ userName + "!\n";

        for (String s : directions){
            body += (s + "\n");   
        }

        int numMessage = body.length() / 300;
        char[] message = new char[301];
        String output = "";
        int count = 0;

        for (int i=0; i< numMessage; i++){   
	        body.getChars(count, count+300, message, 0);
	        output = new String(message);
	        sendMessage(output);
	        count = count + 300;
        }

        if (body.length() % 300 != 0){
	        body.getChars(count, body.length(), message, 0);
	        output = new String(message);
	        sendMessage(output);
        }      

        //cannot place starting point on 4th level        

    }

    

    public void sendMessage(String body){

    String userNumber = settingsMenuPane.getUserNumber();
    	
    	GoogleMail googleMail = new GoogleMail();

    	String carrier =settingsMenuPane.getCarrier();
    	
    	String user;
    	System.out.println(carrier);
    	switch(carrier){
    		case "AT&T Wireless":
    			user = userNumber + "@txt.att.net";
    			break;
    		case "Alltel Wireless":
    			user = userNumber + "@text.wireless.alltel.com";
    			break;
    		case "AT&T Mobility":
    			user = userNumber + "@cingularme.com";
    			break;
    		case "Boost Mobile":
    			user = userNumber + "@myboostmobile.com";
    			break;
    		case "Cricket":
    			user = userNumber + "@sms.mycricket.com";
    			break;
    		case "Metro PCS":
    			user = userNumber + "@mymetropcs.com";
    			break;
    		case "Sprint PCS":
    			user = userNumber + "@messaging.sprintpcs.com";
    			break;
    		case "Sprint Nextel":
    			user = userNumber + "@page.nextel.com";
    			break;
    		case "Straight Talk":
    			user = userNumber + "@vtext.com";
    			break;
    		case "T-Mobile":
    			user = userNumber + "@tmomail.net";
    			break;
    		case "U.S. Cellular":
    			user = userNumber + "@email.uscc.net";
    			break;
    		case "Verizon":
    			user = userNumber + "@vtext.com";
    			break;
    		case "Virgin Mobile":
    			user = userNumber + "@vmobl.com";
    			break;
    		default:
    			user = userNumber + "@vtext.com";
    			break;
    			
    	}

    	System.out.println(user);
        googleMail.send(user, "Directions from goatThere()", body);


    }


}
