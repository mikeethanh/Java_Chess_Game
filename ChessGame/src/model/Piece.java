package model;

import java.awt.Point;
import java.io.Serializable;
import java.util.List;
/*
- "abstract": it cannot be directly instantiated, and must be subclassed to be used
- "Cloneable": object can be cloned
- "Serializable": object can be serialized or converted into a stream of bytes
to be stored on disk or transmitted over a network.

By implementing these interfaces, the Piece class provides support for creating
new objects of the class through cloning, and also provides support for storing
or transmitting objects of the class in binary format.
 */


public abstract class Piece implements Cloneable, Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//đánh dấu xem quân cờ là màu trắng hay không.
	protected boolean white; 
	//vị trí của quân cờ trên bàn cờ
    protected Point piecePosition;
    //đánh dấu xem quân cờ đã di chuyển lần đầu tiên hay không.
    protected boolean firstMove = true;

    public boolean isWhite() {
    	return this.white;
    }

    public Point getPiecePosition() {
    	return piecePosition;
	}

    public boolean isFirstMove() {
    	return firstMove;
    }

    public void setWhite(boolean white) {
    	this.white = white;
    }

    public void setPiecePosition(Point piecePosition) {
    	this.piecePosition = piecePosition;
	}

    public void setFirstMove(boolean firstMove) {
    	this.firstMove = firstMove;
    }

    //  loại bỏ các nước đi khỏi danh sách nếu việc thực hiện nước đi đó khiến cho vua của bên di chuyển bị chiếu
    // đảm bảo tính toàn vẹn của trò chơi và người chơi không thể thực hiện bất kỳ nước đi nào đặt vua vào chiếu.
    protected void removeMovesPutsKingInCheck(Board board, List<Move> movesList) {
        for(int i = 0; i < movesList.size(); i++) {
        	//Kiểm tra nước đi có chiếu vua không
            if(board.movePutsKingInCheck(movesList.get(i), this.white)) {
                movesList.remove(movesList.get(i));
                i--;
            }
        }
    }

    // Move the piece to "destination"
    // di chuyển quân cờ tới vị trí mới.
    public void moveTo(Point destination) {
        this.firstMove = false;
        this.piecePosition = destination;
    }
    // Calculate all legal moves, and return a list contains these moves
    public abstract List<Move> calculateLegalMoves(Board board, boolean checkKing);
    public abstract String toString();
    public abstract String getImageName();
    //tạo ra một bản sao của đối tượng.
    @Override
    protected abstract Piece clone();
}

