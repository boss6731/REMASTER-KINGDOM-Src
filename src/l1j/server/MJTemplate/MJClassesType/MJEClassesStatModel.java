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
		switch(type.toInt()) {
		case 0: // 군주
			str = 13;
			dex = 9;
			con = 11;
			wis = 11;
			cha = 13;
			intel = 9;
			bonus = 9;
			break;
		case 1: // 기사
			str = 16;
			dex = 12;
			con = 16;
			wis = 9;
			cha = 10;
			intel = 8;
			bonus = 4;
			break;
		case 2: // 요정
			str = 10;
			dex = 12;
			con = 12;
			wis = 12;
			cha = 9;
			intel = 12;
			bonus = 8;
			break;
		case 3: // 법사
			str = 8;
			dex = 7;
			con = 12;
			wis = 14;
			cha = 8;
			intel = 14;
			bonus = 12;
			break;
		case 4: // 다크엘프
			str = 15;
			dex = 12;
			con = 12;
			wis = 10;
			cha = 8;
			intel = 11;
			bonus = 7;
			break;
		case 5: // 용기사
			str = 13;
			dex = 11;
			con = 14;
			wis = 10;
			cha = 8;
			intel = 10;
			bonus = 9;
			break;
		case 6: // 환술사
			str = 9;
			dex = 10;
			con = 12;
			wis = 14;
			cha = 8;
			intel = 12;
			bonus = 10;
			break;
		case 7: // 전사
			str = 16;
			dex = 13;
			con = 16;
			wis = 7;
			cha = 9;
			intel = 10;
			bonus = 4;
			break;
		case 8: // 검사
			str = 16;
			dex = 13;
			con = 15;
			wis = 11;
			cha = 5;
			intel = 11;
			bonus = 4;
			break;
		case 9: // 창기사
			str = 14;
			dex = 12;
			con = 16;
			wis = 12;
			cha = 6;
			intel = 9;
			bonus = 6;
			break;
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
