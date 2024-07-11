package l1j.server.MJTemplate.MJProto.resultCode;

import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.MJTemplate.MJProto.resultCode.SC_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI.eUpdateReason;
import l1j.server.Pc_Golden_Buff.Pc_Golden_Buff_Info;
import l1j.server.Pc_Golden_Buff.Pc_Golden_Buff_Loader;
import l1j.server.server.model.monitor.L1PcExpMonitor;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class CS_PC_MASTER_GOLDEN_BUFF_SWITCH_TYPE_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_PC_MASTER_GOLDEN_BUFF_SWITCH_TYPE_REQ newInstance(){
		return new CS_PC_MASTER_GOLDEN_BUFF_SWITCH_TYPE_REQ();
	}
	private int _index;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_PC_MASTER_GOLDEN_BUFF_SWITCH_TYPE_REQ(){
	}
	public int get_index(){
		return _index;
	}
	public void set_index(int val){
		_bit |= 0x1;
		_index = val;
	}
	public boolean has_index(){
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
		if (has_index()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _index);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_index()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_index()){
			output.wirteInt32(1, _index);
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
					set_index(input.readInt32());
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
			int type0 = pc.getAccount().get_Index0_type(pc);
			int type1 = pc.getAccount().get_Index1_type(pc);
			if (get_index() == 0) {
				pc.getAccount().set_Index0_type(type0 + 1 == 4? 1 : type0 + 1);
			} else if (get_index() == 1) {
				pc.getAccount().set_Index1_type(type1 + 1 == 4? 1 : type1 + 1);
			}
			
			ArrayList<Integer> MapList1 = new ArrayList<Integer>();
			int[] maplist1 = Config.ServerAdSetting.PC_GOLDEN_BUFF_MAP1;
			for (int i = 0; i < maplist1.length; i++) {
				MapList1.add(maplist1[i]);
			}
			ArrayList<Integer> MapList2 = new ArrayList<Integer>();
			int[] maplist2 = Config.ServerAdSetting.PC_GOLDEN_BUFF_MAP2;
			for (int i = 0; i < maplist2.length; i++) {
				MapList2.add(maplist2[i]);
			}
			int mapid = pc.getMapId();
			if(MapList1.contains(mapid) || MapList2.contains(mapid)) {
				ArrayList<Integer> bufflistdel = pc.getPcGoldenBuffList();
				if (bufflistdel.size() !=0 && !bufflistdel.isEmpty()) {
					for (int i = 0; i < bufflistdel.size() ;i++) {
						int index = bufflistdel.get(i);
						Pc_Golden_Buff_Info info = Pc_Golden_Buff_Loader.getInstance().getBuffOption(index);
						if (info != null) {
							Pc_Golden_Buff_Info.Pc_Golden_Buff_Option(pc, index, false);
						}
					}
				}
				pc.set_PcGoldenSstatus(false);
				L1PcExpMonitor.Pc_Golden_Buff_Enable_Check(pc);
			}
			
			SC_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI.send(pc, eUpdateReason.SWITCH_TYPE_ACK);

		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new CS_PC_MASTER_GOLDEN_BUFF_SWITCH_TYPE_REQ();
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
