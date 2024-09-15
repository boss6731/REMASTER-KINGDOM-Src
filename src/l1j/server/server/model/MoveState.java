package l1j.server.server.model;

public class MoveState {
	private int _heading; // ● 方向 0. 左上 1. 上 2. 右上 3. 右 4. 右下 5. 下 6. 左下 7. 左

	private int _moveSpeed; // ● 速度 0. 通常 1. 加速 2. 減速

	private int _braveSpeed; // ● 勇敢狀態 0. 通常 1. 勇敢

	public int getHeading() {
		return _heading;
	}

	public void setHeading(int i) {
		_heading = i;
	}

	public int getMoveSpeed() {
		return _moveSpeed;
	}

	public void setMoveSpeed(int i) {
		_moveSpeed = i;
	}

	public int getBraveSpeed() {
		return _braveSpeed;
	}

	public void setBraveSpeed(int i) {
		_braveSpeed = i;
	}
}
