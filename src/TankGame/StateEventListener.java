package TankGame;

/**
 * Az �llapotg�p interf�sz oszt�lya az �llapot�tmeneteket megval�s�t� f�ggv�nyekkel.
 * @author Szab� D�niel
 *
 */
public interface StateEventListener {
	State onEventHostGame();
	State onEventJoinGame();
	State onEventStartGame();
	State onEventCancel();
	State onEventExit();
}