package TankGame;

import GUI_Pack.GUI;

/**
 * A men�k megjelen�t�s�t vez�rl� �llapotg�p.
 * @author Szab� D�niel
 *
 */

public class StateMachine implements StateEventListener {
	public State currentState = State.MainMenu;
	private GUI gui;
	private GameControl gctrl;
	public boolean IsPowerUp; 


	void setGui(GUI g){
		gui = g;
	}

	/**
	 * Host Game gombra kattint�s kezel�se.
	 */
	@Override
	public State onEventHostGame() {
		// TODO Auto-generated method stub
		switch (currentState){
		case MainMenu:
			gui.mainmenu.frm.setVisible(false);
			gui.hostGame.frm.setVisible(true);
			break;
		default:
			break;
		}
		currentState = currentState.onEventHostGame();
		return currentState;
	}

	/**
	 * Join Game gombra kattint�s kezel�se.
	 */
	@Override
	public State onEventJoinGame() {
		// TODO Auto-generated method stub
		switch (currentState){
		case MainMenu:
			gui.mainmenu.frm.setVisible(false);
			gui.joinGame.frm.setVisible(true);
			break;
		default:
			break;
		}
		currentState = currentState.onEventJoinGame();
		return currentState;
	}

	/**
	 * Start gombra kattint�s kezel�se.
	 */
	@Override
	public State onEventStartGame() {
		// TODO Auto-generated method stub
		switch (currentState){
		case HostGame:
			gui.hostGame.frm.setVisible(false);
			gui.lobby.frm.setVisible(true);
			gui.lobby.btnStart.setVisible(true);
			gctrl = new GameControl(this);
			//gctrl.startGame();
			gui.startClient("localhost");
			break;
		case JoinGame:
			gui.startClient(gui.joinGame.IPAddress.getText());
			gui.lobby.frm.setVisible(true);
			gui.joinGame.frm.setVisible(false);
			gui.lobby.btnStart.setVisible(false);
			break;
		case LobbyHost:
			if (gctrl.players.size() > 1){
				currentState = State.GameHost;
				gui.startGame();
				gctrl.startGame();
				gui.lobby.frm.setVisible(false);
				gui.gameFrm.setVisible(true);
			}
			break;
		case Ready:
			gui.startGame();
			gui.lobby.frm.setVisible(false);
			gui.gameFrm.setVisible(true);
			break;
		default:
			break;
		}
		currentState = currentState.onEventStartGame();
		return currentState;
	}

	/**
	 * Cancel gombra kattint�s kezel�se.
	 */
	@Override
	public State onEventCancel() {
		switch (currentState){
		case HostGame:
			//gctrl.closeServer();
			//gui.closeClient();
			gui.mainmenu.frm.setVisible(true);
			gui.hostGame.frm.setVisible(false);
			break;
		case JoinGame:
			gui.mainmenu.frm.setVisible(true);
			gui.joinGame.frm.setVisible(false);
			break;
		case LobbyHost:
			gui.stopThreads();
			gui.closeClient();
			gctrl.stopThreads();
			gctrl.closeServer();
			gui.mainmenu.frm.setVisible(true);
			gui.lobby.frm.setVisible(false);
			break;
		default:
			break;
		}
		currentState = currentState.onEventCancel();
		return currentState;
	}

	/**
	 * Exit gombra kattint�s kezel�se.
	 */
	@Override
	public State onEventExit() {
		// TODO Auto-generated method stub
		System.exit(0);
		currentState = currentState.onEventExit();
		return currentState;
	}
}


