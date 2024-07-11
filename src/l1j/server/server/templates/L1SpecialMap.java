 package l1j.server.server.templates;

 public class L1SpecialMap {
   private String _Name;
   private int _mapid;
   private double _expRate;
   private int _dmgreduction;
   private int _mdmgreduction;
   private double _dmgRate;

   public double getDmgRate() {
     return this._dmgRate;
   }
   public void setDmgRate(double i) {
     this._dmgRate = i;
   }

   public int getMapId() {
     return this._mapid;
   }
   public void setMapId(int id) {
     this._mapid = id;
   }

   public double getExpRate() {
     return this._expRate;
   }
   public void setExpRate(double i) {
     this._expRate = i;
   }

   public String getName() {
     return this._Name;
   }
   public void setName(String name) {
     this._Name = name;
   }

   public int getDmgReduction() {
     return this._dmgreduction;
   }
   public void setDmgReduction(int id) {
     this._dmgreduction = id;
   }

   public int getMdmgReduction() {
     return this._mdmgreduction;
   }
   public void setMdmgReduction(int id) {
     this._mdmgreduction = id;
   }
 }


