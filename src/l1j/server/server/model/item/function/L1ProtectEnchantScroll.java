package l1j.server.server.model.item.function;

import java.io.File;
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

import l1j.server.server.clientpackets.C_ItemUSe;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;

@XmlAccessorType(XmlAccessType.FIELD)
public class L1ProtectEnchantScroll {
	private static Logger _log = Logger.getLogger(L1ProtectEnchantScroll.class.getName());

	private static Random _random = new Random(System.nanoTime());
	private static final String _path = "./data/xml/Item/ProtectEnchantScroll.xml";
	private static HashMap<Integer, L1ProtectEnchantScroll> _dataMap = new HashMap<Integer, L1ProtectEnchantScroll>();

	@XmlAttribute(name = "ItemId")
	private int _antiqueBookId;

	@XmlAttribute(name = "TargetItemId")
	private String _targetItemIds;

	@XmlAttribute(name = "NotargetItemIds")
	private String _NotargetItemIds;

	@XmlAttribute(name = "MaxEnchantLevel")
	private int _target_max_enchant;

	@XmlAttribute(name = "MinEnchantLevel")
	private int _target_min_enchant;

	@XmlElement(name = "Effect")
	private CopyOnWriteArrayList<Effect> _effects;

	public static L1ProtectEnchantScroll get(int id) {
		return (L1ProtectEnchantScroll) _dataMap.get(Integer.valueOf(id));
	}

	private int getAntiqueBookId() {
		return this._antiqueBookId;
	}

	public String getTargetItemIds() {
		return this._targetItemIds;
	}

	public String getNoTargetItemIds() {
		return this._NotargetItemIds;
	}

	public int getMaxEnchantLevel() {
		return _target_max_enchant;
	}

	public int getMinEnchantLevel() {
		return _target_min_enchant;
	}

	private List<Effect> getEffects() {
		return this._effects;
	}

	private boolean init() {
		if (ItemTable.getInstance().getTemplate(getAntiqueBookId()) == null) {
			System.out.println("不存在的物品編號。 " + getAntiqueBookId());
			return false;
		}
		String[] itemIdArray;
		String[] itemIdArray1;

		if (getNoTargetItemIds() != null) {
			itemIdArray1 = getNoTargetItemIds().split(",");
			for (int i = 0; i < itemIdArray1.length; i++) {
				if (ItemTable.getInstance().getTemplate(Integer.valueOf(itemIdArray1[i]).intValue()) == null) {
					System.out.println("不存在的物品編號。 " + itemIdArray1[i]);
					return false;
				}
			}
		}

		if (getTargetItemIds() != null) {
			itemIdArray = getTargetItemIds().split(",");
			for (int i = 0; i < itemIdArray.length; i++) {
				if (ItemTable.getInstance().getTemplate(Integer.valueOf(itemIdArray[i]).intValue()) == null) {
					System.out.println("不存在的物品編號。 " + itemIdArray[i]);
					return false;
				}
			}
		}

		for (Effect each : getEffects()) {
			int totalChance = 0;
			String[] probArray = each.getProbs().split(",");
			for (int i = 0; i < probArray.length; i++) {
				totalChance += Integer.valueOf(probArray[i]).intValue();
			}
			if (totalChance != 1000000) {
				_log.warning("(ID:" + getAntiqueBookId() + " / Enchant:" + each.getEnchantLevel() + ") 機率錯誤：總和為 " + totalChance + "%。請設置為 100%。");
			}
		}

		return true;
	}

	private static void loadXml(HashMap<Integer, L1ProtectEnchantScroll> dataMap) {
		try {
			JAXBContext context = JAXBContext.newInstance(new Class[] { ItemEffectList.class });

			Unmarshaller um = context.createUnmarshaller();

			File file = new File(_path);
			ItemEffectList list = (ItemEffectList) um.unmarshal(file);

			for (L1ProtectEnchantScroll each : list)
				if (each.init())
					dataMap.put(Integer.valueOf(each.getAntiqueBookId()), each);
		} catch (Exception e) {
			_log.log(Level.SEVERE, "./data/xml/Item/ProtectEnchantScroll.xml 載入失敗.", e);
		}
	}

	public static void load() {
		loadXml(_dataMap);
	}

	public static void reload() {
		HashMap<Integer, L1ProtectEnchantScroll> dataMap = new HashMap<Integer, L1ProtectEnchantScroll>();
		loadXml(dataMap);
		_dataMap = dataMap;
	}

