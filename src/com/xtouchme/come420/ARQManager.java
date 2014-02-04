package com.xtouchme.come420;

import com.xtouchme.gamebase.managers.EntityManager;

public class ARQManager {

	private SlidingWindow sender;
	private SlidingWindow receiver;
	
	private static ARQManager instance = null;
	
	public ARQManager setSender(SlidingWindow sender) {
		this.sender = sender;
		EntityManager.getInstance().add(sender);
		return this;
	}
	
	public ARQManager setReceiver(SlidingWindow receiver) {
		this.receiver = receiver;
		EntityManager.getInstance().add(receiver);
		return this;
	}
	
	public SlidingWindow sender() {
		return sender;
	}
	
	public SlidingWindow receiver() {
		return receiver;
	}
	
	private ARQManager() {}
	public static ARQManager getInstance() {
		if(instance == null) instance = new ARQManager();
		return instance;
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}