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
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class GraphController {
	
	@FXML
	TextArea gArea;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	private Vector<User> users = UserGraph.createGraph(GlobalData.getData());
	
	public void showMostInfluencer() {

		String text = UserGraph.showMostInfluencer(users);
		
		gArea.setText(text);
	}
	
	public void showMostActiveUser() {
		UserGraph.createFollowing(users);
		String text = UserGraph.showMostActive(users);
		System.out.println(text);
		gArea.setText(text);
	}

	public void mutaualFriendsBetweenUsers(ActionEvent event) throws IOException {
			
		root = FXMLLoader.load(getClass().getResource("/MutualFriendsScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
		}
	
	public void suggestionListForEachUser() {
		
		String text = UserGraph.createLists(users);
		
		gArea.setText(text);
	}
	
	public void searchForWord(ActionEvent event) throws IOException {
		
		root = FXMLLoader.load(getClass().getResource("/WordScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}
	
	public void setGArea(String text) {
		gArea.setText(text);
	}
	
public void switchToHomeScene(ActionEvent event) throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomeScene.fxml"));	
		root = loader.load();
		HomeSceneController homeSceneController = loader.getController();
		homeSceneController.displayXml(GlobalData.getData());
		
		//root = FXMLLoader.load(getClass().getResource("/HomeScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

}
