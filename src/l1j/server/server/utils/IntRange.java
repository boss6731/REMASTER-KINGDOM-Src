package l1j.server.server.utils;

import java.util.Random;

/**
 * <p>
 * 指定數值範圍的類，範圍由最低值 low 和最高值 high 界定。
 * </p>
 * <p>
 * <b>此類不同步。</b> 如果多個線程同時訪問此類的實例，且有一個或多個線程改變了範圍，那麼需要進行外部同步。
 * </p>
 */
public class IntRange {
	private static final Random _rnd = new Random(System.nanoTime());
	private int _low;
	private int _high;

	public IntRange(int low, int high) {
		_low = low;
		_high = high;
	}

	public IntRange(IntRange range) {
		this(range._low, range._high);
	}
	
	public static int random(int number) {
		Random rnd = new Random();
		return rnd.nextInt(number);
	}
	/**
	 * 返回數值 i 是否在範圍內。
	 *
	 * @param i 數值
	 * @return 如果在範圍內返回 true
	 */
	public boolean includes(int i) {
		return (_low <= i) && (i <= _high);
	}

	public static boolean includes(int i, int low, int high) {
		return (low <= i) && (i <= high);
	}

	/**
	 * 將數值 i 限制在此範圍內。
	 *
	 * @param i 數值
	 * @return 限制後的數值
	 */
	public int ensure(int i) {
		int r = i;
		r = (_low <= r) ? r : _low;
		r = (r <= _high) ? r : _high;
		return r;
	}

	public static int ensure(int n, int low, int high) {
		int r = n;
		r = (low <= r) ? r : low;
		r = (r <= high) ? r : high;
		return r;
	}

	/**
	 * 從此範圍內生成一個隨機值。
	 *
	 * @return 範圍內的隨機值
	 */
	public int randomValue() {
		return _rnd.nextInt(getWidth() + 1) + _low;
	}

	public int getLow() {
		return _low;
	}

	public int getHigh() {
		return _high;
	}

	public int getWidth() {
		return _high - _low;
	}

	/** 最接近的整數的浮點數 */
	public static int getTotalValueRint(double val) {
		return (int) Math.rint(val);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IntRange)) {
			return false;
		}
		IntRange range = (IntRange) obj;
		return (this._low == range._low) && (this._high == range._high);
	}

	@Override
	public String toString() {
		return "low=" + _low + ", high=" + _high;
	}
}
