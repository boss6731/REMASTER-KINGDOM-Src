 package l1j.server.server.serverpackets;

 import l1j.server.Config;

 public class S_TaxRate extends ServerBasePacket {
   private static final String _S__66_TAXRATE = "[S] S_TaxRate";

   public S_TaxRate(int objecId, int taxrate) {
     writeC(95);
     writeD(objecId);
     writeC(Config.ServerAdSetting.TaxRateMin);
     writeC(Config.ServerAdSetting.TaxRateMax);
     writeC(taxrate);
   }


   public byte[] getContent() {
     return getBytes();
   }


   public String getType() {
     return "[S] S_TaxRate";
   }
 }


