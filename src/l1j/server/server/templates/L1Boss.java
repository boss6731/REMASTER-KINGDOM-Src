package l1j.server.server.templates;

import java.util.Random;

import MJFX.UIAdapter.MJUIAdapter;
import l1j.server.server.utils.SystemUtil;

public class L1Boss {
	private int npcid;
	private String mapname;
	private int groupid;
	private int x;
	private int y;
	private int map;
	private int rnd;
	private int movement;
	private int[][] time;
	private int del_time;
	private String monname;
	private String npc_type;
	private String[] yoil;
	private boolean _ment;
	private boolean _yn;

	private String message;
	private String ynmessage;

	public int getNpcId() {
		return npcid;
	}

	public void setNpcId(int id) {
		this.npcid = id;
	}

	public String getMapName() {
		return mapname;
	}

	public void setMapName(String name) {
		this.mapname = name;
	}

	public int getGroupId() {
		return groupid;
	}

	public void setGroupId(int id) {
		this.groupid = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getMap() {
		return map;
	}

	public void setMap(int mapid) {
		this.map = mapid;
	}

	public int getRndLoc() {
		return rnd;
	}

	public void setRndLoc(int i) {
		this.rnd = i;
	}

	public int getMovement_distance() {
		return movement;
	}

	public void setMovement_distance(int i) {
		this.movement = i;
	}

	public String getMonName() {
		return monname;
	}

	public void setMonName(String type) {
		this.monname = type;
	}

	public String getNpcType() {
		return npc_type;
	}

	public void setNpcType(String type) {
		this.npc_type = type;
	}

	public int[][] getTime() {
		return time;
	}

	public void setTime(int[][] time) {
		this.time = time;
	}

	public int getDeleteTime() {
		return del_time;
	}

	public void setDeleteTime(int i) {
		this.del_time = i;
	}

	public String[] getYoil() {
		return yoil;
	}

	public void setYoil(String[] yoil) {
		this.yoil = yoil;
	}

	public boolean isMent() {
		return _ment;
	}

	public void setMent(boolean flag) {
		this._ment = flag;
	}

	public boolean isYn() {
		return _yn;
	}

	public void setYn(boolean flag) {
		this._yn = flag;
	}

	public String getMentMessage() {
		return message;
	}

	public void setMentMessage(String ment) {
		this.message = ment;
	}

	public String getYnMessage() {
		return ynmessage;
	}

	public void setYnMessage(String ment) {
		this.ynmessage = ment;
	}

	private int _display_effect;

	public void set_display_effect(int display_effect) {
		_display_effect = display_effect;
	}

	public int get_display_effect() {
		return _display_effect;
	}

	private int nonespawntimernd;

	public int getnonespawntime() {
		return nonespawntimernd;
	}

	public void setnonespawntime(int i) {
		this.nonespawntimernd = i;
	}

	private int rnd_time_minute;

	public int getRndMinuteTime() {
		return rnd_time_minute;
	}

	public void setRndMinuteTime(int i) {
		rnd_time_minute = i;
	}

	/**
	 * 確認是否是應該生成的時間的函數。
	 *
	 * @param h 小時
	 * @param m 分鐘
	 * @return 是否應生成
	 */
	public boolean isSpawnTime(int h, int m, long current_time) {
		String now_y = SystemUtil.getYoil(System.currentTimeMillis());
		boolean isYoil = false;
		for (String y : yoil) {
			if (y.equalsIgnoreCase("全部") || y.equalsIgnoreCase(now_y))
				isYoil = true;
		}
		if (time == null) {
			System.out.println(String.format("[警告]: spawnlist_boss_date->spawn_time = NULL 存在。"));
			return false;
		}
		if (isYoil == false) {
			return false;
		}

		for (int[] t : time) {
			if (t[0] == h && t[1] == m) {
				return true;
			}
		}
		return false;
	}

	private static Random _random = new Random(System.nanoTime());
	private int[][] real_time;

	public int[][] getRealTime() {
		return real_time;
	}

	public void setRealTime(int[][] time) {
		this.real_time = time;
	}

	public void resetSpawnTime() {
		if (real_time == null || time == null) {
			return;
		}

		int rnd_min;

		for (int i = 0; i < time.length; i++) {
			int[] t = time[i];
			int[] real_t = real_time[i];

			rnd_min = rnd_time_minute == 0 ? 0 : _random.nextInt(rnd_time_minute);

			t[0] = real_t[0];
			t[1] = real_t[1] + rnd_min;

			while (t[1] >= 60) {
				t[1] -= 60;
				t[0] += 1;
			}

			if (t[0] >= 24) {
				t[0] -= 24;
			}

			MJUIAdapter.on_boss_append(npcid, monname + " " + t[0] + ":" + t[1] + " ", x, y, map);
			// System.out.println(monname + " 時間重置 = " + t[0] + ":" + t[1]);
		}
	}

}
