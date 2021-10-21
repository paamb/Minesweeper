package minesweeper;

import java.io.File;

import java.io.FileNotFoundException;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;


public class GameStateManager implements IMinesweeperGameStateReader{
	/**
	 * @return Path of package for saving gamestate
	 */
	private Path getDirPath() {
		return Path.of(System.getProperty("user.dir") + "/src/main/java/minesweeper/gamestate");
	}
	/*
	 * Makes it public so its possible to test the filepath
	 * @return file path in stringformat
	 */
	public String getFilePath(String filename) {
		return getDirPath().toString() + "/" + filename;
	}
	/**
	 * Creates directory to the path if its not existing
	 */
	private void createDirIfNotExisting(){
		if (!Files.exists(getDirPath())) {
			try {
				Files.createDirectory(getDirPath());
			}
			catch(IOException e) {
				System.out.println("Could not get path to directory");
				System.out.println(e);
			}
		}
	}
	/*
	 * Filename can only contain letters and numbers
	 */
	private boolean legalFilename(String filename) {
		return filename.matches("[a-zA-Z0-9]*");
	}
	/**
	 * Loads the game based on filename
	 * @param filename from use
	 * @return Empty list if file is not found. If file is found it returns the gamemode and
	 * @throws FileNotFoundException
	 */
	public Game load(String filename) throws FileNotFoundException{
		String filePath = getFilePath(filename);
		
		Game loadGame = null;
		String gamemode = null;
		String[][] boardArray = null;
		int lineCounter = 0;
		
		try (Scanner scanner = new Scanner(new File(filePath))) {
			if (scanner.hasNextLine()) {
				gamemode = scanner.nextLine();
			}
			while (scanner.hasNextLine()) {
				String[] line = scanner.nextLine().split("");
				if (boardArray == null) {
					boardArray = new String[line.length][line.length];
				}
				boardArray[lineCounter] = line;
				lineCounter++;
			}
			loadGame = new Game(boardArray,gamemode);
			return loadGame;
		}
	}

	/**
	 * Saves the game to a file. Overwrites the file if its already existing. 'B' = bomb, 'E' = Open emptytile, ' ' = Unopened tile, '1,2...' = Number tile  
	 * @param The running game, filname from reader
	 * @throws IOException 
	 */
	public void save(Game game, String filename) throws IOException{
		createDirIfNotExisting();
		if(!legalFilename(filename)) {
			throw new IllegalArgumentException();
		}
		String filePath = getFilePath(filename);
		try (PrintWriter writer = new PrintWriter(filePath)){
			writer.println(game.getGameMode());
			writer.append(game.toString());
		}
	}
}
