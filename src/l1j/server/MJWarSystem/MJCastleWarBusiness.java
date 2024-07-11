package l1j.server.MJWarSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SIEGE_INJURY_TIME_NOIT;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SIEGE_INJURY_TIME_NOIT.SIEGE_KIND;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SIEGE_ZONE_UPDATE_NOT;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SIEGE_ZONE_UPDATE_NOT.SIEGE_ZONE_KIND;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.BaseTime;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.model.gametime.TimeListener;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.SQLUtil;

public class MJCastleWarBusiness implements TimeListener {
	// 定義當前攻城戰的消息
	private static final ServerBasePacket[] _viewMessages = new ServerBasePacket[]{
			new S_SystemMessage("攻城戰正在進行中。"), // "공성전이 진행중입니다。" 翻譯為 "攻城戰正在進行中。"
			new S_SystemMessage("持有城堡的血盟如下：") // "성을 소유하고 있는 혈맹은 다음과 같습니다。" 翻譯為 "持有城堡的血盟如下："
	};

	private static ProtoOutputStream _offInjury;

	static {
		// 創建並初始化攻城傷害時間通知對象
		SC_SIEGE_INJURY_TIME_NOIT noti = SC_SIEGE_INJURY_TIME_NOIT.newInstance();
		noti.set_siegeKind(SIEGE_KIND.SIEGE_ATTACK); // 設定攻城類型為攻擊
		noti.set_remainSecond(0); // 設定剩餘秒數為0
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_SIEGE_INJURY_TIME_NOIT);
		noti.dispose(); // 釋放資源
		_offInjury = stream; // 將流賦值給 _offInjury
	}
}
	
	public static void move(L1PcInstance pc){
		if(pc.war_zone){
			int castleId = L1CastleLocation.getCastleIdByArea(pc);
			if(castleId == 0 || !getInstance().isNowWar(castleId)){
				pc.war_zone = false;
				SC_SIEGE_ZONE_UPDATE_NOT noti = SC_SIEGE_ZONE_UPDATE_NOT.newInstance();
				noti.set_siegeZoneKind(SIEGE_ZONE_KIND.SIEGE_ZONE_END);
				pc.sendPackets(noti, MJEProtoMessages.SC_SIEGE_ZONE_UPDATE_NOT, true);
				pc.sendPackets(_offInjury, false);
			}
		}else{
			int castleId = L1CastleLocation.getCastleIdByArea(pc);
			MJCastleWar war = getInstance().get(castleId);
			if(castleId != 0 && war.isRun()){
				pc.war_zone = true;
				SC_SIEGE_ZONE_UPDATE_NOT noti = SC_SIEGE_ZONE_UPDATE_NOT.newInstance();
				noti.set_siegeZoneKind(SIEGE_ZONE_KIND.SIEGE_ZONE_BEGIN);
				pc.sendPackets(noti, MJEProtoMessages.SC_SIEGE_ZONE_UPDATE_NOT, true);
				
				SC_SIEGE_INJURY_TIME_NOIT injury = SC_SIEGE_INJURY_TIME_NOIT.newInstance();
				injury.set_remainSecond(war.getSpareSeconds());
				L1Clan clan = pc.getClan();
				if(MJWar.isSameWar(clan, war.getDefenseClan())){
					if(war.getOffenseClan(clan.getClanId()) != null)
						injury.set_siegeKind(SIEGE_KIND.SIEGE_ATTACK);
					else
						injury.set_siegeKind(SIEGE_KIND.SIEGE_DEFFENCE);
					injury.set_pledgeName(clan.getClanName());
				}else{
					injury.set_siegeKind(SIEGE_KIND.SIEGE_ATTACK);
				}				
				ProtoOutputStream stream = injury.writeTo(MJEProtoMessages.SC_SIEGE_INJURY_TIME_NOIT);
				injury.dispose();
				pc.sendPackets(stream, true);
			}
		}
	}
	
	private static MJCastleWarBusiness _instance;
	public static MJCastleWarBusiness getInstance(){
		if(_instance == null)
			_instance = new MJCastleWarBusiness();
		return _instance;
	}

	private ConcurrentHashMap<Integer, MJCastleWar> _castleWars;
	private MJCastleWarBusiness(){
		_castleWars = loadedCastleWars();
	}
	
	public void reload(){
		ConcurrentHashMap<Integer, MJCastleWar> tmp = _castleWars;
		_castleWars = loadedCastleWars();
		if(tmp != null){
			for(MJCastleWar war : tmp.values())
				war.dispose();
			tmp.clear();
			tmp = null;
		}
	}

	public ConcurrentHashMap<Integer, MJCastleWar> loadedCastleWars() {
// 創建一個初始容量為6的並發哈希映射
		ConcurrentHashMap<Integer, MJCastleWar> wars = new ConcurrentHashMap<Integer, MJCastleWar>(6);
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			// 獲取數據庫連接
			con = L1DatabaseFactory.getInstance().getConnection();
			// 準備 SQL 語句
			pstm = con.prepareStatement("SELECT * FROM castle");
			// 執行查詢並獲取結果集
			rs = pstm.executeQuery();
			while (rs.next()) {
				// 獲取 castle_id 欄位的值
				int castleId = rs.getInt("castle_id");
				// 根據 castleId 查找對應的家族
				L1Clan clan = ClanTable.getInstance().findCastleClan(castleId);
				// 創建一個新的 MJCastleWar 對象
				MJCastleWar war = MJWarFactory.createCastleWar(clan, castleId, rs.getString("name"));
				// 設置下一場戰爭的時間
				war.nextCalendar(rs.getTimestamp("war_time"));
				// 設置稅率
				war.setTaxRate(rs.getInt("tax_rate"));
				// 設置公共金錢
				war.setPublicMoney(rs.getInt("public_money"));
				// 將 war 對象存入映射中
				wars.put(castleId, war);
			}
		} catch (Exception e) {
			// 捕獲異常並打印堆棧跟蹤信息
			e.printStackTrace();
		} finally {
			// 關閉資源
			SQLUtil.close(rs, pstm, con);
		}
