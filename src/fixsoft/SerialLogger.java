package fixsoft;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Serial Logger class --
 * 
 * This class handles the input stream from the port.  Whenever an event happens over the port this will catch it and do something with it
 * 
 * @author Matt Martucciello
 *
 */

public class SerialLogger implements SerialPortEventListener {
	String portName;

	SerialPort thePort;
	FixSoft fixsoft;
	BufferedReader ifile;

	public SerialLogger(String name, SerialPort port, FixSoft fixsoft) throws IOException {
		portName = name;
		thePort = port;
		this.fixsoft = fixsoft;
		ifile = new BufferedReader(new InputStreamReader(thePort.getInputStream()));
	}

	public void serialEvent(SerialPortEvent ev) {
		String line;
		try {
			line = ifile.readLine().trim();
			if (line == null)
				fixsoft.getFixsoftui().getMessageArea().append("EOF on serial port.\n");
			fixsoft.parseLines(line);
			//System.out.println(line.substring(line.indexOf(" ") + 1, line.lastIndexOf(" ")).trim());
			//System.out.println(line.substring(line.lastIndexOf(" ") + 1, line.length()));
		} catch (IOException ex) {
			fixsoft.getFixsoftui().getMessageArea().append("IO Error " + ex + "\n");
		}
	}
}
