package Model;

import java.util.ArrayList;
import java.util.HashMap;

public class testy{
	public static void main(String args[]) {
		Move move = new Move();
		move.addPiece(GamePiece.getPiece(), 1, 1);
		move.addPiece(GamePiece.getPiece(), 2, 1);
		move.addPiece(GamePiece.getPiece(), 3, 1);
		move.addPiece(GamePiece.getPiece(), 4, 1);
		move.addPiece(GamePiece.getPiece(), 5, 1);
		ArrayList<Position> positions = move.getPositionsbyY();
		for (Position position : positions) {
			System.out.println(move.getDirection() + " " + position.getX() + " " + position.getY() + " " + move.getMove().get(position));
			System.out.println(move.getStartPosition().getX() + " " + move.getStartPosition().getY());
		}
	}
}