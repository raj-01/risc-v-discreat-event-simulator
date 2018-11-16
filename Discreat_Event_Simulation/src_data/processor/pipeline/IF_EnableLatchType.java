package processor.pipeline;

public class IF_EnableLatchType {
	
	boolean IF_enable;
	static boolean isIF_busy ; 
	public IF_EnableLatchType()
	{
		IF_enable = true;
		isIF_busy = false ; 
		EX_MA_LatchType.isMA_busy = false ; 
		OF_EX_LatchType.isEX_busy = false ; 
		IF_OF_LatchType.isOF_busy = false ; 
	}

	public boolean isIF_enable() {
		return IF_enable;
	}

	public void setIF_enable(boolean iF_enable) {
		IF_enable = iF_enable;
	}

	public static boolean isIF_busy() {
		return isIF_busy;
	}

	public void setIF_busy(boolean b ) {
		isIF_busy = b ; 
	}

}
