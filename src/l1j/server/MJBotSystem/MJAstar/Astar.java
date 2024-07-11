package l1j.server.MJBotSystem.MJAstar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import l1j.server.MJBotSystem.Loader.MJBotLoadManager;
import l1j.server.MJBotSystem.Pool.MJNodePool;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.utils.MJCommons;

/**********************************
 * 
 * The class is only for L1(Lineage1) Ai Bot.
 * made by mjsoft, 2016.
 * 
 **********************************/
public class Astar {
	private static Comparator<Node> _comp;
	private ArrayList<Node> _opens;
	private ArrayList<Node> _closes;
	private int _range;
	private int _dx;
	private int _dy;
	private int _mid;

	public Astar() {
		if (_comp == null)
			_comp = new Ascending();

		_opens = new ArrayList<Node>(MJBotLoadManager.MBO_ASTAR_OPENS_SIZE);
		_closes = new ArrayList<Node>(MJBotLoadManager.MBO_ASTAR_CLOSES_SIZE);
		_range = 0;
		_dx = 0;
		_dy = 0;
	}

	public void setRange(int i) {
		_range = i;
	}

	public void setMapId(int i) {
		_mid = i;
	}

	public void release() {
		MJNodePool pool = MJNodePool.getInstance();
		if (_opens.size() > 0) {
			pool.push(_opens);
			_opens.clear();
		}
		if (_closes.size() > 0) {
			pool.push(_closes);
			_closes.clear();
		}
	}

	public Node find(int sx, int sy, int dx, int dy) {
		int count = 0;
		Node best = null;
		Node ret = null;
		Node src = MJNodePool.getInstance().pop();
		_dx = dx;
		_dy = dy;
		src.degree = 0;
		src.distance = MJCommons.getDistance(dx, dy, sx, sy);
		src.factor = src.distance;
		src.x = sx;
		src.y = sy;
		_opens.add(src);

		if (_mid < 0)
			_mid = 4;

		if (_range < 1)
			_range = 1;

		if (equalsPos(src) != null)
			return src;

		int x = 0;
		int y = 0;
		while (count < MJBotLoadManager.MBO_ASTAR_LOOP) {
			if (_opens.size() <= 0)
				break;

			Collections.sort(_opens, _comp);
			best = _opens.get(0);
			_opens.remove(0);
			if (best != null)
				_closes.add(best);

			for (int i = 0; i < 8; i++) {
				x = best.x + MJCommons.HEADING_TABLE_X[i];
				y = best.y + MJCommons.HEADING_TABLE_Y[i];
				if (isAvailableTile(x, y, i)) {
					ret = extendChildNode(best, x, y, i);
					if (ret != null)
						return ret;
				}
			}
			count++;
		}
		return best;
	}

	/** 展開子節點。 **/
	private Node extendChildNode(Node node, int x, int y, int h) {
		int i = 0;
		int size = 0;
		int degree = node.degree + 1;
		Node old = null;
		Node child = null;

		size = _opens.size();
		for (i = 0; i < size; i++) {
			old = _opens.get(i);
			if (old == null)
				continue;

			if (old.x == x && old.y == y) {
				if (degree < old.degree) {
					old.parent = node;
					old.degree = degree;
					old.factor = old.distance + degree;
				}
				return null;
			}
		}

		size = _closes.size();
		for (i = 0; i < size; i++) {
			old = _closes.get(i);
			if (old == null)
				continue;

			if (old.x == x && old.y == y) {
				if (degree < old.degree) {
					old.parent = node;
					old.degree = degree;
					old.factor = old.distance + degree;
				}
				return null;
			}
		}
		child = MJNodePool.getInstance().pop();
		child.parent = node;
		child.degree = degree;
		child.distance = MJCommons.getDistance(x, y, _dx, _dy);
		child.factor = child.distance + degree;
		child.x = x;
		child.y = y;
		_opens.add(child);

		return equalsPos(child);
	}

	/** 節點是否在目的地容差範圍內？為了減少比較，返回輸入為 node *的節點類型 */
	private Node equalsPos(Node node) {
		int distanceX = Math.abs(node.x - _dx);
		int distanceY = Math.abs(node.y - _dy);
		if (distanceX <= _range && distanceY <= _range) {
			if (_range >= 2) {
				if (!MJCommons.isPassableLine(node.x, node.y, _dx, _dy, (short) _mid))
					return null;
			}
			return node;
		}
		return null;
	}

	/** 你能移動到對應的方塊嗎？ **/
	public boolean isAvailableTile(int x, int y, int h) {
		L1Map map = L1WorldMap.getInstance().getMap((short) _mid);
		if (!map.isPassable(x, y))
			return false;
		if (map.isExistDoor(x, y))
			return false;
		return true;
	}

	/** 支援排序的類別。 **/
	public class Ascending implements Comparator<Node> {

		@Override
		public int compare(Node node1, Node node2) {
			if (node1 == null)
				return 1;
			if (node2 == null)
				return -1;
			if (node1.degree < node2.degree)
				return 1;
			else if (node1.degree > node2.degree)
				return -1;
			else if (node2.factor < node1.factor)
				return 1;
			else if (node2.factor > node1.factor)
				return -1;
			return 0;
		}
	}
}
