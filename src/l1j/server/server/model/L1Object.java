package l1j.server.server.model;

import java.io.Serializable;

import l1j.server.MJTemplate.MJL1Type;
import l1j.server.server.model.Instance.L1PcInstance;

import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.types.Point;

/**
 * 월드상에 존재하는 모든 오브젝트의 베이스 클래스
 */
public class L1Object extends L1PcInstance implements Serializable {
	private static final long serialVersionUID = 1L;
	private L1Location _loc = new L1Location();
	private int _id = 0;

	/*private void checkPosition() {
		if(_loc.getX() != 0 && _loc.getY() != 0 && _loc.getMapId() == 53) {
			if(!_loc.getMap().isPassable(_loc.getX(), _loc.getY())) {
				if(this instanceof L1MonsterInstance) {
					L1MonsterInstance m = (L1MonsterInstance)this;
					try {
						throw new Exception(String.format("%s %d, %d, %d, %d, %d", m.getName(), m.getId(), m.getNpcId(), _loc.getX(), _loc.getY(), _loc.getMapId()));
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}*/

	/**
	 * 오브젝트가 존재하는 MAP의 MAP ID를 돌려준다
	 *
	 * @return MAP ID
	 */
	public short getMapId() {
		return (short) _loc.getMap().getId();
	}

	/**
	 * 오브젝트가 존재하는 MAP의 MAP ID를 설정한다
	 *
	 * @param mapId
	 *            MAP ID
	 */
	public void setMap(short mapId) {
		_loc.setMap(L1WorldMap.getInstance().getMap(mapId));
//		checkPosition();
	}

	private int _clusterX = -1, _clusterY = -1;

	public void setClusterPos(int x, int y) {
		_clusterX = x;
		_clusterY = y;
	}

	public int getClusterX(){return _clusterX;}
	public int getClusterY(){return _clusterY;}




	/**
	 * 오브젝트가 존재하는 MAP를 보관 유지하는 L1Map 오브젝트를 돌려준다
	 *
	 */
	public L1Map getMap() {
		return _loc.getMap();
	}

	/**
	 * 오브젝트가 존재하는 MAP를 설정한다
	 *
	 * @param map
	 *            오브젝트가 존재하는 MAP를 보관 유지하는 L1Map 오브젝트
	 */
	public void setMap(L1Map map) {
		if (map == null) {
			throw new NullPointerException();
		}
		_loc.setMap(map);
//		checkPosition();
	}

	/**
	 * 返回識別物件的ID
	 *
	 * @return 物件ID
	 */
	public int getId() {
		return _id;
	}

	/**
	 * 設定識別物件的ID
	 *
	 * @param id
	 *            物件ID
	 */
	public void setId(int id) {
		_id = id;
	}

	/**
	 * 返回物件所在座標的X值
	 *
	 * @return 座標的X值
	 */
	public int getX() {
		return _loc.getX();
	}

	/**
	 * 設定物件所在座標的X值
	 *
	 * @param x
	 *            座標的X值
	 */
	public void setX(int x) {
		_loc.setX(x);
//        checkPosition();
	}

	/**
	 * 返回物件所在座標的Y值
	 *
	 * @return 座標的Y值
	 */
	public int getY() {
		return _loc.getY();
	}

	/**
	 * 設定物件所在座標的Y值
	 *
	 * @param y
	 *            座標的Y值
	 */
	public void setY(int y) {
		_loc.setY(y);
//        checkPosition();
	}

	/**
	 * 返回維護物件所在位置的L1Location物件的引用。
	 *
	 * @return 維護座標的L1Location物件的引用
	 */
	public L1Location getLocation() {
		return _loc;
	}

	public void setLocation(L1Location loc) {
		_loc.setX(loc.getX());
		_loc.setY(loc.getY());
		_loc.setMap(loc.getMapId());
//        checkPosition();
	}

	public void setLocation(int x, int y, int mapid) {
		_loc.setX(x);
		_loc.setY(y);
		_loc.setMap(mapid);
//		checkPosition();
	}

	/**
	 * 지정된 오브젝트까지의 직선 거리를 돌려준다.
	 */
	public double getLineDistance(L1Object obj) {
		return this.getLocation().getLineDistance(obj.getLocation());
	}

	/**
	 * 返回到指定物件的直線瓷磚數。
	 */
	public int getTileLineDistance(Point obj) {
		return this.getLocation().getTileLineDistance(obj.getLocation());
	}

	/**
	 * 返回到指定物件的瓷磚數。
	 */
	public int getTileDistance(L1Object obj) {
		return this.getLocation().getTileDistance(obj.getLocation());
	}

	/**
	 * 當物件進入玩家的視野內（被認識）時被調用。
	 *
	 * @param perceivedFrom
	 *            認識這個物件的玩家角色
	 */
	public void onPerceive(L1PcInstance perceivedFrom) {
	}

	/**
	 * 當物件與動作發生時調用
	 *
	 * @param actionFrom
	 *            引發動作的玩家角色
	 */
	public void onAction(L1PcInstance actionFrom) {
	}

	/**
	 * 當物件進入玩家的視野內（被認識）時被調用。
	 *
	 * @param perceivedFrom
	 *            認識這個物件的玩家角色
	 */

//    public void onPerceive(L1SupportInstance perceivedFrom) {    }
	/**
	 * 當物件與動作發生時調用
	 *
	 * @param actionFrom
	 *            引發動作的玩家角色
	 */
	public void onAction(L1Character actionFrom) {
	}
	public void onAction(L1PcInstance actionFrom, int adddmg) {}

	/**
	 * 當與物件對話時調用
	 *
	 * @param talkFrom
	 *            發起對話的玩家角色
	 */
	public void onTalkAction(L1PcInstance talkFrom) {
	}

	public int getL1Type(){
		return MJL1Type.L1TYPE_OBJECT;
	}

	public boolean instanceOf(int flg){
		return (getL1Type() & flg) > 0;
	}
	private long effectdeletetime;
	public void setEffectDeleteTime(long i) {
		effectdeletetime = i;
	}
	public long getEffectDeleteTime() {
		return effectdeletetime;
	}
	public void addEffectDeleteTime(long i) {
		effectdeletetime += i;
	}

	private L1Character _skilltarget;
	public void setTarget(L1Character cha) {
		_skilltarget = cha;
	}
	public L1Character getTarget() {
		return _skilltarget;
	}
	private int _skillId;
	public void setSkillId(int skillid) {
		_skillId = skillid;
	}
	public int getSkillId() {
		return _skillId;
	}

	public void sendPackets(String s) {

	}
}
