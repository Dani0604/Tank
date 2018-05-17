package TankGame;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.Serializable;
import java.awt.geom.Area;

/**
 * A p�ly�n tal�lhat� objektumokat �ltal�nosan le�r� absztrakt oszt�ly.
 * @author Szab� D�niel
 *
 */
abstract public class Element implements Serializable{
	private static final long serialVersionUID = 4617701502825832245L;
	/**
	 * Az objektum poz�ci�ja.
	 */
	protected Point2D position; // kirajzol�s pozici�ja
	protected Point2D prevPos;
	/**
	 * Az objektum orient�ci�ja.
	 */
	protected double orientation; // orient�ci� megad�sa radi�nban
	/**
	 * Az objektum sebess�ge.
	 */
	protected double velocity; // elem mozg�s�nak sebess�ge
	/**
	 * Az objektum ter�lete.
	 */
	protected Area area;
	
	/**
	 * Ki�rja az objektum poz�ci�j�t �s orient�ci�j�t az output stream-re.
	 * @param stream Object Output Stream.
	 * @throws IOException IO kiv�tel.
	 */
	private void writeObject(java.io.ObjectOutputStream stream) throws IOException{
		stream.writeObject(position);
		stream.writeObject(orientation);		
	}
	
	/**
	 * Fogadja az input stream-r�l az �rkez� objektum poz�ci�kat �s orient�ci�kat.
	 * @param stream Output Stream.
	 * @throws IOException IO kiv�tel.
	 * @throws ClassNotFoundException Ismeretlen oszt�ly kiv�tel.
	 */
	private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException{
		position = (Point2D) stream.readObject();
		orientation = (double)stream.readObject();		
	}	
	
	
	//protected Semaphore s;
	protected enum Type {
		WALL,
		TANK,
		BULLET,
		POWERUP
	}
	public boolean deleteElement;
	
	/**
	 * A t�rk�p, amin az objektumok tal�lhat�k.
	 */
	protected Map map;

	/**
	 * Az objektumok kirajzol�s��rt felel�s f�ggv�ny.
	 * @param g Grafika
	 */
	abstract public void draw(Graphics g);

	/**
	 * Az objektumok elmozdul�s�t sz�m�tja ki.
	 * @param T Mintav�teli id�.s
	 */
	abstract public void move(double T);
	
	/**
	 * Az adott objektum t�pus�t adja meg.
	 * @return Objektum t�pus.
	 */
	abstract protected Type getType();
	
	/**
	 * Objektum t�rl�se.
	 */
	abstract public void delete();
	
	/**
	 * A fallal val� �tk�z�st vizsg�lja.
	 * @param map P�lya
	 */
	abstract public void wallCollision(Map map);
	
	/**
	 * Kisz�m�tja a mozg�s orient�ci�j�t.
	 * @param angle Elfordul�s sz�ge radi�nban.
	 */
	public void rotate(double angle) {
		this.orientation = this.orientation + angle;
	}

	/**
	 * A fallal val� �tk�z�st vizsg�lja.
	 * @param map P�lya.
	 * @return boolean Van-e �tk�z�s a fallal.
	 */
	protected boolean is_wallCollision(Map map){

		for (int i = 0; i < map.lines.size(); i++) { 
			Area a = new Area(area);
			a.intersect(new Area(map.lines.get(i)));
			if(!a.isEmpty()) return true;
		}
		return false;
	}
	
	/**
	 * A param�terk�nt kapott objektumra vizsg�lja meg, hogy �tk�z�tt-e valamivel.
	 * @param e Objektum.
	 */
	abstract protected void collisionDetection(Element e);

}
