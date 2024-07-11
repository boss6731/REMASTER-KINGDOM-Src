 package l1j.server.server.templates;

 public class L1TownNpcInfo {
   private int _id;
   private int _npcid;
   private String _npcName;
   private int _sprid;
   private int _townid;

   public int getId() {
     return this._id;
   }

   public void setId(int id) {
     this._id = id;
   }

   public int getNpcId() {
     return this._npcid;
   }

   public void setNpcId(int id) {
     this._npcid = id;
   }

   public int geSprId() {
     return this._sprid;
   }

   public void setSprId(int id) {
     this._sprid = id;
   }

   public String getNpcName() {
     return this._npcName;
   }
   public void setNpcName(String name) {
     this._npcName = name;
   }

   public int getTownId() {
     return this._townid;
   }

   public void setTownId(int town) {
     this._townid = town;
   }
 }


