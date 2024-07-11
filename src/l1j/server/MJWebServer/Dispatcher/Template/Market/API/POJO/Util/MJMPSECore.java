package l1j.server.MJWebServer.Dispatcher.Template.Market.API.POJO.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJString;
import l1j.server.server.utils.SQLUtil;

public class MJMPSECore {
	private static final Object _lock = new Object();
	private static MJMPSECore _instance;
	public static MJMPSECore getInstance(){
		if(_instance == null)
			_instance = new MJMPSECore();
		return _instance;
	}
	
	public static void release(){
		if(_instance != null){
			_instance.store();
			_instance = null;
		}
	}
	
	public static void reload() {
		if(_instance != null){
			_instance.store();
			_instance = new MJMPSECore();
		}
	}
	
	private HashMap<String, MJMPSEElement> 		_elements;
	private HashMap<String, ArrayList<String>>	_keywords;
	
	private MJMPSECore(){
		ArrayList<String> 	dicList = createDictionary();
		ArrayList<String>	keyList = null;
		MJMPSEElement		element	= null;
		int 				dicSize	= dicList.size();
		int					rows	= 0;
		Connection 			con 	= null;
		PreparedStatement 	pstm 	= null;
		ResultSet			rs		= null;
		String				keyword	= null;
		String				key		= null;
		try{
			con = L1DatabaseFactory.getInstance().getConnection();
			_keywords	= new HashMap<String, ArrayList<String>>(dicList.size());
			_elements	= new HashMap<String, MJMPSEElement>(1024);
			for(int i=0; i<dicSize; i++){
				keyword = dicList.get(i);
				pstm = createPstm(con);

				key = String.format("%%%s%%", keyword);
				pstm.setString(1, key);
				pstm.setString(2, key);
				pstm.setString(3, key);
				rs		= pstm.executeQuery();
				rows	= calcRows(rs);
				if(rows <= 0){
					rs.close();
					pstm.close();
					continue;
				}
				keyList = new ArrayList<String>(rows);
				_keywords.put(keyword, keyList);
				while(rs.next()){
					String real_name_id_view = rs.getString("real_name_id_view");
					if(!keyList.contains(real_name_id_view))
						keyList.add(real_name_id_view);
					element		= _elements.get(real_name_id_view);
					if(element == null){
						element 		= new MJMPSEElement();
						element.real_name_id_view 	= real_name_id_view;
						element.icon_id	= rs.getInt("icon_id");
						_elements.put(real_name_id_view, element);
					}
					
					int iden	= rs.getInt("bless");
					if(iden == 0)		element.blessId  = rs.getInt("item_id");
					else if(iden == 1)	element.normalId = rs.getInt("item_id");
					else 				element.curseId	 = rs.getInt("item_id");
				}
				rs.close();
				pstm.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public void store(){
		ArrayList<String> keywords = new ArrayList<String>(_keywords.keySet());
		int size = keywords.size();
		if(size <= 0)
			return;
		
		Connection 			con 		= null;
		PreparedStatement 	pstm		= null;
		try{
			con = L1DatabaseFactory.getInstance().getConnection();
			con.setAutoCommit(false);
			pstm	= con.prepareStatement("insert ignore into tb_mjdictionary_item set schar=?");
			for(int i=0; i<size; i++){
				String s = keywords.get(i);
				pstm.setString(1, s);
				pstm.addBatch();
				pstm.clearParameters();
				if(i % 10000 == 0 && i > 0){
					pstm.executeBatch();
					pstm.clearBatch();
					con.commit();
				}
			}
			pstm.executeBatch();
			pstm.clearBatch();
			con.commit();
		}catch(Exception e){
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	private ArrayList<String> createDictionary(){
		ArrayList<String>	dictionary 	= null;
		Connection 			con 		= null;
		PreparedStatement 	pstm		= null;
		ResultSet 			rs 			= null;
		int					rownum		= 0;
		try{
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm		= con.prepareStatement("select * from tb_mjdictionary_item");
			rs			= pstm.executeQuery();	rs.last();
			rownum 		= rs.getRow();			rs.beforeFirst();
			if(rownum <= 0)
				return new ArrayList<String>();
			
			dictionary 	= new ArrayList<String>(rownum);
			
			while(rs.next())
				dictionary.add(rs.getString("schar"));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return dictionary;
	}
	
	private int calcRows(ResultSet rs) throws SQLException{
		rs.last();
		int r = rs.getRow();
		rs.beforeFirst();
		return r;
	}
	
	public MJMPSEElement getElement(String s){
		return _elements.get(s);
	}
	
	public ArrayList<String> getKeyworlds(String s){
		if(MJString.isNullOrEmpty(s)) {
			return new ArrayList<>();
		}
//		System.out.println(s);
		ArrayList<String> list = _keywords.get(s);
		if(list != null){
			return list;
		}
		
		String				qry		= String.format("%%%s%%", s);
//		System.out.println(qry);
		Connection 			con 	= null;
		PreparedStatement 	pstm 	= null;
		ResultSet			rs		= null;
		MJMPSEElement		element	= null;
		int					rows	= 0;
		try{
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm					= createPstm(con);
			pstm.setString(1, qry);
			pstm.setString(2, qry);
			pstm.setString(3, qry);
//			System.out.println(pstm);
//			System.out.println(pstm.executeQuery());
			rs						= pstm.executeQuery();
			rows					= calcRows(rs);
			if(rows <= 0)
				return null;
			
			list = new ArrayList<String>(rows);
			synchronized(_lock){
				_keywords.put(s, list);
			}
			
			while(rs.next()){
				String real_name_id_view = rs.getString("real_name_id_view");
				if(!list.contains(real_name_id_view))
					list.add(real_name_id_view);
				
				element		= _elements.get(real_name_id_view);
				if(element == null){
					element = new MJMPSEElement();
					element.real_name_id_view	= real_name_id_view;
					element.icon_id 	= rs.getInt("icon_id");
					synchronized(_lock){
						_elements.put(real_name_id_view, element);
					}
				}
				
				int iden	= rs.getInt("bless");
				if(iden == 0)		element.blessId  = rs.getInt("item_id");
				else if(iden == 1)	element.normalId = rs.getInt("item_id");
				else 				element.curseId	 = rs.getInt("item_id");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return list;
	}
	
	private PreparedStatement createPstm(Connection con) throws SQLException{
		return con.prepareStatement("SELECT item_id,real_name_id_view,bless,icon_id FROM etcitem where real_name_id_view like ? and trade=0 and no_warehouse=0 union SELECT item_id,real_name_id_view,bless,icon_id FROM weapon where real_name_id_view like ? and trade=0 and no_warehouse=0 union SELECT item_id,real_name_id_view,bless,icon_id FROM armor where real_name_id_view like ? and trade=0 and no_warehouse=0 limit 50");
	}
}
