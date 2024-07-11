 package l1j.server.server.serverpackets;
 import java.io.IOException;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import l1j.server.MJPassiveSkill.MJPassiveInfo;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
 import l1j.server.server.SkillCheck;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class S_SpellCheck extends ServerBasePacket {
   private int _skill_id;

   public static S_SpellCheck get(L1PcInstance pc) {
     S_SpellCheck s = new S_SpellCheck();
     List<Integer> skill = SkillCheck.getInstance().CheckSkill(pc);
     ArrayList<MJPassiveInfo> passives = pc.getPassives();
     s.writeD(pc.getId());
     s.writeC(0);
     if ((skill == null && passives == null) || (skill.size() <= 0 && passives.size() <= 0)) {
       s.writeH(0);
       s.writeC(7);
       s.writeC(0);
       return s;
     }
     s.writeH(skill.size() + passives.size());
     if (skill != null) {
       for (Integer spellId : skill) {
         L1ItemInstance item = ItemTable.getInstance().createItem(40005);
         if (spell_info().get(spellId) != null) {
           int i = (((S_SpellCheck)spell_info().get(spellId)).get_skill_gfx() == 0) ? 9568 : ((S_SpellCheck)spell_info().get(spellId)).get_skill_gfx();
           String str = (((S_SpellCheck)spell_info().get(spellId)).get_skill_name() == null) ? "未命名" : ((S_SpellCheck)spell_info().get(spellId)).get_skill_name();
           s.writeD(skill.size());
           s.writeH(i);
           s.writeD(spellId.intValue());
           s.writeS(str);
           s.writeD(0);
           byte[] arrayOfByte = item.serialize();
           s.writeC(arrayOfByte.length);
           s.writeByte(arrayOfByte); continue;
         }
         int gfx_id = 9568;
         String name = "未命名";
         s.writeD(skill.size());
         s.writeH(gfx_id);
         s.writeD(spellId.intValue());
         s.writeS(name);
         s.writeD(0);
         byte[] b = item.serialize();
         s.writeC(b.length);
         s.writeByte(b);
       }
     }


     if (passives != null) {
       for (MJPassiveInfo pInfo : passives) {
         int passiveId = pInfo.getPassiveId() + 1000;
         L1ItemInstance item = ItemTable.getInstance().createItem(40005);
         if (spell_info().get(Integer.valueOf(passiveId)) != null) {
           int i = (((S_SpellCheck)spell_info().get(Integer.valueOf(passiveId))).get_skill_gfx() == 0) ? 9568 : ((S_SpellCheck)spell_info().get(Integer.valueOf(passiveId))).get_skill_gfx();
           String str = (((S_SpellCheck)spell_info().get(Integer.valueOf(passiveId))).get_skill_name() == null) ? "未命名" : ((S_SpellCheck)spell_info().get(Integer.valueOf(passiveId))).get_skill_name();
           s.writeD(passives.size());
           s.writeH(i);
           s.writeD(passiveId - 1000);
           s.writeS(str);
           s.writeD(0);
           byte[] arrayOfByte = item.serialize();
           s.writeC(arrayOfByte.length);
           s.writeByte(arrayOfByte); continue;
         }
         int gfx_id = 9568;
         String name = "未命名";
         s.writeD(skill.size());
         s.writeH(gfx_id);
         s.writeD(passiveId - 1000);
         s.writeS(name);
         s.writeD(0);
         byte[] b = item.serialize();
         s.writeC(b.length);
         s.writeByte(b);
       }
     }


     s.writeH(7);
     return s;
   }
   private String _skill_name; private int _skill_gfx;
   public static HashMap<Integer, S_SpellCheck> spell_info() {
     final HashMap<Integer, S_SpellCheck> list = new HashMap<>(256);
     Selector.exec("select * from spell_check_info", (SelectorHandler)new FullSelectorHandler()
         {
           public void result(ResultSet rs) throws Exception {
             while (rs.next()) {
               S_SpellCheck Info = S_SpellCheck.newInstance(rs);
               if (Info == null)
                 continue;
               list.put(Integer.valueOf(Info.get_skill_id()), Info);
             }
           }
         });
     return list;
   }

   static S_SpellCheck newInstance(ResultSet rs) throws SQLException {
     S_SpellCheck Info = newInstance();
     Info._skill_id = rs.getInt("spellId");
     Info._skill_name = rs.getString("name");
     Info._skill_gfx = rs.getInt("icon");
     return Info;
   }

   private static S_SpellCheck newInstance() {
     return new S_SpellCheck();
   }


   public int get_skill_id() {
     return this._skill_id;
   }

   public String get_skill_name() {
     return this._skill_name;
   }

   public int get_skill_gfx() {
     return this._skill_gfx;
   }

   private S_SpellCheck() {
     writeC(131);
   }


   public byte[] getContent() throws IOException {
     return getBytes();
   }
 }


