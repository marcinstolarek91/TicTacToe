package game;

public enum CellType {
	EMPTY,
	CROSS,
	CIRCLE,
	BAD_POSITION;
	
	public static boolean isEmpty(CellType cell) {
		if (cell == EMPTY)
			return true;
		return false;
	}
	
	/**
	 * 
	 * @param cell
	 * @return if is CROSS or CIRCLE (used in game)
	 */
	public static boolean isUsed(CellType cell) {
		if (cell == CROSS || cell == CIRCLE)
			return true;
		return false;
	}
}
