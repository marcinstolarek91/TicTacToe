package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import game.Board;
import game.CellType;

public class TestBoard{
	private Board boardWin;
	private Board boardNotWin;

	@Before
	public void setUp() {
		boardWin = new Board(false);
		boardNotWin = new Board(false);
		boardWin.setCell(0, 0, CellType.CROSS);
		boardWin.setCell(1, 1, CellType.CROSS);
		boardWin.setCell(2, 2, CellType.CROSS);
		boardWin.setCell(0, 1, CellType.CIRCLE);
		boardWin.setCell(0, 2, CellType.CIRCLE);
		boardNotWin.setCell(0, 0, CellType.CROSS);
		boardNotWin.setCell(0, 1, CellType.CROSS);
		boardNotWin.setCell(0, 2, CellType.CIRCLE);
		boardNotWin.setCell(1, 0, CellType.CIRCLE);
		boardNotWin.setCell(1, 1, CellType.CIRCLE);
		boardNotWin.setCell(1, 2, CellType.CROSS);
		boardNotWin.setCell(2, 0, CellType.CROSS);
		boardNotWin.setCell(2, 1, CellType.CIRCLE);
		boardNotWin.setCell(2, 2, CellType.CROSS);
	}
	
	@Test
	public void testRange1() {
		assertFalse(boardWin.positionInRange(-1, 0));
	}
	
	@Test
	public void testRange2() {
		assertFalse(boardWin.positionInRange(0, -1));
	}
	
	@Test
	public void testRange3() {
		assertFalse(boardWin.positionInRange(3, -1));
	}
	
	@Test
	public void testRange4() {
		assertFalse(boardWin.positionInRange(3, -0));
	}
	
	@Test
	public void testRange5() {
		assertTrue(boardWin.positionInRange(2, 0));
	}
	
	@Test
	public void testWinner1() {
		assertTrue(boardWin.isWinner());
	}
	
	@Test
	public void testWinner2() {
		assertFalse(boardNotWin.isWinner());
	}
	
	@Test
	public void testDone1() {
		assertTrue(boardWin.isDone());
	}
	
	@Test
	public void testDone2() {
		assertTrue(boardNotWin.isDone());
	}

}
