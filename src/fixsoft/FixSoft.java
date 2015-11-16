package fixsoft;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TooManyListenersException;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * FixSoft class --
 * 
 * The main class where all of the program comes back to.
 * A FixSoft object contains the ui, the ports, workbook and everything needed for the program.
 * @author Matt Martucciello
 *
 */

public class FixSoft {

	protected FixSoftUI fixsoftui;
	protected ExcelFile excelFile;
	protected CommPortIdentifier selectedIndentifier = null;
	protected String selectedIndentifierName;
	protected PortChooser port;
	protected SerialPort thePort = null;
	protected String unit;
	protected String measureregex = "[0-9]* *[-[0-9]]*.[0-9]* [a-z]*m[a-z]*";
	protected String statregex = "[a-zA-z]* *[-[0-9]]*.[0-9]* [a-z]*m[a-z]*";
	protected String measuremode = "unkown";
	protected Object[] options = {"Ferrous","Non-Ferrous"};
	protected ArrayList<Double> ferrous = new ArrayList<Double>();
	protected ArrayList<Double> nonferrous = new ArrayList<Double>();
	protected ArrayList<Double> unknown  = new ArrayList<Double>();
	protected double mean;
	protected double maxread;
	protected double minread;
	protected double stdev;
	protected Locale locale = new Locale("en", "US");
	protected ResourceBundle  localizedStrings = ResourceBundle.getBundle("resources.Strings",locale);

