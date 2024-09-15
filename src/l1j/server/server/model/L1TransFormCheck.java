package l1j.server.server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class L1TransFormCheck {

	private static Map<Integer, ArrayList<Integer>> _transform_list = new HashMap<Integer, ArrayList<Integer>>();
	private static ArrayList<Integer> list;

	public static void removeTransFormList(int mapid, int monid) {
		list = _transform_list.get(mapid);
		if (list == null)
			return;

		if (list.contains(monid)) {
			list.remove((Object) monid);
		}
	}

	public static void addTransFormList(int mapid, int monid) {
		list = _transform_list.get(mapid);

		if (list == null)
			list = new ArrayList<Integer>();

		list.add(monid);
		_transform_list.put(mapid, list);

	}

	public static boolean isTransFormList(int mapid, int monid) {
		list = _transform_list.get(mapid);
		if (list == null) {
			return false;
		}

		for (Integer i : list) {
			if (i == monid) {
				return true;
			}
		}

		return false;
	}
}
