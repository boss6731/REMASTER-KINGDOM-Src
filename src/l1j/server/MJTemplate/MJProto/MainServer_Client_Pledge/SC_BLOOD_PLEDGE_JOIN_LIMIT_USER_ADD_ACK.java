package l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.datatables.ClanBanListTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO：此為自動生成的 PROTO 代碼，由 Nature 製作。
public class SC_BLOOD_PLEDGE_JOIN_LIMIT_USER_ADD_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static void send(L1PcInstance pc, String name) {
/*    eRESULT_OK(0),
eRESULT_NO_USER(1),
eRESULT_MAX_USER(2),*/
		SC_BLOOD_PLEDGE_JOIN_LIMIT_USER_ADD_ACK ack = SC_BLOOD_PLEDGE_JOIN_LIMIT_USER_ADD_ACK.newInstance();
		if (L1World.getInstance().findPlayer(name) == null) {
			ack.set_result(eRESULT.eRESULT_NO_USER);
		} /*else if () { //需要確認是否達到最大用戶數量

}*/
		else if (L1World.getInstance().findPlayer(name).getClanid() == pc.getClanid()) {
			pc.sendPackets("無法阻擋同一公會成員。");
			ack.set_result(eRESULT.eRESULT_NO_USER);
		} else {
			ack.set_result(eRESULT.eRESULT_OK);
// 需要確認是否保存到 clan_ban 資料庫
			ClanBanListTable.getInstance().updateClanBanlist(pc.getClanname(), name);
		}
		pc.sendPackets(ack, MJEProtoMessages.SC_BLOOD_PLEDGE_JOIN_LIMIT_USER_ADD_ACK, true);
	}
}
	
	
	
	public static SC_BLOOD_PLEDGE_JOIN_LIMIT_USER_ADD_ACK newInstance(){
		return new SC_BLOOD_PLEDGE_JOIN_LIMIT_USER_ADD_ACK();
	}
	private SC_BLOOD_PLEDGE_JOIN_LIMIT_USER_ADD_ACK.eRESULT _result;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_BLOOD_PLEDGE_JOIN_LIMIT_USER_ADD_ACK(){
	}
	public SC_BLOOD_PLEDGE_JOIN_LIMIT_USER_ADD_ACK.eRESULT get_result(){
		return _result;
	}
	public void set_result(SC_BLOOD_PLEDGE_JOIN_LIMIT_USER_ADD_ACK.eRESULT val){
		_bit |= 0x1;
		_result = val;
	}
	public boolean has_result(){
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
		if (has_result()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _result.toInt());
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_result()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_result()){
			output.writeEnum(1, _result.toInt());
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
					set_result(SC_BLOOD_PLEDGE_JOIN_LIMIT_USER_ADD_ACK.eRESULT.fromInt(input.readEnum()));
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

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Nature.

		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new SC_BLOOD_PLEDGE_JOIN_LIMIT_USER_ADD_ACK();
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
public enum eRESULT {
	// 操作成功
	eRESULT_OK(0),
	// 無此用戶
	eRESULT_NO_USER(1),
	// 已達到最大用戶數量
	eRESULT_MAX_USER(2),

	;

	private int value;

	// 枚舉類型的構造函數，用於初始化枚舉值
	eRESULT(int val) {
		value = val;
	}

	// 返回枚舉值的整數表示
	public int toInt() {
		return value;
	}

	// 比較當前枚舉值與其他枚舉值是否相等
	public boolean equals(eRESULT v) {
		return value == v.value;
	}

	// 通過整數值返回對應的枚舉類型。如果輸入的整數值無效，則拋出 IllegalArgumentException
	public static eRESULT fromInt(int i) {
		switch (i) {
			case 0:
				return eRESULT_OK;
			case 1:
				return eRESULT_NO_USER;
			case 2:
				return eRESULT_MAX_USER;
			default:
				throw new IllegalArgumentException(String.format("無效的 eRESULT 參數: %d", i));
		}
	}
}
