package minesweeper;

import static org.junit.jupiter.api.Assertions.assertEquals;


//import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

//import java.util.Iterator;
//import java.util.Arrays;
//import java.util.Iterator;
import java.util.List;
//import java.util.function.Predicate;
//import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//import minesweeper.Game;

//In the end there is bigger testcases for board with edgecases where revealing tiles and openadjacentempty tiles is tested
public class GameTest {
	private Game easy;
	private Game medium;
	private Game hard;
	
	private Tile[][] easyBoard;
	private Tile[][] mediumBoard;
	
	@BeforeEach
	public void setUp() {
		easy = new Game("easy");
		medium = new Game("medium");
		hard = new Game("hard");
		easyBoard = easy.getBoard(); 
		mediumBoard = medium.getBoard();
	}
	
	/*
	 * Constructs the board shown
	 * B|1|E|E|E|E|E|E|E|E
	 * 1|1|E|E|E|E|E|E|E|E
	 * E|E|E|E|E|E|E|E|E|E
	 * E|E|E|E|E|E|E|E|E|E
	 * 2|3|3|3|3|3|3|3|3|2
	 * B|B|B|B|B|B|B|B|B|B
	 * 2|3|3|3|3|3|3|3|3|2
	 * E|E|E|E|E|E|E|E|E|E
	 * E|E|E|E|E|E|E|E|E|E
	 * E|E|E|E|E|E|E|E|E|E
	 */
	public String[][] makeBoardFromString() {
		String [][] stringBoard = new String[10][10];

		//Making the rest of stringboard as board above
		for (int y = 0; y < stringBoard.length; y++) {
			for (int x = 0; x < stringBoard[0].length; x++) {
				
				// Placing bomb top corner
				if (y == 0 && x == 0) {
					stringBoard[y][x] = "B";
				}
				
				// Placing bombs on line
				else if (y == 5) {
					stringBoard[y][x] = "B";
				}
				else {
					stringBoard[y][x] = " ";
				}
			}
		}
		return stringBoard;
	}
	@Test
	void testGameFromGamemodeConstructor() {
		// Testing easy game mode values
		assertEquals(10, easy.getSizeX(), "Easy gamemode has wrong sizeX");
		assertEquals(10, easy.getSizeY(), "Easy gamemode has wrong sizeY");
		assertEquals(10, easy.getNumBombs(), "Easy gamemode has wrong number of bombs");
		assertEquals("easy", easy.getGameMode(), "The gamemode should be easy");

		// Testing medium game mode values
		assertEquals(14, medium.getSizeX(), "Medium gamemode has wrong sizeX");
		assertEquals(14, medium.getSizeY(), "Medium gamemode has wrong sizeY");
		assertEquals(25, medium.getNumBombs(), "Medium gamemode has wrong number of bombs");
		assertEquals("medium", medium.getGameMode(), "The gamemode should be medium");

		// Testing hard game mode values
		assertEquals(18, hard.getSizeX(), "Hard gamemode has wrong sizeX");
		assertEquals(18, hard.getSizeY(), "Hard gamemode has wrong sizeY");
		assertEquals(45, hard.getNumBombs(), "Hard gamemode has wrong number of bombs");
		assertEquals("hard", hard.getGameMode(), "The gamemode should be hard");

		assertThrows(IllegalArgumentException.class, () -> 
			new Game("wrongGameModeName"), "Gamemode name must either be 'easy', 'medium' or 'hard' ");
	}
	@Test

