package processor.pipeline;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	static boolean isEX_busy ;
	int immediate;
	int branchTarget;
	int op1;
	int op2;
	int PC;
	int rd;
	int opcode;
	int instruction;
	boolean branchTaken;
	int previousOFpc;
	
	
	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	public int getOpcode() {
		return opcode;
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	public OF_EX_LatchType()
	{
		EX_enable = false;
		branchTaken = false;
	}

	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}
	public int getImmediate() {
		return immediate;
	}

	public void setImmediate(int immediate) {
		this.immediate = immediate;
	}

	public int getBranchTarget() {
		return branchTarget;
	}

	public void setBranchTarget(int branchTarget) {
		this.branchTarget = branchTarget;
	}

	public int getOp1() {
		return op1;
	}

	public void setOp1(int op1) {
		this.op1 = op1;
	}

	public int getOp2() {
		return op2;
	}

	public void setOp2(int op2) {
		this.op2 = op2;
	}

	public int getPC() {
		return PC;
	}

	public void setPC(int pC) {
		PC = pC;
	}

	public int getRd() {
		return rd;
	}

	public void setRd(int rd) {
		this.rd = rd;
	}

	public static void setEX_busy(boolean b) {
		 isEX_busy = b ; 
		
	}
	
	public static boolean isEX_busy() {
		return isEX_busy ; 
	}

}
