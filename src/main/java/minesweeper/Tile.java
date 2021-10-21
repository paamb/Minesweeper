package minesweeper;

public class Tile{
	final static char typeBomb = 'B';
	final static char typeEmpty = ' ';
	final static char typeFlag = 'F';
	private char display = typeEmpty;
	private char type = ' ';
	private boolean revealed = false;
	private boolean flagged = false;
	private final int x;
	private final int y;
	
	public Tile(int x, int y) {
		if (x < 0 || y < 0) {
			throw new IllegalArgumentException("Cannot initialize an tile object with negative x and y values");
		}
		this.x = x;
		this.y = y;
	}
	public static char getTypeBomb() {
		return typeBomb;
	}
	public void setTypeBomb() {
		this.type = typeBomb;
	}
	public static char getTypeEmpty() {
		return typeEmpty;
	}

	public char getDisplay() {
		return display;
	}

	public void setDisplay(char display) {
		if (display == this.type || display == typeFlag || display == typeEmpty) {
			this.display = display;
		}
		else {
			throw new IllegalArgumentException("Cannot set display to other than flag or type");
		}
		
	}

	public boolean isRevealed() {
		return revealed;
	}

	public void setRevealed() {
		this.revealed = true;
	}

	public void setTypeEmpty() {
		this.type = typeEmpty;
	}
	public char getType() {
		return type;
	}

	public void setType(char type) {
		if (type == typeBomb || type == typeEmpty || String.valueOf(type).matches("[1-9]")) {
			this.type = type;
		}
		else {
			throw new IllegalArgumentException("Cannot set type to other than number, bomb or empty");
		}
	}

	public boolean isFlagged() {
		return flagged;
	}
	public void setFlagged(boolean flagged) {
		this.flagged = flagged;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
