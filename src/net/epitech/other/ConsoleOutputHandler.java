package net.epitech.other;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public abstract class ConsoleOutputHandler extends Handler {
	
	public ConsoleOutputHandler()
    {
    }
 
    @Override
    public native void flush();
 
    @Override
    public native void close() throws SecurityException;
 
    @Override
    public abstract void publish(LogRecord record);

}
