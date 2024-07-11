 package l1j.server.server.clientpackets;

 import l1j.server.server.GameClient;
 import l1j.server.server.serverpackets.S_ServerVersion;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class C_ServerVersion extends ClientBasePacket {
   private static final String C_SERVER_VERSION = "[C] C_ServerVersion";

   public C_ServerVersion(byte[] decrypt, GameClient client) throws Exception {
     super(decrypt);

     if (client == null) {
       return;
     }
     client.sendPacket((ServerBasePacket)new S_ServerVersion());
   }


   public String getType() {
     return "[C] C_ServerVersion";
   }
 }


