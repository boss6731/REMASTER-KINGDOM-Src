/**
 * 타이머 관련 맵 객체
 * 2008. 12. 04
*/

package l1j.server.server.templates;

public class L1TimeMap {

	private int id;
	private int time;
	private int DoorId;

	/**
	 * 基本建構子
	 *
	 * @param (int) id    地圖 ID
	 * @param (int) time  設定的時間(秒)
	 */
	public L1TimeMap(int id, int time) {
		this.id = id;
		this.time = time;
	}

	/**
	 * 基本建構子
	 *
	 * @param (int) id      地圖 ID
	 * @param (int) time    設定的時間(秒)
	 * @param (int) DoorId  設定的門 ID
	 */
	public L1TimeMap(int id, int time, int DoorId) {
		this.id = id;
		this.time = time;
		this.DoorId = DoorId;
	}

	/**
	 * 返回地圖 ID
	 *
	 * @return (int)    地圖 ID
	 */
	public int getId() {
		return id;
	}

	/**
	 * 返回設定的時間
	 *
	 * @return (int)    設定的時間
	 */
	public int getTime() {
		return time;
	}

	/**
	 * 返回設定的門 ID
	 *
	 * @return (int)    門 ID
	 */
	public int getDoor() {
		return DoorId;
	}

	/**
	 * 計算剩餘時間
	 *
	 * @return (boolean)    如果時間結束返回 true，否則返回 false
	 */
	public boolean count() {
		return time-- <= 0;
	}
}