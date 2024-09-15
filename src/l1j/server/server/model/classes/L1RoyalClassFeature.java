package l1j.server.server.server.model.classes;

class L1RoyalClassFeature extends l1j.server.server.model.classes.L1ClassFeature {
	@Override
	public int getMagicLevel(int playerLevel) {
		return Math.min(2, playerLevel / 10);
	}

	@Override
	public String getClassInitial() {
		return "P";
	}
}
