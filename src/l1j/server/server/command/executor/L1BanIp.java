 package l1j.server.server.command.executor;

 import java.util.StringTokenizer;
 import l1j.server.server.datatables.IpTable;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;


 public class L1BanIp
   implements L1CommandExecutor
 {
   public static L1CommandExecutor getInstance() {
     return new L1BanIp();
   }



   public void execute(L1PcInstance pc, String cmdName, String arg) {
     try {
       StringTokenizer stringtokenizer = new StringTokenizer(arg);
       String s1 = stringtokenizer.nextToken();
       String s2 = null;

       try {
         s2 = stringtokenizer.nextToken();
       } catch (Exception exception) {}

       IpTable iptable = IpTable.getInstance();
       boolean isBanned = iptable.isBannedIp(s1);

       for (L1PcInstance tg : L1World.getInstance().getAllPlayers()) {
         if (tg.getNetConnection() != null && s1.equals(tg.getNetConnection().getIp())) {
           String msg = "IP : " + s1 + " 正在連接的玩家：" + tg.getName();
           pc.sendPackets((ServerBasePacket)new S_SystemMessage(msg));
         }
       }

       if ("添加".equals(s2) && !isBanned) {
         iptable.banIp(s1);
         String msg = "IP : " + s1 + " 在 BAN IP 中添加或者刪除。";
         pc.sendPackets((ServerBasePacket)new S_SystemMessage(msg));
       } else if ("刪除".equals(s2) && isBanned) {
         if (iptable.liftBanIp(s1)) {
           String msg = "IP : " + s1 + " 已從 BAN IP 中刪除。";
           pc.sendPackets((ServerBasePacket)new S_SystemMessage(msg));
         }

       } else if (isBanned) {
         String msg = "IP : " + s1 + " 已在 BAN IP 中添加。 ";
         pc.sendPackets((ServerBasePacket)new S_SystemMessage(msg));
       } else {
         String msg = "IP : " + s1 + " 未在 BAN IP 中添加。";
         pc.sendPackets((ServerBasePacket)new S_SystemMessage(msg));
       }

     } catch (Exception e) {
       pc.sendPackets((ServerBasePacket)new S_SystemMessage(cmdName + " 請輸入.封鎖IP【新增、刪除】。"));
     }
   }
 }


