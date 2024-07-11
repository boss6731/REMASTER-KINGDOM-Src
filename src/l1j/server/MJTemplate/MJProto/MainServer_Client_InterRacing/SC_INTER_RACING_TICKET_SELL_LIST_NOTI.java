package l1j.server.MJTemplate.MJProto.MainServer_Client_InterRacing;

import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.IntRange;

import java.io.IOException;
import java.util.ArrayDeque;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public class SC_INTER_RACING_TICKET_SELL_LIST_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{

	
	public static SC_INTER_RACING_TICKET_SELL_LIST_NOTI newInstance(){
		return new SC_INTER_RACING_TICKET_SELL_LIST_NOTI();
	}
	private int _npcId;
	private int _currency;
	private java.util.LinkedList<SellItemT> _items;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_INTER_RACING_TICKET_SELL_LIST_NOTI(){
	}
	public int get_npcId(){
		return _npcId;
	}
	public void set_npcId(int val){
		_bit |= 0x1;
		_npcId = val;
	}
	public boolean has_npcId(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_currency(){
		return _currency;
	}
	public void set_currency(int val){
		_bit |= 0x2;
		_currency = val;
	}
	public boolean has_currency(){
		return (_bit & 0x2) == 0x2;
	}
	public java.util.LinkedList<SellItemT> get_items(){
		return _items;
	}
	public void add_items(SellItemT val){
		if(!has_items()){
			_items = new java.util.LinkedList<SellItemT>();
			_bit |= 0x4;
		}
		_items.add(val);
	}
	public boolean has_items(){
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
		if (has_npcId())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _npcId);
		if (has_currency())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _currency);
		if (has_items()){
			for(SellItemT val : _items)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_npcId()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_currency()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_items()){
			for(SellItemT val : _items){
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
		if (has_npcId()){
			output.wirteInt32(1, _npcId);
		}
		if (has_currency()){
			output.wirteInt32(2, _currency);
		}
		if (has_items()){
			for(SellItemT val : _items){
				output.writeMessage(3, val);
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
				case 0x00000008:{
					set_npcId(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_currency(input.readInt32());
					break;
				}
				case 0x0000001A:{
					add_items((SellItemT)input.readMessage(SellItemT.newInstance()));
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
		return new SC_INTER_RACING_TICKET_SELL_LIST_NOTI();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_items()){
			for(SellItemT val : _items)
				val.dispose();
			_items.clear();
			_items = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class SellItemT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static SellItemT newInstance(){
			return new SellItemT();
		}
		private int _itemId;
		private int _price;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private SellItemT(){
		}
		public int get_itemId(){
			return _itemId;
		}
		public void set_itemId(int val){
			_bit |= 0x1;
			_itemId = val;
		}
		public boolean has_itemId(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_price(){
			return _price;
		}
		public void set_price(int val){
			_bit |= 0x2;
			_price = val;
		}
		public boolean has_price(){
			return (_bit & 0x2) == 0x2;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;		}
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_itemId())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _itemId);
			if (has_price())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _price);
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_itemId()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_price()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
			if (has_itemId()){
				output.wirteInt32(1, _itemId);
			}
			if (has_price()){
				output.wirteInt32(2, _price);
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
						set_itemId(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_price(input.readInt32());
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
			return new SellItemT();
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
}
