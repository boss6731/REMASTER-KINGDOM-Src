/*    */ package l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint;
/*    */ 
/*    */ 
/*    */ public enum EinhasadFaithResultCode
/*    */ {
/*  6 */   EINHASAD_FAITH_SUCCESS(1),
/*  7 */   EINHASAD_FAITH_FAIL_NEED_REFRESH(2),
/*  8 */   EINHASAD_FAITH_FAIL_WRONG_REQUEST(3),
/*  9 */   EINHASAD_FAITH_FAIL_IS_NOT_GAMESERVER(4);
/*    */   private int value;
/*    */   
/*    */   EinhasadFaithResultCode(int val) {
/* 13 */     this.value = val;
/*    */   }
/*    */   public int toInt() {
/* 16 */     return this.value;
/*    */   }
/*    */   public boolean equals(EinhasadFaithResultCode v) {
/* 19 */     return (this.value == v.value);
/*    */   }
/*    */   public static EinhasadFaithResultCode fromInt(int i) {
/* 22 */     switch (i) {
/*    */       case 1:
/* 24 */         return EINHASAD_FAITH_SUCCESS;
/*    */       case 2:
/* 26 */         return EINHASAD_FAITH_FAIL_NEED_REFRESH;
/*    */       case 3:
/* 28 */         return EINHASAD_FAITH_FAIL_WRONG_REQUEST;
/*    */       case 4:
/* 30 */         return EINHASAD_FAITH_FAIL_IS_NOT_GAMESERVER;
/*    */     } 
/* 32 */     throw new IllegalArgumentException(String.format("invalid arguments EinhasadFaithResultCode, %d", new Object[] { Integer.valueOf(i) }));
/*    */   }
/*    */ }


/* Location:              D:\天堂R\엠제이_군주_용기사_리부트_팩\TheDay.jar!\l1j\server\MJTemplate\MJProto\MainServer_Client_EinhasadPoint\EinhasadFaithResultCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */