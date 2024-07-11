package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.Config;
import l1j.server.server.model.item.collection.favor.construct.L1FavorBookListType;
import l1j.server.server.serverpackets.ServerBasePacket;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class CS_FAVOR_BOOK_LIST_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_FAVOR_BOOK_LIST_REQ newInstance(){
		return new CS_FAVOR_BOOK_LIST_REQ();
	}
	private CS_FAVOR_BOOK_LIST_REQ.eReqType _type;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_FAVOR_BOOK_LIST_REQ(){
	}
	public CS_FAVOR_BOOK_LIST_REQ.eReqType get_type(){
		return _type;
	}
	public void set_type(CS_FAVOR_BOOK_LIST_REQ.eReqType val){
		_bit |= 0x1;
		_type = val;
	}
	public boolean has_type(){
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
		if (has_type()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _type.toInt());
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_type()){
			output.writeEnum(1, _type.toInt());
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
					set_type(CS_FAVOR_BOOK_LIST_REQ.eReqType.fromInt(input.readEnum()));
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
			
//			System.out.println("패킷들어오나");

			if (!Config.ServerAdSetting.FavorSystem || pc == null || pc.isGhost() || pc.getFavorBook() == null) {
				return this;
			}

			if (_type == null){
				return this;
			}
//			System.out.println(_type);
//			parsePacket();
//			if (!isValidation()) {
//				return this;
//			}
			
			L1FavorBookListType listType = L1FavorBookListType.getListType(_type.toInt());
			ServerBasePacket pck = pc.getFavorBook().getListPacket(listType);// 생성되어 있는 패킷
//			ServerBasePacket pck1 = _type;// 생성되어 있는 패킷
			
			if (_type == null) {
				System.out.println(String.format(
						"[A_FavorBookUI] 封包未找到：類型(%s)，名稱(%s)",
						listType.getName(), pc.getName()));
				return this;
			}
			pc.sendPackets(pck);
			

		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new CS_FAVOR_BOOK_LIST_REQ();
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
	public enum eReqType{
		ALL(0),
		PERMANENT(1),
		EVENT(2),
		;
		private int value;
		eReqType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eReqType v){
			return value == v.value;
		}
		public static eReqType fromInt(int i){
			switch(i){
			case 0:
				return ALL;
			case 1:
				return PERMANENT;
			case 2:
				return EVENT;
			default:
				throw new IllegalArgumentException(String.format("無效的參數 eReqType, %d", i));
			}
		}
	}
}
