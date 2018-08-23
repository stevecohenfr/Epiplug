package net.epitech.other;

import java.util.logging.LogRecord;

public class CaptureConsole extends ConsoleOutputHandler {

	@Override
	public void publish(LogRecord record) {
		record.getMessage();
	}

	
}
