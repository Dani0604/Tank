package TankGame;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.Serializable;
import java.awt.geom.Area;

/**
 * A pályán található objektumokat általánosan leíró absztrakt osztály.
 * @author Szabó Dániel
 *
 */
abstract public class Element implements Serializable{
	private static final long serialVersionUID = 4617701502825832245L;
	/**
	 * Az objektum pozíciója.
	 */
	protected Point2D position; // kirajzolás poziciója
	protected Point2D prevPos;
	/**
	 * Az objektum orientációja.
	 */
	protected double orientation; // orientáció megadása radiánban
	/**
	 * Az objektum sebessége.
	 */
	protected double velocity; // elem mozgásának sebessége
	/**
	 * Az objektum területe.
	 */
	protected Area area;
	
	/**
	 * Kiírja az objektum pozícióját és orientációját az output stream-re.
	 * @param stream Object Output Stream.
	 * @throws IOException IO kivétel.
	 */
	private void writeObject(java.io.ObjectOutputStream stream) throws IOException{
		stream.writeObject(position);
		stream.writeObject(orientation);		
	}
	
	/**
	 * Fogadja az input stream-rõl az érkezõ objektum pozíciókat és orientációkat.
	 * @param stream Output Stream.
	 * @throws IOException IO kivétel.
	 * @throws ClassNotFoundException Ismeretlen osztály kivétel.
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
	 * A térkép, amin az objektumok találhatók.
	 */
	protected Map map;

	/**
	 * Az objektumok kirajzolásáért felelõs függvény.
	 * @param g Grafika
	 */
	abstract public void draw(Graphics g);

	/**
	 * Az objektumok elmozdulását számítja ki.
	 * @param T Mintavételi idõ.s
	 */
	abstract public void move(double T);
	
	/**
	 * Az adott objektum típusát adja meg.
	 * @return Objektum típus.
	 */
	abstract protected Type getType();
	
	/**
	 * Objektum törlése.
	 */
	abstract public void delete();
	
	/**
	 * A fallal való ütközést vizsgálja.
	 * @param map Pálya
	 */
	abstract public void wallCollision(Map map);
	
	/**
	 * Kiszámítja a mozgás orientációját.
	 * @param angle Elfordulás szöge radiánban.
	 */
	public void rotate(double angle) {
		this.orientation = this.orientation + angle;
	}

	/**
	 * A fallal való ütközést vizsgálja.
	 * @param map Pálya.
	 * @return boolean Van-e ütközés a fallal.
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
	 * A paraméterként kapott objektumra vizsgálja meg, hogy ütközött-e valamivel.
	 * @param e Objektum.
	 */
	abstract protected void collisionDetection(Element e);

}
