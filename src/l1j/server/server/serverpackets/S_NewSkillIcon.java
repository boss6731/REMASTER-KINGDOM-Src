 package l1j.server.server.serverpackets;



 public class S_NewSkillIcon
   extends ServerBasePacket
 {
   public S_NewSkillIcon(int gfxid, int time) {
     writeC(108);
     writeC(154);
     writeH(time);
     writeD(gfxid);
     writeH(0);
   }
   public S_NewSkillIcon(int skillId, boolean on, long time) {
     writeC(19);
     writeH(110);
     writeC(8);
     writeC(on ? 2 : 3);
     writeC(16);
     byteWrite(skillId);
     if (on) {
       writeC(24);
       if (time < 0L) {
         byte[] minus = { -1, -1, -1, -1, -1, -1, -1, -1, 1 };
         writeByte(minus);
       } else {
         byteWrite(time);
       }  writeC(32);
       if (skillId == 8689) {
         writeC(10);
       } else {
         writeC(8);
       }
       int msgNum = 0;
       byteWrite(msgNum);
       writeC(72);
       writeC(0);
     }
     writeH(80);
     if (on) {
       writeC(88);
       writeC(1);
       writeC(96);
       writeC(0);
       writeC(104);
       writeC(0);
       writeC(112);
       writeC(0);
     }
     writeH(0);
   }
   public static final int[] hextable = new int[] { 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255 };




   private void byteWrite(long value) {
     long temp = value / 128L;
     if (temp > 0L) {
       writeC(hextable[(int)value % 128]);
       while (temp >= 128L) {
         writeC(hextable[(int)temp % 128]);
         temp /= 128L;
       }
       if (temp > 0L) {
         writeC((int)temp);
       }
     } else if (value == 0L) {
       writeC(0);
     } else {
       writeC(hextable[(int)value]);
       writeC(0);
     }
   }


   public byte[] getContent() {
     return getBytes();
   }
 }