	void testGameFromFileConstructor() {
		String[][] stringBoard = makeBoardFromString();
		Game fileConstructorGame = new Game(stringBoard, "easy");
		
		assertEquals(10, fileConstructorGame.getSizeY(), String.format("Size Y should be 10 but is %d", fileConstructorGame.getSizeY()));
		assertEquals(10, fileConstructorGame.getSizeX(), String.format("Size X should be 10 but is %d", fileConstructorGame.getSizeX()));
		assertEquals("easy", fileConstructorGame.getGameMode(), String.format("Gamemode should be 'easy' but was %s", fileConstructorGame.getGameMode()));
		assertTrue(fileConstructorGame.isGameLoaded(), "Game loaded should be true but was false");
	}
	@Test
	/*
	 * Tests that the types are correct according to the string board. Line of bombs with exstra special cases in the corners.
	 * See board in makeBoardFromString()
	 */
	void testSetNumberTilesFromStringBoard() {
		String[][] stringBoard = makeBoardFromString();
		Game fileConstructorGame = new Game(stringBoard, "easy");
		Tile[][] fileConstructorBoard = fileConstructorGame.getBoard();
		
		for (int y = 0; y < stringBoard.length; y++) {
			for (int x = 0; x < stringBoard[0].length; x++) {
				if (y == 5) {
					assertEquals('B', fileConstructorBoard[y][x].getType(), String.format("Type of tile should be 'B' but was %c on tile (%d, %d)", fileConstructorBoard[y][x].getType(), x,y));
				}
				else if((y == 4 || y == 6) && (x == 0 || x == 9)) {
					assertEquals('2', fileConstructorBoard[y][x].getType(), String.format("Type of tile should be 2 but was %c on tile (%d, %d)", fileConstructorBoard[y][x].getType(), x,y));
				}
				else if ((y == 4 || y == 6) && (x > 0 && x < 9)) {
					assertEquals('3', fileConstructorBoard[y][x].getType(), String.format("Type of tile should be 3 but was %c on tile (%d, %d)", fileConstructorBoard[y][x].getType(),x,y));
				}
				
				// Testing bomb top corner
				else if (x == 0 && y == 0) {
					assertEquals('B', fileConstructorBoard[y][x].getType(), String.format("Type of tile should be 'B' but was %c on tile (%d, %d)", fileConstructorBoard[y][x].getType(), x,y));
				}
				//Testing 1's around bomb top corner
				else if ((y == 0 && x == 1) || (y == 1 && (x == 0 || x == 1))) {
					assertEquals('1', fileConstructorBoard[y][x].getType(), String.format("Type of tile should be 1 but was %c on tile (%d, %d)", fileConstructorBoard[y][x].getType(), x,y));
				}
				// Testing the empty tiles
				else {
					assertEquals(' ', fileConstructorBoard[y][x].getType(), String.format("Type of tile should be empty (' ') but was %c on tile (%d, %d)", fileConstructorBoard[y][x].getType(), x,y));
				}
			}
		}
	}
	@Test
	void testSetBombs() {
		// Testing that the array with bombs is same lenght as number of bombs
		assertEquals(easy.getNumBombs(), easy.getBombs().size(),
				"Number of bombs in easy gamemode doesnt match the size of bomb-array");
		assertEquals(medium.getNumBombs(), medium.getBombs().size(),
				"Number of bombs doesnt match the size of bomb-array");
		assertEquals(hard.getNumBombs(), hard.getBombs().size(), "Number of bombs doesnt match the size of bomb-array");

	}
	
	@Test
	void testRevealBombs() {
		easy.revealBombs();
		for (Tile bomb : easy.getBombs()) {
			assertEquals('B', bomb.getDisplay(), "Bomb should have bomb display");
		}
	}
	
