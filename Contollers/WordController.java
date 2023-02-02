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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class WordController {

	@FXML
	TextField word;
	
	@FXML
	RadioButton body, topics;

	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	
	private Vector<User> users = UserGraph.createGraph(GlobalData.getData());

	public void search(ActionEvent event) throws IOException {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/GraphScene.fxml"));	
			root = loader.load();
			GraphController graphController = loader.getController();
			
			String searchResult = word.getText();
			
			if(body.isSelected()) {
				
				String text = UserGraph.postSearchWord(word.getText(),users);
				if(text.isEmpty())
					graphController.setGArea("No such word exists!!");
				else
					graphController.setGArea(text);	
				
			}
			else if(topics.isSelected()) {
				String text = UserGraph.postSearchTopic(word.getText(),users);
				if(text.isEmpty())
					graphController.setGArea("No such word exists!!");
				else
					graphController.setGArea(text);
			}
			else {
				graphController.setGArea("No Method is selected!!");
			}
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
