package com.xtouchme.come420;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import com.xtouchme.gamebase.entities.Entity;

public class Frame extends Entity {

	//TODO: Collision detection + GO-BACK-N ARQ Logic
	
	//States sa Frame
	//Everything should be "sabot"-able
	//Except sa:
	//	- BLANK: Katung black outlines dapit sa "Sender" ug "Receiver"
	//	- RECIEVED: Mu change ang state ni BLANK to this kung maka dawat siya ug stuff
	public enum Type {
		DATA, DAMAGED_DATA, ACK, NACK, RECEIVED, BLANK
	}
	
	private Type type;
	private Rectangle2D.Float frame;
	
	//Initialization
	//x, y - position
	//frame - frame itself :)
	
	//Para mu add ug frame
	//sa GoBackNARQ class, after sa em.add(new Title...);
	//pag add ug em.add(new Frame(x position, y position), pero manu2 kaayu ni na way
	//akong idea is to have the SlidingWindow class add the frames sa iyang constructor (ug ang Window sd)
	public Frame(float x, float y) {
		super(x, y);
		
		setWidth(16).setHeight(32);
		
		frame = new Rectangle2D.Float(x - width()/2, y - height()/2, width(), height());
		setHitbox(frame);
	}

	//
	public Frame setType(Type type) {
		this.type = type;
		return this;
	}
	
	@Override
	public void update(int delta) {
		
		//Movement handling
		switch(type) {
		case ACK:
		case NACK:
			setSpeed(0, -0.5f);	//Rising at 0.5 pixels/update
			break;
		case DATA:
		case DAMAGED_DATA:
			setSpeed(0, 0.5f);	//Falling at 0.5 pixels/update
			break;
		default:			  	//Received and Blank are stationary
		}
		
		super.update(delta);	//Calls Entity#update() to update the entity's position based on speed + updates the hitbox for collision
		//Click handling TODO
	}
	
	@Override
	public void render(Graphics2D g) {		
		//super.render(g); //Can be omitted since it doesn't use sprites/animation, it will only draws debug lines
		
		Color def = g.getColor();
		Color typeColor = getColor();
		
		if(typeColor != null) { //If it isn't a blank frame, fill with color
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
	
	private Color getColor() {
		switch(type) {
		case DAMAGED_DATA:	return Color.RED;
		case RECEIVED:
		case NACK:			return Color.BLUE;
		case ACK:			return Color.GREEN;
		case DATA:			return Color.YELLOW;
		case BLANK:
		default:			return null;
		}
	}
}
