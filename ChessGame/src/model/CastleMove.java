package model;

import java.awt.Point;

// là một lớp con của lớp
public class CastleMove extends Move {
	//Đây là quân xe (rook) được di chuyển cùng với vua trong nước đi "castle".
	 private Piece rook;
	 // Điểm đích của nước đi cho quân xe trong nước đi "castle".
	 private Point moveRookTo;
	 
	 // quân vua, điểm đích của nước đi cho vua, quân xe và điểm đích của nước đi cho quân xe.
	 public CastleMove(Piece king, Point moveKingTo, Piece rook, Point moveRookTo) {
	     super(king, moveKingTo, null);
	     this.moveRookTo = moveRookTo;
	     this.rook = rook;
	 }
	
	 public Point getRookMoveTo() {
	     return moveRookTo;
	 }
	
	 public Piece getRook() {
	     return rook;
	 }
}
