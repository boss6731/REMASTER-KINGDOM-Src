package l1j.server.database;

/**
 * <b>使用Hikari Pool來管理DataSource的類別</b>
 * @see DBSource
 **/
public class HikariSource extends DBSource{
	public HikariSource(HikariSourceInfo sInfo){
		super(sInfo.toHikari());
	}
}