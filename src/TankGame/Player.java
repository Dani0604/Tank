package TankGame;

import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;

/**
 * A játékos beállításait tartalmazza, illetve azt, hogy hogy szeretné irányítani a tankját.
 * @author Szabó Dániel
 *
 */
public class Player implements Serializable  {

	private static final long serialVersionUID = 1L;

	/**
	 * Megadja, hogy a játékos hogy szeretné irányítani a tankját.
	 * @author Szabó Dániel
	 *
	 */
	public class Controls implements Serializable 
	{
		private static final long serialVersionUID = 1L;
		public boolean turnLeft;
		public boolean turnLeft_old;

		public boolean turnRight;
		public boolean turnRight_old;

		public boolean moveForward;
		public boolean moveForward_old;
		
		public boolean moveBackward;
		public boolean moveBackward_old;

		public boolean shoot;
		public boolean shoot_old;

	};
	
	/**
	 * A játékos leíróit tartalmazó osztály.
	 * @author Szabó Dániel
	 *
	 */
	public class Settings implements Serializable 
	{
		private static final long serialVersionUID = 7425231142562328797L;
		public int ID;
		public String name;
		public Color color;
		
		Settings(){
			name = "Default";
			color = Color.WHITE;
			ID = (int)(Math.random()*100000); //Használjuk???
		}
		
		/**
		 * Kiírja a játékos nevét, színét, ID-ját az output streamre.
		 * @param stream Object Output Stream.
		 * @throws IOException IO Kivétel.
		 */
		private void writeObject(java.io.ObjectOutputStream stream) throws IOException{
			stream.writeObject(name);
			stream.writeObject(color);
			stream.writeObject(ID);
		}

		/**
		 * Beolvassa az input streamre érkezõ játékos attribútumokat.
		 * @param stream Object Input Stream.
		 * @throws IOException IO kivétel.
		 * @throws ClassNotFoundException Ismeretlen osztály kivétel.
		 */
		private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException{
			name = (String) stream.readObject();
			color = (Color) stream.readObject();
			ID = (int) stream.readObject();
		}	
	};

	public Controls controls;
	public Tank tank;
	public Settings settings;
	
	public Player(Tank t) {
		this.tank = t;
		this.controls = new Controls();
		settings = new Settings();
		
		if (t != null)
			t.setPlayer(this);
	}

	/**
	 * Kiírja a játékost az output streamre.
	 * @param stream Object Output Stream.
	 * @throws IOException IO kivétel.
	 */
	private void writeObject(java.io.ObjectOutputStream stream) throws IOException{
		stream.writeObject(settings);
		stream.writeObject(controls);
	}

	/**
	 * Beolvassa az input streamre érkezõ játékost.
	 * @param stream Object Input Stream.
	 * @throws IOException IO kivétel.
	 * @throws ClassNotFoundException Ismeretlen osztály kivétel.
	 */
	private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException{
		settings = (Settings) stream.readObject();
		controls = (Controls) stream.readObject();
	}	

}
