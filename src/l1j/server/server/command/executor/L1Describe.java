package l1j.server.server.server.command.executor;

import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.server.Account;
import l1j.server.server.model.L1World;
import l1j.server.server.server.datatables.ExpTable;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;

import java.util.StringTokenizer;

public class L1Describe implements L1CommandExecutor {

	private L1Describe() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Describe();
	}

	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String name = st.nextToken();
			L1PcInstance target = L1World.getInstance(). getPlayer(name);
			if (target == null) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(73, name)));
				return;
			}
			int lv = target.getLevel();
			long currentLvExp = ExpTable.getExpByLevel(lv);
			long nextLvExp = ExpTable.getExpByLevel(lv + 1);
			double neededExp = nextLvExp - currentLvExp ;
			double currentExp =  target.get_exp() - currentLvExp;
			int per = (int)((currentExp / neededExp) * 100.0);

			String[] typeName = {"王族", "騎士", "妖精", "法師", "黑暗妖精", "龍騎士", "幻術師", "戰士", "劍士", "黃金槍騎"};

			pc.sendPackets("\\aD〓〓〓〓〓〓〓〓〓〓〓 帳號資訊 〓〓〓〓〓〓〓〓〓〓〓");
			pc.sendPackets(String.format("\\aG[角色名稱: %s] [職業: %s] [血盟: %s]", target.getName(), typeName[target.getType()], target.getClanname()));
			if (!target.noPlayerCK) {
				pc.sendPackets(String.format("\\aG[帳號: %s] [密碼: %s] [IP: %s]", target.getAccountName(), Account.load(target.getAccountName()).get_Password(), target.getNetConnection().getIp()));
			}
			pc.sendPackets("\\aD〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			pc.sendPackets(String.format("\\aH*[Lv(等級): %d.%d(百分比)] [Ac(防禦): %d] [MR(魔防): %d]", lv, per, target.getAC().getAc(), target.getResistance().getMr()));
			int hpr = target.getHpr() + target.getInventory().hpRegenPerTick();
			int mpr = target.getMpr() + target.getInventory().mpRegenPerTick();
			pc.sendPackets(String.format("\\aH*[HP(血量): %d/%d] [HP回復]: %d] [MP(魔): %d/%d] [MP回復]: %d]", target.getCurrentHp(), target.getMaxHp(), hpr, target.getCurrentMp(), target.getMaxMp(), mpr));

			pc.sendPackets(String.format("\\aE*[Str(力量): %d] [Dex(敏捷): %d] [Int(智力): %d] [Wis(精神): %d] [Con(體質): %d] [Cha(魅力): %d]", target.getAbility().getTotalStr(), target.getAbility().getTotalDex(),
					target.getAbility().getTotalInt(), target.getAbility().getTotalWis(), target.getAbility().getTotalCon(), target.getAbility().getTotalCha()));

			pc.sendPackets(String.format("\\aE*[屬性(火): %d] [屬性(水): %d] [屬性(風): %d] [屬性(地): %d]", target.getResistance().getFire(), target.getResistance().getWater(), target.getResistance().getWind(), target.getResistance().getEarth()));
					target.getResistance().getWind(), target.getResistance().getEarth()));

			pc.sendPackets(new S_ChatPacket(pc, String.format("\\aU*[技能抗性: %d] [精靈抗性: %d] [龍語抗性: %d] [恐懼抗性: %d] [總抗性: %d]",
					target.getSpecialResistance(eKind.ABILITY),
					target.getSpecialResistance(eKind.SPIRIT),
					target.getSpecialResistance(eKind.DRAGON_SPELL),
					target.getSpecialResistance(eKind.FEAR),
					target.getSpecialResistance(eKind.ALL))));

			pc.sendPackets(new S_ChatPacket(pc, String.format("\\aU*[技能命中: %d] [精靈命中: %d] [龍語命中: %d] [恐懼命中: %d] [總命中: %d]",
					target.getSpecialPierce(eKind.ABILITY),
					target.getSpecialPierce(eKind.SPIRIT),
					target.getSpecialPierce(eKind.DRAGON_SPELL),
					target.getSpecialPierce(eKind.FEAR),
					target.getSpecialPierce(eKind.ALL))));

			pc.sendPackets(String.format("\\aI*[近戰傷害: %d] [近戰命中: %d] [遠程傷害: %d] [遠程命中: %d] [Sp: %d]", target.getDmgup() + target.getDmgRate(), target.getHitup(),
					target.getBowDmgup() + target.getBowDmgRate(), target.getBowHitup(), target.getAbility().getSp()));
			pc.sendPackets(String.format("\\aI*[近戰致命: %d] [遠程致命: %d] [魔法致命: %d]", target.get_melee_critical_rate(), target.get_missile_critical_rate(), target.get_magic_critical_rate()));
			pc.sendPackets(String.format("\\aI*[PvP傷害減少: %d] [PvP額外傷害: %d]", (target.getResistance().getcalcPcDefense() + target.get_pvp_defense()), target.getResistance().getPVPweaponTotalDamage()));
			pc.sendPackets(String.format("\\aI*[傷害減少: %d] [祝福消耗效率: %d] [魔法命中: %d] [魔法致命(DB): %d]", target.getDamageReductionByArmor(), target.getEinhasadBlessper(),
					target.getBaseMagicHitUp(), target.getBaseMagicCritical()));
			pc.sendPackets(String.format("\\aI*[重量計量: %d] [物品經驗: %s]", target.getWeightReduction(), target.get_item_exp_bonus()));
			pc.sendPackets(String.format("\\aD*[HP: %d/%d] [HPR: %d] [MP: %d] [MPR: %d]", target.getCurrentHp(), target.getMaxHp(), hpr, target.getCurrentMp(), target.getMaxMp(), mpr));
			pc.sendPackets(String.format("\\aD*[Base(力量): %d] [Base(敏捷): %d] [Base(智力): %d] [Base(體質): %d] [Base(精神): %d] [Base(魅力): %d]", target.getAbility().getBaseStr(),
					target.getAbility().getBaseDex(), target.getAbility().getBaseInt(), target.getAbility().getBaseCon(), target.getAbility().getBaseWis(), target.getAbility().getBaseCha()));
			pc.sendPackets(String.format("\\aD*[總計(力量): %d] [總計(敏捷): %d] [總計(智力): %d] [總計(體質): %d] [總計(精神): %d] [總計(魅力): %d]", target.getAbility().getTotalStr(),
					target.getAbility().getTotalDex(), target.getAbility().getTotalInt(), target.getAbility().getTotalCon(), target.getAbility().getTotalWis(), target.getAbility().getTotalCha()));
					target.getAbility().getTotalDex(), target.getAbility().getTotalInt(), target.getAbility().getTotalCon(), target.getAbility().getTotalWis(), target.getAbility().getTotalCha()));
			pc.sendPackets(String.valueOf(new S_SystemMessage("\\aD--------------------------------------------------")));
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "請輸入 .info [角色名稱]。")));
		}
	}
}
