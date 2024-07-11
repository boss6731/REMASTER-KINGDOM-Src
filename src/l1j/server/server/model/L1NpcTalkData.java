 package l1j.server.server.model;



























 public class L1NpcTalkData
 {
   int ID;
   int NpcID;
   String normalAction;
   String caoticAction;
   String teleportURL;
   String teleportURLA;

   public String getNormalAction() {
     return this.normalAction;
   }





   public void setNormalAction(String normalAction) {
     this.normalAction = normalAction;
   }




   public String getCaoticAction() {
     return this.caoticAction;
   }





   public void setCaoticAction(String caoticAction) {
     this.caoticAction = caoticAction;
   }




   public String getTeleportURL() {
     return this.teleportURL;
   }





   public void setTeleportURL(String teleportURL) {
     this.teleportURL = teleportURL;
   }




   public String getTeleportURLA() {
     return this.teleportURLA;
   }





   public void setTeleportURLA(String teleportURLA) {
     this.teleportURLA = teleportURLA;
   }




   public int getID() {
     return this.ID;
   }





   public void setID(int id) {
     this.ID = id;
   }




   public int getNpcID() {
     return this.NpcID;
   }





   public void setNpcID(int npcID) {
     this.NpcID = npcID;
   }
 }


