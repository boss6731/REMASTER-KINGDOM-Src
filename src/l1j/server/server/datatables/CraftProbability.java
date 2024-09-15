package l1j.server.server.server.datatables;

import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;

public class CraftProbability {
	public static final CraftProbability DEFAULT = new CraftProbability("craft_probability");
	public static final CraftProbability EVENT = new CraftProbability("craft_probability_event");
	private String m_table_name;
	private HashMap<Integer, Integer> m_craft_probabilities;
	private CraftProbability(String table_name) {
		m_table_name = table_name;
	}
	
	public void load_probabilities(){
		HashMap<Integer, Integer> probs = new HashMap<Integer, Integer>(256);
		Selector.exec(String.format("select * from %s", m_table_name), new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next())
					probs.put(rs.getInt("craft_id"), rs.getInt("prob_by_million"));
			}
		});
		m_craft_probabilities = probs;
	}
	
	public static void reload(){
		DEFAULT.load_probabilities();
		EVENT.load_probabilities();
	}
	
	public Integer get(Integer craft_id){
		return m_craft_probabilities.get(craft_id);
	}
}
