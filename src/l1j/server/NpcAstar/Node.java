package l1j.server.NpcAstar;

//******************************************************************************
// 文件名    : Node.java
// 描述      : 節點類
// 創建      : 2003/04/01 JongHa Woo
// 更新      :
//******************************************************************************

public class Node {
	public int f; // f = g + h
	public int h; // 啟發式值
	public int g; // 到目前為止的距離
	public int x, y; // 節點的位置
	public Node prev; // 前一個節點
	public Node direct[]; // 相鄰節點
	public Node next; // 下一個節點

// *************************************************************************
// 名稱 : Node()
// 描述 : 構造函數
// *************************************************************************
	Node() {
		direct = new Node[8];
		for(int i = 0; i < 8; i++)direct[i] = null;
	}

	public void close() {
		for(int i = 0; i < 8; i++)direct[i] = null;
		prev = next = null;
	}

	public void clear() {
		f = 0;
		h = 0;
		g = 0;
		x = 0;
		y = 0;
		prev = null;
		direct = null;
		next = null;
	}
}
