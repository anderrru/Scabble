package Model;

import java.util.ArrayList;
import java.util.HashMap;

public class testy{
	public static void main(String args[]) {
		Move move = new Move();
		move.addPiece(GamePiece.getPiece(), 1, 1);
		move.addPiece(GamePiece.getPiece(), 1, 2);
		move.addPiece(GamePiece.getPiece(), 1, 3);
		move.addPiece(GamePiece.getPiece(), 1, 4);
		move.addPiece(GamePiece.getPiece(), 1, 5);
		ArrayList<Position> positions = move.getPositionsbyY();
		for (Position position : positions) {
			System.out.println(move.getMove().get(position));
		}
	}
}