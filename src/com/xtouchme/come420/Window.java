package com.xtouchme.come420;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.xtouchme.come420.Frame.Type;
import com.xtouchme.gamebase.entities.Entity;
import com.xtouchme.gamebase.managers.EntityManager;
import com.xtouchme.gamebase.managers.InputManager;

public class Window extends Entity {

	//TODO: Fix up receiveFrom()
	
	public enum Type {
		SENDER, RECEIVER
	}
	
	private int size;
	private int frameIndex;
	private int sendIndex;
	private Type type;
	private Rectangle2D window;
	
	public Window(float x, float y) {
		super(x, y);
		
		size = ARQManager.getInstance().windowSize();
		
		setIndex(0).setHeight(32);
		
		sendIndex = frameIndex;
		window = new Rectangle2D.Float(x() - 8 + (16 * frameIndex), y() - height()/2, width(), height());
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		
		EntityManager em = EntityManager.getInstance();
		InputManager im = InputManager.getInstance();
		
		//if(im.isKeyPressed("Send Data") && sendIndex < (size + frameIndex)) {
		if(type != Type.RECEIVER &&
		   im.isMouseClicked(MouseEvent.BUTTON1) &&
		   window.contains(new Point2D.Float(im.getMouseX(), im.getMouseY())) &&
		   sendIndex < (size + frameIndex)) {
			em.add(new Frame(x() + 16 * sendIndex, y() + 16).setType(Frame.Type.DATA));
			sendIndex++;
		}
	}
	
	@Override
	public void updateHitbox() {
		window.setRect(x() - 8 + (16 * frameIndex), y() - height()/2, width(), height());
	}
	
	@Override
	public void render(Graphics2D g) {
		super.render(g);
		
		g.draw(window);
	}
	
	public Window setType(Type type) {
		this.type = type;
		return this;
	}
	
	public Window setIndex(int frameIndex) {
		this.frameIndex = frameIndex;
		return this;
	}
	
	public int frameIndex() {
		return frameIndex;
	}
	
	public Window slideWindow() {
		ARQManager arq = ARQManager.getInstance();
		int move = 0;
		
		switch(type) {
		case SENDER:
			for(int i = 0; i < size; i++, move++) {
				if(arq.sender().frame(i + frameIndex).type() != StaticFrame.Type.RECEIVED) break;
			}
			break;
		case RECEIVER:
			for(int i = 0; i < size; i++, move++) {
				if(arq.receiver().frame(i + frameIndex).type() != StaticFrame.Type.RECEIVED) break;
			}
			break;
		}
		setIndex(frameIndex + move);
		return this;
	}
	
	public void resendFrom(int frameIndex) {
		EntityManager em = EntityManager.getInstance();
		
		switch(type) {
		case SENDER:
			for(int i = frameIndex; i < size; i++) {
				em.add(new Frame(x() + 16 * frameIndex, y() + 16).setType(Frame.Type.DATA));
			}
			break;
		case RECEIVER:
			for(int i = frameIndex; i < size; i++) {
			
			}
			break;
		}
		
	}
	
	public Window setSize(int size) {
		this.size = size;
		setWidth(16 * size);
		return this;
	}
	
	public int size() {
		return size;
	}
}
