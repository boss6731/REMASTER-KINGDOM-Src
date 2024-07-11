 package l1j.server.server.clientpackets;

 import l1j.server.Config;
 import l1j.server.server.GameClient;
 import l1j.server.server.datatables.CharacterConfigTable;
 import l1j.server.server.model.Instance.L1PcInstance;


 public class C_CharcterConfig
   extends ClientBasePacket
 {
   private static final String C_CHARCTER_CONFIG = "[C] C_CharcterConfig";

   public C_CharcterConfig(byte[] abyte0, GameClient client) throws Exception {
     super(abyte0);
     if (client == null) {
       return;
     }
     if (Config.Login.CharacterConfigInServerSide) {
       L1PcInstance pc = client.getActiveChar();
       if (pc == null) {
         pc = client.latestCharacterInstance();
         if (pc == null) {
           return;
         }
       }

       int length = readD();
       if (length > 2048)
         return;  byte[] data = readByte();
       int count = CharacterConfigTable.getInstance().countCharacterConfig(pc.getId());
       if (count == 0) {
         CharacterConfigTable.getInstance().storeCharacterConfig(pc.getId(), length, data);
       } else {
         CharacterConfigTable.getInstance().updateCharacterConfig(pc.getId(), length, data);
       }
     }
   }


   public String getType() {
     return "[C] C_CharcterConfig";
   }
 }


