package l1j.server.server.model.item.function;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import l1j.server.Config;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;

@XmlAccessorType(XmlAccessType.FIELD)
public class L1Material {
	private static Logger _log = Logger.getLogger(L1Material.class.getName());

	private static final String _path = "./data/xml/Item/Material.xml";
	private static HashMap<Integer, L1Material> _dataMap = new HashMap<Integer, L1Material>();

	@XmlAttribute(name = "ItemId")
	private int _itemId;

	@XmlAttribute(name = "Remove")
	private int _remove;

	@XmlElement(name = "Effect")
	private Effect _effect;

	public static L1Material get(int id) {
		return (L1Material) _dataMap.get(Integer.valueOf(id));
	}

	private int getItemId() {
		return this._itemId;
	}

	private int getRemove() {
		return this._remove;
	}

	private Effect getEffect() {
		return this._effect;
	}

	private boolean init() {
		if (ItemTable.getInstance().getTemplate(getItemId()) == null) {
			System.out.println("不存在的物品編號。 " + getItemId());
			return false;
		}
		Effect effect = getEffect();

		if (ItemTable.getInstance().getTemplate(effect.getItemId()) == null) {
			System.out.println("不存在的物品編號。 " + getItemId());
			return false;
		}

		for (String Itemid : effect.getNeedItemId().split(",")) {
			if (ItemTable.getInstance().getTemplate(Integer.valueOf(Itemid)) == null) {
				System.out.println("不存在的物品編號。 " + Itemid);
				return false;
			}
		}
		return true;
	}

	private static void loadXml(HashMap<Integer, L1Material> dataMap) {
		try {
			JAXBContext context = JAXBContext.newInstance(new Class[] { ItemEffectList.class });

			Unmarshaller um = context.createUnmarshaller();

			File file = new File(_path);
			ItemEffectList list = (ItemEffectList) um.unmarshal(file);

			for (L1Material each : list)
				if (each.init())
					dataMap.put(Integer.valueOf(each.getItemId()), each);
		} catch (Exception e) {
			_log.log(Level.SEVERE, "./data/xml/Item/Material.xml 載入失敗.", e);
		}
	}

	public static void load() {
		loadXml(_dataMap);
	}

	public static void reload() {
		HashMap<Integer, L1Material> dataMap = new HashMap<Integer, L1Material>();
		loadXml(dataMap);
		_dataMap = dataMap;
	}

	public boolean use(L1PcInstance pc, L1ItemInstance item) {
		if (Config.Login.StandbyServer) {
			pc.sendPackets(new S_SystemMessage("開放等待中無法進行的動作。"));
			return false;
		}

		Effect effect = getEffect();

		String[] needItem = effect.getNeedItemId().split(",");
		String[] needCount = effect.getNeedCount().split(",");
		int need_size = needItem.length;
		boolean need_item_check = true;
		if (need_size != 0) {
			for (int i = 0; i < need_size; i++) {
				if(!pc.getInventory().checkItem(Integer.valueOf(needItem[i]), Integer.valueOf(needCount[i]))) {
					need_item_check = false;
					break;
				}
			}
		}
		
		if(!need_item_check) {
			for (int i = 0; i < need_size; i++) {
				if(!pc.getInventory().checkItem(Integer.valueOf(needItem[i]), Integer.valueOf(needCount[i]))) {
					L1Item tem = ItemTable.getInstance().getTemplate(Integer.valueOf(needItem[i]));
					pc.sendPackets("\\aG使用所需的材料不足。 - [" + tem.getName() + " (" + Integer.valueOf(needCount[i]) + ")個]");
				}
			}
			return false;
		}
		
		for (int i = 0; i < need_size; i++) {
			pc.getInventory().consumeItem(Integer.valueOf(needItem[i]), Integer.valueOf(needCount[i]));
		}
		
		L1ItemInstance give_item = pc.getInventory().storeItem(effect.getItemId(), effect.getItemCount());
		pc.sendPackets(new S_ServerMessage(403, give_item.getLogName()));
		give_item.setIdentified(true);
		pc.getInventory().updateItem(give_item, L1PcInventory.COL_IS_ID);
		
		if (getRemove() > 0) {
			pc.getInventory().removeItem(item, getRemove());
		}

		return true;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	private static class Effect {

		@XmlAttribute(name = "ItemId")
		private int _itemId;
		
		@XmlAttribute(name = "ItemCount")
		private int _itemCount;

		@XmlAttribute(name = "NeedItemId")
		private String _needItemId;

		@XmlAttribute(name = "NeedCount")
		private String _needCount;

		public int getItemId() {
			return this._itemId;
		}
		
		public int getItemCount() {
			return this._itemCount;
		}

		public String getNeedItemId() {
			return this._needItemId;
		}

		public String getNeedCount() {
			return this._needCount;
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "ItemEffectList")
	private static class ItemEffectList implements Iterable<L1Material> {

		@XmlElement(name = "Item")
		private List<L1Material> _list;

		public Iterator<L1Material> iterator() {
			return this._list.iterator();
		}
	}
}