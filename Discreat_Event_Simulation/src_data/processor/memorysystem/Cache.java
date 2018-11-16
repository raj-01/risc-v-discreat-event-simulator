package processor.memorysystem;

import configuration.Configuration;
import generic.*; 
		import processor.Clock;
import processor.Processor;
import processor.pipeline.ControlUnit;
		import processor.pipeline.MA_RW_LatchType ; 
		import processor.pipeline.EX_MA_LatchType ;
		import processor.memorysystem.*;
		
		public class Cache implements Element{
		
			int[] cache;
			int start;
			int cacheSize; //number of data/instructions
			int latency;
			Processor containingProcessor;
			MainMemory mainMemory;
			int addressRequest;
			String connected;

			EX_MA_LatchType EX_MA_Latch;
			public Cache(EX_MA_LatchType eX_MA_Latch, int cacheSize, int latency, Processor containingProcessor, String connected)
			{
	
				this.EX_MA_Latch = eX_MA_Latch;
				this.start=0;
				
				this.cacheSize = cacheSize; //8B array, i.e 2 instructions
				this.cache = new int[cacheSize];
				
//				1way associative cache
				for(int i=0; i<this.cacheSize; i++) {
					cache[i]=-1;
				}
//				/1way associative cache
				
				this.latency = latency;
				
				this.containingProcessor=containingProcessor;
				this.mainMemory = containingProcessor.getMainMemory();
				
				this.connected=connected;
			}
			
			public void cacheRead(int address)
			{
				boolean found=false;
				
//				fully associative cache
//				for(int i=0; i<cacheSize; i++) {
//					if(cache[i]==address) {
//						found=true;
//					}
//				}
//				/fully associative cache
				
				if(cache[address%cacheSize]==address) {
					found=true;
				}
				
				if(found==true) {
					//return mainMemory.getWord(address);

					
					if(connected.equals("IF")) {
						//Instruction Cache
						Simulator.getEventQueue().addEvent(
								new cacheResponseEvent(
										Clock.getCurrentTime()+1,this,containingProcessor.getIFUnit() ,
										mainMemory.getWord(address)));
					}else if(connected.equals("MA")) {
						//Data Cache
						Simulator.getEventQueue().addEvent(
								new cacheResponseEvent(
										Clock.getCurrentTime()+1,this,containingProcessor.getMAUnit() ,
										mainMemory.getWord(address)));
					}
					
					
					
				}else {
					//not found in cache
					System.out.println("creating event for cache ");
					Simulator.getEventQueue( ).addEvent(
							new MemoryReadEvent (
							Clock.getCurrentTime() + Configuration.mainMemoryLatency,
							this,containingProcessor.getMainMemory( ) ,address));
							System.out.println("cache - mainmemory latency=" + this.latency ) ;
							//IF_EnableLatch.setIF_busy(true);
					
					this.addressRequest=address;
				}
				
			}
			
			public void handleEvent( Event e ) {
				System.out.println("cache handle event called");
				
				System.out.println("cache"+connected+" contents:");
				for(int i=0; i<cacheSize; i++) {
					System.out.println(cache[i]+" ");
				}
				System.out.println("cachi contents over");
				
				if (e.getEventType() == Event.EventType.cacheRead){
					System.out.println("cache handle event (cache read) called");
					cacheReadEvent event = (cacheReadEvent) e ;
					cacheRead(event.getAddressToReadFrom());
				}
				
				if(e.getEventType()== Event.EventType.MemoryResponse) {
					
					System.out.println("cache handle event (memory response) called");
					
					//memory replied - for reading
						MemoryResponseEvent event = (MemoryResponseEvent) e ;
						
						System.out.println("cache" + connected+" - memory response: "+event.getValue());
						
						if(connected.equals("IF")) {
							//Instruction Cache
							Simulator.getEventQueue().addEvent(
									new cacheResponseEvent(
											Clock.getCurrentTime(),this,containingProcessor.getIFUnit() ,
											event.getValue()));
						}else if(connected.equals("MA")) {
							//Data Cache
							Simulator.getEventQueue().addEvent(
									new cacheResponseEvent(
											Clock.getCurrentTime(),this,containingProcessor.getMAUnit() ,
											event.getValue()));
						}
						
//						fully associative cache
//						this.cache[this.start]=this.addressRequest;
//						this.start=(this.start + 1)%this.cacheSize;
//						/fully associative cache
						
//						1way associative cache
						this.cache[this.addressRequest%cacheSize]=this.addressRequest;
//						/1way associative cache
				}
				
				if (e.getEventType() == Event.EventType.cacheWrite){
					System.out.println("cache handle event (cache write) called");
					cacheWriteEvent event = (cacheWriteEvent) e ;
					cacheWrite(event.getAddressToWriteTo(),event.getValue());
				}
			}
			
			
			public void cacheWrite(int address, int value)
			{
				//memory[address] = value;
				
//				fully associative cache
//				this.cache[this.start]=address;
//				this.start=(this.start + 1)%this.cacheSize;
//				/fully associative cache
				
//				1way associative cache
				this.cache[this.addressRequest%cacheSize]=this.addressRequest;
//				/1way associative cache
				
				Simulator.getEventQueue( ).addEvent(
						new MemoryWriteEvent (
						Clock.getCurrentTime() + Configuration.mainMemoryLatency,
						this,containingProcessor.getMainMemory( ) ,address, value));
						System.out.println("cache - mainmemory latency=" + this.latency ) ;
			
				this.addressRequest=address;
			}
			
//			public String getContentsAsString(int startingAddress, int endingAddress)
//			{
//				if(startingAddress == endingAddress)
//					return "";
//				
//				StringBuilder sb = new StringBuilder();
//				sb.append("\nMain Memory Contents:\n\n");
//				for(int i = startingAddress; i <= endingAddress; i++)
//				{
//					sb.append(i + "\t\t: " + memory[i] + "\n");
//				}
//				sb.append("\n");
//				return sb.toString();
//			}
			
			
//			public void handleEvent( Event e ) {
//				System.out.println("handling event for main memoryREADevent ") ;
//				if( e.getEventType() == Event.EventType.MemoryRead) {
//					MemoryReadEvent event = (MemoryReadEvent) e ;
//					Simulator.getEventQueue().addEvent(
//							new MemoryResponseEvent(
//									Clock.getCurrentTime(), this , event.getRequestingElement(),
//									getWord(event.getAddressToReadFrom())));
//									
//				}
//				
//				else if ( e.getEventType() == Event.EventType.MemoryWrite) {
//					System.out.println("handling event for main memoryWRITEevent ") ;
//					MemoryWriteEvent event = (MemoryWriteEvent) e ;
//					System.out.println("address to write to :"+event.getAddressToWriteTo() + " value write :" + event.getValue()) ;
//					
//					setWord( event.getAddressToWriteTo() , event.getValue());
//					System.out.println("getWord  :  " + getWord(event.getAddressToWriteTo() )) ;
//					System.out.println("MA - not using any register");
//					ControlUnit.setMaLoading(-1);
//					//System.out.println("MA - writing "+mdr+" at "+mar);
//					MA_RW_LatchType.setRW_enable(true);
//				
//					EX_MA_Latch.setMA_busy(false);
//					EX_MA_Latch.setMA_enable(false);
//					
//				}
//			}
			
			
			
	}	

