package l1j.server.server.server.datatables;

import static l1j.server.server.model.skill.L1SkillId.*;

import java.sql.PreparedStatement;
import java.util.LinkedList;
import java.util.Map.Entry;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.BatchHandler;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.timer.L1SkillTimer;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.SQLUtil;

public class CharBuffTable {

	private CharBuffTable() {	}

	private static int MetisBlessingScroll;
	private static int MetisSincereSoup;
	private static int MetisSincereCuisine;
	/**
	 * TODO 如果技能表中的 is_save 為 true, 則不應該放入這裡。
	 *      只能放入其中一個。在這種情況下，請在技能表中管理。
	 *      如果技能表中不存在，則放入這裡。一起放入會導致鍵重複錯誤。
	 */
	public static final int[] buffSkill = {
            /* 其他技能保存 **/
			1000,1001,1002,4914,
			STR_BUFF, DEX_BUFF, INT_BUFF, LevelUpBonus, STATUS_ELFBRAVE, STATUS_CHAT_PROHIBITED,
			POLY_RING_MASTER,POLY_RING_MASTER2,DECIDING_BUFF,HERO_GAHO_BUFF,
			SIDE_OF_ME_BLESSING, RE_START_BLESSING, NEW_START_BLESSING, LIFE_BLESSING,
			DISEASE,WIND_SHACKLE,ANTA_BUFF, FAFU_BUFF, RIND_BUFF,
			EXP_POTION, EXP_BUFF, EXP_POTION_Event,STATUS_BLUE_POTION2, STATUS_FRUIT,
			STATUS_CASHSCROLL, STATUS_CASHSCROLL2, STATUS_CASHSCROLL3, STATUS_CASHSCROLL4,STATUS_CASHSCROLL5,STATUS_CASHSCROLL6, STATUS_DRAGON_PEARL,

            /* 烹飪1階段效果賦予 **/
			COOKING_1_0_N, COOKING_1_0_S, COOKING_1_1_N, COOKING_1_1_S,
			COOKING_1_2_N, COOKING_1_2_S, COOKING_1_3_N, COOKING_1_3_S,
			COOKING_1_4_N, COOKING_1_4_S, COOKING_1_5_N, COOKING_1_5_S,
			COOKING_1_6_N, COOKING_1_6_S,

            /* 烹飪2階段效果賦予 **/
			COOKING_1_8_N, COOKING_1_8_S, COOKING_1_9_N, COOKING_1_9_S,
			COOKING_1_10_N, COOKING_1_10_S, COOKING_1_11_N, COOKING_1_11_S,
			COOKING_1_12_N, COOKING_1_12_S, COOKING_1_13_N, COOKING_1_13_S,
			COOKING_1_14_N, COOKING_1_14_S,

            /* 烹飪3階段效果賦予 **/
			COOKING_1_16_N, COOKING_1_16_S, COOKING_1_17_N, COOKING_1_17_S,
			COOKING_1_18_N, COOKING_1_18_S, COOKING_1_19_N, COOKING_1_19_S,
			COOKING_1_20_N, COOKING_1_20_S, COOKING_1_21_N, COOKING_1_21_S,
			COOKING_1_22_N, COOKING_1_22_S,
			MetisBlessingScroll, MetisSincereSoup, MetisSincereCuisine, COMA_A, COMA_B, SetBuff, NarutoThankYouCandy,

            /* 重製料理 **/
			COOK_STR, COOK_DEX, COOK_INT, COOK_GROW,
			COOK_STR_Bless,COOK_DEX_Bless,COOK_INT_Bless,COOK_GROW_Bless,

            /* 龍之翡翠 **/
			EMERALD_NO, EMERALD_YES, DRAGON_TOPAZ, DRAGON_PUPLE, TOP_RANKER,

            /* 魔眼增益 & 運勢增益 **/
			/*ANTA_MAAN, FAFU_MAAN, VALA_MAAN, LIND_MAAN, BIRTH_MAAN, SHAPE_MAAN, LIFE_MAAN, BLACK_DRAGON_MAAN, NAVER_BLACK_DRAGON_MAAN,*/
			FEATHER_BUFF_A, FEATHER_BUFF_B, FEATHER_BUFF_C, FEATHER_BUFF_D,
			WITCH_MANA_POTION,WITCH_MANA_POTION1,BUYER_COOLTIME,
			USER_WANTED1, USER_WANTED2, USER_WANTED3, EINHASAD_PRIMIUM_FLAT, EINHASAD_GREAT_FLAT, DRAGON_ARMOR_BLESSING, WEEK_BOX,

            /* 艾恩哈薩德的祝福 **/
			EINHASAD_GRACE, //PC_CAFE,

			CLASS_RANK_BLESS_PRINCE_1,CLASS_RANK_BLESS_KNIGHT_1,CLASS_RANK_BLESS_ELF_1,CLASS_RANK_BLESS_WIZARD_1,CLASS_RANK_BLESS_DARKELF_1,
			CLASS_RANK_BLESS_DRAGONKNIGHT_1,CLASS_RANK_BLESS_BLACKWIZARD_1,CLASS_RANK_BLESS_WARRIOR_1,CLASS_RANK_BLESS_FENCER_1,

			CLASS_RANK_BLESS_PRINCE_2,CLASS_RANK_BLESS_KNIGHT_2,CLASS_RANK_BLESS_ELF_2,CLASS_RANK_BLESS_WIZARD_2,CLASS_RANK_BLESS_DARKELF_2,
			CLASS_RANK_BLESS_DRAGONKNIGHT_2,CLASS_RANK_BLESS_BLACKWIZARD_2,CLASS_RANK_BLESS_WARRIOR_2,CLASS_RANK_BLESS_FENCER_2,

			CLASS_RANK_BLESS_PRINCE_3,CLASS_RANK_BLESS_KNIGHT_3,CLASS_RANK_BLESS_ELF_3,CLASS_RANK_BLESS_WIZARD_3,CLASS_RANK_BLESS_DARKELF_3,
			CLASS_RANK_BLESS_DRAGONKNIGHT_3,CLASS_RANK_BLESS_BLACKWIZARD_3,CLASS_RANK_BLESS_WARRIOR_3,CLASS_RANK_BLESS_FENCER_3

            /* 網咖額外經驗值增益 **/
//			PC_EXP_UP,

            /* 魔力減少藥水 **/
//			MANADECREASEPOTION
	};

