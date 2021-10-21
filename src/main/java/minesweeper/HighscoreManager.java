package minesweeper;

import java.io.File;


//import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
//import java.io.FileWriter;
import java.io.IOException;
//import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class HighscoreManager implements IMinesweeperHighscoreReader {

	/**
	 * @return Path of package for saving gamestate
	 */
	private Path getDirPath() {
		return Path.of(System.getProperty("user.dir") + "/src/main/java/minesweeper/highscores");
	}
	/*
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
			} catch (IOException e) {
				System.out.println("Could not get path to directory");
				System.out.println(e);
			}
		}
	}

	/*
	 * Loads the 5 best highscores
	 * @return string of the 5 best highscores
	 * @throws FileNotFoundException
	 */
	public String loadHighscoreList(String gameMode) throws FileNotFoundException {
		ArrayList<String> highscoreList = new ArrayList<>();
		String path = getFilePath(gameMode);
		Scanner scanner = new Scanner(new File(path));
		String highscoreString = "";

		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] user = line.split(" ", 2);
			String timeString = Time.flattenTime(user[1]);
			String element = timeString + user[0];

			highscoreList.add(element);

		}
		// Sorts on time and name (0000AAAA)
		Collections.sort(highscoreList);

		// Puts the best usernames to a string and returns
		for (int i = 0; i < highscoreList.size(); i++) {
			String iBestUser = highscoreList.get(i);
			String iBestUserHighscoreListDisplay = iBestUser.substring(4,iBestUser.length()) + " " + iBestUser.substring(0,2) + ":" + iBestUser.substring(2,4);
			highscoreString += iBestUserHighscoreListDisplay + "\n";
			
			//We only want the 5 best users on our highscore list. Will break if there are more than 5 users in on the highscorelist
			if (i == 4) {
				break;
			}
		}
		scanner.close();
		return highscoreString;
	}

	/*
	 * Saves username to file
	 * @throws FileNotFoundException if file is not found.
	 */
	public void saveUsernameToFile(String gameMode, String username, String finalTime) throws FileNotFoundException{
		createDirIfNotExisting();
		
		String path = getFilePath(gameMode);
		File textFile = new File(path);

		PrintWriter writer = null;
		if (textFile.exists() && !textFile.isDirectory()) {
			writer = new PrintWriter(new FileOutputStream(new File(path), true));
		} else {
			writer = new PrintWriter(path);
		}
		writer.append(username + " " + finalTime + "\n");
		writer.flush();
		writer.close();
	}

}
