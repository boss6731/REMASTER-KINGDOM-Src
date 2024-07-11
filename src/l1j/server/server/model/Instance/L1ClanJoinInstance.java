 package l1j.server.server.model.Instance;

 import java.util.concurrent.ConcurrentHashMap;
 import l1j.server.server.IdFactory;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1ClanJoin;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_Message_YN;
 import l1j.server.server.serverpackets.S_RemoveObject;
 import l1j.server.server.serverpackets.S_WorldPutObject;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.utils.MJBytesOutputStream;

 public class L1ClanJoinInstance
   extends L1Object
 {
   private static final long serialVersionUID = 1L;
   private static final int USE_ITEM_ID = 3000248;
   private static final ConcurrentHashMap<Integer, L1ClanJoinInstance> _clan_to_cj = new ConcurrentHashMap<>(32); private String _name; private String _title; private int _owner_id; private int _clan_id;

   public static boolean ban_user(L1PcInstance pc) {
     L1ClanJoinInstance cjInstance = _clan_to_cj.get(Integer.valueOf(pc.getClanid()));
     if (cjInstance == null) {
       return false;
     }
     removeInstance(pc.getClanid());
     return true;
   }
     private int _emblem_id;
     private int _lawful;
     private int _gfx;
     private int _heading;
     private ServerBasePacket _world_put;

     public static boolean use_item(L1PcInstance pc, L1ItemInstance item) {
         // 如果道具ID不是3000248，返回false
         if (item.getItemId() != 3000248) {
             return false;
         }
         // 如果玩家等級小於道具的最低使用等級，發送提示消息
         if (item.getItem().getMinLevel() > pc.getLevel()) {
             pc.sendPackets(String.format("%d等級以上才可使用。", item.getItem().getMinLevel()));
             return true;
         }

         // 根據玩家的血盟ID獲取血盟加入實例
         L1ClanJoinInstance cjInstance = _clan_to_cj.get(pc.getClanid());

         // 如果血盟加入實例為空，進行以下操作
         if (cjInstance == null) {
             L1Clan clan = pc.getClan();

             // 如果玩家沒有血盟，發送提示消息
             if (clan == null) {
                 pc.sendPackets("無血盟無法使用此道具。");
                 return true;
             }

             // 如果玩家不是血盟領袖，發送提示消息
             if (!clan.getLeaderName().equalsIgnoreCase(pc.getName())) {
                 pc.sendPackets("只有血盟領袖才能使用此道具。");
                 return true;
             }

             // 檢查玩家的位置是否符合使用要求（吉蘭旅館前）
             int x = pc.getX();
             int y = pc.getY();
             if (x < 33422 || y < 32794 || x > 33436 || y > 32799) {
                 pc.sendPackets("只能在吉蘭旅館前使用此道具。");
                 return true;
             }

             // 發送開始無人血盟加入提示消息並創建新實例
             pc.sendPackets("無人血盟加入已開始，您可以繼續狩獵。");
             newInstance(pc);
         } else {
             // 如果血盟加入實例不為空，檢查玩家是否是血盟領袖
             L1Clan clan = pc.getClan();
             if (clan.getLeaderId() != pc.getId()) {
                 pc.sendPackets("只有血盟領袖才能使用此道具。");
                 return true;
             }

             // 發送結束無人血盟加入提示消息並移除實例
             pc.sendPackets("無人血盟加入已結束。");
             removeInstance(pc.getClanid());
         }

         return true; // 返回true表示道具使用成功
     }

     return true;
   }

   public static L1ClanJoinInstance newInstance(L1PcInstance pc) {
     L1ClanJoinInstance cjInstance = newInstance();
     cjInstance.set_name(pc.getName());
     cjInstance.set_title(pc.getTitle());
     cjInstance.set_owner_id(pc.getId());
     cjInstance.set_clan_id(pc.getClanid());
     cjInstance.set_lawful(pc.getLawful());
     cjInstance.setId(IdFactory.getInstance().nextId());
     cjInstance.set_emblem_id(IdFactory.getInstance().nextId());
     cjInstance.setX(pc.getX());
     cjInstance.setY(pc.getY());
     cjInstance.setMap(pc.getMapId());
     cjInstance.set_heading(pc.getHeading());
     cjInstance.set_gfx(pc.getClassId());

     cjInstance.set_world_put((ServerBasePacket)S_WorldPutObject.get(cjInstance.serialize()));

     _clan_to_cj.put(Integer.valueOf(pc.getClanid()), cjInstance);
     L1World.getInstance().storeObject(cjInstance);
     L1World.getInstance().addVisibleObject(cjInstance);
     return cjInstance;
   }

   public static L1ClanJoinInstance newInstance() {
     return new L1ClanJoinInstance();
   }

   public static void removeInstance(int clan_id) {
     L1ClanJoinInstance cjInstance = _clan_to_cj.remove(Integer.valueOf(clan_id));
     if (cjInstance != null) {
       cjInstance.dispose();
     }
   }



   public void dispose() {
     L1World world = L1World.getInstance();
     world.removeVisibleObject(this);
     world.removeObject(this);
     if (this._world_put != null) {
       this._world_put.clear();
       this._world_put = null;
     }

     for (L1PcInstance player : world.getAllPlayers()) {
       if (player == null)
         continue;
       if (player.knownsObject(this)) {
         player.removeKnownObject(this);
         player.sendPackets((ServerBasePacket)new S_RemoveObject(this));
       }
     }
   }

   public L1ClanJoinInstance set_name(String name) {
     this._name = name;
     return this;
   }

   public String get_name() {
     return this._name;
   }

   public L1ClanJoinInstance set_title(String title) {
     this._title = title;
     return this;
   }

   public String get_title() {
     return this._title;
   }

   public L1ClanJoinInstance set_owner_id(int owner_id) {
     this._owner_id = owner_id;
     return this;
   }

   public int get_owner_id() {
     return this._owner_id;
   }

   public L1PcInstance get_owner() {
     return L1World.getInstance().getPlayer(get_name());
   }

   public L1ClanJoinInstance set_clan_id(int clan_id) {
     this._clan_id = clan_id;
     return this;
   }

   public int get_clan_id() {
     return this._clan_id;
   }

   public L1Clan get_clan() {
     return L1World.getInstance().getClan(get_clan_id());
   }

   public L1ClanJoinInstance set_emblem_id(int emblem_id) {
     this._emblem_id = emblem_id;
     return this;
   }

   public int get_emblem_id() {
     return this._emblem_id;
   }

   public L1ClanJoinInstance set_lawful(int lawful) {
     this._lawful = lawful;
     return this;
   }

   public int get_lawful() {
     return this._lawful;
   }

   public L1ClanJoinInstance set_gfx(int gfx) {
     this._gfx = gfx;
     return this;
   }

   public int get_gfx() {
     return this._gfx;
   }

   public L1ClanJoinInstance set_heading(int heading) {
     this._heading = heading;
     return this;
   }

   public int get_heading() {
     return this._heading;
   }

   public L1ClanJoinInstance set_world_put(ServerBasePacket pck) {
     this._world_put = pck;
     return this;
   }

   public ServerBasePacket get_world_put() {
     return this._world_put;
   }



   public void onPerceive(L1PcInstance perceivedFrom) {
     if (this._world_put == null || perceivedFrom == null)
       return;
     perceivedFrom.addKnownObject(this);
     if (perceivedFrom.getAI() == null) {
       perceivedFrom.sendPackets(this._world_put, false);
     }
   }

   public void receiveDamage(L1Character attacker, int damage) {
     if (attacker == null || !attacker.instanceOf(4)) {
       return;
     }
     join((L1PcInstance)attacker);
   }

     public void join(L1PcInstance joinner) {
// 檢查玩家是否已經有血盟或紅騎士血盟，如果有則返回
         if (joinner.getClanid() != 0 || joinner.getRedKnightClanId() != 0) {
             return;
         }

// 獲取目標血盟，如果為空則返回
         L1Clan clan = get_clan();
         if (clan == null)
             return;

         try {
             S_Message_YN myn;
             L1PcInstance management = clan.getonline간부(); // 獲取血盟在線管理員

// 根據血盟加入類型進行不同處理
             switch (clan.getJoinType()) {
                 case 0: // 自動加入
                     if (L1ClanJoin.getInstance().ClanJoin(clan, joinner)) {
                         joinner.sendPackets(String.format("您已加入 %s 血盟。", clan.getClanName()));
                     } else {
                         joinner.sendPackets(String.format("加入 %s 血盟失敗。", clan.getClanName()));
                     }
                     return;

                 case 1: // 管理員審批加入
                     if (management == null) {
                         joinner.sendPackets(String.format("%s 血盟目前無法加入。", clan.getClanName()));
                         return;
                     }
                     management.setTempID(joinner.getId());
                     myn = new S_Message_YN(97, joinner.getName()); // 創建消息請求
                     management.sendPackets(myn, true);
                     joinner.sendPackets(String.format("正在等待 %s 血盟的加入批准。", clan.getClanName()));
                     return;
             }

// 如果沒有符合的加入類型，發送無法加入的消息
             joinner.sendPackets(String.format("%s 血盟目前無法加入。", clan.getClanName()));
         }
     catch (Exception e) {
       e.printStackTrace();
     }
   }

     public byte[] serialize() {
         MJBytesOutputStream s = new MJBytesOutputStream(256); // 創建一個輸出流，初始容量為256字節
         byte[] b = null;
         try {
             L1Clan clan = get_clan(); // 獲取當前對象所屬的血盟

             s.write(8); // 寫入一個字節8
             s.writePoint(getX(), getY()); // 寫入X和Y坐標點
             s.write(16); // 寫入一個字節16
             s.writeBit(getId()); // 寫入對象ID
             s.write(24); // 寫入一個字節24
             s.writeBit((this._gfx == 0) ? 6094L : 6080L); // 寫入圖像ID（根據_gfx判斷）

             s.write(32); // 寫入一個字節32
             s.writeBit(0L); // 寫入一個默認值0
             s.write(40); // 寫入一個字節40
             s.writeBit(get_heading()); // 寫入方向
             s.write(48); // 寫入一個字節48
             s.writeBit(15L); // 寫入一個默認值15
             s.write(56); // 寫入一個字節56
             s.write(1); // 寫入一個字節1
             s.write(64); // 寫入一個字節64
             s.writeBit(get_lawful()); // 寫入合法值
             s.write(74); // 寫入一個字節74
             if (clan == null) {
                 s.writeS2(String.format("無人加入^%s", new Object[] { get_name() })); // 寫入字符串 "無人加入" 和名稱
             } else {
                 s.writeS2(String.format("血盟名:%s^角色名(盟主):[%s]", new Object[] { clan.getClanName(), get_name() })); // 寫入血盟名稱和角色名稱
             }
         } catch (IOException e) {
             e.printStackTrace(); // 捕捉並打印IO異常
         }
         b = s.toByteArray(); // 將流轉換為字節數組
         return b; // 返回字節數組
     }
       s.write(82);
       s.writeS2(get_title());
       s.write(88);
       s.write(0);
       s.write(96);
       s.write(0);
       s.write(104);
       s.writeBit(0L);
       s.write(112);
       s.write(0);
       s.write(120);
       s.write(0);
       s.writeBit(128L);
       s.write(0);
       s.writeBit(136L);
       s.write(0);
       s.writeBit(144L);
       s.write(0);
       s.writeBit(152L);
       s.write(0);
       s.writeBit(162L);
       s.write(0);
       s.writeBit(170L);
       s.write(0);
       s.writeBit(176L);
       s.write(0);
       s.writeBit(184L);
       s.writeBit(-1L);
       s.writeBit(192L);
       s.write(1);
       s.writeBit(202L);
       s.writeBit(0L);
       s.writeBit(208L);
       s.writeBit(-1L);
       s.writeBit(216L);
       s.writeB(false);
       s.writeBit(224L);
       s.write(1);
       s.writeBit(232L);
       s.writeBit(0L);
       s.writeBit(240L);
       s.writeBit(-1L);
       s.writeBit(248L);
       s.writeBit(0L);
       s.writeBit(256L);
       s.write(0);
       s.writeBit(264L);
       s.writeBit(0L);
       s.writeBit(272L);
       s.write(5);
       s.writeBit(280L);
       s.write(0);
       s.writeBit(288L);
       s.write(0);
       s.writeH(0);
       b = s.toArray();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       if (s != null) {
         s.close();
         s.dispose();
         s = null;
       }
     }
     return b;
   }

   private static int _instanceType = -1;

   public int getL1Type() {
     return (_instanceType == -1) ? (_instanceType = super.getL1Type() | 0x800) : _instanceType;
   }
 }


