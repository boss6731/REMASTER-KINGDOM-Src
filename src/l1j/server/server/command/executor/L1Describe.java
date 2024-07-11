package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.server.Account;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;

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
                pc.sendPackets(new S_ServerMessage(73, name));
                return;
            }
            int lv = target.getLevel();
            long currentLvExp = ExpTable.getExpByLevel(lv);
            long nextLvExp = ExpTable.getExpByLevel(lv + 1);
            double neededExp = nextLvExp - currentLvExp ;
            double currentExp =  target.get_exp() - currentLvExp;
            int per = (int)((currentExp / neededExp) * 100.0);

            String[] typeName = {"王族", "騎士", "妖精", "魔法師", "黑暗精靈", "龍騎士", "幻術師", "戰士", "劍士", "黃金槍騎"};

            pc.sendPackets("\aD〓〓〓〓〓〓〓〓〓〓〓 帳號信息 〓〓〓〓〓〓〓〓〓〓〓");
            pc.sendPackets(String.format("\aG[角色名: %s] [職業: %s] [血盟: %s]", target.getName(), typeName[target.getType()], target.getClanname()));

            // 如果 target.noPlayerCK 為 false，則發送帳號相關信息
            if (!target.noPlayerCK) {
                pc.sendPackets(String.format("\aG[帳號: %s] [密碼: %s] [IP: %s]", target.getAccountName(), Account.load(target.getAccountName()).get_Password(), target.getNetConnection().getIp()));
            }

            pc.sendPackets("\aD〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
            pc.sendPackets(String.format("\aH*[等級(Lv): %d.%d(%) ] [防禦(Ac): %d] [魔防(MR): %d]", lv, per, target.getAC().getAc(), target.getResistance().getMr()));

            int hpr = target.getHpr() + target.getInventory().hpRegenPerTick(); // 計算 HP 回復速度
            int mpr = target.getMpr() + target.getInventory().mpRegenPerTick(); // 計算 MP 回復速度

            pc.sendPackets(String.format("\aH*[HP(血量): %d/%d] [HP回復(每Tick): %d] [MP(魔量): %d/%d] [MP回復(每Tick): %d]", target.getCurrentHp(), target.getMaxHp(), hpr, target.getCurrentMp(), target.getMaxMp(), mpr));

            pc.sendPackets(String.format("\aE*[力量(Str): %d] [敏捷(Dex): %d] [智力(Int): %d] [精神(WIS): %d] [體質(Con): %d] [魅力(Cha): %d]",
                    target.getAbility().getTotalStr(), target.getAbility().getTotalDex(),
                    target.getAbility().getTotalInt(), target.getAbility().getTotalWis(), target.getAbility().getTotalCon(), target.getAbility().getTotalCha()));

            pc.sendPackets(String.format("\aE*[火屬性: %d] [水屬性: %d] [風屬性: %d] [地屬性: %d]",
                    target.getResistance().getFire(), target.getResistance().getWater(),
                    target.getResistance().getWind(), target.getResistance().getEarth()));

            // 發送角色的特殊抗性信息
            pc.sendPackets(new S_ChatPacket(pc, String.format("\aU*[技能抗性: %d] [精靈抗性: %d] [龍言抗性: %d] [恐懼抗性: %d] [全體抗性: %d]",
                    target.getSpecialResistance(eKind.ABILITY), // 技能抗性
                    target.getSpecialResistance(eKind.SPIRIT), // 精靈抗性
                    target.getSpecialResistance(eKind.DRAGON_SPELL), // 龍言抗性
                    target.getSpecialResistance(eKind.FEAR), // 恐懼抗性
                    target.getSpecialResistance(eKind.ALL)))); // 全體抗性

            // 發送角色的特殊命中信息
            pc.sendPackets(new S_ChatPacket(pc, String.format("\aU*[技能命中: %d] [精靈命中: %d] [龍言命中: %d] [恐懼命中: %d] [全體命中: %d]",
                    target.getSpecialPierce(eKind.ABILITY), // 技能命中
                    target.getSpecialPierce(eKind.SPIRIT), // 精靈命中
                    target.getSpecialPierce(eKind.DRAGON_SPELL), // 龍言命中
                    target.getSpecialPierce(eKind.FEAR), // 恐懼命中
                    target.getSpecialPierce(eKind.ALL)))); // 全體命中

            // 發送角色的近距離和遠距離戰鬥屬性數據
            pc.sendPackets(String.format("\aI*[近距離傷害: %d] [近距離命中: %d] [遠距離傷害: %d] [遠距離命中: %d] [SP: %d]",
                    target.getDmgup() + target.getDmgRate(), // 近距離傷害加成
                    target.getHitup(), // 近距離命中加成
                    target.getBowDmgup() + target.getBowDmgRate(), // 遠距離傷害加成
                    target.getBowHitup(), // 遠距離命中加成
                    target.getAbility().getSp())); // SP（法術點數）

            // 發送角色的各種暴擊數據
            pc.sendPackets(String.format("\aI*[近距離暴擊率: %d] [遠距離暴擊率: %d] [魔法暴擊率: %d]",
                    target.get_melee_critical_rate(), // 近距離暴擊率
                    target.get_missile_critical_rate(), // 遠距離暴擊率
                    target.get_magic_critical_rate())); // 魔法暴擊率

            // 發送角色在PvP中的傷害減少和增加數據
            pc.sendPackets(String.format("\aI*[PvP傷害減少: %d] [PvP附加傷害: %d]",
                    (target.getResistance().getcalcPcDefense() + target.get_pvp_defense()), // PvP傷害減少
                    target.getResistance().getPVPweaponTotalDamage())); // PvP附加傷害

            // 發送角色的其他屬性數據，如傷害減少、祝福消耗效率、魔法命中和魔法暴擊
            pc.sendPackets(String.format("\aI*[傷害減少: %d] [祝福消耗效率: %d] [魔法命中: %d] [魔法暴擊(DB): %d]",
                    target.getDamageReductionByArmor(), // 傷害減少
                    target.getEinhasadBlessper(), // 祝福消耗效率
                    target.getBaseMagicHitUp(), // 基礎魔法命中加成
                    target.getBaseMagicCritical())); // 基礎魔法暴擊率
            try {
                // 發送角色的重量和物品經驗加成
                pc.sendPackets(String.format("\aI*[負重比例: %d] [物品經驗加成: %s]",
                        target.getWeightReduction(), // 負重比例
                        target.get_item_exp_bonus())); // 物品經驗加成

                // 發送角色的 HP 和 MP 信息
                pc.sendPackets(String.format("\aD*[HP: %d/%d] [HP回復速度(HPR): %d] [MP: %d/%d] [MP回復速度(MPR): %d]",
                        target.getCurrentHp(), target.getMaxHp(), hpr, // HP 和 HP 回復速度
                        target.getCurrentMp(), target.getMaxMp(), mpr)); // MP 和 MP 回復速度

                // 發送角色的基礎屬性信息
                pc.sendPackets(String.format("\aD*[基礎(力量): %d] [基礎(敏捷): %d] [基礎(智力): %d] [基礎(體質): %d] [基礎(精神): %d] [基礎(魅力): %d]",
                        target.getAbility().getBaseStr(), // 基礎力量
                        target.getAbility().getBaseDex(), // 基礎敏捷
                        target.getAbility().getBaseInt(), // 基礎智力
                        target.getAbility().getBaseCon(), // 基礎體質
                        target.getAbility().getBaseWis(), // 基礎智慧
                        target.getAbility().getBaseCha())); // 基礎魅力

                // 發送角色的總屬性信息
                pc.sendPackets(String.format("\aD*[總和(力量): %d] [總和(敏捷): %d] [總和(智力): %d] [總和(體質): %d] [總和(精神): %d] [總和(魅力): %d]",
                        target.getAbility().getTotalStr(), // 總力量
                        target.getAbility().getTotalDex(), // 總敏捷
                        target.getAbility().getTotalInt(), // 總智力
                        target.getAbility().getTotalCon(), // 總體質
                        target.getAbility().getTotalWis(), // 總智慧
                        target.getAbility().getTotalCha())); // 總魅力

                // 發送分隔線
                pc.sendPackets(new S_SystemMessage("\aD--------------------------------------------------"));
            } catch (Exception e) {
                // 捕捉異常並提示正確的指令格式
                pc.sendPackets(new S_ChatPacket(pc, ".信息 輸入[角色名]."));
            }

