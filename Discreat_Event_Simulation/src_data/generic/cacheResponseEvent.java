package generic;

import generic.Event.EventType;

public class cacheResponseEvent extends Event {

	int value;
	
	public cacheResponseEvent(long eventTime, Element requestingElement, Element processingElement, int value) {
		super(eventTime, EventType.cacheResponse, requestingElement, processingElement);
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}