package l1j.server.MJTemplate.MJClassesType;

import java.util.HashMap;

import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJClassesType.MainStat.MJClassMainStat;
import l1j.server.MJTemplate.MJClassesType.MainStat.MJMainDexStat;
import l1j.server.MJTemplate.MJClassesType.MainStat.MJMainIntStat;
import l1j.server.MJTemplate.MJClassesType.MainStat.MJMainStrStat;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_OwnCharStatus;
public enum MJEClassesType {
	PRINCE(0,     new MJMainStrStat(), "王族"),
	KNIGHT(1,     new MJMainStrStat(), "騎士"),
	ELF(2,         new MJMainDexStat(), "妖精"),
	WIZARD(3,     new MJMainIntStat(), "法師"),
	DARKELF(4,     new MJMainStrStat(), "黑暗妖精"),
	DKNIGHT(5,     new MJMainStrStat(), "龍騎士"),
	WITCH(6,     new MJMainIntStat(), "幻術師"),
	WARRIOR(7,     new MJMainStrStat(), "戰士"),
	FENCER(8,     new MJMainStrStat(), "劍士"),
	LANCER(9,     new MJMainStrStat(), "黃金槍騎");

	private final int id;
	private final MJMainStat mainStat;
	private final String name;

	MJEClassesType(int id, MJMainStat mainStat, String name) {
		this.id = id;
		this.mainStat = mainStat;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public MJMainStat getMainStat() {
		return mainStat;
	}

	public String getName() {
		return name;
	}
}
	
	private static final HashMap<Integer, MJEClassesType> _gfxToClass;

	public Long toInt() {

	}
static{
		_gfxToClass = new HashMap<Integer, MJEClassesType>(MJEClassesType.values().length * 2);
		_gfxToClass.put(L1PcInstance.CLASSID_PRINCE, 								PRINCE);
		_gfxToClass.put(L1PcInstance.CLASSID_PRINCESS, 								PRINCE);
		_gfxToClass.put(L1PcInstance.CLASSID_KNIGHT_MALE, 							KNIGHT);
		_gfxToClass.put(L1PcInstance.CLASSID_KNIGHT_FEMALE, 						KNIGHT);
		_gfxToClass.put(L1PcInstance.CLASSID_ELF_MALE, 								ELF);
		_gfxToClass.put(L1PcInstance.CLASSID_ELF_FEMALE, 							ELF);
		_gfxToClass.put(L1PcInstance.CLASSID_WIZARD_MALE, 							WIZARD);
		_gfxToClass.put(L1PcInstance.CLASSID_WIZARD_FEMALE, 						WIZARD);
		_gfxToClass.put(L1PcInstance.CLASSID_DARK_ELF_MALE, 						DARKELF);
		_gfxToClass.put(L1PcInstance.CLASSID_DARK_ELF_FEMALE, 						DARKELF);
		_gfxToClass.put(L1PcInstance.CLASSID_DRAGONKNIGHT_MALE, 					DKNIGHT);
		_gfxToClass.put(L1PcInstance.CLASSID_DRAGONKNIGHT_FEMALE,					DKNIGHT);
		_gfxToClass.put(L1PcInstance.CLASSID_BLACKWIZARD_MALE, 						WITCH);
		_gfxToClass.put(L1PcInstance.CLASSID_BLACKWIZARD_FEMALE, 					WITCH);
		_gfxToClass.put(L1PcInstance.CLASSID_戰士_MALE, 								WARRIOR);
		_gfxToClass.put(L1PcInstance.CLASSID_戰士_FEMALE, 							WARRIOR);
		_gfxToClass.put(L1PcInstance.CLASSID_FENCER_MALE, 							FENCER);
		_gfxToClass.put(L1PcInstance.CLASSID_FENCER_FEMALE, 						FENCER);
		_gfxToClass.put(L1PcInstance.CLASSID_LANCER_MALE, 							LANCER);
		_gfxToClass.put(L1PcInstance.CLASSID_LANCER_FEMALE, 						LANCER);
	}
	
	private int 			_type;
	private MJClassMainStat _mstat;
	private String text;
	MJEClassesType(int type, MJClassMainStat mstat, String text){
		_type 	= type;
		_mstat	= mstat;
		this.text = text;
	}
	
	public int toInt(){
		return _type;
	}

	public String text(){
		return text;
	}
	
	public void addBunusStat(L1PcInstance pc, int i){
		_mstat.addStat(pc, i);
		if(pc.getNetConnection() != null) 
			pc.sendPackets(new S_OwnCharStatus(pc), true);
	}
	
	public static MJEClassesType fromInt(int i){
		MJEClassesType[] types = MJEClassesType.values();
		return i < 0 || i >= types.length ? null : types[i];
	}
	
	public static MJEClassesType fromGfx(int gfx){
		MJEClassesType t = _gfxToClass.get(gfx);
		return t == null ? PRINCE : t;
	}
	
	public static MJEClassesType fromRand(){
		MJEClassesType[] types = MJEClassesType.values();
		
		return types[MJRnd.next(types.length)];
	}
	
	public MJEClassesStatModel statModel() {
		return MJEClassesStatModel.fromCharacterType(this);
	}
}
