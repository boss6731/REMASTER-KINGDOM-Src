package l1j.server.server.server.model;

public class L1TaxCalculator {
	/**
	 * 戰爭稅固定為15%
	 */
//private static final int WAR_TAX_RATES = 15;
	private static final int WAR_TAX_RATES = 0;

	/**
	 * 國稅固定為10%（對地方稅的比例）
	 */
//    private static final int NATIONAL_TAX_RATES = 10;
	private static final int NATIONAL_TAX_RATES = 10;

	/**
	 * 迪亞德稅固定為10%（對戰爭稅的比例）
	 */
//    private static final int DIAD_TAX_RATES = 10;
	private static final int DIAD_TAX_RATES = 10;

	private final int _taxRatesCastle;
	private final int _taxRatesTown;
	private final int _taxRatesWar = WAR_TAX_RATES;

	/**
	 * @param merchantNpcId
	 *            計算對象商店的NPCID
	 */
	public L1TaxCalculator(int merchantNpcId) {
		_taxRatesCastle = L1CastleLocation.getCastleTaxRateByNpcId(merchantNpcId);
		_taxRatesTown = L1TownLocation.getTownTaxRateByNpcid(merchantNpcId);
	}

	public int calcTotalTaxPrice(int price) {
		int taxCastle = price * _taxRatesCastle;
		int taxTown = price * _taxRatesTown;
		int taxWar = price * WAR_TAX_RATES;
		return (taxCastle + taxTown + taxWar) / 100;
	}

	// XXX 因為是逐個計算，會出現圓整誤差。
	public int calcCastleTaxPrice(int price) {
		return (price * _taxRatesCastle) / 100 - calcNationalTaxPrice(price);
	}

	public int calcNationalTaxPrice(int price) {
		return (price * _taxRatesCastle) / 100 / (100 / NATIONAL_TAX_RATES);
	}

	public int calcTownTaxPrice(int price) {
		return (price * _taxRatesTown) / 100;
	}

	public int calcWarTaxPrice(int price) {
		return (price * _taxRatesWar) / 100;
	}

	public int calcDiadTaxPrice(int price) {
		return (price * _taxRatesWar) / 100 / (100 / DIAD_TAX_RATES);
	}

	/**
	 * 要求稅後價格。
	 *
	 * @param price
	 *            稅前價格
	 * @return 稅後價格
	 */
	public int layTax(int price) {
		return price + calcTotalTaxPrice(price);
	}
}
