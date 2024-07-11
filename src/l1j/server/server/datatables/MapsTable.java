 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.model.map.L1Map;
 import l1j.server.server.model.map.L1WorldMap;
 import l1j.server.server.utils.SQLUtil;

 public final class MapsTable
 {
   public class MapData {
     public String mapName = null;
     public int startX = 0;
     public int endX = 0;
     public int startY = 0;
     public int endY = 0;
     public double monster_amount = 1.0D;
     public double dropRate = 1.0D;
     public boolean isUnderwater = false;
     public boolean markable = false;
     public boolean teleportable = false;
     public boolean escapable = false;
     public boolean isUseResurrection = false;
     public boolean isUsePainwand = false;
     public boolean isEnabledDeathPenalty = false;
     public boolean isTakePets = false;
     public boolean isRecallPets = false;
     public boolean isUsableItem = false;
     public boolean isUsableSkill = false;
     public boolean is_ruler = false;
     public long monster_respawn_seconds = 40000L;
     public boolean PC_TEL = false;

     public String getMapName() {
       return this.mapName;
     }
     public int getStartX() {
       return this.startX;
     }
     public int getStartY() {
       return this.startY;
     }
     public int getEndX() {
       return this.endX;
     }
     public int getEndY() {
       return this.endY;
     }
     public double getMonster_amount() {
       return this.monster_amount;
     }
     public double getDropRate() {
       return this.dropRate;
     }
     public boolean isUnderwater() {
       return this.isUnderwater;
     }
     public boolean isMarkable() {
       return this.markable;
     }
     public boolean isTeleportable() {
       return this.teleportable;
     }
     public boolean isEscapable() {
       return this.escapable;
     }
     public boolean isUseResurrection() {
       return this.isUseResurrection;
     }
     public boolean isUsePainwand() {
       return this.isUsePainwand;
     }
     public boolean isEnabledDeathPenalty() {
       return this.isEnabledDeathPenalty;
     }
     public boolean isTakePets() {
       return this.isTakePets;
     }
     public boolean isRecallPets() {
       return this.isRecallPets;
     }
     public boolean isUsableItem() {
       return this.isUsableItem;
     }
     public boolean isUsableSkill() {
       return this.isUsableSkill;
     }
   }


   private static Logger _log = Logger.getLogger(MapsTable.class.getName());



   private static MapsTable _instance;


   private final Map<Integer, MapData> _maps = new HashMap<>();




   private MapsTable() {
     loadMapsFromDatabase();
   }

   public static void reload() {
     MapsTable oldInstance = _instance;
     _instance = new MapsTable();
     oldInstance._maps.clear();
   }




   private void loadMapsFromDatabase() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM mapids");
       MapData data = null;
       for (rs = pstm.executeQuery(); rs.next(); ) {
         data = new MapData();
         int mapId = rs.getInt("mapid");
         data.mapName = rs.getString("locationname");
         data.startX = rs.getInt("startX");
         data.endX = rs.getInt("endX");
         data.startY = rs.getInt("startY");
         data.endY = rs.getInt("endY");
         data.monster_amount = rs.getDouble("monster_amount");
         data.dropRate = rs.getDouble("drop_rate");
         data.isUnderwater = rs.getBoolean("underwater");
         data.markable = rs.getBoolean("markable");
         data.teleportable = rs.getBoolean("teleportable");
         data.escapable = rs.getBoolean("escapable");
         data.isUseResurrection = rs.getBoolean("resurrection");
         data.isUsePainwand = rs.getBoolean("painwand");
         data.isEnabledDeathPenalty = rs.getBoolean("penalty");
         data.isTakePets = rs.getBoolean("take_pets");
         data.isRecallPets = rs.getBoolean("recall_pets");
         data.isUsableItem = rs.getBoolean("usable_item");
         data.isUsableSkill = rs.getBoolean("usable_skill");
         data.is_ruler = rs.getBoolean("is_ruler");
         data.monster_respawn_seconds = rs.getLong("monster_respawn_second") * 1000L;
         data.PC_TEL = rs.getBoolean("PC_TEL");
         this._maps.put(new Integer(mapId), data);
       }

       _log.config("Maps " + this._maps.size());
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }






   public static MapsTable getInstance() {
     if (_instance == null) {
       _instance = new MapsTable();
     }
     return _instance;
   }








   public int getStartX(int mapId) {
     MapData map = this._maps.get(Integer.valueOf(mapId));
     if (map == null) {
       return 0;
     }
     return ((MapData)this._maps.get(Integer.valueOf(mapId))).startX;
   }








   public int getEndX(int mapId) {
     MapData map = this._maps.get(Integer.valueOf(mapId));
     if (map == null) {
       return 0;
     }
     return ((MapData)this._maps.get(Integer.valueOf(mapId))).endX;
   }








   public int getStartY(int mapId) {
     MapData map = this._maps.get(Integer.valueOf(mapId));
     if (map == null) {
       return 0;
     }
     return ((MapData)this._maps.get(Integer.valueOf(mapId))).startY;
   }








   public int getEndY(int mapId) {
     MapData map = this._maps.get(Integer.valueOf(mapId));
     if (map == null) {
       return 0;
     }
     return ((MapData)this._maps.get(Integer.valueOf(mapId))).endY;
   }








   public double getMonsterAmount(int mapId) {
     MapData map = this._maps.get(Integer.valueOf(mapId));
     if (map == null) {
       return 0.0D;
     }
     return map.monster_amount;
   }








   public double getDropRate(int mapId) {
     MapData map = this._maps.get(Integer.valueOf(mapId));
     if (map == null) {
       return 0.0D;
     }
     return map.dropRate;
   }









   public boolean isUnderwater(int mapId) {
     MapData map = this._maps.get(Integer.valueOf(mapId));
     if (map == null) {
       return false;
     }
     return ((MapData)this._maps.get(Integer.valueOf(mapId))).isUnderwater;
   }








   public boolean isMarkable(int mapId) {
     MapData map = this._maps.get(Integer.valueOf(mapId));
     if (map == null) {
       return false;
     }
     return ((MapData)this._maps.get(Integer.valueOf(mapId))).markable;
   }








   public boolean isTeleportable(int mapId) {
     MapData map = this._maps.get(Integer.valueOf(mapId));
     if (map == null) {
       return false;
     }
     return ((MapData)this._maps.get(Integer.valueOf(mapId))).teleportable;
   }








   public boolean isEscapable(int mapId) {
     MapData map = this._maps.get(Integer.valueOf(mapId));
     if (map == null) {
       return false;
     }
     return ((MapData)this._maps.get(Integer.valueOf(mapId))).escapable;
   }









   public boolean isUseResurrection(int mapId) {
     MapData map = this._maps.get(Integer.valueOf(mapId));
     if (map == null) {
       return false;
     }
     return ((MapData)this._maps.get(Integer.valueOf(mapId))).isUseResurrection;
   }









   public boolean isUsePainwand(int mapId) {
     MapData map = this._maps.get(Integer.valueOf(mapId));
     if (map == null) {
       return false;
     }
     return ((MapData)this._maps.get(Integer.valueOf(mapId))).isUsePainwand;
   }









   public boolean isEnabledDeathPenalty(int mapId) {
     MapData map = this._maps.get(Integer.valueOf(mapId));
     if (map == null) {
       return false;
     }
     return ((MapData)this._maps.get(Integer.valueOf(mapId))).isEnabledDeathPenalty;
   }









   public boolean isTakePets(int mapId) {
     MapData map = this._maps.get(Integer.valueOf(mapId));
     if (map == null) {
       return false;
     }
     return ((MapData)this._maps.get(Integer.valueOf(mapId))).isTakePets;
   }









   public boolean isRecallPets(int mapId) {
     MapData map = this._maps.get(Integer.valueOf(mapId));
     if (map == null) {
       return false;
     }
     return ((MapData)this._maps.get(Integer.valueOf(mapId))).isRecallPets;
   }









   public boolean isUsableItem(int mapId) {
     MapData map = this._maps.get(Integer.valueOf(mapId));
     if (map == null) {
       return false;
     }
     return ((MapData)this._maps.get(Integer.valueOf(mapId))).isUsableItem;
   }









   public boolean isUsableSkill(int mapId) {
     MapData map = this._maps.get(Integer.valueOf(mapId));
     if (map == null) {
       return false;
     }
     return ((MapData)this._maps.get(Integer.valueOf(mapId))).isUsableSkill;
   }

   public String getMapName(int mapId) {
     L1Map map = L1WorldMap.getInstance().getMap((short)mapId);
     MapData data = this._maps.get(Integer.valueOf((map == null) ? mapId : map.getBaseMapId()));
     if (data == null) {
       return null;
     }
     return data.mapName;
   }

   public boolean isRuler(int mapId) {
     MapData map = this._maps.get(Integer.valueOf(mapId));
     return (map == null) ? false : map.is_ruler;
   }

   public boolean isPCTEL(int mapId) {
     MapData map = this._maps.get(Integer.valueOf(mapId));
     return (map == null) ? false : map.PC_TEL;
   }

   public long get_monster_respawn_seconds(int mapId) {
     MapData map = this._maps.get(Integer.valueOf(mapId));
     return (map == null) ? 40000L : map.monster_respawn_seconds;
   }

   public Map<Integer, MapData> getMaps() {
     return this._maps;
   }

   public MapData getMap(int mapId) {
     return this._maps.get(Integer.valueOf(mapId));
   }
 }


