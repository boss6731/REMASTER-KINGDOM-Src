 package l1j.server.server.clientpackets;

 import java.io.IOException;
 import l1j.server.server.GameClient;
 import l1j.server.server.datatables.CharacterSlotItemTable;
 import l1j.server.server.datatables.ClanBuffTable;
 import l1j.server.server.datatables.ClanTable;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.skill.L1SkillUse;
 import l1j.server.server.serverpackets.S_ACTION_UI2;
 import l1j.server.server.serverpackets.S_CharStat;
 import l1j.server.server.serverpackets.S_EinhasadClanBuff;
 import l1j.server.server.serverpackets.S_Paralysis;
 import l1j.server.server.serverpackets.S_Pledge;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SlotChange;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;




 public class C_Craft
   extends ClientBasePacket
 {
   private static final int NewStat = 228;
   private static final int CLAN_BUFF = 140;
   private static final int 장비 = 33;
   private static final int CLAN_BUFF_CHANGE = 249;
   private static final int CLAN_BUFF_CHOICE = 248;

   public C_Craft(byte[] data, GameClient client) throws IOException {
     super(data); L1Clan clan; int _type, buffnum, _slot, BuffCheck; L1Clan l1Clan1; boolean isStr; ClanBuffTable.ClanBuff Buff; int buffId; boolean isInt; int consume; boolean isWis; int time; boolean isDex, isCon, isCha; int totallength, Classtype, value, i;
     if (client == null) {
       return;
     }
     L1PcInstance pc = client.getActiveChar();
     int type = readC();

     if (type != 228 && pc == null) {
       return;
     }


     switch (type) {

       case 249:
         clan = pc.getClan();
         if (clan == null || !pc.getInventory().checkItem(40308, 300000))
           return;
         pc.getInventory().consumeItem(40308, 300000);
         clan.setEinhasadBlessBuff(0);
         clan.setBuffFirst(ClanBuffTable.getRandomBuff(clan));
         clan.setBuffSecond(ClanBuffTable.getRandomBuff(clan));
         clan.setBuffThird(ClanBuffTable.getRandomBuff(clan));
         pc.sendPackets((ServerBasePacket)new S_EinhasadClanBuff(pc), true);
         ClanTable.getInstance().updateClan(clan);
         break;

       case 248:
         readH();
         readD();
         clan = pc.getClan();
         if (clan == null)
           return;
         buffnum = readBit();
         readC();

         BuffCheck = readC();
         switch (BuffCheck) {
           case 1:
             clan.setEinhasadBlessBuff(buffnum);
             pc.sendPackets((ServerBasePacket)new S_EinhasadClanBuff(pc), true);
             break;
             case 2: // 註解: 處理操作碼為2的情況
                 if (!pc.getMap().isEscapable()) { // 註解: 如果玩家所在的地圖不可逃脫
                     pc.sendPackets((ServerBasePacket)new S_SystemMessage("無法在此地使用。"), true); // 註解: 向玩家發送消息，通知無法在此地使用
                     pc.sendPackets((ServerBasePacket)new S_Paralysis(7, false)); // 註解: 向玩家發送癱瘓狀態封包
                     return; // 註解: 返回，停止後續執行
                 }
             Buff = ClanBuffTable.getBuffList(buffnum);
             if (Buff == null || !pc.getInventory().checkItem(40308, 1000))
               return;
             pc.getInventory().consumeItem(40308, 1000);
             pc.sendPackets((ServerBasePacket)new S_Paralysis(7, false));
             pc.start_teleport(Buff.teleportX, Buff.teleportY, (short)Buff.teleportM, pc.getHeading(), 18339, true, false);
             break;
           case 3:
             if (!pc.getInventory().checkItem(40308, 500000))
               return;
             pc.getInventory().consumeItem(40308, 500000);
             clan.setEinhasadBlessBuff(buffnum);
             pc.sendPackets((ServerBasePacket)new S_EinhasadClanBuff(pc), true);
             break;
         }
         ClanTable.getInstance().updateClan(clan);
         break;

         case 33: // 註解: 處理操作碼為33的情況
             readH(); // 註解: 讀取兩個字節的值（可能無用）
             readC(); // 註解: 讀取一個字節的值（可能無用）
             _type = readC(); // 註解: 讀取一個字節的值，表示類型
             _slot = readC(); // 註解: 讀取一個字節的值，表示槽位
             readC(); // 註解: 讀取一個字節的值（可能無用）
             if (_slot >= 0 && _slot <= 3) { // 註解: 如果槽位在有效範圍內
                 if (_type == 16) { // 註解: 如果類型為16
                     pc.sendPackets((ServerBasePacket)new S_SlotChange(32, _slot)); // 註解: 向玩家發送槽位變更封包
                     pc.setSlotNumber(_slot); // 註解: 設置玩家的槽位編號
                     pc.getChangeSlot(_slot); // 註解: 獲取玩家變更的槽位，跳出循環
                     break;
                 }
                 if (_type == 8) { // 註解: 如果類型為8
                     int _save = readC(); // 註解: 讀取一個字節的值，表示保存標誌
                     readC(); // 註解: 讀取一個字節的值（可能無用）
                     if (_save == 0) { // 註解: 如果保存標誌為0
                         pc.addSlotItem(_slot, 0, true); // 註解: 添加槽位物品
                         break;
                     }
                     if (_save == 1) { // 註解: 如果保存標誌為1
                         int Namelength = readC(); // 註解: 讀取一個字節的值，表示名稱長度
                         String Name = readS(Namelength); // 註解: 讀取名稱字符串
                         int color = readC(); // 註解: 讀取一個字節的值，表示顏色
                         long l = System.currentTimeMillis(); // 註解: 獲取當前時間戳
                         if (color < 0 || color > 5) { // 註解: 如果顏色不在有效範圍內
                             System.out.println(String.format("%s 懷疑中繼器用戶（交換相關）。", new Object[] { pc.getName() })); // 註解: 輸出懷疑中繼器用戶的消息
                             return;
                         }
                         if ((Name.getBytes()).length < 0 || (Name.getBytes()).length > 20) { // 註解: 如果名稱長度不在有效範圍內
                             System.out.println(String.format("%s 懷疑中繼器用戶（交換相關）。", new Object[] { pc.getName() })); // 註解: 輸出懷疑中繼器用戶的消息
                             return;
                         }
                         if (pc.get_slotsavetime() > l) { // 註解: 如果上次保存時間大於當前時間
                             System.out.println(String.format("%s 懷疑中繼器用戶（交換相關）。", new Object[] { pc.getName() })); // 註解: 輸出懷疑中繼器用戶的消息
                             return;
                         }
                         pc.addslotsetting(_slot, color, Name); // 註解: 添加槽位設置
                         CharacterSlotItemTable.getInstance().update_CharSlotcolor(pc, _slot, pc.get_slot_info(_slot)); // 註解: 更新角色槽位顏色
                         pc.set_slotsavetime(l + 1000L); // 註解: 設置下次保存時間
                     }
                 }
             }
         break;
       case 140:
         readH();
         readH();
         l1Clan1 = pc.getClan();
         buffId = read4(read_size()) - 2724;

         consume = 300000000;
         time = 172800;
         if (l1Clan1.getBuffTime()[buffId] != 0) {
           consume = 10000000;
           time = l1Clan1.getBuffTime()[buffId];
         }
         if (!pc.isGm() && !pc.isCrown() && pc.getClanRank() != 9 && pc.getClanRank() != 3) {
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(4648));
           return;
         }
         if (pc.isGm() || l1Clan1.getBlessCount() >= consume) {
           int oldbless = l1Clan1.getBless();
           l1Clan1.setBless(buffId + 1);
           l1Clan1.setBuffTime(buffId, time);
           int[] times = l1Clan1.getBuffTime();
           ClanTable.getInstance().updateBless(l1Clan1.getClanId(), buffId + 1);
           ClanTable.getInstance().updateBuffTime(times[0], times[1], times[2], times[3], l1Clan1.getClanId());
           if (!pc.isGm()) {
             l1Clan1.setBlessCount(l1Clan1.getBlessCount() - consume);
             ClanTable.getInstance().updateBlessCount(l1Clan1.getClanId(), l1Clan1.getBlessCount());
           }
           for (L1PcInstance member : l1Clan1.getOnlineClanMember()) {
             if (oldbless != 0 && member.hasSkillEffect(504 + oldbless)) {
               member.removeSkillEffect(504 + oldbless);
               member.sendPackets((ServerBasePacket)new S_ACTION_UI2(2723 + oldbless, 1, 0, 7231 + oldbless * 2, 0));
             }
             member.sendPackets((ServerBasePacket)new S_Pledge(l1Clan1, buffId + 1));
             (new L1SkillUse()).handleCommands(member, buffId + 505, member.getId(), member.getX(), member.getY(), null, time, 4);
           }  break;
         }
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(4620));
         break;

       case 228:
         isStr = false;
         isInt = false;
         isWis = false;
         isDex = false;
         isCon = false;
         isCha = false;
         readC();
         totallength = readH();
         readH();
         readC();
         Classtype = readC();
         if (pc != null) {
           Classtype = pc.getType();
         }
         readC();
         value = readC();
         for (i = 0; i < (totallength - 6) / 2; i++) {
           boolean check; int charstat = readC();
           if (charstat == 0 || charstat % 8 != 0) {
             break;
           }
           int stat = readC();

           switch (charstat) {
             case 48:
               check = false;
               try {
                 if (pc != null &&
                   value == 16 &&
                   stat == pc.getAbility().getTotalStr()) {
                   client.charStat[0] = stat;
                   check = true;
                 }


                 if (!check) {
                   client.charStat[0] = stat;
                   isStr = true;
                 }
               } catch (Exception e) {
                 e.printStackTrace();
               }
               break;
             case 56:
               client.charStat[1] = stat;
               isInt = true;
               break;
             case 64:
               client.charStat[2] = stat;
               isWis = true;
               break;
             case 72:
               client.charStat[3] = stat;
               isDex = true;
               break;
             case 80:
               if (value == 16 && stat == pc.getAbility().getTotalCon()) {
                 client.charStat[4] = stat; break;
               }
               client.charStat[4] = stat;
               isCon = true;
               break;

             case 88:
               client.charStat[5] = stat;
               isCha = true;
               break;
           }
         }
         if (value == 16 && !isStr && !isInt && !isWis && !isDex && !isCon && !isCha) {
           if (!isStr)
             isStr = true;
           if (!isCon)
             isCon = true;
         }
         if (isStr) {
           client.sendPacket((ServerBasePacket)new S_CharStat(client, 1, Classtype, value, client.charStat[0], client.charStat[1], client.charStat[2], client.charStat[3], client.charStat[4], client.charStat[5]));
           isStr = false;
         }
         if (isInt) {
           client.sendPacket((ServerBasePacket)new S_CharStat(client, 2, Classtype, value, client.charStat[0], client.charStat[1], client.charStat[2], client.charStat[3], client.charStat[4], client.charStat[5]));
           isInt = false;
         }
         if (isWis) {
           client.sendPacket((ServerBasePacket)new S_CharStat(client, 3, Classtype, value, client.charStat[0], client.charStat[1], client.charStat[2], client.charStat[3], client.charStat[4], client.charStat[5]));
           isWis = false;
         }
         if (isDex) {
           client.sendPacket((ServerBasePacket)new S_CharStat(client, 4, Classtype, value, client.charStat[0], client.charStat[1], client.charStat[2], client.charStat[3], client.charStat[4], client.charStat[5]));
           isDex = false;
         }
         if (isCon) {
           client.sendPacket((ServerBasePacket)new S_CharStat(client, 5, Classtype, value, client.charStat[0], client.charStat[1], client.charStat[2], client.charStat[3], client.charStat[4], client.charStat[5]));
           isCon = false;
         }
         if (isCha) {
           client.sendPacket((ServerBasePacket)new S_CharStat(client, 6, Classtype, value, client.charStat[0], client.charStat[1], client.charStat[2], client.charStat[3], client.charStat[4], client.charStat[5]));
           isCha = false;
         }
         break;
     }
   }



   public String getType() {
     return "[C] C_Craft";
   }
 }


