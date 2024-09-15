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
			"Ùí",
			"õÌı­îÜÌ½?(13051)",
			"ÌæĞüíŞ(13052)",
			"ùè?îú(13053)",
	};
	private static StadiumManager _instance;
	public static StadiumManager getInstance(){
		if(_instance == null)
			_instance =new StadiumManager();
		return _instance;
	}
	
	private ConcurrentHashMap<Integer, l1j.server.Stadium.StadiumObject> m_stadiums;
	private StadiumManager(){
		m_stadiums = new ConcurrentHashMap<Integer, l1j.server.Stadium.StadiumObject>();
	}
	
	public void regist(l1j.server.Stadium.StadiumObject obj){
		m_stadiums.put(obj.get_current_play_map_id(), obj);
	}
	
	public void remove(StadiumObject obj){
		m_stadiums.remove(obj.get_current_play_map_id());
	}
	
	public boolean is_on_stadium(int map_id){
		l1j.server.Stadium.StadiumObject obj = m_stadiums.get(map_id);
		return obj != null && obj.is_on();
	}
	
	public boolean is_in_stadium(int map_id){
		StadiumObject obj = m_stadiums.get(map_id);
		return obj != null;
	}
	
	public l1j.server.Stadium.StadiumObject get_stadium(int map_id){
		return m_stadiums.get(map_id);
	}
	
	public void open_stadium(L1PcInstance gm, String param){
		if(param == null || param.equals("")){
			gm.sendPackets(".ÌæĞüíŞ [ÌæĞüíŞûÜØ§]");
			int size = m_index_name_mapped.length;
			for (int i = 1; i < size; ++i) {
				gm.sendPackets(String.format("%d - %s : %s", i, m_index_name_mapped[i], is_on_stadium(m_index_map_mapped[i]) ? "İïßßñé" : "ÔõÓâñé"));
			}
			return;
		}
		
		StringTokenizer token = new StringTokenizer(param);
		int number = Integer.parseInt(token.nextToken());
		if(number <= 0 || number >= m_index_map_mapped.length){
			gm.sendPackets(String.format("%d ûÜÌæĞüíŞÜôğíî¤¡£", number));
			return;
		}
		
		int map_id = m_index_map_mapped[number];
		if(is_in_stadium(map_id)){
			gm.sendPackets(String.format("%d (%d) ûÜÌæĞüíŞì«Ìèî¤òäú¼ñé¡£", number, map_id));
			return;			
		}
		l1j.server.Stadium.StadiumObject obj = new l1j.server.Stadium.StadiumObject(map_id, 10, 1800, m_index_name_mapped[number]);
		obj.execute();
	}
	
	
	public void quit_stadium(L1PcInstance gm, String param){
		StringTokenizer token = new StringTokenizer(param);
		int number = Integer.parseInt(token.nextToken());
		if(number <= 0 || number >= m_index_map_mapped.length){
			gm.sendPackets(String.format("%d ûÜÌæĞüíŞÜôğíî¤¡£", number));
			return;
		}
		
		int map_id = m_index_map_mapped[number];
		l1j.server.Stadium.StadiumObject obj = get_stadium(map_id);
		if(obj == null || !obj.is_on()){
			gm.sendPackets(String.format("%d (%d) ûÜÌæĞüíŞì«ÌèÌ¿áÖûä?Ú±ËÒã·¡£", number, map_id));
			return;
		}
		try {
			obj.do_ended();
			gm.sendPackets(String.format("%d (%d) %sİïßßì«ÌèÌ¿áÖ¡£", number, map_id, m_index_name_mapped[number]));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
