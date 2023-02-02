
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class TextAreaController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	@FXML
	TextArea area;

	public void switchToHomeScene(ActionEvent event) throws IOException {
		String textareaInput = area.getText();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomeScene.fxml"));
		root = loader.load();
		HomeSceneController homeSceneController = loader.getController();
		homeSceneController.displayXml(textareaInput);
		GlobalData.setData(textareaInput);

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

}
