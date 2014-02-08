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
	private List<Integer> removeSend;
	private List<Integer> removeReceive;
	
	private int timeout;
	private int windowSize;
	
	private static ARQManager instance = null;
	
	//TODO: Timeout things
	
	@Override
	public void update(int delta) {
		List<Integer> remove = new ArrayList<>();
		
		for(int i : removeSend) senderTimeout.remove(i);
		removeSend.clear();
		for(int i : removeReceive) receiverTimeout.remove(i);
		removeReceive.clear();
		
		for(int i : senderTimeout.keySet()) {
			senderTimeout.put(i, senderTimeout.get(i) - delta);
			System.out.println("S: " + i + " " + senderTimeout.get(i));
			if(senderTimeout.get(i) <= 0) {
				sender.window().resendFrom(i);
				addTimeout(Window.Type.RECEIVER, i);
				remove.add(i);
			}
		}
		for(int i : remove) {
			senderTimeout.remove(i);
		}
		remove.clear();
		
		for(int i : receiverTimeout.keySet()) {
			receiverTimeout.put(i, receiverTimeout.get(i) - delta);
			System.out.println("R: " + i + " " + receiverTimeout.get(i));
			if(receiverTimeout.get(i) <= 0) {
				receiver.window().resendFrom(i);
				addTimeout(Window.Type.SENDER, i);
				remove.add(i);
			}
		}
		for(int i : remove) {
			receiverTimeout.remove(i);
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
	
	public ARQManager addTimeout(Type type, int frameIndex) {
		switch(type) {
		case SENDER:	senderTimeout.put(frameIndex, timeout); break;
		case RECEIVER:	receiverTimeout.put(frameIndex, timeout); break;
		}
		return this;
	}
	
	public ARQManager cancelTimeout(Type type, int frameIndex) {
		switch(type) {
		case SENDER:	removeSend.add(frameIndex); break;
		case RECEIVER:	removeReceive.add(frameIndex); break;
		}
		return this;
	}
	
	public ARQManager resetTimeout(Type type, int frameIndex) {
		return addTimeout(type, frameIndex);
	}
	
	private ARQManager() {
		super(0, 0);
		timeout = 15000;
		windowSize = 5;
		EntityManager.getInstance().add(this);
		senderTimeout = new HashMap<>();
		receiverTimeout = new HashMap<>();
		removeSend = new ArrayList<>();
		removeReceive = new ArrayList<>();
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