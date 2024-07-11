 package l1j.server.server.model.map;

 import l1j.server.server.types.Point;





































































































































































 class L1NullMap
   extends L1Map
 {
   public int getId() {
     return 0;
   }


   public int getX() {
     return 0;
   }


   public int getY() {
     return 0;
   }


   public int getWidth() {
     return 0;
   }


   public int getHeight() {
     return 0;
   }


   public int getTile(int x, int y) {
     return 0;
   }


   public int getOriginalTile(int x, int y) {
     return 0;
   }


   public boolean isInMap(int x, int y) {
     return false;
   }


   public boolean isInMap(Point pt) {
     return false;
   }


   public boolean isPassable(int x, int y) {
     return false;
   }


   public boolean isUserPassable(int x, int y, int heading) {
     return false;
   }


   public boolean isPassable(Point pt) {
     return false;
   }


   public boolean isPassable(int x, int y, int heading) {
     return false;
   }


   public boolean isPassable(Point pt, int heading) {
     return false;
   }



   public void setPassable(int x, int y, boolean isPassable) {}



   public void setPassable(Point pt, boolean isPassable) {}


   public boolean isSafetyZone(int x, int y) {
     return false;
   }


   public boolean isSafetyZone(Point pt) {
     return false;
   }


   public boolean isCombatZone(int x, int y) {
     return false;
   }


   public boolean isCombatZone(Point pt) {
     return false;
   }


   public boolean isNormalZone(int x, int y) {
     return false;
   }


   public boolean isNormalZone(Point pt) {
     return false;
   }


   public boolean isArrowPassable(int x, int y) {
     return false;
   }


   public boolean isArrowPassable(Point pt) {
     return false;
   }


   public boolean isArrowPassable(int x, int y, int heading) {
     return false;
   }


   public boolean isArrowPassable(Point pt, int heading) {
     return false;
   }


   public boolean isUnderwater() {
     return false;
   }


   public boolean isMarkable() {
     return false;
   }


   public boolean isTeleportable() {
     return false;
   }


   public boolean isEscapable() {
     return false;
   }


   public boolean isUseResurrection() {
     return false;
   }


   public boolean isUsePainwand() {
     return false;
   }


   public boolean isEnabledDeathPenalty() {
     return false;
   }


   public boolean isTakePets() {
     return false;
   }


   public boolean isRecallPets() {
     return false;
   }


   public boolean isUsableItem() {
     return false;
   }


   public boolean isUsableSkill() {
     return false;
   }


   public boolean isFishingZone(int x, int y) {
     return false;
   }


   public boolean isExistDoor(int x, int y) {
     return false;
   }


   public String toString(Point pt) {
     return "null";
   }


   public boolean isNull() {
     return true;
   }


   public L1V1Map copyMap(int id) {
     return null;
   }



   public void setId(int mapId) {}



   public int getBaseMapId() {
     return 0;
   }



   public int getTestTile(int x, int y) {
     return 0;
   }



   public L1Map set_isUnderwater(boolean isUnderwater) {
     return this;
   }



   public L1Map set_isMarkable(boolean isMarkable) {
     return this;
   }



   public L1Map set_isTeleportable(boolean isTeleportable) {
     return this;
   }



   public L1Map set_isEscapable(boolean isEscapable) {
     return this;
   }



   public L1Map set_isUseResurrection(boolean isUseResurrection) {
     return this;
   }



   public L1Map set_isUsePainwand(boolean isUsePainwand) {
     return this;
   }



   public L1Map set_isEnabledDeathPenalty(boolean isEnabledDeathPenalty) {
     return this;
   }



   public L1Map set_isTakePets(boolean isTakePets) {
     return this;
   }



   public L1Map set_isRecallPets(boolean isRecallPets) {
     return this;
   }



   public L1Map set_isUsableItem(boolean isUsableItem) {
     return this;
   }



   public L1Map set_isUsableSkill(boolean isUsableSkill) {
     return this;
   }



   public L1Map set_isRuler(boolean isRuler) {
     return this;
   }



   public L1Map set_isPCTEL(boolean isPCTEL) {
     return this;
   }



   public L1Map set_CrackIntheTower(boolean CrackIntheTower) {
     return this;
   }
 }


