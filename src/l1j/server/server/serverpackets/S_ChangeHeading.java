     package l1j.server.server.serverpackets;

     import l1j.server.server.model.L1Character;



     public class S_ChangeHeading
       extends ServerBasePacket
     {
       public S_ChangeHeading(L1Character cha) {
         buildPacket(cha);
       }

       private void buildPacket(L1Character cha) {
         writeC(120);
         writeD(cha.getId());
         writeC(cha.getHeading());
       }


       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_ChangeHeading";
       }
     }


