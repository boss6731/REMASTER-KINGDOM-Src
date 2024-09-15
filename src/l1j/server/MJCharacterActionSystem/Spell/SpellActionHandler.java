package l1j.server.MJCharacterActionSystem.Spell;

import static l1j.server.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;
import static l1j.server.server.model.skill.L1SkillId.MEDITATION;
import static l1j.server.server.model.skill.L1SkillId.TURN_UNDEAD;
import l1j.server.Config;
import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJ3SEx.Loader.SpriteInformationLoader;
import l1j.server.MJCharacterActionSystem.AbstractActionHandler;
import l1j.server.MJCharacterActionSystem.Executor.CharacterActionExecutor;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_DELAY_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_BuilderCommand.SC_SPELL_LATE_HANDLING_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_BuilderCommand.SC_SPELL_LATE_HANDLING_NOTI.eLevel;
import l1j.server.MJTemplate.PacketHelper.MJPacketParser;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.datatables.CharacterSkillDelayTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.types.Point;
import l1j.server.server.utils.CalcStat;
import l1j.server.server.utils.MJCommons;

public abstract class SpellActionHandler extends AbstractActionHandler implements MJPacketParser {
	protected int skillId;
	protected int tId;
	protected int tX;
	protected int tY;
	protected String message;

	@Override
	public void handle(){
		if(!validation())
			return;
		if (skill_delay())
			return;
		L1SkillUse l1skilluse = new L1SkillUse();
		l1skilluse.handleCommands(owner, skillId, tId, tX, tY, message, 0, L1SkillUse.TYPE_NORMAL);
		l1skilluse = null;
	}
	
	public SpellActionHandler setSkillId(int skillId){
		this.skillId = skillId;
		return this;
	}

	@Override
	public void doWork(){
		if (owner.hasSkillEffect(ABSOLUTE_BARRIER)) {
			owner.removeSkillEffect(ABSOLUTE_BARRIER);
		}
		if (!owner.isPassive(MJPassiveID.MEDITATION_BEYOND.toInt())){
			owner.killSkillEffectTimer(MEDITATION);
		}
		
		if (skillId == L1SkillId.SHADOW_STEP || skillId == L1SkillId.AVENGER || skillId == L1SkillId.ARMOR_BRAKE){
			if (owner.isDarkelf()){
				if (owner.isPassive(MJPassiveID.BLIND_HIDDING_ASSASSIN.toInt())){
					if (owner.hasSkillEffect(L1SkillId.BLIND_HIDING)){
							owner.delBlindHiding();
					}
				}
			}
		}
		
		handle();
	}

