
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

public class LandingController {

	@FXML
	Label invaildChoice;
	@FXML
	RadioButton fullPath, manually;

	private Stage stage;
	private Scene scene;
	private Parent root;

	public void switchNext(ActionEvent event) throws IOException {

		if (fullPath.isSelected())
			root = FXMLLoader.load(getClass().getResource("/LocationScene.fxml"));
		else if (manually.isSelected())
			root = FXMLLoader.load(getClass().getResource("/TextArea.fxml"));
		else {
			invaildChoice.setText("YOU MUST CHOOSE ONE METHOD");
		}
		if (fullPath.isSelected() || manually.isSelected()) {
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();
		}

	}
}
