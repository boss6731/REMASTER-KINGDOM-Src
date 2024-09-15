package MJShiftObject.Battle;

import java.sql.ResultSet;
import java.util.ArrayList;

import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1PcInstance;

// thebes = server_battle_message_thebes

public class MJShiftBattleMessage {
	public static final String DBNAME_THEBES = "server_battle_message_thebes";
	public static final String DBNAME_DOM_TOWER = "server_battle_message_domtower";
	public static final String DBNAME_FOR_ISLAND = "server_battle_message_forisland";
	
	public static void do_test(final L1PcInstance pc, String table_name, boolean is_auto_sequence_message){
		GeneralThreadPool.getInstance().execute(new Runnable(){
			@Override
			public void run(){
				MJShiftBattleMessage message = new MJShiftBattleMessage(table_name, is_auto_sequence_message);
				int size = message.m_messages.size();
				for(int i=0; i<size; ++i){
					MessageInfo mInfo = message.m_messages.get(i);
					ProtoOutputStream stream = SC_NOTIFICATION_MESSAGE.make_stream(mInfo.message, mInfo.rgb, mInfo.duration);
					pc.sendPackets(stream);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				pc.sendPackets(SC_NOTIFICATION_MESSAGE.make_stream("訊息測試已完成。", MJSimpleRgb.green(), 10));
                return new L1PcInstance[0];
            }
		});
	}
	
	private ArrayList<MessageInfo> m_messages;
	private int m_index;
	private boolean m_is_auto_sequence_message;
	public MJShiftBattleMessage(String table_name, boolean is_auto_sequence_message){
		m_index = -1;
		m_is_auto_sequence_message = is_auto_sequence_message;
		m_messages = new ArrayList<MessageInfo>();
		Selector.exec(String.format("select message, rgb, duration from %s", table_name), new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					MessageInfo mInfo = new MessageInfo();
					mInfo.message = rs.getString("message");
					mInfo.rgb = MJSimpleRgb.from_string(rs.getString("rgb"));
					mInfo.duration = rs.getInt("duration");
					m_messages.add(mInfo);
				}
			}
		});
	}
	
	public void set_is_auto_sequence_message(boolean is_auto_sequence_message){
		m_is_auto_sequence_message = is_auto_sequence_message;
	}
	public boolean get_is_auto_sequence_message(){
		return m_is_auto_sequence_message;
	}
	public MessageInfo next_message(){
		int size = m_messages.size();
		if(size <= 0)
			return null;
		
		return m_is_auto_sequence_message ?
			m_messages.get((++m_index) % size) :
			m_messages.get(MJRnd.next(size));
	}
	public class MessageInfo{
		public String message;
		public MJSimpleRgb rgb;
		public int duration;
		
		public ProtoOutputStream create_stream(){
			return SC_NOTIFICATION_MESSAGE.make_stream(message, rgb, duration);
		}
	}
}
