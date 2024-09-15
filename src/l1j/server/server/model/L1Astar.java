//******************************************************************************
// File Name	: L1AStar.java
// Description	: A* 알고리즘을 사용한 길찾기 클래스
// Create		: 2003/04/01 JongHa Woo
// Update		: 2008/03/17 SiraSoni
//******************************************************************************
package l1j.server.server.model;

import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;

public class L1Astar {
	// 開放節點和閉合節點列表
	private L1Node OpenNode, ClosedNode;
	// 最大迴圈次數
	private static final int LIMIT_LOOP = 1000;

	//*************************************************************************
	// 名稱 : L1AStar()
	// 描述 : 構造函數
	//*************************************************************************
	public L1Astar() {
		OpenNode = null;
		ClosedNode = null;
	}

	//*************************************************************************
	// 名稱 : ResetPath()
	// 描述 : 移除先前生成的路徑
	//*************************************************************************
	public void ResetPath() {
		L1Node tmp;
		while( OpenNode != null ) {
			tmp = OpenNode.next;
			OpenNode = null;
			OpenNode = tmp;
		}
		while( ClosedNode != null ) {
			tmp = ClosedNode.next;
			ClosedNode = null;
			ClosedNode = tmp;
		}
	}
//*************************************************************************
// 名稱 : FindPath()
// 描述 : 接收起始位置和目標位置，返回路徑節點列表
//*************************************************************************
	public L1Node FindPath(L1NpcInstance npc, L1Character target) {
		return FindPath(npc, target.getX(), target.getY(), target.getMapId(),target);
	}
	
