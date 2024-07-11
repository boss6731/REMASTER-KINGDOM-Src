package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import java.io.IOException;

import l1j.server.MJCompanion.Instance.MJCompanionInstanceCache;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ItemName;

// TODO: 自動生成的 PROTO 代碼。由 MJSoft 製作。
public class SC_UPDATE_INVENTORY_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {

	/**
	 * 當娃娃召喚時更新庫存通知
	 *
	 * @param pc 玩家實例
	 * @param item_object_id 物品對象ID
	 * @param is_summoned 是否召喚
	 */
	public static void on_doll_summoned(L1PcInstance pc, int item_object_id, boolean is_summoned) {
		// 根據召喚狀態更新物品信息
		ItemInfo iInfo = ItemInfo.do_doll_summoned(item_object_id, is_summoned);
		// 創建新的通知實例
		SC_UPDATE_INVENTORY_NOTI noti = SC_UPDATE_INVENTORY_NOTI.newInstance();
		// 添加物品信息到通知中
		noti.add_item_info(iInfo);
		// 發送更新庫存通知給玩家
		pc.sendPackets(noti, MJEProtoMessages.SC_UPDATE_INVENTORY_NOTI, true);
	}

	/**
	 * 發送寵物控制物品名稱
	 *
	 * @param pc 玩家實例
	 * @param item_object_id 物品對象ID
	 * @param class_npc_name_id NPC名稱ID
	 * @param level 等級
	 */
	public static void send_companion_control_item_name(L1PcInstance pc, int item_object_id, String class_npc_name_id, int level) {
		// 格式化描述文本
		String description = String.format("寵物項圈 (%s LV %d)", class_npc_name_id, level);
		// 發送修改名稱的封包給玩家
		pc.sendPackets(new S_ItemName(item_object_id, description));
	}

	/**
	 * 發送寵物控制物品
	 *
	 * @param pc 玩家實例
	 * @param item_object_id 物品對象ID
	 * @param cache 寵物實例緩存
	 */
	public static void send_companion_control_item(L1PcInstance pc, int item_object_id, MJCompanionInstanceCache cache) {
		if (pc == null)
			return;

		// 根據物品對象ID和緩存創建新的物品信息
		ItemInfo iInfo = ItemInfo.newInstance(item_object_id, cache);
		// 創建新的通知實例
		SC_UPDATE_INVENTORY_NOTI noti = SC_UPDATE_INVENTORY_NOTI.newInstance();
		// 添加物品信息到通知中
		noti.add_item_info(iInfo);
		// 發送更新庫存通知給玩家
		pc.sendPackets(noti, MJEProtoMessages.SC_UPDATE_INVENTORY_NOTI, true);
	}
}
	
	public static SC_UPDATE_INVENTORY_NOTI newInstance(){
		return new SC_UPDATE_INVENTORY_NOTI();
	}
	private java.util.LinkedList<ItemInfo> _item_info;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_UPDATE_INVENTORY_NOTI(){
	}
	public java.util.LinkedList<ItemInfo> get_item_info(){
		return _item_info;
	}
	public void add_item_info(ItemInfo val){
		if(!has_item_info()){
			_item_info = new java.util.LinkedList<ItemInfo>();
			_bit |= 0x1;
		}
		_item_info.add(val);
	}
	public boolean has_item_info(){
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
		if (has_item_info()){
			for(ItemInfo val : _item_info)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_item_info()){
			for(ItemInfo val : _item_info){
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
		if (has_item_info()){
			for(ItemInfo val : _item_info){
				output.writeMessage(1, val);
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
				case 0x0000000A:{
					add_item_info((ItemInfo)input.readMessage(ItemInfo.newInstance()));
					break;
				}
			}
		}
		return this;
	}
	@override
	public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
		// 創建 ProtoInputStream 實例，用於從字節數組中讀取數據
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(
				bytes,
				l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
				((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE
		);
		try {
			// 從流中讀取數據
			readFrom(is);

			// 檢查對象是否已初始化
			if (!isInitialized())
				return this;

			// TODO: 在此處插入處理代碼。由 MJSoft 製作。

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@override
	public MJIProtoMessage copyInstance() {
		// 返回一個新的 SC_UPDATE_INVENTORY_NOTI 實例
		return new SC_UPDATE_INVENTORY_NOTI();
	}

	@override
	public MJIProtoMessage reloadInstance() {
		// 調用 newInstance 方法，返回一個新的 SC_UPDATE_INVENTORY_NOTI 實例
		return newInstance();
	}

	@override
	public void dispose() {
		// 如果有 item_info，則處理和清理
		if (has_item_info()) {
			for (ItemInfo val : _item_info)
				val.dispose();
			_item_info.clear();
			_item_info = null;
		}
		// 重置字段值
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
