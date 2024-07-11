package l1j.server.Stadium;

import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.model.Instance.L1PcInstance;

public class StadiumManager {
    private static final int[] m_index_map_mapped = new int[]{
        0, 
        13051,
        13052,
        13053,
    };
    private static final String[] m_index_name_mapped = new String[]{
        "無", 
        "最終對決(13051)",
        "競技場(13052)",
        "陷阱戰(13053)",
    };
    private static StadiumManager _instance;
    public static StadiumManager getInstance(){
        if(_instance == null)
            _instance = new StadiumManager();
        return _instance;
    }
    
    private ConcurrentHashMap<Integer, StadiumObject> m_stadiums;
    private StadiumManager(){
        m_stadiums = new ConcurrentHashMap<Integer, StadiumObject>();
    }
    
    // 註冊球場物件
    public void regist(StadiumObject obj){
        m_stadiums.put(obj.get_current_play_map_id(), obj);
    }
    
    // 移除球場物件
    public void remove(StadiumObject obj){
        m_stadiums.remove(obj.get_current_play_map_id());
    }
    
    // 判斷球場是否正在進行中
    public boolean is_on_stadium(int map_id){
        StadiumObject obj = m_stadiums.get(map_id);
        return obj != null && obj.is_on();
    }
    
    // 判斷玩家是否在球場中
    public boolean is_in_stadium(int map_id){
        StadiumObject obj = m_stadiums.get(map_id);
        return obj != null;
    }
    
    // 獲取球場物件
    public StadiumObject get_stadium(int map_id){
        return m_stadiums.get(map_id);
    }
    
    // 開啟球場
    public void open_stadium(L1PcInstance gm, String param){
        if(param == null || param.equals("")){
            gm.sendPackets(".競技場 [場地編號]");
            int size = m_index_name_mapped.length;
            for(int i=1; i<size; ++i)
                gm.sendPackets(String.format("%d - %s：%s", i, m_index_name_mapped[i], is_on_stadium(m_index_map_mapped[i]) ? "比賽進行中" : "等待中"));
            return;
        }
        
        StringTokenizer token = new StringTokenizer(param);
        int number = Integer.parseInt(token.nextToken());
        if(number <= 0 || number >= m_index_map_mapped.length){
            gm.sendPackets(String.format("%d 號場地不存在。", number));
            return;
        }
        
        int map_id = m_index_map_mapped[number];
        if(is_in_stadium(map_id)){
            gm.sendPackets(String.format("%d(%d) 號場地已經有比賽進行中。", number, map_id));
            return;            
        }
        StadiumObject obj = new StadiumObject(map_id, 10, 1800, m_index_name_mapped[number]);
        obj.execute();
    }
    
    // 結束球場比賽
    public void quit_stadium(L1PcInstance gm, String param){
        StringTokenizer token = new StringTokenizer(param);
        int number = Integer.parseInt(token.nextToken());
        if(number <= 0 || number >= m_index_map_mapped.length){
            gm.sendPackets(String.format("%d 號場地不存在。", number));
            return;
        }
        
        int map_id = m_index_map_mapped[number];
        StadiumObject obj = get_stadium(map_id);
        if(obj == null || !obj.is_on()){
            gm.sendPackets(String.format("%d(%d) 號場地已經結束或尚未開始比賽。", number, map_id));
            return;            
        }
        try {
            obj.do_ended();
            gm.sendPackets(String.format("%d(%d) %s比賽結束。", number, map_id, m_index_name_mapped[number]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
