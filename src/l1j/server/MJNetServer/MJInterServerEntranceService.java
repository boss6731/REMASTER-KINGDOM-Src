package l1j.server.MJNetServer;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.CS_HIBREED_AUTH_REQ_PACKET;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.utils.MJHexHelper;

public class MJInterServerEntranceService implements Runnable{
	private static final MJInterServerEntranceService service = new MJInterServerEntranceService();
	public static MJInterServerEntranceService service() {
		return service;
	}
	
	private LinkedBlockingQueue<MJInterServerEntranceModel> queue;
	private boolean running;
	private MJInterServerEntranceService() {
		queue = new LinkedBlockingQueue<>();
		running = false;
	}
	
	public void execute() {
		running = true;
		GeneralThreadPool.getInstance().execute(this);
	}
	
	public void offer(GameClient clnt, byte[] buffer) {
		MJInterServerEntranceModel model = new MJInterServerEntranceModel(clnt, buffer);
		queue.offer(model);
	}
	
	@Override
	public void run() {
		try {
			while(running) {
				MJInterServerEntranceModel model = queue.poll(3000, TimeUnit.MILLISECONDS);
				if(model == null) {
					continue;
				}

				CS_HIBREED_AUTH_REQ_PACKET packet = (CS_HIBREED_AUTH_REQ_PACKET) CS_HIBREED_AUTH_REQ_PACKET.newInstance().readFrom(model.clnt, model.buffer);
				if(!packet.isInitialized()) {
					System.out.println(MJHexHelper.toString(model.buffer, model.buffer.length));
					MJEProtoMessages.printNotInitialized(model.clnt.getIp(), MJEProtoMessages.CS_HIBREED_AUTH_REQ.toInt(), packet.getInitializeBit());
				}				
				packet.dispose();
				Thread.sleep(100);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void release() {
		running = false;
	}
	
	static class MJInterServerEntranceModel{
		private GameClient clnt;
		private byte[] buffer;
		MJInterServerEntranceModel(GameClient clnt, byte[] buffer){
			this.clnt = clnt;
			this.buffer = buffer;
		}
	}
}
