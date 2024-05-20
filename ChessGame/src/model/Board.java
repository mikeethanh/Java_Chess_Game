//Quản lý bàn cờ, các vị trí và trạng thái của các ô cờ.
package model;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import constant.GameConstant;

public class Board implements Cloneable, Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3832214038983284610L;
	//Danh sách chứa các quân cờ hiện có trên bàn cờ
	private ArrayList<Piece> pieces = new ArrayList<>();;
	// Biến boolean để xác định lượt đi của người chơi (true là lượt của quân trắng, 
	//false là lượt của quân đen).
    private boolean whiteTurn;
    // To show checked king
    private Piece inCheck = null;
    //Trạng thái trước đó của bàn cờ, dùng để hỗ trợ tính năng undo.
    private Board previousState = null;

  

    // To show Piece moved last.
    private Piece lastMoved = null;
    // Đối tượng AI nếu có (cho phép chơi với máy).
    private Ai ai = null;

    public Board(boolean initPiece) {
        this.whiteTurn = true;
        //nếu initPiece là true thì các quân cờ sẽ được đặt vào vị trí ban đầu.
        if (initPiece) {
            // black pieces
            pieces.add(new Rook(new Point(0, 0), false));
            pieces.add(new Knight(new Point(1, 0), false));
            pieces.add(new Bishop(new Point(2, 0), false));
            pieces.add(new Queen(new Point(3, 0), false));
            pieces.add(new King(new Point(4, 0), false));
            pieces.add(new Bishop(new Point(5, 0), false));
            pieces.add(new Knight(new Point(6, 0), false));
            pieces.add(new Rook(new Point(7, 0), false));
            for (int col = 0; col < GameConstant.GAME_SIZE; col++) {
                pieces.add(new Pawn(new Point(col, 1), false));
            }
            // white player
            pieces.add(new Rook(new Point(0, 7), true));
            pieces.add(new Knight(new Point(1, 7), true));
            pieces.add(new Bishop(new Point(2, 7), true));
            pieces.add(new Queen(new Point(3, 7), true));
            pieces.add(new King(new Point(4, 7), true));
            pieces.add(new Bishop(new Point(5, 7), true));
            pieces.add(new Knight(new Point(6, 7), true));
            pieces.add(new Rook(new Point(7, 7), true));
            for (int col = 0; col < GameConstant.GAME_SIZE; col++) {
                pieces.add(new Pawn(new Point(col, 6), true));
            }
        }
    }

    //Hàm khởi tạo để tạo bản sao của bàn cờ, sao chép trạng thái hiện tại.
    private Board(boolean whiteTurn, Board previousState, List<Piece> pieces, Piece lastMoved, Piece inCheck, Ai ai) {
        this.whiteTurn = whiteTurn;
        this.previousState = previousState;
        for (Piece p : pieces) {
        	this.pieces.add(p.clone());
        }
        if (lastMoved != null)
            this.lastMoved = lastMoved.clone();
        if (inCheck != null)
        	this.inCheck = inCheck.clone();
        this.ai = ai;
    }
    
    //Tạo bản sao của đối tượng Board
    @Override
    public Board clone() {
        return new Board(whiteTurn, this.previousState, pieces, this.lastMoved, this.inCheck, this.ai);
    }
    
    //lay danh sach cac quan co
    public List<Piece> getAllPieces() {
    	return this.pieces;
    }
    
    //kiem tra luot cua quan trang
    public boolean isWhiteTurn() {
    	return this.whiteTurn;
    }
    
    //lay ra trang thai truoc do cua ban co
    public Board getPreviousState() {
        return previousState;
    }
    
    // lay ra quan co vua duoc di chuyen
    public Piece getLastMoved() {
    	return lastMoved;
    }
    
    // lấy quân vua đang bị chiế
    public Piece getInCheck() {
    	return inCheck;
    }
    // lấy quân cờ tại vị trí cụ thể
    public Piece getPieceAt(Point piecePosition) {
    	for (Piece piece : pieces) {
    		if (piece.getPiecePosition().x == piecePosition.x && piece.getPiecePosition().y == piecePosition.y)
    			return piece;
    	}
    	return null;
    }
    
    
    //xóa quân cờ
    public void removePiece(Piece p) {
    	if (pieces.contains(p)) {
    		pieces.remove(p);
    		return;
    	}
    }
    
    // lấy và đặt AI.
    public Ai getAi() {
        return ai;
    }

    public void setAi(Ai ai) {
        this.ai = ai;
    }


    // Thực hiện nước đi
    // humanMove to detect AI or human make move
    public void makeMove(Move move, boolean humanMove) {
        this.previousState = this.clone();
        // Thực hiện luật en passant: 
        // tất cả các quân tốt của bên hiện tại được đặt lại trạng thái "enPassantOK" về false,
        for (Piece piece : this.pieces)
            if (piece.isWhite() == this.whiteTurn && piece instanceof Pawn)
                ((Pawn) piece).enPassantOK = false;
        // Nước đi nhập thành: 
        //Nếu nước đi là một nước nhập thành (CastleMove), cả quân vua và quân xe đều được di chuyển đến vị trí mới của chúng.
        if (move instanceof CastleMove) {
            CastleMove castleMove = (CastleMove) move;
            castleMove.getMovedPiece().moveTo(castleMove.getMoveTo());
            castleMove.getRook().moveTo(castleMove.getRookMoveTo());
        } else {
            if (move.getCapturedPiece() != null) {
                this.removePiece(move.getCapturedPiece());
            }

            // implementing en passant rule: 
            // khi quân tốt mới vừa di chuyển hai ô
            if (move.getMovedPiece() instanceof Pawn) {
                if (Math.abs(move.getMovedPiece().getPiecePosition().y - move.getMoveTo().y) == 2) {
                    ((Pawn) move.getMovedPiece()).enPassantOK = true;
                }	
            }

            move.getMovedPiece().moveTo(move.getMoveTo());
            // Thăng cấp quân tốt
            if (move.getMovedPiece() instanceof Pawn) {
                checkPawnPromotion(move.getMovedPiece(), humanMove);
            }
        }
        
        //đổi lượt chơi
        
        // Update properties of board
        this.lastMoved = move.getMovedPiece();
        this.inCheck = this.kingInCheck();

        // Change turn
        this.whiteTurn = !whiteTurn;
    }

    // Check valid position
    //  Kiểm tra xem một vị trí có hợp lệ trên bàn cờ hay không
    public boolean ValidPos(Point spot) {
        return (spot.x >= 0 && spot.x < GameConstant.GAME_SIZE) && (spot.y >= 0 && spot.y < GameConstant.GAME_SIZE);
    }

    // Check if a move puts own king in check 
    // Kiểm tra nếu nước đi có thể làm vua bị chiếu.
    public boolean movePutsKingInCheck(Move move, boolean isWhite) {
        // Create the board after perform given move
        Board board = new Board(false);
        //tryMove(move) thực hiện nước đi trên bản sao của bàn cờ và trả về bản sao đã cập nhật.
        board = tryMove(move);

        // Go through all the opponent's piece
        for (Piece piece : board.getAllPieces()) {
            if (piece.white != isWhite) {
                // check whether piece capture king or not.
            	// tính toán tất cả các nước đi hợp lệ của từng quân cờ của đối phương.
                for (Move muv : piece.calculateLegalMoves(board, false))
                    // kiểm tra xem bất kỳ nước đi nào của đối phương có thể bắt vua hay không.
                    if (muv.getCapturedPiece() instanceof King) {
                        return true;
                    }
            }
        }
        return false;
    }

    // Thử nước đi trên bản sao bàn cờ
    public Board tryMove(Move move) {
        // creates a copy of the board
        Board copyBoard = this.clone();
        //Kiểm tra và thực hiện nước đi nhập thành (Castle Move)
        if (move instanceof CastleMove) {
            // creates a copy of the move for the copied board
            CastleMove c = (CastleMove) move;
            Piece king = copyBoard.getPieceAt(c.getMovedPiece().getPiecePosition());
            Piece rook = copyBoard.getPieceAt(c.getRook().getPiecePosition());

            // performs the move on the copied board
            copyBoard.makeMove(new CastleMove(king, c.getMoveTo(), rook, c.getRookMoveTo()), false);
            
        } 
        //Kiểm tra và thực hiện các loại nước đi khác
        else {
            // creates a copy of the move for the copied board
            Piece capture = null;
            if (move.getCapturedPiece() != null) {
                capture = copyBoard.getPieceAt(move.getCapturedPiece().getPiecePosition());
            }

            Piece moving = copyBoard.getPieceAt(move.getMovedPiece().getPiecePosition());

            // performs the move on the copied board
            copyBoard.makeMove(new Move(moving, move.getMoveTo(), capture), false);
        }

        // returns the copied board with the move executed
        return copyBoard;
    }
    
    // trong tình huống hiện tại trên bàn cờ, vua của đối phương có bị chiếu hay không
    private Piece kingInCheck() {
        for (Piece pc : pieces)
            for (Move mv : pc.calculateLegalMoves(this, false))
                if (mv.getCapturedPiece() instanceof King) {
                    this.inCheck = mv.getCapturedPiece();
                    return mv.getCapturedPiece();
                }
        return null;
    }

    private void checkPawnPromotion(Piece pawn, boolean humanMove) {
    	//kiểm tra xem quân cờ được truyền vào có phải là một quân tốt (Pawn) không và có đang ở hàng đầu hoặc hàng cuối của bàn cờ không
        if (pawn instanceof Pawn && (pawn.getPiecePosition().y == 0 || pawn.getPiecePosition().y == 7)) {
            Piece promotedPiece = null;

            // if ai, automatically promote to queen
            if (!humanMove || (this.ai != null && ai.isWhite() == pawn.isWhite())) {
                promotedPiece = new Queen(pawn.getPiecePosition(), pawn.isWhite());
            } else {
                // else, give the player a choice
                Object type = JOptionPane.showInputDialog(null, "", "Choose promotion:", JOptionPane.QUESTION_MESSAGE,
                        null, new Object[] { "Queen", "Rook", "Bishop", "Knight" }, "Queen");

                // will be null if JOptionPane is cancelled or close
                // default is to queen
                if (type == null) {
                    type = "Queen";
                }

                if (type.toString().equals("Queen")) {
                    promotedPiece = new Queen(pawn.getPiecePosition(), pawn.isWhite());
                } else if (type.toString().equals("Rook")) {
                    promotedPiece = new Rook(pawn.getPiecePosition(), pawn.isWhite());
                } else if (type.toString().equals("Bishop")) {
                    promotedPiece = new Bishop(pawn.getPiecePosition(), pawn.isWhite());
                } else {
                    promotedPiece = new Knight(pawn.getPiecePosition(), pawn.isWhite());
                }
            }

            // remove pawn and add promoted piece to board
            this.pieces.remove(pawn);
            this.pieces.add(promotedPiece);
        }
    }
    // kiểm tra xem trò chơi đã kết thúc hay chưa.
    public boolean EndGame() {
    	// lưu trữ tất cả các nước đi hợp lệ cho bên trắng và bên đen.
        List<Move> whiteMoves = new ArrayList<>();
        List<Move> blackMoves = new ArrayList<>();

        for (Piece piece : this.getAllPieces()) {
        	//tính toán các nước đi hợp lệ cho mỗi quân cờ.
            if (piece.white) {
                whiteMoves.addAll(piece.calculateLegalMoves(this, true));
            } else {
                blackMoves.addAll(piece.calculateLegalMoves(this, true));
            }
        }

        // Captured both checkmate case and stalemate case
        return (whiteMoves.size() == 0 || blackMoves.size() == 0);
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        String pieceString = new String();
        for (int row = 0; row < GameConstant.GAME_SIZE; row++) {
            for (int col = 0; col < GameConstant.GAME_SIZE; col++) {
            	//Đối với mỗi ô của bàn cờ, kiểm tra xem có quân cờ nào ở ô đó không ,
            	//Nếu không có quân cờ, sử dụng ký tự "-" để đại diện cho ô trống. 
            	//Nếu quân cờ là màu trắng, sử dụng ký tự thường của biểu diễn chuỗi của quân cờ. Ngược lại, 
            	//nếu quân cờ là màu đen, sử dụng ký tự in hoa của biểu diễn chuỗi của quân cờ.
                pieceString = (this.getPieceAt(new Point(col, row)) == null) ? "-"
                        : this.getPieceAt(new Point(col, row)).isWhite()
                        ? this.getPieceAt(new Point(col, row)).toString().toLowerCase()
                        : this.getPieceAt(new Point(col, row)).toString();
                string.append(pieceString + " ");
            }
            string.append("\n");
        }
        return string.toString();
    }
    
    //hiển thị trạng thái hiện tại của trò chơi dưới dạng văn bản trên màn hình console.
    public void showTextGame() {
        System.out.println(this.toString());
    }
}