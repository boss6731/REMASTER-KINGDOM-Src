 package l1j.server.server.datatables;

 import java.lang.reflect.Constructor;
 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.ArrayList;
 import java.util.HashMap;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.IdFactory;
 import l1j.server.server.model.Instance.L1DoorInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.utils.SQLUtil;



 public class DoorSpawnTable
 {
   private static DoorSpawnTable _instance;
   private final ArrayList<L1DoorInstance> _doorList = new ArrayList<>();

   private final HashMap<Integer, L1DoorInstance> _doorMap = new HashMap<>(256);

   public static DoorSpawnTable getInstance() {
     if (_instance == null) {
       _instance = new DoorSpawnTable();
     }
     return _instance;
   }

   private DoorSpawnTable() {
     FillDoorSpawnTable();
   }

   private void FillDoorSpawnTable() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM spawnlist_door");
       rs = pstm.executeQuery();

       while (rs.next()) {



         L1Npc l1npc = NpcTable.getInstance().getTemplate(81158);
         if (l1npc != null) {
           String s = l1npc.getImpl();

           Constructor<?> constructor = Class.forName("l1j.server.server.model.Instance." + s + "Instance").getConstructors()[0];
           Object[] parameters = { l1npc };
           L1DoorInstance door = (L1DoorInstance)constructor.newInstance(parameters);
           door = (L1DoorInstance)constructor.newInstance(parameters);
           door.setId(IdFactory.getInstance().nextId());

           door.setDoorId(rs.getInt(1));
           door.setCurrentSprite(rs.getInt(3));
           door.setX(rs.getInt(4));
           door.setY(rs.getInt(5));
           door.setMap((short)rs.getInt(6));
           door.setHomeX(rs.getInt(4));
           door.setHomeY(rs.getInt(5));
           door.setDirection(rs.getInt(7));
           door.setLeftEdgeLocation(rs.getInt(8));
           door.setRightEdgeLocation(rs.getInt(9));
           door.setMaxHp(rs.getInt(10));
           door.setCurrentHp(rs.getInt(10));
           door.setKeeperId(rs.getInt(11));
           door.isPassibleDoor(false);
           L1World.getInstance().storeObject((L1Object)door);
           L1World.getInstance().addVisibleObject((L1Object)door);

           this._doorList.add(door);
           this._doorMap.put(Integer.valueOf(door.getDoorId()), door);
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

   public ArrayList<L1DoorInstance> getDoorList() {
     return this._doorList;
   }

   public L1DoorInstance getDoor(int npcId) {
     L1DoorInstance sTemp = null;
     for (L1DoorInstance door : (L1DoorInstance[])this._doorList.<L1DoorInstance>toArray(new L1DoorInstance[this._doorList.size()])) {
       if (door.getDoorId() == npcId) {
         sTemp = door;
         break;
       }
     }
     return sTemp;
   }

   public L1DoorInstance get(int i) {
     return this._doorMap.get(Integer.valueOf(i));
   }
 }


