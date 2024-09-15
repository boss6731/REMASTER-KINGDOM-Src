package l1j.server.server.model.shop;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import l1j.server.Config;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_POINT_POINT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_GOODS_INVEN_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_OBTAINED_ITEM_INFO;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_CONTRIBUTION_ACK;
import l1j.server.MJWarSystem.MJCastleWar;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.NpcShopCash.NpcShopCashTable;
import l1j.server.PowerBall.PowerBallController;
import l1j.server.server.Controller.BugRaceController;
import l1j.server.server.Controller.BugRaceController.BugTicketInfo;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NpcCashShopTable;
import l1j.server.server.datatables.NpcShopTable;
import l1j.server.server.datatables.NpcShopTable2;
import l1j.server.server.datatables.NpcShopTable3;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.ShopBuyLimitInfo;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1TaxCalculator;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcCashShopInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Warehouse.SupplementaryService;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_ACTION_UI;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SurvivalCry;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.templates.ShopBuyLimit;
import l1j.server.server.templates.eShopBuyLimitType;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.IntRange;

public class L1Shop {
	public static boolean is_normal_shop(L1NpcInstance npc) {
		return ShopTable.getInstance().get(npc.getNpcId()) != null;
	}

	public static L1Shop find_shop(L1NpcInstance npc) {
		int npcId = npc.getNpcTemplate().get_npcId();
		L1Shop shop = null;
		if (npc instanceof L1NpcShopInstance) {
			shop = NpcShopTable.getInstance().get(npcId);
			if (shop == null)
				shop = NpcShopTable2.getInstance().get(npcId);
			if (shop == null)
				shop = NpcShopTable3.getInstance().get(npcId);
		} else if (npc instanceof L1NpcCashShopInstance) {
			shop = NpcCashShopTable.getInstance().get(npcId);
		} else {
			shop = ShopTable.getInstance().get(npcId);
		}
		if (shop == null)
			System.out.println("NPC 商店錯誤：編號" + npc.getNpcId() + " x: " + npc.getX() + " y: " + npc.getY() + " 地圖: " + npc.getMapId());
		return shop;
	}

	private final int _npcId;
	private final List<L1ShopItem> _sellingItems;
	private final List<L1ShopItem> _purchasingItems;

	private static final HashMap<Integer, Integer> _checkbox_rules;
	static {
		_checkbox_rules = new HashMap<Integer, Integer>();
		_checkbox_rules.put(4100251, 4100251);
		_checkbox_rules.put(4100252, 4100252);
		_checkbox_rules.put(4100253, 4100253);

		/*
		 * _checkbox_rules.put(40222, 40222); _checkbox_rules.put(41148, 41148); _checkbox_rules.put(5559, 5559); _checkbox_rules.put(210125, 210125); _checkbox_rules.put(4100046, 4100046); _checkbox_rules.put(4100079, 4100079);
		 */
	}

	public L1Shop(int npcId, List<L1ShopItem> sellingItems, List<L1ShopItem> purchasingItems) {
		if (sellingItems == null || purchasingItems == null) {
			throw new NullPointerException();
		}
		_npcId = npcId;
		_sellingItems = sellingItems;
		_purchasingItems = purchasingItems;
	}

	public int getNpcId() {
		return _npcId;
	}

	public List<L1ShopItem> getSellingItems() {
		return _sellingItems;
	}

	public List<L1ShopItem> getBuyingItems() {
		return _purchasingItems;
	}

	private boolean isPurchaseableItem(L1ItemInstance item) {
		if (item == null) {
			return false;
		}
		if (item.isEquipped()) {
			return false;
		}
		if (item.getBless() >= 128) {
			return false;
		}
		if (item.get_Carving() != 0) {
			return false;
		}
		/** 인형 착용 여부 **/
		if (item.isDollOn()) {
			return false;
		}
		return true;
	}

	public L1ShopItem getSellItem(int itemid) {
		for (L1ShopItem a : _sellingItems) {
			if (a.getItemId() == itemid) {
				return a;
			}
		}
		return null;
	}

	public L1ShopItem getBuyItem(int itemid) {
		for (L1ShopItem a : _purchasingItems) {
			if (a.getItemId() == itemid) {
				return a;
			}
		}
		return null;
	}

	public boolean isSellingItem(int itemid) {
		for (L1ShopItem a : _sellingItems) {
			if (a.getItemId() == itemid) {
				return true;
			}
		}
		return false;
	}

	private L1ShopItem getPurchasingItem(int itemId, int enchant) {
		for (L1ShopItem shopItem : _purchasingItems) {
			if (shopItem.getItemId() == itemId && shopItem.getEnchant() == enchant) {
				return shopItem;
			}
		}
		return null;
	}

	public L1ShopItem getSellingItem(int itemId) {
		for (L1ShopItem shopItem : _sellingItems) {
			if (shopItem.getItemId() == itemId) {
				return shopItem;
			}
		}
		return null;
	}

	public L1AssessedItem assessItem(L1ItemInstance item) {
		L1ShopItem shopItem = getPurchasingItem(item.getItemId(), item.getEnchantLevel());
		if (shopItem == null) {
			return null;
		}
		return new L1AssessedItem(item.getId(), getAssessedPrice(shopItem));
	}

	private int getAssessedPrice(L1ShopItem item) {
		return (int) (item.getPrice() * Config.ServerRates.RateShopPurchasingPrice / item.getPackCount());
	}

	public List<L1AssessedItem> assessItems(L1PcInventory inv) {
		List<L1AssessedItem> result = new ArrayList<L1AssessedItem>();
		for (L1ShopItem item : _purchasingItems) {
			for (L1ItemInstance targetItem : inv.findItemsId(item.getItemId())) {
				if (!isPurchaseableItem(targetItem)) {
					continue;
				}
				if (item.getEnchant() == targetItem.getEnchantLevel()) { // 인챈트가 같은 아이템만
					result.add(new L1AssessedItem(targetItem.getId(), getAssessedPrice(item)));
				}
			}
		}
		return result;
	}

