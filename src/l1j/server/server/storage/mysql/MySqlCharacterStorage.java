         package l1j.server.server.storage.mysql;

         import java.sql.Connection;
         import java.sql.PreparedStatement;
         import java.sql.ResultSet;
         import java.sql.SQLException;
         import java.sql.Timestamp;
         import java.util.logging.Level;
         import java.util.logging.Logger;
         import l1j.server.Config;
         import l1j.server.L1DatabaseFactory;
         import l1j.server.MJTemplate.MJObjectWrapper;
         import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
         import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.storage.CharacterStorage;
         import l1j.server.server.utils.SQLUtil;

         public class MySqlCharacterStorage
           implements CharacterStorage
         {
           private static Logger _log = Logger.getLogger(MySqlCharacterStorage.class.getName());


           public L1PcInstance loadCharacter(String charName) {
             L1PcInstance pc = null;
             Connection con = null;
             PreparedStatement pstm = null;

             ResultSet rs = null;
             try {
               con = L1DatabaseFactory.getInstance().getConnection();
               pstm = con.prepareStatement("SELECT * FROM characters WHERE char_name=? limit 1");
               pstm.setString(1, charName);

               rs = pstm.executeQuery();

               if (!rs.next()) {
                 return null;
               }

               pc = new L1PcInstance();
               pc.setAccessLevel(rs.getShort("AccessLevel"));
               if (pc.getAccessLevel() == Config.ServerAdSetting.GMCODE) {
                 pc.setGm(true);
                 pc.setMonitor(false);
               } else if (pc.getAccessLevel() == 100) {
                 pc.setGm(false);
                 pc.setMonitor(true);
               } else {
                 pc.setGm(false);
                 pc.setMonitor(false);
               }
               pc.setAccountName(rs.getString("account_name"));
               pc.setId(rs.getInt("objid"));
               pc.setName(rs.getString("char_name"));
               pc.setHighLevel(rs.getInt("HighLevel"));
               pc.set_exp(rs.getLong("Exp"));
               pc.addBaseMaxHp(rs.getShort("MaxHp"));
               short currentHp = rs.getShort("CurHp");
               if (currentHp < 1) {
                 currentHp = 1;
               }
               pc.setCurrentHpDB(currentHp);

               pc.setCurrentHp(currentHp);

               pc.setDead(false);
               pc.setStatus(0);
               pc.addBaseMaxMp(rs.getShort("MaxMp"));
               pc.setCurrentMp(rs.getShort("CurMp"));
               pc.setCurrentMpDB(rs.getShort("CurMp"));
               pc.getAbility().setBaseStr(rs.getByte("BaseStr"));
               pc.getAbility().setStr(rs.getByte("Str"));
               pc.getAbility().setBaseCon(rs.getByte("BaseCon"));
               pc.getAbility().setCon(rs.getByte("Con"));
               pc.getAbility().setBaseDex(rs.getByte("BaseDex"));
               pc.getAbility().setDex(rs.getByte("Dex"));
               pc.getAbility().setBaseCha(rs.getByte("BaseCha"));
               pc.getAbility().setCha(rs.getByte("Cha"));
               pc.getAbility().setBaseInt(rs.getByte("BaseIntel"));
               pc.getAbility().setInt(rs.getByte("Intel"));
               pc.getAbility().setBaseWis(rs.getByte("BaseWis"));
               pc.getAbility().setWis(rs.getByte("Wis"));

               int status = rs.getInt("Status");
               pc.setCurrentWeapon(status);
               int classId = rs.getInt("Class");
               pc.setClassId(classId);
               pc.setCurrentSprite(classId);
               pc.set_sex(rs.getInt("Sex"));
               pc.setType(rs.getInt("Type"));
               int head = rs.getInt("Heading");
               if (head > 7) {
                 head = 0;
               }
               pc.setHeading(head);
               pc.setX(rs.getInt("locX"));
               pc.setY(rs.getInt("locY"));
               pc.setMap(rs.getShort("MapID"));
               pc.set_food(rs.getInt("Food"));
               pc.setLawful(rs.getInt("Lawful"));
               pc.setTitle(rs.getString("Title"));
               pc.setClanid(rs.getInt("ClanID"));
               pc.setClanname(rs.getString("Clanname"));
               pc.setClanRank(rs.getInt("ClanRank"));
               pc.setClanMemberNotes(rs.getString("notes"));
               pc.setElixirStats(rs.getInt("ElixirStatus"));
               pc.setElfAttr(rs.getInt("ElfAttr"));
               pc.setGlory_Earth_Attr(rs.getInt("GloryEarthAttr"));
               pc.set_PKcount(rs.getInt("PKcount"));
               pc.set_exp_res(rs.getLong("ExpRes"));
               pc.setPartnerId(rs.getInt("PartnerID"));
               pc.setOnlineStatus(rs.getInt("OnlineStatus"));
               pc.setHomeTownId(rs.getInt("HomeTownID"));
               pc.setContribution(rs.getInt("Contribution"));
               pc.setHellTime(rs.getInt("HellTime"));
               pc.setBanned(rs.getBoolean("Banned"));
               pc.setKarma(rs.getInt("Karma"));
               pc.setLastPk(rs.getTimestamp("LastPk"));
               pc.setDeleteTime(rs.getTimestamp("DeleteTime"));
               pc.setReturnStat(rs.getInt("ReturnStat"));
               pc.setSealingPW(rs.getString("sealingPW"));
               pc.setSealScrollTime(rs.getInt("sealScrollTime"));
               pc.setSealScrollCount(rs.getInt("sealScrollCount"));
               pc.setLastLoginTime(rs.getTimestamp("lastLoginTime"));
               pc.setAinState(rs.getInt("AinState"));
               pc.setSurvivalGauge(rs.getLong("SurvivalGauge"));
               pc.setMark_count(rs.getInt("Mark_Count"));
               pc.setAge(rs.getInt("Age"));


               pc.setAddDamage(rs.getInt("AddDamage"));
               pc.setAddDamageRate(rs.getInt("AddDamageRate"));
               pc.setAddReduction(rs.getInt("AddReduction"));
               pc.setAddReductionRate(rs.getInt("AddReductionRate"));



               pc.setTamTime(rs.getTimestamp("TamEndTime"));
               pc.set_SpecialSize(rs.getInt("SpecialSize"));
               pc.setSlotNumber(rs.getInt("slot_number"));
               pc.setElfAttrInitCount(rs.getInt("elf_attrinit_count"));
               pc.setLastTopBless(rs.getTimestamp("last_topBless"));
               pc.setBirthDay(rs.getInt("BirthDay"));
               pc.setLastLogoutTime(rs.getTimestamp("lastLogoutTime"));
               try {
                 pc.setFishingShopBuyTime_1(rs.getTimestamp("FishingShopBuyTime_1").getTime());
               } catch (Exception e) {
                 pc.setFishingShopBuyTime_1(0L);
               }
               pc.setClanContribution(rs.getInt("clan_contribution"));
               pc.setClassRankBlessTime(rs.getTimestamp("class_rank_bless_time"));
               _log.finest("restored char data: ");
             } catch (Exception e) {
               e.printStackTrace();
               pc = null;
             } finally {
               SQLUtil.close(rs);
               SQLUtil.close(pstm);
               SQLUtil.close(con);
             }
             pc.refresh();
             pc.setMoveSpeed(0);
             pc.setBraveSpeed(0);
             pc.setGmInvis(false);
             return pc;
           }


           public void createCharacter(L1PcInstance pc) {
             Connection con = null;
             PreparedStatement pstm = null;
             try {
               int i = 0;
               con = L1DatabaseFactory.getInstance().getConnection();
               pstm = con.prepareStatement("INSERT INTO characters SET AccessLevel=?,account_name=?,objid=?,char_name=?,level=?,HighLevel=?,Exp=?,MaxHp=?,CurHp=?,MaxMp=?,CurMp=?,Ac=?,Str=?,BaseStr=?,Con=?,BaseCon=?,Dex=?,BaseDex=?,Cha=?,BaseCha=?,Intel=?,BaseIntel=?,Wis=?,BaseWis=?,Status=?,Class=?,Sex=?,Type=?,Heading=?,LocX=?,LocY=?,MapID=?,Food=?,Lawful=?,Title=?,ClanID=?,Clanname=?,ClanRank=?,notes=?,BonusStatus=?,ElixirStatus=?,ElfAttr=?,GloryEarthAttr=?,PKcount=?,ExpRes=?,PartnerID=?,OnlineStatus=?,HomeTownID=?,Contribution=?,Pay=?,HellTime=?,Banned=?,Karma=?,LastPk=?,DeleteTime=?,ReturnStat=?,lastLoginTime=now(),AinState=?,SurvivalGauge=?,BirthDay=?,Mark_Count=?,Age=?,AddDamage=?,AddDamageRate=?,AddReduction=?,AddReductionRate=?,TamEndTime=?,SpecialSize=?,FishingShopBuyTime_1=?,slot_number=?,elf_attrinit_count=?,clan_contribution=?, class_rank_bless_time=?");


               pstm.setShort(++i, pc.getAccessLevel());
               pstm.setString(++i, pc.getAccountName());
               pstm.setInt(++i, pc.getId());
               pstm.setString(++i, pc.getName());
               pstm.setInt(++i, pc.getLevel());
               pstm.setInt(++i, pc.getHighLevel());
               pstm.setLong(++i, pc.get_exp());
               pstm.setInt(++i, pc.getBaseMaxHp());
               int hp = pc.getCurrentHp();
               if (hp < 1) {
                 hp = 1;
               }
               pstm.setInt(++i, hp);
               pstm.setInt(++i, pc.getBaseMaxMp());
               pstm.setInt(++i, pc.getCurrentMp());
               pstm.setInt(++i, pc.getAC().getAc());
               pstm.setInt(++i, pc.getAbility().getStr());
               pstm.setInt(++i, pc.getAbility().getBaseStr());
               pstm.setInt(++i, pc.getAbility().getCon());
               pstm.setInt(++i, pc.getAbility().getBaseCon());
               pstm.setInt(++i, pc.getAbility().getDex());
               pstm.setInt(++i, pc.getAbility().getBaseDex());
               pstm.setInt(++i, pc.getAbility().getCha());
               pstm.setInt(++i, pc.getAbility().getBaseCha());
               pstm.setInt(++i, pc.getAbility().getInt());
               pstm.setInt(++i, pc.getAbility().getBaseInt());
               pstm.setInt(++i, pc.getAbility().getWis());
               pstm.setInt(++i, pc.getAbility().getBaseWis());
               pstm.setInt(++i, pc.getCurrentWeapon());
               pstm.setInt(++i, pc.getClassId());
               pstm.setInt(++i, pc.get_sex());
               pstm.setInt(++i, pc.getType());
               pstm.setInt(++i, pc.getHeading());
               pstm.setInt(++i, pc.getX());
               pstm.setInt(++i, pc.getY());
               pstm.setInt(++i, pc.getMapId());
               pstm.setInt(++i, pc.get_food());
               pstm.setInt(++i, pc.getLawful());
               pstm.setString(++i, pc.getTitle());
               pstm.setInt(++i, pc.getClanid());
               pstm.setString(++i, pc.getClanname());
               pstm.setInt(++i, pc.getClanRank());
               pstm.setString(++i, pc.getClanMemberNotes());
               pstm.setInt(++i, pc.totalBonusStats());
               pstm.setInt(++i, pc.getElixirStats());
               pstm.setInt(++i, pc.getElfAttr());
               pstm.setInt(++i, pc.getGlory_Earth_Attr());
               pstm.setInt(++i, pc.get_PKcount());
               pstm.setLong(++i, pc.get_exp_res());
               pstm.setInt(++i, pc.getPartnerId());
               pstm.setInt(++i, pc.getOnlineStatus());
               pstm.setInt(++i, pc.getHomeTownId());
               pstm.setInt(++i, pc.getContribution());
               pstm.setInt(++i, 0);
               pstm.setInt(++i, pc.getHellTime());
               pstm.setBoolean(++i, pc.isBanned());
               pstm.setInt(++i, pc.getKarma());
               pstm.setTimestamp(++i, pc.getLastPk());
               pstm.setTimestamp(++i, pc.getDeleteTime());
               pstm.setLong(++i, pc.getReturnStat());
               pstm.setInt(++i, pc.getAinState());
               pstm.setLong(++i, pc.getSurvivalGauge());
               pstm.setInt(++i, pc.getBirthDay());
               pstm.setInt(++i, pc.getMark_count());
               pstm.setInt(++i, pc.getAge());


               pstm.setInt(++i, pc.getAddDamage());
               pstm.setInt(++i, pc.getAddDamageRate());
               pstm.setInt(++i, pc.getAddReduction());
               pstm.setInt(++i, pc.getAddReductionRate());

               pstm.setTimestamp(++i, pc.getTamTime());
               pstm.setInt(++i, pc.get_SpecialSize());
               pstm.setString(++i, (pc.getFishingShopBuyTime_1() == 0L) ? "0000-00-00 00:00:00" : (new Timestamp(pc.getFishingShopBuyTime_1())).toString());
               pstm.setInt(++i, pc.getSlotNumber());
               pstm.setInt(++i, pc.getElfAttrInitCount());
               pstm.setLong(++i, pc.getClanContribution());
               pstm.setTimestamp(++i, pc.getClassRankBlessTime());
               pstm.execute();
               _log.finest("stored char data: " + pc.getName());
             } catch (Exception e) {
               e.printStackTrace();
             } finally {
               SQLUtil.close(pstm);
               SQLUtil.close(con);
             }
           }



           public void deleteCharacter(String accountName, String charName) throws Exception {
             Connection con = null;
             PreparedStatement pstm = null;
             ResultSet rs = null;

             try {
               con = L1DatabaseFactory.getInstance().getConnection();
               pstm = con.prepareStatement("SELECT objid FROM characters WHERE account_name=? AND char_name=?");
               pstm.setString(1, accountName);
               pstm.setString(2, charName);
               rs = pstm.executeQuery();
               if (!rs.next()) {




                 _log.warning("invalid delete char request: account=" + accountName + " char=" + charName);
                 System.out.println("invalid delete char request: account=" + accountName + " char=" + charName);
                 throw new RuntimeException("could not delete character");
               }

               int objId = rs.getInt("objid");
               pstm.close();
               pstm = con.prepareStatement("DELETE FROM character_buddys WHERE char_id = ?");
               pstm.setInt(1, objId);
               pstm.execute();
               pstm.close();
               pstm = con.prepareStatement("DELETE FROM character_buff WHERE char_obj_id = ?");
               pstm.setInt(1, objId);
               pstm.execute();
               pstm.close();
               pstm = con.prepareStatement("DELETE FROM character_config WHERE object_id = ?");
               pstm.setInt(1, objId);
               pstm.execute();
               pstm.close();
               pstm = con.prepareStatement("DELETE FROM character_items WHERE char_id = ?");
               pstm.setInt(1, objId);
               pstm.execute();
               pstm.close();
               pstm = con.prepareStatement("DELETE FROM character_quests WHERE char_id = ?");
               pstm.setInt(1, objId);
               pstm.execute();
               pstm.close();
               pstm = con.prepareStatement("DELETE FROM character_skills WHERE char_obj_id = ?");
               pstm.setInt(1, objId);
               pstm.execute();
               pstm.close();
               pstm = con.prepareStatement("DELETE FROM character_teleport WHERE char_id = ?");
               pstm.setInt(1, objId);
               pstm.execute();
               pstm.close();
               pstm = con.prepareStatement("DELETE FROM characters WHERE objid=?");
               pstm.setInt(1, objId);
               pstm.execute();
               pstm.close();

               pstm = con.prepareStatement("DELETE FROM character_warehouse WHERE id = ?");
               pstm.setInt(1, objId);
               pstm.execute();
               pstm.close();
               pstm = con.prepareStatement("DELETE FROM character_exp_cache WHERE object_id = ?");
               pstm.setInt(1, objId);
               pstm.execute();
               pstm.close();
               pstm = con.prepareStatement("DELETE FROM character_skills_delay WHERE char_id = ?");
               pstm.setInt(1, objId);
               pstm.execute();
               pstm.close();
               pstm = con.prepareStatement("DELETE FROM character_slot_items WHERE char_id = ?");
               pstm.setInt(1, objId);
               pstm.execute();
               pstm.close();
               pstm = con.prepareStatement("DELETE FROM character_special_stat WHERE obj_id = ?");
               pstm.setInt(1, objId);
               pstm.execute();
               pstm.close();
               pstm = con.prepareStatement("DELETE FROM character_supplementary_service WHERE id = ?");
               pstm.setInt(1, objId);
               pstm.execute();
               pstm.close();
               pstm = con.prepareStatement("DELETE FROM attendance_rewards_history WHERE object_id = ?");
               pstm.setInt(1, objId);
               pstm.execute();
               pstm.close();
               pstm = con.prepareStatement("DELETE FROM buy_limit_item_account WHERE account = ?");
               pstm.setInt(1, objId);
               pstm.execute();
               pstm.close();
               pstm = con.prepareStatement("DELETE FROM buy_limit_item_character WHERE char_id = ?");
               pstm.setInt(1, objId);
               pstm.execute();
               pstm.close();
               pstm = con.prepareStatement("DELETE FROM tb_dungeon_time_account_information WHERE owner_info = ?");
               pstm.setInt(1, objId);
               pstm.execute();
               pstm.close();
               pstm = con.prepareStatement("DELETE FROM tb_dungeon_time_char_information WHERE owner_info = ?");
               pstm.setInt(1, objId);
               pstm.execute();
               pstm.close();
               pstm = con.prepareStatement("DELETE FROM tb_kda WHERE objid = ?");
               pstm.setInt(1, objId);
               pstm.execute();
               pstm.close();
               pstm = con.prepareStatement("DELETE FROM character_time_collection WHERE charObjId = ?");
               pstm.setInt(1, objId);
               pstm.execute();
               pstm.close();
             }
             catch (Exception e) {
               throw e;
             } finally {
               SQLUtil.close(rs);
               SQLUtil.close(pstm);
               SQLUtil.close(con);
             }
           }


           public void updateAccountName(L1PcInstance pc) {
             Connection con = null;
             PreparedStatement pstm = null;
             try {
               int i = 0;
               con = L1DatabaseFactory.getInstance().getConnection();

               pstm = con.prepareStatement("UPDATE characters SET account_name=? WHERE objid=?");
               pstm.setString(++i, pc.getAccountName());
               pstm.setInt(++i, pc.getId());
               pstm.execute();
               _log.finest("更新帳號資料：" + pc.getName());
             } catch (Exception e) {
               e.printStackTrace();
             } finally {
               SQLUtil.close(pstm);
               SQLUtil.close(con);
             }
           }


           public void storeCharacter(L1PcInstance pc) {
             Connection con = null;
             PreparedStatement pstm = null;
             try {
               int i = 0;
               con = L1DatabaseFactory.getInstance().getConnection();
               pstm = con.prepareStatement("UPDATE characters SET AccessLevel=?,level=?,HighLevel=?,Exp=?,MaxHp=?,CurHp=?,MaxMp=?,CurMp=?,Ac=?,Str=?,BaseStr=?,Con=?,BaseCon=?,Dex=?,BaseDex=?,Cha=?,BaseCha=?,Intel=?,BaseIntel=?,Wis=?,BaseWis=?,Status=?,Class=?,Sex=?,Type=?,Heading=?,LocX=?,LocY=?,MapID=?,Food=?,Lawful=?,Title=?,ClanID=?,Clanname=?,ClanRank=?,notes=?,BonusStatus=?,ElixirStatus=?,ElfAttr=?,GloryEarthAttr=?,PKcount=?,ExpRes=?,PartnerID=?,OnlineStatus=?,HomeTownID=?,Contribution=?,HellTime=?,Banned=?,Karma=?,LastPk=?,DeleteTime=?,ReturnStat=?,sealScrollTime=?,sealScrollCount=?,lastLogoutTime=now(),AinState=?,SurvivalGauge=?,Mark_Count=?,Age=?,AddDamage=?,AddDamageRate=?,AddReduction=?,AddReductionRate=?, TamEndTime=?,SpecialSize=?,FishingShopBuyTime_1=?,slot_number=?,elf_attrinit_count=?, last_topBless=?, clan_contribution=?, class_rank_bless_time=? WHERE objid=?");




               pstm.setShort(++i, pc.getAccessLevel());
               pstm.setInt(++i, pc.getLevel());
               pstm.setInt(++i, pc.getHighLevel());
               pstm.setLong(++i, pc.get_exp());
               pstm.setInt(++i, pc.getBaseMaxHp());
               int hp = pc.getCurrentHp();
               if (hp < 1) {
                 hp = 1;
               }
               pstm.setInt(++i, hp);
               pstm.setInt(++i, pc.getBaseMaxMp());
               pstm.setInt(++i, pc.getCurrentMp());
               pstm.setInt(++i, pc.getAC().getAc());
               pstm.setInt(++i, pc.getAbility().getStr());
               pstm.setInt(++i, pc.getAbility().getBaseStr());
               pstm.setInt(++i, pc.getAbility().getCon());
               pstm.setInt(++i, pc.getAbility().getBaseCon());
               pstm.setInt(++i, pc.getAbility().getDex());
               pstm.setInt(++i, pc.getAbility().getBaseDex());
               pstm.setInt(++i, pc.getAbility().getCha());
               pstm.setInt(++i, pc.getAbility().getBaseCha());
               pstm.setInt(++i, pc.getAbility().getInt());
               pstm.setInt(++i, pc.getAbility().getBaseInt());
               pstm.setInt(++i, pc.getAbility().getWis());
               pstm.setInt(++i, pc.getAbility().getBaseWis());
               pstm.setInt(++i, pc.getCurrentWeapon());
               pstm.setInt(++i, pc.getClassId());
               pstm.setInt(++i, pc.get_sex());
               pstm.setInt(++i, pc.getType());
               pstm.setInt(++i, pc.getHeading());
               pstm.setInt(++i, pc.getX());
               pstm.setInt(++i, pc.getY());
               pstm.setInt(++i, pc.getMapId());
               pstm.setInt(++i, pc.get_food());
               pstm.setInt(++i, pc.getLawful());
               pstm.setString(++i, pc.getTitle());
               pstm.setInt(++i, pc.getClanid());
               pstm.setString(++i, pc.getClanname());
               pstm.setInt(++i, pc.getClanRank());
               pstm.setString(++i, pc.getClanMemberNotes());
               pstm.setInt(++i, pc.totalBonusStats());
               pstm.setInt(++i, pc.getElixirStats());
               pstm.setInt(++i, pc.getElfAttr());
               pstm.setInt(++i, pc.getGlory_Earth_Attr());
               pstm.setInt(++i, pc.get_PKcount());
               pstm.setLong(++i, pc.get_exp_res());
               pstm.setInt(++i, pc.getPartnerId());
               pstm.setInt(++i, pc.getOnlineStatus());
               pstm.setInt(++i, pc.getHomeTownId());
               pstm.setInt(++i, pc.getContribution());
               pstm.setInt(++i, pc.getHellTime());
               pstm.setBoolean(++i, pc.isBanned());
               pstm.setInt(++i, pc.getKarma());
               pstm.setTimestamp(++i, pc.getLastPk());
               pstm.setTimestamp(++i, pc.getDeleteTime());
               pstm.setLong(++i, pc.getReturnStat());
               pstm.setInt(++i, pc.getSealScrollTime());
               pstm.setInt(++i, pc.getSealScrollCount());
               pstm.setInt(++i, pc.getAinState());
               pstm.setLong(++i, pc.getSurvivalGauge());
               pstm.setInt(++i, pc.getMark_count());
               pstm.setInt(++i, pc.getAge());


               pstm.setInt(++i, pc.getAddDamage());
               pstm.setInt(++i, pc.getAddDamageRate());
               pstm.setInt(++i, pc.getAddReduction());
               pstm.setInt(++i, pc.getAddReductionRate());

               pstm.setTimestamp(++i, pc.getTamTime());
               pstm.setInt(++i, pc.get_SpecialSize());
               pstm.setString(++i, (pc.getFishingShopBuyTime_1() == 0L) ? "0000-00-00 00:00:00" : (new Timestamp(pc.getFishingShopBuyTime_1())).toString());
               pstm.setInt(++i, pc.getSlotNumber());
               pstm.setInt(++i, pc.getElfAttrInitCount());
               pstm.setTimestamp(++i, pc.getLastTopBless());
               pstm.setLong(++i, pc.getClanContribution());
               pstm.setTimestamp(++i, pc.getClassRankBlessTime());
               pstm.setInt(++i, pc.getId());
               pstm.execute();
               _log.finest("stored char data:" + pc.getName());
             } catch (Exception e) {
               System.out.println(pc.getName() + " 請確認角色異常處理（轉交給負責程式碼的同事）");
               e.printStackTrace();
             } finally {
               SQLUtil.close(pstm);
               SQLUtil.close(con);
             }
           }

           public void storeMemo(L1PcInstance pc, String memo) {
             Connection con = null;
             PreparedStatement pstm = null;
             try {
               con = L1DatabaseFactory.getInstance().getConnection();
               pstm = con.prepareStatement("UPDATE characters SET Memo=? WHERE objid=?");
               pstm.setString(1, memo);
               pstm.setInt(2, pc.getId());
               pstm.execute();
               _log.finest("stored char data:" + pc.getName());
             } catch (Exception e) {
               e.printStackTrace();
             } finally {
               SQLUtil.close(pstm);
               SQLUtil.close(con);
             }
           }
           public String getMemo(String name) {
             String memo = null;
             Connection con = null;
             PreparedStatement pstm = null;
             ResultSet rs = null;
             try {
               con = L1DatabaseFactory.getInstance().getConnection();
               pstm = con.prepareStatement("SELECT Memo FROM characters WHERE char_name=?");
               pstm.setString(1, name);
               rs = pstm.executeQuery();

               if (rs.next())
                 memo = rs.getString(1);
             } catch (SQLException e) {
               e.printStackTrace();
               _log.log(Level.SEVERE, "MySqlCharacterStorage[]Error2", e);
             } catch (Exception e) {
               e.printStackTrace();
             } finally {
               SQLUtil.close(rs);
               SQLUtil.close(pstm);
               SQLUtil.close(con);
             }
             return memo;
           }
           public static Timestamp getLogOutTime(String name) {
             Timestamp time = null;
             Connection con = null;
             PreparedStatement pstm = null;
             ResultSet rs = null;
             try {
               con = L1DatabaseFactory.getInstance().getConnection();
               pstm = con.prepareStatement("SELECT lastLogoutTime FROM characters WHERE char_name=?");
               pstm.setString(1, name);
               rs = pstm.executeQuery();

               if (rs.next())
                 time = rs.getTimestamp(1);
             } catch (SQLException e) {
               e.printStackTrace();
               _log.log(Level.SEVERE, "MySqlCharacterStorage[]Error2", e);
             } catch (Exception e) {
               e.printStackTrace();
             } finally {
               SQLUtil.close(rs);
               SQLUtil.close(pstm);
               SQLUtil.close(con);
             }


             return time;
           }

           public static Timestamp getClanJoinTime(String name) {
             Timestamp time = null;
             Connection con = null;
             PreparedStatement pstm = null;
             ResultSet rs = null;
             try {
               con = L1DatabaseFactory.getInstance().getConnection();
               pstm = con.prepareStatement("SELECT clan_join_date FROM characters WHERE char_name=?");
               pstm.setString(1, name);
               rs = pstm.executeQuery();

               if (rs.next())
                 time = rs.getTimestamp(1);
             } catch (SQLException e) {
               e.printStackTrace();
               _log.log(Level.SEVERE, "MySqlCharacterStorage[]Error2", e);
             } catch (Exception e) {
               e.printStackTrace();
             } finally {
               SQLUtil.close(rs);
               SQLUtil.close(pstm);
               SQLUtil.close(con);
             }


             return time;
           }

           public static void storeClanJoinTime(L1PcInstance pc) {
             Timestamp time = new Timestamp(System.currentTimeMillis());
             Connection con = null;
             PreparedStatement pstm = null;
             try {
               con = L1DatabaseFactory.getInstance().getConnection();
               pstm = con.prepareStatement("UPDATE characters SET clan_join_date=? WHERE objid=?");
               pstm.setTimestamp(1, time);
               pstm.setInt(2, pc.getId());
               pstm.execute();
               _log.finest("stored char data:" + pc.getName());
             } catch (Exception e) {
               e.printStackTrace();
             } finally {
               SQLUtil.close(pstm);
               SQLUtil.close(con);
             }
           }


           public static boolean isValidUserObjectId(final int objectId) {
             final MJObjectWrapper<Boolean> wrapper = new MJObjectWrapper();
             wrapper.value = Boolean.valueOf(false);
             Selector.exec("select objid from characters where objid=?", new SelectorHandler()
                 {
                   public void handle(PreparedStatement pstm) throws Exception {
                     pstm.setInt(1, objectId);
                   }


                   public void result(ResultSet rs) throws Exception {
                     wrapper.value = Boolean.valueOf(rs.next());
                   }
                 });
             return ((Boolean)wrapper.value).booleanValue();
           }
         }


