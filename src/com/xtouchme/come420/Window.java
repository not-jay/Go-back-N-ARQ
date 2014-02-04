package com.xtouchme.come420;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import com.xtouchme.come420.Frame.Type;
import com.xtouchme.gamebase.entities.Entity;
import com.xtouchme.gamebase.managers.EntityManager;
import com.xtouchme.gamebase.managers.InputManager;

public class Window extends Entity {

	//TODO: Click detection (Ang window lng i click para mu send ug frame?) + GO-BACK-N ARQ Logic na mu move sa window inig recieve ug ACK sa leftmost frame
	
	private int size;
	private int frameIndex;
	private int sendIndex;
	private Rectangle2D window;
	
	public Window(float x, float y) {
		super(x, y);
		
		size = 5;
		
		setIndex(0).setHeight(32);
		
		sendIndex = frameIndex;
		window = new Rectangle2D.Float(x() - 8 + (16 * frameIndex), y() - height()/2, width(), height());
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		
		EntityManager em = EntityManager.getInstance();
		InputManager im = InputManager.getInstance();
		
		if(im.isKeyPressed("Send Data") && sendIndex < (size + frameIndex)) {
		//if(im.isMouseClicked(MouseEvent.BUTTON1)) {
			em.add(new Frame(x() + 16 * sendIndex, y() + 16).setType(Type.DATA));
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
	public Window setIndex(int frameIndex) {
		this.frameIndex = frameIndex;
		return this;
	}
	
	public int frameIndex() {
		return frameIndex;
	}
	
	public Window slideWindow() {
		setIndex(frameIndex + 1);
		sendIndex = frameIndex;
		return this;
	}
	
	public Window setSize(int size) {
		this.size = size;
		setWidth(16 * size);
		return this;
	}
	
	public int size() {
		return size;
	}
	
	public void senderWindow() {
		InputManager im = InputManager.getInstance();
		im.define("Send Data", KeyEvent.VK_SPACE);
	}
}
