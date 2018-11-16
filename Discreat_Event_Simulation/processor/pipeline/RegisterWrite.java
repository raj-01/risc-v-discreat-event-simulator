package processor.pipeline;

import generic.Simulator;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	ControlUnit CUnit;
	int rd;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch, ControlUnit CUnit)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
		this.CUnit = CUnit;
	}
	
	public void performRW()
	{
		System.out.println("Register Writeback reached");
		
		if(MA_RW_Latch.isRW_enable())
		{
			System.out.println("Register Writeback enabled");
			
			int opcode = MA_RW_Latch.getOpcode();
			CUnit.setOpcode(opcode);
			int instruction = MA_RW_Latch.getInstruction();
			
			System.out.println("RW - pc="+MA_RW_Latch.getPC());
			
			if(CUnit.isBubble()) {
				System.out.println("RW - bubble detected");
				MA_RW_Latch.setRW_enable(false);
				IF_EnableLatch.setIF_enable(true);
				System.out.println("RW - not using any register");
				CUnit.setRwWriting(-1);
				return;
			}
			
			//TODO
			
			// if instruction being processed is an end instruction, remember to call Simulator.setSimulationComplete(true);
/////////////////////////////////not required //////////////////////////////////////////////////////////////////////////////////
			rd = MA_RW_Latch.getRd();
			
			boolean enable = ((CUnit.isR3() || CUnit.isR2I()) && (!CUnit.isBeq() && !CUnit.isBlt() && !CUnit.isBgt() && !CUnit.isBne() && !CUnit.isStore()));
			
			if(enable && ! CUnit.isBubble()) {
				System.out.println("RW - enabled");
				int data=0;
				if(CUnit.isLoad()) {
					data = MA_RW_Latch.getLdResult();
				}else{
					data = MA_RW_Latch.getAluResult();
				}
				System.out.println("RW - writing register"+rd);
				CUnit.setRwWriting(rd);
				
				System.out.println("RW - setting "+data+" at register"+rd);
				CUnit.setRwForward(data);
				
				containingProcessor.getRegisterFile().setValue(rd, data);				
			}else {
				System.out.println("RW - not using any register");
				CUnit.setRwWriting(-1);
			}
/////////////////////////////////not required /////////////////////////////////////////////////////////////////////////////
			
			/// clearing all variable and initialising them again after the bubble passed from register write.
			CUnit.exCalculating=-1;
			CUnit.maLoading=-1;
			CUnit.rwWriting=-1;
			CUnit.clash=false;
			CUnit.maPassing=-1;
			CUnit.exPassing=-1;
			
			containingProcessor.IncrementInstruction();
			
			if(CUnit.isEnd() || CUnit.isBlank(instruction)) {
				Simulator.setSimulationComplete(true);
				containingProcessor.getRegisterFile().decrementProgramCounter();
//				System.out.println(containingProcessor.getMainMemory().getContentsAsString(0, 65535));
				System.out.println("number of instruction fetched &&&&&&&&&&&&&&&&&&&&&&&&&&&&" + InstructionFetch.num_inst_fetch() );
				System.out.println("number of opcode processed &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + OperandFetch.num_opcode() );
			}
			
			
			if( !CUnit.isBubble() ) {
				MA_RW_Latch.setRW_enable(false);
				}
			
			IF_EnableLatch.setIF_enable(true);
			
		}
	}

}
