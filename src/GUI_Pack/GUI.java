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
	StateMachine SM;
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
	 * K�ldi a j�t�kost a szervernek adott id�k�z�nk�nt.
	 */
	private PeriodicPlayerUpdater ppu;
	
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
	 * A GUI-hoz tartoz� j�t�kos szervernek val� k�ld�s�t megval�s�t� sz�l.
	 * @author Hegyi S�muel
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
		//TODO �ltal�nos billenty�kombin�ci�k
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			System.out.println("lenyomott bill: " + keyCode);
			System.out.println("V�rt bill: " + player.controls.turnLeftBtnKey);
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

		ppu = new PeriodicPlayerUpdater();
		ppu.start();
	}

	/**
	 * Lecsatlakozik a szerverr�l, bez�rja a socketet.
	 */
	public void closeClient(){
		client.stopThreads();
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

	/**
	 * Be�ll�tja az el�re mozg�s billenty�j�t.
	 * @param keyCode El�re mozg�s billenty�j�nek k�dja
	 */
	public void setForwardBtnKey(int _keyCode) {
		player.controls.moveForwardBtnKey = _keyCode;
	}

	/**
	 * Be�ll�tja a h�tra mozg�s billenty�j�t.
	 * @param keyCode H�tra mozg�s billenty�j�nek k�dja
	 */
	public void setBwdBtnKey(int _keyCode) {
		player.controls.moveBackwardBtnKey = _keyCode;
	}

	/**
	 * Be�ll�tja a jobbra fordul�s billenty�j�t.
	 * @param keyCode Jobbra fordul�s billenty�j�nek k�dja
	 */
	public void setRightBtnKey(int _keyCode) {
		player.controls.turnRightBtnKey = _keyCode;
	}
	
	/**
	 * Be�ll�tja a balra fordul�s billenty�j�t.
	 * @param keyCode Balra fordul�s billenty�j�nek k�dja
	 */
	public void setLeftBtnKey(int _keyCode) {
		player.controls.turnLeftBtnKey = _keyCode;
	}
	
	/**
	 * Be�ll�tja a l�v�s billenty�j�t.
	 * @param keyCode L�v�s billenty�j�nek k�dja
	 */
	public void setShootBtnKey(int _keyCode) {
		player.controls.shootBtnKey = _keyCode;
	}

	/**
	 * Le�ll�tja a GUI sz�lait
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
