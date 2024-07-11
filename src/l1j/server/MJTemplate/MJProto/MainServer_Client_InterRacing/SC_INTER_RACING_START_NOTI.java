package l1j.server.MJTemplate.MJProto.MainServer_Client_InterRacing;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.RaceInfoT;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.RacerInfoT;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public class SC_INTER_RACING_START_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{


	public static SC_INTER_RACING_START_NOTI newInstance(){
		return new SC_INTER_RACING_START_NOTI();
	}
	private RaceInfoT _race;
	private java.util.LinkedList<RacerInfoT> _racer;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_INTER_RACING_START_NOTI(){
	}
	public RaceInfoT get_race(){
		return _race;
	}
	public void set_race(RaceInfoT val){
		_bit |= 0x1;
		_race = val;
	}
	public boolean has_race(){
		return (_bit & 0x1) == 0x1;
	}
	public java.util.LinkedList<RacerInfoT> get_racer(){
		return _racer;
	}
	public void add_racer(RacerInfoT val){
		if(!has_racer()){
			_racer = new java.util.LinkedList<RacerInfoT>();
			_bit |= 0x2;
		}
		_racer.add(val);
	}
	public boolean has_racer(){
		return (_bit & 0x2) == 0x2;
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
		if (has_race())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, _race);
		if (has_racer()){
			for(RacerInfoT val : _racer)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_race()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_racer()){
			for(RacerInfoT val : _racer){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_race()){
			output.writeMessage(1, _race);
		}
		if (has_racer()){
			for(RacerInfoT val : _racer){
				output.writeMessage(2, val);
			}
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
				case 0x0000000A:{
					set_race((RaceInfoT)input.readMessage(RaceInfoT.newInstance()));
					break;
				}
				case 0x00000012:{
					add_racer((RacerInfoT)input.readMessage(RacerInfoT.newInstance()));
					break;
				}
			}
		}
		return this;
	}
	@Override
	public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try{
			readFrom(is);

			if (!isInitialized())
				return this;
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Lenz.

		} catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public MJIProtoMessage copyInstance(){
		return new SC_INTER_RACING_START_NOTI();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_race() && _race != null){
			_race.dispose();
			_race = null;
		}
		if (has_racer()){
			for(RacerInfoT val : _racer)
				val.dispose();
			_racer.clear();
			_racer = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
