package l1j.server.MJTemplate.MJProto.resultCode;

import l1j.server.server.clientpackets.C_ItemUSe;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_SystemMessage;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class CS_PC_MASTER_UTILITY_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_PC_MASTER_UTILITY_REQ newInstance(){
		return new CS_PC_MASTER_UTILITY_REQ();
	}
	private String _action;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_PC_MASTER_UTILITY_REQ(){
	}
	public String get_action(){
		return _action;
	}
	public void set_action(String val){
		_bit |= 0x1;
		_action = val;
	}
	public boolean has_action(){
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
		if (has_action()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(1, _action);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_action()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_action()){
			output.writeString(1, _action);
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
				case 0x0000000A:{
					set_action(input.readString());
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
//			System.out.println(get_action());
			String s = get_action();
// TODO : 在下面插入處理代碼。由 Nature 製作。
			if (s.equalsIgnoreCase("a_giant") || s.equalsIgnoreCase("a_soul_center")) {
				if (!pc.getMap().isTeleportable()) {
					if (!pc.getMap().isRuler()) {
						return this;
					} else {
						if (pc.getInventory().checkItem(900111)) {
							// 這裡可以加入進一步處理邏輯
						}
					}
				}
			}
			if (s.startsWith("a_giant")) {
//    System.out.println("패킷확인"); // 翻譯：檢查數據包
				pc.start_teleport(32767, 32880, 624, pc.getHeading(), 18339, true);
			} else if (s.equalsIgnoreCase("a_soul_center")) {
				pc.start_teleport(32797, 32917, 430, pc.getHeading(), 18339, true);
			} else if (s.equalsIgnoreCase("buff_enchant")) {
				// 這裡可以加入 buff_enchant 的處理邏輯
			}
				if (pc.isPcBuff()) {
					if (pc.getInventory().checkItem(41921, 2)) {
						pc.getInventory().consumeItem(41921, 2);
//						pc.getAccount().useTotalFeatherCount(2);
						int[] allBuffSkill = { 26, 37, 42, 48 };
						pc.setBuffnoch(1);
						L1SkillUse l1skilluse = new L1SkillUse();
						for (int i = 0; i < allBuffSkill.length; i++) {
							l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 120*60, L1SkillUse.TYPE_GMBUFF);
						}
						pc.setBuffnoch(0);
						
						if (pc.hasSkillEffect(L1SkillId.PC_EXP_UP)) {
							pc.killSkillEffectTimer(L1SkillId.PC_EXP_UP);
//							System.out.println(pc.get_item_exp_bonus());
						}
						pc.setSkillEffect(L1SkillId.PC_EXP_UP, 120* 60* 1000);
						L1SkillUse.on_icons(pc, L1SkillId.PC_EXP_UP, 120 * 60);


					} else {
						pc.sendPackets(new S_SystemMessage("缺少精靈的金色羽毛。")); // 翻譯：픽시의 금빛 깃털이 부족합니다。
					}
				}
		} else if (s.equalsIgnoreCase("buff_acceleration")) { // 1,3階段，持續1小時
			if (pc.getInventory().checkItem(41921, 10)) {
				pc.getInventory().consumeItem(41921, 10);
// pc.getAccount().useTotalFeatherCount(10);
				C_ItemUSe.useGreenPotion(pc, 41921); // 使用綠色藥水，增加速度
				C_ItemUSe.useDragonPearl(pc, 41921); // 使用龍之珍珠，增加速度

			} else {
				pc.sendPackets(new S_SystemMessage("缺少精靈的金色羽毛。")); // 翻譯：픽시의 금빛 깃털이 부족합니다。
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
return this;
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new CS_PC_MASTER_UTILITY_REQ();
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
