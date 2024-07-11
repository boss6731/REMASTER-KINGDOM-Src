     package l1j.server.server.serverpackets;



     public class S_Deposit
       extends ServerBasePacket
     {
       private static final String _S__53_DEPOSIT = "[S] S_Deposit";

       public S_Deposit(int objecId) {
         writeC(49);
         writeD(objecId);
       }


       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] S_Deposit";
       }
     }


