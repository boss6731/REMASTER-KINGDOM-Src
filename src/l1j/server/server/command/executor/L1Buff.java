package l1j.server.server.server.command.executor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import l1j.server.server.model.L1Object;
import l1j.server.server.server.datatables.SkillsTable;
import l1j.server.server.model.L1World;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Skills;

public abstract class L1Buff implements l1j.server.server.command.executor.L1CommandExecutor {

	private L1Buff() {  }

	public static l1j.server.server.command.executor.L1CommandExecutor getInstance() {
		L1Buff l1Buff = new L1Buff() {
			@Override
			public void execute(L1Object pc, String cmdName, String arg) {

			}
		};
		return l1Buff;
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {

	}

	@Override
	public void execute(L1Object pc, String cmdName, String arg) {
		try {
			StringTokenizer tok = new StringTokenizer(arg);
			Collection<L1PcInstance> players = null;
			String s = tok.nextToken();
			if (s.equals("我")) {
				players = new ArrayList<L1PcInstance>();
				players.add(pc);
				s = tok.nextToken();
			} else if (s.equals("全部")) {
				players = L1World.getInstance(). getAllPlayers();
				s = tok.nextToken();
			} else {
				players = L1World.getInstance(). getVisiblePlayer(pc);
			}

			int skillId = Integer.parseInt(s);
			int time = 0;
			if (tok.hasMoreTokens()) {
				time = Integer.parseInt(tok.nextToken());
			}

			L1Skills skill = SkillsTable.getInstance(). getTemplate(skillId);

			if (skill.getTarget(). equals("buff")) {
				for (L1PcInstance tg : players) {
					//new L1SkillUse(). handleCommands(pc, skillId, tg.getId(), tg.getX(), tg.getY(), null, time, L1SkillUse.TYPE_SPELLSC);
					new L1SkillUse(). handleCommands(tg, skillId, tg.getId(), tg.getX(), tg.getY(), null, time, L1SkillUse.TYPE_GMBUFF);
				}
			} else if (skill.getTarget(). equals("none")) {
				for (L1PcInstance tg : players) {
					new L1SkillUse(). handleCommands(tg, skillId, tg.getId(), tg.getX(), tg.getY(), null, time, L1SkillUse.TYPE_GMBUFF);
				}
			} else {
				pc.sendPackets(String.valueOf(new S_SystemMessage("這不是一個有效的增益技能。")));
			}
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " 請輸入 [全部, 我] [技能ID] [時間]。")));
		}
	}
}
