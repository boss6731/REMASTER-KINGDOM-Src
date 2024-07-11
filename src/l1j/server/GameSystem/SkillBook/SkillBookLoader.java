package l1j.server.GameSystem.SkillBook;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.MJPassiveSkill.MJPassiveInfo;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_AVAILABLE_SPELL_NOTI;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.templates.L1Skills;

public class SkillBookLoader {
	private static SkillBookLoader _instance;

	public static SkillBookLoader getInstance() {
		if (_instance == null) {
			_instance = new SkillBookLoader();
		}
		return _instance;
	}

	public static void release() {
		if (_instance != null) {
			_instance.dispose();
			_instance = null;
		}
	}

	public static void reload() {
		SkillBookLoader old = _instance;
		_instance = new SkillBookLoader();
		if (old != null) {
			old.dispose();
			old = null;
		}
	}

	private HashMap<Integer, SkillBookInfo> skill_book;

	private SkillBookLoader() {
		skill_book = load();
	}

	private boolean _master = false;

	public void setMaster(boolean flag) {
		_master = flag;
	}

	public boolean isMaster() {
		return _master;
	}

	private HashMap<Integer, SkillBookInfo> load() {
		HashMap<Integer, SkillBookInfo> skills = new HashMap<Integer, SkillBookInfo>();
		Selector.exec("select * from skill_book_mapped", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					try {
						SkillBookInfo sInfo = SkillBookInfo.newInstance(rs);
						// skills.put(sInfo.getSkillId(), sInfo);
						skills.put(sInfo.getSkillBookId(), sInfo);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		return skills;
	}

	public SkillBookInfo fromSkillBookId(int skillBookId) {
		return skill_book.get(skillBookId);
	}

	public ArrayList<SkillBookInfo> fromClassType(int type) {
		ArrayList<SkillBookInfo> skills = values();
		if (skills == null) {
			return null;
		}
		ArrayList<SkillBookInfo> result = new ArrayList<SkillBookInfo>();
		for (SkillBookInfo sInfo : skills) {
			if (sInfo.getClassType() == type) {
				result.add(sInfo);
			}
		}
		return result;
	}

	public ArrayList<SkillBookInfo> values() {
		return new ArrayList<SkillBookInfo>(skill_book.values());
	}

	private static final String[] CLASSE_NAMES = new String[] { "王族", "騎士", "妖精", "法師", "黑暗妖精", "龍騎士", "幻術師", "戰士",
			"劍士", "黃金槍騎" };

	public boolean masterSkill(L1PcInstance pc, SkillBookInfo sInfo, boolean temp) {
		checkskillstage(pc);
		int level = sInfo.getUseMinLevel();
		if (!pc.isTempSkillActive(sInfo.getSkillId())) {
			if (pc.isSkillMastery(sInfo.getSkillId())) {
				pc.sendPackets("已經學會的魔法.");
				return false;
			}
		}
		if (sInfo.isNormalMagic()) {
			switch (pc.getClassNumber()) {
				case 0:
				case 4:
				case 8:
				case 5:
				case 9:
					level = level / 8 * 15;
					break;
				case 1:
				case 7:
					level = level / 8 * 50;
					break;
				case 2:
					level = level / 8 * 10;
					break;
				default:
					break;
			}
			
			if (skillstage < sInfo.getSkillStep()) {
				if (!isMaster()) {
					pc.sendPackets("無法學習該技能。");
				}
				return false;
			}
			
			if (pc.getLevel() < level) {
				if (!isMaster()) {
					pc.sendPackets(String.format("從 %d 級開始可以使用。", sInfo.getUseMinLevel()));
					return false;
				}
			} else {
				if (pc.getLevel() < sInfo.getUseMinLevel()) {
					if (!isMaster()) {
						pc.sendPackets(String.format("從 %d 級開始可以使用。", sInfo.getUseMinLevel()));
						return false;
					}
				}
				if (pc.getClassNumber() != sInfo.getClassType()) {
					pc.sendPackets(String.format("該技能只有 %s 可以學習。", CLASSE_NAMES[sInfo.getClassType()]));
					return false;
				}
			}
		if (temp) {
			
		} else {
			SC_AVAILABLE_SPELL_NOTI noti = SC_AVAILABLE_SPELL_NOTI.newInstance();
			noti.appendNewSpell(sInfo.getSkillId(), true);
			pc.sendPackets(noti, MJEProtoMessages.SC_AVAILABLE_SPELL_NOTI, true);
			S_SkillSound s_skillSound = new S_SkillSound(pc.getId(), sInfo.isLawful() ? 224 : 231);
			pc.sendPackets(s_skillSound);
			pc.broadcastPacket(s_skillSound);
			L1Skills l1skills = null;
			l1skills = SkillsTable.getInstance().getTemplate(sInfo.getSkillId());
			String skillname = l1skills.getName();
			SkillsTable.getInstance().spellMastery(pc.getId(), sInfo.getSkillId(), skillname, 0, 0);
		}
		return true;
	}

	private int skillstage = 0;

	private void checkskillstage(L1PcInstance pc) {
		int classtype = pc.getClassNumber();
		switch (classtype) {
			case 0: // 王族
				skillstage = 2;
				break;
			case 1: // 騎士
				skillstage = 1;
				break;
			case 2: // 妖精
				skillstage = 6;
				break;
			case 3: // 法師
				skillstage = 11;
				break;
			case 4: // 黑暗妖精
				skillstage = 2;
				break;
			case 5: // 龍騎士
				skillstage = 2;
				break;
			case 6: // 幻術師
				skillstage = 0;
				break;
			case 7: // 戰士
				skillstage = 1;
				break;
			case 8: // 劍士
				skillstage = 2;
				break;
			case 9: // 黃金槍騎
				skillstage = 2;
				break;
		}

	}

	public boolean useItem(L1PcInstance pc, L1ItemInstance item) {
		SkillBookInfo sInfo = fromSkillBookId(item.getItemId());
		if (sInfo == null) {
			return false;
		}
		if (masterSkill(pc, sInfo, false)) {
			pc.getInventory().removeItem(item, 1);
		}
		return true;
	}

	public void dispose() {

	}
}