	@Test
	void testGetNumbersOfFlaggedTilesDisplay() {
		easy.flagTile(0, 0);
		assertEquals(9, easy.getNumberOfFlaggedTilesDisplay(), "Should display 9 flagged tiles left");
		
		for (int i = 1; i < easyBoard.length; i++) {
			easy.flagTile(i, i);
		}
		assertEquals(0, easy.getNumberOfFlaggedTilesDisplay(), "Should display 0 flagged tiles left because 10 tiles were flagged");
		
		//Flaggs 11th tile but should still be 0 because cannot go below 0
		easy.flagTile(1, 0);
		assertEquals(0, easy.getNumberOfFlaggedTilesDisplay(), "Should display 0 flagged tiles left because 11 tiles were flagged but display cannot go below 0");
	}
	@Test
	/*
	 * Test for getting the adjacent tiles to a tile
	 */
	void testGetAdjacentTiles() {
		// Testing that the adjacent tiles to a tile
		List<Tile> adjacentTilesCorner = easy.getAdjacentTiles(0, 0);
		List<Tile> adjacentTilesMiddle = easy.getAdjacentTiles(5, 5);
		
		//Checks adjacent tiles to tile in corner
		assertTrue(adjacentTilesCorner.contains(easy.getBoard()[0][1]), "Tile (0,0) should be adjacent to Tile (0,1)");
		assertTrue(adjacentTilesCorner.contains(easy.getBoard()[1][1]), "Tile (0,0) should be adjacent to Tile (1,1)");
		assertTrue(adjacentTilesCorner.contains(easy.getBoard()[1][0]), "Tile (0,0) should be adjacent to Tile (1,0)");
		assertFalse(adjacentTilesCorner.contains(easy.getBoard()[0][0]), "Tile (0,0) should not be adjacent to Tile (0,0)");
		assertFalse(adjacentTilesCorner.contains(easy.getBoard()[2][0]), "Tile (0,0) should not be adjacent to Tile (2,0)");

		assertThrows(ArrayIndexOutOfBoundsException.class, () -> 
			adjacentTilesCorner.contains(easy.getBoard()[-1][0]),
			"(-1,0) is not adjacent to (0,0)");

		//Checks adjacent tiles to tile in middle
		assertTrue(adjacentTilesMiddle.contains(easy.getBoard()[5][6]), "Tile (5,5) should be adjacent to Tile (5,6)");
		assertTrue(adjacentTilesMiddle.contains(easy.getBoard()[6][5]), "Tile (5,5) should be adjacent to Tile (6,5)");
		assertTrue(adjacentTilesMiddle.contains(easy.getBoard()[5][4]), "Tile (5,5) should be adjacent to Tile (5,4)");
		assertTrue(adjacentTilesMiddle.contains(easy.getBoard()[4][5]), "Tile (5,5) should be adjacent to Tile (4,5)");
		assertTrue(adjacentTilesMiddle.contains(easy.getBoard()[6][6]), "Tile (5,5) should be adjacent to Tile (6,6)");
		assertTrue(adjacentTilesMiddle.contains(easy.getBoard()[4][4]), "Tile (5,5) should be adjacent to Tile (4,4)");
		assertTrue(adjacentTilesMiddle.contains(easy.getBoard()[4][6]), "Tile (5,5) should be adjacent to Tile (4,6)");
		assertTrue(adjacentTilesMiddle.contains(easy.getBoard()[6][4]), "Tile (5,5) should be adjacent to Tile (6,4)");

		assertFalse(adjacentTilesMiddle.contains(easy.getBoard()[5][7]),
				"Tile (5,5) should not be adjacent to Tile (5,7)");
		assertFalse(adjacentTilesMiddle.contains(easy.getBoard()[7][5]),
				"Tile (5,5) should not be adjacent to Tile (7,5)");
		assertFalse(adjacentTilesMiddle.contains(easy.getBoard()[0][0]),
				"Tile (5,5) should not be adjacent to Tile (0,0)");
	}


	@Test
	/*
	 * Finds a bomb and tests that the adjacent tiles have type bomb or number
	 */
	void testSetNumberTilesRandomBoard() {
		//Finds a bomb on the board
		Tile bomb = easy.getBombs().get(0);
		int x = bomb.getX();
		int y = bomb.getY();
		
		List<Tile> adjacentBombTiles = easy.getAdjacentTiles(x, y);
		
		//Loops through every adjacent tile, and checks if its a number- or bombtile.
		for(Tile adjacentBombTile : adjacentBombTiles) {
			assertTrue(String.valueOf(adjacentBombTile.getType()).matches("[0-9]|B"), "One of the tiles adjacent to a bomb is not a numbertile nor bombtile which is incorrect");
		}
	}
	@Test
	/*
	 * Tests for flagging tiles
	 */
	void testFlagTile() {
		Tile tile = easyBoard[0][0];
		easy.flagTile(0, 0);
		
		//Flags tile should be flagged
		assertTrue(tile.isFlagged(), "Tile is not flagged when flagged");
		assertEquals(1, easy.getNumFlaggedTiles(), String.format("Number of flagged tiles is %d, but should be %d tiles", easy.getNumFlaggedTiles(), 1)); 
		
		//Flags tile again. Should not be flagged
		easy.flagTile(0,0);
		assertFalse(tile.isFlagged(), "The tile should not be flagged when flagged again");
		assertEquals(0, easy.getNumFlaggedTiles(), String.format("Number of flagged tiles is %d, but should be %d tiles", easy.getNumFlaggedTiles(), 0));
		
		//Flags again. Should not be able to reveal when not called from openAdjacentEmptyTiles - function
		easy.flagTile(0, 0);
		easy.revealTile(0, 0, false);
		assertFalse(tile.isRevealed(), "Should not be able to reveal tile when tile is flagged and is not called from openAdjacentEmptyTiles - function");
		assertEquals(1, easy.getNumFlaggedTiles(), String.format("Number of flagged tiles is %d, but should be %d tiles because a tile is flagged", easy.getNumFlaggedTiles(), 0));
	
		//Should be able to reveal when called from openAdjacentEmptyTiles
		easy.revealTile(0, 0, true);
		assertTrue(tile.isRevealed(), "Should be able to reveal tile when tile is flagged and called from openAdjacentEmptyTiles - function");
		assertEquals(0, easy.getNumFlaggedTiles(), String.format("Number of flagged tiles is %d, but should be %d tiles because a tile is flagged", easy.getNumFlaggedTiles(), 0));
		
		// Reveals tile then flags tile. Should not be able to flag tile when revealed
		easy.revealTile(1, 0,false);
		easy.flagTile(1, 0);
		assertFalse(easyBoard[0][1].isFlagged(), "Tile should not be flagged when tile is opened");
		assertEquals(0, easy.getNumFlaggedTiles(), String.format("Number of flagged tiles is %d, but should be %d tiles because no tile was flagged", easy.getNumFlaggedTiles(), 0));
	}
	
