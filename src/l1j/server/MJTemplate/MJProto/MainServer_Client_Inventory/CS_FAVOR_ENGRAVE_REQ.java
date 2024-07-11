package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import java.sql.Timestamp;
import java.util.LinkedList;

import l1j.server.Config;
import l1j.server.CraftList.CraftItemInfo;
import l1j.server.CraftList.CraftListLoader;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.IO.ProtoInputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.CS_CRAFT_MAKE_REQ;
import l1j.server.MJTemplate.MJProto.MainServer_Client.CS_CRAFT_MAKE_REQ.CraftInputItemSlotInfo;
import l1j.server.MJTemplate.MJProto.MainServer_Client.CraftCommonBin;
import l1j.server.MJTemplate.MJProto.MainServer_Client.CraftCommonBin.SC_CRAFT_LIST_ALL_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client.CraftCommonBin.SC_CRAFT_LIST_ALL_ACK.Craft;
import l1j.server.MJTemplate.MJProto.MainServer_Client.CraftCommonBin.SC_CRAFT_LIST_ALL_ACK.Craft.CraftInputList;
import l1j.server.MJTemplate.MJProto.MainServer_Client.CraftCommonBin.SC_CRAFT_LIST_ALL_ACK.Craft.CraftOutputList.CraftOutputSFList.CraftOutputItem;
import l1j.server.MJTemplate.MJProto.MainServer_Client.CraftCommonBin.SC_CRAFT_LIST_ALL_ACK.CraftOutputItemResult;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_MAKE_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_MAKE_ACK.eCraftMakeReqResultType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MESSAGE_NOTI;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.CraftLimitTimeTrigger;
import l1j.server.server.datatables.CraftListNewAllow;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1TreasureBox;
import l1j.server.server.model.item.collection.favor.L1FavorBookInventory;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookObject;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookTypeObject;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookUserObject;
import l1j.server.server.model.item.collection.favor.construct.L1FavorBookListType;
import l1j.server.server.model.item.collection.favor.loader.L1FavorBookLoader;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.templates.L1Item;
import MJShiftObject.Battle.MJShiftBattlePlayManager;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class CS_FAVOR_ENGRAVE_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_FAVOR_ENGRAVE_REQ newInstance(){
		return new CS_FAVOR_ENGRAVE_REQ();
	}
	
	private L1FavorBookInventory _favorInv;
	private L1FavorBookTypeObject _favorType;
	private L1FavorBookObject _favor;
	private L1FavorBookUserObject _favorUser;
	private L1ItemInstance _favorItem;
	private SC_CRAFT_MAKE_ACK.CraftOutputItem outputItemResult;
	
	// CS_CRAFT_MAKE_REQ 변수 선언
	private int _craft_id;
	private int _count;
	private LinkedList<CraftInputItemSlotInfo> _slotInfo;
	
	private int _category;
	private int _slot_id;
	private CS_CRAFT_MAKE_REQ _craft_req;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_FAVOR_ENGRAVE_REQ(){
	}
	public int get_category(){
		return _category;
	}
	public void set_category(int val){
		_bit |= 0x1;
		_category = val;
	}
	public boolean has_category(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_slot_id(){
		return _slot_id;
	}
	public void set_slot_id(int val){
		_bit |= 0x2;
		_slot_id = val;
	}
	public boolean has_slot_id(){
		return (_bit & 0x2) == 0x2;
	}
	public CS_CRAFT_MAKE_REQ get_craft_req(){
		return _craft_req;
	}
	public void set_craft_req(CS_CRAFT_MAKE_REQ val){
		_bit |= 0x4;
		_craft_req = val;
	}
	public boolean has_craft_req(){
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
		if (has_category()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _category);
		}
		if (has_slot_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _slot_id);
		}
		if (has_craft_req()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, _craft_req);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_category()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_slot_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_craft_req()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	
	/**
	 * 성물에 해당하는 제작인지 유효성 검사
	 * @return boolean
	 */
	boolean isValidataion(){
		return _favorType != null && _favor != null && _favorInv != null;
	}
	
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_category()){
			output.wirteInt32(1, _category);
		}
		if (has_slot_id()){
			output.wirteInt32(2, _slot_id);
		}
		if (has_craft_req()){
			output.writeMessage(3, _craft_req);
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
					set_category(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_slot_id(input.readInt32());
					break;
				}
				case 0x0000001A:{
					set_craft_req((CS_CRAFT_MAKE_REQ)input.readMessage(CS_CRAFT_MAKE_REQ.newInstance()));
					break;
				}
				default:{
					return this;
				}
			}
		}
		return this;
	}
	
	private eCraftMakeReqResultType doMake(L1PcInstance pc, CraftOutputItemResult outputResult) {
		int removedCount = pc.getInventory().fillCraftInputItemFavorEx(_slotInfo, _favorItem);
		if (removedCount < _slotInfo.size()) {
			System.out.println(String.format("[(製作)無法在用戶的背包中找到材料資訊。聖物] 角色名稱：%s，製作ID：%d，名稱ID：%d，找到的數量：%d，所需數量：%d", pc.getName(), _craft_id, craft.get_craft_attr().get_desc(), removedCount, _slotInfo.size()));
			System.out.println();
			return eCraftMakeReqResultType.RP_ERROR_NO_REQUIRED_ITEM;
		}
		
		// craft output check.
		CraftOutputItem outputItem = outputResult.outputItem;
		if (outputItem == null) {
			if (outputResult.isSuccess) {
				System.out.println(String.format("[(製作)無法找到輸出物品。] 角色名稱：%s，製作ID：%d，名稱ID：%d", pc.getName(), _craft_id, craft.get_craft_attr().get_desc()));
				return eCraftMakeReqResultType.RP_ERROR_SUCCESS_COUNT_EXCEED;
			}
			eType = eValue = 0;
			for (CraftInputItemSlotInfo sInfo : _slotInfo)
				sInfo.doRemoved(pc.getInventory(), _craft_id);
//				sInfo.doRemovedFavor(pc.getInventory(), _favorInv, _favorUser, _favorItem, _craft_id);
			return eCraftMakeReqResultType.RP_FAILURE;
		}

		// item DB check.
		int inheritItemElemental_descId = outputItem.get_inherit_elemental_enchant_from();
		L1Item l1item = ItemTable.getInstance().findCraftCachedFavorItem(outputItem);
		if (l1item == null) {
			System.out.println(String.format("[(製作)無法找到對應於輸出物品的伺服器物品。] 角色名稱：%s，製作ID：%d，名稱ID：%d，桌面ID：%d", pc.getName(), _craft_id, craft.get_craft_attr().get_desc(), outputItem.get_name_id()));
			return eCraftMakeReqResultType.RP_ERROR_BLOCKED_CRAFT_ID;
		}
		
		// inventory status check.
		int addCode = pc.getInventory().checkAddItem(l1item, outputItem.get_count());
		if (addCode != L1Inventory.OK)
			return (addCode == L1Inventory.WEIGHT_OVER ? eCraftMakeReqResultType.RP_ERROR_WEIGHT_OVER : eCraftMakeReqResultType.RP_ERROR_INVEN_OVER);

		// success.
		L1ItemInstance result	= null;
		L1TreasureBox box		= L1TreasureBox.get(l1item.getItemId());
		boolean isBoxOpen		= box != null && box.open(pc);
		if (isBoxOpen) {
			result				= pc.getFavorBook().getCraftBoxOpenResult();
			pc.getFavorBook().setCraftBoxOpenResult(null);
		} else {
			result				= ItemTable.getInstance().createItem(l1item);// 제작할 아이템 생성
		}
		result.setIdentified(true);

		
		CraftLimitTimeTrigger trigger = CraftLimitTimeTrigger.get_trigger(_craft_id);
		if (trigger != null) {
			L1ItemInstance inherit_input = null;
			if (trigger.get_inherit_item_id() != 0) {
				for (CraftInputItemSlotInfo sInfo : _slotInfo) {
					inherit_input = sInfo.find_temporary_removed_item(trigger.get_inherit_item_id());
					if (inherit_input != null)
						break;
				}
			}
			if (inherit_input != null && inherit_input.getEndTime() != null) {
				result.setEndTime(new Timestamp(inherit_input.getEndTime().getTime()));
			} else {
				result.setEndTime(new Timestamp(System.currentTimeMillis() + trigger.get_limit_millis()));
			}
		}

		eType = eValue = 0;
		if (inheritItemElemental_descId == 0 && outputItem.get_elemental_level() > 0 && outputItem.get_elemental_type() > 0) {
			eType = outputItem.get_elemental_type();
			eValue = outputItem.get_elemental_level();
			result.setAttrEnchantLevel(L1ItemInstance.calculateElementalEnchant(eType, eValue));
		} 

		for (CraftInputItemSlotInfo sInfo : _slotInfo) {
			if (inheritItemElemental_descId != 0 && inheritItemElemental_descId == sInfo.get_item_name_id()) {
				eType = sInfo.get_elemental_enchant_type();
				eValue = sInfo.get_elemental_enchant_value();
				result.setAttrEnchantLevel(L1ItemInstance.calculateElementalEnchant(eType, eValue));
				inheritItemElemental_descId = 0;
			}
			sInfo.doRemovedFavor(pc.getInventory(), _favorInv, _favorUser, _favorItem, _craft_id);// 재료 소모
		}
		// TODO 제작 성공 메세지 출력
		if (outputResult.isSuccess && pc != null && outputResult.outputItem.get_broadcast_desc() > 0) {
			String message = String.format("" + pc.getName() + " 您成功製作了 (%s)。", result.getLogName());
			L1World.getInstance().broadcastPacketToAll(SC_MESSAGE_NOTI.newDropMessage(result.getItem().getItemDescId(), message), true);
		}
		
		pc.getInventory().storeItem(result);// 인벤토리 생성
		if (outputResult.isSuccess) {
			outputItemResult = isBoxOpen ? outputResult.outputItem.toMakeOutputItemTrans(result, eType, eValue)
					: outputResult.outputItem.toMakeOutputItem(eType, eValue);
		}
		
		//수정
		CraftItemInfo limit = CraftListLoader.getInstance().getCraftId(_craft_id);
		if (limit != null && outputResult.isSuccess) {
			limit.add_craft_current_count(outputItem.get_count());
			CraftListLoader.getInstance().updateLimitItem(_craft_id, limit.get_craft_current_count());
		}
		return outputResult.isSuccess ? eCraftMakeReqResultType.RP_SUCCESS : eCraftMakeReqResultType.RP_FAILURE;
	}
	
	private Craft craft;
	private int eType;
	private int eValue;
	
	@Override
	public MJIProtoMessage readFrom(GameClient clnt, byte[] bytes){
		ProtoInputStream is = ProtoInputStream.newInstance(bytes, WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + WireFormat.READ_EXTENDED_SIZE);
		try{
			readFrom(is);

			if (!isInitialized())
				return this;

			l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
			if (pc == null){
				return this;
			}

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Nature.

			
			if (!Config.ServerAdSetting.FavorSystem) {
				 eCraftMakeReqResultType.RP_ERROR_NO_REQUIRED_ITEM.sendCachedMessage(pc);
				 return this;
			}
			
			if (pc.getInventory().getSize() >= 160) {
				eCraftMakeReqResultType.RP_ERROR_INVEN_OVER.sendCachedMessage(pc);
				return this;
			}
			
			if (MJShiftBattlePlayManager.is_shift_battle(pc)) {
				eCraftMakeReqResultType.RP_ERROR_UNKNOWN.sendCachedMessage(pc);
				return this;
			}
			
			_favorType	 	= L1FavorBookLoader.getType(_category);
			_favor			= L1FavorBookLoader.getFavor(L1FavorBookListType.ALL, _favorType, _slot_id);
			_favorInv		= pc.getFavorBook();
			if (!isValidataion()) {
				eCraftMakeReqResultType.RP_ERROR_NO_REQUIRED_ITEM.sendCachedMessage(pc);
				return this;
			}
			
			_favorUser		= _favorInv.getFavorUser(_favor.getListType(), _favor.getType(), _favor.getIndex());
			if (_favorUser != null) {
				_favorItem	= _favorUser.getCurrentItem();
			}
			
			if (_favor.getType().getStartTime() != null && _favor.getType().getEndTime() != null){ //기간 제한 제작
				long currentTime = System.currentTimeMillis();
				if (currentTime < _favor.getType().getStartTime().getTime() || currentTime > _favor.getType().getEndTime().getTime()){
					return this;
				}
			}
			
			_craft_id	= _craft_req.get_craft_id();
			_count		= _craft_req.get_count();
			_slotInfo	= _craft_req.get_slotInfo();
			
			// TODO 특정 제작은 못하도록 !를 한이유는 서버측에서 등록한거 외에는 사용불가
			if (_craft_id != 0) {
				if (!CraftListNewAllow.getInstance().isCraft(_craft_id)) {
					pc.sendPackets("該製作服務暫時中斷，請重新啟動客戶端。");
					if (pc.isGm())
						System.out.println("[CS_CRAFT_MAKE_REQ](GM)1 : (不可)craft_id:" + _craft_id);
					return this;
				}
			}
			
			if (_count <= 0) {
				System.out.println(String.format("[(製作)製作數量異常。] 角色名稱：%s，製作ID：%d，數量：%d", pc.getName(), _craft_id, _count));
				return this;
			}
			
			// craft info check.
			CraftCommonBin ccb = (CraftCommonBin) CraftCommonBin._PROTO_MESSAGE;
			if(ccb == null)
				return this;
			
			
			SC_CRAFT_LIST_ALL_ACK sclaa = ccb.getCraftListAllAck(_craft_id);
			
			if(sclaa == null)
				return this;
			
			
			craft = sclaa.get_craft(_craft_id);
			if (craft == null) {
				eCraftMakeReqResultType.RP_ERROR_CRAFT_DOES_NOT_EXIST.sendCachedMessage(pc);
				System.out.println(String.format("[(製作)無法找到製作ID。] 角色名稱：%s，製作ID：%d", pc.getName(), _craft_id));
				return this;
			}

			// craft input check.
			CraftInputList inputs = craft.get_inputs();
			if (!inputs.has_arr_input_item()) {
				eCraftMakeReqResultType.RP_ERROR_INVALID_INPUT.sendCachedMessage(pc);
				System.out.println(String.format("[(製作)無法找到製作材料物品清單。] 角色名稱：%s，製作ID：%d，名稱ID：$%d", pc.getName(), _craft_id, craft.get_craft_attr().get_desc()));
				return this;
			}

			// craft input slot check.
			int[] calcSlotAndIncreaseProb = inputs.calculateSlotAndIncreaseProb(_slotInfo);
			if (calcSlotAndIncreaseProb[0] - 1 != _slotInfo.size()) {
				eCraftMakeReqResultType.RP_ERROR_INVALID_INPUT.sendCachedMessage(pc);
				System.out.println(String.format("[(製作)材料槽位資訊不一致。] 角色名稱：%s，製作ID：%d，名稱ID：%d，槽位號碼：%d，用戶發送的封包槽位數：%d", pc.getName(), _craft_id, craft.get_craft_attr().get_desc(), calcSlotAndIncreaseProb[0] - 1, _slotInfo.size()));
				return this;
			}

			CraftItemInfo limit = CraftListLoader.getInstance().getCraftId(_craft_id);
			if (limit != null) {
				if (limit.get_craft_current_count() > limit.get_craft_max_count() || _count + limit.get_craft_current_count() > limit.get_craft_max_count()) {
					eCraftMakeReqResultType.RP_ERROR_SUCCESS_COUNT_EXCEED.sendCachedMessage(pc);
					return this;
				}
			}

			for (int i = 0; i < _count; ++i) {
				CraftOutputItemResult outputResult = craft.get_outputs().createOutputItem(calcSlotAndIncreaseProb[1], _craft_id, pc);
				eCraftMakeReqResultType eResult = doMake(pc, outputResult);
				if (eResult.equals(eCraftMakeReqResultType.RP_SUCCESS)) {
					SC_CRAFT_MAKE_ACK.sendResultPacket(pc, eResult, outputItemResult, i + 1, _count);
					if (_count == 1) {
						LoggerInstance.getInstance().addcraftMake(pc, craft.get_craft_attr().get_desc(), "成功", _count);
					}
				} else if (eResult.equals(eCraftMakeReqResultType.RP_FAILURE)) {
					SC_CRAFT_MAKE_ACK.sendResultPacket(pc, eResult, null, i + 1, _count);
					if (_count == 1) {
						LoggerInstance.getInstance().addcraftMake(pc, craft.get_craft_attr().get_desc(), "失敗", _count);
					}
				} else {
					eResult.sendCachedMessage(pc);
					break;
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	
	
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new CS_FAVOR_ENGRAVE_REQ();
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
