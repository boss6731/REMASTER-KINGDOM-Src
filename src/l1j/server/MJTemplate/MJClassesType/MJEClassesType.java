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
	PRINCE(0, 	new MJMainStrStat(), "군주"),
	KNIGHT(1, 	new MJMainStrStat(), "기사"),
	ELF(2, 		new MJMainDexStat(), "요정"),
	WIZARD(3, 	new MJMainIntStat(), "마법사"),
	DARKELF(4, 	new MJMainStrStat(), "다크엘프"),
	DKNIGHT(5, 	new MJMainStrStat(), "용기사"),
	WITCH(6, 	new MJMainIntStat(), "환술사"),
	WARRIOR(7, 	new MJMainStrStat(), "전사"),
	FENCER(8, 	new MJMainStrStat(), "검사"),
	LANCER(9, 	new MJMainStrStat(), "창기사");
	
	private static final HashMap<Integer, MJEClassesType> _gfxToClass;
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
		_gfxToClass.put(L1PcInstance.CLASSID_전사_MALE, 								WARRIOR);
		_gfxToClass.put(L1PcInstance.CLASSID_전사_FEMALE, 							WARRIOR);
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
