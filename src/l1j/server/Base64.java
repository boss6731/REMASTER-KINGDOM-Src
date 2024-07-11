/*
 * 本程序是自由軟體；您可以重新分發它並/或修改它
 * 按照 GNU 通用公共許可證的條款發布，由
 * 自由軟體基金會；或（在您的選擇下）
 * 任何以後的版本。
 *
 * 本程序是基於希望它是有用的分發，
 * 但沒有任何保證；甚至沒有隱含的保證
 * 適銷性或特定用途適用。詳情請參見
 * GNU 通用公共許可證。
 *
 * 您應該已經收到了 GNU 通用公共許可證的副本
 * 連同這個程序；如果沒有，請寫信給自由軟體
 * 基金會，Inc.，59 Temple Place - Suite 330，波士頓，MA
 * 02111-1307，美國。
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server;

import l1j.server.server.utils.StreamUtil;

public class Base64 {

    /* ******** 公共字段 ******** */

    public final static int NO_OPTIONS = 0;

    public final static int ENCODE = 1;

    public final static int DECODE = 0;

    public final static int GZIP = 2;

    public final static int DONT_BREAK_LINES = 8;

    /* ******** 私有字段 ******** */

    private final static int MAX_LINE_LENGTH = 76;

    private final static byte EQUALS_SIGN = (byte) '=';

    private final static byte NEW_LINE = (byte) '\n';

    private final static String PREFERRED_ENCODING = "UTF-8";

    private final static byte[] ALPHABET;

    private final static byte[] _NATIVE_ALPHABET = /*
                                                     * 可能是一些有趣的東西
                                                     * ，如EBCDIC
                                                     */
        { (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F',
            (byte) 'G', (byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K',
            (byte) 'L', (byte) 'M', (byte) 'N', (byte) 'O', (byte) 'P',
            (byte) 'Q', (byte) 'R', (byte) 'S', (byte) 'T', (byte) 'U',
            (byte) 'V', (byte) 'W', (byte) 'X', (byte) 'Y', (byte) 'Z',
            (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e',
            (byte) 'f', (byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j',
            (byte) 'k', (byte) 'l', (byte) 'm', (byte) 'n', (byte) 'o',
            (byte) 'p', (byte) 'q', (byte) 'r', (byte) 's', (byte) 't',
            (byte) 'u', (byte) 'v', (byte) 'w', (byte) 'x', (byte) 'y',
            (byte) 'z', (byte) '0', (byte) '1', (byte) '2', (byte) '3',
            (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8',
            (byte) '9', (byte) '+', (byte) '/' };

    static {
        byte[] __bytes;
        try {
            __bytes = new String(
                    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/")
                    .getBytes(PREFERRED_ENCODING);
        } // end try
        catch (java.io.UnsupportedEncodingException use) {
            __bytes = _NATIVE_ALPHABET; // Fall back to native encoding
        } // end catch
        ALPHABET = __bytes;
    } // end static

    private final static byte[] DECODABET = { -9, -9, -9, -9, -9, -9, -9, -9,
            -9, // Decimal 0 - 8
            -5, -5, // Whitespace: Tab and Linefeed
            -9, -9, // Decimal 11 - 12
            -5, // Whitespace: Carriage Return
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, // Decimal 14 -
            // 26
            -9, -9, -9, -9, -9, // Decimal 27 - 31
            -5, // Whitespace: Space
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, // Decimal 33 - 42
            62, // Plus sign at decimal 43
            -9, -9, -9, // Decimal 44 - 46
            63, // Slash at decimal 47
            52, 53, 54, 55, 56, 57, 58, 59, 60, 61, // Numbers zero through nine
            -9, -9, -9, // Decimal 58 - 60
            -1, // Equals sign at decimal 61
            -9, -9, -9, // Decimal 62 - 64
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, // Letters 'A'
            // through 'N'
            14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, // Letters 'O'
            // through 'Z'
            -9, -9, -9, -9, -9, -9, // Decimal 91 - 96
            26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, // Letters 'a'
            // through 'm'
            39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, // Letters 'n'
            // through 'z'
            -9, -9, -9, -9 // Decimal 123 - 126
    /*
     * ,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9, // Decimal 127 - 139
     * -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9, // Decimal 140 - 152
     * -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9, // Decimal 153 - 165
     * -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9, // Decimal 166 - 178
     * -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9, // Decimal 179 - 191
     * -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9, // Decimal 192 - 204
     * -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9, // Decimal 205 - 217
     * -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9, // Decimal 218 - 230
     * -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9, // Decimal 231 - 243
     * -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9 // Decimal 244 - 255
     */
    };

    private final static byte WHITE_SPACE_ENC = -5; // 在編碼中表示空格

    private final static byte EQUALS_SIGN_ENC = -1; // 在編碼中表示等號

    private Base64() {
    }

    /* ******** 編碼方法 ******** */

    private static byte[] encode3to4(byte[] b4, byte[] threeBytes,
            int numSigBytes) {
        encode3to4(threeBytes, 0, numSigBytes, b4, 0);
        return b4;
    } // end encode3to4
	private static byte[] encode3to4(byte[] source, int srcOffset,
	int numSigBytes, byte[] destination, int destOffset) {
// 1 2 3
// 01234567890123456789012345678901 位元位置
// --------000000001111111122222222 來自 threeBytes 的陣列位置
// --------| || || || | 六位元組以索引 ALPHABET
// >>18 >>12 >> 6 >> 0 右移必要
// 0x3f 0x3f 0x3f 附加的 AND 運算

// 如果在陣列中僅傳入了一個或兩個有效位元組，則創建帶有零填充的緩衝區。
// 我們必須左移 24 以清除 Java 在將從位元組轉換為整數的值視為負數時出現的 1。
int inBuff = (numSigBytes > 0 ? ((source[srcOffset] << 24) >>> 8) : 0)
		| (numSigBytes > 1 ? ((source[srcOffset + 1] << 24) >>> 16) : 0)
		| (numSigBytes > 2 ? ((source[srcOffset + 2] << 24) >>> 24) : 0);

switch (numSigBytes) {
case 3:
	destination[destOffset] = ALPHABET[(inBuff >>> 18)];
	destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];
	destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 0x3f];
	destination[destOffset + 3] = ALPHABET[(inBuff) & 0x3f];
	return destination;

case 2:
	destination[destOffset] = ALPHABET[(inBuff >>> 18)];
	destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];
	destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 0x3f];
	destination[destOffset + 3] = EQUALS_SIGN;
	return destination;

case 1:
	destination[destOffset] = ALPHABET[(inBuff >>> 18)];
	destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];
	destination[destOffset + 2] = EQUALS_SIGN;
	destination[destOffset + 3] = EQUALS_SIGN;
	return destination;

default:
	return destination;
} // end switch
} // end encode3to4

public static String encodeObject(java.io.Serializable serializableObject) {
return encodeObject(serializableObject, NO_OPTIONS);
} // end encodeObject

public static String encodeObject(java.io.Serializable serializableObject,
	int options) {
// 流
java.io.ByteArrayOutputStream baos = null;
java.io.OutputStream b64os = null;
java.io.ObjectOutputStream oos = null;
java.util.zip.GZIPOutputStream gzos = null;

// 分離選項
int gzip = (options & GZIP);
int dontBreakLines = (options & DONT_BREAK_LINES);

try {
	// ObjectOutputStream -> (GZIP) -> Base64 -> ByteArrayOutputStream
	baos = new java.io.ByteArrayOutputStream();
	b64os = new Base64.OutputStream(baos, ENCODE | dontBreakLines);

	// 是否使用 GZip？
	if (gzip == GZIP) {
		gzos = new java.util.zip.GZIPOutputStream(b64os);
		oos = new java.io.ObjectOutputStream(gzos);
	} // end if: gzip
	else {
		oos = new java.io.ObjectOutputStream(b64os);
	}

	oos.writeObject(serializableObject);
} // end try
catch (java.io.IOException e) {
	e.printStackTrace();
	return null;
} // end catch
finally {
	StreamUtil.close(oos, gzos, b64os, baos);
} // end finally

// 根據相關編碼返回值。
try {
	return new String(baos.toByteArray(), PREFERRED_ENCODING);
} // end try
catch (java.io.UnsupportedEncodingException uue) {
	return new String(baos.toByteArray());
} // end catch

} // end encode

public static String encodeBytes(byte[] source) {
return encodeBytes(source, 0, source.length, NO_OPTIONS);
} // end encodeBytes

public static String encodeBytes(byte[] source, int options) {
return encodeBytes(source, 0, source.length, options);
} // end encodeBytes

public static String encodeBytes(byte[] source, int off, int len) {
return encodeBytes(source, off, len, NO_OPTIONS);
} // end encodeBytes

public static String encodeBytes(byte[] source, int off, int len,
	int options) {
// 分離選項
int dontBreakLines = (options & DONT_BREAK_LINES);
int gzip = (options & GZIP);

// 壓縮？
if (gzip == GZIP) {
	java.io.ByteArrayOutputStream baos = null;
	java.util.zip.GZIPOutputStream gzos = null;
	Base64.OutputStream b64os = null;

	try {
		// GZip -> Base64 -> ByteArray
		baos = new java.io.ByteArrayOutputStream();
		b64os = new Base64.OutputStream(baos, ENCODE | dontBreakLines);
		gzos = new java.util.zip.GZIPOutputStream(b64os);

		gzos.write(source, off, len);
		gzos.close();
	} // end try
	catch (java.io.IOException e) {
		e.printStackTrace();
		return null;
	} // end catch
	finally {
		StreamUtil.close(gzos, b64os, baos);
	} // end finally

	// 根據相關編碼返回值。
	try {
		return new String(baos.toByteArray(), PREFERRED_ENCODING);
	} // end try
	catch (java.io.UnsupportedEncodingException uue) {
		return new String(baos.toByteArray());
	} // end catch
} // end if: compress

// 否則，不要壓縮。最好根本不使用流。
else {
	// 將選項轉換為布林值，這樣代碼喜歡它。
	boolean breakLines = dontBreakLines == 0;

	int len43 = len * 4 / 3;
	byte[] outBuff = new byte[(len43) // 主要 4:3
			+ ((len % 3) > 0 ? 4 : 0) // 考慮填充
			+ (breakLines ? (len43 / MAX_LINE_LENGTH) : 0)]; // 新的行
	int d = 0;
	int e = 0;
	int len2 = len - 2;
	int lineLength = 0;
	for (; d < len2; d += 3, e += 4) {
		encode3to4(source, d + off, 3, outBuff, e);

		lineLength += 4;
		if (breakLines && lineLength == MAX_LINE_LENGTH) {
			outBuff[e + 4] = NEW_LINE;
			e++;
			lineLength = 0;
		} // end if: end of line
	} // en dfor: each piece of array

	if (d < len) {
		encode3to4(source, d + off, len - d, outBuff, e);
		e += 4;
	} // end if: some padding needed

	// 根據相關編碼返回值。
	try {
		return new String(outBuff, 0, e, PREFERRED_ENCODING);
	} // end try
	catch (java.io.UnsupportedEncodingException uue) {
		return new String(outBuff, 0, e);
	} // end catch

} // end else: don't compress

} // end encodeBytes
/* ******** 解碼方法 ******** */

/**
 * 從陣列<var>source</var>解碼四個位元組，並將結果位元組（最多三個）寫入<var>destination</var>。
 * 可以通過指定<var>srcOffset</var>和<var>destOffset</var>在它們的長度的任何地方操縱來操作源和目標陣列。
 * 此方法不檢查您的陣列是否足夠大，以容納<var>source</var>陣列的<var>srcOffset</var> + 4或<var>destination</var>陣列的<var>destOffset</var> + 3。
 * 此方法返回從Base64編碼轉換的實際位元組數。
 * 
 * @param source 欲轉換的陣列
 * @param srcOffset 開始轉換的索引
 * @param destination 保存轉換結果的陣列
 * @param destOffset 輸出的索引位置
 * @return 轉換的解碼位元組數
 * @since 1.3
 */
private static int decode4to3(byte[] source, int srcOffset,
        byte[] destination, int destOffset) {
    // 範例：Dk==
    if (source[srcOffset + 2] == EQUALS_SIGN) {
        int outBuff = ((DECODABET[source[srcOffset]] & 0xFF) << 18)
                | ((DECODABET[source[srcOffset + 1]] & 0xFF) << 12);

        destination[destOffset] = (byte) (outBuff >>> 16);
        return 1;
    }

    // 範例：DkL=
    else if (source[srcOffset + 3] == EQUALS_SIGN) {
        int outBuff = ((DECODABET[source[srcOffset]] & 0xFF) << 18)
                | ((DECODABET[source[srcOffset + 1]] & 0xFF) << 12)
                | ((DECODABET[source[srcOffset + 2]] & 0xFF) << 6);

        destination[destOffset] = (byte) (outBuff >>> 16);
        destination[destOffset + 1] = (byte) (outBuff >>> 8);
        return 2;
    }

    // 範例：DkLE
    else {
        try {
            int outBuff = ((DECODABET[source[srcOffset]] & 0xFF) << 18)
                    | ((DECODABET[source[srcOffset + 1]] & 0xFF) << 12)
                    | ((DECODABET[source[srcOffset + 2]] & 0xFF) << 6)
                    | ((DECODABET[source[srcOffset + 3]] & 0xFF));

            destination[destOffset] = (byte) (outBuff >> 16);
            destination[destOffset + 1] = (byte) (outBuff >> 8);
            destination[destOffset + 2] = (byte) (outBuff);

            return 3;
        } catch (Exception e) {
            System.out.println("" + source[srcOffset] + ": " + (DECODABET[source[srcOffset]]));
            System.out.println("" + source[srcOffset + 1] + ": " + (DECODABET[source[srcOffset + 1]]));
            System.out.println("" + source[srcOffset + 2] + ": " + (DECODABET[source[srcOffset + 2]]));
            System.out.println("" + source[srcOffset + 3] + ": " + (DECODABET[source[srcOffset + 3]]));
            return -1;
        }
    }
} // end decodeToBytes

/**
 * 對ASCII字符進行非常低級別的解碼，以位元組陣列的形式。
 * 不支持自動解壓縮或任何其他“高級”功能。
 * 
 * @param source Base64編碼的數據
 * @param off 開始解碼的偏移量
 * @param len 要解碼的字符長度
 * @return 解碼後的數據
 * @since 1.3
 */
public static byte[] decode(byte[] source, int off, int len) {
    int len34 = len * 3 / 4;
    byte[] outBuff = new byte[len34]; // 輸出的大小上限
    int outBuffPosn = 0;

    byte[] b4 = new byte[4];
    int b4Posn = 0;
    int i = 0;
    byte sbiCrop = 0;
    byte sbiDecode = 0;
    for (i = off; i < off + len; i++) {
        sbiCrop = (byte) (source[i] & 0x7f); // 只取低七位
        sbiDecode = DECODABET[sbiCrop];

        if (sbiDecode >= WHITE_SPACE_ENC) {
            if (sbiDecode >= EQUALS_SIGN_ENC) {
                b4[b4Posn++] = sbiCrop;
                if (b4Posn > 3) {
                    outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn);
                    b4Posn = 0;

                    if (sbiCrop == EQUALS_SIGN) {
                        break;
                    }
                }
            }
        } else {
            System.err.println("錯誤的Base64輸入字符在" + i + "位置: " + source[i] + "(十進制)");
            return null;
        }
    }

    byte[] out = new byte[outBuffPosn];
    System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
    return out;
} // end decode

