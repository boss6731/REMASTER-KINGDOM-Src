/*
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

import java.io.IOException;
import java.util.logging.FileHandler;

/**
 * 錯誤日誌處理器
 */
public class ErrorLogHandler extends FileHandler {

	public ErrorLogHandler() throws IOException, SecurityException {

	}
}