	private boolean ensureSell(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPriceTaxIncluded();

		if (!IntRange.includes(price, 0, 2000000000)) {
			pc.sendPackets(new S_ServerMessage(904, "2000000000"));
			return false;
		}
		if (!pc.getInventory().checkItem(L1ItemId.ADENA, price)) {
			pc.sendPackets(new S_ServerMessage(189));
			return false;
		}

		int currentWeight = (int) (pc.getInventory().getWeight() * Config.ServerRates.RateWeightLimit);// 구매 무게
		if ((currentWeight + orderList.getTotalWeight() / 1000 > (pc.getMaxWeight() * Config.ServerRates.RateWeightLimit))) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}

		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
//		if (totalCount > 180) {
		if (totalCount > 200) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		// ## (버그 방지) 상점 버그 방지
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}

	private void payCastleTax(L1ShopBuyOrderList orderList) {
		L1TaxCalculator calc = orderList.getTaxCalculator();

		int price = orderList.getTotalPrice();

		int castleId = L1CastleLocation.getCastleIdByNpcid(_npcId);
		int castleTax = calc.calcCastleTaxPrice(price);
		int nationalTax = calc.calcNationalTaxPrice(price);
		if (castleId == L1CastleLocation.ADEN_CASTLE_ID || castleId == L1CastleLocation.DIAD_CASTLE_ID) {
			castleTax += nationalTax;
			nationalTax = 0;
		}

		if (castleId != 0 && castleTax > 0) {
			MJCastleWar war = MJCastleWarBusiness.getInstance().get(castleId);
			int money = war.getPublicMoney();
			if (2000000000 > money + castleTax) {
				money = money + castleTax;
				war.setPublicMoney(money);
				MJCastleWarBusiness.getInstance().updateCastle(castleId);
			}

			if (nationalTax > 0) {
				war = MJCastleWarBusiness.getInstance().get(L1CastleLocation.ADEN_CASTLE_ID);
				money = war.getPublicMoney();
				if (2000000000 > money + castleTax) {
					money = money + nationalTax;
					war.setPublicMoney(money);
					MJCastleWarBusiness.getInstance().updateCastle(L1CastleLocation.ADEN_CASTLE_ID);
				}

			}
		}
	}

	private void payDiadTax(L1ShopBuyOrderList orderList) {
		L1TaxCalculator calc = orderList.getTaxCalculator();

		int price = orderList.getTotalPrice();

		int diadTax = calc.calcDiadTaxPrice(price);
		if (diadTax <= 0) {
			return;
		}

		MJCastleWar war = MJCastleWarBusiness.getInstance().get(L1CastleLocation.DIAD_CASTLE_ID);
		int money = war.getPublicMoney();
		if (2000000000 > money + diadTax) {
			money = money + diadTax;
			war.setPublicMoney(money);
			MJCastleWarBusiness.getInstance().updateCastle(L1CastleLocation.DIAD_CASTLE_ID);
		}
	}

	private void payTax(L1ShopBuyOrderList orderList) {
		payCastleTax(orderList);
		payDiadTax(orderList);
	}

	private void sellItems(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(L1ItemId.ADENA, orderList.getTotalPriceTaxIncluded())) {
			throw new IllegalStateException("無法消耗購買所需的金幣。");
		}
		int consume_count = orderList.getTotalPriceTaxIncluded();
		L1ItemInstance item = null;
		Random random = new Random(System.nanoTime());
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			int enchant = order.getItem().getEnchant();
			int endtime = order.getItem().getEndTime();
			boolean carving = order.getItem().isCarving();
			int bless = order.getItem().getBless();
			int attrenchant = order.getItem().getAttrEnchant();
			int buylevel = order.getItem().getBuyLevel();

			if (_npcId == BugRaceController.RACE_SELLER_NPCID) {
				BugTicketInfo tInfo = BugRaceController.getInstance().find_ticket_info(itemId);
				if (tInfo != null) {
					itemId = tInfo.converter_itemid;
				}
			}

			item = ItemTable.getInstance().createItem(itemId);
			if (getSellingItems().contains((Object) item)) {
				return;
			}

			if (!isShopBuyLimitItem(inv, order, item, amount)) {
				inv.getOwner().getInventory().storeItem(L1ItemId.ADENA, consume_count);
				return;
			}
			consume_count -= order.getItem().getPrice();

			if (inv.getOwner() != null) {
				if (buylevel != 0) {
					if (inv.getOwner().getLevel() > buylevel) {
						inv.getOwner().sendPackets(String.format("該物品僅限 %d 級以下購買。", buylevel));
						continue;
					}
				}
			}

			if (attrenchant > 0) {
				item.setAttrEnchantLevel(attrenchant);
			}

			if (carving) {
				item.set_Carving(1);
			}

			item.setBless(bless);

			if (endtime > 0) {
				Timestamp deleteTime = null;
				deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * endtime));
				item.setEndTime(deleteTime);
			}

			if (_npcId == 70068 || _npcId == 70020 || _npcId == 70056) {// 인챈상점
				item.setIdentified(false);
				int chance = random.nextInt(150) + 1;
				if (chance <= 15) {
					item.setEnchantLevel(-2);
				} else if (chance >= 16 && chance <= 30) {
					item.setEnchantLevel(-1);
				} else if (chance >= 31 && chance <= 89) {
					item.setEnchantLevel(0);
				} else if (chance >= 90 && chance <= 141) {
					item.setEnchantLevel(random.nextInt(2) + 1);
				} else if (chance >= 142 && chance <= 147) {
					item.setEnchantLevel(random.nextInt(3) + 3);
				} else if (chance >= 148 && chance <= 149) {
					item.setEnchantLevel(6);
				} else if (chance == 150) {
					item.setEnchantLevel(7);
				}
			}
			if (_npcId == 900173) {// 인챈상점
				item.setIdentified(false);
				int chance = random.nextInt(200) + 1;
				if (chance <= 20) {
					item.setEnchantLevel(-2);
				} else if (chance >= 25 && chance <= 35) {
					item.setEnchantLevel(-1);
				} else if (chance >= 40 && chance <= 55) {
					item.setEnchantLevel(0);
				} else if (chance >= 60 && chance <= 70) {
					item.setEnchantLevel(random.nextInt(1));
				} else if (chance >= 100 && chance <= 120) {
					item.setEnchantLevel(random.nextInt(2) + 1);
				} else if (chance >= 150 && chance <= 170) {
					item.setEnchantLevel(3);
				}
			}
			if (_npcId == 7310125) {// 인챈상점
				item.setIdentified(false);
				int chance = random.nextInt(200) + 1;
				if (chance <= 19) {
					item.setEnchantLevel(-2);
				} else if (chance >= 20 && chance <= 30) {
					item.setEnchantLevel(-1);
				} else if (chance >= 31 && chance <= 49) {
					item.setEnchantLevel(0);
				} else if (chance >= 50 && chance <= 60) {
					item.setEnchantLevel(random.nextInt(1) + 3);
				} else if (chance >= 70 && chance <= 80) {
					item.setEnchantLevel(random.nextInt(2) + 2);
				} else if (chance >= 130 && chance <= 150) {
					item.setEnchantLevel(5);
				} else if (chance == 170) {
					item.setEnchantLevel(6);
				}
			}
			if (_npcId == 7310104) {// 마족
				item.setIdentified(false);
				int chance1 = random.nextInt(150) + 2;
				if (chance1 <= 15) {
					item.setEnchantLevel(-3);
				} else if (chance1 >= 31 && chance1 <= 40) {
					item.setEnchantLevel(-2);
				} else if (chance1 >= 41 && chance1 <= 50) {
					item.setEnchantLevel(-1);
				} else if (chance1 >= 10 && chance1 <= 20) {
					item.setEnchantLevel(0);
				} else if (chance1 >= 21 && chance1 <= 30) {
					item.setEnchantLevel(random.nextInt(2) + 1);
				} else if (chance1 >= 51 && chance1 <= 60) {
					item.setEnchantLevel(random.nextInt(3) + 3);
				} else if (chance1 >= 61 && chance1 <= 70) {
					item.setEnchantLevel(2);
				} else if (chance1 >= 71 && chance1 <= 80) {
					item.setEnchantLevel(3);
				} else if (chance1 >= 81 && chance1 <= 90) {
					item.setEnchantLevel(4);
				} else if (chance1 == 100) {
					item.setEnchantLevel(5);
				}

				/** 아데나 인챈물품판매 할 경우 추가 **/
				// } else if (_npcId == 200004 || _npcId == 900171 || _npcId == 81008 || _npcId == 81004 || _npcId == 7320053
				// || _npcId == 526 || _npcId == 531 || _npcId == 70010 || _npcId == 200005 || _npcId == 7320222
				// || _npcId == 7320223 || _npcId == 8500303) {
				// item.setEnchantLevel(enchant);
			}
			item.setEnchantLevel(enchant);



			// TODO 버경표를 구매했을때 마일리지 지급수량
			onSellings(inv.getOwner(), order.getItem(), item);

			item.setCount(amount);
			inv.storeShopItem(item, true);
			L1PcInstance pc = inv.getOwner();
			obtained_item(pc, item);
		}
	}

	private void onSellings(L1PcInstance pc, L1ShopItem shopItem, L1ItemInstance item) {
		if (shopItem.isTimeLimit() && pc != null) {
			pc.setFishingShopBuyTime_1(System.currentTimeMillis());
		}
	}

	public void sellItems(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		NpcShopCashTable.L1CashType at = NpcShopCashTable.getInstance().getNpcCashType(getNpcId());

		if (at != null) {
			if (!ensurePremiumSell(pc, orderList, at)) {
				return;
			}
			if (at.getCashType() == 41921) {
				
			}
			sellPremiumItems(pc.getInventory(), orderList, at);
		} else {
			if (getNpcId() == 200060 || getNpcId() == 200061 || getNpcId() == 200062 || getNpcId() == 200063 || getNpcId() == 7310103 || getNpcId() == 523 || getNpcId() == 5000000
					|| getNpcId() == 900047 || getNpcId() == 5072 || getNpcId() == 5073 || getNpcId() == 519 || getNpcId() == 224
					/* || getNpcId() == 7310101 */ || getNpcId() == 7310113 || getNpcId() == 7320124 || getNpcId() == 6000002) {
				if (!ensurePremiumSell(pc, orderList)) {
					return;
				}
				sellPremiumItems(pc.getInventory(), orderList);
				return;
			}
			
			// TODO 클랜상점
			if (this.getNpcId() == 0) {
				if (!this.ensure_contribution_tokken(pc, orderList)) {
					return;
				}
				this.sell_contribution_tokken(pc.getInventory(), orderList);
				return;
			}
			
			// TODO 기사단 주화
			if (this.getNpcId() == 7320121 || this.getNpcId() == 202056 || this.getNpcId() == 8502049 || this.getNpcId() == 2020700 || this.getNpcId() == 2020701 || this.getNpcId() == 2020702
					|| this.getNpcId() == 2020703 || this.getNpcId() == 2020704 || this.getNpcId() == 2020705 || this.getNpcId() == 2020706 || this.getNpcId() == 2020707 || this.getNpcId() == 2020708
					|| this.getNpcId() == 7320085 || this.getNpcId() == 73201211 || this.getNpcId() == 73201212 || this.getNpcId() == 73201213 || this.getNpcId() == 73201214
					|| this.getNpcId() == 73201215 || this.getNpcId() == 73201216 || this.getNpcId() == 73201217 || this.getNpcId() == 73201218 || this.getNpcId() == 73201219 || this.getNpcId() == 73201220) {
				if (!this.ensure_certtoken(pc, orderList)) {
					return;
				}
				this.sell_certtoken(pc.getInventory(), orderList);
				return;
			}
			// TODO N코인
			if (this.getNpcId() == 7320055 || this.getNpcId() == 19) {
				if (!this.ensure_certNcoin(pc, orderList)) {
					return;
				}
				this.sell_certNcoin(pc, orderList);
				return;
			}
			// 영자 엔피씨무인상점
			if (getNpcId() >= 4000001 && getNpcId() <= 4000061 || getNpcId() == 7320087 || getNpcId() == 7320122 || getNpcId() == 7320123 || getNpcId() == 7320157
					|| NpcTable.getInstance().getTemplate(getNpcId()).getImpl().equalsIgnoreCase("L1NpcShop")) {
				if (!NoTaxEnsureSell(pc, orderList)) {
					return;
				}
				NpcShopSellItems(pc.getInventory(), orderList);
				return;
			}

			// 신묘년 이벤트
			if (getNpcId() == 900107) {
				if (!ensureMarkSell(pc, orderList)) {
					return;
				}
				sellMarkItems(pc.getInventory(), orderList);
				return;
			}
			if (!ensureSell(pc, orderList)) {
				return;
			} else {
				sellItems(pc.getInventory(), orderList);
				payTax(orderList);
			}
		}
	}

	public void buyItems(L1ShopSellOrderList orderList) {
		L1PcInventory inv = orderList.getPc().getInventory();
		int totalPrice = 0;
		L1Object object = null;
		L1ItemInstance item = null;
		for (L1ShopSellOrder order : orderList.getList()) {
			object = inv.getItem(order.getItem().getTargetId());
			item = (L1ItemInstance) object;
			if (item == null)
				continue;
			if (item.getItem().getBless() < 128) {
				int count = inv.removeItem(item, order.getCount());
				if (totalPrice + order.getItem().getAssessedPrice() * count > 2147483647L) {
					return;
				}
				totalPrice += order.getItem().getAssessedPrice() * count;
			}
		}

		if (item == null) {
			return;
		}

		totalPrice = IntRange.ensure(totalPrice, 0, 2000000000);
		if (0 < totalPrice) {
			/** 패키지상점 **/
			if (getNpcId() >= 6100000 && getNpcId() <= 6100035) {
				inv.storeItem(getNpcId() - 5299999, totalPrice, true);
			} else if (_npcId == 7000077) {// 행베리
				if (0 < totalPrice) {
					inv.storeItem(41302, totalPrice, true);
				}
			} else if (_npcId == 7320055) {
				if (0 < totalPrice) {
					inv.storeItem(4100056, totalPrice, true);
				}
			} else if (_npcId == 8502050) {
				inv.storeItem(4100463, totalPrice, true);
			} else {
				inv.storeItem(L1ItemId.ADENA, totalPrice, true);
			}
		}
	}

	public L1ShopBuyOrderList newBuyOrderList() {
		return new L1ShopBuyOrderList(this);
	}

	public L1ShopSellOrderList newSellOrderList(L1PcInstance pc) {
		return new L1ShopSellOrderList(this, pc);
	}

	/** 깃털 상점관련 **/
	private void sellPremiumItems(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(41921, orderList.getTotalPriceTaxIncluded())) {
			inv.getOwner().sendPackets(new S_SystemMessage("無法消耗購買所需的皮克西的金色羽毛。"));
			return;
		}
		int consume_count = orderList.getTotalPriceTaxIncluded();
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int enchant = order.getItem().getEnchant();
			int attrenchant = order.getItem().getAttrEnchant();
			int amount = order.getCount();
			int endtime = order.getItem().getEndTime();
			boolean carving = order.getItem().isCarving();
			int bless = order.getItem().getBless();
			int buylevel = order.getItem().getBuyLevel();
			L1PcInstance pc = inv.getOwner();
			if (buylevel != 0) {
				if (pc.getLevel() > buylevel) {
					pc.sendPackets(String.format("該物品僅限 %d 級以下購買。", buylevel));
					continue;
				}
			}

			Random random = new Random(System.nanoTime());
			item = ItemTable.getInstance().createItem(itemId);
			if (getSellingItems().contains((Object) item)) {
				return;
			}

			if (!isShopBuyLimitItem(inv, order, item, amount)) {
				inv.getOwner().getInventory().storeItem(41921, consume_count);
				return;
			}
			consume_count -= order.getItem().getPrice();

			if (attrenchant > 0) {
				item.setAttrEnchantLevel(attrenchant);
			}

			if (carving) {
				item.set_Carving(1);
			}

			item.setBless(bless);

			if (endtime > 0) {
				Timestamp deleteTime = null;
				deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * endtime));
				item.setEndTime(deleteTime);
			}

			if (_npcId == 7310103) {// 행운의 랜덤 상점
				item.setIdentified(false);
				int chance1 = random.nextInt(100) + 1;
				if (chance1 <= 50) {
					item.setEnchantLevel(7);
				} else if (chance1 >= 60 && chance1 <= 70) {
					item.setEnchantLevel(8);
				} else if (chance1 >= 80 && chance1 <= 90) {
					item.setEnchantLevel(9);
				} else if (chance1 >= 95 && chance1 <= 100) {
					item.setEnchantLevel(10);
				} else {
					item.setEnchantLevel(7);
				}

				/** 깃털 인챈상점 추가 [1] **/
				/*
				 * } else if (_npcId == 5073 || _npcId == 7310101 || _npcId == 7310113 || _npcId == 519 || _npcId == 224 || _npcId == 523 || _npcId == 6000002) { item.setEnchantLevel(enchant);
				 */
			}
			item.setEnchantLevel(enchant);
			onSellings(pc, order.getItem(), item);

			item.setCount(amount);
			item = inv.storeShopItem(item, true);
			obtained_item(pc, item);
		}
	}

	// 檢查是否能從高級商人處購買物品
	private boolean ensurePremiumSell(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		int FeatherCount = Config.ServerAdSetting.FeatherShopNum;
		// 溢出檢查
		if (!IntRange.includes(price, 0, FeatherCount)) {
			pc.sendPackets(new S_SystemMessage("皮克西的金色羽毛一次不能使用超過 " + Config.ServerAdSetting.FeatherShopNum + " 個。"));
			return false;
		}
		// 檢查是否能購買
		if (!pc.getInventory().checkItem(41921, price)) {
			pc.sendPackets(new S_SystemMessage("皮克西的金色羽毛不足。"));
			return false;
		}
		// 重量檢查
		int currentWeight = (int) (pc.getInventory().getWeight() * Config.ServerRates.RateWeightLimit); // 購買重量
		if ((currentWeight + orderList.getTotalWeight() / 1000 > (pc.getMaxWeight() * Config.ServerRates.RateWeightLimit))) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		// 數量檢查
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
//		if (totalCount > 180) {
		if (totalCount > 200) {
			// \f1한사람의 캐릭터가 가지고 걸을 수 있는 아이템은 최대 180개까지입니다.
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}

	private boolean NoTaxEnsureSell(L1PcInstance pc, L1ShopBuyOrderList orderList) {

		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 2000000000)) {
			pc.sendPackets(new S_ServerMessage(904, "2000000000"));
			return false;
		}
		if (!pc.getInventory().checkItem(L1ItemId.ADENA, price)) {
			pc.sendPackets(new S_ServerMessage(189));
			return false;
		}
		int currentWeight = (int) (pc.getInventory().getWeight() * Config.ServerRates.RateWeightLimit);// 구매 무게
		if ((currentWeight + orderList.getTotalWeight() / 1000 > (pc.getMaxWeight() * Config.ServerRates.RateWeightLimit))) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
