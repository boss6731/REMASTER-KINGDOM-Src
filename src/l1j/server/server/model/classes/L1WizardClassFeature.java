package l1j.server.server.server.model.classes;

class L1WizardClassFeature extends l1j.server.server.model.classes.L1ClassFeature {
	@Override
	public int getMagicLevel(int playerLevel) {
		return Math.min(10, playerLevel / 4);
	}

	@Override
	public String getClassInitial() {
		return "M";

	}



}