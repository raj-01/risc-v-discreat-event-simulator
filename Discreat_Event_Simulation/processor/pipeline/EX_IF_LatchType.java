package processor.pipeline;

public class EX_IF_LatchType {
	
	boolean isBranchTaken;
	int branchPC;
	
	public EX_IF_LatchType(RegisterFile registerFile)
	{
		isBranchTaken=true;
		branchPC=registerFile.getProgramCounter();
	}

	public boolean isBranchTaken() {
		return isBranchTaken;
	}

	public void setBranchTaken(boolean isBranchTaken) {
		System.out.println("EX_IF_latch: setting isBranchTaken:"+isBranchTaken);
		this.isBranchTaken = isBranchTaken;
	}

	public int getBranchPC() {
		return branchPC;
	}

	public void setBranchPC(int branchPC) {
		System.out.println("EX_IF_latch: setting branchPC:"+branchPC);
		this.branchPC = branchPC;
	}

}
