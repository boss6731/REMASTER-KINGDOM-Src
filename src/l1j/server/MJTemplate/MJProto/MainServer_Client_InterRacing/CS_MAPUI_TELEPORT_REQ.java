package l1j.server.MJTemplate.MJProto.MainServer_Client_InterRacing;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.datatables.InterRaceZoneTable;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public class CS_MAPUI_TELEPORT_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_MAPUI_TELEPORT_REQ newInstance(){
		return new CS_MAPUI_TELEPORT_REQ();
	}
	private int _worldNumber;
	private int _x;
	private int _y;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_MAPUI_TELEPORT_REQ(){
	}
	public int get_worldNumber(){
		return _worldNumber;
	}
	public void set_worldNumber(int val){
		_bit |= 0x1;
		_worldNumber = val;
	}
	public boolean has_worldNumber(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_x(){
		return _x;
	}
	public void set_x(int val){
		_bit |= 0x2;
		_x = val;
	}
	public boolean has_x(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_y(){
		return _y;
	}
	public void set_y(int val){
		_bit |= 0x4;
		_y = val;
	}
	public boolean has_y(){
		return (_bit & 0x4) == 0x4;
	}
	@Override
	public long getInitializeBit(){
		return (long)_bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_worldNumber())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _worldNumber);
		if (has_x())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _x);
		if (has_y())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _y);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_worldNumber()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_x()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_y()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_worldNumber()){
			output.wirteInt32(1, _worldNumber);
		}
		if (has_x()){
			output.wirteInt32(2, _x);
		}
		if (has_y()){
			output.wirteInt32(3, _y);
		}
	}
	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream =
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try{
			writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}
	@Override
	public MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				default:{
					return this;
				}
				case 0x00000008:{
					set_worldNumber(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_x(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_y(input.readInt32());
					break;
				}
			}
		}
		return this;
	}
	
	public static boolean isInterRacingZone(int locX, int locY, int mapId) {
		String key = new StringBuilder().append(mapId).append(locX).append(locY).toString();
		if (InterRaceZoneTable.getInstance().isLockey(key)) {
			return true;
		}
		return false;
	}
	
	@Override
	public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try{
			readFrom(is);

			if (!isInitialized())
				return this;
			
			L1PcInstance pc = clnt.getActiveChar();
			if(pc == null)
				return this;
			
			if (isInterRacingZone(_x, _y, _worldNumber)) {
				pc.start_teleport(_x, _y, _worldNumber, pc.getHeading(), 18339, true);
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public MJIProtoMessage copyInstance(){
		return new CS_MAPUI_TELEPORT_REQ();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
