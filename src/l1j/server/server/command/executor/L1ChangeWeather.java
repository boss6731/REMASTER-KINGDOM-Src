 package l1j.server.server.command.executor;

 import java.util.StringTokenizer;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.S_Weather;
 import l1j.server.server.serverpackets.ServerBasePacket;


 public class L1ChangeWeather
   implements L1CommandExecutor
 {
   public static L1CommandExecutor getInstance() {
     return new L1ChangeWeather();
   }


   public void execute(L1PcInstance pc, String cmdName, String arg) {
     try {
       StringTokenizer tok = new StringTokenizer(arg);

       int weather = Integer.parseInt(tok.nextToken());
       L1World.getInstance().setWeather(weather);
       L1World.getInstance().broadcastPacketToAll((ServerBasePacket)new S_Weather(weather));
     } catch (Exception e) {
       pc.sendPackets((ServerBasePacket)new S_SystemMessage(cmdName + " 請輸入0~3（雪）和16~19（雨）。"));
     }
   }
 }


