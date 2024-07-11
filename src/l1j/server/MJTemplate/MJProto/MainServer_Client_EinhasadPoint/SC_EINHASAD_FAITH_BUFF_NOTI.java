/*     */ package l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint;
/*     */ import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
/*     */ 
/*     */ public class SC_EINHASAD_FAITH_BUFF_NOTI implements MJIProtoMessage {
/*     */   private eNotiType _noti_type;
/*     */   private int _tooltip_str_id;
/*     */   
/*     */   public static void send_index(L1PcInstance pc, int i, int desc_id, int index_id) {
/*   9 */     SC_EINHASAD_FAITH_BUFF_NOTI noti = new SC_EINHASAD_FAITH_BUFF_NOTI();
/*  10 */     noti.set_noti_type(eNotiType.fromInt(i));
/*  11 */     noti.set_tooltip_str_id(desc_id);
/*  12 */     noti.add_enable_indexIds(index_id);
/*     */     
/*  14 */     pc.sendPackets(noti.writeTo(MJEProtoMessages.SC_EINHASAD_FAITH_BUFF_NOTI), true);
/*     */   } private LinkedList<Integer> _enable_indexIds; private LinkedList<Integer> _enable_groupIds;
/*     */   public static void send_group(L1PcInstance pc, int i, int desc_id, int group_id) {
/*  17 */     SC_EINHASAD_FAITH_BUFF_NOTI noti = new SC_EINHASAD_FAITH_BUFF_NOTI();
/*  18 */     noti.set_noti_type(eNotiType.fromInt(i));
/*  19 */     noti.set_tooltip_str_id(desc_id);
/*  20 */     noti.add_enable_groupIds(group_id);
/*     */     
/*  22 */     pc.sendPackets(noti.writeTo(MJEProtoMessages.SC_EINHASAD_FAITH_BUFF_NOTI), true);
/*     */   }
/*     */   
/*     */   public static SC_EINHASAD_FAITH_BUFF_NOTI newInstance() {
/*  26 */     return new SC_EINHASAD_FAITH_BUFF_NOTI();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  32 */   private int _memorizedSerializedSize = -1;
/*  33 */   private byte _memorizedIsInitialized = -1;
/*     */   
/*     */   private int _bit;
/*     */   
/*     */   public eNotiType get_noti_type() {
/*  38 */     return this._noti_type;
/*     */   }
/*     */   public void set_noti_type(eNotiType val) {
/*  41 */     this._bit |= 0x1;
/*  42 */     this._noti_type = val;
/*     */   }
/*     */   public boolean has_noti_type() {
/*  45 */     return ((this._bit & 0x1) == 1);
/*     */   }
/*     */   public int get_tooltip_str_id() {
/*  48 */     return this._tooltip_str_id;
/*     */   }
/*     */   public void set_tooltip_str_id(int val) {
/*  51 */     this._bit |= 0x2;
/*  52 */     this._tooltip_str_id = val;
/*     */   }
/*     */   public boolean has_tooltip_str_id() {
/*  55 */     return ((this._bit & 0x2) == 2);
/*     */   }
/*     */   public LinkedList<Integer> get_enable_indexIds() {
/*  58 */     return this._enable_indexIds;
/*     */   }
/*     */   public void add_enable_indexIds(int val) {
/*  61 */     if (!has_enable_indexIds()) {
/*  62 */       this._enable_indexIds = new LinkedList<>();
/*  63 */       this._bit |= 0x4;
/*     */     } 
/*  65 */     this._enable_indexIds.add(Integer.valueOf(val));
/*     */   }
/*     */   public boolean has_enable_indexIds() {
/*  68 */     return ((this._bit & 0x4) == 4);
/*     */   }
/*     */   public LinkedList<Integer> get_enable_groupIds() {
/*  71 */     return this._enable_groupIds;
/*     */   }
/*     */   public void add_enable_groupIds(int val) {
/*  74 */     if (!has_enable_groupIds()) {
/*  75 */       this._enable_groupIds = new LinkedList<>();
/*  76 */       this._bit |= 0x8;
/*     */     } 
/*  78 */     this._enable_groupIds.add(Integer.valueOf(val));
/*     */   }
/*     */   public boolean has_enable_groupIds() {
/*  81 */     return ((this._bit & 0x8) == 8);
/*     */   }
/*     */   
/*     */   public long getInitializeBit() {
/*  85 */     return this._bit;
/*     */   }
/*     */   
/*     */   public int getMemorizedSerializeSizedSize() {
/*  89 */     return this._memorizedSerializedSize;
/*     */   }
/*     */   
/*     */   public int getSerializedSize() {
/*  93 */     int size = 0;
/*  94 */     if (has_noti_type()) {
/*  95 */       size += ProtoOutputStream.computeEnumSize(1, this._noti_type.toInt());
/*     */     }
/*  97 */     if (has_tooltip_str_id()) {
/*  98 */       size += ProtoOutputStream.computeUInt32Size(2, this._tooltip_str_id);
/*     */     }
/* 100 */     if (has_enable_indexIds()) {
/* 101 */       for (Iterator<Integer> iterator = this._enable_indexIds.iterator(); iterator.hasNext(); ) { int val = ((Integer)iterator.next()).intValue();
/* 102 */         size += ProtoOutputStream.computeInt32Size(3, val); }
/*     */     
/*     */     }
/* 105 */     if (has_enable_groupIds()) {
/* 106 */       for (Iterator<Integer> iterator = this._enable_groupIds.iterator(); iterator.hasNext(); ) { int val = ((Integer)iterator.next()).intValue();
/* 107 */         size += ProtoOutputStream.computeInt32Size(4, val); }
/*     */     
/*     */     }
/* 110 */     this._memorizedSerializedSize = size;
/* 111 */     return size;
/*     */   }
/*     */   
/*     */   public boolean isInitialized() {
/* 115 */     if (this._memorizedIsInitialized == 1)
/* 116 */       return true; 
/* 117 */     if (!has_noti_type()) {
/* 118 */       this._memorizedIsInitialized = -1;
/* 119 */       return false;
/*     */     } 
/* 121 */     if (!has_tooltip_str_id()) {
/* 122 */       this._memorizedIsInitialized = -1;
/* 123 */       return false;
/*     */     } 
/* 125 */     if (has_enable_indexIds());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 133 */     if (has_enable_groupIds());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 141 */     this._memorizedIsInitialized = 1;
/* 142 */     return true;
/*     */   }
/*     */   
/*     */   public void writeTo(ProtoOutputStream output) throws IOException {
/* 146 */     if (has_noti_type()) {
/* 147 */       output.writeEnum(1, this._noti_type.toInt());
/*     */     }
/* 149 */     if (has_tooltip_str_id()) {
/* 150 */       output.writeUInt32(2, this._tooltip_str_id);
/*     */     }
/* 152 */     if (has_enable_indexIds()) {
/* 153 */       for (Iterator<Integer> iterator = this._enable_indexIds.iterator(); iterator.hasNext(); ) { int val = ((Integer)iterator.next()).intValue();
/* 154 */         output.wirteInt32(3, val); }
/*     */     
/*     */     }
/* 157 */     if (has_enable_groupIds()) {
/* 158 */       for (Iterator<Integer> iterator = this._enable_groupIds.iterator(); iterator.hasNext(); ) { int val = ((Integer)iterator.next()).intValue();
/* 159 */         output.wirteInt32(4, val); }
/*     */     
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ProtoOutputStream writeTo(MJEProtoMessages message) {
/* 166 */     ProtoOutputStream stream = ProtoOutputStream.newInstance(getSerializedSize() + 5, message.toInt());
/*     */     try {
/* 168 */       writeTo(stream);
/* 169 */     } catch (IOException e) {
/* 170 */       e.printStackTrace();
/*     */     } 
/* 172 */     return stream;
/*     */   }
/*     */   
/*     */   public MJIProtoMessage readFrom(ProtoInputStream input) throws IOException {
/* 176 */     while (!input.isAtEnd()) {
/* 177 */       int tag = input.readTag();
/* 178 */       switch (tag) {
/*     */         case 8:
/* 180 */           set_noti_type(eNotiType.fromInt(input.readEnum()));
/*     */           continue;
/*     */         
/*     */         case 16:
/* 184 */           set_tooltip_str_id(input.readUInt32());
/*     */           continue;
/*     */         
/*     */         case 24:
/* 188 */           add_enable_indexIds(input.readInt32());
/*     */           continue;
/*     */         
/*     */         case 32:
/* 192 */           add_enable_groupIds(input.readInt32());
/*     */           continue;
/*     */       } 
/*     */       
/* 196 */       return this;
/*     */     } 
/*     */ 
/*     */     
/* 200 */     return this;
/*     */   }
/*     */   
/*     */   public MJIProtoMessage readFrom(GameClient clnt, byte[] bytes) {
/* 204 */     ProtoInputStream is = ProtoInputStream.newInstance(bytes, 5, (bytes[3] & 0xFF | bytes[4] << 8 & 0xFF00) + 5);
/*     */     try {
/* 206 */       readFrom(is);
/*     */       
/* 208 */       if (!isInitialized()) {
/* 209 */         return this;
/*     */       }
/* 211 */       L1PcInstance pc = clnt.getActiveChar();
/* 212 */       if (pc == null) {
/* 213 */         return this;
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 218 */     catch (Exception e) {
/* 219 */       e.printStackTrace();
/*     */     } 
/* 221 */     return this;
/*     */   }
/*     */   
/*     */   public MJIProtoMessage copyInstance() {
/* 225 */     return new SC_EINHASAD_FAITH_BUFF_NOTI();
/*     */   }
/*     */   
/*     */   public MJIProtoMessage reloadInstance() {
/* 229 */     return newInstance();
/*     */   }
/*     */   
/*     */   public void dispose() {
/* 233 */     this._bit = 0;
/* 234 */     this._memorizedIsInitialized = -1;
/*     */   }
/*     */   
/* 237 */   public enum eNotiType { NEW(1),
/* 238 */     RESTART(2),
/* 239 */     END(3);
/*     */     private int value;
/*     */     
/*     */     eNotiType(int val) {
/* 243 */       this.value = val;
/*     */     }
/*     */     public int toInt() {
/* 246 */       return this.value;
/*     */     }
/*     */     public boolean equals(eNotiType v) {
/* 249 */       return (this.value == v.value);
/*     */     }
/*     */     public static eNotiType fromInt(int i) {
/* 252 */       switch (i) {
/*     */         case 1:
/* 254 */           return NEW;
/*     */         case 2:
/* 256 */           return RESTART;
/*     */         case 3:
/* 258 */           return END;
/*     */       } 
/* 260 */       throw new IllegalArgumentException(String.format("invalid arguments eNotiType, %d", new Object[] { Integer.valueOf(i) }));
/*     */     } }
/*     */ 
/*     */ }


/* Location:              D:\天堂R\엠제이_군주_용기사_리부트_팩\TheDay.jar!\l1j\server\MJTemplate\MJProto\MainServer_Client_EinhasadPoint\SC_EINHASAD_FAITH_BUFF_NOTI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */