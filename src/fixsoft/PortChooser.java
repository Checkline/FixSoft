package fixsoft;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PortChooser extends JDialog implements ItemListener {
	  /** A mapping from names to CommPortIdentifiers. */
	  @SuppressWarnings({ "unchecked" })
	  protected HashMap map = new HashMap();
	  /** The name of the choice the user made. */
	  protected String selectedPortName;
	  /** The CommPortIdentifier the user chose. */
	  protected CommPortIdentifier selectedPortIdentifier;
	  /** The JComboBox for serial ports */
	  protected JComboBox serialPortsChoice;
	  /** The SerialPort object */
	  protected SerialPort ttya;
	  /** To display the chosen */
	  protected JLabel choice;
	  /** Padding in the GUI */
	  protected final int PAD = 5;
	  protected FixSoft fixsoft;

	  /** This will be called from either of the JComboBoxen when the
	   * user selects any given item.
	   */
	  public void itemStateChanged(ItemEvent e) {
	    // Get the name
	    selectedPortName = (String)((JComboBox)e.getSource()).getSelectedItem();
	    // Get the given CommPortIdentifier
	    selectedPortIdentifier = (CommPortIdentifier)map.get(selectedPortName);
	    // Display the name.
	    choice.setText(selectedPortName);
	    fixsoft.updatePort(selectedPortName, selectedPortIdentifier);
	  }

	  /* The public "getter" to retrieve the chosen port by name. */
	  public String getSelectedName() {
	    return selectedPortName;
	  }

	  /* The public "getter" to retrieve the selection by CommPortIdentifier. */
	  public CommPortIdentifier getSelectedIdentifier() {
	    return selectedPortIdentifier;
	  }

	  /** Construct a PortChooser --make the GUI and populate the ComboBoxes.
	   */
	  public PortChooser(JFrame parent, FixSoft fixsoft) {
	    super(parent, "Port Chooser", true);
	    this.fixsoft = fixsoft;
	    setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 100, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 100);
	    makeGUI();
	    populate();
	    finishGUI();
	  }

	  /** Build the GUI.
	   */
	  protected void makeGUI() {
	    Container cp = getContentPane();

	    JPanel centerPanel = new JPanel();
	    cp.add(BorderLayout.CENTER, centerPanel);

	    centerPanel.setLayout(new GridLayout(0,2, PAD, PAD));

	    centerPanel.add(new JLabel("Serial Ports", JLabel.RIGHT));
	    serialPortsChoice = new JComboBox();
	    centerPanel.add(serialPortsChoice);
	    serialPortsChoice.setEnabled(false);

	    centerPanel.add(new JLabel("Your choice:", JLabel.RIGHT));
	    centerPanel.add(choice = new JLabel());

	    JButton okButton;
	    cp.add(BorderLayout.SOUTH, okButton = new JButton("OK"));
	    okButton.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        PortChooser.this.dispose();
	      }
	    });

	  }

	  /** Populate the ComboBoxes by asking the Java Communications API
	   * what ports it has.  Since the initial information comes from
	   * a Properties file, it may not exactly reflect your hardware.
	   */
	  @SuppressWarnings({ "unchecked" })
	protected void populate() {
	    // get list of ports available on this particular computer,
	    // by calling static method in CommPortIdentifier.
	    Enumeration pList = CommPortIdentifier.getPortIdentifiers();

	    // Process the list, putting serial and parallel into ComboBoxes
	    while (pList.hasMoreElements()) {
	      CommPortIdentifier cpi = (CommPortIdentifier)pList.nextElement();
	      // System.out.println("Port " + cpi.getName());
	      map.put(cpi.getName(), cpi);
	      if (cpi.getPortType() == CommPortIdentifier.PORT_SERIAL) {
	        serialPortsChoice.setEnabled(true);
	        serialPortsChoice.addItem(cpi.getName());
	      }
	    }
	    serialPortsChoice.setSelectedIndex(-1);
	  }

	  protected void finishGUI() {
	    serialPortsChoice.addItemListener(this);
	    pack();
	    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	  }
	}
