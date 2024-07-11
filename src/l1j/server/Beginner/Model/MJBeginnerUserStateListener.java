package l1j.server.Beginner.Model;

public interface MJBeginnerUserStateListener {
	public void onStarted(boolean completed);
	public void onFinished();
	public void onRevealed();
	public void onNotFound();
}
