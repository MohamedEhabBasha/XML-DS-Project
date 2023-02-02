
import java.io.IOException;

import Xml.ToJSON;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class HomeSceneController {

	@FXML
	TextArea xml;
	TextArea info;
	
	private Stage stage;
	private Scene scene;
	private Parent root;

	public void makeGraph(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("/GraphScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	public void format(ActionEvent event) {
		XmlFormatter formater = new XmlFormatter();
		String unFormatted = xml.getText();
		String formatted = formater.format(unFormatted);
		xml.setText(formatted);

	}

	public void switchToConsistencyScene(ActionEvent event) throws IOException {

		String TextAreaInput = xml.getText();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ConsistencyScene.fxml"));
		root = loader.load();
		ConsistencySceneController consistencySceneController = loader.getController();
		consistencySceneController.displayXml(TextAreaInput);

		// root = FXMLLoader.load(getClass().getResource("/HomeScene.fxml"));
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

	public void switchToJsonScene(ActionEvent event) throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/JsonScene.fxml"));
		root = loader.load();
		JsonSceneController jsonSceneController = loader.getController();
		
		String json = ToJSON.stringJson(GlobalData.getData());
		jsonSceneController.displayJson(json);

		// root = FXMLLoader.load(getClass().getResource("/HomeScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
		
		
		
	}

	public void switchToDataScene(ActionEvent event) throws IOException {

		//GlobalData.setData(xml.getText());

		root = FXMLLoader.load(getClass().getResource("/DataScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	public void displayXml(String input) {
		xml.setText(input);
	}

}
