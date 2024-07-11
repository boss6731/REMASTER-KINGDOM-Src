package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javolution.util.FastTable;


import l1j.server.L1DatabaseFactory;
import l1j.server.ItemSelector.ItemSelectorLoader;
import l1j.server.ItemSelector.ItemSelectorModel;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemSelectorTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.datatables.ItemSelectorTable.SelectorData;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class CS_SELECTION_BAG_I_WANT_IT_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_SELECTION_BAG_I_WANT_IT_REQ newInstance(){
		return new CS_SELECTION_BAG_I_WANT_IT_REQ();
	}
	private int _bag_obj_id;
	private int _want_item_name_id;
	private int _use_count;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_SELECTION_BAG_I_WANT_IT_REQ(){
	}
	public int get_bag_obj_id(){
		return _bag_obj_id;
	}
	public void set_bag_obj_id(int val){
		_bit |= 0x1;
		_bag_obj_id = val;
	}
	public boolean has_bag_obj_id(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_want_item_name_id(){
		return _want_item_name_id;
	}
	public void set_want_item_name_id(int val){
		_bit |= 0x2;
		_want_item_name_id = val;
	}
	public boolean has_want_item_name_id(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_use_count(){
		return _use_count;
	}
	public void set_use_count(int val){
		_bit |= 0x4;
		_use_count = val;
	}
	public boolean has_use_count(){
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
		if (has_bag_obj_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _bag_obj_id);
		}
		if (has_want_item_name_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _want_item_name_id);
		}
		if (has_use_count()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _use_count);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_bag_obj_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_want_item_name_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_bag_obj_id()){
			output.wirteInt32(1, _bag_obj_id);
		}
		if (has_want_item_name_id()){
			output.wirteInt32(2, _want_item_name_id);
		}
		if (has_use_count()){
			output.wirteInt32(3, _use_count);
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
					set_bag_obj_id(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_want_item_name_id(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_use_count(input.readInt32());
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
			
			
			int enchant = 0;	
			
			L1ItemInstance useItem = pc.getInventory().getItem(_bag_obj_id);
			
			if(useItem == null){
				return this;
			}

			FastTable<SelectorData> selectors = ItemSelectorTable.getSelectorInfo(useItem.getItemId());
			if(selectors == null || selectors.isEmpty()){
				return this;
			}
			if(_want_item_name_id <= 0){
				return this;
			}
			
			L1Item selectItem = ItemTable.getInstance().findDescId(_want_item_name_id, 1);			
			if (selectItem == null){
				L1Item selectItemBless = ItemTable.getInstance().findDescId(_want_item_name_id, 0);
				selectItem = selectItemBless;
			}
			
			SelectorData selectorData = null;
			FastTable<SelectorData> list = ItemSelectorTable.getInstance().getSelectorInfo(useItem.getItemId());
			if (list == null || list.isEmpty()) {
				return this;
			}
			for (SelectorData data : list) {
				if (data == null) {
					continue;
				}
				if (data._selectItemId == selectItem.getItemId()) {
					selectorData = data;
					break;
				}
			}
			
			if (selectorData == null) {
				System.out.println("E_ItemSelect selectItem empty descId -> " + _want_item_name_id);
				return this;
			}
			
			int count = pc.getInventory().checkItemCount(useItem.getItemId());
			if (_use_count <0) {
				return this;
			}
			if (_use_count >= count) {
				_use_count = count;
			}
			int countitem = selectorData._count;
			if (countitem == 0) {
				countitem = 1;
			}
			
				
				
/*				if (new_item.isStackable()) {
					L1PcInventory targetInven = pc.getInventory();
					L1ItemInstance new_item = ItemTable.getInstance().createItem(selectItem.getItemId());
					new_item.setEnchantLevel(selectorData._enchantLevel);
					new_item.setCount(countitem * _use_count);
					new_item.setAttrEnchantLevel(selectorData._attrLevel);

					new_item.setIdentified(true);
					targetInven.storeItem(new_item, true);	
		
				} else {*/
			L1PcInventory targetInven = null;
			L1ItemInstance new_item = null;
					for (int i = 0; i< countitem * _use_count; i++) {
						targetInven = pc.getInventory();
						new_item = ItemTable.getInstance().createItem(selectItem.getItemId());
						new_item.setEnchantLevel(selectorData._enchantLevel);
//						new_item.setCount(countitem * _use_count);
						new_item.setAttrEnchantLevel(selectorData._attrLevel);
						if (selectorData._endTime != 0) {
							Timestamp deleteTime = new Timestamp(System.currentTimeMillis() + (selectorData._endTime * 60 * 60 * 1000));
							new_item.setEndTime(deleteTime);
						}
						new_item.setIdentified(true);
						targetInven.storeItem(new_item, true);	
					}
/*
					
					new_item.setEnchantLevel(selectorData._enchantLevel);
	//				new_item.setCount(countitem);
					new_item.setAttrEnchantLevel(selectorData._attrLevel);
					if (selectorData._endTime != 0) {
						Timestamp deleteTime = new Timestamp(System.currentTimeMillis() + (selectorData._endTime * 60 * 60 * 1000));
						new_item.setEndTime(deleteTime);
					}
					new_item.setIdentified(true);
					for (int i = 0; i < countitem * _use_count; i++) {
						targetInven.storeItem(new_item, true);		
					}
				}*/
			pc.getInventory().consumeItem(useItem.getItemId(), _use_count);
			

		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new CS_SELECTION_BAG_I_WANT_IT_REQ();
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
