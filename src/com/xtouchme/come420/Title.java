package com.xtouchme.come420;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import com.xtouchme.gamebase.entities.Entity;
import com.xtouchme.gamebase.managers.ResourceManager;

public class Title extends Entity {

	private String text;
	private String winSize;
	private String timeout;
	private Font font;
	private FontMetrics fontMetrics;
	
	@SuppressWarnings("deprecation")
	public Title(float x, float y) {
		super(x, y);
		
		font = ResourceManager.getInstance(null).getFont(Font.TRUETYPE_FONT, "Boxy-Bold.ttf", 16);	//Load font
		text = "Go-Back-N ARQ";
		winSize = "Window Size :";
		timeout = "Timeout :";
		
		fontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(font);								//Get fontMetrics for getting text width based on font
		setWidth(fontMetrics.stringWidth(text)).setHeight(fontMetrics.getHeight());					//Set width and height
	}

	@Override
	public void render(Graphics2D g) {
		//Draws the text with the font, centered @ x, y
		super.render(g);
		
		fontMetrics = g.getFontMetrics(font);
		
		Font def = g.getFont();
		g.setFont(font);
		g.drawString(text, x() - width()/2, y());
		g.drawString(winSize, 23, 80);
		g.drawString(timeout, 71, 110);
		g.setFont(def);
	}
	
}
