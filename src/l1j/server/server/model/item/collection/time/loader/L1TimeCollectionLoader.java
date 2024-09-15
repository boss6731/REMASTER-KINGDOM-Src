package l1j.server.server.model.item.collection.time.loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollection;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionAblity;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionDuration;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionMaterial;
import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionType;
import l1j.server.server.utils.SQLUtil;

/**
 * 실렉티스 전시회 데이터 로드 클래스
 * @author LinOffice
 */
public class L1TimeCollectionLoader {
	private static Logger _log	= Logger.getLogger(L1TimeCollectionLoader.class.getName());
	private static L1TimeCollectionLoader _instance;
	
	private static final ConcurrentHashMap<L1TimeCollectionType, ConcurrentHashMap<Integer, L1TimeCollection>> DATA = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Integer, L1TimeCollection> FLAG_DATA = new ConcurrentHashMap<>();
	
	/**
	 * 전체 데이터 조사
	 * @return Map
	 */
	public static ConcurrentHashMap<L1TimeCollectionType, ConcurrentHashMap<Integer, L1TimeCollection>> getAllData(){
		return DATA;
	}
	
	/**
	 * 타입별 데이터 조사
	 * @param type
	 * @return Map
	 */
	public static ConcurrentHashMap<Integer, L1TimeCollection> getTypeData(L1TimeCollectionType type){
		return DATA.get(type);
	}
	
	/**
	 * 컬렉션에 대한 데이터 조사
	 * @param type
	 * @param collectionIndex
	 * @return L1TimeCollection
	 */
	public static L1TimeCollection getData(L1TimeCollectionType type, int collectionIndex){
		ConcurrentHashMap<Integer, L1TimeCollection> map = getTypeData(type);
		if (map == null || map.isEmpty()) {
			return null;
		}
		return map.get(collectionIndex);
	}
	
	/**
	 * 컬렉션에 대한 데이터 조사
	 * @param flag
	 * @return L1TimeCollection
	 */
	public static L1TimeCollection getData(int flag){
		return FLAG_DATA.get(flag);
	}
	
	/**
	 * 싱글톤 생성
	 * @return L1TimeCollectionLoader
	 */
	public static L1TimeCollectionLoader getInstance(){
		if (_instance == null) {
			_instance = new L1TimeCollectionLoader();
		}
		return _instance;
	}
	
	/**
	 * 기본 생성자
	 */
	private L1TimeCollectionLoader(){
		load();
	}
	
	/**
	 * 데이터 로드
	 */
	private void load(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		L1TimeCollection obj	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			
			// 기본 데이터 로드
			pstm	= con.prepareStatement("SELECT * FROM time_collection");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				obj = new L1TimeCollection(rs);
				ConcurrentHashMap<Integer, L1TimeCollection> map = DATA.get(obj.getType());
				if (map == null) {
					map = new ConcurrentHashMap<Integer, L1TimeCollection>();
					DATA.put(obj.getType(), map);
				}
				map.put(obj.getCollectionIndex(), obj);
				FLAG_DATA.put(obj.getFlag(), obj);
			}
			SQLUtil.close(rs, pstm);
			
			// 버프 시간 로드 
			pstm	= con.prepareStatement("SELECT * FROM time_collection_duration");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				int flag = rs.getInt("flag");
				obj = FLAG_DATA.get(flag);
				if (obj == null) {
					System.out.println(String.format("[L1TimeCollectionLoader] DURATION NOT FOUND FLAG(%d)", flag));
					continue;
				}
				obj.putDuration(new L1TimeCollectionDuration(rs));
			}
			SQLUtil.close(rs, pstm);
			
			// 재료 아이템 로드
			pstm	= con.prepareStatement("SELECT * FROM time_collection_material");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				int flag = rs.getInt("flag");
				obj = FLAG_DATA.get(flag);
				if (obj == null) {
					System.out.println(String.format("[L1TimeCollectionLoader] MATERIAL NOT FOUND FLAG(%d)", flag));
					continue;
				}
				obj.putMaterial(new L1TimeCollectionMaterial(rs));
			}
			SQLUtil.close(rs, pstm);
			
			// 옵션 로드
			pstm	= con.prepareStatement("SELECT * FROM time_collection_ablity");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				int flag = rs.getInt("flag");
				obj = FLAG_DATA.get(flag);
				if (obj == null) {
					System.out.println(String.format("[L1TimeCollectionLoader] ABLITY NOT FOUND FLAG(%d)", flag));
					continue;
				}
				obj.putAblity(new L1TimeCollectionAblity(rs));
			}
			
			// 데이터의 마지막 수치 설정
			for(L1TimeCollection last : FLAG_DATA.values()){
				last.setLastValue();
			}
		} catch(SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	/**
	 * 데이터 리로드
	 */
	public void reload(){
		if (!DATA.isEmpty()) {
			for (ConcurrentHashMap<Integer, L1TimeCollection> map : DATA.values()) {
				if (map == null || map.isEmpty()) {
					continue;
				}
				map.clear();
			}
			DATA.clear();
		}
		FLAG_DATA.clear();
		load();
	}
}
