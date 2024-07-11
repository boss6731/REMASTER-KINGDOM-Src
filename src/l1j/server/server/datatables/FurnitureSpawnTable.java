 package l1j.server.server.datatables;

 import java.lang.reflect.Constructor;
 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.IdFactory;
 import l1j.server.server.model.Instance.L1FurnitureInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.utils.SQLUtil;

 public class FurnitureSpawnTable {
   private static FurnitureSpawnTable _instance;

   public static FurnitureSpawnTable getInstance() {
     if (_instance == null) {
       _instance = new FurnitureSpawnTable();
     }
     return _instance;
   }

   private FurnitureSpawnTable() {
     FillFurnitureSpawnTable();
   }

   private void FillFurnitureSpawnTable() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM spawnlist_furniture");
       rs = pstm.executeQuery();

       while (rs.next()) {



         L1Npc l1npc = NpcTable.getInstance().getTemplate(rs.getInt(2));
         if (l1npc != null) {
           String s = l1npc.getImpl();
           Constructor<?> constructor = Class.forName("l1j.server.server.model.Instance." + s + "Instance").getConstructors()[0];
           Object[] parameters = { l1npc };
           L1FurnitureInstance furniture = (L1FurnitureInstance)constructor.newInstance(parameters);
           furniture = (L1FurnitureInstance)constructor.newInstance(parameters);
           furniture.setId(IdFactory.getInstance().nextId());

           furniture.setItemObjId(rs.getInt(1));
           furniture.setX(rs.getInt(3));
           furniture.setY(rs.getInt(4));
           furniture.setMap((short)rs.getInt(5));
           furniture.setHomeX(furniture.getX());
           furniture.setHomeY(furniture.getY());
           furniture.setHeading(0);

           L1World.getInstance().storeObject((L1Object)furniture);
           L1World.getInstance().addVisibleObject((L1Object)furniture);
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

   public void insertFurniture(L1FurnitureInstance furniture) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("INSERT INTO spawnlist_furniture SET item_obj_id=?, npcid=?, locx=?, locy=?, mapid=?");
       pstm.setInt(1, furniture.getItemObjId());
       pstm.setInt(2, furniture.getNpcTemplate().get_npcId());
       pstm.setInt(3, furniture.getX());
       pstm.setInt(4, furniture.getY());
       pstm.setInt(5, furniture.getMapId());
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public void deleteFurniture(L1FurnitureInstance furniture) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("DELETE FROM spawnlist_furniture WHERE item_obj_id=?");
       pstm.setInt(1, furniture.getItemObjId());
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }
 }


