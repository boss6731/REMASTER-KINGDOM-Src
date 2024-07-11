package l1j.server.MJTemplate.MJProto.MainServer_Client_InterRacing;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.RaceInfoT;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.RacerInfoT;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.RacerMapViewDataT;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.RacerTicketT;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public class CS_INTER_RACING_STATUS_INFO_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_INTER_RACING_STATUS_INFO_REQ newInstance(){
		return new CS_INTER_RACING_STATUS_INFO_REQ();
	}
	private int _worldNumber;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_INTER_RACING_STATUS_INFO_REQ(){
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
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_worldNumber()){
			output.wirteInt32(1, _worldNumber);
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
			
			L1PcInstance pc = clnt.getActiveChar();
			if(pc == null) {
				return this;
			}
			
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Lenz.
			// InterRaceProvider.scheduler().handler().currentRacers()
			// data reading...
			// InterRaceUserView.showStatusInfo(....) <- 안에서 처리해도 됨.
			
			SC_INTER_RACING_STATUS_INFO_ACK ack = SC_INTER_RACING_STATUS_INFO_ACK.newInstance();
			L1Object shop = L1World.getInstance().isNpcShop(70041);
			RaceInfoT rInfo = RaceInfoT.newInstance();
			rInfo.set_bookMakerNpcId(shop.getId());
			rInfo.set_raceKind(9);

			rInfo.set_worldNumber(38);
			ack.set_race(rInfo);

			


			pc.sendPackets(ack, MJEProtoMessages.SC_INTER_RACING_STATUS_INFO_ACK);

			
			/*
			SC_INTER_RACING_READY_NOTI noti = SC_INTER_RACING_READY_NOTI.newInstance();
			noti.set_canBuy(true);
			rInfo = RaceInfoT.newInstance();
			rInfo.set_bookMakerNpcId(pc.getId());
			rInfo.set_raceKind(1);
			rInfo.set_raceNumber(1);
			rInfo.set_trackId(0);
			rInfo.set_worldNumber(38);
			noti.set_race(rInfo);
			int[] array = new int[] {0, 10, 50, 80};
			for(int i=1; i<=5; ++i) {
				RacerTicketT ticket = RacerTicketT.newInstance();
				ticket.set_condition(array[MJRnd.next(array.length)]);
				ticket.set_laneId(i);
				ticket.set_name(String.format("$%d", i));
				ticket.set_price(i + 100);
				ticket.set_racerId(i);
				ticket.set_winRate(i + 10000);
				noti.add_racerTicket(ticket);
				
				RacerInfoT racer = RacerInfoT.newInstance();
				racer.set_dir(i);
				racer.set_laneId(i);
				racer.set_racerId(i);
				racer.set_x(pc.getX() + i + 1);
				racer.set_y(pc.getY() + i + 1);
				noti.add_racer(racer);
			}
			pc.sendPackets(noti, MJEProtoMessages.SC_INTER_RACING_READY_NOTI);
			*/

			
			/*SC_INTER_RACING_TICKET_SELL_LIST_NOTI sell_noti = SC_INTER_RACING_TICKET_SELL_LIST_NOTI.newInstance();
			sell_noti.set_currency(7);
			sell_noti.set_npcId(pc.getId());
			L1ItemInstance instance = pc.getInventory().findItemId(30001112);
			if(instance != null) {
				SellItemT item = SellItemT.newInstance();
				item.set_itemId(instance.getId());
				item.set_price(1);
				sell_noti.add_items(item);	
			}
			pc.sendPackets(sell_noti, MJEProtoMessages.SC_INTER_RACING_TICKET_SELL_LIST_NOTI);*/
			/*if(MJIRTimeScheduler.getInstance().isReady()) {
				pc.sendPackets(SC_INTER_RACING_READY_NOTI.ready_send(pc));
			}else {
				pc.sendPackets(SC_INTER_RACING_START_NOTI.start_send(pc));			
			}*/
		} catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public MJIProtoMessage copyInstance(){
		return new CS_INTER_RACING_STATUS_INFO_REQ();
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
