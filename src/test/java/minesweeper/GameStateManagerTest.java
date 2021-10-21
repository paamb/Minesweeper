package minesweeper;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class GameStateManagerTest {
	private Game game;
	private Game loadGame;
	private IMinesweeperGameStateReader manager;

	@BeforeEach
	void setUp() throws Exception {
		game = new Game("easy");
		manager = new GameStateManager();

	}

	@Test
	void testLoad() throws FileNotFoundException {
		assertThrows(FileNotFoundException.class, () -> manager.load("wrongLoadName"),
				"Cannot load from a file that is not loaded");
		try {
			manager.save(game, "saveFile");
		}
		catch(Exception e) {
			fail("Could not save to file");
		}
		try {
			loadGame = manager.load("saveFile");
		} catch (Exception e) {
			fail("Could not load from file");
		}
		
		
		assertEquals(loadGame.getGameMode(), game.getGameMode(), "Gamemode from loaded game should be the same as gamemode from the saved game");
		assertEquals(loadGame.getSizeX(), game.getSizeX(), "Loaded game should have the same sizeX as saved game");
		assertEquals(loadGame.getSizeY(), game.getSizeY(), "Loaded game should have the same sizeY as saved game");
		
		// Checks if all tiles are the same
		for (int y = 0; y < loadGame.getBoard().length; y++) {
			for (int x = 0; x < loadGame.getBoard()[0].length; x++) {
				assertEquals(loadGame.getBoard()[y][x].getType(), game.getBoard()[y][x].getType(),
						"Type should be the same");
				assertEquals(loadGame.getBoard()[y][x].getDisplay(), game.getBoard()[y][x].getDisplay(),
						"Display should be the same");
			}
		}
	}

	@Test
	void testSave() {
		String file1Path = manager.getFilePath("file1");
		String file2Path = manager.getFilePath("file2");

		try {
			manager.save(game, "file1");
		}
		catch(Exception e) {
			fail("Could not save file1 to file");
		}
		
		try {
			manager.save(game, "file2");
		}
		catch(Exception e) {
			fail("Could not save file2 to file");
		}
		byte[] file1 = null, file2 = null;
		
		try {
			Path file1PathToPath = Path.of(file1Path);
			file1 = Files.readAllBytes(file1PathToPath);
		}
		catch(Exception e){
			fail("Could not read file1 from file");
		}
		try {
			Path file2PathToPath = Path.of(file2Path); 
			file2 = Files.readAllBytes(file2PathToPath);
		}
		catch(Exception e) {
			fail("Could not read file2 from file");
		}
		
		assertNotNull(file1, "File1 should not be null");
		assertNotNull(file2, "File2 should not be null");
		assertTrue(Arrays.equals(file1, file2), "File1 and file2 shoud be equal");
	}
	@Test
	void testOverwritingExistingFile() {
		Game mediumGame = new Game("medium");
		
		try {
			manager.save(game, "file");
		}
		catch(Exception e) {
			fail("Could not save game to file");
		}
		try {
			loadGame = manager.load("file");
		} catch (FileNotFoundException e) {
			fail("Could not load file");
		}
		assertEquals(loadGame.getGameMode(), "easy", "Loadgame should have gamemode easy");
		
		
		//Overwrites file with new file
		try {
			manager.save(mediumGame, "file");
		}
		catch(Exception e) {
			fail("Could not save mediumgame to file");
		}
		
		try {
			loadGame = manager.load("file");
		} catch (FileNotFoundException e) {
			fail("Could not find file");
		}
		
		assertEquals(loadGame.getGameMode(), "medium", "Loadgame should have gamemode medium");
	}
	@Test
	void testInvalidFilename() {
		assertThrows(IllegalArgumentException.class, () -> manager.save(game, "?filename"),
				"Cannot save to file because filename can only contain letters and numbers");
		
		assertThrows(IllegalArgumentException.class, () -> manager.save(game, "f/ilename"),
				"Cannot save to file because filename can only contain letters and numbers");
		
		assertThrows(IllegalArgumentException.class, () -> manager.save(game, "f+ilename"),
				"Cannot save to file because filename can only contain letters and numbers");
		
		assertThrows(IllegalArgumentException.class, () -> manager.save(game, "f{ilename"),
				"Cannot save to file because filename can only contain letters and numbers");
	}
	@AfterEach
	void tearDown() {
		File saveFile = new File(manager.getFilePath("saveFile"));
		File file1 = new File(manager.getFilePath("file1"));
		File file2 = new File(manager.getFilePath("file2"));
		
		saveFile.delete();
		file1.delete();
		file2.delete();
	}
}
