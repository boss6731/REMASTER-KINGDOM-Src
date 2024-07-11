package l1j.server.MJWebServer.Dispatcher.my.service.character;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import l1j.server.Config;
import l1j.server.MJRankSystem.Loader.MJRankUserLoader;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJClassesType.MJEClassesType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TOP_RANKER_NOTI;
import l1j.server.MJTemplate.MJSqlHelper.Clause.MJClauseBuilder;
import l1j.server.MJTemplate.MJSqlHelper.Clause.MJClauseResult;
import l1j.server.server.model.Ability;
import l1j.server.server.model.Instance.L1PcInstance;

abstract class MJMyCharDatabaseProvider<T> {
	private static final MJMyCharDatabaseProvider<MJMyCharSimpleInfo> simple = new SimpleProvider();
	private static final MJMyCharDatabaseProvider<MJMyCharDetailInfo> detail = new DetailProvider();
	
	static MJMyCharDatabaseProvider<MJMyCharSimpleInfo> simple(){
		return simple;
	}
	
	static MJMyCharDatabaseProvider<MJMyCharDetailInfo> detail(){
		return detail;
	}
	
	protected MJMyCharDatabaseProvider(){	
	}
	
	abstract T fromMemory(L1PcInstance pc);
	abstract List<T> fromDatabase(MJClauseBuilder<List<T>> builder, boolean whereAnd);
	
	private static class SimpleProvider extends MJMyCharDatabaseProvider<MJMyCharSimpleInfo>{
		private static final List<String> simpleColumns = Arrays.asList("objid", "char_name", "level", "Class", "Sex", "AccessLevel");
		private static final MJClauseResult<List<MJMyCharSimpleInfo>> result = new MJClauseResult<List<MJMyCharSimpleInfo>>(){
			@Override
			public List<MJMyCharSimpleInfo> onResult(ResultSet rs) throws Exception {
				LinkedList<MJMyCharSimpleInfo> characters = new LinkedList<>();
				while(rs.next()){
					MJMyCharSimpleInfo cInfo = new MJMyCharSimpleInfo();
					cInfo.objectId(rs.getInt("objid"));
					cInfo.nick(rs.getString("char_name"));
					cInfo.level(rs.getInt("level"));
					cInfo.characterClass(MJEClassesType.fromGfx(rs.getInt("Class")).toInt());
					cInfo.gender(rs.getInt("Sex") == 0 ? "m" : "f");
					cInfo.gm(rs.getInt("AccessLevel") == Config.ServerAdSetting.GMCODE);
					characters.add(cInfo);
				}
				return characters;
			}
		};
		@Override
		MJMyCharSimpleInfo fromMemory(L1PcInstance pc){	
			MJMyCharSimpleInfo cInfo = new MJMyCharSimpleInfo();
			cInfo.objectId(pc.getId());
			cInfo.nick(pc.getName());
			cInfo.level(pc.getLevel());
			cInfo.characterClass(MJEClassesType.fromGfx(pc.getClassId()).toInt());
			cInfo.gender(pc.get_sex() == 0 ? "m" : "f");
			cInfo.gm(pc.isGm());
			return cInfo;
		}
		
		@Override
		List<MJMyCharSimpleInfo> fromDatabase(MJClauseBuilder<List<MJMyCharSimpleInfo>> builder, boolean whereAnd){
			return builder
				.table("characters")
				.columns(simpleColumns)
				.result(result)
				.build(whereAnd);
		}
	}
	
