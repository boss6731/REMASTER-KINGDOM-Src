/**
 * 處理跨伺服器相關事項時，需要在庫存或物品欄中同樣添加附加欄位。
 * 即使是本地跨伺服器，也必須添加這些欄位。
 **/
package MJShiftObject.Object.Converter.Updator;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import MJShiftObject.DB.MJEShiftDBName;
import MJShiftObject.Template.CharacterBuffInfo;
import MJShiftObject.Template.CharacterConfigInfo;
import MJShiftObject.Template.CharacterPassiveInfo;
import MJShiftObject.Template.CharacterQuestInfo;
import MJShiftObject.Template.CharacterSkillInfo;
import MJShiftObject.Template.CharacterSlotInfo;
import MJShiftObject.Template.CharacterTamInfo;
import l1j.server.Config;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.BatchHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.server.Account;
import l1j.server.server.IdFactory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJShiftObjectUpdator {
	private String m_server_identity;
	private Updator m_updator;

	/**
	 * 創建一個新的 MJShiftObjectUpdator 實例。
	 *
	 * @param server_identity 伺服器身份
	 * @param updator 更新器對象
	 * @return 初始化好的 MJShiftObjectUpdator 實例
	 ***/
	public static MJShiftObjectUpdator newInstance(String server_identity, Updator updator) {
		MJShiftObjectUpdator sUpdator = newInstance();
		sUpdator.m_server_identity = server_identity;
		sUpdator.m_updator = updator;
		return sUpdator;
	}

	/**
	 * 私有的無參數構造函數，用於創建一個新的 MJShiftObjectUpdator 實例。
	 *
	 * @return 初始化好的 MJShiftObjectUpdator 實例
	 */
	private static MJShiftObjectUpdator newInstance() {
		return new MJShiftObjectUpdator();
	}

	/**
	 * 獲取伺服器身份。
	 *
	 * @return 伺服器身份
	 */
	public String get_server_identity() {
		return this.m_server_identity;
	}

	/**
	 * 刪除特定 object_id 的角色信息。
	 *
	 * @param object_id 角色的 object_id
	 */
	public void delete_character_info(final int object_id) {
		this.m_updator.execute(String.format("delete from %s where objid=?", MJEShiftDBName.CHARACTERS.get_table_name(this.m_server_identity)), new Handler() {
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id);
			}
		});
	}

	/**
	 * 刪除特定角色名稱的角色信息。
	 *
	 * @param character_name 角色名稱
	 */
	public void delete_character_info(final String character_name) {
		this.m_updator.execute(String.format("delete from %s where char_name=?", MJEShiftDBName.CHARACTERS.get_table_name(this.m_server_identity)), new Handler() {
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, character_name);
			}
		});
	}

	/**
	 * 刪除特定 object_id 的角色 Buff。
	 *
	 * @param object_id 角色的 object_id
	 */
	public void delete_character_buff(final int object_id) {
		this.m_updator.execute(String.format("delete from %s where objid=?", MJEShiftDBName.CHARACTER_BUFFS.get_table_name(this.m_server_identity)), new Handler() {
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id);
			}
		});
	}

/**
	刪除特定 object_id 的角色物品。
			*
	@param object_id 角色的 object_id
*/
	public void delete_character_items(final int object_id) {
		this.m_updator.execute(String.format("delete from %s where objid=?", MJEShiftDBName.CHARACTER_ITEMS.get_table_name(this.m_server_identity)), new Handler() {
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id);
			}
		});
	}

