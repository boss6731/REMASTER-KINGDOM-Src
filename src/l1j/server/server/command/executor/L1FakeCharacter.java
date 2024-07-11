package l1j.server.server.command.executor;

import java.util.Random;
import java.util.StringTokenizer;

import l1j.server.server.IdFactory;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1FakeCharacter implements L1CommandExecutor {

	private static Random _random =  new Random(System.nanoTime());


	//	private static final int[] MALE_LIST = new int[] { 61, 138, 734, 2786, 6658, 6671, 12490 };
	//	private static final int[] FEMALE_LIST = new int[] { 48, 37, 1186, 2796, 6661, 6650, 12494 };

	private L1FakeCharacter() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1FakeCharacter();
	}

    @override
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            // 使用 StringTokenizer 分解傳入的參數
            StringTokenizer stringtokenizer = new StringTokenizer(arg);
            String name = stringtokenizer.nextToken(); // 獲取角色名稱

            // 檢查角色名稱是否已存在
            if (CharacterTable.getInstance().isContainNameList(name) || L1World.getInstance().getPlayer(name) != null) {
                pc.sendPackets(new S_SystemMessage("該角色名稱已存在"));
                return;
            }

            // 剩下的程式碼會在這裡繼續
            // ...
        } catch (Exception e) {
            // 捕捉異常並發送錯誤消息給使用者
            pc.sendPackets(new S_SystemMessage("錯誤: 無法創建角色"));
        }
    }
        // 創建新的角色實例
        L1PcInstance newPc = new L1PcInstance();
        newPc.setAccountName(""); // 設置帳號名稱
        newPc.setId(IdFactory.getInstance().nextId()); // 設置角色ID
        newPc.setName(name); // 設置角色名稱
        newPc.setHighLevel(1); // 設置最高等級為1
        newPc.set_exp(0); // 設置經驗值為0
        newPc.addBaseMaxHp((short)2000); // 設置基礎最大HP為2000
        newPc.setCurrentHp(2000); // 設置當前HP為2000
        newPc.setDead(false); // 設置角色為未死亡狀態
        newPc.setStatus(0); // 設置角色狀態為0
        newPc.addBaseMaxMp((short)2); // 設置基礎最大MP為2
        newPc.setCurrentMp(2); // 設置當前MP為2

        // 設置角色的基礎屬性和當前屬性
        newPc.getAbility().setBaseStr(16); // 設置基礎力量為16
        newPc.getAbility().setStr(16); // 設置當前力量為16
        newPc.getAbility().setBaseCon(16); // 設置基礎體質為16
        newPc.getAbility().setCon(16); // 設置當前體質為16
        newPc.getAbility().setBaseDex(11); // 設置基礎敏捷為11
        newPc.getAbility().setDex(11); // 設置當前敏捷為11
        newPc.getAbility().setBaseCha(13); // 設置基礎魅力為13
        newPc.getAbility().setCha(13); // 設置當前魅力為13
        newPc.getAbility().setBaseInt(12); // 設置基礎智力為12
        newPc.getAbility().setInt(12); // 設置當前智力為12
        newPc.getAbility().setBaseWis(11); // 設置基礎智慧為11
        newPc.getAbility().setWis(11); // 設置當前智慧為11
         // 生成一個隨機數範圍在0到119之間
         int ran = _random.nextInt(120);

         // 根據隨機數設置角色的職業、外觀和類型
         // 等級 更新
         // 生成一個隨機數範圍在0到159之間
         int ran = _random.nextInt(160);

    // 根據隨機數設置角色的職業、外觀和類型
    // 等級 更新
    if (ran >= 0 && ran < 15) { // 15% 機率為男性騎士
            newPc.setClassId(20553);
            newPc.setCurrentSprite(20553);
            newPc.setType(20553);
        } else if (ran >= 15 && ran < 20) { // 5% 機率為女性角色，因為大部分玩家不扮演女性角色
            newPc.setClassId(48);
            newPc.setCurrentSprite(48);
            newPc.setType(48);
        } else if (ran >= 20 && ran < 30) { // 10% 機率
            newPc.setClassId(138);
            newPc.setCurrentSprite(138);
            newPc.setType(138);
        } else if (ran >= 30 && ran < 35) { // 5% 機率
            newPc.setClassId(37);
            newPc.setCurrentSprite(37);
            newPc.setType(37);
        } else if (ran >= 35 && ran < 45) { // 10% 機率
            newPc.setClassId(20278);
            newPc.setCurrentSprite(20278);
            newPc.setType(20278);
        } else if (ran >= 45 && ran < 50) { // 5% 機率
            newPc.setClassId(20279);
            newPc.setCurrentSprite(20279);
            newPc.setType(20279);
        } else if (ran >= 50 && ran < 60) { // 10% 機率
            newPc.setClassId(2786);
            newPc.setCurrentSprite(2786);
            newPc.setType(2786);
        } else if (ran >= 60 && ran < 65) { // 5% 機率
            newPc.setClassId(2796);
            newPc.setCurrentSprite(2796);
            newPc.setType(2796);
        } else if (ran >= 65 && ran < 75) { // 10% 機率
            newPc.setClassId(6658);
            newPc.setCurrentSprite(6658);
            newPc.setType(6658);
        } else if (ran >= 75 && ran < 80) { // 5% 機率
            newPc.setClassId(6661);
            newPc.setCurrentSprite(6661);
            newPc.setType(6661);
        } else if (ran >= 80 && ran < 90) { // 10% 機率
            newPc.setClassId(6671);
            newPc.setCurrentSprite(6671);
            newPc.setType(6671);
        } else if (ran >= 90 && ran < 100) { // 10% 機率
            newPc.setClassId(6650);
            newPc.setCurrentSprite(6650);
            newPc.setType(6650);
        } else if (ran >= 100 && ran < 110) { // 10% 機率
            newPc.setClassId(20567);
            newPc.setCurrentSprite(20567);
            newPc.setType(20567);
        } else if (ran >= 110 && ran < 120) { // 10% 機率
            newPc.setClassId(20577);
            newPc.setCurrentSprite(20577);
            newPc.setType(20577);

        } else if (ran >= 120 && ran < 130) { // 10% 機率
            newPc.setClassId(18520);
            newPc.setCurrentSprite(18520);
            newPc.setType(18520);
        } else if (ran >= 130 && ran < 140) { // 10% 機率
            newPc.setClassId(18499);
            newPc.setCurrentSprite(18499);
            newPc.setType(18499);
        } else if (ran >= 140 && ran < 150) { // 10% 機率
            newPc.setClassId(19296);
            newPc.setCurrentSprite(19296);
            newPc.setType(19296);
        } else if (ran >= 150 && ran < 160) { // 10% 機率
            newPc.setClassId(19299);
            newPc.setCurrentSprite(19299);
            newPc.setType(19299);
        }

            // 設置新角色的初始武器狀態和位置
            newPc.setCurrentWeapon(0); // 設置當前武器為空手
            newPc.setHeading(pc.getHeading()); // 設置面向方向
            newPc.setX(pc.getX()); // 設置 X 座標
            newPc.setY(pc.getY()); // 設置 Y 座標
            newPc.setMap(pc.getMap()); // 設置地圖

            // 設置新角色的初始屬性
            newPc.set_food(39); // 設置食物值
            newPc.setLawful(30000); // 設置正義值

            newPc.setClanid(289371727); // 設置血盟ID
            newPc.setClanname("新手保護"); // 設置血盟名稱為「신규보호」
            newPc.setTitle(""); // 設置稱號為空
            newPc.setClanRank(L1Clan.일반); // 設置血盟等級為一般
            newPc.setElixirStats(0); // 設置靈藥數量
            newPc.setElfAttr(0); // 設置精靈屬性
            newPc.setGlory_Earth_Attr(0); // 設置地屬性
            newPc.set_PKcount(0); // 設置PK次數
            newPc.set_exp_res(0); // 設置死亡經驗回復次數
            newPc.setPartnerId(0); // 設置伴侶ID
            newPc.setAccessLevel((short)0); // 設置訪問等級為0
            newPc.setGm(false); // 設置為非GM
            newPc.setMonitor(false); // 設置為非監視角色
            newPc.setOnlineStatus(1); // 設置在線狀態
            newPc.setHomeTownId(0); // 設置家鄉ID
            newPc.setContribution(0); // 設置貢獻度
            newPc.setHellTime(0); // 設置地獄時間
            newPc.setBanned(false); // 設置為未被封禁
            newPc.setKarma(0); // 設置業力值
            newPc.setReturnStat(0); // 設置返回狀態

            // 刷新角色狀態並設置移動和勇敢速度
            newPc.refresh(); // 刷新角色狀態
            newPc.setMoveSpeed(0); // 設置移動速度
            newPc.setBraveSpeed(0); // 設置勇敢速度
            newPc.setGmInvis(false); // 設置為非GM隱形
            newPc.noPlayerck2 = true; // 設置 noPlayerck2 標記為真

            try {
                L1ItemInstance item = ItemTable.getInstance().createItem(35); // 修練者的單手劍
                L1ItemInstance item1 = ItemTable.getInstance().createItem(175); // 修練者的弓
                L1ItemInstance item2 = ItemTable.getInstance().createItem(120); // 修練者的杖
                L1ItemInstance item3 = ItemTable.getInstance().createItem(73); // 修練者的雙刀
                L1ItemInstance item4 = ItemTable.getInstance().createItem(203012); // 修練者的斧頭

                if (newPc.isKnight() || newPc.isCrown() || newPc.isDragonknight() || newPc.isFencer() || newPc.isLancer()) { // 騎士、君主、龍騎士、劍士、槍兵
                    newPc.getInventory().storeItem(item);
                    newPc.getInventory().setEquipped(item, true);
                } else if (newPc.isElf()) { // 精靈
                    newPc.getInventory().storeItem(item1);
                    newPc.getInventory().setEquipped(item1, true);
                } else if (newPc.isWizard() || newPc.isBlackwizard()) { // 魔法師、黑魔法師
                    newPc.getInventory().storeItem(item2);
                    newPc.getInventory().setEquipped(item2, true);
                } else if (newPc.isDarkelf()) { // 黑暗精靈
                    newPc.getInventory().storeItem(item3);
                    newPc.getInventory().setEquipped(item3, true);
                } else if (newPc.isWarrior()) { // 戰士
                    newPc.getInventory().storeItem(item4);
                    newPc.getInventory().setEquipped(item4, true);
                }

                // 將新角色添加到遊戲世界中
                L1World.getInstance().storeObject(newPc);
                L1World.getInstance().addVisibleObject(newPc);

                // 設置網絡連接為空並開始自動更新
                newPc.setNetConnection(null);
                newPc.startObjectAutoUpdate();

            } catch (Exception e) {
                // 捕捉異常並提示用戶正確的指令格式
                pc.sendPackets(new S_SystemMessage(new StringBuilder().append(".無人 [角色名] 輸入.").toString()));
            }
        }
