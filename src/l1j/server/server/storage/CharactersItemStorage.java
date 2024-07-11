         package l1j.server.server.storage;

         import java.util.ArrayList;
         import l1j.server.server.model.Instance.L1ItemInstance;
         import l1j.server.server.storage.mysql.MySqlCharactersItemStorage;

         public abstract class CharactersItemStorage {
           private static CharactersItemStorage _instance;

           public abstract ArrayList<L1ItemInstance> loadItems(int paramInt) throws Exception;

           public abstract void storeItem(int paramInt, L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void deleteItem(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updateItemAll(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updateItemId(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updateItemCount(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updateSpecialEnchant(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updateItemIdentified(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updateItemEquipped(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updateItemEnchantLevel(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updateItemDurability(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updateItemChargeCount(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updateItemRemainingTime(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updateItemDelayEffect(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updateItemBless(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updateItemBlessLevel(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updateItemAttrEnchantLevel(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updateItemEndTime(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updateItemLevel(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updateHotelTowm(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract int getItemCount(int paramInt) throws Exception;

           public abstract void updateSupportItem(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updatecarving(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updateDollLevel(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updateDollValue(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updateBlessType(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updateBlessTypeValue(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updateSmeltingValue(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updateSmeltingItemId1(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updateSmeltingItemId2(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updateSmeltingKind1(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updateSmeltingKind2(L1ItemInstance paramL1ItemInstance) throws Exception;

           public abstract void updateHalpasTime(L1ItemInstance paramL1ItemInstance) throws Exception;

           public static CharactersItemStorage create() {
             if (_instance == null) {
               _instance = (CharactersItemStorage)new MySqlCharactersItemStorage();
             }
             return _instance;
           }
         }


