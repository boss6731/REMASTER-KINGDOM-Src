package l1j.server.server.model.Instance;

import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.server.model.L1Attack;
import l1j.server.server.serverpackets.S_ChangeHeading;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.CalcExp;

public class L1ScarecrowInstance extends L1NpcInstance {

	private static final long serialVersionUID = 1L;

	public L1ScarecrowInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance player) {
		L1Attack attack = new L1Attack(player, this);
		/*if (player.isInParty() || player.isDead()) {
    	player.sendPackets("\f3組隊中無法攻擊稻草人。");
    	return;
		}*/
		boolean is_hit = attack.calcHit();
		if (is_hit && player.getAI() == null) {
			if (player.getLevel() < Config.ServerAdSetting.ScareLevel && !player.noPlayerCK) {
				ArrayList<L1PcInstance> targetList = new ArrayList<L1PcInstance>();
				targetList.add(player);
				ArrayList<Integer> hateList = new ArrayList<Integer>();
				hateList.add(1);
				CalcExp.calcExp(player, getId(), targetList, hateList, 8);
			}
			if (player.getLevel() >= 1 && !player.noPlayerCK) {// 탐지급 레벨
				if (player != null) {
					player.getInventory().storeItem(41302, Config.ServerAdSetting.tamsc1);
					player.getInventory().storeItem(40308, Config.ServerAdSetting.tamsc2);

					if (MJRnd.isWinning(1000000, Config.ServerAdSetting.SCAEVENTITEMCHANCE)) {
						player.getInventory().storeItem(Config.ServerAdSetting.SCAEVENTITEM, 1);
					} else if (MJRnd.isWinning(1000000, Config.ServerAdSetting.SCAEVENTITEMCHANCE1)) {
						player.getInventory().storeItem(Config.ServerAdSetting.SCAEVENTITEM1, 1);
					} else if (MJRnd.isWinning(1000000, Config.ServerAdSetting.SCAEVENTITEMCHANCE2)) {
						player.getInventory().storeItem(Config.ServerAdSetting.SCAEVENTITEM2, 1);
					}
					player.sendPackets(new S_NewCreateItem(S_NewCreateItem.TAM_POINT, player.getNetConnection()), true);// 탐지급
					player.getNetConnection().getAccount().tam_point += Config.ServerAdSetting.tamsc;// 탐지급갯수
					player.getNetConnection().getAccount().updateTam();// 탐업뎃
				}
			}
			int dmg = attack.calcDamage();
			if (this.getNpcId() == 7320088) {
				player.sendPackets(new S_SystemMessage("물리대미지: [" + dmg + "]"));
			}
			if (getHeading() < 7) {
				setHeading(getHeading() + 1);
			} else {
				setHeading(0);
			}
			broadcastPacket(new S_ChangeHeading(this));
		}
		attack.action();
	}

	@Override
	public void onTalkAction(L1PcInstance l1pcinstance) {}
	public void onFinalAction() {}
	public void doFinalAction() {}
}
