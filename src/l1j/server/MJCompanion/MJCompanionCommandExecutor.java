package l1j.server.MJCompanion;

import java.util.ArrayList;

import l1j.server.MJCompanion.Basic.Buff.MJCompanionBuffInfo;
import l1j.server.MJCompanion.Basic.ClassInfo.MJCompanionClassInfo;
import l1j.server.MJCompanion.Basic.Exp.MJCompanionExpPenalty;
import l1j.server.MJCompanion.Basic.FriendShip.MJCompanionFriendShipBonus;
import l1j.server.MJCompanion.Basic.HuntingGround.MJCompanionHuntingGround;
import l1j.server.MJCompanion.Basic.Joke.MJCompanionJokeInfo;
import l1j.server.MJCompanion.Basic.Potion.MJCompanionPotionInfo;
import l1j.server.MJCompanion.Basic.Skills.MJCompanionSkillEnchantInfo;
import l1j.server.MJCompanion.Basic.Skills.MJCompanionSkillInfo;
import l1j.server.MJCompanion.Basic.Skills.MJCompanionSkillTierOpenInfo;
import l1j.server.MJCompanion.Basic.Stat.MJCompanionStatEffect;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJCompanion.Instance.MJCompanionInstanceCache;
import l1j.server.MJCompanion.Instance.MJCompanionRegenerator;
import l1j.server.MJCompanion.Instance.MJCompanionUpdateFlag;
import l1j.server.MJTemplate.Command.MJCommand;
import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.MJTemplate.Exceptions.MJCommandArgsIndexException;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.SC_COMPANION_SKILL_NOTI.Skill;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.SC_COMPANION_SKILL_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.SC_COMPANION_STATUS_NOTI;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.IntRange;

public class MJCompanionCommandExecutor implements MJCommand{
	public static void exec(MJCommandArgs args){
		new MJCompanionCommandExecutor().execute(args);
	}

	@Override
	public void execute(MJCommandArgs args) {

		try{
			switch(args.nextInt()){
				case 1:
					do_reload_commands(args);
					break;
				case 2:
					do_levelup_companion(args);
					break;
				case 3:
					do_give_friend_ship(args);
					break;
				case 4:
					do_oblivion(args);
					break;
				case 5:
					do_stats_initialized(args);
					break;
				case 6:
					do_restore_exp(args);
					break;
				case 7:
					do_traning_initialized(args);
					break;
				case 8:
					do_enchant_skill(args);
					break;
			}

		}catch(Exception e){
			args.notify(".寵物  [1.重新載入][2.升級][3.給予友情點數]");
			args.notify(".寵物  [4.忘記][5.重置技能][6.經驗值恢復]");
			args.notify(".寵物  [7.訓練重置][8.技能附魔]");

		}
	}

	private MJCompanionInstance find_companion(MJCommandArgs args, String name) throws MJCommandArgsIndexException {
		L1PcInstance pc = L1World.getInstance().findpc(name);
		if (pc == null) {
			args.notify(String.format("無法找到%s。", name));
			return null;
		}
		MJCompanionInstance companion = pc.get_companion();
		if (companion == null) {
			args.notify(String.format("%s沒有召喚寵物。", name));
			return null;
		}
		return companion;
	}

	private void do_levelup_companion(MJCommandArgs args) {
		try {
			String name = args.nextString();
			int level = args.nextInt();
			if (!IntRange.includes(level, 1, MJCompanionSettings.COMPANION_MAX_LEVEL)) {
				args.notify(String.format("等級超出範圍。1~%d", MJCompanionSettings.COMPANION_MAX_LEVEL));
				return;
			}
			MJCompanionInstance companion = find_companion(args, name);
			if (companion == null)
				return;

			long exp = ExpTable.getExpByLevel(level) + 1;
			companion.update_exp(exp, MJCompanionUpdateFlag.UPDATE_ALL);
			args.notify(String.format("已將%s的寵物%s(%d)等級調整為%d。", name, companion.getName(), companion.getId(), companion.getLevel()));
		} catch(Exception e) {
			args.notify(".寵物 2(升級) [寵物主人名字] [等級]");
		}
	}

	private void do_give_friend_ship(MJCommandArgs args) {
		try {
			String name = args.nextString();
			int count = args.nextInt();
			MJCompanionInstance companion = find_companion(args, name);
			if (companion == null)
				return;

			companion.set_friend_ship_marble(companion.get_friend_ship_marble() + count);
			companion.update_instance();
			SC_COMPANION_STATUS_NOTI.send(companion.get_master(), companion, MJCompanionUpdateFlag.UPDATE_FRIEND_SHIP);
			args.notify(String.format("已給%s的寵物%s(%d) %d個友情點數。", name, companion.getName(), companion.getId(), count));
		} catch(Exception e) {
			args.notify(".寵物 3(給予友情點數) [寵物主人名字] [數量]");
		}
	}

	private void do_oblivion(MJCommandArgs args) {
		try {
			String name = args.nextString();
			MJCompanionInstance companion = find_companion(args, name);
			if (companion == null)
				return;

			companion.do_oblivion(true);
			args.notify(String.format("已忘記%s的寵物%s(%d)。", name, companion.getName(), companion.getId()));
		} catch(Exception e) {
			args.notify(".寵物 4(忘記) [寵物主人名字]");
		}
	}

