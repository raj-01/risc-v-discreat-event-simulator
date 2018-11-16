package processor.memorysystem;

//import org.w3c.dom.events.Event;


import generic.* ; 
import processor.Clock;
import processor.pipeline.ControlUnit;
import processor.pipeline.MA_RW_LatchType ; 
import processor.pipeline.EX_MA_LatchType ;

public class MainMemory implements Element{
	int[] memory;

	EX_MA_LatchType EX_MA_Latch;
	public MainMemory(EX_MA_LatchType eX_MA_Latch )
	{
		memory = new int[65536];
		this.EX_MA_Latch = eX_MA_Latch;
		
	}
	
	public int getWord(int address)
	{
		return memory[address];
	}
	
	public void setWord(int address, int value)
	{
		memory[address] = value;
	}
	
	public String getContentsAsString(int startingAddress, int endingAddress)
	{
		if(startingAddress == endingAddress)
			return "";
		
		StringBuilder sb = new StringBuilder();
		sb.append("\nMain Memory Contents:\n\n");
		for(int i = startingAddress; i <= endingAddress; i++)
		{
			sb.append(i + "\t\t: " + memory[i] + "\n");
		}
		sb.append("\n");
		return sb.toString();
	}
	
	
	public void handleEvent( Event e ) {
		System.out.println("handling event for main memoryREADevent ") ;
		if( e.getEventType() == Event.EventType.MemoryRead) {
			MemoryReadEvent event = (MemoryReadEvent) e ;
			Simulator.getEventQueue().addEvent(
					new MemoryResponseEvent(
							Clock.getCurrentTime(), this , event.getRequestingElement(),
							getWord(event.getAddressToReadFrom())));
							
		}
		
		else if ( e.getEventType() == Event.EventType.MemoryWrite) {
			System.out.println("handling event for main memoryWRITEevent ") ;
			MemoryWriteEvent event = (MemoryWriteEvent) e ;
			System.out.println("address to write to :"+event.getAddressToWriteTo() + " value write :" + event.getValue()) ;
			
			setWord( event.getAddressToWriteTo() , event.getValue());
			System.out.println("getWord  :  " + getWord(event.getAddressToWriteTo() )) ;
			System.out.println("MA - not using any register");
			ControlUnit.setMaLoading(-1);
			//System.out.println("MA - writing "+mdr+" at "+mar);
			MA_RW_LatchType.setRW_enable(true);
		
			EX_MA_Latch.setMA_busy(false);
			EX_MA_Latch.setMA_enable(false);
			
	}
	}
	
	
}



