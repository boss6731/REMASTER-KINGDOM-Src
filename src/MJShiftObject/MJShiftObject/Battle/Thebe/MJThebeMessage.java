package MJShiftObject.Battle.Thebe;

import MJShiftObject.Battle.MJShiftBattleArgs;

import java.sql.ResultSet;
import java.util.ArrayList;

import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJThebeMessage {
	public static void do_message_test(final L1PcInstance pc) {
		GeneralThreadPool.getInstance().execute(new Runnable() {
			public void run() {
				MJThebeMessage message = new MJThebeMessage();
				int size = message.m_messages.size();
				for (int i = 0; i < size; i++) {
					MJThebeMessage.MessageInfo mInfo = message.m_messages.get(i);
					ProtoOutputStream stream = SC_NOTIFICATION_MESSAGE.make_stream(mInfo.message, mInfo.rgb, mInfo.duration);
					pc.sendPackets(stream);
					try {
						Thread.sleep(1000L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				pc.sendPackets(SC_NOTIFICATION_MESSAGE.make_stream("訊息測試完成.", MJSimpleRgb.green(), 10));
			}
		});
	}

	private ArrayList<MessageInfo> m_messages;

	public MJThebeMessage() {
		this.m_index = -1;
		this.m_messages = new ArrayList<>();
		Selector.exec("select message, rgb, duration from server_battle_message_thebes", (SelectorHandler) new FullSelectorHandler() {
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					MJThebeMessage.MessageInfo mInfo = new MJThebeMessage.MessageInfo();
					mInfo.message = rs.getString("message");
					mInfo.rgb = MJSimpleRgb.from_string(rs.getString("rgb"));
					mInfo.duration = rs.getInt("duration");
					MJThebeMessage.this.m_messages.add(mInfo);
				}
			}
		});
	}

	private int m_index;

	public MessageInfo next_message() {
		int size = this.m_messages.size();
		if (size <= 0) {
			return null;
		}
		return MJShiftBattleArgs.THEBE_IS_AUTO_SEQUENCE_MESSAGE ? this.m_messages
				.get(++this.m_index % size) : this.m_messages
				.get(MJRnd.next(size));
	}

	public class MessageInfo {
		public String message;
		public MJSimpleRgb rgb;
		public int duration;
	}
}


