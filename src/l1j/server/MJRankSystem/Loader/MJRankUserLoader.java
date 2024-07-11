package l1j.server.MJRankSystem.Loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TOP_RANKER_NOTI;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.utils.SQLUtil;

public class MJRankUserLoader {
	private static MJRankUserLoader _instance;
	public static MJRankUserLoader getInstance(){
		if(_instance == null)
			_instance = new MJRankUserLoader();
		return _instance;
	}
	
	public static void release(){
		if(_instance != null){
			_instance.dispose();
			_instance = null;
		}
	}
	
	public static void reload(){
		MJRankUserLoader tmp = _instance;
		_instance = new MJRankUserLoader();

		if(tmp != null){
			tmp.dispose();
			tmp = null;
		}
	}
	
	private ConcurrentHashMap<Integer, SC_TOP_RANKER_NOTI> _ranks;
	private ArrayList<SC_TOP_RANKER_NOTI> _dummys;
	
	private MJRankUserLoader() {
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		ResultSet 			rs 		= null;
		try{
			_dummys = new ArrayList<SC_TOP_RANKER_NOTI>(MJRankLoadManager.MRK_SYS_TOTAL_RANGE);
			for(int i=MJRankLoadManager.MRK_SYS_TOTAL_RANGE - 1; i>=0; --i)
				_dummys.add(SC_TOP_RANKER_NOTI.newDummyInstance());
			
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from characters where level>=? and Banned=?");
			pstm.setInt(1, MJRankLoadManager.MRK_SYS_MINLEVEL);
			pstm.setInt(2, 0);
			rs = pstm.executeQuery();
			int rows = SQLUtil.calcRows(rs);
			_ranks = new ConcurrentHashMap<Integer, SC_TOP_RANKER_NOTI>(Config.Login.MaximumOnlineUsers > rows ? Config.Login.MaximumOnlineUsers : rows);
			while(rs.next()){
				if(rs.getInt("AccessLevel") == Config.ServerAdSetting.GMCODE)
					continue;
				
				if(isBanAccounts(rs.getString("account_name")))
					continue;
				
				int object_id = rs.getInt("objid");
				L1Object obj = L1World.getInstance().findObject(object_id);
				if(obj != null && obj instanceof L1PcInstance) {
					if(((L1PcInstance)obj).is_shift_client())
						continue;
				}
				
				put(SC_TOP_RANKER_NOTI.newInstance(rs));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	private boolean isBanAccounts(String login){
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		ResultSet 			rs 		= null;
		try{
			con						= L1DatabaseFactory.getInstance().getConnection();
			pstm					= con.prepareStatement("select Banned from accounts where login=?");			
			pstm.setString(1, login);
			rs						= pstm.executeQuery();
			if(rs.next())
				return rs.getInt("Banned") != 0;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
		return false;
	}
	
	public SC_TOP_RANKER_NOTI create_user_information(L1PcInstance pc){
		SC_TOP_RANKER_NOTI noti = SC_TOP_RANKER_NOTI.newInstance(pc);
		put(noti);
		return noti;
	}
	
	public void onUser(L1PcInstance pc) {
		if (pc == null || pc.is_shift_client() || pc.isGm() || pc.getName().equalsIgnoreCase("梅蒂斯") || pc.getName().equalsIgnoreCase("米斯皮") || pc.getName().equalsIgnoreCase("仙后座")) {
			return;
		}
		
		SC_TOP_RANKER_NOTI noti = get(pc.getId());
		if(noti == null){
			if(pc.getLevel() < MJRankLoadManager.MRK_SYS_MINLEVEL)
				return;
			
			noti = create_user_information(pc);
			noti.onBuff();
		}else if(noti.get_characterInstance() == null){
			noti.set_characterInstance(pc);
			noti.onBuff();
		}else{
			if(!pc.is_ranking_buff()){
				noti.set_characterInstance(pc);
				noti.onBuff();
			}
		}
		String name = new String(noti.get_class_ranker().get_name());
		
		if(!name.equalsIgnoreCase(pc.getName()))
			noti.get_class_ranker().set_name(pc.getName().getBytes());
		if(!name.equalsIgnoreCase(pc.getName()))
			noti.get_total_ranker().set_name(pc.getName().getBytes());
		
		
/*		if(!noti.get_class_ranker().get_name().equalsIgnoreCase(pc.getName()))
			noti.get_class_ranker().set_name(pc.getName());
		if(!noti.get_total_ranker().get_name().equalsIgnoreCase(pc.getName()))
			noti.get_total_ranker().set_name(pc.getName());
		*/
		if (MJRankLoadManager.MRK_TOPCLASSPROTECTION_USE) {
			String[] class_rank_item_list = MJRankLoadManager.MRK_TOPCLASSPROTECTION_ID.split(",");
			int class_rank_item = Integer.valueOf(class_rank_item_list[pc.getClassNumber()]);
			String[] class_2nd_rank_item_list = MJRankLoadManager.MRK_2NDCLASSPROTECTION_ID.split(",");
			int class_2nd_rank_item = Integer.valueOf(class_2nd_rank_item_list[pc.getClassNumber()]);
			String[] class_3rd_rank_item_list = MJRankLoadManager.MRK_3RDCLASSPROTECTION_ID.split(",");
			int class_3rd_rank_item = Integer.valueOf(class_3rd_rank_item_list[pc.getClassNumber()]);
			
			if (pc.getInventory().checkItem(class_rank_item)) {
				pc.getInventory().consumeRankItem(pc, class_rank_item, 1, 0);
			}
			if (pc.getInventory().checkItem(class_2nd_rank_item)) {
				pc.getInventory().consumeRankItem(pc, class_2nd_rank_item, 1, 0);
			}
			if (pc.getInventory().checkItem(class_3rd_rank_item)) {
				pc.getInventory().consumeRankItem(pc, class_3rd_rank_item, 1, 0);
			}
			if (getClassRankLevel(pc) == 1) {
				if (!pc.getInventory().checkItem(class_rank_item)) {
					L1ItemInstance tem = ItemTable.getInstance().createItem(class_rank_item);
					L1ItemInstance give_tem = pc.getInventory().storeItem(tem);
					pc.sendPackets(new S_ServerMessage(813, "進入職業排名第1位", give_tem.getLogName(), pc.getName()));
				}
			} else if (getClassRankLevel(pc) == 2) {
				if (!pc.getInventory().checkItem(class_2nd_rank_item)) {
					L1ItemInstance tem = ItemTable.getInstance().createItem(class_2nd_rank_item);
					L1ItemInstance give_tem = pc.getInventory().storeItem(tem);
					pc.sendPackets(new S_ServerMessage(813, "進入職業排名第2位", give_tem.getLogName(), pc.getName()));
				}
			} else if (getClassRankLevel(pc) == 3) {
				if (!pc.getInventory().checkItem(class_3rd_rank_item)) {
					L1ItemInstance tem = ItemTable.getInstance().createItem(class_3rd_rank_item);
					L1ItemInstance give_tem = pc.getInventory().storeItem(tem);
					pc.sendPackets(new S_ServerMessage(813, "進入職業排名第3位", give_tem.getLogName(), pc.getName()));
				}
			}
		
		if (MJRankLoadManager.MRK_TOP_GIVE_ITEM_USE) {
			String[] rank_limit = MJRankLoadManager.MRK_TOP_GIVE_ITEM_LIMIT.split(",");
			String[] rank_item_id = MJRankLoadManager.MRK_TOP_GIVE_ITEM_ID.split(",");
			String[] rank_item_enchant = MJRankLoadManager.MRK_TOP_GIVE_ITEM_ENCHANT.split(",");
			for (int r = 0; r < rank_limit.length; r++) {
				int limit = Integer.valueOf(rank_limit[r]);
				int itemid = Integer.valueOf(rank_item_id[r]);
				int enchant = Integer.valueOf(rank_item_enchant[r]);

				if (pc.getInventory().checkItem(itemid)) {
					if (noti.get_total_ranker().get_rank() != limit)
						pc.getInventory().consumeRankItem(pc, itemid, 1, enchant);
				}
			}

			for (int r = 0; r < rank_limit.length; r++) {
				int limit = Integer.valueOf(rank_limit[r]);
				int itemid = Integer.valueOf(rank_item_id[r]);
				int enchant = Integer.valueOf(rank_item_enchant[r]);

				if (pc.getInventory().checkItem(itemid)) {
					if (noti.get_total_ranker().get_rank() != limit)
						pc.getInventory().consumeRankItem(pc, itemid, 1, enchant);
				}

				if (noti.get_total_ranker().get_rank() == limit) {
					if (!pc.getInventory().checkItem(itemid)) {
						L1ItemInstance tem = ItemTable.getInstance().createItem(itemid);
						tem.setEnchantLevel(enchant);
						L1ItemInstance give_tem = pc.getInventory().storeItem(tem);
						pc.sendPackets(new S_ServerMessage(813, "進入排名", give_tem.getLogName(), pc.getName()));
					}
				}
		if (MJRankLoadManager.MRK_1ST_GIVE_AZIT_USE) {
			int itemid = MJRankLoadManager.MRK_1ST_GIVE_AZIT_KEY_ITEM_ID;
			if (pc.getInventory().checkItem(itemid)) {
				if (noti.get_total_ranker().get_rank() != 1) {
					pc.getInventory().consumeItem(itemid);
				}
			}
			
			if (noti.get_total_ranker().get_rank() == 1) {
				if (!pc.getInventory().checkItem(itemid)) {
					L1ItemInstance tem = ItemTable.getInstance().createItem(itemid);
					L1ItemInstance give_tem = pc.getInventory().storeItem(tem);
				}
			}
		}
	}
	
	public void offUser(L1PcInstance pc) {
		if (pc == null || pc.isGm() || pc.getName().equalsIgnoreCase("梅蒂斯") || pc.getName().equalsIgnoreCase("米斯皮") || pc.getName().equalsIgnoreCase("仙后座"))
			return;

		pc.getInventory().findAndRemoveItemId(MJRankLoadManager.MRK_TOPPROTECTION_ID);
		SC_TOP_RANKER_NOTI noti = get(pc.getId());
		if(noti != null) {
			noti.set_characterInstance(null);
		}
	}
	
	public void banUser(L1PcInstance pc) {
		if (pc == null || pc.isGm() || pc.getName().equalsIgnoreCase("梅蒂斯") || pc.getName().equalsIgnoreCase("米斯皮") || pc.getName().equalsIgnoreCase("仙后座"))
			return;
		
		pc.getInventory().findAndRemoveItemId(MJRankLoadManager.MRK_TOPPROTECTION_ID);
		SC_TOP_RANKER_NOTI noti = remove(pc.getId());
		if(noti != null) {
			noti.set_characterInstance(null);
		}
	}
	
	public void removeUser(L1PcInstance pc) {
		if (pc == null || pc.isGm() || pc.getName().equalsIgnoreCase("梅蒂斯") || pc.getName().equalsIgnoreCase("米斯皮") || pc.getName().equalsIgnoreCase("仙后座"))
			return;
		
		pc.getInventory().findAndRemoveItemId(MJRankLoadManager.MRK_TOPPROTECTION_ID);
		SC_TOP_RANKER_NOTI noti = remove(pc.getId());
		if(noti != null) {
			noti.dispose();
		}
	}

	public void offBuff(){
		GeneralThreadPool.getInstance().execute(new Runnable(){
			@Override
			public void run(){
				try{
					createStream()
					.filter((SC_TOP_RANKER_NOTI noti) -> noti.get_characterInstance() != null)
					.forEach((SC_TOP_RANKER_NOTI noti) ->{
						try{
							noti.offBuff();
						}catch(Exception e){
							e.printStackTrace();
						}
					});
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
	public Stream<SC_TOP_RANKER_NOTI> createStream(){
		return _ranks.values().size() >= 1024 ? _ranks.values().parallelStream() : _ranks.values().stream();
	}
	
	public SC_TOP_RANKER_NOTI get(int id){
		return _ranks.get(id);
	}
	
	public void put(SC_TOP_RANKER_NOTI rnk){
		_ranks.put(rnk.get_objectId(), rnk);
	}
	
	public SC_TOP_RANKER_NOTI remove(int id){
		return _ranks.remove(id);
	}
	
	public SC_TOP_RANKER_NOTI remove(L1PcInstance pc){
		return _ranks.remove(pc.getId());
	}
	
	public SC_TOP_RANKER_NOTI remove(SC_TOP_RANKER_NOTI rnk){
		return _ranks.remove(rnk.get_objectId());
	}
	
	public ArrayList<SC_TOP_RANKER_NOTI> createSortSnapshot(){
		ArrayList<SC_TOP_RANKER_NOTI> rankers = createSnapshot();
		if(rankers != null){
			rankers.addAll(_dummys);
			Collections.sort(rankers);
		}
		return rankers;
	}
	
	public ArrayList<SC_TOP_RANKER_NOTI> createSnapshot(){
		return _ranks.size() <= 0 ? null : new ArrayList<SC_TOP_RANKER_NOTI>(_ranks.values());		
	}
	
	public boolean isRankPoly(L1PcInstance pc){
		SC_TOP_RANKER_NOTI noti = get(pc.getId());
		if(noti == null)
			return false;
		return noti.get_total_ranker().get_rank() <= 20 || noti.get_class_ranker().get_rank() <= 1;
//		return noti.get_total_ranker().get_rating() >= 9 || noti.get_class_ranker().get_rank() <= 1;
//		return noti.get_total_ranker().get_rating() >= 9 || noti.get_class_ranker().get_rank() <= 3;
	}
	
	public int getRankLevel(L1PcInstance pc){
		SC_TOP_RANKER_NOTI noti = get(pc.getId());
		return noti == null ? 0 : noti.get_total_ranker().get_rating();
	}
	
	public int getClassRankLevel(L1PcInstance pc){
		SC_TOP_RANKER_NOTI noti = get(pc.getId());
		return noti == null ? 0 :noti.get_class_ranker().get_rank();
	}
	
	public SC_TOP_RANKER_NOTI getRankNoti(L1PcInstance pc){
		SC_TOP_RANKER_NOTI noti = get(pc.getId());
		return noti;
	}
	
	public void dispose(){
		if(_ranks != null){
			_ranks.clear();
			_ranks = null;
		}
	}
}
