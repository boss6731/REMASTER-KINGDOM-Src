package l1j.server.MJWebServer.Dispatcher.my.service.character;

public class MJMyCharInvItemInfo{
	public String name;
	public String display;
	public int objectId;
	public int itemId;
	public int iconId;
	public int count;
	public boolean equipped;
	public boolean identified;
	public int bless;
	public int templateBless;
	public int elementalEnchantLevel;
	public int enchantLevel;
	public int dollbonuslevel;
	public int dollbonusvalue;
	public int blesslevel;
	public int BlessType;
	public int BlessTypeValue;

	public MJMyCharInvItemCategory category;
	MJMyCharInvItemInfo(){	
		category = MJMyCharInvItemCategory.etc;
	}
}