//		if (totalCount > 180) {
		if (totalCount > 200) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}

	private synchronized void NpcShopSellItems_for_locked(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		int sellings_price = orderList.getTotalPrice();
		if (!inv.consumeItem(L1ItemId.ADENA, sellings_price)) {
			inv.getOwner().sendPackets(new S_SystemMessage("無法消耗購買所需的金幣。"));
		}

		/** 2016.11.24 MJ 앱센터 시세 **/
		L1NpcInstance npc = L1World.getInstance().findNpc(getNpcId());
		/** 2016.11.24 MJ 앱센터 시세 **/

		for (L1ShopBuyOrder order : orderList.getList()) {
			int orderid = order.getOrderNumber();
			int amount = order.getCount();
			int remaindcount = getSellingItems().get(orderid).getCount();
			if (remaindcount < amount)
				return;
		}
		int consume_count = orderList.getTotalPrice();
		L1ItemInstance item = null;
		boolean[] isRemoveFromList = new boolean[_sellingItems.size()];
		for (L1ShopBuyOrder order : orderList.getList()) {
			int orderid = order.getOrderNumber();
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			int enchant = order.getItem().getEnchant();
			int attrenchant = order.getItem().getAttrEnchant();
			int endtime = order.getItem().getEndTime();
			boolean carving = order.getItem().isCarving();
			int bless = order.getItem().getBless();
			int buylevel = order.getItem().getBuyLevel();
			L1PcInstance pc = inv.getOwner();
			if (buylevel != 0) {
				if (pc.getLevel() > buylevel) {
					pc.sendPackets(String.format("該物品僅限 %d 級以下購買。", buylevel));
					continue;
				}
			}
			int remaindcount = getSellingItems().get(orderid).getCount();
			if (remaindcount < amount)
				return;
			item = ItemTable.getInstance().createItem(itemId);

			if (getSellingItems().contains((Object) item)) {
				return;
			}

			if (!isShopBuyLimitItem(inv, order, item, amount)) {
				inv.getOwner().getInventory().storeItem(L1ItemId.ADENA, consume_count);
				return;
			}
			consume_count -= order.getItem().getPrice();

			item.setEnchantLevel(enchant);

			if (attrenchant > 0) {
				item.setAttrEnchantLevel(attrenchant);
			}

			if (carving) {
				item.set_Carving(1);
			}

			item.setBless(bless);

			if (endtime > 0) {
				Timestamp deleteTime = null;
				deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * endtime));
				item.setEndTime(deleteTime);
			}

			if (remaindcount == amount) {
				isRemoveFromList[orderid] = true;
			} else
				_sellingItems.get(orderid).setCount(remaindcount - amount);

			onSellings(inv.getOwner(), order.getItem(), item);

			item.setCount(amount);
			item = inv.storeShopItem(item, true);
			obtained_item(pc, item);
			
			for (int i = isRemoveFromList.length - 1; i >= 0; i--) {
				if (isRemoveFromList[i]) {
					_sellingItems.remove(i);
				}
			}

			/** 2016.11.24 MJ 앱센터 시세 **/
			npc.updateSellings(item.getId(), item.getCount());
			/** 2016.11.24 MJ 앱센터 시세 **/
		}
	}

	private void NpcShopSellItems(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (Config.Synchronization.IsSellingsShopLocked) {
			NpcShopSellItems_for_locked(inv, orderList);
			return;
		}

		int sellings_price = orderList.getTotalPrice();
		if (inv.countItems(L1ItemId.ADENA) < sellings_price) {
			throw new IllegalStateException("無法消耗購買所需的金幣。");
		}

		/** 2016.11.24 MJ 앱센터 시세 **/
		L1NpcInstance npc = L1World.getInstance().findNpc(getNpcId());
		/** 2016.11.24 MJ 앱센터 시세 **/

		for (L1ShopBuyOrder order : orderList.getList()) {
			int orderid = order.getOrderNumber();
			int amount = order.getCount();
			int remaindcount = getSellingItems().get(orderid).getCount();
			if (remaindcount < amount)
				return;
		}

		if (!inv.consumeItem(L1ItemId.ADENA, orderList.getTotalPrice())) {
			inv.getOwner().sendPackets(new S_SystemMessage("無法消耗購買所需的金幣。"));
			return;
		}
		int consume_count = orderList.getTotalPrice();
		L1ItemInstance item = null;
		boolean[] isRemoveFromList = new boolean[_sellingItems.size()];
		for (L1ShopBuyOrder order : orderList.getList()) {
			int orderid = order.getOrderNumber();
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			int enchant = order.getItem().getEnchant();
			int attrenchant = order.getItem().getAttrEnchant();
			int endtime = order.getItem().getEndTime();
			boolean carving = order.getItem().isCarving();
			int bless = order.getItem().getBless();
			int buylevel = order.getItem().getBuyLevel();
			L1PcInstance pc = inv.getOwner();
			if (buylevel != 0) {
				if (pc.getLevel() > buylevel) {
					pc.sendPackets(String.format("該物品僅限 %d 級以下購買。", buylevel));
					continue;
				}
			}
			int remaindcount = getSellingItems().get(orderid).getCount();
			if (remaindcount < amount)
				return;
			item = ItemTable.getInstance().createItem(itemId);
			if (getSellingItems().contains((Object) item)) {
				return;
			}
			if (!isShopBuyLimitItem(inv, order, item, amount)) {
				inv.getOwner().getInventory().storeItem(L1ItemId.ADENA, consume_count);
				return;
			}
			consume_count -= order.getItem().getPrice();
			item.setEnchantLevel(enchant);

			if (attrenchant > 0) {
				item.setAttrEnchantLevel(attrenchant);
			}

			if (carving) {
				item.set_Carving(1);
			}

			item.setBless(bless);

			if (endtime > 0) {
				Timestamp deleteTime = null;
				deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * endtime));
				item.setEndTime(deleteTime);
			}

			if (remaindcount == amount) {
				isRemoveFromList[orderid] = true;
			} else
				_sellingItems.get(orderid).setCount(remaindcount - amount);
			onSellings(inv.getOwner(), order.getItem(), item);

			item.setCount(amount);
			item = inv.storeShopItem(item, true);
			obtained_item(pc, item);

			for (int i = isRemoveFromList.length - 1; i >= 0; i--) {
				if (isRemoveFromList[i]) {
					_sellingItems.remove(i);
				}
			}

			/** 2016.11.24 MJ 앱센터 시세 **/
			npc.updateSellings(item.getId(), item.getCount());
			/** 2016.11.24 MJ 앱센터 시세 **/
		}
	}

	private void sellMarkItems(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(410093, orderList.getTotalPrice())) {
			throw new IllegalStateException("無法消耗購買所需的滿月精氣。");
		} // 신묘년
		int consume_count = orderList.getTotalPrice();
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);

			if (!isShopBuyLimitItem(inv, order, item, amount)) {
				inv.getOwner().getInventory().storeItem(410093, consume_count);
				return;
			}
			consume_count -= order.getItem().getPrice();
			onSellings(inv.getOwner(), order.getItem(), item);

			item.setCount(amount);
			item = inv.storeShopItem(item, true);
			L1PcInstance pc = inv.getOwner();
			obtained_item(pc, item);
		}
	}

	private void sell_certNcoin(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		if (pc.getNcoin() < orderList.getTotalPrice() || pc.getAccount() == null)
			throw new IllegalStateException("無法消耗購買所需的N幣。");

		pc.getAccount().Ncoin_point -= orderList.getTotalPrice();
		int consume_count = 0;
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			// int endtime = order.getItem().getEndTime();
			L1Item template = ItemTable.getInstance().getTemplate(itemId);
			if (template == null) {
				throw new IllegalStateException(String.format("[N幣商店] 無法確認購買的物品。 %d", itemId));
			}

			SupplementaryService pwh = WarehouseManager.getInstance().getSupplementaryService(pc.getAccountName());
			if (template.isStackable()) {
				item = ItemTable.getInstance().createItem(template);
				if (item == null) {
					throw new IllegalStateException(String.format("[N幣商店] 無法生成購買的物品。 %d", itemId));
				}

				/*
				 * if (endtime > 0) { Timestamp deleteTime = null; deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * endtime)); item.setEndTime(deleteTime); }
				 */

				if (!isShopBuyLimitItem(pc.getInventory(), order, item, amount)) {
					//pc.getAccount().Ncoin_point += consume_count;
					consume_count += order.getItem().getPrice() * amount;
					continue;
				}
				//consume_count -= order.getItem().getPrice();
				item.setIdentified(true);
				item.setChargeCount(template.getMaxChargeCount());
				item.setCount(amount);
				pwh.storeTradeItem(item);
			} else {
				if (amount == 1) {
					for (int i = 0; i < order.getItem().getPackCount(); i++) {
						item = ItemTable.getInstance().createItem(template);
						if (item == null) {
							throw new IllegalStateException(String.format("[N幣商店] 無法生成購買的物品。 %d", itemId));
						}

						if (!isShopBuyLimitItem(pc.getInventory(), order, item, amount)) {
							consume_count += order.getItem().getPrice() * amount;
							continue;
						}
						// consume_count -= order.getItem().getPrice();
						item.setIdentified(true);
						item.setChargeCount(template.getMaxChargeCount());
						pwh.storeTradeItem(item);
					}
				} else {
					for (int j = 0; j < amount; j++) {
						for (int i = 0; i < order.getItem().getPackCount(); i++) {
							item = ItemTable.getInstance().createItem(template);
							if (item == null) {
								throw new IllegalStateException(String.format("[N幣商店] 無法生成購買的物品。 %d", itemId));
							}

							if (!isShopBuyLimitItem(pc.getInventory(), order, item, amount)) {
								consume_count += order.getItem().getPrice() * amount;
								continue;
							}
							//consume_count -= order.getItem().getPrice();
							item.setIdentified(true);
							item.setChargeCount(template.getMaxChargeCount());
							pwh.storeTradeItem(item);
						}
					}
				}
			}

			/**
			 * 상점 시간제한 이용시는 주석해제
			 */
			// onSellings(pc, order.getItem(), item);
		}

		pc.getAccount().Ncoin_point += consume_count;
		pc.sendPackets(new S_SurvivalCry(2, pc));
		pc.getAccount().updateNcoin();
		SC_GOODS_INVEN_NOTI.do_send(pc);
	}

	private void sell_certtoken(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(4100463, orderList.getTotalPrice())) {
			inv.getOwner().sendPackets(new S_SystemMessage("無法消耗購買所需的騎士團硬幣。"));
			return;
		}
		int consume_count = orderList.getTotalPrice();
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int enchant = order.getItem().getEnchant();
			int amount = order.getCount();
			Random random = new Random(System.nanoTime());

			item = ItemTable.getInstance().createItem(itemId);
			if (getSellingItems().contains((Object) item)) {
				return;
			}
			if (!isShopBuyLimitItem(inv, order, item, amount)) {
				inv.getOwner().getInventory().storeItem(4100463, consume_count);
				return;
			}
			consume_count -= order.getItem().getPrice();
			if (_npcId == 123010) {// 행운의 랜덤 상점
				item.setIdentified(false);
				int chance2 = random.nextInt(150) + 1;
				if (chance2 <= 15) {
					item.setEnchantLevel(-2);
				} else if (chance2 >= 20 && chance2 <= 30) {
					item.setEnchantLevel(-1);
				} else if (chance2 >= 31 && chance2 <= 40) {
					item.setEnchantLevel(0);
				} else if (chance2 >= 51 && chance2 <= 60) {
					item.setEnchantLevel(random.nextInt(1) + 2);
				} else if (chance2 >= 70 && chance2 <= 80) {
					item.setEnchantLevel(random.nextInt(2) + 3);
				} else if (chance2 >= 90 && chance2 <= 110) {
					item.setEnchantLevel(5);
				} else if (chance2 == 148) {
					item.setEnchantLevel(6);
				}

				// } else if (_npcId == 010) { // -- 깃털상점 수량2개이상 구매시 체크해야하니 번호적기
				// item.setEnchantLevel(enchant);
			}
			item.setEnchantLevel(enchant);
			onSellings(inv.getOwner(), order.getItem(), item);

			item.setCount(amount);
			item = inv.storeShopItem(item, true);
			L1PcInstance pc = inv.getOwner();
			obtained_item(pc, item);
		}
	}

	private boolean ensure_certtoken(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 10000000)) {
			pc.sendPackets("一次不能使用超過 1,000 萬個。");
			return false;
		}

		if (!pc.getInventory().checkItem(4100463, price)) {
			pc.sendPackets("騎士團硬幣不足。");
			return false;
		}
		int currentWeight = (int) (pc.getInventory().getWeight() * Config.ServerRates.RateWeightLimit);// 구매 무게
		if ((currentWeight + orderList.getTotalWeight() / 1000 > (pc.getMaxWeight() * Config.ServerRates.RateWeightLimit))) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
