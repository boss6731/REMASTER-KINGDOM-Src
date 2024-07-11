package l1j.server.MJWebServer.Dispatcher.Template.Market.API;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJWebServer.Dispatcher.Template.Market.API.POJO.MJMarketItemObject;
import l1j.server.MJWebServer.Dispatcher.Template.Market.API.POJO.Util.MJMPSEElement;
import l1j.server.server.utils.SQLUtil;

public class MJMarketPriceLoader {
	private static final Object			_lock = new Object();
	private static MJMarketPriceLoader 	_instance;
	public static MJMarketPriceLoader getInstance(){
		if(_instance == null)
			_instance = new MJMarketPriceLoader();
		return _instance;
	}
	
	public static void release(){
		if(_instance != null){
			_instance.clear();
			_instance = null;
		}
	}
	
	public static void reload(){
		MJMarketPriceLoader tmp = _instance;
		_instance = new MJMarketPriceLoader();
		if(tmp != null){
			tmp.clear();
			tmp = null;
		}
	}
	
	private HashMap<Integer, ArrayList<MJMarketItemObject>> _purs;
	private HashMap<Integer, ArrayList<MJMarketItemObject>>	_sells;
	private MJMarketPriceLoader(){
		Connection con 						= null;
		PreparedStatement pstm 				= null;
		ResultSet rs 						= null;
		MJMarketItemObject obj				= null;
		_sells								= new HashMap<Integer, ArrayList<MJMarketItemObject>>(1024);
		_purs								= new HashMap<Integer, ArrayList<MJMarketItemObject>>(1024);
		
		try{
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm		= con.prepareStatement("select * from character_shop order by price asc");
			rs			= pstm.executeQuery();
			while(rs.next()){
				obj = new MJMarketItemObject();
				obj.itemId	= rs.getInt("item_id");
				obj.cname 	= rs.getString("char_name");
				obj.real_name_id_view	= rs.getString("item_name");
				obj.price	= rs.getInt("price");
				obj.iden	= rs.getInt("iden");
				obj.type	= rs.getInt("type");
				obj.icon_id	= rs.getInt("invgfx");
				obj.attr	= rs.getInt("attr");
				obj.enchant	= rs.getInt("enchant");
				obj.count	= rs.getInt("count");
				if(obj.type == 0 && obj.count > 0)
					getAndSet(_sells, obj);
				else if(obj.count > 0)
					getAndSet(_purs, obj);
			}
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	private void getAndSet(HashMap<Integer, ArrayList<MJMarketItemObject>> map, MJMarketItemObject obj){
		ArrayList<MJMarketItemObject> list = map.get(obj.itemId);
		if(list == null){
			list = new ArrayList<MJMarketItemObject>(64);//64->0->64
			map.put(obj.itemId, list);
		}
		
		list.add(obj);
	}
	
	public boolean contains(MJMPSEElement element){
		return _sells.containsKey(element.normalId) || _sells.containsKey(element.blessId) || _sells.containsKey(element.curseId) ||
				_purs.containsKey(element.normalId) || _purs.containsKey(element.blessId) || _purs.containsKey(element.curseId);		
	}
	
	public ArrayList<MJMarketItemObject> getPurchasings(int i){
		synchronized(_lock){
			return _purs.get(i);
		}
	}
	
	public ArrayList<MJMarketItemObject> getSellings(int i){
		synchronized(_lock){
			return _sells.get(i);
		}
	}
	
	public void clear(){
		synchronized(_lock){
			if(_sells != null){
				for(ArrayList<MJMarketItemObject> list : _sells.values()){
					if(list != null) list.clear();
				}
				_sells.clear();
				_sells = null;
			}
			
			if(_purs != null){
				for(ArrayList<MJMarketItemObject> list : _purs.values()){
					if(list != null) list.clear();
				}
				_purs.clear();
				_purs = null;
			}
		}
	}
}
