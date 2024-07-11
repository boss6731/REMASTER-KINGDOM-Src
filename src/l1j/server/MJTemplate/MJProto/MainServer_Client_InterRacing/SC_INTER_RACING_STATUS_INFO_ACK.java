package l1j.server.MJTemplate.MJProto.MainServer_Client_InterRacing;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.RaceInfoT;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.RacerMapViewDataT;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.RacerTicketT;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public class SC_INTER_RACING_STATUS_INFO_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_INTER_RACING_STATUS_INFO_ACK newInstance(){
		return new SC_INTER_RACING_STATUS_INFO_ACK();
	}
	private RaceInfoT _race;
	private boolean _canBuy;
	private boolean _canSell;
	private java.util.LinkedList<RacerTicketT> _racerTicket;
	private java.util.LinkedList<RacerMapViewDataT> _racerMapView;
	private int _remainWaitingTime;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_INTER_RACING_STATUS_INFO_ACK(){
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
	public boolean get_canBuy(){
		return _canBuy;
	}
	public void set_canBuy(boolean val){
		_bit |= 0x2;
		_canBuy = val;
	}
	public boolean has_canBuy(){
		return (_bit & 0x2) == 0x2;
	}
	
	public boolean get_canSell(){
		return _canSell;
	}
	public void set_canSell(boolean val){
		_bit |= 0x4;
		_canSell = val;
	}
	public boolean has_canSell(){
		return (_bit & 0x4) == 0x4;
	}
	
	public java.util.LinkedList<RacerTicketT> get_racerTicket(){
		return _racerTicket;
	}
	public void add_racerTicket(RacerTicketT val){
		if(!has_racerTicket()){
			_racerTicket = new java.util.LinkedList<RacerTicketT>();
			_bit |= 0x8;
		}
		_racerTicket.add(val);
	}
	public boolean has_racerTicket(){
		return (_bit & 0x8) == 0x8;
	}
	public java.util.LinkedList<RacerMapViewDataT> get_racerMapView(){
		return _racerMapView;
	}
	public void add_racerMapView(RacerMapViewDataT val){
		if(!has_racerMapView()){
			_racerMapView = new java.util.LinkedList<RacerMapViewDataT>();
			_bit |= 0x10;
		}
		_racerMapView.add(val);
	}
	public boolean has_racerMapView(){
		return (_bit & 0x10) == 0x10;
	}
	public int get_remainWaitingTime(){
		return _remainWaitingTime;
	}
	public void set_remainWaitingTime(int val){
		_bit |= 0x20;
		_remainWaitingTime = val;
	}
	public boolean has_remainWaitingTime(){
		return (_bit & 0x20) == 0x20;
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
		if (has_canBuy())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _canBuy);
		if (has_canSell()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _canSell);
		}
		if (has_racerTicket()){
			for(RacerTicketT val : _racerTicket)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, val);
		}
		if (has_racerMapView()){
			for(RacerMapViewDataT val : _racerMapView)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(5, val);
		}
		if (has_remainWaitingTime()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _remainWaitingTime);
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
		if (!has_canBuy()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_racerTicket()){
			for(RacerTicketT val : _racerTicket){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_racerMapView()){
			for(RacerMapViewDataT val : _racerMapView){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (!has_remainWaitingTime()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_race()){
			output.writeMessage(1, _race);
		}
		if (has_canBuy()){
			output.writeBool(2, _canBuy);
		}
		if (has_canSell()){
			output.writeBool(3, _canSell);
		}
		if (has_racerTicket()){
			for(RacerTicketT val : _racerTicket){
				output.writeMessage(4, val);
			}
		}
		if (has_racerMapView()){
			for(RacerMapViewDataT val : _racerMapView){
				output.writeMessage(5, val);
			}
		}
		if (has_remainWaitingTime()){
			output.wirteInt32(6, _remainWaitingTime);
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

				case 0x0000000A:{
					set_race((RaceInfoT)input.readMessage(RaceInfoT.newInstance()));
					break;
				}
				case 0x00000010:{
					set_canBuy(input.readBool());
					break;
				}
				case 0x00000018:{
					set_canSell(input.readBool());
					break;
				}
				case 0x00000022:{
					add_racerTicket((RacerTicketT)input.readMessage(RacerTicketT.newInstance()));
					break;
				}
				case 0x0000002A:{
					add_racerMapView((RacerMapViewDataT)input.readMessage(RacerMapViewDataT.newInstance()));
					break;
				}
				case 0x00000030:{
					set_remainWaitingTime(input.readInt32());
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
		return new SC_INTER_RACING_STATUS_INFO_ACK();
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
		if (has_racerTicket()){
			for(RacerTicketT val : _racerTicket)
				val.dispose();
			_racerTicket.clear();
			_racerTicket = null;
		}
		if (has_racerMapView()){
			for(RacerMapViewDataT val : _racerMapView)
				val.dispose();
			_racerMapView.clear();
			_racerMapView = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
