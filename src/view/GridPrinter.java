package view;

import java.awt.Color;
import java.awt.Point;

public class GridPrinter implements Runnable {
	
	private final static int SLEEPTIME = 2;
	GPStruct[] db;
	SimGUI grid;
	
	public GridPrinter(GPStruct[] db, SimGUI grid){
		this.db = db;
		this.grid = grid;
	}

	@Override
	public void run() {
		GPStruct.PointNode ptr;
		for(int x = 0; x < db.length; x++){
			if(db[x] == null || !db[x].ready){
				try {
					synchronized(this){
						wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			for(int i = 0; i < 160; i++){
				for(int j = 0; j < 120; j++){
					updateCell(i,j, db[x].gV);
				}
			}
			
			for(ptr = db[x].head; ptr != null; ptr = ptr.next){
				grid.setCell(ptr.self.y, ptr.self.x, ptr.c);
			}
			
			try {
				Thread.sleep(SLEEPTIME*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void updateCell(int i, int j, Cell[][] gV){
		if(gV[i][j].route){
			if(grid.getColor(j, i) != Color.RED)
				grid.setCell(j, i, Color.RED);
			return;
		}
		if(gV[i][j].path){
			if(grid.getColor(j, i) != Color.BLUE)
				grid.setCell(j, i, Color.BLUE);
			return;
		}
		switch(gV[i][j].type){
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
	
	public static class GPStruct{
		boolean ready = false;
		Cell[][] gV;
		PointNode head, last;
		
		public GPStruct(Cell[][] gV){
			this.gV = gV;
			head = null;
			last = null;					
		}
		
		public void add(Point p, Color c){
			PointNode tmp = new PointNode(p, c);
			if(head == null){
				head = tmp;
				last = tmp;
			}else{
				last.next = tmp;
				last = last.next;
			}
		}
		
		public class PointNode {
			Point self;
			Color c;
			PointNode next;
			
			public PointNode(Point p, Color c){
				self = p;
				this.c = c;
			}
		}
	}
}
