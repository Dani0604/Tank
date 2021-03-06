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
 * Szerver t�pus� h�l�zati kommunik�ci�t megval�s�t� oszt�ly
 * @author Horv�th Gy�z�
 *
 */
public class SerialServer extends Network {

	/**
	 * Ezen a socketen kereszt�l kommunik�l az oszt�ly.
	 */
	private ServerSocket serverSocket = null;
	/**
	 * A kliensek socketjeit tartalmaz� lista.
	 */
	private ArrayList<Socket> clientSocket = null;
	/**
	 * A kliensekhez k�ld�tt adatok ezekb�l a listaelemekb�l ker�lnek kik�ld�sre.
	 */
	private ArrayList<ObjectOutputStream> out = null;
	/**
	 * Kliensekt�l erkez� adatok ezekbe a listaelemekbe �rkeznek.
	 */
	private ArrayList<ObjectInputStream> in = null;
	/**
	 * K�l�nb�z� kliensekt�l k�l�nb�z� sz�lakon fogadunk, ebben a list�ban tal�lhat�k ezek a sz�lak.
	 */
	private ArrayList<ReceiverThread> rec = null;
	/**
	 * A j�t�k �llapot�t k�ldi a klienseknek.
	 */
	public PeriodicControl pc;
	/**
	 * Kliensekre v�rakozik.
	 */
	private WaitForClientThread wc;
	/**
	 * A j�t�k �llapot�t ismeri �s sz�molja.
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
	 * A j�t�k �llapot�t az �sszes kliensnek elk�ld� sz�l.
	 * @author Horv�th Gy�z�
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
	 * Kliensek csatlakoz�s�t v�r� oszt�ly.
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
	 * Egy adott klienst�l fogadja az ahhoz tartoz� j�t�kost.
	 * @author Horv�th Gy�z�
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
	 * Csatlakozik egy adott IP c�mre.
	 * @param ip IP c�m.
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
	 * Lecsatlakozik a kliensekr�l.
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
	 * J�t�k aktu�lis �llapot�t k�ldi a klienseknek.
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
		//kliensekt�l fogad� sz�lak le�ll�t�sa
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
