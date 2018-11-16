package processor.pipeline;

import processor.Processor;

public class ControlUnit {
	Processor containingProcessor;
	int opcode;
	String opcodeStr;
	static boolean clash;
	static int exCalculating;
	static int maLoading;
	static int maPassing;
	static int rwWriting;
	static int exPassing;
	
	static int rwForward;
	
	public ControlUnit(Processor containingProcessor)
	{
		this.containingProcessor = containingProcessor;
		this.exCalculating=-1;
		this.maLoading=-1;
		this.rwWriting=-1;
		this.clash=false;
		this.maPassing=-1;
		this.exPassing=-1;
	}
	
	public static int getExPassing() {
		return exPassing;
	}

	public static void setExPassing(int exPassing) {
		ControlUnit.exPassing = exPassing;
	}

	public boolean isBlank(String instruction) {
		return Long.parseLong(instruction, 2)==0;
	}
	
	public static int getMaPassing() {
		return maPassing;
	}

	public static void setMaPassing(int maPassing) {
		ControlUnit.maPassing = maPassing;
	}
	
	public static int getRwForward() {
		return rwForward;
	}
	
	public boolean isBubble() {
		return opcodeStr.equals("11111");
	}

	public static void setRwForward(int rwForward) {
		ControlUnit.rwForward = rwForward;
	}

	public static boolean isClash() {
		return clash;
	}

	public static void setClash(boolean clash) {
		ControlUnit.clash = clash;
	}

	public int getExCalculating() {
		return exCalculating;
	}

	public void setExCalculating(int exCalculating) {
		this.exCalculating = exCalculating;
	}

	public int getMaLoading() {
		return maLoading;
	}

	public static void setMaLoading(int maLoading) {
		maLoading = maLoading;
	}

	public int getRwWriting() {
		return rwWriting;
	}

	public void setRwWriting(int rwWriting) {
		this.rwWriting = rwWriting;
	}

	public int getOpcode() {
		return opcode;
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
		this.opcodeStr = String.format("%5s", Integer.toBinaryString(opcode)).replace(' ', '0');
	}
	
	public boolean isJmp() {
		return opcodeStr.equals("11000");
	}
	
	public boolean isStore() {
		return opcodeStr.equals("10111");
	}
	
	public boolean isR3() {
		return (opcodeStr.equals("00000") ||
				opcodeStr.equals("00010") ||
				opcodeStr.equals("00100") ||
				opcodeStr.equals("00110") ||
				opcodeStr.equals("01000") ||
				opcodeStr.equals("01010") ||
				opcodeStr.equals("01100") ||
				opcodeStr.equals("01110") ||
				opcodeStr.equals("10000") ||
				opcodeStr.equals("10010") ||
				opcodeStr.equals("10100"));
	}
	
	public boolean isR2I() {
		return (opcodeStr.equals("00001") ||
				opcodeStr.equals("00011") ||
				opcodeStr.equals("00101") ||
				opcodeStr.equals("00111") ||
				opcodeStr.equals("01001") ||
				opcodeStr.equals("01011") ||
				opcodeStr.equals("01101") ||
				opcodeStr.equals("01111") ||
				opcodeStr.equals("10001") ||
				opcodeStr.equals("10011") ||
				opcodeStr.equals("10101") ||
				opcodeStr.equals("10110") ||
				opcodeStr.equals("10111") ||
				opcodeStr.equals("11001") ||
				opcodeStr.equals("11010") ||
				opcodeStr.equals("11011") ||
				opcodeStr.equals("11100"));
	}
	
	public boolean isRI() {
		if(this.isR2I() || this.isR3()) return false;
		else return true;
	}
	
	public boolean isBeq() {
		return opcodeStr.equals("11001");
	}
	
	public boolean isBne() {
		return opcodeStr.equals("11010");
	}
	
	public boolean isBlt() {
		return opcodeStr.equals("11011");
	}
	
	public boolean isBgt() {
		return opcodeStr.equals("11100");
	}
	
	public boolean isLoad() {
		return opcodeStr.equals("10110");
	}
	
	public boolean isEnd() {
		return opcodeStr.equals("11101");
	}
	
	public boolean isBranch() {
		return (this.isBeq() || this.isBne() || this.isBlt() || this.isBgt() || this.isJmp());
	}
	
	public boolean isMAused() {
		return (this.isStore() || this.isLoad());
	}
	
	public boolean isBlank(int Instruction) {
		return Instruction==0;
	}

}
