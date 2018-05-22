package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import TankGame.GameControl;
import TankGame.GameState;
import TankGame.Player;

/**
 * Szerver típusú hálózati kommunikációt megvalósító osztály
 * @author Horváth Gyõzõ
 *
 */
public class SerialServer extends Network {

	/**
	 * Ezen a socketen keresztül kommunikál az osztály.
	 */
	private ServerSocket serverSocket = null;
	/**
	 * A kliensek socketjeit tartalmazó lista.
	 */
	private ArrayList<Socket> clientSocket = null;
	/**
	 * A kliensekhez küldött adatok ezekbõl a listaelemekbõl kerülnek kiküldésre.
	 */
	private ArrayList<ObjectOutputStream> out = null;
	/**
	 * Kliensektõl erkezõ adatok ezekbe a listaelemekbe érkeznek.
	 */
	private ArrayList<ObjectInputStream> in = null;
	/**
	 * Különbözõ kliensektõl különbözõ szálakon fogadunk, ebben a listában találhatók ezek a szálak.
	 */
	private ArrayList<ReceiverThread> rec = null;
	/**
	 * A játék állapotát küldi a klienseknek.
	 */
	public PeriodicControl pc;
	/**
	 * Kliensekre várakozik.
	 */
	private WaitForClientThread wc;
	/**
	 * A játék állapotát ismeri és számolja.
	 */
	GameControl gctrl;

	public SerialServer(GameControl c) {
		gctrl = c;
		pc = new PeriodicControl();
		pc.start();
		clientSocket = new ArrayList<Socket>();
		out = new ArrayList<ObjectOutputStream>();
		in = new ArrayList<ObjectInputStream>();
		rec = new ArrayList<ReceiverThread>();
	}

	/**
	 * A játék állapotát az összes kliensnek elküldõ szál.
	 * @author Horváth Gyõzõ
	 */
	public class PeriodicControl extends Thread {
		public boolean stopThread = false;
		@Override
		public void run() {
			while(!stopThread){
				send(gctrl.gameState);
				try {
					Thread.sleep(5);
				} 
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Kliensek csatlakozását váró osztály.
	 * @author Gyozo
	 *
	 */
	public class WaitForClientThread extends Thread{
		public boolean stopThread = false;
		public void run() {
			while(!stopThread){
				try {
					System.out.println("Waiting for Client");
					clientSocket.add(serverSocket.accept());
					System.out.println("Client connected.");

					out.add(new ObjectOutputStream(clientSocket.get(clientSocket.size()-1).getOutputStream()));
					in.add(new ObjectInputStream(clientSocket.get(clientSocket.size()-1).getInputStream()));
					out.get(clientSocket.size()-1).flush();
					rec.add(new ReceiverThread(clientSocket.size()-1));
					rec.get(clientSocket.size()-1).start();
				} catch (IOException e) {
					/*try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}*/
				}
			}
		}
	}

	/**
	 * Egy adott klienstõl fogadja az ahhoz tartozó játékost.
	 * @author Horváth Gyõzõ
	 *
	 */
	public class ReceiverThread extends Thread {
		public boolean stopThread = false;
		int num;
		public ReceiverThread(int n){
			num = n;
		}
		public void run() {
			try {
				while (!stopThread) {
					//System.out.println("Server received an object.");
					Player received = (Player) in.get(num).readUnshared();
					//System.out.println(received.controls.moveForward);
					gctrl.playerReceived(received);
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				//System.err.println("Client disconnected!");					
			} finally {
				disconnect();
			}
		}
	}

	/**
	 * Csatlakozik egy adott IP címre.
	 * @param ip IP cím.
	 */
	@Override
	public void connect(String ip) {
		disconnect();
		try {
			serverSocket = new ServerSocket(10007);
			wc = new WaitForClientThread();
			wc.start();
		} catch (IOException e) {
			System.err.println("Could not listen on port: 10007.");
		}
	}

	/**
	 * Lecsatlakozik a kliensekrõl.
	 */
	@Override
	public void disconnect() {
		try {
			if(clientSocket != null){
				for (int i = 0; i < clientSocket.size(); i++) { 
					if (clientSocket.get(i) != null)
						clientSocket.get(i).close();
				}
			}

			if (serverSocket != null)
				serverSocket.close();
		} catch (IOException ex) {
			Logger.getLogger(SerialServer.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	/**
	 * Játék aktuális állapotát küldi a klienseknek.
	 * @param gameState
	 */
	public void send(GameState gameState){
		if (out == null)
			return;
		//System.out.println("Sending elements to Client");
		try {
			for (int i = 0; i < out.size(); i++) { 
				out.get(i).reset();
				out.get(i).writeUnshared(gameState);
				out.get(i).flush();
			}

		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}

	public void stopThreads() {
		//kliensektõl fogadó szálak leállítása
		for(int i = 0; i < rec.size(); i++){
			try {
				rec.get(i).stopThread = true;
				rec.get(i).join();
				rec.get(i).stopThread = false;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			pc.stopThread = true;
			pc.join();
			pc.stopThread = false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			wc.stopThread = true;
			wc.join(1000);
			wc.stopThread = false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
