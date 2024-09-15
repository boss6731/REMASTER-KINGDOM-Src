package l1j.server.Beginner.View;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_QUEST_REVEAL_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_QUEST_REVEAL_ACK.eResultCode;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJBeginnerRevealView implements MJBeginnerView {
	private L1PcInstance pc;
	private int questId;

	MJBeginnerRevealView(L1PcInstance pc, int questId) {
		this.pc = pc;
		this.questId = questId;
	}

	public L1PcInstance pc() {
		return pc;
	}

	public int questId() {
		return questId;
	}

	private void viewInternal(eResultCode resultCode) {
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			//@override
			public void run() {
				SC_QUEST_REVEAL_ACK ack = SC_QUEST_REVEAL_ACK.newInstance();
				ack.set_id(questId);
				ack.set_result(resultCode);

				// 發送任務揭示封包
				pc().sendPackets(ack, MJEProtoMessages.SC_QUEST_REVEAL_ACK, false);
                return new L1PcInstance[0];
            }
		}, 10L);
	}


	public void onSuccess() {
		viewInternal(eResultCode.SUCCESS);
	}

	public void onFail() {
		viewInternal(eResultCode.FAIL);
	}

	public void onAlreadyRevealed() {
		viewInternal(eResultCode.FAIL_ALREADY_REVEALED);
	}

	public void onAlreadyStarted() {
		viewInternal(eResultCode.FAIL_ALREADY_STARTED);
	}

	public void onAlreadyFinished() {
		viewInternal(eResultCode.FAIL_ALREADY_FINISHED);
	}

	public void onObsolete() {
		viewInternal(eResultCode.FAIL_OBSOLETE);
	}

	public class MJBeginnerRevealView implements MJBeginnerView {
		private L1PcInstance pc;
		private int questId;

		MJBeginnerRevealView(L1PcInstance pc, int questId) {
			this.pc = pc;
			this.questId = questId;
		}

		public L1PcInstance pc() {
			return pc;
		}

		public int questId() {
			return questId;
		}

		private void viewInternal(eResultCode resultCode) {
			GeneralThreadPool.getInstance().schedule(new Runnable() {
				//@override
				public void run() {
					SC_QUEST_REVEAL_ACK ack = SC_QUEST_REVEAL_ACK.newInstance();
					ack.set_id(questId);
					ack.set_result(resultCode);

					// 發送任務揭示封包
					pc().sendPackets(ack, MJEProtoMessages.SC_QUEST_REVEAL_ACK, false);
                    return new L1PcInstance[0];
                }
			}, 10L);
		}

		static class MJBeginnerRevealDevelopView extends MJBeginnerRevealView {
			MJBeginnerRevealDevelopView(L1PcInstance pc, int questId) {
				super(pc, questId);
			}

			//@override
			public void onSuccess() {
				System.out.println(String.format("MJBeginnerRevealView -> 成功. 任務ID: %d", questId()));
				super.onSuccess();
			}

			//@override
			public void onFail() {
				System.out.println(String.format("MJBeginnerRevealView -> 失敗. 任務ID: %d", questId()));
				super.onFail();
			}

			//@override
			public void onAlreadyRevealed() {
				System.out.println(String.format("MJBeginnerRevealView -> 已揭示. 任務ID: %d", questId()));
				super.onAlreadyRevealed();
			}

			//@override
			public void onAlreadyStarted() {
				System.out.println(String.format("MJBeginnerRevealView -> 已開始. 任務ID: %d", questId()));
				super.onAlreadyStarted();
			}

			//@override
			public void onAlreadyFinished() {
				System.out.println(String.format("MJBeginnerRevealView -> 已完成. 任務ID: %d", questId()));
				super.onAlreadyFinished();
			}

			//@override
			public void onObsolete() {
				System.out.println(String.format("MJBeginnerRevealView -> 已過期. 任務ID: %d", questId()));
				super.onObsolete();
			}
		}
	}
}