	private void do_stats_initialized(MJCommandArgs args) {
		try {
			String name = args.nextString();
			MJCompanionInstance companion = find_companion(args, name);
			if (companion == null)
				return;

			companion.do_stats_initialized();
			args.notify(String.format("已重置%s的寵物%s(%d)的屬性。", name, companion.getName(), companion.getId()));
		} catch(Exception e) {
			args.notify(".寵物 5(屬性重置) [寵物主人名字]");
		}
	}

	private void do_restore_exp(MJCommandArgs args){
		try{
			String name = args.nextString();
			MJCompanionInstance companion = find_companion(args, name);
			if(companion == null)
				return;

			companion.do_restore_keep_exp(true);
			args.notify(String.format("已恢復%s的寵物%s(%d)的經驗值。", name, companion.getName(), companion.getId()));
		}catch(Exception e){
			args.notify(".寵物 6(經驗值恢復) [寵物主人名字]");
		}
	}

	private void do_traning_initialized(MJCommandArgs args) {
		MJCompanionRegenerator.getInstance().do_traning_initialized();
		args.notify("已重置所有寵物的訓練。");
	}

	private void do_enchant_skill(MJCommandArgs args){
		try{
			String name = args.nextString();
			MJCompanionInstance companion = find_companion(args, name);
			if(companion == null)
				return;

			ArrayList<Skill> skills = companion.get_all_skills();
			for(Skill sk : skills)
				sk.set_enchant(10);
			companion.update_skills(skills);
			SC_COMPANION_SKILL_NOTI.send(companion.get_master(), companion);
			companion.update_skill_effect();
			SC_COMPANION_STATUS_NOTI.send(companion.get_master(), companion, MJCompanionUpdateFlag.UPDATE_ALL);

			args.notify(String.format("已將%s的寵物%s(%d)技能強化到10級。", name, companion.getName(), companion.getId()));
		}catch(Exception e){
			args.notify(".寵物 8(技能強化) [寵物主人名字]");
		}
	}

	private void do_reload_commands(MJCommandArgs args){
		try{
			switch(args.nextInt()){
				case 1:{
					MJCompanionClassInfo.do_load();
					for(MJCompanionInstance companion : L1World.getInstance().getAllCompanions()){
						if(companion == null)
							continue;

						MJCompanionInstanceCache.update_cache(companion, true);
						SC_COMPANION_STATUS_NOTI.send(companion.get_master(), companion, MJCompanionUpdateFlag.UPDATE_CLASS_ID);
					}
					args.notify("companion_class表已重新加載。");
					break;
				}
				case 2:{
					MJCompanionSettings.do_load();
					args.notify("companion_settings表已重新加載。");
					break;
				}
				case 3:{
					MJCompanionHuntingGround.do_load();
					args.notify("companion_hunting_ground表已重新加載。");
					break;
				}
				case 4:{
					MJCompanionJokeInfo.do_load();
					args.notify("companion_joke_effect表已重新加載。");
					break;
				}
				case 5:{
					MJCompanionSkillInfo.do_load();
					for(MJCompanionInstance companion : L1World.getInstance().getAllCompanions()){
						if(companion == null)
							continue;

						companion.update_skill_effect();
						SC_COMPANION_STATUS_NOTI.send(companion.get_master(), companion, MJCompanionUpdateFlag.UPDATE_ALL);
					}
					args.notify("companion_skills表已重新加載。");
					break;
				}
				case 6:{
					MJCompanionSkillEnchantInfo.do_load();
					args.notify("companion_skills_enchant表已重新加載。");
					break;
				}
				case 7:{
					MJCompanionSkillTierOpenInfo.do_load();
					args.notify("companion_skills_open表已重新加載。");
					break;
				}
				case 8:{
					MJCompanionStatEffect.do_load();
					for(MJCompanionInstance companion : L1World.getInstance().getAllCompanions()){
						if(companion == null)
							continue;

						companion.update_stats();
						SC_COMPANION_STATUS_NOTI.send(companion.get_master(), companion, MJCompanionUpdateFlag.UPDATE_ALL);
					}
					args.notify("companion_stat_effects表已重新加載。");
					break;
				}
				case 9:{
					MJCompanionPotionInfo.do_load();
					args.notify("companion_potion_info表已重新加載。");
					break;
				}
				case 10:{
					MJCompanionBuffInfo.do_load();
					args.notify("companion_buff表已重新加載。");
					break;
				}
				case 11:{
					MJCompanionFriendShipBonus.do_load();
					args.notify("companion_friend_ship_bonus表已重新加載。");
					break;
				}
				case 12:{
					MJCompanionExpPenalty.do_load();
					args.notify("companion_penalty表已重新加載。");

				}
				default:
					throw new Exception();
			}

		}catch(Exception e){
			args.notify(".寵物 1(重新加載) [1.類別信息][2.設置][3.狩獵場]");
			args.notify(".寵物 1(重新加載) [4.笑話][5.技能效果][6.技能強化]");
			args.notify(".寵物 1(重新加載) [7.技能階段][8.屬性效果][9.藥水效果]");
			args.notify(".寵物 1(重新加載) [10.增益信息][11.友誼獎勵][12.懲罰]");
		}
	}
}
