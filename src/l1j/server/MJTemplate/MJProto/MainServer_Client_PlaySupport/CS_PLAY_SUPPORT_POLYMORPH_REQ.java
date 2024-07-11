package l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport;

import static l1j.server.server.model.skill.L1SkillId.SHAPE_CHANGE;
import l1j.server.Config;
import l1j.server.MJRankSystem.Loader.MJRankUserLoader;
import l1j.server.server.clientpackets.C_ItemUSe;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class CS_PLAY_SUPPORT_POLYMORPH_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_PLAY_SUPPORT_POLYMORPH_REQ newInstance(){
		return new CS_PLAY_SUPPORT_POLYMORPH_REQ();
	}
	private String _monster_name;
	private java.util.LinkedList<Integer> _polymorph_item_ids;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	

	private CS_PLAY_SUPPORT_POLYMORPH_REQ(){
	}
	public String get_monster_name(){
		return _monster_name;
	}
	public void set_monster_name(String val){
		_bit |= 0x1;
		_monster_name = val;
	}
	public boolean has_monster_name(){
		return (_bit & 0x1) == 0x1;
	}
	public java.util.LinkedList<Integer> get_polymorph_item_ids(){
		return _polymorph_item_ids;
	}
	public void add_polymorph_item_ids(int val){
		if(!has_polymorph_item_ids()){
			_polymorph_item_ids = new java.util.LinkedList<Integer>();
			_bit |= 0x2;
		}
		_polymorph_item_ids.add(val);
	}
	public boolean has_polymorph_item_ids(){
		return (_bit & 0x2) == 0x2;
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
		if (has_monster_name()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(1, _monster_name);
		}
		if (has_polymorph_item_ids()){
			for(int val : _polymorph_item_ids){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_monster_name()){
			_memorizedIsInitialized = -1;
			return false;
		}
//		if (has_polymorph_item_ids()){
//			for(int val : _polymorph_item_ids){
//				if (!val.isInitialized()){
//					_memorizedIsInitialized = -1;
//					return false;
//				}
//			}
//		}
		if (!has_polymorph_item_ids()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_monster_name()){
			output.writeString(1, _monster_name);
		}
		if (has_polymorph_item_ids()){
			for (int val : _polymorph_item_ids){
				output.wirteInt32(2, val);
			}
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
					set_monster_name(input.readString());
					break;
				}
				case 0x00000010:{
					add_polymorph_item_ids(input.readInt32());
					break;
				}
				default:{
					return this;
				}
			}
		}
		return this;
	}
	@override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
		// 創建 ProtoInputStream 實例，用於從字節數組中讀取數據
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(
				bytes,
				l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
				((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE
		);

		try {
			// 從流中讀取數據
			readFrom(is);

			// 檢查對象是否已初始化
			if (!isInitialized()) {
				return this;
			}

			// 獲取玩家實例
			l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
			if (pc == null) {
				return this;
			}

			// TODO: 在此處插入處理代碼。由 Nature 製作。
			int itemid = 0;
			if (pc.getInventory().checkItem(4100500)) { // 100렙 變身 지배 반지 (100級變身支配戒指)
				itemid = 4100500;
			} else if (pc.getInventory().checkItem(4100610)) { // 變신 지배 반지 (變身支配戒指)
				itemid = 4100610;
			} else if (pc.getInventory().checkItem(40088)) { // 變신 주문서 (變身卷軸)
				itemid = 40088;
				// 消耗一張變身卷軸
				pc.getInventory().consumeItem(40088, 1);
			}

			// 使用變身卷軸
			C_ItemUSe.usePolyScroll(pc, itemid, _monster_name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		// 返回一個新的 CS_PLAY_SUPPORT_POLYMORPH_REQ 實例
		return new CS_PLAY_SUPPORT_POLYMORPH_REQ();
	}

	@override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
		// 調用 newInstance 方法，返回一個新的 CS_PLAY_SUPPORT_POLYMORPH_REQ 實例
		return newInstance();
	}

	@override
	public void dispose() {
		// 重置字段值
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
