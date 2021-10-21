package minesweeper;

import static org.junit.jupiter.api.Assertions.*;


//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimeTest {
	@Test
	void testFlattenTime() {
		assertEquals("0011",Time.flattenTime("0:11"), "Time flattens the time wrong");
		assertEquals("1111", Time.flattenTime("11:11"), "Time flattens the time wrong");
	}
}
