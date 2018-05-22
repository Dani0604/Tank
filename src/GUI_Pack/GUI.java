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
 *Felhasználói felületet implementáló osztály.
 * @author Hegyi Sámuel
 */

public class GUI implements StateEventListener {
	private JFrame frmWorkOfTanks;
	/**
	 * Állapotgép, mely megadja, hogy hol vagyunk a játék menüjében.
	 */
	StateMachine SM;
	/**
	 * Kirajzolja a játék aktuális állapotát.
	 */
	private DrawPanel drawPanel;
	/**
	 * A játékos, aki elindította a játékot.
	 */
	private Player player;
	/**
	 * A pályán található elemek összessége (tankok, lövedékek, powerupok).
	 */
	private CopyOnWriteArrayList<Element> elements;
	/**
	 * A játszó játékosok listája. Az attribútumaikat tárolja (név, tank színe).
	 */
	public 	CopyOnWriteArrayList<Player> players;
	/**
	 * A pálya térképe.
	 */
	private Map map;
	/**
	 * A GUI-hoz tartozó kliens socket.
	 */
	private SerialClient client;
	/**
	 * A szerver IP címe, melyre a kliens csatlakozik.
	 */
	private String Serverip;
	/**
	 * Kirajzolja a pályát adott idõközönként.
	 */
	private PeriodicDrawer pd;
	/**
	 * Küldi a játékost a szervernek adott idõközönként.
	 */
	private PeriodicPlayerUpdater ppu;
	
	/**
	 * Fõmenü felhasználói felület.
	 */
	public MainMenu mainmenu;
	/**
	 * Host game felhasználói felület.
	 */
	public HostGame hostGame;
	/**
	 * Join game felhasználói felület.
	 */
	public JoinGame joinGame;
	/**
	 * Lobby felhasználói felület.
	 */
	public Lobby lobby;
	/**
	 * A játék futása során használt felület.
	 */
	public JFrame gameFrm;
	
	/**
	 * A kirajzolást folyamatosan frissítõ szál.
	 * @author Hegyi Sámuel
	 *
	 */
	public class PeriodicDrawer extends Thread {
		public boolean stopThread = false;

		public void run() {
			while (!stopThread) {
				drawPanel.repaint();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * A GUI-hoz tartozó játékos szervernek való küldését megvalósító szál.
	 * @author Hegyi Sámuel
	 *
	 */
	public class PeriodicPlayerUpdater extends Thread {
		public boolean stopThread = false;
		@Override
		public void run() {
			client.connect(Serverip);
			while (!stopThread) {
				try {
					Thread.sleep(10);
					send(player);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * A térkép és a játékosok kirajzolásért felelõs osztály.
	 * @author Hegyi Sámuel
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
		//TODO általános billentyûkombinációk
		public void keyReleased(KeyEvent e) {
			int keyCode = e.getKeyCode();
			if(keyCode == player.controls.moveForwardBtnKey){			
				player.controls.moveForward = false;
				}
			else if(keyCode == player.controls.moveBackwardBtnKey){
				player.controls.moveBackward = false;
				}
			else if(keyCode == player.controls.turnLeftBtnKey){
				player.controls.turnLeft = false;
				}
			else if(keyCode == player.controls.turnRightBtnKey){
				player.controls.turnRight = false;
				}
			else if(keyCode == player.controls.shootBtnKey){
				player.controls.shoot = false;
				}
		}

		@Override
		//TODO általános billentyûkombinációk
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			System.out.println("lenyomott bill: " + keyCode);
			System.out.println("Várt bill: " + player.controls.turnLeftBtnKey);
			if(keyCode == player.controls.moveForwardBtnKey){			
				player.controls.moveForward = true;
				}
			else if(keyCode == player.controls.moveBackwardBtnKey){
				player.controls.moveBackward = true;
				}
			else if(keyCode == player.controls.turnLeftBtnKey){
				player.controls.turnLeft = true;
				}
			else if(keyCode == player.controls.turnRightBtnKey){
				player.controls.turnRight = true;
				}
			else if(keyCode == player.controls.shootBtnKey){
				player.controls.shoot = true;
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
	 * Új állapot beérkezésekor állítja a GUI állapotgépét.
	 * @param gamestate A játék új állapota.
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
	 * Létrehozza a kliens socketet, és elindítja a játékost a szervernek küldõ szálat.
	 * @param ip Szerver IP címe.
	 */
	public void startClient(String ip){
		if(client != null){
			client.disconnect();
		}
		
		client = new SerialClient(this);
		Serverip = ip;

		ppu = new PeriodicPlayerUpdater();
		ppu.start();
	}

	/**
	 * Lecsatlakozik a szerverrõl, bezárja a socketet.
	 */
	public void closeClient(){
		client.stopThreads();
		client.disconnect();
	}

	/**
	 * Elindítja a játékmenetet.
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
	 * A kliens itt küldi a szervernek a játékost.
	 * @param _player GUI-hoz tartozó játékos
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
	 * Host Game gombra kattintás lekezelése
	 */
	@Override
	public State onEventHostGame() {
		this.SM.onEventHostGame();
		return this.SM.currentState;
	}

	/**
	 * Join Game gombra kattintás lekezelése
	 */
	@Override
	public State onEventJoinGame() {
		this.SM.onEventJoinGame();
		return this.SM.currentState;
	}

	/**
	 * Start Game gombra kattintás lekezelése
	 */
	@Override
	public State onEventStartGame() {
		this.SM.onEventStartGame();
		return this.SM.currentState;
	}

	/**
	 * Cancel gombra kattintás lekezelése
	 */
	@Override
	public State onEventCancel() {
		this.SM.onEventCancel();
		return this.SM.currentState;
	}

	/**
	 * Exit gombra kattintás lekezelése
	 */
	@Override
	public State onEventExit() {
		this.SM.onEventExit();
		return this.SM.currentState;
	}

	/**
	 * Beállítja az elõre mozgás billentyûjét.
	 * @param keyCode Elõre mozgás billentyûjének kódja
	 */
	public void setForwardBtnKey(int _keyCode) {
		player.controls.moveForwardBtnKey = _keyCode;
	}

	/**
	 * Beállítja a hátra mozgás billentyûjét.
	 * @param keyCode Hátra mozgás billentyûjének kódja
	 */
	public void setBwdBtnKey(int _keyCode) {
		player.controls.moveBackwardBtnKey = _keyCode;
	}

	/**
	 * Beállítja a jobbra fordulás billentyûjét.
	 * @param keyCode Jobbra fordulás billentyûjének kódja
	 */
	public void setRightBtnKey(int _keyCode) {
		player.controls.turnRightBtnKey = _keyCode;
	}
	
	/**
	 * Beállítja a balra fordulás billentyûjét.
	 * @param keyCode Balra fordulás billentyûjének kódja
	 */
	public void setLeftBtnKey(int _keyCode) {
		player.controls.turnLeftBtnKey = _keyCode;
	}
	
	/**
	 * Beállítja a lövés billentyûjét.
	 * @param keyCode Lövés billentyûjének kódja
	 */
	public void setShootBtnKey(int _keyCode) {
		player.controls.shootBtnKey = _keyCode;
	}

	/**
	 * Leállítja a GUI szálait
	 */
	public void stopThreads() {
		if(pd != null){		
			try {
				pd.stopThread = true;
				pd.join();
				pd.stopThread = false;
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(ppu != null){
			try {
				ppu.stopThread = true;
				ppu.join();
				ppu.stopThread = false;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
