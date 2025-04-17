package Model;

import java.util.HashMap;

public class testy{
	public static void main(String args[]) {
		HashMap<int[], GamePiece> map = new HashMap<int[], GamePiece>();
		GamePiece p1 = GamePiece.getPiece();
		int[] p1pos = {1,2};
		GamePiece p2 = GamePiece.getPiece();
		int[] p2pos = {3,4};
		GamePiece p3 = GamePiece.getPiece();
		int[] p3pos = {2,3};
		GamePiece p4 = GamePiece.getPiece();
		int[] p4pos = {4,5};
		map.put(p1pos, p1);
		map.put(p2pos, p2);
		map.put(p3pos, p3);
		map.put(p4pos, p4);
		for (int[] k : map.keySet()) {
			System.out.println("x: " + k[0] + " " + k[1] + " " + map.get(k));
		}
	}
}