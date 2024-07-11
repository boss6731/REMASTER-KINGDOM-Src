 package MJShiftObject.Battle.ForgottenIsland;

 import MJShiftObject.Battle.MJShiftBattleArgs;
 import MJShiftObject.Battle.MJShiftBattleCharacterInfo;
 import MJShiftObject.Battle.MJShiftBattlePlayManager;
 import MJShiftObject.MJShiftObjectHelper;
 import MJShiftObject.MJShiftObjectManager;
 import MJShiftObject.Object.MJShiftObject;
 import l1j.server.MJTemplate.Chain.Action.MJDeadRestartChain;
 import l1j.server.MJTemplate.Chain.Action.MJIDeadRestartHandler;
 import l1j.server.MJTemplate.Chain.Action.MJIPickupHandler;
 import l1j.server.MJTemplate.Chain.Action.MJIRestartHandler;
 import l1j.server.MJTemplate.Chain.Action.MJITeleportHandler;
 import l1j.server.MJTemplate.Chain.Action.MJIWalkFilterHandler;
 import l1j.server.MJTemplate.Chain.Action.MJPickupChain;
 import l1j.server.MJTemplate.Chain.Action.MJTeleportChain;
 import l1j.server.MJTemplate.Chain.Action.MJWalkFilterChain;
 import l1j.server.MJTemplate.Chain.KillChain.MJCharacterKillChain;
 import l1j.server.MJTemplate.Chain.KillChain.MJICharacterKillHandler;
 import l1j.server.MJTemplate.Chain.KillChain.MJIMonsterKillHandler;
 import l1j.server.MJTemplate.Chain.KillChain.MJMonsterKillChain;
 import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
 import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
 import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CHANGE_TEAM_NOTI_PACKET;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_EVENT_COUNTDOWN_NOTI_PACKET;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TEAM_ID_SERVER_NO_MAPPING_INFO;
 import l1j.server.MJTemplate.MJString;
 import l1j.server.MJTemplate.PacketHelper.MJPacketFactory;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1MonsterInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.utils.IntRange;








 public class MJFIslandPlayManager
   extends MJShiftBattlePlayManager<MJFIslandTeamInfo>
   implements MJIWalkFilterHandler, MJIMonsterKillHandler, MJICharacterKillHandler, MJITeleportHandler, MJIPickupHandler, MJIRestartHandler, MJIDeadRestartHandler
 {
   public static int REVISION_TEAM_ID = 22;
   public MJFIslandPlayManager(long ended_millis, boolean is_local_server) {
     super(ended_millis, is_local_server);
     for (int i = 4; i <= 6; i++)
       this.m_teams.put(Integer.valueOf(i + REVISION_TEAM_ID), MJFIslandTeamInfo.newInstance(i + REVISION_TEAM_ID));
     MJWalkFilterChain.getInstance().add_handler(this);
     MJMonsterKillChain.getInstance().add_handler(this);
     MJCharacterKillChain.getInstance().add_handler(this);
     MJTeleportChain.getInstance().add_handler(this);
     MJDeadRestartChain.getInstance().add_handler(this);
     MJPickupChain.getInstance().add_handler(this);
   }


   protected String get_default_countdown_ready_message() {
     return "\\f2遺忘島等待時間";
   }

   protected String get_default_countdown_play_message() {
     return "\\f3被遺忘的島嶼";
   }

   protected int get_default_ready_seconds() {
     return MJShiftBattleArgs.FISLAND_READY_SECONDS;
   }

   protected int get_default_auto_message_remain_seconds() {
     return MJShiftBattleArgs.FISLAND_AUTO_MESSAGE_REMAIN_SECONDS;
   }

   protected String get_default_auto_message_db_name() {
     return "server_battle_message_forisland";
   }

   protected boolean get_default_is_auto_sequence_message() {
     return MJShiftBattleArgs.FISLAND_IS_AUTO_SEQUENCE_MESSAGE;
   }

   protected void do_ended_ready() {
     this.m_is_ready = false;
     GeneralThreadPool.getInstance().execute(new Runnable()
         {
           public void run() {
             try {
               SC_EVENT_COUNTDOWN_NOTI_PACKET noti = MJFIslandPlayManager.this.create_countdown();
               ServerBasePacket[] pcks = MJPacketFactory.create_duplicate_message_packets("被遺忘的島嶼開始了。");
               ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_EVENT_COUNTDOWN_NOTI_PACKET);
               for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
                 if (pc == null || pc.getNetConnection() == null || pc.getNetConnection().isClosed()) {
                   continue;
                 }
                 if (pc.get_battle_info() == null) {
                   continue;
                 }
                 pc.sendPackets(pcks, false);
                 pc.sendPackets(stream, false);
               }
               stream.dispose();
               for (ServerBasePacket pck : pcks)
                 pck.clear();
             } catch (Exception e) {
               e.printStackTrace();
             }
           }
         });
   }


   protected void on_play_tick() {}


   public int next_update_tick() {
     return -1;
   }


   public void on_update_tick() {}


   public void on_enter(L1PcInstance pc) {
     int object_id = pc.getId();
     MJFIslandTeamInfo tInfo = (MJFIslandTeamInfo)this.m_players_to_team.get(Integer.valueOf(object_id));
     if (tInfo == null) {
       int team_id = 0;
       if (this.m_is_local_server) {
         team_id = this.m_local_server_team_index.getAndIncrement() % 3 + 4;
       } else {
         team_id = MJShiftObjectManager.getInstance().get_receiver_team_id(pc.get_server_identity());
       }
       if (!IntRange.includes(team_id, 4, 6)) {
         System.out.println(String.format("找不到 %s 的團隊 ID（%d，%s）。", new Object[] { pc.getName(), Integer.valueOf(team_id), pc.getNetConnection().get_server_identity() }));
         return;
       }
       tInfo = (MJFIslandTeamInfo)this.m_teams.get(Integer.valueOf(team_id + REVISION_TEAM_ID));
       this.m_players_to_team.put(Integer.valueOf(object_id), tInfo);
     }
     tInfo.do_enter(pc, 0);
   }

   public void on_closed() {
     MJWalkFilterChain.getInstance().remove_handler(this);
     MJMonsterKillChain.getInstance().remove_handler(this);
     MJCharacterKillChain.getInstance().remove_handler(this);
     MJTeleportChain.getInstance().remove_handler(this);
     MJPickupChain.getInstance().remove_handler(this);
     MJDeadRestartChain.getInstance().remove_handler(this);
     for (MJFIslandTeamInfo bInfo : this.m_teams.values())
       bInfo.clear();
     this.m_players_to_team.clear();
   }


   public void on_kill(L1PcInstance attacker, L1PcInstance victim) {}


   public void on_kill(L1PcInstance attacker, L1MonsterInstance m) {}

   public boolean on_teleported(L1PcInstance owner, int next_x, int next_y, int map_id, int old_mapid) {
     MJShiftBattleCharacterInfo cInfo = owner.get_battle_info();
     if (cInfo == null || !(cInfo instanceof MJFIslandCharacterInfo)) {
       return false;
     }
     cInfo.team_info.do_revision_map(owner);
     owner.sendPackets((MJIProtoMessage)create_countdown(), MJEProtoMessages.SC_EVENT_COUNTDOWN_NOTI_PACKET, true);
     SC_CHANGE_TEAM_NOTI_PACKET noti = SC_CHANGE_TEAM_NOTI_PACKET.newInstance();
     noti.set_object_id(owner.getId());
     noti.set_object_team_id(cInfo.team_info.team_id);
     owner.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_CHANGE_TEAM_NOTI_PACKET, true);
     SC_TEAM_ID_SERVER_NO_MAPPING_INFO info = SC_TEAM_ID_SERVER_NO_MAPPING_INFO.newInstance();
     for (int i = 4; i <= 6; i++) {
       SC_TEAM_ID_SERVER_NO_MAPPING_INFO.MAPPING mapping = SC_TEAM_ID_SERVER_NO_MAPPING_INFO.MAPPING.newInstance();
       mapping.set_server_no(i + REVISION_TEAM_ID);
       mapping.set_team_id(i + REVISION_TEAM_ID);
       info.add_mapping_info(mapping);
     }
     owner.sendPackets((MJIProtoMessage)info, MJEProtoMessages.SC_TEAM_ID_SERVER_NO_MAPPING_INFO, true);
     owner.sendPackets(MJPacketFactory.create_duplicate_message_packets(this.m_is_ready ? "這是目前的等待時間。" : "《被遺忘的島嶼》副本正在構建中。"), true);
     return false;
   }

   public boolean is_teleport(L1PcInstance owner, int next_x, int next_y, int map_id) {
     return false;
   }

   public boolean is_moved(L1PcInstance owner, int next_x, int next_y) {
     return false;
   }

   public boolean on_pickup(L1PcInstance pc, L1ItemInstance item, int amount) {
     if (!pc.is_shift_battle() || pc.getNetConnection() == null) {
       return false;
     }
     MJShiftBattleCharacterInfo bInfo = pc.get_battle_info();
     if (bInfo == null || !(bInfo instanceof MJFIslandCharacterInfo)) {
       return false;
     }
     String homeserver_identity = pc.getNetConnection().get_server_identity();
     if (MJString.isNullOrEmpty(homeserver_identity)) {
       return false;
     }
     MJShiftObject sobject = MJShiftObjectManager.getInstance().get_shift_object(pc);
     if (sobject == null) {
       return false;
     }
     L1ItemInstance external_item = ItemTable.getInstance().createItem(item.getItemId());
     external_item.setCount(amount);
     external_item.setEnchantLevel(item.getEnchantLevel());
     external_item.setIdentified(item.isIdentified());
     external_item.set_durability(item.get_durability());
     external_item.setChargeCount(item.getChargeCount());
     external_item.setRemainingTime(item.getRemainingTime());
     external_item.setLastUsed(item.getLastUsed());
     external_item.setBless(item.getItem().getBless());
     external_item.setAttrEnchantLevel(item.getAttrEnchantLevel());
     external_item.setSpecialEnchant(item.getSpecialEnchant());
     external_item.set_bless_level(item.get_bless_level());
     external_item.set_item_level(item.get_item_level());
     external_item.setHotel_Town(item.getHotel_Town());
     MJShiftObjectHelper.update_pickup_items(sobject.get_source_id(), external_item, homeserver_identity);
     return false;
   }

   public int[] get_death_location(L1PcInstance pc) {
     MJShiftBattleCharacterInfo cInfo = pc.get_battle_info();
     if (cInfo == null || !(cInfo instanceof MJFIslandCharacterInfo)) {
       return null;
     }
     return cInfo.team_info.next_position(pc);
   }

   public void on_death_restarted(final L1PcInstance pc) {
     MJShiftBattleCharacterInfo cInfo = pc.get_battle_info();
     if (cInfo == null || !(cInfo instanceof MJFIslandCharacterInfo)) {
       return;
     }
     GeneralThreadPool.getInstance().schedule(new Runnable()
         {
           public void run() {
             MJFIslandPlayManager.this.on_teleported(pc, pc.getX(), pc.getY(), pc.getMapId(), pc.getMapId());
           }
         }10L);
   }

   public boolean is_restart(L1PcInstance pc) {
     if (pc.get_battle_info() != null) {
       pc.sendPackets("這裡無法重新啟動。");
       return true;
     }
     return false;
   }

   public void on_restarted(L1PcInstance pc) {}
 }


