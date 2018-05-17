/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI_Pack;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Network.SerialClient;
import TankGame.Element;
import TankGame.GameState;
import TankGame.Map;
import TankGame.Player;
import TankGame.State;
import TankGame.StateEventListener;
import TankGame.StateMachine;


/**
 *Felhaszn�l�i fel�letet implement�l� oszt�ly.
 * @author Hegyi S�muel
 */

public class GUI implements StateEventListener {
	private JFrame frmWorkOfTanks;
	/**
	 * �llapotg�p, mely megadja, hogy hol vagyunk a j�t�k men�j�ben.
	 */
	private StateMachine SM;
	/**
	 * Kirajzolja a j�t�k aktu�lis �llapot�t.
	 */
	private DrawPanel drawPanel;
	/**
	 * A j�t�kos, aki elind�totta a j�t�kot.
	 */
	private Player player;
	/**
	 * A p�ly�n tal�lhat� elemek �sszess�ge (tankok, l�ved�kek, powerupok).
	 */
	private CopyOnWriteArrayList<Element> elements;
	/**
	 * A j�tsz� j�t�kosok list�ja. Az attrib�tumaikat t�rolja (n�v, tank sz�ne).
	 */
	public 	CopyOnWriteArrayList<Player> players;
	/**
	 * A p�lya t�rk�pe.
	 */
	private Map map;
	/**
	 * A GUI-hoz tartoz� kliens socket.
	 */
	private SerialClient client;
	/**
	 * A szerver IP c�me, melyre a kliens csatlakozik.
	 */
	private String Serverip;
	/**
	 * Kirajzolja a p�ly�t adott id�k�z�nk�nt.
	 */
	private PeriodicDrawer pd;
	
	/**
	 * F�men� felhaszn�l�i fel�let.
	 */
	public MainMenu mainmenu;
	/**
	 * Host game felhaszn�l�i fel�let.
	 */
	public HostGame hostGame;
	/**
	 * Join game felhaszn�l�i fel�let.
	 */
	public JoinGame joinGame;
	/**
	 * Lobby felhaszn�l�i fel�let.
	 */
	public Lobby lobby;
	/**
	 * A j�t�k fut�sa sor�n haszn�lt fel�let.
	 */
	public JFrame gameFrm; 