	@Test
	/*
	 * Tests for gamewon and gamestate
	 */
	void testGameWon() {
		assertEquals(0,easy.getGameState(), "Gamestate should be 0 when game is not won nor lost");
		
		// Opens all tiles except for bomb tile. Game should be won
		for (int y = 0; y < easyBoard.length; y++) {
			for (int x = 0; x < easyBoard[0].length; x++) {
				if (easyBoard[y][x].getType() != 'B') {
					easy.revealTile(x,y, false);
				}
			}
		}
		assertTrue(easy.gameWon(), "Game should be won when all tiles are open except the bombs");
		assertEquals(1, easy.getGameState(), "Game should have gamestate 1 when the game is won");
		
		for (int y = 0; y < mediumBoard.length; y++) {
			for (int x = 0; x < mediumBoard[0].length; x++) {
				medium.revealTile(x,y, false);
			}
		}
		assertFalse(medium.gameWon(), "Game should not be won when all tiles are open including bombs");
	}
	
	@Test
	/*
	 * Checks for game lost
	 */
	void testGameLost(){
		//Opens a bomb tile. Game should be lost
		for (int y = 0; y < easyBoard.length; y++) {
			for (int x = 0; x < easyBoard[0].length; x++) {
				if (easyBoard[y][x].getType() == 'B') {
					easy.revealTile(x,y, false);
					break;
				}
			}
		}
		assertEquals(-1, easy.getGameState(), "Game should have gamestate -1 when one bomb is clicked and the game is lost");
		
		//Opens all tiles. Game should still be lost even thought this is impossible in real game
		for (int y = 0; y < mediumBoard.length; y++) {
			for (int x = 0; x < mediumBoard[0].length; x++) {
				medium.revealTile(x,y, false);
			}
		}
		assertEquals(-1, easy.getGameState(), "Game should have gamestate -1 when all tiles is clicked including bombs");
	}
	
	@Test
	/*
	 * User is not able to set the dimentions on the board, but is still test the validation
	 */
	void testSetSizeOfBoard() {
		//Making a board with lower than y < 2
		String twoTileBoard[][] = {{"E", "E"}};
		assertThrows(IllegalArgumentException.class, () -> new Game(twoTileBoard,"easy"),"Cannot make a board with size Y < 2");
		
		//Making an unevensized board
		String unEvenDimentions[][] = {{"E","E","E"}, {"B", "B","B"}};
		assertThrows(IllegalArgumentException.class, () -> new Game(unEvenDimentions,"easy"),"Cannot make an unevensized board");
		
		//Making a board with no bombs
		String zeroBombBoard[][] = {{"E","E"},{"E", "E"}};
		assertThrows(IllegalArgumentException.class, () -> new Game(zeroBombBoard,"easy"),"Cannot make a zero bomb board");
		
		//Setting size when game is already created
		assertThrows(IllegalArgumentException.class, () -> easy.setSizeY(10),"Cannot set sizeY when game is already created");
		assertThrows(IllegalArgumentException.class, () -> easy.setSizeX(10),"Cannot set sizeX when game is already created");
	}
	
