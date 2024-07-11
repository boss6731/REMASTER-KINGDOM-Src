         package l1j.server.server.command.executor;
         import java.sql.PreparedStatement;
         import java.sql.ResultSet;
         import java.util.StringTokenizer;
         import java.util.logging.Logger;
         import l1j.server.MJTemplate.MJObjectWrapper;
         import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
         import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
         import l1j.server.MJTemplate.MJString;
         import l1j.server.server.Account;
         import l1j.server.server.datatables.IpTable;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.model.L1Object;
         import l1j.server.server.model.L1World;
         import l1j.server.server.serverpackets.S_LoginResult;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;

         public class L1RangeKick implements L1CommandExecutor {
           private static Logger _log = Logger.getLogger(L1RangeKick.class.getName());



           public static L1CommandExecutor getInstance() {
             return new L1RangeKick();
           }


           public void execute(L1PcInstance pc, String cmdName, String arg) {
             try {
               StringTokenizer st = new StringTokenizer(arg);
               final String sname = st.nextToken();
               if (sname == null || sname.equalsIgnoreCase("")) {
                 throw new Exception("");
               }
               Integer reason = (Integer)S_LoginResult.banServerCodes.get(Integer.valueOf(Integer.parseInt(st.nextToken())));
               if (reason == null) {
                 throw new Exception("");
               }
               L1PcInstance target = L1World.getInstance().getPlayer(sname);




               if (target != null) {
                 IpTable ip = IpTable.getInstance();

                 Account.ban(target.getAccountName(), reason.intValue());
                 ip.rangeBanIp(target.getNetConnection().getHostname());
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage(target.getName() + "[" + pc.getNetConnection() + "] 被驅逐出大範圍。"));
                 L1World.getInstance().removeObject((L1Object)target);
                 L1PowerKick.duplicateKick(target.getNetConnection().getIp(), target, reason.intValue());
               } else {
                 final MJObjectWrapper<String> wrapper = new MJObjectWrapper();
                 wrapper.value = "";
                 Selector.exec("select account_name from characters where char_name=?", new SelectorHandler()
                     {
                       public void handle(PreparedStatement pstm) throws Exception {
                         pstm.setString(1, sname);
                       }


                       public void result(ResultSet rs) throws Exception {
                         while (rs.next()) {
                           wrapper.value = rs.getString("account_name");
                         }
                       }
                     });

                 if (MJString.isNullOrEmpty((String)wrapper.value)) {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage(String.format("%s 是不存在的角色名稱。", new Object[] { sname })));
                 } else {
                   IpTable ip = IpTable.getInstance();
                   Account.ban((String)wrapper.value, reason.intValue());
                   Integer[] octet = Account.loadAccountAddress((String)wrapper.value);
                   StringBuilder sb = new StringBuilder(256);
                   sb.append(sname).append("已被大規模驅逐。").append((String)wrapper.value);
                   if (octet != null) {
                     sb.append(", ").append(octet[0]).append(".").append(octet[1]).append(".").append(octet[2]).append(".").append("*");
                     ip.rangeBanIp(octet);
                   }
                   sb.append(")");
                   pc.sendPackets(sb.toString());
                 }
               }
             } catch (Exception e) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage(cmdName + " 請輸入[角色名稱]和[暫停原因編號]。"));
             }
           }
         }


