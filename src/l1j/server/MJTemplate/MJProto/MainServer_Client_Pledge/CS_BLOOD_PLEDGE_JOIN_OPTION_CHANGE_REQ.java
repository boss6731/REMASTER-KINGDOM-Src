package l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge;

import MJShiftObject.Battle.MJShiftBattlePlayManager;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_BLOOD_PLEDGE_JOIN_OPTION_CHANGE_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_BLOOD_PLEDGE_JOIN_OPTION_CHANGE_REQ newInstance(){
		return new CS_BLOOD_PLEDGE_JOIN_OPTION_CHANGE_REQ();
	}
	private boolean _enable_join;
	private ePLEDGE_JOIN_REQ_TYPE _join_type;
	private byte[] _hashed_password;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_BLOOD_PLEDGE_JOIN_OPTION_CHANGE_REQ(){
	}
	public boolean get_enable_join(){
		return _enable_join;
	}
	public void set_enable_join(boolean val){
		_bit |= 0x1;
		_enable_join = val;
	}
	public boolean has_enable_join(){
		return (_bit & 0x1) == 0x1;
	}
	public ePLEDGE_JOIN_REQ_TYPE get_join_type(){
		return _join_type;
	}
	public void set_join_type(ePLEDGE_JOIN_REQ_TYPE val){
		_bit |= 0x2;
		_join_type = val;
	}
	public boolean has_join_type(){
		return (_bit & 0x2) == 0x2;
	}
	public byte[] get_hashed_password(){
		return _hashed_password;
	}
	public void set_hashed_password(byte[] val){
		_bit |= 0x4;
		_hashed_password = val;
	}
	public boolean has_hashed_password(){
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
		if (has_enable_join()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(1, _enable_join);
		}
		if (has_join_type()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _join_type.toInt());
		}
		if (has_hashed_password()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(3, _hashed_password);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_enable_join()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_join_type()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_enable_join()){
			output.writeBool(1, _enable_join);
		}
		if (has_join_type()){
			output.writeEnum(2, _join_type.toInt());
		}
		if (has_hashed_password()){
			output.writeBytes(3, _hashed_password);
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
					set_enable_join(input.readBool());
					break;
				}
				case 0x00000010:{
					set_join_type(ePLEDGE_JOIN_REQ_TYPE.fromInt(input.readEnum()));
					break;
				}
				case 0x0000001A:{
					set_hashed_password(input.readBytes());
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

			// 如果 _hashed_password 為空，則設置為空字節數組
			if (_hashed_password == null) {
				this.set_hashed_password("".getBytes());
			}

			// 檢查對象是否已初始化
			if (!isInitialized()) {
				return this;
			}

			// 獲取玩家實例
			l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
			if (pc == null) {
				return this;
			}

			// TODO: 在此處插入處理代碼。由 MJSoft 製作。

			// 檢查玩家是否沒有血盟或不是王族且血盟等級不是守護級別
			if (pc.getClanid() == 0 || (!pc.isCrown() && pc.getClanRank() != L1Clan.수호)) {
				return this;
			}

			// 檢查玩家是否在戰鬥中
			if (MJShiftBattlePlayManager.is_shift_battle(pc)) {
				return this;
			}

			/** 2016.11.25 MJ 앱센터 혈맹 **/

			// 檢查加入類型是否為 2（假設 2 是不允許的類型）
			if (get_join_type().toInt() == 2) {
				pc.sendPackets("目前無法設置密碼加入。");
				pc.sendPackets(SC_BLOOD_PLEDGE_JOIN_OPTION_ACK.sendJoinSetting(pc, pc.getClan().getJoinSetting(), pc.getClan().getJoinType()));
				return this;
        /*String code = new String(get_hashed_password());
        pc.getClan().setJoinPassword(code);
        ClanTable.getInstance().updateClanPassword(pc.getClan());*/
			}

			// 設置血盟的加入設置和加入類型並更新至數據庫
			pc.getClan().setJoinSetting(get_enable_join() ? 1 : 0);
			pc.getClan().setJoinType(get_join_type().toInt());
			ClanTable.getInstance().updateClan(pc.getClan());

			// 向玩家發送消息 3980
			pc.sendPackets(3980);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new CS_BLOOD_PLEDGE_JOIN_OPTION_CHANGE_REQ();
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
