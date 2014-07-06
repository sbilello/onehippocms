package org.onehippo.listener;

import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;

import org.apache.jackrabbit.commons.webdav.EventUtil;

public final class MyListener implements EventListener{
	private int counter;
	private int limit;
	
	public MyListener(){
		counter=0;
		limit=5;
	}
	
	public MyListener(int limit){
		this.limit=limit;
	}

	public int getCounter() {
		return counter;
	}

	public int getLimit() {
		return limit;
	}

	public void onEvent(EventIterator events) {
		while (events.hasNext()){
			Event e = (Event) events.next();
			System.out.println("Event "+EventUtil.getEventName(e.getType())+" counter: "+counter);
			counter++;
		}
		if (counter>limit){
			System.exit(0);
		}
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
}