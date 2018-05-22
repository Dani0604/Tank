package GUI_Pack;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Az irányítás beállítását végzõ felület.
 * @author Hegyi Sámuel
 *
 */
public class KeySettings extends JDialog {

	private static final long serialVersionUID = 1L;
	static GUI gui; //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	private final JPanel contentPanel = new JPanel();
	private JTextField fwdTextField;
	private JTextField bwdTextField;
	private JTextField rightTextField;
	private JTextField leftTextField;
	private JTextField shootTextField;
	private JFrame frame;

	/**
	 * Elindítja az alkalmazást.
	 */
	public static void main(String[] args) {
		try {
			System.out.println("Itt");
			KeySettings dialog = new KeySettings(gui);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public KeySettings(GUI _gui) {
		gui = _gui;
		frame=new JFrame();
		frame.setTitle("Key Settings");
		frame.setBounds(100, 100, 320, 320);
		frame.getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblPleaseEnterThe = new JLabel("Please enter the suitable boardkey");
			lblPleaseEnterThe.setBounds(12, 13, 214, 25);
			contentPanel.add(lblPleaseEnterThe);
		}
		{
			JLabel lblUp = new JLabel("Up:");
			lblUp.setBounds(12, 51, 25, 25);
			contentPanel.add(lblUp);
		}
		{
			JLabel lblDown = new JLabel("Down:");
			lblDown.setBounds(12, 90, 37, 25);
			contentPanel.add(lblDown);
		}
		{
			JLabel lblRight = new JLabel("Right:");
			lblRight.setBounds(12, 136, 37, 25);
			contentPanel.add(lblRight);
		}
		{
			JLabel lblLeft = new JLabel("Left:");
			lblLeft.setBounds(12, 174, 37, 25);
			contentPanel.add(lblLeft);
		}
		{
			JLabel lblShoot = new JLabel("Shoot:");
			lblShoot.setBounds(12, 212, 45, 26);
			contentPanel.add(lblShoot);
		}
		{
			fwdTextField = new JTextField();
			fwdTextField.setBounds(36, 51, 25, 22);
			contentPanel.add(fwdTextField);
			fwdTextField.setColumns(10);
		}
		{
			bwdTextField = new JTextField();
			bwdTextField.setBounds(52, 91, 25, 22);
			contentPanel.add(bwdTextField);
			bwdTextField.setColumns(10);
		}
		{
			rightTextField = new JTextField();
			rightTextField.setBounds(49, 137, 25, 22);
			contentPanel.add(rightTextField);
			rightTextField.setColumns(10);
		}
		{
			leftTextField = new JTextField();
			leftTextField.setBounds(42, 175, 25, 22);
			contentPanel.add(leftTextField);
			leftTextField.setColumns(10);
		}
		{
			shootTextField = new JTextField();
			shootTextField.setBounds(52, 214, 25, 22);
			contentPanel.add(shootTextField);
			shootTextField.setColumns(10);
		}
		
		JButton btnNewButton = new JButton("Ok");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//elõre mozgás gomb beállítása
				String strFw= fwdTextField.getText();
				if(strFw.length() > 0){
					int code = Character.codePointAt(strFw, 0);
					code = ascii2keycode(code);
					System.out.println(code);
					gui.setForwardBtnKey(code);
				}
				else{
					//JOptionPane.showMessageDialog(null, "Wrong input! Please enter only one key!");
				}
				//hátra mozgás gomb beállítása
				String strBw=bwdTextField.getText();
				if(strBw.length() > 0){
					int code = Character.codePointAt(strBw, 0);
					code = ascii2keycode(code);
					System.out.println(code);
					gui.setBwdBtnKey(code);
				}
				else{
					//JOptionPane.showMessageDialog(null, "Wrong input! Please enter only one key!");
				}
				//jobbra fordulás gomb beállítása
				String strRg=rightTextField.getText();
				if(strRg.length() > 0){
					int code = Character.codePointAt(strRg, 0);
					code = ascii2keycode(code);
					System.out.println(code);
					gui.setRightBtnKey(code);
				}
				else{
					//JOptionPane.showMessageDialog(null, "Wrong input! Please enter only one key!");
				}
				//balra fordulás gomb beállítása
				String strLf=leftTextField.getText();
				if(strLf.length() > 0){
					int code = Character.codePointAt(strLf, 0);
					code = ascii2keycode(code);
					System.out.println(code);
					gui.setLeftBtnKey(code);
				}
				else{
					//JOptionPane.showMessageDialog(null, "Wrong input! Please enter only one key!");
				}
				String strSh=shootTextField.getText();
				if(strSh.length() > 0){
					int code = Character.codePointAt(strSh, 0);
					code = ascii2keycode(code);
					System.out.println(code);
					gui.setShootBtnKey(code);
				}
				else{
					//JOptionPane.showMessageDialog(null, "Wrong input! Please enter only one key!");
				}
				
				
				if(strFw.length()+strBw.length()+strRg.length()+strLf.length()+strSh.length() > 0)
					frame.dispose();
			}

			private int ascii2keycode(int _code) {
				int ret = -1;
				if(_code == 97){ //a
					ret = 65;
				}
				else if(_code == 115){ //s
					ret = 83;
				}
				else if(_code == 119){ //w
					ret = 87;
				}
				else if(_code == 100){ //d
					ret = 68;
				}
				else if(_code == 103){ //g
					ret = 71;
				}
				return ret;
			}
		});
		
		
		btnNewButton.setBounds(127, 205, 100, 40);
		contentPanel.add(btnNewButton);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	

	public void NewScreen() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KeySettings window = new KeySettings(gui);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}