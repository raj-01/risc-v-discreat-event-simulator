package processor.pipeline;

public class IF_OF_LatchType {
	
	boolean OF_enable;
	static boolean isOF_busy ; 
	int instruction;
	int PC;
	boolean IFstall;
	boolean IFstallNext;
	int opcode; //used only for branching (opcode = 11111)
	boolean OFstallNext;
	int previousTempPC;
	boolean ignoreDataHazards;

	public boolean getOFstallNext() {
		return OFstallNext;
	}

	public void setOFstallNext(boolean oFstallNext) {
		OFstallNext = oFstallNext;
	}

	public int getOpcode() {
		return opcode;
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	public boolean isIFstallNext() {
		return IFstallNext;
	}

	public void setIFstallNext(boolean iFstallNext) {
		IFstallNext = iFstallNext;
	}

	public IF_OF_LatchType()
	{
		OF_enable = false;
		opcode = -1;
		IFstall = false;
		PC = 0;
	}

	public boolean isIFStall() {
		return IFstall;
	}

	public void setIFStall(boolean IFstall) {
		this.IFstall = IFstall;
	}

	public boolean isOF_enable() {
		return OF_enable;
	}

	public void setOF_enable(boolean oF_enable) {
		OF_enable = oF_enable;
	}

	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}
	
	public int getPC() {
		return PC;
	}

	public void setPC(int pC) {
		PC = pC;
	}

	public void setOF_busy(boolean b) {
		 isOF_busy = b ; 
		
	}
	
	public static  boolean isOF_busy() {
		return isOF_busy ; 
	}
	

}
