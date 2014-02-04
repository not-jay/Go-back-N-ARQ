package com.xtouchme.come420;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.xtouchme.come420.Window.Type;
import com.xtouchme.gamebase.entities.Entity;
import com.xtouchme.gamebase.managers.EntityManager;

public class ARQManager extends Entity {

	private SlidingWindow sender;
	private SlidingWindow receiver;
	private HashMap<Integer, Integer> senderTimeout;
	private HashMap<Integer, Integer> receiverTimeout;
	
	private int timeout;
	private int windowSize;
	
	private static ARQManager instance = null;
	
	//TODO: Timeout things
	
	@Override
	public void update(int delta) {
		List<Integer> remove = new ArrayList<>();
		
		for(int i : senderTimeout.keySet()) {
			senderTimeout.put(i, senderTimeout.get(i) - delta);
			if(senderTimeout.get(i) <= 0) {
				sender.window().resendFrom(i);
				remove.add(i);
			}
		}
		for(int i : remove) {
			senderTimeout.remove(i);
		}
		remove.clear();
	}
	
	public ARQManager setSender(SlidingWindow sender) {
		this.sender = sender;
		sender.window().setType(Type.SENDER);
		EntityManager.getInstance().add(sender);
		return this;
	}
	
	public ARQManager setReceiver(SlidingWindow receiver) {
		this.receiver = receiver;
		receiver.window().setType(Type.RECEIVER);
		EntityManager.getInstance().add(receiver);
		return this;
	}
	
	public SlidingWindow sender() {
		return sender;
	}
	
	public SlidingWindow receiver() {
		return receiver;
	}
	
	public ARQManager setTimeout(int timeout) {
		this.timeout = timeout;
		return this;
	}
	
	public ARQManager setWindowSize(int windowSize) {
		this.windowSize = windowSize;
		return this;
	}
	
	public int windowSize() {
		return windowSize;
	}
	
	private ARQManager() {
		super(0, 0);
		timeout = 15000;
		windowSize = 5;
		EntityManager.getInstance().add(this);
	}
	public static ARQManager getInstance() {
		if(instance == null) instance = new ARQManager();
		return instance;
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}