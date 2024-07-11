/*
 * 本程序是自由软件；您可以重新发布或修改它
 * 根据自由软件基金会发布的 GNU 通用公共许可证，版本 2 或
 * 任何以后的版本。
 *
 * 本程序发布的目的是希望它有用，
 * 但没有任何担保；甚至没有适销性或特定用途的暗示担保。
 * 请参阅 GNU 通用公共许可证获取更多详细信息。
 *
 * 您应该已收到一份 GNU 通用公共许可证的副本
 * 如果没有，请写信至自由软件基金会，地址为 59 Temple Place，Suite 330，Boston, MA 02111-1307，USA。
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class FileLogFormatter extends Formatter {
	private static final String CRLF = "\r\n";

	private static final String a = "\t";

	@Override
	public String format(LogRecord record) {
		StringBuffer output = new StringBuffer();
		output.append(record.getMillis());
		output.append(a);
		output.append(record.getLevel().getName());
		output.append(a);
		output.append(record.getThreadID());
		output.append(a);
		output.append(record.getLoggerName());
		output.append(a);
		output.append(record.getMessage());
		output.append(CRLF);
		return output.toString();
	}
}
