package TankGame;


import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import Network.SerialServer;
import TankGame.Element.Type;

/**
 * A játékmenet során számolja ki a tankok, lövedékek pozícióját a játékosoktól érkezõ vezérlési adatok alapján.
 * @author Szabó Dániel
 *
 */
public class GameControl {

	/**
	 * A játék állapota
	 */
	public StateMachine SM;
	/**
	 * A játszó játékosok listája.
	 */
	public CopyOnWriteArrayList<Player> players;
	/**
	 * A játék állapotát számítja adott idõközönként.
	 */
	private PeriodicControl pc;
	/**
	 * A mintavételi idõ - ekkora idõközönként számolja ki az osztály az objektumok pozícióját.
	 */
	private static final double T = 10;
	/**
	 * A játék vége után ennyi idõt kell várni a következõ játékig.
	 */
	private static final double GAME_END_WAIT_TIME = 3000;
	/**
	 * A pályán található objektumok listája.
	 */
	public CopyOnWriteArrayList<Element> elements;
	/**
	 * A pálya térképe.
	 */
	public Map map;
	/**
	 * Kizárólag a gamecontrolhoz tartozik szerver, hozzá érkeznek a játékosok attribútumai.
	 */
	public SerialServer server;
	/**
	 * A játék állapotát leíró változó
	 */
	public GameState gameState;
	
	/**
	 * Minimális idõ két powerup között.
	 */
	private final double POWERUPTIME = 3;
	/**
	 * Powerupok érkezésének gyakorisága.
	 */
	private double PowerUpTimer = POWERUPTIME + Math.random()*5;
	 
	/**
	 * Új játék indítása a korábbi befejezése után.
	 */
	private void newMatch(){
		map = new Map();
		elements = new CopyOnWriteArrayList<Element>();
		for (int i = 0; i < players.size(); i++) {
			Player p = players.get(i);
			p.tank = null;
			players.set(i,p);
		}
	}
	
	/**
	 * Szerversocket indítása.
	 */
	public void startServer(){
		if(server != null){
			server.disconnect();
		}
		server = new SerialServer(this);
 		server.connect("localhost");
	}
	
	/**
	 * Szerversocket leállítása.
	 */
	public void closeServer(){
		
		server.stopThreads();
		server.disconnect();
	}
	
	/**
	 * Megadja, hogy épp folyik-e a játék.
	 * @return isGame Folyamatban van-e a játék.
	 */
	public boolean currentStateIsGame(){
		return SM.currentState == State.GameHost;
	}
	
	/**
	 * A játéklogikát megvalósító szál.
	 * @author Szabó Dániel
	 *
	 */
	public class PeriodicControl extends Thread {
		public boolean stopThread = false;
		@Override
		public void run() {
			double prevTime = System.nanoTime();
			double currentTime;
			double waitTime = GAME_END_WAIT_TIME;
			while (!stopThread) {
				if (currentStateIsGame()){
				currentTime = System.nanoTime();
				double deltaT = (currentTime - prevTime)/1000000;
				prevTime = currentTime;	
				if (map == null)
					map = new Map();
				for (int i = 0; i < players.size(); i++) {
					Player p = players.get(i);
					if (p.tank == null){
						p.tank = new Tank(map);
						p.tank.player = p;
						elements.add(p.tank);
					}
					p.tank.color = p.settings.color;
					if (p.controls.turnLeft)
						p.tank.rotate(-Math.PI*1.2/ 1000 * deltaT);
					if (p.controls.turnRight)
						p.tank.rotate(Math.PI*1.2 / 1000 * deltaT);
					if (p.controls.shoot && !p.controls.shoot_old) {
						p.tank.nextBullet.shoot(elements);
					}
					p.controls.shoot_old = p.controls.shoot;
				}

				//Mozgatások, ütközések
				for (int i = 0; i < elements.size(); i++) {
					Element e1 = elements.get(i);
					e1.move(T);
					e1.wallCollision(map);
					for (int j = i+1; j < elements.size(); j++) {
						Element e2 = elements.get(j);
						e1.collisionDetection(e2);
						e2.collisionDetection(e1);	
						if(e2.deleteElement){
							e2.delete();
							elements.remove(j);
						}
					}
					if(e1.deleteElement){
						e1.delete();
						elements.remove(i);
					}
				}
				
				PowerUpTimer -= T/1000;		
				if (PowerUpTimer <= 0){
					elements.add(new PowerUp(map));
					PowerUpTimer = POWERUPTIME + Math.random()*5;
				}
				
				int tankNum = 0;
				for (int i = 0; i < elements.size(); i++) {
					if (elements.get(i).getType() == Type.TANK) tankNum++;
				}

				if (tankNum <= 1){
					waitTime -= deltaT;
				}
				else{
					waitTime = GAME_END_WAIT_TIME;
				} 
				
				if (waitTime <= 0){
					newMatch();
				}
				}
				gameState = new GameState();
				gameState.state = SM.currentState;
				gameState.elements = elements == null ? null : new CopyOnWriteArrayList<Element>(elements);
				gameState.map = map == null || map.lines == null ? null : new ArrayList<Rectangle>(map.lines);
				gameState.players = new CopyOnWriteArrayList<Player>(players);
				
				try {
					Thread.sleep((int)T);
				} 
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	GameControl(StateMachine sm) {
		SM = sm;
		players = new CopyOnWriteArrayList<Player>();
		gameState = new GameState();
		gameState.state = SM.currentState;
		startServer();
		pc = new PeriodicControl();
		pc.start();
	}
	
	/**
	 * Játék indítása.
	 */
	void startGame(){
		map = new Map();
		elements = new CopyOnWriteArrayList<Element>();
	}
	
	/**
	 * Játékos fogadása, majd feldolgozása.
	 * @param _player Fogadott játékos
	 */
	public void playerReceived(Player _player) {
		int i;
		for (i = 0; i < players.size(); i++) {
			Player p = players.get(i);
			if (p.settings.ID == _player.settings.ID){
				//System.out.println(_player.controls.moveForward);
				//System.out.println(i);
				p.controls.moveForward = _player.controls.moveForward;
				p.controls.moveBackward = _player.controls.moveBackward;
				p.controls.shoot = _player.controls.shoot;
				p.controls.turnLeft = _player.controls.turnLeft;
				p.controls.turnRight = _player.controls.turnRight;
				p.settings.color = _player.settings.color;
				players.set(i, p);
				return;
			}
		}
		//System.out.println('a');
		if (_player.settings.name.equals("Default"))
			_player.settings.name = "Player" + i;
		players.add(_player);
	}

	public void stopThreads() {
		try {
			pc.stopThread = true;
			pc.join();
			pc.stopThread = true;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
