

package minesweeper;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TileTest {
	private Tile tile;

	@BeforeEach
	void setUp(){
		tile = new Tile(0,0);
		tile.setType(' ');
	}

	@Test
	void testTileConstructor(){		
		assertThrows(IllegalArgumentException.class, () -> 
		new Tile(-1,0),
		"Cannot make a tile with negative X value"); 
		
		assertThrows(IllegalArgumentException.class, () -> 
		new Tile(0,-1),
		"Cannot make a tile with negative Y value");
	}
	@Test
	void testSetDisplay() {
		assertThrows(IllegalArgumentException.class, () -> 
		tile.setDisplay('H'),
		"Cannot set display to 'H' because 'H' is not the type nor 'F' which is the display of flagging a tile");
		
		//Can set display to 'F' because you can flag tile
		tile.setDisplay('F');
		assertEquals('F', tile.getDisplay(), "Tile display should be 'F' when set to 'F'");
		
		//Can set display to ' ' because type is ' '
		tile.setDisplay(' ');
		assertEquals(' ', tile.getDisplay(), "Tile display should be ' ' when set to ' '");
		
		//Setting display to not the type
		assertThrows(IllegalArgumentException.class, () -> 
		tile.setDisplay('1'),
		"Cannot set display to '1' because '1' is not the type nor 'F' which is the display of flagging a tile");
	}
	@Test
	void testSetType() {
		assertThrows(IllegalArgumentException.class, () -> 
		tile.setType('M'),
		"Cannot set type to 'M' because 'M' is not type bomb ('B') nor type empty (' ') nor a number from 1 to 9");
		
		//Tests that its illegal to set a type to 0
		assertThrows(IllegalArgumentException.class, () -> 
		tile.setType('0'),
		"Cannot set type to '0' because '0' is not type bomb ('B') nor type empty (' ') nor a number form 1 to 9");
		
		// Can set type to '3' because its a number
		tile.setType('3');
		assertEquals('3', tile.getType(), "Tile type should be '3' when set to '3'");
		
		// Can set type to 'B' because its type bomb
		tile.setType('B');
		assertEquals('B', tile.getType(), "Tile type should be 'B' when set to 'B'");
		
		// Can set type to ' ' because its type empty
		tile.setType(' ');
		assertEquals(' ', tile.getType(), "Tile type should be ' ' when set to ' '");
	}

}
