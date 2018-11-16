package generic;

import processor.Clock;
import processor.Processor;
import processor.memorysystem.MainMemory;
import java.io.*;
import processor.pipeline.RegisterFile;

public class Simulator {
		
	static Processor processor;
	static EventQueue eventQueue ;
	static boolean simulationComplete;
	
	public static void setupSimulation(String assemblyProgramFile, Processor p ,EventQueue eventQueue )
	{
		Simulator.processor = p;
		Simulator.eventQueue = eventQueue ; 
		loadProgram(assemblyProgramFile);
		
		simulationComplete = false;
	}
	
	static void loadProgram(String assemblyProgramFile)
	{
//		System.out.println("loadProgram called!");
		/*
		 * TODO
		 * 1. load the program into memory according to the program layout described
		 *    in the ISA specification
		 * 2. set PC to the address of the first instruction in the main
		 * 3. set the following registers:
		 *     x0 = 0
		 *     x1 = 65535
		 *     x2 = 65535
		 */
		
		MainMemory mainmemory = Simulator.processor.getMainMemory();
	    int currMemLocation=0;
	    
		try
		   {
		     DataInputStream instr = 
		       new DataInputStream(
		         new BufferedInputStream(
		           new FileInputStream( assemblyProgramFile ) ) );
		     	
		     int instruction;
		     Boolean pcSet=false;
		     processor.getRegisterFile().setValue(0, 0);
		     processor.getRegisterFile().setValue(1, 65535);
		     processor.getRegisterFile().setValue(2, 65535);

		     
		     while(true) {
			     try {
			    	 instruction = instr.readInt();
			    	 if(pcSet==false) {
			    		 processor.getRegisterFile().setProgramCounter(instruction);
			    		 processor.getEX_IF_Latch().setBranchPC(instruction);
			    		 System.out.println("Simulator - pc set to "+instruction);
			    		 pcSet=true;
			    	 }else {
			    		 mainmemory.setWord(currMemLocation, instruction);
			    		 currMemLocation++;
			    	 }
			    	 
			     }
			     catch(EOFException e) {
			    	 break;
			     }
		     }
		    
		     instr.close();
		   }
		   catch ( IOException iox )
		   {
		     System.out.println("Problem reading " + assemblyProgramFile );
		   }
		
		System.out.println(mainmemory.getContentsAsString(0,currMemLocation+10));
	}
	
	public static void simulate()
	{
		MainMemory mainmemory = Simulator.processor.getMainMemory();
		while(simulationComplete == false)
		{
			System.out.println("-----------------"+processor.getnCycles());
			if(processor.getnCycles()==20000) {
				return;
			}
			processor.getRWUnit().performRW();
			//Clock.incrementClock();
			processor.getMAUnit().performMA();
			//Clock.incrementClock();
			processor.getEXUnit().performEX();
			
			
			
			processor.getOFUnit().performOF();
			//Clock.incrementClock();
			processor.getIFUnit().performIF();
			eventQueue.processEvents();
			processor.IncrementCycle();
			Clock.incrementClock();
			//Clock.incrementClock();
			
			
			
		}
		
		// TODO
		// set statistics

//		System.out.println("___________________");
		System.out.println("Simulation complete");
		System.out.println(processor.getnCycles());
		Statistics.setNumberOfOFStalls(processor.getOFStalls());
		Statistics.setNumberofWrongInstructions(processor.getWrongInstructions());
//		System.out.println("Number of cycles = "+processor.getnCycles());
//		System.out.println("Number of OFstalls = "+processor.getOFStalls());
//		System.out.println("Number of Wrong instructions entering the pipeline = "+processor.getWrongInstructions());

//		System.out.println(mainmemory.getContentsAsString(0,65535));
		
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
	
	public static  EventQueue getEventQueue() {
		return eventQueue ; 
		
	}
}