	public L1Node FindPath(L1Object npc, int tx, int ty, int mapId, L1Character target) {
		L1Node	src, best = null;
		int	count = 0;
		
		src = new L1Node();
		src.g = 0;
		src.h = (tx - npc.getX()) * (tx - npc.getX()) + (ty - npc.getY()) * (ty - npc.getY());
		src.f = src.h;
		src.x = npc.getX();
		src.y = npc.getY();
		OpenNode = src;
		
		while (count < LIMIT_LOOP) {
			if ( OpenNode == null ) {
				return best;
			}
			best = OpenNode;
			if (best == null) {
				return null;
			}
			OpenNode = best.next;
			best.next = ClosedNode;
			ClosedNode = best;

			if (Math.max(Math.abs(tx - best.x), Math.abs(ty - best.y)) == 1) {
				return best;
			}
			if( MakeChild(best, tx, ty, npc.getMapId()) == 0 && count == 0 ) {
				return null;
			}
			count++;
		}
		return best;
	}
//*************************************************************************
// 名稱 : MakeChild()
// 描述 : 擴展接收節點的鄰近節點
//*************************************************************************
	public char MakeChild(L1Node node, int tx, int ty, short m) {
		int x, y;
		char flag = 0;
		char cc[] = {0, 0, 0, 0, 0, 0, 0, 0};

		x = node.x;
		y = node.y;
		// 檢查是否可以移動到相鄰節點
		cc[0] = IsMove(x  , y+1, m);
		cc[1] = IsMove(x-1, y+1, m);
		cc[2] = IsMove(x-1, y  , m);
		cc[3] = IsMove(x-1, y-1, m);
		cc[4] = IsMove(x  , y-1, m);
		cc[5] = IsMove(x+1, y-1, m);
		cc[6] = IsMove(x+1, y  , m);
		cc[7] = IsMove(x+1, y+1, m);
		// 如果可以移動的方向，則生成節點並計算評估值
		if ( cc[2] == 1 ) {
			MakeChildSub(node, x-1, y, tx, ty);
			flag = 1;
		}
		if ( cc[6] == 1 ) {
			MakeChildSub(node, x+1, y, tx, ty);
			flag = 1;
		}
		if ( cc[4] == 1 ) {
			MakeChildSub(node, x, y-1, tx, ty);
			flag = 1;
		}
		if ( cc[0] == 1 ) {
			MakeChildSub(node, x, y+1, tx, ty);
			flag = 1;
		}
		if ( cc[7] == 1 && cc[6] == 1 && cc[0] == 1 ) {
			MakeChildSub(node, x+1, y+1, tx, ty);
			flag = 1;
		}
		if ( cc[3] == 1 && cc[2] == 1 && cc[4] == 1 ) {
			MakeChildSub(node, x-1, y-1, tx, ty);
			flag = 1;
		}
		if ( cc[5] == 1 && cc[4] == 1 && cc[6] == 1 ) {
			MakeChildSub(node, x+1, y-1, tx, ty);
			flag = 1;
		}
		if ( cc[1] == 1 && cc[0] == 1 && cc[2] == 1 ){
			MakeChildSub(node, x-1, y+1, tx, ty);
			flag = 1;
		}

		return flag;
	}
	//*************************************************************************
// 名稱 : IsMove()
// 描述 : 檢查位置是否可以移動
//*************************************************************************
	public char IsMove(int x, int y, short mapid) {
		L1Map map = L1WorldMap.getInstance().getMap(mapid);
		if (map.isPassable(x, y) == false) {
			return 0;
		}
		if (map.isExistDoor(x, y) == true) {
			return 0;
		}

		return 1;
	}
	//*************************************************************************
// 名稱 : MakeChildSub()
// 描述 : 生成節點。如果節點已經存在於開放節點或閉合節點列表中，
//        與先前值比較，如果 f 更小，則修改信息。
//        如果它在閉合節點中，則同時修改所有連接的節點的信息。
//*************************************************************************
	public void MakeChildSub(L1Node node, int x, int y, int tx, int ty) {
		L1Node	old = null, child = null;
		int		i;
		int		g = node.g + 1;
		// 현재노드가 열린 노드에 있고 f가 더 작으면 정보 수정
		if ( (old = IsOpen(x, y)) != null ) {
			for ( i = 0; i < 8; i++ ) {
				if ( node.direct[i] == null ) {
					node.direct[i] = old;
					break;
				}
			}
			if ( g < old.g ) {
				old.prev = node;
				old.g = g;
				old.f = old.h + old.g;
			}
		}
		// 如果當前節點在閉合節點列表中且 f 更小，則修改信息
		else if ( (old = IsClosed(x, y)) != null ) {
			for ( i = 0; i < 8; i++ ) {
				if ( node.direct[i] == null ) {
					node.direct[i] = old;
					break;
				}
			}
			if ( g < old.g ) {
				old.prev = node;
				old.g = g;
				old.f = old.h + old.g;
				// 如果當前節點在閉合節點列表中，則修改所有連接的節點的信息
				//MakeDown(old);
			}
		}
				// 如果是新節點，則生成節點信息並加入開放節點列表
		else {
				// 生成新節點
			child = new L1Node();
			
			child.prev = node;
			child.g = g;
			child.h = (x-tx)*(x-tx) + (y-ty)*(y-ty);
			child.f = child.h + child.g;
			child.x = x;
			child.y = y;
			
			// 새로운 노드를 열린노드에 추가
			InsertNode(child);

			for ( i = 0; i < 8; i++ ) {
				if ( node.direct[i] == null ) {
					node.direct[i] = child;
					break;
				}
			}
		}
	}
	//*************************************************************************
// 名稱 : IsOpen()
// 描述 : 檢查輸入的節點是否在開放節點列表中
//*************************************************************************
	public L1Node IsOpen(int x, int y) {
		L1Node tmp = OpenNode;
		while (tmp != null) {
			if (tmp.x == x && tmp.y == y) {
				return tmp;
			}
			tmp = tmp.next;
		}
		return null;
	}

	//*************************************************************************
// 名稱 : IsClosed()
// 描述 : 檢查輸入的節點是否在閉合節點列表中
//*************************************************************************
	public L1Node IsClosed(int x, int y) {
		L1Node tmp = ClosedNode;
		while (tmp != null) {
			if (tmp.x == x && tmp.y == y) {
				return tmp;
			}
			tmp = tmp.next;
		}
		return null;
	}

	//*************************************************************************
// 名稱 : InsertNode()
// 描述 : 根據 f 值排序並將輸入的節點添加到開放節點中
//        f 值最高的節點放在最上面 -> 最優節點
//*************************************************************************
	public void InsertNode(L1Node src) {
		L1Node old = null, tmp = null;
		if( OpenNode == null ) {
			OpenNode = src;
			return;
		}
		tmp = OpenNode;
		while ( tmp != null && (tmp.f < src.f) ) {
			old = tmp;
			tmp = tmp.next;
		}
		if ( old != null ) {
			src.next = tmp;
			old.next = src;
		} else {
			src.next = tmp;
			OpenNode = src;
		}
	}
}
