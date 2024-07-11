 package l1j.server.server.clientpackets;

 import java.util.Collection;
 import l1j.server.MJDungeonTimer.DungeonTimeInformation;
 import l1j.server.MJDungeonTimer.Loader.DungeonTimeInformationLoader;
 import l1j.server.MJDungeonTimer.Progress.DungeonTimeProgress;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_ALLY_LIST;
 import l1j.server.MJWarSystem.MJCastleWarBusiness;
 import l1j.server.server.GameClient;
 import l1j.server.server.datatables.CharacterTable;
 import l1j.server.server.model.Broadcaster;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_ACTION_UI2;
 import l1j.server.server.serverpackets.S_Message_YN;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SkillSound;
 import l1j.server.server.serverpackets.S_SurvivalCry;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class C_Rank
   extends ClientBasePacket {
   private static final String C_RANK = "[C] C_Rank";
   private L1ItemInstance weapon;

   public C_Rank(byte[] abyte0, GameClient clientthread) throws Exception {
     super(abyte0); int rank; String name; L1PcInstance targetPc, restorePc; String target_clan_name; L1Clan target_clan; L1PcInstance allianceLeader;
     L1Clan leader_clan;
     int gfx3, EnchantLevel2, kick_clanId, type = readC();

     L1PcInstance pc = clientthread.getActiveChar();
     if (pc == null) {
       return;
     }
     L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
     int castle_id = MJCastleWarBusiness.getInstance().NowCastleWarState();
     boolean is_war = MJCastleWarBusiness.getInstance().isNowWar(castle_id);

     if (clan == null) {
       return;
     }
       switch (type) {
           case 1:
               rank = readC(); // 讀取等級
               name = readS(); // 讀取名稱
               targetPc = L1World.getInstance().getPlayer(name); // 根據名稱獲取目標玩家

               if (!pc.isCrown() && pc.getClanRank() != 9 && pc.getClanRank() != 14) {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("賦予等級失敗：沒有賦予等級的權限。"));
                   return; // 如果玩家不是王冠且等級不為9或14，則發送權限不足的消息並結束程式
               }

               if (targetPc != null) {
                   if (pc.getClanid() == targetPc.getClanid()) {
                       try {
                           if (pc.getClanRank() != 10 && pc.getClanRank() != 9 && pc.getClanRank() != 14) {
                               pc.sendPackets((ServerBasePacket)new S_SystemMessage("賦予等級失敗：沒有賦予等級的權限。"));
                               return; // 如果玩家等級不是10、9或14，則發送權限不足的消息並結束程式
                           }
                           if (targetPc.isCrown() && targetPc.getId() == targetPc.getClan().getLeaderId()) {
                               pc.sendPackets((ServerBasePacket)new S_SystemMessage("賦予等級失敗：目標是血盟的君主。"));
                               return; // 如果目標玩家是君主，則發送相關消息並結束程式
                           }
                           if (pc.getClanRank() == 14 && rank == 3) {
                               pc.sendPackets((ServerBasePacket)new S_SystemMessage("賦予等級失敗：賦予的等級高於或等於自己。"));
                               return; // 如果玩家等級為14且賦予的等級為3，則發送相關消息並結束程式
                           }
                           if (pc.getClanRank() == 9 && rank == 9) {
                               pc.sendPackets((ServerBasePacket)new S_SystemMessage("賦予等級失敗：賦予的等級高於或等於自己。"));
                               return; // 如果玩家等級為9且賦予的等級也是9，則發送相關消息並結束程式
                           }
                           if (pc.getClanRank() == 9 && (targetPc.getClanRank() == 10 || targetPc.getClanRank() == 9 || targetPc.getClanRank() == 14)) {
                               pc.sendPackets((ServerBasePacket)new S_SystemMessage("賦予等級失敗：目標的等級高於或等於自己。"));
                               return; // 如果玩家等級為9且目標玩家的等級是10、9或14，則發送相關消息並結束程式
                           }
                           targetPc.setClanRank(rank); // 設置目標玩家的血盟等級
                           targetPc.save(); // 保存目標玩家數據
                           pc.sendPackets((ServerBasePacket)new S_PacketBox(27, rank, name)); // 發送封包通知等級變更
                           clan.UpdataClanMember(targetPc.getName(), targetPc.getClanRank()); // 更新血盟成員信息

                           String rankString = "一般"; // 預設等級名稱為“一般”
                           if (rank == 7) {
                               rankString = "修煉"; // 如果等級為7，設置等級名稱為“修煉”
                           } else if (rank == 8) {
                               rankString = "一般"; // 如果等級為8，設置等級名稱為“一般”
                           } else if (rank == 9) {
                               rankString = "守護騎士"; // 如果等級為9，設置等級名稱為“守護騎士”
                           } else if (rank == 13) {
                               rankString = "精銳"; // 如果等級為13，設置等級名稱為“精銳”
                           } else if (rank == 14) {
                               rankString = "副君主"; // 如果等級為14，設置等級名稱為“副君主”
                           }

                           targetPc.sendPackets((ServerBasePacket)new S_SystemMessage("等級: " + rankString + " 被授予")); // 發送系統消息通知目標玩家其新等級
                           targetPc.sendPackets((ServerBasePacket)new S_ACTION_UI2(targetPc, 25)); // 發送 UI 行動封包

                       } catch (Exception e) {
                           e.printStackTrace(); // 捕捉並打印異常
                       }
                       break;
                   }

                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("不是同一血盟的成員。")); // 發送系統消息通知玩家目標不是同一血盟成員

                   return;
                   restorePc = CharacterTable.getInstance().restoreCharacter(name); // 根據名稱恢復角色
                   if (restorePc != null && restorePc.getClanid() == pc.getClanid()) { // 如果恢復的角色存在並且與當前玩家屬於同一血盟
                       try {
                           if (restorePc.isCrown() && restorePc.getId() == restorePc.getClan().getLeaderId()) {
                               pc.sendPackets((ServerBasePacket)new S_SystemMessage("賦予等級失敗：目標是血盟的君主。")); // 如果目標是血盟君主，發送失敗消息
                               return;
                           }
                           if (pc.getClanRank() != 10 && pc.getClanRank() != 9 && pc.getClanRank() != 14) {
                               pc.sendPackets((ServerBasePacket)new S_SystemMessage("賦予等級失敗：沒有賦予等級的權限。")); // 如果玩家無權賦予等級，發送失敗消息
                               return;
                           }
                           if (pc.getClanRank() == 14 && rank == 3) {
                               pc.sendPackets((ServerBasePacket)new S_SystemMessage("賦予等級失敗：賦予的等級高於或等於自己。")); // 如果賦予的等級高於或等於自己，發送失敗消息
                               return;
                           }
                           if (pc.getClanRank() == 9 && rank == 9) {
                               pc.sendPackets((ServerBasePacket)new S_SystemMessage("賦予等級失敗：賦予的等級高於或等於自己。")); // 如果賦予的等級高於或等於自己，發送失敗消息
                               return;
                           }
                           if (pc.getClanRank() == 9 && (restorePc.getClanRank() == 10 || restorePc.getClanRank() == 9 || restorePc.getClanRank() == 14)) {
                               pc.sendPackets((ServerBasePacket)new S_SystemMessage("賦予等級失敗：目標的等級高於或等於自己。")); // 如果目標的等級高於或等於自己，發送失敗消息
                               return;
                           }
                           restorePc.setClanRank(rank); // 設置恢復角色的血盟等級
                           restorePc.save(); // 保存恢復角色數據
                           restorePc.sendPackets((ServerBasePacket)new S_PacketBox(27, rank, name)); // 發送封包通知恢復角色的等級變更
                           pc.sendPackets((ServerBasePacket)new S_PacketBox(27, rank, name)); // 發送封包通知當前玩家的等級變更
                           clan.UpdataClanMember(restorePc.getName(), restorePc.getClanRank()); // 更新血盟成員信息

                           String rankString = "一般"; // 預設等級名稱為“一般”
                           if (rank == 7) {
                               rankString = "修煉"; // 如果等級為7，設置等級名稱為“修煉”
                           } else if (rank == 3) {
                               rankString = "副君主"; // 如果等級為3，設置等級名稱為“副君主”
                           } else if (rank == 8) {
                               rankString = "一般"; // 如果等級為8，設置等級名稱為“一般”
                           } else if (rank == 9) {
                               rankString = "守護騎士"; // 如果等級為9，設置等級名稱為“守護騎士”
                           } else if (rank == 13) {
                               rankString = "精銳"; // 如果等級為13，設置等級名稱為“精銳”
                           }

                           for (L1PcInstance mem : clan.getOnlineClanMember()) {
                               mem.sendPackets((ServerBasePacket)new S_SystemMessage(restorePc.getName() + " 的等級已變更為 " + rankString + "。")); // 發送系統消息通知所有在線血盟成員恢復角色的等級變更
                           }
           } catch (Exception e) {
             e.printStackTrace();
           }
         } else {
           pc.sendPackets(2069);
           return;
         }
         restorePc = null;
         break;

       case 2:
         try {
           if (clan.AllianceSize() > 0) {
             SC_BLOOD_PLEDGE_ALLY_LIST.ally_list_send(pc);
           }
         } catch (Exception exception) {}
         break;

                   case 3:
                       target_clan_name = readS(); // 讀取目標血盟名稱
                       target_clan = L1World.getInstance().findClan(target_clan_name); // 根據名稱查找目標血盟

                       if (target_clan == null) {
                           pc.sendPackets((ServerBasePacket)new S_SystemMessage("不存在的血盟。")); // 如果目標血盟不存在，發送系統消息
                           return;
                       }
         allianceLeader = L1World.getInstance().findpc(target_clan.getLeaderName());
         if (allianceLeader == null) {
           pc.sendPackets(218, target_clan.getClanName());

           return;
         }
         if (is_war) {
           pc.sendPackets(1234);
         }

         if (pc.getLevel() < 25 || !pc.isCrown()) {
           pc.sendPackets(1206);
           return;
         }
         if (pc.getClan().AllianceSize() != 0) {
           pc.sendPackets(1202);
           return;
         }
                       if (clan.AllianceSize() > 4) {
                           pc.sendPackets((ServerBasePacket)new S_SystemMessage("同盟最多只能有4個血盟。"));
                           return;
                       }
         if (clan.getCurrentWar() != null) {
           pc.sendPackets(1234);

           return;
         }

                       leader_clan = allianceLeader.getClan(); // 獲取同盟領袖的血盟
                       if (allianceLeader != null) {
                           if (!leader_clan.isAlliance_leader() && allianceLeader.isCrown() && leader_clan.AllianceSize() > 0) {
                               pc.sendPackets((ServerBasePacket)new S_SystemMessage("只有同盟領袖的血盟可以加入。"));
                               return;
                           }
                           if (leader_clan.AllianceSize() > 4 && allianceLeader.isCrown()) {
                               pc.sendPackets((ServerBasePacket)new S_SystemMessage("對方血盟的同盟已超過4個血盟。"));
                               return;
                           }
                           if (allianceLeader.getLevel() > 24 && allianceLeader.isCrown()) {
                               allianceLeader.setTempID(pc.getId()); // 設置臨時ID為當前玩家的ID
                               allianceLeader.sendPackets((ServerBasePacket)new S_Message_YN(223, pc.getName())); // 發送確認消息給同盟領袖
                               break;
                           }
                           pc.sendPackets((ServerBasePacket)new S_SystemMessage("指令無效。")); // 發送無效指令消息
                       }
                       break;

                   case 4:
                       if (is_war) {
                           pc.sendPackets((ServerBasePacket)new S_SystemMessage("同盟：在攻城戰期間無法退出同盟。")); // 在攻城戰期間無法退出同盟
                       }

         if (clan.getCurrentWar() != null) {
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(1203));

           return;
         }
         if (clan.AllianceSize() > 0) {
           pc.sendPackets((ServerBasePacket)new S_Message_YN(1210, "")); break;
         }
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(1233));
         break;






                   case 5:
                       if (pc.get_food() >= 225) { // 如果玩家的食物值大於等於225
                           int addHp = 0;
                           int gfxId1 = 0;
                           int gfxId2 = 0;
                           long curTime = System.currentTimeMillis() / 1000L; // 當前時間（秒）
                           int fullTime = (int)((curTime - pc.getCryOfSurvivalTime()) / 60L); // 計算自上次使用技能至今的時間（分鐘）

                           if (fullTime < 180) { // 如果時間小於180分鐘
                               long time = pc.getCryOfSurvivalTime() + 10800L - curTime; // 計算剩餘冷卻時間

                               pc.sendPackets((ServerBasePacket)new S_SystemMessage("生存的呼喊：等待時間 (" + (time / 60L) + " 分 " + (time % 60L) + " 秒)")); // 發送系統消息通知等待時間

                               return;
                           }
           if (pc.getLevel() >= 1 && pc.getLevel() <= 87) {
             gfxId1 = 19286;
             gfxId2 = 8910;
             addHp = 800;
           } else if (pc.getLevel() >= 88 && pc.getLevel() <= 89) {
             gfxId1 = 19288;
             gfxId2 = 8908;
             addHp = 1000;
           } else if (pc.getLevel() >= 90 && pc.getLevel() <= 92) {
             gfxId1 = 19290;
             gfxId2 = 8908;
             addHp = 1200;
           } else if (pc.getLevel() >= 93 && pc.getLevel() <= 94) {
             gfxId1 = 19292;
             gfxId2 = 8908;
             addHp = 1300;
           } else if (pc.getLevel() >= 95 && pc.getLevel() <= 127) {
             gfxId1 = 19294;
             gfxId2 = 8908;
             addHp = 1500;
           }

           S_SkillSound sound = new S_SkillSound(pc.getId(), gfxId1);
           pc.sendPackets((ServerBasePacket)sound);
           Broadcaster.broadcastPacket((L1Character)pc, (ServerBasePacket)sound);
           sound = new S_SkillSound(pc.getId(), gfxId2);
           pc.sendPackets((ServerBasePacket)sound);
           Broadcaster.broadcastPacket((L1Character)pc, (ServerBasePacket)sound);
           pc.setCryOfSurvivalTime();
           pc.set_food(0);
           pc.sendPackets((ServerBasePacket)new S_PacketBox(11, 0));
           pc.setCurrentHp(pc.getCurrentHp() + addHp);
           pc.sendPackets((ServerBasePacket)new S_SurvivalCry(10800)); break;
         }
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(3461));
         break;

       case 6:
         if (pc.getWeapon() == null) {
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(1973));
           return;
         }
         gfx3 = 0;
         this.weapon = pc.getWeapon();
         EnchantLevel2 = this.weapon.getEnchantLevel();
         if (EnchantLevel2 < 0) {
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(79)); return;
         }
         if (EnchantLevel2 >= 0 && EnchantLevel2 <= 6) {
           gfx3 = 8684;
         } else if (EnchantLevel2 >= 7 && EnchantLevel2 <= 8) {
           gfx3 = 8685;
         } else if (EnchantLevel2 >= 9 && EnchantLevel2 <= 10) {
           gfx3 = 8773;
         } else if (EnchantLevel2 >= 11) {
           gfx3 = 8686;
         }
         pc.sendPackets((ServerBasePacket)new S_SkillSound(pc.getId(), gfx3));
         pc.broadcastPacket((ServerBasePacket)new S_SkillSound(pc.getId(), gfx3));
         break;
       case 8:
         send_dungeon_remains(pc, pc.get_account_progresses());
         send_dungeon_remains(pc, pc.get_character_progresses());
         break;
                   case 9:
                       kick_clanId = readD(); // 讀取要踢出的血盟ID
                       if (is_war) {
                           pc.sendPackets((ServerBasePacket)new S_SystemMessage("同盟：在攻城戰期間無法驅逐同盟。")); // 如果處於戰爭狀態，發送系統消息
                           return;
                       }

                       if (clan.getCurrentWar() != null) {
                           pc.sendPackets((ServerBasePacket)new S_ServerMessage(1235)); // 如果血盟處於戰爭中，發送系統消息
                           return;
                       }

                       if (clan.AllianceSize() > 0) {
                           L1Clan kick_clan = L1World.getInstance().getClan(kick_clanId); // 根據ID獲取要踢出的血盟
                           if (kick_clan != null) {
                               pc.setTempID(kick_clanId); // 設置臨時ID
                               pc.sendPackets((ServerBasePacket)new S_Message_YN(192, 6008, String.format("是否要將 %s 血盟從同盟中驅逐？", kick_clan.getClanName()))); // 發送確認消息
                               break;
                           }
                           pc.sendPackets((ServerBasePacket)new S_SystemMessage("同盟：要驅逐的血盟不存在。")); // 如果要踢出的血盟不存在，發送系統消息
                           break;
                       }
                       pc.sendPackets((ServerBasePacket)new S_ServerMessage(1233)); // 如果沒有同盟，發送系統消息
                       break;

                   case 10:
                       if (is_war) {
                           pc.sendPackets((ServerBasePacket)new S_SystemMessage("同盟：在攻城戰期間無法解散同盟。")); // 如果處於戰爭狀態，發送系統消息
                       }

         if (clan.getCurrentWar() != null) {
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(1235));

           return;
         }
         if (clan.AllianceSize() > 0) {
           pc.sendPackets((ServerBasePacket)new S_Message_YN(5460, "")); break;
         }
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(1233));
         break;
     }
   }




   private void send_dungeon_remains(L1PcInstance pc, Collection<DungeonTimeProgress<?>> progresses) {
     DungeonTimeInformationLoader loader = DungeonTimeInformationLoader.getInstance();
     for (DungeonTimeProgress<?> progress : progresses) {
       DungeonTimeInformation dtInfo = loader.from_timer_id(progress.get_timer_id());
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(2535, dtInfo.get_description(), String.valueOf(progress.get_remain_seconds() / 60)));
     }
   }


   public String getType() {
     return "[C] C_Rank";
   }
 }


