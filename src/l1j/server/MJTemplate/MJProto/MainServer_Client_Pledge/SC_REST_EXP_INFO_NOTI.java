package l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge;

import l1j.server.Config;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eEinhasadBonusType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_EXP_BOOSTING_INFO_NOTI;
import l1j.server.server.datatables.IncreaseEinhasadMap;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;

// TODO：此為自動生成的 PROTO 代碼
public class SC_REST_EXP_INFO_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static final int EINHASAD_LIMIT = Config.Einhasad_item.EINHASAD_LIMIT; // 阿因哈薩德最大充電範圍
	public static final int EINHASAD_UNIT = Config.Einhasad_item.EINHASAD_UNIT;

	// 阿因哈薩德的庇護充電量
	public static final int RE_DRAGON_DIAMOND_CHARGE = Config.Einhasad_item.RE_DRAGON_DIAMOND_CHARGE * EINHASAD_UNIT;
	// 最高級龍鑽充電量
	public static final int RE_DRAGON_DIAMOND_FINEST = Config.Einhasad_item.RE_DRAGON_DIAMOND_FINEST * EINHASAD_UNIT;
	// 高級龍鑽充電量
	public static final int RE_DRAGON_DIAMOND_ADVANCED = Config.Einhasad_item.RE_DRAGON_DIAMOND_ADVANCED * EINHASAD_UNIT;
	// 龍鑽充電量
	public static final int RE_DRAGON_DIAMOND_NORMAL = Config.Einhasad_item.RE_DRAGON_DIAMOND_NORMAL * EINHASAD_UNIT;
	// 龍藍寶石充電量
	public static final int RE_DRAGON_SAPPHIRE_NORMAL = Config.Einhasad_item.RE_DRAGON_SAPPHIRE_NORMAL * EINHASAD_UNIT;
	// 龍紅寶石充電量
	public static final int RE_DRAGON_RUBY_NORMAL = Config.Einhasad_item.RE_DRAGON_RUBY_NORMAL * EINHASAD_UNIT;
	// 龍鑽蘋果充電量
	public static final int RE_DRAGON_DIAMOND_APPLE = Config.Einhasad_item.RE_DRAGON_DIAMOND_APPLE * EINHASAD_UNIT;
}
	
	private static final int[] LEVEL_TO_BONUS_EFFECT = new int[] { 
			5, // 80
			6, // 81
			7, // 82
			8, // 83
			9, // 84
			10, // 85
			12, // 86
			14, // 87
			16, // 88
			18, // 89
			20, // 90
			23, // 91
			26, // 92
			29, // 93
			32, // 94
			35, // 95
			35, // 96
			35, // 97
			35, // 98
			35, // 99
			35, // 100
			35, // 101
			35, // 102
			35, // 103
			35, // 104
			35, // 105
			35, // 106
			35, // 107
			35, // 108
	};

	public static double calcDecreaseCharacterEinhasad(L1PcInstance pc, double descrease) {
		double dec = descrease;
		double effect = calcEinhasadEffectToDouble(pc);
		if (effect > 0D) {
			dec -= (dec * effect);
		}
		dec = IncreaseEinhasadMap.getInstance().increaseEinhasadValue(pc.getMapId(), dec);
		return dec;
	}

	public static double calcEinhasadEffectToDouble(L1PcInstance pc) {
		double effect = calcEinhasadEffect(pc);
		return effect <= 0 ? effect : effect * 0.01D;
	}

	public static int calcEinhasadEffect(L1PcInstance pc) {
		int effect = 0;
		int level = pc.getLevel();
		if (level >= 80) {
			int idx = level - 80;
			effect = LEVEL_TO_BONUS_EFFECT[idx];
		}
		if (pc.getEinhasadBlessper() >= 0) {
			effect += pc.getEinhasadBlessper();
		}

		return Math.min(effect, 100);
	}
