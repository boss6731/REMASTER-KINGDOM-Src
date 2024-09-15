package l1j.server.server.server.model.classes;

// 添加槍兵
public class L1LancerClassFeature extends l1j.server.server.model.classes.L1ClassFeature {
	@Override
	public int getMagicLevel(int playerLevel) {
		return playerLevel / 50;
	}
	
	@Override
	public String getClassInitial() {
		return "L";
	}
}