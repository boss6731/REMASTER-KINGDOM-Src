package l1j.server.server.command.executor;

import l1j.server.server.IdFactory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.server.command.executor.L1CommandExecutor;
import l1j.server.server.server.datatables.CharacterTable;
import l1j.server.server.server.datatables.ItemTable;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

import java.util.Random;
import java.util.StringTokenizer;

public class L1FakeCharacter implements L1CommandExecutor {

	private static Random _random =  new Random(System.nanoTime());


	//	private static final int[] MALE_LIST = new int[] { 61, 138, 734, 2786, 6658, 6671, 12490 };
	//	private static final int[] FEMALE_LIST = new int[] { 48, 37, 1186, 2796, 6661, 6650, 12494 };

	private L1FakeCharacter() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1FakeCharacter();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer stringtokenizer = new StringTokenizer(arg);
			String name = stringtokenizer.nextToken();

			if (CharacterTable.getInstance().isContainNameList(name) || L1World.getInstance().getPlayer(name) != null) {
				pc.sendPackets(String.valueOf(new S_SystemMessage("該角色名稱已存在")));
				return;
			}
			L1PcInstance newPc = new L1PcInstance();
			newPc.setAccountName("");
			newPc.setId(IdFactory.getInstance().nextId());
			newPc.setName(name);
			newPc.setHighLevel(1);
			newPc.set_exp(0);
			newPc.addBaseMaxHp((short)2000);//14
			newPc.setCurrentHp(2000);//14
			newPc.setDead(false);
			newPc.setStatus(0);
			newPc.addBaseMaxMp((short)2);
			newPc.setCurrentMp(2);			
			newPc.getAbility().setBaseStr(16);
			newPc.getAbility().setStr(16);
			newPc.getAbility().setBaseCon(16);
			newPc.getAbility().setCon(16);
			newPc.getAbility().setBaseDex(11);
			newPc.getAbility().setDex(11);
			newPc.getAbility().setBaseCha(13);
			newPc.getAbility().setCha(13);
			newPc.getAbility().setBaseInt(12);
			newPc.getAbility().setInt(12);
			newPc.getAbility().setBaseWis(11);
			newPc.getAbility().setWis(11);
			int ran = _random.nextInt(120);
			// 職業更新
			if (ran >= 0 && ran < 15) { // 15，男騎士的機率為15%
				newPc.setClassId(20553);
				newPc.setCurrentSprite(20553);
				newPc.setType(20553);
			} else if (ran >= 15 && ran < 20){ // 5，女角色的機率為5%，因為私人伺服器中很少有人玩女角色。
				newPc.setClassId(20554); // 假設女角色的 ID 是 20554
				newPc.setCurrentSprite(20554);
				newPc.setType(20554);
				newPc.setClassId(48);
				newPc.setCurrentSprite(48);
				newPc.setType(48);
			} else if (ran >= 20 && ran < 30){ // 10
				newPc.setClassId(138);
				newPc.setCurrentSprite(138);
				newPc.setType(138);
			} else if (ran >= 30 && ran < 35){ // 5
				newPc.setClassId(37);
				newPc.setCurrentSprite(37);
				newPc.setType(37);
			} else if (ran >= 35 && ran < 45){ // 10
				newPc.setClassId(20278);
				newPc.setCurrentSprite(20278);
				newPc.setType(20278);
			} else if (ran >= 45 && ran < 50){ // 5
				newPc.setClassId(20279);
				newPc.setCurrentSprite(20279);
				newPc.setType(20279);
			} else if (ran >= 50 && ran < 60){// 10
				newPc.setClassId(2786);
				newPc.setCurrentSprite(2786);
				newPc.setType(2786);
			} else if (ran >= 60 && ran < 65){ // 5
				newPc.setClassId(2796);
				newPc.setCurrentSprite(2796);
				newPc.setType(2796);
			} else if (ran >= 65 && ran < 75){ // 10
				newPc.setClassId(6658);
				newPc.setCurrentSprite(6658);
				newPc.setType(6658);
			} else if (ran >= 75 && ran < 80){// 5
				newPc.setClassId(6661);
				newPc.setCurrentSprite(6661);
				newPc.setType(6661);
			} else if (ran >= 80 && ran < 90){
				newPc.setClassId(6671);
				newPc.setCurrentSprite(6671);
				newPc.setType(6671);
			} else if (ran >= 90 && ran < 100){
				newPc.setClassId(6650);
				newPc.setCurrentSprite(6650);
				newPc.setType(6650);
			} else if (ran >= 100 && ran < 110){
				newPc.setClassId(20567);
				newPc.setCurrentSprite(20567);
				newPc.setType(20567);
			} else if (ran >= 110 && ran < 120){
				newPc.setClassId(20577);
				newPc.setCurrentSprite(20577);
				newPc.setType(20577);
			} else if (ran >= 120 && ran < 130){
				newPc.setClassId(18520);
				newPc.setCurrentSprite(18520);
				newPc.setType(18520);
			} else if (ran >= 130 && ran < 140){
				newPc.setClassId(18499);
				newPc.setCurrentSprite(18499);
				newPc.setType(18499);
			} else if (ran >= 140 && ran < 150){
				newPc.setClassId(19296);
				newPc.setCurrentSprite(19296);
				newPc.setType(19296);
			} else if (ran >= 150 && ran < 160){
				newPc.setClassId(19299);
				newPc.setCurrentSprite(19299);
				newPc.setType(19299);
			}	

