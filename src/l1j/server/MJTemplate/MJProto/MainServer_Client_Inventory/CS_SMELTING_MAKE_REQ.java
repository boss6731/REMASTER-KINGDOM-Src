package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import java.util.Comparator;
import java.util.LinkedList;

import l1j.server.Config;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.MainServer_Client.CS_CRAFT_MAKE_REQ;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ALCHEMY_MAKE_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MESSAGE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_SMELTING_MAKE_ACK.ResultCode;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_SYNTHESIS_SMELTING_DESIGN_ACK.Alchemy;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_SYNTHESIS_SMELTING_DESIGN_ACK.Alchemy.Output;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_SYNTHESIS_SMELTING_DESIGN_ACK.InputList;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.MJSmeltingProbabilityBox;
import l1j.server.server.datatables.SmeltingProbability;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.templates.L1Item;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class CS_SMELTING_MAKE_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_SMELTING_MAKE_REQ newInstance(){
		return new CS_SMELTING_MAKE_REQ();
	}
	
	private int _resultAlchemyId;
	private SC_SMELTING_MAKE_ACK.ResultCode _resultCode;
	private LinkedList<Integer> _filled_slots;

	private int _alchemy_id;
	private java.util.LinkedList<Input> _inputs;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	
	private CS_SMELTING_MAKE_REQ(){
		_filled_slots = new LinkedList<Integer>();
	}
	
	public LinkedList<Integer> get_filled_slot(){
		return _filled_slots;
	}
	public int get_alchemy_id(){
		return _alchemy_id;
	}
	public void set_alchemy_id(int val){
		_bit |= 0x1;
		_alchemy_id = val;
	}
	public boolean has_alchemy_id(){
		return (_bit & 0x1) == 0x1;
	}
	public java.util.LinkedList<CS_SMELTING_MAKE_REQ.Input> get_inputs(){
		return _inputs;
	}
	public void add_inputs(CS_SMELTING_MAKE_REQ.Input val){
		if(!has_inputs()){
			_inputs = new LinkedList<Input>();
			_bit |= 0x2;
		}
		int sno = val.get_slot_no();
		_filled_slots.add(sno);
		_inputs.add(val);
	}
	public boolean has_inputs(){
		return (_bit & 0x2) == 0x2;
	}
	
	public void remove_inputs(L1PcInstance pc){
		L1Inventory inv = pc.getInventory();
		for(Input input : _inputs)			
			inv.removeItem(input.getTemporaryRemovedItems());
	}
	public L1Item selectRemovedItemTemplate(){
		_resultCode = SC_SMELTING_MAKE_ACK.ResultCode.RC_FAIL;
		return _inputs.get(0).getTemporaryRemovedItems().getItem();
	}
	private L1Item toInvalidOutputItem(L1PcInstance pc){
		System.out.println(String.format("[(鍊石合成)無法找到輸出物品。] 角色名稱：%s，合成ID：%d", pc.getName(), _alchemy_id));
		return selectRemovedItemTemplate();
	}

	private CS_SMELTING_MAKE_REQ toInvalidAlchemyId(L1PcInstance pc){
		SC_SMELTING_MAKE_ACK.ResultCode.RC_ERROR_BLOCKED_ALCHEMY_ID.sendCachedMessage(pc);
		System.out.println(String.format("[(鍊石合成)無法找到鍊金ID。] 角色名稱：%s，合成ID：%d", pc.getName(), _alchemy_id));
		return this;
	}

	private CS_SMELTING_MAKE_REQ toInvalidInput(L1PcInstance pc){
		SC_SMELTING_MAKE_ACK.ResultCode.RC_ERROR_INVALID_INPUT.sendCachedMessage(pc);
		System.out.println(String.format("[(鍊石合成)無法找到輸入物品。] 角色名稱：%s，合成ID：%d，角色輸入大小：%d", pc.getName(), _alchemy_id, _inputs.size()));
		return this;
	}

	private CS_SMELTING_MAKE_REQ toInvalidInputForInventory(L1PcInstance pc){
		SC_SMELTING_MAKE_ACK.ResultCode.RC_ERROR_INVALID_INPUT.sendCachedMessage(pc);
		System.out.println(String.format("[(鍊石合成)無法在背包中找到玩家發送的鍊石信息。] 角色名稱：%s，合成ID：%d", pc.getName(), _alchemy_id));
		StringBuilder sb = new StringBuilder(256);
		for (Input input : _inputs)
			sb.append(input.toString()).append("
					");
					System.out.println("-發送的信息-");
		System.out.print(sb.toString());
		return this;
	}
	
	private CS_SMELTING_MAKE_REQ doNotAllowInventory(L1PcInstance pc, int addCode){
		(addCode == L1Inventory.WEIGHT_OVER ? 
				SC_SMELTING_MAKE_ACK.ResultCode.RC_ERROR_WEIGHT_OVER : SC_SMELTING_MAKE_ACK.ResultCode.RC_ERROR_INVEN_OVER)
				.sendCachedMessage(pc);
		return this;
	}
	private L1Item selectResultItem(Alchemy alchemy, L1PcInstance pc){
		// original formula.
		if(_alchemy_id != 5 && _alchemy_id != 6 && !SmeltingProbability.getInstance().is_winning(_alchemy_id, _inputs.size())){
			return selectRemovedItemTemplate();
		}

		_filled_slots.sort(new Comparator<Integer>(){
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1 - o2;
			}
		});

		Output output = alchemy.findOutput(_filled_slots);
//		System.out.println(output);
		if(output == null)
			return toInvalidOutputItem(pc);
		
		LinkedList<Output> hyper_outputs = output.get_hyper_outputs();
		_resultCode = SC_SMELTING_MAKE_ACK.ResultCode.RC_SUCCESS;

		if(hyper_outputs != null && hyper_outputs.size() > 0 && MJRnd.isWinning(CS_CRAFT_MAKE_REQ.CRAFT_ITEM_SUCCESS_MILLIONS, _alchemy_id >= 3 ? Config.CraftAlchemySetting.ALCHEMYHYPERSUCCESSPROBBYMILLION / 2 : Config.CraftAlchemySetting.ALCHEMYHYPERSUCCESSPROBBYMILLION)){
			output = hyper_outputs.get(MJRnd.next(hyper_outputs.size()));
			_resultCode = SC_SMELTING_MAKE_ACK.ResultCode.RC_HYPER_SUCCESS;
		}
//		L1Item selectItem = SC_ALCHEMY_DESIGN_ACK.selectOutputL1Item(_alchemy_id == 5 ? output.get_output_list_id() - 1 : output.get_output_list_id());
		L1Item selectItem = SC_SYNTHESIS_SMELTING_DESIGN_ACK.selectOutputL1Item(_alchemy_id == 5 || _alchemy_id == 6 ? output.get_output_list_id() -1 : output.get_output_list_id());
		if(selectItem == null)
			return selectRemovedItemTemplate();
		
		_resultAlchemyId = output.get_output_list_id();
		return selectItem;
	}
	
	private void doMake(L1PcInstance pc, final L1ItemInstance resultItemInstance){
		final int resultAlchemyId = _resultAlchemyId;
		final String name = pc.getName();
		
		// TODO 유저가 합성할때 마다 인형/변신 한번 섞기
		MJSmeltingProbabilityBox.getInstance().shuffleList();
		
		GeneralThreadPool.getInstance().schedule(new Runnable(){
			@Override
			public void run(){
				if(resultAlchemyId >= Config.CraftAlchemySetting.ALCHEMYNOTIFYLEVEL && !ResultCode.RC_FAIL.equals(_resultCode)){
					String message = "";
					message = String.format("%s", resultItemInstance.getName());
					L1World.getInstance().broadcastPacketToAll(SC_MESSAGE_NOTI.newSmeltingMessage(resultItemInstance.getItem().getItemDescId(), message), true);
				}
				try{
					pc.getInventory().storeItem(resultItemInstance, true);
				}catch(Exception e){
					System.out.println(String.format("[(鍊石合成)沒有找到能接收製作結果的角色。] 角色名稱：%s，物品ID：%d，物品名稱：%s", name, resultItemInstance.getItemId(), resultItemInstance.getName()));
					L1World.getInstance().removeObject(resultItemInstance);
				}
			}
			// TODO 인형 합성 딜레이
		}, 100);
		
		SC_SMELTING_MAKE_ACK ack = SC_SMELTING_MAKE_ACK.newInstance();
		ack.set_result_code(_resultCode);
		ack.add_output_items(resultItemInstance.getId(), resultItemInstance.get_gfxid(), resultItemInstance.getBless());		
		pc.sendPackets(ack, MJEProtoMessages.SC_SMELTING_MAKE_ACK, true);
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
		if (has_alchemy_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _alchemy_id);
		}
		if (has_inputs()){
			for(CS_SMELTING_MAKE_REQ.Input val : _inputs){
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
		if (!has_alchemy_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_inputs()){
			for(CS_SMELTING_MAKE_REQ.Input val : _inputs){
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
		if (has_alchemy_id()){
			output.wirteInt32(1, _alchemy_id);
		}
		if (has_inputs()){
			for (CS_SMELTING_MAKE_REQ.Input val : _inputs){
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
					set_alchemy_id(input.readInt32());
					break;
				}
				case 0x00000012:{
					add_inputs((CS_SMELTING_MAKE_REQ.Input)input.readMessage(CS_SMELTING_MAKE_REQ.Input.newInstance()));
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
		L1PcInstance pc = null;
		try{
			readFrom(is);

			if (!isInitialized())
				return this;

			pc = clnt.getActiveChar();
			if (pc == null){
				return this;
			}

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Nature.

			int alchemyId = _alchemy_id - 1;
//			Alchemy alchemy = SC_SYNTHESIS_SMELTING_DESIGN_ACK.getAlchemy(_alchemy_id);
			Alchemy alchemy = SC_SYNTHESIS_SMELTING_DESIGN_ACK.getAlchemy(alchemyId);

			
			
			if(alchemy == null){
				return toInvalidAlchemyId(pc);
			}
			int userInputsSize = _inputs.size();
			if (_alchemy_id >= 1 && _alchemy_id <= 4){
				if (userInputsSize <4){
					return toInvalidInput(pc);
				}
			}
			if (_alchemy_id == 1 && !pc.getInventory().checkItem(40308, 10000)){
				return toInvalidInput(pc);
			} else if (_alchemy_id == 1 && pc.getInventory().checkItem(40308, 10000)){
				pc.getInventory().consumeItem(40308, 10000);
			}
			
			if (_alchemy_id == 2 && !pc.getInventory().checkItem(40308, 20000)){
				return toInvalidInput(pc);
			} else if (_alchemy_id == 2 && pc.getInventory().checkItem(40308, 20000)){
				pc.getInventory().consumeItem(40308, 20000);
			}
			
			if (_alchemy_id == 3 && !pc.getInventory().checkItem(40308, 50000)){
				return toInvalidInput(pc);
			} else if (_alchemy_id == 3 && pc.getInventory().checkItem(40308, 50000)){
				pc.getInventory().consumeItem(40308, 50000);
			}
			
			if (_alchemy_id == 4 && !pc.getInventory().checkItem(40308, 100000)){
				return toInvalidInput(pc);
			} else if (_alchemy_id == 4 && pc.getInventory().checkItem(40308, 100000)){
				pc.getInventory().consumeItem(40308, 100000);
			}
			
			if (_alchemy_id >= 5 && _alchemy_id <= 6 && !pc.getInventory().checkItem(30001350, 10)){
				return toInvalidInput(pc);
			} else if (_alchemy_id >= 5 && _alchemy_id <= 6 && pc.getInventory().checkItem(30001350, 10)){
				pc.getInventory().consumeItem(30001350, 10);
			}
			
			if (_alchemy_id == 6){
				alchemyId -= 1;
			}
//			InputList alchemy_inputs = SC_SYNTHESIS_SMELTING_DESIGN_ACK.getInputList(_alchemy_id);

			InputList alchemy_inputs = SC_SYNTHESIS_SMELTING_DESIGN_ACK.getInputList(alchemyId);
			
			if (_alchemy_id >= 5 && _alchemy_id <= 6){
				if (alchemy_inputs == null){
					return toInvalidInput(pc);
				}
			} else {
				if(alchemy_inputs == null || alchemy_inputs.isInInputs(_inputs) < 1){
					return toInvalidInput(pc);
				}
			}
			
			int fillCount = pc.getInventory().fillSmeltingAlchemyInputItem(_inputs);
			if(fillCount != userInputsSize){
				return toInvalidInputForInventory(pc);
			}
			
			_resultAlchemyId = _alchemy_id;
			_resultCode = SC_SMELTING_MAKE_ACK.ResultCode.RC_FAIL;
			L1Item resultItemTemplate = selectResultItem(alchemy, pc);
			int addCode = pc.getInventory().checkAddItem(resultItemTemplate, 1);
			if(addCode != L1Inventory.OK){
				return doNotAllowInventory(pc, addCode);
			}
			remove_inputs(pc);
			L1ItemInstance result_instance = ItemTable.getInstance().createItem(resultItemTemplate);
//			System.out.println(resultItemTemplate.getItemId());
			//LoggerInstance.getInstance().addSmeltingMake(pc, result_instance, _resultCode == SC_SMELTING_MAKE_ACK.ResultCode.RC_SUCCESS || _resultCode == SC_SMELTING_MAKE_ACK.ResultCode.RC_HYPER_SUCCESS);
			doMake(pc, result_instance);

		} catch (Exception e){
			if(pc != null){
				System.out.println(String.format("[(鍊石合成)異常信息。] 角色名稱：%s", pc.getName()));
				SC_ALCHEMY_MAKE_ACK.ResultCode.RC_ERROR_CURRENTLY_CLOSED.sendCachedMessage(pc);
			}
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public MJIProtoMessage copyInstance(){
		return new CS_SMELTING_MAKE_REQ();
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
	public static class Input implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static Input newInstance(){
			return new Input();
		}
		private L1ItemInstance _temporaryRemovedItems;
		
		private int _slot_no;
		private int _item_name_id;
		private int _item_id;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private Input(){
		}
		
		@Override
		public String toString(){
			StringBuilder sb = new StringBuilder(32);
			sb.append("[slot:").append(_slot_no);
			sb.append(", desc:").append(_item_name_id);
			sb.append(", objectId:").append(_item_id).append("]");
			return sb.toString();
		}
		
		public boolean isInput(L1ItemInstance item){
			return item.getId() == _item_id && item.getItem().getItemDescId() == _item_name_id;
		}
		
		public boolean hasRemoved(){
			return _temporaryRemovedItems != null;
		}
		
		public L1ItemInstance getTemporaryRemovedItems(){
			return _temporaryRemovedItems;
		}
		
		public void setTemporaryRemovedItems(L1ItemInstance item){
			_temporaryRemovedItems = item;
		}
			
	
		
		public int get_slot_no(){
			return _slot_no;
		}
		public void set_slot_no(int val){
			_bit |= 0x1;
			_slot_no = val;
		}
		public boolean has_slot_no(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_item_name_id(){
			return _item_name_id;
		}
		public void set_item_name_id(int val){
			_bit |= 0x2;
			_item_name_id = val;
		}
		public boolean has_item_name_id(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_item_id(){
			return _item_id;
		}
		public void set_item_id(int val){
			_bit |= 0x4;
			_item_id = val;
		}
		public boolean has_item_id(){
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
			if (has_slot_no()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _slot_no);
			}
			if (has_item_name_id()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _item_name_id);
			}
			if (has_item_id()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _item_id);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_slot_no()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_item_name_id()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_item_id()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
			if (has_slot_no()){
				output.wirteInt32(1, _slot_no);
			}
			if (has_item_name_id()){
				output.wirteInt32(2, _item_name_id);
			}
			if (has_item_id()){
				output.wirteInt32(3, _item_id);
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
						set_slot_no(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_item_name_id(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_item_id(input.readInt32());
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
}
