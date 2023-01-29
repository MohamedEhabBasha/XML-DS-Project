
import java.io.IOException;

import Correction.DS_Correct;
import Xml.ErrorDetection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ConsistencySceneController {

	@FXML
	TextArea areaConsistency;
	
	private Stage stage;
	private Scene scene;
	private Parent root;

	public void switchToHomeScene(ActionEvent event) throws IOException {

		String TextAreaInput = areaConsistency.getText();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomeScene.fxml"));	
		root = loader.load();
		HomeSceneController homeSceneController = loader.getController();
		homeSceneController.displayXml(TextAreaInput);
		
		//root = FXMLLoader.load(getClass().getResource("/HomeScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	public void showXml(ActionEvent event) {
		
		areaConsistency.setText(GlobalData.getData());
	}
	
	public void displayXml(String s) {
		areaConsistency.setText(s);
	}
	
	public void showErrors(ActionEvent event) {
		
		if(GlobalData.isPath) {
			
			String errors = ErrorDetection.errorByPath(GlobalData.getPath());
			if(ErrorDetection.hasError(errors)) 
				areaConsistency.setText(errors);
			else
				areaConsistency.setText("No Errors Exists!");
		}
		else {
			String errors = ErrorDetection.errorByString(GlobalData.getData());
			if(ErrorDetection.hasError(errors)) 
				areaConsistency.setText(errors);
			else
				areaConsistency.setText("No Errors Exists!");
		}
		
	}

	public void correctErrors(ActionEvent event) {
		
			if(GlobalData.isPath) {
			String errors = DS_Correct.correctXMLAPIPath(GlobalData.getPath());
			XmlFormatter format = new XmlFormatter();
			errors = format.format(errors);
			areaConsistency.setText(errors);
			
		}
		else {
			String errors = DS_Correct.correctXMLAPI(GlobalData.getData());
			XmlFormatter format = new XmlFormatter();
			errors = format.format(errors);
			areaConsistency.setText(errors);
			
		}
	}
}
