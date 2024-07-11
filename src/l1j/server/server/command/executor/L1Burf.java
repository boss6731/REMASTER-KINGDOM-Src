 package l1j.server.server.command.executor;

 import java.util.StringTokenizer;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_SkillSound;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;


 public class L1Burf
   implements L1CommandExecutor
 {
   public static L1CommandExecutor getInstance() {
     return new L1Burf();
   }

   static class Burfskill implements Runnable {
     private L1PcInstance _pc = null;
     private int _sprid;
     private int _count;

     public Burfskill(L1PcInstance pc, int sprid, int count) {
       this._pc = pc;
       this._sprid = sprid;
       this._count = count;
     }


     public void run() {
       for (int i = 0; i < this._count; i++) {
         try {
           Thread.sleep(500L);
           int num = this._sprid + i;
           if (this._pc.getOnlineStatus() == 0)
             break;
           this._pc.sendPackets((ServerBasePacket)new S_SystemMessage("技能編號： " + num + ""));
           this._pc.sendPackets((ServerBasePacket)new S_SkillSound(this._pc.getId(), this._sprid + i));
           this._pc.broadcastPacket((ServerBasePacket)new S_SkillSound(this._pc.getId(), this._sprid + i));
         } catch (Exception exception) {
           break;
         }
       }
     }
   }




   public void execute(L1PcInstance pc, String cmdName, String arg) {
     try {
       StringTokenizer st = new StringTokenizer(arg);
       int sprid = Integer.parseInt(st.nextToken(), 10);
       int count = Integer.parseInt(st.nextToken(), 10);

       Burfskill spr = new Burfskill(pc, sprid, count);
       GeneralThreadPool.getInstance().execute(spr);
     }
     catch (Exception e) {
       pc.sendPackets((ServerBasePacket)new S_SystemMessage(cmdName + " 請輸入[castgfx]。 "));
     }
   }
 }


