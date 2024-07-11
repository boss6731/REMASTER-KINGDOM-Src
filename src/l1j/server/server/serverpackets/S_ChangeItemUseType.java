     package l1j.server.server.serverpackets;

     import java.io.IOException;

     public class S_ChangeItemUseType
       extends ServerBasePacket
     {
       public S_ChangeItemUseType(int object_id, int use_type) {
         writeC(237);
         writeD(object_id);
         writeC(use_type);
         writeC(0);
       }


       public byte[] getContent() throws IOException {
         return getBytes();
       }
     }


