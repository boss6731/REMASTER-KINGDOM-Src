package l1j.server.MJTemplate.MJProto.MainServer_Client_Potential;

import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.PotentialBonusT;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_ENCHANT_POTENTIAL_RESULT_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void potenccial_send(L1PcInstance pc, int target_id, int doll_floor, int bonus_type, int bonus_desc, int cur_bonus) {
		SC_ENCHANT_POTENTIAL_RESULT_NOTI noti = SC_ENCHANT_POTENTIAL_RESULT_NOTI.newInstance();

		noti.set_target_id(target_id);
		noti.set_bonus_grade(doll_floor);
		// TODO 단계별 옵션 랜덤 슬롯
//		System.out.println();
		if (doll_floor == 6) {
			int bonus_count = 0;
			if (bonus_desc >= 1 && bonus_desc <= 19) {
				bonus_count = MJRnd.next(1, 2);
			} else if (bonus_desc >= 20 && bonus_desc <= 52) {
				bonus_count = MJRnd.next(1, 3);
			} else if (bonus_desc >= 53 && bonus_desc <= 93) {
				bonus_count = MJRnd.next(1, 5);
			} else if (bonus_desc >= 94 && bonus_desc <= 131 || bonus_desc >= 148 && bonus_desc <= 154) {
				bonus_count = MJRnd.next(1, 7);
			} else if (bonus_desc >= 132 && bonus_desc <= 143 || bonus_desc >= 155 && bonus_desc <= 167) {
				bonus_count = 10;
			}
			// TODO 단계별 옵션 랜덤 슬롯에 대해 표기값 설정
			for (int i = 0; i < bonus_count; i++) {
				int special_bonus_value = 0;
				if (bonus_desc >= 1 && bonus_desc <= 19) {
					special_bonus_value = MJRnd.next(1, 19);
					bonus_type = 1;
				} else if (bonus_desc >= 20 && bonus_desc <= 52) {
					special_bonus_value = MJRnd.next(20, 52);
					bonus_type = 2;
				} else if (bonus_desc >= 53 && bonus_desc <= 93) {
					special_bonus_value = MJRnd.next(53, 93);
					bonus_type = 3;
				} else if (bonus_desc >= 94 && bonus_desc <= 131 || bonus_desc >= 148 && bonus_desc <= 154) {
					int rand1 = MJRnd.next(1,2);
					if (rand1 == 1) {
						special_bonus_value = MJRnd.next(94, 131);	
					} else {
						special_bonus_value = MJRnd.next(148, 154);
					}
					bonus_type = 4;
				} else if (bonus_desc >= 132 && bonus_desc <= 146 || bonus_desc >= 155 && bonus_desc <= 163 ) {
					int ran2 = MJRnd.next(1,2);
					if (ran2 == 1) {
						special_bonus_value = MJRnd.next(132, 146);	
					} else {
						special_bonus_value = MJRnd.next(155, 163);
					}
					bonus_type = 5;
				}

				if (special_bonus_value == 134)
					special_bonus_value--;
				else if (special_bonus_value == 135)
					special_bonus_value++;
				
				PotentialBonusT potencial = PotentialBonusT.newInstance();
				if (bonus_desc >=144 && bonus_desc <= 163 ) {
					switch (bonus_desc) {
						case 148:
						case 149:
						case 150:
						case 151:
						case 152:
						case 153:
						case 154:
							bonus_type = 4;
							break;
						case 144:
						case 145:
						case 146:
						case 155:
						case 156:
						case 157:
						case 158:
						case 159:
						case 160:
						case 161:
						case 162:
						case 163:
							bonus_type = 5;
							break;
						default:
							break;
						
					}
				}
				/*				if (bonus_desc == 147) {
					bonus_type = 4;
				} else if (bonus_desc >= 144 && bonus_desc <= 146) {
					bonus_type = 5;
				}*/
				potencial.set_bonus_grade(bonus_type);// 1:흰색2:초록3:파랑4:빨강5:보라
				potencial.set_bonus_id(special_bonus_value);// 03 // 보너스 아이디
//				if (special_bonus_value >=144 && special_bonus_value <= 163 ) {
					String bonusdesc = Integer.toString(dool_desc(bonus_desc));
//					System.out.println("6단인형 잠재력 "+bonus_desc);
					potencial.set_bonus_desc(dool_desc(special_bonus_value));// 보너스 데스크
					potencial.set_bonus_value(null); // 현재 안씀 null
//					potencial.set_bonus_desc_str(null); // 현재 안씀 null
					potencial.set_bonus_desc_str(bonusdesc.getBytes()); // 현재 안씀 null
				/*} else {
					potencial.set_bonus_desc(dool_desc(special_bonus_value));// 보너스 데스크	
					potencial.set_bonus_value(null); // 현재 안씀 null
					potencial.set_bonus_desc_str(null); // 현재 안씀 null
				}*/
				

				noti.add_select_bonuslist(potencial);
				pc.doll_update_option[0] = 0;
				pc.doll_update_option[1] = 0;
			}
		} else {
			PotentialBonusT potencial = PotentialBonusT.newInstance();
			potencial.set_bonus_grade(bonus_type);// 1:흰색2:초록3:파랑4:빨강5:보라
			potencial.set_bonus_id(bonus_desc);// 03 // 보너스 아이디
//			if (bonus_desc >=144 && bonus_desc <= 163 ) {
				String bonusdesc = Integer.toString(dool_desc(bonus_desc));
				potencial.set_bonus_desc(dool_desc(bonus_desc));// 보너스 데스크
				potencial.set_bonus_value(null); // 현재 안씀 null
				potencial.set_bonus_desc_str(bonusdesc.getBytes()); // 현재 안씀 null
			/*} else {
				potencial.set_bonus_desc(dool_desc(bonus_desc));// 보너스 데스크
				potencial.set_bonus_value(null); // 현재 안씀 null
				potencial.set_bonus_desc_str(null); // 현재 안씀 null
			}*/
			
			noti.add_select_bonuslist(potencial);
			pc.doll_update_option[0] = bonus_type;
			pc.doll_update_option[1] = bonus_desc;
		}
		noti.set_curbonus_desc(dool_desc(cur_bonus)); // 기존 잠재력 데스크.
		pc.sendPackets(noti, MJEProtoMessages.SC_ENCHANT_POTENTIAL_RESULT_NOTI, true);
	}
	
	private static int dool_desc(int desc) {
		int special_bonus_value = 0;
		if (desc >=144 && desc <= 163 ) {
			switch (desc) {
			case 144:
				special_bonus_value = 37865;
				break;
			case 145:
				special_bonus_value = 37866;
				break;
			case 146:
				special_bonus_value = 33913;
				break;
			case 148:
				special_bonus_value = 40575;
				break;
			case 149:
				special_bonus_value = 40576;
				break;
			case 150:
				special_bonus_value = 40577;
				break;
			case 151:
				special_bonus_value = 40578;
				break;
			case 152:
				special_bonus_value = 40579;
				break;
			case 153:
				special_bonus_value = 40580;
				break;
			case 154:
				special_bonus_value = 40581;
				break;
			case 155:
				special_bonus_value = 40585;
				break;
			case 156:
				special_bonus_value = 40586;
				break;
			case 157:
				special_bonus_value = 40587;
				break;
			case 158:
				special_bonus_value = 40588;
				break;
			case 159:
				special_bonus_value = 40589;
				break;
			case 160:
				special_bonus_value = 40590;
				break;
			case 161:
				special_bonus_value = 40591;
				break;
			case 162:
				special_bonus_value = 40592;
				break;
			case 163:
				special_bonus_value = 40593;
				break;
				
			default:
				break;
				
			}
		} else {
			special_bonus_value = desc+32065;
		}
		return special_bonus_value;
	}
	
	
	public static SC_ENCHANT_POTENTIAL_RESULT_NOTI newInstance(){
		return new SC_ENCHANT_POTENTIAL_RESULT_NOTI();
	}
	private int _target_id;
	private int _bonus_grade;
	private java.util.LinkedList<PotentialBonusT> _select_bonuslist;
	private int _curbonus_desc;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_ENCHANT_POTENTIAL_RESULT_NOTI(){
	}
	public int get_target_id(){
		return _target_id;
	}
	public void set_target_id(int val){
		_bit |= 0x1;
		_target_id = val;
	}
	public boolean has_target_id(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_bonus_grade(){
		return _bonus_grade;
	}
	public void set_bonus_grade(int val){
		_bit |= 0x2;
		_bonus_grade = val;
	}
	public boolean has_bonus_grade(){
		return (_bit & 0x2) == 0x2;
	}
	public java.util.LinkedList<PotentialBonusT> get_select_bonuslist(){
		return _select_bonuslist;
	}
	public void add_select_bonuslist(PotentialBonusT val){
		if(!has_select_bonuslist()){
			_select_bonuslist = new java.util.LinkedList<PotentialBonusT>();
			_bit |= 0x4;
		}
		_select_bonuslist.add(val);
	}
	public boolean has_select_bonuslist(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_curbonus_desc(){
		return _curbonus_desc;
	}
	public void set_curbonus_desc(int val){
		_bit |= 0x8;
		_curbonus_desc = val;
	}
	public boolean has_curbonus_desc(){
		return (_bit & 0x8) == 0x8;
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
		if (has_target_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _target_id);
		}
		if (has_bonus_grade()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _bonus_grade);
		}
		if (has_select_bonuslist()){
			for(PotentialBonusT val : _select_bonuslist){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
			}
		}
		if (has_curbonus_desc()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _curbonus_desc);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_target_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_bonus_grade()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_select_bonuslist()){
			for(PotentialBonusT val : _select_bonuslist){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_target_id()){
			output.wirteInt32(1, _target_id);
		}
		if (has_bonus_grade()){
			output.wirteInt32(2, _bonus_grade);
		}
		if (has_select_bonuslist()){
			for (PotentialBonusT val : _select_bonuslist){
				output.writeMessage(3, val);
			}
		}
		if (has_curbonus_desc()){
			output.wirteInt32(4, _curbonus_desc);
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
					set_target_id(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_bonus_grade(input.readInt32());
					break;
				}
				case 0x0000001A:{
					add_select_bonuslist((PotentialBonusT)input.readMessage(PotentialBonusT.newInstance()));
					break;
				}
				case 0x00000020:{
					set_curbonus_desc(input.readInt32());
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

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new SC_ENCHANT_POTENTIAL_RESULT_NOTI();
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
