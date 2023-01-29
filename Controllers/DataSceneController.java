
import java.io.IOException;

import Compressing.HuffmanCode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class DataSceneController {
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	TextArea info;
	@FXML
	TextArea xml;
	
	HuffmanCode huffman = new HuffmanCode();
	
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
	
	public void encode() {
		String compressed =  huffman.encodeing(GlobalData.getData());
		xml.setText(compressed);
		info.setText(huffman.encodingInfo(GlobalData.getData()));
	}
	
	public void decode() {
		String decodedData = huffman.decoding(GlobalData.getData());
		xml.setText(decodedData);
	}
	
	public void minifying() {
		String minified = HuffmanCode.RemoveSpace(GlobalData.getData());
		xml.setText(minified);
	}
}
