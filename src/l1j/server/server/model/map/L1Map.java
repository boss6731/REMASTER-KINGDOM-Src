 package l1j.server.server.model.map;

 import l1j.server.server.types.Point;

 public abstract class L1Map {
   private static L1NullMap _nullMap = new L1NullMap();


   public abstract int getId();


   public abstract void setId(int paramInt);


   public abstract int getBaseMapId();


   public abstract int getX();


   public abstract int getY();


   public abstract int getWidth();


   public abstract int getHeight();


   public abstract int getTile(int paramInt1, int paramInt2);


   public abstract int getTestTile(int paramInt1, int paramInt2);


   public abstract int getOriginalTile(int paramInt1, int paramInt2);


   public abstract boolean isInMap(Point paramPoint);


   public abstract boolean isInMap(int paramInt1, int paramInt2);


   public abstract boolean isPassable(Point paramPoint);


   public abstract boolean isPassable(int paramInt1, int paramInt2);

   public abstract boolean isPassable(Point paramPoint, int paramInt);

   public abstract boolean isUserPassable(int paramInt1, int paramInt2, int paramInt3);

   public abstract boolean isPassable(int paramInt1, int paramInt2, int paramInt3);

   public abstract void setPassable(Point paramPoint, boolean paramBoolean);

   public abstract void setPassable(int paramInt1, int paramInt2, boolean paramBoolean);

   public abstract boolean isSafetyZone(Point paramPoint);

   public abstract boolean isSafetyZone(int paramInt1, int paramInt2);

   public abstract boolean isCombatZone(Point paramPoint);

   public abstract boolean isCombatZone(int paramInt1, int paramInt2);

   public abstract boolean isNormalZone(Point paramPoint);

   public abstract boolean isNormalZone(int paramInt1, int paramInt2);

   public abstract boolean isArrowPassable(Point paramPoint);

   public abstract boolean isArrowPassable(int paramInt1, int paramInt2);

   public abstract boolean isArrowPassable(Point paramPoint, int paramInt);

   public abstract boolean isArrowPassable(int paramInt1, int paramInt2, int paramInt3);

   public abstract boolean isUnderwater();

   public abstract L1Map set_isUnderwater(boolean paramBoolean);

   public abstract boolean isMarkable();

   public abstract L1Map set_isMarkable(boolean paramBoolean);

   public abstract boolean isTeleportable();

   public abstract L1Map set_isTeleportable(boolean paramBoolean);

   public abstract boolean isEscapable();

   public abstract L1Map set_isEscapable(boolean paramBoolean);

   public abstract boolean isUseResurrection();

   public abstract L1Map set_isUseResurrection(boolean paramBoolean);

   public abstract boolean isUsePainwand();

   public abstract L1Map set_isUsePainwand(boolean paramBoolean);

   public abstract boolean isEnabledDeathPenalty();

   public abstract L1Map set_isEnabledDeathPenalty(boolean paramBoolean);

   public abstract boolean isTakePets();

   public abstract L1Map set_isTakePets(boolean paramBoolean);

   public abstract boolean isRecallPets();

   public abstract L1Map set_isRecallPets(boolean paramBoolean);

   public abstract boolean isUsableItem();

   public abstract L1Map set_isUsableItem(boolean paramBoolean);

   public abstract boolean isUsableSkill();

   public abstract L1Map set_isUsableSkill(boolean paramBoolean);

   public abstract boolean isFishingZone(int paramInt1, int paramInt2);

   public abstract boolean isExistDoor(int paramInt1, int paramInt2);

   public abstract L1V1Map copyMap(int paramInt);

   public static L1Map newNull() {
     return _nullMap;
   }

   public abstract String toString(Point paramPoint);

   public boolean isNull() {
     return false;
   }


   public static boolean isTeleportable(int x, int y, int mapId) {
     if (mapId == 4 && x >= 33469 && x <= 33528 && y >= 32839 && y <= 32869) {
       return false;
     }
     return true;
   }

   public abstract L1Map set_isRuler(boolean paramBoolean);

   public abstract L1Map set_isPCTEL(boolean paramBoolean);

   public boolean isRuler() {
     return false;
   }

   public boolean isPCTEL() {
     return false;
   }

   public abstract L1Map set_CrackIntheTower(boolean paramBoolean);

   public boolean isCrackIntheTower() {
     return false;
   }
 }


