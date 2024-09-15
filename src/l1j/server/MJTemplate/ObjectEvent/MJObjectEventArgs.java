package l1j.server.MJTemplate.ObjectEvent;

/**
 * @author mjsoft
 * <b>오브젝트 이벤트 리스너가 이벤트를 받을 때 사용되는 아규먼트의 최상위 클래스</b>
 * @see MJObjectEventListener
 * @see MJObjectEventComposite
 **/
public abstract class MJObjectEventArgs {
	private boolean canceled;
	private boolean removed;
	protected MJObjectEventArgs(){
		canceled = false;
		removed = false;
	}

	
	
	/**
	 * <b>이벤트를 중지시킬 필요가 있을때 중지시킨다.</b>
	 * <b>다음 리스너부터는 이벤트를 받을 수 없다.</b>
	 **/
	public void cancel(){
		canceled = true;
	}

	
	
	/**
	 * <b>이벤트 중지 상태 여부</b>
	 * @return boolean 중지되었다면 true를 반환한다.
	 **/
	boolean canceled(){
		return canceled;
	}

	
	
	/**
	 * <b>이벤트 목록에서 현재 리스너를 삭제 대기 상태로 전활할 때 사용</b>
	 * <b>반드시 이벤트 핸들러 내에서 사용할 것</b>
	 **/
	public void remove(){
		removed = true;
	}
	
	
	
	void resetRemove(){
		removed = false;
	}

	
	
	/**
	 * <b>현재 리스너가 삭제대기 상태인지</b>
	 * @return boolean 삭제대기 상태라면 true를 반환한다.
	 **/
	boolean removed(){
		return removed;
	}
}
