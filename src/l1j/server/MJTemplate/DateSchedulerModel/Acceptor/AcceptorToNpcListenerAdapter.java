package l1j.server.MJTemplate.DateSchedulerModel.Acceptor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import l1j.server.MJActionListener.ActionListener;
import l1j.server.MJActionListener.Npc.NpcActionListener;
import l1j.server.MJActionListener.Npc.TeleporterActionListener;
import l1j.server.MJTemplate.Chain.Action.MJITeleportHandler;
import l1j.server.MJTemplate.Chain.Action.MJTeleportChain;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;

public class AcceptorToNpcListenerAdapter extends AbstractDateAcceptor implements ActionListener, MJITeleportHandler {

	/**
	 * 解析剩餘時間類型字符串並返回對應的 Calendar 常量。
	 * @param s 剩餘時間類型字符串 ("시", "분", "초")
	 * @return 對應的 Calendar 常量
	 * @throws IllegalArgumentException 如果給定的類型無效
	 */
	public static int parse_remain_type(String s) {
		switch (s) {
			case "小時":
				return Calendar.HOUR_OF_DAY;
			case "分鐘":
				return Calendar.MINUTE;
			case "秒":
				return Calendar.SECOND;
			default:
				throw new IllegalArgumentException(String.format("invalid remain type %s", s));
		}
	}
}
	
	private static Integer[] parse_opened_date(String s){
		return (Integer[])MJArrangeParser.parsing(s, ",", MJArrangeParseeFactory.createIntArrange()).result();
	}
	
	public static AcceptorToNpcListenerAdapter newInstance(NpcActionListener listener, ResultSet rs) throws SQLException{
		AcceptorToNpcListenerAdapter adapter = (AcceptorToNpcListenerAdapter) newInstance()
				.set_opened_message(rs.getString("opened_message"))
				.set_closed_message(rs.getString("closed_message"))
				.setActionListener(listener)
				.set_is_opened(false)
				.add_include_day_week(parse_opened_date(rs.getString("opened_date")))
				.set_opened_time(rs.getInt("opened_hour"), rs.getInt("opened_minute"), rs.getInt("opened_second"))
				.set_remain_time(rs.getInt("remain_time"))
				.set_remain_type(parse_remian_type(rs.getString("remain_type")))
				.do_register();
		
		if(listener instanceof TeleporterActionListener) {
			MJTeleportChain.getInstance().add_handler(adapter);
		}
		
		return adapter;
	}
	
	public static AcceptorToNpcListenerAdapter newInstance(){
		return new AcceptorToNpcListenerAdapter();
	}
	
	private NpcActionListener _listener;
	private String	_opened_message;
	private String _closed_message;
	private AcceptorToNpcListenerAdapter(){
		
	}
	
	public AcceptorToNpcListenerAdapter set_opened_message(String opened_message){
		_opened_message = opened_message;
		return this;
	}
	
	public String get_opened_message(){
		return _opened_message;
	}
	
	public AcceptorToNpcListenerAdapter set_closed_message(String closed_message){
		_closed_message = closed_message;
		return this;
	}
	
	public String get_closed_message(){
		return _closed_message;
	}
	
	@Override
	public ActionListener deep_copy(ActionListener listener){
		return ((NpcActionListener) _listener.deep_copy(listener)).set_opened(false);
	}
	
	@Override
	public ActionListener deep_copy(){
		return ((NpcActionListener) _listener.deep_copy()).set_opened(false);
	}
	
	@Override
	public ActionListener drain(ActionListener listener) {
		return _listener.drain(listener);
	}
	
	public AcceptorToNpcListenerAdapter deep_copy(ResultSet rs) throws SQLException{
		AcceptorToNpcListenerAdapter adapter = (AcceptorToNpcListenerAdapter) newInstance()
				.set_opened_message(rs.getString("opened_message"))
				.set_closed_message(rs.getString("closed_message"))
				.setActionListener((NpcActionListener) _listener.deep_copy())
				.set_is_opened(false)
				.add_include_day_week(parse_opened_date(rs.getString("opened_date")))
				.set_opened_time(rs.getInt("opened_hour"), rs.getInt("opened_minute"), rs.getInt("opened_second"))
				.set_remain_time(rs.getInt("remain_time"))
				.set_remain_type(parse_remian_type(rs.getString("remain_type")))
				.do_register();
		
		if(_listener instanceof TeleporterActionListener) {
			MJTeleportChain.getInstance().add_handler(adapter);
		}
		
		return adapter;
	}
	
	protected AcceptorToNpcListenerAdapter setActionListener(NpcActionListener listener){
		_listener = listener;
		return this;
	}
	
	@Override
	public boolean eqauls_action(String action) {
		return _listener.eqauls_action(action);
	}

	@Override
	public String to_action(L1PcInstance pc, L1Object target) {
		return _listener.to_action(pc, target);
	}

	@Override
	public AbstractDateAcceptor do_open(Calendar cal) {
		if(_opened_message != null)
			L1World.getInstance().broadcastPacketToAll(_opened_message);
		return this;
	}

	@Override
	public AbstractDateAcceptor do_close(Calendar cal) {
		if(_listener instanceof TeleporterActionListener){
			// S_SystemMessage 消息 = new S_SystemMessage(_closed_message == null ? String.format("%s已經結束。", ((TeleporterActionListener) _listener).get_destination_name()) : _closed_message);
			final TeleporterActionListener listener = (TeleporterActionListener)_listener;
			final short[] mapIds = listener.get_teleport_map_id();
			
			GeneralThreadPool.getInstance().execute(new Runnable(){
				@Override
				public void run(){
					for(int i=0; i<2; ++i){
						for(L1PcInstance pc : L1World.getInstance().getAllPlayers()){
							if(pc == null || pc.getNetConnection() == null || pc.getNetConnection().is_shift_battle())
								continue;
							
							//if(i == 0)
							//	pc.sendPackets(message, false);
							boolean is_in = false;
							int mid = pc.getMapId();
							for(int mapId : mapIds){
								if(mid == mapId){
									is_in=true;
									break;
								}
							}
							//TODO下面有一座統治之塔，所以需要找到座標..
							if(is_in){
								if(i == 0){
//									int[] loc = Getback.GetBack_Location(pc, true);
//									pc.start_teleport(loc[0], loc[1], loc[2], pc.getHeading(), 169, true, false);
									pc.start_teleport(33926, 33350, 4, pc.getHeading(), 18339, true, false);
								}else{
//									int[] loc = Getback.GetBack_Location(pc, true);
//									pc.do_simple_teleport(loc[0], loc[1], loc[2]);
									pc.do_simple_teleport(33926, 33350, 4);
								}
							}
						}
					}
					//message.clear();
				}
			});
		}
		return this;
	}
	
	@Override
	public boolean is_opened(){
		return _listener.is_opened();
	}
	
	@Override
	public boolean is_closed(){
		return !_listener.is_opened();
	}
	
	@Override
	public AbstractDateAcceptor set_is_opened(boolean is_opened){
		_listener.set_opened(is_opened);
		return this;
	}
	
	@Override
	public void dispose(){
		MJTeleportChain.getInstance().remove_handler(this);
		do_removed();
		if(is_opened())
			set_is_opened(false).do_close(null);
		set_opened_time(null).set_closed_cal(null);
		
		if(_listener != null){
			_listener.dispose();
			_listener = null;
		}
	}

	@Override
	public boolean is_action() {
		return _listener.is_action();
	}

	@Override
	public boolean on_teleported(L1PcInstance owner, int next_x, int next_y, int map_id, int old_mapid) {
		if(!is_opened() || !(_listener instanceof TeleporterActionListener))
			return false;
		
		TeleporterActionListener listener = (TeleporterActionListener)_listener;
		short[] mapIds = listener.get_teleport_map_id();
		for(int mid : mapIds) {
			if(map_id == mid) {
				 long remain_seconds = (_closed_cal.getTimeInMillis() / 1000) - (System.currentTimeMillis() / 1000);
				if(remain_seconds > 0)
					owner.sendPackets(new S_PacketBox(S_PacketBox.MAP_TIMER, (int)remain_seconds));
				break;
			}
		}
		return false;
	}

	@Override
	public boolean is_teleport(L1PcInstance owner, int next_x, int next_y, int map_id) {
		return false;
	}
}
