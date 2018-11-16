package processor.pipeline;

import processor.Processor;

import processor.Clock;
import generic.*;
import configuration.Configuration ; 
import processor.memorysystem.*;

public class InstructionFetch implements Element{
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch ;
	ControlUnit CUnit; 
	int currentPC ; 
	boolean isBranching ;
	
	Cache L1i; 
	 
	   static  int  num_inst_fetch = 0 ; 
	//Configuration confi ; //= new Configuration() ; 
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, EX_IF_LatchType eX_IF_Latch, ControlUnit CUnit, Cache cache)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.CUnit = CUnit;
		this.L1i = cache;
		this.isBranching = false ;
	
	}
	
	public void performIF ()
	{
		
		System.out.println("IF - number of instructions executed="+containingProcessor.getnInstructions());
		
		if(IF_EnableLatch.isIF_enable()){
			
			
			
				
			System.out.println("Instruction fetch enabled");
			isBranching=false;
			
			currentPC=IF_OF_Latch.getPC();
			int tempPC;
			
			if(!IF_OF_Latch.isIFstallNext()) {
				
				
				System.out.println("trying to creat event for IF if isIF_busy is false ");
				if( IF_EnableLatch.isIF_busy()) {
					
					System.out.println("not able to creat event for IF beacuse isIF_busy is true ");
					return;
				}
				
				// First calculating currentPC value taking both branch taken and not taken case 
				
					if(EX_IF_Latch.isBranchTaken()) {
						//IF_OF_Latch.ignoreDataHazards=true;
						// turning off the CUnit clash;
						CUnit.setClash(false);
					
						System.out.println("IF - isBranchTaken is true");
						containingProcessor.getRegisterFile().setProgramCounter(EX_IF_Latch.getBranchPC());
						System.out.println("IF - PC="+containingProcessor.getRegisterFile().getProgramCounter());
						currentPC=containingProcessor.getRegisterFile().getProgramCounter();
						EX_IF_Latch.setBranchTaken(false);
						isBranching=true;
					} else {
						System.out.println("IF - isBranchTaken is false");
						containingProcessor.getRegisterFile().setProgramCounter(containingProcessor.getRegisterFile().getProgramCounter() + 1);
						currentPC=containingProcessor.getRegisterFile().getProgramCounter();
						System.out.println("IF - PC="+containingProcessor.getRegisterFile().getProgramCounter());
					}
					IF_OF_Latch.setPC(currentPC);
					
					/*if( currentPC == 25 ) {															
						System.out.println("yes first opcode for store found") ;
							System.exit(0);
						};*/
					
			//else {
					
				//	System.out.println("IF - Stall detected, bypass IF");
			//		System.out.println("IF - currentPC="+currentPC);
			//	}
			
					
					
		
// Now using the above calculates currentPC to create an event 
			
			
			
			System.out.println("creating event for IF  and setting isIF_busy true");
			Simulator.getEventQueue( ).addEvent(
					new cacheReadEvent (
					Clock.getCurrentTime() + Configuration.L1i_latency ,
					this ,L1i ,currentPC));
					System.out.println("IF - cacheL1i latency=" + Configuration.L1i_latency ) ;
					IF_EnableLatch.setIF_busy(true);
			
		}
		
		
		IF_OF_Latch.setIFstallNext(IF_OF_Latch.isIFStall());
			
		if(isBranching && containingProcessor.getnInstructions()!=0) {
			//branching, bubble out the instructions in IF_OF latch and OF_EX latch
			System.out.println("EX - branching, bubbling IF_OF and OF_EX");
			IF_OF_Latch.setPC(currentPC);
			OF_EX_Latch.setOpcode(31);
		}
		
		
		
		//IF_EnableLatch.setIF_enable(false);
		
		
	}
	
	}
		
		
		// event handler class created by main memory and called by EventQueue 
		
		public void handleEvent(Event e ) {
			// IF_OF_Latch.isOF_busy() is  equivalent to  IF_OF_Latch.isIFstallNext()
			System.out.println("trying to handle event for IF if isIFstallNext() is false ");
			
				if(IF_OF_Latch.isIFstallNext()) {
					System.out.println("IF - IFstallnext is true");
				}
				if(IF_OF_Latch.isOF_busy()) {
					System.out.println("IF - isOF_busy is true");
				}
				if(OF_EX_Latch.isEX_busy()) {
					System.out.println("IF - isEX_busy is true");
				}
				if(EX_MA_Latch.isMA_busy()) {
					System.out.println("IF - isMA_busy is true");
				}
			
				if( IF_OF_Latch.isIFstallNext() || IF_OF_Latch.isOF_busy() || OF_EX_Latch.isEX_busy() || EX_MA_Latch.isMA_busy()) {  
					e.setEventTime( Clock.getCurrentTime() + 1);
					Simulator.getEventQueue().addEvent(e);
						System.out.println("IF - Stall detected, bypass IF");
						System.out.println("IF - currentPC="+currentPC);	
				
				}
				else {
				
					cacheResponseEvent event = ( cacheResponseEvent) e ; 
					int newInstruction = event.getValue();
					//System.out.println(containingProcessor.getMainMemory().getContentsAsString(0,25));
					num_inst_fetch++ ; 
					System.out.println("IF - newInstrcution="+newInstruction);
					System.out.println("IF - instructionRead="+String.format("%32s", Integer.toBinaryString(newInstruction)).replace(' ', '0'));
					System.out.println("current programm counter from IF :" + currentPC ); 
					IF_OF_Latch.setInstruction(newInstruction);
					System.out.println(" instruction/opcode from IF :" + newInstruction ) ; 
					IF_EnableLatch.setIF_busy(false);
					IF_OF_Latch.setOF_enable(true);
					IF_OF_Latch.setPC(currentPC);
					IF_OF_Latch.setIFstallNext(IF_OF_Latch.isIFStall());
					
				}
			
			
			
		}

		public static int num_inst_fetch() {
			// TODO Auto-generated method stub
			return num_inst_fetch ;
		}
		
}

