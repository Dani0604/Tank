package TankGame;

/**
 * Állapotok közötti váltást okozó eseményeket kezelõ osztály.
 * @author Szabó Dániel
 *
 */
public enum State implements StateEventListener {
	/**
	 * Fõmenüben történõ állapotátmenetek.
	 */
	MainMenu{

		@Override
		public State onEventHostGame() {	
			return HostGame;
		}
		@Override
		public State onEventJoinGame() {
			return JoinGame;
		}

		@Override
		public State onEventStartGame() {
			return this;
		}

		@Override
		public State onEventCancel() {
			return this;
		}

		@Override
		public State onEventExit() {
			return this;
		}

	},
	/**
	 * Host game menüben történõ állapotátmenetek.
	 */
	HostGame {

		@Override
		public State onEventHostGame() {
			return this;
		}

		@Override
		public State onEventJoinGame() {
			return this;
		}

		@Override
		public State onEventStartGame() {
			return LobbyHost;
		}

		@Override
		public State onEventCancel() {
			return MainMenu;
		}

		@Override
		public State onEventExit() {
			return this;
		}

	},
	/**
	 * Join game menüben történõ állapotátmenetek.
	 */
	JoinGame {

		@Override
		public State onEventHostGame() {
			return this;
		}

		@Override
		public State onEventJoinGame() {
			return this;
		}

		@Override
		public State onEventStartGame() {
			return Lobby;
		}

		@Override
		public State onEventCancel() {
			return MainMenu;
		}

		@Override
		public State onEventExit() {
			return this;
		}

	},
	/**
	 * Lobby host mennüben történõ állapotátmenetek.
	 */
	LobbyHost{

		@Override
		public State onEventHostGame() {
			return this;
		}

		@Override
		public State onEventJoinGame() {
			return this;
		}

		@Override
		public State onEventStartGame() {
			return this;
		}

		@Override
		public State onEventCancel() {
			return MainMenu;
		}

		@Override
		public State onEventExit() {
			return this;
		}
		
	},
	/**
	 * Lobby kliens menüben történõ állapotátmenetek.
	 */
	Lobby{

		@Override
		public State onEventHostGame() {
			return this;
		}

		@Override
		public State onEventJoinGame() {
			return Ready;		
		}

	

		@Override
		public State onEventStartGame() {
			return this;
		}

		@Override
		public State onEventCancel() {
			return this;
		}

		@Override
		public State onEventExit() {
			return this;
		}
	},
	/**
	 * Ready jelzés kiadása utáni állapotátmenetek.
	 */
	Ready{

		@Override
		public State onEventHostGame() {
			// TODO Auto-generated method stub
			return this;
		}

		@Override
		public State onEventJoinGame() {
			return this;
		}

		@Override
		public State onEventStartGame() {
			// TODO Auto-generated method stub
			return Game;
		}

		@Override
		public State onEventCancel() {
			// TODO Auto-generated method stub
			return this;
		}

		@Override
		public State onEventExit() {
			System.exit(0);
			return this;
		}
		
	},
	/**
	 * Beállítások menüben történõ állapotátmenetek.
	 */
	Settings {

		@Override
		public State onEventHostGame() {
			// TODO Auto-generated method stub
			return this;
		}

		@Override
		public State onEventJoinGame() {
			// TODO Auto-generated method stub
			return this;
		}

		@Override
		public State onEventStartGame() {
			// TODO Auto-generated method stub
			return this;
		}

		@Override
		public State onEventCancel() {
			// TODO Auto-generated method stub
			return this;
		}

		@Override
		public State onEventExit() {
			System.exit(0);
			return this;
		}

	
	},
	/**
	 * Fõmenüben történõ állapotátmenetek.
	 */
	GameHost{

		@Override
		public State onEventHostGame() {
			// TODO Auto-generated method stub
			return this;
		}

		@Override
		public State onEventJoinGame() {
			// TODO Auto-generated method stub
			return this;
		}

		@Override
		public State onEventStartGame() {
			// TODO Auto-generated method stub
			return this;
		}

		@Override
		public State onEventCancel() {
			// TODO Auto-generated method stub
			return this;
		}

		@Override
		public State onEventExit() {
			System.exit(0);
			return this;
		}
	},
	/**
	 * A játék során történõ állapotátmenetek.
	 */
	Game{

		@Override
		public State onEventHostGame() {
			// TODO Auto-generated method stub
			return this;
		}

		@Override
		public State onEventJoinGame() {
			// TODO Auto-generated method stub
			return this;
		}

		@Override
		public State onEventStartGame() {
			// TODO Auto-generated method stub
			return this;
		}

		@Override
		public State onEventCancel() {
			// TODO Auto-generated method stub
			return this;
		}

		@Override
		public State onEventExit() {
			System.exit(0);
			return this;
		}
	};
}

