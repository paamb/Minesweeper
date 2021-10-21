package minesweeper;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class HighscoreManagerTest {
	private String highscoreList;
	private IMinesweeperHighscoreReader manager;

	@BeforeEach
	void setUp() throws Exception {
		manager = new HighscoreManager();
	}
	void makeEasyTestFile() {
		try {
			manager.saveUsernameToFile("easyTest", "user1", "0:11");
		} catch (FileNotFoundException e) {
			fail("Could not save username to file");
		}
		try {
			manager.saveUsernameToFile("easyTest", "user2", "99:99");
		} catch (FileNotFoundException e) {
			fail("Could not save username to file");
		}
		try {
			manager.saveUsernameToFile("easyTest", "user3", "0:10");
		} catch (FileNotFoundException e) {
			fail("Could not save username to file");
		}
		try {
			manager.saveUsernameToFile("easyTest", "user4", "0:11");
		} catch (FileNotFoundException e) {
			fail("Could not save username to file");
		}
		try {
			manager.saveUsernameToFile("easyTest", "user5", "0:09");
		} catch (FileNotFoundException e) {
			fail("Could not save username to file");
		}
	}
	@Test
	void testLoadHighscoreList() {
		//Checks what happens when loading from a file that not exists
		assertThrows(FileNotFoundException.class, () -> manager.loadHighscoreList("wrongLoadName"),
				"Cannot load from a file that is not saved");
		
		//Makes the easy-test file
		makeEasyTestFile();
		
		//Loads from file made above
		try {
			highscoreList = manager.loadHighscoreList("easyTest");
		} catch (FileNotFoundException e) {
			fail("Could not load easyTest file");
		}
		
		//The returnvalue of loading highscorelist should be the 5 best highscores in order
		String [] usernameArray1 = highscoreList.split("\n");
		assertEquals(usernameArray1[0], "user5 00:09", "The username string is either in wrong format or wrong posistion");
		assertEquals(usernameArray1[1], "user3 00:10", "The username string is either in wrong format or wrong posistion");
		assertEquals(usernameArray1[2], "user1 00:11", "The username string is either in wrong format or wrong posistion");
		assertEquals(usernameArray1[3], "user4 00:11", "The username string is either in wrong format or wrong posistion");
		assertEquals(usernameArray1[4], "user2 99:99", "The username string is either in wrong format or wrong posistion");
	}
	@Test
	/*
	 * Tests what happens when you add a better score to the highscore list
	 */
	void pushingOutWorseHighscores() {
		makeEasyTestFile();
		
		//Adding a better time than 5th place to the file
		try {
			manager.saveUsernameToFile("easyTest", "user6", "0:05");
		} catch (FileNotFoundException e) {
			fail("Could not save username to file");
		}
		try {
			highscoreList = manager.loadHighscoreList("easyTest");
		} catch (FileNotFoundException e) {
			fail("Could not load easyTest file");
		}
		//user 6 should be first. Rest should come after. 5th should be pushed out
		String [] usernameArray2 = highscoreList.split("\n");
		assertEquals(usernameArray2[0], "user6 00:05", "The username string is either in wrong format or wrong posistion");
		assertEquals(usernameArray2[1], "user5 00:09", "The username string is either in wrong format or wrong posistion");
		assertEquals(usernameArray2[2], "user3 00:10", "The username string is either in wrong format or wrong posistion");
		assertEquals(usernameArray2[3], "user1 00:11", "The username string is either in wrong format or wrong posistion");
		assertEquals(usernameArray2[4], "user4 00:11", "The username string is either in wrong format or wrong posistion");
		
	}
	@Test
	/*
	 * file should not be null when saving to file
	 */
	void testSaveUsernameToFile() {
		makeEasyTestFile();
		byte [] file = null;
		try {
			file = Files.readAllBytes(Path.of(manager.getFilePath("easyTest")));
		} catch (IOException e) {
			fail("Could not read file");
		}
		assertNotNull(file, "file should not be null");
	}

	@AfterEach
	void tearDown() {
		File easy_test = new File(manager.getFilePath("easyTest"));
		easy_test.delete();
	}
}
