package l1j.server.server.server.model.classes;

class L1ElfClassFeature extends l1j.server.server.model.classes.L1ClassFeature {

	@Override
	public int getMagicLevel(int playerLevel) {
		return Math.min(6, playerLevel / 8);
	}

	@Override
	public String getClassInitial() {
		return "E";
	}
}