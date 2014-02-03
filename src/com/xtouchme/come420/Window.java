package com.xtouchme.come420;

import com.xtouchme.gamebase.entities.Entity;

public class Window extends Entity {

	//TODO: Click detection (Ang window lng i click para mu send ug frame?) + GO-BACK-N ARQ Logic na mu move sa window inig recieve ug ACK sa leftmost frame
	
	private int size;
	private int frameIndex;
	
	public Window(float x, float y) {
		super(x, y);
		
		size = 5;
	}

	public Window setSize(int size) {
		this.size = size;
		return this;
	}
}
