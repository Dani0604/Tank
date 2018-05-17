package GUI_Pack;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JButton;

/**
 * Játék szoba létrehozásakor megjelenõ felület.
 * @author Hegyi Sámuel
 *
 */
public class HostGame {

	public JFrame frm;

	private JTextField playerNameTextField;
	private JTextField gameNameTextField;
	private JTextField maxPlayerTextField;
	private String PlayerName="";
	private String GameName="";
	private String max_p="";
	private int MaxPlayer=1;
	private boolean IsPowerUp=false;
	public JPanel panel;
	public GUI gui;
	
	/**
	 * Elindítja az alkalmazást.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HostGame window = new HostGame(null);
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
	public HostGame(GUI gui) {
		this.gui = gui;
		frm = new JFrame();
		frm.setTitle("Host Game");
		frm.setBounds(100, 100, 612, 460);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.getContentPane().setLayout(null);
		frm.setVisible(true);

		panel = new JPanel();
		frm.getContentPane().add(panel, "name_364527376487148");
		panel.setLayout(null);
		panel.setVisible(true);
		
		JLabel name = new JLabel("Game Name:");
		name.setBounds(200, 70, 76, 26);
		panel.add(name);
		
		JLabel gameName = new JLabel("Game Name:");
		gameName.setBounds(200, 70, 76, 26);
		panel.add(gameName);
		
		JLabel playerName = new JLabel("Player Name:");
		playerName.setBounds(200, 130, 122, 26);
		panel.add(playerName);
		
		JLabel maxPlayer = new JLabel("Maximum Number of Players:");
		maxPlayer.setBounds(200, 190, 168, 26);
		panel.add(maxPlayer);
		
		JLabel power = new JLabel("Power Up:");
		power.setBounds(200, 250, 60, 16);
		panel.add(power);
		
		JCheckBox checkBox = new JCheckBox("");
		checkBox.setBounds(258, 247, 25, 25);
		panel.add(checkBox);
		
		gameNameTextField = new JTextField();
		gameNameTextField.setBounds(277, 72, 127, 22);
		panel.add(gameNameTextField);
		gameNameTextField.setColumns(10);
		
		maxPlayerTextField = new JTextField();
		maxPlayerTextField.setBounds(370, 192, 35, 22);
		panel.add(maxPlayerTextField);
		maxPlayerTextField.setColumns(10);
		
		JButton btnLobby = new JButton("Lobby");
		btnLobby.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int k=0;
				if(gameNameTextField.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter a game name!");
				}
				else {
					GameName=gameNameTextField.getText();
					k=k+1;
				}
				if(playerNameTextField.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter a player name!");
				}
				else {
					PlayerName=playerNameTextField.getText();
					gui.setPlayerName(PlayerName);
					k=k+1;
				}
				max_p=maxPlayerTextField.getText();
				if(isInteger(max_p)) {
					int mp=Integer.parseInt(max_p);
					if(mp<1) {
						JOptionPane.showMessageDialog(null, "Please enter a positive number to Maximum Number of Players option!");
					}
					else {
						MaxPlayer=mp;
						k=k+1;
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "Please enter a number to Maximum Number of Players option!");
				}
				IsPowerUp=checkBox.isSelected();
				System.out.println(GameName);
				System.out.println(MaxPlayer);
				System.out.println(IsPowerUp);
				if(k==3) {
					gui.onEventStartGame();
				}
			}
		});
		btnLobby.setBounds(200, 296, 168, 50);
		//panel.add(btnLobby);
		
		JLabel lblGameName = new JLabel("Game Name:");
		lblGameName.setBounds(150, 70, 76, 26);
		frm.getContentPane().add(lblGameName);
		
		JLabel lblPlayerName = new JLabel("Player Name:");
		lblPlayerName.setBounds(150, 125, 122, 26);
		frm.getContentPane().add(lblPlayerName);
		
		JLabel lblMaximumNumberOf = new JLabel("Maximum number of Players:");
		lblMaximumNumberOf.setBounds(150, 180, 168, 26);
		frm.getContentPane().add(lblMaximumNumberOf);
		
		JLabel lblPowerUp = new JLabel("Power Up:");
		lblPowerUp.setBounds(150, 235, 60, 16);
		frm.getContentPane().add(lblPowerUp);
		
		JCheckBox isPowerUp = new JCheckBox("");
		isPowerUp.setBounds(217, 231, 25, 25);
		frm.getContentPane().add(isPowerUp);
		
		gameNameTextField = new JTextField();
		gameNameTextField.setBounds(228, 72, 127, 22);
		frm.getContentPane().add(gameNameTextField);
		gameNameTextField.setColumns(10);
		
		playerNameTextField = new JTextField();
		playerNameTextField.setBounds(228, 132, 127, 22);
		frm.getContentPane().add(playerNameTextField);
		playerNameTextField.setColumns(10);
		
		maxPlayerTextField = new JTextField();
		maxPlayerTextField.setBounds(320, 182, 35, 22);
		frm.getContentPane().add(maxPlayerTextField);
		maxPlayerTextField.setColumns(10);
		
		
		frm.getContentPane().add(btnLobby);
	}
	
	/**
	 * Megadja, hogy a kapott sztring számot tartalmaz-e.
	 * @param input String
	 * @return
	 */
	public boolean isInteger(String input)
	{
	   try
	   {
	      Integer.parseInt(input);
	      return true;
	   }
	   catch(Exception e)
	   {
	      return false;
	   }
	}
	
}
