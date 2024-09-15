package l1j.server.server.clientpackets;


import java.io.UnsupportedEncodingException;

import javolution.util.FastMap;
import l1j.server.MJCompanion.Instance.MJCompanionInstanceCache;
import l1j.server.MJDShopSystem.MJDShopItem;
import l1j.server.MJDShopSystem.MJDShopStorage;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.server.ActionCodes;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.server.Controller.FishingTimeController;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ChangeShape;
import l1j.server.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_DoActionShop;
import l1j.server.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;

public class C_Shop extends ClientBasePacket {

	private static final String C_SHOP = "[C] C_Shop";

	public C_Shop(byte abyte0[], GameClient clientthread) {
		super(abyte0);

		L1PcInstance pc = clientthread.getActiveChar();
		if (pc == null || pc.isGhost()) {
			return;
		}
		if (pc.isInvisble()) {
			pc.sendPackets(String.valueOf(new S_ServerMessage(755)));
			return;
		}
		if (pc.getMapId() != 800) {
			pc.sendPackets(String.valueOf(new S_SystemMessage("個人商店只能在市場開設。")));
			return;
		}
		
		if (pc.getMapId() != 800) {
			if (pc.isFishing()) {
				try {
					pc.setFishing(false);
					pc.setFishingTime(0);
					pc.setFishingReady(false);
					pc.sendPackets(new S_CharVisualUpdate(pc));
					Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(pc));
					FishingTimeController.getInstance().removeMember(pc);
					pc.sendPackets(String.valueOf(new S_ServerMessage(2120)));
					return;
				} catch (Exception e) {
				}
			} else {
				pc.sendPackets(String.valueOf(new S_ServerMessage(3405)));
				return;
			}
		}

