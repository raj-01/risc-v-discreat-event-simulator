package generic;

import java.io.PrintWriter;

public class Statistics {
	
	// TODO add your statistics here
	static int numberOfInstructions;
	static int numberOfCycles;
	static int numberOfOFStalls;
	static int numberofWrongInstructions;

	public static void printStatistics(String statFile)
	{
		try
		{
			PrintWriter writer = new PrintWriter(statFile);
			System.out.println("Number of instructions executed = " + numberOfInstructions);
			System.out.println("Number of cycles taken = " + numberOfCycles);
			System.out.println("Number of OF Stalls taken = " + numberOfOFStalls);
			System.out.println("Number of Wrong Instructions that entered pipeline = " + numberofWrongInstructions);
			
			writer.println("Number of instructions executed = " + numberOfInstructions);
			writer.println("Number of cycles taken = " + numberOfCycles);
			writer.println("Number of OF Stalls taken = " + numberOfOFStalls);
			writer.println("Number of Wrong Instructions that entered pipeline = " + numberofWrongInstructions);
			
			// TODO add code here to print statistics in the output file
			
			writer.close();
		}
		catch(Exception e)
		{
			Misc.printErrorAndExit(e.getMessage());
		}
	}
	
	// TODO write functions to update statistics
	public static void setNumberOfInstructions(int numberOfInstructions) {
		Statistics.numberOfInstructions = numberOfInstructions;
	}

	public static void setNumberOfCycles(int numberOfCycles) {
		Statistics.numberOfCycles = numberOfCycles;
	}

	public static void setNumberOfOFStalls(int numberOfOFStalls) {
		Statistics.numberOfOFStalls = numberOfOFStalls;
	}

	public static void setNumberofWrongInstructions(int numberofWrongInstructions) {
		Statistics.numberofWrongInstructions = numberofWrongInstructions;
	}
}
