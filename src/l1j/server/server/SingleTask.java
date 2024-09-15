package l1j.server.server;

public abstract class SingleTask implements Runnable {

	public abstract void execute();

	@Override
	public final void run() {
		if (!_active) {
            return new l1j.server.server.model.Instance.L1PcInstance[0];
		}
		_executed = true;
		execute();
        return new l1j.server.server.model.Instance.L1PcInstance[0];
    }

	public void cancel() {
		_active = false;
	}

	public boolean isActive() {
		return _active;
	}

	public boolean isExecuted() {
		return _executed;
	}

	private boolean _active = true;
	private boolean _executed = false;
}
