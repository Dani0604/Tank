package Network;

import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

import GUI_Pack.GUI;
import TankGame.GameState;
import TankGame.Player;

/**
 * Kliens t�pus� h�l�zati kommunik�ci�t megval�s�t� oszt�ly.
 * @author Horv�th Gy�z�
 *
 */
public class SerialClient extends Network {

	/**
	 * Ezen a socketen kereszt�l kommunik�l az oszt�ly.
	 */
	private Socket socket = null;
	/**
	 * Adatok k�ld�se itt t�rt�nik.
	 */
	private ObjectOutputStream out = null;
	/**
	 * Adatok ide �rkeznek.
	 */
	private ObjectInputStream in = null;
	private GUI gui;

	/**
	 * Itt t�rt�nik a t�rk�p, illetve a p�lyaelemek fogad�sa.
	 * @author Horv�th Gy�z�
	 *
	 */
	private class ReceiverThread implements Runnable {

		public void run() {
			GameState received;
			try {
				System.out.println("Tankok fogadasa.");
				while (true) {
					received = (GameState) in.readUnshared();
					gui.gameStateReceived(received);
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				System.err.println("Server disconnected!");
			} finally {
				disconnect();
			}

			try {
				System.out.println("Terkep fogadasa.");

				System.out.println("Map received.");
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				System.err.println("Server disconnected!");
			}
		}
	}

	/**
	 * Adott IP c�mre csatlakozik.
	 * @param ip IP c�m
	 */
	@Override
	public void connect(String ip) {
		disconnect();
		try {
			socket = new Socket(ip, 10007);

			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			out.flush();
			
			Thread rec = new Thread(new ReceiverThread());
			rec.start();
			
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection. ");
			JOptionPane.showMessageDialog(null, "Cannot connect to server!");
		}
	}

	public SerialClient(GUI gui){
		this.gui = gui;
	}

	/**
	 * J�t�kos k�ld�se a szervernek.
	 * @param _player Elk�ldend� j�t�kos.
	 */
	public void send(Player _player) {
		if (out == null)
			return;
		try {
			out.reset();
			out.writeUnshared(_player);
			out.flush();
		//	System.out.println("Sending player to server.");
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
			
		}
	}

	/**
	 * Lecsatlakoz�s a szerverr�l.
	 */
	@Override
	public void disconnect() {
		try {
			if (out != null)
				out.close();
			if (in != null)
				in.close();
			if (socket != null)
				socket.close();
		} catch (IOException ex) {
			System.err.println("Error while closing conn.");
		}
	}
}