	public static void DeleteBuff(L1PcInstance pc) {
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_buff WHERE char_obj_id=?");
			pstm.setInt(1, pc.getId());
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);

		}
	}

	public static void SaveBuff(L1PcInstance pc) {
		if(pc.getAI() != null)
			return;

		final LinkedList<BuffStoreModel> skillModels = new LinkedList<>();

		for (Entry<Integer, L1SkillTimer> entry : pc.hasSkills()) {
			L1Skills skill = SkillsTable.getInstance().getTemplate(entry.getKey());

			if (skill != null) {
				if (skill.isSave()) {
					int timeSec = pc.getSkillEffectTimeSec(entry.getKey());
					int polyId = 0;
					if (entry.getKey() == SHAPE_CHANGE || entry.getKey() == POLY_RING_MASTER || entry.getKey() == POLY_RING_MASTER2) {
						polyId = pc.getCurrentSpriteId();
					}
					skillModels.add(new BuffStoreModel(entry.getValue().skillId(), timeSec, polyId));
				}
			}
		}

		for (int skillId : buffSkill) {
			if(!pc.hasSkillEffect(skillId))
				continue;

			int timeSec = pc.getSkillEffectTimeSec(skillId);

			int polyId = 0;
			if (skillId == SHAPE_CHANGE || skillId == POLY_RING_MASTER || skillId == POLY_RING_MASTER2) {
				polyId = pc.getCurrentSpriteId();
			}
			if (skillId == USER_WANTED1 || skillId == USER_WANTED2 || skillId == USER_WANTED3) {
				timeSec = -1;
			}
//            System.out.println("保存增益" + skillId + "+" + timeSec);
			skillModels.add(new BuffStoreModel(skillId, timeSec, polyId));
//            System.out.println("技能ID: " + skillId + " 時間: " + timeSec);
			//StoreBuff(pc.getId(), skillId, timeSec, polyId);
			
			/*
			int timeSec = pc.getSkillEffectTimeSec(skillId);
			if (0 < timeSec) {
				System.out.println(skillId);
				int polyId = 0;
				if (skillId == SHAPE_CHANGE || skillId == POLY_RING_MASTER) {
					polyId = pc.getCurrentSpriteId();
				}
				StoreBuff(pc.getId(), skillId, timeSec, polyId);
			}*/
		}
		int size = skillModels.size();
		if(size <= 0) {
			return;
		}
		final int characterId = pc.getId();
		Updator.batch("INSERT INTO character_buff SET char_obj_id=?, skill_id=?, remaining_time=?, poly_id=?", new BatchHandler() {
			@Override
			public void handle(PreparedStatement pstm, int callNumber) throws Exception {
				BuffStoreModel model = skillModels.get(callNumber);
				pstm.setInt(1, characterId);
				pstm.setInt(2, model.skillId);
				pstm.setInt(3, model.timeSec);
				pstm.setInt(4, model.polyId);
			}
		}, size);
	}

	private static class BuffStoreModel{
		int skillId;
		int timeSec;
		int polyId;
		BuffStoreModel(int skillId, int timeSec, int polyId){
			this.skillId = skillId;
			this.timeSec = timeSec;
			this.polyId = polyId;
		}
	}
}
