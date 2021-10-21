package minesweeper;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Game {
// -1 == loss, 0 == running, 1 == won
	private int gameState = 0;
	private List<Tile> bombs = new ArrayList<>();
	private Tile[][] board;
	private int sizeX;
	private int sizeY;
	private int numBombs;
	private int numFlaggedTiles = 0;
	private String gameMode;
	private String displayedTime;
	private boolean gameLoaded = false;
	
	/**
	 * Initial constructor for starting a game
	 * @param game mode (easy, medium or hard)
	 */
	public Game(String gameMode) {
		makeGameFromGameMode(gameMode);
		setBoard();
		setBombs();
		fillBoardWithEmptyTiles();
		setNumberTiles();
	}
	
	/**
	 * Constructor for loading from file
	 * @param The board and the gamemode (easy, medium or hard)
	 */
	public Game(String[][] stringBoard,String gameMode) {
		setSizeY(stringBoard.length);
		setSizeX(stringBoard[0].length);
		setGameMode(gameMode);
		setBoard();
		setGameLoaded();
		
		for (int y = 0; y < sizeY; y++) {
			for (int x = 0; x < sizeX; x++) {
				Tile tile = new Tile(x,y);
				if (stringBoard[y][x].charAt(0) == 'B') {
					tile.setTypeBomb();
					bombs.add(tile);
					board[y][x] = tile;
				}
				else if (stringBoard[y][x].charAt(0) == 'E') {
					tile.setType(' ');
					tile.setDisplay(' ');
					tile.setRevealed();
					board[y][x] = tile;
				}
				else if (Character.isDigit(stringBoard[y][x].charAt(0))) {
					tile.setType(stringBoard[y][x].charAt(0));
					tile.setDisplay(stringBoard[y][x].charAt(0));
					tile.setRevealed();
					board[y][x] = tile;
				}
			}
		}
		setNumBombs(bombs.size());
		fillBoardWithEmptyTiles();
		setNumberTiles();
	}
	
	/**
	 * Starts the game mode that were giving in parameters
	 * @param Gamemode specified by user (easy, medium or hard)
	 * @throws If gamemode parameter is not easy, medium or hard
	 */
	public void makeGameFromGameMode(String gameMode) {
		setGameMode(gameMode);
		
		if (gameMode.equals("easy")){
			setSizeX(10);
			setSizeY(10);
			setNumBombs(10);
		}
		else if (gameMode.equals("medium")){
			setSizeX(14);
			setSizeY(14);
			setNumBombs(25);
		}
		else if (gameMode.equals("hard")){
			setSizeX(18);
			setSizeY(18);
			setNumBombs(45);
		}
	}

	/**
	 * Fills the initial board with bombs on random tiles
	 */
	private void setBombs() {
		Random rand = new Random();
		for (int i = 0; i < numBombs; i++) {
			while (true) {
				
				int x = rand.nextInt(sizeX);
				int y = rand.nextInt(sizeY);
				
				if (board[y][x] == null) {

					Tile bomb = new Tile(x, y);
					bomb.setTypeBomb();

					board[y][x] = bomb;
					bombs.add(bomb);
					break;
				}
			}
		}
	}

	/**
	 * Fills the board with empty tiles
	 */
	private void fillBoardWithEmptyTiles() {
		for (int y = 0; y < sizeY; y++) {
			for (int x = 0; x < sizeX; x++) {
				if (board[y][x] == null) {
					board[y][x] = new Tile(x, y);
					board[y][x].setTypeEmpty();
				}
			}
		}
	}

	/**
	 * Sets the Tiles with numbers that are adjacent to the bombs
	 */
	public void setNumberTiles() {
		for (int y = 0; y < sizeY; y++) {
			for (int x = 0; x < sizeX; x++) {
				int numAdjacentBombs = 0;
				List<Tile> adjacentTiles = getAdjacentTiles(x, y);

				if (board[y][x].getType() == Tile.getTypeBomb() || board[y][x].isRevealed()) {
					continue;
				}
				// Counts number of bomb tiles adjacent to tile.
				for (Tile adjacentTile : adjacentTiles) {
					if (adjacentTile.getType() == Tile.getTypeBomb()) {
						numAdjacentBombs += 1;
					}
				}
				
				
				Tile tile = new Tile(x, y);
				if (numAdjacentBombs == 0) {
					tile.setType(Tile.getTypeEmpty());
				} else {
					String stringAdjacentBombs = Integer.toString(numAdjacentBombs);
					char charAdjacentBombs = stringAdjacentBombs.charAt(0);
					tile.setType(charAdjacentBombs);
				}
				board[y][x] = tile;
			}
		}
	}
	
	/**
	 * Returns all bombs adjacent to tile with parameters coordinates
	 * @param x and y values of tile
	 * @return List of adjacent tiles to the tile
	 */
	public List<Tile> getAdjacentTiles(int x, int y) {
		List<Tile> adjacentTiles = new ArrayList<Tile>();

		int minDirectionX = -1;
		int minDirectionY = -1;

		int maxDirectionX = 1;
		int maxDirectionY = 1;

		if (x == 0) {
			minDirectionX = 0;
		} else if (x == sizeX - 1) {
			maxDirectionX = 0;
		}
		if (y == 0) {
			minDirectionY = 0;
		} else if (y == sizeY - 1) {
			maxDirectionY = 0;
		}

		for (int i = minDirectionY; i <= maxDirectionY; i++) {
			for (int j = minDirectionX; j <= maxDirectionX; j++) {
				
				//Should not add the input tile to adjacent tiles
				if (j == 0 && i == 0) {
					continue;
				}
				adjacentTiles.add(board[y + i][x + j]);
			}
		}
		return adjacentTiles;
	}
	
	/**
	 * Flags tile with parameter coordinats
	 * @param x and y values of tile
	 */
	public void flagTile(int x, int y) {

		Tile tile = board[y][x];
		if (tile.isRevealed()) {
			return;
		}
		if (tile.isFlagged()) {
			decreaseFlaggedTiles();
			tile.setFlagged(false);
			tile.setDisplay(Tile.typeEmpty);
		} else {
			incrementFlaggedTiles();
			tile.setFlagged(true);
			tile.setDisplay(Tile.typeFlag);
		}
		if (gameWon()) {
			gameState = 1;
		}
	}
	
	/**
	 * Checks if game is won by checking if all tiles is opened
	 * @return True if game is won, false if game is not won
	 */
	public boolean gameWon() {
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[0].length; y++) {
				if (emptyAndNumberTileIsNotClicked(x,y)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Checks if tile is not clicked. If tile is not clicked and tile is bomb OR tile is of type bomb and tile is clicked
	 * @param x and y values of tile
	 * @return True if tile is not clicked and not bomb. False else
	 */
	private boolean emptyAndNumberTileIsNotClicked(int x, int y) {
		return (((board[y][x].getDisplay() != board[y][x].getType()) && (board[y][x].getType() != Tile.typeBomb))
				|| ((board[y][x].getType() == Tile.typeBomb) && (board[y][x].getDisplay() == Tile.typeBomb)));
	}
	
	/**
	 * Displays tile on parameter coordinates. If tile is empty then it opens the tiles around
	 * @param x value of tile
	 * @param y value of tile
	 * @param if revealTile is called from openAdjacentEmptyTiles()
	 */
	public void revealTile(int x, int y, boolean fromSeveralTileOpener) {
		Tile tile = board[y][x];
		char type = tile.getType();
		if(tile.isRevealed() || (tile.isFlagged() && !fromSeveralTileOpener)) {
			return;
		}
		
		if(tile.isFlagged() && fromSeveralTileOpener) {
			decreaseFlaggedTiles();
		}
		
		tile.setRevealed();
		tile.setDisplay(tile.getType());
		if (type == Tile.typeEmpty) {
			openAdjacentEmptyTiles(x, y);
		}
		else if (type == Tile.typeBomb) {
			setGameState(-1);
		}
		if (gameWon()) {
			setGameState(1);
		}
	}

	/**
	 * Displays all bombs
	 */
	public void revealBombs() {
		for (Tile bomb : bombs) {
			bomb.setDisplay(Tile.typeBomb);
		}
	}
	
	/**
	 * Opens all empty tiles that are adjacent to the tile on paramater coordinates
	 * @param x value of tile
	 * @param y value of tile
	 */
	public void openAdjacentEmptyTiles(int x, int y) {
		List<Tile> adjacentTiles = getAdjacentTiles(x, y);
		for (Iterator<Tile> iterator = adjacentTiles.iterator(); iterator.hasNext();) {
			Tile tile = (Tile) iterator.next();
			if (tile.isRevealed()) {
				continue;
			}
			revealTile(tile.getX(), tile.getY(),true);
		}
	}
	
	/**
	 * @return Number of flagged tiles to show on the display in the app
	 */
	public int getNumberOfFlaggedTilesDisplay() {
		int difference = getNumBombs() - getNumFlaggedTiles();
		if (difference > 0) {
			return difference;
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		String boardString = "";
		for (int y = 0; y < board.length; y++) {
			for (int x = 0; x < board[0].length; x++) {
				if (board[y][x].getType() == 'B')
					boardString += 'B';
				else if (board[y][x].isRevealed() && board[y][x].getType() == ' ')
					boardString += 'E';
				else
					boardString += board[y][x].getDisplay();
			}
			boardString += "\n";
		}
		return boardString;
	}
	public void setGameMode(String gameMode) {
		if (this.gameMode == null && gameMode.matches("easy|medium|hard")) {
			this.gameMode = gameMode;
		}
		else {
			throw new IllegalArgumentException("Gamemode was wrong or gamemode was already set");
		}
	}
	public int getGameState() {
		return gameState;
	}

	public void setGameState(int gameState) {
		if(gameState < -1 || gameState > 1) {
			throw new IllegalArgumentException("Game state should be between -1 and 1"); 
		}
		this.gameState = gameState;
	}

	public Tile getTile(int x, int y) {
		return board[y][x];
	}

	public List<Tile> getBombs() {
		return bombs;
	}
	public Tile[][] getBoard() {
		return board;
	}
	public int getSizeX() {
		return sizeX;
	}

	public void setSizeX(int sizeX) {
		if(this.sizeX != 0) {
			throw new IllegalArgumentException("Cannot change size in the middle of a game");
		}
		if(sizeX < 2) {
			throw new IllegalArgumentException("sizeX cannot be smaller than 2");
		}
		this.sizeX = sizeX;
	}
	public void setBoard() {
		if (sizeY != sizeX) {
			throw new IllegalArgumentException("Cannot make a board with uneven dimentions");
		}
		if (sizeY < 1 || sizeX < 1) {
			throw new IllegalArgumentException("Board has wrong dimentions");
		}
		this.board = new Tile[sizeY][sizeX];
	}
	public int getSizeY() {
		return sizeY;
	}

	public void setSizeY(int sizeY) {
		if(this.sizeY != 0) {
			throw new IllegalArgumentException("Cannot change size in the middle of a game");
		}
		if(sizeY < 2) {
			throw new IllegalArgumentException("sizeY cannot be smaller than 2");
		}
		this.sizeY = sizeY;
	}

	public int getNumBombs() {
		return numBombs;
	}

	public void setNumBombs(int numBombs) {
		if(numBombs < 1) {
			throw new IllegalArgumentException("There cannot be less than 1 bomb on the board");
		}
		else if(numBombs >= sizeY*sizeX) {
			throw new IllegalArgumentException("There should be at least one non bomb tile on the board");
		}
		this.numBombs = numBombs;
	}

	public void incrementFlaggedTiles() {
		numFlaggedTiles++;
	}

	public void decreaseFlaggedTiles() {
		numFlaggedTiles--;
		if (numFlaggedTiles < 0) {
			throw new IllegalStateException("Flagged tiles cannot be less than 0");
		}
	}

	public int getNumFlaggedTiles() {
		return numFlaggedTiles;
	}

	public String getGameMode() {
		return gameMode;
	}

	public String getDisplayedTime() {
		return displayedTime;
	}

	public void setDisplayedTime(String displayedTime) {
		this.displayedTime = displayedTime;
	}
	public boolean isGameLoaded() {
		return gameLoaded;
	}
	public void setGameLoaded() {
		gameLoaded = true;
	}
}
