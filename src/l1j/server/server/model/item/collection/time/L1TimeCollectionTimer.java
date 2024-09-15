package l1j.server.server.model.item.collection.time;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionAblity;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;
import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionStatus;

/**
 * Selectis 展示會增益計時器
 * @author LinOffice
 */
public class L1TimeCollectionTimer implements Runnable {
	private final L1PcInstance owner;
	private L1TimeCollectionAblity ablity;
	private boolean active;

	/**
	 * 基本建構子
	 * @param owner 玩家實體
	 * @param ablity 能力
	 */
	public L1TimeCollectionTimer(L1PcInstance owner, L1TimeCollectionAblity ablity) {
		this.owner = owner;
		this.ablity = ablity;
		this.active = true;
	}

	@Override
	public void run() {
		try{
			if (!active || owner == null || owner.getNetConnection() == null || owner.getTimeCollection() == null) {
				return;
			}
			L1TimeCollectionUser user = owner.getTimeCollection().getUser(ablity.getFlag());
			if (user == null || user.getAblity() == null || !isEquals(user.getAblity())) {
				return;
			}
			// TODO 移除增益效果。
			owner.getTimeCollection().delete(this.owner, user.getObj(), L1TimeCollectionStatus.CLOSE);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 檢查選項是否相同
	 * @param ablity
	 * @return boolean
	 */
	public boolean isEquals(L1TimeCollectionAblity ablity){
		return this.ablity.equals(ablity);
	}

	/**
	 * 變更能力值
	 * @param ablity
	 */
	public void trans(L1TimeCollectionAblity ablity){
		this.ablity = ablity;
	}

	/**
	 * 停止計時器
	 */
	public void cancel(){
		this.active = false;
	}
}
