package fr.ludwig.minesweeper.test;

import org.junit.Test;

import fr.ludwig.minesweeper.Cell;
import junit.framework.TestCase;

public class TestCell extends TestCase
{
	@Test
	public void test_toText_1()
	{
		Cell cellTrapped = new Cell(null, 0, 0);
		cellTrapped.markAsTrapped();
		assertEquals(cellTrapped.toText(), Cell.CHAR_TRAP);
	}

}
