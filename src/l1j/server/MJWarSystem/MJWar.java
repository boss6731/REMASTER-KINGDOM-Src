package l1j.server.MJWarSystem;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

import l1j.server.MJWarSystem.MJWarFactory.WAR_TYPE;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_War;

public class MJWar {
	public static MJWar newInstance(L1Clan defense, WAR_TYPE warType, int warId){
		MJWar war = new MJWar(defense, warType, warId);
		return war;
	}
	
	public static boolean isSameWar(L1Clan c1, L1Clan c2){
		if(c1 == null || c2 == null)
			return false;
		
		MJWar w1 = c1.getCurrentWar();
		MJWar w2 = c2.getCurrentWar();
		if(w1 == null || w2 == null)
			return false;
		
		return w1.equals(w2);
	}
	
	public static boolean isNowWar(L1Clan clan){
		return clan != null && clan.getCurrentWar() != null;
	}
	
	public static boolean isOffenseClan(L1Clan clan){
		if(clan == null)
			return false;
		
		MJWar war = clan.getCurrentWar();
		if(war == null)
			return false;
		return war.getOffenseClan(clan.getClanId()) != null;
	}
	
	public static boolean isCastleOffenseClan(L1Clan clan){
		if(clan == null)
			return false;
		
		MJWar war = clan.getCurrentWar();
		if(war == null || !war.getWarType().equals(WAR_TYPE.CASTLE))
			return false;
		
		return war.getOffenseClan(clan.getClanId()) != null;
	}
	
	protected int						_warId;
	protected HashMap<Integer, L1Clan> 	_offenses;
	protected L1Clan					_defense;
	protected int						_defenseId;
	protected WAR_TYPE					_warType;
	protected Lock						_warLock;
	protected MJWar(L1Clan defense, WAR_TYPE warType, int id){
		_warType = warType;
		_offenses = new HashMap<Integer, L1Clan>(8);
		_warLock = new ReentrantLock();
		_warId = id;
		setDefenseClan(defense);
		L1World.getInstance().addWar(this);
	}
	
	public int getId(){
		return _warId;
	}
	
	public WAR_TYPE getWarType(){
		return _warType;
	}
	
	public void updateDefense(L1Clan nextDefense){
		try{
			_warLock.lock();
			if(WAR_TYPE.CASTLE.equals(_warType)){
				broadcastWinner(nextDefense, _defense);
				for(L1Clan clan : _offenses.values()){
					broadcastEndWar(_defense, clan);
					clan.setCurrentWar(null);
				}
			}else{
				notifyWinner(nextDefense, _defense);
				for(L1Clan clan : _offenses.values()){
					notifyEndWar(_defense, clan);
					clan.setCurrentWar(null);
				}
			}
			_defense.setCurrentWar(null);
			_offenses.clear();
			setDefenseClan(nextDefense);
		}finally{
			_warLock.unlock();
		}
	}
	
	public void register(L1Clan offense){
		putOffenseClan(offense);
		if(_warType == WAR_TYPE.CASTLE){
			broadcastDeclaration(_defense, offense);
		}else if(_warType == WAR_TYPE.NORMAL){
			notifyDeclaration(_defense, offense);
		}
	}
	