//	10000000 1000%
//	1000000 100% 
//	100000 10% 
//	10000 1% 
//	1000 0.1%
	// 아인하사드 단계별 수치 설정
	public static int get_ein_level(int ein) { 
		//1000%
		if(ein >= 1)
			return 1;
		/*else if(ein >= 2601)
			return 3;
		else if(ein >= 1001)
			return 2;
		else if(ein > 0)
			return 1;*/
		return 0;
	}

	// 아인하사드 EXP(토파즈) 경험치 배율(1.0 = 100%)
	public static double expExtra(L1PcInstance pc, int einhasadLevel) {
		switch(einhasadLevel) {
		case 1:
			return 2.0D;
			
//		case 2:
//			return 1.0D;
//		case 3:
//			return 1.0D;
//		case 4:
//			return 2.0D;
		}
		return pc.hasSkillEffect(L1SkillId.EINHASAD_PRIMIUM_FLAT) || pc.hasSkillEffect(L1SkillId.EINHASAD_GREAT_FLAT) ?
				1.00D : 
				0.00D;
	}
	
	// 아인하사드 축복 EXP 보너스(1.0 = 100%)
	public static double expRation(int einhasadLevel) {
		return 1.0D;
//		switch(einhasadLevel) {
//		case 1:
//			return 1.0D;
////		case 2:
////			return 0.5D;
////		case 3:
////			return 1.0D;
////		case 4:
////			return 1.0D;
//		default:
//			return 0D;
//		}
	}
	
	// 아인하사드 레벨에 따른 드랍확률
	public static double selectInventoryItemsRateFromEinhasad(L1PcInstance pc, int einhasadLevel) {
		switch (einhasadLevel) {
//		case 4:
//			return 1.50; // 150%
//		case 3:
//			return 1.30; // 130% ...
//		case 2:
//			return 1.20;
		case 1:
			return 1.50;
		}
		
		if (pc.isGm()) {
			return 1.00;
		}
		return pc.hasSkillEffect(L1SkillId.EINHASAD_PRIMIUM_FLAT) || pc.hasSkillEffect(L1SkillId.EINHASAD_GREAT_FLAT) ? 1.00D : 0.00D;
	}
	public static void send(L1PcInstance pc, eEinhasadBonusType type) {
//		StackTraceElement[] a = new Throwable().getStackTrace();
//        
//        for(int i = a.length - 1; i > 0 ; i--){
//            System.out.print("클래스 - " + a[i].getClassName());
//            System.out.print(", 메소드 - "+a[i].getMethodName());
//            System.out.print(", 라인 - "+a[i].getLineNumber());
//            System.out.print(", 파일 - "+a[i].getFileName());
//            System.out.println();
//        }

//		if (pc == null || pc.getAccount() == null || pc.noPlayerCK)
//			return;

		synchronized(pc) {
			if (pc == null || pc.getAccount() == null || pc.noPlayerCK)
				return;
			
			int hasad = pc.getAccount().getBlessOfAin();
			if (hasad > 0)
				hasad /= 10000;

			int ration = 0;
			if (pc.hasSkillEffect(L1SkillId.DRAGON_TOPAZ))
				ration += 150; // 리뉴얼 81

			buff_end(pc);
			int ein_level = get_ein_level(hasad);
			ration += (expRation(ein_level) * 100);
//			int extra = (int)(expExtra(ein_level) * 100);
			buff_start(pc, ein_level - 1);
			
			SC_REST_EXP_INFO_NOTI noti = newInstance();
			if(pc.hasSkillEffect(L1SkillId.EINHASAD_GREAT_FLAT)) {
				hasad = 0;
			}
			noti.set_rest_gauge(hasad); //아인하사드 수치
			noti.set_default_ration(ration * 100);//축복 EXP
			noti.set_reduce_efficiency(calcEinhasadEffect(pc));//축복 소모 효율
			//noti.set_extra_exp_ratio(extra);//EXP
			noti.set_extra_exp_ratio(0);//EXP
			
			if (hasad <=0 && pc.hasSkillEffect(L1SkillId.EINHASAD_GREAT_FLAT)) { //차후에 변경해야함..
				type = eEinhasadBonusType.EinhasadFavor;
			}

			noti.set_type(type);
			
//			if (type != eEinhasadBonusType.BonusNone)
//				noti.set_type(type);
			
			//TODO 주석하면 정액제일때 1표시되냐 안되냐 차이 주석이맞음
//			if(type != eEinhasadBonusType.EinhasadFavor)
				noti.set_can_charge_count(pc.getAccount().getBlessOfAinCharge());//아인하사드 하루 충전 횟수

			L1Clan c = pc.getClan();
			if(c == null) {
				noti.set_bless_of_blood_pledge(false);
			}else {
				noti.set_bless_of_blood_pledge(pc.getClanBuffMap() != 0 && c != null && c.getEinhasadBlessBuff() != 0);				
			}
			pc.sendPackets(noti, MJEProtoMessages.SC_REST_EXP_INFO_NOTI, true);

			SC_EXP_BOOSTING_INFO_NOTI.send(pc);

		}
	}
	public static void send(L1PcInstance pc) {
		send(pc, eEinhasadBonusType.BonusNone);
	}
	
	private static void buff_start(L1PcInstance pc, int buff) {
		try {
			switch (buff) {
			case 1:
				if (!pc.hasSkillEffect(L1SkillId.EINHASAD_BUFF_1ST)) {
					pc.setSkillEffect(L1SkillId.EINHASAD_BUFF_1ST, -1);
					pc.addDamageReductionByArmor(2);
					pc.addWeightReduction(100);
				}
				break;
			case 2:
				if (!pc.hasSkillEffect(L1SkillId.EINHASAD_BUFF_2ND)) {
					pc.setSkillEffect(L1SkillId.EINHASAD_BUFF_2ND, -1);
					pc.addDamageReductionByArmor(3);
					pc.addWeightReduction(200);
				}
				break;
			case 3:
				if (!pc.hasSkillEffect(L1SkillId.EINHASAD_BUFF_3RD)) {
					pc.setSkillEffect(L1SkillId.EINHASAD_BUFF_3RD, -1);
					pc.addDamageReductionByArmor(4);
					pc.addWeightReduction(300);
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void buff_end(L1PcInstance pc) {
		try {
			if (pc.hasSkillEffect(L1SkillId.EINHASAD_BUFF_1ST)) {
				pc.killSkillEffectTimer(L1SkillId.EINHASAD_BUFF_1ST);
				pc.addDamageReductionByArmor(-2);
				pc.addWeightReduction(-100);
			}
			if (pc.hasSkillEffect(L1SkillId.EINHASAD_BUFF_2ND)) {
				pc.killSkillEffectTimer(L1SkillId.EINHASAD_BUFF_2ND);
				pc.addDamageReductionByArmor(-3);
				pc.addWeightReduction(-200);
			} 
			if (pc.hasSkillEffect(L1SkillId.EINHASAD_BUFF_3RD)) {
				pc.killSkillEffectTimer(L1SkillId.EINHASAD_BUFF_3RD);
				pc.addDamageReductionByArmor(-4);
				pc.addWeightReduction(-300);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static SC_REST_EXP_INFO_NOTI newInstance() {
		return new SC_REST_EXP_INFO_NOTI();
	}

	private int _rest_gauge;
	private int _default_ration;
	private int _extra_exp_ratio;
	private int _reduce_efficiency;
	private boolean _bless_of_blood_pledge;
	private int _can_charge_count;
	private eEinhasadBonusType _type;
	private int _bonus_exp_map;
	private int _default_ration_ex;
	private int _event_exp_map;
	
	private int _einhasad_rest_gauge_bonus_exp;
	
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_REST_EXP_INFO_NOTI(){
		set_bless_of_blood_pledge(false);
	}
	public int get_rest_gauge(){
		return _rest_gauge;
	}
	public void set_rest_gauge(int val){
		_bit |= 0x1;
		_rest_gauge = val;
	}
	public boolean has_rest_gauge(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_default_ration(){
		return _default_ration;
	}
	public void set_default_ration(int val){
		_bit |= 0x2;
		_default_ration = val;
	}
	public boolean has_default_ration(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_extra_exp_ratio(){
		return _extra_exp_ratio;
	}
	public void set_extra_exp_ratio(int val){
		_bit |= 0x4;
		_extra_exp_ratio = val;
	}
	public boolean has_extra_exp_ratio(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_reduce_efficiency(){
		return _reduce_efficiency;
	}
	public void set_reduce_efficiency(int val){
		_bit |= 0x8;
		_reduce_efficiency = val;
	}
	public boolean has_reduce_efficiency(){
		return (_bit & 0x8) == 0x8;
	}
	public boolean get_bless_of_blood_pledge(){
		return _bless_of_blood_pledge;
	}
	public void set_bless_of_blood_pledge(boolean val){
		_bit |= 0x10;
		_bless_of_blood_pledge = val;
	}
	public boolean has_bless_of_blood_pledge(){
		return (_bit & 0x10) == 0x10;
	}
	public int get_can_charge_count(){
		return _can_charge_count;
	}
	public void set_can_charge_count(int val){
		_bit |= 0x20;
		_can_charge_count = val;
	}
	public boolean has_can_charge_count(){
		return (_bit & 0x20) == 0x20;
	}
	public eEinhasadBonusType get_type(){
		return _type;
	}
	public void set_type(eEinhasadBonusType val){
		_bit |= 0x40;
		_type = val;
	}
	public boolean has_type(){
		return (_bit & 0x40) == 0x40;
	}
	public int get_bonus_exp_map(){
		return _bonus_exp_map;
	}
	public void set_bonus_exp_map(int val){
		_bit |= 0x80;
		_bonus_exp_map = val;
	}
	public boolean has_bonus_exp_map(){
		return (_bit & 0x80) == 0x80;
	}
	public int get_default_ration_ex(){
		return _default_ration_ex;
	}
	public void set_default_ration_ex(int val){
		_bit |= 0x100;
		_default_ration_ex = val;
	}
	public boolean has_default_ration_ex(){
		return (_bit & 0x100) == 0x100;
	}
	public int get_event_exp_map(){
		return _event_exp_map;
	}
	public void set_event_exp_map(int val){
		_bit |= 0x200;
		_event_exp_map = val;
	}
	public boolean has_event_exp_map(){
		return (_bit & 0x200) == 0x200;
	}
	public int get_einhasad_rest_gauge_bonus_exp(){
		return _einhasad_rest_gauge_bonus_exp;
	}
	public void set_einhasad_rest_gauge_bonus_exp(int val){
		_bit |= 0x400;
		_einhasad_rest_gauge_bonus_exp = val;
	}
	public boolean has_einhasad_rest_gauge_bonus_exp(){
		return (_bit & 0x400) == 0x400;
	}
	@Override
	public long getInitializeBit(){
		return (long)_bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;
	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_rest_gauge()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _rest_gauge);
		}
		if (has_default_ration()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _default_ration);
		}
		if (has_extra_exp_ratio()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _extra_exp_ratio);
		}
		if (has_reduce_efficiency()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _reduce_efficiency);
		}
		if (has_bless_of_blood_pledge()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(5, _bless_of_blood_pledge);
		}
		if (has_can_charge_count()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _can_charge_count);
		}
		if (has_type()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(7, _type.toInt());
		}
		if (has_bonus_exp_map()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _bonus_exp_map);
		}
		if (has_default_ration_ex()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(9, _default_ration_ex);
		}
		if (has_event_exp_map()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(10, _event_exp_map);
		}
		if (has_einhasad_rest_gauge_bonus_exp()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(11, _einhasad_rest_gauge_bonus_exp);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_rest_gauge()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_default_ration()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_extra_exp_ratio()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_reduce_efficiency()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_rest_gauge()){
			output.wirteInt32(1, _rest_gauge);
		}
		if (has_default_ration()){
			output.wirteInt32(2, _default_ration);
		}
		if (has_extra_exp_ratio()){
			output.wirteInt32(3, _extra_exp_ratio);
		}
		if (has_reduce_efficiency()){
			output.wirteInt32(4, _reduce_efficiency);
		}
		if (has_bless_of_blood_pledge()){
			output.writeBool(5, _bless_of_blood_pledge);
		}
		if (has_can_charge_count()){
			output.wirteInt32(6, _can_charge_count);
		}
		if (has_type()){
			output.writeEnum(7, _type.toInt());
		}
		if (has_bonus_exp_map()){
			output.wirteInt32(8, _bonus_exp_map);
		}
		if (has_default_ration_ex()){
			output.wirteInt32(9, _default_ration_ex);
		}
		if (has_event_exp_map()){
			output.wirteInt32(10, _event_exp_map);
		}
		if (has_einhasad_rest_gauge_bonus_exp()){
			output.wirteInt32(11, _einhasad_rest_gauge_bonus_exp);
		}
	}
	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = 
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.newInstance(getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try{
			writeTo(stream);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		return stream;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000008:{
					set_rest_gauge(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_default_ration(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_extra_exp_ratio(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_reduce_efficiency(input.readInt32());
					break;
				}
				case 0x00000028:{
					set_bless_of_blood_pledge(input.readBool());
					break;
				}
				case 0x00000030:{
					set_can_charge_count(input.readInt32());
					break;
				}
				case 0x00000038:{
					set_type(eEinhasadBonusType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000040:{
					set_bonus_exp_map(input.readInt32());
					break;
				}
				case 0x00000048:{
					set_default_ration_ex(input.readInt32());
					break;
				}
				case 0x00000050:{
					set_event_exp_map(input.readInt32());
					break;
				}
				case 0x00000058:{
					set_einhasad_rest_gauge_bonus_exp(input.readInt32());
					break;
				}
				default:{
					return this;
				}
			}
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try{
			readFrom(is);

			if (!isInitialized())
				return this;

			l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
			if (pc == null){
				return this;
			}

			// TODO : 아래부터 처리 코드를 삽입하십시오

		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new SC_REST_EXP_INFO_NOTI();
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
