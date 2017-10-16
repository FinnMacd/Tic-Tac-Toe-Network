package main;

import io.ExportNet;
import io.ImportNet;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import math.Matrix;
import network.Network;
import network.NetworkControler;


public class Main extends JPanel implements Runnable, MouseListener, MouseMotionListener{
	
	private static int width;
	
	private static double[] board;
	
	public static NetworkControler network;
	
	static Button train, run, save, load;
	static JFrame j;
	static JTextField textFeild;
	
	private Font font = new Font("Ariel",0,250);
	
	public int mx = 0,my = 0, lx,ly;
	
	public static void main(String[] args){
		
		j = new JFrame();
		Main main = new Main(3);
		j.setResizable(false);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j.setTitle("NN");
		
		j.add(main);
		j.pack();
		j.setLocationRelativeTo(null);
		
		j.setVisible(true);
		
	}
	
	public Main(int width){
		Main.width = width;
		
		board = new double[width*width];
		
		network = new NetworkControler(99);
		
		train = new Button("Train", 20, 700, 40);
		run = new Button("Run", 120, 700, 40);
		save = new Button("Save", 320, 700, 40);
		load = new Button("Load", 420, 700, 40);
		
		textFeild = new JFormattedTextField();
		textFeild.setBounds(220,670,80,40);
		textFeild.setFont(new Font("TimesNewRoman",0,40));
		
		
		this.add(textFeild);
		this.setLayout(null);
		setPreferredSize(new Dimension(680,760));
		
		setFocusable(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		new Thread(this).start();
		
		for(int i = 0;i < board.length; i++){
				
			board[i] = 0;
				
		}
		
	}
	
	public void run() {
		
		while(true){
			train.update(key == 1, mx, my);
			run.update(key == 1, mx, my);
			save.update(key == 1, mx, my);
			load.update(key == 1, mx, my);
			
			if(run.isClicked()){
				key = 0;
				
				double[] output = network.getTop().test(Matrix.rowMatrix(board)).getSingleArray();
				
				double jTop = -1, outTop = Double.MIN_NORMAL;
				
				for(int j = 0; j < board.length; j++){
					
					if(board[j] != 0)continue;
					
					if(output[j] > outTop){
						outTop = output[j];
						jTop = j;
					}
					
				}
				
				board[(int)jTop] = 1;
				
			}
			
			if(save.isClicked()){
				key = 0;
				try {
					ExportNet.saveGeneticNet("net", network);
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("saved");
				
			}
			
			if(load.isClicked()){
				key = 0;
				try {
					network = ImportNet.readGeneticNet("net");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				System.out.println("loaded");
			}
			
			if(train.isClicked()){
				key = 0;
				
				int limit = Integer.parseInt(textFeild.getText());
				
				
				for(int j = 0; j < limit; j++){
				
					if(limit>=100 && j%(limit/100) == 0)System.out.println(j*100/limit +"% done");
					
					network.generation(500);

				}
				
				System.out.println("trained");
				
			}
			
			
			j.repaint();
			
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		for(int x = 0; x < width; x++){
			for(int y = 0; y < width; y++){
				
				g.setColor(Color.white);
				g.fillRect(20 + x*(640/width), 20 + y*(640/width), (640/width), (640/width));
				g.setColor(Color.black);
				g.drawRect(20 + x*(640/width), 20 + y*(640/width), (640/width), (640/width));
				
				if(board[y*width + x] == -1){
					g.setFont(font);
					g.drawString("x", x*(640/width) + 70, y*(640/width) + 195);
				}else if(board[y*width + x] == 1){
					g.setFont(font);
					g.drawString("o", x*(640/width) + 70, y*(640/width) + 195);
				}
				
			}
		}
		
		train.draw((Graphics2D)g);
		run.draw((Graphics2D)g);
		save.draw((Graphics2D)g);
		load.draw((Graphics2D)g);
		
	}

	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {
		
		
	}

	public void mouseExited(MouseEvent e) {
		
	}
	
	static int key = 0;
	
	public void mousePressed(MouseEvent e) {
		key = e.getButton();
		
		int x = (int)((e.getX())/(640/width));
		int y = (int)((e.getY())/(640/width));
		
		mx = e.getX();
		my = e.getY();

		if(ly == y && lx == x)return;

		lx = x;
		ly = y;
		
		if(!(x >= 0 && x < width && y >= 0 && y < width))return;
		
		if(key == 1){
			
			board[y*width + x] = -1;
			
			
		}else{
			
			for(int i = 0; i < board.length; i++)board[i] = 0;
			
		}
		
	}

	public void mouseReleased(MouseEvent e) {
		key = 0;
		ly = lx = -1;
	}

	@Override
	public void mouseDragged(MouseEvent e){
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
		
	}
	
}
