package GUI_Pack;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.ButtonGroup;

import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import GUI_Pack.GUI;

/**
 * Játékosokra várakozó felület.
 * @author Hegyi Sámuel
 *
 */
public class Lobby {

	public JFrame frm;
	private GUI gui;
	/**
	 * Játékosok listája
	 */
	public JList<String> list;
	/**
	 * A játékot indító gomb.
	 */
	public JButton btnStart;
	
	/**
	 * Megnyitja a felületet.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Lobby window = new Lobby(null);
					window.frm.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Lobby(GUI _gui){
		this.gui= _gui;
		frm = new JFrame();
		frm.setTitle("Lobby");
		frm.setBounds(100, 100, 612, 460);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.getContentPane().setLayout(null);
		frm.setVisible(false);
		
		//Várakozó játékosok listája
		JLabel lblPlayers = new JLabel("Players");
		lblPlayers.setBounds(25, 25, 56, 16);
		frm.getContentPane().add(lblPlayers);

		list = new JList<String>();
		list.setFont(new Font("Tahoma", Font.PLAIN, 17));
		list.setModel(new AbstractListModel<String>() {
			private static final long serialVersionUID = 1L;
			@Override
			public String getElementAt(int arg0) {
				return gui.players.get(arg0).settings.name;
			}
			@Override
			public int getSize() {
				// TODO Auto-generated method stub
				if (gui.players == null)
					return 0;
				else 
					return gui.players.size();
			}
		});
		list.setBounds(25, 50, 125, 150);
		frm.getContentPane().add(list);
		
		String ip = "IP";
        try {
            InetAddress ipAddr = InetAddress.getLocalHost();
            ip = ipAddr.getHostAddress();
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
		JLabel lblIP = new JLabel("Your IP: " + ip);
		lblIP.setBounds(25, 170, 180, 190);
		frm.getContentPane().add(lblIP);
		
		//Szín kiválasztása
		JLabel lblSelectColor = new JLabel("Select Color");
		lblSelectColor.setBounds(315, 25, 81, 16);
		frm.getContentPane().add(lblSelectColor);
		
		JPanel colorpanel = new JPanel();
		colorpanel.setBounds(396, 25, 25, 25);
		frm.getContentPane().add(colorpanel);

		JRadioButton rdbtnRed = new JRadioButton("Red");
		rdbtnRed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				colorpanel.setBackground(Color.RED);
				colorpanel.setVisible(true);
				gui.setPlayerColor(Color.RED);
			}
		});
		rdbtnRed.setBounds(321, 55, 127, 25);
		frm.getContentPane().add(rdbtnRed);

		JRadioButton rdbtnBlue = new JRadioButton("Blue");
		rdbtnBlue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				colorpanel.setBackground(Color.BLUE);
				colorpanel.setVisible(true);
				gui.setPlayerColor(Color.BLUE);
			}
		});
		rdbtnBlue.setBounds(321, 78, 127, 25);
		frm.getContentPane().add(rdbtnBlue);

		JRadioButton rdbtnGreen = new JRadioButton("Green");
		rdbtnGreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				colorpanel.setBackground(Color.GREEN);
				colorpanel.setVisible(true);
				gui.setPlayerColor(Color.GREEN);
			}
		});
		rdbtnGreen.setBounds(321, 100, 127, 25);
		frm.getContentPane().add(rdbtnGreen);

		JRadioButton rdbtnYellow = new JRadioButton("Yellow");
		rdbtnYellow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				colorpanel.setBackground(Color.YELLOW);
				colorpanel.setVisible(true);
				gui.setPlayerColor(Color.YELLOW);
			}
		});
		rdbtnYellow.setBounds(321, 123, 127, 25);
		frm.getContentPane().add(rdbtnYellow);

		JRadioButton rdbtnOrange = new JRadioButton("Orange");
		rdbtnOrange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				colorpanel.setBackground(Color.ORANGE);
				colorpanel.setVisible(true);
				gui.setPlayerColor(Color.ORANGE);
			}
		});
		rdbtnOrange.setBounds(321, 146, 127, 25);
		frm.getContentPane().add(rdbtnOrange);

		JRadioButton rdbtnPurple = new JRadioButton("Purple");
		rdbtnPurple.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				colorpanel.setBackground(Color.MAGENTA);
				colorpanel.setVisible(true);
				gui.setPlayerColor(Color.MAGENTA);
			}
		});
		rdbtnPurple.setBounds(321, 169, 127, 25);
		frm.getContentPane().add(rdbtnPurple);

		JRadioButton rdbtnBlack = new JRadioButton("Black");
		rdbtnBlack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				colorpanel.setBackground(Color.BLACK);
				colorpanel.setVisible(true);
				gui.setPlayerColor(Color.BLACK);
			}
		});
		rdbtnBlack.setBounds(321, 192, 127, 25);
		frm.getContentPane().add(rdbtnBlack);

		JRadioButton rdbtnWhite = new JRadioButton("White");
		rdbtnWhite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				colorpanel.setBackground(Color.WHITE);
				colorpanel.setVisible(true);
				gui.setPlayerColor(Color.WHITE);
			}
		});
		rdbtnWhite.setBounds(321, 215, 127, 25);
		frm.getContentPane().add(rdbtnWhite);

		ButtonGroup group=new ButtonGroup();
		group.add(rdbtnRed);
		group.add(rdbtnBlue);
		group.add(rdbtnGreen);
		group.add(rdbtnYellow);
		group.add(rdbtnOrange);
		group.add(rdbtnPurple);
		group.add(rdbtnBlack);
		group.add(rdbtnWhite);
		
		
		//Gombok
		JButton btnNewButton = new JButton("Main Menu");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gui.onEventCancel();
				}
			});
		btnNewButton.setBounds(25, 340, 165, 50);
		frm.getContentPane().add(btnNewButton);
		
		JButton readyButton = new JButton("Ready");
		readyButton.setBounds(205, 340, 165, 50);
		readyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gui.onEventJoinGame();
				}
			});
		frm.getContentPane().add(readyButton);
				
		btnStart = new JButton("Start");
		btnStart.setBounds(405, 340, 165, 50);
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gui.onEventStartGame();
				}
			});
		frm.getContentPane().add(btnStart);
		}
}