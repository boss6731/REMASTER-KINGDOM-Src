package l1j.server.MJTemplate.Spawn;

import java.lang.reflect.Constructor;
import java.util.Collection;

import l1j.server.Config;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1MobGroupSpawn;
import l1j.server.server.model.L1Spawn;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.types.Point;

public abstract class MJSpawnInfo {
	private int m_spawn_id;
	private String m_location_name;
	private int m_npc_id;
	private int m_count;
	private int m_group_Id;
	private int m_loc_x;
	private int m_loc_y;
	private int m_area_left;
	private int m_area_top;
	private int m_area_right;
	private int m_area_bottom;
	private int m_mapid;
	private int m_movement_distance;
	private int m_spawn_type;
	private int m_delete_delay;
	protected MJSpawnInfo(){
	}

	public abstract boolean is_spawn(long current_millis);
	public abstract void on_death(L1NpcInstance npc);
	private void delete_instance() {
		L1NpcInstance npc = MJSpawnUpdator.getInstance().remove_npc_info(m_spawn_id);
		if(npc != null && !npc.isDead()) {
			npc.deleteMe();
		}
	}

	public L1NpcInstance do_spawn() {
		try {
			delete_instance(); // 刪除當前的 NPC 實例
			L1Npc template = NpcTable.getInstance().getTemplate(get_npc_id()); // 根據 NPC ID 獲取 NPC 模板
			if(template == null) {
				System.out.println(String.format("MJSpawnInfo::do_spawn()...
						找不到 NPC 模板信息... NPC ID : %s", get_npc_id()));
				return null;
			}
// 根據模板創建 NPC 實例
			Constructor<?> constructor = Class.forName(String.format("l1j.server.server.model.Instance.%sInstance", template.getImpl())).getConstructors()[0];
			L1NpcInstance npc = (L1NpcInstance)constructor.newInstance(template);
			npc.setId(get_spawn_id()); // 設置 NPC 實例 ID
			npc.setHeading(MJRnd.next(8)); // 設置 NPC 面朝方向
			npc.setMap((short)get_mapid()); // 設置 NPC 所在地圖
			npc.setMovementDistance(get_movement_distance()); // 設置 NPC 移動距離
			npc.setRest(false); // 設置 NPC 不在休息狀態
			update_location(npc); // 更新 NPC 位置
			if (npc instanceof L1MonsterInstance) {
				L1MonsterInstance mob = (L1MonsterInstance)npc;
				mob.initHide(); // 初始化隱藏狀態
				if(mob.getHiddenStatus() == 0)
					mob.onNpcAI(); // 啟動 NPC AI

			} else if (npc instanceof L1MerchantInstance) {
				L1MerchantInstance merchant = (L1MerchantInstance)npc;
				merchant.getMap().setPassable(merchant.getLocation(), false); // 設置地圖位置信息為不可通行
			}

			return npc;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		public L1NpcInstance do_spawn() {
			try {
				delete_instance(); // 刪除當前的 NPC 實例
				L1Npc template = NpcTable.getInstance().getTemplate(get_npc_id()); // 根據 NPC ID 獲取 NPC 模板
				if (template == null) {
					System.out.println(String.format("MJSpawnInfo::do_spawn()...
							找不到 NPC 模板信息... NPC ID: %s", get_npc_id()));
					return null;
				}
// 根據模板創建 NPC 實例
				Constructor<?> constructor = Class.forName(String.format("l1j.server.server.model.Instance.%sInstance", template.getImpl())).getConstructors()[0];
				L1NpcInstance npc = (L1NpcInstance) constructor.newInstance(template);
				npc.setId(get_spawn_id()); // 設置 NPC 實例 ID
				npc.setHeading(MJRnd.next(8)); // 設置 NPC 面朝方向
				npc.setMap((short) get_mapid()); // 設置 NPC 所在地圖
				npc.setMovementDistance(get_movement_distance()); // 設置 NPC 移動距離
				npc.setRest(false); // 設置 NPC 不在休息狀態
				update_location(npc); // 更新 NPC 位置

				if (npc instanceof L1MonsterInstance) {
					L1MonsterInstance mob = (L1MonsterInstance) npc;
					mob.initHide(); // 初始化隱藏狀態
					if (mob.getHiddenStatus() == 0)
						mob.onNpcAI(); // 啟動 NPC AI

				} else if (npc instanceof L1MerchantInstance) {
					L1MerchantInstance merchant = (L1MerchantInstance) npc;
					merchant.getMap().setPassable(merchant.getLocation(), false); // 設置地圖位置信息為不可通行
				}

				npc.setRespawn(false); // 設置 NPC 不會重生
				npc.set_spawn_ex(this); // 設置 NPC 的生成信息
				L1World.getInstance().storeObject(npc); // 將 NPC 存儲到世界對象中
				L1World.getInstance().addVisibleObject(npc); // 將 NPC 添加到可見對象中

				boolean spawntype = false;
				if (get_spawn_type() == 1)
					spawntype = true;

				if (get_group_id() != 0) {
					L1MobGroupSpawn.getInstance().doSpawn(npc, get_group_id(), spawntype, false); // 生成 NPC 群組
				}

				npc.getLight().turnOnOffLight(); // 開啟或關閉 NPC 的光源
				npc.startChat(L1NpcInstance.CHAT_TIMING_SPAWN); // 啟動 NPC 生成時的聊天
				npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 啟動 NPC 出現時的聊天

				return npc;

			} catch (Exception e) {
				System.out.println(String.format("MJSpawnInfo::do_spawn()...
						生成 ID: %d / NPC ID: %d", get_spawn_id(), get_npc_id()));
				e.printStackTrace();
			}
			return null;
		}
	
	private void update_location(L1NpcInstance npc) {
		L1Location loc = null;
		Point pt = null;
		for(int try_count = 50; try_count >= 0; --try_count) {
			if(is_pc_around_spawn()) {
				loc = on_location_around();
				if(loc == null) {
					loc = on_location_area();
				}
			}else if(is_area_spawn()) {
				loc = on_location_area();
			}else {
				loc = on_location_normal();
			}
			if(loc == null)
				continue;
			
			L1Map m = loc.getMap();
			
			if (m.isInMap(loc) && m.isPassable(loc)) {
				if (L1World.getInstance().getVisibleNpc(loc, npc, 5)) {
					pt = new Point(loc.getX(), loc.getY());
					loc = new L1Location(pt, m_mapid).randomLocation(Config.ServerAdSetting.SPAWNHOMEPOINTRANGE, false);
					npc.setX(loc.getX());
					npc.setHomeX(loc.getX());
					npc.setY(loc.getY());
					npc.setHomeY(npc.getY());
				} else {
					pt = new Point(loc.getX(), loc.getY());
					loc = new L1Location(pt, m_mapid).randomLocation(Config.ServerAdSetting.SPAWNHOMEPOINTRANGE, false);
					npc.setX(loc.getX());
					npc.setHomeX(loc.getX());
					npc.setY(loc.getY());
					npc.setHomeY(npc.getY());
				}
				return;
			}
		}
		loc = on_location_normal();
		npc.setX(loc.getX());
		npc.setHomeX(loc.getX());
		npc.setY(loc.getY());
		npc.setHomeY(npc.getY());
	}
	
	private L1Location on_location_normal() {
		return new L1Location(
				get_loc_x(),
				get_loc_y(),
				get_mapid()
				);
	}
	
	private L1Location on_location_area() {
		int x = m_area_left > m_area_right ? MJRnd.next(m_area_right, m_area_left) : MJRnd.next(m_area_left, m_area_right);
		int y = m_area_top > m_area_bottom ? MJRnd.next(m_area_bottom, m_area_top) :MJRnd.next(m_area_top, m_area_bottom);
		return new L1Location(x, y, get_mapid());
	}
	
	private L1Location on_location_around() {
		Collection<L1PcInstance> collections = L1World.getInstance().getVisiblePlayers(get_mapid());
		if(collections == null || collections.size() <= 0)
			return null;
		
		int size =  collections.size();
		int selected_index = MJRnd.next(size);
		int current_index = 0;
		L1PcInstance selected_pc = null;
		for(L1PcInstance pc : collections) {
			if(current_index == selected_index) {
				selected_pc = pc;
				break;
			}
			++current_index;
		}

		return selected_pc.getLocation().randomLocation(L1Spawn.PC_AROUND_DISTANCE, false);
	}
	
	public boolean is_pc_around_spawn() {
		return get_spawn_type() == L1Spawn.SPAWN_TYPE_PC_AROUND;
	}
	
	public boolean is_area_spawn() {
		return get_area_left() != 0 || 
				get_area_top() != 0 ||
				get_area_right() != 0 ||
				get_area_bottom() != 0;
	}
	
	public MJSpawnInfo set_spawn_id(int spawn_id){
		m_spawn_id = spawn_id;
		return this;
	}
	public MJSpawnInfo set_location_name(String location_name){
		m_location_name = location_name;
		return this;
	}
	public MJSpawnInfo set_count(int count){
		m_count = count;
		return this;
	}
	public MJSpawnInfo set_npc_id(int npc_id){
		m_npc_id = npc_id;
		return this;
	}
	
	public MJSpawnInfo set_group_id(int group_id){
		m_group_Id = group_id;
		return this;
	}
	public MJSpawnInfo set_loc_x(int loc_x){
		m_loc_x = loc_x;
		return this;
	}
	public MJSpawnInfo set_loc_y(int loc_y){
		m_loc_y = loc_y;
		return this;
	}
	public MJSpawnInfo set_area_left(int area_left){
		m_area_left = area_left;
		return this;
	}
	public MJSpawnInfo set_area_top(int area_top){
		m_area_top = area_top;
		return this;
	}
	public MJSpawnInfo set_area_right(int area_right){
		m_area_right = area_right;
		return this;
	}
	public MJSpawnInfo set_area_bottom(int area_bottom){
		m_area_bottom = area_bottom;
		return this;
	}
	public MJSpawnInfo set_mapid(int mapid){
		m_mapid = mapid;
		return this;
	}
	public MJSpawnInfo set_movement_distance(int movement_distance){
		m_movement_distance = movement_distance;
		return this;
	}
	public MJSpawnInfo set_spawn_type(int spawn_type){
		m_spawn_type = spawn_type;
		return this;
	}
	
	public MJSpawnInfo set_delete_delay(int delay){
		m_delete_delay = delay;
		return this;
	}

	public int get_spawn_id(){
		return m_spawn_id;
	}
	public String get_location_name(){
		return m_location_name;
	}
	public int get_count(){
		return m_count;
	}	
	public int get_npc_id(){
		return m_npc_id;
	}	
	public int get_group_id(){
		return m_group_Id;
	}
	public int get_loc_x(){
		return m_loc_x;
	}
	public int get_loc_y(){
		return m_loc_y;
	}
	public int get_area_left(){
		return m_area_left;
	}
	public int get_area_top(){
		return m_area_top;
	}
	public int get_area_right(){
		return m_area_right;
	}
	public int get_area_bottom(){
		return m_area_bottom;
	}
	public int get_mapid(){
		return m_mapid;
	}
	public int get_movement_distance(){
		return m_movement_distance;
	}
	public int get_spawn_type(){
		return m_spawn_type;
	}
	public int get_delete_delay(){
		return m_delete_delay;
	}
}