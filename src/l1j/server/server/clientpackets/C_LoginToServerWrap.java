 package l1j.server.server.clientpackets;

 import java.io.FileNotFoundException;
 import l1j.server.server.GameClient;
 import l1j.server.server.serverpackets.S_CharPass;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class C_LoginToServerWrap
   extends ClientBasePacket {
   public C_LoginToServerWrap(byte[] abyte0, GameClient client) throws FileNotFoundException, Exception {
     super(abyte0);

     if (client.getAccount().getCPW() != null && !client.isLoginRecord()) {
       client.getAccount().setwaitpacket(abyte0);
       client.sendPacket((ServerBasePacket)new S_CharPass(20));
       return;
     }
     ClientBasePacket pck = new C_LoginToServer(abyte0, client);

     pck.clear();
   }



   private static final String C_LoginToServerWrap = "[C] C_LoginToServerWrap";


   public String getType() {
     return "[C] C_LoginToServerWrap";
   }
 }


