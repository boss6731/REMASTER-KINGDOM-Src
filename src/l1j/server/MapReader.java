/*
 * 本程式是自由軟體；您可以重新發佈或修改它
 * 根據自由軟體基金會所發佈的GNU通用公共授權條款
 * 的規定，使用版本為2或(如果您選擇的話)任何後續版本。
 *
 * 本程式是按“原樣”提供，不附帶任何形式的保證，
 * 包括但不限於隱含的商業性和特定用途適用性的保證。
 * 請參閱GNU通用公共授權條款以了解更多詳細信息。
 *
 * 您應該已收到GNU通用公共授權條款的副本；
 * 如果沒有，請訪問Free Software Foundation網站，
 * 或者寫信給Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA。
 *
 * http://www.gnu.org/copyleft/gpl.html
 */

 package l1j.server;

 import java.io.IOException;
 import java.util.Map;
 
 import l1j.server.server.model.map.L1Map;
 
 public abstract class MapReader {
	 /**
	  * 讀取所有文本地圖（抽象類）
	  *
	  * @return Map
	  * @throws IOException
	  */
	 public abstract Map<Integer, L1Map> read() throws IOException;
 
	 /**
	  * 讀取指定地圖編號的文本地圖。
	  *
	  * @param id
	  *            地圖ID
	  * @return L1Map
	  * @throws IOException
	  */
	 public abstract L1Map read(int id) throws IOException;
 
	 /**
	  * 判斷要讀取的地圖文件類型（文本地圖、緩存地圖或V2文本地圖）。
	  *
	  * @return MapReader
	  */
	 public static MapReader getDefaultReader() {
		 if (Config.Synchronization.LoadV2MapFiles) {
			 return new V2MapReader();
		 }
		 if (Config.Synchronization.CacheMapFiles) {
			 return new CachedMapReader();
		 }
		 return new TextMapReader();
	 }
 }
 