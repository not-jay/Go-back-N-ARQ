package com.xtouchme.come420;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;

import com.xtouchme.gamebase.entities.Entity;
import com.xtouchme.gamebase.managers.EntityManager;
import com.xtouchme.gamebase.managers.InputManager;
import com.xtouchme.gamebase.managers.ResourceManager;

public class Title extends Entity {

	private String text;
	private Font font;
	private FontMetrics fontMetrics;
	
	@SuppressWarnings("deprecation")
	public Title(float x, float y) {
		super(x, y);
		
		font = ResourceManager.getInstance(null).getFont(Font.TRUETYPE_FONT, "Boxy-Bold.ttf", 16);	//Load font
		text = "Go-Back-N ARQ";
		
		fontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(font);								//Get fontMetrics for getting text width based on font
		setWidth(fontMetrics.stringWidth(text)).setHeight(fontMetrics.getHeight());					//Set width and height
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		
		InputManager im = InputManager.getInstance();
		EntityManager em = EntityManager.getInstance();
		
		if(im.isMouseClicked(MouseEvent.BUTTON1)) {
			em.add(new Frame(im.getMouseX(), im.getMouseY()));
		}
	}
	
	@Override
	public void render(Graphics2D g) {
		//Draws the text with the font, centered @ x, y
		super.render(g);
		
		fontMetrics = g.getFontMetrics(font);
		
		Font def = g.getFont();
		g.setFont(font);
		g.drawString(text, x() - width()/2, y());
		g.setFont(def);
	}
	
}