// 返回所有載入的城堡戰爭
		return wars;
	}
	
	public void updateCastle(int castleId){
		Connection con = null;
		PreparedStatement pstm = null;
		MJCastleWar war = _castleWars.get(castleId);
		try{
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE castle SET name=?, war_time=?, tax_rate=?, public_money=? WHERE castle_id=?");
			int idx = 0;
			
			pstm.setString(++idx, war.getCastleName());
			pstm.setTimestamp(++idx, new Timestamp(war.nextCal().getTimeInMillis()));
			pstm.setInt(++idx, war.getTaxRate());
			pstm.setInt(++idx, war.getPublicMoney());
			pstm.setInt(++idx, war.getCastleId());
			pstm.execute();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(pstm, con);
		}
	}
	
	public void run(){
		RealTimeClock.getInstance().addListener(this, Calendar.SECOND);
	}
	
	@Override
	public void onMonthChanged(BaseTime time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDayChanged(BaseTime time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHourChanged(BaseTime time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMinuteChanged(BaseTime time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSecondChanged(BaseTime time) {
		Calendar cal = time.getCalendar();
		for(MJCastleWar war : _castleWars.values()){
			if(war.isClosing()){
				continue;
			}else if(war.isRun()){
				if(war.isClosingTime(cal))
					war.close();
			}else{
				if(war.isReadyTime(cal))
					war.ready();
				else if(war.isRunTime(cal))
					war.run();
			}
		}
	}
	
	public void dispose(){
		if(_castleWars != null){
			for(MJCastleWar war : _castleWars.values())
				war.dispose();
			_castleWars.clear();
			_castleWars = null;
		}
	}
	
	public boolean isNowWar(int castleId){
		MJCastleWar war = get(castleId);
		if(war == null)
			return false;
		return war.isRun();
	}
	
	public boolean isNowReady(int castleId){
		return get(castleId).isReady();
	}
	
	public MJCastleWar get(int castleId){
		return _castleWars.get(castleId);
	}

	public void proclaim(L1PcInstance pc, int castleId) {
		L1Clan clan = pc.getClan();
		if (clan == null) {
// 如果玩家不在任何家族中，發送編號為272的封包
			pc.sendPackets(272);
			return;
		}

		if (pc.getRedKnightClanId() != 0 || !pc.isCrown() || pc.getId() != clan.getLeaderId()) {
// 如果玩家是紅色騎士團成員、不是君主、或者不是家族領袖，發送編號為478的封包
			pc.sendPackets(478);
			return;
		}

		if (clan.getCastleId() != 0) {
// 如果家族已經擁有城堡，發送編號為474的封包
			pc.sendPackets(474);
			return;
		}

		if (!isNowWar(castleId)) {
// 如果目前沒有進行攻城戰，發送 "공성전이 진행중이지 않습니다。" 信息
			pc.sendPackets("攻城戰尚未進行。");
			return;
		}
	}
		
		if (clan.getAllianceList() != null) {
			String  alliance_clanlist = clan.getAllianceList().toString();
			int first_idx = alliance_clanlist.indexOf("[") + 1;
			int last_idx = alliance_clanlist.lastIndexOf("]");
			if (first_idx > -1 && last_idx > -1) {
				String claninfo = alliance_clanlist.substring(first_idx, last_idx);
				String[] alliance_clan_id = (String[]) MJArrangeParser.parsing(claninfo, ", ", MJArrangeParseeFactory.createStringArrange()).result();
				for (int i = 0; i < alliance_clan_id.length; i++) {
					L1Clan alliance_clan = L1World.getInstance().getClan(Integer.parseInt(alliance_clan_id[i]));
					for(MJCastleWar war : _castleWars.values()){
						if(!war.isRun())
							continue;
						if(alliance_clan == war.getDefenseClan()) {
							pc.sendPackets(1205);	
							return;
						}
					}
				}
			}
		}

// 宣告攻城戰
public void proclaim(L1PcInstance pc, int castleId) {
		L1Clan clan = pc.getClan();
		if (clan == null) {
		pc.sendPackets(272); // 玩家不在任何家族中，發送編號為272的封包
		return;
		}

		if (pc.getRedKnightClanId() != 0 || !pc.isCrown() || pc.getId() != clan.getLeaderId()) {
		pc.sendPackets(478); // 玩家是紅色騎士團成員、不是君主、或者不是家族領袖，發送編號為478的封包
		return;
		}

		if (clan.getCastleId() != 0) {
		pc.sendPackets(474); // 家族已經擁有城堡，發送編號為474的封包
		return;
		}

		if (!isNowWar(castleId)) {
		pc.sendPackets("攻城戰尚未進行。"); // 目前沒有進行攻城戰，發送 "攻城戰尚未進行。" 信息
		return;
		}

		if (pc.getLevel() < Config.ServerAdSetting.WARMINLEVEL) {
		pc.sendPackets(String.format("王子/公主等級 [%d] 以上才能宣告。", Config.ServerAdSetting.WARMINLEVEL)); // 君主/公主等級不足
		return;
		}

		if (clan.getCurrentOnlineMemebers() < Config.ServerAdSetting.WARPLAYER) {
		pc.sendPackets(String.format("在線的家族成員需達到 [%d] 人以上才能宣告。", Config.ServerAdSetting.WARPLAYER)); // 在線家族成員不足
		return;
		}

		get(castleId).proclaim(pc); // 宣告攻城戰
		}

// 查看當前攻城戰狀態
public void viewNowCastleWarState(L1PcInstance pc) {
		boolean isFirst = true;
		for (MJCastleWar war : _castleWars.values()) {
		if (!war.isRun())
		continue;

		if (isFirst) {
		isFirst = false;
		pc.sendPackets(_viewMessages, false); // 發送當前攻城戰的信息
		}
		pc.sendPackets(String.format("[%s=%s 血盟]", war.getCastleName(), war.getDefenseClan().getClanName())); // 發送城堡名稱和防守家族名稱
		}
		}
	
	public int NowCastleWarState(){
		for(MJCastleWar war : _castleWars.values()){
			if(!war.isRun())
				continue;
			return war.getCastleId();	
		}
		return 0;
	}
	
	public int getTaxRate(int castleId){
		return get(castleId).getTaxRate();
	}
	
	public void setTaxRate(int castleId, int tax){
		get(castleId).setTaxRate(tax);
	}
	
	public int getPublicMoney(int castleId){
		return get(castleId).getPublicMoney();
	}
	
	public void setPublicMoney(int castleId, int publicMoney){
		get(castleId).setPublicMoney(publicMoney);
	}
	
	public int getSecurity(int castleId){
		return get(castleId).getCastleSecurity();
	}
	
	public void setSecurity(int castleId, int security){
		get(castleId).setCastleSecurity(security);
	}
	
	public MJCastleWar findWar(String name){
		for(MJCastleWar war : _castleWars.values()){
			if(war.getCastleName().startsWith(name))
				return war;
		}
		return null;
	}
}
