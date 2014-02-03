package com.xtouchme.come420;

import java.util.ArrayList;
import java.util.List;

import com.xtouchme.gamebase.entities.Entity;

public class SlidingWindow extends Entity {

	//TODO: GO-BACK-N ARQ Logic
	
	private List<Frame> frames;
	
	public SlidingWindow(float x, float y) {
		super(x, y);
		
		frames = new ArrayList<>();
	}

	private void fillFrames(int size) {
		
	}
	
	public void reset() {
		int size = frames.size();
		frames.clear();
		fillFrames(size);
	}
}