	@Override
	public boolean validation() {
		owner.offFishing();
		L1Map m = owner.getMap();

		if (MJCommons.isNonAction(owner)) {
			if (skillId == L1SkillId.RECOVERY)
				return true;
		}
		if (!m.isUsableSkill()) {
			owner.sendPackets(new S_ServerMessage(563)); // 在此處無法使用。
			return false;
		}
		if(MJCommons.isNonAction(owner))
			return false;

		// TODO 如果添加魔法頭盔（神之頭盔、力量頭盔等），也需要在 C_UseSkill.java 中添加（防止錯誤）
		if (skillId != L1SkillId.HASTE && skillId != L1SkillId.PHYSICAL_ENCHANT_DEX
				&& skillId != L1SkillId.HEAL && skillId != L1SkillId.EXTRA_HEAL
				&& skillId != L1SkillId.ENCHANT_WEAPON && skillId != L1SkillId.DETECTION && skillId != L1SkillId.PHYSICAL_ENCHANT_STR
				&& skillId != L1SkillId.GREATER_HASTE) {
			// 檢查是否學會技能
			if (!owner.isTempSkillActive(skillId)) {
				if (!owner.isSkillMastery(skillId)) {
					System.out.println(String.format("未學習的技能使用嘗試，錯誤利用者：[ %s ] 技能ID：[ %d ]", owner.getName(), skillId));
				}
					return false;
				}
			}

		// XXX 阻止自動狩獵時特定增益效果的無限循環
		L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
		if (skill != null) {
			if (skill.is_auto_skill_err()) {
				if (owner.hasSkillEffect(skillId) && owner.get_is_client_auto()) {
					return false;
				}
			}
		}
		
		if (skillId == L1SkillId.COUNTER_BARRIER) {
			if (owner.isPassive(MJPassiveID.COUNTER_BARRIER_MASTER.toInt())) {
				if (owner.hasSkillEffect(L1SkillId.COUNTER_BARRIER) && owner.get_is_client_auto()) {
					return false;
				}
			}
		}
		
		if (skillId == L1SkillId.DOUBLE_BRAKE) {
			if (owner.isPassive(MJPassiveID.DOUBLE_BREAK_DESTINY.toInt())) {
				if (owner.hasSkillEffect(L1SkillId.DOUBLE_BRAKE) && owner.get_is_client_auto()) {
					return false;
				}
			}
		}
		if (skillId == L1SkillId.SHADOW_STEP) {
			if (!owner.isPassive(MJPassiveID.SHADOW_STEP_CHASER.toInt())){
				if (Target_loc_check(tX, tY, tId)) {
					owner.sendPackets(6754);
					return false;
				}
			} else if (owner.isPassive(MJPassiveID.SHADOW_STEP_CHASER.toInt())){
				return true;
			}
		}
		if (skillId == L1SkillId.CALL_CLAN_ADVENCE) {
			L1Object obj = L1World.getInstance().findObject(tId);
			if (obj instanceof L1PcInstance) {
				L1PcInstance target = (L1PcInstance) obj;
				if (owner.getClan() != target.getClan()) {
					owner.sendPackets(414);
					return false;
				}
				if (target.hasSkillEffect(L1SkillId.TEMPEST)|| target.hasSkillEffect(L1SkillId.PRESSURE_DEATH_RECALL)
						|| target.hasSkillEffect(L1SkillId.PHANTOM_DEATH)) {
					return false;
				}

				boolean isRange = (owner.getLocation().getTileLineDistance(new Point(obj.getX(), obj.getY())) <= 8);
				if (isRange) {
					L1Location loc = new L1Location();
					int locX = owner.getX();
					int locY = owner.getY();
					int heading = owner.getHeading();
					loc.setMap(owner.getMapId());
					switch (heading) {
					case 1:
						locX += 1;
						locY -= 1;
						break;
					case 2:
						locX += 1;
						break;
					case 3:
						locX += 1;
						locY += 1;
						break;
					case 4:
						locY += 1;
						break;
					case 5:
						locX -= 1;
						locY += 1;
						break;
					case 6:
						locX -= 1;
						break;
					case 7:
						locX -= 1;
						locY -= 1;
						break;
					case 0:
						locY -= 1;
						break;
					}
					loc.setX(locX);
					loc.setY(locY);

					L1Map map = L1WorldMap.getInstance().getMap(owner.getMapId());
					if (!MJPoint.isValidPosition(map, locX, locY)) {
						owner.sendPackets(6754);
						return false;
					}
				} else {
					owner.sendPackets(4220);
					return false;
				}
			} else {
				owner.sendPackets(281);
				return false;
			}
		}
		
		L1Object obj = L1World.getInstance().findObject(tId);
		if (obj instanceof L1PcInstance) {
			L1PcInstance target = (L1PcInstance) obj;
			if (target != null) {
				// 在 l1skilluse 中單獨設置
/*				int DCCD = CalcStat.calcPureDecreaseCCDuration(target.getAbility().getCha())+CalcStat.calcDecreaseCCDuration(target.getAbility().getCha())
						+ target.get_status_time_reduce();
				if (DCCD !=0){
					if (check_DecreaseCCDuration_skills(skillId)) {
						L1SkillUse l1skilluse = new L1SkillUse();
						l1skilluse.handleCommands(owner, skillId, owner.getId(), owner.getX(), owner.getY(), message, 0, L1SkillUse.TYPE_GMBUFF);
					}
				}
				int ICCD = owner.get_CC_Increase();
				if (ICCD != 0) {
					if (check_DecreaseCCDuration_skills(skillId)) {
						
					}
				}*/
				
				if (target.isPassive(MJPassiveID.MAELSTROM.toInt())) {
					if (check_maelstrom_skills(skillId)) {
						if (MJRnd.isWinning(100, Config.MagicAdSetting_Lancer.MAELSTROM)) {
							owner.set_Maelstrom(true);
							target.send_effect(19394);
							L1SkillUse l1skilluse = new L1SkillUse();
							l1skilluse.handleCommands(owner, skillId, owner.getId(), owner.getX(), owner.getY(), message, 0, L1SkillUse.TYPE_GMBUFF);
							l1skilluse = null;
							owner.set_Maelstrom(false);
							skill_delay();
							return false;
						}
					}
				}
				if (target.hasSkillEffect(L1SkillId.MAFR)) {
					L1Skills l1skills = SkillsTable.getInstance().getTemplate(skillId);
					if (l1skills.isDebuff()) {
						if (MJRnd.isWinning(100, SkillsTable.getInstance().getTemplate(L1SkillId.MAFR).getProbabilityValue())) {
							target.send_effect(18945);
						} else {
							target.removeSkillEffect(L1SkillId.MAFR);
							target.send_effect(18946);
						}
						S_DoActionGFX gfx = new S_DoActionGFX(owner.getId(), l1skills.getActionId());
						owner.sendPackets(gfx);
						owner.broadcastPacket(gfx);
						skill_delay();
						return false;
					}
				}
			}
		}

		// TODO 魔法 -> 怪物的回應 (條件) 部分應該在下面添加.
		if (obj instanceof L1MonsterInstance) {
			L1MonsterInstance mon = (L1MonsterInstance) obj;
			if (mon != null) {
				if (skillId == TURN_UNDEAD && !mon.getNpcTemplate().get_IsTU()) {
					owner.sendPackets(3327);
					return false;
				}

				// TODO 魔法 -> 怪物的回應 (條件) 這部分應使用 if 語句.
			}
		}
		
		return check_morph();
	}

