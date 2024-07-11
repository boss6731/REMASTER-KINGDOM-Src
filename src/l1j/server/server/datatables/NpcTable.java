 package l1j.server.server.datatables;

 import java.lang.reflect.Constructor;
 import java.lang.reflect.InvocationTargetException;
 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import java.util.LinkedList;
 import java.util.List;
 import java.util.Map;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.content.NpcContentLoader;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.utils.SQLUtil;


 public class NpcTable
 {
   static Logger _log = Logger.getLogger(NpcTable.class.getName());

   private final boolean _initialized;

   private static NpcTable _instance;

   private final HashMap<Integer, L1Npc> _npcs = new HashMap<>(5120);

   private static final Map<String, Integer> _familyTypes = buildFamily();

   public static NpcTable getInstance() {
     if (_instance == null) {
       _instance = new NpcTable();
     }
     return _instance;
   }

   public static void reload() {
     NpcTable oldInstance = _instance;
     _instance = new NpcTable();
     oldInstance._npcs.clear();
   }

   public boolean isInitialized() {
     return this._initialized;
   }

   private NpcTable() {
     loadNpcData();
     this._initialized = true;
   }

   private void loadNpcBorn() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("select * from npc_born");
       rs = pstm.executeQuery();
       while (rs.next()) {
         int npcid = rs.getInt("npcid");
         L1Npc npc = getTemplate(npcid);
         if (npc == null) {
           System.out.println(String.format("[npc_born 加載失敗]無法找到 NPC ID。 %d\n", new Object[] { Integer.valueOf(npcid) }));
           continue;
         }
         npc.createBorn(rs);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
   }

   private void loadNpcData() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     NpcContentLoader protoInfo = NpcContentLoader.getInstance();
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM npc");
       rs = pstm.executeQuery();
       L1Npc npc = null;
       while (rs.next()) {
         npc = new L1Npc();
         int npcId = rs.getInt("npcid");
         npc.set_npcId(npcId);
         npc.set_nameid(rs.getString("desc"));
         npc.set_name(rs.getString("desc_view"));
         npc.set_gfxid(rs.getInt("sprite_id"));
         npc.setImpl(rs.getString("impl"));
         npc.set_level(rs.getInt("level"));
         npc.set_hp(rs.getInt("hp"));
         npc.set_mp(rs.getInt("mp"));
         npc.set_ac(rs.getInt("ac"));
         npc.set_str(rs.getByte("str"));
         npc.set_con(rs.getByte("con"));
         npc.set_dex(rs.getByte("dex"));
         npc.set_wis(rs.getByte("wis"));
         npc.set_int(rs.getByte("intel"));
         npc.set_mr(rs.getInt("mr"));
         npc.set_exp(rs.getLong("exp"));
         npc.set_lawful(rs.getInt("lawful"));
         npc.set_size(rs.getString("size"));
         npc.set_hard(rs.getBoolean("hard"));
         npc.set_weakAttr(rs.getInt("weakAttr"));
         npc.set_ranged(rs.getInt("ranged"));
         npc.setTaming(rs.getBoolean("Taming"));
         npc.setUseAction(rs.getString("use_action"));
         npc.set_undead(rs.getInt("undead"));
         npc.set_poisonatk(rs.getString("poison_atk"));
         npc.set_poisonatkdmg(rs.getInt("poisonatk_dmg"));
         npc.set_poisonatkchance(rs.getInt("poisonatk_chance"));
         npc.set_poisonatkms(rs.getInt("poisonatk_ms"));
         npc.set_poisonatkSilencems(rs.getInt("poisonatk_Silence_ms"));
         npc.set_paralysisatk(rs.getInt("paralysis_atk"));
         npc.setAgro_lvl(rs.getString("agro_lvl").equalsIgnoreCase("true"));
         npc.set_agro(rs.getBoolean("agro"));
         npc.set_agrososc(rs.getBoolean("agrososc"));
         npc.set_agrocoi(rs.getBoolean("agrocoi"));
         Integer family = _familyTypes.get(rs.getString("family"));
         if (family == null) {
           npc.set_family(0);
         } else {
           npc.set_family(family.intValue());
         }
         int agrofamily = rs.getInt("agrofamily");
         if (npc.get_family() == 0 && agrofamily == 1) {
           npc.set_agrofamily(0);
         } else {
           npc.set_agrofamily(agrofamily);
         }
         npc.set_agrogfxid1(rs.getInt("agrogfxid1"));
         npc.set_agrogfxid2(rs.getInt("agrogfxid2"));
         npc.set_picupitem(rs.getString("picupitem").equalsIgnoreCase("true"));
         npc.set_digestitem(rs.getInt("digestitem"));
         npc.set_hprinterval(rs.getInt("hprinterval"));
         npc.set_hpr(rs.getInt("hpr"));
         npc.set_mprinterval(rs.getInt("mprinterval"));
         npc.set_mpr(rs.getInt("mpr"));
         npc.set_teleport_run(rs.getString("teleport_run").equalsIgnoreCase("true"));
         npc.set_teleport(rs.getString("teleport").equalsIgnoreCase("true"));
         npc.set_randomlevel(rs.getInt("randomlevel"));
         npc.set_randomhp(rs.getInt("randomhp"));
         npc.set_randommp(rs.getInt("randommp"));
         npc.set_randomac(rs.getInt("randomac"));
         npc.set_randomexp(rs.getInt("randomexp"));
         npc.set_randomlawful(rs.getInt("randomlawful"));
         npc.set_damagereduction(rs.getInt("damage_reduction"));
         npc.set_doppel(rs.getString("doppel").equalsIgnoreCase("true"));
         npc.set_IsTU(rs.getString("IsTU").equalsIgnoreCase("true"));
         npc.set_IsErase(rs.getString("IsErase").equalsIgnoreCase("true"));
         npc.setBowActId(rs.getInt("bowActId"));
         npc.setKarma(rs.getInt("karma"));
         npc.setTransformId(rs.getInt("transform_id"));
         npc.setTransformGfxId(rs.getInt("transform_gfxid"));
         npc.setTransformProbability(rs.getInt("transform_probability"));
         npc.setTransformHard(rs.getString("transform_hard").equalsIgnoreCase("true"));
         npc.setTransformdrop(rs.getString("transform_drop").equalsIgnoreCase("true"));
         npc.setLightSize(rs.getInt("light_size"));
         npc.setAmountFixed(rs.getString("amount_fixed").equalsIgnoreCase("true"));
         npc.setChangeHead(rs.getString("change_head").equalsIgnoreCase("true"));
         npc.setDoor(rs.getInt("spawnlist_door"));
         npc.setCountId(rs.getInt("count_map"));
         npc.setCantResurrect(rs.getString("cant_resurrect").equalsIgnoreCase("true"));
         npc.setShapeChange(rs.getString("shape_change").equalsIgnoreCase("true"));
         int infoNumber = rs.getInt("class_id");
         if (infoNumber == 0) {
           infoNumber = protoInfo.getProtoNpcClassId(npc.get_name(), npc.get_nameid(), npc.get_gfxid());
         }
         if (infoNumber == 0) {
           infoNumber = getNpcClassId(npc);
         }

         npc.set_class_id(infoNumber);


         String move = rs.getString("movement");
         if (move.equalsIgnoreCase("true")) {
           npc.setMoveMent(true);
         } else {
           npc.setMoveMent(false);
         }
         npc.setboss(rs.getString("boss").equalsIgnoreCase("true"));


         this._npcs.put(Integer.valueOf(npcId), npc);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
       loadNpcBorn();
     }
   }

   private int getNpcClassId(L1Npc npc) {
     if (npc.get_IsTU()) return 25;

     return 0;
   }

   public boolean shape_change(String type) {
     if (type.equalsIgnoreCase("true"))
       return true;
     if (type.equalsIgnoreCase("false")) {
       return false;
     }
     return false;
   }

   public L1Npc getTemplate(int id) {
     return this._npcs.get(Integer.valueOf(id));
   }

   public L1NpcInstance newNpcInstance(int id) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, IllegalArgumentException {
     L1Npc npcTemp = getTemplate(id);
     if (npcTemp == null) {
       throw new IllegalArgumentException(String.format("NpcTemplate：找不到 %d", new Object[] { Integer.valueOf(id) }));
     }
     String s = npcTemp.getImpl();
     Constructor<?> con = Class.forName("l1j.server.server.model.Instance." + s + "Instance").getConstructors()[0];
     return (L1NpcInstance)con.newInstance(new Object[] { npcTemp });
   }



   public static Map<String, Integer> buildFamily() {
     Map<String, Integer> result = new HashMap<>(256);
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("select distinct(family) as family from npc WHERE NOT trim(family) =''");
       rs = pstm.executeQuery();
       int id = 1;
       while (rs.next()) {
         String family = rs.getString("family");
         result.put(family, Integer.valueOf(id++));
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
     return result;
   }

   public int findNpcIdByName(String name) {
     int nl = name.length();
     for (L1Npc npc : this._npcs.values()) {
       String s = npc.get_name();
       if (s.length() != nl) {
         continue;
       }
       if (s.equals(name))
         return npc.get_npcId();
     }
     return 0;
   }

   public int findNpcIdByNameWithoutSpace(String name) {
     for (L1Npc npc : this._npcs.values()) {
       if (npc.get_name().replace(" ", "").equals(name)) {
         return npc.get_npcId();
       }
     }
     return 0;
   }

   public L1Npc findNpcFromClassId(int classId) {
     for (L1Npc npc : this._npcs.values()) {
       if (npc.get_npc_class_id() == classId) {
         return npc;
       }
     }
     return null;
   }

   public List<L1Npc> findNpcAllFromClassId(int classId) {
     LinkedList<L1Npc> npcs = new LinkedList<>();
     for (L1Npc npc : this._npcs.values()) {
       if (npc.get_npc_class_id() == classId) {
         npcs.add(npc);
       }
     }
     return npcs;
   }
 }


