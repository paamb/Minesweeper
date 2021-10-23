package minesweeper;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
 
public class MinesweeperApp extends Application{
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("MinesweeperGUI.fxml"));
		Parent parent = fxmlLoader.load();
		primaryStage.setTitle("Minesweeper");
		primaryStage.setScene(new Scene(parent));
		primaryStage.show();
	}
	
	public static void main(final String[] args){
		Application.launch(args);
	}
}
	