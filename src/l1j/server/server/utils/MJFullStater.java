     package l1j.server.server.utils;
     import l1j.server.server.GeneralThreadPool;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.serverpackets.S_SystemMessage;
     import l1j.server.server.serverpackets.ServerBasePacket;

     public class MJFullStater implements Runnable {
         public static void running(L1PcInstance pc, String s, int count) {
             int i = pc.remainBonusStats();
             if (i <= 0 || count <= 0 || i < count) {
                 // 屬性點不足時發送訊息
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage(String.format("屬性點不足。 [剩餘屬性點 : %d]", new Object[] { Integer.valueOf(i) })));
                 return;
             }
             MJFullStater stater = new MJFullStater(pc, s, count);
             if (count <= 1) {
                 try {
                     stater.work(0L); // 執行工作
                     // 屬性點增加後發送訊息
                     pc.sendPackets((ServerBasePacket)new S_SystemMessage(String.format("%s已增加。 [剩餘屬性點 : %d]", new Object[] { s, Integer.valueOf(pc.remainBonusStats()) })));
                     pc.sendBonusStats();
                 } catch (Exception e) {
                     // 捕捉到異常時發送訊息
                     pc.sendPackets((ServerBasePacket)new S_SystemMessage(String.format("屬性點不足。 [剩餘屬性點 : %d]", new Object[] { Integer.valueOf(pc.remainBonusStats()) })));
                 }
             } else {
                 // 如果count大於1，使用執行緒池執行
                 GeneralThreadPool.getInstance().execute(stater);
             }
         }

         // 其餘程式碼應包括work方法及其他必要部分
     }

       private L1PcInstance _pc;

       private MJFullStater(L1PcInstance pc, String s, int count) {
         this._pc = pc;
         this._s = s;
         this._count = count;
       }
       private String _s; private int _count;

         public void run() {
             try {
                 // 從_count開始，遞減迴圈
                 for (int i = this._count - 1; i >= 0; i--) {
                     work(100L); // 執行工作
                 }
                 // 成功完成迴圈後發送訊息
                 this._pc.sendPackets((ServerBasePacket)new S_SystemMessage(String.format("%s已增加。 [剩餘屬性點 : %d]",
                         new Object[] { this._s, Integer.valueOf(this._pc.remainBonusStats()) })));
             } catch (Exception e) {
                 // 捕捉到異常時發送訊息
                 if (this._pc != null)
                     this._pc.sendPackets((ServerBasePacket)new S_SystemMessage(String.format("屬性點不足。 [剩餘屬性點 : %d]",
                             new Object[] { Integer.valueOf(this._pc.remainBonusStats()) })));
             } finally {
                 // 最後發送剩餘的bonus stats
                 this._pc.sendBonusStats();
             }
         }

       private void work(long sleeping) throws Exception {
         if (!this._pc.onStat(this._s))
           throw new Exception();
         if (sleeping > 0L)
           Thread.sleep(sleeping);
       }
     }