	@Test
	/**
	 * Checks if tile is revealed when revealed and type is same as display
	 */
	void testRevealTile() {
		for (int y = 0; y < easyBoard.length; y++) {
			for (int x = 0; x < easyBoard[0].length; x++) {
				Tile tile = easyBoard[y][x];
				easy.revealTile(x, y, false);
				assertTrue(tile.isRevealed());
				assertEquals(tile.getType(), tile.getDisplay(), "The display should be shown when tile is revealed");
			}
		}
		for (int y = 0; y < easyBoard.length; y++) {
			for (int x = 0; x < easyBoard[0].length; x++) {
				if (y == 1 && x == 1) {
					easyBoard[y][x].setTypeEmpty();
				}
				else {
					easyBoard[y][x].setTypeEmpty();
				}
				assertTrue(easyBoard[y][x].getType() == Tile.typeEmpty);
			}
		}
	}
	
	/*
	 * Every other line is a bomb line. Tests revealing tiles. E = empty tile. B = bomb. 1 = 
	 * E|2|B|2|E|2|B|2|E|E
	 * E|3|B|3|E|3|B|3|E|E
	 * E|3|B|3|E|3|B|3|E|E
	 * E|3|B|3|E|3|B|3|E|E
	 * E|3|B|3|E|3|B|3|E|E
	 * E|3|B|3|E|3|B|3|E|E
	 * E|3|B|3|E|3|B|3|E|E
	 * E|3|B|3|E|3|B|3|E|E
	 * E|3|B|3|E|3|B|3|E|E
	 * E|2|B|2|E|2|B|2|E|E
	 */
	@Test
	void testEveryOtherLineIsBombLineRevealTile(){
		//Making the board as shown in commenting above. 
		String [][] stringBoard = new String[10][10];
		for (int y = 0; y < stringBoard.length; y++) {
			for (int x = 0; x < stringBoard[0].length; x++) {
				if (x == 2 || x == 6) {
					stringBoard[y][x] = "B";
				}
				else {
					stringBoard[y][x] = " ";
				}
			}
		}
		Game lineBombGame = new Game(stringBoard, "easy");
		Tile [][] lineBombBoard = lineBombGame.getBoard();
		
		// Opens first line check board above. Line 1 and 2 should be revealed
		lineBombGame.revealTile(0, 0, false);
		for (int y = 0; y < lineBombBoard.length; y++) {
			for (int x = 0; x < lineBombBoard[0].length; x++) {
				if(x == 0 || x == 1) {
					assertTrue(lineBombBoard[y][x].isRevealed(), String.format("All tiles in column 0 and 1 should be revealed %d, %d", x,y));
				}
				else {
					assertFalse(lineBombBoard[y][x].isRevealed(),String.format("All tiles in column 2 to 9 should not be revealed because they are separated by line of bombs of revealed tile %d, %d", x,y));
				}
			}
		}
		// Reveals a numberTile
		lineBombGame.revealTile(3, 0, false);
		for (int y = 2; y < lineBombBoard.length; y++) {
			for (int x = 2; x < lineBombBoard[0].length; x++) {
				if(x == 3 && y == 0) {
					assertTrue(lineBombBoard[y][x].isRevealed(), String.format("Tile (%d, %d) should be revealed because its revealed", x,y));
				}
				else {
					assertFalse(lineBombBoard[y][x].isRevealed(),String.format("No other tiles than (3,0) should be revealed because its a numbertile (%d, %d) should not be revealed", x,y));
				}
			}
		}	
		// Reveals an empty Tile
		lineBombGame.revealTile(4, 0, false);
		for (int y = 2; y < lineBombBoard.length; y++) {
			for (int x = 2; x < lineBombBoard[0].length; x++) {
				if(x == 3 || x == 4 || x == 5) {
					assertTrue(lineBombBoard[y][x].isRevealed(), String.format("All tiles in column 3, 4, 5 should be revealed %d, %d", x,y));
				}
				else {
					assertFalse(lineBombBoard[y][x].isRevealed(),String.format("All tiles in column 6 to 9 should not be revealed because they are separated by line of bombs of revealed tile %d, %d", x,y));
				}
			}
		}
	}
	
