//******************************************************************************
// File Name	: L1Node.java
// Description	: 노드 클래스
// Create		: 2003/04/01 JongHa Woo
// Update		: 2008/03/17 SiraSoni
//******************************************************************************
package l1j.server.server.server.model;

public class L1Node {
	public int f;                // f = g+h
	public int h;                // 啟發值
	public int g;                // 到目前為止的距離
	public int x, y;            // 節點的位置
	public L1Node prev;            // 前一個節點
	public L1Node direct[];    // 相鄰的節點
	public L1Node next;        // 下一個節點

	//*************************************************************************
	// Name : L1Node()
	// Desc : 建構子
	//*************************************************************************
	public L1Node() {
		direct = new L1Node[8];
		
		for ( int i = 0; i < 8; i++) {
			direct[i] = null;
		}
	}
}

