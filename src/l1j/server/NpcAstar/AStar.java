package l1j.server.NpcAstar;

import java.util.List;

import javolution.util.FastTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1DollInstance;

public class AStar {

	Node OpenNode, ClosedNode;// 開放節點和已關閉節點列表
	private L1Character _cha = null;

	public void setCha(L1Character cha) {
		_cha = cha;
	}

	private static final int LIMIT_LOOP = 200;// 最大迴圈次數
	private FastTable<Node> pool;
	private FastTable<Node> sabu;

	private Node getPool() {
		Node node;
		if(pool.size() > 0){
			node = pool.get(0);
			pool.remove(0);
		}else{
			node = new Node();
		}
		return node;
	}

	private void setPool(Node node) {
		if(node != null){
			node.close();
			if(isPoolAppend(pool, node))pool.add(node);
		}
	}

	// *************************************************************************
	// Name : AStar()
	// Desc : 建構子
	// *************************************************************************
	public AStar() {
		sabu = new FastTable<Node>();
		OpenNode = null;
		ClosedNode = null;
		pool = new FastTable<Node>();
	}

	public void clear() {
		for(Node s : sabu){
			try {
				s.close();
			} catch (Exception e) {
			}
			s.clear();
		}
		for(Node s2 : pool){
			try {
				s2.close();
			} catch (Exception e) {
			}
			s2.clear();
		}
		OpenNode = null;
		ClosedNode = null;
		sabu.clear();
		pool.clear();
		sabu = null;
		pool = null;
	}

	// *************************************************************************
	// Name : ResetPath()
	// Desc : 刪除之前生成的路徑
	// *************************************************************************
	public void cleanTail() {
		Node tmp;
		int cnt = 0;
		while(OpenNode != null){
			cnt++;
			if(_cha != null){
				if(_cha.isDead())return;
				else if(cnt > 10000)return;
			}
			tmp = OpenNode.next;
			setPool(OpenNode);
			OpenNode = tmp;
		}
		cnt = 0;
		while(ClosedNode != null){
			cnt++;
			if(_cha != null){
				if(_cha.isDead()){
					ClosedNode = null;
					return;
				}else if(cnt > 10000)
					return;
			}
			tmp = ClosedNode.next;
			setPool(ClosedNode);
			ClosedNode = tmp;
		}
	}

	// *************************************************************************
	// Name : FindPath()
	// 描述：輸入起始位置和目標位置，返回路徑節點列表
	// *************************************************************************
	// 怪物座標 sx, sy
	// 移動到的座標 tx, ty
	public Node searchTail(L1Object o, int tx, int ty, int m, boolean obj) {
		int calcx = o.getX() - tx;
		int calcy = o.getY() - ty;
		if(o.getMapId() != m || Math.abs(calcx) > 30 || Math.abs(calcy) > 30)
			return null;
		Node src, best = null;
		int count = 0;
		int sx = o.getX();
		int sy = o.getY();

		// 創建初始起始節點
		src = getPool();
		src.g = 0;
		src.h = (tx - sx) * (tx - sx) + (ty - sy) * (ty - sy);
		src.f = src.h;
		src.x = sx;
		src.y = sy;

		// 將起始節點添加到開放節點列表
		OpenNode = src;

		// 路徑查找主循環
		// 如果超過最大迴圈次數則停止路徑查找
		while(count < LIMIT_LOOP) {
			if(_cha != null && _cha.isDead()) return null;
			// 如果開放節點為空則搜索完所有節點，停止路徑查找
			if(OpenNode == null) return null;

			// 獲取開放節點的第一個節點並從開放節點中移除
			best = OpenNode;
			OpenNode = best.next;

			// 將獲取的節點添加到已關閉節點
			best.next = ClosedNode;
			ClosedNode = best;

			// 如果當前獲取的節點是目標節點則路徑查找成功
			if(best.x == tx && best.y == ty) return best;

			// 擴展當前節點的鄰近節點並添加到開放節點
			if(MakeChild(o, best, tx, ty, obj) == 0 && count == 0) return null;
			count++;
		}
		return null;
	}

