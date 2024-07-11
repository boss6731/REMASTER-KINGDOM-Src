         package l1j.server.server.command.executor;

         import java.sql.Connection;
         import java.sql.PreparedStatement;
         import java.sql.ResultSet;
         import java.util.Collection;
         import l1j.server.L1DatabaseFactory;
         import l1j.server.server.GameClient;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.model.L1World;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;
         import l1j.server.server.utils.SQLUtil;


         public class L1QueryCharacter
           implements L1CommandExecutor
         {
           public static L1CommandExecutor getInstance() {
             return new L1QueryCharacter();
           }

           private static String getCClass(String ip) {
             return ip.substring(0, ip.lastIndexOf('.'));
           }


           public void execute(L1PcInstance pc, String cmdName, String arg) {
             try {
               L1PcInstance target = L1World.getInstance().getPlayer(arg);

               if (target != null) {
                 long totalAdena = 0L;
                 GameClient client = target.getNetConnection();

                 if (client == null) {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("您無法搜尋不在線上的角色。"));

                   return;
                 }
                 String cClass = getCClass(client.getIp());

                 Collection<L1PcInstance> pcs = L1World.getInstance().getAllPlayers();

                 for (L1PcInstance otherPc : pcs) {

                   if (otherPc.getNetConnection() != null) {
                     String otherPcIp = otherPc.getNetConnection().getIp();

                     if (cClass.equals(getCClass(otherPcIp))) {
                       totalAdena += printInfo(pc, otherPc, otherPcIp);
                     }
                   }
                 }

                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("所有帳號中 金幣 的總金額為【" + totalAdena + "】 是。"));
               } else {

                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("世界上不存在具有這個名字的角色。"));
               }

             } catch (Exception e) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage(cmdName + " 請輸入[角色名稱]。"));
             }
           }

           private long printInfo(L1PcInstance master, L1PcInstance pc, String ip) {
             Connection conn = null;
             PreparedStatement pstm = null;
             ResultSet rs = null;
             long storageAdena = 0L;
             long characterAdena = 0L;

             try {
               conn = L1DatabaseFactory.getInstance().getConnection();
               pstm = conn.prepareStatement("select ifnull(sum(count), 0) as 'adena' from character_warehouse where item_id = 40308 and account_name = ?");

               pstm.setString(1, pc.getAccountName());
               rs = pstm.executeQuery();
               if (rs.next()) {
                 storageAdena = rs.getInt("adena");
               }
             } catch (Exception e) {
               e.printStackTrace();
             } finally {
               SQLUtil.close(rs);
               SQLUtil.close(pstm);
               SQLUtil.close(conn);
             }


             try {
               conn = L1DatabaseFactory.getInstance().getConnection();
               pstm = conn.prepareStatement("select ifnull(sum(count), 0) as 'adena' from character_items where item_id = 40308 and char_id IN (select objid FROM characters WHERE account_name = ?)");

               pstm.setString(1, pc.getAccountName());
               rs = pstm.executeQuery();
               if (rs.next()) {
                 characterAdena = rs.getInt("adena");
               }
             } catch (Exception e) {
               e.printStackTrace();
             } finally {
               SQLUtil.close(rs);
               SQLUtil.close(pstm);
               SQLUtil.close(conn);
             }

             master.sendPackets((ServerBasePacket)new S_SystemMessage(ip + "在 【" + pc.getName() + "】目前已上線，帳號倉庫金幣為【" + storageAdena + "】角色中的金幣總量為【" + characterAdena + "】 是。"));


             return storageAdena + characterAdena;
           }
         }


