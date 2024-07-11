package l1j.server.MJTemplate;

public class MJStringConverter {
	static final int BASE_BEGIN_CODE = 0xAC00;
	static final int BASE_END_CODE = 0xD7A3;
	
	
	static final int INCOMPLETE_BEGIN_CODE = 0x3130;
	static final int INCOMPLETE_END_CODE = 0x318E;
	static final String[] INCOMPLETE_ARRAY = {"", "r", "R", "rt", "s", "sw", "sg", "e", "E", "f", "fr", "fa", "fq", "ft", "fx", "fv", "fg", "a", "q", "Q", "qt", "t", "T", "d", "w", "W",
			"c", "z", "x", "v", "g", "k", "o", "i", "O", "j", "p", "u", "P", "h", "hk", "ho", "hl", "y", "n", "nj", "np", "nl", "b", "m", "ml", "l"};
	
	
	static final String[] CHOSUNG = { "r", "R", "s", "e", "E", "f", "a", "q", "Q", "t", "T", "d", "w", "W", "c", "z", "x", "v", "g" };
	static final String[] JUNGSUNG = { "k", "o", "i", "O", "j", "p", "u", "P", "h", "hk", "ho", "hl", "y", "n", "nj", "np", "nl", "b", "m", "ml", "l" };
	static final String[] JONGSUNG = { "", "r", "R", "rt", "s", "sw", "sg", "e", "f", "fr", "fa", "fq", "ft", "fx", "fb", "fg", "a", "q", "qt", "t", "tt", "d", "w", "c", "z", "x", "v", "g" };
	static final int NEXT_CHOSUNG_CODE = 588;
	static final int NEXT_JUNGSUNG_CODE = 28;

	private static int incomplete(char c) {
		return (int)c - INCOMPLETE_BEGIN_CODE;
	}
	
	private static int base(char c) {
		return (int) c - BASE_BEGIN_CODE;
	}

	private static int chosung(int base) {
		return base / NEXT_CHOSUNG_CODE;
	}

	private static int jungsung(int base, int chosungIdx) {
		return (base - (NEXT_CHOSUNG_CODE * chosungIdx)) / NEXT_JUNGSUNG_CODE;
	}

	private static int jongsung(int base, int chosungIdx, int jungsungIdx) {
		return base - (NEXT_CHOSUNG_CODE * chosungIdx)
				- (NEXT_JUNGSUNG_CODE * jungsungIdx);
	}

	public static boolean isKor(char c) {
		return (c >= INCOMPLETE_BEGIN_CODE && c <= INCOMPLETE_END_CODE) ||
				(c >= BASE_BEGIN_CODE && c <= BASE_END_CODE);
	}
	
	public static String korConvertToEng(String s) {
		StringBuilder sb = new StringBuilder();
		for(char c : s.toCharArray()) {
			if(c >= INCOMPLETE_BEGIN_CODE && c <= INCOMPLETE_END_CODE) {
				int incomplete = incomplete(c);
				if(incomplete >= 0 && incomplete < INCOMPLETE_ARRAY.length) {
					sb.append(INCOMPLETE_ARRAY[incomplete]);
				}else {
					throw new IllegalArgumentException(String.format("invalid text %s", s));
				}
			}else if(c >= BASE_BEGIN_CODE && c <= BASE_END_CODE) {
				int base = base(c);
				int chosungIdx = chosung(base);
				int jungsungIdx = jungsung(base, chosungIdx);
				int jongsungIdx = jongsung(base, chosungIdx, jungsungIdx);
				
				if(chosungIdx >= 0 && chosungIdx < CHOSUNG.length) {
					sb.append(CHOSUNG[chosungIdx]);
				}
				if(jungsungIdx >= 0 && jungsungIdx < JUNGSUNG.length) {
					sb.append(JUNGSUNG[jungsungIdx]);
				}
				if(jongsungIdx >= 0 && jongsungIdx < JONGSUNG.length) {
					sb.append(JONGSUNG[jongsungIdx]);
				}
			}else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
