package view;

import javax.swing.JFrame;

import constant.GameConstant;
import controller.Controller;

public class View extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2949165663709873410L;
	// GUI
    private BoardPanel boardPanel;
    // Convert data sent from controller into GUI.
    private Controller chessGame;

    public View(Controller chessGame)  {
        super("Chess");
        this.chessGame = chessGame;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(GameConstant.BOARD_PANEL_DIMENSION);
        this.setLocationRelativeTo(null);

        boardPanel = new BoardPanel(this.chessGame);
        this.add(boardPanel);
        this.pack();
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }
}
