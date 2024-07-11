 package l1j.server.server.serverpackets;

 import java.io.IOException;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.L1Character;


 public class S_SmeltingEject
   extends ServerBasePacket
 {
   public static S_SmeltingEject send(L1Character pc, L1ItemInstance item) {
     S_SmeltingEject s = new S_SmeltingEject();
     s.writeC(10);
     s.writeC(69);



     s.writeH(0);
     return s;
   }


   private S_SmeltingEject() {
     writeC(108);
   }


   public byte[] getContent() throws IOException {
     return getBytes();
   }
 }


