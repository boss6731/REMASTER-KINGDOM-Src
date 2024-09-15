package l1j.server.MJTemplate.Command;


public interface MJCommand {
	public void execute(MJCommandArgs args);

    void execute(l1j.server.MJTemplate.Command.MJCommandArgs args);
}
