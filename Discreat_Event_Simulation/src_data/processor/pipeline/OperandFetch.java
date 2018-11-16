package processor.pipeline;

import processor.Processor;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	ControlUnit CUnit;
	String instruction;
	int immediate;
	int branchTarget;
	int op1;
	int op2;
	static int num_opcode ; 
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, ControlUnit CUnit)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.CUnit = CUnit;
	}
	
	public static int getTwosComplement(String binaryInt) {
	    //Check if the number is negative.
	    //We know it's negative if it starts with a 1
	    if (binaryInt.charAt(0) == '1') {
	        //Call our invert digits method
	        String invertedInt = invertDigits(binaryInt);
	        //Change this to decimal format.
	        int decimalValue = Integer.parseInt(invertedInt, 2);
	        //Add 1 to the current decimal and multiply it by -1
	        //because we know it's a negative number
	        decimalValue = (decimalValue + 1) * -1;
	        //return the final result
	        return decimalValue;
	    } else {
	        //Else we know it's a positive number, so just convert
	        //the number to decimal base.
	        return Integer.parseInt(binaryInt, 2);
	    }
	}

	public static String invertDigits(String binaryInt) {
	    String result = binaryInt;
	    result = result.replace("0", " "); //temp replace 0s
	    result = result.replace("1", "0"); //replace 1s with 0s
	    result = result.replace(" ", "1"); //put the 1s back in
	    return result;
	}
	
	public void performOF()
	{
		System.out.println("Operation fetch reached");
		
		if(IF_OF_Latch.isOF_enable()) {
			
			
			int pc;
			System.out.println("Operation fetch enabled");
			//TODO
			int opcode = IF_OF_Latch.getOpcode();
		    CUnit.setOpcode(opcode);
		
			
//			if(IF_OF_Latch.getOFstallNext()) {
//				System.out.println("OF - bubble detected");
//				OF_EX_Latch.setOpcode(31);
//				IF_OF_Latch.setOF_enable(false);
//				OF_EX_Latch.setEX_enable(true);
//				System.out.println("OF - not using any register");
//				IF_OF_Latch.setOFstallNext(false);
//				return;
//			}
			
			if(CUnit.isBubble()) {
				System.out.println("OF - bubble detected");
				OF_EX_Latch.setOpcode(opcode);
				IF_OF_Latch.setOF_enable(false);
				OF_EX_Latch.setEX_enable(true);
				System.out.println("OF - not using any register");
				return;
			}
			
			
			
			////////////////////////////////////////////////////////////////////////
			if( OF_EX_LatchType.isEX_busy()  ) { //|| IF_EnableLatchType.isIF_busy() ) {
				IF_OF_Latch.setOF_busy(true);
				System.out.println(" OF is seeing that EX is busy so return");
				return ;
			}
			else {
				IF_OF_Latch.setOF_busy(false);
			}
			///////////////////////////////////////////////////////////////////////
			
			
			
			
			System.out.println("OF - Clash exists:"+CUnit.isClash());
			
			if(!CUnit.isClash()) {
				//No clash, take instruction and PC from IF_OF latch
				instruction = String.format("%32s", Integer.toBinaryString(IF_OF_Latch.getInstruction())).replace(' ', '0');
				OF_EX_Latch.setInstruction(IF_OF_Latch.getInstruction());
				pc = IF_OF_Latch.getPC();
			}else {				
				//Clash, take previous instruction and PC from OF_EX latch
				System.out.println("** Incrementing OF Stalls");
				containingProcessor.IncrementStalls();
				
				instruction = String.format("%32s", Integer.toBinaryString(OF_EX_Latch.getInstruction())).replace(' ', '0');
				pc = OF_EX_Latch.getPC();
				System.out.println("OF - clash exists, restoring PC:" + OF_EX_Latch.getPC() + " and instruction:" + instruction);
			}
				
			System.out.println("OF - instruction:"+instruction);
			
			//setting opcode (in control unit)
			opcode = Integer.parseInt(instruction.substring(0, 5), 2);
			
			
			
			CUnit.setOpcode(opcode);
			num_opcode++ ; 
			
			System.out.println("OP code in OF stage :" + opcode ) ; 
			
			
			//calculating immediate
			if(CUnit.isJmp()) {
				immediate = getTwosComplement(instruction.substring(10, 32));
				System.out.println("OF - jmp, imm="+immediate);
			}else {
				immediate = getTwosComplement(instruction.substring(15, 32));
				System.out.println("OF - nonjmp, imm="+immediate);
			}
			
//			System.out.println("OF - immediate="+immediate);
			
			//calculating branchTarget
			branchTarget = getTwosComplement(instruction.substring(15, 32))+IF_OF_Latch.getPC();
			
			if(IF_OF_Latch.isIFStall()) {
				branchTarget = getTwosComplement(instruction.substring(15, 32))+IF_OF_Latch.getPC()-1;
			}
			
			System.out.println("OF - imm="+getTwosComplement(instruction.substring(15, 32)));
			System.out.println("OF - pc="+pc);
			
			if(pc == OF_EX_Latch.previousOFpc) {
				
			}
			
			OF_EX_Latch.previousOFpc=pc;
			
			System.out.println("OF - branchTarget="+branchTarget);
//			System.out.println(branchTarget);
			
			//calculating op1 and op2
			int rs1 = Integer.parseInt(instruction.substring(5, 10), 2);
			int rs2 = Integer.parseInt(instruction.substring(10, 15), 2);
			
			int rd = 0;
			if(CUnit.isR3()) {
				rd = Integer.parseInt(instruction.substring(15, 20), 2);
			}else if(CUnit.isR2I()) {
				rd = Integer.parseInt(instruction.substring(10, 15), 2);
			}else if(CUnit.isRI()) {
				rd = Integer.parseInt(instruction.substring(5, 10), 2);
			}
			
			if(CUnit.isJmp() || CUnit.isStore()) {
				op1 = rd;
			}else {
				op1 = rs1;
			}
			System.out.println("OF - op1="+op1);
			
			if(CUnit.isStore()) {
				op2 = rs1;
			}else {
				op2 = rs2;
			}
			System.out.println("OF - op2="+op2);
			
			CUnit.setClash(false);
			
			if((CUnit.isR2I()) && (!CUnit.isBranch()) && (!CUnit.isBlank(instruction))) {
				//checking subsequent units for R2I type
				if(rs1 == CUnit.getExCalculating()) {
					//clash of clans
					System.out.println("OF - rs1 = " + rs1 + " matching getExCalculating, clash detected");
					CUnit.setClash(true);
				}else if(rs1 == CUnit.getMaLoading()) {
					//clash of clans
					System.out.println("OF - rs1 = " + rs1 + " matching getMaLoading, clash detected");
					CUnit.setClash(true);
				}else if(rs1 == CUnit.getRwWriting()) {
					//clash of clans
					//rs1 = CUnit.getRwForward();
					System.out.println("OF - rs1 = " + rs1 + " matching getRwWriting, clash detected");
					CUnit.setClash(true);
				}else if(rs1 == CUnit.getMaPassing()) {
					//clash of clans
					//rs1 = CUnit.getRwForward();
					System.out.println("OF - rs1 = " + rs1 + " matching getMaPassing, clash detected");
					CUnit.setClash(true);
				}else if(rs1 == CUnit.getExPassing()) {
					//clash of clans
					//rs1 = CUnit.getRwForward();
					System.out.println("OF - rs1 = " + rs1 + " matching getExPassing, clash detected");
					CUnit.setClash(true);
				}
			}
			
			if((CUnit.isR3()) && (!CUnit.isBranch()) && (!CUnit.isMAused()) && (!CUnit.isBlank(instruction))) {
				//checking subsequent units for R2I type
				if(rs2 == CUnit.getExCalculating()) {
					//clash of clans
					System.out.println("OF - rs2 = " + rs2 + " matching getExCalculating, clash detected");
					CUnit.setClash(true);
				}else if(rs2 == CUnit.getMaLoading()) {
					//clash of clans
					System.out.println("OF - rs2 = " + rs2 + " matching getMaLoading, clash detected");
					CUnit.setClash(true);
				}else if(rs2 == CUnit.getRwWriting()) {
					//clash of clans
					//rs1 = CUnit.getRwForward();
					System.out.println("OF - rs2 = " + rs2 + " matching getRwWriting, clash detected");
					CUnit.setClash(true);
				}else if(rs2 == CUnit.getMaPassing()) {
					//clash of clans
					//rs1 = CUnit.getRwForward();
					System.out.println("OF - rs2 = " + rs2 + " matching getExPassing, clash detected");
					CUnit.setClash(true);
				}else if(rs2 == CUnit.getExPassing()) {
					//clash of clans
					//rs1 = CUnit.getRwForward();
					System.out.println("OF - rs2 = " + rs2 + " matching getMaPassing, clash detected");
					CUnit.setClash(true);
				}
			}
			
			if((CUnit.isBranch()) && (!CUnit.isJmp()) && (!CUnit.isBlank(instruction))) {
				//checking rs1 in subsequent units for branching
				if(rs1 == CUnit.getExCalculating()) {
					//clash of clans
					System.out.println("OF - rs1 = " + rs1 + " matching getExCalculating, clash detected");
					CUnit.setClash(true);
				}else if(rs1 == CUnit.getMaLoading()) {
					//clash of clans
					System.out.println("OF - rs1 = " + rs1 + " matching getMaLoading, clash detected");
					CUnit.setClash(true);
				}else if(rs1 == CUnit.getRwWriting()) {
					//clash of clans
					//rs1 = CUnit.getRwForward();
					System.out.println("OF - rs1 = " + rs1 + " matching getRwWriting, clash detected");
					CUnit.setClash(true);
				}else if(rs1 == CUnit.getMaPassing()) {
					//clash of clans
					//rs1 = CUnit.getRwForward();
					System.out.println("OF - rs1 = " + rs1 + " matching getMaPassing, clash detected");
					CUnit.setClash(true);
				}else if(rs1 == CUnit.getExPassing()) {
					//clash of clans
					//rs1 = CUnit.getRwForward();
					System.out.println("OF - rs1 = " + rs1 + " matching getExPassing, clash detected");
					CUnit.setClash(true);
				}
			}
			
			if((CUnit.isBranch()) && (!CUnit.isJmp()) && (!CUnit.isBlank(instruction))) {
				//checking rd in subsequent units for branching
				if(rd == CUnit.getExCalculating()) {
					//clash of clans
					System.out.println("OF - rd = " + rd + " matching getExCalculating, clash detected");
					CUnit.setClash(true);
				}else if(rd == CUnit.getMaLoading()) {
					//clash of clans
					System.out.println("OF - rd = " + rd + " matching getMaLoading, clash detected");
					CUnit.setClash(true);
				}else if(rd == CUnit.getRwWriting()) {
					//clash of clans
					//rs1 = CUnit.getRwForward();
					System.out.println("OF - rd = " + rd + " matching getRwWriting, clash detected");
					CUnit.setClash(true);
				}else if(rd == CUnit.getMaPassing()) {
					//clash of clans
					//rs1 = CUnit.getRwForward();
					System.out.println("OF - rd = " + rd + " matching getMaPassing, clash detected");
					CUnit.setClash(true);
				}else if(rd == CUnit.getExPassing()) {
					//clash of clans
					//rs1 = CUnit.getRwForward();
					System.out.println("OF - rd = " + rd + " matching getExPassing, clash detected");
					CUnit.setClash(true);
				}
			}
			
			
			OF_EX_Latch.setImmediate(immediate);
			OF_EX_Latch.setBranchTarget(branchTarget);
			OF_EX_Latch.setOp1(op1);
			OF_EX_Latch.setOp2(op2);
			/*int mdr = op1 + immediate   ; 
			if( mdr <100 && mdr >8  ) {
				System.out.println( mdr ) ; 
				System.exit(0);
			}*/
			OF_EX_Latch.setPC(pc);
			OF_EX_Latch.setRd(rd);
			OF_EX_Latch.setOp2(op2);
			OF_EX_Latch.setOpcode(opcode);
			
			OF_EX_Latch.setEX_enable(true) ; 
			IF_OF_Latch.setOF_busy( false); 
			
			
			
			if(CUnit.isClash() == false) {
				System.out.println("No bubble produced");
				IF_OF_Latch.setOF_enable(false);
				//OF_EX_Latch.setEX_enable(true);
				OF_EX_Latch.setOpcode(opcode);
				IF_OF_Latch.setIFStall(false);
			}else {
				//clash detected, increment counter
				//IF_OF_Latch.setOF_enable(false);
				System.out.println("OF - Stall setting IFstall true");
				IF_OF_Latch.setIFStall(true);
//				OF_EX_Latch.setInstruction(0);
				//OF_EX_Latch.setEX_enable(true);
				OF_EX_Latch.setOpcode(31);
			}		
			

		}
	}
	public static int num_opcode() {
		// TODO Auto-generated method stub
		return num_opcode ;
	}
	

}
