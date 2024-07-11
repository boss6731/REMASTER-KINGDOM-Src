package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.List;

import l1j.server.MJTemplate.MJString;

class MJMyApiCharDetailModel extends MJMyApiModel{
	String pledge;
	String birth;
	long login;
	int level;
	double exp;
	int totalRank;
	int classRank;
	int maxHp;
	int curHp;
	int maxMp;
	int curMp;
	int str;
	int intel;
	int dex;
	int wis;
	int con;
	int cha;
	int pk;
	int lawful;
	List<EquipmentInfo> equipmentList;
	
	static class EquipmentInfo{
		String name;
		String display;
		int iconId;
		EquipmentInfo(){
			name = MJString.EmptyString;
			display = MJString.EmptyString;
		}
	}
}
