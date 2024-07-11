/*    */ package l1j.server.server.command.executor;

 import java.util.StringTokenizer;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;



 public class L1Adena
   implements L1CommandExecutor
 {
   public static L1CommandExecutor getInstance() {
     return new L1Adena();
   }



   public void execute(L1PcInstance pc, String cmdName, String arg) {
     try {
       StringTokenizer stringtokenizer = new StringTokenizer(arg);

       int count = Integer.parseInt(stringtokenizer.nextToken());
       L1ItemInstance adena = pc.getInventory().storeItem(40308, count);

       if (adena != null) {
         pc.sendPackets((ServerBasePacket)new S_SystemMessage(count + "【金幣】以被創建。"));
       }
     } catch (Exception e) {
       pc.sendPackets((ServerBasePacket)new S_SystemMessage(".金幣 請輸入【金額】。"));
     }
   }
 }


