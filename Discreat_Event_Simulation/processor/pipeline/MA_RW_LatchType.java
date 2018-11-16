package processor.pipeline;

public class MA_RW_LatchType {
	
	static boolean RW_enable;
	int ldResult;
	int aluResult;
	int rd;
	int opcode;
	int PC;
	int instruction;
	
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

	public int getOpcode() {
		return opcode;
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	public MA_RW_LatchType()
	{
		RW_enable = false;
	}

	public static boolean isRW_enable() {
		return RW_enable;
	}

	public static void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}

	public int getLdResult() {
		return ldResult;
	}

	public void setLdResult(int ldResult) {
		this.ldResult = ldResult;
	}

	public int getAluResult() {
		return aluResult;
	}

	public void setAluResult(int aluResult) {
		this.aluResult = aluResult;
	}

	public int getRd() {
		return rd;
	}

	public void setRd(int rd) {
		this.rd = rd;
	}

}
