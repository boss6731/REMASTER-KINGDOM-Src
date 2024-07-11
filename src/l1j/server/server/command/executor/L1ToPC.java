/*    */ package l1j.server.server.command.executor;
/*    */ 
/*    */ import l1j.server.server.model.Instance.L1PcInstance;
/*    */ import l1j.server.server.model.L1World;
/*    */ import l1j.server.server.serverpackets.S_SystemMessage;
/*    */ import l1j.server.server.serverpackets.ServerBasePacket;
/*    */ 
/*    */ 
/*    */ public class L1ToPC
/*    */   implements L1CommandExecutor
/*    */ {
/*    */   public static L1CommandExecutor getInstance() {
/* 13 */     return new L1ToPC();
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(L1PcInstance pc, String cmdName, String arg) {
/*    */     try {
/* 19 */       L1PcInstance target = L1World.getInstance().getPlayer(arg);
/*    */       
/* 21 */       if (target != null) {
/* 22 */         pc.start_teleport(target.getX(), target.getY(), target.getMapId(), 5, 18339, false, false);
/*    */       } else {
/* 24 */         pc.sendPackets((ServerBasePacket)new S_SystemMessage(arg + " : 沒有這樣的角色。"));
/*    */       } 
/* 26 */     } catch (Exception e) {
/* 27 */       pc.sendPackets((ServerBasePacket)new S_SystemMessage(cmdName + " 請輸入[角色名稱]。"));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\天堂R\REMASTER KINGDOM\TheDay.jar!\l1j\server\server\command\executor\L1ToPC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */