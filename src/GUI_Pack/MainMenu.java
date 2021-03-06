package GUI_Pack;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * F�men� fel�let.
 * @author Hegyi S�muel
 *
 */
public class MainMenu {
	private GUI gui;
	public JFrame frm;
	public JPanel panel;
	
	public MainMenu(GUI _gui){
		this.gui= _gui;
		frm = new JFrame();
		frm.setTitle("Work of Tanks");
		frm.setBounds(100, 100, 612, 460);
		frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frm.getContentPane().setLayout(new CardLayout(0, 0));
		frm.setVisible(true);
		
		panel = new JPanel();
		frm.getContentPane().add(panel, "name_366326468076502");
		panel.setLayout(null);
		panel.setVisible(true);
		
		JButton btnHostGame = new JButton("Host Game");
		btnHostGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gui.onEventHostGame();	
			}
		});
		btnHostGame.setBounds(200, 60, 165, 50);
		panel.add(btnHostGame);
		
		JButton btnJoinGame = new JButton("Join Game");
		btnJoinGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gui.onEventJoinGame();
			}
		});
		btnJoinGame.setBounds(200, 165, 165, 50);
		panel.add(btnJoinGame);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(200, 265, 165, 50);
		panel.add(btnExit);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 61, 26);
		panel.add(menuBar);
		
		JMenu mnNewMenu = new JMenu("Settings");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmKeySettings = new JMenuItem("Control Settings");
		mntmKeySettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KeySettings ks=new KeySettings(gui);
				ks.NewScreen();
			}
		});
		mnNewMenu.add(mntmKeySettings);
	}
}