	public boolean use(L1PcInstance pc, L1ItemInstance item, L1ItemInstance target) {
		int safe_enchant = target.getItem().get_safeenchant();
		if (safe_enchant < 0) {
			pc.sendPackets(new S_ServerMessage(79));
			return false;
		}

		if (target.getBless() >= 128) {
			pc.sendPackets(new S_ServerMessage(79));
			return false;
		}

		if (item.getItem().getUseType() == 26) { // 무기강화
			if (target.getItem().getType2() != 1) {
				pc.sendPackets(new S_ServerMessage(79));
				return false;
			}
		}

		if (item.getItem().getUseType() == 27) { // 갑옷강화
			if (target.getItem().getType2() != 2) {
				pc.sendPackets(new S_ServerMessage(79));
				return false;
			}
		}

		if (item.getItem().getUseType() == 46) { // 장신구강화
			if (!(target.getItem().getType2() == 2 && target.isAccessory())) {
				pc.sendPackets(new S_ServerMessage(79));
				return false;
			}
		}

		if (getNoTargetItemIds() != null) {
			boolean check1 = true;
			String[] itemIdArray1 = getNoTargetItemIds().split(",");
			for (int i = 0; i < itemIdArray1.length; i++) {
				if (target.getItemId() == Integer.valueOf(itemIdArray1[i]).intValue()) {
					check1 = false;
					break;
				}
			}

			if (!check1) {
				pc.sendPackets(new S_ServerMessage(79));
				return false;
			}
		}

		if (getTargetItemIds() != null) {
			boolean check = false;
			String[] itemIdArray = getTargetItemIds().split(",");
			for (int i = 0; i < itemIdArray.length; i++) {
				if (target.getItemId() == Integer.valueOf(itemIdArray[i]).intValue()) {
					check = true;
					break;
				}
			}

			if (!check) {
				pc.sendPackets(new S_ServerMessage(79));
				return false;
			}
		}

		int enchant_level = target.getEnchantLevel();
		if (getMinEnchantLevel() != 0) {
			if (enchant_level < getMinEnchantLevel()) {
				pc.sendPackets("\\aG強化 +" + getMinEnchantLevel() + " 之後才可使用。");
				return false;
			}
		}

		if (getMaxEnchantLevel() != 0) {
			if (enchant_level >= getMaxEnchantLevel()) {
				pc.sendPackets("\\aG強化 +" + getMaxEnchantLevel() + " 以上不可使用。");
				return false;
			}
		}

		Effect effect = null;
		for (Effect each : getEffects()) {
			if (each.getEnchantLevel() == target.getEnchantLevel()) {
				effect = each;
				break;
			}
		}

		if (effect == null) {
			pc.sendPackets(new S_ServerMessage(79));
			return false;
		}

		String[] probArray = effect.getProbs().split(",");
		int prob = 0;
		int chance = _random.nextInt(1000000);
		if (pc.isGm()) {
			pc.sendPackets("\aH[機率 : <-1(" + (prob + Integer.valueOf(probArray[0]).intValue()) + ")" + ", 0(" + (prob + Integer.valueOf(probArray[1]).intValue()) + ")" + ", +1("
					+ (prob + Integer.valueOf(probArray[2]).intValue()) + ")> / 隨機 : " + chance + "]");
		}

		if (chance < prob + Integer.valueOf(probArray[0]).intValue()) {
			C_ItemUSe.SuccessEnchant(pc, target, pc.getNetConnection(), -1);
			pc.sendPackets(new S_ServerMessage(1310, target.getLogName()));
		} else if (chance < prob + Integer.valueOf(probArray[1]).intValue()) {
			pc.sendPackets(new S_ServerMessage(1310, target.getLogName()));
		} else {
			C_ItemUSe.SuccessEnchant(pc, target, pc.getNetConnection(), 1);
		}

		if (item.getItem().getMaxChargeCount() > 0) {
			if (item.getChargeCount() > 0) {
				item.setChargeCount(item.getChargeCount() - 1);
				pc.getInventory().updateItem(item, 128);
			} else {
				pc.getInventory().removeItem(item, 1);
			}
		} else
			pc.getInventory().removeItem(item, 1);

		return true;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	private static class Effect {

		@XmlAttribute(name = "Enchant")
		private int _enchantLevel;

		@XmlAttribute(name = "Prob")
		private String _probs;

		private int getEnchantLevel() {
			return this._enchantLevel;
		}

		public String getProbs() {
			return this._probs;
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "ItemEffectList")
	private static class ItemEffectList implements Iterable<L1ProtectEnchantScroll> {

		@XmlElement(name = "Item")
		private List<L1ProtectEnchantScroll> _list;

		public Iterator<L1ProtectEnchantScroll> iterator() {
			return this._list.iterator();
		}
	}
}