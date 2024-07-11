package l1j.server.MJTemplate.MJClassesType;

public class MJEClassesStatModel {
	static MJEClassesStatModel fromCharacterType(MJEClassesType type) {
		int str = 0;
		int dex = 0;
		int con = 0;
		int intel = 0;
		int wis = 0;
		int cha = 0;
		int bonus = 0;
		if (type.toInt() == 0) { // 王族
			str = 13;
			dex = 9;
			con = 11;
			wis = 11;
			cha = 13;
			intel = 9;
			bonus = 9;
		} else if (type.toInt() == 1) { // 騎士
			str = 16;
			dex = 12;
			con = 16;
			wis = 9;
			cha = 10;
			intel = 8;
			bonus = 4;
		} else if (type.toInt() == 2) { // 妖精
			str = 10;
			dex = 12;
			con = 12;
			wis = 12;
			cha = 9;
			intel = 12;
			bonus = 8;
		} else if (type.toInt() == 3) { // 法師
			str = 8;
			dex = 7;
			con = 12;
			wis = 14;
			cha = 8;
			intel = 14;
			bonus = 12;
		} else if (type.toInt() == 4) { // 黑暗妖精
			str = 15;
			dex = 12;
			con = 12;
			wis = 10;
			cha = 8;
			intel = 11;
			bonus = 7;
		} else if (type.toInt() == 5) { // 龍騎士
			str = 13;
			dex = 11;
			con = 14;
			wis = 10;
			cha = 8;
			intel = 10;
			bonus = 9;
		} else if (type.toInt() == 6) { // 幻術師
			str = 9;
			dex = 10;
			con = 12;
			wis = 14;
			cha = 8;
			intel = 12;
			bonus = 10;
		} else if (type.toInt() == 7) { // 戰士
			str = 16;
			dex = 13;
			con = 16;
			wis = 7;
			cha = 9;
			intel = 10;
			bonus = 4;
		} else if (type.toInt() == 8) { // 劍士
			str = 16;
			dex = 13;
			con = 15;
			wis = 11;
			cha = 5;
			intel = 11;
			bonus = 4;
		} else if (type.toInt() == 9) { // 黃金槍騎
			str = 14;
			dex = 12;
			con = 16;
			wis = 12;
			cha = 6;
			intel = 9;
			bonus = 6;
		}
		return new MJEClassesStatModel(str, dex, con, wis, cha, intel, bonus);
	}
	
	public int str;
	public int dex;
	public int con;
	public int wis;
	public int cha;
	public int intel;
	public int bonus;
	private MJEClassesStatModel(){}
	
	MJEClassesStatModel(int str, int dex, int con, int wis, int cha, int intel, int bonus){
		this.str = str;
		this.dex = dex;
		this.con = con;
		this.wis = wis;
		this.cha = cha;
		this.intel = intel;
		this.bonus = bonus;
	}
	
	public MJEClassesStatModel clone() {
		MJEClassesStatModel model = new MJEClassesStatModel();
		model.drain(this);
		return model;
	}
	
	public void drain(MJEClassesStatModel model) {
		this.str = model.str;
		this.dex = model.dex;
		this.con = model.con;
		this.wis = model.wis;
		this.cha = model.cha;
		this.intel = model.intel;
		this.bonus = model.bonus;
	}
}
