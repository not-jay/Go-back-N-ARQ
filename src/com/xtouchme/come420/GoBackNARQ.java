package com.xtouchme.come420;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.xtouchme.gamebase.managers.EntityManager;
import com.xtouchme.gamebase.managers.InputManager;
import com.xtouchme.gamebase.managers.ResourceManager;
import com.xtouchme.gamebase.managers.SettingsManager;

public class GoBackNARQ extends Applet implements Runnable, KeyListener, MouseListener {
	
	private static final long serialVersionUID = 959084623062324290L;
	
	private ARQManager am;
	private EntityManager em;
	private InputManager im;
	
	//For double buffering
	private Image image;
	private Graphics second;
	
	//For delta
	long lastFrame;
	
	/**
	 * I believe mau ni ang method flow for Applets
	 * 
	 * Applet#init() - called first by browsers/applet viewers
	 * Applet#start() - next call sa Applet class
	 * Thread#start() - called from the Applet#start() na method
	 * 				  - internally calls Thread#run() method
	 * Thread#run() - "Game Loop" nato mag cge ra ni ug call sa EntityManager#update()
	 * 				- and Applet#repaint(), which in turn internally calls Applet#paint() and Applet#update() (wala ko'y sure asa jd ni siya gi call gikan)
	 * Applet#paint() - "Main render" method, dri i call ang most sa drawing functions like EntityManager#render()
	 */
	
	@Override
	public void init() {
		//Initialize window
		setSize(720, 480);
		setBackground(Color.white);
		setFocusable(true);
		addKeyListener(this);
		addMouseListener(this);
		
//		Frame frame = (Frame) this.getParent().getParent();
//		frame.setTitle("Yet Another SHMUP");
		
		//Initialize managers
		ResourceManager.getInstance(getDocumentBase(), "data"); //Holds methods for getting resources, initialized with the "data" folder
		SettingsManager.getInstance().setResolution(getWidth(), getHeight()); //Holds settings, initialized with the window's width and height
		am = ARQManager.getInstance(); //Holds ARQ logic
		em = EntityManager.getInstance(); //Holds all the entities used in the program/game, handles the updating, rendering and collision detection of all entities.
		im = InputManager.getInstance(); //Handles the keyboard and mouse input
		
		lastFrame = System.currentTimeMillis();
		
		em.add(new Title(getWidth()/2, 50)); //Adds the Title entity to the window
		am.setSender(new SlidingWindow(30, 165).setType(SlidingWindow.Type.SENDER).fillFrames(42));
		am.setReceiver(new SlidingWindow(30, 400).setType(SlidingWindow.Type.RECEIVER).fillFrames(42));
		
//		em.add(new com.xtouchme.come420.Frame(30, 181).setType(Type.DATA));
//		em.add(new com.xtouchme.come420.Frame(46, 181).setType(Type.DATA));
//		em.add(new com.xtouchme.come420.Frame(62, 181).setType(Type.DATA));
//		em.add(new com.xtouchme.come420.Frame(78, 181).setType(Type.DATA));
//		em.add(new com.xtouchme.come420.Frame(94, 181).setType(Type.DATA));
	}
	
	@Override
	public void start() {
		Thread main = new Thread(this);
		main.start();
	}
	
	@Override
	public void stop() {
		
	}
	
	@Override
	public void destroy() {
		
	}
	
	@Override
	public void run() {
		while(true) {
			em.update(delta());
			repaint();
			
			try {
				Thread.sleep(17);
			} catch(InterruptedException e) { e.printStackTrace(); }
		}
	}

	//Calculates the time it took from the last update() to the next, in milliseconds
	//Efficient update+render code should have at around 17-18ms (Due to the Thread.sleep(17);
	private int delta() {
		long time = System.currentTimeMillis();
		int delta = (int)(time - lastFrame);
		lastFrame = time;
		return delta;
	}
	
	@Override
	public void paint(Graphics graphics) {
		Graphics2D g = (Graphics2D)graphics;
		em.render(g);
	}
	
	//Wala sd kaayo ko'y sure how it works jd, pero it's for double buffering
	//Basically reduces flicker per frame update (Ayaw lng labuta ug sabot, kay insignificant ra ni siya haha)
	//But kung ganahan ka mu read up: http://en.wikipedia.org/wiki/Multiple_buffering
	@Override
	public void update(Graphics g) {
		if(image == null) {
			image = createImage(getWidth(), getHeight());
			second = image.getGraphics();
		}
		
		second.setColor(getBackground());
		second.fillRect(0, 0, getWidth(), getHeight());
		second.setColor(getForeground());
		paint(second);
		
		g.drawImage(image, 0, 0, this);
	}
	
	//Delegate keylistener and mouselistener methods to the input manager
	@Override
	public void keyPressed(KeyEvent e) {
		im.onKeyPress(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		im.onKeyRelease(e);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		im.onKeyType(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		im.onMouseClicked(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		im.onMouseEntered(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		im.onMouseExited(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		im.onMousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		im.onMouseReleased(e);
	}
}
