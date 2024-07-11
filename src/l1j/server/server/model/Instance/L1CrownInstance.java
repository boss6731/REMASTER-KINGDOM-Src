 package l1j.server.server.model.Instance;

 import java.sql.Timestamp;
 import l1j.server.MJBotSystem.AI.MJBotAI;
 import l1j.server.MJBotSystem.MJBotType;
 import l1j.server.MJWarSystem.MJCastleWar;
 import l1j.server.server.model.L1CastleLocation;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_RemoveObject;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;

 public class L1CrownInstance
   extends L1NpcInstance
 {
   private static final long serialVersionUID = 1L;

   public L1CrownInstance(L1Npc template) {
     super(template);
   }


     public synchronized void onAction(L1PcInstance player) {
         // 如果對象已被摧毀，則返回
         if (this._destroyed) {
             return;
         }

         try {
             MJBotAI ai = player.getAI(); // 獲取玩家的AI
             L1Clan clan = player.getClan(); // 獲取玩家的血盟

             // 如果玩家沒有血盟或當前沒有進行中的戰爭，則返回
             if (clan == null || clan.getCurrentWar() == null) {
                 return;
             }

             // 如果玩家的AI類型為 REDKNIGHT 或 PROTECTOR，更新防禦並刪除對象
             if (ai != null && (ai.getBotType() == MJBotType.REDKNIGHT || ai.getBotType() == MJBotType.PROTECTOR)) {
                 clan.getCurrentWar().updateDefense(clan);
                 deleteMe();
                 return;
             }

             // 如果玩家沒有血盟或玩家不是君主，則返回
             if (player.getClanid() == 0 || !player.isCrown()) {
                 return;
             }

             // 獲取玩家所在血盟並檢查其領袖ID是否為當前玩家ID
             clan = L1World.getInstance().getClan(player.getClanid());
             if (clan == null || clan.getLeaderId() != player.getId()) {
                 return;
             }

             // 如果血盟已佔領城堡，則發送消息並返回
             if (clan.getCastleId() != 0) {
                 player.sendPackets((ServerBasePacket)new S_ServerMessage(474));
                 return;
             }

             // 如果玩家不在合法範圍內，則返回
             if (!checkRange(player)) {
                 return;
             }

             // 獲取當前血盟戰爭實例並檢查是否發生在玩家所在城堡
             MJCastleWar war = (MJCastleWar)clan.getCurrentWar();
             int castle_id = L1CastleLocation.getCastleId(getX(), getY(), getMapId());
             if (war == null || war.getDefenseClan().getCastleId() != castle_id) {
                 player.sendPackets((ServerBasePacket)new S_SystemMessage("未宣戰。"));
                 return;
             }
         } catch (Exception e) {
             e.printStackTrace(); // 捕捉並打印異常
         }
     }
       clan.setCastleDate(new Timestamp(System.currentTimeMillis()));
       war.updateDefense(clan);
       deleteMe();
     } catch (Exception e) {
       e.printStackTrace();
     }
   }


   public void deleteMe() {
     this._destroyed = true;
     if (getInventory() != null) {
       getInventory().clearItems();
     }
     allTargetClear();
     this._master = null;
     L1World.getInstance().removeVisibleObject((L1Object)this);
     L1World.getInstance().removeObject((L1Object)this);
     for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer((L1Object)this)) {
       pc.removeKnownObject((L1Object)this);
       pc.sendPackets((ServerBasePacket)new S_RemoveObject((L1Object)this), true);
     }
     removeAllKnownObjects();
   }

   private boolean checkRange(L1PcInstance pc) {
     return (getX() - 1 <= pc.getX() && pc.getX() <= getX() + 1 && getY() - 1 <= pc.getY() && pc
       .getY() <= getY() + 1);
   }
 }