	// *************************************************************************
	// Name : MakeChild()
	// 描述：擴展輸入節點的相鄰節點
	// *************************************************************************
	// Re-edited to fit the Lineage environment by sabu
	private char makeChild(L1Object o, Node node, int tx, int ty, boolean obj) {
		int x, y;
		char flag = 0;

		x = node.x;
		y = node.y;
		boolean ckckck = false;
		// 檢查是否能移動到相鄰的節點
		for(int i = 0; i < 8; ++i){
			if(ckckck || World.isThroughObject(x, y, o.getMapId(), i)){
				int nx = x + getXY(i, true);
				int ny = y + getXY(i, false);
				boolean ck = true;
				// 目標點的座標無需搜索。
				if((tx != nx || ty != ny) && obj){
					if(o instanceof L1DollInstance) {
						ck = true;
					} else if(World.isDoorAccessible(x, y, o.getMapId(), i) == true) {
						ck = false;
					} else {
						ck = World.isMapdynamic(nx, ny, o.getMapId()) == false;
						ck = !World.isMapdynamic(nx, ny, o.getMapId());
					}
				}
				if(ck){
					MakeChildSub(node, nx, ny, o.getMapId(), tx, ty);
					flag = 1;
				} else if (tx != nx || ty != ny)
					sabu.add(node);
			}
		}
		return flag;
	}

	// *************************************************************************
	// Name : FindPath()
	// 描述：尋找相近的位置.. **能行嗎**
	// *************************************************************************
	// 怪物座標 sx, sy
	// 移動到的座標 tx, ty
	public Node searchNearbyTile(L1Object o, int tx, int ty, int m, boolean obj) {
		int calcx = o.getX() - tx;
		int calcy = o.getY() - ty;
		if(o.getMapId() != m || Math.abs(calcx) > 30 || Math.abs(calcy) > 30)
			return null;

		Node src, best = null;
		int count = 0;
		int sx = o.getX();
		int sy = o.getY();

		// 創建初始起始節點
		src = getPool();
		src.g = 0;
		src.h = (tx - sx) * (tx - sx) + (ty - sy) * (ty - sy);
		src.f = src.h;
		src.x = sx;
		src.y = sy;

		// 將起始節點添加到開放節點列表
		OpenNode = src;

		// 路徑尋找主循環
		// 如果超過最大重複次數則停止路徑尋找
		while(count < LIMIT_LOOP) {
			if(_cha != null && _cha.isDead()) return null;
			// 如果沒有開放節點則停止路徑尋找，因為已經搜索了所有節點
			if(OpenNode == null) return null;

			// 獲取開放節點中的第一個節點，並從開放節點中刪除
			best = OpenNode;
			OpenNode = best.next;

			// 將獲取到的節點添加到關閉節點中
			best.next = ClosedNode;
			ClosedNode = best;

			// 如果當前獲取的節點是目標節點，則路徑尋找成功
			if(best.x == tx && best.y == ty) return best;

			// 擴展當前節點到相鄰節點，並將其添加到開放節點中
			if(makeChild(o, best, tx, ty, obj) == 0 && count == 0) return null;
			count++;
		}
		int tmpdis = 0;
		for(Node saNode : sabu){
			int x = saNode.x;
			int y = saNode.y;
			saNode.h = (tx - x) * (tx - x) + (ty - y) * (ty - y);
			if(tmpdis == 0){
				best = saNode;
				tmpdis = saNode.h;
			}
			if(tmpdis > saNode.h){
				best = saNode;
				tmpdis = saNode.h;
			}
		}

		if(best == null || best.h >= (tx - sx) * (tx - sx) + (ty - sy) * (ty - sy))
			return null;
		if(sabu.size() > 0)
			sabu.clear();
		return best;
	}

	// *************************************************************************
	// Name : MakeChild()
	// 描述：擴展到輸入節點的相鄰節點
	// *************************************************************************
	// 根據天堂環境重新修改 by sabu

	private char MakeChild(L1Object o, Node node, int tx, int ty, boolean obj) {
		int x, y;
		char flag = 0;

		x = node.x;
		y = node.y;
		boolean ckckck = false; // 檢查是否可以移動到相鄰的節點

		for(int i = 0; i < 8; ++i) {
			if(ckckck || World.isThroughObject(x, y, o.getMapId(), i)) {
				int nx = x + getXY(i, true);
				int ny = y + getXY(i, false);
				boolean ck = true;
				// 終點的座標無需搜索。
				if((tx != nx || ty != ny) && obj){
					if(o instanceof L1DollInstance)
						ck = true;
					else if(World.canMoveThroughDoor(x, y, o.getMapId(), i) == true)
						ck = false;
					else
						ck = World.isMapdynamic(nx, ny, o.getMapId()) == false;
				}
				if(ck){
					MakeChildSub(node, nx, ny, o.getMapId(), tx, ty);
					flag = 1;
				}
			}
		}
		return flag;
	}

