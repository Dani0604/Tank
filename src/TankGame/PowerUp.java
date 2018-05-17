package TankGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import sl.shapes.StarPolygon;


/**
 * Power-up osztály.
 * @author Szabó Dániel
 *
 */
public class PowerUp extends Element{

	final double VELOCITY = 1;
	final int DIAMETER = 16;
	final int STARRADIUS = 6;
	final int INNERRADIUS = 3;
	
	private static final long serialVersionUID = -4196220458632733988L;
	
	PowerUp(Map map){
		velocity = VELOCITY;
		do {
			position = new Point2D.Double(Math.random()*map.MapWidth, Math.random()*map.MapHeight);
			Ellipse2D.Double circle = new Ellipse2D.Double(position.getX()-DIAMETER/2, position.getY()-DIAMETER/2, DIAMETER, DIAMETER);
			area = new Area(circle);
		}
		while(is_wallCollision(map));
		orientation = 0;
	}
	
	
	@Override
	public void draw(Graphics g) {
		Ellipse2D.Double circle = new Ellipse2D.Double(position.getX()-DIAMETER/2, position.getY()-DIAMETER/2, DIAMETER, DIAMETER);
		StarPolygon SP = new StarPolygon((int)position.getX(), (int)position.getY(), STARRADIUS, INNERRADIUS, 5, orientation);
		
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.setColor(Color.black);
		g2d.draw(circle);
		g2d.draw(SP);
	}

	@Override
	public void move(double T) {
		rotate(velocity*T/1000);
	}

	@Override
	protected Type getType() {
		return Type.POWERUP;
	}

	@Override
	public void delete() {
	}

	@Override
	public void wallCollision(Map map) {	
	}

	@Override
	protected void collisionDetection(Element e) {
		// TODO Auto-generated method stub
		switch (e.getType()){
		case TANK:
			Area a = new Area(area);
			a.intersect(e.area);
			if (!a.isEmpty()) deleteElement = true; 
			break;
		default:
			break;
		}
	}
}
