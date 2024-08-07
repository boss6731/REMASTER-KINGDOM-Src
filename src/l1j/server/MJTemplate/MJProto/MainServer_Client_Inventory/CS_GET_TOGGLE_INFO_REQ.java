package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import java.sql.Timestamp;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class CS_GET_TOGGLE_INFO_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_GET_TOGGLE_INFO_REQ newInstance(){
		return new CS_GET_TOGGLE_INFO_REQ();
	}
	private eToggleInfoType _toggle_info_type;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_GET_TOGGLE_INFO_REQ(){
	}
	public eToggleInfoType get_toggle_info_type(){
		return _toggle_info_type;
	}
	public void set_toggle_info_type(eToggleInfoType val){
		_bit |= 0x1;
		_toggle_info_type = val;
	}
	public boolean has_toggle_info_type(){
		return (_bit & 0x1) == 0x1;
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
		if (has_toggle_info_type()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _toggle_info_type.toInt());
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_toggle_info_type()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_toggle_info_type()){
			output.writeEnum(1, _toggle_info_type.toInt());
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
					set_toggle_info_type(eToggleInfoType.fromInt(input.readEnum()));
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
			

			if (pc.ishalpaspaith()) {
				if (pc.checkHalpasTime()) {
					L1SkillUse.off_icons(pc, L1SkillId.DRAGON_ARMOR_EQUIP);
				} else {
					if(pc.is_halpas_armor()) {
						long endTime = Timestamp.valueOf(pc.getHalpasArmor().getHalpas_Time().toString()).getTime();
						long currentTime = System.currentTimeMillis();
						int remainTime = Long.valueOf((endTime-currentTime)/1000).intValue();
						if (remainTime >0) {
							L1SkillUse.off_icons(pc, L1SkillId.DRAGON_ARMOR_BLESSING);
						}
					}
				}
/*				if (pc.getInventory().checkEquipped(900263)) {
					L1ItemInstance item = pc.getInventory().getEquippedItem(900263);
					long endTime = Timestamp.valueOf(item.getHalpas_Time().toString()).getTime();	
					long currentTime = System.currentTimeMillis();
					int remainTime = Long.valueOf((endTime - currentTime) / 1000).intValue();
					if (remainTime < 0) {
						
					}
				} else if (pc.getInventory().checkEquipped(900264)) {
					L1ItemInstance item = pc.getInventory().getEquippedItem(900264);
					long endTime = Timestamp.valueOf(item.getHalpas_Time().toString()).getTime();	
					long currentTime = System.currentTimeMillis();
					int remainTime = Long.valueOf((endTime - currentTime) / 1000).intValue();
					if (remainTime < 0)  {
						L1SkillUse.off_icons(pc, L1SkillId.DRAGON_ARMOR_EQUIP);
					}
				} else if (pc.getInventory().checkEquipped(900265)) {
					L1ItemInstance item = pc.getInventory().getEquippedItem(900265);
					long endTime = Timestamp.valueOf(item.getHalpas_Time().toString()).getTime();	
					long currentTime = System.currentTimeMillis();
					int remainTime = Long.valueOf((endTime - currentTime) / 1000).intValue();
					if (remainTime < 0)  {
						L1SkillUse.off_icons(pc, L1SkillId.DRAGON_ARMOR_EQUIP);
					}
				} */
				
				pc.sethalpaspaith(false);
				pc.sendPackets(SC_GET_TOGGLE_INFO_ACK.make_stream(false));
			} else {
				if (pc.checkHalpasTime()) {
					L1SkillUse.on_icons(pc, L1SkillId.DRAGON_ARMOR_EQUIP, -1);
				} else {
					long endTime = Timestamp.valueOf(pc.getHalpasArmor().getHalpas_Time().toString()).getTime();
					long currentTime = System.currentTimeMillis();
					int remainTime = Long.valueOf((endTime-currentTime)/1000).intValue();
					L1SkillUse.on_icons(pc, L1SkillId.DRAGON_ARMOR_BLESSING, remainTime);
				}
				
/*				if (pc.getInventory().checkEquipped(900263)) {
					L1ItemInstance item = pc.getInventory().getEquippedItem(900263);
					long endTime = Timestamp.valueOf(item.getHalpas_Time().toString()).getTime();	
					long currentTime = System.currentTimeMillis();
					int remainTime = Long.valueOf((endTime - currentTime) / 1000).intValue();
					if (remainTime < 0) {
						L1SkillUse.on_icons(pc, L1SkillId.DRAGON_ARMOR_EQUIP, -1);
					}
				} else if (pc.getInventory().checkEquipped(900264)) {
					L1ItemInstance item = pc.getInventory().getEquippedItem(900264);
					long endTime = Timestamp.valueOf(item.getHalpas_Time().toString()).getTime();	
					long currentTime = System.currentTimeMillis();
					int remainTime = Long.valueOf((endTime - currentTime) / 1000).intValue();
					if (remainTime < 0)  {
						L1SkillUse.on_icons(pc, L1SkillId.DRAGON_ARMOR_EQUIP, -1);
					}
				} else if (pc.getInventory().checkEquipped(900265)) {
					L1ItemInstance item = pc.getInventory().getEquippedItem(900265);
					long endTime = Timestamp.valueOf(item.getHalpas_Time().toString()).getTime();	
					long currentTime = System.currentTimeMillis();
					int remainTime = Long.valueOf((endTime - currentTime) / 1000).intValue();
					if (remainTime < 0)  {
						L1SkillUse.on_icons(pc, L1SkillId.DRAGON_ARMOR_EQUIP, -1);
					}
				} */
				pc.sethalpaspaith(true);
				pc.sendPackets(SC_GET_TOGGLE_INFO_ACK.make_stream(true));
			}
			
/*			if(pc.ishalpaspaith()) {
				if (!pc.hasSkillEffect(L1SkillId.DRAGON_ARMOR_BLESSING)) {
					if (pc.getInventory().checkEquipped(900263) || pc.getInventory().checkEquipped(900264) || pc.getInventory().checkEquipped(900265)) {
						
						L1SkillUse.off_icons(pc, L1SkillId.DRAGON_ARMOR_EQUIP);
					}
				}
				pc.sethalpaspaith(false);
				pc.sendPackets(SC_GET_TOGGLE_INFO_ACK.make_stream(false));
			} else {
				if (pc.getInventory().checkEquipped(900263) || pc.getInventory().checkEquipped(900264) || pc.getInventory().checkEquipped(900265)) {
					if (!pc.hasSkillEffect(L1SkillId.DRAGON_ARMOR_BLESSING)){
						L1SkillUse.on_icons(pc, L1SkillId.DRAGON_ARMOR_EQUIP, -1);
					}
				}
				pc.sethalpaspaith(true);
				pc.sendPackets(SC_GET_TOGGLE_INFO_ACK.make_stream(true));
			}*/

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Nature.

		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new CS_GET_TOGGLE_INFO_REQ();
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
