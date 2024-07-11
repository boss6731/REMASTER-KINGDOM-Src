package l1j.server.MJBotSystem.MJAstar;

import l1j.server.MJBotSystem.Pool.MJNodePool;

/************************************
*
*本課程僅適用於L1(Lineage1) Ai Bot。
*由 mjsoft 製作，2016 年。
*
***********************************/
/** l1節點*在astar演算法中替換 */
public class Node {
	public int x; // ypos
	public int y; // xpos
	public int degree; // 깊이.
	public int distance; // 거리.
	public int factor; // 평가값.
	public Node parent; // 이전 노드

	public Node() {
	}

	public void clear() {
		close();
	}

	public void close() {
		x = y = degree = distance = 0;
		parent = null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(16);
		sb.append("X:").append(x).append(", Y:").append(y);
		return sb.toString();
	}

	public Node clonePos() {
		Node node = MJNodePool.getInstance().pop();
		node.x = x;
		node.y = y;
		return node;
	}
}