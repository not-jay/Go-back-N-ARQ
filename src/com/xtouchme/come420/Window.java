package com.xtouchme.come420;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

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
	private int sent;
	private boolean clicked;
	private Type type;
	private Rectangle2D window;
	
	public Window(float x, float y) {
		super(x, y);
		
		size = ARQManager.getInstance().windowSize();
		clicked = false;
		
		setIndex(0).setHeight(32);
		
		sendIndex = sent = frameIndex;
		window = new Rectangle2D.Float(x() - 8 + (16 * frameIndex), y() - height()/2, width(), height());
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		
		EntityManager em = EntityManager.getInstance();
		InputManager im = InputManager.getInstance();
		ARQManager arq = ARQManager.getInstance();
		
		setSize(arq.windowSize());
		
		if(!im.isMouseDown(MouseEvent.BUTTON1)) clicked = false;
		
		//if(im.isKeyPressed("Send Data") && sendIndex < (size + frameIndex)) {
		if(type != Type.RECEIVER &&
		   im.isMouseDown(MouseEvent.BUTTON1) &&
		   window.contains(new Point2D.Float(im.getMouseX(), im.getMouseY())) &&
		   sendIndex < (size + frameIndex) && 
		   sendIndex < arq.sender().maxFrames() &&
		   !clicked) {
			em.add(new Frame(x() + 16 * sendIndex, y() + 16).setIndex(sendIndex).setType(Frame.Type.DATA));
			arq.addTimeout(Window.Type.RECEIVER, sendIndex);
			arq.disableInput();
			sendIndex++;
			sent++;
			clicked = true;
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
				if(arq.sender().frame(i + frameIndex).type() != StaticFrame.Type.RECEIVED ||
				   frameIndex + move + size >= arq.sender().maxFrames()) break;
			}
			break;
		case RECEIVER:
			for(int i = 0; i < size; i++, move++) {
				if(arq.receiver().frame(i + frameIndex).type() != StaticFrame.Type.RECEIVED ||
				   frameIndex + move + size >= arq.receiver().maxFrames()) break;
			}
			break;
		}
		setIndex(frameIndex + move);
		return this;
	}
	
	public void resendFrom(int frameIndex) {
		EntityManager em = EntityManager.getInstance();
		ARQManager arq = ARQManager.getInstance();
		
		switch(type) {
		case SENDER:
			for(int i = frameIndex; i < sent; i++) {
				em.add(new Frame(x() + 16 * i, y() + 16).setIndex(i).setType(Frame.Type.DATA));
			}
			break;
		case RECEIVER:
			em.add(new Frame(x() + 16 * frameIndex, y() - 16).setIndex(frameIndex).setType(Frame.Type.NACK));
			arq.addTimeout(Type.SENDER, frameIndex);
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