	public void dispose(){
		try{
			_warLock.lock();
			L1World.getInstance().removeWar(this);
			if(_defense != null){
				_defense.setCurrentWar(null);
				_defense = null;
			}
			if(_offenses != null){
				for(L1Clan c : _offenses.values())
					c.setCurrentWar(null);
				_offenses.clear();
				_offenses = null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			_warLock.unlock();
		}
	}
	
	public void broadcastDeclaration(L1Clan defense, L1Clan offense){
		broadcast(1, offense, defense);
	}
	
	public void broadcastSurrender(L1Clan winner, L1Clan loser){
		broadcast(2, loser, winner);
	}
	
	public void broadcastEndWar(L1Clan defense, L1Clan offense){
		broadcast(3, defense, offense);
	}
	
	public void broadcastWinner(L1Clan winner, L1Clan loser){
		broadcast(4, winner, loser);
	}
	
	public void broadcast(int type, L1Clan c1, L1Clan c2){
		L1World.getInstance().broadcastPacketToAll(new S_War(type, c1.getClanName(), c2.getClanName()));
	}
	
	public void notifyDeclaration(L1Clan defense, L1Clan offense){
		notify(1, offense, defense);
	}
	
	public void notifySurrender(L1Clan winner, L1Clan loser){
		notify(2, loser, winner);
	}
	
	public void notifyEndWar(L1Clan defense, L1Clan offense){
		notify(3, defense, offense);
	}
	
	public void notifyWinner(L1Clan winner, L1Clan loser){
		notify(4, winner, loser);
	}
	
	public void notify(int type, L1Clan c1, L1Clan c2){
		S_War war = new S_War(1, c1.getClanName(), c2.getClanName());
		c1.broadcast(war, false);
		c2.broadcast(war, true);
	}
	
	public void notifyEnenmy(L1PcInstance pc){
		int clanId = pc.getClanid();
		if(clanId <= 0 || _defense == null || _offenses.size() <= 0)
			return;
		
		final String clanName = pc.getClanname();
		if(_defense.getClanId() != clanId)
			pc.sendPackets(new S_War(8, clanName, _defense.getClanName()));
		
		Stream<L1Clan> stream = createOffensesStream();
		if(stream == null)
			return;
		
		stream.filter((L1Clan enemy) ->{
			return enemy != null && enemy.getClanId() != clanId;
		})
		.forEach((L1Clan enemy) -> {
			try{
				pc.sendPackets(new S_War(8, clanName, enemy.getClanName()));
			}catch(Exception e){}
		});
	}
	
	public void initializeOffenses(){
		try{
			_warLock.lock();
			_offenses.clear();
		}finally{
			_warLock.unlock();
		}
	}
	
	public L1Clan getOffenseClan(int id){
		L1Clan c = null;
		try{
			_warLock.lock();
			c = _offenses.get(id);
		}finally{
			_warLock.unlock();
		}
		return c;
	}
	
	public Stream<L1Clan> createOffensesStream(){
		if(_offenses.size() <= 0)
			return null;
		
		Stream<L1Clan> stream = null;
		try{
			_warLock.lock();
			stream = _offenses.values().stream();
		}finally{
			_warLock.unlock();
		}
		return stream;
	}
	
	public void putOffenseClan(L1Clan offenseClan){
		try{
			_warLock.lock();
			_offenses.put(offenseClan.getClanId(), offenseClan);
			offenseClan.setCurrentWar(this);
		}finally{
			_warLock.unlock();
		}
	}
	
	public void removeOffenseClan(int id){
		try{
			_warLock.lock();
			L1Clan clan = _offenses.remove(id);
			if(clan != null)
				clan.setCurrentWar(null);
		}finally{
			_warLock.unlock();
		}
	}
	
	public void removeOffenseClan(L1Clan c){
		removeOffenseClan(c.getClanId());
	}
	
	public L1Clan getDefenseClan(){
		return _defense == null ? L1World.getInstance().getClan(_defenseId) : _defense;
	}
	
	public void setDefenseClan(L1Clan clan){
		_defense = clan;
		_defenseId = clan.getClanId();
		clan.setCurrentWar(this);
	}

	@override
	public String toString() {
		StringBuilder sb = new StringBuilder(256); // 創建一個初始容量為256的StringBuilder對象
		sb.append("[戰爭] Id : ").append(getId()).append("\r\n"); // 添加戰爭Id
				sb.append("防守 : ").append(_defense.getClanName()); // 添加防守家族名稱
		Stream<L1Clan> stream = createOffensesStream(); // 創建進攻家族的流
		if (stream != null) {
			stream.forEach((L1Clan c) -> {
				sb.append("進攻 : ").append(c.getClanName()); // 遍歷並添加進攻家族名稱
			});
		}
		return sb.toString(); // 返回構造好的字符串
	}
