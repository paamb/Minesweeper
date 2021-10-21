package minesweeper;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class MinesweeperController {
	private Game game;
	private Time time;
	private IMinesweeperHighscoreReader filemanager = new HighscoreManager();
	private IMinesweeperGameStateReader gameStateManager = new GameStateManager();

	@FXML
	AnchorPane anchorPane;

	@FXML
	GridPane gridPane;

	@FXML
	Pane grid, userText, flagged, timePane, highscoreList, feedbackPane;

	@FXML
	TextField userName, filename;

	@FXML
	Button registerUsername, save, load;

	@FXML
	VBox feedbackContainer;

	private Timer timer;

	@FXML
	private void initialize() {
		createGameBoard("easy");

	}

	/*
	 * Creating the game a new game
	 */
	private void createGameBoard(String gamemode) {
		Game board = new Game(gamemode);
		createGameBoard(board);
	}

	/*
	 * Creating the game from loaded game
	 */
	private void createGameBoard(Game game) {
		this.time = new Time();
		this.timer = new Timer();
		this.game = game;

		userText.getChildren().clear();
		grid.getChildren().clear();

		createGrid();
		startTimerSchedule();
		updateHighscoreList(game.getGameMode());
		filenameVisibility();
		resetFeedbackMessage();

	}

	/*
	 * Creating the game grid
	 */
	private void createGrid() {
		double paneWidth = grid.getPrefWidth();
		double paneHeight = grid.getPrefHeight();

		grid.setPrefWidth(paneWidth);
		grid.setMaxWidth(paneWidth);

		grid.setMaxHeight(paneHeight);
		grid.setPrefHeight(paneHeight);

		double buttonSizeX = paneWidth / game.getSizeX();
		double buttonSizeY = paneHeight / game.getSizeY();
		for (int y = 0; y < game.getSizeY(); y++) {
			for (int x = 0; x < game.getSizeY(); x++) {
				Button tile = new Button();
				tile.setTranslateY(buttonSizeY * y);
				tile.setTranslateX(buttonSizeX * x);
				tile.setPrefHeight(buttonSizeY);
				tile.setPrefWidth(buttonSizeX);
				tile.setStyle(
						"-fx-border-radius: 10 10 10 10; -fx-background-radius: 10 10 10 10; -fx-border-width: 1px; -fx-font-size: 100%");
				tile.setId(String.format("%d,%d", x, y));
				addEventToButton(tile);
				grid.getChildren().add(tile);
			}
		}

		drawBoard();

	}

	@FXML
	private void easy() {
		timer.cancel();
		createGameBoard("easy");
	}

	@FXML
	private void medium() {
		timer.cancel();
		createGameBoard("medium");

	}

	@FXML
	private void hard() {
		timer.cancel();
		createGameBoard("hard");

	}

	@FXML
	private void save() {
		resetFeedbackMessage();
		try {
			gameStateManager.save(game, filename.getText());
			makeSavedOrLoadedFeedback("Saved to file " + filename.getText() + ".txt");
		}catch (IllegalArgumentException e) {
			makeErrorMessage("Filename can only contain letters and numbers");
		} catch (IOException e) {
			makeErrorMessage("Could not create dir");
		}
	}

	@FXML
	private void load() {
		try {
			Game game = gameStateManager.load(filename.getText());
			timer.cancel();
			createGameBoard(game);
			makeSavedOrLoadedFeedback("Loaded from file " + filename.getText() + ".txt");
		} catch (FileNotFoundException e) {
			makeErrorMessage("File not found");
		}
	}

	@FXML
	private void registerName() {
		String name = userName.getText();
		if (name.equals("Username")) {
			makeErrorMessage("Change this field to a username");
		} else if (name.length() > 9) {
			makeErrorMessage("Username cannot be longer than 9 characters");
		} else {
			String gameMode = game.getGameMode();
			String finalTime = game.getDisplayedTime();
			try {
				filemanager.saveUsernameToFile(gameMode, name, finalTime);
				updateHighscoreList(gameMode);
				makeSavedOrLoadedFeedback(String.format("Saved '%s' to highscorelist", name));
				noVisibility();
			} catch (FileNotFoundException e) {
				makeErrorMessage("Could not save username to file");
			}
		}
	}

	@FXML
	public void gameLost() {
		Text text = new Text();
		text.setText("\n         Game Over");
		text.setFill(Color.RED);
		text.setStyle("-fx-font-size: 200%");
		text.setTextAlignment(TextAlignment.CENTER);
		userText.getChildren().add(text);
		timer.cancel();
		game.revealBombs();
		disableButtons();
		noVisibility();
	}

	@FXML
	public void gameWon() {
		Text text = new Text();
		text.setText("\n         Game Won");
		text.setFill(Color.GREEN);
		text.setStyle("-fx-font-size: 200%");
		userText.getChildren().add(text);
		timer.cancel();
		game.revealBombs();
		if (!game.isGameLoaded()) {
			usernameVisibility();
		} else if (game.isGameLoaded()) {
			makeSystemFeedback("Cannot registrer for highscorelist when game is from file");
			noVisibility();
		}
		disableButtons();
	}

	/*
	 * Draws the number of flagged tiles
	 */
	public void flagged() {
		Text text = new Text();
		text.setText(String.format("\n Flagged : %d", game.getNumberOfFlaggedTilesDisplay()));
		text.setFill(Color.RED);
		text.setStyle("-fx-font-size: 130%");
		flagged.getChildren().clear();
		flagged.getChildren().add(text);
	}

	private void updateHighscoreList(String gameMode) {
		String highscoreString = "Highscores: \n";
		try {
			highscoreString += filemanager.loadHighscoreList(gameMode);
		} catch (FileNotFoundException e) {
			highscoreString += "";
		}
		Text highscoreListText = new Text("\n" + highscoreString);
		highscoreListText.setStyle("-fx-color: blue; -fx-font-size: 130%");
		highscoreList.getChildren().clear();
		highscoreList.getChildren().add(highscoreListText);
	}

	/*
	 * Makes the timer display. And updates every second
	 */
	public void startTimerSchedule() {
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {

						Text text = new Text();
						time.updateTime();

						long elapsedSeconds = time.getElapsedSeconds();
						long elapsedMinutes = time.getElapsedMinutes();

						String displayedTime = String.format("%d : %d", elapsedMinutes, elapsedSeconds);

						game.setDisplayedTime(displayedTime);

						text.setText(String.format("\n Time %s", displayedTime));
						text.setStyle("-fx-color: blue; -fx-font-size: 130%");
						timePane.getChildren().clear();
						timePane.getChildren().add(text);
					}
				});
			}
		}, 0, 1000);
	}

	public void disableButtons() {
		for (Node nodeButton : grid.getChildren()) {
			Button button = (Button) nodeButton;
			button.setDisable(true);
		}
	}

	public void addEventToButton(Button tile) {
		tile.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				resetFeedbackMessage();
				Button tile = (Button) e.getSource();
				String[] indexer = (tile.getId().split(",", 2));

				int x = Integer.parseInt(indexer[0]);
				int y = Integer.parseInt(indexer[1]);

				if (e.getButton() == MouseButton.PRIMARY) {
					game.revealTile(x, y, false);
				} else if (e.getButton() == MouseButton.SECONDARY) {
					game.flagTile(x, y);
				}
				if (game.getGameState() == -1) {
					gameLost();
				} else if (game.getGameState() == 1) {
					gameWon();
				}
				drawBoard();

			}
		});
	}

	public void drawBoard() {
		flagged();
		for (int y = 0; y < game.getSizeY(); y++) {
			for (int x = 0; x < game.getSizeX(); x++) {
				Button button = (Button) grid.getChildren().get(y * game.getSizeX() + x);
				Tile tile = game.getTile(x, y);
				button.setText(String.valueOf(game.getTile(x, y).getDisplay()));
				if (tile.isRevealed()) {
					paintTile(button, "white");
				} else {
					paintTile(button, "grey");
				}
			}
		}
	}

	public void paintTile(Button button, String color) {
		button.setStyle("-fx-background-color:" + color + ";" + "-fx-border-color: blue; -fx-text-fill: blue;");
	}

	public void filenameVisibility() {
		userName.setVisible(false);
		registerUsername.setVisible(false);
		save.setVisible(true);
		load.setVisible(true);
		filename.setVisible(true);
	}

	public void usernameVisibility() {
		userName.setText("Username");
		userName.setVisible(true);
		registerUsername.setVisible(true);
		save.setVisible(false);
		load.setVisible(false);
		filename.setVisible(false);
	}

	public void noVisibility() {
		userName.setVisible(false);
		registerUsername.setVisible(false);
		save.setVisible(false);
		load.setVisible(false);
		filename.setVisible(false);
	}

	public void makeErrorMessage(String error) {
		makeFeedbackMessage(error, "error");
	}

	public void makeSavedOrLoadedFeedback(String feedback) {
		makeFeedbackMessage(feedback, "savedOrLoaded");
	}

	public void makeSystemFeedback(String feedback) {
		makeFeedbackMessage(feedback, "systemFeedback");
	}

	public void makeFeedbackMessage(String message, String feedbackType) {
		Text text = new Text();
		text.setText(message);

		if (feedbackType.equals("error")) {
			text.setFill(Color.RED);
		} else if (feedbackType.equals("savedOrLoaded")) {
			text.setFill(Color.GREEN);
		} else if (feedbackType.equals("systemFeedback")) {
			text.setFill(Color.BLACK);
		}
		text.setStyle("-fx-font-size: 100%");
		text.setTextAlignment(TextAlignment.CENTER);
		feedbackContainer.getChildren().clear();
		feedbackContainer.getChildren().add(text);
	}

	public void resetFeedbackMessage() {
		feedbackContainer.getChildren().clear();
	}
}