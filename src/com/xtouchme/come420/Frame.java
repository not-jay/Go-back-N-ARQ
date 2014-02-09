package com.xtouchme.come420;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.xtouchme.gamebase.entities.Entity;
import com.xtouchme.gamebase.managers.EntityManager;
import com.xtouchme.gamebase.managers.InputManager;

public class Frame extends Entity {

	//TODO: Collision detection + GO-BACK-N ARQ Logic
	
	//States sa Frame
	//Everything should be "sabot"-able
	//Except sa:
	//	- BLANK: Katung black outlines dapit sa "Sender" ug "Receiver"
	//	- RECIEVED: Mu change ang state ni BLANK to this kung maka dawat siya ug stuff
	public enum Type {
		DATA, DAMAGED_DATA, ACK, NACK
	}
	
	private int index;
	private Type type;
	private Rectangle2D frame;
	
	//Initialization
	//x, y - position
	//frame - frame itself :)
	public Frame(float x, float y) {
		super(x, y);
		
		setWidth(8).setHeight(16);
		
		frame = new Rectangle2D.Float(x - width()/2, y - height()/2, width(), height());
		setHitbox(frame).setCollidable(true);
	}

	public Frame setIndex(int index) {
		this.index = index;
		return this;
	}
	
	public int index() {
		return index;
	}
	
	public Frame setType(Type type) {
		this.type = type;
		return this;
	}
	
	@Override
	public void update(int delta) {
		InputManager im = InputManager.getInstance();
		
		if(im.isMouseDown(MouseEvent.BUTTON1) && frame.contains(new Point2D.Float(im.getMouseX(), im.getMouseY())) && type == Type.DATA) {
			setType(Type.DAMAGED_DATA);
		}
		if(im.isMouseDown(MouseEvent.BUTTON3) && frame.contains(new Point2D.Float(im.getMouseX(), im.getMouseY()))) {
			EntityManager.getInstance().remove(this);
		}
		
		//Movement handling
		switch(type) {
		case ACK:
		case NACK:
			setSpeed(0, -1f);	//Rising at 1 pixels/update
			break;
		case DATA:
		case DAMAGED_DATA:
			setSpeed(0, 1f);	//Falling at 1 pixels/update
			break;
		default:			  	//Received and Blank are stationary
		}
		
		super.update(delta);	//Calls Entity#update() to update the entity's position based on speed + updates the hitbox for collision
	}
	
	@Override
	public void render(Graphics2D g) {		
		super.render(g); //Can be omitted since it doesn't use sprites/animation, it will only draw debug lines
		
		Color def = g.getColor();
		Color typeColor = getColor();
		
		if(typeColor != null) {
			g.setColor(getColor());
			g.fill(frame);
		}
		//Then draw frame outline
		g.setColor(Color.BLACK);
		g.draw(frame);
		g.setColor(def);
	}
	
	@Override
	public void updateHitbox() {
		frame.setRect(x() - width()/2, y() - height()/2, width(), height());
	}
	
	@Override
	public boolean collides(Entity other) {
		if(!(other instanceof StaticFrame)) return false;
		return super.collides(other);
	}
	
	@Override
	public void collisionResponse(Entity other) {
		EntityManager.getInstance().remove(this);
	}
	
	public Type type() {
		return type;
	}
	
	private Color getColor() {
		switch(type) {
		case DAMAGED_DATA:	return Color.RED;
		case NACK:			return Color.BLUE;
		case ACK:			return Color.GREEN;
		case DATA:			return Color.YELLOW;
		default:			return null;
		}
	}
}
