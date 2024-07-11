package l1j.server.database;

import java.beans.PropertyVetoException;

/**
 * <b>使用c3p0管理資料來源的類別</b>
 * 
 * @參閱資料庫來源
 **/
public class C3PSource extends DBSource {
	public C3PSource(C3PSourceInfo sInfo) throws PropertyVetoException {
		super(sInfo.toComboPooledDataSource());
	}
}
