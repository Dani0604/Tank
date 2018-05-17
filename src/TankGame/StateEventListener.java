package TankGame;

/**
 * Az állapotgép interfész osztálya az állapotátmeneteket megvalósító függvényekkel.
 * @author Szabó Dániel
 *
 */
public interface StateEventListener {
	State onEventHostGame();
	State onEventJoinGame();
	State onEventStartGame();
	State onEventCancel();
	State onEventExit();
}