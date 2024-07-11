package l1j.server.MJBookQuestSystem.Loader;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;

import l1j.server.MJBookQuestSystem.BQSInformation;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.templates.L1Npc;

public class BQSCriteriaNpcMappedService {
	private static final BQSCriteriaNpcMappedService service = new BQSCriteriaNpcMappedService();
	public static BQSCriteriaNpcMappedService service() {
		return service;
	}

	private HashMap<Integer, Integer> criteriaToClassId;
	private HashMap<Integer, Integer> classIdToCriteria;
	private BQSCriteriaNpcMappedService() {
		final HashMap<Integer, Integer> criteriaToClassId = new HashMap<>();
		final HashMap<Integer, Integer> classIdToCriteria = new HashMap<>();
		Selector.exec("select criteria_id, npc_class_id from tb_mbook_criteria", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()) {
					int criteria_id = rs.getInt("criteria_id");
					int npc_class_id = rs.getInt("npc_class_id");
					criteriaToClassId.put(criteria_id, npc_class_id);
					classIdToCriteria.put(npc_class_id, criteria_id);
				}
			}
		});
		this.criteriaToClassId = criteriaToClassId;
		this.classIdToCriteria = classIdToCriteria;
	}
	
	public List<L1Npc> findNpcFromCriteria(int criteria_id){
		if(!criteriaToClassId.containsKey(criteria_id)) {
			return null;
		}
		return NpcTable.getInstance().findNpcAllFromClassId(criteriaToClassId.get(criteria_id));
	}
	
	public BQSInformation findInformationFromNpcClassId(int npc_class_id) {
		if(!classIdToCriteria.containsKey(npc_class_id)) {
			return null;
		}
		return BQSInformationLoader.getInstance().getInformation(classIdToCriteria.get(npc_class_id));
	}
}
