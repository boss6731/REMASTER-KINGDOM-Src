 package l1j.server.server.model;

 import java.util.List;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_EXCHANGE_ITEM_LIST_NOTI;
 import l1j.server.server.GameServer;
 import l1j.server.server.datatables.CharacterTable;
 import l1j.server.server.datatables.ExpTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Warehouse.PrivateWarehouse;
 import l1j.server.server.model.Warehouse.Warehouse;
 import l1j.server.server.model.Warehouse.WarehouseManager;
 import l1j.server.server.monitor.LoggerInstance;
 import l1j.server.server.serverpackets.S_ChatPacket;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_TradeStatus;
 import l1j.server.server.serverpackets.ServerBasePacket;



 public class L1Trade
 {
     public void TradeAddItem(L1PcInstance player, int itemid, int itemcount) {
         L1PcInstance trading_partner = (L1PcInstance)L1World.getInstance().findObject(player.getTradeID());
         L1ItemInstance l1iteminstance = player.getInventory().getItem(itemid);
         if (l1iteminstance != null && trading_partner != null &&
                 !l1iteminstance.isEquipped()) {
             if (l1iteminstance.getCount() < itemcount || 0 >= itemcount) {

                 TradeCancel(player);

                 return;
             }
             player.getInventory().tradeItem(l1iteminstance, itemcount, player.getTradeWindowInventory());

             int lv = player.getLevel();
             long currentLvExp = ExpTable.getExpByLevel(lv);
             long nextLvExp = ExpTable.getExpByLevel(lv + 1);
             double neededExp = (nextLvExp - currentLvExp);
             double currentExp = (player.get_exp() - currentLvExp);
             int per = (int)(currentExp / neededExp * 100.0D);

                // 檢查物品ID是否為100002或100003
             if (l1iteminstance.getItemId() == 100002 || l1iteminstance.getItemId() == 100003) {

                 // 執行交換操作
                 SC_EXCHANGE_ITEM_LIST_NOTI.do_exchange(player, l1iteminstance, itemcount, 0);
                 SC_EXCHANGE_ITEM_LIST_NOTI.do_exchange(trading_partner, l1iteminstance, itemcount, 1);

                 // 向玩家發送聊天訊息
                 player.sendPackets((ServerBasePacket)new S_ChatPacket(player, "--------------------------------------------------"));
                 player.sendPackets((ServerBasePacket)new S_ChatPacket(player, "角色出售中。"));
                 player.sendPackets((ServerBasePacket)new S_ChatPacket(player, "交易後交易的金額會進入此帳號倉庫。"));
                 player.sendPackets((ServerBasePacket)new S_ChatPacket(player, "--------------------------------------------------"));

                 // 向交易對方發送聊天訊息
                 trading_partner.sendPackets((ServerBasePacket)new S_ChatPacket(player, "--------------------------------------------------"));
                 trading_partner.sendPackets((ServerBasePacket)new S_ChatPacket(player, "狀態房間角色資訊。"));
                 trading_partner.sendPackets((ServerBasePacket)new S_ChatPacket(player, "職業: [" + player.getClassName() + "] 等級: [" + Integer.toString(player.getLevel()) + "." + per + "%] 精華點數: [" + player.getElixirStats() + "]。"));
                 trading_partner.sendPackets((ServerBasePacket)new S_ChatPacket(player, "--------------------------------------------------"));
             }

             }
             else {

                 SC_EXCHANGE_ITEM_LIST_NOTI.do_exchange(player, l1iteminstance, itemcount, 0);
                 SC_EXCHANGE_ITEM_LIST_NOTI.do_exchange(trading_partner, l1iteminstance, itemcount, 1);
             }
         }
     }


     public void doCharacterTrade(L1PcInstance player, boolean characterTrade1, L1PcInstance target, boolean characterTrade2) {
         // 檢查玩家或目標是否沒有網絡連接
         if (player.getNetConnection() == null || target.getNetConnection() == null) {
             // 向玩家和目標發送聊天訊息，通知對方連接異常
             player.sendPackets((ServerBasePacket)new S_ChatPacket(player, "交易對象連接異常。"));
             target.sendPackets((ServerBasePacket)new S_ChatPacket(player, "交易對象連接異常。"));

             // 取消交易
             TradeCancel(player);
             return;
         }

         // 檢查目標玩家是否有足夠的角色槽位進行交易
         if (characterTrade1 && target.getNetConnection().getAccount().countCharacters() >= target.getNetConnection().getAccount().getCharSlot()) {
             // 向玩家和目標發送聊天訊息，通知對方沒有空餘角色槽位
             player.sendPackets((ServerBasePacket)new S_ChatPacket(player, "交易對象沒有空餘的角色槽位。"));
             target.sendPackets((ServerBasePacket)new S_ChatPacket(player, "沒有空餘的角色槽位。請確保有足夠的角色槽位後再試。"));

             // 取消交易
             TradeCancel(player);
             return;
         }

         // 檢查玩家自己是否有足夠的角色槽位進行交易
         if (characterTrade2 && player.getNetConnection().getAccount().countCharacters() >= player.getNetConnection().getAccount().getCharSlot()) {
             // 向目標和玩家發送聊天訊息，通知對方沒有空餘角色槽位
             target.sendPackets((ServerBasePacket)new S_ChatPacket(player, "交易對象沒有空餘的角色槽位。"));
             player.sendPackets((ServerBasePacket)new S_ChatPacket(player, "沒有空餘的角色槽位。請確保有足夠的角色槽位後再試。"));

             // 取消交易
             TradeCancel(player);
             return;
         }

         // 交易的其餘邏輯（未提供）
     }
         if (characterTrade1) {
             PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(player.getAccountName());
             if (warehouse == null) {
                 TradeCancel(player);

                 return;
             }
             for (L1ItemInstance item : target.getTradeWindowInventory().getItems()) {
                 if (warehouse.checkAddItemToWarehouse(item, item.getCount()) == 1) {
                     target.sendPackets((ServerBasePacket)new S_ServerMessage(75));

                     TradeCancel(player);

                     return;
                 }
             }
         }
         if (characterTrade2) {
             PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(target.getAccountName());

             if (warehouse == null) {
                 TradeCancel(player);

                 return;
             }

             for (L1ItemInstance item : player.getTradeWindowInventory().getItems()) {
                 if (warehouse.checkAddItemToWarehouse(item, item.getCount()) == 1) {
                     player.sendPackets((ServerBasePacket)new S_ServerMessage(75));

                     TradeCancel(player);

                     return;
                 }
             }
         }
         if (characterTrade1) {
             PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(player.getAccountName());

             while (target.getTradeWindowInventory().getItems().size() > 0) {
                 L1ItemInstance item = target.getTradeWindowInventory().getItems().get(0);
                 if (item.getItemId() == 100002 || item.getItemId() == 100003) {
                     target.getTradeWindowInventory().consumeItem(item.getItemId(), item.getCount()); continue;
                 }
                 target.getTradeWindowInventory().tradeItem(item, item.getCount(), (Warehouse)warehouse);
             }


             if (!characterTrade2) {
                 while (player.getTradeWindowInventory().getItems().size() > 0) {
                     L1ItemInstance item = player.getTradeWindowInventory().getItems().get(0);
                     if (item.getItemId() == 100002 || item.getItemId() == 100003) {
                         player.getTradeWindowInventory().consumeItem(item.getItemId(), item.getCount()); continue;
                     }
                     player.getTradeWindowInventory().tradeItem(item, item.getCount(), player.getInventory());
                 }
             }
         }


         if (characterTrade2) {
             PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(target.getAccountName());
             while (player.getTradeWindowInventory().getItems().size() > 0) {
                 L1ItemInstance item = player.getTradeWindowInventory().getItems().get(0);
                 if (item.getItemId() == 100002 || item.getItemId() == 100003) {
                     player.getTradeWindowInventory().consumeItem(item.getItemId(), item.getCount()); continue;
                 }
                 player.getTradeWindowInventory().tradeItem(item, item.getCount(), (Warehouse)warehouse);
             }


             if (!characterTrade1) {
                 while (target.getTradeWindowInventory().getItems().size() > 0) {
                     L1ItemInstance item = target.getTradeWindowInventory().getItems().get(0);
                     if (item.getItemId() == 100002 || item.getItemId() == 100003) {
                         target.getTradeWindowInventory().consumeItem(item.getItemId(), item.getCount()); continue;
                     }
                     target.getTradeWindowInventory().tradeItem(item, item.getCount(), target.getInventory());
                 }
             }
         }


         player.sendPackets((ServerBasePacket)new S_TradeStatus(0));
         target.sendPackets((ServerBasePacket)new S_TradeStatus(0));
         player.setTradeOk(false);
         target.setTradeOk(false);
         player.setTradeID(0);
         target.setTradeID(0);
         player.getLight().turnOnOffLight();
         target.getLight().turnOnOffLight();

         String playerAccountName = player.getAccountName();
         String targetAccountName = target.getAccountName();

         if (characterTrade1) {
             player.setAccountName(targetAccountName);
             try {
                 CharacterTable.getInstance().updateCharacterAccount(player);
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }

         if (characterTrade2) {
             target.setAccountName(playerAccountName);
             try {
                 CharacterTable.getInstance().updateCharacterAccount(target);
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }

         GameServer.disconnectChar(player);
         GameServer.disconnectChar(target);
     }


     public void TradeOK(L1PcInstance player) {
         L1PcInstance trading_partner = (L1PcInstance)L1World.getInstance().findObject(player.getTradeID());
         if (trading_partner != null && trading_partner.getTradeID() == player.getId()) {
             List<?> player_tradelist = player.getTradeWindowInventory().getItems();
             int player_tradecount = player_tradelist.size();
             List<?> trading_partner_tradelist = trading_partner.getTradeWindowInventory().getItems();

             int trading_partner_tradecount = trading_partner_tradelist.size();

             L1ItemInstance l1iteminstance1 = null;
             L1ItemInstance l1iteminstance2 = null;



             boolean characterTrade1 = false;
             boolean characterTrade2 = false;
             int cnt;
             for (cnt = 0; cnt < player_tradecount; cnt++) {
                 l1iteminstance1 = (L1ItemInstance)player_tradelist.get(cnt);

                 if (l1iteminstance1.getItemId() == 100002 || l1iteminstance1.getItemId() == 100003) {
                     characterTrade1 = true;
                     break;
                 }
             }
             for (cnt = 0; cnt < trading_partner_tradecount; cnt++) {
                 l1iteminstance2 = (L1ItemInstance)trading_partner_tradelist.get(cnt);
                 if (l1iteminstance2.getItemId() == 100002 || l1iteminstance2.getItemId() == 100003) {
                     characterTrade2 = true;

                     break;
                 }
             }
             if (characterTrade1 || characterTrade2) {
                 doCharacterTrade(player, characterTrade1, trading_partner, characterTrade2);
             } else {
                 for (cnt = 0; cnt < player_tradecount; cnt++) {
                     l1iteminstance1 = (L1ItemInstance)player_tradelist.get(0);
                     player.getTradeWindowInventory().tradeItem(l1iteminstance1, l1iteminstance1.getCount(), trading_partner.getInventory());


                     LoggerInstance.getInstance().addTrade(true, player, trading_partner, l1iteminstance1, l1iteminstance1.getCount());
                 }

                 for (cnt = 0; cnt < trading_partner_tradecount; cnt++) {
                     l1iteminstance2 = (L1ItemInstance)trading_partner_tradelist.get(0);
                     trading_partner.getTradeWindowInventory().tradeItem(l1iteminstance2, l1iteminstance2.getCount(), player.getInventory());

                     LoggerInstance.getInstance().addTrade(true, trading_partner, player, l1iteminstance2, l1iteminstance2.getCount());
                 }


                 player.sendPackets((ServerBasePacket)new S_TradeStatus(0));
                 trading_partner.sendPackets((ServerBasePacket)new S_TradeStatus(0));
                 player.setTradeOk(false);
                 trading_partner.setTradeOk(false);
                 player.setTradeID(0);
                 trading_partner.setTradeID(0);
                 player.getLight().turnOnOffLight();
                 trading_partner.getLight().turnOnOffLight();
             }
         } else {
             TradeCancel(player);
         }
     }



     public void TradeCancel(L1PcInstance player) {
         L1PcInstance trading_partner = (L1PcInstance)L1World.getInstance().findObject(player.getTradeID());


         List<?> player_tradelist = player.getTradeWindowInventory().getItems();
         int player_tradecount = player.getTradeWindowInventory().getSize();

         L1ItemInstance l1iteminstance1 = null; int cnt;
         for (cnt = 0; cnt < player_tradecount; cnt++) {
             l1iteminstance1 = (L1ItemInstance)player_tradelist.get(0);
             player.getTradeWindowInventory().tradeItem(l1iteminstance1, l1iteminstance1.getCount(), player.getInventory());

             LoggerInstance.getInstance().addTrade(false, player, trading_partner, l1iteminstance1, l1iteminstance1.getCount());
         }

         player.sendPackets((ServerBasePacket)new S_TradeStatus(1));
         player.setTradeOk(false);
         player.setTradeID(0);


         if (trading_partner != null && trading_partner.getTradeID() == player.getId()) {
             List<?> trading_partner_tradelist = trading_partner.getTradeWindowInventory().getItems();
             int trading_partner_tradecount = trading_partner.getTradeWindowInventory().getSize();
             L1ItemInstance l1iteminstance2 = null;
             for (cnt = 0; cnt < trading_partner_tradecount; cnt++) {
                 l1iteminstance2 = (L1ItemInstance)trading_partner_tradelist.get(0);
                 trading_partner.getTradeWindowInventory().tradeItem(l1iteminstance2, l1iteminstance2.getCount(), trading_partner.getInventory());

                 LoggerInstance.getInstance().addTrade(false, trading_partner, player, l1iteminstance2, l1iteminstance2.getCount());
             }

             trading_partner.sendPackets((ServerBasePacket)new S_TradeStatus(1));
             trading_partner.setTradeOk(false);
             trading_partner.setTradeID(0);
         }
     }
 }


