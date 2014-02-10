package com.xtouchme.come420;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.xtouchme.gamebase.entities.Entity;
import com.xtouchme.gamebase.managers.InputManager;
import com.xtouchme.gamebase.managers.ResourceManager;

public class Input extends Entity {

	public enum Type {
		WINSIZE, TIMEOUT
	}
	
	private int value;
	private int step;
	private int limit;
	private boolean clicked;
	private Rectangle2D increment;
	private Rectangle2D decrement;
	private Font font;
	private FontMetrics fontMetrics;
	private Type type;
	
	@SuppressWarnings("deprecation")
	public Input(float x, float y) {
		super(x, y);
		
		limit = -1;
		
		font = ResourceManager.getInstance(null).getFont(Font.TRUETYPE_FONT, "Boxy-Bold.ttf", 16);
		
		fontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(font);									//Get fontMetrics for getting text width based on font
		setWidth(fontMetrics.stringWidth(String.valueOf(value))).setHeight(fontMetrics.getHeight());	//Set width and height
		
		increment = new Rectangle2D.Float(x + fontMetrics.stringWidth("123456"), y - height(), 16, height());
		decrement = new Rectangle2D.Float(x + fontMetrics.stringWidth("123456789"), y - height(), 16, height());
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		
		ARQManager arq = ARQManager.getInstance();
		InputManager im = InputManager.getInstance();
		
		if(arq.isInputDisabled()) return;
		
		if(!im.isMouseDown(MouseEvent.BUTTON1)) clicked = false;
		
		if(im.isMouseDown(MouseEvent.BUTTON1) && !clicked) {
			if(increment.contains(new Point2D.Float(im.getMouseX(), im.getMouseY()))) {
				if(limit == -1) value += step;
				else {
					if(value + step < limit) value += step;
				}
			}
			
			if(decrement.contains(new Point2D.Float(im.getMouseX(), im.getMouseY()))) {
				if(limit == -1) value -= step;
				else {
					if(value - step > 0) value -= step;
				}
			}
			
			switch (type) {
			case WINSIZE:
				arq.setWindowSize(value);
				break;
			case TIMEOUT:
				arq.setTimeout(value);
				break;
			default: break;
			}
			
			clicked = true;
		}
	}
	
	@Override
	public void render(Graphics2D g) {
		super.render(g);
		
		Font def = g.getFont();
		g.setFont(font);
		g.drawString(String.format("%d", value), x(), y());
		g.drawString("+", x() + fontMetrics.stringWidth("123456"), y());
		g.drawString("-", x() + fontMetrics.stringWidth("123456789"), y());
		g.setFont(def);
	}
	
	public Input setType(Type type) {
		this.type = type;
		return this;
	}
	
	public Input setLimit(int limit) {
		this.limit = limit;
		return this;
	}
	
	public Input setValue(int value) {
		this.value = value;
		setWidth(fontMetrics.stringWidth(String.valueOf(value))).setHeight(fontMetrics.getHeight());
		return this;
	}
	
	public Input setStep(int step) {
		this.step = step;
		return this;
	}
	
	public int value() {
		return value;
	}
	
}
