package l1j.server.server.server.model.classes;

public class L1WarriorClassFeature extends l1j.server.server.model.classes.L1ClassFeature {
	@Override
	public int getMagicLevel(int playerLevel) {
		return playerLevel / 50;
	}

	@Override
	public String getClassInitial() {
		return "W";
	}
}