package l1j.server.ExpMerge;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJClassesType.MJEClassesType;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.model.Instance.L1PcInstance;

class ExpMergeDatabaseService {
	private static final int INVALID_MERGE_COUNT = 256;

	private ExpMergeController controller;

	ExpMergeDatabaseService(ExpMergeController controller) {
		this.controller = controller;
	}

	void increaseMerge(final L1PcInstance pc) {
		Updator.exec("update characters set exp_merge_count=exp_merge_count+1 where objid=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, pc.getId());
			}
		});
	}

	void initializeMergeTarget(final ExpMergeTargetModel targetModel) {
		Updator.exec(
				"update characters set level=1, highLevel=1, Exp=0, Str=BaseStr, Con=BaseCon, Dex=BaseDex, Cha=BaseCha, Intel=BaseIntel, Wis=BaseWis, ReturnStat=1 where objid=?",
				new Handler() {
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						pstm.setInt(1, targetModel.objectId);
					}
				});
	}

	boolean possibleMerge(final L1PcInstance pc) {
		final MJObjectWrapper<Integer> wrapper = new MJObjectWrapper<>();
		wrapper.value = INVALID_MERGE_COUNT;
		Selector.exec("select exp_merge_count from characters where objid=? limit 1", new SelectorHandler() {

			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, pc.getId());
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				if (rs.next()) {
					wrapper.value = rs.getInt("exp_merge_count");
				}
			}
		});

		return possibleMerge0(pc, wrapper.value);
	}

	private boolean possibleMerge0(final L1PcInstance pc, final int mergeredCount) {
		if (mergeredCount == INVALID_MERGE_COUNT) {
			pc.sendPackets("目前無法使用.");
			return false;
		}

		ExpMergeSystemModel model = controller.systemModel();
		if (model == null) {
			pc.sendPackets("目前系統已被管理員暫停.");
			return false;
		}

		if (pc.getLevel() >= model.mergeTargetMaxLevel) {
			pc.sendPackets("當前角色的等級過高，無法使用.");
			return false;
		}

		if (mergeredCount >= model.mergeLimitCount) {
			pc.sendPackets(String.format("%s 您已超過經驗值合併使用次數限制。(共 %d 次)", pc.getName(), model.mergeLimitCount));
			return false;
		}
		return true;
	}

	ExpMergeTargetModel selectMergeTarget(int targetObjectId) {
		final MJObjectWrapper<ExpMergeTargetModel> wrapper = new MJObjectWrapper<>();
		wrapper.value = null;
		Selector.exec(
				"select char_name, level, Exp, Class, BaseStr, BaseCon, BaseDex, BaseCha, BaseIntel, BaseWis, ElixirStatus from characters where objid=? limit 1",
				new SelectorHandler() {
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						pstm.setInt(1, targetObjectId);
					}

					@Override
					public void result(ResultSet rs) throws Exception {
						if (rs.next()) {
							ExpMergeTargetModel model = new ExpMergeTargetModel();
							model.objectId = targetObjectId;
							model.name = rs.getString("char_name");
							model.level = rs.getInt("level");
							model.exp = rs.getInt("Exp");
							model.classType = MJEClassesType.fromGfx(rs.getInt("Class"));
							wrapper.value = model;
						}
					}
				});
		return wrapper.value;
	}

	boolean possibleMergeTarget(final L1PcInstance pc, final ExpMergeTargetModel targetModel) {
		ExpMergeSystemModel model = controller.systemModel();
		if (model == null) {
			pc.sendPackets("目前系統已被管理員暫時停用.");
			return false;
		}

		if (targetModel.level < model.mergeTargetMinLevel) {
			pc.sendPackets(String.format("角色 %s 的等級過低。（最低等級為 %d 級）", pc.getName(), model.mergeTargetMinLevel));
			return false;
		}
		if (targetModel.level > model.mergeTargetMaxLevel) {
			pc.sendPackets(String.format("角色 %s 的等級過高。（最高等級為 %d 級）", pc.getName(), model.mergeTargetMaxLevel));
			return false;
		}
		return true;
	}

}
