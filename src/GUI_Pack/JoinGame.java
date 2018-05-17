package GUI_Pack;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.AbstractListModel;
import javax.swing.JButton;

/**
 * Szobához való csatlakozáskor megjelenõ felület.
 * @author Hegyi Sámuel
 *
 */
public class JoinGame {
	public JTextField textField_2;
	public JFrame frm;
	public JPanel panel;

	/**
	 * Elindítja az alkalmazást.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JoinGame window = new JoinGame(null);
					window.frm.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Inicializálja a felület tartalmát.
	 */
	public JoinGame(GUI gui) {
		frm = new JFrame();
		frm.setTitle("Join Game");
		frm.setBounds(100, 100, 612, 460);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.getContentPane().setLayout(null);
		frm.setVisible(true);

		panel = new JPanel();
		frm.getContentPane().add(panel, "name_364531217708078");
		panel.setLayout(null);
		panel.setVisible(true);

		JButton btnRefresh = new JButton("Cancel");
		btnRefresh.setBounds(107, 240, 165, 50);
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gui.onEventCancel();
			}
		});
		frm.add(btnRefresh);

		JLabel lblIpAddress = new JLabel("IP address");
		lblIpAddress.setBounds(273, 100, 66, 16);
		frm.add(lblIpAddress);



		textField_2 = new JTextField();
		textField_2.setBounds(196, 120, 220, 22);
		// Listen for changes in the text
		textField_2.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				warn();
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				warn();
			}

			public void warn() {
				//gui.joinGame(textField_2.getText());
			}
		});
		frm.add(textField_2);
		textField_2.setColumns(10);

		/*JButton btnNewButton = new JButton("Search");
		btnNewButton.setBounds(100, 320, 165, 50);
		frm.add(btnNewButton);*/

		JButton btnJoinGame = new JButton("Join Game");
		btnJoinGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gui.onEventStartGame();
			}
		});
		btnJoinGame.setBounds(340, 240, 165, 50);
		frm.add(btnJoinGame);
	}
}