	// 颶風之怒被動技能 (反射暈眩類技能)
	public static boolean check_maelstrom_skills(int use_skill) {
		Integer[] skills = { L1SkillId.EARTH_BIND, L1SkillId.SHOCK_STUN, L1SkillId.PANTHERA, L1SkillId.EMPIRE,
				L1SkillId.ICE_LANCE, L1SkillId.CURSE_PARALYZE, L1SkillId.BONE_BREAK, L1SkillId.PHANTASM, L1SkillId.CHAINSWORD_STUN,
				L1SkillId.CRUEL, L1SkillId.TEMPEST };
		for(Integer skillId : skills) {
			if(use_skill == skillId)
				return true;
		}
		return false;
	}
	
	
	public static boolean check_DecreaseCCDuration_skills(int use_skill){
		Integer[] skills = { L1SkillId.EARTH_BIND, L1SkillId.SHOCK_STUN, L1SkillId.PANTHERA, L1SkillId.EMPIRE,
				L1SkillId.ICE_LANCE, L1SkillId.CURSE_PARALYZE, L1SkillId.BONE_BREAK, L1SkillId.PHANTASM, L1SkillId.CHAINSWORD_STUN,
				L1SkillId.CRUEL, L1SkillId.TEMPEST };
		for(Integer skillId : skills) {
			if(use_skill == skillId)
				return true;
		}
		return false;
	}
	
	private boolean check_morph(){
		if(owner.isGm() || tId == owner.getId() || tId == 0)
			return true;
		
		int spr_id = owner.getCurrentSpriteId();
		if (spr_id != 12015 && spr_id != 5641 && spr_id != 11685 && spr_id != 11620 && spr_id != 11621)
			return true;
		
		L1Object target = L1World.getInstance().findObject(tId);
		if(target == null || !(target instanceof L1PcInstance))
			return true;
		
		return false;
	}
	
