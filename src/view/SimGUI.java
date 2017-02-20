package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SimGUI extends JFrame {

	private static final long serialVersionUID = 2L;

	private static final int BUTTON_SIZE = 15;
	private static final int delay = 5 * 100000;
	public JButton[][] buttons;
	private JPanel gamePanel;
	private JPanel panel;
	private JTextField label;
	private Cell[][] gV;

	/**
	 * Constructs a visual representation of the tissue
	 *
	 * @param size the length of one dimension of a square tissue sample.
	 * @param delay how long a cell waits before changing colors (in milliseconds)
	 */
	public SimGUI(int x, int y, PathFinderController controller) {
		super("PathFinder");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(y * BUTTON_SIZE, x * BUTTON_SIZE);

		gamePanel = new JPanel(new GridLayout(y, x));
		panel = new JPanel(new BorderLayout());
		buttons = new JButton[y][x];
		label = new JTextField("");
		//label.setSize(new Dimension(500, 330));

		for (int row = 0; row < y; row++){
			 for (int col = 0; col < x; col++) {
				JButton b = new PointJButton("", new Point(col,row));
				b.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(java.awt.event.ActionEvent e) {
						clicked(((PointJButton)(e.getSource())).pos);}});
				b.setBackground(Color.WHITE);
				buttons[row][col] = b;
				gamePanel.add(b);
			}
		}
		panel.add(gamePanel, BorderLayout.CENTER);
		panel.add(label, BorderLayout.PAGE_END);
		this.add(panel);
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
		if(getColor(row, col) == c)
			return;
		try{
		Thread.sleep(0, delay);}catch(Exception e){}
		buttons[row][col].setBackground(c);
		buttons[row][col].repaint();
	}

	public Color getColor(int row, int col){
		return buttons[row][col].getBackground();
	}

	private class PointJButton extends JButton{
		private static final long serialVersionUID = 1L;
		Point pos;

		public PointJButton(String s, Point p){
			super(s);
			pos = p;
		}
	}

	public void setVals(Cell[][] gV){
		this.gV = gV;
	}

	private void clicked(Point p){
		Cell c = gV[p.x][p.y];

		label.setText("Cell Position: [" + p.x + "," + p.y + "]"
				+ "\tG: " + c.g
				+ "\tH: " + c.h
				+ "\tF: " + c.f);
	}
}

