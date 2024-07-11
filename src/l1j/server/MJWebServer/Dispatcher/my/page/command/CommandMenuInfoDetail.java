package l1j.server.MJWebServer.Dispatcher.my.page.command;

import l1j.server.MJTemplate.MJClassesType.MJEClassesType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.MJWebServer.Dispatcher.my.page.command.MJMyPageGmCommandModel.MyGmCommandDataModel;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharDetailInfo;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyCharService;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.Ability;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Resistance;
import l1j.server.server.model.Instance.L1PcInstance;

class CommandMenuInfoDetail implements MJMyPageGmCommand{

	@Override
	public MJMyPageGmCommandModel execute(String command, String targetAccount, String targetCharacter, String parameter0) {
		MJMyCharDetailInfo cInfo = MJMyCharService.service().fromCharacterNameDetail(targetCharacter);
		if(cInfo == null){
			return new MJMyPageGmCommandModel(String.format("%s ĳ���͸� ã�� �� �����ϴ�.", targetCharacter)); 
		}
		
		MJMyPageGmCommandModel model = new MJMyPageGmCommandModel();
		model.title = String.format("%s ���� ����", targetCharacter);
		model.dataItems.add(new MyGmCommandDataModel("effect", "ĳ����", targetCharacter));
		model.dataItems.add(new MyGmCommandDataModel("desc", "����", targetAccount));
		model.dataItems.add(new MyGmCommandDataModel("desc", "������Ʈ", cInfo.objectId));
		model.dataItems.add(new MyGmCommandDataModel("desc", "����", cInfo.gm ? "���" : "�Ϲ�����"));
		model.dataItems.add(new MyGmCommandDataModel("desc", "����", String.format("Lv.%d %.4f%%", cInfo.level, ExpTable.getExpPercentagedouble(cInfo.level, (int)cInfo.exp))));
		model.dataItems.add(new MyGmCommandDataModel("desc", "����", cInfo.gender.equals("f") ? "��" : "��"));
		model.dataItems.add(new MyGmCommandDataModel("desc", "Ŭ����", MJEClassesType.fromInt(cInfo.characterClass).text()));
		model.dataItems.add(new MyGmCommandDataModel("desc", "����", cInfo.pledge));
		model.dataItems.add(new MyGmCommandDataModel("desc", "������", cInfo.birth));
		model.dataItems.add(new MyGmCommandDataModel("desc", "��ŷ", String.format("Ŭ����:%d, ��ü:%d", cInfo.classRank, cInfo.totalRank)));
		model.dataItems.add(new MyGmCommandDataModel("desc", "pk", cInfo.pk));
		model.dataItems.add(new MyGmCommandDataModel("desc", "����", cInfo.lawful));
		L1PcInstance pc = L1World.getInstance().getPlayer(targetCharacter);
		if(pc != null){
			Resistance resistance = pc.getResistance();
			Ability ability = pc.getAbility();
			model.dataItems.add(new MyGmCommandDataModel("desc", "�ູ �Ҹ� ȿ��", pc.getEinhasadBlessper()));		
			model.dataItems.add(new MyGmCommandDataModel("desc", "������ ����ġ", pc.get_item_exp_bonus()));		
			model.dataItems.add(new MyGmCommandDataModel("desc", "AC", pc.getAC().getAc()));		
			model.dataItems.add(new MyGmCommandDataModel("desc", "����", resistance.getMr()));
			model.dataItems.add(new MyGmCommandDataModel("desc", "��ƽ/��ƽ", String.format("%d / %d", pc.getHpr(), pc.getMpr())));
			model.dataItems.add(new MyGmCommandDataModel("desc", "hp", String.format("%d / %d", pc.getCurrentHp(), pc.getMaxHp())));
			model.dataItems.add(new MyGmCommandDataModel("desc", "mp", String.format("%d / %d", pc.getCurrentMp(), pc.getMaxMp())));
			model.dataItems.add(new MyGmCommandDataModel("desc", "�Ӽ����", String.format("fire:%d%%, water:%d%%, wind:%d%%, earth%d%%", 
					resistance.getFire(), resistance.getWater(), resistance.getWind(), resistance.getEarth())));
			model.dataItems.add(new MyGmCommandDataModel("desc", "����", String.format("���:%d, ����:%d, ���:%d, ����%d, ��ü:%d", 
					pc.getSpecialResistance(eKind.ABILITY), 
					pc.getSpecialResistance(eKind.SPIRIT), 
					pc.getSpecialResistance(eKind.DRAGON_SPELL), 
					pc.getSpecialResistance(eKind.FEAR),
					pc.getSpecialResistance(eKind.ALL))));
			model.dataItems.add(new MyGmCommandDataModel("desc", "����", String.format("���:%d, ����:%d, ���:%d, ����%d, ��ü:%d", 
					pc.getSpecialPierce(eKind.ABILITY), 
					pc.getSpecialPierce(eKind.SPIRIT), 
					pc.getSpecialPierce(eKind.DRAGON_SPELL), 
					pc.getSpecialPierce(eKind.FEAR),
					pc.getSpecialPierce(eKind.ALL))));
			
			model.dataItems.add(new MyGmCommandDataModel("desc", "str", String.format("%d(%d)", cInfo.str, ability.getTotalStr())));
			model.dataItems.add(new MyGmCommandDataModel("desc", "dex", String.format("%d(%d)", cInfo.dex, ability.getTotalDex())));
			model.dataItems.add(new MyGmCommandDataModel("desc", "con", String.format("%d(%d)", cInfo.con, ability.getTotalCon())));
			model.dataItems.add(new MyGmCommandDataModel("desc", "wis", String.format("%d(%d)", cInfo.wis, ability.getTotalWis())));
			model.dataItems.add(new MyGmCommandDataModel("desc", "int", String.format("%d(%d)", cInfo.intel, ability.getTotalInt())));
			model.dataItems.add(new MyGmCommandDataModel("desc", "cha", String.format("%d(%d)", cInfo.cha, ability.getTotalCha())));
			model.dataItems.add(new MyGmCommandDataModel("desc", "�����", String.format("�ٰŸ�:%d/%d, ���Ÿ�:%d/%d", pc.getDmgup(), pc.getDmgRate(), pc.getBowDmgup(), pc.getBowDmgRate())));
			model.dataItems.add(new MyGmCommandDataModel("desc", "����", String.format("�ٰŸ�:%d/%d, ���Ÿ�:%d/%d", pc.getHitup(), pc.getHitRate(), pc.getBowHitup(), pc.getBowHitRate())));
			model.dataItems.add(new MyGmCommandDataModel("desc", "ġ��Ÿ", String.format("�ٰŸ�:%d, ���Ÿ�:%d, ����:%d/%d", pc.get_melee_critical_rate(), pc.get_missile_critical_rate(), pc.getBaseMagicCritical(), pc.get_magic_critical_rate())));
			model.dataItems.add(new MyGmCommandDataModel("desc", "PVP", String.format("�����:%d, ������:%d", resistance.getPVPweaponTotalDamage(), resistance.getcalcPcDefense())));
			model.dataItems.add(new MyGmCommandDataModel("desc", "sp/er", String.format("sp:%d / er:%d", ability.getSp(), pc.getTotalER())));
			model.dataItems.add(new MyGmCommandDataModel("desc", "������", String.format("����:%d, ����:%d", pc.getDamageReductionByArmor(), pc.getDamageReductionIgnore())));
		}else{
			model.dataItems.add(new MyGmCommandDataModel("desc", "hp", String.format("%d / %d", cInfo.curHp, cInfo.maxHp)));
			model.dataItems.add(new MyGmCommandDataModel("desc", "mp", String.format("%d / %d", cInfo.curMp, cInfo.maxMp)));
			model.dataItems.add(new MyGmCommandDataModel("desc", "str", cInfo.str));
			model.dataItems.add(new MyGmCommandDataModel("desc", "dex", cInfo.dex));
			model.dataItems.add(new MyGmCommandDataModel("desc", "con", cInfo.con));
			model.dataItems.add(new MyGmCommandDataModel("desc", "wis", cInfo.wis));
			model.dataItems.add(new MyGmCommandDataModel("desc", "int", cInfo.intel));
			model.dataItems.add(new MyGmCommandDataModel("desc", "cha", cInfo.cha));
		}
		return model;
	}

}
