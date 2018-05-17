/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

/**
 * Általános hálózati osztály.
 * @author Horváth Gyõzõ
 */
abstract class Network {

	public Network(){}
	
	/**
	 * Adott ip címre csatlakozik.
	 * @param ip IP cím
	 */
	abstract public void connect(String ip);

	/**
	 * Lecsatlakozik a socketrõl.
	 */
	abstract public void disconnect();

	
}
