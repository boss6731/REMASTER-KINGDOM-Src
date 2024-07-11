 package l1j.server.server.clientpackets;

 import java.util.Random;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_NOTI;
 import l1j.server.MJTemplate.MJSimpleRgb;
 import l1j.server.server.GameClient;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1ReturnStatTemp;
 import l1j.server.server.serverpackets.S_HPUpdate;
 import l1j.server.server.serverpackets.S_MPUpdate;
 import l1j.server.server.serverpackets.S_OwnCharAttrDef;
 import l1j.server.server.serverpackets.S_OwnCharStatus;
 import l1j.server.server.serverpackets.S_Paralysis;
 import l1j.server.server.serverpackets.S_ReturnedStat;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.utils.CalcStat;
 import l1j.server.server.utils.CheckInitStat;
 import l1j.server.server.utils.CommonUtil;






 public class C_ReturnStaus
   extends ClientBasePacket
 {
   private static final int SICODE_ENDED = 8;
   private static final int SICODE_ELIXIR_CONTINUE = 14;
   private static final int SICODE_ELIXIR_ENDED = 15;

     public C_ReturnStaus(byte[] decrypt, GameClient client) {
// 調用父類的構造函數
         super(decrypt);
// 讀取請求的類型
         int type = readC();
// 獲取當前玩家實例
         L1PcInstance pc = client.getActiveChar();
// 如果玩家實例為空或返回狀態為 0，則退出
         if (pc == null || pc.getReturnStat() == 0L) {
             return;
         }

// 如果請求類型為 1
         if (type == 1) {
// 創建臨時返回狀態實例
             pc.rst = new L1ReturnStatTemp();
// 保存玩家的現有靈藥數量
             pc.rst.remainElixirAmount = pc.getElixirStats();
// 初始化健康和魔力值
             short init_hp = 0, init_mp = 0;

// 讀取玩家的屬性值
             byte str = (byte)readC();
             byte intel = (byte)readC();
             byte wis = (byte)readC();
             byte dex = (byte)readC();
             byte con = (byte)readC();
             byte cha = (byte)readC();

// 如果屬性值有負數或總和超過75，則踢出玩家
             if (isMinus(pc, str, dex, con, wis, intel, cha) || str + intel + wis + dex + con + cha > 75) {
                 client.kick();
                 try {
                     client.kick();
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
                 System.out.println("▶ 屬性錯誤被踢出: " + pc.getName());
             }

// 這裡可以繼續添加其他邏輯來處理玩家的返回狀態
         }
     }

       pc.rst.basestr = str;
       pc.rst.baseint = intel;
       pc.rst.basewis = wis;
       pc.rst.basedex = dex;
       pc.rst.basecon = con;
       pc.rst.basecha = cha;

       pc.getAbility().init();

       pc.getAbility().setBaseStr(str);
       pc.getAbility().setBaseInt(intel);
       pc.getAbility().setBaseWis(wis);
       pc.getAbility().setBaseDex(dex);
       pc.getAbility().setBaseCon(con);
       pc.getAbility().setBaseCha(cha);

       pc.rst.level = 1;

       if (pc.isCrown()) {
         init_hp = 14;

         switch (pc.rst.basewis) {
           case 11:
             init_mp = 2;
             break;
           case 12:
           case 13:
           case 14:
           case 15:
             init_mp = 3;
             break;
           case 16:
           case 17:
           case 18:
             init_mp = 4;
             break;
           default:
             init_mp = 2;
             break;
         }
       } else if (pc.isKnight()) {
         init_hp = 16;

         switch (pc.rst.basewis) {
           case 9:
           case 10:
           case 11:
             init_mp = 1;
             break;
           case 12:
           case 13:
             init_mp = 2;
             break;
           default:
             init_mp = 1;
             break;
         }
       } else if (pc.isElf()) {
         init_hp = 15;

         switch (pc.rst.basewis) {
           case 12:
           case 13:
           case 14:
           case 15:
             init_mp = 4;
             break;
           case 16:
           case 17:
           case 18:
             init_mp = 6;
             break;
           default:
             init_mp = 4;
             break;
         }
       } else if (pc.isWizard()) {
         init_hp = 12;

         switch (pc.rst.basewis) {
           case 12:
           case 13:
           case 14:
           case 15:
             init_mp = 6;
             break;
           case 16:
           case 17:
           case 18:
             init_mp = 8;
             break;
           default:
             init_mp = 6;
             break;
         }
       } else if (pc.isDarkelf()) {
         init_hp = 12;

         switch (pc.rst.basewis) {
           case 10:
           case 11:
             init_mp = 3;
             break;
           case 12:
           case 13:
           case 14:
           case 15:
             init_mp = 4;
             break;
           case 16:
           case 17:
           case 18:
             init_mp = 6;
             break;
           default:
             init_mp = 3;
             break;
         }
       } else if (pc.isDragonknight()) {
         init_hp = 16;
         init_mp = 2;
       } else if (pc.isBlackwizard()) {
         init_hp = 14;

         switch (pc.rst.basewis) {
           case 10:
           case 11:
           case 12:
           case 13:
           case 14:
           case 15:
             init_mp = 5;
             break;
           case 16:
           case 17:
           case 18:
             init_mp = 6;
             break;
           default:
             init_mp = 5;
             break;
         }
       } else if (pc.is전사()) {
         init_hp = 16;

         switch (pc.rst.basewis) {
           case 9:
           case 10:
           case 11:
             init_mp = 1;
             break;
           case 12:
           case 13:
             init_mp = 2;
             break;
           default:
             init_mp = 1;
             break;
         }
       } else if (pc.isFencer()) {
         init_hp = 16;
         switch (pc.rst.basewis) {
           case 9:
           case 10:
           case 11:
             init_mp = 1;
             break;
           case 12:
           case 13:
             init_mp = 2;
             break;
           default:
             init_mp = 1;
             break;
         }
       } else if (pc.isLancer()) {
         init_hp = 16;
         switch (pc.rst.basewis) {
           case 9:
           case 10:
           case 11:
             init_mp = 1;
             break;
           case 12:
           case 13:
             init_mp = 2;
             break;
           default:
             init_mp = 1;
             break;
         }

       }
       pc.rst.baseHp = init_hp;
       pc.rst.baseMp = init_mp;
       pc.rst.upMp = 0;
       pc.rst.upHp = 0;
       pc.rst.ac = 10;

       if (pc.getHighLevel() == 1) {
         onEndStatus(client, pc);
       } else {
         pc.sendPackets((ServerBasePacket)new S_ReturnedStat(pc, 2), true);
       }
     } else if (type == 2) {
       int levelup = readC();
       try {
         if (pc.rst.level == pc.getHighLevel() && !latestScene(levelup)) {
           return;
         }

         if (pc.rst.level > pc.getHighLevel() || (pc.rst.level == pc.getHighLevel() && !latestScene(levelup)))
         {
           return;

         }
       }
       catch (Exception e) {
         e.printStackTrace();

         return;
       }
       if (pc.rst.level <= 50 && levelup != 0 && levelup != 7 && levelup != 8 && pc

         .getElixirStats() == 0) {

         // 輸出玩家嘗試在等級50以下進行屬性添加操作的訊息
         System.out.println(String.format("%s 玩家嘗試在等級 %d 以下進行屬性添加操作。嘗試等級：%d", new Object[]{pc.getName(), Integer.valueOf(50), Integer.valueOf(pc.rst.level)}));

// 向玩家發送系統訊息，通知其屬性值無效
         pc.sendPackets((ServerBasePacket)new S_SystemMessage("無效的屬性值。"));

// 嘗試踢出玩家
         try {
         client.kick();
         } catch (Exception e) {
         e.printStackTrace();
         }
         return;
       }
       switch (levelup) {
         case 0:
           onLevelUp(pc);
           break;
         case 1:
           pc.rst.str++;
           if (checkingExternal(client, pc, pc.rst.str, "str")) {
             return;
           }
           onLevelUp(pc);
           break;
         case 2:
           pc.rst.Int++;
           if (checkingExternal(client, pc, pc.rst.Int, "Int")) {
             return;
           }
           onLevelUp(pc);
           break;
         case 3:
           pc.rst.wis++;
           if (checkingExternal(client, pc, pc.rst.wis, "wis")) {
             return;
           }
           onLevelUp(pc);
           break;
         case 4:
           pc.rst.dex++;
           if (checkingExternal(client, pc, pc.rst.dex, "dex")) {
             return;
           }
           onLevelUp(pc);
           break;
         case 5:
           pc.rst.con++;
           if (checkingExternal(client, pc, pc.rst.con, "con")) {
             return;
           }
           onLevelUp(pc);
           break;
         case 6:
           pc.rst.cha++;
           if (checkingExternal(client, pc, pc.rst.cha, "cha")) {
             return;
           }
           onLevelUp(pc);
           break;
         case 7:
           if (pc.rst.level > 40) {
             return;
           }
           if (pc.rst.level + 10 < pc.getHighLevel()) {
             for (int m = 0; m < 10; m++)
               statup(pc);
             pc.sendPackets((ServerBasePacket)new S_ReturnedStat(pc, 2), true);
           }
           break;
         case 8:
           onSiEnded(client, pc);
           break;
         case 14:
           onSiElixirContinue(client, pc);
           break;

         case 15:
           onSiElixirEnded(client, pc);
           break;
       }
     }
   }


   private boolean latestScene(int sicode) {
     return (sicode == 8 || sicode == 15);
   }

   private void onSiEnded(GameClient client, L1PcInstance pc) {
     int statusup = readC();
     if (pc.rst.level > 50) {
       switch (statusup) {
         case 1:
           pc.rst.str++;
           if (checkingExternal(client, pc, pc.rst.str, "str")) {
             return;
           }
           break;
         case 2:
           pc.rst.Int++;
           if (checkingExternal(client, pc, pc.rst.Int, "Int")) {
             return;
           }
           break;
         case 3:
           pc.rst.wis++;
           if (checkingExternal(client, pc, pc.rst.wis, "wis")) {
             return;
           }
           break;
         case 4:
           pc.rst.dex++;
           if (checkingExternal(client, pc, pc.rst.dex, "dex")) {
             return;
           }
           break;
         case 5:
           pc.rst.con++;
           if (checkingExternal(client, pc, pc.rst.con, "con")) {
             return;
           }
           break;
         case 6:
           pc.rst.cha++;
           if (checkingExternal(client, pc, pc.rst.cha, "cha")) {
             return;
           }
           break;
       }
     }
     onEndStatus(client, pc);
   }

   private void onSiElixirEnded(GameClient client, L1PcInstance pc) {
     int str = readC();
     int intel = readC();
     int wis = readC();
     int dex = readC();
     int con = readC();
     int cha = readC();
     onElixirLevelup(pc, str, dex, con, intel, wis, cha, false);
     onEndStatus(client, pc);
   }

   private void onSiElixirContinue(GameClient client, L1PcInstance pc) {
     int str = readC();
     int intel = readC();
     int wis = readC();
     int dex = readC();
     int con = readC();
     int cha = readC();
     onElixirLevelup(pc, str, dex, con, intel, wis, cha, true);
   }

   private void onEndStatus(GameClient client, L1PcInstance pc) {
     try {
       pc.sendPackets((ServerBasePacket)new S_ReturnedStat(pc, 3, 0), true);

       pc.set_exp(pc.getReturnStat());
       pc.addBaseMaxHp((short)(pc.rst.baseHp - pc.getBaseMaxHp()));
       pc.addBaseMaxMp((short)(pc.rst.baseMp - pc.getBaseMaxMp()));

       pc.addBaseMaxHp((short)pc.rst.upHp);
       pc.addBaseMaxMp((short)pc.rst.upMp);

       pc.setCurrentHp(pc.getMaxHp());
       pc.setCurrentMp(pc.getMaxHp());

       pc.sendPackets((ServerBasePacket)new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
       pc.sendPackets((ServerBasePacket)new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));

       int locx2 = 32625 + CommonUtil.random(5);
       int locy2 = 32776 + CommonUtil.random(5);
       pc.start_teleport(locx2, locy2, 4, pc.getHeading(), 169, true, false);

       pc.setStatReset(false);
       pc.getAbility().init();

       pc.getAbility().setBaseStr(pc.rst.basestr);
       pc.getAbility().setBaseInt(pc.rst.baseint);
       pc.getAbility().setBaseWis(pc.rst.basewis);
       pc.getAbility().setBaseDex(pc.rst.basedex);
       pc.getAbility().setBaseCon(pc.rst.basecon);
       pc.getAbility().setBaseCha(pc.rst.basecha);

       pc.getAbility().addStr((byte)pc.rst.str);
       pc.getAbility().addInt((byte)pc.rst.Int);
       pc.getAbility().addWis((byte)pc.rst.wis);
       pc.getAbility().addDex((byte)pc.rst.dex);
       pc.getAbility().addCon((byte)pc.rst.con);
       pc.getAbility().addCha((byte)pc.rst.cha);

       pc.resetBaseAc();
       pc.getAC().setAc(pc.getBaseAc());

       pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(pc));
       pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
       pc.RenewStat();

       pc.checkStatus();
       if (!CheckInitStat.CheckPcStat(pc)) {
         client.kick();

         return;
       }

       pc.setReturnStat(0L);
       pc.save();
       pc.rst = null;



       Restar_World(pc);
     } catch (Exception exception) {
       exception.printStackTrace();
     }
   }

   private boolean elixirlevel(int level) {
     switch (level) { case 50: case 52: case 54: case 56: case 58: case 60: case 62: case 64: case 66: case 68: case 70: case 72: case 74: case 76: case 78: case 80: case 82: case 84: case 86: case 88:
       case 90:
       case 92:
       case 94:
       case 96:
       case 98:
       case 100:
         return true; }

     return false;
   }

   private void onLevelUp(L1PcInstance pc) {
     int elixerCount = pc.getElixirStats();



     if (elixirlevel(pc.rst.level + 1))
     {
       if (pc.rst.remainElixirAmount > 0)
       {
         if (pc.rst.level == 99 && elixerCount > 27) {
           pc.sendPackets((ServerBasePacket)new S_ReturnedStat(pc, 76, (elixerCount > 29) ? 3 : ((elixerCount > 28) ? 2 : 1)), true);
         } else if (pc.rst.level == 89 && elixerCount > 21) {
           pc.sendPackets((ServerBasePacket)new S_ReturnedStat(pc, 76, (elixerCount > 21) ? 2 : 1), true);
         } else if (pc.rst.level == 79 && elixerCount > 15) {
           pc.sendPackets((ServerBasePacket)new S_ReturnedStat(pc, 76, (elixerCount > 16) ? 2 : 1), true);
         } else if (pc.rst.level == 49 && elixerCount > 1) {
           pc.sendPackets((ServerBasePacket)new S_ReturnedStat(pc, 76, 1), true);
         } else {
           pc.sendPackets((ServerBasePacket)new S_ReturnedStat(pc, 76, 1), true);
         }
       }
     }

     statup(pc);
     pc.sendPackets((ServerBasePacket)new S_ReturnedStat(pc, 2), true);
   }


   private boolean checking(L1PcInstance pc, int stat) {
     int maximum = 50;
     if (pc.getHighLevel() >= 90) {
       maximum = 55;
     } else if (pc.getHighLevel() >= 100) {
       maximum = 60;
     }
     return (stat > maximum || stat < 1);
   }

   private boolean checkingExternal(GameClient clnt, L1PcInstance pc, int stat, String name) {
    // 檢查玩家的屬性值是否超過限制
         if (checking(pc, stat)) {
             // 打印檢測到超過屬性值的訊息
         System.out.print(String.format("%s 檢測到屬性值超過限制.. %s : %d", new Object[] { pc.getName(), name, Integer.valueOf(stat) }));
         try {
             // 嘗試踢出玩家
         clnt.kick();
         } catch (Exception e) {
             // 捕獲並打印異常資訊
         e.printStackTrace();
         }
         return true;
         }
         return false;
 }

   private static boolean isMinus(L1PcInstance pc, int str, int dex, int con, int wis, int intel, int cha) {
     return (str <= 0 || dex <= 0 || con <= 0 || wis <= 0 || intel <= 0 || cha <= 0 || str < pc.rst.str + pc.rst.basestr || dex < pc.rst.dex + pc.rst.basedex || con < pc.rst.con + pc.rst.basecon || wis < pc.rst.wis + pc.rst.basewis || intel < pc.rst.Int + pc.rst.baseint || cha < pc.rst.cha + pc.rst.basecha);
   }

   private static int remainStat(L1PcInstance pc, int str, int dex, int con, int wis, int intel, int cha) {
     return str - pc.rst.str + pc.rst.basestr + dex - pc.rst.dex + pc.rst.basedex + con - pc.rst.con + pc.rst.basecon + wis - pc.rst.wis + pc.rst.basewis + intel - pc.rst.Int + pc.rst.baseint + cha - pc.rst.cha + pc.rst.basecha;
   }

   private void safeKick(GameClient clnt, String message) {
     try {
       clnt.kick();
       clnt.close();
     } catch (Exception e) {
       e.printStackTrace();
     }
     System.out.println(message);
   }



   private void onElixirLevelup(L1PcInstance pc, int str, int dex, int con, int intel, int wis, int cha, boolean next) {

     try {


         // 檢查屬性值是否有負數

         if (isMinus(pc, str, dex, con, wis, intel, cha)) {

             // 如果有負數，踢出玩家並輸出訊息

         safeKick(pc.getNetConnection(), "▶ 屬性值錯誤被踢出: " + pc.getName());

         return;

         }



         // 檢查剩餘的靈藥數量是否小於等於0

         if (pc.rst.remainElixirAmount <= 0) {

             // 如果數量不正確，踢出玩家並輸出訊息

         safeKick(pc.getNetConnection(), "▶ 靈藥數量錯誤被踢出: " + pc.getName());

         return;

         }



         // 檢查靈藥等級是否有效

         if (!elixirlevel(pc.rst.level)) {

             // 如果等級不正確，踢出玩家並輸出訊息

         safeKick(pc.getNetConnection(), "▶ 靈藥等級錯誤被踢出: " + pc.getName() + ", 等級: " + pc.rst.level);

         return;

         }



         // 檢查更新後的屬性總和是否超過5

         if (remainStat(pc, str, dex, con, wis, intel, cha) > 5) {

             // 如果超過限制，踢出玩家並輸出訊息，包含舊的和新的屬性值

         safeKick(pc.getNetConnection(), String.format("▶ 屬性值錯誤被踢出: %s\r\n舊屬性值: %d %d %d %d %d %d\r\n新屬性值: %d %d %d %d %d %d",

         new Object[] {

                 pc.getName(),

         Integer.valueOf(pc.rst.str + pc.rst.basestr), Integer.valueOf(pc.rst.dex + pc.rst.basedex), Integer.valueOf(pc.rst.con + pc.rst.basecon),

         Integer.valueOf(pc.rst.wis + pc.rst.basewis), Integer.valueOf(pc.rst.Int + pc.rst.baseint), Integer.valueOf(pc.rst.cha + pc.rst.basecha),

         Integer.valueOf(str), Integer.valueOf(dex), Integer.valueOf(con), Integer.valueOf(wis), Integer.valueOf(intel), Integer.valueOf(cha) }));

         return;
         }

// 這裡可以繼續添加其他邏輯處理靈藥升級
         } catch (Exception e) {
         e.printStackTrace();
         }
         }

       pc.rst.remainElixirAmount--;
       pc.rst.str = str - pc.rst.basestr;
       pc.rst.dex = dex - pc.rst.basedex;
       pc.rst.con = con - pc.rst.basecon;
       pc.rst.wis = wis - pc.rst.basewis;
       pc.rst.Int = intel - pc.rst.baseint;
       pc.rst.cha = cha - pc.rst.basecha;
       statup(pc);
       if (next) {
         pc.sendPackets((ServerBasePacket)new S_ReturnedStat(pc, 2), true);
       }
     } catch (Exception e) {
       e.printStackTrace();
     }
   }

   public void statup(L1PcInstance pc) {
     Random random = new Random();
     pc.rst.level++;

     int minmp = CalcStat.MinincreaseMp(pc.getType(), (byte)(pc.rst.wis + pc.rst.basewis));
     int maxmp = CalcStat.MaxincreaseMp(pc.getType(), (byte)(pc.rst.wis + pc.rst.basewis));
     short randomHp = (short)(CalcStat.PureHp(pc.getType(), (byte)(pc.rst.con + pc.rst.basecon)) + random.nextInt(2));
     int randomMp = (int)(Math.random() * (maxmp - minmp) + minmp);
     if (minmp == 0) {
       randomMp = random.nextInt(maxmp + 1);
     }
     pc.rst.upHp += randomHp;
     pc.rst.upMp += randomMp;
   }

   private static void Restar_World(final L1PcInstance pc) {
    // 發送通知給玩家，告知其當前是否在水下地圖
         pc.sendPackets(SC_WORLD_PUT_NOTI.make_stream(pc, pc.getMap().isUnderwater(), false));

         // 發送通知訊息給玩家，告知其屬性將會被應用
         pc.sendPackets(SC_NOTIFICATION_MESSAGE.make_stream("\f3請稍候，屬性將會被應用。", MJSimpleRgb.green(), 5));
         pc.sendPackets("\f3請稍候，屬性將會被應用。");

         // 發送癱瘓狀態封包給玩家，使其無法動彈
         pc.sendPackets((ServerBasePacket)new S_Paralysis(11, true));

         // 使用一般線程池安排一個新的任務，延遲1500毫秒後執行
         GeneralThreadPool.getInstance().schedule(new Runnable() {
             public void run() {
                 // 獲取玩家的網絡連接和名稱
         GameClient clnt = pc.getNetConnection();
         String name = pc.getName();
         // 獲取玩家當前的位置和地圖ID
         int x = pc.getX();
         int y = pc.getY();
         int mapId = pc.getMapId();
         // 重啟角色選擇過程
         C_NewCharSelect.restartProcess(pc);
         try {
             // 暫停700毫秒
         Thread.sleep(700L);
         // 再次發送癱瘓狀態封包給玩家
         pc.sendPackets((ServerBasePacket)new S_Paralysis(11, true));
         // 讓玩家重新進入世界
         C_LoginToServer.doEnterWorld(name, clnt, false, x, y, mapId);
         } catch (Exception e) {
             // 捕獲並打印異常信息
         e.printStackTrace();
         }
             }
             }, 1500L);
 }