		if (pc.getInventory().checkEquipped(22232) || pc.getInventory().checkEquipped(22234) || 
			pc.getInventory().checkEquipped(22233) || pc.getInventory().checkEquipped(22235) ||	
			pc.getInventory().checkEquipped(22236) || pc.getInventory().checkEquipped(22237) || 
			pc.getInventory().checkEquipped(22238) || pc.getInventory().checkEquipped(22239) || 
			pc.getInventory().checkEquipped(22240) || pc.getInventory().checkEquipped(22241) ||
			pc.getInventory().checkEquipped(22242) || pc.getInventory().checkEquipped(22243) || 
			pc.getInventory().checkEquipped(22244) || pc.getInventory().checkEquipped(22245) ||
			pc.getInventory().checkEquipped(22246) || pc.getInventory().checkEquipped(22247) ||
		if (pc.getInventory().checkEquipped(22248) || pc.getInventory().checkEquipped(22249)) { // 符文防具
			pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc),"如果您穿戴了符文，請解除。")));
			return;
		}

		if (pc.getCurrentSpriteId() != pc.getClassId() && pc.getSkillEffectTimeSec(L1SkillId.SHAPE_CHANGE) <= 0) {
			pc.sendPackets(String.valueOf(new S_SystemMessage("請解除變身道具。")));
			return;
		}

		L1ItemInstance checkItem;
		boolean tradable = true;

		
		int type = readC();
		if (type == 0) { // 開始
			
			int sellTotalCount = readH();
			int sellObjectId;
			int sellPrice;
			int sellCount;
			Object[] petlist = null;
			for (int i = 0; i < sellTotalCount; i++) {
				sellObjectId 	= readD();
				sellPrice 		= readD();
				sellCount 		= readD();

				/** 修正個人商店錯誤 */
				if(sellTotalCount == 8){
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc),"最多只能登記7件物品。")));
					return;
				}

				// 檢查可交易的物品
				checkItem = pc.getInventory().getItem(sellObjectId);
				if ((sellObjectId != checkItem.getId()) || (!checkItem.isStackable() && sellCount != 1) || (checkItem.getCount() < sellCount || checkItem.getCount() <= 0 || sellCount <= 0)) {
					/** 2016.11.24 MJ App Center 市價 **/
					pc.disposeShopInfo();
					/** 2016.11.24 MJ App Center 市價 **/
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (!checkItem.isStackable() && sellCount != 1) {
					pc.sendPackets(new S_Disconnect());
					/** 2016.11.24 MJ App Center 市價 **/
					pc.disposeShopInfo();
					/** 2016.11.24 MJ App Center 市價 **/
					return;
				}
				if (sellCount > checkItem.getCount()) {
					sellCount = checkItem.getCount();
				}

				if(checkItem.getBless() >= 128){
					pc.sendPackets(String.valueOf(new S_ServerMessage(210, checkItem.getItem().getName()))); // 1%0無法丟棄或轉讓給他人。
					/** 2016.11.24 MJ App Center 市價 **/
					pc.disposeShopInfo();
					/** 2016.11.24 MJ App Center 市價 **/
					return;
				}
				if (!checkItem.getItem().isTradable()) {
					tradable = false;
					pc.sendPackets(String.valueOf(new S_ServerMessage(166, checkItem.getItem().getName(), "無法交易。")));
				}

				if (checkItem.get_Carving() != 0) {
					tradable = false;
					pc.sendPackets(new S_SystemMessage("刻印的物品無法交易。"), true);
				}

				if(!MJCompanionInstanceCache.is_companion_oblivion(checkItem.getId())){
					pc.disposeShopInfo();
					pc.sendPackets(String.valueOf(new S_ServerMessage(166, checkItem.getItem().getName(), "無法交易。")));
					return;
				}

				petlist = pc.getPetList().values().toArray();
				for (Object petObject : petlist) {
					if (petObject instanceof L1PetInstance) {
						L1PetInstance pet = (L1PetInstance) petObject;
						if (checkItem.getId() == pet.getItemObjId()) {
							tradable = false;
							pc.sendPackets(String.valueOf(new S_ServerMessage(166, checkItem.getItem().getName(), "無法交易。")));
							break;
						}
					}
				}

				L1DollInstance doll = pc.getMagicDoll();
				if(doll != null) {
					if (checkItem.getId() == doll.getItemObjId()) {
						tradable = false;
						pc.sendPackets(String.valueOf(new S_ServerMessage(166, checkItem.getItem().getName(), "無法交易。")));
						break;
					}
				}

			/** 2016.11.24 MJ App Center 市價 **/
			//pc.addSellings(MJDShopItem.create(checkItem, sellCount, sellPrice, false));
			/** 2016.11.24 MJ App Center 市價 **/
			}
			int buyTotalCount = readH();
			int buyObjectId;
			int buyPrice;
			int buyCount;
			for (int i = 0; i < buyTotalCount; i++) {
				buyObjectId = readD();
				buyPrice = readD();
				buyCount = readD();

				/** 修正個人商店錯誤 */
				if(sellTotalCount == 8){
					pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc),"最多只能登記7件物品。")));
					return;
				}
				// 檢查可交易的物品
				checkItem = pc.getInventory().getItem(buyObjectId);
				/*防止漏洞*/
				if ((buyObjectId != checkItem.getId()) || (!checkItem.isStackable() && buyCount != 1) || (buyCount <= 0 || checkItem.getCount() <= 0)) {
				/** 2016.11.24 MJ App Center 市價 **/
					pc.disposeShopInfo();

					pc.sendPackets(new S_Disconnect());
					return;
				}
				
				if (buyCount > checkItem.getCount()) {
					buyCount = checkItem.getCount();
				}
				/*防止漏洞*/
				// 檢查可交易的物品
				checkItem = pc.getInventory().getItem(buyObjectId);
				if (!checkItem.getItem().isTradable()) {
					tradable = false;
					pc.sendPackets(String.valueOf(new S_ServerMessage(166, checkItem.getItem().getName(), "無法交易。")));
				}

				if (checkItem.get_Carving() != 0) {
					tradable = false;
					pc.sendPackets(new S_SystemMessage("刻印的物品無法交易。"), true);
				}

				if(!MJCompanionInstanceCache.is_companion_oblivion(checkItem.getId())){
					pc.disposeShopInfo();
					pc.sendPackets(String.valueOf(new S_ServerMessage(166, checkItem.getItem().getName(), "無法交易。")));
					return;
				}

				petlist = pc.getPetList().values().toArray();
				for (Object petObject : petlist) {
					if (petObject instanceof L1PetInstance) {
						L1PetInstance pet = (L1PetInstance) petObject;
						if (checkItem.getId() == pet.getItemObjId()) {
							tradable = false;
							pc.sendPackets(String.valueOf(new S_ServerMessage(166, checkItem.getItem().getName(), "無法交易。")));
							break;
						}
					}
				}

				/** 2016.11.24 MJ App Center 市價 **/
				//pc.addPurchasings(MJDShopItem.create(checkItem, buyCount, buyPrice, true));

			}
			if (!tradable) { // 包含無法交易的物品時，結束個人商店
				/** 2016.11.24 MJ App Center 市價 **/
				pc.disposeShopInfo();
				
				pc.setPrivateShop(false);
				pc.sendPackets(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
				pc.broadcastPacket(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
				return;
			}
			byte[] chat = readByte();
			String test;
			int poly;
			test = null;
			try {
				test = new String(chat, 0, chat.length, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			pc.getNetConnection().getAccount().updateShopOpenCount();
			pc.sendPackets(new S_PacketBox(S_PacketBox.SHOP_OPEN_COUNT, pc.getNetConnection().getAccount().Shop_open_count), true);
			
			pc.setShopChat(chat);
			pc.setPrivateShop(true);
			pc.sendPackets(new S_DoActionShop(pc.getId(), ActionCodes.ACTION_Shop, chat));
			pc.broadcastPacket(new S_DoActionShop(pc.getId(), ActionCodes.ACTION_Shop, chat));
			pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), ".無人商店後，可以登入其他角色。", 1)));
			
			poly = 0;
			if (test.matches(".*tradezone1.*"))
				poly = 11479;
			else if (test.matches(".*tradezone2.*"))
				poly = 11483;
			else if (test.matches(".*tradezone3.*"))
				poly = 11480;
			else if (test.matches(".*tradezone4.*"))
				poly = 11485;
			else if (test.matches(".*tradezone5.*"))
				poly = 11482;
			else if (test.matches(".*tradezone6.*"))
				poly = 11486;
			else if (test.matches(".*tradezone7.*"))
				poly = 11481;
			else if (test.matches(".*tradezone8.*")) {
				poly = 11484;
			}
			test = null;
			pc.privateShopTransform = poly;
			pc.setCurrentSprite(poly);
			pc.sendPackets(new S_ChangeShape(pc.getId(), poly, 70));
			Broadcaster.broadcastPacket(pc, new S_ChangeShape(pc.getId(), poly, 70));
			pc.sendPackets(new S_CharVisualUpdate(pc));