//		if (totalCount > 180) {
		if (totalCount > 200) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}

	/*
	 * private boolean ensure_cert1(L1PcInstance pc, L1ShopBuyOrderList orderList) { // Some Shop
	 * int price = orderList.getTotalPrice();
	 * if (!IntRange.includes(price, 0, 10000000)) {
	 *     pc.sendPackets(new S_SystemMessage("一次不能使用超過 1,000 萬個。"));
	 *     return false;
	 * }
	 *
	 * if (!pc.getInventory().checkItem(3000156, price)) {
	 *     pc.sendPackets(new S_SystemMessage("紙鶴不足。"));
	 *     return false;
	 * }
	 * int currentWeight = (int) (pc.getInventory().getWeight() * Config.ServerRates.RateWeightLimit); // 購買重量
	 * if ((currentWeight + orderList.getTotalWeight() / 1000 > (pc.getMaxWeight() * Config.ServerRates.RateWeightLimit))) {
	 *     pc.sendPackets(new S_ServerMessage(82));
	 *     return false;
	 * }
	 * int totalCount = pc.getInventory().getSize();
	 * L1Item temp = null;
	 * for (L1ShopBuyOrder order : orderList.getList()) {
	 *     temp = order.getItem().getItem();
	 *     if (temp.isStackable()) {
	 *         if (!pc.getInventory().checkItem(temp.getItemId())) {
	 *             totalCount += 1;
	 *         }
	 *     } else {
	 *         totalCount += 1;
	 *     }
	 * }
	 * if (totalCount > 180) {
	 *     pc.sendPackets(new S_ServerMessage(263));
	 *     return false;
	 * }
	 * if (price <= 0 || price > 2000000000) {
	 *     pc.sendPackets(new S_Disconnect());
	 *     return false;
	 * }
	 * return true;
	 * }
	 */

	/*
	 * private boolean ensure_cert4(L1PcInstance pc, L1ShopBuyOrderList orderList) { // Some Shop
	 *     int price = orderList.getTotalPrice();
	 *     if (!IntRange.includes(price, 0, 10000000)) {
	 *         pc.sendPackets(new S_SystemMessage("一次不能使用超過 1,000 萬個。"));
	 *         return false;
	 *     }
	 *
	 *     if (!pc.getInventory().checkItem(3000156, price)) {
	 *         pc.sendPackets(new S_SystemMessage("LFC紙鶴不足。"));
	 *         return false;
	 *     }
	 *     int currentWeight = (int) (pc.getInventory().getWeight() * Config.ServerRates.RateWeightLimit); // 購買重量
	 *     if ((currentWeight + orderList.getTotalWeight() / 1000 > (pc.getMaxWeight() * Config.ServerRates.RateWeightLimit))) {
	 *         pc.sendPackets(new S_ServerMessage(82));
	 *         return false;
	 *     }
	 *     int totalCount = pc.getInventory().getSize();
	 *     L1Item temp = null;
	 *     for (L1ShopBuyOrder order : orderList.getList()) {
	 *         temp = order.getItem().getItem();
	 *         if (temp.isStackable()) {
	 *             if (!pc.getInventory().checkItem(temp.getItemId())) {
	 *                 totalCount += 1;
	 *             }
	 *         } else {
	 *             totalCount += 1;
	 *         }
	 *     }
	 *     if (totalCount > 180) {
	 *         pc.sendPackets(new S_ServerMessage(263));
	 *         return false;
	 *     }
	 *     if (price <= 0 || price > 2000000000) {
	 *         pc.sendPackets(new S_Disconnect());
	 *         return false;
	 *     }
	 *     return true;
	 * }
	 */

	/*
	 * private boolean ensure_cert7(L1PcInstance pc, L1ShopBuyOrderList orderList) {
	 *     int price = orderList.getTotalPrice();
	 *     if (!IntRange.includes(price, 0, 10000000)) {
	 *         pc.sendPackets(new S_SystemMessage("一次不能使用超過 1,000 萬個。"));
	 *         return false;
	 *     }
	 *
	 *     if (!pc.getInventory().checkItem(3000156, price)) {
	 *         pc.sendPackets(new S_SystemMessage("\f2這是預覽。購買請聯絡 (Metis)。"));
	 *         return false;
	 *     }
	 *     int currentWeight = (int) (pc.getInventory().getWeight() * Config.ServerRates.RateWeightLimit); // 購買重量
	 *     if ((currentWeight + orderList.getTotalWeight() / 1000 > (pc.getMaxWeight() * Config.ServerRates.RateWeightLimit))) {
	 *         pc.sendPackets(new S_ServerMessage(82));
	 *         return false;
	 *     }
	 *     int totalCount = pc.getInventory().getSize();
	 *     L1Item temp = null;
	 *     for (L1ShopBuyOrder order : orderList.getList()) {
	 *         temp = order.getItem().getItem();
	 *         if (temp.isStackable()) {
	 *             if (!pc.getInventory().checkItem(temp.getItemId())) {
	 *                 totalCount += 1;
	 *             }
	 *         } else {
	 *             totalCount += 1;
	 *         }
	 *     }
	 *     if (totalCount > 180) {
	 *         pc.sendPackets(new S_ServerMessage(263));
	 *         return false;
	 *     }
	 *     if (price <= 0 || price > 2000000000) {
	 *         pc.sendPackets(new S_Disconnect());
	 *         return false;
	 *     }
	 *     return true;
	 * }
	 */

	/*
	 * private boolean ensure_cert2(L1PcInstance pc, L1ShopBuyOrderList orderList) {
	 *     int price = orderList.getTotalPrice();
	 *     if (!IntRange.includes(price, 0, 10000000)) {
	 *         pc.sendPackets(new S_SystemMessage("一次不能使用超過 10000 個。"));
	 *         return false;
	 *     }
	 *
	 *     if (!pc.getInventory().checkItem(3000180, price)) {
	 *         pc.sendPackets(new S_SystemMessage("夜明珠不足。"));
	 *         return false;
	 *     }
	 *     int currentWeight = (int) (pc.getInventory().getWeight() * Config.ServerRates.RateWeightLimit); // 購買重量
	 *     if ((currentWeight + orderList.getTotalWeight() / 1000 > (pc.getMaxWeight() * Config.ServerRates.RateWeightLimit))) {
	 *         pc.sendPackets(new S_ServerMessage(82));
	 *         return false;
	 *     }
	 *     int totalCount = pc.getInventory().getSize();
	 *     L1Item temp = null;
	 *     for (L1ShopBuyOrder order : orderList.getList()) {
	 *         temp = order.getItem().getItem();
	 *         if (temp.isStackable()) {
	 *             if (!pc.getInventory().checkItem(temp.getItemId())) {
	 *                 totalCount += 1;
	 *             }
	 *         } else {
	 *             totalCount += 1;
	 *         }
	 *     }
	 *     if (totalCount > 180) {
	 *         pc.sendPackets(new S_ServerMessage(263));
	 *         return false;
	 *     }
	 *     if (price <= 0 || price > 2000000000) {
	 *         pc.sendPackets(new S_Disconnect());
	 *         return false;
	 *     }
	 *     return true;
	 * }
	 */

	/*
	 * private void sell_cert5(L1PcInventory inv, L1ShopBuyOrderList orderList) {
	 *   if (!inv.consumeItem(3000156, orderList.getTotalPrice())) {
	 *       throw new IllegalStateException("無法消耗購買所需的紙鶴。");
	 *   }
	 *   L1ItemInstance item = null;
	 *   for (L1ShopBuyOrder order : orderList.getList()) {
	 *       int itemId = order.getItem().getItemId();
	 *       int amount = order.getCount();
	 *
	 *       item = ItemTable.getInstance().createItem(itemId);
	 *       if (getSellingItems().contains(item)) {
	 *           return;
	 *       }
	 *       if(!isShopBuyLimitItem(inv, order, item, amount)){
	 *           return;
	 *       }
	 *       item.setCount(amount);
	 *
	 *       if (_npcId == 5000009 || _npcId == 5000007 || _npcId == 5000008 || _npcId == 5000010 ||
	 *           _npcId >= 5000001 && _npcId <= 5000004 || _npcId >= 5000011 && _npcId <= 5000018) {
	 *           item.setEnchantLevel(order.getItem().getEnchant());
	 *       }
	 *       onSellings(inv.getOwner(), order.getItem(), item);
	 *       item = inv.storeItem(item, true);
	 *   }
	 * }
	 */

	/*
	 * private boolean ensure_Colosseum(L1PcInstance pc, L1ShopBuyOrderList orderList) {
	 *     int price = orderList.getTotalPrice();
	 *     if (!IntRange.includes(price, 0, 10000000)) {
	 *         pc.sendPackets(new S_SystemMessage("購買：一次不能使用超過 10000 個。"));
	 *         return false;
	 *     }
	 *
	 *     if (!pc.getInventory().checkItem(710, price)) {
	 *         pc.sendPackets(new S_SystemMessage("購買：角斗場硬幣不足。"));
	 *         return false;
	 *     }
	 *     int currentWeight = (int) (pc.getInventory().getWeight() * Config.ServerRates.RateWeightLimit); // 購買重量
	 *     if ((currentWeight + orderList.getTotalWeight() / 1000 > (pc.getMaxWeight() * Config.ServerRates.RateWeightLimit))) {
	 *         pc.sendPackets(new S_ServerMessage(82));
	 *         return false;
	 *     }
	 *     int totalCount = pc.getInventory().getSize();
	 *     L1Item temp = null;
	 *     for (L1ShopBuyOrder order : orderList.getList()) {
	 *         temp = order.getItem().getItem();
	 *         if (temp.isStackable()) {
	 *             if (!pc.getInventory().checkItem(temp.getItemId())) {
	 *                 totalCount += 1;
	 *             }
	 *         } else {
	 *             totalCount += 1;
	 *         }
	 *     }
	 *     if (totalCount > 180) {
	 *         pc.sendPackets(new S_ServerMessage(263));
	 *         return false;
	 *     }
	 *     if (price <= 0 || price > 2000000000) {
	 *         pc.sendPackets(new S_Disconnect());
	 *         return false;
	 *     }
	 *     return true;
	 * }
	 */

	private boolean ensure_certNcoin(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 10000000)) {
			pc.sendPackets(new S_SystemMessage("一次不能使用超過 10000000 個。"));
			return false;
		}

		if (pc.getNcoin() < price) {
			pc.sendPackets(new S_SystemMessage("N幣不足。"));
			return false;
		}
		int currentWeight = (int) (pc.getInventory().getWeight() * Config.ServerRates.RateWeightLimit);// 구매 무게
		if ((currentWeight + orderList.getTotalWeight() / 1000 > (pc.getMaxWeight() * Config.ServerRates.RateWeightLimit))) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
