/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TankGame;

import GUI_Pack.GUI;

/**
 * TankTrouble++
 * @author Hegyi S�muel, Horv�th Gy�z�, Szab� D�niel
 */
public class Main {
	public static void main(String[] args) {
		StateMachine SM = new StateMachine();
		GUI g = new GUI(SM);
		SM.setGui(g);
	}
}