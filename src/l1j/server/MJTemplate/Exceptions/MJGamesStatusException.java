package l1j.server.MJTemplate.Exceptions;

@SuppressWarnings("serial")
public class MJGamesStatusException extends Exception{
	public MJGamesStatusException(String method, String status){
		super(String.format("方法：%s，狀態：%s", method, status));
	}
}