//		if (totalCount > 180) {
		if (totalCount > 200) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}

	private boolean ensureMarkSell(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		// 신묘년
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 1000)) {
			pc.sendPackets(new S_SystemMessage("滿月精華一次不能使用超過 1000 個。"));
			return false;
		}

		if (!pc.getInventory().checkItem(410093, price)) {
			pc.sendPackets(new S_ServerMessage(337, "$10196"));
			return false;
		}
		int currentWeight = (int) (pc.getInventory().getWeight() * Config.ServerRates.RateWeightLimit);// 구매 무게
		if ((currentWeight + orderList.getTotalWeight() / 1000 > (pc.getMaxWeight() * Config.ServerRates.RateWeightLimit))) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
//		if (totalCount > 180) {
		if (totalCount > 200) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}

	private boolean ensurePremiumSell(L1PcInstance pc, L1ShopBuyOrderList orderList, NpcShopCashTable.L1CashType at) {
		int price = orderList.getTotalPrice();
		int aden_type = at.getCashType();

		if (at.get_max_use_count() != 0) {
			if (!IntRange.includes(price, 0, at.get_max_use_count())) {
				pc.sendPackets(new S_SystemMessage(at.getCashName() + "無法一次使用超過 " + MJString.parse_money_string(at.get_max_use_count()) + "個。"));
				return false;
			}
		}
		if (at.getCashType() == -2) {
			int ainhasad_point = pc.getAccount().getBlessOfAinBonusPoint();
			if (ainhasad_point < price) {
				pc.sendPackets(String.format("%s不足。", at.getCashName()));
				return false;
			}
		} else if (at.getCashType() == 0) {
			int tamCount = pc.getAccount().getTamPoint();
			if (tamCount < price) {
				pc.sendPackets(String.format("%s不足。", at.getCashName()));
				return false;
			}
		} else if (!pc.getInventory().checkItem(aden_type, price)) {
			pc.sendPackets(String.format("%s不足。", at.getCashName()));
			return false;
		}

		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId()))
					totalCount++;
			} else {
				totalCount++;
			}
		}
