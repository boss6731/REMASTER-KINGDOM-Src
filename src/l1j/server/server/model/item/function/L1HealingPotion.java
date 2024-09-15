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

import l1j.server.Config;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatInfo;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatLoader;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.Chain.Etc.MJHealingPotionDrinkChain;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;

@XmlAccessorType(XmlAccessType.FIELD)
public class L1HealingPotion {

	private static Logger _log = Logger.getLogger(L1HealingPotion.class.getName());

	private static Random _random = new Random(System.nanoTime());

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "ItemEffectList")
	private static class ItemEffectList implements Iterable<L1HealingPotion> {
		@XmlElement(name = "Item")
		private List<L1HealingPotion> _list;

		public Iterator<L1HealingPotion> iterator() {
			return _list.iterator();
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	private static class Effect {
		@XmlAttribute(name = "Min")
		private int _min;

		private int getMin() {
			return _min;
		}

		@XmlAttribute(name = "Max")
		private int _max;

		private int getMax() {
			return _max;
		}

		@XmlAttribute(name = "GfxId")
		private int _gfxid;

		private int getGfxId() {
			return _gfxid;
		}

		@XmlAttribute(name = "MapId")
		private int _mapid;

		private int getMapId() {
			return _mapid;
		}
	}

	private static final String _path = "./data/xml/Item/HealingPotion.xml";

	private static HashMap<Integer, L1HealingPotion> _dataMap = new HashMap<Integer, L1HealingPotion>();

	public static L1HealingPotion get(int id) {
		return _dataMap.get(id);
	}

	@XmlAttribute(name = "ItemId")
	private int _itemId;

	private int getItemId() {
		return _itemId;
	}

	@XmlAttribute(name = "Remove")
	private int _remove;

	private int getRemove() {
		return _remove;
	}

	@XmlElement(name = "Effect")
	private CopyOnWriteArrayList<Effect> _effects;

	private List<Effect> getEffects() {
		return _effects;
	}

	private static void loadXml(HashMap<Integer, L1HealingPotion> dataMap) {
		// PerformanceTimer timer = new PerformanceTimer();
		// System.out.print("■ 藥水恢復量數據 .......................... ");
		try {
			JAXBContext context = JAXBContext.newInstance(L1HealingPotion.ItemEffectList.class);

			Unmarshaller um = context.createUnmarshaller();

			File file = new File(_path);
			ItemEffectList list = (ItemEffectList) um.unmarshal(file);

			for (L1HealingPotion each : list) {
				if (ItemTable.getInstance().getTemplate(each.getItemId()) == null) {
					System.out.print("物品ID " + each.getItemId() + " 的模板未被找到。");
				} else {
					dataMap.put(each.getItemId(), each);
				}
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, _path + " 的讀取失敗。", e);
		}
		// System.out.println("■ 載入正常完成 " + timer.get() + "ms");
	}
	
	public static void load() {
		loadXml(_dataMap);
	}

	public static void reload() {
		HashMap<Integer, L1HealingPotion> dataMap = new HashMap<Integer, L1HealingPotion>();
		loadXml(dataMap);
		_dataMap = dataMap;
	}

	public boolean use(L1PcInstance pc, L1ItemInstance item) {
		if (pc.hasSkillEffect(L1SkillId.DECAY_POTION) == true) { // 디케이포션 상태
			pc.sendPackets(new S_ServerMessage(698)); // 마력에 의해 아무것도 마실 수가 없습니다.
			return false;
		}

		cancelAbsoluteBarrier(pc);
//		cancelMOEBIUS(pc);

		int maxChargeCount = item.getItem().getMaxChargeCount();
		int chargeCount = item.getChargeCount();
		if (maxChargeCount > 0 && chargeCount <= 0) {
			pc.sendPackets(new S_ServerMessage(79));
			return false;
		}

		Effect effect = null;
		for (Effect each : getEffects()) {
			if (each.getMapId() != 0 && pc.getMapId() != each.getMapId()) {
				continue;
			}
			effect = each;
			break;
		}

		if (effect == null) {
			pc.sendPackets(new S_ServerMessage(79));
			return false;
		}

		pc.sendPackets(new S_SkillSound(pc.getId(), effect.getGfxId()));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), effect.getGfxId()));
		pc.sendPackets(new S_ServerMessage(77)); // \f1기분이 좋아졌습니다.

		AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(pc.getId());
		int chance = effect.getMax() - effect.getMin();
		double healHp = effect.getMin();
		int crichance = MJRnd.next(10000);
		int potioncri = 0;
		if (Info != null) {
			if (Info.get_potion() != 0) {
				potioncri = Info.get_potion_val_1();
			}
		}
		if (chance > 0) {
			if(potioncri != 0 && crichance < potioncri) {
				healHp += effect.getMax() - effect.getMin();
//				System.out.println("healHp="+healHp);
			} else {
				healHp += _random.nextInt(chance) + 1;
//				System.out.println("healHp="+healHp);
			}
		}

		// System.out.println("前藥水恢復量 : " + healHp);
		healHp = MJHealingPotionDrinkChain.getInstance().do_drink(pc, healHp);
		// System.out.println("後藥水恢復量 : " + healHp);
		healHp *= (double) pc.getPotionRecoveryRatePct() / 100 + 1;

		if (pc != null && pc.isPassive(MJPassiveID.SURVIVE.toInt())) {
			/**
			 * TODO
			 * 例如，如果CON是15，則乘以2等於30，這意味著額外的恢復量在1到30之間隨機增加。
			 * 2的值越小，恢復量越少。
			 **/
			int con = pc.getAbility().getCon() * 2;
			int check_hp = (int) Math.round(pc.getMaxHp() * (45 * 0.01));
			if (pc.getCurrentHp() < check_hp) {
				int percent = Config.MagicAdSetting_Fencer.SURVIVE_PER * 10000;
				int plus_heal = (int) Math.round(healHp * (double)(con * Config.MagicAdSetting_Fencer.SURVIVE_ADDITION) / 100);
				if (MJRnd.isWinning(1000000, percent)) {
					if (pc.getInventory().consumeItem(41246, plus_heal)) {
						int potion_effect = 0;
						switch(item.getItemId()) {
						case 40010:
						case 140010:
						case 240010:
						case 40019:
						case 40022:
						case 40029:
						case 42658:
						case 4100152:
						case 4100464:
						case 4100691:
							potion_effect = 18566;
							break;
						case 7007:
						case 4100657:
						case 40011:
						case 140011:
						case 40020:
						case 40023:
						case 4100153:
						case 4100475:
						case 4100692:
							potion_effect = 18568;
							break;
						case 4100658:
						case 7008:
						case 40012:
						case 40021:
						case 140012:
						case 40024:
						case 4100021:
						case 4100154:
						case 4100693:
							potion_effect = 18570;
							break;
						}
						pc.send_effect(potion_effect, false);
						pc.getInventory().consumeItem(41246, plus_heal);
						healHp += plus_heal;
					}
				}
			}
		}
		
		pc.setCurrentHp(pc.getCurrentHp() + (int) healHp);
// System.out.println("後藥水恢復量 : " + healHp);
		
//		if (item.getItemId() != 4100691 && item.getItemId() != 4100692 && item.getItemId() != 4100693) {
			if (getRemove() > 0) {
				if (chargeCount > 0) {
					item.setChargeCount(chargeCount - getRemove());
					pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
				} else {
					pc.getInventory().removeItem(item, getRemove());
				}
			}
			return true;
//		}
//		return false;
	}

	private void cancelAbsoluteBarrier(L1PcInstance pc) { // 絕對屏障的解除
		if (pc.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER)) {
			pc.removeSkillEffect(L1SkillId.ABSOLUTE_BARRIER);
			pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 43, false));
		}
	}
	/*private void cancelMOEBIUS(L1PcInstance pc) { // 莫比烏斯的解除
	    if (pc.hasSkillEffect(L1SkillId.MOEBIUS)) {
	        pc.removeSkillEffect(L1SkillId.MOEBIUS);
	        L1SkillUse.off_icons(pc, L1SkillId.MOEBIUS);
	    }
	}*/
}
