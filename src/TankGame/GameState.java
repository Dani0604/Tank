package TankGame;

import java.awt.Rectangle;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A j�t�k �llapot�t tartalmaz� oszt�ly.
 * @author Szab� D�niel
 *
 */
public class GameState implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * A j�t�k �llapota. Megadja, hogy hol vagyunk �pp a men�ben.
	 */
	public State state;
	/**
	 * A p�ly�n tal�lhat� objektumok.
	 */
	public CopyOnWriteArrayList<Element> elements;
	/**
	 * A j�t�kkal j�tsz� j�t�kosok.
	 */
	public CopyOnWriteArrayList<Player> players;
	/**
	 * A p�lya t�rk�pe.
	 */
	public ArrayList<Rectangle> map;

	/**
	 * �llapot, objektumok, j�t�kosok, t�rk�p k�ld�se output stream-en kereszt�l.
	 * @param stream Output Stream
	 * @throws IOException IO Kiv�tel.
	 */
	private void writeObject(java.io.ObjectOutputStream stream) throws IOException{
		stream.writeObject(state);
		stream.writeObject(elements);
		stream.writeObject(players);
		stream.writeObject(map);
	}

	/**
	 * �llapot, objektumok, j�t�kosok, t�rk�p fogad�sa input stream-en kereszt�l.
	 * @param stream Output Stream.
	 * @throws IOException IO kiv�tel.
	 * @throws ClassNotFoundException Ismeretlen oszt�ly kiv�tel.
	 */
	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException{
		state = (State)stream.readObject();
		elements = (CopyOnWriteArrayList<Element>)stream.readObject();
		players = (CopyOnWriteArrayList<Player>) stream.readObject();
		map = (ArrayList<Rectangle>) stream.readObject();
	}	

}