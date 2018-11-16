package processor.pipeline;

public class EX_MA_LatchType {
	
	static boolean MA_enable;
	static boolean isMA_busy ; 
	int rd;
	int op2;
	int aluResult;
	int immediate;
	int opcode;
	int PC;
	int registerPassing;
	int instruction;
	
	
	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	public int getRegisterPassing() {
		return registerPassing;
	}

	public void setRegisterPassing(int registerPassing) {
		this.registerPassing = registerPassing;
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

	public EX_MA_LatchType()
	{
		MA_enable = false;
		this.registerPassing = -1;
	}

	public boolean isMA_enable() {
		return MA_enable;
	}

	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}

	public int getRd() {
		return rd;
	}

	public void setRd(int rd) {
		this.rd = rd;
	}

	public int getOp2() {
		return op2;
	}

	public void setOp2(int op2) {
		this.op2 = op2;
	}

	public int getAluResult() {
		return aluResult;
	}

	public void setAluResult(int aluResult) {
		this.aluResult = aluResult;
	}

	public int getImmediate() {
		return immediate;
	}

	public void setImmediate(int immediate) {
		this.immediate = immediate;
	}

	public static void setMA_busy(boolean b) {
		isMA_busy = b ; 
		
	}

	public static boolean isMA_busy() {
		return isMA_busy ;
	}

}
