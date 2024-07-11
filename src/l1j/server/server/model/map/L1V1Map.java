 package l1j.server.server.model.map;

 import java.util.ArrayList;
 import l1j.server.server.datatables.DoorSpawnTable;
 import l1j.server.server.model.Instance.L1DoorInstance;
 import l1j.server.server.types.Point;






 public class L1V1Map
   extends L1Map
 {
   private int _mapId;
   private int _baseMapId;
   private int _worldTopLeftX;
   private int _worldTopLeftY;
   private int _worldBottomRightX;
   private int _worldBottomRightY;
   private byte[][] _map;
   public byte[][] _doorMap;
   private boolean _isUnderwater;
   private boolean _isMarkable;
   private boolean _isTeleportable;
   private boolean _isEscapable;
   private boolean _isUseResurrection;
   private boolean _isUsePainwand;
   private boolean _isEnabledDeathPenalty;
   private boolean _isTakePets;
   private boolean _isRecallPets;
   private boolean _isUsableItem;
   private boolean _isUsableSkill;
   private boolean _is_ruler;
   private boolean _isPC_TEL;
   private boolean _is_Crackinthetower;
   private static final byte BITFLAG_IS_IMPASSABLE = -128;

   protected L1V1Map() {}

   public L1V1Map(int mapId, byte[][] map, int worldTopLeftX, int worldTopLeftY, boolean underwater, boolean markable, boolean teleportable, boolean escapable, boolean useResurrection, boolean usePainwand, boolean enabledDeathPenalty, boolean takePets, boolean recallPets, boolean usableItem, boolean usableSkill, boolean isRuler, boolean isPCTEL, boolean crackinthetower) {
     this._mapId = mapId;
     this._baseMapId = mapId;

     this._doorMap = new byte[map.length][(map[0]).length];
     for (int i = 0; i < map.length; i++) {
       for (int j = 0; j < (map[0]).length; j++) {
         this._doorMap[i][j] = 0;
       }
     }

     this._map = map;
     this._worldTopLeftX = worldTopLeftX;
     this._worldTopLeftY = worldTopLeftY;

     this._worldBottomRightX = worldTopLeftX + map.length - 1;
     this._worldBottomRightY = worldTopLeftY + (map[0]).length - 1;

     this._isUnderwater = underwater;
     this._isMarkable = markable;
     this._isTeleportable = teleportable;
     this._isEscapable = escapable;
     this._isUseResurrection = useResurrection;
     this._isUsePainwand = usePainwand;
     this._isEnabledDeathPenalty = enabledDeathPenalty;
     this._isTakePets = takePets;
     this._isRecallPets = recallPets;
     this._isUsableItem = usableItem;
     this._isUsableSkill = usableSkill;
     this._is_ruler = isRuler;
     this._isPC_TEL = isPCTEL;
     this._is_Crackinthetower = crackinthetower;
   }

   public L1V1Map(L1V1Map map) {
     this._mapId = map._mapId;
     this._baseMapId = map._mapId;

     this._map = new byte[map._map.length][]; int i;
     for (i = 0; i < map._map.length; i++) {
       this._map[i] = (byte[])map._map[i].clone();
     }

     this._doorMap = new byte[this._map.length][(this._map[0]).length];
     for (i = 0; i < this._map.length; i++) {
       for (int j = 0; j < (this._map[0]).length; j++) {
         this._doorMap[i][j] = 0;
       }
     }

     this._worldTopLeftX = map._worldTopLeftX;
     this._worldTopLeftY = map._worldTopLeftY;
     this._worldBottomRightX = map._worldBottomRightX;
     this._worldBottomRightY = map._worldBottomRightY;
   }


   public L1V1Map clone(int id) {
     L1V1Map map = new L1V1Map(this);
     map._mapId = id;
     map._isUnderwater = this._isUnderwater;
     map._isMarkable = this._isMarkable;
     map._isTeleportable = this._isTeleportable;
     map._isEscapable = this._isEscapable;
     map._isUseResurrection = this._isUseResurrection;
     map._isUsePainwand = this._isUsePainwand;
     map._isEnabledDeathPenalty = this._isEnabledDeathPenalty;
     map._isTakePets = this._isTakePets;
     map._isRecallPets = this._isRecallPets;
     map._isUsableItem = this._isUsableItem;
     map._isUsableSkill = this._isUsableSkill;
     map._is_ruler = this._is_ruler;
     map._isPC_TEL = this._isPC_TEL;
     map._is_Crackinthetower = this._is_Crackinthetower;
     return map;
   }

   public int accessTile(int x, int y) {
     if (!isInMap(x, y)) {
       return 0;
     }

     return this._map[x - this._worldTopLeftX][y - this._worldTopLeftY];
   }

   private int accessOriginalTile(int x, int y) {
     return accessTile(x, y) & 0x7F;
   }

   private void setTile(int x, int y, int tile) {
     if (!isInMap(x, y)) {
       return;
     }
     this._map[x - this._worldTopLeftX][y - this._worldTopLeftY] = (byte)tile;
   }

   public byte[][] getRawTiles() {
     return this._map;
   }


   public L1V1Map copyMap(int newMapId) {
     return clone(newMapId);
   }


   public int getId() {
     return this._mapId;
   }


   public int getX() {
     return this._worldTopLeftX;
   }


   public int getY() {
     return this._worldTopLeftY;
   }


   public int getWidth() {
     return this._worldBottomRightX - this._worldTopLeftX + 1;
   }



   public int getHeight() {
     return this._worldBottomRightY - this._worldTopLeftY + 1;
   }


   public int getTile(int x, int y) {
     short tile = (short)this._map[x - this._worldTopLeftX][y - this._worldTopLeftY];
     if (0 != (tile & 0xFFFFFF80)) {
       return 300;
     }
     return accessOriginalTile(x, y);
   }


   public int getOriginalTile(int x, int y) {
     return accessOriginalTile(x, y);
   }


   public boolean isInMap(Point pt) {
     return isInMap(pt.getX(), pt.getY());
   }


   public boolean isInMap(int x, int y) {
     if (this._mapId == 4 && (x < 32520 || y < 32070 || (y < 32190 && x < 33950))) {
       return false;
     }
     return (this._worldTopLeftX <= x && x <= this._worldBottomRightX && this._worldTopLeftY <= y && y <= this._worldBottomRightY);
   }


   public boolean isPassable(Point pt) {
     return isPassable(pt.getX(), pt.getY());
   }


   public boolean isPassable(int x, int y) {
     return (isPassable(x, y - 1, 4) || isPassable(x + 1, y, 6) || isPassable(x, y + 1, 0) || isPassable(x - 1, y, 2));
   }


   public boolean isPassable(Point pt, int heading) {
     return isPassable(pt.getX(), pt.getY(), heading);
   }


   public boolean isUserPassable(int x, int y, int heading) {
     int tile2, tile3, tile4, tile1 = accessTile(x, y);


     switch (heading) {
       case 0:
         tile2 = accessTile(x, y - 1);
         break;
       case 1:
         tile2 = accessTile(x + 1, y - 1);
         break;
       case 2:
         tile2 = accessTile(x + 1, y);
         break;
       case 3:
         tile2 = accessTile(x + 1, y + 1);
         break;
       case 4:
         tile2 = accessTile(x, y + 1);
         break;
       case 5:
         tile2 = accessTile(x - 1, y + 1);
         break;
       case 6:
         tile2 = accessTile(x - 1, y);
         break;
       case 7:
         tile2 = accessTile(x - 1, y - 1);
         break;
       default:
         return false;
     }

     switch (heading) {
       case 0:
         return ((tile1 & 0x2) == 2);

       case 1:
         tile3 = accessTile(x, y - 1);
         tile4 = accessTile(x + 1, y);
         return (((tile1 & 0x2) == 2 && (tile3 & 0x1) == 1) || ((tile1 & 0x1) == 1 && (tile4 & 0x2) == 2));

       case 2:
         return ((tile1 & 0x1) == 1);

       case 3:
         tile3 = accessTile(x, y + 1);
         return ((tile3 & 0x3) == 3 || ((tile1 & 0x1) == 1 && (tile2 & 0x2) == 2));

       case 4:
         return ((tile2 & 0x2) == 2);

       case 5:
         tile3 = accessTile(x, y + 1);
         tile4 = accessTile(x - 1, y);
         return (((tile2 & 0x1) == 1 && (tile3 & 0x2) == 2) || ((tile2 & 0x2) == 2 && (tile4 & 0x1) == 1));

       case 6:
         return ((tile2 & 0x1) == 1);

       case 7:
         tile3 = accessTile(x - 1, y);
         return ((tile3 & 0x3) == 3 || ((tile1 & 0x2) == 2 && (tile2 & 0x1) == 1));
     }




     return false;
   }


   public boolean isPassable(int x, int y, int heading) {
     int tile2, tile3, tile4, tile1 = accessTile(x, y);


     switch (heading) {
       case 0:
         tile2 = accessTile(x, y - 1);
         break;
       case 1:
         tile2 = accessTile(x + 1, y - 1);
         break;
       case 2:
         tile2 = accessTile(x + 1, y);
         break;
       case 3:
         tile2 = accessTile(x + 1, y + 1);
         break;
       case 4:
         tile2 = accessTile(x, y + 1);
         break;
       case 5:
         tile2 = accessTile(x - 1, y + 1);
         break;
       case 6:
         tile2 = accessTile(x - 1, y);
         break;
       case 7:
         tile2 = accessTile(x - 1, y - 1);
         break;
       default:
         return false;
     }

     if (getId() >= 10010 && getId() <= 10100 &&
       tile2 == 12) {
       tile2 = 15;
       return true;
     }


     if ((tile2 & 0xFFFFFF80) == -128) {
       return false;
     }

     switch (heading) {
       case 0:
         return ((tile1 & 0x2) == 2);

       case 1:
         tile3 = accessTile(x, y - 1);
         tile4 = accessTile(x + 1, y);
         return (((tile1 & 0x2) == 2 && (tile3 & 0x1) == 1) || ((tile1 & 0x1) == 1 && (tile4 & 0x2) == 2));

       case 2:
         return ((tile1 & 0x1) == 1);

       case 3:
         tile3 = accessTile(x, y + 1);
         return ((tile3 & 0x3) == 3 || ((tile1 & 0x1) == 1 && (tile2 & 0x2) == 2));

       case 4:
         return ((tile2 & 0x2) == 2);

       case 5:
         tile3 = accessTile(x, y + 1);
         tile4 = accessTile(x - 1, y);
         return (((tile2 & 0x1) == 1 && (tile3 & 0x2) == 2) || ((tile2 & 0x2) == 2 && (tile4 & 0x1) == 1));

       case 6:
         return ((tile2 & 0x1) == 1);

       case 7:
         tile3 = accessTile(x - 1, y);
         return ((tile3 & 0x3) == 3 || ((tile1 & 0x2) == 2 && (tile2 & 0x1) == 1));
     }




     return false;
   }


   public void setPassable(Point pt, boolean isPassable) {
     setPassable(pt.getX(), pt.getY(), isPassable);
   }


   public void setPassable(int x, int y, boolean isPassable) {
     if (isPassable) {
       setTile(x, y, (short)(accessTile(x, y) & 0x7F));
     } else {
       setTile(x, y, (short)(accessTile(x, y) | 0xFFFFFF80));
     }
   }


   public boolean isSafetyZone(Point pt) {
     return isSafetyZone(pt.getX(), pt.getY());
   }


   public boolean isSafetyZone(int x, int y) {
     int tile = accessOriginalTile(x, y);

     return ((tile & 0x30) == 16);
   }


   public boolean isCombatZone(Point pt) {
     return isCombatZone(pt.getX(), pt.getY());
   }


   public boolean isCombatZone(int x, int y) {
     int tile = accessOriginalTile(x, y);

     return ((tile & 0x30) == 32);
   }


   public boolean isNormalZone(Point pt) {
     return isNormalZone(pt.getX(), pt.getY());
   }


   public boolean isNormalZone(int x, int y) {
     int tile = accessOriginalTile(x, y);
     return ((tile & 0x30) == 0);
   }


   public boolean isArrowPassable(Point pt) {
     return isArrowPassable(pt.getX(), pt.getY());
   }


   public boolean isArrowPassable(int x, int y) {
     return ((accessOriginalTile(x, y) & 0xE) != 0);
   }


   public boolean isArrowPassable(Point pt, int heading) {
     return isArrowPassable(pt.getX(), pt.getY(), heading);
   }


   public boolean isArrowPassable(int x, int y, int heading) {
     int tile2, newX, newY, tile3, tile4, tile1 = accessTile(x, y);




     switch (heading) {
       case 0:
         tile2 = accessTile(x, y - 1);
         newX = x;
         newY = y - 1;
         break;
       case 1:
         tile2 = accessTile(x + 1, y - 1);
         newX = x + 1;
         newY = y - 1;
         break;
       case 2:
         tile2 = accessTile(x + 1, y);
         newX = x + 1;
         newY = y;
         break;
       case 3:
         tile2 = accessTile(x + 1, y + 1);
         newX = x + 1;
         newY = y + 1;
         break;
       case 4:
         tile2 = accessTile(x, y + 1);
         newX = x;
         newY = y + 1;
         break;
       case 5:
         tile2 = accessTile(x - 1, y + 1);
         newX = x - 1;
         newY = y + 1;
         break;
       case 6:
         tile2 = accessTile(x - 1, y);
         newX = x - 1;
         newY = y;
         break;
       case 7:
         tile2 = accessTile(x - 1, y - 1);
         newX = x - 1;
         newY = y - 1;
         break;
       default:
         return false;
     }

     if (isExistDoor(newX, newY)) {
       return false;
     }

     switch (heading) {
       case 0:
         return ((tile1 & 0x8) == 8);

       case 1:
         tile3 = accessTile(x, y - 1);
         tile4 = accessTile(x + 1, y);
         return ((tile3 & 0x4) == 4 || (tile4 & 0x8) == 8);

       case 2:
         return ((tile1 & 0x4) == 4);

       case 3:
         tile3 = accessTile(x, y + 1);
         return ((tile3 & 0x4) == 4);

       case 4:
         return ((tile2 & 0x8) == 8);

       case 5:
         return ((tile2 & 0x4) == 4 || (tile2 & 0x8) == 8);

       case 6:
         return ((tile2 & 0x4) == 4);

       case 7:
         tile3 = accessTile(x - 1, y);
         return ((tile3 & 0x8) == 8);
     }




     return false;
   }


   public boolean isUnderwater() {
     return this._isUnderwater;
   }


   public boolean isMarkable() {
     return this._isMarkable;
   }


   public boolean isTeleportable() {
     return this._isTeleportable;
   }


   public boolean isEscapable() {
     return this._isEscapable;
   }


   public boolean isUseResurrection() {
     return this._isUseResurrection;
   }


   public boolean isUsePainwand() {
     return this._isUsePainwand;
   }


   public boolean isEnabledDeathPenalty() {
     return this._isEnabledDeathPenalty;
   }


   public boolean isTakePets() {
     return this._isTakePets;
   }


   public boolean isRecallPets() {
     return this._isRecallPets;
   }


   public boolean isUsableItem() {
     return this._isUsableItem;
   }


   public boolean isUsableSkill() {
     return this._isUsableSkill;
   }


   public boolean isRuler() {
     return this._is_ruler;
   }


   public boolean isPCTEL() {
     return this._isPC_TEL;
   }


   public boolean isCrackIntheTower() {
     return this._is_Crackinthetower;
   }


   public boolean isFishingZone(int x, int y) {
     return (accessOriginalTile(x, y) == 16);
   }


   public boolean isExistDoor(int x, int y) {
     ArrayList<L1DoorInstance> doors = DoorSpawnTable.getInstance().getDoorList();
     for (L1DoorInstance door : doors) {
       if (this._mapId != door.getMapId()) {
         continue;
       }
       if (door.getOpenStatus() == 28) {
         continue;
       }
       if (door.isDead()) {
         continue;
       }
       int leftEdgeLocation = door.getLeftEdgeLocation();
       int rightEdgeLocation = door.getRightEdgeLocation();
       int size = rightEdgeLocation - leftEdgeLocation;
       if (size == 0) {
         if (x == door.getX() && y == door.getY())
           return true;
         continue;
       }
       if (door.getDirection() == 0) {
         for (int doorX = leftEdgeLocation; doorX <= rightEdgeLocation; doorX++) {
           if (x == doorX && y == door.getY())
             return true;
         }
         continue;
       }
       for (int doorY = leftEdgeLocation; doorY <= rightEdgeLocation; doorY++) {
         if (x == door.getX() && y == doorY) {
           return true;
         }
       }
     }


     doors = null;
     return false;
   }


   public String toString(Point pt) {
     return "" + getOriginalTile(pt.getX(), pt.getY());
   }


   public void setId(int mapId) {
     this._mapId = mapId;
   }


   public int getBaseMapId() {
     return this._baseMapId;
   }


   public int getTestTile(int x, int y) {
     System.out.println(x + "+" + y);
     System.out.println(this._worldTopLeftX + "+" + this._worldTopLeftY);
     return this._map[x - this._worldTopLeftX][y - this._worldTopLeftY];
   }


   public L1Map set_isUnderwater(boolean isUnderwater) {
     this._isUnderwater = isUnderwater;
     return this;
   }


   public L1Map set_isMarkable(boolean isMarkable) {
     this._isMarkable = isMarkable;
     return this;
   }


   public L1Map set_isTeleportable(boolean isTeleportable) {
     this._isTeleportable = isTeleportable;
     return this;
   }


   public L1Map set_isEscapable(boolean isEscapable) {
     this._isEscapable = isEscapable;
     return this;
   }


   public L1Map set_isUseResurrection(boolean isUseResurrection) {
     this._isUseResurrection = isUseResurrection;
     return this;
   }


   public L1Map set_isUsePainwand(boolean isUsePainwand) {
     this._isUsePainwand = isUsePainwand;
     return this;
   }


   public L1Map set_isEnabledDeathPenalty(boolean isEnabledDeathPenalty) {
     this._isEnabledDeathPenalty = isEnabledDeathPenalty;
     return this;
   }


   public L1Map set_isTakePets(boolean isTakePets) {
     this._isTakePets = isTakePets;
     return this;
   }


   public L1Map set_isRecallPets(boolean isRecallPets) {
     this._isRecallPets = isRecallPets;
     return this;
   }


   public L1Map set_isUsableItem(boolean isUsableItem) {
     this._isUsableItem = isUsableItem;
     return this;
   }


   public L1Map set_isUsableSkill(boolean isUsableSkill) {
     this._isUsableSkill = isUsableSkill;
     return this;
   }


   public L1Map set_isRuler(boolean isRuler) {
     this._is_ruler = isRuler;
     return this;
   }


   public L1Map set_isPCTEL(boolean isPCTEL) {
     this._isPC_TEL = isPCTEL;
     return this;
   }


   public L1Map set_CrackIntheTower(boolean CrackIntheTower) {
     this._is_Crackinthetower = CrackIntheTower;
     return this;
   }

   public void reset(L1V1Map map) {
     if (map == null || this._map.length != map._map.length)
       return;  int i;
     for (i = 0; i < map._map.length; i++) {
       this._map[i] = (byte[])map._map[i].clone();
     }
     for (i = 0; i < this._map.length; i++) {
       for (int j = 0; j < (this._map[0]).length; j++)
         this._doorMap[i][j] = 0;
     }
   }
 }


