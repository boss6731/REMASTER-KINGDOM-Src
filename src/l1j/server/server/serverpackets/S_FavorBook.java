package l1j.server.server.serverpackets;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.favor.L1FavorBookInventory;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookObject;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookRegistObject;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookTypeObject;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookUserObject;
import l1j.server.server.model.item.collection.favor.construct.L1FavorBookInventoryStatus;
import l1j.server.server.model.item.collection.favor.construct.L1FavorBookItemStatus;
import l1j.server.server.model.item.collection.favor.construct.L1FavorBookListType;
import l1j.server.server.model.item.collection.favor.loader.L1FavorBookLoader;
import l1j.server.server.utils.BinaryOutputStream;
import l1j.server.server.utils.StringUtil;

public class S_FavorBook extends ServerBasePacket {
	private static final String S_FAVOR_BOOK = "[S] S_FavorBook";
	private byte[] _byte = null;
	
	public static final int LIST		= 0x0a5a;
	public static final int INVENTORY	= 0x0a5c;
	
	public S_FavorBook(L1PcInstance pc, L1FavorBookInventory favorInv, L1FavorBookListType listType){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(LIST);
//		System.out.println("열렸나?");
		switch(listType){
		case ALL:
			ConcurrentHashMap<L1FavorBookListType, ConcurrentHashMap<L1FavorBookTypeObject, ArrayList<L1FavorBookObject>>> all = L1FavorBookLoader.getAllData();
			if (all != null && !all.isEmpty()) {
				ConcurrentHashMap<L1FavorBookTypeObject, ArrayList<L1FavorBookObject>> value = null;
				for (Map.Entry<L1FavorBookListType, ConcurrentHashMap<L1FavorBookTypeObject, ArrayList<L1FavorBookObject>>> entry : all.entrySet()) {
					value	= entry.getValue();
					if (value == null || value.isEmpty()) {
						continue;
					}
					ArrayList<L1FavorBookObject> list	= null;
					for (Map.Entry<L1FavorBookTypeObject, ArrayList<L1FavorBookObject>> entry2 : value.entrySet()) {
						list	= entry2.getValue();
						if (list == null || list.isEmpty()) {
							continue;
						}
						writeC(0x0a);
						writeBytesWithLength(getListBytes(pc, favorInv, entry.getKey(), entry2.getKey(), list));
					}
				}
			}
			break;
		default:
			ConcurrentHashMap<L1FavorBookTypeObject, ArrayList<L1FavorBookObject>> map = L1FavorBookLoader.getListType(listType);
			if (map != null && !map.isEmpty()) {
				for (Map.Entry<L1FavorBookTypeObject, ArrayList<L1FavorBookObject>> entry : map.entrySet()) {
					writeC(0x0a);
					writeBytesWithLength(getListBytes(pc, favorInv, listType, entry.getKey(), entry.getValue()));
				}
			}
			break;
		}

        writeH(0x00);
	}
	
	public S_FavorBook(L1PcInstance pc, L1FavorBookUserObject user, L1FavorBookInventoryStatus status){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(INVENTORY);
		
		writeC(0x08);
		writeC(0x00);
		
		writeC(0x12);
		writeBytesWithLength(getInventoryBytes(pc, user));
		
		writeC(0x18);
		writeC(status.getStatus());// 0: 획득, 1:목록
		
        writeH(0x00);
	}
	
