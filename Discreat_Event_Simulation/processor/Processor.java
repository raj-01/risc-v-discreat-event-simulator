package processor;

import processor.memorysystem.MainMemory;
import processor.pipeline.*;
import configuration.Configuration;
import processor.memorysystem.Cache ;

public class Processor {
	
	RegisterFile registerFile;
	MainMemory mainMemory;
	Cache cache ; 
	
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	InstructionFetch IFUnit;
	OperandFetch OFUnit;
	Execute EXUnit;
	MemoryAccess MAUnit;
	RegisterWrite RWUnit;
	ControlUnit CUnit;
	static int nInstructions;
	static int nCycles;
	static int nOFstalls;
	static int nWrongInstructions;
	
	int cacheIsize;
	int cacheDsize;
	
	public boolean isBranchTake=false;
	public int branchPC=0;
	
	public Processor()
	{
		registerFile = new RegisterFile();
		
		Cache cachei;
		Cache cached;
		
		IF_EnableLatch = new IF_EnableLatchType();
		IF_OF_Latch = new IF_OF_LatchType();
		OF_EX_Latch = new OF_EX_LatchType();
		EX_MA_Latch = new EX_MA_LatchType();
		EX_IF_Latch = new EX_IF_LatchType(registerFile);
		MA_RW_Latch = new MA_RW_LatchType();
		
		cacheIsize = 256;
		cacheDsize = 32;
		
		mainMemory = new MainMemory(EX_MA_Latch );
		
		cachei = new Cache (EX_MA_Latch, cacheIsize, Configuration.L1i_latency, this, "IF");
		cached = new Cache (EX_MA_Latch, cacheDsize, Configuration.L1d_latency, this, "MA");
		
		CUnit = new ControlUnit(this);
		IFUnit = new InstructionFetch(this, IF_EnableLatch, IF_OF_Latch, OF_EX_Latch, EX_IF_Latch, CUnit, cachei);
		OFUnit = new OperandFetch(this, IF_OF_Latch, OF_EX_Latch, CUnit);
		EXUnit = new Execute(this, OF_EX_Latch, EX_MA_Latch, EX_IF_Latch, IF_OF_Latch, CUnit);
		MAUnit = new MemoryAccess(this, EX_MA_Latch, MA_RW_Latch, CUnit, cached);
		RWUnit = new RegisterWrite(this, MA_RW_Latch, IF_EnableLatch, CUnit);

		
		this.nInstructions=0;
		this.nCycles=0;
		this.nOFstalls=0;
		this.nWrongInstructions=0;
	}
	
	public void printState(int memoryStartingAddress, int memoryEndingAddress)
	{
		System.out.println(registerFile.getContentsAsString());
		
		System.out.println(mainMemory.getContentsAsString(memoryStartingAddress, memoryEndingAddress));		
	}

	public RegisterFile getRegisterFile() {
		return registerFile;
	}

	public void setRegisterFile(RegisterFile registerFile) {
		this.registerFile = registerFile;
	}

	public MainMemory getMainMemory() {
		return mainMemory;
	}

	public void setMainMemory(MainMemory mainMemory) {
		this.mainMemory = mainMemory;
	}

	public InstructionFetch getIFUnit() {
		return IFUnit;
	}

	public OperandFetch getOFUnit() {
		return OFUnit;
	}

	public Execute getEXUnit() {
		return EXUnit;
	}

	public MemoryAccess getMAUnit() {
		return MAUnit;
	}

	public RegisterWrite getRWUnit() {
		return RWUnit;
	}
	
	public EX_IF_LatchType getEX_IF_Latch() {
		return EX_IF_Latch;
	}
	
	public void IncrementCycle() {
		nCycles = nCycles+1;
	}
	
	public void IncrementInstruction() {
		nInstructions = nInstructions+1;
	}
	
	public void DecrementInstruction() {
		nInstructions = nInstructions-1;
	}
	
	public int getnInstructions() {
		return nInstructions;
	}

	public int getnCycles() {
		return nCycles;
	}
	
	public void IncrementStalls() {
		nOFstalls=nOFstalls+1;
	}

	public int getOFStalls() {
		return nOFstalls;
	}
	
	public void IncrementWrongInstructions() {
		nWrongInstructions=nWrongInstructions+1;
	}
	
	public void DecrementWrongInstructions() {
		nWrongInstructions=nWrongInstructions-1;
	}

	public int getWrongInstructions() {
		return nWrongInstructions;
	}

}
