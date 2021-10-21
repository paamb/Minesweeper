package minesweeper;
import javafx.application.Application;

//import javafx.scene.Node;
//import javafx.scene.layout.GridPane;

//import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
 
public class MinesweeperApp extends Application{
	public void start(Stage primaryStage) throws Exception {
		Parent parent = FXMLLoader.load(getClass().getResource("MinesweeperGUI.fxml"));
		primaryStage.setTitle("Minesweeper");
		primaryStage.setScene(new Scene(parent));
		primaryStage.show();
	}
	
	public static void main(final String[] args){
		Application.launch(args);
	}
}
	