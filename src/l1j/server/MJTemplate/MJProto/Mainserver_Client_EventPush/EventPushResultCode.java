/*    */ package l1j.server.MJTemplate.MJProto.Mainserver_Client_EventPush;
/*    */ 
/*    */ 
/*    */ public enum EventPushResultCode
/*    */ {
/*  6 */   EVENT_PUSH_SUCCESS(1),
/*  7 */   EVENT_PUSH_SYSTEM_LOAD_LIST_FAIL(2),
/*  8 */   EVENT_PUSH_LIST_IS_NOT_LOAD(3),
/*  9 */   EVENT_PUSH_CAN_NOT_FIND_USER(4),
/* 10 */   EVENT_PUSH_CAN_NOT_CHANGE_STATE(5),
/* 11 */   EVENT_PUSH_FAIL_REQ_IS_INVALID(6),
/* 12 */   EVENT_PUSH_FAIL_ALREADY_DONE(7),
/* 13 */   EVENT_PUSH_FAIL_REFRESH_LIST(8),
/* 14 */   EVENT_PUSH_FAIL_CAN_NOT_FIND_EVENT_PUSH(9),
/* 15 */   EVENT_PUSH_FAIL_RECEIVE_ITEM_SAFE_PLACE(10),
/* 16 */   EVENT_PUSH_SYSTEM_RESULT_CODE_INVALID(11),
/* 17 */   EVENT_PUSH_SYSTEM_RESULT_FAIL(12),
/* 18 */   EVENT_PUSH_RECEIVE_FAIL_WEIGHT(13),
/* 19 */   EVENT_PUSH_RECEIVE_FAIL_INVENTORY_SIZE(14),
/* 20 */   EVENT_PUSH_RECEIVE_FAIL_ALREADY_FULL(15),
/* 21 */   EVENT_PUSH_RECEIVE_FAIL_INVALID_ITEM(16),
/* 22 */   EVENT_PUSH_CAN_NOT_USE_INTER_SERVER(17),
/* 23 */   EVENT_PUSH_RECEIVE_FAIL_CAN_GET_SAFETY_ZONE(18),
/* 24 */   EVENT_PUSH_SERVER_ERROR_INVALID_PARAM(19),
/* 25 */   EVENT_PUSH_ITEMS_ALREADY_BEING_RECIEVED(20),
/* 26 */   EVENT_PUSH_SYSTEM_ALREADY_DELETE(21);
/*    */   
/*    */   private int value;
/*    */   
/*    */   EventPushResultCode(int val) {
/* 31 */     this.value = val;
/*    */   }
/*    */   public int toInt() {
/* 34 */     return this.value;
/*    */   }
/*    */   public boolean equals(EventPushResultCode v) {
/* 37 */     return (this.value == v.value);
/*    */   }
/*    */   public static EventPushResultCode fromInt(int i) {
/* 40 */     switch (i) {
/*    */       case 1:
/* 42 */         return EVENT_PUSH_SUCCESS;
/*    */       case 2:
/* 44 */         return EVENT_PUSH_SYSTEM_LOAD_LIST_FAIL;
/*    */       case 3:
/* 46 */         return EVENT_PUSH_LIST_IS_NOT_LOAD;
/*    */       case 4:
/* 48 */         return EVENT_PUSH_CAN_NOT_FIND_USER;
/*    */       case 5:
/* 50 */         return EVENT_PUSH_CAN_NOT_CHANGE_STATE;
/*    */       case 6:
/* 52 */         return EVENT_PUSH_FAIL_REQ_IS_INVALID;
/*    */       case 7:
/* 54 */         return EVENT_PUSH_FAIL_ALREADY_DONE;
/*    */       case 8:
/* 56 */         return EVENT_PUSH_FAIL_REFRESH_LIST;
/*    */       case 9:
/* 58 */         return EVENT_PUSH_FAIL_CAN_NOT_FIND_EVENT_PUSH;
/*    */       case 10:
/* 60 */         return EVENT_PUSH_FAIL_RECEIVE_ITEM_SAFE_PLACE;
/*    */       case 11:
/* 62 */         return EVENT_PUSH_SYSTEM_RESULT_CODE_INVALID;
/*    */       case 12:
/* 64 */         return EVENT_PUSH_SYSTEM_RESULT_FAIL;
/*    */       case 13:
/* 66 */         return EVENT_PUSH_RECEIVE_FAIL_WEIGHT;
/*    */       case 14:
/* 68 */         return EVENT_PUSH_RECEIVE_FAIL_INVENTORY_SIZE;
/*    */       case 15:
/* 70 */         return EVENT_PUSH_RECEIVE_FAIL_ALREADY_FULL;
/*    */       case 16:
/* 72 */         return EVENT_PUSH_RECEIVE_FAIL_INVALID_ITEM;
/*    */       case 17:
/* 74 */         return EVENT_PUSH_CAN_NOT_USE_INTER_SERVER;
/*    */       case 18:
/* 76 */         return EVENT_PUSH_RECEIVE_FAIL_CAN_GET_SAFETY_ZONE;
/*    */       case 19:
/* 78 */         return EVENT_PUSH_SERVER_ERROR_INVALID_PARAM;
/*    */       case 20:
/* 80 */         return EVENT_PUSH_ITEMS_ALREADY_BEING_RECIEVED;
/*    */       case 21:
/* 82 */         return EVENT_PUSH_SYSTEM_ALREADY_DELETE;
/*    */     } 
/* 84 */     throw new IllegalArgumentException(String.format("invalid arguments EventPushResultCode, %d", new Object[] { Integer.valueOf(i) }));
/*    */   }
/*    */ }


/* Location:              D:\天堂R\엠제이_군주_용기사_리부트_팩\TheDay.jar!\l1j\server\MJTemplate\MJProto\Mainserver_Client_EventPush\EventPushResultCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */