package l1j.server;

public class KoreanToEnglish {
	
	
	/* **********************************************
	 * 分離子音和母音
	 * 설연수 -> ㅅㅓㄹㅇㅕㄴㅅㅜ,    바보 -> ㅂㅏㅂㅗ
	 * **********************************************/
	/** 初聲 - 가(ㄱ), 날(ㄴ) 닭(ㄷ) */
	public static char[] arrChoSung = { 0x3131, 0x3132, 0x3134, 0x3137, 0x3138,
			0x3139, 0x3141, 0x3142, 0x3143, 0x3145, 0x3146, 0x3147, 0x3148,
			0x3149, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e };
	/** 中聲 - 가(ㅏ), 야(ㅑ), 뺨(ㅑ)*/
	public static char[] arrJungSung = { 0x314f, 0x3150, 0x3151, 0x3152,
			0x3153, 0x3154, 0x3155, 0x3156, 0x3157, 0x3158, 0x3159, 0x315a,
			0x315b, 0x315c, 0x315d, 0x315e, 0x315f, 0x3160, 0x3161, 0x3162,
			0x3163 };
	/** 終聲 - 가(沒有), 갈(ㄹ) 천(ㄴ) */
	public static char[] arrJongSung = { 0x0000, 0x3131, 0x3132, 0x3133,
			0x3134, 0x3135, 0x3136, 0x3137, 0x3139, 0x313a, 0x313b, 0x313c,
			0x313d, 0x313e, 0x313f, 0x3140, 0x3141, 0x3142, 0x3144, 0x3145,
			0x3146, 0x3147, 0x3148, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e };
	
	
	/* **********************************************
	 * 轉換為英文字母
	 * 설연수 -> tjfdustn, 멍충 -> ajdcnd 
	 * **********************************************/
	/** 初聲 - 가(ㄱ), 날(ㄴ) 닭(ㄷ) */
	public static String[] arrChoSungEng = { "r", "R", "s", "e", "E",
		"f", "a", "q", "Q", "t", "T", "d", "w",
		"W", "c", "z", "x", "v", "g" };
	
	/** 中聲 - 가(ㅏ), 야(ㅑ), 뺨(ㅑ)*/
	public static String[] arrJungSungEng = { "k", "o", "i", "O",
		"j", "p", "u", "P", "h", "hk", "ho", "hl",
		"y", "n", "nj", "np", "nl", "b", "m", "ml",
		"l" };
	
	/** 終聲 - 가(沒有), 갈(ㄹ) 천(ㄴ) */
	public static String[] arrJongSungEng = { "", "r", "R", "rt",
		"s", "sw", "sg", "e", "f", "fr", "fa", "fq",
		"ft", "fx", "fv", "fg", "a", "q", "qt", "t",
		"T", "d", "w", "c", "z", "x", "v", "g" };
	
	/** 單獨子音 - ㄱ,ㄴ,ㄷ,ㄹ... (ㄸ,ㅃ,ㅉ是單獨子音(初聲)但不使用單獨子音) */
	public static String[] arrSingleJaumEng = { "r", "R", "rt",
		"s", "sw", "sg", "e","E" ,"f", "fr", "fa", "fq",
		"ft", "fx", "fv", "fg", "a", "q","Q", "qt", "t",
		"T", "d", "w", "W", "c", "z", "x", "v", "g" };
	
	
	
	public static String Convert(String name) {

		String result		= "";									// 儲存結果的變數
		String resultEng	= "";									// 轉換為英文字母
		
		for (int i = 0; i < name.length(); i++) {
			
			/*  逐個讀取字元 */
			char chars = (char) (name.charAt(i) - 0xAC00);

			if (chars >= 0 && chars <= 11172) {
				/* A. 字母組合的情況 */

				/* A-1. 分離初/中/終聲 */
				int chosung 	= chars / (21 * 28);
				int jungsung 	= chars % (21 * 28) / 28;
				int jongsung 	= chars % (21 * 28) % 28;

				
				/* A-2. 儲存到 result 中 */
				result = result + arrChoSung[chosung] + arrJungSung[jungsung];

				
				/* 分離子音 */
				if (jongsung != 0x0000) {
					/* A-3. 如果有終聲則儲存到 result 中 */
					result =  result + arrJongSung[jongsung];
				}

				/* 轉換為英文字母 */
				resultEng = resultEng + arrChoSungEng[chosung] + arrJungSungEng[jungsung];
				if (jongsung != 0x0000) {
					/* A-3. 如果有終聲則儲存到 result 中 */
					resultEng =  resultEng + arrJongSungEng[jongsung];
				}

			} else {
				/* B. 不是韓文或只有子音的情況 */
				
				/* 分離子音 */
				result = result + ((char)(chars + 0xAC00));
				
				/* 轉換為英文字母 */
				if( chars>=34097 && chars<=34126){
					/* 單獨子音的情況 */
					int jaum 	= (chars-34097);
					resultEng = resultEng + arrSingleJaumEng[jaum];
				} else if( chars>=34127 && chars<=34147) {
					/* 單獨母音的情況 */
					int moum 	= (chars-34127);
					resultEng = resultEng + arrJungSungEng[moum];
				} else {
					/* 如果是字母則直接新增 */
					resultEng = resultEng + ((char)(chars + 0xAC00));
				}
			}//if
			
		}//for

		return resultEng;
	}
}
