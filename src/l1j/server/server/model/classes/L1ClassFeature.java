package l1j.server.server.model.classes;

import l1j.server.server.model.Instance.L1PcInstance;

public abstract class L1ClassFeature {
	public static L1ClassFeature newClassFeature(int classId) {
		if (classId == L1PcInstance.CLASSID_PRINCE
				|| classId == L1PcInstance.CLASSID_PRINCESS) {
			return new L1RoyalClassFeature();
		}
		if (classId == L1PcInstance.CLASSID_ELF_MALE
				|| classId == L1PcInstance.CLASSID_ELF_FEMALE) {
			return new L1ElfClassFeature();
		}
		if (classId == L1PcInstance.CLASSID_KNIGHT_MALE
				|| classId == L1PcInstance.CLASSID_KNIGHT_FEMALE) {
			return new L1KnightClassFeature();
		}
		if (classId == L1PcInstance.CLASSID_WIZARD_MALE
				|| classId == L1PcInstance.CLASSID_WIZARD_FEMALE) {
			return new L1WizardClassFeature();
		}
		if (classId == L1PcInstance.CLASSID_DARK_ELF_MALE
				|| classId == L1PcInstance.CLASSID_DARK_ELF_FEMALE) {
			return new L1DarkElfClassFeature();
		}
		if (classId == L1PcInstance.CLASSID_DRAGONKNIGHT_MALE
				|| classId == L1PcInstance.CLASSID_DRAGONKNIGHT_FEMALE) {
			return new L1DragonKnightClassFeature();
		}
		if (classId == L1PcInstance.CLASSID_BLACKWIZARD_MALE
				|| classId == L1PcInstance.CLASSID_BLACKWIZARD_FEMALE) {
			return new L1BlackWizardClassFeature();
		}
		if (classId == L1PcInstance.CLASSID_WARRIOR_MALE
				|| classId == L1PcInstance.CLASSID_WARRIOR_FEMALE) {
			return new L1WarriorClassFeature();
		}
		if (classId == L1PcInstance.CLASSID_FENCER_MALE
				|| classId == L1PcInstance.CLASSID_FENCER_FEMALE) {
			return new L1FencerClassFeature();
		}
		if (classId == L1PcInstance.CLASSID_LANCER_MALE
				|| classId == L1PcInstance.CLASSID_LANCER_FEMALE) {
			return new L1LancerClassFeature();
		}
		throw new IllegalArgumentException();
	}

	public abstract int getMagicLevel(int playerLevel);

	public abstract String getClassInitial();
}