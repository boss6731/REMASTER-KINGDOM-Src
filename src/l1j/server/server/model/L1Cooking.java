package l1j.server.server.model;

import static l1j.server.server.model.skill.L1SkillId.COOKING_1_0_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_0_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_10_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_10_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_11_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_11_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_12_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_12_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_13_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_13_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_14_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_14_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_15_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_15_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_16_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_16_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_17_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_17_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_18_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_18_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_19_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_19_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_1_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_1_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_20_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_20_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_21_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_21_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_22_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_22_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_23_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_23_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_2_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_2_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_3_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_3_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_4_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_4_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_5_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_5_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_6_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_6_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_7_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_7_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_8_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_8_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_9_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_9_S;
import static l1j.server.server.model.skill.L1SkillId.COOK_DEX;
import static l1j.server.server.model.skill.L1SkillId.COOK_DEX_Bless;
import static l1j.server.server.model.skill.L1SkillId.COOK_GROW;
import static l1j.server.server.model.skill.L1SkillId.COOK_GROW_Bless;
import static l1j.server.server.model.skill.L1SkillId.COOK_INT;
import static l1j.server.server.model.skill.L1SkillId.COOK_INT_Bless;
import static l1j.server.server.model.skill.L1SkillId.COOK_STR;
import static l1j.server.server.model.skill.L1SkillId.COOK_STR_Bless;
import static l1j.server.server.model.skill.L1SkillId.나루터감사캔디;
import static l1j.server.server.model.skill.L1SkillId.메티스정성스프;
import static l1j.server.server.model.skill.L1SkillId.메티스정성요리;
import static l1j.server.server.model.skill.L1SkillId.수련자의닭고기스프;
import static l1j.server.server.model.skill.L1SkillId.수련자의연어찜;
import static l1j.server.server.model.skill.L1SkillId.수련자의칠면조구이;
import static l1j.server.server.model.skill.L1SkillId.수련자의한우스테이크;
import static l1j.server.server.model.skill.L1SkillId.천하장사버프;
import static l1j.server.server.model.skill.L1SkillId.아덴의특제스테이크;
import static l1j.server.server.model.skill.L1SkillId.아덴의특제카나페;
import static l1j.server.server.model.skill.L1SkillId.아덴의특제샐러드;
import static l1j.server.server.model.skill.L1SkillId.아덴의토마토스프;
import static l1j.server.server.model.skill.L1SkillId.축복받은아덴의특제스테이크;
import static l1j.server.server.model.skill.L1SkillId.축복받은아덴의특제카나페;
import static l1j.server.server.model.skill.L1SkillId.축복받은아덴의특제샐러드;
import static l1j.server.server.model.skill.L1SkillId.축복받은아덴의토마토스프;


import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;

public class L1Cooking {

	private L1Cooking() {
	}