/**
 * 從Base64編碼中解碼數據，自動檢測並解壓縮gzip壓縮的數據。
 * 
 * @param s 要解碼的字符串
 * @return 解碼後的數據
 * @since 1.4
 */
public static byte[] decode(String s) {
    byte[] bytes;
    try {
        bytes = s.getBytes(PREFERRED_ENCODING);
    } catch (java.io.UnsupportedEncodingException uee) {
        bytes = s.getBytes();
    }

    bytes = decode(bytes, 0, bytes.length);

    if (bytes.length >= 2) {
        int head = (bytes[0] & 0xff) | ((bytes[1] << 8) & 0xff00);
        if (bytes != null &&
                bytes.length >= 4 &&
                java.util.zip.GZIPInputStream.GZIP_MAGIC == head) {
            java.io.ByteArrayInputStream bais = null;
            java.util.zip.GZIPInputStream gzis = null;
            java.io.ByteArrayOutputStream baos = null;
            byte[] buffer = new byte[2048];
            int length = 0;

            try {
                baos = new java.io.ByteArrayOutputStream();
                bais = new java.io.ByteArrayInputStream(bytes);
                gzis = new java.util.zip.GZIPInputStream(bais);

                while ((length = gzis.read(buffer)) >= 0) {
                    baos.write(buffer, 0, length);
                }

                bytes = baos.toByteArray();

            } catch (java.io.IOException e) {
                // Just return originally-decoded bytes
            } finally {
                StreamUtil.close(baos, gzis, bais);
            }
        }
    }

    return bytes;
} // end decode
	/**
	 * Attempts to decode Base64 data and deserialize a Java Object within.
	 * Returns <tt>null</tt> if there was an error.
	 * 
	 * @param encodedObject
	 *            要解碼的Base64資料
	 * @return 解碼並反序列化後的物件
	 * @since 1.5
	 */
	public static Object decodeToObject(String encodedObject) {
		// 解碼並在需要時解壓縮
		byte[] objBytes = decode(encodedObject);

		java.io.ByteArrayInputStream bais = null;
		java.io.ObjectInputStream ois = null;
		Object obj = null;

		try {
			bais = new java.io.ByteArrayInputStream(objBytes);
			ois = new java.io.ObjectInputStream(bais);

			obj = ois.readObject();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		} catch (java.lang.ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			StreamUtil.close(bais, ois);
		}

		return obj;
	} // end decodeObject

	/* ******** 內部類別 InputStream ******** */

	/**
	 * 一個 {@link Base64#InputStream} 將從另一個 {@link java.io.InputStream}
	 * 中讀取資料，並在流動中進行Base64編碼或解碼。
	 * 
	 * @see Base64
	 * @see java.io.FilterInputStream
	 * @since 1.3
	 */
	public static class InputStream extends java.io.FilterInputStream {
		private boolean encode; // 是否編碼

		private int position; // 目前的位置

		private byte[] buffer; // 用於保存轉換後的資料的小緩衝區

		private int bufferLength; // 緩衝區的長度（3或4）

		private int numSigBytes; // 緩衝區中有效的位元組數

		private int lineLength;

		private boolean breakLines; // 是否在不超過80個字元時斷行

		/**
		 * 以 DECODE 模式構造一個 {@link Base64#InputStream}。
		 * 
		 * @param in
		 *            要從中讀取資料的 {@link java.io.InputStream}。
		 * @since 1.3
		 */
		public InputStream(java.io.InputStream in) {
			this(in, DECODE);
		} // end constructor

		/**
		 * 以 ENCODE 或 DECODE 模式之一構造一個 {@link Base64#InputStream}。
		 * 
		 * <p>
		 * 可選的選項有：
		 * 
		 * <pre>
		 *      ENCODE 或 DECODE：在讀取資料時進行編碼或解碼。
		 *      DONT_BREAK_LINES：不在76個字元處斷行
		 *        （僅在編碼時有效）
		 *        &lt;i&gt;注意：技術上，這會使您的編碼不符合規範。&lt;/i&gt;
		 * </pre>
		 * 
		 * <p>
		 * 範例： <code>new Base64.InputStream( in, Base64.DECODE )</code>
		 * 
		 * @param in
		 *            要從中讀取資料的 {@link java.io.InputStream}。
		 * @param options
		 *            指定的選項
		 * @see Base64#ENCODE
		 * @see Base64#DECODE
		 * @see Base64#DONT_BREAK_LINES
		 * @since 2.0
		 */
		public InputStream(java.io.InputStream in, int options) {
			super(in);
			this.breakLines = (options & DONT_BREAK_LINES) != DONT_BREAK_LINES;
			this.encode = (options & ENCODE) == ENCODE;
			this.bufferLength = encode ? 4 : 3;
			this.buffer = new byte[bufferLength];
			this.position = -1;
			this.lineLength = 0;
		} // end constructor

		/**
		 * 讀取足夠的輸入流以進行Base64轉換，並返回下一個位元組。
		 * 
		 * @return 下一個位元組
		 * @since 1.3
		 */
		@Override
		public int read() throws java.io.IOException {
			// 是否需要取得資料？
			if (position < 0) {
				if (encode) {
					byte[] b3 = new byte[3];
					int numBinaryBytes = 0;
					for (int i = 0; i < 3; i++) {
						try {
							int b = in.read();

							// 如果流結束，b 將是 -1。
							if (b >= 0) {
								b3[i] = (byte) b;
								numBinaryBytes++;
							}

						} catch (java.io.IOException e) {
							// 如果根本沒有得到任何資料，這才是問題。
							if (i == 0) {
								throw e;
							}
						}
					}

					if (numBinaryBytes > 0) {
						encode3to4(b3, 0, numBinaryBytes, buffer, 0);
						position = 0;
						numSigBytes = 4;
					} else {
						return -1;
					}
				} else {
					byte[] b4 = new byte[4];
					int i = 0;
					for (i = 0; i < 4; i++) {
						int b = 0;
						do {
							b = in.read();
						} while (b >= 0 && DECODABET[b & 0x7f] <= WHITE_SPACE_ENC);

						if (b < 0) {
							break;
						}

						b4[i] = (byte) b;
					}

					if (i == 4) {
						numSigBytes = decode4to3(b4, 0, buffer, 0);
						position = 0;
					} else if (i == 0) {
						return -1;
					} else {
						throw new java.io.IOException("Improperly padded Base64 input.");
					}
				}
			}

			if (position >= 0) {
				if (!encode && position >= numSigBytes) {
					return -1;
				}

				if (encode && breakLines && lineLength >= MAX_LINE_LENGTH) {
					lineLength = 0;
					return '\n';
				} else {
					lineLength++;
					int b = buffer[position++];

					if (position >= bufferLength) {
						position = -1;
					}

					return b & 0xFF;
				}
			} else {
				throw new java.io.IOException("Error in Base64 code reading stream.");
			}
		}

		@Override
		public int read(byte[] dest, int off, int len) throws java.io.IOException {
			int i;
			int b;
			for (i = 0; i < len; i++) {
				b = read();

				if (b >= 0) {
					dest[off + i] = (byte) b;
				} else if (i == 0) {
					return -1;
				} else {
					break;
				}
			}
			return i;
		}

	} // end inner class InputStream

	/* ******** Inner Class OutputStream ******** */

	/**
	 * A {@link Base64#OutputStream} will write data to another
	 * {@link java.io.OutputStream}, given in the constructor, and
	 * encode/decode to/from Base64 notation on the fly.
	 * 
	 * @see Base64
	 * @see java.io.FilterOutputStream
	 * @since 1.3
	 */
	public static class OutputStream extends java.io.FilterOutputStream {
		private boolean encode;

		private int position;

		private byte[] buffer;

		private int bufferLength;

		private int lineLength;

		private boolean breakLines;

		private byte[] b4;

		private boolean suspendEncoding;

		/**
		 * Constructs a {@link Base64#OutputStream} in ENCODE mode.
		 * 
		 * @param out
		 *            the {@link java.io.OutputStream} to which data will be
		 *            written.
		 * @since 1.3
		 */
		public OutputStream(java.io.OutputStream out) {
			this(out, ENCODE);
		}
		/**
		 * 以 ENCODE 或 DECODE 模式之一構造一個 {@link Base64#OutputStream}。
		 * <p>
		 * 有效的選項有：
		 * 
		 * <pre>
		 *      ENCODE 或 DECODE：在讀取資料時進行編碼或解碼。
		 *      DONT_BREAK_LINES：不在76個字元處斷行
		 *        （僅在編碼時有效）
		 *        &lt;i&gt;注意：技術上，這會使您的編碼不符合規範。&lt;/i&gt;
		 * </pre>
		 * 
		 * <p>
		 * 範例： <code>new Base64.OutputStream( out, Base64.ENCODE )</code>
		 * 
		 * @param out
		 *            要寫入資料的 {@link java.io.OutputStream}。
		 * @param options
		 *            指定的選項。
		 * @see Base64#ENCODE
		 * @see Base64#DECODE
		 * @see Base64#DONT_BREAK_LINES
		 * @since 1.3
		 */
		public OutputStream(java.io.OutputStream out, int options) {
			super(out);
			this.breakLines = (options & DONT_BREAK_LINES) != DONT_BREAK_LINES;
			this.encode = (options & ENCODE) == ENCODE;
			this.bufferLength = encode ? 3 : 4;
			this.buffer = new byte[bufferLength];
			this.position = 0;
			this.lineLength = 0;
			this.suspendEncoding = false;
			this.b4 = new byte[4];
		} // end constructor

		/**
		 * 將位元組寫入輸出流，並在編碼或解碼成 Base64 符號之後。當編碼時，每次寫入三個位元組，才會呼叫實際的 write() 方法。解碼時，每次寫入四個位元組。
		 * 
		 * @param theByte
		 *            要寫入的位元組
		 * @since 1.3
		 */
		@Override
		public void write(int theByte) throws java.io.IOException {
			// 編碼是否暫停？
			if (suspendEncoding) {
				super.out.write(theByte);
				return;
			} // end if: supsended

			// 編碼？
			if (encode) {
				buffer[position++] = (byte) theByte;
				if (position >= bufferLength) // 足夠編碼。
				{
					out.write(encode3to4(b4, buffer, bufferLength));

					lineLength += 4;
					if (breakLines && lineLength >= MAX_LINE_LENGTH) {
						out.write(NEW_LINE);
						lineLength = 0;
					} // end if: end of line

					position = 0;
				} // end if: enough to output
			} // end if: encoding

			// 否則，解碼
			else {
				// 有效的 Base64 符號？
				if (DECODABET[theByte & 0x7f] > WHITE_SPACE_ENC) {
					buffer[position++] = (byte) theByte;
					if (position >= bufferLength) // 足夠輸出。
					{
						int len = Base64.decode4to3(buffer, 0, b4, 0);
						out.write(b4, 0, len);
						// out.write( Base64.decode4to3( buffer ) );
						position = 0;
					} // end if: enough to output
				} // end if: meaningful base64 character
				else if (DECODABET[theByte & 0x7f] != WHITE_SPACE_ENC) {
					throw new java.io.IOException(
							"Invalid character in Base64 data.");
				} // end else: not white space either
			} // end else: decoding
		} // end write

		/**
		 * 重複調用 {@link #write} 直到寫入了 <var>len</var> 個位元組。
		 * 
		 * @param theBytes
		 *            要從中讀取位元組的陣列
		 * @param off
		 *            陣列的偏移量
		 * @param len
		 *            最多要寫入陣列的位元組數
		 * @since 1.3
		 */
		@Override
		public void write(byte[] theBytes, int off, int len)
				throws java.io.IOException {
			// 編碼是否暫停？
			if (suspendEncoding) {
				super.out.write(theBytes, off, len);
				return;
			} // end if: supsended

			for (int i = 0; i < len; i++) {
				write(theBytes[off + i]);
			} // end for: each byte written

		} // end write

		/**
		 * PHIL 添加的方法。[感謝，PHIL。 -Rob] 這個方法在不關閉流的情況下填充緩衝區。
		 */
		public void flushBase64() throws java.io.IOException {
			if (position > 0) {
				if (encode) {
					out.write(encode3to4(b4, buffer, position));
					position = 0;
				} // end if: encoding
				else {
					throw new java.io.IOException(
							"Base64 input not properly padded.");
				} // end else: decoding
			} // end if: buffer partially full

		} // end flush

		/**
		 * 刷新並關閉（我想，在父類中）流。
		 * 
		 * @since 1.3
		 */
		@Override
		public void close() throws java.io.IOException {
			// 1. 確保寫入待定的字元
			flushBase64();

			// 2. 實際關閉流
			// 父類同時刷新和關閉。
			super.close();

			buffer = null;
			out = null;
		} // end close

		/**
		 * 暫停流的編碼。如果您需要將一段 Base64 編碼的資料嵌入流中，這可能會有所幫助。
		 * 
		 * @since 1.5.1
		 */
		public void suspendEncoding() throws java.io.IOException {
			flushBase64();
			this.suspendEncoding = true;
		} // end suspendEncoding

		/**
		 * 恢復流的編碼。如果您需要將一段 Base64 編碼的資料嵌入流中，這可能會有所幫助。
		 * 
		 * @since 1.5.1
		 */
		public void resumeEncoding() {
			this.suspendEncoding = false;
		} // end resumeEncoding

	} // end inner class OutputStream

} // end class Base64
