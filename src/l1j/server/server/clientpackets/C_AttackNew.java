/*    */ package l1j.server.server.clientpackets;
/*    */ 
/*    */ import l1j.server.MJCharacterActionSystem.AttackContinue.AttackContinueActionHandler;
/*    */ import l1j.server.MJCharacterActionSystem.AttackContinueActionHandlerFactory;
/*    */ import l1j.server.server.GameClient;
/*    */ import l1j.server.server.model.Instance.L1PcInstance;
/*    */ 
/*    */ public class C_AttackNew extends ClientBasePacket {
/*    */   public C_AttackNew(byte[] decrypt, GameClient client) {
/* 10 */     super(decrypt);
/* 11 */     L1PcInstance pc = client.getActiveChar();
/* 12 */     if (pc == null) {
/*    */       return;
/*    */     }
/* 15 */     if (pc.isstop()) {
/*    */       return;
/*    */     }
/* 18 */     AttackContinueActionHandler attackContinueActionHandler = AttackContinueActionHandlerFactory.create();
/* 19 */     if (attackContinueActionHandler == null) {
/*    */       return;
/*    */     }
/* 22 */     attackContinueActionHandler.parse(pc, this);
/* 23 */     attackContinueActionHandler.doWork();
/*    */   }
/*    */ }


/* Location:              D:\天堂R\REMASTER KINGDOM\TheDay.jar!\l1j\server\server\clientpackets\C_AttackNew.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */