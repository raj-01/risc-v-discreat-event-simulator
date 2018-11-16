package processor.pipeline;

import processor.Processor;

public class ALU {
	
	Processor containingProcessor;
	static boolean beqflag;
	static boolean bgtflag;
	static boolean bneflag;
	static boolean bltflag;
	
	public ALU(Processor containingProcessor) {
		beqflag=false;
		bgtflag=false;
		bneflag=false;
		bltflag=false;
		this.containingProcessor = containingProcessor;
	}
	
	public int operate(int op1_input, int op2_input, int rd_input, int opcode_input) {
		
		int op1 = op1_input;
		int op2 = op2_input;
		int rd = rd_input;
		int opcode = opcode_input;
		
		System.out.println("ALU - opcode:"+Integer.toBinaryString(opcode));
		System.out.println("ALU - op1:"+op1);
		System.out.println("ALU - op2:"+op2);
		System.out.println("ALU - rd:"+rd);
		
		int result=-1;
		
		switch(String.format("%5s", Integer.toBinaryString(opcode)).replace(' ', '0')) {
			case "00000":
			case "00001":
			case "10110":
			case "10111":
				result= op1+op2;
				break;
			case "00010":
			case "00011":
				result= op1-op2;
				break;
			case "00100":
			case "00101":
				result= op1*op2;
				break;
			case "00110":
			case "00111":
				result= op1/op2;
				containingProcessor.getRegisterFile().setValue(31, op1%op2);
				break;
			case "01000":
			case "01001":
				result= op1 & op2;
				break;
			case "01010":
			case "01011":
				result= op1|op2;
				break;
			case "01100":
			case "01101":
				result= op1^op2;
				break;
			case "01110":
			case "01111":
				if(op1<op2) {
					result= 1;
				}else {
					result= 0;
				}
				break;
			case "10000":
			case "10001":
				result= op1<<op2;
				break;
			case "10010":
			case "10011":
				result= op1>>>op2;
				break;
			case "10100":
			case "10101":
				result= op1>>op2;
				break;
		}
		this.setBeqflag(op1==rd);
		this.setBneflag(op1!=rd);
		this.setBltflag(op1<rd);
		this.setBgtflag(op1>rd);
		
		System.out.println("ALU - result returned:"+result);
		
		return result;
	}

	public boolean isBeqflag() {
		return beqflag;
	}

	public void setBeqflag(boolean beqflag) {
		this.beqflag = beqflag;
	}

	public boolean isBgtflag() {
		return bgtflag;
	}

	public void setBgtflag(boolean bgtflag) {
		this.bgtflag = bgtflag;
	}

	public boolean isBneflag() {
		return bneflag;
	}

	public void setBneflag(boolean bneflag) {
		this.bneflag = bneflag;
	}

	public boolean isBltflag() {
		return bltflag;
	}

	public void setBltflag(boolean bltflag) {
		this.bltflag = bltflag;
	}
	
}
