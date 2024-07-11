 package l1j.server.server.model.Instance;

 import l1j.server.CPMWReNewClan.ClanDungeon.ClanDugeon;
 import l1j.server.CPMWReNewClan.ClanDungeon.L1ClanDugeon;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_RemoveObject;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;

 public class L1FieldObjectInstance extends L1NpcInstance {
   private static final long serialVersionUID = 1L;

   public L1FieldObjectInstance(L1Npc template) {
     super(template);
   }

   private int moveMapId;

   public void onAction(L1PcInstance pc) {}

   public void onTalkAction(L1PcInstance pc) {
     L1ClanDugeon LCD;
     int randomX, randomY, npcid = getNpcTemplate().get_npcId();
     switch (npcid) {
       case 120620:
         LCD = ClanDugeon.getInstance().getClanDugeon(this.moveMapId);
         randomX = 0;
         randomY = 0;
         if (npcid == 120620) {
           randomX = 32747 + (int)(Math.random() * 2.0D) - (int)(Math.random() * 2.0D);
           randomY = 32805 + (int)(Math.random() * 2.0D) - (int)(Math.random() * 2.0D);
         } else {
           randomX = 33537 + (int)(Math.random() * 20.0D) - (int)(Math.random() * 20.0D);
           randomY = 32702 + (int)(Math.random() * 20.0D) - (int)(Math.random() * 20.0D);
         }
           // 檢查玩家等級是否低於血盟地牢的最低限制
           if (pc.getLevel() < (ClanDugeon.getInstance()).ClanDugeonInfo.minlv) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("由於血盟地下城的等級限制，您無法進入傳送門。"));
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("血盟地下城的最低進入等級為 " + (ClanDugeon.getInstance()).ClanDugeonInfo.minlv + "。"));
               return;
           }

// 檢查是否需要特定物品
           if ((ClanDugeon.getInstance()).ClanDugeonInfo.checkitem) {
               // 如果玩家持有ID為4101007的物品並且NPC ID為120600
               if (pc.getInventory().checkItem(4101007) && npcid == 120600) {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("持有誓言徽章時無法進入。"));
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("每日早上6點重置血盟日常地下城。"));
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("每週三早上6點重置血盟每週地下城。"));
                   return;
               }

               // 如果玩家持有ID為4101007的物品並且NPC ID為120601或120620，且允許每日遊玩
               if (pc.getInventory().checkItem(4101007) && (npcid == 120601 || npcid == 120620) && (ClanDugeon.getInstance()).ClanDugeonInfo.dayplay) {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("持有誓言徽章時無法進入。"));
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("每日早上6點重置血盟日常地下城。"));
                   return;
               }

               // 如果玩家持有ID為4101008的物品並且NPC ID為120601或120620，且不允許每日遊玩
               if (pc.getInventory().checkItem(4101008) && (npcid == 120601 || npcid == 120620) && !(ClanDugeon.getInstance()).ClanDugeonInfo.dayplay) {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("持有誓言徽章時無法進入。"));
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("每週三早上6點重置血盟每週地下城。"));
                   return;
               }
           }
           // 檢查地牢的最大進入人數限制
           if ((ClanDugeon.getInstance()).ClanDugeonInfo.maxuser < LCD.getMembersCount()) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("最大進入人數為 " + (ClanDugeon.getInstance()).ClanDugeonInfo.maxuser + " 人。"));
               return;
           }

// 檢查地牢實例是否已經開始
           if (LCD.isNowCD()) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("血盟地下城實例已經開始。"));
               return;
           }

// 添加玩家到地牢成員列表
           LCD.addMember(pc);

// 開始傳送玩家到地牢位置
           pc.start_teleport(randomX, randomY, this.moveMapId, pc.getHeading(), 169, true);
           break;
     }
   }





   public void setMoveMapId(int id) {
     this.moveMapId = id;
   }


   public void deleteMe() {
     this._destroyed = true;
     if (getInventory() != null) {
       getInventory().clearItems();
     }
     L1World.getInstance().removeVisibleObject((L1Object)this);
     L1World.getInstance().removeObject((L1Object)this);
     for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer((L1Object)this)) {
       pc.removeKnownObject((L1Object)this);
       pc.sendPackets((ServerBasePacket)new S_RemoveObject((L1Object)this));
     }
     removeAllKnownObjects();
   }
 }


