package l1j.server.MJTemplate.MJProto.resultCode;

import java.io.IOException;
import java.util.ArrayList;

import l1j.server.InvenBonusItem.InvenBonusItemInfo;
import l1j.server.InvenBonusItem.InvenBonusItemLoader;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.Pc_Golden_Buff.Pc_Golden_Buff_Info;
import l1j.server.Pc_Golden_Buff.Pc_Golden_Buff_Loader;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_PC_MASTER_GOLDEN_BUFF_ENABLE_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send(L1PcInstance pc, int index, boolean active) {
		SC_PC_MASTER_GOLDEN_BUFF_ENABLE_NOTI noti = SC_PC_MASTER_GOLDEN_BUFF_ENABLE_NOTI.newInstance();
		noti.set_enable(active);
		if (active) {
			if (index == 0) {
//				System.out.println(pc.getAccount().get_Index0_Remain_Time());
				noti.set_remain_count(pc.getAccount().get_Index0_Remain_Time());
			} else if (index == 1) {
//				System.out.println(pc.getAccount().get_Index1_Remain_Time());
				noti.set_remain_count(pc.getAccount().get_Index1_Remain_Time());
			}
			
			ArrayList<Integer> bufflist = pc.getPcGoldenBuffList();
			for (int i = 0; i < bufflist.size(); i++) {
				Pc_Golden_Buff_Info info = Pc_Golden_Buff_Loader.getInstance().getBuffOption(bufflist.get(i));
				noti.add_desc(Pc_Golden_Buff_Info.getOptionView(info));
			}

		} else {
			noti.set_remain_count(0);
			noti.add_desc(null);
		}
		pc.sendPackets(noti, MJEProtoMessages.SC_PC_MASTER_GOLDEN_BUFF_ENABLE_NOTI, true);
	}
	public static SC_PC_MASTER_GOLDEN_BUFF_ENABLE_NOTI newInstance(){
		return new SC_PC_MASTER_GOLDEN_BUFF_ENABLE_NOTI();
	}
	private boolean _enable;
	private int _remain_count;
	private java.util.LinkedList<byte[]> _desc;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_PC_MASTER_GOLDEN_BUFF_ENABLE_NOTI(){
	}
	public boolean get_enable(){
		return _enable;
	}
	public void set_enable(boolean val){
		_bit |= 0x1;
		_enable = val;
	}
	public boolean has_enable(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_remain_count(){
		return _remain_count;
	}
	public void set_remain_count(int val){
		_bit |= 0x2;
		_remain_count = val;
	}
	public boolean has_remain_count(){
		return (_bit & 0x2) == 0x2;
	}
	public java.util.LinkedList<byte[]> get_desc(){
		return _desc;
	}
	public void add_desc(byte[] val){
		if(!has_desc()){
			_desc = new java.util.LinkedList<byte[]>();
			_bit |= 0x4;
		}
		_desc.add(val);
	}
	public boolean has_desc(){
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
		if (has_enable()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(1, _enable);
		}
		if (has_remain_count()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _remain_count);
		}
		if (has_desc()){
			for(byte[] val : _desc){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(3, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_enable()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_remain_count()){
			_memorizedIsInitialized = -1;
			return false;
		}
/*		if (has_desc()){
			for(byte[] val : _desc){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}*/
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_enable()){
			output.writeBool(1, _enable);
		}
		if (has_remain_count()){
			output.wirteInt32(2, _remain_count);
		}
		if (has_desc()){
			for (byte[] val : _desc){
				output.writeBytes(3, val);
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
				case 0x00000008:{
					set_enable(input.readBool());
					break;
				}
				case 0x00000010:{
					set_remain_count(input.readInt32());
					break;
				}
				case 0x0000001A:{
					add_desc(input.readBytes());
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
		return new SC_PC_MASTER_GOLDEN_BUFF_ENABLE_NOTI();
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
