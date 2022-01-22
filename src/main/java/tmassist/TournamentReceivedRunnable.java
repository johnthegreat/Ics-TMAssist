package tmassist;

public abstract class TournamentReceivedRunnable implements Runnable {
	protected Tournament tournament;
	public abstract void run();
	
	public TournamentReceivedRunnable() { }

	public Tournament getTournament() {
		return tournament;
	}

	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}
}
