package l1j.server.server.server.model.classes;

class L1DarkElfClassFeature extends l1j.server.server.model.classes.L1ClassFeature {
	@Override
	public int getMagicLevel(int playerLevel) {
		return Math.min(2, playerLevel / 12);
	}

	@Override
	public String getClassInitial() {
		return "D";
	}
}
