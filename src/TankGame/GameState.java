package TankGame;

import java.awt.Rectangle;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A játék állapotát tartalmazó osztály.
 * @author Szabó Dániel
 *
 */
public class GameState implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * A játék állapota. Megadja, hogy hol vagyunk épp a menüben.
	 */
	public State state;
	/**
	 * A pályán található objektumok.
	 */
	public CopyOnWriteArrayList<Element> elements;
	/**
	 * A játékkal játszó játékosok.
	 */
	public CopyOnWriteArrayList<Player> players;
	/**
	 * A pálya térképe.
	 */
	public ArrayList<Rectangle> map;

	/**
	 * Állapot, objektumok, játékosok, térkép küldése output stream-en keresztül.
	 * @param stream Output Stream
	 * @throws IOException IO Kivétel.
	 */
	private void writeObject(java.io.ObjectOutputStream stream) throws IOException{
		stream.writeObject(state);
		stream.writeObject(elements);
		stream.writeObject(players);
		stream.writeObject(map);
	}

	/**
	 * Állapot, objektumok, játékosok, térkép fogadása input stream-en keresztül.
	 * @param stream Output Stream.
	 * @throws IOException IO kivétel.
	 * @throws ClassNotFoundException Ismeretlen osztály kivétel.
	 */
	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException{
		state = (State)stream.readObject();
		elements = (CopyOnWriteArrayList<Element>)stream.readObject();
		players = (CopyOnWriteArrayList<Player>) stream.readObject();
		map = (ArrayList<Rectangle>) stream.readObject();
	}	

}