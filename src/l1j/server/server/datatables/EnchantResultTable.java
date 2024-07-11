 package l1j.server.server.datatables;

 import java.sql.ResultSet;
 import java.util.HashMap;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_ENCHANT_RESULT;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class EnchantResultTable {
   private static EnchantResultTable _ins;

   public static EnchantResultTable getIns() {
     if (_ins == null) {
       _ins = new EnchantResultTable();
     }
     return _ins;
   }

   public void reLoad() {
     EnchantResultTable oldIns = _ins;
     _ins = new EnchantResultTable();
     oldIns._list.clear();
     oldIns = null;
   }

   private HashMap<Integer, L1EnchantResult> _list = new HashMap<>();
   private EnchantResultTable() {
     Selector.exec("SELECT * FROM enchant_result", (SelectorHandler)new FullSelectorHandler()
         {
           public void result(ResultSet rs) throws Exception
           {
             while (rs.next()) {
               int itemId = rs.getInt("item_id");
               EnchantResultTable.this._list.put(Integer.valueOf(itemId), new EnchantResultTable.L1EnchantResult(rs));
             }
           }
         });
   }

   public void send(L1PcInstance pc, SC_ENCHANT_RESULT.eResult result, int enchantGap, L1ItemInstance item) {
     L1EnchantResult er = this._list.get(Integer.valueOf(item.getItemId()));
     int effect_enchant = 7;
     if (er != null) {
       SC_ENCHANT_RESULT.send(pc, item, result, er.isBm, enchantGap, er.effectId, er.isHigh, false);
     } else if (item.getEnchantLevel() >= 7) {
       SC_ENCHANT_RESULT.send(pc, item, result, true, enchantGap, effect_enchant, true, false);
     } else {
       SC_ENCHANT_RESULT.send(pc, item, result, false, enchantGap, effect_enchant, false, false);
     }
   }

   private class L1EnchantResult {
     private boolean isBm;
     private int effectId;
     private boolean isHigh;

     public L1EnchantResult(ResultSet rs) {
       try {
         this.isBm = rs.getBoolean("is_bm");
         this.effectId = Integer.parseInt(rs.getString("effect"));
         this.isHigh = rs.getBoolean("is_high");
       } catch (Exception e) {
         e.printStackTrace();
       }
     }
   }
 }


