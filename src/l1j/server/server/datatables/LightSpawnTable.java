 package l1j.server.server.datatables;

 import java.lang.reflect.Constructor;
 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.IdFactory;
 import l1j.server.server.model.Instance.L1FieldObjectInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.utils.SQLUtil;



 public class LightSpawnTable
 {
   private static LightSpawnTable _instance;

   public static LightSpawnTable getInstance() {
     if (_instance == null) {
       _instance = new LightSpawnTable();
     }
     return _instance;
   }

   private LightSpawnTable() {
     FillLightSpawnTable();
   }

   public void FillLightSpawnTable() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM spawnlist_light");
       rs = pstm.executeQuery();

       while (rs.next()) {


         L1Npc l1npc = NpcTable.getInstance().getTemplate(rs.getInt(2));
         if (l1npc != null) {
           String s = l1npc.getImpl();

           Constructor<?> constructor = Class.forName("l1j.server.server.model.Instance." + s + "Instance").getConstructors()[0];
           Object[] parameters = { l1npc };
           L1FieldObjectInstance field = (L1FieldObjectInstance)constructor.newInstance(parameters);
           field = (L1FieldObjectInstance)constructor.newInstance(parameters);
           field.setId(IdFactory.getInstance().nextId());
           field.setX(rs.getInt("locx"));
           field.setY(rs.getInt("locy"));
           field.setMap((short)rs.getInt("mapid"));
           field.setHomeX(field.getX());
           field.setHomeY(field.getY());
           field.setHeading(0);
           field.setLightSize(l1npc.getLightSize());

           L1World.getInstance().storeObject((L1Object)field);
           L1World.getInstance().addVisibleObject((L1Object)field);
         }
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public void storeSpawn(L1PcInstance pc, L1Npc npc) {
     Connection con = null;
     PreparedStatement pstm = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();

       pstm = con.prepareStatement("INSERT INTO spawnlist_light SET npcid=?,locx=?,locy=?,mapid=?");
       pstm.setInt(1, npc.get_npcId());
       pstm.setInt(2, pc.getX());
       pstm.setInt(3, pc.getY());
       pstm.setInt(4, pc.getMapId());
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {

       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }
 }