//		if (totalCount > 180) {
		if (totalCount > 200) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if ((price <= 0) || (price > 2000000000)) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}

	private void sellPremiumItems(L1PcInventory inv, L1ShopBuyOrderList orderList, NpcShopCashTable.L1CashType at) {
		
		int aden_type = at.getCashType();

		if (aden_type == 0) {
			int tamCount = inv.getOwner().getAccount().getTamPoint();
			if (tamCount < orderList.getTotalPrice()) {
				inv.getOwner().sendPackets("無法消耗購買所需的TAM。");
				return;

				inv.getOwner().getAccount().addTamPoint(-orderList.getTotalPrice());
				inv.getOwner().sendPackets(new S_ACTION_UI(S_ACTION_UI.TAM, inv.getOwner().getAccount().getTamPoint()));
				inv.getOwner().getNetConnection().getAccount().updateTam();
			} else if (aden_type == -2) {
				int tamCount = inv.getOwner().getAccount().getBlessOfAinBonusPoint();
				if (tamCount < orderList.getTotalPrice()) {
					inv.getOwner().sendPackets("無法消耗購買所需的愛因哈薩德點數。");
					return;
				}
				inv.getOwner().getAccount().minusBlessOfAinBonusPoint(orderList.getTotalPrice());
				SC_EINHASAD_POINT_POINT_NOTI.send_point(inv.getOwner(), inv.getOwner().getAccount().getBlessOfAinBonusPoint());
			} else {
				if (aden_type == 41921) {
					if (inv.checkItem(41921, orderList.getTotalPrice())) {
						inv.consumeItem(41921, orderList.getTotalPrice());
						L1PcInstance pc = inv.getOwner();
//                    pc.getAccount().useTotalFeatherCount(orderList.getTotalPrice());
					} else {
						inv.getOwner().sendPackets("無法消耗購買所需的 " + at.getCashName() + "。");
						return;
					}
				} else {
					if (!inv.consumeItem(aden_type, orderList.getTotalPrice())) {
						inv.getOwner().sendPackets("無法消耗購買所需的 " + at.getCashName() + "。");
						return;
					}
				}
			}

		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			int enchantLevel = order.getItem().getEnchant();
			int endtime = order.getItem().getEndTime();
			boolean carving = order.getItem().isCarving();
			int bless = order.getItem().getBless();
			int attrenchant = order.getItem().getAttrEnchant();
			int buylevel = order.getItem().getBuyLevel();

			item = ItemTable.getInstance().createItem(itemId);

			if (!isShopBuyLimitItem(inv, order, item, amount)) {
				if (aden_type == 0) {
					inv.getOwner().getAccount().addTamPoint(order.getItem().getPrice() * amount);
					inv.getOwner().sendPackets(new S_ACTION_UI(S_ACTION_UI.TAM, inv.getOwner().getAccount().getTamPoint()));
					inv.getOwner().getNetConnection().getAccount().updateTam();
				} else {
					inv.getOwner().getInventory().storeItem(aden_type, order.getItem().getPrice() * amount);
				}
				continue;
			}
			
			if (aden_type == 0) {
				if (buylevel != 0) {
					if (inv.getOwner().getLevel() > buylevel) {
						inv.getOwner().sendPackets(String.format("該項目僅限 %d 等級以下的玩家購買。", buylevel));
						continue;
					}
				}
			} else {
				if (buylevel != 0) {
					if (inv.getOwner().getLevel() > buylevel) {
						inv.getOwner().sendPackets(String.format("該項目僅限 %d 等級以下的玩家購買。", buylevel));
						continue;
					}
				}
			}

			if (attrenchant > 0) {
				item.setAttrEnchantLevel(attrenchant);
			}

			if (carving) {
				item.set_Carving(1);
			}

			item.setBless(bless);

			if (endtime > 0) {
				Timestamp deleteTime = null;
				deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * endtime));
				item.setEndTime(deleteTime);
			}

			if (at.is_random_enchant_use()) {
				int chance = CommonUtil.random(100);
				int size = at.get_random_enchant().length;
				int enchant = 0;
				for (int i = 0; i < size; i++) {
					if (Integer.valueOf(at.get_random_enchant_chance()[i]) < chance) {
						enchant = Integer.valueOf(at.get_random_enchant()[i]);
						break;
					}
				}
				item.setEnchantLevel(enchant);
			} else {
				item.setEnchantLevel(enchantLevel);
			}

			onSellings(inv.getOwner(), order.getItem(), item);

			item.setCount(amount);
			item = inv.storeShopItem(item, true);
			L1PcInstance pc = inv.getOwner();
			obtained_item(pc, item);
		}
	}

	private boolean isShopBuyLimitItem(L1PcInventory inv, L1ShopBuyOrder order, L1ItemInstance item, int amount) {
		ShopBuyLimit sbl = ShopBuyLimitInfo.getInstance().getShopBuyLimit(order.getItem().getItemId());
		if (sbl != null) {
			ShopBuyLimit char_sbl = null;
			if (sbl.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT || sbl.get_type() == eShopBuyLimitType.ACCOUNT_WEEK_LIMIT)
				char_sbl = ShopBuyLimitInfo.getInstance().findShopBuyLimitByAccount(inv.getOwner().getAccountName(), item.getItem().getItemId());
			else 
				char_sbl = ShopBuyLimitInfo.getInstance().findShopBuyLimitByObjid(inv.getOwner().getId(), item.getItem().getItemId());
			
			if (char_sbl != null) {
				if (char_sbl.get_count() > 0) {
					int buy_result = char_sbl.get_count() - amount;
					int set_buy_result = buy_result < 0 ? 0 : buy_result;
					if (buy_result >= 0) {
						char_sbl.set_count(set_buy_result);
						if (char_sbl.get_count() == 0) {
							if (char_sbl.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT || char_sbl.get_type() == eShopBuyLimitType.CHARACTER_DAY_LIMIT) {
								char_sbl.set_end_time(new Timestamp(System.currentTimeMillis() + (86400 * 1000)));
							} else if (char_sbl.get_type() == eShopBuyLimitType.ACCOUNT_WEEK_LIMIT || char_sbl.get_type() == eShopBuyLimitType.CHARACTER_DAY_LIMIT) {
								char_sbl.set_end_time(new Timestamp(System.currentTimeMillis() + (86400 * 1000 * 7)));
							}

							SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String sLatestLoginDate = formatter.format(char_sbl.get_end_time());
							inv.getOwner()
									.sendPackets(String.format("\f3%s\f3無法購買，因為"
											+ (char_sbl.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT || char_sbl.get_type() == eShopBuyLimitType.ACCOUNT_WEEK_LIMIT ? "\f3同一帳號 " : "\f3")
											+ "只能在 %s 之後重新購買。", item.getLogName(), sLatestLoginDate));
						}
					} else {
						String type = char_sbl.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT || char_sbl.get_type() == eShopBuyLimitType.CHARACTER_DAY_LIMIT? "日限" : "週限";
						inv.getOwner().sendPackets(char_sbl.get_item_name() + "無法購買，因為已經達到 " + type + " " + char_sbl.get_count() + "個的購買限制。");
						// 購買限制訊息
						return false;
					}
				} else {
					String type = char_sbl.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT || char_sbl.get_type() == eShopBuyLimitType.CHARACTER_DAY_LIMIT ? "日限" : "週限";
					inv.getOwner().sendPackets(char_sbl.get_item_name() + "無法購買，因為已經達到" + type + " " + char_sbl.get_count() + "個的購買限制。");
					// 購買限制訊息
					return false;
				}
			} else {
				ShopBuyLimit new_sbl = new ShopBuyLimit();
				new_sbl.set_item_id(sbl.get_item_id());
				new_sbl.set_item_name(sbl.get_item_name());
				new_sbl.set_objid(inv.getOwner().getId());
				new_sbl.set_type(sbl.get_type());
				new_sbl.set_account_name(inv.getOwner().getAccount().getName());
				new_sbl.set_count(sbl.get_count());

				int buy_result = new_sbl.get_count() - amount;
				int set_buy_result = buy_result < 0 ? 0 : buy_result;
				if (buy_result >= 0) {
					new_sbl.set_count(set_buy_result);
					if (new_sbl.get_count() == 0) {
						if (new_sbl.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT || new_sbl.get_type() == eShopBuyLimitType.CHARACTER_DAY_LIMIT) {
							new_sbl.set_end_time(new Timestamp(System.currentTimeMillis() + (86400 * 1000)));
						} else if (new_sbl.get_type() == eShopBuyLimitType.ACCOUNT_WEEK_LIMIT || new_sbl.get_type() == eShopBuyLimitType.CHARACTER_DAY_LIMIT) {
							new_sbl.set_end_time(new Timestamp(System.currentTimeMillis() + (86400 * 1000 * 7)));
						}

						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String sLatestLoginDate = formatter.format(new_sbl.get_end_time());
						inv.getOwner()
								.sendPackets(String.format("\f3%s\f3無法購買，因為"
										+ (new_sbl.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT || new_sbl.get_type() == eShopBuyLimitType.ACCOUNT_WEEK_LIMIT? "\f3同一帳號 " : "\f3")
										+ "只能在 %s 之後重新購買。", item.getLogName(), sLatestLoginDate));

					}
				} else {
					String type = new_sbl.get_type() == eShopBuyLimitType.ACCOUNT_DAY_LIMIT || new_sbl.get_type() == eShopBuyLimitType.CHARACTER_DAY_LIMIT ? "日限" : "週限";
					inv.getOwner().sendPackets(new_sbl.get_item_name() + "無法購買，因為已經達到" + type + " " + new_sbl.get_count() + "個的購買限制。");
					// 購買限制訊息
					return false;
				}

				ShopBuyLimitInfo.getInstance().addShopBuyLimit(new_sbl);
			}
		}
		return true;
	}
	
	private boolean ensure_contribution_tokken(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		L1Clan clan = pc.getClan();
		if (!IntRange.includes(price, 0, 1000000000)) {
			pc.sendPackets("一次不能使用超過10億個。");
			return false;
		}

			if (clan.getContribution() < price) {
				pc.sendPackets("貢獻度不足。");
				return false;
			}
		int currentWeight = (int) (pc.getInventory().getWeight() * Config.ServerRates.RateWeightLimit);// 구매 무게
		if ((currentWeight + orderList.getTotalWeight() / 1000 > (pc.getMaxWeight() * Config.ServerRates.RateWeightLimit))) {
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
//		if (totalCount > 180) {
		if (totalCount > 200) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}
	
	private void sell_contribution_tokken(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		L1PcInstance pc = inv.getOwner();
		L1Clan clan = pc.getClan();
		
		if (clan.getContribution() < orderList.getTotalPrice()) {
			throw new IllegalStateException("無法消耗購買所需的貢獻度。");
		} else {
			clan.addClanShopContribution(orderList.getTotalPrice());
			ClanTable.getInstance().updateContribution(clan.getClanId(), clan.getContribution());
			clan.createOnlineMembers().forEach((L1Clan.ClanMember m) -> {
				if (m.player != null) {
					SC_BLOOD_PLEDGE_CONTRIBUTION_ACK.clan_contribution_send(m.player);
				}
			});
		}

		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			if (getSellingItems().contains(item)) {
				return;
			}
			item.setCount(amount);

			item = inv.storeShopItem(item, true);
			obtained_item(pc, item);
		}
	}
	
	public void obtained_item(L1PcInstance pc, L1ItemInstance item) {
		if (pc != null) {
			SC_OBTAINED_ITEM_INFO.send_obtained(pc, item, item.getCount());
		}
	}
}