			newPc.setCurrentWeapon(0);
			newPc.setHeading(pc.getHeading());
			newPc.setX(pc.getX());
			newPc.setY(pc.getY());
			newPc.setMap(pc.getMap());

			newPc.set_food(39);
			newPc.setLawful(30000);

			newPc.setClanid(289371727);
			newPc.setClanname("新手保護");
			newPc.setTitle("");
			newPc.setClanRank(L1Clan.Regular);
			newPc.setElixirStats(0);
			newPc.setElfAttr(0);
			newPc.setGlory_Earth_Attr(0);
			newPc.set_PKcount(0);
			newPc.set_exp_res(0);
			newPc.setPartnerId(0);
			newPc.setAccessLevel((short)0);
			newPc.setGm(false);
			newPc.setMonitor(false);
			newPc.setOnlineStatus(1);
			newPc.setHomeTownId(0);
			newPc.setContribution(0);
			newPc.setHellTime(0);
			newPc.setBanned(false);
			newPc.setKarma(0);
			newPc.setReturnStat(0);

			newPc.refresh();
			newPc.setMoveSpeed(0);
			newPc.setBraveSpeed(0);
			newPc.setGmInvis(false);
			newPc.noPlayerck2 = true;

			L1ItemInstance item = ItemTable.getInstance().createItem(35); // 修練者的單手劍
			L1ItemInstance item1 = ItemTable.getInstance().createItem(175); // 修練者的弓
			L1ItemInstance item2 = ItemTable.getInstance().createItem(120); // 修練者的法杖
			L1ItemInstance item3 = ItemTable.getInstance().createItem(73); // 修練者的雙刀
			L1ItemInstance item4 = ItemTable.getInstance().createItem(203012); // 修練者的斧頭

			if (newPc.isKnight() || newPc.isCrown() || newPc.isDragonknight() || newPc.isFencer() || newPc.isLancer()) { // 騎士、王族、龍騎士、劍士、黃金槍騎
				newPc.getInventory().storeItem(item);
				newPc.getInventory().setEquipped(item, true);
			} else if (newPc.isElf()) { // 妖精
				newPc.getInventory().storeItem(item1);
				newPc.getInventory().setEquipped(item1, true);
			} else if (newPc.isWizard() || newPc.isBlackwizard()) { // 魔法師、幻術師
				newPc.getInventory().storeItem(item2);
				newPc.getInventory().setEquipped(item2, true);
			} else if (newPc.isDarkelf()) { // 黑暗妖精
				newPc.getInventory().storeItem(item3);
				newPc.getInventory().setEquipped(item3, true);
			} else if (newPc.isWarrior()) { // 戰士
				newPc.getInventory().storeItem(item4);
				newPc.getInventory().setEquipped(item4, true);
			}

			L1World.getInstance().storeObject(newPc);
			L1World.getInstance().addVisibleObject(newPc);

			newPc.setNetConnection(null);
			newPc.startObjectAutoUpdate();

		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage((new StringBuilder()).append("請輸入 .muin [角色名稱].").toString())));
		}
	}

}