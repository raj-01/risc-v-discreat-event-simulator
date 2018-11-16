package generic;

import generic.Event.EventType;

public class cacheReadEvent extends Event {

	int addressToReadFrom;
	
	public cacheReadEvent(long eventTime, Element requestingElement, Element processingElement, int address) {
		super(eventTime, EventType.cacheRead, requestingElement, processingElement);
		this.addressToReadFrom = address;
	}

	public int getAddressToReadFrom() {
		return addressToReadFrom;
	}

	public void setAddressToReadFrom(int addressToReadFrom) {
		this.addressToReadFrom = addressToReadFrom;
	}
}
