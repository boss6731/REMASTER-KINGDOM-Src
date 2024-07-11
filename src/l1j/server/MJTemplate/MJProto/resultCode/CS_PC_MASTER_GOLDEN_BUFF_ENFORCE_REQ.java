package l1j.server.MJTemplate.MJProto.resultCode;

import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.resultCode.SC_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI.eUpdateReason;
import l1j.server.Pc_Golden_Buff.Pc_Golden_Buff_Info;
import l1j.server.Pc_Golden_Buff.Pc_Golden_Buff_Loader;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionBuffType;
import l1j.server.server.model.monitor.L1PcExpMonitor;

// TODO：自動產生 PROTO 程式碼。大自然創造.
public class CS_PC_MASTER_GOLDEN_BUFF_ENFORCE_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{

	public static CS_PC_MASTER_GOLDEN_BUFF_ENFORCE_REQ newInstance(){
		return new CS_PC_MASTER_GOLDEN_BUFF_ENFORCE_REQ();
	}
	private int _index;
	private int _group;
	private int _target_bonus;
	private int _target_grade;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_PC_MASTER_GOLDEN_BUFF_ENFORCE_REQ(){
	}
	public int get_index(){
		return _index;
	}
	public void set_index(int val){
		_bit |= 0x1;
		_index = val;
	}
	public boolean has_index(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_group(){
		return _group;
	}
	public void set_group(int val){
		_bit |= 0x2;
		_group = val;
	}
	public boolean has_group(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_target_bonus(){
		return _target_bonus;
	}
	public void set_target_bonus(int val){
		_bit |= 0x4;
		_target_bonus = val;
	}
	public boolean has_target_bonus(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_target_grade(){
		return _target_grade;
	}
	public void set_target_grade(int val){
		_bit |= 0x8;
		_target_grade = val;
	}
	public boolean has_target_grade(){
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
		if (has_index()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _index);
		}
		if (has_group()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _group);
		}
		if (has_target_bonus()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _target_bonus);
		}
		if (has_target_grade()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _target_grade);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_index()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_group()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_target_bonus()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_target_grade()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_index()){
			output.wirteInt32(1, _index);
		}
		if (has_group()){
			output.wirteInt32(2, _group);
		}
		if (has_target_bonus()){
			output.wirteInt32(3, _target_bonus);
		}
		if (has_target_grade()){
			output.wirteInt32(4, _target_grade);
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
					set_index(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_group(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_target_bonus(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_target_grade(input.readInt32());
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

			// TODO：從下面插入處理程式碼。大自然創造..
			int type = pc.getType();
			int bufftype = 0;
			switch(type){
			case 2:// 요정
				bufftype = 2;
				break;
			case 3:// 법사
			case 6:// 환술사
				bufftype = 3;
				break;
			default:
				bufftype = 1;
				break;
			}
			if (pc.getAccount().get_Index0_type(pc) == 0) {
				pc.getAccount().set_Index0_type(bufftype);
			}
			if (pc.getAccount().get_Index1_type(pc) == 0) {
				pc.getAccount().set_Index1_type(bufftype);
			}
			int buffid = 0;
			if (get_target_grade() == 1) {
				if (pc.getInventory().checkItem(41921, 5)) {
					pc.getInventory().consumeItem(41921, 5);
//					pc.getAccount().useTotalFeatherCount(5);
					if (get_target_grade() == 1) {
						if (pc.getInventory().checkItem(41921, 5)) {
							pc.getInventory().consumeItem(41921, 5);
							// pc.getAccount().useTotalFeatherCount(5);
						} else {
							pc.sendPackets("缺少精靈的金色羽毛。"); // 翻譯：픽시의 금빛 깃털이 모자랍니다.
							return this;
						}
					} else if (get_target_grade() == 2) {
						if (pc.getInventory().checkItem(41921, 10)) {
							pc.getInventory().consumeItem(41921, 10);
							// pc.getAccount().useTotalFeatherCount(10);
						} else {
							pc.sendPackets("缺少精靈的金色羽毛。"); // 翻譯：픽시의 금빛 깃털이 모자랍니다.
							return this;
						}
					} else if (get_target_grade() == 3) {
						if (pc.getInventory().checkItem(41921, 20)) {
							pc.getInventory().consumeItem(41921, 20);
							// pc.getAccount().useTotalFeatherCount(20);
						} else {
							pc.sendPackets("缺少精靈的金色羽毛。"); // 翻譯：픽시의 금빛 깃털이 모자랍니다.
							return this;
						}
					}
//			System.out.println(get_index()+"+"+get_target_bonus()+"+"+get_target_grade()+"+"+get_group());


			pc.getAccount().update_Index_value(get_index(), get_target_bonus()+1, get_target_grade());
			buffid = get_index() * 1000 + (get_target_bonus() + 1) * 100 + get_group() * 10 + get_target_grade();
//			System.out.println(buffid);
			Pc_Golden_Buff_Info info = Pc_Golden_Buff_Loader.getInstance().getBuffOption(buffid);
			
			if (get_index() == 0) {
				pc.getAccount().add_Index0_Time(info.get_Hours() * 60 * 60);
			} else if (get_index() == 1) {
				pc.getAccount().add_Index1_Time(info.get_Hours() * 60 * 60);	
			}
			
			ArrayList<Integer> MapList1 = new ArrayList<Integer>();
			int[] maplist1 = Config.ServerAdSetting.PC_GOLDEN_BUFF_MAP1;
			for (int i = 0; i < maplist1.length; i++) {
				MapList1.add(maplist1[i]);
			}
			ArrayList<Integer> MapList2 = new ArrayList<Integer>();
			int[] maplist2 = Config.ServerAdSetting.PC_GOLDEN_BUFF_MAP2;
			for (int i = 0; i < maplist2.length; i++) {
				MapList2.add(maplist2[i]);
			}
			int mapid = pc.getMapId();
			if(MapList1.contains(mapid) || MapList2.contains(mapid)) {
				ArrayList<Integer> bufflistdel = pc.getPcGoldenBuffList();
				if (bufflistdel.size() !=0 && !bufflistdel.isEmpty()) {
					for (int i = 0; i < bufflistdel.size() ;i++) {
						int index = bufflistdel.get(i);
						Pc_Golden_Buff_Info info1 = Pc_Golden_Buff_Loader.getInstance().getBuffOption(index);
						if (info1 != null) {
							Pc_Golden_Buff_Info.Pc_Golden_Buff_Option(pc, index, false);
						}
					}
				}
				pc.set_PcGoldenSstatus(false);
				L1PcExpMonitor.Pc_Golden_Buff_Enable_Check(pc);
			}
			
//			System.out.println("여기옴");
			SC_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI.send(pc, eUpdateReason.ENFORCE_ACK);
			

		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new CS_PC_MASTER_GOLDEN_BUFF_ENFORCE_REQ();
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
