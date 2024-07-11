 package l1j.server.server.command.executor;

 import java.util.StringTokenizer;
 import l1j.server.server.Account;
 import l1j.server.server.GameClient;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.datatables.CharacterTable;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_Disconnect;
 import l1j.server.server.serverpackets.S_LoginResult;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;


 public class L1AccountBanKick
   implements L1CommandExecutor
 {
   public static L1CommandExecutor getInstance() {
     return new L1AccountBanKick();
   }


   public void execute(L1PcInstance pc, String cmdName, String arg) {
     try {
       StringTokenizer st = new StringTokenizer(arg);
       String name = st.nextToken();
       if (name == null || name.equalsIgnoreCase("")) {
         throw new Exception("");
       }
       int reason = ((Integer)S_LoginResult.banServerCodes.get(Integer.valueOf(Integer.parseInt(st.nextToken())))).intValue();

       L1PcInstance target = L1World.getInstance().getPlayer(name);
       if (target == null) {
         target = CharacterTable.getInstance().restoreCharacter(name);
       }

       if (target != null) {
         final GameClient clnt = target.getNetConnection();
         Account.ban(target.getAccountName(), reason);
         pc.sendPackets((ServerBasePacket)new S_SystemMessage(target.getName() + " 【該帳號已被封禁。】"));
         target.sendPackets((ServerBasePacket)new S_Disconnect());

         if (target.getOnlineStatus() == 1) {
           target.sendPackets((ServerBasePacket)new S_Disconnect());
         }
         GeneralThreadPool.getInstance().schedule(new Runnable()
             {
               public void run() {
                 if (clnt != null && clnt.isConnected()) {
                   try {
                     clnt.close();
                   } catch (Exception e) {
                     e.printStackTrace();
                   }
                 }
               }
             },  1000L);
       } else {
         pc.sendPackets((ServerBasePacket)new S_SystemMessage("世界上不存在具有這個名字的角色。"));
       }
     } catch (Exception e) {
       pc.sendPackets((ServerBasePacket)new S_SystemMessage("帳號被扣押請輸入 .封禁【角色名稱】【扣押原因編號】。"));
       pc.sendPackets((ServerBasePacket)new S_SystemMessage("【封禁原因】：1（已定）。微風和良好的舉止……僅此而已。"));
     }
   }
 }


