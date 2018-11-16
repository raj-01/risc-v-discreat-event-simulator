package processor.pipeline;

import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.MemoryWriteEvent;
import generic.Simulator;
import processor.Clock;
import processor.Processor;
import configuration.Configuration;
import processor.memorysystem.*;
import generic.*;

public class MemoryAccess implements Element{
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	ControlUnit CUnit;
	int opcode ; 
	MemoryResponseEvent memoryResponseEvent; // = new MemoryResponseEvent();
	int rd;
	
	Cache L1d; 
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch, ControlUnit CUnit, Cache cache)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.CUnit = CUnit;
		this.L1d=cache;
	}
	
	public void performMA()
	{
		System.out.println("Memory Access reached");
		
		if(EX_MA_Latch.isMA_enable()) {
		//TODO
			
			//MA_RW_Latch.setAluResult(EX_MA_Latch.getAluResult());
			MA_RW_Latch.setPC(EX_MA_Latch.getPC());
			MA_RW_Latch.setRd(rd);
			//MA_RW_Latch.setOpcode(opcode);
			MA_RW_Latch.setInstruction(EX_MA_Latch.getInstruction());
			
			
			opcode = EX_MA_Latch.getOpcode();
			CUnit.setOpcode(opcode);
			
			
			System.out.println(" Opcode in MA :" + opcode ) ; 
			System.out.println(" isMA_busy :" + EX_MA_Latch.isMA_busy()) ; 
			
			System.out.println(" trying to create event for isLoad operation if isMA_busy() is false and CUnit.isBubble() == false") ;
			if( EX_MA_Latch.isMA_busy() ) {
				System.out.println(" Not able to create event for isLoad operation beacuse isMA_busy() is true") ;
				System.out.println("MA - not using any register");
				CUnit.setMaLoading(-1);
				return;
			}
			
			System.out.println("Memory Access enabled");
			
			
			
			//opcode = EX_MA_Latch.getOpcode();
			//CUnit.setOpcode(opcode);
			
			System.out.println("MA - pc="+EX_MA_Latch.getPC());
			
			
				
			
			//////////////////////////////////////////////////////////////////////////////////////////////////////
				
			if(EX_MA_Latch.getRegisterPassing()!=-1) {
				System.out.println("MA - register"+EX_MA_Latch.getRegisterPassing()+" passing");
				CUnit.setMaPassing(EX_MA_Latch.getRegisterPassing());
			}
			
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			
			rd = EX_MA_Latch.getRd();
			int mdr;
			
			if(CUnit.isStore()) {
				mdr = EX_MA_Latch.getOp2()+EX_MA_Latch.getImmediate();
				
			}
			else mdr = EX_MA_Latch.getOp2();
			
			int mar = EX_MA_Latch.getAluResult();
			
			System.out.println("MA - MDR:"+mdr+" MAR:"+mar);
			
			if(CUnit.isLoad() ) {
				
				System.out.println("  creating event for isLoad operation and making  isMA_busy true") ;
				
//				Simulator.getEventQueue( ).addEvent(
//						new MemoryReadEvent (
//						Clock.getCurrentTime() + Configuration.mainMemoryLatency ,
//						this ,containingProcessor.getMainMemory( ) , mar ));
					
				Simulator.getEventQueue( ).addEvent(
						new cacheReadEvent (
						Clock.getCurrentTime() + Configuration.L1d_latency ,
						this ,L1d , mar ));
				
				EX_MA_Latch.setMA_busy(true);
			} 
			
			else if(CUnit.isStore() && !CUnit.isBubble()){
				
				System.out.println("  creating event for isStore operation") ;
				//System.exit(0);
				Simulator.getEventQueue( ).addEvent(
						new cacheWriteEvent (
						Clock.getCurrentTime() + Configuration.L1d_latency ,
						this ,L1d , mar , mdr ));	
				        EX_MA_Latch.setMA_busy(true);
			
				//containingProcessor.getMainMemory().setWord(mar, mdr);
				
			}else {
				
				if(CUnit.isBubble()) {
					System.out.println("MA - bubble detected");
					MA_RW_Latch.setOpcode(opcode);
					EX_MA_Latch.setMA_enable(false);
					MA_RW_Latch.setRW_enable(true);
					System.out.println("MA - not using any register");
					CUnit.setMaLoading(-1);
					CUnit.setMaPassing(-1);
					return;
				}
				System.out.println("MA - not using any register i.e. no load or store operation ");
				CUnit.setMaLoading(-1);
				MA_RW_Latch.setRW_enable(true);
				EX_MA_Latch.setMA_enable(false);
			}
			
			
			MA_RW_Latch.setAluResult(EX_MA_Latch.getAluResult());
			MA_RW_Latch.setPC(EX_MA_Latch.getPC());
			MA_RW_Latch.setRd(rd);
			MA_RW_Latch.setOpcode(opcode);
			MA_RW_Latch.setInstruction(EX_MA_Latch.getInstruction());
			
			
			
	}
	}
	
	
	
	public void handleEvent(Event e ) {
		System.out.println("Trying to handling event for memoryAccess if CUnit.isBubble() is false ") ;
		if( CUnit.isBubble()) {
			e.setEventTime( Clock.getCurrentTime() + 1);
			Simulator.getEventQueue().addEvent(e);
			System.out.println("not able to handel the event because of isBubble true ") ;
			System.out.println("MA - bubble detected");
			MA_RW_Latch.setOpcode(opcode);
			EX_MA_Latch.setMA_enable(false);
			MA_RW_Latch.setRW_enable(true);
			System.out.println("MA - not using any register");
			CUnit.setMaLoading(-1);
			CUnit.setMaPassing(-1);
			//return ; 
		}
		else {
			System.out.println(" handling event for memoryAccess  ") ;
			cacheResponseEvent event = (cacheResponseEvent) e;    
			int ldResult = event.getValue() ;     
			MA_RW_Latch.setLdResult(ldResult); 
			System.out.println("MA - loading for register"+rd);
			CUnit.setMaLoading(rd);
			System.out.println("MA - ldresult:"+ldResult);
			MA_RW_Latch.setRW_enable(true);
			MA_RW_Latch.setPC(EX_MA_Latch.getPC());
			EX_MA_Latch.setMA_busy(false);
			EX_MA_Latch.setMA_enable(false);
		}
			
		
	}
	}


	
	