//			pc.broadcastPacket(S_WorldPutObject.get(pc));
			pc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc));
			//Broadcaster.broadcastPacket(pc, new S_OtherCharPacks(pc));
			Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(pc));
			pc.curePoison();
			//** 2016.11.24 MJ App Center 市價 **/
			GeneralThreadPool.getInstance().execute(new MJDShopStorage(pc, false));
			//** 2016.11.24 MJ App Center 市價 **/
		} else if (type == 1) { // 結束
			pc.setPrivateShop(false);
			pc.privateShopTransform = 0;
			pc.sendPackets(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
			pc.broadcastPacket(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
			L1PolyMorph.undoPolyPrivateShop(pc);
			//** 2016.11.24 MJ App Center 市價 **/
			GeneralThreadPool.getInstance().execute(new MJDShopStorage(pc, true));
			//** 2016.11.24 MJ App Center 市價 **/
		}
	}

	private static FastMap<String, Integer> shopCreationAccountCount = new FastMap<String, Integer>();

	public static boolean getShopCreationAccountCount(String account) {
		synchronized (shopCreationAccountCount) {
			int time = 0;
			try {
				time = shopCreationAccountCount.get(account);
			} catch (Exception e) {
			}
			if (time >= 50) {
				return false;
			}
			shopCreationAccountCount.put(account, time + 1); // 修正自增操作
			return true;
		}
	}

	public static void resetShopCreationAccountCount() {
		synchronized (shopCreationAccountCount) {
			shopCreationAccountCount.clear();
		}
	}
	
	@Override
	public String getType() {
		return C_SHOP;
	}

}
