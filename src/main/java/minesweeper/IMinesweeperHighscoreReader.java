package minesweeper;

import java.io.FileNotFoundException;
//import java.nio.file.Path;

//import java.io.IOException;
//import java.io.InputStream;

public interface IMinesweeperHighscoreReader{
	
	String loadHighscoreList(String filename) throws FileNotFoundException;
	
	void saveUsernameToFile(String filename, String userName, String finalTime) throws FileNotFoundException;
	String getFilePath(String filename);
}
