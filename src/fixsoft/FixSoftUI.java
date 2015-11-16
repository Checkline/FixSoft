package fixsoft;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class FixSoftUI extends JFrame {

	@SuppressWarnings("unused")
	private FixSoft fixsoft;
	private FixSoftUIListener listener;
	private JPanel mainPanel;	
	private JPanel buttonPanel;
	private JPanel bannerPanel;
	private JButton clearButton;
	private JButton portButton;
	private JButton excelButton;
	private JButton textButton;
	private JButton portSettingsButton;
	private JLabel banner;
	private TextArea messageArea;

	public FixSoftUI(FixSoft fixsoft) {

		super("FixSoft -- 0.6.0");
		this.fixsoft = fixsoft;
		//Creates a listener for the Frame -- all actions handled by FixSoftUIListener Class
		listener = new FixSoftUIListener(fixsoft);
		mainPanel = new JPanel();	
		buttonPanel = new JPanel();
		bannerPanel = new JPanel();
		clearButton = new JButton(fixsoft.getLocalizedStrings().getString("ClearScreen"));
		portButton = new JButton(fixsoft.getLocalizedStrings().getString("OpenPort"));
		portSettingsButton = new JButton(fixsoft.getLocalizedStrings().getString("PortSettings"));
		textButton = new JButton(fixsoft.getLocalizedStrings().getString("ExporttoTextFile"));
		excelButton = new JButton(fixsoft.getLocalizedStrings().getString("ExporttoExcel"));
		messageArea = new TextArea(1,38);
		banner = new JLabel();
		banner.setIcon(new ImageIcon(getClass().getResource("/images/banner.png")));
		
		getContentPane().setLayout(new BorderLayout());
		bannerPanel.setLayout(new BorderLayout(0,3));
		mainPanel.setLayout(new BorderLayout(0,3));
		buttonPanel.setLayout(new GridLayout(5, 1, 10, 5));
		messageArea.setEditable(false);
		
		portButton.addActionListener(listener);
		portSettingsButton.addActionListener(listener);
		excelButton.addActionListener(listener);
		textButton.addActionListener(listener);
		clearButton.addActionListener(listener);
		
		bannerPanel.add(banner);
		buttonPanel.add(portButton);
		buttonPanel.add(portSettingsButton);
		buttonPanel.add(excelButton);
		buttonPanel.add(textButton);
		buttonPanel.add(clearButton);
		mainPanel.add(bannerPanel, BorderLayout.PAGE_START);
		mainPanel.add(buttonPanel, BorderLayout.LINE_START);
		mainPanel.add(messageArea, BorderLayout.LINE_END);
		getContentPane().add(mainPanel);
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/menu2.gif")));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(screenSize.width / 2 - 212, screenSize.height / 2 - 150);
		setSize(440, 300);

	}

	/**
	 * @return the portButton
	 */
	public JButton getPortButton() {
		return portButton;
	}


	/**
	 * @return the messageArea
	 */
	public TextArea getMessageArea() {
		return messageArea;
	}

}
