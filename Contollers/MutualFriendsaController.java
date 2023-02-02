import java.io.IOException;
import java.util.Vector;

import Graph.User;
import Graph.UserGraph;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MutualFriendsaController {

	@FXML
	TextField userid1;
	
	@FXML
	TextField userid2;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	private Vector<User> users = UserGraph.createGraph(GlobalData.getData());
	
	public void getMutualfriends(ActionEvent event) throws IOException {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/GraphScene.fxml"));	
			root = loader.load();
			GraphController graphController = loader.getController();
			
			int userid1_int = Integer.parseInt(userid1.getText());
			int userid2_int = Integer.parseInt(userid2.getText());
			
			
			String mutualFriends = UserGraph.mutualFollowers(userid1_int, userid2_int, users);
			
			if(mutualFriends.isEmpty())
				graphController.setGArea("No Mutual Friends !! ");
			else
				graphController.setGArea(mutualFriends);

			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();
		}
	
	public void back(ActionEvent event) throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GraphScene.fxml"));	
		root = loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
		}
	
	
}