	/*
	 * Bomb in the middle reveals tile in corner. Every tile except for the bomb should be open
	 * E|E|E|E|E|E|E|E|E|E
	 * E|E|E|E|E|E|E|E|E|E
	 * E|E|E|E|E|E|E|E|E|E
	 * E|E|E|E|E|E|E|E|E|E
	 * E|E|E|E|1|1|1|E|E|E
	 * E|E|E|E|1|B|1|E|E|E
	 * E|E|E|E|1|1|1|E|E|E
	 * E|E|E|E|E|E|E|E|E|E
	 * E|E|E|E|E|E|E|E|E|E
	 * E|E|E|E|E|E|E|E|E|E
	 */
	@Test
	void testOneBombGameRevealTile() {
		String [][] stringBoard = new String[10][10];
		for (int y = 0; y < stringBoard.length; y++) {
			for (int x = 0; x < stringBoard[0].length; x++) {
				if (x == 5 && y == 5) {
					stringBoard[5][5] = "B";
				}
				else {
					stringBoard[y][x] = " ";
				}
			}
		}
		Game oneBombGame = new Game(stringBoard, "easy");
		Tile [][] oneBombBoard = oneBombGame.getBoard();
		oneBombGame.revealTile(0, 0, false);
		for (int y = 0; y < oneBombGame.getSizeY(); y++) {
			for (int x = 0; x < oneBombGame.getSizeX(); x++) {
				if (x == 5 && y == 5) {
					assertFalse(oneBombBoard[5][5].isRevealed());
				}
				else {
					assertTrue(oneBombBoard[y][x].isRevealed(), String.format("All the empty tile should be opened so should tile %d, %d", x,y));
				}
				
			}
		}
	}
	
	/*
	 * Bombs on all walls revealing middle tile. Every tile except wall should be open
	 * B|B|B|B|B|B|B|B|B|B
	 * B|5|3|3|3|3|3|3|5|B
	 * B|3|E|E|E|E|E|E|3|B
	 * B|3|E|E|E|E|E|E|3|B
	 * B|3|E|E|E|E|E|E|3|B
	 * B|3|E|E|E|E|E|E|3|B
	 * B|3|E|E|E|E|E|E|3|B
	 * B|3|E|E|E|E|E|E|3|B
	 * B|5|3|3|3|3|3|3|5|B
	 * B|B|B|B|B|B|B|B|B|B
	 */
	@Test
	void testBombsOnWallsRevealTile() {
		String [][] stringBoard = new String[10][10];
		for (int y = 0; y < stringBoard.length; y++) {
			for (int x = 0; x < stringBoard[0].length; x++) {
				if (x == 0 || y == 0 || x == 9 || y == 9) {
					stringBoard[y][x] = "B";
				}
				else {
					stringBoard[y][x] = " ";
				}
			}
		}

		Game bombsOnWallGame = new Game(stringBoard, "easy");
		Tile [][] bombsOnWallBoard = bombsOnWallGame.getBoard();
		
		// Tests that tile (1,1) type is '5'
		assertEquals('5', bombsOnWallBoard[1][1].getType(),"The tile (1,1) has 5 adjacent bombs so it should be of type '5'");
		assertEquals('3', bombsOnWallBoard[1][2].getType(),"The tile (2,1) has 3 adjacent bombs so it should be of type '3'");
		
		bombsOnWallGame.revealTile(5, 5, false);
		for (int y = 0; y < bombsOnWallGame.getSizeY(); y++) {
			for (int x = 0; x < bombsOnWallGame.getSizeX(); x++) {
				if (x == 0 || y == 0 || x == 9 || y == 9) {
					assertFalse(bombsOnWallBoard[y][x].isRevealed());
				}
				else {
					assertTrue(bombsOnWallBoard[y][x].isRevealed(), String.format("All the empty tile should be opened so should tile %d, %d", x,y));
				}
				
			}
		}
	}
	@Test
	void testSetGameModeFromExistingGame() {
		assertThrows(IllegalArgumentException.class, () -> 
		easy.setGameMode("easy"), "Cannot set game mode when game already exist");
	}
	@Test
	void testSetGameState() {
		assertEquals(0,easy.getGameState(), String.format("GameState should be 0 when a game is initialized because then the game is not over, but gamestate was %d", easy.getGameState()));
		
		easy.setGameState(1);
		assertEquals(1,easy.getGameState(), String.format("GameState should be 1 when a game is set to be 1 but gamestate was %d", easy.getGameState()));
		
		assertThrows(IllegalArgumentException.class, () -> 
		easy.setGameState(2), "2 is not a valid gamestate");
	}
	@Test
	void testSetNumBombs() {
		assertThrows(IllegalArgumentException.class, () -> 
		easy.setNumBombs(0), "Cannot have less than 1 bomb");
		
		
		assertThrows(IllegalArgumentException.class, () -> 
		easy.setNumBombs(100), "Cannot have more or equal number of bombs as gamesize");
	}
}