	private static class DetailProvider extends MJMyCharDatabaseProvider<MJMyCharDetailInfo>{
		private static final MJClauseResult<List<MJMyCharDetailInfo>> result = new MJClauseResult<List<MJMyCharDetailInfo>>(){
			@Override
			public List<MJMyCharDetailInfo> onResult(ResultSet rs) throws Exception {
				LinkedList<MJMyCharDetailInfo> characters = new LinkedList<>();
				while(rs.next()){
					MJMyCharDetailInfo cInfo = new MJMyCharDetailInfo();
					cInfo.objectId = rs.getInt("objid");
					cInfo.nick = rs.getString("char_name");
					cInfo.level = rs.getInt("level");
					cInfo.exp = rs.getLong("Exp");
					cInfo.characterClass = MJEClassesType.fromGfx(rs.getInt("Class")).toInt();
					cInfo.gender = rs.getInt("Sex") == 0 ? "m" : "f";
					cInfo.gm = rs.getInt("AccessLevel") == Config.ServerAdSetting.GMCODE;
					cInfo.pledge = rs.getString("Clanname");
					cInfo.birth = String.valueOf(rs.getInt("BirthDay"));
					cInfo.login = rs.getTimestamp("lastLoginTime").getTime();
					cInfo.totalRank = 0;
					cInfo.classRank = 0;
					SC_TOP_RANKER_NOTI noti = MJRankUserLoader.getInstance().get(cInfo.objectId);
					if(noti != null){
						cInfo.totalRank = noti.get_total_ranker().get_rank();
						cInfo.classRank = noti.get_class_ranker().get_rank();
					}
					cInfo.maxHp = rs.getInt("MaxHp");
					cInfo.curHp = rs.getInt("CurHp");
					cInfo.maxMp = rs.getInt("MaxMp");
					cInfo.curMp = rs.getInt("CurMp");
					
					cInfo.str = rs.getInt("Str");
					cInfo.intel = rs.getInt("Intel");
					cInfo.dex = rs.getInt("Dex");
					cInfo.wis = rs.getInt("Wis");
					cInfo.con = rs.getInt("Con");
					cInfo.cha = rs.getInt("Cha");
					cInfo.pk = rs.getInt("PKcount");
					cInfo.lawful = rs.getInt("Lawful");
					characters.add(cInfo);
				}
				return characters;
			}	
		};
		
		@Override
		MJMyCharDetailInfo fromMemory(L1PcInstance pc) {
			MJMyCharDetailInfo cInfo = new MJMyCharDetailInfo();
			cInfo.objectId = pc.getId();
			cInfo.nick = pc.getName();
			cInfo.level = pc.getLevel();
			cInfo.exp = pc.get_exp();
			cInfo.characterClass = MJEClassesType.fromGfx(pc.getClassId()).toInt();
			cInfo.gender = pc.get_sex() == 0 ? "m" : "f";
			cInfo.gm = pc.isGm();
			cInfo.pledge = MJString.isNullOrEmpty(pc.getClanname()) ? MJString.EmptyString : pc.getClanname();
			cInfo.birth = String.valueOf(pc.getBirthDay());
			cInfo.login = pc.getLastLoginTime().getTime();
			
			cInfo.totalRank = 0;
			cInfo.classRank = 0;
			SC_TOP_RANKER_NOTI noti = MJRankUserLoader.getInstance().get(pc.getId());
			if(noti != null){
				cInfo.totalRank = noti.get_total_ranker().get_rank();
				cInfo.classRank = noti.get_class_ranker().get_rank();
			}
			cInfo.maxHp = pc.getMaxHp();
			cInfo.curHp = pc.getCurrentHp();
			cInfo.maxMp = pc.getMaxMp();
			cInfo.curMp = pc.getCurrentMp();
			
			final Ability ability = pc.getAbility(); 
			cInfo.str = ability.getStr();
			cInfo.intel = ability.getInt();
			cInfo.dex = ability.getDex();
			cInfo.wis = ability.getWis();
			cInfo.con = ability.getCon();
			cInfo.cha = ability.getCha();
			cInfo.pk = pc.get_PKcount();
			cInfo.lawful = pc.getLawful();
			return cInfo;
		}

		@Override
		List<MJMyCharDetailInfo> fromDatabase(MJClauseBuilder<List<MJMyCharDetailInfo>> builder, boolean whereAnd) {
			return builder
				.table("characters")
				.result(result)
				.build(whereAnd);
		}
	}
	
}
