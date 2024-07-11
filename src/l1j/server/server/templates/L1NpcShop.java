 package l1j.server.server.templates;

 public class L1NpcShop
 {
   private int _npcId;
   private String _name;
   private int _locx;
   private int _locy;

   public int getNpcId() {
     return this._npcId;
   }
   private short _mapid; private int _heading; private String _title; private String _shopname;
   public void setNpcId(int i) {
     this._npcId = i;
   }



   public String getName() {
     return this._name;
   }

   public void setName(String s) {
     this._name = s;
   }



   public int getX() {
     return this._locx;
   }

   public void setX(int i) {
     this._locx = i;
   }



   public int getY() {
     return this._locy;
   }

   public void setY(int i) {
     this._locy = i;
   }



   public short getMapId() {
     return this._mapid;
   }

   public void setMapId(short i) {
     this._mapid = i;
   }



   public int getHeading() {
     return this._heading;
   }

   public void setHeading(int i) {
     this._heading = i;
   }



   public String getTitle() {
     return this._title;
   }

   public void setTitle(String s) {
     this._title = s;
   }



   public String getShopName() {
     return this._shopname;
   }

   public void setShopName(String s) {
     this._shopname = s;
   }
 }


