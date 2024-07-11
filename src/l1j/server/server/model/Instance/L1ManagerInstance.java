 package l1j.server.server.model.Instance;

 import MJFX.UIAdapter.MJUIAdapter;
 import l1j.server.Config;
 import l1j.server.server.IdFactory;

 public class L1ManagerInstance extends L1PcInstance {
   private static L1ManagerInstance _instance;

   public static L1ManagerInstance getInstance() {
     if (_instance == null)
       _instance = new L1ManagerInstance();
     return _instance;
   }

   private L1ManagerInstance() {
     setId(IdFactory.getInstance().nextId());
   }

   public boolean isGm() {
     return true;
   }


   public short getAccessLevel() {
     return (short)Config.ServerAdSetting.GMCODE;
   }


   public String getName() {
     return "管理員";
   }


   public void sendPackets(String s) {
     MJUIAdapter.on_gm_command_append(s);
   }
 }


