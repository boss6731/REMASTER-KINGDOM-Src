package l1j.server.server.server.model.classes;

class L1DragonKnightClassFeature extends l1j.server.server.model.classes.L1ClassFeature {

	@Override
	public int getMagicLevel(int playerLevel) {
		return Math.min(4, playerLevel / 9);
	}

	@Override
	public String getClassInitial() {
		return "R";
	}
}