	public static void useCookingItem(L1PcInstance pc, L1ItemInstance item) {
		int itemId = item.getItem().getItemId();
		if (itemId == 41284 // 蘑菇湯
				|| itemId == 49056 // 蟹肉湯
				|| itemId == 49064 // 幻象蟹肉湯
				|| itemId == 41292 // 幻象蘑菇湯
				|| itemId == 210055 // 蜥蜴蛋湯
				|| itemId == 210063) { // 幻象蜥蜴蛋湯
			if (pc.get_food() != 225) { // 100%
				pc.sendPackets(new S_ServerMessage(74, item.getNumberedName(1)));
			}
			pc.sendPackets(new S_EffectLocation(pc.getX(), pc.getY(), 6392)); // 當飽食度為225時，使用道具和效果
		}

// TODO 烹飪類
		if (itemId >= 41277 && itemId <= 41283 // 第一階段烹飪
				|| itemId >= 49049 && itemId <= 49056 // 第二階段烹飪
				|| itemId >= 210048 && itemId <= 210055 // 第三階段烹飪
				|| itemId >= 41285 && itemId <= 41291 // 第一階段幻象烹飪
				|| itemId >= 49057 && itemId <= 49064 // 第二階段幻象烹飪
				|| itemId >= 210056 && itemId <= 210062 // 第三階段幻象烹飪
				|| itemId >= 30051 && itemId <= 30053 || itemId >= 4100156 && itemId <= 4100158 || itemId == 3000129 
				|| itemId >= 30001858 && itemId <= 30001860
				|| itemId >= 30001862 && itemId <= 30001864) {
			pc.sendPackets(new S_EffectLocation(pc.getX(), pc.getY(), 6392));// 요리이펙트
			int cookingId = pc.getCookingId();
			if (cookingId != 0) {
				pc.removeSkillEffect(cookingId);
			}
		}

		// TODO 湯類
		if (itemId == 41284 // 蘑菇湯
				|| itemId == 49056 // 蟹肉湯
				|| itemId == 49064 // 幻象蟹肉湯
				|| itemId == 41292 // 幻象蘑菇湯
				|| itemId == 200021 // 蜥蜴蛋湯
				|| itemId == 200029 // 幻象蜥蜴蛋湯
				|| itemId == 30054 // 訓練用雞肉湯
				|| itemId == 4100159 // 訓練用雞肉湯
				|| itemId == 3000130
				|| itemId == 30001861
				|| itemId == 30001865) {
			pc.sendPackets(new S_EffectLocation(pc.getX(), pc.getY(), 6392)); // 料理效果
			int dessertId = pc.getDessertId(); // 獲取甜點ID
			if (dessertId != 0) { // 如果甜點ID不為0
				pc.removeSkillEffect(dessertId); // 移除這個甜點效果
			}
			// 結束湯類處理的括號應該在這裡
			if (itemId >= 42650 && itemId <= 42652) { // 除了訓練者雞肉以外都刪除
				pc.sendPackets(new S_EffectLocation(pc.getX(), pc.getY(), 6392)); // 料理效果
			int dessertId = pc.getDessertId();
			if (dessertId != 0) {
				pc.removeSkillEffect(dessertId);
			}
		}

		/** 1차 요리 효과 */
		int cookingId;
		int time = 900;
		switch (itemId) {
		case 41277:
		case 41285:
			if (itemId == 41277) {
				cookingId = COOKING_1_0_N;
			} else {
				cookingId = COOKING_1_0_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 41278:
		case 41286:
			if (itemId == 41278) {
				cookingId = COOKING_1_1_N;
			} else {
				cookingId = COOKING_1_1_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 41279:
		case 41287:
			if (itemId == 41279) {
				cookingId = COOKING_1_2_N;
			} else {
				cookingId = COOKING_1_2_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 41280:
		case 41288:
			if (itemId == 41280) {
				cookingId = COOKING_1_3_N;
			} else {
				cookingId = COOKING_1_3_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 41281:
		case 41289:
			if (itemId == 41281) {
				cookingId = COOKING_1_4_N;
			} else {
				cookingId = COOKING_1_4_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 41282:
		case 41290:
			if (itemId == 41282) {
				cookingId = COOKING_1_5_N;
			} else {
				cookingId = COOKING_1_5_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 41283:
		case 41291:
			if (itemId == 41283) {
				cookingId = COOKING_1_6_N;
			} else {
				cookingId = COOKING_1_6_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 41284:
		case 41292:
			if (itemId == 41284) {
				cookingId = COOKING_1_7_N;
			} else {
				cookingId = COOKING_1_7_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 49049:
		case 49057:
			if (itemId == 49049) {
				cookingId = COOKING_1_8_N;
			} else {
				cookingId = COOKING_1_8_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 49050:
		case 49058:
			if (itemId == 49050) {
				cookingId = COOKING_1_9_N;
			} else {
				cookingId = COOKING_1_9_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 49051:
		case 49059:
			if (itemId == 49051) {
				cookingId = COOKING_1_10_N;
			} else {
				cookingId = COOKING_1_10_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 49052:
		case 49060:
			if (itemId == 49052) {
				cookingId = COOKING_1_11_N;
			} else {
				cookingId = COOKING_1_11_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 49053:
		case 49061:
			if (itemId == 49053) {
				cookingId = COOKING_1_12_N;
			} else {
				cookingId = COOKING_1_12_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 49054:
		case 49062:
			if (itemId == 49054) {
				cookingId = COOKING_1_13_N;
			} else {
				cookingId = COOKING_1_13_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 49055:
		case 49063:
			if (itemId == 49055) {
				cookingId = COOKING_1_14_N;
			} else {
				cookingId = COOKING_1_14_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 49056:
		case 49064:
			if (itemId == 49056) {
				cookingId = COOKING_1_15_N;
			} else {
				cookingId = COOKING_1_15_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 210048:
		case 210056:
			if (itemId == 210048) {
				cookingId = COOKING_1_16_N;
			} else {
				cookingId = COOKING_1_16_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 210049:
		case 210057:
			if (itemId == 210049) {
				cookingId = COOKING_1_17_N;
			} else {
				cookingId = COOKING_1_17_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 210050:
		case 210058:
			if (itemId == 210050) {
				cookingId = COOKING_1_18_N;
			} else {
				cookingId = COOKING_1_18_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 210051:
		case 210059:
			if (itemId == 210051) {
				cookingId = COOKING_1_19_N;
			} else {
				cookingId = COOKING_1_19_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 210052:
		case 210060:
			if (itemId == 210052) {
				cookingId = COOKING_1_20_N;
			} else {
				cookingId = COOKING_1_20_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 210053:
		case 210061:
			if (itemId == 210053) {
				cookingId = COOKING_1_21_N;
			} else {
				cookingId = COOKING_1_21_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 210054:
		case 210062:
			if (itemId == 210054) {
				cookingId = COOKING_1_22_N;
			} else {
				cookingId = COOKING_1_22_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 210055:
		case 210063:
			if (itemId == 210055) {
				cookingId = COOKING_1_23_N;
			} else {
				cookingId = COOKING_1_23_S;
			}
			eatCooking(pc, cookingId, time);
			break;
			case 4100156: // 受祝福的強力韓牛牛排
				eatCooking(pc, COOK_STR_Bless, 3600);
				break;
			case 30051: // 強力韓牛牛排
				eatCooking(pc, COOK_STR, 1800);
				break;
			case 4100157: // 受祝福的敏捷鮭魚蒸菜
				eatCooking(pc, COOK_DEX_Bless, 3600);
				break;
			case 30052: // 敏捷鮭魚蒸菜
				eatCooking(pc, COOK_DEX, 1800);
				break;
			case 4100158: // 受祝福的聰明火雞烤肉
				eatCooking(pc, COOK_INT_Bless, 3600);
				break;
			case 30053: // 聰明火雞烤肉
				eatCooking(pc, COOK_INT, 1800);
				break;
			case 4100159: // 受祝福的訓練用雞肉湯
				eatCooking(pc, COOK_GROW_Bless, 3600);
				break;
			case 30054: // 訓練用雞肉湯
				eatCooking(pc, COOK_GROW, 1800);
				break;
			case 3000129: // 誠意料理
				eatCooking(pc, 메티스정성요리, 1800);
				break;
			case 3000130: // 誠意湯
			case 4100156: // 受祝福的強力韓牛牛排
				eatCooking(pc, COOK_STR_Bless, 3600);
				break;
			case 30051: // 強力韓牛牛排
				eatCooking(pc, COOK_STR, 1800);
				break;
			case 4100157: // 受祝福的敏捷鮭魚蒸菜
				eatCooking(pc, COOK_DEX_Bless, 3600);
				break;
			case 30052: // 敏捷鮭魚蒸菜
				eatCooking(pc, COOK_DEX, 1800);
				break;
			case 4100158: // 受祝福的聰明火雞烤肉
				eatCooking(pc, COOK_INT_Bless, 3600);
				break;
			case 30053: // 聰明火雞烤肉
				eatCooking(pc, COOK_INT, 1800);
				break;
			case 4100159: // 受祝福的訓練用雞肉湯
				eatCooking(pc, COOK_GROW_Bless, 3600);
				break;
			case 30054: // 訓練用雞肉湯
				eatCooking(pc, COOK_GROW, 1800);
				break;
			case 3000129: // 誠意料理
				eatCooking(pc, METIS_DILIGENT_COOK, 1800);
				break;
			case 3000130: // 誠意湯
				eatCooking(pc, METIS_DILIGENT_SOUP, 1800);
				break;
			case 42650: // 訓練者的韓牛牛排
				eatCooking(pc, TRAINEE_BEEF_STEAK, 1800);
				break;
			case 42651: // 訓練者的鮭魚蒸菜
				eatCooking(pc, TRAINEE_SALMON_STEAM, 1800);
				break;
			case 42652: // 訓練者的火雞烤肉
				eatCooking(pc, TRAINEE_TURKEY_ROAST, 1800);
				break;
			case 42653: // 訓練者的雞肉湯
				eatCooking(pc, TRAINEE_CHICKEN_SOUP, 1800);
				break;
			case 30001858: // 阿登的特製牛排
				eatCooking(pc, ADEN_SPECIAL_STEAK, 1800);
				break;
			case 30001859: // 阿登的特製開胃小吃
				eatCooking(pc, ADEN_SPECIAL_CANAPE, 1800);
				break;
			case 30001860: // 阿登的特製沙拉
				eatCooking(pc, ADEN_SPECIAL_SALAD, 1800);
				break;
			case 30001861: // 阿登的番茄湯
				eatCooking(pc, ADEN_TOMATO_SOUP, 1800);
				break;
			case 30001862: // 受祝福的阿登特製牛排
				eatCooking(pc, BLESSED_ADEN_SPECIAL_STEAK, 1800);
				break;
			case 30001863: // 受祝福的阿登特製開胃小吃
				eatCooking(pc, BLESSED_ADEN_SPECIAL_CANAPE, 1800);
				break;
			case 30001864: // 受祝福的阿登特製沙拉
				eatCooking(pc, BLESSED_ADEN_SPECIAL_SALAD, 1800);
				break;
			case 30001865: // 受祝福的阿登番茄湯
				eatCooking(pc, BLESSED_ADEN_TOMATO_SOUP, 1800);
				break;
			
		default:
			break;
		}
		pc.sendPackets(new S_ServerMessage(76, item.getNumberedName(1)));
		pc.getInventory().removeItem(item, 1);

	}

	/** 1차요리 효과 */
	public static void eatCooking(L1PcInstance pc, int cookingId, int time) {
		int cookingType = 0;

			if (cookingId == COOKING_1_23_N || cookingId == COOKING_1_23_S || cookingId == COOK_GROW || cookingId == COOK_GROW_Bless || cookingId == TRAINEE_CHICKEN_SOUP || cookingId == STRONG_MAN_BUFF
					|| cookingId == METIS_DILIGENT_SOUP || cookingId == BLESSED_ADEN_TOMATO_SOUP  || cookingId == ADEN_TOMATO_SOUP) {
				// 如果玩家有甜點效果，移除該效果
				if (pc.getDessertId() > 0) {
					pc.removeSkillEffect(pc.getDessertId());
				}
			}
		
		switch (cookingId) {
			case ADEN_SPECIAL_STEAK: // 阿登的特製牛排
//			pc.setDessertId(cookingId);
			cookingType = 226;
			pc.addDamageReduction(2);
			pc.addDmgup(3);
			pc.addHitup(2);
			pc.addHpr(5);
			pc.addMpr(2);
			pc.addMaxHp(50);
			pc.add_item_exp_bonus(4);
			pc.getResistance().addMr(10);
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			break;
			case ADEN_SPECIAL_CANAPE: // 阿登的特製開胃小吃
//			pc.setDessertId(cookingId);
			cookingType = 227;
			pc.addDamageReduction(2);
			pc.addBowDmgup(3);
			pc.addBowHitup(2);
			pc.addHpr(3);
			pc.addMpr(3);
			pc.addMaxHp(25);
			pc.addMaxMp(25);
			pc.add_item_exp_bonus(4);
			pc.getResistance().addMr(10);
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			break;
			case ADEN_SPECIAL_SALAD: // 阿登的特製沙拉
//			pc.setDessertId(cookingId);
			cookingType = 228;
			pc.addDamageReduction(2);
			pc.getAbility().addSp(3);
			pc.addHpr(2);
			pc.addMpr(5);
			pc.addMaxMp(50);
			pc.add_item_exp_bonus(4);
			pc.getResistance().addMr(10);
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			break;
			case ADEN_TOMATO_SOUP: // 阿登的番茄湯
//			pc.setDessertId(cookingId);
			cookingType = 229;
			pc.addDamageReduction(3);
			pc.add_item_exp_bonus(6);
			pc.addMaxHp(50);
			pc.getResistance().addcalcPcDefense(2);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			break;
			case BLESSED_ADEN_SPECIAL_STEAK: // 受祝福的阿登特製牛排
//			pc.setDessertId(cookingId);
			cookingType = 230;
			pc.addDamageReduction(2);
			pc.addDmgup(3);
			pc.addHitup(2);
			pc.addHpr(5);
			pc.addMpr(2);
			pc.addMaxHp(50);
			pc.add_item_exp_bonus(4);
			pc.getResistance().addMr(10);
			pc.getResistance().addAllNaturalResistance(10);
			pc.addSpecialPierce(eKind.ALL, 3);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			break;
			case BLESSED_ADEN_SPECIAL_CANAPE: // 受祝福的阿登特製開胃小吃
//			pc.setDessertId(cookingId);
			cookingType = 231;
			pc.addDamageReduction(2);
			pc.addBowDmgup(3);
			pc.addBowHitup(2);
			pc.addHpr(3);
			pc.addMpr(3);
			pc.addMaxHp(25);
			pc.addMaxMp(25);
			pc.add_item_exp_bonus(4);
			pc.getResistance().addMr(10);
			pc.getResistance().addAllNaturalResistance(10);
			pc.addSpecialPierce(eKind.ALL, 3);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			break;
			case BLESSED_ADEN_SPECIAL_SALAD: // 受祝福的阿登特製沙拉
//			pc.setDessertId(cookingId);
			cookingType = 232;
			pc.addDamageReduction(2);
			pc.getAbility().addSp(3);
			pc.addHpr(2);
			pc.addMpr(5);
			pc.addMaxMp(50);
			pc.add_item_exp_bonus(4);
			pc.getResistance().addMr(10);
			pc.getResistance().addAllNaturalResistance(10);
			pc.addSpecialPierce(eKind.ALL, 3);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			break;
			case BLESSED_ADEN_TOMATO_SOUP: // 受祝福的阿登番茄湯
//			pc.setDessertId(cookingId);
			cookingType = 233;
			pc.addDamageReduction(3);
			pc.add_item_exp_bonus(6);
			pc.addMaxHp(50);
			pc.getResistance().addcalcPcDefense(2);
			pc.addSpecialResistance(eKind.ALL, 2);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			break;
			case TRAINEE_HANWOO_STEAK: // 修煉者的韓牛牛排
			pc.setDessertId(cookingId);
			cookingType = 157;
			pc.addHitup(1);
			pc.addDmgup(2);
			pc.addHpr(2);
			pc.addMpr(2);
			pc.getResistance().addMr(10);
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			break;
			case TRAINEE_STEAMED_SALMON: // 修煉者的蒸鮭魚
				pc.setDessertId(cookingId);
				cookingType = 158;
				pc.addBowHitup(1);
				pc.addBowDmgup(2);
				pc.addHpr(2);
				pc.addMpr(2);
				pc.getResistance().addMr(10);
				pc.getResistance().addAllNaturalResistance(10);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				break;

			case TRAINEE_ROAST_TURKEY: // 修煉者的烤火雞
				pc.setDessertId(cookingId);
				cookingType = 159;
				pc.addHpr(2);
				pc.addMpr(3);
				pc.getAbility().addSp(2);
				pc.getResistance().addMr(10);
				pc.getResistance().addAllNaturalResistance(10);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				break;

			case TRAINEE_CHICKEN_SOUP: // 修煉者的雞肉湯
				cookingType = 160;
				pc.add_item_exp_bonus(4);
				break;
		case COOKING_1_0_N:
		case COOKING_1_0_S:
			cookingType = 0;
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			break;
			case NARUTO_THANKSGIVING_CANDY: // 感謝糖果
				if (pc.getLevel() >= 1 && pc.getLevel() <= 60) {
					pc.getAbility().addStr(7);
					pc.getAbility().addDex(7);
				} else {
					pc.getAbility().addStr(6);
					pc.getAbility().addDex(6);
				}
				pc.sendPackets(new S_OwnCharStatus(pc));
				break;
		case COOKING_1_1_N:
		case COOKING_1_1_S:
			cookingType = 1;
			pc.addMaxHp(30);
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			if (pc.isInParty()) {
				// TODO 派對原型
				pc.getParty().refreshPartyMemberStatus(pc);
			}
			break;
		case COOKING_1_2_N:
		case COOKING_1_2_S:
			cookingType = 2;
			break;
		case COOKING_1_3_N:
		case COOKING_1_3_S:
			cookingType = 3;
			pc.getAC().addAc(-1);
			pc.sendPackets(new S_OwnCharStatus(pc));
			break;
		case COOKING_1_4_N:
		case COOKING_1_4_S:
			cookingType = 4;
			pc.addMaxMp(20);
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			break;
		case COOKING_1_5_N:
		case COOKING_1_5_S:
			cookingType = 5;
			break;
		case COOKING_1_6_N:
		case COOKING_1_6_S:
			cookingType = 6;
			pc.getResistance().addMr(5);
			break;
		case COOKING_1_7_N:
		case COOKING_1_7_S:
			cookingType = 7;
			pc.add_item_exp_bonus(1);
			break;
		/** 1차요리 효과끝 */
		case COOKING_1_8_N:
		case COOKING_1_8_S:
			cookingType = 16;
			pc.addBowHitRate(2);
			pc.addBowDmgup(1);
			break;
		case COOKING_1_9_N:
		case COOKING_1_9_S:
			cookingType = 17;
			pc.addMaxHp(30);
			pc.addMaxMp(30);

			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			if (pc.isInParty()) {
				// TODO 派對原型
				pc.getParty().refreshPartyMemberStatus(pc);
			}
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			break;
		case COOKING_1_10_N:
		case COOKING_1_10_S:
			cookingType = 18;
			pc.getAC().addAc(-2);
			pc.sendPackets(new S_OwnCharStatus2(pc));
			break;
		case COOKING_1_11_N:
		case COOKING_1_11_S:
			cookingType = 19;
			break;
		case COOKING_1_12_N:
		case COOKING_1_12_S:
			cookingType = 20;
			break;
		case COOKING_1_13_N:
		case COOKING_1_13_S:
			cookingType = 21;
			pc.getResistance().addMr(10);
			break;
		case COOKING_1_14_N:
		case COOKING_1_14_S:
			cookingType = 22;
			pc.getAbility().addSp(1);
			break;
		case COOKING_1_15_N:
		case COOKING_1_15_S:
			cookingType = 23;
			pc.add_item_exp_bonus(5);
			break;
		/** 2차요리 효과끝 */
		case COOKING_1_16_N:
		case COOKING_1_16_S:
			cookingType = 45;
			pc.addBowHitRate(2);
			pc.addBowDmgup(1);
			break;
		case COOKING_1_17_N:
		case COOKING_1_17_S:
			cookingType = 46;
			pc.addMaxHp(50);
			pc.addMaxMp(50);
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			if (pc.isInParty()) {
				// TODO 派對原型
				pc.getParty().refreshPartyMemberStatus(pc);
			}
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			break;
		case COOKING_1_18_N:
		case COOKING_1_18_S:
			cookingType = 47;
			pc.addHitup(2);
			pc.addDmgup(1);
			break;
		case COOKING_1_19_N:
		case COOKING_1_19_S:
			cookingType = 48;
			pc.getAC().addAc(-3);
			pc.sendPackets(new S_OwnCharStatus2(pc));
			break;
		case COOKING_1_20_N:
		case COOKING_1_20_S:
			cookingType = 49;
			pc.getResistance().addAllNaturalResistance(10);
			pc.getResistance().addMr(15);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			break;
		case COOKING_1_21_N:
		case COOKING_1_21_S:
			cookingType = 50;
			pc.getAbility().addSp(2);
			break;
		case COOKING_1_22_N:
		case COOKING_1_22_S:
			cookingType = 51;
			pc.addMaxHp(30);
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			if (pc.isInParty()) {
				// TODO 派對原型
				pc.getParty().refreshPartyMemberStatus(pc);
			}
			break;
		case COOKING_1_23_N:
		case COOKING_1_23_S:
			cookingType = 52;
			pc.add_item_exp_bonus(9);
			break;
		case COOK_STR:
			cookingType = 157;
			pc.addDamageReductionByArmor(2);
			pc.addDmgup(2);
			pc.addHitup(1);
			pc.addHpr(2);
			pc.addMpr(2);
			pc.getResistance().addWater(10);
			pc.getResistance().addFire(10);
			pc.getResistance().addWind(10);
			pc.getResistance().addEarth(10);
			pc.getResistance().addMr(10);
			pc.add_item_exp_bonus(2);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			break;
		case COOK_DEX:
			cookingType = 158;
			pc.addDamageReductionByArmor(2);
			pc.addBowDmgup(2);
			pc.addBowHitup(1);
			pc.addHpr(2);
			pc.addMpr(2);
			pc.getResistance().addWater(10);
			pc.getResistance().addFire(10);
			pc.getResistance().addWind(10);
			pc.getResistance().addEarth(10);
			pc.getResistance().addMr(10);
			pc.add_item_exp_bonus(2);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			break;
		case COOK_INT:
			cookingType = 159;
			pc.addDamageReductionByArmor(2);
			pc.getAbility().addSp(2);
			pc.addHpr(2);
			pc.addMpr(3);
			pc.getResistance().addMr(10);
			pc.getResistance().addWater(10);
			pc.getResistance().addFire(10);
			pc.getResistance().addWind(10);
			pc.getResistance().addEarth(10);
			pc.add_item_exp_bonus(2);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			break;
		case COOK_GROW:
			cookingType = 160;
			pc.add_item_exp_bonus(4);
			break;
		case COOK_STR_Bless:
			cookingType = 215;
			pc.addDmgup(2);
			pc.addHitup(1);
			pc.addHpr(2);
			pc.addMpr(2);
			pc.getResistance().addMr(10);
			pc.getResistance().addWater(10);
			pc.getResistance().addFire(10);
			pc.getResistance().addWind(10);
			pc.getResistance().addEarth(10);
			pc.add_item_exp_bonus(2);
			pc.addSpecialPierce(eKind.ALL, 3);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			break;
		case COOK_DEX_Bless:
			cookingType = 216;
			pc.addBowDmgup(2);
			pc.addBowHitup(1);
			pc.addHpr(2);
			pc.addMpr(2);
			pc.getResistance().addMr(10);
			pc.getResistance().addWater(10);
			pc.getResistance().addFire(10);
			pc.getResistance().addWind(10);
			pc.getResistance().addEarth(10);
			pc.add_item_exp_bonus(2);
			pc.addSpecialPierce(eKind.ALL, 3);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			break;
		case COOK_INT_Bless:
			cookingType = 217;
			pc.getAbility().addSp(2);
			pc.addHpr(2);
			pc.addMpr(3);
			pc.getResistance().addMr(10);
			pc.getResistance().addWater(10);
			pc.getResistance().addFire(10);
			pc.getResistance().addWind(10);
			pc.getResistance().addEarth(10);
			pc.add_item_exp_bonus(2);
			pc.addSpecialPierce(eKind.ALL, 3);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			break;
		case COOK_GROW_Bless:
			cookingType = 218;
			pc.getResistance().addcalcPcDefense(2);
			pc.add_item_exp_bonus(4);
			pc.addSpecialResistance(eKind.ALL, 2);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			break;
			case CheonhaJangsaBuff: // 天下壯士增益
				if (pc.hasSkillEffect(cookingId))
					pc.removeSkillEffect(cookingId);
				cookingType = 187;
				pc.addDamageReductionByArmor(5);
				pc.add_item_exp_bonus(20);
				break;

			case MetisSeongseongCooking: // 梅蒂斯精心料理
				cookingType = 151;
				pc.addHitup(2);
				pc.addDmgup(2);
				pc.addBowHitup(2);
				pc.addBowDmgup(2);
				pc.getAbility().addSp(2);
				pc.addHpr(3);
				pc.addMpr(4);
				pc.getResistance().addMr(15);
				pc.getResistance().addAllNaturalResistance(10);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				break;

			case MetisSeongseongSoup: // 梅蒂斯精心湯
				cookingType = 162;
				pc.add_item_exp_bonus(5);
				break;
			
			
		default:
			break;
		}

		// pc.sendPackets(new S_EffectLocation(pc.getX(), pc.getY(), 6392)); // Cooking effect
		pc.sendPackets(new S_PacketBox(53, cookingType, time));
		pc.setSkillEffect(cookingId, time * 1000);

			/** 賦予一階料理效果（檢查重複性） */
			if ((cookingId >= COOKING_1_0_N && cookingId <= COOKING_1_6_N) || (cookingId >= COOKING_1_0_S && cookingId <= COOKING_1_6_S)) {
				pc.setCookingId(cookingId);
			} else if (cookingId == COOKING_1_7_N || cookingId == COOKING_1_7_S || cookingId == ApprenticeChickenSoup) {
				pc.setDessertId(cookingId);
			}
			/** 賦予二階料理效果（檢查重複性） */
			else if (cookingId >= COOKING_1_8_N && cookingId <= COOKING_1_14_N // 魚子醬小點 // 鱷魚牛排 // 龜龍點心 // 奇異果鸚鵡烤肉 // 蠍子烤肉 // 伊萊克卡頓燉菜
					|| cookingId >= COOKING_1_8_S && cookingId <= COOKING_1_14_S) { // 二階料理
				pc.setCookingId(cookingId);
			} else if (cookingId == COOKING_1_15_N || cookingId == COOKING_1_15_S || cookingId == ApprenticeChickenSoup) { // 蟹肉湯
				pc.setDessertId(cookingId);
			}

			/** 賦予三階料理效果（檢查重複性） */
			else if (cookingId >= COOKING_1_16_N && cookingId <= COOKING_1_22_N || cookingId >= COOKING_1_16_S && cookingId <= COOKING_1_22_S || cookingId == COOK_STR_Bless || cookingId == COOK_DEX_Bless
					|| cookingId == COOK_INT_Bless || cookingId >= COOK_STR && cookingId <= COOK_INT || cookingId == AdenSpecialSteak || cookingId == AdenSpecialCanape || cookingId == AdenSpecialSalad || cookingId == BlessedAdenSpecialSalad
					|| cookingId == BlessedAdenSpecialSteak || cookingId == BlessedAdenSpecialCanape) {
				pc.setCookingId(cookingId);
			} else if (cookingId == COOKING_1_23_N || cookingId == COOKING_1_23_S || cookingId == COOK_GROW || cookingId == COOK_GROW_Bless || cookingId == CheonhaJangsaBuff || cookingId == MetisSeongseongSoup || cookingId == AdenTomatoSoup
					|| cookingId == BlessedAdenTomatoSoup) {
				pc.setDessertId(cookingId);
			}

			/** 特殊料理/湯（檢查重複性） **/

		else if (cookingId == MetisSeongseongCooking) {
				pc.setCookingId(cookingId);
			} else if (cookingId == MetisSeongseongSoup) {
				pc.setDessertId(cookingId);
			}

		pc.sendPackets(new S_OwnCharStatus(pc));
	}

}