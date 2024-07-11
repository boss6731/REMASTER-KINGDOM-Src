         package l1j.server.server.storage.mysql;

         import MJShiftObject.MJShiftObjectHelper;
         import MJShiftObject.MJShiftObjectManager;
         import java.sql.Connection;
         import java.sql.PreparedStatement;
         import java.sql.ResultSet;
         import java.sql.SQLException;
         import java.sql.Timestamp;
         import java.util.ArrayList;
         import java.util.List;
         import java.util.logging.Logger;
         import l1j.server.Config;
         import l1j.server.L1DatabaseFactory;
         import l1j.server.server.IdFactory;
         import l1j.server.server.datatables.ItemTable;
         import l1j.server.server.datatables.KeyTable;
         import l1j.server.server.model.Instance.L1ItemInstance;
         import l1j.server.server.storage.CharactersItemStorage;
         import l1j.server.server.templates.L1Item;
         import l1j.server.server.utils.SQLUtil;

         public class MySqlCharactersItemStorage
           extends CharactersItemStorage
         {
           private static final Logger _log = Logger.getLogger(MySqlCharactersItemStorage.class.getName());


           public ArrayList<L1ItemInstance> loadItems(int objId) throws Exception {
             ArrayList<L1ItemInstance> items = null;

             Connection con = null;
             PreparedStatement pstm = null;
             ResultSet rs = null;
             try {
               items = new ArrayList<>();
               con = L1DatabaseFactory.getInstance().getConnection();
               pstm = con.prepareStatement("SELECT * FROM character_items WHERE char_id = ?");
               pstm.setInt(1, objId);

               L1ItemInstance item = null;
               rs = pstm.executeQuery();
               L1Item itemTemplate = null;

               while (rs.next()) {
                 int itemId = rs.getInt("item_id");
                 itemTemplate = ItemTable.getInstance().getTemplate(itemId);
                 if (itemTemplate == null) {
                   _log.warning(String.format("item id:%d not found", new Object[] { Integer.valueOf(itemId) }));
                   System.out.println(String.format("item id:%d not found", new Object[] { Integer.valueOf(itemId) }));
                   continue;
                 }
                 item = new L1ItemInstance();
                 item.setId(rs.getInt("id"));
                 item.setItem(itemTemplate);
                 item.setCount(rs.getInt("count"));
                 item.setEquipped((rs.getInt("Is_equipped") != 0));
                 item.setEnchantLevel(rs.getInt("enchantlvl"));
                 item.setIdentified((rs.getInt("is_id") != 0));
                 item.set_durability(rs.getInt("durability"));
                 item.setChargeCount(rs.getInt("charge_count"));
                 item.setRemainingTime(rs.getInt("remaining_time"));
                 item.setLastUsed(rs.getTimestamp("last_used"));
                 item.setBless(rs.getInt("bless"));
                 item.setAttrEnchantLevel(rs.getInt("attr_enchantlvl"));
                 item.setSpecialEnchant(rs.getInt("special_enchant"));
                 item.setEndTime(rs.getTimestamp("end_time"));
                 item.setPackage((rs.getInt("package") != 0));
                 item.set_bless_level(rs.getInt("bless_level"));
                 item.set_item_level(rs.getInt("item_level"));
                 item.setHotel_Town(rs.getString("Hotel_Town"));
                 item.setSupportItem((rs.getInt("is_support_item") != 0));
                 item.set_Carving(rs.getInt("Carving"));
                 item.set_Doll_Bonus_Level(rs.getInt("doll_bonus_level"));
                 item.set_Doll_Bonus_Value(rs.getInt("doll_bonus_value"));
                 item.setBlessType(rs.getInt("bless_type"));
                 item.setBlessTypeValue(rs.getInt("bless_type_value"));
                 item.setSmeltingValue(rs.getInt("smelting_slot_value"));
                 item.setSmeltingItemId1(rs.getInt("smelting_itemid_1"));
                 item.setSmeltingItemId2(rs.getInt("smelting_itemid_2"));
                 item.setHalpas_Time(rs.getTimestamp("halpas_time"));

                 item.set_Cantunseal(rs.getInt("cant_unseal"));
                 item.getLastStatus().updateAll();
                 if (item.getItem().getItemId() == 80500) {
                   KeyTable.checkey(item);
                 }
                 items.add(item);
               }
             } catch (Exception e) {
               e.printStackTrace();
             } finally {
               SQLUtil.close(rs);
               SQLUtil.close(pstm);
               SQLUtil.close(con);
             }
             if (Config.Login.UseShiftServer) {
               List<L1ItemInstance> shift_pickups = MJShiftObjectHelper.select_pickup_items(objId, MJShiftObjectManager.getInstance().get_home_server_identity());
               if (shift_pickups != null) {
                 MJShiftObjectHelper.delete_pickup_items(objId, MJShiftObjectManager.getInstance().get_home_server_identity());
                 for (L1ItemInstance item : shift_pickups) {
                   boolean is_non_add = false;
                   if (item.isStackable()) {
                     for (L1ItemInstance i : items) {
                       if (i.getItemId() == item.getItemId()) {
                         i.setCount(i.getCount() + item.getCount());
                         updateItemAll(i);
                         is_non_add = true;
                       }
                     }
                   }
                   if (!is_non_add) {
                     item.setId(IdFactory.getInstance().nextId());
                     items.add(item);
                     storeItem(objId, item);
                   }
                 }
               }
             }
             return items;
           }


           public void storeItem(int objId, L1ItemInstance item) throws Exception {
             Connection con = null;
             PreparedStatement pstm = null;
             try {
               int idx = 0;
               con = L1DatabaseFactory.getInstance().getConnection();
               pstm = con.prepareStatement("INSERT INTO character_items SET id = ?, item_id = ?, char_id = ?, item_name = ?, count = ?, is_equipped = 0, enchantlvl = ?, is_id = ?, durability = ?, charge_count = ?, remaining_time = ?, last_used = ?, bless = ?, attr_enchantlvl = ?, end_time =?,buy_time=?, package = ?, bless_level = ?, item_level = ?, Hotel_Town = ?, is_support_item=?, Carving = ?, doll_bonus_level = ?, doll_bonus_value = ?,  cant_unseal=? on duplicate key update item_id = ?, char_id = ?, item_name = ?, count = ?, is_equipped = 0, enchantlvl = ?, is_id = ?, durability = ?, charge_count = ?, remaining_time = ?, last_used = ?, bless = ?, attr_enchantlvl = ?, end_time =?,buy_time=?, package = ?, bless_level = ?, item_level = ?, Hotel_Town=?, is_support_item=?, Carving = ?, doll_bonus_level = ?, doll_bonus_value = ?, bless_type=?, bless_type_value=?, smelting_slot_value=?, smelting_itemid_1=?, smelting_itemid_2=?, smelting_kind_1=?, smelting_kind_2=?, halpas_time=?, cant_unseal=?");








               pstm.setInt(++idx, item.getId());
               pstm.setInt(++idx, item.getItem().getItemId());
               pstm.setInt(++idx, objId);
               pstm.setString(++idx, item.getItem().getName());
               pstm.setInt(++idx, item.getCount());
               pstm.setInt(++idx, item.getEnchantLevel());
               pstm.setInt(++idx, item.isIdentified() ? 1 : 0);
               pstm.setInt(++idx, item.get_durability());
               pstm.setInt(++idx, item.getChargeCount());
               pstm.setInt(++idx, item.getRemainingTime());
               pstm.setTimestamp(++idx, item.getLastUsed());
               pstm.setInt(++idx, item.getBless());
               pstm.setInt(++idx, item.getAttrEnchantLevel());
               pstm.setTimestamp(++idx, item.getEndTime());
               pstm.setTimestamp(++idx, item.getEndTime());
               pstm.setInt(++idx, !item.isPackage() ? 0 : 1);
               pstm.setInt(++idx, item.get_bless_level());
               pstm.setInt(++idx, item.get_item_level());
               pstm.setString(++idx, item.getHotel_Town());
               pstm.setInt(++idx, item.isSupportItem() ? 1 : 0);
               pstm.setInt(++idx, item.get_Carving());
               pstm.setInt(++idx, item.get_Doll_Bonus_Level());
               pstm.setInt(++idx, item.get_Doll_Bonus_Value());
               pstm.setInt(++idx, item.get_Cantunseal());


               pstm.setInt(++idx, item.getItem().getItemId());
               pstm.setInt(++idx, objId);
               pstm.setString(++idx, item.getItem().getName());
               pstm.setInt(++idx, item.getCount());
               pstm.setInt(++idx, item.getEnchantLevel());
               pstm.setInt(++idx, item.isIdentified() ? 1 : 0);
               pstm.setInt(++idx, item.get_durability());
               pstm.setInt(++idx, item.getChargeCount());
               pstm.setInt(++idx, item.getRemainingTime());
               pstm.setTimestamp(++idx, item.getLastUsed());
               pstm.setInt(++idx, item.getBless());
               pstm.setInt(++idx, item.getAttrEnchantLevel());
               pstm.setTimestamp(++idx, item.getEndTime());
               pstm.setTimestamp(++idx, item.getEndTime());
               pstm.setInt(++idx, !item.isPackage() ? 0 : 1);
               pstm.setInt(++idx, item.get_bless_level());
               pstm.setInt(++idx, item.get_item_level());
               pstm.setString(++idx, item.getHotel_Town());
               pstm.setInt(++idx, item.isSupportItem() ? 1 : 0);
               pstm.setInt(++idx, item.get_Carving());
               pstm.setInt(++idx, item.get_Doll_Bonus_Level());
               pstm.setInt(++idx, item.get_Doll_Bonus_Value());
               pstm.setInt(++idx, item.getBlessType());
               pstm.setInt(++idx, item.getBlessTypeValue());
               pstm.setInt(++idx, item.getSmeltingValue());
               pstm.setInt(++idx, item.getSmeltingItemId1());
               pstm.setInt(++idx, item.getSmeltingItemId2());
               pstm.setInt(++idx, item.getSmeltingKind1());
               pstm.setInt(++idx, item.getSmeltingKind2());
               pstm.setTimestamp(++idx, item.getHalpas_Time());
               pstm.setInt(++idx, item.get_Cantunseal());
               pstm.execute();
             }
               try {
                   // 這裡是可能會拋出異常的程式碼
               } catch (Exception e) {
                   // 捕捉異常並打印堆疊追蹤
                   e.printStackTrace();
                   // 輸出錯誤訊息，包含物品名稱及其所有者
                   System.out.println("資料庫物品保存錯誤，物品名稱:  " + item.getName() + " 所有者: " + item.getItemOwner().getName());
                   // 再次拋出異常
                   throw e;
               } finally {
                   // 關閉SQL語句和連接
                   SQLUtil.close(pstm);
                   SQLUtil.close(con);
               }
// 更新物品的最後狀態
               item.getLastStatus().updateAll();
           }


           public void deleteItem(L1ItemInstance item) throws Exception {
             Connection con = null;
             PreparedStatement pstm = null;
             try {
               con = L1DatabaseFactory.getInstance().getConnection();
               pstm = con.prepareStatement("DELETE FROM character_items WHERE id = ?");
               pstm.setInt(1, item.getId());
               pstm.execute();
             } catch (Exception e) {
               throw e;
             } finally {
               SQLUtil.close(pstm);
               SQLUtil.close(con);
             }
           }


           public void updateItemAll(L1ItemInstance item) throws Exception {
             int idx = 0;
             Connection con = null;
             PreparedStatement pstm = null;
             try {
               con = L1DatabaseFactory.getInstance().getConnection();
               pstm = con.prepareStatement("UPDATE character_items SET item_id = ?,count = ?,durability = ?,charge_count = ?,remaining_time = ?,enchantlvl = ?,is_equipped = ?,is_id = ?,last_used = ?,bless = ?,attr_enchantlvl = ?,special_enchant = ?,end_time = ?,bless_level = ?,item_level = ?,Hotel_Town = ?, is_support_item = ?,Carving = ?, doll_bonus_level = ?, doll_bonus_value=?,bless_type = ?, bless_type_value = ?,smelting_slot_value=? ,smelting_itemid_1=? ,smelting_itemid_2=? ,smelting_kind_1=? ,smelting_kind_2=?, halpas_time=?, cant_unseal=? WHERE id=?");



               pstm.setInt(++idx, item.getItemId());
               pstm.setInt(++idx, item.getCount());
               pstm.setInt(++idx, item.get_durability());
               pstm.setInt(++idx, item.getChargeCount());
               pstm.setInt(++idx, item.getRemainingTime());
               pstm.setInt(++idx, item.getEnchantLevel());
               pstm.setInt(++idx, item.isEquipped() ? 1 : 0);
               pstm.setInt(++idx, item.isIdentified() ? 1 : 0);
               pstm.setTimestamp(++idx, item.getLastUsed());
               pstm.setInt(++idx, item.getBless());
               pstm.setInt(++idx, item.getAttrEnchantLevel());
               pstm.setInt(++idx, item.getSpecialEnchant());
               pstm.setTimestamp(++idx, item.getEndTime());
               pstm.setInt(++idx, item.get_bless_level());
               pstm.setInt(++idx, item.get_item_level());
               pstm.setString(++idx, item.getHotel_Town());
               pstm.setInt(++idx, item.isSupportItem() ? 1 : 0);
               pstm.setInt(++idx, item.get_Carving());
               pstm.setInt(++idx, item.get_Doll_Bonus_Level());
               pstm.setInt(++idx, item.get_Doll_Bonus_Value());
               pstm.setInt(++idx, item.getBlessType());
               pstm.setInt(++idx, item.getBlessTypeValue());
               pstm.setInt(++idx, item.getSmeltingValue());
               pstm.setInt(++idx, item.getSmeltingItemId1());
               pstm.setInt(++idx, item.getSmeltingItemId2());
               pstm.setInt(++idx, item.getSmeltingKind1());
               pstm.setInt(++idx, item.getSmeltingKind2());
               pstm.setTimestamp(++idx, item.getHalpas_Time());
               pstm.setInt(++idx, item.get_Cantunseal());
               pstm.setInt(++idx, item.getId());

               pstm.execute();

               item.getLastStatus().updateItemId();
               item.getLastStatus().updateCount();
               item.getLastStatus().updateDuraility();
               item.getLastStatus().updateChargeCount();
               item.getLastStatus().updateRemainingTime();
               item.getLastStatus().updateEnchantLevel();
               item.getLastStatus().updateEquipped();
               item.getLastStatus().updateIdentified();
               item.getLastStatus().updateLastUsed();
               item.getLastStatus().updateBless();
               item.getLastStatus().updateAttrEnchantLevel();
               item.getLastStatus().updateEndTime();
               item.getLastStatus().updateSupportItem();
               item.getLastStatus().update_Carving();
               item.getLastStatus().update_Doll_Bonus_Level();
               item.getLastStatus().update_Doll_Bonus_Value();
               item.getLastStatus().updateBlessType();
               item.getLastStatus().updateBlessTypeValue();
               item.getLastStatus().updateSmeltingValue();
               item.getLastStatus().updateSmeltingItemId1();
               item.getLastStatus().updateSmeltingItemId2();
               item.getLastStatus().updateSmeltingKind1();
               item.getLastStatus().updateSmeltingKind2();
               item.getLastStatus().updateHalpasTime();
               item.getLastStatus().updateCantUnseal();
             }
             catch (Exception e) {
               e.printStackTrace();
             } finally {
               SQLUtil.close(pstm);
               SQLUtil.close(con);
             }
           }


           public void updateItemId(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET item_id = ? WHERE id = ?", item.getItemId());
             item.getLastStatus().updateItemId();
           }





           public void updateItemCount(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET count = ?, package = ? WHERE id = ?", item.getCount(), !item.isPackage() ? 0 : 1);
             item.getLastStatus().updateCount();
           }


           public void updateHalpasTime(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET halpas_time = ? WHERE id = ?", item.getHalpas_Time());
             item.getLastStatus().updateHalpasTime();
           }


           public void updateItemDurability(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET durability = ? WHERE id = ?", item.get_durability());
             item.getLastStatus().updateDuraility();
           }


           public void updateItemChargeCount(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET charge_count = ? WHERE id = ?", item.getChargeCount());
             item.getLastStatus().updateChargeCount();
           }


           public void updateItemRemainingTime(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET remaining_time = ? WHERE id = ?", item.getRemainingTime());
             item.getLastStatus().updateRemainingTime();
           }


           public void updateItemEnchantLevel(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET enchantlvl = ? WHERE id = ?", item.getEnchantLevel());
             item.getLastStatus().updateEnchantLevel();
           }


           public void updateItemEquipped(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET is_equipped = ? WHERE id = ?", item.isEquipped() ? 1 : 0);
             item.getLastStatus().updateEquipped();
           }


           public void updateItemIdentified(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET is_id = ? WHERE id = ?", item.isIdentified() ? 1 : 0);
             item.getLastStatus().updateIdentified();
           }


           public void updateSpecialEnchant(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET special_enchant = ? WHERE id = ?", item.getSpecialEnchant());
             item.getLastStatus().updateSpecialEnchant();
           }


           public void updateItemDelayEffect(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET last_used = ? WHERE id = ?", item.getLastUsed());
             item.getLastStatus().updateLastUsed();
           }


           public void updateItemBless(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET bless = ? WHERE id = ?", item.getBless());
             item.getLastStatus().updateBless();
           }


           public void updateItemBlessLevel(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET bless_level = ? WHERE id = ?", item.get_bless_level());
             item.getLastStatus().update_bless_level();
           }


           public void updateItemAttrEnchantLevel(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET attr_enchantlvl = ? WHERE id = ?", item.getAttrEnchantLevel());
             item.getLastStatus().updateAttrEnchantLevel();
           }


           public void updateItemEndTime(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET end_time = ? WHERE id = ?", item.getEndTime());
             item.getLastStatus().updateEndTime();
           }



           public void updateItemLevel(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET item_level = ? WHERE id = ?", item.get_item_level());
             item.getLastStatus().update_item_level();
           }





           public void updateHotelTowm(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET Hotel_Town = ? WHERE id = ?", item.getHotel_Town());
             item.getLastStatus().update_town_name();
           }




           public void updateSupportItem(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET is_support_item = ? WHERE id = ?", item.isSupportItem() ? 1 : 0);
             item.getLastStatus().updateSupportItem();
           }


           public void updatecarving(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET Carving = ? WHERE id = ?", item.get_Carving());
             item.getLastStatus().update_Carving();
           }


           public void updateDollLevel(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET doll_bonus_level = ? WHERE id = ?", item.get_Doll_Bonus_Level());
             item.getLastStatus().update_Doll_Bonus_Level();
           }



           public void updateDollValue(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET doll_bonus_value = ? WHERE id = ?", item.get_Doll_Bonus_Value());
             item.getLastStatus().update_Doll_Bonus_Value();
           }


           public void updateBlessType(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET bless_type = ? WHERE id = ?", item.getBlessType());
             item.getLastStatus().updateBlessType();
           }


           public void updateBlessTypeValue(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET bless_type_value = ? WHERE id = ?", item.getBlessTypeValue());
             item.getLastStatus().updateBlessTypeValue();
           }


           public void updateSmeltingValue(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET smelting_slot_value = ? WHERE id = ?", item.getSmeltingValue());
             item.getLastStatus().updateSmeltingValue();
           }


           public void updateSmeltingItemId1(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET smelting_itemid_1 = ? WHERE id = ?", item.getSmeltingItemId1());
             item.getLastStatus().updateSmeltingItemId1();
           }


           public void updateSmeltingItemId2(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET smelting_itemid_2 = ? WHERE id = ?", item.getSmeltingItemId2());
             item.getLastStatus().updateSmeltingItemId2();
           }


           public void updateSmeltingKind1(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET smelting_kind_1 = ? WHERE id = ?", item.getSmeltingKind1());
             item.getLastStatus().updateSmeltingKind1();
           }


           public void updateSmeltingKind2(L1ItemInstance item) throws Exception {
             executeUpdate(item.getId(), "UPDATE character_items SET smelting_kind_2 = ? WHERE id = ?", item.getSmeltingKind2());
             item.getLastStatus().updateSmeltingKind2();
           }



           public int getItemCount(int objId) throws Exception {
             int count = 0;
             Connection con = null;
             PreparedStatement pstm = null;
             ResultSet rs = null;
             try {
               con = L1DatabaseFactory.getInstance().getConnection();
               pstm = con.prepareStatement("SELECT * FROM character_items WHERE char_id = ?");
               pstm.setInt(1, objId);
               rs = pstm.executeQuery();
               while (rs.next()) {
                 count++;
               }
             } catch (Exception e) {
               e.printStackTrace();
             } finally {
               SQLUtil.close(rs);
               SQLUtil.close(pstm);
               SQLUtil.close(con);
             }
             return count;
           }

           private void executeUpdate(int objId, String sql, int updateNum) throws SQLException {
             Connection con = null;
             PreparedStatement pstm = null;
             try {
               con = L1DatabaseFactory.getInstance().getConnection();
               pstm = con.prepareStatement(sql.toString());
               pstm.setInt(1, updateNum);
               pstm.setInt(2, objId);
               pstm.execute();
             } catch (Exception e) {
               e.printStackTrace();
             } finally {
               SQLUtil.close(pstm);
               SQLUtil.close(con);
             }
           }

           private void executeUpdate(int objId, String sql, String string) throws SQLException {
             Connection con = null;
             PreparedStatement pstm = null;
             try {
               con = L1DatabaseFactory.getInstance().getConnection();
               pstm = con.prepareStatement(sql.toString());
               pstm.setString(1, string);
               pstm.setInt(2, objId);
               pstm.execute();
             } catch (Exception e) {
               e.printStackTrace();
             } finally {
               SQLUtil.close(pstm);
               SQLUtil.close(con);
             }
           }


           private void executeUpdate(int objId, String sql, int updateNum, int updatePackage) throws SQLException {
             Connection con = null;
             PreparedStatement pstm = null;
             try {
               con = L1DatabaseFactory.getInstance().getConnection();
               pstm = con.prepareStatement(sql.toString());
               pstm.setInt(1, updateNum);
               pstm.setInt(2, updatePackage);
               pstm.setInt(3, objId);
               pstm.execute();
             } catch (Exception e) {
               e.printStackTrace();
             } finally {
               SQLUtil.close(pstm);
               SQLUtil.close(con);
             }
           }

           private void executeUpdate(int objId, String sql, Timestamp ts) throws SQLException {
             Connection con = null;
             PreparedStatement pstm = null;
             try {
               con = L1DatabaseFactory.getInstance().getConnection();
               pstm = con.prepareStatement(sql.toString());
               pstm.setTimestamp(1, ts);
               pstm.setInt(2, objId);
               pstm.execute();
             } catch (Exception e) {
               e.printStackTrace();
             } finally {
               SQLUtil.close(pstm);
               SQLUtil.close(con);
             }
           }
         }


