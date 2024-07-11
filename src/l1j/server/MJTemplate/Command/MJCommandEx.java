package l1j.server.MJTemplate.Command;

public interface MJCommandEx{
	public boolean execute(MJCommandArgs args);
	public String commandKey();
}
