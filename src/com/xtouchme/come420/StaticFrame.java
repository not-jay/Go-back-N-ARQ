package com.xtouchme.come420;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import com.xtouchme.gamebase.entities.Entity;
import com.xtouchme.gamebase.managers.EntityManager;

public class StaticFrame extends Entity {

	//TODO: Collision detection + GO-BACK-N ARQ Logic
	
	public enum Type {
		RECEIVED, BLANK
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
	public StaticFrame(float x, float y) {
		super(x, y);
		
		setWidth(8).setHeight(16);
		
		setType(Type.BLANK);
		frame = new Rectangle2D.Float(x - width()/2, y - height()/2, width(), height());
		setHitbox(frame).setCollidable(true);
	}
	
	private StaticFrame setType(Type type) {
		this.type = type;
		return this;
	}
	
	public Type type() {
		return type;
	}
	
	@Override
	public void render(Graphics2D g) {		
		super.render(g); //Can be omitted since it doesn't use sprites/animation, it will only draws debug lines
		
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
	public boolean collides(Entity other) {
		if(!(other instanceof Frame)) return false;
		return super.collides(other);
	}
	
	@Override
	public void collisionResponse(Entity other) {
		Frame frame = (Frame)other;
		EntityManager em = EntityManager.getInstance();
		ARQManager arq = ARQManager.getInstance();
		
		switch(frame.type()) {
		case ACK:
			setType(Type.RECEIVED);
			arq.sender().window().slideWindow();
			arq.cancelTimeout(Window.Type.SENDER, frame.index());
			break;
		case DATA:
			setType(Type.RECEIVED);
			em.add(new Frame(x(), y() - 16).setType(Frame.Type.ACK));
			arq.receiver().window().slideWindow();
			arq.cancelTimeout(Window.Type.RECEIVER, frame.index());
			arq.addTimeout(Window.Type.SENDER, frame.index());
			break;
		case DAMAGED_DATA:
			em.add(new Frame(x(), y() - 16).setType(Frame.Type.NACK));
			arq.cancelTimeout(Window.Type.RECEIVER, frame.index());
			arq.addTimeout(Window.Type.SENDER, frame.index());
			break;
		case NACK:
//			em.add(new Frame(x(), y() + 16).setType(Frame.Type.DATA));
			arq.sender().window().resendFrom(frame.index());
			arq.cancelTimeout(Window.Type.SENDER, frame.index());
			arq.addTimeout(Window.Type.RECEIVER, frame.index());
			break;
		}
	}
	
	private Color getColor() {
		switch(type) {
		case RECEIVED:		return Color.GRAY;
		case BLANK:
		default:			return null;
		}
	}
}
