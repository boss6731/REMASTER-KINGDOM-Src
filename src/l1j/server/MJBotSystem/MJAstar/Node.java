package l1j.server.MJBotSystem.MJAstar;

import l1j.server.MJBotSystem.Pool.MJNodePool;

/**********************************
 * 
 * The class is only for L1(Lineage1) Ai Bot.
 * made by mjsoft, 2016.
 *  
 **********************************/
/* A* 算法用的 L1 節點 */
public class Node {
	public int x;          // ypos
	public int y;          // xpos
	public int degree;     // 深度
	public int distance;   // 距離
	public int factor;     // 評價值
	public Node parent;    // 前一個節點

	public Node(){
	}
	
	public void clear(){
		close();
	}
	
	public void close(){
		x = y = degree = distance = 0;
		parent = null;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder(16);
		sb.append("X:").append(x).append(", Y:").append(y);
		return sb.toString();
	}
	
	public Node clonePos(){
		Node node = MJNodePool.getInstance().pop();
		node.x = x;
		node.y = y;
		return node;
	}
}