	public boolean skill_delay() {
		long currentMillis = System.currentTimeMillis();
		if (!CharacterSkillDelayTable.getInstace().checkDelay(owner, skillId)) {
			return true;
		}

		// TODO 測量魔法延遲時 (解除所有增益效果後測量)
		// System.out.println(currentMillis - owner.lastSpellUseMillis + " 技能ID : " + skillId + "");

		if (Config.MagicAdSetting.SPELLDELAYRUN) {
				if (owner.lastSpellUseMillis > currentMillis) {
					if (++owner.lastSpellUsePending > Config.MagicAdSetting.SPELLDELAYOVERPENDING) {
						if (!owner.get_is_client_auto()) {
							System.out.println(String.format("[延遲+][無延遲/反延遲] 技能延遲過高(延遲檢查):[%s] 技能ID : [%d], 誤差 : [%d] [監視警告]", owner.getName(), skillId, owner.lastSpellUseMillis - currentMillis));
							return true;
						}
					}
				} else
					owner.lastSpellUsePending = 0;
		}

		// TODO 測量法術延遲時的註釋
		long delay = SpriteInformationLoader.getInstance().getUseSpellInterval(owner, skillId);
		int group_id = SpriteInformationLoader.getInstance().getUseSpellGroupId(skillId);
		long global_delay = SpriteInformationLoader.getInstance().getUseSpellGlobalInterval(owner, skillId);
		if (group_id > 0) {
			owner.lastSpellUseMillis = currentMillis + (global_delay > 0 ? global_delay : delay); // 一號 二號 三號
		} else {
			owner.lastSpellUseMillis = currentMillis + (delay > 0 ? delay : 0L);
		}
		long last_delay = global_delay > 0 ? global_delay : delay;
		// System.out.println("use delay:" + delay + " last_delay:" + last_delay + " group_id:" + group_id);
		// TODO 測量魔法延遲時

		// XXX 基於特定條件減少延遲的代碼
		// 2022-11-05 移除先鋒技能的再使用時間減少
		/*    if (owner.isLancer()) {
		if (skillId == L1SkillId.VANGUARD) {
		if (owner.getEquipSlot().getWeapon() != null) {
		if (owner.getEquipSlot().getWeapon().getItemId() == 203042) { // 馬普爾的苦痛
		delay -= 2000;
		}
		}
		}
		}*/
		
		int DCT = CalcStat.calcDecreaseCoolTime(owner.getAbility().getTotalCha()) + CalcStat.calcPureDecreaseCoolTime(owner.getAbility().getCha());
		if (DCT != 0){ // 通過魅力數據減少冷卻時間
			delay -= DCT;
//			long delay1 = delay;
			if (delay < 200){
				delay = 200;
			}
			global_delay -= DCT;
			if (global_delay <= 200){
				global_delay = 200;
			}
		}
		

		SC_SPELL_LATE_HANDLING_NOTI.send(owner, true, eLevel.NOT_CORRECTION);
		SC_SPELL_DELAY_NOTI.UseSkillDelay(owner, (int) delay, (int) last_delay, group_id);

		// XXX 修改版
		if (skillId == L1SkillId.ABSOLUTE_BARRIER || skillId == L1SkillId.MAFR || skillId == L1SkillId.ASURA || skillId == L1SkillId.DEVINE_PROTECTION) {
			CharacterSkillDelayTable.getInstace().update(owner, skillId, delay + currentMillis);
		}
		return false;
	}

	public boolean Target_loc_check(int tx, int ty, int targetid) {
		L1Map map = owner.getMap();
		int chx = owner.getX();
		int chy = owner.getY();
		for (int i = 0; i < 15; i++) {
			int cx = Math.abs(chx - tx);
			int cy = Math.abs(chy - ty);
			if (cx <= 1 && cy <= 1)
				break;

			if (chx < tx)
				chx++;
			else if (chx > tx)
				chx--;
			if (chy < ty)
				chy++;
			else if (chy > ty)
				chy--;
			
			if (map != null) {// 如果再次出現錯誤，請檢查座標（黑屏）
				try {
					L1Character target = (L1Character) L1World.getInstance().findVisibleCharacterFromPosition(chx, chy, map.getBaseMapId());
					if (target != null) {
						if (target.getId() != targetid) {
							return true;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(String.format("[警告:重要] 請務必向程式員報告。錯誤技能編號：%d", skillId));
				}
			}
		}

		return false;
	}
	
	@Override
	public void dispose(){
		message = null;
		super.dispose();
	}
	
	@Override
	public int getRegistIndex(){
		return CharacterActionExecutor.ACTION_IDX_SPELL;
	}
	
	@Override
	public long getInterval() {
		L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
		return owner.getCurrentSpriteInterval(skill.getTarget().equals("attack") ? EActionCodes.spell_dir : EActionCodes.spell_nodir);
	}
	
	@Override
	public abstract void parse(L1PcInstance owner, ClientBasePacket pck);
	public abstract SpellActionHandler copy();
	
	@Override
	public boolean empty() {
		return false;
	}
}
