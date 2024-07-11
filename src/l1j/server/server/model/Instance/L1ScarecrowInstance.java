 package l1j.server.server.model.Instance;

 import java.util.ArrayList;
 import l1j.server.Config;
 import l1j.server.MJTemplate.MJRnd;
 import l1j.server.server.model.L1Attack;
 import l1j.server.server.serverpackets.S_ChangeHeading;
 import l1j.server.server.serverpackets.S_NewCreateItem;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.utils.CalcExp;

 public class L1ScarecrowInstance
   extends L1NpcInstance {
   private static final long serialVersionUID = 1L;

   public L1ScarecrowInstance(L1Npc template) {
     super(template);
   }


   public void onAction(L1PcInstance player) {
     L1Attack attack = new L1Attack(player, this);




     boolean is_hit = attack.calcHit();
     if (is_hit && player.getAI() == null) {
       if (player.getLevel() < Config.ServerAdSetting.ScareLevel && !player.noPlayerCK) {
         ArrayList<L1PcInstance> targetList = new ArrayList<>();
         targetList.add(player);
         ArrayList<Integer> hateList = new ArrayList<>();
         hateList.add(Integer.valueOf(1));
         CalcExp.calcExp(player, getId(), targetList, hateList, 8L);
       }
       if (player.getLevel() >= 1 && !player.noPlayerCK &&
         player != null) {
         player.getInventory().storeItem(41302, Config.ServerAdSetting.tamsc1);
         player.getInventory().storeItem(40308, Config.ServerAdSetting.tamsc2);

         if (MJRnd.isWinning(1000000, Config.ServerAdSetting.SCAEVENTITEMCHANCE)) {
           player.getInventory().storeItem(Config.ServerAdSetting.SCAEVENTITEM, 1);
         } else if (MJRnd.isWinning(1000000, Config.ServerAdSetting.SCAEVENTITEMCHANCE1)) {
           player.getInventory().storeItem(Config.ServerAdSetting.SCAEVENTITEM1, 1);
         } else if (MJRnd.isWinning(1000000, Config.ServerAdSetting.SCAEVENTITEMCHANCE2)) {
           player.getInventory().storeItem(Config.ServerAdSetting.SCAEVENTITEM2, 1);
         }
         player.sendPackets((ServerBasePacket)new S_NewCreateItem(450, player.getNetConnection()), true);
         (player.getNetConnection().getAccount()).tam_point += Config.ServerAdSetting.tamsc;
         player.getNetConnection().getAccount().updateTam();
       }

         int dmg = attack.calcDamage(); // 計算攻擊傷害
         if (getNpcId() == 7320088) { // 假設 NPC ID 為 7320088 時
             player.sendPackets((ServerBasePacket)new S_SystemMessage("物理傷害: [" + dmg + "]"));
         }
         if (getHeading() < 7) { // 更新 NPC 的朝向
             setHeading(getHeading() + 1);
         } else {
             setHeading(0);
         }
         broadcastPacket((ServerBasePacket)new S_ChangeHeading(this)); // 廣播 NPC 朝向變更
     }
       attack.action(); // 執行攻擊動作
   }
   public void onTalkAction(L1PcInstance l1pcinstance) {}

   public void onFinalAction() {}

   public void doFinalAction() {}
 }


