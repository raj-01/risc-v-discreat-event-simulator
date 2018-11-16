package processor.pipeline;

import configuration.Configuration;
import generic.MemoryWriteEvent;
import generic.Simulator;
import generic.Element;
import generic.Event;
import generic.ExecutionCompleteEvent;
import generic.MemoryResponseEvent;
import processor.Clock;
import processor.Processor;

public class Execute implements Element {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	IF_OF_LatchType IF_OF_Latch;
	ControlUnit CUnit;

	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch, IF_OF_LatchType iF_OF_Latch, ControlUnit CUnit)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.CUnit = CUnit;
	}

	public void performEX()
	{
		System.out.println("Execute reached");

	
		if(OF_EX_Latch.isEX_enable()) {
			System.out.println("Execute enabled");

			int opcode = OF_EX_Latch.getOpcode();
			CUnit.setOpcode(opcode);

			System.out.println("EX - pc="+OF_EX_Latch.getPC()) ;
			System.out.println(" Opcode in EX stage:" + opcode ) ;  
			System.out.println("isEX_busy :" +  OF_EX_Latch.isEX_busy() ) ; 
			
			if(CUnit.isBubble()) {
				System.out.println("EX - bubble detected");
				EX_MA_Latch.setOpcode(opcode);
				OF_EX_Latch.setEX_enable(false);
				EX_MA_Latch.setMA_enable(true);
				System.out.println("EX - not using any register");
				CUnit.setExCalculating(-1);
				CUnit.setExPassing(-1);
				return;
			}
			
			//EX_MA_Latch.setAluResult(aluResult);
			//EX_MA_Latch.setOp2(op2);
			EX_MA_Latch.setRd(OF_EX_Latch.getRd());
			EX_MA_Latch.setPC(OF_EX_Latch.getPC());
			EX_MA_Latch.setImmediate(OF_EX_Latch.getImmediate());
			EX_MA_Latch.setOpcode(opcode);
			EX_MA_Latch.setInstruction(OF_EX_Latch.getInstruction());
			
			
				/*///////////////////////////////////////////////////////////
				if( EX_MA_LatchType.isMA_busy() ) {
					
					return ; 
				}
				
				///////////////////////////////////////////////////////////*/
				
				
			// creating event for multiplication 
			if( Integer.toBinaryString(CUnit.getOpcode()).equals("00100" )|| Integer.toBinaryString(CUnit.getOpcode()).equals("00101") ) {
				System.out.println("trying to creat event for multiplication if isEX_busy is false");
				if( OF_EX_Latch.isEX_busy()) {
								return;
					}
				System.out.println("creating event for multiplication and making isEX_busy true");
				Simulator.getEventQueue( ).addEvent(
						new ExecutionCompleteEvent (
						Clock.getCurrentTime() + Configuration.multiplier_latency ,
						this , this ));	
				        OF_EX_Latch.setEX_busy(true);
			}
			
			// creating event for division 
			else if( Integer.toBinaryString(CUnit.getOpcode()).equals("00110") || Integer.toBinaryString(CUnit.getOpcode()).equals("00111") ) {
				System.out.println("trying to creat event for division if isEX_busy is false");
							if( OF_EX_Latch.isEX_busy()) {
											return;
								}
							System.out.println("creating event for division and making isEX_busy is true");
							Simulator.getEventQueue( ).addEvent(
									new ExecutionCompleteEvent (
									Clock.getCurrentTime() + Configuration.divider_latency,
									this , this ));	
							        OF_EX_Latch.setEX_busy(true);
			}
			// creating event for other ALU operation
			else {
				System.out.println("trying to creat event for other ALU ops if isEX_busy is false");
							if( OF_EX_Latch.isEX_busy()) {
									return;
							}
							System.out.println("creating  event for other ALU ops and setting isEX_busy is true");	
							Simulator.getEventQueue( ).addEvent(
									new ExecutionCompleteEvent (
									Clock.getCurrentTime() + Configuration.ALU_latency,
									this , this ));	
							System.out.println(" alu latancy :" + Configuration.ALU_latency );
							OF_EX_Latch.setEX_busy(true);
}
			
			//EX_MA_Latch.setAluResult(aluResult);
			//EX_MA_Latch.setOp2(op2);
			EX_MA_Latch.setRd(OF_EX_Latch.getRd());
			EX_MA_Latch.setPC(OF_EX_Latch.getPC());
			EX_MA_Latch.setImmediate(OF_EX_Latch.getImmediate());
			EX_MA_Latch.setOpcode(opcode);
			EX_MA_Latch.setInstruction(OF_EX_Latch.getInstruction());
		}
		
		
		
	}
		
		
	
	
	// defining event handler function for  division multiplication and other ALU operations 

