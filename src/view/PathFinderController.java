package view;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PathFinderController implements Initializable {

	private static SimGUI grid;
	private static Cell[][] gridVals;
	private static Point start, goal;
	private final static int MAXTRIALS = 2;
	private final static String path = "Trial Output\\";
	private static List<Point> centers = new ArrayList<Point>();
	private static HeuristicAlgorithm[] algorithms;
	
	@FXML
	protected Button begin;
	@FXML
	protected Button load;
	@FXML
	protected Button browse;
	@FXML
	protected Label label;
	@FXML
	protected TextField pathname;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		algorithms = new HeuristicAlgorithm[3];
		algorithms[0] = new UniformSearch();
	}
	
	public void start(){
		double a, b, c;
		
		begin.setDisable(true);
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		grid = new SimGUI (160, 120, 1);
		
		a = runTrials(0);
		b = runTrials(1);
		c = runTrials(2);
		
		begin.setDisable(false);
		
		label.setText("Average Costs:\n"
				+ "Uniform:\t " + a
				+ "A*:\t\t" + b
				+ "Weighted A*:\t" + c);
	}
	
	public void load(){
		
	}
	
	public void browse(){
		FileChooser fileChooser = new FileChooser();
		Stage stage = new Stage();
		File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            pathname.setText(file.getPath());
        }
	}
	
	private static double runTrials(int x){
		double total = 0;
		
		gridVals = new Cell[160][120];
			
		for(int i = 0; i < MAXTRIALS; i++){
			initGridVals();
			
			placeHardCells();
			while(!placePaths());
			placeBlockedCells();
			selectVertices();
			printGrid(x, i);
			
			total += algorithms[0].findPath(start, goal, gridVals, grid);
		}
		
		return total / MAXTRIALS;
	}
	
	private static void printGrid(int x, int count){
		String name, line;
		Point temp = null;
		
		switch(x){
		case 0: name = 		"Uniform Search\\Uniform-" 	+ count + ".txt";	break;
		case 1: name = 		"A-Star\\AStar-" 			+ count + ".txt";	break;
		default: name = 	"Weighted-A-Star\\WAStar-"	+ count + ".txt";	break;
		}
		
		FileWriter file;
		try {
			file = new FileWriter(path + name, false);
			line = "[" + start.x + "," + start.y + "]";
			file.write(line, 0, line.length());
			file.write(System.getProperty("line.separator"));
			line = "[" + goal.x + "," + goal.y + "]";
			file.write(line, 0, line.length());
			file.write(System.getProperty("line.separator"));
			
			for(int i = 0; i < centers.size(); i++){
				temp = centers.get(i);
				line = "[" + temp.x + "," + temp.y + "]";
				file.write(line, 0, line.length());
				file.write(System.getProperty("line.separator"));
			}
			
			for(int j = 0; j < 120; j++){
				for(int i = 0; i < 160; i++){
					if(!gridVals[i][j].path)
						file.write(""+gridVals[i][j].type, 0, 1);
					else if(gridVals[i][j].type == 1)
						file.write("a", 0, 1);
					else
						file.write("b", 0, 1);
				}
				file.write(System.getProperty("line.separator"));
			}
			
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void  selectVertices(){
		int x, y;
		boolean valid = false;
		
		while(!valid){
			x = (int)(40*Math.random());
			y = (int)(40*Math.random());
			
			if(x > 19)
				x += 120;
			if(y > 19)
				y += 80;
			
			if(gridVals[x][y].type != 0){
				valid = true;
				start = new Point(x,y);
				gridVals[x][y].route = true;
				updateCell(x,y);			
			}
		}
		
		valid = false;
		while(!valid){
			x = (int)(40*Math.random());
			y = (int)(40*Math.random());
		
			if(x > 19)
				x += 120;
			if(y > 19)
				y += 80;
			
			if(gridVals[x][y].type != 0){
				int dX = start.x - x, dY = start.y - y;
				double d = Math.sqrt(dX*dX + dY*dY);
				if(d >= 100){
					valid = true;
					goal = new Point(x,y);
					gridVals[x][y].route = true;
					updateCell(x,y);	
				}		
			}
		}
	}
	
	private static void  placeBlockedCells(){
		int x, y;
		
		for(int i = 0; i < 3840; i++){
			x = (int)(160*Math.random());
			y = (int)(120*Math.random());
			
			if(gridVals[x][y].type == 0 || gridVals[x][y].path)
				i--;
			else{
				gridVals[x][y].type = 0;
				updateCell(x,y);
			}
		}
	}
	
	private static boolean placePaths(){
		boolean valid;
		int i = 0;
		
		for(int a = 0; a < 4; a++){
			valid = false;
			
			while(!valid){
				if(i == 25){
					unmarkAllPaths();
					return false;
				}
				switch ((int) (4*Math.random())){
				case 0: valid = drawPath(0,   (int)(120*Math.random()), 0, 0); break;
				case 1: valid = drawPath((int)(160*Math.random()),   0, 1, 0); break;
				case 2: valid = drawPath(159, (int)(120*Math.random()), 2, 0); break;
				case 3: valid = drawPath((int)(160*Math.random()), 119, 3, 0); break;
				}
				i++;
			}
		}
		
		updateGrid(1);
		return true;
	}

	private static boolean drawPath(int x, int y, int dir, int count){
		int c, newDir, x1, y1;
		double r;
		boolean valid;
		
		if(isClear(x, y, dir)){
			c = markPath(x, y, dir);
			
			switch (dir){
			case 0: x1 = x + (c-1); y1 = y; break;
			case 1: x1 = x; y1 = y + (c-1); break;
			case 2: x1 = x - (c-1); y1 = y; break;
			default: x1 = x; y1 = y - (c-1); break;
			}
				
			if(x1 == 0 || y1 == 0 || x1 == 159 || y1 == 119){
				if(c+count >= 100)
					return true;
				else
					unmarkPath(x, y, dir);
					return false;
			}else{
				r = Math.random();
				if(r < .6)
					newDir = dir;
				else if(r < .8)
					newDir = (dir+1) % 4;
				else
					newDir = (dir+3) % 4;
				
				switch (newDir){
				case 0:  valid = drawPath(x1 + 1, y1, 0, count + c); break;
				case 1:  valid = drawPath(x1, y1 + 1, 1, count + c); break;
				case 2:  valid = drawPath(x1 - 1, y1, 2, count + c); break;
				default: valid = drawPath(x1, y1 - 1, 3, count + c); break;
				}
				
				if(valid)
					return true;
				
				unmarkPath(x, y, dir);
				return false;
			}
		}else
			return false;
	}
	
	private static boolean isClear(int x, int y, int dir){
		int a = 1;
		
		if(dir > 1)
			a = -1;
			
		if(dir % 2 == 0){
			for(int i = 0; i < 20; i++){
				if(x+(i*a) < 0 || x+(i*a) > 159){
					return true;
				}else if(gridVals[x+(i*a)][y].path)
					return false;
			}
		}else{
			for(int i = 0; i < 20; i++){
				if(y+(i*a) < 0 || y+(i*a) > 119){
					return true;
				}else if(gridVals[x][y+(i*a)].path)
					return false;
			}
		}
		return true;
	}
	
	private static int markPath(int x, int y, int dir){
		int a = 1;
		
		if(dir > 1)
			a = -1;
			
		if(dir % 2 == 0){
			for(int i = 0; i < 20; i++){
				if(x+(i*a) < 0 || x+(i*a) > 159)
					return i;
				gridVals[x+(i*a)][y].path = true;
				//updateCell(x+(i*a),y,true);
			}
		}else{
			for(int i = 0; i < 20; i++){
				if(y+(i*a) < 0 || y+(i*a) > 119)
					return i;
				gridVals[x][y+(i*a)].path = true;
				//updateCell(x,y+(i*a),true);
			}
		}
		return 20;
	}
	
	private static void unmarkPath(int x, int y, int dir){
		int a = 1;
		
		if(dir > 1)
			a = -1;
			
		if(dir % 2 == 0){
			for(int i = 0; i < 20; i++){
				if(x+(i*a) < 0 || x+(i*a) > 159)
					return;
				gridVals[x+(i*a)][y].path = false;
				updateCell(x+(i*a),y);
			}
		}else{
			for(int i = 0; i < 20; i++){
				if(y+(i*a) < 0 || y+(i*a) > 119)
					return;
				gridVals[x][y+(i*a)].path = false;
				updateCell(x,y+(i*a));
			}
		}
		return;
	}
	
	private static void unmarkAllPaths(){
		for(int i = 0; i < 160; i++)
			for(int j = 0; j < 120; j++)
				if(gridVals[i][j].path){
					gridVals[i][j].path = false;
					//updateCell(i,j,true);
				}
	}
	
	private static void placeHardCells(){
		int x, y;
		
		for(int a = 0; a < 8; a++){
			x = (int) (Math.random() * 160);
			y = (int) (Math.random() * 120);
			
			centers.add(new Point(x,y));
			
			for(int i = x-16; i < x+16 && i < 160; i++){
				for(int j = y-16; j < y+16 && j < 120; j++){
					if(i < 0)
						break;
					else if(j < 0)
						continue;
					gridVals[i][j].type = (short)(2*Math.random() + 1);
				}
			}
		}
		updateGrid(0);
	}
	
	private static void updateGrid(int mode){
		for(int i = 0; i < 160; i++)
			for(int j = 0; j < 120; j++){
				if(mode == 1){
					if(gridVals[i][j].path)
						updateCell(i,j);
					continue;
				}
				updateCell(i,j);
			}
	}
	
	private static void updateCell(int i, int j){
		if(gridVals[i][j].route){
			if(grid.getColor(j, i) != Color.RED)
				grid.setCell(j, i, Color.RED);
			return;
		}		
		if(gridVals[i][j].path){
			if(grid.getColor(j, i) != Color.BLUE)
				grid.setCell(j, i, Color.BLUE);
			return;
		}
		switch(gridVals[i][j].type){
		case 0: 
			if(grid.getColor(j, i) != Color.BLACK)
				grid.setCell(j, i, Color.BLACK); break;
		case 1: 
			if(grid.getColor(j, i) != Color.WHITE)
				grid.setCell(j, i, Color.WHITE); break;		
		case 2:
			if(grid.getColor(j, i) != Color.GRAY)
				grid.setCell(j, i, Color.GRAY); break;
		}
	}
	
	private static void initGridVals(){
		for(int i = 0; i < 160; i++)
			for(int j = 0; j < 120; j++)
					gridVals[i][j] = new Cell();
	}
}
