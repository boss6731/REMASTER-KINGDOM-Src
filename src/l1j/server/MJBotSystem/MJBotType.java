package l1j.server.MJBotSystem;

import java.util.ArrayList;

import l1j.server.MJBotSystem.AI.MJBotAI;
import l1j.server.MJBotSystem.AI.MJBotRedKnightAI;

public enum MJBotType {
	HUNT(0), // 打獵
	SCARECROW(1), // 稻草人
	FISH(2), // 釣魚
	WANDER(3), // 村莊流浪機器人
	ILLUSION(4), // 錯覺
	SIEGELEADER(5), // 攻城領主
	REDKNIGHT(6), // 紅騎士
	PROTECTOR(7); // 守護神

	private int _type;
	private ArrayList<MJBotAI> _ais;

	MJBotType(int i) {
		_type = i;
		_ais = new ArrayList<MJBotAI>(64);
	}

	public void add(MJBotAI ai) {
		synchronized (_ais) {
			_ais.add(ai);
		}
	}

	public boolean isDefender() {
		if (_ais.size() <= 0)
			return true;

		return ((MJBotRedKnightAI) _ais.get(0)).isDefender();
	}

	public MJBotAI remove(int i) {
		if (_ais.size() <= i)
			return null;

		synchronized (_ais) {
			return _ais.remove(i);
		}
	}

	public void remove(MJBotAI ai) {
		synchronized (_ais) {
			_ais.remove(ai);
		}
	}

	public MJBotAI[] toArray() {
		if (_ais.size() <= 0)
			return null;

		return _ais.toArray(new MJBotAI[_ais.size()]);
	}

	public String toBotList() {
		StringBuilder sb = new StringBuilder(_ais.size() * 7);
		MJBotAI ai = null;
		int cnt = 0;
		int size = _ais.size();
		for (int i = 0; i < size; i++) {
			ai = _ais.get(i);
			if (ai == null || ai.getBody() == null)
				continue;

			if (cnt != 0)
				sb.append(", ");
			sb.append(ai.getBody().getName());
			cnt++;
		}

		return sb.toString();
	}

	public int toInt() {
		return _type;
	}

	public static MJBotType fromInt(int i) {
		MJBotType[] array = MJBotType.values();
		for (int idx = 0; idx < array.length; idx++) {
			if (array[idx]._type == i)
				return array[idx];
		}
		return null;
	}

	public static String enumString() {
		StringBuilder sb = new StringBuilder(128);
		MJBotType[] array = MJBotType.values();
		for (int idx = 0; idx < array.length; idx++) {
			if (idx != 0)
				sb.append(", ");
			sb.append(array[idx].name()).append("(").append(array[idx]._type).append(")");
		}

		return sb.toString();
	}

	public void dispose() {
		synchronized (_ais) {
			_ais.clear();
		}
	}

	public static void reload() {
		MJBotType[] array = MJBotType.values();
		for (int idx = 0; idx < array.length; idx++)
			array[idx].dispose();
	}

	public static void release() {
		reload();
	}
}
