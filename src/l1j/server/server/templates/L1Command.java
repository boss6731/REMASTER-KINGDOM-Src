package l1j.server.server.templates;

import l1j.server.Config;

public class L1Command {
	private final String _name;
	private final int _level;
	private final String _executorClassName;

	public L1Command(String name, int level, String executorClassName) {
		_name = name;
		_level = Config.ServerAdSetting.GMCODE;
		_executorClassName = executorClassName;
	}

	public String getName() {
		return _name;
	}

	public int getLevel() {
		return _level;
	}

	public String getExecutorClassName() {
		return _executorClassName;
	}
}
