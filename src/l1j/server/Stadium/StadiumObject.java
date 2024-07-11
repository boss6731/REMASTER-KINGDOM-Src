package l1j.server.Stadium;

import java.util.Collection;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_EVENT_COUNTDOWN_NOTI_PACKET;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;

public class StadiumObject {
    private int m_map_id; // 球場地圖 ID
    private int m_ready_seconds; // 準備時間秒數
    private int m_play_seconds; // 遊玩時間秒數
    private boolean m_is_on; // 球場是否啟用
    private String m_name; // 球場名稱
    
    // 建構子
    public StadiumObject(int map_id, int ready_seconds, int play_seconds, String name){
        m_map_id = map_id;
        m_ready_seconds = ready_seconds;
        m_play_seconds = play_seconds;
        m_is_on = false;
        m_name = name;
    }
    
    // 執行
    public void execute(){
        StadiumManager.getInstance().regist(this);
        GeneralThreadPool.getInstance().execute(new Runnable(){
            @Override
            public void run(){
                try{
                    for(int i=m_ready_seconds; i>=0; --i){
                        Collection<L1PcInstance> col = L1World.getInstance().getVisiblePlayers(m_map_id);
                        SC_EVENT_COUNTDOWN_NOTI_PACKET count_down = null;
                        ServerBasePacket[] pcks = null;
                        if(i > 0){
                            pcks = new ServerBasePacket[]{
                                    new S_PacketBox(S_PacketBox.GREEN_MESSAGE, String.format("%d秒後開始比賽。", i)),
                            };
                        }else{
                            pcks = new ServerBasePacket[]{
                                    new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "Go!!"),
                            };
                            count_down = SC_EVENT_COUNTDOWN_NOTI_PACKET.create(m_play_seconds, String.format("\\f3%s", m_name));
                        }
                        for(L1PcInstance pc : col){
                            if(!check_pc(pc))
                                continue;

                            pc.sendPackets(pcks, false);
                            if(count_down != null)
                                pc.sendPackets(count_down, MJEProtoMessages.SC_EVENT_COUNTDOWN_NOTI_PACKET, false);
                        }
                        for(ServerBasePacket pck : pcks)
                            pck.clear();
                        if(count_down != null)
                            count_down.dispose();
                        pcks = null;
                        Thread.sleep(1000);
                    }
                    m_is_on = true;
                    do_wait();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    
    // 執行等待
    private void do_wait(){
        try{
            Thread.sleep(m_play_seconds * 1000);
            if(m_is_on)
                do_ended();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    // 執行結束
    public void do_ended() throws Exception{
        m_is_on = false;
        StadiumManager.getInstance().remove(this);
        SC_EVENT_COUNTDOWN_NOTI_PACKET count_down = SC_EVENT_COUNTDOWN_NOTI_PACKET.create(0, String.format("\\f3%s", m_name));
        ServerBasePacket message = new S_SystemMessage(String.format("%s比賽結束。", m_name));
        Collection<L1PcInstance> col = L1World.getInstance().getVisiblePlayers(m_map_id);
        for(L1PcInstance pc : col){
            if(!check_pc(pc))
                continue;

            pc.sendPackets(count_down, MJEProtoMessages.SC_EVENT_COUNTDOWN_NOTI_PACKET, false);
            pc.sendPackets(message, false);
        }
        count_down.dispose();
        message.clear();
    }
    
    // 是否正在進行中
    public boolean is_on(){
        return m_is_on;
    }
    
    // 獲取當前遊玩地圖 ID
    public int get_current_play_map_id(){
        return m_map_id;
    }

    // 檢查玩家
    private boolean check_pc(L1PcInstance pc){
        return !(pc == null || pc.getNetConnection() == null || pc.getNetConnection().isClosed() || pc.getAI() != null);
    }
}
