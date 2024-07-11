 package l1j.server.server.model.Instance;

 import MJNCoinSystem.MJNCoinAdenaManager;
 import l1j.server.server.Controller.BugRaceController;
 import l1j.server.server.serverpackets.S_Board;
 import l1j.server.server.serverpackets.S_BoardRead;
 import l1j.server.server.serverpackets.S_EnchantRanking;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;



 public class L1BoardInstance
   extends L1NpcInstance
 {
   private static final long serialVersionUID = 1L;

   public L1BoardInstance(L1Npc template) {
     super(template);
   }


     public void onAction(L1PcInstance player) {
         if (getNpcTemplate().get_npcId() == 999999) { // 如果 NPC 的 ID 是 999999
             if (BugRaceController.getInstance().getBugState() == 0) { // 如果比賽狀態為 0
                 player.sendPackets((ServerBasePacket)new S_Board(this)); // 向玩家發送板子消息
             } else if (BugRaceController.getInstance().getBugState() == 1) { // 如果比賽狀態為 1
                 player.sendPackets((ServerBasePacket)new S_SystemMessage("比賽進行中，無法查看。")); // 向玩家發送系統消息
             } else if (BugRaceController.getInstance().getBugState() == 2) { // 如果比賽狀態為 2
                 player.sendPackets((ServerBasePacket)new S_SystemMessage("正在準備下一場比賽。")); // 向玩家發送系統消息
             }
         }
     }


     }
     else if (getNpcTemplate().get_npcId() == 45000178) {
       MJNCoinAdenaManager.DEFAULT.on_ncoin_adena_show_list(player, this);
     } else {

       player.sendPackets((ServerBasePacket)new S_Board(this));
     }
   }




   public void onAction(L1PcInstance player, int number) {
     if (getNpcTemplate().get_npcId() == 45000178) {
       MJNCoinAdenaManager.DEFAULT.on_ncoin_adena_show_list(player, this, number);
     } else {

       player.sendPackets((ServerBasePacket)new S_Board(this, number));
     }
   }


   public void onActionRead(L1PcInstance player, int number) {

    if (getNpcTemplate().get_npcId() == 4200013) { // 如果 NPC 的 ID 是 4200013

        player.sendPackets((ServerBasePacket)new S_EnchantRanking(player, number)); // 向玩家發送附魔排名的消息

    }

    else if (getNpcTemplate().get_npcId() == 45000178) { // 如果 NPC 的 ID 是 45000178

        MJNCoinAdenaManager.DEFAULT.on_ncoin_adena_show_content(player, number); // 顯示 N幣和亞丁幣的內容

    } else {



        if (getNpcTemplate().get_npcId() == 500002) { // 如果 NPC 的 ID 是 500002
            if (!player.isGm()) { // 如果玩家不是 GM
                player.sendPackets((ServerBasePacket)new S_SystemMessage("只有管理員可以查看。")); // 向玩家發送系統消息
         player.sendPackets((ServerBasePacket)new S_Board(this)); // 向玩家發送板子消息
         return; // 結束方法
            }

        } else if (getNpcTemplate().get_npcId() == 9200036 &&
         !player.isGm()) { // 如果 NPC 的 ID 是 9200036 且玩家不是 GM
            player.sendPackets((ServerBasePacket)new S_SystemMessage("只有管理員可以查看。")); // 向玩家發送系統消息
         player.sendPackets((ServerBasePacket)new S_Board(this)); // 向玩家發送板子消息
         return; // 結束方法

        }

        player.sendPackets((ServerBasePacket)new S_BoardRead(this, number)); // 向玩家發送板子閱讀消息

    }

}


