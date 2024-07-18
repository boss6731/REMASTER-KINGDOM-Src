package l1j.server.server.serverpackets;

import l1j.server.Config;
import l1j.server.server.Opcodes;

public class S_TaxRate extends ServerBasePacket {


	public S_TaxRate(int objecId, int taxrate) {
		writeC(Opcodes.S_TAX);
		writeD(objecId);
		writeC(Config.ServerAdSetting.TaxRateMin); // 10%~50%
		writeC(Config.ServerAdSetting.TaxRateMax); // 最大值 為 100
		writeC(taxrate);
	}

	@override
	public byte[] getContent() {
		return getBytes();
	}

	@override
	public String getType() {
		return _S__66_TAXRATE;
	}

	private static final String _S__66_TAXRATE = "[S] S_TaxRate";
}
