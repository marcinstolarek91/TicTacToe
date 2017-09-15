package test;

import static org.junit.Assert.*;

import org.junit.Test;

import game.CellType;
import game.Game;

public class TestGame {
	Game game = new Game();
	
	@Test
	public void testRange1() {
		assertFalse(game.positionInRange(-1, 0));
	}
	
	@Test
	public void testRange2() {
		assertFalse(game.positionInRange(0, -1));
	}
	
	@Test
	public void testRange3() {
		assertFalse(game.positionInRange(3, -1));
	}
	
	@Test
	public void testRange4() {
		assertFalse(game.positionInRange(3, -0));
	}
	
	@Test
	public void testRange5() {
		assertTrue(game.positionInRange(2, 0));
	}
	
	@Test
	public void testChangeType() {
		CellType oldType = game.getTypeToMove();
		CellType newType;
		game.changeTypeToMove();
		newType = game.getTypeToMove();
		assertNotEquals(oldType, newType);
	}
	
	@Test
	public void testCanUseThisBoard1() {
		assertFalse(game.canUseThisBoard(-1, 0));
	}
	
	@Test
	public void testCanUseThisBoard2() {
		assertTrue(game.canUseThisBoard(2, 2));
	}
	
	@Test
	public void testCanUseThisBoard3() {
		game.setBoardToMoveX(1);
		game.setBoardToMoveY(1);
		assertFalse(game.canUseThisBoard(2, 2));
	}
	
	@Test
	public void testCanUseThisBoard4() {
		game.setBoardToMoveX(1);
		game.setBoardToMoveY(1);
		assertTrue(game.canUseThisBoard(1, 1));
	}

}
