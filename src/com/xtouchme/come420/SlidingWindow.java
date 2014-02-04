package com.xtouchme.come420;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.xtouchme.gamebase.entities.Entity;
import com.xtouchme.gamebase.managers.EntityManager;
import com.xtouchme.gamebase.managers.ResourceManager;

public class SlidingWindow extends Entity {

	//TODO: GO-BACK-N ARQ Logic
	public enum Type {
		SENDER, RECEIVER
	}
	
	private EntityManager em = EntityManager.getInstance();
	
	private List<StaticFrame>	frames;
	private Window				window;
	private Type				type;
	
	public SlidingWindow(float x, float y) {
		super(x, y);
		
		frames = new ArrayList<>();
		window = new Window(x, y).setSize(5);
		
		//fillFrames(15);
		em.add(window);
	}

	public SlidingWindow setType(Type type) {
		this.type = type;
		return this;
	}
	
	@Override
	public void render(Graphics2D g) {
		// super.render(g);
		
		Font def = g.getFont();
		Font font = ResourceManager.getInstance(null).getFont(Font.TRUETYPE_FONT, "Boxy-Bold.ttf", 16);
		g.setFont(font);
		switch(type) {
		case SENDER:
			g.drawString("Sender", x() - 8, y() - 24);
			break;
		case RECEIVER:
			g.drawString("Receiver", x() - 8, y() + 36);
			break;
		}
		g.setFont(def);
	}
	
	public SlidingWindow fillFrames(int size) {
		for(int i = 0; i < size; i++)
			frames.add(new StaticFrame(x() + 16 * i, y()));
		
		for(StaticFrame f : frames) em.add(f);
		return this;
	}
	
	public Window window() {
		return window;
	}
	
	public StaticFrame frame(int index) {
		return frames.get(index);
	}
}
