

import java.io.IOException;

import Xml.ErrorDetection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LocationController {

	@FXML
	TextField fullPathInput;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public void switchToHomeScene(ActionEvent event) throws IOException {
		String textFiledInput = fullPathInput.getText();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomeScene.fxml"));	
		root = loader.load();
		HomeSceneController homeSceneController = loader.getController();
		GlobalData.setPath(textFiledInput);
		GlobalData.isPath = true;
		GlobalData.setData(ErrorDetection.readFile(textFiledInput));
		homeSceneController.displayXml(GlobalData.getData());
		
		//root = FXMLLoader.load(getClass().getResource("/HomeScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}
	
	public void switchToLandingScene(ActionEvent event) throws IOException {	
		
		root = FXMLLoader.load(getClass().getResource("/LandingScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}
	
}
