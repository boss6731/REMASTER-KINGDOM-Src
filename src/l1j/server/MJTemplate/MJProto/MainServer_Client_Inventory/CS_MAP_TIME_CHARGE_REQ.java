package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.MJDungeonTimer.DungeonTimeInformation;
import l1j.server.MJDungeonTimer.DungeonTimePotion;
import l1j.server.MJDungeonTimer.Loader.DungeonTimeInformationLoader;
import l1j.server.MJDungeonTimer.Loader.DungeonTimePotionLoader;
import l1j.server.MJDungeonTimer.Progress.DungeonTimeProgress;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class CS_MAP_TIME_CHARGE_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_MAP_TIME_CHARGE_REQ newInstance(){
		return new CS_MAP_TIME_CHARGE_REQ();
	}
	private int _used_item_id;
	private int _group;
	private int _charge_count;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_MAP_TIME_CHARGE_REQ(){
	}
	public int get_used_item_id(){
		return _used_item_id;
	}
	public void set_used_item_id(int val){
		_bit |= 0x1;
		_used_item_id = val;
	}
	public boolean has_used_item_id(){
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
	public int get_charge_count(){
		return _charge_count;
	}
	public void set_charge_count(int val){
		_bit |= 0x4;
		_charge_count = val;
	}
	public boolean has_charge_count(){
		return (_bit & 0x4) == 0x4;
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
		if (has_used_item_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _used_item_id);
		}
		if (has_group()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _group);
		}
		if (has_charge_count()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _charge_count);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_used_item_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_group()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_charge_count()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_used_item_id()){
			output.wirteInt32(1, _used_item_id);
		}
		if (has_group()){
			output.wirteInt32(2, _group);
		}
		if (has_charge_count()){
			output.wirteInt32(3, _charge_count);
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
					set_used_item_id(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_group(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_charge_count(input.readInt32());
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
			L1PcInstance pc = clnt.getActiveChar();
			
			if (pc == null){
				return this;
			}
			int itemid = get_used_item_id();
			int group = get_group();
			int count = get_charge_count();
			int remain_time = 0;
			int time = 0;
			L1ItemInstance[] item = pc.getInventory().findItemsId(itemid, true);
			L1ItemInstance keyitem ;


//            System.out.println(item.length);
			if (item == null || item.length == 0) {
				return this;
			}
			if (item.length > 1) {
				System.out.println("隱藏的次元沙漏使用錯誤 (角色ID: " + pc.getName() + " 使用物品 Desc_Id: " + itemid);
				return this;
			}
			DungeonTimePotion potion = DungeonTimePotionLoader.getInstance().get_potion(item[0].getItemId());
			if (potion == null) {
				pc.sendPackets("此物品已被管理員禁用。");
				return this;
			}

			time = potion.get_timer_time();

			if (group == 1) { //一般沙漏
				DungeonTimeInformation dtInfo = DungeonTimeInformationLoader.getInstance().from_timer_id(15);
				if (dtInfo == null) {
					pc.sendPackets("此物品已被管理員禁用。");
					return this;
				}
				DungeonTimeProgress<?> progress = pc.get_progress(dtInfo);

				if (progress == null) {
					pc.sendPackets(String.format("在使用充電石之前，您必須至少進入一次副本 (%s)。", dtInfo.get_description()));
					return this;
				}
			}
				remain_time = progress.get_remain_seconds() + get_charge_count() * time;
				int charged_count = progress.get_charge_count();
				progress.set_charge_count(charged_count + get_charge_count());
				progress.set_remain_seconds(remain_time);
				pc.getInventory().consumeItem(item[0].getItemId(), count);

		} else if (group == 3) { // 隱藏沙漏增益
			DungeonTimeInformation dtInfo = DungeonTimeInformationLoader.getInstance().from_timer_id(23);
			if (dtInfo == null) {
				pc.sendPackets("此物品已被管理員禁用。");
				return this;
			}
			DungeonTimeProgress<?> progress = pc.get_progress(dtInfo);
			if (progress == null) {
				pc.sendPackets(String.format("在使用充電石之前，您必須至少進入一次副本 (%s)。", dtInfo.get_description()));
				return this;
			}
			remain_time = progress.get_remain_seconds() + get_charge_count() * time;
			int charged_count = progress.get_charge_count();
			progress.set_charge_count(charged_count + get_charge_count());
			progress.set_remain_seconds(remain_time);
			pc.getInventory().consumeItem(item[0].getItemId(), count * 4);

		}
			
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Nature.
/*			System.out.println("itemid :"+get_used_item_id());
			System.out.println("group :"+get_group());
			System.out.println("charge :"+get_charge_count());*/
			
			
			
			
			
			
			


		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new CS_MAP_TIME_CHARGE_REQ();
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
