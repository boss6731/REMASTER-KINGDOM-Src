package MJShiftObject.Battle;

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


public class MJShiftBattleMessage {
	public static final String DBNAME_THEBES = "server_battle_message_thebes";
	public static final String DBNAME_DOM_TOWER = "server_battle_message_domtower";
	public static final String DBNAME_FOR_ISLAND = "server_battle_message_forisland";

	public static void do_test(final L1PcInstance pc, final String table_name, final boolean is_auto_sequence_message) {
		GeneralThreadPool.getInstance().execute(new Runnable() {
			public void run() {
				MJShiftBattleMessage message = new MJShiftBattleMessage(table_name, is_auto_sequence_message);
				int size = message.m_messages.size();
				for (int i = 0; i < size; i++) {
					MJShiftBattleMessage.MessageInfo mInfo = message.m_messages.get(i);
					ProtoOutputStream stream = SC_NOTIFICATION_MESSAGE.make_stream(mInfo.message, mInfo.rgb, mInfo.duration);
					pc.sendPackets(stream);
					try {
						Thread.sleep(1000L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				pc.sendPackets(SC_NOTIFICATION_MESSAGE.make_stream("訊息測試已完成。如有其他問題或需要進一步協助，請隨時告知。", MJSimpleRgb.green(), 10));
			}
		});
	}

	private ArrayList<MessageInfo> m_messages;
	private int m_index;
	private boolean m_is_auto_sequence_message;

	public MJShiftBattleMessage(String table_name, boolean is_auto_sequence_message) {
		this.m_index = -1;
		this.m_is_auto_sequence_message = is_auto_sequence_message;
		this.m_messages = new ArrayList<>();
		Selector.exec(String.format("select message, rgb, duration from %s", new Object[]{table_name}), (SelectorHandler) new FullSelectorHandler() {
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					MJShiftBattleMessage.MessageInfo mInfo = new MJShiftBattleMessage.MessageInfo();
					mInfo.message = rs.getString("message");
					mInfo.rgb = MJSimpleRgb.from_string(rs.getString("rgb"));
					mInfo.duration = rs.getInt("duration");
					MJShiftBattleMessage.this.m_messages.add(mInfo);
				}
			}
		});
	}

	public void set_is_auto_sequence_message(boolean is_auto_sequence_message) {
		this.m_is_auto_sequence_message = is_auto_sequence_message;
	}

	public boolean get_is_auto_sequence_message() {
		return this.m_is_auto_sequence_message;
	}

	public MessageInfo next_message() {
		int size = this.m_messages.size();
		if (size <= 0) {
			return null;
		}
		return this.m_is_auto_sequence_message ? this.m_messages
				.get(++this.m_index % size) : this.m_messages
				.get(MJRnd.next(size));
	}

	public class MessageInfo {
		public String message;
		public MJSimpleRgb rgb;
		public int duration;

		public ProtoOutputStream create_stream() {
			return SC_NOTIFICATION_MESSAGE.make_stream(this.message, this.rgb, this.duration);
		}
	}

}