	// *************************************************************************
	// Name : MakeChildSub()
	// 描述：創建節點。如果節點已經在開放節點或關閉節點中，
	// 則與之前的值比較，如果 f 值更小則更新信息。
	// 如果在關閉節點中，則一起更新其連接的所有節點的信息。
	// *************************************************************************
	private void MakeChildSub(Node node, int x, int y, int m, int tx, int ty) {
		Node old = null, child = null;
		int g = node.g + 1;
		// 如果當前節點在開放節點中並且 f 值較小則更新信息
		if ((old = IsOpen(x, y, m)) != null) {
			if (g < old.g) {
				old.prev = node;
				old.g = g;
				old.f = old.h + old.g;
			}

			// 如果當前節點在關閉節點中並且 f 值較小則更新信息
		} else if ((old = IsClosed(x, y, m)) != null) {
			if (g < old.g) {
				old.prev = node;
				old.g = g;
				old.f = old.h + old.g;
			}
			// 如果是新節點，則創建節點信息並添加到開放節點中
		} else {
			try {
				// 創建新節點
				child = getPool();
				child.prev = node;
				child.g = g;
				child.h = (x - tx) * (x - tx) + (y - ty) * (y - ty);
				child.f = child.h + child.g;
				child.x = x;
				child.y = y;

				// 將新節點添加到開放節點中
				InsertNode(child);
			} catch (Exception e) {
			}
		}
	}

	// *************************************************************************
	// Name : IsOpen()
	// 描述：檢查輸入的節點是否是開放節點
	// *************************************************************************
	private Node IsOpen(int x, int y, int mapid) {
		Node tmp = OpenNode;
		int cnt = 0;
		while(tmp != null){
			cnt++;
			if(_cha != null){
				if(_cha.isDead())
					return null;
				else if(cnt > 10000)
					return null;
			}
			if(tmp.x == x && tmp.y == y)
				return tmp;
			tmp = tmp.next;
		}
		return null;
	}

	// *************************************************************************
	// Name : IsClosed()
	// 描述：檢查輸入的節點是否是關閉節點
	// *************************************************************************
	private Node IsClosed(int x, int y, int mapid) {
		Node tmp = ClosedNode;
		int cnt = 0;
		while(tmp != null){
			cnt++;
			if(_cha != null){
				if(_cha.isDead())
					return null;
				else if(cnt > 10000)
					return null;
			}
			if(tmp.x == x && tmp.y == y)
				return tmp;
			tmp = tmp.next;
		}
		return null;
	}

	// *************************************************************************
	// Name : InsertNode()
	// 描述：根據 f 值將輸入的節點排序並添加到開放節點中
	// 使 f 值較高的節點位於最上面 -> 最佳節點
	// *************************************************************************
	private void InsertNode(Node src) {
		Node old = null, tmp = null;
		int cnt = 0;
		if(OpenNode == null){
			OpenNode = src;
			return;
		}
		tmp = OpenNode;
		while(tmp != null && (tmp.f < src.f)){
			cnt++;
			if(_cha != null){
				if(_cha.isDead())
					return;
				else if(cnt > 10000)
					return;
			}
			old = tmp;
			tmp = tmp.next;
		}
		if(old != null){
			src.next = tmp;
			old.next = src;
		}else{
			src.next = tmp;
			OpenNode = src;
		}
	}

	/**
	 * 檢查是否可以添加到資源池中的函數：如果註冊過多會造成問題，因此需要控制在適當範圍內。
	 * java.lang.OutOfMemoryError: Java heap space
	 *
	 * @param c
	 * @return
	 */
	private boolean isPoolAppend(List<?> pool, Object c) {
		return pool.size() < 200; // 通過總數檢查。
	}

	/**
	 * 根據方向和類型返回適當的座標值設置
	 *
	 * @param h
	 *            : 方向
	 * @param type
	 *            : true ? x : y
	 * @return
	 */
	public int getXY(final int h, final boolean type) {
		int loc = 0;
		switch (h) {
		case 0:
			if(!type)loc -= 1;
			break;
		case 1:
			if(type)loc += 1;
			else	loc -= 1;
			break;
		case 2:if(type)loc += 1;
			break;
		case 3:
			loc += 1;
			break;
		case 4:
			if(!type)loc += 1;
			break;
		case 5:
			if(type)loc -= 1;
			else	loc += 1;
			break;
		case 6:
			if(type)loc -= 1;
			break;
		case 7:
			loc -= 1;
			break;
		}
		return loc;
	}

	public int calcheading(int myx, int myy, int tx, int ty) {
		if(tx > myx && ty > myy)		return 3;
		else if(tx < myx && ty < myy)	return 7;
		else if(tx > myx && ty == myy)	return 2;
		else if(tx < myx && ty == myy)	return 6;
		else if(tx == myx && ty < myy)	return 0;
		else if(tx == myx && ty > myy)	return 4;
		else if(tx < myx && ty > myy)	return 5;
		else							return 1;
	}

}