package TankGame;

import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Shotgun lövedék osztály, egyszerre több lövedéket lõ ki.
 * @author Szabó Dániel
 *
 */
public class Shotgun extends Bullet {

	private static final long serialVersionUID = 1L;

	public Shotgun(Tank t) {
		super(t);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void shoot(CopyOnWriteArrayList<Element> elements) {
			for(int i = 0;i < 5;i++){
				Bullet b = new Bullet(null);
				b.position = new Point2D.Double();
				b.prevPos = new Point2D.Double();
				b.orientation = t.orientation + (i-2)*Math.PI/12;
				b.position.setLocation(t.position.getX() + ((Tank.LENGTH / 2 + DIAMETER*2) * Math.cos(b.orientation)),
						t.position.getY() + ((Tank.LENGTH / 2 + DIAMETER*2) * Math.sin(b.orientation)));
				b.prevPos.setLocation(b.position);
				
				Ellipse2D.Double circle = new Ellipse2D.Double(b.position.getX(), b.position.getY(), DIAMETER, DIAMETER);
				b.area = new Area(circle);
				elements.add(b);	
			}
			t.nextBullet = new Bullet(t);
		
	}
}
