     package l1j.server.server.serverpackets;


     public class S_CharAmount
       extends ServerBasePacket
     {
       public S_CharAmount(int value, int i) {
         buildPacket(value, i);
       }

       private void buildPacket(int value, int slot) {
         writeC(190);
         writeC(value);
         writeC(slot);
       }


       public byte[] getContent() {
         return getBytes();
       }
     }


