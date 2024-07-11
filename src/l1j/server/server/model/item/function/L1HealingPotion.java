 package l1j.server.server.model.item.function;

 import java.io.File;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Random;
 import java.util.concurrent.CopyOnWriteArrayList;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import javax.xml.bind.JAXBContext;
 import javax.xml.bind.Unmarshaller;
 import javax.xml.bind.annotation.XmlAccessType;
 import javax.xml.bind.annotation.XmlAccessorType;
 import javax.xml.bind.annotation.XmlAttribute;
 import javax.xml.bind.annotation.XmlElement;
 import javax.xml.bind.annotation.XmlRootElement;
 import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatInfo;
 import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatLoader;
 import l1j.server.Config;
 import l1j.server.MJPassiveSkill.MJPassiveID;
 import l1j.server.MJTemplate.Chain.Etc.MJHealingPotionDrinkChain;
 import l1j.server.MJTemplate.MJRnd;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SkillSound;
 import l1j.server.server.serverpackets.ServerBasePacket;




 @XmlAccessorType(XmlAccessType.FIELD)
 public class L1HealingPotion
 {
   private static Logger _log = Logger.getLogger(L1HealingPotion.class.getName());

   private static Random _random = new Random(System.nanoTime());
   private static final String _path = "./data/xml/Item/HealingPotion.xml";

   @XmlAccessorType(XmlAccessType.FIELD)
   @XmlRootElement(name = "ItemEffectList")
   private static class ItemEffectList implements Iterable<L1HealingPotion> { @XmlElement(name = "Item")
     private List<L1HealingPotion> _list;

     public Iterator<L1HealingPotion> iterator() {
       return this._list.iterator();
     } }


   @XmlAccessorType(XmlAccessType.FIELD)
   private static class Effect {
     @XmlAttribute(name = "Min")
     private int _min;

     private int getMin() {
       return this._min;
     }
     @XmlAttribute(name = "Max")
     private int _max; @XmlAttribute(name = "GfxId")
     private int _gfxid; @XmlAttribute(name = "MapId")
     private int _mapid;
     private int getMax() {
       return this._max;
     }




     private int getGfxId() {
       return this._gfxid;
     }




     private int getMapId() {
       return this._mapid;
     }
   }



   private static HashMap<Integer, L1HealingPotion> _dataMap = new HashMap<>(); @XmlAttribute(name = "ItemId")
   private int _itemId;
   public static L1HealingPotion get(int id) {
     return _dataMap.get(Integer.valueOf(id));
   }
   @XmlAttribute(name = "Remove")
   private int _remove; @XmlElement(name = "Effect")
   private CopyOnWriteArrayList<Effect> _effects;

   private int getItemId() {
     return this._itemId;
   }




   private int getRemove() {
     return this._remove;
   }




   private List<Effect> getEffects() {
     return this._effects;
   }



     // 加載 XML 方法，將數據加載到哈希映射中
     private static void loadXml(HashMap<Integer, L1HealingPotion> dataMap) {
         try {
                // 創建 JAXB 上下文，指定要解析的類
             JAXBContext context = JAXBContext.newInstance(new Class[] { ItemEffectList.class });

                // 創建解組器（Unmarshaller）
             Unmarshaller um = context.createUnmarshaller();

                // 指定 XML 文件的路徑
             File file = new File("./data/xml/Item/HealingPotion.xml");

                // 將 XML 文件解組為對象列表
             ItemEffectList list = (ItemEffectList)um.unmarshal(file);

                // 遍歷解組得到的每個物品
             for (L1HealingPotion each : list) {
                    // 檢查物品 ID 是否存在於 ItemTable 中
                 if (ItemTable.getInstance().getTemplate(each.getItemId()) == null) {
                        // 如果物品 ID 不存在，輸出錯誤信息並跳過本次迭代
                     System.out.print("物品 ID " + each.getItemId() + " 的模板未找到。");
                     continue;
                 }
                    // 將物品添加到哈希映射中
                 dataMap.put(Integer.valueOf(each.getItemId()), each);
             }

         } catch (Exception e) {
            // 如果出現異常，記錄錯誤信息
             _log.log(Level.SEVERE, "./data/xml/Item/HealingPotion.xml 加載失敗。", e);
         }
     }


   public static void load() {
     loadXml(_dataMap);
   }

   public static void reload() {
     HashMap<Integer, L1HealingPotion> dataMap = new HashMap<>();
     loadXml(dataMap);
     _dataMap = dataMap;
   }

   public boolean use(L1PcInstance pc, L1ItemInstance item) {
     if (pc.hasSkillEffect(71) == true) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(698));
       return false;
     }

     cancelAbsoluteBarrier(pc);


     int maxChargeCount = item.getItem().getMaxChargeCount();
     int chargeCount = item.getChargeCount();
     if (maxChargeCount > 0 && chargeCount <= 0) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
       return false;
     }

     Effect effect = null;
     for (Effect each : getEffects()) {
       if (each.getMapId() != 0 && pc.getMapId() != each.getMapId()) {
         continue;
       }
       effect = each;
     }


     if (effect == null) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
       return false;
     }

     pc.sendPackets((ServerBasePacket)new S_SkillSound(pc.getId(), effect.getGfxId()));
     pc.broadcastPacket((ServerBasePacket)new S_SkillSound(pc.getId(), effect.getGfxId()));
     pc.sendPackets((ServerBasePacket)new S_ServerMessage(77));

     AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(pc.getId());
     int chance = effect.getMax() - effect.getMin();
     double healHp = effect.getMin();
     int crichance = MJRnd.next(10000);
     int potioncri = 0;
     if (Info != null &&
       Info.get_potion() != 0) {
       potioncri = Info.get_potion_val_1();
     }

     if (chance > 0) {
       if (potioncri != 0 && crichance < potioncri) {
         healHp += (effect.getMax() - effect.getMin());
       } else {

         healHp += (_random.nextInt(chance) + 1);
       }
     }



     healHp = MJHealingPotionDrinkChain.getInstance().do_drink(pc, healHp);

     healHp *= pc.getPotionRecoveryRatePct() / 100.0D + 1.0D;

     if (pc != null && pc.isPassive(MJPassiveID.SURVIVE.toInt())) {





       int con = pc.getAbility().getCon() * 2;
       int check_hp = (int)Math.round(pc.getMaxHp() * 0.45D);
       if (pc.getCurrentHp() < check_hp) {
         int percent = Config.MagicAdSetting_Fencer.SURVIVE_PER * 10000;
         int plus_heal = (int)Math.round(healHp * (con * Config.MagicAdSetting_Fencer.SURVIVE_ADDITION) / 100.0D);
         if (MJRnd.isWinning(1000000, percent) &&
           pc.getInventory().consumeItem(41246, plus_heal)) {
           int potion_effect = 0;
           switch (item.getItemId()) {
             case 40010:
             case 40019:
             case 40022:
             case 40029:
             case 42658:
             case 140010:
             case 240010:
             case 4100152:
             case 4100464:
             case 4100691:
               potion_effect = 18566;
               break;
             case 7007:
             case 40011:
             case 40020:
             case 40023:
             case 140011:
             case 4100153:
             case 4100475:
             case 4100657:
             case 4100692:
               potion_effect = 18568;
               break;
             case 7008:
             case 40012:
             case 40021:
             case 40024:
             case 140012:
             case 4100021:
             case 4100154:
             case 4100658:
             case 4100693:
               potion_effect = 18570;
               break;
           }
           pc.send_effect(potion_effect, false);
           pc.getInventory().consumeItem(41246, plus_heal);
           healHp += plus_heal;
         }
       }
     }


     pc.setCurrentHp(pc.getCurrentHp() + (int)healHp);



     if (getRemove() > 0) {
       if (chargeCount > 0) {
         item.setChargeCount(chargeCount - getRemove());
         pc.getInventory().updateItem(item, 128);
       } else {
         pc.getInventory().removeItem(item, getRemove());
       }
     }
     return true;
   }



   private void cancelAbsoluteBarrier(L1PcInstance pc) {
     if (pc.hasSkillEffect(78)) {
       pc.removeSkillEffect(78);
       pc.sendPackets((ServerBasePacket)new S_PacketBox(180, 43, false));
     }
   }
 }


