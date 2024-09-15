package l1j.server.server.model.item.function;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ItemSelector;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;

@XmlAccessorType(XmlAccessType.FIELD)
public class L1ItemSelector {
	private static Logger _log = Logger.getLogger(L1ItemSelector.class.getName());
	private static final String _path = "./data/xml/Item/ItemSelector.xml";
	private static HashMap<Integer, L1ItemSelector> _dataMap = new HashMap<Integer, L1ItemSelector>();
	
	@XmlAttribute(name = "ItemId")
	private int _itemId;

	@XmlAttribute(name = "Remove")
	private int _remove;

	@XmlElement(name = "Effect")
	private CopyOnWriteArrayList<Effect> _effects;
	
	public static L1ItemSelector get(int id) {
		return _dataMap.get(Integer.valueOf(id));
	}

	private int getItemId() {
		return this._itemId;
	}

	private int getRemove() {
		return this._remove;
	}

	private List<Effect> getEffects() {
		return this._effects;
	}
	
	private boolean init() {
		if (ItemTable.getInstance().getTemplate(getItemId()) == null) {
			System.out.println("BoxItem : 不存在的物品編號: " + getItemId());
			return false;
		}
		for (Effect each : getEffects()) {
			if (ItemTable.getInstance().getTemplate(each.getItemId()) == null) {
				System.out.println("BoxItem : 不存在的物品編號: " + each.getItemId());

				return false;
			}
		}
		return true;
	}
	
	private static void loadXml(HashMap<Integer, L1ItemSelector> dataMap) {
		try {
			JAXBContext context = JAXBContext.newInstance(new Class[] { ItemEffectList.class });

			Unmarshaller um = context.createUnmarshaller();

			File file = new File(_path);
			ItemEffectList list = (ItemEffectList) um.unmarshal(file);
			for (L1ItemSelector each : list) {
				if (each.init()) {
					dataMap.put(Integer.valueOf(each.getItemId()), each);
				}
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, "./data/xml/Item/ItemSelector.xml 加載失敗。", e);
		}
	}

	public static void load() {
		loadXml(_dataMap);
	}

	public static void reload() {
		HashMap<Integer, L1ItemSelector> dataMap = new HashMap<Integer, L1ItemSelector>();
		loadXml(dataMap);
		_dataMap = dataMap;
	}

	public boolean use(L1PcInstance pc, L1ItemInstance item) {
		int maxChargeCount = item.getItem().getMaxChargeCount();
		int chargeCount = item.getChargeCount();
		if (maxChargeCount > 0 && chargeCount <= 0) {
			pc.sendPackets(new S_ServerMessage(79));
			return false;
		}

		List<Effect> effect = new ArrayList<Effect>();
		for (Effect each : getEffects()) {
			if (pc.getClassFeature().getClassInitial().equalsIgnoreCase(each.getClassInitial()) || each.getClassInitial().equalsIgnoreCase("A")) {
				effect.add(each);
			}
		}

		if (effect.size() > 0) {
			
			
			for (Effect ec : effect) {
				L1PcInventory targetInven = pc.getInventory();
				L1ItemInstance new_item = ItemTable.getInstance().createItem(ec.getItemId());
				new_item.setEnchantLevel(ec.getEnchant());
				new_item.setCount(ec.getCount());
				new_item.setBless(ec.getBless());
				new_item.setAttrEnchantLevel(ec.getAttr());
				new_item.setSupportItem(ec.getSupportItem() == 1 ? true : false);
				new_item.set_Carving(ec.get_Carving());

				if (ec.getUseTime() != 0) {
					SetDeleteTime(new_item, ec.getUseTime());
				}
				new_item.setIdentified(true);
				targetInven.storeItem(new_item, ec.getBless());

				if (ec.getBless() >= 128) {
					int st = 0;
					if (new_item.isIdentified())
						st += 1;
					if (!new_item.getItem().isTradable())
						st += 2;
					if (!new_item.getItem().isCantDelete())
						st += 4;
					if (new_item.getItem().get_safeenchant() < 0)
						st += 8;
					if (new_item.getBless() >= 128) {
						st = 32;
						if (new_item.isIdentified()) {
							st += 15;
						} else {
							st += 14;
						}
					}

					pc.sendPackets(new S_PacketBox(S_PacketBox.ITEM_STATUS, new_item, st));
					pc.getInventory().updateItem(new_item, L1PcInventory.COL_IS_ID);
				}
				pc.sendPackets(new S_ServerMessage(403, new_item.getLogName()));
			}

			if (getRemove() > 0) {
				if (chargeCount > 0) {
					item.setChargeCount(chargeCount - getRemove());
					pc.getInventory().updateItem(item, 128);
				} else {
					pc.getInventory().removeItem(item, getRemove());
				}
			}
		} else {
			pc.sendPackets("沒有對應該職業的物品。");
		}
		return true;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	private static class Effect {

		@XmlAttribute(name = "ItemId")
		private int _itemId;

		@XmlAttribute(name = "Enchant")
		private int _enchant;

		@XmlAttribute(name = "Count")
		private int _count;

		@XmlAttribute(name = "Attr")
		private int _attr;

		@XmlAttribute(name = "Bless")
		private int _bless;

		@XmlAttribute(name = "ClassInitial")
		private String _classInitial;

		@XmlAttribute(name = "UseTime")
		private int _usetime;

		@XmlAttribute(name = "SupportItem")
		private int _supportItem;

		@XmlAttribute(name = "Carving")
		private int _Carving;

		public int getItemId() {
			return this._itemId;
		}

		public int getEnchant() {
			return _enchant;
		}

		public int getCount() {
			return _count;
		}

		public int getBless() {
			return _bless;
		}

		public int getAttr() {
			return _attr;
		}

		private String getClassInitial() {
			return this._classInitial;
		}

		public int getUseTime() {
			return _usetime;
		}

		public int getSupportItem() {
			return _supportItem;
		}

		public int get_Carving() {
			return _Carving;
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "ItemEffectList")
	private static class ItemEffectList implements Iterable<L1ItemSelector> {

		@XmlElement(name = "Item")
		private List<L1ItemSelector> _list;

		public Iterator<L1ItemSelector> iterator() {
			return this._list.iterator();
		}
	}

	private void SetDeleteTime(L1ItemInstance item, int minute) {
		Timestamp deleteTime = null;
		deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * minute));
		item.setEndTime(deleteTime);
	}
	
	
	
	
	
}