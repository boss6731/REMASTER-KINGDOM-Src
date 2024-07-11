 package l1j.server.server.model.map;

 import java.util.ArrayList;
 import l1j.server.server.datatables.DoorSpawnTable;
 import l1j.server.server.model.Instance.L1DoorInstance;
 import l1j.server.server.types.Point;


 public class L1V2Map
   extends L1Map
 {
   private final int _id;
   private final int _xLoc;
   private final int _yLoc;
   private final int _width;
   private final int _height;
   private final byte[] _map;
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
   private boolean _PC_TEL;
   private boolean _is_Crackinthetower;
   private static final byte BITFLAG_IS_IMPASSABLE = -128;

   private int offset(int x, int y) {
     return (y - this._yLoc) * this._width * 2 + (x - this._xLoc) * 2;
   }

   private int accessOriginalTile(int x, int y) {
     return this._map[offset(x, y)] & Byte.MAX_VALUE;
   }





   public L1V2Map(int id, byte[] map, int xLoc, int yLoc, int width, int height, boolean underwater, boolean markable, boolean teleportable, boolean escapable, boolean useResurrection, boolean usePainwand, boolean enabledDeathPenalty, boolean takePets, boolean recallPets, boolean usableItem, boolean usableSkill, boolean is_ruler, boolean PC_TEL, boolean Crackinthetower) {
     this._id = id;
     this._map = map;
     this._xLoc = xLoc;
     this._yLoc = yLoc;
     this._width = width;
     this._height = height;

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
     this._is_ruler = is_ruler;
     this._PC_TEL = PC_TEL;
     this._is_Crackinthetower = Crackinthetower;
   }



   public int getHeight() {
     return this._height;
   }


   public int getId() {
     return this._id;
   }


   public int getOriginalTile(int x, int y) {
     int lo = this._map[offset(x, y)];
     int hi = this._map[offset(x, y) + 1];
     return lo | hi << 8 & 0xFF00;
   }


   public int getTile(int x, int y) {
     return this._map[offset(x, y)];
   }


   public int getWidth() {
     return this._width;
   }


   public int getX() {
     return this._xLoc;
   }


   public int getY() {
     return this._yLoc;
   }


   public boolean isArrowPassable(Point pt) {
     return isArrowPassable(pt.getX(), pt.getY());
   }


   public boolean isArrowPassable(int x, int y) {
     return (accessOriginalTile(x, y) != 1);
   }


   public boolean isArrowPassable(Point pt, int heading) {
     return isArrowPassable(pt.getX(), pt.getY(), heading);
   }



   public boolean isArrowPassable(int x, int y, int heading) {
     int tile;
     int newX;
     int newY;
     switch (heading) { case 0:
         tile = accessOriginalTile(x, y - 1); newX = x; newY = y - 1; break;
       case 1: tile = accessOriginalTile(x + 1, y - 1); newX = x + 1; newY = y - 1; break;
       case 2: tile = accessOriginalTile(x + 1, y); newX = x + 1; newY = y; break;
       case 3: tile = accessOriginalTile(x + 1, y + 1); newX = x + 1; newY = y + 1; break;
       case 4: tile = accessOriginalTile(x, y + 1); newX = x; newY = y + 1; break;
       case 5: tile = accessOriginalTile(x - 1, y + 1); newX = x - 1; newY = y + 1; break;
       case 6: tile = accessOriginalTile(x - 1, y); newX = x - 1; newY = y; break;
       case 7: tile = accessOriginalTile(x - 1, y - 1); newX = x - 1; newY = y - 1; break;
       default: return false; }

     if (isExistDoor(newX, newY)) {
       return false;
     }
     return (tile != 1);
   }


   public boolean isCombatZone(Point pt) {
     return isCombatZone(pt.getX(), pt.getY());
   }


   public boolean isCombatZone(int x, int y) {
     return (accessOriginalTile(x, y) == 8);
   }


   public boolean isInMap(Point pt) {
     return isInMap(pt.getX(), pt.getY());
   }


   public boolean isInMap(int x, int y) {
     return (this._xLoc <= x && x < this._xLoc + this._width && this._yLoc <= y && y < this._yLoc + this._height);
   }



   public boolean isNormalZone(Point pt) {
     return isNormalZone(pt.getX(), pt.getY());
   }


   public boolean isNormalZone(int x, int y) {
     return (!isCombatZone(x, y) && !isSafetyZone(x, y));
   }


   public boolean isPassable(Point pt) {
     return isPassable(pt.getX(), pt.getY());
   }



   public boolean isPassable(int x, int y) {
     int tile = accessOriginalTile(x, y);
     if (tile == 1 || tile == 9 || tile == 65 || tile == 69 || tile == 73) {
       return false;
     }
     if (0 != (this._map[offset(x, y)] & Byte.MIN_VALUE)) {
       return false;
     }
     return true;
   }


   public boolean isPassable(Point pt, int heading) {
     return isPassable(pt.getX(), pt.getY(), heading);
   }


   public boolean isUserPassable(int x, int y, int heading) {
     int tile;
     switch (heading) { case 0:
         tile = accessOriginalTile(x, y - 1); break;
       case 1: tile = accessOriginalTile(x + 1, y - 1); break;
       case 2: tile = accessOriginalTile(x + 1, y); break;
       case 3: tile = accessOriginalTile(x + 1, y + 1); break;
       case 4: tile = accessOriginalTile(x, y + 1); break;
       case 5: tile = accessOriginalTile(x - 1, y + 1); break;
       case 6: tile = accessOriginalTile(x - 1, y); break;
       case 7: tile = accessOriginalTile(x - 1, y - 1); break;
       default: return false; }


     if (tile == 1 || tile == 9 || tile == 65 || tile == 69 || tile == 73) {
       return false;
     }
     return true;
   }


   public boolean isPassable(int x, int y, int heading) {
     int tile;
     switch (heading) { case 0:
         tile = accessOriginalTile(x, y - 1); break;
       case 1: tile = accessOriginalTile(x + 1, y - 1); break;
       case 2: tile = accessOriginalTile(x + 1, y); break;
       case 3: tile = accessOriginalTile(x + 1, y + 1); break;
       case 4: tile = accessOriginalTile(x, y + 1); break;
       case 5: tile = accessOriginalTile(x - 1, y + 1); break;
       case 6: tile = accessOriginalTile(x - 1, y); break;
       case 7: tile = accessOriginalTile(x - 1, y - 1); break;
       default: return false; }


     if (tile == 1 || tile == 9 || tile == 65 || tile == 69 || tile == 73) {
       return false;
     }
     if (0 != (this._map[offset(x, y)] & Byte.MIN_VALUE)) {
       return false;
     }
     return true;
   }


   public boolean isSafetyZone(Point pt) {
     return isSafetyZone(pt.getX(), pt.getY());
   }


   public boolean isSafetyZone(int x, int y) {
     return (accessOriginalTile(x, y) == 4);
   }


   public void setPassable(Point pt, boolean isPassable) {
     setPassable(pt.getX(), pt.getY(), isPassable);
   }


   public void setPassable(int x, int y, boolean isPassable) {
     if (isPassable) {
       this._map[offset(x, y)] = (byte)(this._map[offset(x, y)] & Byte.MAX_VALUE);
     } else {
       this._map[offset(x, y)] = (byte)(this._map[offset(x, y)] | Byte.MIN_VALUE);
     }
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


   public boolean isFishingZone(int x, int y) {
     return (accessOriginalTile(x, y) == 16);
   }


   public boolean isExistDoor(int x, int y) {
     ArrayList<L1DoorInstance> doors = DoorSpawnTable.getInstance().getDoorList();
     for (L1DoorInstance door : doors) {
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
         int doorX = leftEdgeLocation;
         for (; doorX <= rightEdgeLocation; doorX++) {
           if (x == doorX && y == door.getY())
             return true;
         }
         continue;
       }
       int doorY = leftEdgeLocation;
       for (; doorY <= rightEdgeLocation; doorY++) {
         if (x == door.getX() && y == doorY) {
           return true;
         }
       }
     }


     doors = null;
     return false;
   }


   public String toString(Point pt) {
     int tile = getOriginalTile(pt.getX(), pt.getY());
     return (tile & 0xFF) + " " + (tile >> 8 & 0xFF);
   }


   public L1V1Map copyMap(int a) {
     return null;
   }


   public void setId(int mapId) {}


   public int getBaseMapId() {
     return getId();
   }


   public int getTestTile(int x, int y) {
     return this._map[offset(x, y)];
   }


   public boolean isRuler() {
     return this._is_ruler;
   }


   public boolean isPCTEL() {
     return this._PC_TEL;
   }


   public boolean isCrackIntheTower() {
     return this._is_Crackinthetower;
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
     this._PC_TEL = isPCTEL;
     return this;
   }


   public L1Map set_CrackIntheTower(boolean CrackIntheTower) {
     this._is_Crackinthetower = CrackIntheTower;
     return this;
   }
 }


