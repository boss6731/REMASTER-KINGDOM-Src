package l1j.server.server.server.model.classes;

import l1j.server.server.server.model.classes.L1ClassFeature;

class L1BlackWizardClassFeature extends L1ClassFeature {
	@Override
	public int getMagicLevel(int playerLevel) {
		return Math.min(10, playerLevel / 6);
	}

	@Override
	public String getClassInitial() {
		return "I";
	}
}