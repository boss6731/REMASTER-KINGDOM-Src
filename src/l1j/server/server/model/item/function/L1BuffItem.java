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
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;

@XmlAccessorType(XmlAccessType.FIELD)
public class L1BuffItem {
	private static Logger _log = Logger.getLogger(L1BuffItem.class.getName());

	private static final String _path = "./data/xml/Item/BuffItem.xml";
	private static HashMap<Integer, L1BuffItem> _dataMap = new HashMap<Integer, L1BuffItem>();

	@XmlAttribute(name = "ItemId")
	private int _itemId;

	@XmlAttribute(name = "Remove")
	private int _remove;

	@XmlAttribute(name = "NeedItemId")
	private int _needitemId;

	@XmlAttribute(name = "NeedCount")
	private int _needCount;

	@XmlElement(name = "Effect")
	private Effect _effect;

	public static L1BuffItem get(int id) {
		return (L1BuffItem) _dataMap.get(Integer.valueOf(id));
	}

	private int getItemId() {
		return this._itemId;
	}

	private int getRemove() {
		return this._remove;
	}

	private int getNeedItemId() {
		return _needitemId;
	}

	private int getNeedCount() {
		return _needCount;
	}

	private Effect getEffect() {
		return this._effect;
	}

	private boolean init() {
		if (ItemTable.getInstance().getTemplate(getItemId()) == null) {
			System.out.println("不存在的物品編號: " + getItemId());
			return false;
		}
		Effect effect = getEffect();
		for (String skillId : effect.getSkillIds().split(",")) {
			if (SkillsTable.getInstance().getTemplate(Integer.valueOf(skillId)) == null) {
				System.out.println("不存在的技能編號: " + skillId);
				return false;
			}
		}
		return true;
	}

	private static void loadXml(HashMap<Integer, L1BuffItem> dataMap) {
		try {
			JAXBContext context = JAXBContext.newInstance(new Class[] { ItemEffectList.class });

			Unmarshaller um = context.createUnmarshaller();

			File file = new File(_path);
			ItemEffectList list = (ItemEffectList) um.unmarshal(file);

			for (L1BuffItem each : list)
				if (each.init())
					dataMap.put(Integer.valueOf(each.getItemId()), each);
		} catch (Exception e) {
			_log.log(Level.SEVERE, "./data/xml/Item/BuffItem.xml load failed.", e);
		}
	}

	public static void load() {
		loadXml(_dataMap);
	}

	public static void reload() {
		HashMap<Integer, L1BuffItem> dataMap = new HashMap<Integer, L1BuffItem>();
		loadXml(dataMap);
		_dataMap = dataMap;
	}

	public boolean use(L1PcInstance pc, L1ItemInstance item) {
		if (Config.Login.StandbyServer) {
			pc.sendPackets(new S_SystemMessage("開啟等待中無法執行此操作。"));
			return false;
		}

		if (pc.isstop()) {
			return false;
		}

		if (getNeedItemId() != 0) {
			if (!pc.getInventory().consumeItem(getNeedItemId(), getNeedCount())) {
				L1Item needItem = ItemTable.getInstance().getTemplate(getNeedItemId());
				pc.sendPackets("\\aG無法使用：缺少 " + needItem + "(" + getNeedCount() + ")");
				return false;
			}
		}

		Effect effect = getEffect();

		pc.setBuffnoch(1);// 추가
		for (String skillId : effect.getSkillIds().split(",")) {
			new L1SkillUse().handleCommands(pc, Integer.valueOf(skillId), pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
		}
		pc.setBuffnoch(0);// 추가

		if (getRemove() > 0) {
			pc.getInventory().removeItem(item, getRemove());
		}

		return true;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	private static class Effect {

		@XmlAttribute(name = "SkillId")
		private String _skillId;

		public String getSkillIds() {
			return this._skillId;
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "ItemEffectList")
	private static class ItemEffectList implements Iterable<L1BuffItem> {

		@XmlElement(name = "Item")
		private List<L1BuffItem> _list;

		public Iterator<L1BuffItem> iterator() {
			return this._list.iterator();
		}
	}
}