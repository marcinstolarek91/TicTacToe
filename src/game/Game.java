package game;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public final class Game {
	private GameWindow gameWindow;
	private Board [][] board;
	private int boardToMoveX, boardToMoveY; // - 1 all available
	private CellType typeToMove; // actual type
	private Board bigBoard = new Board(false);
	private int lastBoardToMoveX, lastBoardToMoveY;
	private int lastUsedBigBoardX, lastUsedBigBoardY;
	private int lastUsedSmallBoardX, lastUsedSmallBoardY;
	private boolean canUndo;
	
	public Game() {
		boardToMoveX = -1;
		boardToMoveY = -1;
		typeToMove = CellType.CROSS;
		saveLastMove(false, 0, 0, 0, 0);
		boardInitialization();
		gameWindow = new GameWindow();
		gameWindow.setBackgroundColor(Color.RED);
		gameWindow.setToolTip(typeToMove);
	}
	
	public CellType getTypeToMove() {
		return typeToMove;
	}
	
	public void setBoardToMoveX(int x) {
		if (posXYInRange(x))
			boardToMoveX = x;
	}
	
	public void setBoardToMoveY(int y) {
		if (posXYInRange(y))
			boardToMoveY = y;
	}
	
	private void boardInitialization() {
		board = new Board[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				board[i][j] = new Board(true);
			}
		}
	}
	
	private void saveLastMove(boolean allowToUndo, int bigBoardX, int bigBoardY, int smallBoardX, int smallBoardY) {
		lastBoardToMoveX = boardToMoveX;
		lastBoardToMoveY = boardToMoveY;
		lastUsedBigBoardX = bigBoardX;
		lastUsedBigBoardY = bigBoardY;
		lastUsedSmallBoardX = smallBoardX;
		lastUsedSmallBoardY = smallBoardY;
		canUndo = allowToUndo;
	}
	
	public void changeTypeToMove() {
		if (typeToMove == CellType.CROSS) {
			typeToMove = CellType.CIRCLE;
			//gameWindow.setBackgroundColor(Color.BLUE);
		}
		else {
			typeToMove = CellType.CROSS;
			//gameWindow.setBackgroundColor(Color.RED);
		}
		gameWindow.setToolTip(typeToMove);
	}
	
	/**
	 * 
	 * @param newX - board X pos to check
	 * @param newY - board Y pos to check
	 * if board(x, y) isn't done - it has to be next move area
	 */
	public void changeBoardToMove(int newX, int newY) {
		if (boardToMoveX != -1 && boardToMoveY != -1) {
			if (!board[boardToMoveX][boardToMoveY].isWinner())
				gameWindow.colorBoard(boardToMoveX, boardToMoveY, Color.GRAY); // return to old color
		}
		if ((newX == -1 && newY == -1) || board[newX][newY].isDone()) {
			boardToMoveX = -1;
			boardToMoveY = -1;
			Statement.printProgrammerInfo("Next move can be anywhere.");
		}
		else {
			boardToMoveX = newX;
			boardToMoveY = newY;
			Statement.printProgrammerInfo("Next move has to be at (" + newX + ", " + newY + ").");
			if (!board[newX][newY].isWinner())
				gameWindow.colorBoard(newX, newY, Color.GREEN);
		}
	}
	
	/**
	 * 
	 * @return true if player can move at each board
	 */
	private boolean canUseAnyBoard() {
		if (boardToMoveX == -1 && boardToMoveY == -1)
			return true;
		return false;
	}
	
	public boolean canUseThisBoard(int x, int y) {
		if (positionInRange(x, y)) {
			if (canUseAnyBoard() || (boardToMoveX == x && boardToMoveY == y))
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
		Statement.printError("Game - position (" + x + ", " + y + ") is not in range!");
		return false;
	}
	
	public void undoLastMove() {
		if (!canUndo)
			return;
		board[lastUsedBigBoardX][lastUsedBigBoardY].resetCell(lastUsedSmallBoardX, lastUsedSmallBoardY);
		if (!board[lastUsedBigBoardX][lastUsedBigBoardY].isWinner()) // no winner on board - reset cell on big board
			bigBoard.resetCell(lastUsedBigBoardX, lastUsedBigBoardY);
		gameWindow.colorBoard(lastUsedBigBoardX, lastUsedBigBoardY, Color.GRAY);
		changeBoardToMove(lastBoardToMoveX, lastBoardToMoveY);
		changeTypeToMove();
		canUndo = false;
	}
	
	private void move(int xBigBoard, int yBigBoard, int xSmallBoard, int ySmallBoard) {
		if (canUseThisBoard(xBigBoard, yBigBoard) && positionInRange(xSmallBoard, ySmallBoard)) {
			if (board[xBigBoard][yBigBoard].isCellEmpty(xSmallBoard, ySmallBoard)) {
				board[xBigBoard][yBigBoard].setCell(xSmallBoard, ySmallBoard, typeToMove);
				if (board[xBigBoard][yBigBoard].isWinner()) { // big board - new element
					bigBoard.setCell(xBigBoard, yBigBoard, board[xBigBoard][yBigBoard].getWinner());
					if (board[xBigBoard][yBigBoard].getWinner() == CellType.CROSS)
						gameWindow.colorBoard(xBigBoard, yBigBoard, Color.RED);
					else
						gameWindow.colorBoard(xBigBoard, yBigBoard, Color.BLUE);
				}
				changeTypeToMove();
				saveLastMove(true, xBigBoard, yBigBoard, xSmallBoard, ySmallBoard);
				changeBoardToMove(xSmallBoard, ySmallBoard);
				if (bigBoard.checkIfWinner())
					endOfTheGame(bigBoard.getWinner().name() + " wins!");
				else if (bigBoard.isDone())
					endOfTheGame("The end of game - there is no winner!");
			}
		}
	}
	
	public void mouseHandler(int xMouse, int yMouse) {
		int xBigBoard = xMouse / 150;
		int yBigBoard = yMouse / 150;
		int xSmallBoard = (xMouse / 50) % 3;
		int ySmallBoard = (yMouse / 50) % 3;
		Statement.printProgrammerInfo("Mouse (real pos) released at (" + xMouse + ", " + yMouse + ").");
		Statement.printProgrammerInfo("Mouse released at big board (" + xBigBoard + ", " + yBigBoard + ").");
		Statement.printProgrammerInfo("Mouse released at small board (" + xSmallBoard + ", " + ySmallBoard + ").");
		move(xBigBoard, yBigBoard, xSmallBoard, ySmallBoard);
	}
	
	public void endOfTheGame(String textWinner) {
		final class EndText extends JFrame implements ActionListener {
			private JLabel text = new JLabel(textWinner);
			private Button button = new Button("Exit");
			public EndText() {
				super("The end of game");
				setLayout(new GridLayout(1,2));
				setVisible(true);
				setSize(500,200);
				text.setFont(new Font("Arial", Font.BOLD, 12));
				add(text);
				add(button);
				button.addActionListener(this);
			}
			
			public void exit() {
				this.processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
				System.exit(0);
			}
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getSource() == button)
					exit();
			}
		}
		
		EndText endText = new EndText();
	}
	
	public class GameWindow extends JFrame implements MouseListener, KeyListener {
		private JPanel [][] bigBoardGraphics = new JPanel [3][3];
		private JLabel toolTip = new JLabel("");
		
		public GameWindow() {
			super("Tic Tac Toe");			
			addMouseListener(this);
			addKeyListener(this);
			windowInitialization();
			bigBoardGraphicsInitialization();
			textInitialization();
		}
		
		private void windowInitialization() {
			GridLayout grid = new GridLayout(4,3);
			grid.setHgap(10);
			grid.setVgap(10);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setResizable(false);
			setVisible(true);
			setLayout(grid);
			setLocation(0, 0);
			setSize(450 + 3, 600 + 25);
			setBackgroundColor(Color.WHITE);
		}
		
		private void textInitialization() {
			JLabel text1 = new JLabel("Next");
			JLabel text2 = new JLabel("move:");
			Font font = new Font("Arial", Font.BOLD, 40);
			text1.setFont(font);
			text1.setHorizontalAlignment(SwingConstants.RIGHT);
			add(text1);
			text2.setFont(font);
			add(text2);
			toolTip.setFont(font);
			toolTip.setHorizontalAlignment(SwingConstants.LEFT);
			add(toolTip);
		}
		
		public void setToolTip(CellType type) {
			if (type == CellType.CROSS) {
				toolTip.setForeground(Color.RED);
				toolTip.setText("X");
			}
			else if (type == CellType.CIRCLE) {
				toolTip.setForeground(Color.BLUE);
				toolTip.setText("O");
			}
		}
		
		public void setBackgroundColor(Color color) {
			setBackground(color);
		}
		
		private void bigBoardGraphicsInitialization() {
			GridLayout grid = new GridLayout(3,3);
			grid.setHgap(5);
			grid.setVgap(5);
			for (int j = 0; j < 3; j++) {
				for (int i = 0; i < 3; i++) {
					bigBoardGraphics[i][j] = new JPanel(grid);
					bigBoardGraphics[i][j].setBackground(Color.GRAY);
					this.add(bigBoardGraphics[i][j]);
					smallBoardGraphicsInitialization(i, j);
				}
			}
		}
		
		private void smallBoardGraphicsInitialization(int x, int y) {
			if (positionInRange(x, y)) {
				for (int j = 0; j < 3; j++) {
					for (int i = 0; i < 3; i++)
						bigBoardGraphics[x][y].add(board[x][y].getGraphics(i, j));
				}
			}
		}
		
		public void colorBoard(int x, int y, Color color) {
			if (!positionInRange(x, y))
				return;
			bigBoardGraphics[x][y].setBackground(color);
		}
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent event) {
			mouseHandler(event.getX() - 3, event.getY() - 25);
		}

		@Override
		public void keyPressed(KeyEvent key) {
			if (key.getKeyCode() == KeyEvent.VK_BACK_SPACE)
				undoLastMove();
		}

		@Override
		public void keyReleased(KeyEvent arg0) {

		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			
		}
		
		
	}
}