	/**
	 * default constructor --
	 * creates the UI object and porthandler object
	 */
	public FixSoft() {
		fixsoftui = new FixSoftUI(this);
		port = new PortChooser(null, this);
		fixsoftui.setVisible(true);
		fixsoftui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * getFixSoftui --
	 * returns a reference to the ui, useful for accessing ui elements like buttons and text areas
	 * The UI is never passed around only the main fixsoft object!!!
	 * @return a reference to the UI
	 */
	public FixSoftUI getFixsoftui() {
		return fixsoftui;
	}

	/**
	 * togglePort --
	 * Opens or closes the selected port
	 */
	public  void togglePort() {
		//Checks to see if a port was chosen
		if (selectedIndentifier == null) {
			JOptionPane.showMessageDialog(null, "Please Select a Port first");
		}
		else {
			//Checks the state of the port
			if ( fixsoftui.getPortButton().getText().equals(this.getLocalizedStrings().getString("OpenPort")) ) {
				try {
					thePort = (SerialPort) selectedIndentifier.open("SerialLogger", 1000);
					thePort.notifyOnDataAvailable(true);
					thePort.addEventListener(new SerialLogger(selectedIndentifierName, thePort, this));
					fixsoftui.getPortButton().setText(this.getLocalizedStrings().getString("ClosePort"));
					fixsoftui.getMessageArea().append("=====Port " + selectedIndentifierName + " Opened====\n");
				} catch (PortInUseException ev) {
					fixsoftui.getMessageArea().append("Port in use: " + selectedIndentifierName);
				}
				catch (TooManyListenersException ev) {
					fixsoftui.getMessageArea().append("Too many listeners(!) " + ev);
				} catch (IOException e) {
					fixsoftui.getMessageArea().append(e.getMessage());
				}
			}
			else {
				thePort.close();
				thePort.removeEventListener();

				fixsoftui.getPortButton().setText(this.getLocalizedStrings().getString("OpenPort"));

				fixsoftui.getMessageArea().append("=====Port Closed====\n");
			}
		}
	}

	/**
	 * clearScreen --
	 * simple enough, clears the text from the text area
	 */
	public void clearScreen() {
		fixsoftui.getMessageArea().setText(null);
	}

	/**
	 * portSettings --
	 * displays the port chooser window,
	 * check out the PortChooser class for more details
	 */
	public void portSettings() {

		port.setVisible(true);
	}

	/**
	 * updatePort--
	 * updates the variables that store the port information
	 * @param selectedPortName - Name of the port i.e COMM1
	 * @param selectedPortIdentifier - the chosen port identifier
	 */
	public void updatePort(String selectedPortName, CommPortIdentifier selectedPortIdentifier) {
		this.selectedIndentifierName = selectedPortName;
		this.selectedIndentifier = selectedPortIdentifier;	
	}

	/**
	 * excel --
	 * WIP, will create a new excel file that stores the data from the gauge, will be classed
	 */
	public void excel() {
		String test = fixsoftui.getMessageArea().getText();
		String test2[] = test.split("\n");
		System.out.println(Arrays.toString(test2));
		excelFile = new ExcelFile(this, "test");
		excelFile.createSheet(measuremode);
		excelFile.addCell(0, 0, "Mean");
		excelFile.addCell(0, 1, mean);
		excelFile.addCell(1, 0, "St. Dev.");
		excelFile.addCell(1, 1, stdev);
		excelFile.addCell(2, 0, "Max");
		excelFile.addCell(2, 1, maxread);
		excelFile.addCell(3, 0, "Min");
		excelFile.addCell(3, 1, minread);
		if (measuremode.equals("nonferrous")){
			for(int i = 4; i < nonferrous.size();i++){
				excelFile.addCell(i, 0, (i+1)-4);
				excelFile.addCell(i, 1, nonferrous.get(i));
				excelFile.addCell(i, 2, unit);
			}
		}
		else if (measuremode.equals("ferrous")){
			for(int i = 0; i < ferrous.size();i++){
				excelFile.addCell(i, 0, (i+1)-4);
				excelFile.addCell(i, 1, ferrous.get(i));
				excelFile.addCell(i, 2, unit);
			}			
		}
		excelFile.writeExcel();
	}

	public void parseLines(String line) {
		//condition to check which measuring mode
		if ( line.toLowerCase().equals("non-ferrous") || line.equals("nonferrous")  || line.equals("non ferrous") )
			measuremode = "nonferrous";
		//condition to check which measuring mode
		else if (line.toLowerCase().equals("ferrous"))
			measuremode = "ferrous";
		//START CONDITION FOR MEASUREMENTS
		//if the line matches the regex for readings like "1 1.0 um" it will add the reading to the appropriate array
		//if measuremode is not set it will prompt the user to select the correct measuring mode
		else if (line.matches(measureregex)){
			String[] linesplit = line.split(" ");
			unit = linesplit[linesplit.length-1].trim();
			if (measuremode.equals("ferrous"))
				ferrous.add(Double.parseDouble(linesplit[linesplit.length-2]));
			else if (measuremode.equals("nonferrous"))
				nonferrous.add(Double.parseDouble(linesplit[linesplit.length-2]));
			else {
				int selected = JOptionPane.showOptionDialog(null, "Please specifify if ferrous or non-ferrous", "Ferrous or Non-Ferros", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				if (selected == 0) {
					measuremode = "ferrous";
					ferrous.add(Double.parseDouble(linesplit[linesplit.length-2]));
				}
				else if (selected == 1) {
					measuremode = "nonferrous";
					nonferrous.add(Double.parseDouble(linesplit[linesplit.length-2]));
				}
				else
					unknown.add(Double.parseDouble(linesplit[linesplit.length-2]));
			}
		}
		else if (line.toLowerCase().contains("st.dev.")){
			String[] linesplit = line.split(" ");
			stdev = Double.parseDouble(linesplit[linesplit.length-2]);
		}
		else if (line.toLowerCase().contains("mean")){
			String[] linesplit = line.split(" ");
			mean = Double.parseDouble(linesplit[linesplit.length-2]);
		}
		else if (line.toLowerCase().contains("minimum")){
			String[] linesplit = line.split(" ");
			minread = Double.parseDouble(linesplit[linesplit.length-2]);
		}
		else if (line.toLowerCase().contains("max")){
			String[] linesplit = line.split(" ");
			maxread = Double.parseDouble(linesplit[linesplit.length-2]);
		}
		fixsoftui.getMessageArea().append(line + "\n");
	}

	/**
	 * textFile--
	 * WIP, creates a text file and writes the input stream from the port to the text file
	 * probably will not make it to a release, just for testing(easier to testing parsing and stuff with a text file then an excel file)
	 */
	public void textFile() {
		File file = new File("text.txt");
		Writer output;
		try {
			output = new BufferedWriter(new FileWriter(file));
			output.write(fixsoftui.getMessageArea().getText());
			output.close();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public ResourceBundle getLocalizedStrings() {
		return localizedStrings;
	}

	public void setLocalizedStrings(ResourceBundle localizedStrings) {
		this.localizedStrings = localizedStrings;
	}

}