/**
	刪除特定帳戶名稱的帳戶信息。
			*
	@param account_name 帳戶名稱
*/
	public void delete_accounts(final String account_name) {
		this.m_updator.execute(String.format("delete from %s where account_name=?", MJEShiftDBName.ACCOUNTS.get_table_name(this.m_server_identity)), new Handler() {
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, account_name);
			}
		});
	}

	/**
	 更新角色信息。
	 *
	 @param pc        L1PcInstance 對象
	 @param object_id 角色的 object_id
	 */
	public void update_character_info(final L1PcInstance pc, final int object_id) {
		this.m_updator.execute(String.format("insert into %s set account_name=?,objid=?,char_name=?,level=?,HighLevel=?,Exp=?,MaxHp=?,CurHp=?,MaxMp=?,CurMp=?,Ac=?,Str=?,BaseStr=?,Con=?,BaseCon=?,Dex=?,BaseDex=?,Cha=?,BaseCha=?,Intel=?,BaseIntel=?,Wis=?,BaseWis=?,Status=?,Class=?,Sex=?,Type=?,Heading=?,LocX=?,LocY=?,MapID=?,Food=?,Lawful=?,Title=?,ClanID=?,Clanname=?,ClanRank=?,notes=?,BonusStatus=?,ElixirStatus=?,ElfAttr=?,GloryEarthAttr=?,PKcount=?,ExpRes=?,PartnerID=?,AccessLevel=?,OnlineStatus=?,HomeTownID=?,Contribution=?,HellTime=?,Banned=?,Karma=?,LastPk=?,DeleteTime=?,ReturnStat=?,sealScrollTime=?,sealScrollCount=?,lastLogoutTime=now(),AinState=?,SurvivalGauge=?,BirthDay=?,Mark_Count=?,Age=?,AddDamage=?,AddDamageRate=?,AddReduction=?,AddReductionRate=?,TamEndTime=?,SpecialSize=?,FishingShopBuyTime_1=?,slot_number=?,elf_attrinit_count=?, last_topBless=? on duplicate key update level=?,HighLevel=?,Exp=?,MaxHp=?,CurHp=?,MaxMp=?,CurMp=?,Ac=?,Str=?,BaseStr=?,Con=?,BaseCon=?,Dex=?,BaseDex=?,Cha=?,BaseCha=?,Intel=?,BaseIntel=?,Wis=?,BaseWis=?,Status=?,Class=?,Sex=?,Type=?,Heading=?,LocX=?,LocY=?,MapID=?,Food=?,Lawful=?,Title=?,ClanID=?,Clanname=?,ClanRank=?,notes=?,BonusStatus=?,ElixirStatus=?,ElfAttr=?,GloryEarthAttr=?,PKcount=?,ExpRes=?,PartnerID=?,AccessLevel=?,OnlineStatus=?,HomeTownID=?,Contribution=?,HellTime=?,Banned=?,Karma=?,LastPk=?,DeleteTime=?,ReturnStat=?,sealScrollTime=?,sealScrollCount=?,lastLogoutTime=now(),AinState=?,SurvivalGauge=?,BirthDay=?,Mark_Count=?,Age=?,AddDamage=?,AddDamageRate=?,AddReduction=?,AddReductionRate=?,TamEndTime=?,SpecialSize=?,FishingShopBuyTime_1=?,slot_number=?,elf_attrinit_count=?, last_topBless=?", MJEShiftDBName.CHARACTERS.get_table_name(this.m_server_identity)), new Handler() {
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				int hp = Math.max(pc.getCurrentHp(), 1);

				pstm.setString(++idx, pc.getAccountName());
				pstm.setInt(++idx, object_id);
				pstm.setString(++idx, pc.getName());
				pstm.setInt(++idx, pc.getLevel());
				pstm.setInt(++idx, pc.getHighLevel());
				pstm.setLong(++idx, pc.getExp());
				pstm.setInt(++idx, pc.getBaseMaxHp());
				pstm.setInt(++idx, hp);
				pstm.setInt(++idx, pc.getBaseMaxMp());
				pstm.setInt(++idx, pc.getCurrentMp());
				pstm.setInt(++idx, pc.getAc());
				pstm.setInt(++idx, pc.getAbility().getStr());
				pstm.setInt(++idx, pc.getAbility().getBaseStr());
				pstm.setInt(++idx, pc.getAbility().getCon());
				pstm.setInt(++idx, pc.getAbility().getBaseCon());
				pstm.setInt(++idx, pc.getAbility().getDex());
				pstm.setInt(++idx, pc.getAbility().getBaseDex());
				pstm.setInt(++idx, pc.getAbility().getCha());
				pstm.setInt(++idx, pc.getAbility().getBaseCha());
				pstm.setInt(++idx, pc.getAbility().getInt());
				pstm.setInt(++idx, pc.getAbility().getBaseInt());
				pstm.setInt(++idx, pc.getAbility().getWis());
				pstm.setInt(++idx, pc.getAbility().getBaseWis());
				pstm.setInt(++idx, pc.getCurrentWeapon());
				pstm.setInt(++idx, pc.getClassId());
				pstm.setInt(++idx, pc.getSex());
				pstm.setInt(++idx, pc.getType());
				pstm.setInt(++idx, pc.getHeading());
				pstm.setInt(++idx, pc.getX());
				pstm.setInt(++idx, pc.getY());
				pstm.setInt(++idx, pc.getMapId());
				pstm.setInt(++idx, pc.getFood());
				pstm.setInt(++idx, pc.getLawful());
				pstm.setString(++idx, (pc.getTitle() == null) ? "" : pc.getTitle());
				pstm.setInt(++idx, 0); // ClanID
				pstm.setString(++idx, ""); // ClanName
				pstm.setInt(++idx, pc.getClanRank());
				pstm.setString(++idx, ""); // Notes
				pstm.setInt(++idx, pc.totalBonusStats());
				pstm.setInt(++idx, pc.getElixirStats());
				pstm.setInt(++idx, pc.getElfAttr());
				pstm.setInt(++idx, pc.getGlory_Earth_Attr());
				pstm.setInt(++idx, pc.get_PKcount());
				pstm.setLong(++idx, pc.get_exp_res());
				pstm.setInt(++idx, pc.getPartnerId());
				pstm.setShort(++idx, pc.getAccessLevel());
				pstm.setInt(++idx, pc.getOnlineStatus());
				pstm.setInt(++idx, pc.getHomeTownId());
				pstm.setInt(++idx, pc.getContribution());
				pstm.setInt(++idx, pc.getHellTime());
				pstm.setBoolean(++idx, pc.isBanned());
				pstm.setInt(++idx, pc.getKarma());

				pstm.setTimestamp(++idx, pc.getLastPk());
				pstm.setTimestamp(++idx, pc.getDeleteTime());
				pstm.setLong(++idx, pc.getReturnStat());
				pstm.setInt(++idx, pc.getSealScrollTime());
				pstm.setInt(++idx, pc.getSealScrollCount());
				pstm.setInt(++idx, pc.getAinState());
				pstm.setLong(++idx, pc.getSurvivalGauge());

				Calendar local_c = Calendar.getInstance();
				SimpleDateFormat local_sdf = new SimpleDateFormat("yyyyMMdd");
				local_c.setTimeInMillis(System.currentTimeMillis());
				pstm.setInt(++idx, Integer.parseInt(local_sdf.format(local_c.getTime())));

				pstm.setInt(++idx, pc.getMark_count());
				pstm.setInt(++idx, pc.getAge());
				pstm.setInt(++idx, pc.getAddDamage());
				pstm.setInt(++idx, pc.getAddDamageRate());
				pstm.setInt(++idx, pc.getAddReduction());
				pstm.setInt(++idx, pc.getAddReductionRate());
				pstm.setTimestamp(++idx, pc.getTamTime());
				pstm.setInt(++idx, pc.get_SpecialSize());
				pstm.setString(++idx, (pc.getFishingShopBuyTime_1() == 0L) ? "0000-00-00 00:00:00" : (new Timestamp(pc.getFishingShopBuyTime_1())).toString());
				pstm.setInt(++idx, pc.getSlotNumber());
				pstm.setInt(++idx, pc.getElfAttrInitCount());
				pstm.setTimestamp(++idx, pc.getLastTopBless());

				// 重复更新部分
				pstm.setInt(++idx, pc.getLevel());
				pstm.setInt(++idx, pc.getHighLevel());
				pstm.setLong(++idx, pc.getExp());
				pstm.setInt(++idx, pc.getBaseMaxHp());
				pstm.setInt(++idx, hp);
				pstm.setInt(++idx, pc.getBaseMaxMp());
				pstm.setInt(++idx, pc.getCurrentMp());
				pstm.setInt(++idx, pc.getAc());
				pstm.setInt(++idx, pc.getAbility().getStr());
				pstm.setInt(++idx, pc.getAbility().getBaseStr());
				pstm.setInt(++idx, pc.getAbility().getCon());
				pstm.setInt(++idx, pc.getAbility().getBaseCon());
				pstm.setInt(++idx, pc.getAbility().getDex());
				pstm.setInt(++idx, pc.getAbility().getBaseDex());
				pstm.setInt(++idx, pc.getAbility().getCha());
				pstm.setInt(++idx, pc.getAbility().getBaseCha());
				pstm.setInt(++idx, pc.getAbility().getInt());
				pstm.setInt(++idx, pc.getAbility().getBaseInt());
				pstm.setInt(++idx, pc.getAbility().getWis());

				pstm.setInt(++idx, pc.getAbility().getBaseWis());
				pstm.setInt(++idx, pc.getCurrentWeapon());
				pstm.setInt(++idx, pc.getClassId());
				pstm.setInt(++idx, pc.getSex());
				pstm.setInt(++idx, pc.getType());
				pstm.setInt(++idx, pc.getHeading());
				pstm.setInt(++idx, pc.getX());
				pstm.setInt(++idx, pc.getY());
				pstm.setInt(++idx, pc.getMapId());
				pstm.setInt(++idx, pc.getFood());
				pstm.setInt(++idx, pc.getLawful());
				pstm.setString(++idx, (pc.getTitle() == null) ? "" : pc.getTitle());
				pstm.setInt(++idx, 0); // ClanID
				pstm.setString(++idx, ""); // ClanName
				pstm.setInt(++idx, pc.getClanRank());
				pstm.setString(++idx, ""); // Notes
				pstm.setInt(++idx, pc.totalBonusStats());
				pstm.setInt(++idx, pc.getElixirStats());
				pstm.setInt(++idx, pc.getElfAttr());
				pstm.setInt(++idx, pc.getGlory_Earth_Attr());
				pstm.setInt(++idx, pc.get_PKcount());
				pstm.setLong(++idx, pc.get_exp_res());
				pstm.setInt(++idx, pc.getPartnerId());
				pstm.setShort(++idx, pc.getAccessLevel());
				pstm.setInt(++idx, pc.getOnlineStatus());
				pstm.setInt(++idx, pc.getHomeTownId());
				pstm.setInt(++idx, pc.getContribution());
				pstm.setInt(++idx, pc.getHellTime());
				pstm.setBoolean(++idx, pc.isBanned());
				pstm.setInt(++idx, pc.getKarma());
				pstm.setTimestamp(++idx, pc.getLastPk());
				pstm.setTimestamp(++idx, pc.getDeleteTime());
				pstm.setLong(++idx, pc.getReturnStat());
				pstm.setInt(++idx, pc.getSealScrollTime());
				pstm.setInt(++idx, pc.getSealScrollCount());
				pstm.setInt(++idx, pc.getAinState());
				pstm.setLong(++idx, pc.getSurvivalGauge());
				pstm.setInt(++idx, Integer.parseInt(local_sdf.format(local_c.getTime())));
				pstm.setInt(++idx, pc.getMark_count());
				pstm.setInt(++idx, pc.getAge());
				pstm.setInt(++idx, pc.getAddDamage());
				pstm.setInt(++idx, pc.getAddDamageRate());
				pstm.setInt(++idx, pc.getAddReduction());
				pstm.setInt(++idx, pc.getAddReductionRate());
				pstm.setTimestamp(++idx, pc.getTamTime());
				pstm.setInt(++idx, pc.get_SpecialSize());

				pstm.setString(++idx, (pc.getFishingShopBuyTime_1() == 0L) ? "0000-00-00 00:00:00" : (new Timestamp(pc.getFishingShopBuyTime_1())).toString());
				pstm.setInt(++idx, pc.getSlotNumber());
				pstm.setInt(++idx, pc.getElfAttrInitCount());
				pstm.setTimestamp(++idx, pc.getLastTopBless());
			}
		});
	}


	public void update_accounts_only(final Account account) {
		this.m_updator.execute(String.format(
						"update %s set password=?, lastactive=?, access_level=?, ip=?, host=?, banned=?, charslot=?, gamepassword=?, notice=?, phone=?, Tam_Point=?, tam=?, tamStep=?, BUFF_PCROOM_Time=?, CharPassword=?, Ncoin_Point=?, Shop_open_count=?, DragonRaid=?, bless_of_ain=?, bless_of_ain_charge=?, bless_of_ain_bonus_point=?, fatigue_point=?, fatigue_start_time=?, last_log_out=?, name=? where login=?",
						new Object[]{MJEShiftDBName.ACCOUNTS.get_table_name(this.m_server_identity)}),
				new Handler() {
					public void handle(PreparedStatement pstm) throws Exception {
						int idx = 0;
						pstm.setString(++idx, account.get_Password());
						pstm.setTimestamp(++idx, account.getLastActive());
						pstm.setInt(++idx, account.getAccessLevel());
						pstm.setString(++idx, account.getIp());
						pstm.setString(++idx, account.getHost());
						pstm.setInt(++idx, account.getBannedCode());
						pstm.setInt(++idx, Config.ServerAdSetting.CharactersCharSlot);
						pstm.setInt(++idx, account.getGamePassword());
						pstm.setString(++idx, account.getNotice());
						pstm.setString(++idx, account.getphone());
						pstm.setInt(++idx, account.getTamPoint());
						pstm.setInt(++idx, account.getTam());
						pstm.setInt(++idx, account.getTamStep());
						pstm.setTimestamp(++idx, account.getBuff_PC방());
						pstm.setString(++idx, account.getCPW());
						pstm.setInt(++idx, account.Ncoin_point);
						pstm.setInt(++idx, account.Shop_open_count);
						pstm.setTimestamp(++idx, account.getDragonRaid());
						pstm.setInt(++idx, account.getBlessOfAin());
						pstm.setInt(++idx, account.getBlessOfAinCharge());
						pstm.setInt(++idx, account.getBlessOfAinBonusPoint());
						pstm.setInt(++idx, account.get_fatigue_point());
						pstm.setLong(++idx, account.get_fatigue_start_time());
						pstm.setTimestamp(++idx, account.getLastLogOut());
						pstm.setString(++idx, account.getName());
						pstm.setString(++idx, account.getLogin());
					}
				}
		);
	}

	public void update_accounts(final Account account) {
		this.m_updator.execute(String.format(
				"insert into %s set login=?, password=?, lastactive=?, access_level=?, ip=?, host=?, banned=?, charslot=?, gamepassword=?, notice=?, phone=?, Tam_Point=?, tam=?, tamStep=?, BUFF_PCROOM_Time=?, CharPassword=?, Ncoin_Point=?, Shop_open_count=?, DragonRaid=?, bless_of_ain=?, bless_of_ain_charge=?, bless_of_ain_bonus_point=?, fatigue_point=?, fatigue_start_time=?, fatigue_regeneration_time=?, last_log_out=? " +
						"on duplicate key update password=?, lastactive=?, access_level=?, ip=?, host=?, banned=?, charslot=?, gamepassword=?, notice=?, phone=?, Tam_Point=?, tam=?, tamStep=?, BUFF_PCROOM_Time=?, CharPassword=?, Ncoin_Point=?, Shop_open_count=?, DragonRaid=?, bless_of_ain=?, bless_of_ain_charge=?, bless_of_ain_bonus_point=?, fatigue_point=?, fatigue_start_time=?, fatigue_regeneration_time=?, last_log_out=?",
				new Object[]{MJEShiftDBName.ACCOUNTS.get_table_name(this.m_server_identity)}), new Handler() {
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;

				// Insert part
				pstm.setString(++idx, account.getLogin());
				pstm.setString(++idx, account.get_Password());
				pstm.setTimestamp(++idx, account.getLastActive());
				pstm.setInt(++idx, account.getAccessLevel());
				pstm.setString(++idx, account.getIp());
				pstm.setString(++idx, account.getHost());
				pstm.setInt(++idx, account.getBannedCode());
				pstm.setInt(++idx, Config.ServerAdSetting.CharactersCharSlot);
				pstm.setInt(++idx, account.getGamePassword());
				pstm.setString(++idx, account.getNotice());
				pstm.setString(++idx, account.getphone());
				pstm.setInt(++idx, account.getTamPoint());
				pstm.setInt(++idx, account.getTam());
				pstm.setInt(++idx, account.getTamStep());
				pstm.setTimestamp(++idx, account.getBuff_PC방());
				pstm.setString(++idx, account.getCPW());
				pstm.setInt(++idx, account.Ncoin_point);
				pstm.setInt(++idx, account.Shop_open_count);
				pstm.setTimestamp(++idx, account.getDragonRaid());
				pstm.setInt(++idx, account.getBlessOfAin());
				pstm.setInt(++idx, account.getBlessOfAinCharge());
				pstm.setInt(++idx, account.getBlessOfAinBonusPoint());
				pstm.setInt(++idx, account.get_fatigue_point());
				pstm.setLong(++idx, account.get_fatigue_start_time());
				pstm.setLong(++idx, 0L); // fatigue_regeneration_time;
				pstm.setTimestamp(++idx, account.getLastLogOut());

				// Update part (duplicate key)
				pstm.setString(++idx, account.get_Password());
				pstm.setTimestamp(++idx, account.getLastActive());
				pstm.setInt(++idx, account.getAccessLevel());
				pstm.setString(++idx, account.getIp());
				pstm.setString(++idx, account.getHost());
				pstm.setInt(++idx, account.getBannedCode());
				pstm.setInt(++idx, Config.ServerAdSetting.CharactersCharSlot);
				pstm.setInt(++idx, account.getGamePassword());
				pstm.setString(++idx, account.getNotice());
				pstm.setString(++idx, account.getphone());
				pstm.setInt(++idx, account.getTamPoint());
				pstm.setInt(++idx, account.getTam());
				pstm.setInt(++idx, account.getTamStep());
				pstm.setTimestamp(++idx, account.getBuff_PC방());
				pstm.setString(++idx, account.getCPW());
				pstm.setInt(++idx, account.Ncoin_point);
				pstm.setInt(++idx, account.Shop_open_count);
				pstm.setTimestamp(++idx, account.getDragonRaid());
				pstm.setInt(++idx, account.getBlessOfAin());
				pstm.setInt(++idx, account.getBlessOfAinCharge());
				pstm.setInt(++idx, account.getBlessOfAinBonusPoint());
				pstm.setInt(++idx, account.get_fatigue_point());
				pstm.setLong(++idx, account.get_fatigue_start_time());
				pstm.setLong(++idx, 0L); // fatigue_regeneration_time;
				pstm.setTimestamp(++idx, account.getLastLogOut());
			}
		});
	}


	public void delete_accounts(final String account_name) {
		// 執行刪除操作，根據帳戶名稱刪除對應的帳戶記錄。
		this.m_updator.execute(String.format("delete from %s where login=?", new Object[]{MJEShiftDBName.ACCOUNTS.get_table_name(this.m_server_identity)}), new Handler() {
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, account_name); // 設置刪除條件的帳戶名稱
			}
		});
	}

	public void update_character_buff(final List<CharacterBuffInfo> buffs, final int object_id) {
		// 如果 buffs 列表為 null 或者大小小於等於 0，則直接返回。
		if (buffs == null || buffs.size() <= 0) {
			return;
		}
		// 執行批量插入或更新操作，根據角色 ID 和技能 ID 更新角色的 buff 信息。
		this.m_updator.execBatch(String.format("insert into %s SET char_obj_id=?, skill_id=?, remaining_time=?, poly_id=? on duplicate key update remaining_time=?, poly_id=?", new Object[]{MJEShiftDBName.CHARACTERS_BUFF.get_table_name(this.m_server_identity)}),
				new BatchHandler() {
					public void handle(PreparedStatement pstm, int callNumber) throws Exception {
						int idx = 0;
						CharacterBuffInfo bInfo = buffs.get(callNumber); // 從列表中獲取當前處理的 buff 信息
						pstm.setInt(++idx, object_id); // 設置角色對象 ID
						pstm.setInt(++idx, bInfo.skill_id); // 設置技能 ID
						pstm.setInt(++idx, bInfo.time_sec); // 設置剩餘時間
						pstm.setInt(++idx, bInfo.poly_id); // 設置 poly_id
						pstm.setInt(++idx, bInfo.time_sec); // 更新剩餘時間
						pstm.setInt(++idx, bInfo.poly_id); // 更新 poly_id
					}
				}, buffs.size()); // 傳遞批次大小
	}

	public void delete_character_buff(final int object_id) {
		// 執行刪除操作，根據角色對象 ID 刪除對應的 buff 記錄。
		this.m_updator.execute(String.format("delete from %s where char_obj_id=?", new Object[]{MJEShiftDBName.CHARACTERS_BUFF.get_table_name(this.m_server_identity)}), new Handler() {
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id); // 設置刪除條件的角色對象 ID
			}
		});
	}


	public void update_character_config(final CharacterConfigInfo cInfo, final int object_id) {
		// 如果 cInfo 為 null，則直接返回。
		if (cInfo == null) {
			return;
		}
		// 執行插入或更新操作，根據角色對象 ID 更新角色的配置信息。
		this.m_updator.execute(String.format("insert into %s SET object_id=?, length=?, data=? on duplicate key update length=?, data=?", new Object[]{MJEShiftDBName.CHARACTERS_CONFIG.get_table_name(this.m_server_identity)}), new Handler() {
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, object_id); // 設置角色對象 ID
				pstm.setInt(++idx, cInfo.length); // 設置配置信息的長度
				pstm.setBytes(++idx, cInfo.buff); // 設置配置信息的數據
				pstm.setInt(++idx, cInfo.length); // 更新配置信息的長度
				pstm.setBytes(++idx, cInfo.buff); // 更新配置信息的數據
			}
		});
	}

	public void delete_character_config(final int object_id) {
		// 執行刪除操作，根據角色對象 ID 刪除對應的配置記錄。
		this.m_updator.execute(String.format("delete from %s where object_id=?", new Object[]{MJEShiftDBName.CHARACTERS_CONFIG.get_table_name(this.m_server_identity)}), new Handler() {
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id); // 設置刪除條件的角色對象 ID
			}
		});
	}

	public void update_character_items(final List<L1ItemInstance> items, final int object_id) {
		// 如果 items 列表為 null 或者大小小於等於 0，則直接返回。
		if (items == null || items.size() <= 0) {
			return;
		}
			// 執行批量插入或更新操作，根據角色 ID 更新角色的物品信息。
		this.m_updator.execBatch(String.format(
				"insert into %s SET id=?, item_id=?, char_id=?, item_name=?, count=?, is_equipped=?, enchantlvl=?, is_id=?, durability=?, charge_count=?, remaining_time=?, last_used=?, bless=?, attr_enchantlvl=?, end_time=?, buy_time=?, package=?, bless_level=?, item_level=?, Hotel_Town=?, is_support_item=?, Carving=? " +
						"on duplicate key update item_id=?, char_id=?, item_name=?, count=?, is_equipped=?, enchantlvl=?, is_id=?, durability=?, charge_count=?, remaining_time=?, last_used=?, bless=?, attr_enchantlvl=?, end_time=?, buy_time=?, package=?, bless_level=?, item_level=?, Hotel_Town=?, is_support_item=?, Carving=?",
				new Object[]{MJEShiftDBName.CHARACTERS_ITEMS.get_table_name(this.m_server_identity)}), new BatchHandler() {
			public void handle(PreparedStatement pstm, int callNumber) throws Exception {
				int idx = 0;
				L1ItemInstance item = items.get(callNumber); // 從列表中獲取當前處理的物品信息
				pstm.setInt(++idx, IdFactory.getInstance().nextId()); // 設置新生成的物品唯一 ID
				pstm.setInt(++idx, item.getItem().getItemId()); // 設置物品類型 ID
				pstm.setInt(++idx, object_id); // 設置角色 ID
				pstm.setString(++idx, item.getItem().getName()); // 設置物品名稱
				pstm.setInt(++idx, item.getCount()); // 設置物品數量
				pstm.setInt(++idx, item.isEquipped() ? 1 : 0); // 設置是否裝備
				pstm.setInt(++idx, item.getEnchantLevel()); // 設置強化等級
				pstm.setInt(++idx, item.isIdentified() ? 1 : 0); // 設置是否鑑定
				pstm.setInt(++idx, item.get_durability()); // 設置耐久度
				pstm.setInt(++idx, item.getChargeCount()); // 設置充能次數
				pstm.setInt(++idx, item.getRemainingTime()); // 設置剩餘時間
				pstm.setTimestamp(++idx, item.getLastUsed()); // 設置最後使用時間
				pstm.setInt(++idx, item.getBless()); // 設置祝福等級
				pstm.setInt(++idx, item.getAttrEnchantLevel()); // 設置屬性強化等級
				pstm.setTimestamp(++idx, item.getEndTime()); // 設置結束時間
				pstm.setTimestamp(++idx, item.getBuyTime()); // 設置購買時間
				pstm.setInt(++idx, !item.isPackage() ? 0 : 1); // 設置是否為包裹
				pstm.setInt(++idx, item.get_bless_level()); // 設置祝福等級
				pstm.setInt(++idx, item.get_item_level()); // 設置物品等級
				pstm.setString(++idx, item.getHotel_Town()); // 設置酒店名稱
				pstm.setInt(++idx, item.isSupportItem() ? 1 : 0); // 設置是否為支援物品
				pstm.setInt(++idx, item.get_Carving()); // 設置雕刻值

				// 更新部分
				pstm.setInt(++idx, item.getItem().getItemId());
				pstm.setInt(++idx, object_id);
				pstm.setString(++idx, item.getItem().getName());
				pstm.setInt(++idx, item.getCount());
				pstm.setInt(++idx, item.isEquipped() ? 1 : 0);
				pstm.setInt(++idx, item.getEnchantLevel());
				pstm.setInt(++idx, item.isIdentified() ? 1 : 0);
				pstm.setInt(++idx, item.get_durability());
				pstm.setInt(++idx, item.getChargeCount());
				pstm.setInt(++idx, item.getRemainingTime());
				pstm.setTimestamp(++idx, item.getLastUsed());
				pstm.setInt(++idx, item.getBless());
				pstm.setInt(++idx, item.getAttrEnchantLevel());
				pstm.setTimestamp(++idx, item.getEndTime());
				pstm.setTimestamp(++idx, item.getBuyTime());
				pstm.setInt(++idx, !item.isPackage() ? 0 : 1);
				pstm.setInt(++idx, item.get_bless_level());
				pstm.setInt(++idx, item.get_item_level());
				pstm.setString(++idx, item.getHotel_Town());
				pstm.setInt(++idx, item.isSupportItem() ? 1 : 0);
				pstm.setInt(++idx, item.get_Carving());
			}
		}, items.size()); // 傳遞批次大小
	}

	public void update_character_skills(final List<CharacterSkillInfo> skills, final int object_id) {
		// 如果 skills 列表為 null 或其大小小於等於 0，則直接返回。
		if (skills == null || skills.size() <= 0) {
			return;
		}
		// 執行批量插入或更新操作，根據角色 ID 更新角色的技能信息。
		this.m_updator.execBatch(String.format(
				"insert into %s set char_obj_id=?, skill_id=?, skill_name=?, is_active=?, activetimeleft=? " +
						"on duplicate key update skill_name=?, is_active=?, activetimeleft=?",
				new Object[]{MJEShiftDBName.CHARACTERS_SKILLS.get_table_name(this.m_server_identity)}), new BatchHandler() {
			public void handle(PreparedStatement pstm, int callNumber) throws Exception {
				CharacterSkillInfo csInfo = skills.get(callNumber); // 從列表中獲取當前處理的技能信息
				int idx = 0;
				pstm.setInt(++idx, object_id); // 設置角色 ID
				pstm.setInt(++idx, csInfo.skill_id); // 設置技能 ID
				pstm.setString(++idx, csInfo.skill_name); // 設置技能名稱
				pstm.setInt(++idx, csInfo.is_active ? 1 : 0); // 設置技能是否激活
				pstm.setInt(++idx, csInfo.active_time_left); // 設置技能剩餘激活時間

				// 更新部分
				pstm.setString(++idx, csInfo.skill_name); // 更新技能名稱
				pstm.setInt(++idx, csInfo.is_active ? 1 : 0); // 更新技能是否激活
				pstm.setInt(++idx, csInfo.active_time_left); // 更新技能剩餘激活時間
			}
		}, skills.size()); // 傳遞批次大小
	}

	public void delete_character_skills(final int object_id) {
		// 執行刪除操作，根據角色 ID 刪除對應的技能記錄。
		this.m_updator.execute(String.format("delete from %s where char_obj_id=?", new Object[]{MJEShiftDBName.CHARACTERS_SKILLS.get_table_name(this.m_server_identity)}), new Handler() {
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id); // 設置刪除條件的角色 ID
			}
		});
	}

	public void update_character_passive(final List<CharacterPassiveInfo> passives, final int object_id) {
		// 如果 passives 列表為 null 或其大小小於等於 0，則直接返回。
		if (passives == null || passives.size() <= 0) {
			return;
		}
		// 執行批量插入或更新操作，根據角色 ID 更新角色的被動技能信息。
		this.m_updator.execBatch(String.format(
				"insert into %s set character_id=?, passive_id=?, passive_name=? " +
						"on duplicate key update passive_name=?",
				new Object[]{MJEShiftDBName.PASSIVE_USER_INFO.get_table_name(this.m_server_identity)}), new BatchHandler() {
			public void handle(PreparedStatement pstm, int callNumber) throws Exception {
				int idx = 0;
				CharacterPassiveInfo pInfo = passives.get(callNumber); // 從列表中獲取當前處理的被動技能信息
				pstm.setInt(++idx, object_id); // 設置角色 ID
				pstm.setInt(++idx, pInfo.passive_id); // 設置被動技能 ID
				pstm.setString(++idx, pInfo.passive_name); // 設置被動技能名稱
				// 更新部分
				pstm.setString(++idx, pInfo.passive_name); // 更新被動技能名稱
			}
		}, passives.size()); // 傳遞批次大小
	}

	public void delete_character_passive(final int object_id) {
		// 執行刪除操作，根據角色 ID 刪除對應的被動技能記錄。
		this.m_updator.execute(String.format("delete from %s where character_id=?", new Object[]{MJEShiftDBName.PASSIVE_USER_INFO.get_table_name(this.m_server_identity)}), new Handler() {
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id); // 設置刪除條件的角色 ID
			}
		});
	}

	public void update_character_slot_items(final List<CharacterSlotInfo> slots, final int object_id) {
		// 如果 slots 列表為 null 或其大小小於等於 0，則直接返回。
		if (slots == null || slots.size() <= 0) {
			return;
		}
			// 執行批量插入操作，根據角色 ID 更新角色的插槽物品信息。
		this.m_updator.execBatch(String.format(
				"insert ignore into %s set char_id=?, item_objid=?, slot_number=?",
				new Object[]{MJEShiftDBName.CHARACTERS_SLOT_ITEMS.get_table_name(this.m_server_identity)}), new BatchHandler() {
			public void handle(PreparedStatement pstm, int callNumber) throws Exception {
				int idx = 0;
				CharacterSlotInfo csInfo = slots.get(callNumber); // 從列表中獲取當前處理的插槽物品信息
				pstm.setInt(++idx, object_id); // 設置角色 ID
				pstm.setInt(++idx, csInfo.source_item_id); // 設置物品 ID
				pstm.setInt(++idx, csInfo.slot_number); // 設置插槽編號
			}
		}, slots.size()); // 傳遞批次大小
	}

	public void delete_character_slot_items(final int object_id) {
		// 執行刪除操作，根據角色 ID 刪除對應的插槽物品記錄。
		this.m_updator.execute(String.format("delete from %s where char_id=?", new Object[]{MJEShiftDBName.CHARACTERS_SLOT_ITEMS.get_table_name(this.m_server_identity)}), new Handler() {
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id); // 設置刪除條件的角色 ID
			}
		});
	}

	public void update_character_tams(final List<CharacterTamInfo> tams, final int object_id) {
		// 如果 tams 列表為 null 或其大小小於等於 0，則直接返回。
		if (tams == null || tams.size() <= 0) {
			return;
		}
			// 執行批量插入或更新操作，根據角色 ID 更新角色的 TAM 信息。
		this.m_updator.execBatch(String.format(
				"insert into %s set account_id=?, char_id=?, char_name=?, skill_id=?, expiration_time=?, reserve=? " +
						"on duplicate key update account_id=?, char_name=?, expiration_time=?, reserve=?",
				new Object[]{MJEShiftDBName.CHARACTERS_TAMS.get_table_name(this.m_server_identity)}), new BatchHandler() {
			public void handle(PreparedStatement pstm, int callNumber) throws Exception {
				CharacterTamInfo tInfo = tams.get(callNumber); // 從列表中獲取當前處理的 TAM 信息
				int idx = 0;
				pstm.setInt(++idx, tInfo.account_id); // 設置賬戶 ID
				pstm.setInt(++idx, object_id); // 設置角色 ID
				pstm.setString(++idx, tInfo.character_name); // 設置角色名稱
				pstm.setInt(++idx, tInfo.skill_id); // 設置技能 ID
				pstm.setTimestamp(++idx, tInfo.expiration_time); // 設置過期時間
				pstm.setInt(++idx, tInfo.reserve); // 設置預留字段

				// 更新部分
				pstm.setInt(++idx, tInfo.account_id); // 更新賬戶 ID
				pstm.setString(++idx, tInfo.character_name); // 更新角色名稱
				pstm.setTimestamp(++idx, tInfo.expiration_time); // 更新過期時間
				pstm.setInt(++idx, tInfo.reserve); // 更新預留字段
			}
		}, tams.size()); // 傳遞批次大小
	}

	public void delete_character_tams(final int object_id) {
		// 執行刪除操作，根據角色 ID 刪除對應的 TAM 記錄。
		this.m_updator.execute(String.format("delete from %s where char_id=?", new Object[]{MJEShiftDBName.CHARACTERS_TAMS.get_table_name(this.m_server_identity)}), new Handler() {
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id); // 設置刪除條件的角色 ID
			}
		});
	}

	public void update_character_level_bonus(final int character_level_bonus, final int object_id) {
			// 執行插入或更新操作，根據角色 ID 更新角色的級別獎勵信息。
		this.m_updator.execute(String.format("insert into %s set objid=?, level=? on duplicate key update level=?", new Object[]{MJEShiftDBName.CHARACTERS_BONUS.get_table_name(this.m_server_identity)}), new Handler() {
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id); // 設置角色 ID
				pstm.setInt(2, character_level_bonus); // 設置角色等級獎勵
				pstm.setInt(3, character_level_bonus); // 更新角色等級獎勵
			}
		});
	}

	public void delete_character_level_bonus(final int object_id) {
		// 執行刪除操作，根據角色 ID 刪除對應的級別獎勵記錄。
		this.m_updator.execute(String.format("delete from %s where objid=?", new Object[]{MJEShiftDBName.CHARACTERS_BONUS.get_table_name(this.m_server_identity)}), new Handler() {
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id); // 設置刪除條件的角色 ID
			}
		});
	}

	public void update_character_quest_info(final List<CharacterQuestInfo> quests, final int object_id) {
		// 如果 quests 列表為 null 或其大小小於等於 0，則直接返回。
		if (quests == null || quests.size() <= 0) {
			return;
		}
		// 執行批量插入或更新操作，根據角色 ID 更新角色的任務信息。
		this.m_updator.execBatch(String.format(
				"insert into %s set char_id=?, quest_id=?, quest_step=? " +
						"on duplicate key update quest_id=?, quest_step=?",
				new Object[]{MJEShiftDBName.CHARACTERS_QUEST.get_table_name(this.m_server_identity)}), new BatchHandler() {
			public void handle(PreparedStatement pstm, int callNumber) throws Exception {
				CharacterQuestInfo qInfo = quests.get(callNumber); // 從列表中獲取當前處理的任務信息
				int idx = 0;
				pstm.setInt(++idx, object_id); // 設置角色 ID
				pstm.setInt(++idx, qInfo.quest_id); // 設置任務 ID
				pstm.setInt(++idx, qInfo.quest_step); // 設置任務步驟
				// 更新部分
				pstm.setInt(++idx, qInfo.quest_id); // 更新任務 ID
				pstm.setInt(++idx, qInfo.quest_step); // 更新任務步驟
			}
		}, quests.size()); // 傳遞批次大小
	}

	public void delete_character_quest_info(final int object_id) {
			// 執行刪除操作，根據角色 ID 刪除對應的任務信息記錄。
		this.m_updator.execute(String.format("delete from %s where char_id=?", new Object[]{MJEShiftDBName.CHARACTERS_QUEST.get_table_name(this.m_server_identity)}), new Handler() {
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id); // 設置刪除條件的角色 ID
			}
		});
	}



