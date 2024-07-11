 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.List;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.model.L1ArmorSet;
 import l1j.server.server.templates.L1ArmorSets;
 import l1j.server.server.utils.SQLUtil;

 public class ArmorSetTable {
   private static ArmorSetTable _instance;

   public static ArmorSetTable getInstance() {
     if (_instance == null) {
       _instance = new ArmorSetTable();
     }
     return _instance;
   }
   private List<L1ArmorSet.L1ArmorSetImpl> m_armor_sets;
   public static void reload() {
     _instance = new ArmorSetTable();
   }


   private ArmorSetTable() {
     load();
   }

   private void load() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM armor_set");
       rs = pstm.executeQuery();
       fillTable(rs);
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   private void fillTable(ResultSet rs) throws SQLException {
     List<L1ArmorSet.L1ArmorSetImpl> armor_sets = new ArrayList<>();
     L1ArmorSets as = null;
     while (rs.next()) {
       as = new L1ArmorSets();
       as.setId(rs.getInt("id"));
       as.setSets(rs.getString("sets"));
       as.set_main_id(rs.getInt("main_id"));
       as.setPolyId(rs.getInt("polyid"));
       as.setPolyDesc(rs.getInt("polyDesc"));
       as.setAc(rs.getInt("ac"));
       as.setHp(rs.getInt("hp"));
       as.setMp(rs.getInt("mp"));
       as.setHpr(rs.getInt("hpr"));
       as.setMpr(rs.getInt("mpr"));
       as.setMr(rs.getInt("mr"));
       as.setStr(rs.getInt("str"));
       as.setDex(rs.getInt("dex"));
       as.setCon(rs.getInt("con"));
       as.setWis(rs.getInt("wis"));
       as.setCha(rs.getInt("cha"));
       as.setIntl(rs.getInt("intl"));
       as.set_defense_water(rs.getInt("defense_water"));
       as.set_defense_earth(rs.getInt("defense_earth"));
       as.set_defense_wind(rs.getInt("defense_wind"));
       as.set_defense_fire(rs.getInt("defense_fire"));
       as.set_sp(rs.getInt("sp"));
       as.set_melee_damage(rs.getInt("melee_damage"));
       as.set_melee_hit(rs.getInt("melee_hit"));
       as.set_missile_damage(rs.getInt("missile_damage"));
       as.set_missile_hit(rs.getInt("missile_hit"));
       as.set_defense_all(rs.getInt("defense_all"));
       as.setTechniqueTolerance(rs.getInt("ability_resis"));
       as.setTechniqueHit(rs.getInt("ability_pierce"));
       as.setSpiritTolerance(rs.getInt("spirit_resis"));
       as.setSpiritHit(rs.getInt("spirit_pierce"));
       as.setDragonLangTolerance(rs.getInt("dragonS_resis"));
       as.setDragonLangHit(rs.getInt("dragonS_pierce"));
       as.setFearTolerance(rs.getInt("fear_resis"));
       as.setFearHit(rs.getInt("fear_pierce"));
       as.setAllTolerance(rs.getInt("all_resis"));
       as.setAllHit(rs.getInt("all_pierce"));

       as.set_regist_calcPcDefense(rs.getInt("PVPcalcPcDefense"));
       as.set_regist_PVPweaponTotalDamage(rs.getInt("PVPweaponTotalDamage"));
       as.setWeightReduction(rs.getInt("weight_reduction"));
       as.setMagicHitup(rs.getInt("magic_hit_up"));

       L1ArmorSet.L1ArmorSetImpl impl = L1ArmorSet.L1ArmorSetImpl.newInstance(as);
       armor_sets.add(impl);
     }
     this.m_armor_sets = armor_sets;
   }

   public Collection<L1ArmorSet.L1ArmorSetImpl> values() {
     return this.m_armor_sets;
   }

   public L1ArmorSet.L1ArmorSetImpl find(int main_id) {
     for (L1ArmorSet.L1ArmorSetImpl impl : values()) {
       if (impl.get_source_effects().get_main_id() == main_id)
         return impl;
     }
     return null;
   }
 }


