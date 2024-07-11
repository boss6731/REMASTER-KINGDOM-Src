     package MJShiftObject.DB;
     import java.util.HashMap;

     public enum MJEShiftDBName {
       CHARACTERS(0, "characters"),
       CHARACTERS_BUFF(1, "character_buff"),
       CHARACTERS_CONFIG(2, "character_config"),
       CHARACTERS_ITEMS(3, "character_items"),
       CHARACTERS_SKILLS(4, "character_skills"),
       PASSIVE_USER_INFO(5, "passive_user_info"),
       CHARACTERS_SLOT_ITEMS(6, "character_slot_items"),
       CHARACTERS_TAMS(7, "character_tams"),
       CHARACTERS_BONUS(8, "tb_character_bonus"),
       ACCOUNTS(9, "accounts"),
       OBJECT_CONVERTER(10, "object_converter"),
       CHARACTERS_QUEST(11, "character_quests"),
       CHARACTERS_PICKUP(12, "character_pickups"),
       CHARACTERS_CACHE(13, "character_cache"); private int m_idx;
       private String m_name;
       public static final HashMap<Integer, MJEShiftDBName> m_names;

       MJEShiftDBName(int idx, String name) {
         this.m_idx = idx;
         this.m_name = name;
       }


       static {
         MJEShiftDBName[] names = values();
         m_names = new HashMap<>(names.length);
         for (MJEShiftDBName name : names)
           m_names.put(Integer.valueOf(name.toInt()), name);
       }

       public static MJEShiftDBName fromInt(int i) {
         return m_names.get(Integer.valueOf(i));
       }

       public int toInt() {
         return this.m_idx;
       }

       public String get_table_name(String server_identity) {
         return MJString.isNullOrEmpty(server_identity) ? this.m_name : String.format("%s_%s", new Object[] { server_identity, this.m_name });
       }
     }


