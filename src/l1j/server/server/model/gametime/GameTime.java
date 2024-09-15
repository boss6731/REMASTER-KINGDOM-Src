package l1j.server.server.model.gametime;

public class GameTime extends BaseTime {
	// 2003년 7월 3일 12:00(UTC)이 1월 1일00:00
//	protected static final long BASE_TIME_IN_MILLIS_REAL = 1214913600000L;

	// 2017-01-01T00:00:00
//	protected static final long BASE_TIME_IN_MILLIS_REAL = 1483196400186L;
	protected static final long BASE_TIME_IN_MILLIS_REAL = 1483196400065L;

	@Override
	protected long getBaseTimeInMil() {
		return BASE_TIME_IN_MILLIS_REAL;
	}

	@Override
	protected int makeTime(long timeMillis) {
		if(timeMillis <= BASE_TIME_IN_MILLIS_REAL) {
			throw new IllegalArgumentException();
		}

		int t1 = (int) ((timeMillis - getBaseTimeInMil()) / 1000);
		/*int t3 = t2 % 3; // 시간이 3의 배수가 되도록(듯이) 조정
		return t2 - t3;*/
//		System.out.println(t1);
		return t1 * 6;
	}
}
