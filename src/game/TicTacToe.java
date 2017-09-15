package game;

public class TicTacToe {
	public static final String version = "1.00.00";

	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new Game();	
			}
		});
	}

}
