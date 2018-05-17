package TankGame;

/**
 * �llapotok k�z�tti v�lt�st okoz� esem�nyeket kezel� oszt�ly.
 * @author Szab� D�niel
 *
 */
public enum State implements StateEventListener {
	/**
	 * F�men�ben t�rt�n� �llapot�tmenetek.
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
	 * Host game men�ben t�rt�n� �llapot�tmenetek.
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
	 * Join game men�ben t�rt�n� �llapot�tmenetek.
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
	 * Lobby host menn�ben t�rt�n� �llapot�tmenetek.
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
	 * Lobby kliens men�ben t�rt�n� �llapot�tmenetek.
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
	 * Ready jelz�s kiad�sa ut�ni �llapot�tmenetek.
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
	 * Be�ll�t�sok men�ben t�rt�n� �llapot�tmenetek.
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
	 * F�men�ben t�rt�n� �llapot�tmenetek.
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
	 * A j�t�k sor�n t�rt�n� �llapot�tmenetek.
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

