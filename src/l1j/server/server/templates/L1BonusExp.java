package l1j.server.server.templates;

public class L1BonusExp {
	private int _level;
	private double _exp_bonus;
	
	public int getLevel(){
		return _level;
	}
	
	public void setLevel(int lv){
		_level = lv;
	}
	
	public double getExpBonus(){
		return _exp_bonus;
	}
	
	public void setExpBonus(double exp){
		_exp_bonus = exp;
	}
}