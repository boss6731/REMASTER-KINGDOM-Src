package l1j.server.server.model.item;

import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
import l1j.server.server.datatables.ItemMessageBoxTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.favor.loader.L1FavorBookLoader;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ItemMessageBox;
import l1j.server.server.utils.CommonUtil;

@XmlAccessorType(XmlAccessType.FIELD)
public class L1TreasureBox {

	private static Logger _log = Logger.getLogger(L1TreasureBox.class.getName());

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "TreasureBoxList")
	private static class TreasureBoxList implements Iterable<L1TreasureBox> {
		@XmlElement(name = "TreasureBox")
		private List<L1TreasureBox> _list;

		public Iterator<L1TreasureBox> iterator() {
			return _list.iterator();
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	private static class Item {
		@XmlAttribute(name = "ItemId")
		private int _itemId;

		@XmlAttribute(name = "Count")
		private int _count;

		@XmlAttribute(name = "Enchant")
		private int _enchant;

		@XmlAttribute(name = "Attr")
		private int _attr;

		@XmlAttribute(name = "Identi")
		private boolean _identified;
		
		@XmlAttribute(name = "Time")
		private int _time;
		
		@XmlAttribute(name = "Bless")
		private int _bless = 1;
		
		@XmlAttribute(name = "Carving")
		private int _Carving;
		
		private int _chance;

		@XmlAttribute(name = "Chance")
		private void setChance(double chance) {
			_chance = (int) (chance * 10000);
		}

		public int getItemId() {
			return _itemId;
		}

		public int getCount() {
			return _count;
		}

		// 아이템 인첸트 레벨
		public int getEnchant() {
			return _enchant;
		}

		// 속성 인첸트 레벨
		public int getAttr() {
			return _attr;
		}

		// 확인 상태
		public boolean getIdentified() {
			return _identified;
		}

		public double getChance() {
			return _chance;
		}
		
		public int getTime(){
			return _time;
		}
		
		public int getBless(){
			return _bless;
		}
		
		public int get_Carving() {
			return _Carving;
		}
	}

	private static enum TYPE {
		RANDOM, SPECIFIC, RANDOM_SPECIFIC
	}

	private static final String PATH = "./data/xml/Item/TreasureBox.xml";

	private static final HashMap<Integer, L1TreasureBox> _dataMap = new HashMap<Integer, L1TreasureBox>();

	public static L1TreasureBox get(int id) {
		return _dataMap.get(id);
	}

	@XmlAttribute(name = "ItemId")
	private int _boxId;

	@XmlAttribute(name = "Type")
	private TYPE _type;

	private int getBoxId() {
		return _boxId;
	}

	private TYPE getType() {
		return _type;
	}

	@XmlElement(name = "Item")
	private CopyOnWriteArrayList<Item> _items;

	private List<Item> getItems() {
		return _items;
	}

	private int _totalChance;

	private int getTotalChance() {
		return _totalChance;
	}

	private void init() {
		for (Item each : getItems()) {
			_totalChance += each.getChance();
			if (ItemTable.getInstance().getTemplate(each.getItemId()) == null) {
				getItems().remove(each);
				_log.warning("物品 ID " + each.getItemId() + " 的模板未找到。");
				System.out.println("物品 ID " + each.getItemId() + " 的模板未找到。");
			}
		}
		if (getType() == TYPE.RANDOM && getTotalChance() != 1000000) {
			_log.warning("ID " + getBoxId() + " 的概率總和未達到 100%。");
			System.out.println("ID " + getBoxId() + " 的概率總和未達到 100%。");
		}
	}

	public static boolean load() {
		// PerformanceTimer timer = new PerformanceTimer();
		// System.out.print("■ 寶箱數據 .......................... ");
		try {
			JAXBContext context = JAXBContext.newInstance(L1TreasureBox.TreasureBoxList.class);

			Unmarshaller um = context.createUnmarshaller();

			File file = new File(PATH);
			TreasureBoxList list = (TreasureBoxList) um.unmarshal(file);

			for (L1TreasureBox each : list) {
				each.init();
				_dataMap.put(each.getBoxId(), each);
			}
			return true;
		} catch (Exception e) {
			_log.log(Level.SEVERE, PATH + " 加載失敗。", e);
			return false;
			// System.exit(0);
		}
			// System.out.println("■ 加載成功 " + timer.get() + "ms");
	}

	public boolean open(L1PcInstance pc) {
		L1ItemInstance item = null;
		Random random = null;

		if (pc._ErzabeBox == true && getBoxId() == 30102) {
			Random boxrandom = new Random();
			int[] itemrnd = { 3000090, 3000096, 41148 };// 랜덤아이템
			int ran1 = boxrandom.nextInt(3);
			item = pc.getInventory().storeItem(itemrnd[ran1], 1);
			L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "有人從艾爾扎貝的蛋中獲得了 " + item.getName() + "。"));
			pc._ErzabeBox = false;
		} else if (pc._SandwormBox == true && getBoxId() == 30103) {
			Random boxrandom = new Random();
			int[] itemrnd = { 3000092, 3000091, 3000095, 3000094, 210125, 5559 }; // 隨機物品
			int ran1 = boxrandom.nextInt(6);
			item = pc.getInventory().storeItem(itemrnd[ran1], 1);
			L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "有人從沙蟲的沙袋中獲得了 " + item.getName() + "。"));
			pc._SandwormBox = false;
		} else if (getType().equals(TYPE.SPECIFIC)) {
			for (Item each : getItems()) {
//				int itemid					= each.getItemId();
//				int itemcount				= each.getCount();
//				int enchantLevel			= each.getEnchant();
//				int attrEnchantLevel		= each.getAttr();
//				int bless					= each.getBless();

				item = ItemTable.getInstance().createItem(each.getItemId());
				if (item != null && !isOpen(pc)) {
					item.setCount(each.getCount());
					item.setEnchantLevel(each.getEnchant());
					item.setAttrEnchantLevel(each.getAttr());
					item.setIdentified(each.getIdentified());
					item.setBless(each.getBless());
					item.set_Carving(each.get_Carving());
					
					if(each.getTime() > 0){
						Timestamp deleteTime = null;
						deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * each.getTime()));
						item.setEndTime(deleteTime);
					}
					
					if (ItemMessageBoxTable.getInstance().isBoxMessage(getBoxId())) {
						L1ItemMessageBox temp = ItemMessageBoxTable.getInstance().getBoxMessage(getBoxId());
						String men = "";
						if (temp != null) {
							String[] bonusitem = (String[])MJArrangeParser.parsing(temp.getBonusItem(), ",", MJArrangeParseeFactory.createStringArrange()).result();
							String[] enchant = (String[])MJArrangeParser.parsing(temp.getEnchant(), ",", MJArrangeParseeFactory.createStringArrange()).result();
							for (int i = 0; i < bonusitem.length; i++) {
								int bitem = Integer.parseInt(bonusitem[i]);
								int benchant = Integer.parseInt(enchant[i]);
								
								if (item.getItemId() != bitem)
									continue;
								
								if (item.getEnchantLevel() != benchant)
									continue;

								if (temp.getMentoption().equalsIgnoreCase("某人")) {
									men = "某人 ";
								} else {
									men = "" + pc.getName() + " 您 ";

								if (temp.getMent() != null) {
//									String BoxName = ItemTable.getInstance().findItemIdByName(getBoxId());
									String BoxName = temp.getItemName();
									String itemName = item.getViewName();
									String message = String.format(men + temp.getMent(), BoxName, itemName);
									L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(message));
									L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, message));
								} else {
									// String BoxName = ItemTable.getInstance().findItemIdByName(getBoxId());
									String BoxName = temp.getItemName();
									String itemName = item.getViewName();
									if (itemName == null)
										itemName = item.getName();
									String message = String.format("" + men + "%s 在 " + BoxName + " 中獲得了。", itemName);
									String message2 = String.format("" + men + "%s 在 " + BoxName + " 中獲得了。", itemName);
									L1World.getInstance().broadcastPacketToAll(new ServerBasePacket[] { new S_SystemMessage(message), new S_PacketBox(S_PacketBox.GREEN_MESSAGE, message2) });
								}
							}
						}
					}
					storeItem(pc, item);
				}
			}

		} else if (getType().equals(TYPE.RANDOM)) {
//			random = new Random(System.nanoTime());
			int chance = 0;
//			int r = random.nextInt(getTotalChance());
			int r = CommonUtil.random(getTotalChance());
			for (Item each : getItems()) {
				chance += each.getChance();
				if (r < chance) {
					item = ItemTable.getInstance().createItem(each.getItemId());
					if (item != null && !isOpen(pc)) {
						item.setCount(each.getCount());
						item.setBless(each.getBless());
						item.setEnchantLevel(each.getEnchant());
					    item.set_Carving(each.get_Carving());
					    
						if(each.getTime() > 0){
							Timestamp deleteTime = null;
							deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * each.getTime()));
							item.setEndTime(deleteTime);
						}
						storeItem(pc, item);
						
						if (ItemMessageBoxTable.getInstance().isBoxMessage(getBoxId())) {
							L1ItemMessageBox temp = ItemMessageBoxTable.getInstance().getBoxMessage(getBoxId());
							String men = "";
							if (temp != null) {
								String[] bonusitem = (String[])MJArrangeParser.parsing(temp.getBonusItem(), ",", MJArrangeParseeFactory.createStringArrange()).result();
								String[] enchant = (String[])MJArrangeParser.parsing(temp.getEnchant(), ",", MJArrangeParseeFactory.createStringArrange()).result();
								for (int i = 0; i < bonusitem.length; i++) {
									int bitem = Integer.parseInt(bonusitem[i]);
									int benchant = Integer.parseInt(enchant[i]);
									
									if (item.getItemId() != bitem)
										continue;
									
									if (item.getEnchantLevel() != benchant)
										continue;
									
									if (temp.getMentoption().equalsIgnoreCase("某人"))
										men = "某人 ";
									else
										men = "" + pc.getName() + " 您 ";

									if (temp.getMent() != null) {
//										String BoxName = ItemTable.getInstance().findItemIdByName(getBoxId());
										String BoxName = temp.getItemName();
										String itemName = item.getViewName();
										String message = String.format(men + temp.getMent(), BoxName, itemName);
										L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(message));
										L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, message));
									} else {
//										String BoxName = ItemTable.getInstance().findItemIdByName(getBoxId());
										String BoxName = temp.getItemName();
										String itemName = item.getViewName();
										if (itemName == null)
											itemName = item.getName();
										String message = String.format("" + men + "%s 在 " + BoxName + " 中獲得了。", itemName);
										String message2 = String.format("" + men + "%s 在 " + BoxName + " 中獲得了。", itemName);
										L1World.getInstance().broadcastPacketToAll(new ServerBasePacket[] { new S_SystemMessage(message),new S_PacketBox(S_PacketBox.GREEN_MESSAGE, message2) });
									}
								}
							}
						}
						
					}
					break;
				}
			}
		} else if (getType().equals(TYPE.RANDOM_SPECIFIC)) {
			random = new Random(System.nanoTime());
			int chance = 0;

			int r = random.nextInt(getTotalChance());

			for (Item each : getItems()) {
				if (each.getChance() == 0) {
					item = ItemTable.getInstance().createItem(each.getItemId());
					if (item != null && !isOpen(pc)) {
						item.setCount(each.getCount());
						item.setBless(each.getBless());
						item.setEnchantLevel(each.getEnchant());
						item.set_Carving(each.get_Carving());
						
						if(each.getTime() > 0){
							Timestamp deleteTime = null;
							deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * each.getTime()));
							item.setEndTime(deleteTime);
						}
						
						if (ItemMessageBoxTable.getInstance().isBoxMessage(getBoxId())) {
							L1ItemMessageBox temp = ItemMessageBoxTable.getInstance().getBoxMessage(getBoxId());
							String men = "";
							if (temp != null) {
								String[] bonusitem = (String[])MJArrangeParser.parsing(temp.getBonusItem(), ",", MJArrangeParseeFactory.createStringArrange()).result();
								String[] enchant = (String[])MJArrangeParser.parsing(temp.getEnchant(), ",", MJArrangeParseeFactory.createStringArrange()).result();
								for (int i = 0; i < bonusitem.length; i++) {
									int bitem = Integer.parseInt(bonusitem[i]);
									int benchant = Integer.parseInt(enchant[i]);
									
									if (item.getItemId() != bitem)
										continue;
									
									if (item.getEnchantLevel() != benchant)
										continue;

									if (temp.getMentoption().equalsIgnoreCase("某人"))
										men = "某人 ";
									else
										men = "" + pc.getName() + " 您 ";

									if (temp.getMent() != null) {
//										String BoxName = ItemTable.getInstance().findItemIdByName(getBoxId());
										String BoxName = temp.getItemName();
										String itemName = item.getViewName();
										String message = String.format(men + temp.getMent(), BoxName, itemName);
										L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(message));
										L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, message));
									} else {
//										String BoxName = ItemTable.getInstance().findItemIdByName(getBoxId());
										String BoxName = temp.getItemName();
										String itemName = item.getViewName();
										if (itemName == null)
											itemName = item.getName();
										String message = String.format("" + men + "%s 在 " + BoxName + " 中獲得了。", itemName);
										String message2 = String.format("" + men + "%s 在 " + BoxName + " 中獲得了。", itemName);
										L1World.getInstance()
												.broadcastPacketToAll(new ServerBasePacket[] { new S_SystemMessage(message),
														new S_PacketBox(S_PacketBox.GREEN_MESSAGE, message2) });
									}
								}
							}
						}
						
						storeItem(pc, item);
					}
					continue;
				}
				chance += each.getChance();
				if (r < chance) {
					item = ItemTable.getInstance().createItem(each.getItemId());
					if (item != null && !isOpen(pc)) {
						item.setCount(each.getCount());
						item.setBless(each.getBless());
						item.setEnchantLevel(each.getEnchant());
						item.set_Carving(each.get_Carving());
						
						if(each.getTime() > 0){
							Timestamp deleteTime = null;
							deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * each.getTime()));
							item.setEndTime(deleteTime);
						}
						storeItem(pc, item);
						
						if (ItemMessageBoxTable.getInstance().isBoxMessage(getBoxId())) {
							L1ItemMessageBox temp = ItemMessageBoxTable.getInstance().getBoxMessage(getBoxId());
							String men = "";
							if (temp != null) {
								String[] bonusitem = (String[])MJArrangeParser.parsing(temp.getBonusItem(), ",", MJArrangeParseeFactory.createStringArrange()).result();
								String[] enchant = (String[])MJArrangeParser.parsing(temp.getEnchant(), ",", MJArrangeParseeFactory.createStringArrange()).result();
								for (int i = 0; i < bonusitem.length; i++) {
									int bitem = Integer.parseInt(bonusitem[i]);
									int benchant = Integer.parseInt(enchant[i]);
									
									if (item.getItemId() != bitem)
										continue;
									
									if (item.getEnchantLevel() != benchant)
										continue;

									if (temp.getMentoption().equalsIgnoreCase("某人"))
										men = "某人 ";
									else
										men = "" + pc.getName() + " 您 ";

									if (temp.getMent() != null) {
//										String BoxName = ItemTable.getInstance().findItemIdByName(getBoxId());
										String BoxName = temp.getItemName();
										String itemName = item.getViewName();
										String message = String.format(men + temp.getMent(), BoxName, itemName);
										L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(message));
										L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, message));
									} else {
//										String BoxName = ItemTable.getInstance().findItemIdByName(getBoxId());
										String BoxName = temp.getItemName();
										String itemName = item.getViewName();
										if (itemName == null)
											itemName = item.getName();
										String message = String.format("" + men + "%s 在 " + BoxName + " 中獲得了。", itemName);
										String message2 = String.format("" + men + "%s 在 " + BoxName + " 中獲得了。", itemName);
										L1World.getInstance()
												.broadcastPacketToAll(new ServerBasePacket[] { new S_SystemMessage(message),
														new S_PacketBox(S_PacketBox.GREEN_MESSAGE, message2) });
									}
								}
							}
						}
						
						// TODO 진 데스나이트의 유물 상자
						if (getBoxId() == 4100145) {
							if (each.getItemId() == 505011 || each.getItemId() == 505015 || each.getItemId() == 505012 || each.getItemId() == 620 || each.getItemId() == 625
									 || each.getItemId() == 626 || each.getItemId() == 623 || each.getItemId() == 618 || each.getItemId() == 619 || each.getItemId() == 616 
									 || each.getItemId() == 622 || each.getItemId() == 617) {
								L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "某位亞丁勇士 您在 真·死騎的遺物箱中 獲得了 \f3(" + item.getName() + ")\f2。"));
								L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("某位亞丁勇士 您在 真·死騎的遺物箱中 獲得了 (" + item.getName() + ")。"));
							}
							break;
						}
					}
					break;
				}
			}
		}

		if (item == null) {
			return false;
		} else {
			int itemId = getBoxId();
			
			if (itemId == 40576 || itemId == 40577 || itemId == 40578 || itemId == 40411 || itemId == 49013) {
				// pc.death(null, true);
			}
			if (itemId == 3000045) { // 고대 물품:무기
				int[] enchantrnd = { 0, 0, 0, 1, 1, 1, 2, 2, 0, 0, 0, 1, 1, 1, 2, 2, 3, 3, 3, 1, 2, 3, 4, 4, 0, 0, 0, 1,
						1, 1, 2, 2, 0, 0, 0, 1, 1, 1, 2, 6, 3, 3, 3, 1, 2, 3, 4, 4, 5, 1, 2, 3, 7 };
				int RandomEchant = random.nextInt(enchantrnd.length);
				item.setEnchantLevel(enchantrnd[RandomEchant]);
			}
			if (itemId >= 3000038 && itemId <= 3000044) { // 고대 물품:방어구
				int[] enchantrnd = { 0, 0, 0, 1, 1, 1, 2, 2, 0, 0, 0, 1, 1, 1, 2, 2, 3, 3, 3, 1, 2, 3, 4, 4, 0, 0, 0, 1,
						1, 1, 2, 2, 0, 0, 0, 1, 1, 1, 2, 2, 3, 3, 3, 1, 2, 3, 4, 4, 5 };
				int RandomEchant = random.nextInt(enchantrnd.length);
				item.setEnchantLevel(enchantrnd[RandomEchant]);
			}
			
			if (L1FavorBookLoader.isFavorItem(item.getItemId())) {
				pc.getFavorBook().setCraftBoxOpenResult(item);
			}
			
			if (itemId >= 30001834 && itemId <= 30001842) {
				pc.getInventory().setCraftBoxOpenResult(item);
			}
			return true;
		}
	}

	private boolean isOpen(L1PcInstance pc) {
		int totalCount = 0;
		totalCount = pc.getInventory().getSize();
		if (pc.getInventory().getWeight100() >= 82 || totalCount > 165) {
			pc.sendPackets(new S_SystemMessage("檢查背包：重量/數量超過限制，行動受限。"));
			return true;
		}
			return false;
		}

		private static void storeItem(L1PcInstance pc, L1ItemInstance item) {
			L1Inventory inventory;
			if (pc.getInventory().checkAddItem(item, item.getCount()) != L1Inventory.OK) {
				pc.sendPackets(new S_SystemMessage("您攜帶的物品太多了。"));
			return;
		} else {
			inventory = pc.getInventory();
		}
		
		if(item.getItem().isEndedTimeMessage())
			item.setOpenEffect(0x01);
		else 
			item.setOpenEffect(0x20);
		item.setIdentified(true);// 확인상태
//		inventory.storeItem(item);
		inventory.storeItemTrea(item);
		pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
	}
}
