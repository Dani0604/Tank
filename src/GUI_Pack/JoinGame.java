package GUI_Pack;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

/**
 * Szobához való csatlakozáskor megjelenõ felület.
 * @author Hegyi Sámuel
 *
 */
public class JoinGame {
	/**
	 * Csatlakozó játékos itt adja meg a nevét.
	 */
	public JTextField playerName;
	/**
	 * Az IP címet tartalmazza, amire csatlakozni szeretnénk.
	 */
	public JTextField IPAddress;
	public JFrame frm;

	/**
	 * Megnyitja a felületet.
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

		JLabel lblplayerName = new JLabel("Player name:");
		lblplayerName.setBounds(273, 60, 76, 16);
		frm.add(lblplayerName);
		
		playerName = new JTextField();
		playerName.setBounds(196, 80, 220, 22);
		frm.add(playerName);
		playerName.setColumns(10);
		
		JLabel lblIpAddress = new JLabel("IP address");
		lblIpAddress.setBounds(273, 140, 66, 16);
		frm.add(lblIpAddress);
		
		IPAddress = new JTextField();
		IPAddress.setBounds(196, 160, 220, 22);
		frm.add(IPAddress);
		IPAddress.setColumns(10);

		JButton btnJoinGame = new JButton("Join Game");
		btnJoinGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gui.setPlayerName(playerName.getText());
				gui.onEventStartGame();
			}
		});
		btnJoinGame.setBounds(340, 240, 165, 50);
		frm.add(btnJoinGame);
		
		JButton btnCancel= new JButton("Cancel");
		btnCancel.setBounds(107, 240, 165, 50);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gui.onEventCancel();
			}
		});
		frm.add(btnCancel);
	}
}