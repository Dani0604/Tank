/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

/**
 * �ltal�nos h�l�zati oszt�ly.
 * @author Horv�th Gy�z�
 */
abstract class Network {

	public Network(){}
	
	/**
	 * Adott ip c�mre csatlakozik.
	 * @param ip IP c�m
	 */
	abstract public void connect(String ip);

	/**
	 * Lecsatlakozik a socketr�l.
	 */
	abstract public void disconnect();

	
}
