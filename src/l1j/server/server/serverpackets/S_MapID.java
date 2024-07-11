 package l1j.server.server.serverpackets;

 import l1j.server.MJInstanceSystem.MJInstanceSpace;
 import l1j.server.MJRaidSystem.MJRaidSpace;

 public class S_MapID
   extends ServerBasePacket
 {
   public S_MapID(int mapid, boolean isUnderwater) {
     writeC(19);
     writeC(118);
     writeC(0);
     writeC(8);
     if (mapid > 6000 && mapid < 6499) {
       write4bit(1005);
     }
     else if (mapid > 6501 && mapid < 6999) {
       write4bit(1011);
     }
     else if (mapid > 1017 && mapid < 1023) {
       write4bit(1017);
     } else if (mapid > 9000 && mapid < 9099) {
       write4bit(9000);
     }
     else if (mapid > 2101 && mapid < 2151) {
       write4bit(2101);
     }
     else if (mapid > 2151 && mapid < 2201) {
       write4bit(2151);
     }
     else if (mapid > 2699 && mapid < 2798) {
       write4bit(2699);
     } else if (mapid > 2600 && mapid < 2698) {
       write4bit(2600);

     }
     else if (mapid >= 7783 && mapid <= 7793) {
       write4bit(7783);
     } else if (mapid >= 12152 && mapid <= 12252) {
       write4bit(12152);
     } else if (mapid >= 12257 && mapid <= 12357) {
       write4bit(12257);
     } else {

       mapid = MJRaidSpace.getInstance().getIdenMap(mapid);


       mapid = MJInstanceSpace.getInstance().getIdenMap(mapid);

       write4bit(mapid);
     }
     writeC(16);
     writeC(0);
     writeC(24);
     writeC(isUnderwater ? 1 : 0);
     writeC(32);
     writeC(0);
     writeC(40);
     writeC(0);
     writeC(48);
     writeC(0);
     writeH(0);
   }


   public byte[] getContent() {
     return getBytes();
   }
 }


