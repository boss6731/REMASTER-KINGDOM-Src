package l1j.server.PowerBall;

public class PowerBallInfo {
	private String _Date;
	private int _num;
	private int _TodatCount;
	private int _plusnum;
	private String _oddEven;
	private String _unover;
	private int _nextnum;
	private Object _TotalNum;
	
	public String getDate() {
		return _Date;
	}

	public void setDate(String t) {
		_Date = t;
	}

	public int getNum() {
		return _num;
	}

	public void setNum(int num) {
		_num = num;
	}

	public int getTodatCount() {
		return _TodatCount;
	}

	public void setTodatCount(int TodatCount) {
		_TodatCount = TodatCount;
	}
	
	public int getPlusNum() {
		return _plusnum;
	}

	public void setPlusNum(int PlusNum) {
		_plusnum = PlusNum;
	}
	
	public String getoddEven() {
		return _oddEven;
	}

	public void setoddEven(String oddEven) {
		_oddEven = oddEven;
	}
	
	public String getUnderOver() {
		return _unover;
	}
	
	public void setUnderOver(String UnderOver) {
		_unover = UnderOver;
	}
	
	public int getNextNum() {
		return _nextnum;
	}

	public void setNextNum(int NextNum) {
		_nextnum = NextNum;
	}
	
	public Object getTotalNum() {
		return _TotalNum;
	}

	public void setTotalNum(Object object) {
		_TotalNum = object;
	}
	
}
