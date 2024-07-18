package l1j.server.server.serverpackets;

import java.io.IOException;
import java.util.ArrayList;

import javolution.util.FastMap;
import javolution.util.FastTable;
import l1j.server.ItemSelector.ItemSelectorModel;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemSelectorTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.ItemSelectorTable.SelectorData;
//import l1j.server.server.datatables.ItemSelectorTable.SelectorWarehouseData;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.BinaryOutputStream;

public class S_ItemSelector extends ServerBasePacket {






	
	private static final String S_ITEM_SELECTOR = "[S] S_ItemSelector";
	private byte[] _byte = null;
	
	public static final int ITEM_SELECT = 0x09ca;
	
	private static final FastMap<Integer, byte[]> TEMP_ITEM_BYTE = new FastMap<Integer, byte[]>();

	public S_ItemSelector(L1ItemInstance useItem) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(ITEM_SELECT);

		byte[] tempByte = TEMP_ITEM_BYTE.get(useItem.getItemId());
		// 如果 tempByte 不為空，則寫入 tempByte；
		// 如果為空，則寫入 putTempItemByte 的返回值
		// if(tempByte != null){
		//     writeByte(tempByte);
		// } else {
		writeByte(putTempItemByte(useItem.getItemId()));
		// }

		writeC(0x10); // 寫入物品物件 ID，會被 c 封包調用
		writeBit(useItem.getId());

		writeC(0x18); // 寫入物品描述 ID，UI 物品名稱
		writeBit(useItem.getItem().getItemDescId());

		writeH(0x00);
	}
	
	private byte[] putTempItemByte(int useitemId){
		BinaryOutputStream os = new BinaryOutputStream();

		try{
			L1Item temp = null;
			if (ItemSelectorTable.getSelectorInfo(useitemId) != null){
				for(SelectorData data : ItemSelectorTable.getSelectorInfo(useitemId)){
					temp = ItemTable.getInstance().getTemplate(data._selectItemId);
					if(temp == null){
						System.out.println("S_ItemSelector - SelectorData itemTempInfo empty itemId : " + data._selectItemId);
						continue;
					}
					int descId = temp.getItemDescId();
					int attrvalue = 0;
					int attrtype = 0;
					int length = 0;
					if (data._attrLevel != 0) {
						if (data._attrLevel >= 1 && data._attrLevel <= 5) {
							attrtype = 1;
							attrvalue = data._attrLevel;
						}
						if (data._attrLevel >= 6 && data._attrLevel <= 10) {
							attrtype = 2;
							attrvalue = data._attrLevel - 5;
						}
						if (data._attrLevel >= 11 && data._attrLevel <= 15) {
							attrtype = 3;
							attrvalue = data._attrLevel - 10;
						}
						if (data._attrLevel >= 16 && data._attrLevel <= 20) {
							attrtype = 4;
							attrvalue = data._attrLevel - 15;
						}
						length += 4;
					}
					if (data._enchantLevel != 0) {
						length += 2;
					}
					if (data._count != 0) {
						length += os.getBitSize(data._count)+1;
					}
					os.writeC(0x0a);
					os.writeC(os.getBitSize(descId) + 1 + length);
					os.writeC(0x08);
					os.writeBit(descId);
					if (data._attrLevel != 0) {
						os.writeC(0x10); // 屬性附魔類型
						os.writeC(attrtype);
						os.writeC(0x18); // 屬性附魔值
						os.writeC(attrvalue);
					}
					if (data._enchantLevel != 0) {
						os.writeC(0x20); // 附魔值
						os.writeC(data._enchantLevel);
					}
					if (data._count != 0) {
						os.writeC(0x28); // 數量
						os.writeC(data._count);
					}
				}
				TEMP_ITEM_BYTE.put(useitemId, os.getBytes());
				return os.getBytes();
			} else {
				System.out.println(ItemSelectorTable.getSelectorInfo(useitemId));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(os != null){
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public S_ItemSelector(L1ItemInstance useItem, FastTable<Integer> list) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(ITEM_SELECT);

		writeByte(getTempItemByte(list));

		writeC(0x10); // 寫入物品物件 ID，會被 c 封包調用
		writeBit(useItem.getId());

		writeC(0x18); // 寫入物品描述 ID，UI 物品名稱
		writeBit(useItem.getItem().getItemDescId());

		writeH(0x00);
	}
	
	private byte[] getTempItemByte(FastTable<Integer> list){
		BinaryOutputStream os = new BinaryOutputStream();
		try{
			L1Item temp = null;
			for(int itemIds : list){
				temp = ItemTable.getInstance().getTemplate(itemIds);
				if(temp == null){
					System.out.println("S_ItemSelector - SelectorData melt empty itemId : " + itemIds);
					continue;
				}
				int descId = temp.getItemDescId();
				os.writeC(0x0a);
				os.writeC(os.getBitSize(descId) + 1);
				
				os.writeC(0x08);
				os.writeBit(descId);
			}
			return os.getBytes();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(os != null){
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	// 倉庫格式輸出(C_BUY_SELL)
	public S_ItemSelector(L1PcInstance pc, int useItemId, FastTable<SelectorData> list){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(0x0408);
		
		writeC(0x08);// objid
		writeBit(useItemId);
		
		writeC(0x10);// size
		writeBit(list.size());
		
		writeC(0x18);// type
		writeBit(3);
		
		writeC(0x20);// price
		writeBit(1);

		writeC(0x28);
		writeBit(180);

		for(SelectorData data : list){
			if(data == null || data._item == null){
				System.out.println("S_ItemSelector - SelectorWarehouseData empty itemId : " + data._itemId);
				continue;
			}
			writeC(0x32);
			writeBytesWithLength(itemDetailBytes(pc, data._index, data._item));
		}
		
		writeC(0x38);
		writeC(0x01);
		
		writeH(0x00);
	}

	
	private byte[] itemDetailBytes(L1PcInstance pc, int index, L1ItemInstance item) {

		BinaryOutputStream os = new BinaryOutputStream();
		os.writeC(0x08);
		os.writeBit(index);
		os.writeC(0x12);
		os.writeBytesWithLength(pc.sendItemPacket(pc, item));
		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return os.getBytes();
	}

	
	@Override
	public byte[] getContent() {
		if(_byte == null)_byte = _bao.toByteArray();
		return _byte;
	}

	@Override
	public String getType() {
		return S_ITEM_SELECTOR;
	}
}


