package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

    /**
	 * 
	 */
	private static final long serialVersionUID = 223927281433952627L;
	//Constructor này khởi tạo một quân Tượng với vị trí và màu sắc xác định.
	public Bishop(Point piecePosition, boolean white) {
        this.piecePosition = piecePosition;
        this.white = white;
    }
	
	//Constructor này tương tự như constructor trước, nhưng cũng cho phép thiết lập trạng thái di chuyển đầu tiên của quân cờ.
	// Điều này có thể hữu ích trong việc áp dụng các luật riêng cho các quân cờ di chuyển lần đầu tiên, như di chuyển hai ô của quân tốt.
    public Bishop(Point piecePosition, boolean white, boolean firstMove) {
    	this.piecePosition = piecePosition;
    	this.white = white;
        this.firstMove = firstMove;
    }

    
    //Phương thức này tính toán tất cả các nước đi hợp lệ của quân Tượng trên bàn cờ.
    @Override
    public List<Move> calculateLegalMoves(Board board, boolean checkKing)  {
        //Tạo ra một danh sách moves để lưu trữ các nước đi hợp lệ.
    	List<Move> moves = new ArrayList<Move>();
        int x = piecePosition.x;
        int y = piecePosition.y;
        // if no board given, return empty list
        if (board == null) {
        	return moves;        	
        }

        int[] dx = { 1, -1, 1, -1 };
        int[] dy = { 1, 1, -1, -1 };
        // add moves in diagonal lines to the list
        
        //giúp tìm ra tất cả các ô trống hoặc có quân cờ đối phương trên các đường chéo mà quân Tượng có thể di chuyển.3.5
        for (int i = 0; i < 4; i++) {
            Point pos = new Point(x + dx[i], y + dy[i]);
            Piece oppositePiece;
            
            // để xác định các ô trống hoặc có quân cờ đối phương trên các đường chéo mà quân Tượng có thể di chuyển.
            while(board.ValidPos(pos)) {
            	oppositePiece = board.getPieceAt(pos);
                if(oppositePiece == null) {
                	moves.add(new Move(this, pos, oppositePiece)); 
                	//kiểm tra xem quân cờ đó có phải là quân cờ đối phương không. Nếu đúng, nó cũng sẽ thêm nước đi mới vào danh sách moves. Sau đó, nó sẽ dừng vòng lặp bằng cách sử dụng lệnh break
                } else if(oppositePiece.white != this.white) {
                    moves.add(new Move(this, pos, oppositePiece));
                    break;
                } else {
                	break;                	
                }

                pos = new Point(pos.x + dx[i], pos.y + dy[i]);
            }
        }


        // check that move doesn't put own king in check
        if(checkKing) {
        	this.removeMovesPutsKingInCheck(board, moves);        	
        }

        return moves;
    }
    
    @Override
    public String toString() {
        return "B";
    }
    
    @Override
    public Bishop clone() {
    	return new Bishop(this.piecePosition, this.white, this.firstMove);
	}

    @Override
    public String getImageName() {
    	return "Bishop";
	}
}
