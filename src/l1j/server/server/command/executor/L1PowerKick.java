         package l1j.server.server.command.executor;

         import java.sql.Connection;
         import java.sql.PreparedStatement;
         import java.sql.ResultSet;
         import java.util.Collection;
         import java.util.StringTokenizer;
         import java.util.logging.Logger;
         import l1j.server.Config;
         import l1j.server.L1DatabaseFactory;
         import l1j.server.MJNetSafeSystem.DriveSafe.MJHddIdChecker;
         import l1j.server.MJNetServer.Codec.MJNSHandler;
         import l1j.server.MJRankSystem.Loader.MJRankUserLoader;
         import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
         import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
         import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CUSTOM_MSGBOX;
         import l1j.server.server.Account;
         import l1j.server.server.GameClient;
         import l1j.server.server.GeneralThreadPool;
         import l1j.server.server.datatables.IpTable;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.model.L1World;
         import l1j.server.server.serverpackets.S_LoginResult;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;
         import l1j.server.server.utils.SQLUtil;

         public class L1PowerKick
           implements L1CommandExecutor
         {
           private static Logger _log = Logger.getLogger(L1PowerKick.class.getName());




           public static L1CommandExecutor getInstance() {
             return new L1PowerKick();
           }


           public void execute(L1PcInstance pc, String cmdName, String arg) {
             try {
               StringTokenizer st = new StringTokenizer(arg);
               String sname = st.nextToken();
               if (sname == null || sname.equalsIgnoreCase("")) {
                 throw new Exception("");
               }
               Integer reason = (Integer)S_LoginResult.banServerCodes.get(Integer.valueOf(Integer.parseInt(st.nextToken())));
               if (reason == null) {
                 throw new Exception("");
               }
               L1PcInstance target = L1World.getInstance().getPlayer(sname);
               IpTable iptable = IpTable.getInstance();
               if (target != null) {
                 if (target.isGm()) {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("管理員不能進行風險處理。"));
                   return;
                 }
                 Account.ban(target.getAccountName(), reason.intValue());

                 if (!iptable.isBannedIp(target.getNetConnection().getIp())) {
                   iptable.banIp(target.getNetConnection().getIp());
                 } else {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage(" 已註冊IP：" + target.getNetConnection().getIp()));
                 }  pc.sendPackets((ServerBasePacket)new S_SystemMessage(target.getName() + " 已被永久驅逐。 "));

                 MJHddIdChecker.update_denials(target, 1);
                 duplicateKick(target.getNetConnection().getIp(), target, reason.intValue());
               } else {
                 String name = loadCharacter(sname);
                 if (name != null) {
                   MJHddIdChecker.update_denials(name, 1);
                   Account.ban(name, reason.intValue());
                   String nc = Account.checkIP(name);
                   if (nc != null) {
                     duplicateKick(nc, null, reason.intValue());
                     if (!iptable.isBannedIp(nc)) {
                       iptable.banIp(nc);
                     } else {
                       pc.sendPackets((ServerBasePacket)new S_SystemMessage(name + " 已註冊IP：" + nc));
                     }
                   }
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage(name + " 該帳號已被封禁。"));
                 }
               }
             } catch (Exception e) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage(cmdName + " 請輸入[角色名稱]和[封禁原因編號]。"));
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("原因1：使用非法程序 / 原因2：公共安全與秩序 / 原因3：商業廣告"));
             }
           }

             public static void duplicateKick(String addr, final L1PcInstance target, int reason) {
                 try {
                     // 獲取所有的遊戲客戶端
                     Collection<GameClient> cList = MJNSHandler.getClients();

                     if (target != null) {
                         // 封禁目標用戶
                         MJRankUserLoader.getInstance().banUser(target);

                         // 準備自定義消息框
                         SC_CUSTOM_MSGBOX box = SC_CUSTOM_MSGBOX.newInstance();
                         box.set_button_type(SC_CUSTOM_MSGBOX.ButtonType.MB_OK);
                         box.set_icon_type(SC_CUSTOM_MSGBOX.IconType.MB_ICONHAND);
                         box.set_message("由於客戶您的帳號存在妨害公共秩序、道德風俗或詐欺行為等原因，已限制您使用本遊戲。詳細情況請諮詢伺服器官網客服中心。");
                         box.set_title(Config.Message.GameServerName);
                         box.set_message_id(target.getId());

                         // 向目標用戶發送消息框
                         target.sendPackets((MJIProtoMessage) box, MJEProtoMessages.SC_CUSTOM_MSGBOX, true);

                         // 延遲3秒後踢出用戶並登出
                         GeneralThreadPool.getInstance().schedule(new Runnable() {
                             public void run() {
                                 target.getNetConnection().kick();
                                 target.logout();
                             }
                         }, 3000L);
                     }

                     if (cList != null) {
                         // 遍歷所有客戶端
                         for (GameClient clnt : cList) {
                             if (clnt == null) continue;
                             if (clnt.getIp().equalsIgnoreCase(addr)) {
                                 L1PcInstance tt = clnt.getActiveChar();
                                 if (tt == null) continue;

                                 // 封禁該IP地址的所有用戶
                                 MJRankUserLoader.getInstance().banUser(tt);

                                 // 封禁帳號
                                 if (clnt.getAccountName() != null && !clnt.getAccountName().equalsIgnoreCase("")) {
                                     Account.ban(clnt.getAccountName(), reason);
                                     MJHddIdChecker.update_denials(clnt.getAccountName(), 2);
                                 }

                                 // 準備自定義消息框
                                 SC_CUSTOM_MSGBOX box = SC_CUSTOM_MSGBOX.newInstance();
                                 box.set_button_type(SC_CUSTOM_MSGBOX.ButtonType.MB_OK);
                                 box.set_icon_type(SC_CUSTOM_MSGBOX.IconType.MB_ICONHAND);

                                 box.set_message("由於客戶您的帳號存在妨害公共秩序、道德風俗或詐欺行為等原因，已限制您使用本遊戲。詳細情況請諮詢伺服器官網客服中心。");
                                 box.set_title(Config.Message.GameServerName);
                                 box.set_message_id(tt.getId());

                                 // 向客戶端發送消息框
                                 clnt.sendPacket((MJIProtoMessage) box, MJEProtoMessages.SC_CUSTOM_MSGBOX.toInt(), true);

                                 // 延遲3秒後踢出客戶端
                                 final GameClient c = clnt;
                                 GeneralThreadPool.getInstance().schedule(new Runnable() {
                                     public void run() {
                                         c.kick();
                                     }
                                 }, 3000L);
                             }
                         }
                     }
                 } catch (Exception e) {
                     // 異常處理（這段原始碼中沒有給出具體的異常處理邏輯）
                 }
             }

                 } catch (Exception e) {
                     // 異常處理（這段原始碼中沒有給出具體的異常處理邏輯）
                 }
             }
             } catch (Exception e) {
               e.printStackTrace();
             }
           }

           private String loadCharacter(String charName) {
             Connection con = null;
             PreparedStatement pstm = null;
             ResultSet rs = null;
             String name = null;
             try {
               con = L1DatabaseFactory.getInstance().getConnection();
               pstm = con.prepareStatement("SELECT * FROM characters WHERE char_name=?");
               pstm.setString(1, charName);

               rs = pstm.executeQuery();

               if (rs.next()) {
                 name = rs.getString("account_name");
               }
             }
             catch (Exception e) {
               e.printStackTrace();
             } finally {
               SQLUtil.close(rs);
               SQLUtil.close(pstm);
               SQLUtil.close(con);
             }
             return name;
           }
         }


