package l1j.server.MJWebServer.Dispatcher.Template.Item.POJO;

import l1j.server.server.templates.L1Item;

public class MJNameCodeInfo {
	public int code;
	public String displayName;
	public String name;
	
	public MJNameCodeInfo(){}
	public MJNameCodeInfo(int code, String displayName, String name){
		this.code = code;
		this.displayName = displayName;
		this.name = name;
	}
	
	public static class ATTRIBUTE{
		public static final MJNameCodeInfo ALL = new MJNameCodeInfo(-1, "�Ӽ� ��ü", "ALL");
		public static final MJNameCodeInfo FIRE = new MJNameCodeInfo(1, "ȭ��", "FIRE");
		public static final MJNameCodeInfo WATER = new MJNameCodeInfo(2, "����", "WATER");
		public static final MJNameCodeInfo EARTH = new MJNameCodeInfo(4, "����", "EARTH");
		public static final MJNameCodeInfo WIND = new MJNameCodeInfo(3, "ǳ��", "WIND");
		public static final MJNameCodeInfo NONE = new MJNameCodeInfo(0, "����", "NONE");
		public static final MJNameCodeInfo[] ATTRIBUTE_LIST = new MJNameCodeInfo[]{
				ALL, FIRE, WATER, EARTH, WIND, NONE
		};
	}
	
	public static class STATUS{
		public static final MJNameCodeInfo ALL = new MJNameCodeInfo(-1, "���� ��ü", "ALL");
		public static final MJNameCodeInfo NORMAL = new MJNameCodeInfo(2, "�Ϲ�", "NORMAL");
		public static final MJNameCodeInfo BLESS = new MJNameCodeInfo(3, "�ູ", "BLESS");
		public static final MJNameCodeInfo CURSE = new MJNameCodeInfo(4, "����", "CURSE");
		public static final MJNameCodeInfo NOT_IDENT = new MJNameCodeInfo(1, "��Ȯ��", "NOT_IDENT");
		public static final MJNameCodeInfo[] ITEM_STATUS_LIST = new MJNameCodeInfo[]{
			ALL, NORMAL, BLESS, CURSE, NOT_IDENT	
		};
	}
	
	public static class TRADE_TYPE{
		public static final MJNameCodeInfo SELL = new MJNameCodeInfo(0, "����", "SELL");
		public static final MJNameCodeInfo BUY = new MJNameCodeInfo(1, "�Ǹ�", "BUY");
	}
	
	public static class CATEGORY{
		public static final MJNameCodeInfo ALL = new MJNameCodeInfo(0, "��ü", "ALL");
		public static final MJNameCodeInfo WEAPON = new MJNameCodeInfo(1, "����", "WEAPON");
		public static final MJNameCodeInfo ARMOR = new MJNameCodeInfo(2, "��", "ARMOR");
		public static final MJNameCodeInfo ACCESSORY = new MJNameCodeInfo(3, "�׼�����", "ACCESSORY");
		public static final MJNameCodeInfo ETC = new MJNameCodeInfo(4, "��Ÿ", "ETC");
		public static final MJNameCodeInfo[] ITEM_CATEGORIES = new MJNameCodeInfo[]{
				ALL, WEAPON, ARMOR, ACCESSORY, ETC
		};
		
		public static MJNameCodeInfo select_category(L1Item item){
			int type2 = item.getType2();
			if(type2 == 0)
				return ETC;
			else if(type2 == 1)
				return WEAPON;
			
			int type = item.getType();
			if(type == 8 || 
					type == 9 || 
					type == 10 || 
					type == 11 || 
					type == 12 || 
					type == 14 || 
					type == 28)
				return ACCESSORY;
			return ARMOR;
		}
	}
}
