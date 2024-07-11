/*
 * $Header: /cvsroot/l2j/L2_Gameserver/java/net/sf/l2j/Server.java,v 1.2 2004/06/27 08:12:59 jeichhorn Exp $
 *
 * $Author: jeichhorn $
 * $Date: 2004/06/27 08:12:59 $
 * $Revision: 1.2 $
 * $Log: Server.java,v $
 * Revision 1.2  2004/06/27 08:12:59  jeichhorn
 * Added copyright notice
 *
 * 
 * 本程式是自由軟體；您可以根據自由軟體基金會發佈的 GNU 通用公共授權條款
 * （版本 2 或任何後續版本）的條款重新分發和/或修改它。
 *
 * 本程式是基於希望它將是有用的目的分發的，
 * 但沒有任何保證；甚至沒有適合特定用途的暗示保證。
 * 詳情請參閱 GNU 通用公共授權條款。
 *
 * 您應該已收到一份 GNU 通用公共授權條款的副本
 * 如果沒有，請寫信給自由軟體基金會，地址：59 Temple Place - Suite 330, Boston, MA 02111-1307, USA。
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * 控制台日誌格式化器
 */
public class ConsoleLogFormatter extends Formatter {
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 */
	public ConsoleLogFormatter() {
	}

	/**
	 * 格式化日誌記錄
	 * @param record 日誌記錄
	 * @return 格式化後的日誌字串
	 */
	@Override
	public String format(LogRecord record) {
		StringBuffer output = new StringBuffer();
		output.append(record.getMessage());
		output.append("\r\n");

		if (record.getThrown() != null) {
			try {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				record.getThrown().printStackTrace(pw);
				pw.close();
				output.append(sw.toString());
				output.append("\r\n");
			} catch (Exception ex) {
			}
		}
		return output.toString();
	}
}
