package view;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SimGUI extends JFrame {
	
	private static final long serialVersionUID = 2L;
	
	private int delay;
	//private boolean isEnabled;
	
	private static final int BUTTON_SIZE = 15;	
	private JButton[][] buttons;
	private JPanel gamePanel;

	/**
	 * Constructs a visual representation of the tissue
	 * 
	 * @param size the length of one dimension of a square tissue sample.
	 * @param delay how long a cell waits before changing colors (in milliseconds)
	 */
	public SimGUI(int x, int y, int delay) {
		super("PathFinder");
		//this.isEnabled = true;
		this.delay = delay;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(y * BUTTON_SIZE, x * BUTTON_SIZE);
		
		gamePanel = new JPanel(new GridLayout(y, x));
		buttons = new JButton[y][x];

		for (int row = 0; row < y; row++){
			 for (int col = 0; col < x; col++) {
				JButton b = new JButton("");
				b.setBackground(Color.WHITE);
				buttons[row][col] = b;
				gamePanel.add(b);
			}
		}

		this.add(gamePanel);
		this.setVisible(true);
	}

	/**
	* Sets the color of the cell located at a particular position
	* 
	* @param row the row position of target cell
	* @param col the col position of target cell
	* @param c the color you wish to change the cell to
	**/
	public void setCell(int row, int col, Color c){
		try{
		Thread.sleep(delay);}catch(Exception e){}
		buttons[row][col].setBackground(c);
		buttons[row][col].repaint();
	
	}
}

