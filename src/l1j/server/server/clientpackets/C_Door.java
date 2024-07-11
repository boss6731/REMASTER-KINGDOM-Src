 package l1j.server.server.clientpackets;

 import java.util.ArrayList;
 import java.util.Random;
 import l1j.server.Config;
 import l1j.server.MJTemplate.TreasureChest.MJL1TreasureChest;
 import l1j.server.server.GameClient;
 import l1j.server.server.datatables.HouseTable;
 import l1j.server.server.model.Instance.L1DoorInstance;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1House;






 public class C_Door
   extends ClientBasePacket
 {
   private static final String C_DOOR = "[C] C_Door";
   private static Random _random = new Random(System.nanoTime());

   public C_Door(byte[] abyte0, GameClient client) throws Exception {
     super(abyte0);
     int locX = readH();
     int locY = readH();
     int objectId = readD();

     L1PcInstance pc = client.getActiveChar();
     if (pc == null) {
       return;
     }


     L1DoorInstance door = null;
     L1Object obj = L1World.getInstance().findObject(objectId);
     if (obj == null)
       return;
     if (obj.instanceOf(128)) {
       ((MJL1TreasureChest)obj).open(pc); return;
     }
     if (obj instanceof L1DoorInstance) {
       door = (L1DoorInstance)obj;
     }



     if (L1World.getInstance().findObject(objectId) == null || !(L1World.getInstance().findObject(objectId) instanceof L1DoorInstance)) {
       return;
     }

     if (locX > pc.getX() + 1 || locX < pc.getX() - 1 || locY > pc.getY() + 1 || locY < pc.getY() - 1) {
       return;
     }

     if (door.getNpcId() >= 5147 && door.getNpcId() <= 5151) {
       return;
     }

     if (door.getDoorId() >= 120825 && door.getDoorId() <= 120830) {
       return;
     }

     if (door != null && !isExistKeeper(pc, door.getKeeperId())) {
       if (door.getNpcId() >= 780220 && door.getNpcId() <= 7800223) {
         return;
       }

       if (door.getNpcId() >= 7800316 && door.getNpcId() <= 7800319) {
         if (door.getOpenStatus() == 28) {
           door.close();
         } else {
           return;
         }
       }
       if (door.getNpcId() >= 7800240 && door.getNpcId() <= 7800243) {
         if (door.getOpenStatus() == 29 &&
           pc.getInventory().checkItem(14000006, 1)) {
           pc.getInventory().consumeItem(14000006, 1);
           door.open();
           Luun_Secret_Box(pc, door);
         }

         return;
       }
       if (door.getNpcId() == 7800245 || door.getNpcId() == 7800246) {
         if (door.getOpenStatus() == 29) {
           door.open();
           WhaleBox(pc, door);
         }
         return;
       }
       if (door.getDoorId() == 113) {
         if (pc.getInventory().checkItem(40163)) {
           pc.getInventory().consumeItem(40163, 1);
         } else {
           return;
         }
       }









       if (door.getDoorId() >= 4100 && door.getDoorId() <= 4111) {
         if (pc.getInventory().checkItem(40313, 1)) {
           pc.getInventory().consumeItem(40313, 1);
           pc.바포방 = true;
         } else {
           pc.바포방 = false;
           return;
         }
       }
       if (door.getDoorId() >= 8001 && door.getDoorId() <= 8010) {
         if (pc.getInventory().checkItem(430026, 1)) {
           giranCaveBox(pc, door);

           return;
         }
         return;
       }
       if (door.getDoorId() >= 900151 && door.getDoorId() <= 900154) {
         return;
       }


       if (door.getNpcId() >= 7210013 && door.getNpcId() <= 7210015) {
         return;
       }
       if (door.getOpenStatus() == 28) {
         if (door.getDoorId() < 7210016 || door.getDoorId() > 7210019) {
           door.close();
         }
       } else if (door.getOpenStatus() == 29) {
         door.open();
         if (door.getDoorId() >= 7210016 && door.getDoorId() <= 7210019) {
           phoenixEgg(pc, door);
         }
       }
     }
   }






   private void WhaleBox(L1PcInstance pc, L1DoorInstance door) {
     int[] items = Config.TreasureBox.EVA_BOX_ITEMS;
     ArrayList<Integer> itemList = new ArrayList<>(); int arrayOfInt1[], j; byte b1;
     for (arrayOfInt1 = items, j = arrayOfInt1.length, b1 = 0; b1 < j; ) { Integer temp = Integer.valueOf(arrayOfInt1[b1]);
       itemList.add(temp); b1++; }

     if (itemList.size() == 0 || itemList.isEmpty()) {
       return;
     }
     int[] counts = Config.TreasureBox.EVA_BOX_COUNTS;
     ArrayList<Integer> countsList = new ArrayList<>(); int arrayOfInt2[], k; byte b2;
     for (arrayOfInt2 = counts, k = arrayOfInt2.length, b2 = 0; b2 < k; ) { Integer temp = Integer.valueOf(arrayOfInt2[b2]);
       countsList.add(temp); b2++; }

            // 處理玩家打開伊娃王國寶箱的邏輯
       if (itemList.size() != countsList.size()) {
           System.out.println("伊娃王國寶箱物品數量錯誤");
           return;
       }

        // 獲取配置中的物品機率清單
       int[] proc = Config.TreasureBox.EVA_BOX_PRO;
       ArrayList<Integer> proList = new ArrayList<>();
       int arrayOfInt3[], m;
       byte b3;

            // 將機率添加到 proList 中
       for (arrayOfInt3 = proc, m = arrayOfInt3.length, b3 = 0; b3 < m; ) {
           Integer temp = Integer.valueOf(arrayOfInt3[b3]);
           proList.add(temp);
           b3++;
       }

            // 檢查 itemList 和 proList 的大小是否相等
       if (itemList.size() != proList.size()) {
           System.out.println("伊娃王國寶箱物品機率錯誤"); // 機率錯誤，輸出錯誤信息
           return;
       }

            // 計算機率總和
       int SumTotal = 0;
       for (int i = 0; i < proList.size(); i++) {
           SumTotal += ((Integer)proList.get(i)).intValue();
       }

            // 檢查機率總和是否為零
       if (SumTotal == 0) {
           System.out.println("伊娃王國寶箱錯誤"); // 機率總和為零，輸出錯誤信息
           return;
       }

            // 根據隨機數從物品列表中選擇並獲得物品的邏輯
       int ran = _random.nextInt(SumTotal) + 1; // 生成1到SumTotal之間的隨機數
       int TempProc = 0;

       for (int n = 0; n < itemList.size(); n++) {
           TempProc += ((Integer)proList.get(n)).intValue(); // 累加機率值

                // 如果隨機數小於累加的機率值
           if (ran < TempProc) {
               L1ItemInstance item = pc.getInventory().storeItem(
                       ((Integer)itemList.get(n)).intValue(),
                       ((Integer)countsList.get(n)).intValue(),
                       true
               ); // 獲得對應的物品

                    // 判斷物品數量是否為1
               if (((Integer)countsList.get(n)).intValue() == 1) {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage(item.getName() + " 獲得了。"));
                   break; // 顯示獲得單個物品的消息，並結束迴圈
               }

                // 顯示獲得多個物品的消息
               pc.sendPackets((ServerBasePacket)new S_SystemMessage(item.getName() + "(" + countsList.get(n) + ") 獲得了。"));
               break; // 結束迴圈
           }
       }









     // 處理玩家打開祕密寶箱的邏輯
     private void Luun_Secret_Box(L1PcInstance pc, L1DoorInstance door) {
         // 獲取配置中的物品清單
         int[] items = Config.TreasureBox.LUUN_SECRET_BOX_ITEMS;
         ArrayList<Integer> itemList = new ArrayList<>();
         int arrayOfInt1[], j;
         byte b1;

         // 將物品添加到 itemList 中
         for (arrayOfInt1 = items, j = arrayOfInt1.length, b1 = 0; b1 < j; ) {
             Integer temp = Integer.valueOf(arrayOfInt1[b1]);
             itemList.add(temp);
             b1++;
         }

         // 檢查 itemList 是否為空
         if (itemList.size() == 0 || itemList.isEmpty()) {
             return; // 如果為空，則返回
         }

         // 獲取配置中的物品數量清單
         int[] counts = Config.TreasureBox.LUUN_SECRET_BOX_COUNTS;
         ArrayList<Integer> countsList = new ArrayList<>();
         int arrayOfInt2[], k;
         byte b2;

         // 將數量添加到 countsList 中
         for (arrayOfInt2 = counts, k = arrayOfInt2.length, b2 = 0; b2 < k; ) {
             Integer temp = Integer.valueOf(arrayOfInt2[b2]);
             countsList.add(temp);
             b2++;
         }

         // 檢查 itemList 和 countsList 的大小是否相等
         if (itemList.size() != countsList.size()) {
             System.out.println("祕密寶箱物品數量錯誤"); // 數量錯誤，輸出錯誤信息
             return;
         }

         // 獲取配置中的物品機率清單
         int[] proc = Config.TreasureBox.LUUN_SECRET_BOX_PRO;
         ArrayList<Integer> proList = new ArrayList<>();
         int arrayOfInt3[], m;
         byte b3;

         // 將機率添加到 proList 中
         for (arrayOfInt3 = proc, m = arrayOfInt3.length, b3 = 0; b3 < m; ) {
             Integer temp = Integer.valueOf(arrayOfInt3[b3]);
             proList.add(temp);
             b3++;
         }

         // 檢查 itemList 和 proList 的大小是否相等
         if (itemList.size() != proList.size()) {
             System.out.println("祕密寶箱物品機率錯誤"); // 機率錯誤，輸出錯誤信息
             return;
         }

         // 計算機率總和
         int SumTotal = 0;
         for (int i = 0; i < proList.size(); i++) {
             SumTotal += ((Integer)proList.get(i)).intValue();
         }

         // 檢查機率總和是否為零
         if (SumTotal == 0) {
             System.out.println("祕密寶箱錯誤"); // 機率總和為零，輸出錯誤信息
             return;
         }

         // 根據隨機數從物品列表中選擇並獲得物品的邏輯
         int ran = _random.nextInt(SumTotal) + 1; // 生成1到SumTotal之間的隨機數
         int TempProc = 0;

         for (int n = 0; n < itemList.size(); n++) {
             TempProc += ((Integer)proList.get(n)).intValue(); // 累加機率值

             // 如果隨機數小於累加的機率值
             if (ran < TempProc) {
                 L1ItemInstance item = pc.getInventory().storeItem(
                         ((Integer)itemList.get(n)).intValue(),
                         ((Integer)countsList.get(n)).intValue(),
                         true
                 ); // 獲得對應的物品

                 // 判斷物品數量是否為1
                 if (((Integer)countsList.get(n)).intValue() == 1) {
                     pc.sendPackets((ServerBasePacket)new S_SystemMessage(item.getName() + " 獲得了。"));
                     break; // 顯示獲得單個物品的消息，並結束迴圈
                 }

                 // 顯示獲得多個物品的消息
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage(item.getName() + "(" + countsList.get(n) + ") 獲得了。"));
                 break; // 結束迴圈
             }
         }
     }




     // 處理角色在奇岩洞穴中打開寶箱的邏輯
     private void giranCaveBox(L1PcInstance pc, L1DoorInstance door) {
         int ran = _random.nextInt(100) + 1; // 生成1到100之間的隨機數
         if (door.getOpenStatus() == 29) {
             pc.getInventory().consumeItem(430026, 1); // 消耗一個鑰匙道具
             door.open(); // 打開門

                    // 根據隨機數決定獎勵
             if (ran >= 0 && ran <= 15) {
                 pc.getInventory().storeItem(40308, 10000); // 獲得 10000 金幣
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("獲得了 10000 金幣。"));
             } else if (ran >= 16 && ran <= 29) {
                 pc.getInventory().storeItem(40308, 20000); // 獲得 20000 金幣
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("獲得了 20000 金幣。"));
             } else if (ran >= 30 && ran <= 49) {
                 pc.getInventory().storeItem(40308, 30000); // 獲得 30000 金幣
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("獲得了 30000 金幣。"));
             } else if (ran >= 50 && ran <= 59) {
                 pc.getInventory().storeItem(40308, 50000); // 獲得 50000 金幣
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("獲得了 50000 金幣。"));
             } else if (ran >= 60 && ran <= 64) {
                 pc.getInventory().storeItem(40308, 100000); // 獲得 100000 金幣
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("獲得了 100000 金幣。"));
             } else if (ran >= 65 && ran <= 69) {
                 pc.getInventory().storeItem(40308, 200000); // 獲得 200000 金幣
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("獲得了 200000 金幣。"));
             } else if (ran >= 70 && ran <= 72) {
                 pc.getInventory().storeItem(40308, 300000); // 獲得 300000 金幣
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("獲得了 300000 金幣。"));
             } else if (ran >= 73 && ran <= 75) {
                 pc.getInventory().storeItem(40308, 400000); // 獲得 400000 金幣
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("獲得了 400000 金幣。"));
             } else if (ran >= 76 && ran <= 78) {
                 pc.getInventory().storeItem(40308, 500000); // 獲得 500000 金幣
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("獲得了 500000 金幣。"));
             } else if (ran >= 79 && ran <= 80) {
                 pc.getInventory().storeItem(40308, 1000000); // 獲得 1000000 金幣
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("獲得了 1000000 金幣。"));
             } else if (ran >= 81 && ran <= 90) {
                 pc.getInventory().storeItem(40074, 3); // 獲得 3 張防具魔法卷軸
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("獲得了 3 張 對盔甲施法的卷軸。"));
             } else if (ran >= 91 && ran <= 100) {
                 pc.getInventory().storeItem(40087, 3); // 獲得 3 張武器魔法卷軸
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("獲得了 3 張 對武器施法的卷軸。"));
             }
         }
     }



   private void phoenixEgg(L1PcInstance pc, L1DoorInstance door) {
     int ran = _random.nextInt(100) + 1;
     L1ItemInstance item = null;
     if (ran >= 0 && ran <= 10) {
       item = pc.getInventory().storeItem(40010, 5);
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(403, item.getName() + " (5)"));
     } else if (ran >= 11 && ran <= 20) {
       item = pc.getInventory().storeItem(40010, 10);
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(403, item.getName() + " (10)"));
     } else if (ran >= 21 && ran <= 30) {
       item = pc.getInventory().storeItem(40010, 15);
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(403, item.getName() + " (15)"));
     } else if (ran >= 31 && ran <= 40) {
       item = pc.getInventory().storeItem(40010, 30);
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(403, item.getName() + " (30)"));
     } else if (ran >= 41 && ran <= 50) {
       item = pc.getInventory().storeItem(40010, 50);
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(403, item.getName() + " (50)"));
     } else if (ran >= 51 && ran <= 60) {
       item = pc.getInventory().storeItem(40012, 5);
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(403, item.getName() + " (5)"));
     } else if (ran >= 61 && ran <= 70) {
       item = pc.getInventory().storeItem(40012, 10);
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(403, item.getName() + " (10)"));
     } else if (ran >= 71 && ran <= 80) {
       item = pc.getInventory().storeItem(40012, 15);
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(403, item.getName() + " (15)"));
     } else if (ran >= 81 && ran <= 90) {
       item = pc.getInventory().storeItem(40012, 30);
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(403, item.getName() + " (30)"));
     } else if (ran >= 91 && ran <= 100) {
       item = pc.getInventory().storeItem(40012, 50);
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(403, item.getName() + " (50)"));
     }
   }

   private boolean isExistKeeper(L1PcInstance pc, int keeperId) {
     if (keeperId == 0) {
       return false;
     }

     L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
     if (clan != null) {
       int houseId = clan.getHouseId();
       if (houseId != 0) {
         L1House house = HouseTable.getInstance().getHouseTable(houseId);
         if (keeperId == house.getKeeperId()) {
           return false;
         }
       }
     }
     return true;
   }


   public String getType() {
     return "[C] C_Door";
   }
 }


