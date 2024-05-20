package model;

import java.awt.Point;

//Class's purpose is only contain the info of the move
public class Move {
	//Đây là quân cờ được di chuyển trong nước đi.
	private Piece movedPiece; // The piece makes the move
	//Điểm đích của nước đi, nơi mà quân cờ sẽ được di chuyển tới.
	private Point destination; // The destination of the move
	// Đây là quân cờ bị bắt trong nước đi, nếu có
    private Piece capturedPiece; // The piece be captured
    
    //Constructor của lớp Move nhận ba tham số: quân cờ được di chuyển, điểm đích của nước đi và quân cờ bị bắt (nếu có).
    public Move(Piece movedPiece, Point destination, Piece capturedPiece) {
        this.movedPiece = movedPiece;
        this.destination = destination;
        this.capturedPiece = capturedPiece;
    }

    // Check if the "movedPiece" is null
    public boolean isMovedPieceNull() {
        return this.movedPiece == null;
    }

    public Piece getMovedPiece() {
        return movedPiece;
    }


    public Point getMoveTo() {
        return destination;
    }

    public Piece getCapturedPiece() {
    	return capturedPiece;
    }

    public void setCapturedPiece(Piece capturedPiece) {
        this.capturedPiece = capturedPiece;
    }

    public void setMovedPiece(Piece movedPiece) {
        this.movedPiece = movedPiece;
    }

    public void setMoveTo(Point destionation) {
        this.destination = destionation;
    }
    
    //Phương thức này trả về một chuỗi biểu diễn của đối tượng Move, bao gồm thông tin về quân cờ được di chuyển, quân cờ bị bắt và điểm đích của nước 
    @Override
    public String toString() {
        return "[movedPiece=" + movedPiece + ", capturedPiece=" + capturedPiece + ", moveTo=" + destination + "]";
    }
}
