 package l1j.server.server.serverpackets;




 public class S_SkillIconGFX
   extends ServerBasePacket
 {
   public S_SkillIconGFX(int i, int j) {
     writeC(108);
     writeC(i);
     writeH(j);
     writeH(0);
   }
   public S_SkillIconGFX(int i, int j, int gfxid, int objid) {
     writeC(108);
     writeC(i);
     writeH(j);

     writeH(gfxid);
     writeD(objid);
   }
   public S_SkillIconGFX(int i) {
     writeC(108);
     writeC(160);
     writeC(1);
     writeH(0);
     writeC(2);
     writeH(i);
   }
   public S_SkillIconGFX(int i, int j, boolean on) {
     writeC(108);
     writeC(i);
     writeH(j);
     if (on) {
       writeC(1);
     } else {

       writeC(0);
     }
   } public S_SkillIconGFX(int i, int j, boolean on, boolean on2) {
     int a;
     writeC(108);
     writeC(i);
     writeH(j);

     if (on) {
       a = 1;
     } else if (on2) {
       a = 2;
     } else {
       a = 0;
     }  writeC(a);
   }


   public byte[] getContent() {
     return getBytes();
   }
 }


