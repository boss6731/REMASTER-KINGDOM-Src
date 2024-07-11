package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

import l1j.server.Config;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.IO.ProtoInputStream;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.SmeltingProbability;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.MJHexHelper;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_SYNTHESIS_SMELTING_DESIGN_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	private static ArrayList<ProtoOutputStream> _smeltingCaches;
	private static ProtoOutputStream _smeltingSameCache;
	
	private static ArrayList<ProtoOutputStream> createsmeltingCache(){
		SC_SYNTHESIS_SMELTING_DESIGN_ACK src = (SC_SYNTHESIS_SMELTING_DESIGN_ACK) MJEProtoMessages.SC_SYNTHESIS_SMELTING_DESIGN_ACK.getMessageInstance();
		ArrayList<ProtoOutputStream> smeltingcaches = new ArrayList<ProtoOutputStream>();

		SC_SYNTHESIS_SMELTING_DESIGN_ACK ack = new SC_SYNTHESIS_SMELTING_DESIGN_ACK();
		ack.set_result_code(ResultCode.RC_SYNTHESIS_SMELTING_LOAD_START);
		ack.set_inputLists(src.get_inputLists());
		smeltingcaches.add(doMakeCreateProto(ack));
		
		ack = new SC_SYNTHESIS_SMELTING_DESIGN_ACK();
		ack.set_result_code(ResultCode.RC_SYNTHESIS_SMELTING_LOADING);
		ack.set_outputLists(src.get_outputLists());
		smeltingcaches.add(doMakeCreateProto(ack));
		
		ack = new SC_SYNTHESIS_SMELTING_DESIGN_ACK();
		ack.set_result_code(ResultCode.RC_SYNTHESIS_SMELTING_LOADING);
		ack.set_alchemies(src.get_alchemies());
		smeltingcaches.add(doMakeCreateProto(ack));
		
		ack = new SC_SYNTHESIS_SMELTING_DESIGN_ACK();
		ack.set_result_code(ResultCode.RC_SYNTHESIS_SMELTING_LOAD_FINISH);
		smeltingcaches.add(doMakeCreateProto(ack));
		ack.dispose();
		
		return smeltingcaches;
	}
	
	private static ProtoOutputStream doMakeCreateProto(SC_SYNTHESIS_SMELTING_DESIGN_ACK ack){
		ProtoOutputStream stream = ack.writeTo(MJEProtoMessages.SC_SYNTHESIS_SMELTING_DESIGN_ACK);
		stream.createProtoBytes();
		return stream;
	}
	
	public static void reloadedAlchemyCaches(){
		ArrayList<ProtoOutputStream> tmp = _smeltingCaches;
		_smeltingCaches = createsmeltingCache();
		if(tmp != null){
			for(ProtoOutputStream s : tmp)
				s.dispose();
			tmp.clear();
			tmp = null;
		}
	}
	
	public static void reloadedAlchemySameCaches(){
		ProtoOutputStream tmp = _smeltingSameCache;
		SC_SYNTHESIS_SMELTING_DESIGN_ACK ack = new SC_SYNTHESIS_SMELTING_DESIGN_ACK();
		ack.set_result_code(ResultCode.RC_SYNTHESIS_SMELTING_SAME_HASH_VAL);
		_smeltingSameCache = ack.writeTo(MJEProtoMessages.SC_SYNTHESIS_SMELTING_DESIGN_ACK);
		ack.dispose();
		
		if(tmp != null){
			tmp.dispose();
			tmp = null;
		}
	}
	
	public static SC_SYNTHESIS_SMELTING_DESIGN_ACK newInstance(){
		final SC_SYNTHESIS_SMELTING_DESIGN_ACK message = new SC_SYNTHESIS_SMELTING_DESIGN_ACK();
		GeneralThreadPool.getInstance().schedule(new Runnable(){
			@Override
			public void run(){
				try {
					message.readFrom(ProtoInputStream.newInstance("./data/smelting.dat"));
					if(!message.isInitialized()){
						throw new IllegalArgumentException(String.format("fail initialized smelting data.(SC_SYNTHESIS_SMELTING_DESIGN_ACK) %d", message.getInitializeBit()));
					}
					reloadedAlchemyCaches();
					reloadedAlchemySameCaches();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 2000L);
		
		return message;
	}

	public static void send(L1PcInstance pc, byte[] hash_val) {
		try {
// 比較當前的哈希值與配置中的哈希值
			if (MJHexHelper.compareArrays(Config.CraftAlchemySetting.SMELTING_VERSION_HASH, hash_val, Config.CraftAlchemySetting.SMELTING_VERSION_HASH.length)) {
				pc.sendPackets(_smeltingSameCache, false); // 如果相同，發送緩存的熔煉數據
			} else if (Config.CraftAlchemySetting.CRAFTTRANSMITSAFELINE <= L1World.getInstance().getAllPlayersSize()) {
// 如果當前玩家數量超過安全線，提醒玩家版本過舊
				pc.sendPackets("當前客戶端的娃娃信息是舊版本，因此可能不會顯示所有列表。");
				pc.sendPackets("請在連接器上執行錯誤修補程序後重新連接，即可接收所有列表。");
				pc.sendPackets(_smeltingSameCache, false); // 發送相同的緩存數據
			} else {
// 提示玩家更新娃娃信息
				pc.sendPackets("娃娃信息正在更新。請稍候1秒，界面將彈出。");
				GeneralThreadPool.getInstance().schedule(new Runnable() {
					@override
					public void run() {
// 1秒後直接發送新的熔煉數據包
						pc.getNetConnection().directSendPacket(_smeltingCaches);
					}
				}, 100L); // 延遲100毫秒
			}
		} catch (Exception e) {
// 捕獲異常並發送失敗的響應
			SC_SYNTHESIS_SMELTING_DESIGN_ACK ack = new SC_SYNTHESIS_SMELTING_DESIGN_ACK();
			ack.set_result_code(ResultCode.RC_SYNTHESIS_SMELTING_CURRENTLY_CLOSED);
			pc.sendPackets(ack, MJEProtoMessages.SC_SYNTHESIS_SMELTING_DESIGN_ACK, true);
			e.printStackTrace(); // 打印異常堆棧信息
		}
	}
	
	public static Alchemy getAlchemy(int alchemyId){
		SC_SYNTHESIS_SMELTING_DESIGN_ACK ack = (SC_SYNTHESIS_SMELTING_DESIGN_ACK)MJEProtoMessages.SC_SYNTHESIS_SMELTING_DESIGN_ACK.getMessageInstance();
		LinkedList<Alchemy> alchemies = ack.get_alchemies();
		if(alchemyId < 0 || alchemyId >= alchemies.size()){
			return null;
		}
		return alchemies.get(alchemyId);
	}
	
	public static OutputList getOutputList(int alchemyId){
		SC_SYNTHESIS_SMELTING_DESIGN_ACK ack = (SC_SYNTHESIS_SMELTING_DESIGN_ACK)MJEProtoMessages.SC_SYNTHESIS_SMELTING_DESIGN_ACK.getMessageInstance();
		LinkedList<OutputList> outputs = ack.get_outputLists();
		if(alchemyId < 0 || alchemyId >= outputs.size())
			return null;
		
		return outputs.get(alchemyId);
	}
	
	public static Item selectOutputItem(int alchemyId){
		OutputList outputs = getOutputList(alchemyId);
		HashMap<Integer, Item> output = outputs.get_item();
		int select = MJRnd.next(output.size());
		int idx = 0;
		Item result = null;
		
		for(Item item : output.values()){
			result = item;
			if(idx++ == select)
				break;
		}
		return result;
	}
	
	public static L1Item selectOutputL1Item(int alchemyId){
		return ItemTable.getInstance().getTemplate(SmeltingProbability.getInstance().select_id(alchemyId));
	}
	
	public static InputList getInputList(int alchemyId){
		SC_SYNTHESIS_SMELTING_DESIGN_ACK ack = (SC_SYNTHESIS_SMELTING_DESIGN_ACK)MJEProtoMessages.SC_SYNTHESIS_SMELTING_DESIGN_ACK.getMessageInstance();
		LinkedList<InputList> inputs = ack.get_inputLists();
		if(alchemyId < 0 || alchemyId >= inputs.size())
			return null;

		return inputs.get(alchemyId);
	}

	private ResultCode _result_code;
	private LinkedList<InputList> _inputLists;
	private LinkedList<OutputList> _outputLists;
	private LinkedList<Alchemy> _alchemies;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_SYNTHESIS_SMELTING_DESIGN_ACK(){
	}
	public ResultCode get_result_code(){
		return _result_code;
	}
	public void set_result_code(ResultCode val){
		_bit |= 0x00000001;
		_result_code = val;
	}
	public boolean has_result_code(){
		return (_bit & 0x00000001) == 0x00000001;
	}
	public LinkedList<InputList> get_inputLists(){
		return _inputLists;
	}
	
	public void set_inputLists(LinkedList<InputList> val){
		_bit |= 0x00000002;
		_inputLists = val;
	}
	
	public void add_inputLists(InputList val){
		if(!has_inputLists()){
			_inputLists = new LinkedList<InputList>();
			_bit |= 0x00000002;
		}
		_inputLists.add(val);
	}
	public boolean has_inputLists(){
		return (_bit & 0x00000002) == 0x00000002;
	}
	public LinkedList<OutputList> get_outputLists(){
		return _outputLists;
	}
	public void set_outputLists(LinkedList<OutputList> val){
		_bit |= 0x00000004;
		_outputLists = val;
	}
	public void add_outputLists(OutputList val){
		if(!has_outputLists()){
			_outputLists = new LinkedList<OutputList>();
			_bit |= 0x00000004;
		}
		_outputLists.add(val);
	}
	public boolean has_outputLists(){
		return (_bit & 0x00000004) == 0x00000004;
	}
	
	
	public java.util.LinkedList<Alchemy> get_alchemies(){
		return _alchemies;
	}
	public void set_alchemies(LinkedList<Alchemy> val){
		_bit |= 0x00000008;
		_alchemies = val;
	}
	public void add_alchemies(Alchemy val){
		if(!has_alchemies()){
			_alchemies = new java.util.LinkedList<Alchemy>();
			_bit |= 0x00000008;
		}
		_alchemies.add(val);
	}
	public boolean has_alchemies(){
		return (_bit & 0x00000008) == 0x00000008;
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
		if (has_result_code()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _result_code.toInt());
		}
		if (has_inputLists()){
			for(InputList val : _inputLists){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
			}
		}
//		if (has_outputLists()){
//			for(OutputList val : _outputLists){
//				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
//			}
//		}
		if (has_alchemies()){
			for(Alchemy val : _alchemies){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_result_code()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_inputLists()){
			for(InputList val : _inputLists){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_outputLists()){
			for(OutputList val : _outputLists){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_alchemies()){
			for(Alchemy val : _alchemies){
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
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_result_code()){
			output.writeEnum(1, _result_code.toInt());
		}
		if (has_inputLists()){
			for (InputList val : _inputLists){
				output.writeMessage(2, val);
			}
		}
//		if (has_outputLists()){
//			for (SC_SYNTHESIS_SMELTING_DESIGN_ACK.OutputList val : _outputLists){
//				output.writeMessage(3, val);
//			}
//		}
		if (has_alchemies()){
			for (Alchemy val : _alchemies){
				output.writeMessage(4, val);
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
					set_result_code(ResultCode.fromInt(input.readEnum()));
					break;
				}
				case 0x00000012:{
					add_inputLists((InputList)input.readMessage(InputList.newInstance()));
					break;
				}
				case 0x0000001A:{
					add_outputLists((OutputList)input.readMessage(OutputList.newInstance()));
					break;
				}
				case 0x00000022:{
					add_alchemies((Alchemy)input.readMessage(Alchemy.newInstance()));
					break;
				}
				default:{
					return this;
				}
			}
		}
		if(has_inputLists()){
			_inputLists.sort(new Comparator<InputList>(){
				@Override
				public int compare(InputList o1, InputList o2) {
					return o1.get_id() - o2.get_id();
				}
			});
		}
		if(has_outputLists()){
			_outputLists.sort(new Comparator<OutputList>(){
				@Override
				public int compare(OutputList o1, OutputList o2) {
					return o1.get_id() - o2.get_id();
				}
			});
		}
		
		if(has_alchemies()){
			_alchemies.sort(new Comparator<Alchemy>(){
				@Override
				public int compare(Alchemy o1, Alchemy o2) {
					return o1.get_id() - o2.get_id();
				}
			});
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
	public MJIProtoMessage copyInstance(){
		return new SC_SYNTHESIS_SMELTING_DESIGN_ACK();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_inputLists()){
			for(InputList val : _inputLists)
				val.dispose();
			_inputLists.clear();
			_inputLists = null;
		}
		if (has_outputLists()){
			for(OutputList val : _outputLists)
				val.dispose();
			_outputLists.clear();
			_outputLists = null;
		}
		if (has_alchemies()){
			for(Alchemy val : _alchemies)
				val.dispose();
			_alchemies.clear();
			_alchemies = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class Item implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static Item newInstance(){
			return new Item();
		}
		private int _name_id;
		private int _icon;
		private int _bless_code;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private Item(){
		}
		public int get_name_id(){
			return _name_id;
		}
		public void set_name_id(int val){
			_bit |= 0x1;
			_name_id = val;
		}
		public boolean has_name_id(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_icon(){
			return _icon;
		}
		public void set_icon(int val){
			_bit |= 0x2;
			_icon = val;
		}
		public boolean has_icon(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_bless_code(){
			return _bless_code;
		}
		public void set_bless_code(int val){
			_bit |= 0x4;
			_bless_code = val;
		}
		public boolean has_bless_code(){
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
			if (has_name_id()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _name_id);
			}
			if (has_icon()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _icon);
			}
			if (has_bless_code()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _bless_code);
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
			if (has_name_id()){
				output.wirteInt32(1, _name_id);
			}
			if (has_icon()){
				output.wirteInt32(2, _icon);
			}
			if (has_bless_code()){
				output.wirteInt32(3, _bless_code);
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
						set_name_id(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_icon(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_bless_code(input.readInt32());
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
			return new Item();
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
	public static class InputList implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static InputList newInstance(){
			return new InputList();
		}
		private int _id;
		private HashMap<Integer, Item> _item;
//		private int _grade;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private InputList(){
		}
		public int get_id(){
			return _id;
		}
		public void set_id(int val){
			_bit |= 0x00000001;
			_id = val;
		}
		public boolean has_id(){
			return (_bit & 0x00000001) == 0x00000001;
		}
		
		public boolean isInInput(int descId){
			return _item.containsKey(descId);
		}
		
		public int isInInputs(LinkedList<CS_SMELTING_MAKE_REQ.Input> inputs){
			int inCount = 0;
			for(CS_SMELTING_MAKE_REQ.Input input : inputs){
				if(_item.containsKey(input.get_item_name_id()))
					++inCount;
			}
			return inCount;
		}
	
		
		public HashMap<Integer, Item> get_item(){
			return _item;
		}
		
		public Item get_item(int name_id){
			return _item.get(name_id);
		}
		
		public void add_item(Item val){
			if(!has_item()){
				_item = new HashMap<Integer, Item>();
				_bit |= 0x00000002;
			}
			_item.put(val.get_name_id(), val);
		}
		public boolean has_item(){
			return (_bit & 0x00000002) == 0x00000002;
		}
/*		public int get_grade(){
			return _grade;
		}
		public void set_grade(int val){
			_bit |= 0x4;
			_grade = val;
		}
		public boolean has_grade(){
			return (_bit & 0x4) == 0x4;
		}*/
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
			if (has_id()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _id);
			}
			if (has_item()){
				for(Item val : _item.values()){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
				}
			}
//			if (has_grade()){
//				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _grade);
//			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_id()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_item()){
				for(Item val : _item.values()){
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
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
			if (has_id()){
				output.wirteInt32(1, _id);
			}
			if (has_item()){
				for (Item val : _item.values()){
					output.writeMessage(2, val);
				}
			}
//			if (has_grade()){
//				output.wirteInt32(3, _grade);
//			}
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
						set_id(input.readInt32());
						break;
					}
					case 0x00000012:{
						add_item((Item)input.readMessage(Item.newInstance()));
						break;
					}
//					case 0x00000018:{
//						set_grade(input.readInt32());
//						break;
//					}
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
			return new InputList();
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
	public static class OutputList implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static OutputList newInstance(){
			return new OutputList();
		}
		private int _id;
		private HashMap<Integer, Item> _item;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private OutputList(){
		}
		public int get_id(){
			return _id;
		}
		public void set_id(int val){
			_bit |= 0x1;
			_id = val;
		}
		public boolean has_id(){
			return (_bit & 0x1) == 0x1;
		}
		public HashMap<Integer, Item> get_item(){
			return _item;
		}
		public void add_item(Item val){
			if(!has_item()){
				_item = new HashMap<Integer, Item>();
				_bit |= 0x2;
			}
			_item.put(val.get_name_id(), val);
		}
		public boolean has_item(){
			return (_bit & 0x2) == 0x2;
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
			if (has_id()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _id);
			}
			if (has_item()){
				for(Item val : _item.values()){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
				}
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_id()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_item()){
				for(SC_SYNTHESIS_SMELTING_DESIGN_ACK.Item val : _item.values()){
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
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
			if (has_id()){
				output.wirteInt32(1, _id);
			}
			if (has_item()){
				for (SC_SYNTHESIS_SMELTING_DESIGN_ACK.Item val : _item.values()){
					output.writeMessage(2, val);
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
						set_id(input.readInt32());
						break;
					}
					case 0x00000012:{
						add_item((SC_SYNTHESIS_SMELTING_DESIGN_ACK.Item)input.readMessage(SC_SYNTHESIS_SMELTING_DESIGN_ACK.Item.newInstance()));
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
			return new OutputList();
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
	public static class Alchemy implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static Alchemy newInstance(){
			return new Alchemy();
		}
		private int _id;
		private LinkedList<Input> _inputs;
		private LinkedList<Output> _outputs;
		private SubInput _subInput;
		private int _type;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private Alchemy(){
		}
		public int get_id(){
			return _id;
		}
		public void set_id(int val){
			_bit |= 0x1;
			_id = val;
		}
		public boolean has_id(){
			return (_bit & 0x1) == 0x1;
		}
		public java.util.LinkedList<SC_SYNTHESIS_SMELTING_DESIGN_ACK.Alchemy.Input> get_inputs(){
			return _inputs;
		}
		public void add_inputs(SC_SYNTHESIS_SMELTING_DESIGN_ACK.Alchemy.Input val){
			if(!has_inputs()){
				_inputs = new java.util.LinkedList<SC_SYNTHESIS_SMELTING_DESIGN_ACK.Alchemy.Input>();
				_bit |= 0x2;
			}
			_inputs.add(val);
		}
		public boolean has_inputs(){
			return (_bit & 0x2) == 0x2;
		}
		public java.util.LinkedList<SC_SYNTHESIS_SMELTING_DESIGN_ACK.Alchemy.Output> get_outputs(){
			return _outputs;
		}
		public void add_outputs(SC_SYNTHESIS_SMELTING_DESIGN_ACK.Alchemy.Output val){
			if(!has_outputs()){
				_outputs = new java.util.LinkedList<SC_SYNTHESIS_SMELTING_DESIGN_ACK.Alchemy.Output>();
				_bit |= 0x4;
			}
			_outputs.add(val);
		}
		public boolean has_outputs(){
			return (_bit & 0x4) == 0x4;
		}
		
		public Output findOutput(LinkedList<Integer> filled_slots){
			for(Output output : _outputs){
				if(output.equalsFilled(filled_slots))
					return output;
			}
			return null;
		}
		
		public SubInput get_subInput(){
			return _subInput;
		}
		public void set_subInput(SC_SYNTHESIS_SMELTING_DESIGN_ACK.Alchemy.SubInput val){
			_bit |= 0x8;
			_subInput = val;
		}
		public boolean has_subInput(){
			return (_bit & 0x8) == 0x8;
		}
		public int get_type(){
			return _type;
		}
		public void set_type(int val){
			_bit |= 0x10;
			_type = val;
		}
		public boolean has_type(){
			return (_bit & 0x10) == 0x10;
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
			if (has_id()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _id);
			}
			if (has_inputs()){
				for(SC_SYNTHESIS_SMELTING_DESIGN_ACK.Alchemy.Input val : _inputs){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
				}
			}
			if (has_outputs()){
				for(SC_SYNTHESIS_SMELTING_DESIGN_ACK.Alchemy.Output val : _outputs){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
				}
			}
			if (has_subInput()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, _subInput);
			}
			if (has_type()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _type);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_id()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_inputs()){
				for(SC_SYNTHESIS_SMELTING_DESIGN_ACK.Alchemy.Input val : _inputs){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			if (has_outputs()){
				for(SC_SYNTHESIS_SMELTING_DESIGN_ACK.Alchemy.Output val : _outputs){
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
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
			if (has_id()){
				output.wirteInt32(1, _id);
			}
			if (has_inputs()){
				for (SC_SYNTHESIS_SMELTING_DESIGN_ACK.Alchemy.Input val : _inputs){
					output.writeMessage(2, val);
				}
			}
			if (has_outputs()){
				for (SC_SYNTHESIS_SMELTING_DESIGN_ACK.Alchemy.Output val : _outputs){
					output.writeMessage(3, val);
				}
			}
			if (has_subInput()){
				output.writeMessage(4, _subInput);
			}
			if (has_type()){
				output.wirteInt32(5, _type);
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
						set_id(input.readInt32());
						break;
					}
					case 0x00000012:{
						add_inputs((Input)input.readMessage(Input.newInstance()));
						break;
					}
					case 0x0000001A:{
						add_outputs((Output)input.readMessage(Output.newInstance()));
						break;
					}
					case 0x00000022:{
						set_subInput((SubInput)input.readMessage(SubInput.newInstance()));
						break;
					}
					case 0x00000028:{
						set_type(input.readInt32());
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
			return new Alchemy();
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
		public static class Input implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
			public static Input newInstance(){
				return new Input();
			}
			private int _slot_number;
			private boolean _is_required;
			private int _input_list_id;
			private boolean _is_hyper;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private Input(){
			}
			public int get_slot_number(){
				return _slot_number;
			}
			public void set_slot_number(int val){
				_bit |= 0x1;
				_slot_number = val;
			}
			public boolean has_slot_number(){
				return (_bit & 0x1) == 0x1;
			}
			public boolean get_is_required(){
				return _is_required;
			}
			public void set_is_required(boolean val){
				_bit |= 0x2;
				_is_required = val;
			}
			public boolean has_is_required(){
				return (_bit & 0x2) == 0x2;
			}
			public int get_input_list_id(){
				return _input_list_id;
			}
			public void set_input_list_id(int val){
				_bit |= 0x4;
				_input_list_id = val;
			}
			public boolean has_input_list_id(){
				return (_bit & 0x4) == 0x4;
			}
			public boolean get_is_hyper(){
				return _is_hyper;
			}
			public void set_is_hyper(boolean val){
				_bit |= 0x8;
				_is_hyper = val;
			}
			public boolean has_is_hyper(){
				return (_bit & 0x8) == 0x8;
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
				if (has_slot_number()){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _slot_number);
				}
				if (has_is_required()){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _is_required);
				}
				if (has_input_list_id()){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _input_list_id);
				}
				if (has_is_hyper()){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(4, _is_hyper);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_slot_number()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_is_required()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_input_list_id()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
				if (has_slot_number()){
					output.wirteInt32(1, _slot_number);
				}
				if (has_is_required()){
					output.writeBool(2, _is_required);
				}
				if (has_input_list_id()){
					output.wirteInt32(3, _input_list_id);
				}
				if (has_is_hyper()){
					output.writeBool(4, _is_hyper);
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
							set_slot_number(input.readInt32());
							break;
						}
						case 0x00000010:{
							set_is_required(input.readBool());
							break;
						}
						case 0x00000018:{
							set_input_list_id(input.readInt32());
							break;
						}
						case 0x00000020:{
							set_is_hyper(input.readBool());
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
				return new Input();
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
		public static class Output implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
			public static Output newInstance(){
				return new Output();
			}
			private LinkedList<Integer> _filled_slots;
			private int _output_list_id;
			private LinkedList<Output> _hyper_outputs;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private Output(){
			}
			public java.util.LinkedList<Integer> get_filled_slots(){
				return _filled_slots;
			}
			public void add_filled_slots(int val){
				if(!has_filled_slots()){
					_filled_slots = new java.util.LinkedList<Integer>();
					_bit |= 0x1;
				}
				_filled_slots.add(val);
			}
			public boolean has_filled_slots(){
				return (_bit & 0x1) == 0x1;
			}			
			public boolean equalsFilled(LinkedList<Integer> filled_slots){
				int size = filled_slots.size();
				if(size != _filled_slots.size())
					return false;
				
				for(int i=size - 1; i>=0; --i){
					if(filled_slots.get(i) != _filled_slots.get(i))
						return false;
				}
				return true;
			}
			
			
			public int get_output_list_id(){
				return _output_list_id;
			}
			public void set_output_list_id(int val){
				_bit |= 0x2;
				_output_list_id = val;
			}
			public boolean has_output_list_id(){
				return (_bit & 0x2) == 0x2;
			}
			public java.util.LinkedList<SC_SYNTHESIS_SMELTING_DESIGN_ACK.Alchemy.Output> get_hyper_outputs(){
				return _hyper_outputs;
			}
			public void add_hyper_outputs(SC_SYNTHESIS_SMELTING_DESIGN_ACK.Alchemy.Output val){
				if(!has_hyper_outputs()){
					_hyper_outputs = new java.util.LinkedList<SC_SYNTHESIS_SMELTING_DESIGN_ACK.Alchemy.Output>();
					_bit |= 0x4;
				}
				_hyper_outputs.add(val);
			}
			public boolean has_hyper_outputs(){
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
				if (has_filled_slots()){
					for(int val : _filled_slots){
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, val);
					}
				}
				if (has_output_list_id()){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _output_list_id);
				}
				if (has_hyper_outputs()){
					for(SC_SYNTHESIS_SMELTING_DESIGN_ACK.Alchemy.Output val : _hyper_outputs){
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
					}
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_filled_slots()){
//				if (has_filled_slots()){
//					for(int val : _filled_slots){
//						if (!val.isInitialized()){
							_memorizedIsInitialized = -1;
							return false;
						}
//					}
//				}
				if (!has_output_list_id()){
					_memorizedIsInitialized = -1;
					return false;
				}
/*				if (has_hyper_outputs()){
					for(SC_SYNTHESIS_SMELTING_DESIGN_ACK.Alchemy.Output val : _hyper_outputs){
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
				if (has_filled_slots()){
					for (int val : _filled_slots){
						output.wirteInt32(1, val);
					}
				}
				if (has_output_list_id()){
					output.wirteInt32(2, _output_list_id);
				}
				if (has_hyper_outputs()){
					for (Output val : _hyper_outputs){
						output.writeMessage(3, val);
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
							add_filled_slots(input.readInt32());
							break;
						}
						case 0x00000010:{
							set_output_list_id(input.readInt32());
							break;
						}
						case 0x0000001A:{
							add_hyper_outputs((Output)input.readMessage(Output.newInstance()));
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
				return new Output();
			}
			@Override
			public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance(){
				return newInstance();
			}
			@Override
			public void dispose(){
				if (has_filled_slots()){
					_filled_slots = null;
				}
				if (has_hyper_outputs()){
					for(Output val : _hyper_outputs)
						val.dispose();
					_hyper_outputs.clear();
					_hyper_outputs = null;
				}
				_bit = 0;
				_memorizedIsInitialized = -1;
			}
		}
		
		public static class SubInput implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
			public static SubInput newInstance(){
				return new SubInput();
			}
			private int _name_id;
			private int _count;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private SubInput(){
			}
			public int get_name_id(){
				return _name_id;
			}
			public void set_name_id(int val){
				_bit |= 0x1;
				_name_id = val;
			}
			public boolean has_name_id(){
				return (_bit & 0x1) == 0x1;
			}
			public int get_count(){
				return _count;
			}
			public void set_count(int val){
				_bit |= 0x2;
				_count = val;
			}
			public boolean has_count(){
				return (_bit & 0x2) == 0x2;
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
				if (has_name_id()){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _name_id);
				}
				if (has_count()){
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _count);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_name_id()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_count()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
				if (has_name_id()){
					output.wirteInt32(1, _name_id);
				}
				if (has_count()){
					output.wirteInt32(2, _count);
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
							set_name_id(input.readInt32());
							break;
						}
						case 0x00000010:{
							set_count(input.readInt32());
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
				return new SubInput();
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

		
	}
	public enum ResultCode {
		RC_SYNTHESIS_SMELTING_LOAD_START(0),        // 熔煉加載開始
		RC_SYNTHESIS_SMELTING_LOADING(1),           // 熔煉加載中
		RC_SYNTHESIS_SMELTING_LOAD_FINISH(2),       // 熔煉加載完成
		RC_SYNTHESIS_SMELTING_SAME_HASH_VAL(3),     // 相同的哈希值
		RC_SYNTHESIS_SMELTING_INVALID_HASH_VAL(4),  // 無效的哈希值
		RC_SYNTHESIS_SMELTING_CURRENTLY_CLOSED(5),  // 熔煉當前關閉
		RC_ERROR_UNKNOWN(9999);                     // 未知錯誤

		private int value;

		// 枚舉構造函數
		ResultCode(int val) {
			value = val;
		}

		// 返回枚舉的整數值
		public int toInt() {
			return value;
		}

		// 比較兩個枚舉是否相等
		public boolean equals(ResultCode v) {
			return value == v.value;
		}

		// 根據整數值返回對應的枚舉
		public static ResultCode fromInt(int i) {
			switch (i) {
				case 0:
					return RC_SYNTHESIS_SMELTING_LOAD_START;
				case 1:
					return RC_SYNTHESIS_SMELTING_LOADING;
				case 2:
					return RC_SYNTHESIS_SMELTING_LOAD_FINISH;
				case 3:
					return RC_SYNTHESIS_SMELTING_SAME_HASH_VAL;
				case 4:
					return RC_SYNTHESIS_SMELTING_INVALID_HASH_VAL;
				case 5:
					return RC_SYNTHESIS_SMELTING_CURRENTLY_CLOSED;
				case 9999:
					return RC_ERROR_UNKNOWN;
				default:
					throw new IllegalArgumentException(String.format("無效的 ResultCode，%d", i));
			}
		}
	}
