     package l1j.server.server.serverpackets;

     import java.util.HashMap;
     import l1j.server.MJCTSystem.Loader.MJCTSpellLoader;
     import l1j.server.MJCTSystem.MJCTSpell;




     public class S_InventoryIcon
       extends ServerBasePacket
     {
       public static final int SHOW_INVEN_BUFFICON = 110;
       public static final int TYPE_EFF_NONE = 0;
       public static final int TYPE_EFF_PERCENT = 1;
       public static final int TYPE_EFF_MINUTE = 2;
       public static final int TYPE_EFF_PERCENT_ORC_SERVER = 3;
       public static final int TYPE_EFF_EINHASAD_COOLTIME_MINUTE = 4;
       public static final int TYPE_EFF_LEGACY_TIME = 5;
       public static final int TYPE_EFF_VARIABLE_VALUE = 6;
       public static final int TYPE_EFF_DAY_HOUR_MIN = 7;
       public static final int TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC = 8;
       public static final int TYPE_EFF_NSERVICE_TOPPING = 9;
       public static final int TYPE_EFF_UNLIMIT = 10;
       public static final int TYPE_EFF_CUSTOM = 11;
       public static final int TYPE_EFF_COUNT = 12;
       private static final HashMap<Integer, StrMessageId> _skillIdToMsgIds = new HashMap<>(8);

       static {
         _skillIdToMsgIds.put(Integer.valueOf(73), new StrMessageId(4737, 0, 4744));




         _skillIdToMsgIds.put(Integer.valueOf(7791), new StrMessageId(4181, 0, 0));
         _skillIdToMsgIds.put(Integer.valueOf(205), new StrMessageId(3074, 0, 0));
         _skillIdToMsgIds.put(Integer.valueOf(210), new StrMessageId(3075, 0, 0));
         _skillIdToMsgIds.put(Integer.valueOf(215), new StrMessageId(1348, 0, 0));
         _skillIdToMsgIds.put(Integer.valueOf(220), new StrMessageId(3073, 0, 0));


         _skillIdToMsgIds.put(Integer.valueOf(707073), new StrMessageId(4737, 0, 4744));
       }


       private static final StrMessageId _dummyId = new StrMessageId(0, 0, 0);

       static class StrMessageId {
         int tooltipId;
         int newId;
         int endId;

         StrMessageId(int t, int n, int e) {
           this.tooltipId = t;
           this.newId = n;
           this.endId = e;
         }
       }

       public static S_InventoryIcon on(int priority, int type, int skillId, int showType, long time, boolean isGood, int invGfx, int tooltip, int startId, int endId) {
         S_InventoryIcon s = new S_InventoryIcon();
         s.writeC(8);
         s.writeC(type);
         s.writeC(16);
         s.writeBit(skillId);
         s.writeC(24);
         s.writeBit(time);
         s.writeC(32);
         s.writeBit(showType);
         s.writeC(40);



         s.writeBit(invGfx);
         s.writeC(48);
         s.writeC(0);
         s.writeC(56);
         s.writeC(priority);
         s.writeC(64);
         s.writeBit(tooltip);
         s.writeC(72);
         s.writeBit(startId);
         s.writeC(80);
         s.writeBit(endId);
         s.writeC(88);
         s.writeC(isGood ? 1 : 0);
         s.writeC(96);
         s.writeC(0);
         s.writeC(104);
         s.writeC(0);
         s.writeC(112);
         s.writeC(0);
         return s;
       }

       public static S_InventoryIcon on(int type, int skillId, long time, boolean isGood, int invGfx) {
         StrMessageId sid = _skillIdToMsgIds.get(Integer.valueOf(skillId));
         if (sid == null)
           sid = _dummyId;
         return on(3, type, skillId, 8, time, isGood, invGfx, sid.tooltipId, sid.newId, sid.endId);
       }

       public static S_InventoryIcon onUnLimit(int type, int skillId, long time, boolean isGood, int invGfx) {
         StrMessageId sid = _skillIdToMsgIds.get(Integer.valueOf(skillId));
         if (sid == null)
           sid = _dummyId;
         return on(3, type, skillId, 10, time, isGood, invGfx, sid.tooltipId, sid.newId, sid.endId);
       }

       public static S_InventoryIcon on(int type, int skillId, long time, boolean isGood) {
         MJCTSpell sp = MJCTSpellLoader.getInstance().get(skillId);
         int ico = 0;
         if (sp != null) {
           ico = sp.xicon;
         }
         StrMessageId sid = _skillIdToMsgIds.get(Integer.valueOf(skillId));
         if (sid == null) {
           sid = _dummyId;
         }
         return on(3, type, skillId, 8, time, isGood, ico, sid.tooltipId, sid.newId, sid.endId);
       }

       public static S_InventoryIcon onUnLimit(int priority, int type, int skillId, long time, boolean isGood) {
         MJCTSpell sp = MJCTSpellLoader.getInstance().get(skillId);
         int ico = 0;
         if (sp != null) {
           ico = sp.xicon;
         }
         StrMessageId sid = _skillIdToMsgIds.get(Integer.valueOf(skillId));
         if (sid == null) {
           sid = _dummyId;
         }
         return on(priority, type, skillId, 10, time, isGood, ico, sid.tooltipId, sid.newId, sid.endId);
       }

       public static S_InventoryIcon icoNew(int skillId, long time, boolean isGood) {
         return on(1, skillId, time, isGood);
       }

       public static S_InventoryIcon icoNew(int skillId, int tooltip, long time, boolean isGood) {
         return on(3, 1, skillId, 8, time, isGood, skillId, tooltip, 0, 0);
       }

       public static S_InventoryIcon iconNewUnLimit(int skillId, boolean isGood) {
         return onUnLimit(3, 1, skillId, 10L, isGood);
       }

       public static S_InventoryIcon iconNewUnLimitAndPriority(int priority, int skillId, boolean isGood) {
         return onUnLimit(priority, 1, skillId, 10L, isGood);
       }

       public static S_InventoryIcon iconNewUnLimit(int skillId, int tooltip, boolean isGood) {
         return on(3, 1, skillId, 10, 1L, isGood, skillId, tooltip, 0, 0);
       }

       public static S_InventoryIcon iconNewUnLimitAndPriority(int priority, int skillId, int tooltip, boolean isGood) {
         return on(priority, 1, skillId, 10, 1L, isGood, skillId, tooltip, 0, 0);
       }

       public static S_InventoryIcon icoReset(int skillId, int tooltip, long time, boolean isGood) {
         return on(3, 2, skillId, 8, time, isGood, skillId, tooltip, 0, 0);
       }

       public static S_InventoryIcon icoReset(int skillId, long time, boolean isGood) {
         return on(2, skillId, time, isGood);
       }

       public static S_InventoryIcon icoEnd(int skillId) {
         S_InventoryIcon s = new S_InventoryIcon();
         s.writeC(8);
         s.writeC(3);
         s.writeC(16);
         s.writeBit(skillId);
         s.writeC(48);
         s.writeC(0);
         s.writeC(80);
         s.writeC(0);
         s.writeH(0);
         return s;
       }

       public S_InventoryIcon() {
         writeC(19);
         writeH(110);
       }

       public byte[] getContent() {
         return getBytes();
       }
     }


