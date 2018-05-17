package TankGame;

import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;

/**
 * A j�t�kos be�ll�t�sait tartalmazza, illetve azt, hogy hogy szeretn� ir�ny�tani a tankj�t.
 * @author Szab� D�niel
 *
 */
public class Player implements Serializable  {

	private static final long serialVersionUID = 1L;

	/**
	 * Megadja, hogy a j�t�kos hogy szeretn� ir�ny�tani a tankj�t.
	 * @author Szab� D�niel
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
	 * A j�t�kos le�r�it tartalmaz� oszt�ly.
	 * @author Szab� D�niel
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
			ID = (int)(Math.random()*100000); //Haszn�ljuk???
		}
		
		/**
		 * Ki�rja a j�t�kos nev�t, sz�n�t, ID-j�t az output streamre.
		 * @param stream Object Output Stream.
		 * @throws IOException IO Kiv�tel.
		 */
		private void writeObject(java.io.ObjectOutputStream stream) throws IOException{
			stream.writeObject(name);
			stream.writeObject(color);
			stream.writeObject(ID);
		}

		/**
		 * Beolvassa az input streamre �rkez� j�t�kos attrib�tumokat.
		 * @param stream Object Input Stream.
		 * @throws IOException IO kiv�tel.
		 * @throws ClassNotFoundException Ismeretlen oszt�ly kiv�tel.
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
	 * Ki�rja a j�t�kost az output streamre.
	 * @param stream Object Output Stream.
	 * @throws IOException IO kiv�tel.
	 */
	private void writeObject(java.io.ObjectOutputStream stream) throws IOException{
		stream.writeObject(settings);
		stream.writeObject(controls);
	}

	/**
	 * Beolvassa az input streamre �rkez� j�t�kost.
	 * @param stream Object Input Stream.
	 * @throws IOException IO kiv�tel.
	 * @throws ClassNotFoundException Ismeretlen oszt�ly kiv�tel.
	 */
	private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException{
		settings = (Settings) stream.readObject();
		controls = (Controls) stream.readObject();
	}	

}
