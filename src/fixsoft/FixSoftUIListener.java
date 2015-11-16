package fixsoft;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * FixSoftUIListener class --
 * 
 * This class listens for any actions on objects this listener is attached to, used for the buttons of the ui
 *
 * @author Matt Martucciello
 *
 */

public class FixSoftUIListener  implements ActionListener {

	FixSoft fixsoft;

	public FixSoftUIListener(FixSoft fixsoft) {
		this.fixsoft = fixsoft;
	}

	@Override
	/**
	 * actionPerformed --
	 * whenever an action is performed on an object that uses this class as a listener this method will execute
	 * needs to be reworked to somehow use an ID system instead of comparing strings
	 */
	public void actionPerformed(ActionEvent event) {
		String name = event.getActionCommand();
		if ( name.equals(fixsoft.getLocalizedStrings().getString("OpenPort")) || name.equals(fixsoft.getLocalizedStrings().getString("ClosePort")) )
			fixsoft.togglePort();
		else if ( name.equals(fixsoft.getLocalizedStrings().getString("ClearScreen")) )
			fixsoft.clearScreen();
		else if ( name.equals(fixsoft.getLocalizedStrings().getString("PortSettings")) ) 
			fixsoft.portSettings();
		else if ( name.equals(fixsoft.getLocalizedStrings().getString("ExporttoExcel")) )
			fixsoft.excel();
		else if ( name.equals(fixsoft.getLocalizedStrings().getString("ExporttoTextFile")) )
			fixsoft.textFile();
		
	}

}
