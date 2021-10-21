package minesweeper;

import java.io.FileNotFoundException;

import java.io.IOException;
//import java.nio.file.Path;

public interface IMinesweeperGameStateReader {

	Game load(String filename) throws FileNotFoundException;
	
	void save(Game game, String filename) throws IOException;
	String getFilePath(String filename);
}
