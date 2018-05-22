package TankGame;


import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import Network.SerialServer;
import TankGame.Element.Type;

/**
 * A j�t�kmenet sor�n sz�molja ki a tankok, l�ved�kek poz�ci�j�t a j�t�kosokt�l �rkez� vez�rl�si adatok alapj�n.
 * @author Szab� D�niel
 *
 */
public class GameControl {

	/**
	 * A j�t�k �llapota
	 */
	public StateMachine SM;
	/**
	 * A j�tsz� j�t�kosok list�ja.
	 */
	public CopyOnWriteArrayList<Player> players;
	/**
	 * A j�t�k �llapot�t sz�m�tja adott id�k�z�nk�nt.
	 */
	private PeriodicControl pc;
	/**
	 * A mintav�teli id� - ekkora id�k�z�nk�nt sz�molja ki az oszt�ly az objektumok poz�ci�j�t.
	 */
	private static final double T = 10;
	/**
	 * A j�t�k v�ge ut�n ennyi id�t kell v�rni a k�vetkez� j�t�kig.
	 */
	private static final double GAME_END_WAIT_TIME = 3000;
	/**
	 * A p�ly�n tal�lhat� objektumok list�ja.
	 */
	public CopyOnWriteArrayList<Element> elements;
	/**
	 * A p�lya t�rk�pe.
	 */
	public Map map;
	/**
	 * Kiz�r�lag a gamecontrolhoz tartozik szerver, hozz� �rkeznek a j�t�kosok attrib�tumai.
	 */
	public SerialServer server;
	/**
	 * A j�t�k �llapot�t le�r� v�ltoz�
	 */
	public GameState gameState;
	
	/**
	 * Minim�lis id� k�t powerup k�z�tt.
	 */
	private final double POWERUPTIME = 3;
	/**
	 * Powerupok �rkez�s�nek gyakoris�ga.
	 */
	private double PowerUpTimer = POWERUPTIME + Math.random()*5;
	 
	/**
	 * �j j�t�k ind�t�sa a kor�bbi befejez�se ut�n.
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
	 * Szerversocket ind�t�sa.
	 */
	public void startServer(){
		if(server != null){
			server.disconnect();
		}
		server = new SerialServer(this);
 		server.connect("localhost");
	}
	
	/**
	 * Szerversocket le�ll�t�sa.
	 */
	public void closeServer(){
		
		server.stopThreads();
		server.disconnect();
	}
	
	/**
	 * Megadja, hogy �pp folyik-e a j�t�k.
	 * @return isGame Folyamatban van-e a j�t�k.
	 */
	public boolean currentStateIsGame(){
		return SM.currentState == State.GameHost;
	}
	
	/**
	 * A j�t�klogik�t megval�s�t� sz�l.
	 * @author Szab� D�niel
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

				//Mozgat�sok, �tk�z�sek
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
	 * J�t�k ind�t�sa.
	 */
	void startGame(){
		map = new Map();
		elements = new CopyOnWriteArrayList<Element>();
	}
	
	/**
	 * J�t�kos fogad�sa, majd feldolgoz�sa.
	 * @param _player Fogadott j�t�kos
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
