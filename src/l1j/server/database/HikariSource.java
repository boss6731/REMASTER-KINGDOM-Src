package l1j.server.database;

/**
 * <b>使用 Hikari Pool 管理資料來源的類別</b>
 * 
 * @參閱資料庫來源
 **/
public class HikariSource extends DBSource {
	public HikariSource(HikariSourceInfo sInfo) {
		super(sInfo.toHikari());
	}
}