 package l1j.server.server.clientpackets;

 import MJShiftObject.MJShiftObjectManager;
 import MJShiftObject.Object.MJShiftObject;
 import java.io.IOException;
 import java.io.UnsupportedEncodingException;
 import java.text.SimpleDateFormat;
 import java.util.Calendar;
 import java.util.Random;
 import java.util.logging.Logger;
 import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatLoader;
 import l1j.server.Config;
 import l1j.server.MJBotSystem.Loader.MJBotNameLoader;
 import l1j.server.MJNetServer.Codec.MJNSHandler;
 import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
 import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_AVAILABLE_SPELL_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CUSTOM_MSGBOX;
 import l1j.server.MJTemplate.MJStringAnalyser;
 import l1j.server.server.BadNamesList;
 import l1j.server.server.GameClient;
 import l1j.server.server.IdFactory;
 import l1j.server.server.datatables.CharacterTable;
 import l1j.server.server.datatables.SkillsTable;
 import l1j.server.server.model.Beginner;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_CharCreateStatus;
 import l1j.server.server.serverpackets.S_NewCharPacket;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Skills;

 public class C_CreateChar extends ClientBasePacket {
   private static Logger _log = Logger.getLogger(C_CreateChar.class.getName());
   private static final String C_CREATE_CHAR = "[C] C_CreateChar";

   public C_CreateChar(byte[] abyte0, GameClient client) throws Exception {
     super(abyte0);
     L1PcInstance pc = new L1PcInstance();

     String name = readS();
     int class_type = readC();
     int class_sex = readC();

       Calendar cal = Calendar.getInstance(); // 註解: 獲取當前日期和時間的Calendar實例
       int 時間 = 10; // 註解: 定義時間變量
       int 分鐘 = 12; // 註解: 定義分鐘變量

       String 上午下午 = "下午"; // 註解: 定義上午/下午字符串，初始值為"下午"
       if (cal.get(Calendar.AM_PM) == Calendar.AM) { // 註解: 如果當前時間為上午
           上午下午 = "上午"; // 註解: 將上午/下午字符串設置為"上午"
       }

       if (Config.Login.UseShiftServer) { // 註解: 如果伺服器配置中啟用ShiftServer
           MJShiftObject sobject = MJShiftObjectManager.getInstance().get_shift_sender_object_from_account(client.getAccountName()); // 註解: 根據客戶端賬戶名獲取Shift對象
           if (sobject != null) { // 註解: 如果找到Shift對象
               System.out.println(String.format("【帳號】%s 【參加中】%s 【創建】%s 【%s】【IP】%s 參加對抗戰的賬號嘗試創建角色", new Object[] { client.getAccountName(), sobject.get_source_character_name(), name, MJNSHandler.getLocalTime(), client.getIp() })); // 註解: 輸出賬號、參加角色名、創建角色名、本地時間和IP的日誌
               SC_CUSTOM_MSGBOX.do_kick(client, String.format("由於%s正在參加對抗戰，無法創建角色。", new Object[] { sobject.get_source_character_name() })); // 註解: 向客戶端發送踢出消息，通知其參加對抗戰不能創建角色

               return; // 註解: 返回，停止後續執行
           }
       }

       if (name.length() > 20) { // 註解: 如果角色名長度超過20
           System.out.println("■ 數據包攻擊 ID ■ :" + name); // 註解: 輸出數據包攻擊的ID日誌
           System.out.println("■ 數據包攻擊 IP ■ :" + client.getIp()); // 註解: 輸出數據包攻擊的IP日誌
           client.kick(); // 註解: 踢出客戶端

           return; // 註解: 返回，停止後續執行
       }
            // 創建一個MJStringAnalyser對象，並執行分析
       MJStringAnalyser analyser = MJStringAnalyser.execute(name);
       int i;

            // 遍歷不可創建的職業列表，檢查是否包含當前職業
       for (i = 0; i < Config.CharSettings.NotCreateClass.length; i++) {
           if (Config.CharSettings.NotCreateClass[i] == class_type) {
               // 如果當前職業在不可創建列表中，返回一個狀態23的封包並結束
               S_CharCreateStatus s_charcreatestatus = new S_CharCreateStatus(23);
               client.sendPacket((ServerBasePacket)s_charcreatestatus);
               return;
           }
       }

            // 檢查分析結果中的特殊字符長度和無效字符長度
       if (analyser.get_special_length() > 0 || analyser.get_invalid_length() > 0) {
           // 如果存在特殊字符或無效字符，返回一個狀態9的封包並結束
           S_CharCreateStatus s_charcreatestatus = new S_CharCreateStatus(9);
           client.sendPacket((ServerBasePacket)s_charcreatestatus);
           return;
       }

            // 檢查分析結果中的韓文字符長度和字母字符長度
       if (analyser.get_kor_length() <= 0 && analyser.get_alpha_length() > 5) {
           // 如果沒有韓文字符且字母字符超過5個，返回一個狀態9的封包
           S_CharCreateStatus s_charcreatestatus = new S_CharCreateStatus(9);
           client.sendPacket((ServerBasePacket)s_charcreatestatus);
       }

            // 檢查名稱的長度是否小於等於0
       if (name.length() <= 0) {
           // 如果名稱長度小於等於0，返回一個狀態9的封包並結束
           S_CharCreateStatus s_charcreatestatus = new S_CharCreateStatus(9);
           client.sendPacket((ServerBasePacket)s_charcreatestatus);
           return;
       }
       // 檢查角色名稱是否在禁止名稱列表中
       if (BadNamesList.getInstance().isBadName(name)) {
           S_CharCreateStatus s_charcreatestatus = new S_CharCreateStatus(9);
           client.sendPacket((ServerBasePacket)s_charcreatestatus);
           return;
       }

            // 檢查角色名稱是否已經被其他機器人使用
       if (MJBotNameLoader.isAlreadyName(name)) {
           _log.fine("角色名稱： " + pc.getName() + " 已經存在。創建失敗。");
           S_CharCreateStatus s_charcreatestatus1 = new S_CharCreateStatus(6);
           client.sendPacket((ServerBasePacket)s_charcreatestatus1);
           return;
       }

            // 檢查角色名稱是否無效
       if (isInvalidName(name)) {
           S_CharCreateStatus s_charcreatestatus = new S_CharCreateStatus(9);
           client.sendPacket((ServerBasePacket)s_charcreatestatus);
           return;
       }

            // 檢查角色名稱是否已經存在於角色表中或世界中
       if (CharacterTable.getInstance().isContainNameList(name) || L1World.getInstance().getPlayer(name) != null) {
           _log.fine("角色名稱： " + pc.getName() + " 已經存在。創建失敗。");
           S_CharCreateStatus s_charcreatestatus1 = new S_CharCreateStatus(6);
           client.sendPacket((ServerBasePacket)s_charcreatestatus1);
           return;
       }

            // 檢查帳戶中的角色數量是否已經達到上限
       if (client.getAccount().countCharacters() >= 10) {
           S_CharCreateStatus s_charcreatestatus1 = new S_CharCreateStatus(21);
           client.sendPacket((ServerBasePacket)s_charcreatestatus1);
           return;
       }
       for (i = 0; i < name.length(); i++) { // 註解: 遍歷角色名稱中的每個字符
           // 註解: 如果角色名稱中包含以下任意一個禁止的字符
           if (name.charAt(i) == 'ㄱ' || name.charAt(i) == 'ㄲ' || name.charAt(i) == 'ㄴ' || name.charAt(i) == 'ㄷ' || name
                   .charAt(i) == 'ㄸ' || name.charAt(i) == 'ㄹ' || name.charAt(i) == 'ㅁ' || name.charAt(i) == 'ㅂ' || name
                   .charAt(i) == 'ㅃ' || name.charAt(i) == 'ㅅ' || name.charAt(i) == 'ㅆ' || name.charAt(i) == 'ㅇ' || name
                   .charAt(i) == 'ㅈ' || name.charAt(i) == 'ㅉ' || name.charAt(i) == 'ㅊ' || name.charAt(i) == 'ㅋ' || name
                   .charAt(i) == 'ㅌ' || name.charAt(i) == 'ㅍ' || name.charAt(i) == 'ㅎ' || name.charAt(i) == 'ㅛ' || name
                   .charAt(i) == 'ㅕ' || name.charAt(i) == 'ㅑ' || name.charAt(i) == 'ㅐ' || name.charAt(i) == 'ㅔ' || name
                   .charAt(i) == 'ㅗ' || name.charAt(i) == 'ㅓ' || name.charAt(i) == 'ㅏ' || name.charAt(i) == 'ㅣ' || name
                   .charAt(i) == 'ㅠ' || name.charAt(i) == 'ㅜ' || name.charAt(i) == 'ㅡ' || name.charAt(i) == 'ㅒ' || name
                   .charAt(i) == 'ㅖ' || name.charAt(i) == 'ㅢ' || name.charAt(i) == 'ㅟ' || name.charAt(i) == 'ㅝ' || name
                   .charAt(i) == 'ㅞ' || name.charAt(i) == 'ㅙ' || name.charAt(i) == 'ㅚ' || name.charAt(i) == 'ㅘ' || name
                   .charAt(i) == '씹' || name.charAt(i) == '좃' || name.charAt(i) == '좆' || name.charAt(i) == 'ㅤ') {
               S_CharCreateStatus s_charcreatestatus = new S_CharCreateStatus(9); // 註解: 創建一個新的角色創建狀態封包，狀態碼為9
               client.sendPacket((ServerBasePacket)s_charcreatestatus); // 註解: 向客戶端發送封包通知角色創建失敗

               return; // 註解: 返回，停止後續執行
           }
       }
       // 設置角色名稱
       pc.setName(name);

        // 設置角色類型
       pc.setType(class_type);

        // 設置角色性別
       pc.set_sex(class_sex);

        // 設置角色的基本屬性
       pc.getAbility().setBaseStr((byte)readC()); // 註解: 設置角色的基本力量
       pc.getAbility().setBaseDex((byte)readC()); // 註解: 設置角色的基本敏捷
       pc.getAbility().setBaseCon((byte)readC()); // 註解: 設置角色的基本體質
       pc.getAbility().setBaseWis((byte)readC()); // 註解: 設置角色的基本智慧
       pc.getAbility().setBaseCha((byte)readC()); // 註解: 設置角色的基本魅力
       pc.getAbility().setBaseInt((byte)readC()); // 註解: 設置角色的基本智力

       int statusAmount = pc.getAbility().getAmount(); // 註解: 獲取角色能力點數總數

            // 註解: 檢查角色的基本屬性是否超過20或能力點數總數是否不等於75
       if (pc.getAbility().getBaseStr() > 20 || pc.getAbility().getBaseDex() > 20 || pc.getAbility().getBaseCon() > 20 ||
               pc.getAbility().getBaseWis() > 20 || pc.getAbility().getBaseCha() > 20 ||
               pc.getAbility().getBaseInt() > 20 || statusAmount != 75) {
           // 註解: 記錄錯誤日誌，表示角色屬性值錯誤
           _log.finest("角色屬性值錯誤");
           S_CharCreateStatus s_charcreatestatus3 = new S_CharCreateStatus(21); // 註解: 創建一個新的角色創建狀態封包，狀態碼為21
           client.sendPacket((ServerBasePacket)s_charcreatestatus3); // 註解: 向客戶端發送封包通知角色創建失敗

           return; // 註解: 返回，停止後續執行
       }

            // 註解: 記錄正確創建角色的日誌，包含角色名稱和職業ID
       _log.fine("角色名稱: " + pc.getName() + " 職業ID: " + pc.getClassId());
       S_CharCreateStatus s_charcreatestatus2 = new S_CharCreateStatus(2); // 註解: 創建一個新的角色創建狀態封包，狀態碼為2
       client.sendPacket((ServerBasePacket)s_charcreatestatus2); // 註解: 向客戶端發送封包通知角色創建成功
       initNewChar(client, pc); // 註解: 初始化新角色

            // 註解: 輸出角色創建的控制台日誌，包含角色名稱和創建時間
       System.out.println("角色創建: [" + pc.getName() + "] 時間: [" + 上午下午 + " " + cal.get(Calendar.HOUR) + "時" + cal.get(Calendar.MINUTE) + "分] 創建");

       // 註解: 定義男性角色職業ID列表
       public static final int[] MALE_LIST = new int[] {
               0, 20553, 138, 20278, 2786, 6658, 6671, 20567, 18520, 19296
       };

            // 註解: 定義女性角色職業ID列表
       public static final int[] FEMALE_LIST = new int[] {
               1, 48, 37, 20279, 2796, 6661, 6650, 20577, 18499, 19299
       };

            // 註解: 定義角色初始位置的X座標
       public static final int[][] START_LOC_X = new int[][] {
               Config.CharSettings.START_LOC_X
       };

            // 註解: 定義角色初始位置的Y座標
       public static final int[][] START_LOC_Y = new int[][] {
               Config.CharSettings.START_LOC_Y
       };

            // 註解: 定義地圖ID列表
       public static final short[] MAPID_LIST = new short[] {
               Config.CharSettings.MAPID_LIST, Config.CharSettings.MAPID_LIST,
               Config.CharSettings.MAPID_LIST, Config.CharSettings.MAPID_LIST,
               Config.CharSettings.MAPID_LIST, Config.CharSettings.MAPID_LIST,
               Config.CharSettings.MAPID_LIST, Config.CharSettings.MAPID_LIST,
               Config.CharSettings.MAPID_LIST, Config.CharSettings.MAPID_LIST
       };


       // 註解: 設置初始位置類型為0
       startPosType = 0;
   } else if (pc.isLancer()) { // 註解: 如果角色是黃金槍騎
         init_hp = 16; // 註解: 初始HP設置為16
         switch (pc.getAbility().getBaseWis()) { // 註解: 根據角色的基本智慧值設置初始MP
             case 9:
             case 10:
             case 11:
                 init_mp = 1; // 註解: 基本智慧值為9-11時，初始MP設置為1
                 break;
             case 12:
             case 13:
                 init_mp = 2; // 註解: 基本智慧值為12-13時，初始MP設置為2
                 break;
             default:
                 init_mp = 1; // 註解: 其他情況下，初始MP設置為1
                 break;
         }
         startPosType = 0; // 註解: 設置初始位置類型為0
     }

         // 註解: 使用配置中的初始位置座標設置角色的初始位置
         pc.setX(START_LOC_X[startPosType][startPos]);
         pc.setY(START_LOC_Y[startPosType][startPos]);
         pc.setMap((short) MAPID_LIST[startPosType]);

         // 註解: 設置角色的HP和MP
         pc.addBaseMaxHp((short) (init_hp - pc.getBaseMaxHp()));
         pc.addBaseMaxMp((short) (init_mp - pc.getBaseMaxMp()));

         // 註解: 添加角色到世界中
         L1World.getInstance().storeObject(pc);
         L1World.getInstance().addVisibleObject(pc);

         // 註解: 傳送角色到初始位置
         L1Teleport.teleport(pc, pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), true);
         }

           pc.setId(IdFactory.getInstance().nextId()); // 註解: 設置角色的唯一ID

           if (pc.get_sex() == 0) { // 註解: 如果角色性別為男性
               pc.setClassId(MALE_LIST[pc.getType()]); // 註解: 根據角色類型設置男性職業ID
           } else { // 註解: 如果角色性別為女性
               pc.setClassId(FEMALE_LIST[pc.getType()]); // 註解: 根據角色類型設置女性職業ID
           }

           if (pc.isCrown()) { // 註解: 如果角色是王族
               init_hp = 14; // 註解: 初始HP設置為14
               switch (pc.getAbility().getBaseWis()) { // 註解: 根據角色的基本精神值設置初始MP
                   case 11:
                       init_mp = 2; // 註解: 基本精神值為11時，初始MP設置為2
                       break;
                   case 12:
                   case 13:
                   case 14:
                   case 15:
                       init_mp = 3; // 註解: 基本精神值為12-15時，初始MP設置為3
                       break;
                   case 16:
                   case 17:
                   case 18:
                       init_mp = 4; // 註解: 基本精神值為16-18時，初始MP設置為4
                       break;
                   default:
                       init_mp = 2; // 註解: 其他情況下，初始MP設置為2
                       break;
               }
               startPosType = 0; // 註解: 設置初始位置類型為0
           } else if (pc.isKnight()) { // 註解: 如果角色是騎士
               init_hp = 16; // 註解: 初始HP設置為16
               switch (pc.getAbility().getBaseWis()) { // 註解: 根據角色的基本精神值設置初始MP
                   case 9:
                   case 10:
                   case 11:
                       init_mp = 1; // 註解: 基本精神值為9-11時，初始MP設置為1
                       break;
                   case 12:
                   case 13:
                       init_mp = 2; // 註解: 基本精神值為12-13時，初始MP設置為2
                       break;
                   default:
                       init_mp = 1; // 註解: 其他情況下，初始MP設置為1
                       break;
               }
               startPosType = 0; // 註解: 設置初始位置類型為0
           } else if (pc.isElf()) { // 註解: 如果角色是妖精
               init_hp = 15; // 註解: 初始HP設置為15
               switch (pc.getAbility().getBaseWis()) { // 註解: 根據角色的基本精神值設置初始MP
                   case 12:
                   case 13:
                   case 14:
                   case 15:
                       init_mp = 4; // 註解: 基本精神值為12-15時，初始MP設置為4
                       break;
                   case 16:
                   case 17:
                   case 18:
                       init_mp = 6; // 註解: 基本精神值為16-18時，初始MP設置為6
                       break;
                   default:
                       init_mp = 4; // 註解: 其他情況下，初始MP設置為4
                       break;
               }
               startPosType = 0; // 註解: 設置初始位置類型為0
           }
         startPosType = 0; // 註解: 設置初始位置類型為0
     } else if (pc.isWizard()) { // 註解: 如果角色是法師
         init_hp = 12; // 註解: 初始HP設置為12
         switch (pc.getAbility().getBaseWis()) { // 註解: 根據角色的基本精神值設置初始MP
             case 12:
             case 13:
             case 14:
             case 15:
                 init_mp = 6; // 註解: 基本精神值為12-15時，初始MP設置為6
                 break;
             case 16:
             case 17:
             case 18:
                 init_mp = 8; // 註解: 基本精神值為16-18時，初始MP設置為8
                 break;
             default:
                 init_mp = 6; // 註解: 其他情況下，初始MP設置為6
                 break;
         }
         startPosType = 0; // 註解: 設置初始位置類型為0
     } else if (pc.isDarkelf()) { // 註解: 如果角色是黑暗妖精
         init_hp = 12; // 註解: 初始HP設置為12
         switch (pc.getAbility().getBaseWis()) { // 註解: 根據角色的基本精神值設置初始MP
             case 10:
             case 11:
                 init_mp = 3; // 註解: 基本精神值為10-11時，初始MP設置為3
                 break;
             case 12:
             case 13:
             case 14:
             case 15:
                 init_mp = 4; // 註解: 基本精神值為12-15時，初始MP設置為4
                 break;
             case 16:
             case 17:
             case 18:
                 init_mp = 6; // 註解: 基本精神值為16-18時，初始MP設置為6
                 break;
             default:
                 init_mp = 3; // 註解: 其他情況下，初始MP設置為3
                 break;
         }
         startPosType = 0; // 註解: 設置初始位置類型為0
     } else if (pc.isDragonknight()) { // 註解: 如果角色是龍騎士
         init_hp = 16; // 註解: 初始HP設置為16
         init_mp = 2; // 註解: 初始MP設置為2
         startPosType = 0; // 註解: 設置初始位置類型為0
     } else if (pc.isBlackwizard()) { // 註解: 如果角色是幻術師
         init_hp = 14; // 註解: 初始HP設置為14
         switch (pc.getAbility().getBaseWis()) { // 註解: 根據角色的基本精神值設置初始MP
             case 12:
             case 13:
             case 14:
             case 15:
                 init_mp = 5; // 註解: 基本精神值為12-15時，初始MP設置為5
                 break;
             case 16:
             case 17:
             case 18:
                 init_mp = 6; // 註解: 基本精神值為16-18時，初始MP設置為6
                 break;
             default:
                 init_mp = 5; // 註解: 其他情況下，初始MP設置為5
                 break;
         }
         startPosType = 0; // 註解: 設置初始位置類型為0
     } else if (pc.is戰士()) { // 註解: 如果角色是戰士
         init_hp = 16; // 註解: 初始HP設置為16
         switch (pc.getAbility().getBaseWis()) { // 註解: 根據角色的基本精神值設置初始MP
             case 9:
             case 10:
             case 11:
                 init_mp = 1; // 註解: 基本精神值為9-11時，初始MP設置為1
                 break;
             case 12:
             case 13:
                 init_mp = 2; // 註解: 基本精神值為12-13時，初始MP設置為2
                 break;
             default:
                 init_mp = 1; // 註解: 其他情況下，初始MP設置為1
                 break;
         }
           startPosType = 0; // 註解: 設置初始位置類型為0
       } else if (pc.isFencer()) { // 註解: 如果角色是劍士
           init_hp = 16; // 註解: 初始HP設置為16
           switch (pc.getAbility().getBaseWis()) { // 註解: 根據角色的基本精神值設置初始MP
               case 9:
               case 10:
               case 11:
                   init_mp = 1; // 註解: 基本精神值為9-11時，初始MP設置為1
                   break;
               case 12:
               case 13:
                   init_mp = 2; // 註解: 基本精神值為12-13時，初始MP設置為2
                   break;
               default:
                   init_mp = 1; // 註解: 其他情況下，初始MP設置為1
                   break;
           }
           startPosType = 0; // 註解: 設置初始位置類型為0
       } else if (pc.isLancer()) { // 註解: 如果角色是黃金槍騎
           init_hp = 16; // 註解: 初始HP設置為16
           switch (pc.getAbility().getBaseWis()) { // 註解: 根據角色的基本精神值設置初始MP
               case 9:
               case 10:
               case 11:
                   init_mp = 1; // 註解: 基本精神值為9-11時，初始MP設置為1
                   break;
               case 12:
               case 13:
                   init_mp = 2; // 註解: 基本精神值為12-13時，初始MP設置為2
                   break;
               default:
                   init_mp = 1; // 註解: 其他情況下，初始MP設置為1
                   break;
           }
           startPosType = 0; // 註解: 設置初始位置類型為0
       }

     pc.setX(START_LOC_X[startPosType][startPos]);
     pc.setY(START_LOC_Y[startPosType][startPos]);
     pc.setMap(MAPID_LIST[pc.getType()]);

     pc.setHeading(0);
     pc.setLawful(0);

     pc.addBaseMaxHp(init_hp);
     pc.setCurrentHp(init_hp);
     pc.addBaseMaxMp(init_mp);
     pc.setCurrentMp(init_mp);
     pc.resetBaseAc();
     pc.setTitle(Config.Message.GameServerName);
     pc.setClanid(0);
     pc.setClanRank(0);
     pc.set_food(39);
     pc.setAccessLevel((short)0);
     pc.setGm(false);
     pc.setMonitor(false);
     pc.setGmInvis(false);
     pc.set_exp(0L);
     pc.setHighLevel(1);
     pc.setStatus(0);
     pc.setAccessLevel((short)0);
     pc.setClanname("");
     pc.setClanMemberNotes("");
     pc.setElixirStats(0);
     pc.resetBaseMr();
     pc.setElfAttr(0);
     pc.setGlory_Earth_Attr(0);
     pc.set_PKcount(0);
     pc.set_exp_res(0L);
     pc.setPartnerId(0);
     pc.setOnlineStatus(0);
     pc.setHomeTownId(0);
     pc.setContribution(0);
     pc.setBanned(false);
     pc.setKarma(0);
     pc.setReturnStat(0L);
     pc.setMark_count(60);
     Calendar local_c = Calendar.getInstance();
     SimpleDateFormat local_sdf = new SimpleDateFormat("yyyyMMdd");
     local_c.setTimeInMillis(System.currentTimeMillis());
     pc.setBirthDay(Integer.parseInt(local_sdf.format(local_c.getTime())));
     if (pc.isWizard()) {
       SC_AVAILABLE_SPELL_NOTI noti = SC_AVAILABLE_SPELL_NOTI.newInstance();
       noti.appendNewSpell(4, true);
       pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_AVAILABLE_SPELL_NOTI, true);

       int object_id = pc.getId();
       L1Skills l1skills = SkillsTable.getInstance().getTemplate(4);
       String skill_name = l1skills.getName();
       int skill_id = l1skills.getSkillId();
       SkillsTable.getInstance().spellMastery(object_id, skill_id, skill_name, 0, 0);
     }

     if (pc.isElf()) {
       SC_AVAILABLE_SPELL_NOTI noti = SC_AVAILABLE_SPELL_NOTI.newInstance();
       noti.appendNewSpell(131, true);
       pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_AVAILABLE_SPELL_NOTI, true);

       int object_id = pc.getId();
       L1Skills l1skills = SkillsTable.getInstance().getTemplate(131);
       String skill_name = l1skills.getName();
       int skill_id = l1skills.getSkillId();
       SkillsTable.getInstance().spellMastery(object_id, skill_id, skill_name, 0, 0);
     }
     Beginner.getInstance().GiveItem(pc);
     Beginner.getInstance().writeBookmark(pc);
     pc.setAccountName(client.getAccountName());
     CharacterTable.getInstance().storeNewCharacter(pc);
     S_NewCharPacket s_newcharpacket = new S_NewCharPacket(pc);
     client.sendPacket((ServerBasePacket)s_newcharpacket);
     pc.save();
     pc.refresh();
     AinhasadSpecialStatLoader.getInstance().addSpecialStat(pc.getId(), pc.getName());
   }

   private static boolean isAlphaNumeric(String s) {
     if (s == null) {
       return false;
     }
     boolean flag = true;
     char[] ac = s.toCharArray();
     int i = 0;

     while (i < ac.length) {


       if (!Character.isLetterOrDigit(ac[i])) {
         flag = false;
         break;
       }
       i++;
     }
     return flag;
   }

 // 檢查角色名稱是否無效的函數
 private static boolean isInvalidName(String name) {
         int numOfNameBytes = 0;

         try {
         // 註解: 獲取名稱的字節數（使用EUC-KR編碼）
         numOfNameBytes = (name.getBytes("EUC-KR")).length;
         } catch (UnsupportedEncodingException e) {
         // 註解: 處理不支持的編碼異常
         e.printStackTrace();
         return false;
         }

         // 註解: 如果名稱是字母數字組成，返回false表示名稱有效
         if (isAlphaNumeric(name)) {
         return false;
         }

         // 註解: 其餘代碼的意圖和作用待補充
         }


     if (5 < numOfNameBytes - name.length() || 12 < numOfNameBytes) {
       return false;
     }

     if (BadNamesList.getInstance().isBadName(name)) {
       return false;
     }
     return true;
   }


   public String getType() {
     return "[C] C_CreateChar";
   }
 }