	/**
	 * A kirajzol�st folyamatosan friss�t� sz�l.
	 * @author Hegyi S�muel
	 *
	 */
	public class PeriodicDrawer extends Thread {
		@Override
		public void run() {
			double old_time = 0;
			while (true) {
				drawPanel.repaint();
				try {
					Thread.sleep(10);
					double new_time;
					new_time = System.currentTimeMillis();
					double delta = new_time - old_time;
					//double fps = 1 / (delta / 1000);
					old_time = new_time;
					// System.out.println(fps);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * A GUI-hoz tartoz� j�t�kos szervernek val� k�ld�s�t megval�s�t� sz�l.
	 * @author Hegyi S�muel
	 *
	 */
	public class PeriodicPlayerUpdater extends Thread {
		@Override
		public void run() {
			client.connect(Serverip);
			while (true) {
				try {
					Thread.sleep(5);
					//System.out.println(player.controls.moveForward);
					send(player);
				}
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * A t�rk�p �s a j�t�kosok kirajzol�s�rt felel�s oszt�ly.
	 * @author Hegyi S�muel
	 *
	 */
	public class DrawPanel extends JPanel implements KeyListener {
		private static final long serialVersionUID = 1L;

		DrawPanel(){
			setDoubleBuffered(true);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (map != null)
				map.draw(g);
			if (elements != null){
				for (Element e : elements) {
					e.draw(g);
				}
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		//TODO �ltal�nos billenty�kombin�ci�k
		public void keyReleased(KeyEvent e) {
			int keyCode = e.getKeyCode();
			switch (keyCode) {
			case KeyEvent.VK_UP:
				player.controls.moveForward = false;
				break;
			case KeyEvent.VK_DOWN:
				// handle down
				player.controls.moveBackward = false;
				break;
			case KeyEvent.VK_LEFT:
				player.controls.turnLeft = false;
				break;
			case KeyEvent.VK_RIGHT:
				// handle right
				player.controls.turnRight = false;
				break;
			case KeyEvent.VK_SPACE:
				player.controls.shoot = false;
				break;
			}
		}

		@Override
		//TODO �ltal�nos billenty�kombin�ci�k
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			switch (keyCode) {
			case KeyEvent.VK_UP:
				player.controls.moveForward = true;
				break;
			case KeyEvent.VK_DOWN:
				// handle down
				player.controls.moveBackward = true;
				break;
			case KeyEvent.VK_LEFT:
				player.controls.turnLeft = true;
				break;
			case KeyEvent.VK_RIGHT:
				// handle right
				player.controls.turnRight = true;
				break;
			case KeyEvent.VK_SPACE:
				player.controls.shoot = true;
				break;
			}
		}
	}

	public GUI(StateMachine sm) {
		SM = sm;
		map = new Map();
		player = new Player(null);
		mainmenu = new MainMenu(this);
		hostGame = new HostGame(this);
		joinGame = new JoinGame(this);
		lobby = new Lobby(this);

		mainmenu.frm.setVisible(true);
		hostGame.frm.setVisible(false);
		joinGame.frm.setVisible(false);
		}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	/**
	 * �j �llapot be�rkez�sekor �ll�tja a GUI �llapotg�p�t.
	 * @param gamestate A j�t�k �j �llapota.
	 */
	public void gameStateReceived(GameState gamestate){
		if (gamestate.state == State.GameHost){
			if (SM.currentState == State.Ready)
				SM.onEventStartGame();
			if (gamestate.elements != null)
				this.elements = gamestate.elements;
			else 
				this.elements = new CopyOnWriteArrayList<Element>();
			this.map.lines = new ArrayList<Rectangle>(gamestate.map);
		}
		
		players = gamestate.players;
		DefaultListModel<String> model = new DefaultListModel<String>();
		if(players != null && players.size() != 0){
			for (int i = 0; i < gamestate.players.size(); i++) {
				Player p = gamestate.players.get(i);
				model.addElement(p.settings.name);
				if (p.settings.ID == player.settings.ID){
					player.settings.name = p.settings.name;
					player.tank = p.tank;
				}
			}
			lobby.list.setModel(model);
		}
	}

	/**
	 * L�trehozza a kliens socketet, �s elind�tja a j�t�kost a szervernek k�ld� sz�lat.
	 * @param ip Szerver IP c�me.
	 */
	public void startClient(String ip){
		if(client != null){
			client.disconnect();
		}

		client = new SerialClient(this);
		Serverip = ip;

		Thread networkthread = new PeriodicPlayerUpdater();
		networkthread.start();
	}

	/**
	 * Lecsatlakozik a szerverr�l, bez�rja a socketet.
	 */
	public void closeClient(){
		client.disconnect();
	}

	/**
	 * Elind�tja a j�t�kmenetet.
	 */
	public void startGame(){
		gameFrm = new JFrame();
		gameFrm.setSize(1024, 1024);
		gameFrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrm.setLayout(null);

		drawPanel = new DrawPanel();
		drawPanel.setBounds(0, 0, 800, 800);
		//drawPanel.setBorder(BorderFactory.createTitledBorder("Draw"));
		gameFrm.addKeyListener(drawPanel);
		gameFrm.add(drawPanel);

		pd = new PeriodicDrawer();
		pd.start();
	}
	
	/**
	 * A kliens itt k�ldi a szervernek a j�t�kost.
	 * @param _player GUI-hoz tartoz� j�t�kos
	 */
	public void send(Player _player){
		if(_player != null && client != null){
			client.send(_player);
		}
	}
	
	public void setFrmWorkOfTanks(boolean b) {
		this.frmWorkOfTanks.setVisible(b);
	}
	
	public void setPlayerColor(Color color) {
		player.settings.color = color;
	}
	
	public void setPlayerName(String _name){
		player.settings.name = _name;
	}

	/**
	 * Host Game gombra kattint�s lekezel�se
	 */
	@Override
	public State onEventHostGame() {
		this.SM.onEventHostGame();
		return this.SM.currentState;
	}

	/**
	 * Join Game gombra kattint�s lekezel�se
	 */
	@Override
	public State onEventJoinGame() {
		this.SM.onEventJoinGame();
		return this.SM.currentState;
	}

	/**
	 * Start Game gombra kattint�s lekezel�se
	 */
	@Override
	public State onEventStartGame() {
		this.SM.onEventStartGame();
		return this.SM.currentState;
	}

	/**
	 * Cancel gombra kattint�s lekezel�se
	 */
	@Override
	public State onEventCancel() {
		this.SM.onEventCancel();
		return this.SM.currentState;
	}

	/**
	 * Exit gombra kattint�s lekezel�se
	 */
	@Override
	public State onEventExit() {
		this.SM.onEventExit();
		return this.SM.currentState;
	}
}
