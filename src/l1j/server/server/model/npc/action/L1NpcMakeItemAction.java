 package l1j.server.server.model.npc.action;

 import java.util.ArrayList;
 import java.util.List;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1ObjectAmount;
 import l1j.server.server.model.L1PcInventory;
 import l1j.server.server.model.npc.L1NpcHtml;
 import l1j.server.server.serverpackets.S_HowManyMake;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Item;
 import l1j.server.server.utils.IterableElementList;
 import org.w3c.dom.Element;
 import org.w3c.dom.NodeList;



 public class L1NpcMakeItemAction
   extends L1NpcXmlAction
 {
   private final List<L1ObjectAmount<Integer>> _materials = new ArrayList<>();
   private final List<L1ObjectAmount<Integer>> _items = new ArrayList<>();
   private final boolean _isAmountInputable;
   private final L1NpcAction _actionOnSucceed;
   private final L1NpcAction _actionOnFail;

   public L1NpcMakeItemAction(Element element) {
     super(element);

     this._isAmountInputable = L1NpcXmlParser.getBoolAttribute(element, "AmountInputable", true);
     NodeList list = element.getChildNodes();
     for (Element element1 : new IterableElementList(list)) {
       if (element1.getNodeName().equalsIgnoreCase("Material")) {
         int id = Integer.valueOf(element1.getAttribute("ItemId")).intValue();
         int amount = Integer.valueOf(element1.getAttribute("Amount")).intValue();
         int enchant = 0;
         try {
           enchant = Integer.valueOf(element1.getAttribute("Enchant")).intValue();
         } catch (Exception exception) {}
         this._materials.add(new L1ObjectAmount(Integer.valueOf(id), amount, enchant));
         continue;
       }
       if (element1.getNodeName().equalsIgnoreCase("Item")) {
         int id = Integer.valueOf(element1.getAttribute("ItemId")).intValue();
         int amount = Integer.valueOf(element1.getAttribute("Amount")).intValue();
         int enchant = 0;
         try {
           enchant = Integer.valueOf(element1.getAttribute("Enchant")).intValue();
         } catch (Exception exception) {}
         this._items.add(new L1ObjectAmount(Integer.valueOf(id), amount, enchant));
       }
     }


     if (this._items.isEmpty() || this._materials.isEmpty()) {
       throw new IllegalArgumentException();
     }

     Element elem = L1NpcXmlParser.getFirstChildElementByTagName(element, "Succeed");
     this._actionOnSucceed = (elem == null) ? null : new L1NpcListedAction(elem);
     elem = L1NpcXmlParser.getFirstChildElementByTagName(element, "Fail");
     this._actionOnFail = (elem == null) ? null : new L1NpcListedAction(elem);
   }


   private boolean makeItems(L1PcInstance pc, String npcName, int amount) {
     if (amount <= 0 || amount >= 1000) {
       return false;
     }

     boolean isEnoughMaterials = true;
     L1Item temp = null;
     for (L1ObjectAmount<Integer> material : this._materials) {
       if (!pc.getInventory().제작리스트(((Integer)material.getObject()).intValue(), material.getEnchant(), material.getAmount() * amount)) {
         temp = ItemTable.getInstance().getTemplate(((Integer)material.getObject()).intValue());
         if (material.getEnchant() != 0) {
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(337, ((material.getEnchant() < 0) ? (String)Integer.valueOf(material.getEnchant()) : ("+" + material.getEnchant())) + " " + temp.getName() + "(" + (material
                 .getAmount() * amount - pc.getInventory().countItems(temp.getItemId())) + ")"));
         } else {
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(337, temp.getName() + "(" + (material
                 .getAmount() * amount - pc.getInventory().countItems(temp.getItemId())) + ")"));
         }  isEnoughMaterials = false;
       }
     }
     if (!isEnoughMaterials) {
       return false;
     }

     int countToCreate = 0;
     int weight = 0;

     for (L1ObjectAmount<Integer> makingItem : this._items) {
       temp = ItemTable.getInstance().getTemplate(((Integer)makingItem.getObject()).intValue());
       if (temp.isStackable()) {
         if (!pc.getInventory().checkItem(((Integer)makingItem.getObject()).intValue())) {
           countToCreate++;
         }
       } else {
         countToCreate += makingItem.getAmount() * amount;
       }
       weight += temp.getWeight() * makingItem.getAmount() * amount / 1000;
       long _CountToCreate = countToCreate;

       if (_CountToCreate < 0L || _CountToCreate > 1000L) {
         return false;
       }
     }

     if (pc.getInventory().getSize() + countToCreate >= 200) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(263));
       return false;
     }
     if (pc.getMaxWeight() < pc.getInventory().getWeight() + weight) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(82));
       return false;
     }

     for (L1ObjectAmount<Integer> material : this._materials) {
       if (material.getEnchant() != 0) {
         pc.getInventory().제작리스트1(((Integer)material.getObject()).intValue(), material.getAmount() * amount, material.getEnchant()); continue;
       }
       pc.getInventory().consumeItem(((Integer)material.getObject()).intValue(), material.getAmount() * amount);
     }
     L1ItemInstance item = null;
     for (L1ObjectAmount<Integer> makingItem : this._items) {
       item = pc.getInventory().storeItem(((Integer)makingItem.getObject()).intValue(), makingItem.getAmount() * amount, makingItem.getEnchant());
       if (item != null) {
         item.setEnchantLevel(makingItem.getEnchant());
         String itemName = ItemTable.getInstance().getTemplate(((Integer)makingItem.getObject()).intValue()).getName();
         if (makingItem.getAmount() * amount > 1) {
           itemName = itemName + " (" + (makingItem.getAmount() * amount) + ")";
         }

         if (makingItem.getEnchant() != 0) {
           itemName = ((makingItem.getEnchant() < 0) ? (String)Integer.valueOf(makingItem.getEnchant()) : ("+" + makingItem.getEnchant())) + " " + itemName;
         }
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(143, npcName, itemName));
       }
     }
     return true;
   }

   private int countNumOfMaterials(L1PcInventory inv) {
     int count = Integer.MAX_VALUE;
     for (L1ObjectAmount<Integer> material : this._materials) {
       int numOfSet = inv.countItems(((Integer)material.getObject()).intValue()) / material.getAmount();
       count = Math.min(count, numOfSet);
     }
     return count;
   }


   public L1NpcHtml execute(String actionName, L1PcInstance pc, L1Object obj, byte[] args) {
     int numOfMaterials = countNumOfMaterials(pc.getInventory());
     if (1 < numOfMaterials && this._isAmountInputable) {
       pc.sendPackets((ServerBasePacket)new S_HowManyMake(obj.getId(), numOfMaterials, actionName));
       return null;
     }
     return executeWithAmount(actionName, pc, obj, 1);
   }


   public L1NpcHtml executeWithAmount(String actionName, L1PcInstance pc, L1Object obj, int amount) {
     L1NpcInstance npc = (L1NpcInstance)obj;
     L1NpcHtml result = null;
     if (makeItems(pc, npc.getNpcTemplate().get_name(), amount)) {
       if (this._actionOnSucceed != null) {
         result = this._actionOnSucceed.execute(actionName, pc, obj, new byte[0]);
       }
     }
     else if (this._actionOnFail != null) {
       result = this._actionOnFail.execute(actionName, pc, obj, new byte[0]);
     }

     return (result == null) ? L1NpcHtml.HTML_CLOSE : result;
   }
 }


