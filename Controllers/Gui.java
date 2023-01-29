
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Gui extends Application {

	public static void main(String[] args) {
		Application.launch(args);
		
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		Parent root = FXMLLoader.load(getClass().getResource("/LandingScene.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		Image icon = new Image("icon.jpg");
		stage.getIcons().add(icon);
		stage.setTitle("Data Structure Project");
		stage.setResizable(false);
		stage.show();
	}

}
