 package l1j.server.server.command.executor;

 import java.util.StringTokenizer;
 import l1j.server.MJWebServer.Dispatcher.PhoneApp.AutoCashResultDatabase;
 import l1j.server.MJWebServer.Dispatcher.PhoneApp.AutoCashUserInfo;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;


 public class L1AutoCashInfoAdd
   implements L1CommandExecutor
 {
   public static L1CommandExecutor getInstance() {
     return new L1AutoCashInfoAdd();
   }


   public void execute(L1PcInstance pc, String cmdName, String arg) {
     try {
       StringTokenizer st = new StringTokenizer(arg);
       String name = st.nextToken();

       AutoCashUserInfo acui = AutoCashResultDatabase.getIntstance().getAutoCashUserInfo(name);
       if (acui != null) {
         pc.sendPackets((ServerBasePacket)new S_SystemMessage("存在相同的存款人姓名。請輸入不同的名稱。"));
         pc.sendPackets((ServerBasePacket)new S_SystemMessage("** 注意：存款時姓名必須一致。 **"));
         return;
       }
       acui = new AutoCashUserInfo();
       acui.setAccountName(pc.getAccountName());
       acui.setCharName(pc.getName());

       AutoCashResultDatabase.getIntstance().addAutoCashUserInfo(name, acui);
       pc.sendPackets((ServerBasePacket)new S_SystemMessage("** 存款人姓名已成功註冊。 **"));
       pc.sendPackets((ServerBasePacket)new S_SystemMessage("** 注意：存款時姓名必須一致。 **"));
     }
     catch (Exception exception) {
       pc.sendPackets((ServerBasePacket)new S_SystemMessage(cmdName + " 請輸入[存款人姓名]。"));
     }
   }
 }


