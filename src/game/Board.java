package game;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Board {
	private CellType [][] cell = new CellType[3][3];
	private boolean done;
	private CellType winner;
	private int crossNumber, circleNumber;
	private JLabel [][] graphics = new JLabel[3][3];
	private static Font font = new Font("Arial", Font.BOLD, 40);
	private boolean withGraphics;
	
	public Board(boolean withGraphics) {
		crossNumber = 0;
		circleNumber = 0;
		done = false;
		winner = CellType.EMPTY;
		this.withGraphics = withGraphics;
		cellInitialization();
		if (withGraphics)
			graphicsInitialization();
	}
	
	private void cellInitialization() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				cell[i][j] = CellType.EMPTY;
			}
		}
	}
	
	private void graphicsInitialization() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				graphics[i][j] = new JLabel("");
				graphics[i][j].setBackground(Color.YELLOW);
				graphics[i][j].setOpaque(true);
				graphics[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				graphics[i][j].setVerticalAlignment(SwingConstants.CENTER);
				graphics[i][j].setFont(font);
			}
		}
	}
	
	public CellType getCell(int x, int y) {
		if (positionInRange(x, y))
			return cell[x][y];
		return CellType.BAD_POSITION;
	}
	
	public JLabel getGraphics(int x, int y) {
		if (positionInRange(x, y))
			return graphics[x][y];
		return null;
	}
	
	public boolean isDone() {
		return done;
	}
	
	public boolean isWinner() {
		if (CellType.isUsed(winner))
			return true;
		return false;
	}
	
	public CellType getWinner() {
		return winner;
	}
	
	public void setFont(String arg0, int arg1, int arg2) {
		font = new Font(arg0, arg1, arg2);
	}
	
	public void setCell(int x, int y, CellType type) {
		if (positionInRange(x, y)) {
			if (isCellEmpty(x, y)) {
				if (done)
					Statement.printProgrammerInfo("Done board - added new Cell.");
				cell[x][y] = type;
				if (type == CellType.CROSS) {
					++crossNumber;
					if (withGraphics) {
						graphics[x][y].setText("X");
						graphics[x][y].setForeground(Color.RED);
					}
				}
				else if (type == CellType.CIRCLE) {
					++circleNumber;
					if (withGraphics) {
						graphics[x][y].setText("O");
						graphics[x][y].setForeground(Color.BLUE);
					}
				}
				done = checkIfWinner();
				if (circleNumber + crossNumber == 9)
					done = true;
			}
		}
	}
	
	public void resetCell(int x, int y) {
		if (positionInRange(x, y)) {
			if (cell[x][y] == CellType.CROSS) {
				--crossNumber;
				winner = CellType.EMPTY;
				done = checkIfWinner();
			}
			else if (cell[x][y] == CellType.CIRCLE) {
				--circleNumber;
				winner = CellType.EMPTY;
				done = checkIfWinner();
			}
			cell[x][y] = CellType.EMPTY;
			if (withGraphics)
				graphics[x][y].setText("");
		}
	}
	
	public boolean isCellEmpty(int x, int y) {
		if (positionInRange(x, y)) {
			if (CellType.isEmpty(cell[x][y]))
				return true;
		}
		return false;
	}
	
	private boolean posXYInRange(int pos) {
		if (pos >= 0 && pos <= 2)
			return true;
		return false;
	}
	
	public boolean positionInRange(int x, int y) {
		if (posXYInRange(x) && posXYInRange(y))
			return true;
		Statement.printError("Board - position (" + x + ", " + y + ") is not in range!");
		return false;
	}
	
	public boolean checkIfWinner() {
		if (crossNumber < 3 && circleNumber < 3)
			return false;
		if (checkCrossWinner()) {
			winner = cell[1][1];
			Statement.printInfo("There is a winner on board!");
			return true;
		}
		for (int i = 0; i < 3; i++) {
			if (checkColumnWinner(i) || checkRowWinner(i)) {
				winner = cell[i][i];
				Statement.printInfo("There is a winner on board!");
				return true;
			}
		}
		return false;
	}
	
	private boolean checkColumnWinner(int column) {
		if (!posXYInRange(column)) {
			Statement.printError("Board - checkIfWinner - bad column: " + column + "!");
			return false;
		}
		if (CellType.isUsed(cell[column][0]) && cell[column][0].equals(cell[column][1]) && cell[column][1].equals(cell[column][2]))
			return true;
		return false;
	}
	
	private boolean checkRowWinner(int row) {
		if (!posXYInRange(row)) {
			Statement.printError("Board - checkIfWinner - bad row: " + row + "!");
			return false;
		}
		if (CellType.isUsed(cell[0][row]) && cell[0][row].equals(cell[1][row]) && cell[1][row].equals(cell[2][row]))
			return true;
		return false;
	}
	
	private boolean checkCrossWinner() {
		if (CellType.isUsed(cell[1][1])) { // central cell is used
			if (cell[0][0].equals(cell[1][1]) && cell[2][2].equals(cell[1][1]))
				return true;
			if (cell[2][0].equals(cell[1][1]) && cell[2][0].equals(cell[0][2]))
				return true;
		}
		return false;
	}
}