public void handleEvent(Event e ) {
		
	System.out.println(" trying to execute  event handler for execute stage if CUint.isBubble is false");

	int opcode = OF_EX_Latch.getOpcode();
	CUnit.setOpcode(opcode);

	System.out.println("EX - pc="+OF_EX_Latch.getPC());

	if(CUnit.isBubble()) {
		System.out.println("EX - bubble detected");
		EX_MA_Latch.setOpcode(opcode);
		OF_EX_Latch.setEX_enable(false);
		EX_MA_Latch.setMA_enable(true);
		System.out.println("EX - not using any register");
		CUnit.setExCalculating(-1);
		CUnit.setExPassing(-1);
		e.setEventTime( Clock.getCurrentTime() + 1);
		Simulator.getEventQueue().addEvent(e);
		System.out.println(" not able to execute event handler for execute stage because CUint.isBubble is true");
	}
	
	else if ( EX_MA_Latch.isMA_busy()) {
		// for bubble case just wait for next cycle 
		
		e.setEventTime( Clock.getCurrentTime() + 1);
		Simulator.getEventQueue().addEvent(e);
		System.out.println(" not able to execute event handler for execute stage because CUint.isBubble is true");
		
	}
	else{
	System.out.println("executing  event handler for execute stage because CUint.isBubble and isMA_busy is false");

	ALU alu = new ALU(containingProcessor);

	int op1 = containingProcessor.getRegisterFile().getValue(OF_EX_Latch.getOp1());
	int op2 = 0;
	int rd = containingProcessor.getRegisterFile().getValue(OF_EX_Latch.getRd());
	//IMPORTANT: From now onwards (in this stage) rd now contains value of the register rd

	if((CUnit.isR2I() || CUnit.isR3()) && (!CUnit.isBranch()) && (!CUnit.isMAused())) {
		System.out.println("EX - computing for register"+OF_EX_Latch.getRd());
		EX_MA_Latch.setRegisterPassing(OF_EX_Latch.getRd());
		CUnit.setExCalculating(OF_EX_Latch.getRd());
	}else {
		System.out.println("EX - not using any register");
		CUnit.setExCalculating(-1);
		EX_MA_Latch.setRegisterPassing(-1);
	}
	
	if(CUnit.isLoad() || CUnit.isStore()) {
		CUnit.setExPassing(OF_EX_Latch.getRd());
	}else {
		CUnit.setExPassing(-1);
	}
	
	if(CUnit.isR2I()) {
		op2=OF_EX_Latch.getImmediate();
	}else {
		op2=containingProcessor.getRegisterFile().getValue(OF_EX_Latch.getOp2());
	}
	
	
	

	int aluResult = alu.operate(op1, op2, rd, CUnit.getOpcode());

	if(CUnit.isStore()) {
		
		op1 = containingProcessor.getRegisterFile().getValue(OF_EX_Latch.getOp1());
		op2 = containingProcessor.getRegisterFile().getValue(OF_EX_Latch.getOp2());
	}

	if(CUnit.isBeq() && alu.isBeqflag()) {
		System.out.println("beq matches");
		EX_IF_Latch.setBranchTaken(true);
	}else if(CUnit.isBne() && alu.isBneflag()) {
		System.out.println("bne matches");
		EX_IF_Latch.setBranchTaken(true);
	}else if(CUnit.isBlt() && alu.isBltflag()) {
		System.out.println("blt matches");
		EX_IF_Latch.setBranchTaken(true);
	}else if(CUnit.isBgt() && alu.isBgtflag()) {
		System.out.println("bgt matches");
		EX_IF_Latch.setBranchTaken(true);
	}else if(CUnit.isJmp()) {
		System.out.println("jmp matches");
		EX_IF_Latch.setBranchTaken(true);
	}
	else {
		EX_IF_Latch.setBranchTaken(false);
	}

//	if(EX_IF_Latch.isBranchTaken()) {
//		//branching, bubble out the instructions in IF_OF latch and OF_EX latch
//		System.out.println("EX - branching, bubbling IF_OF and OF_EX");
//		IF_OF_Latch.setOpcode(31);
//		OF_EX_Latch.setOpcode(31);
//	}


	if(CUnit.isJmp()) {
		System.out.println("EX - is jmp is true PC:"+OF_EX_Latch.getPC()+" rd="+OF_EX_Latch.getRd()+" imm="+OF_EX_Latch.getImmediate());
		EX_IF_Latch.setBranchPC(OF_EX_Latch.getPC()+containingProcessor.getRegisterFile().getValue(OF_EX_Latch.getRd())+OF_EX_Latch.getImmediate());
	}else{
		EX_IF_Latch.setBranchPC(OF_EX_Latch.getBranchTarget());
	}

	if(EX_IF_Latch.isBranchTaken() == true) {
		//2 wrong instructions went through
		containingProcessor.IncrementWrongInstructions();
	}

	//if(CUnit.isBubble()) {
		//EX_IF_Latch.setBranchTaken(true);
		//EX_IF_Latch.setBranchPC(OF_EX_Latch.getPC());
	//}

	EX_MA_Latch.setAluResult(aluResult);
	EX_MA_Latch.setOp2(op2);
	EX_MA_Latch.setRd(OF_EX_Latch.getRd());
	EX_MA_Latch.setPC(OF_EX_Latch.getPC());
	EX_MA_Latch.setImmediate(OF_EX_Latch.getImmediate());
	EX_MA_Latch.setOpcode(opcode);
	EX_MA_Latch.setInstruction(OF_EX_Latch.getInstruction());
	
	OF_EX_Latch.setEX_enable(false);
	EX_MA_Latch.setMA_enable(true);
	OF_EX_Latch.setEX_busy( false);
	

}

}
}