	private byte[] getListBytes(L1PcInstance pc, L1FavorBookInventory favorInv, L1FavorBookListType firstKey, L1FavorBookTypeObject type, ArrayList<L1FavorBookObject> list){
		BinaryOutputStream os = null;
		try {
			os = new BinaryOutputStream();
			os.writeC(0x0a);
			os.writeBytesWithLength(type.getDesc().getBytes());
			
			os.writeC(0x12);// 시작 시간
			if (StringUtil.isNullOrEmpty(type.getStartTimeToString())) {
				os.writeC(0x00);
			} else {
				os.writeBytesWithLength(type.getStartTimeToString().getBytes());
			}
			
			os.writeC(0x1a);// 종료 시간
			if (StringUtil.isNullOrEmpty(type.getEndTimeToString())) {
				os.writeC(0x00);
			} else {
				os.writeBytesWithLength(type.getEndTimeToString().getBytes());
			}
			
			os.writeC(0x20);
			os.writeC(firstKey.getType());
			
			for (L1FavorBookObject obj : list) {
				os.writeC(0x2a);
				os.writeBytesWithLength(getListItemBytes(pc, favorInv, obj));// 길이
			}
			return os.getBytes();
		} catch(Exception e) {
	    	e.printStackTrace();
	    } finally {
	    	if (os != null) {
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
	
	private byte[] getListItemBytes(L1PcInstance pc, L1FavorBookInventory favorInv, L1FavorBookObject obj){
		BinaryOutputStream os = null;
		try {
			os = new BinaryOutputStream();
			L1FavorBookUserObject user = favorInv.getFavorUser(obj.getListType(), obj.getType(), obj.getIndex());
			writeDetail(os, pc, obj, user == null ? null : user.getCurrentItem());
			return os.getBytes();
		} catch(Exception e) {
	    	e.printStackTrace();
	    } finally {
	    	if (os != null) {
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
	
	private byte[] getInventoryBytes(L1PcInstance pc, L1FavorBookUserObject user){
		BinaryOutputStream os = null;
		try {
			os = new BinaryOutputStream();
			writeDetail(os, pc, user.getObj(), user.getCurrentItem());
			return os.getBytes();
		} catch(Exception e) {
	    	e.printStackTrace();
	    } finally {
	    	if (os != null) {
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

	private void writeDetail(BinaryOutputStream os, L1PcInstance pc, L1FavorBookObject obj, L1ItemInstance item) {
		int descId = item == null ? 0 : item.getItem().getItemDescId(); // 如果 item 為 null，則 descId 為 0，否則為 item 的描述 ID
		int bless = item == null ? -1 : item.getBless(); // 如果 item 為 null，則 bless 為 -1，否則為 item 的祝福狀態

		L1FavorBookRegistObject regist = obj.getRegister(descId, bless); // 從 obj 獲取對應 descId 和 bless 的註冊對象

		os.writeC(0x08); // 神物類型
		os.writeC(obj.getType().getType()); // 寫入神物類型

		os.writeC(0x10); // 神物順序
		os.writeC(obj.getIndex()); // 寫入神物順序

		os.writeC(0x18); // 描述 ID
		os.writeBit(descId); // 寫入描述 ID

		os.writeC(0x20); // 製作號
		os.writeBit(regist == null ? 0x00 : regist.getCraftId()); // 如果 regist 為 null，則寫入 0x00，否則寫入註冊對象的製作 ID

		if (item != null) {
			os.writeC(0x28); // 0:製作, 1:強化, 2:祝福, 3:變更
			os.writeC(regist == null ? L1FavorBookItemStatus.CRAFT.getStatus() : regist.getStatus().getStatus()); // 如果 regist 為 null，則寫入製作狀態，否則寫入註冊對象的狀態

			// 以下為新增部分
			byte[] itemBytes = pc.sendItemPacket(pc, item); // 獲取物品的封包數據
			int itemLength = itemBytes.length + 12 + os.getBitSize(item.getBless()) + os.getBitSize(item.getEnchantLevel()) + os.getBitSize(item.getItem().getWeight()); // 計算物品數據的總長度

			os.writeC(0x3a); // 固定數據
			os.writeBit(itemLength); // 寫入物品數據的長度
			os.writeByte(itemBytes); // 寫入物品的封包數據

			os.writeC(0xb0); // 固定數據
			os.writeC(0x01); // 固定數據
			os.writeBit(item.getBless()); // 寫入物品的祝福狀態

			os.writeC(0xb8); // 固定數據
			os.writeC(0x01); // 固定數據
			os.writeBit(item.getEnchantLevel()); // 寫入物品的強化等級

			os.writeC(0xc0); // 固定數據
			os.writeC(0x01); // 固定數據
			os.writeC(item.getItem().isStackable() ? 0x01 : 0x00); // 寫入物品是否可堆疊

			os.writeC(0xc8); // 固定數據
			os.writeC(0x01); // 固定數據
			os.writeBit(item.getItem().getWeight()); // 寫入物品的重量

			os.writeC(0xd0); // 固定數據
			os.writeC(0x01); // 固定數據
			os.writeC(item.isIdentified() ? 0x01 : 0x00); // 寫入物品是否已鑑定
		}
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_FAVOR_BOOK;
	}
}


