package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_BMTYPE_DEL_CHECK_ACK.eResult;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharService;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharSimpleInfo;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_BMTYPE_DEL_CHECK_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_BMTYPE_DEL_CHECK_REQ newInstance(){
		return new CS_BMTYPE_DEL_CHECK_REQ();
	}
	private byte[] _char_name;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_BMTYPE_DEL_CHECK_REQ(){
	}
	public byte[] get_char_name(){
		return _char_name;
	}
	public void set_char_name(byte[] val){
		_bit |= 0x1;
		_char_name = val;
	}
	public boolean has_char_name(){
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
		if (has_char_name()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(1, _char_name);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_char_name()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_char_name()){
			output.writeBytes(1, _char_name);
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
					set_char_name(input.readBytes());
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

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.
			String characterName = new String(_char_name, MJEncoding.MS949);
			L1PcInstance pc = L1World.getInstance().getPlayer(characterName);
			SC_BMTYPE_DEL_CHECK_ACK ack = SC_BMTYPE_DEL_CHECK_ACK.newInstance();
			if(pc != null) {
				ack.set_result(eResult.InvalidPacket);				
			}else {
				MJMyCharSimpleInfo sInfo = MJMyCharService.service().fromCharacterName(characterName);
				if(sInfo == null) {
					ack.set_result(eResult.InvalidPacket);				
				}else {
					ack.set_result(hasEinhasadPrimium(sInfo.objectId()) ? eResult.BMTypeFail : eResult.Success);
				}
			}
			clnt.sendPacket(ack, MJEProtoMessages.SC_BMTYPE_DEL_CHECK_ACK.toInt());

		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	
	private boolean hasEinhasadPrimium(final int characterId) {
		MJObjectWrapper<Boolean> wrapper = new MJObjectWrapper<>();
		wrapper.value = false;
		Selector.exec("select * from character_buff where char_obj_id=? and (skill_id=? or skill_id=?) ", new SelectorHandler() {

			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, characterId);
				pstm.setInt(2, L1SkillId.EINHASAD_GREAT_FLAT);
				pstm.setInt(3, L1SkillId.EINHASAD_PRIMIUM_FLAT);
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				wrapper.value = rs.next();
			}
		});
		return wrapper.value;
	}
	
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new CS_BMTYPE_DEL_CHECK_REQ();
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
