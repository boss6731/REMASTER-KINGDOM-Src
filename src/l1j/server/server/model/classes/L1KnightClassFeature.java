package l1j.server.server.server.model.classes;

class L1KnightClassFeature extends l1j.server.server.model.classes.L1ClassFeature {
	@Override
	public int getMagicLevel(int playerLevel) {
		return playerLevel / 50;
	}

	@Override
	public String getClassInitial() {
		return "K";
	}
}