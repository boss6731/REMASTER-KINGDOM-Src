package l1j.server.MJNetServer.Codec;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.concurrent.ConcurrentHashMap;

import MJFX.MJFxEntry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import l1j.server.Config;
import l1j.server.MJNetSafeSystem.MJClientVersionChecker;
import l1j.server.MJNetSafeSystem.MJNetSafeLoadManager;
import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
import l1j.server.MJNetServer.MJNetServerLoadManager;
import l1j.server.MJNetServer.Buffer.MJByteBufferFactory;
import l1j.server.MJNetServer.ClientManager.MJNSClientCounter;
import l1j.server.MJNetServer.ClientManager.MJNSDenialAddress;
import l1j.server.MJNetServer.Codec.Cryptor.MJCryptor;
import l1j.server.MJNetServer.Codec.Cryptor.MJCryptorEx;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJWhiteIP;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.CS_CLIENT_VERSION_INFO;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CUSTOM_MSGBOX;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CUSTOM_MSGBOX.ButtonType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CUSTOM_MSGBOX.IconType;
import l1j.server.MJWebServer.Dispatcher.Login.MJHttpLoginManager;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.IpTable;
import l1j.server.server.utils.MJCommons;

/**********************************
 * 
 * MJ Network Server System Handler. made 2017.
 * 
 **********************************/
public class MJNSHandler extends ChannelInboundHandlerAdapter{
	private static final Object m_counter_lock = new Object();
	private static CounterChecker m_checker = null;
	private static int m_counter = 0;
	private static final AttributeKey<GameClient> _clntAttr = AttributeKey.newInstance("GameClient");
	private static final AttributeKey<MJCryptor> _cptAttr = AttributeKey.newInstance("MJCryptor");

	private static ConcurrentHashMap<ChannelHandlerContext, GameClient> _clnts = null;
	private static Collection<GameClient> _clntsSnap = null;
	private long clientversion;

	public static void initializer() {
		if (_clnts == null)
			_clnts = new ConcurrentHashMap<ChannelHandlerContext, GameClient>(Config.Login.MaximumOnlineUsers);
		MJNSDenialAddress.getInstance();
		
	}

	static class CounterChecker implements Runnable{
		CounterChecker(){}
		@Override
		public void run() {
			while(true) {
				try {
					Thread.sleep(MJNetSafeLoadManager.NS_CLIENT_PER_SECONDS_FROM_SECONDS * 1000L);
				}catch(Exception e) {
					e.printStackTrace();
				}
				synchronized(m_counter_lock) {
					m_counter = 0;
				}
			}
		}
	}

	public static Collection<GameClient> getClients() {
		Collection<GameClient> col = _clntsSnap;
		return col == null ? _clntsSnap = Collections.unmodifiableCollection(_clnts.values()) : col;
	}

	public static int getClientSize(){
		return _clnts.size();
	}
	
	public static GameClient getClient(ChannelHandlerContext ctx) {
		return _clnts.get(ctx);
	}

	public static MJCryptor getCryptor(ChannelHandlerContext ctx) {
		return ctx.channel().attr(_cptAttr).get();
	}

	public static void do_denials(String address) {
		for (GameClient clnt : getClients()) {
			if (clnt.getIp().equalsIgnoreCase(address)) {
				try {
					clnt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void set_version(long version) {
		clientversion = version;
	}

	@override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		InetSocketAddress inetAddr = (InetSocketAddress) ctx.channel().remoteAddress();
		int port = inetAddr.getPort();
		String hostAddress = inetAddr.getAddress().getHostAddress();

		if (MJNSDenialAddress.getInstance().isDenialsAddress(hostAddress)) {
			ctx.close();
			return;
		}

			/*if (MJHttpLoginManager.getInstance().getLoginInfoFromAddress(hostAddress) == null) {
			print(hostAddress, port, "因未通過網頁訪問而被阻止連接。");
			ctx.close();
			return;
			}*/

		if (MJHttpLoginManager.getInstance().removeAndGetFromAddress(hostAddress) == null) {
			synchronized (m_counter_lock) {
				if (m_checker == null) {
					m_checker = new CounterChecker();
					new Thread(m_checker).start();
				}

				/*if (++m_counter > MJNetSafeLoadManager.NS_CLIENT_PER_SECONDS) {
				ctx.close();
				print(hostAddress, port, "[因每秒訪客過多而拒絕連接]");
				return;
				}
				}
				}*/
			}
				if (++m_counter > MJNetSafeLoadManager.NS_CLIENT_PER_SECONDS) {
					print(host_address, port, "[由於每秒連接數過多，拒絕連接]");
					GameClient clnt = new GameClient(ctx.channel());
					SC_CUSTOM_MSGBOX box = SC_CUSTOM_MSGBOX.newInstance();
					box.set_button_type(ButtonType.MB_OK);
					box.set_icon_type(IconType.MB_ICONASTERISK);
					box.set_message("瞬間連接量飽和，請稍後再試。\n\n從連接器進行網頁登入時，可以直接連接。\n\n詳細信息請參考官網的‘連接方法’。 \n\n-人工智慧系統");
					box.set_title(Config.Message.GameServerName);
					box.set_message_id(0);
					clnt.sendPacket(box, MJEProtoMessages.SC_CUSTOM_MSGBOX.toInt());
					GeneralThreadPool.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							try {
								ctx.close();
							} catch (Exception e) {
							}
						}
					}, 5000L);
				}
			}
		}

		if (!Config.Login.SERVERSTANDBY) {
			GameClient clnt = new GameClient(ctx.channel());
			SC_CUSTOM_MSGBOX box = SC_CUSTOM_MSGBOX.newInstance();
			box.set_button_type(ButtonType.MB_OK);
			box.set_icon_type(IconType.MB_ICONHAND);
			box.set_message("開放前無法進入。\n\n從20:00開始可以進入。");
			box.set_title(Config.Message.GameServerName);
			box.set_message_id(0);
			clnt.sendPacket(box, MJEProtoMessages.SC_CUSTOM_MSGBOX.toInt());
			final GameClient c = clnt;
			GeneralThreadPool.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					try {
						c.close();
					} catch (Exception e) {
					}
				}
			}, 5000L);
		}

		if (port <= MJNetServerLoadManager.NETWORK_WELL_KNOWNPORT) {
			print(host_address, port, "用戶嘗試通過知名端口連接，被阻止了。");
			MJNSDenialAddress.getInstance().insert_address(host_address, MJNSDenialAddress.REASON_WELL_KNWON);
			do_denials(host_address);
			ctx.close();
			return;
		}
		if (_clnts.size() + 1 >= Config.Login.MaximumOnlineUsers) {
			print(host_address, port, String.format("連接IP數量超過%d個，自動阻止。(連接人數)", Config.Login.MaximumOnlineUsers));
			ctx.close();
			return;
		}

		if (!Config.shutdownCheck && ctx.channel().isActive()) {
			int nSize = MJNSClientCounter.getInstance().isPermission(inetAddr.getAddress());
			IpTable iptable = IpTable.getInstance();
			if (nSize == -1) {
				print(host_address, port, String.format("◆[確認]: 嘗試連接超過%d次，已被阻止。◆", MJNetServerLoadManager.NETWORK_CLIENT_PERMISSION));
				MJNSDenialAddress.getInstance().insert_address(host_address, MJNSDenialAddress.REASON_CONNECTION_OVER);
				do_denials(host_address);
				iptable.banIp(host_address);
				ctx.close();
				return;
			}
			

			MJCryptor crt = null;
			GameClient clnt = null;
			ByteBuf buf = null;


			print("登入 IP 位址:" + host_address, port, String.format("(正在連接) (連接人數:%d/%d)", nSize, _clnts.size() + 1));

			
			
			if (Config.Login.UseExConnect) {
				byte[] encode_key = MJRnd.next_bytes(8);
				byte[] enc_to_encode_key = Arrays.copyOf(encode_key, encode_key.length);
				MJCommons.encode_xor(enc_to_encode_key, MJNetSafeLoadManager.NS_CLIENT_SIDE_KEY, 0, enc_to_encode_key.length);
				MJCommons.reverse(enc_to_encode_key, 0);
				MJCommons.zigzag(enc_to_encode_key, 0);
				MJCommons.PK_T_LOG(enc_to_encode_key);
				buf = MJByteBufferFactory.createKey(ctx, enc_to_encode_key);
				clnt = new GameClient(ctx.channel());
				crt = new MJCryptorEx(encode_key);
			} else {
				buf = MJByteBufferFactory.createKey(ctx);
				clnt = new GameClient(ctx.channel());
				crt = new MJCryptor();
			}
			ctx.writeAndFlush(buf);
			_clnts.put(ctx, clnt);
			ctx.channel().attr(_clntAttr).set(clnt);
			crt.initKey(Opcodes.getSeed());
			ctx.channel().attr(_cptAttr).set(crt);
			clnt.setStatus(MJClientStatus.CLNT_STS_HANDSHAKE);
			if (!MJFxEntry.IS_DEBUG_MODE && !MJWhiteIP.getInstance().contains(clnt.getIp()))
				MJClientVersionChecker.execute(clnt);
		} else {
			ctx.close();
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		byte[] b = (byte[]) msg;
		GameClient clnt = ctx.channel().attr(_clntAttr).get();
		if (clnt == null) {
			print(ctx, "由於無客戶端的 socket 數據傳輸，被阻止了。");
			ctx.close();
		} else {
			try {
				clnt.handle(b);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		try {
//			GameClient clnt1 = ctx.channel().attr(_clntAttr).get();
//			if (clnt1 != null) {
//				if (clnt1.getAccountName() != null) {
//					System.out.println("account : " + clnt1.getAccountName());
//				}
//			}
			print(ctx, "[狀態:正常客戶端關閉]");
			GameClient clnt = _clnts.remove(ctx);
			InetSocketAddress inetAddr = (InetSocketAddress) ctx.channel().remoteAddress();
			MJNSClientCounter.getInstance().decrese(inetAddr.getAddress());
			if (clnt != null) {
				clnt.get_client_close(clnt);
				clnt.close();
				clnt = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ctx.close();
		} catch (Exception e) {
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		try {
//			cause.printStackTrace();//TEST 원인찾기
			print(ctx, "[狀態:非正常客戶端關閉]");
			GameClient clnt = _clnts.remove(ctx);
			InetSocketAddress inetAddr = (InetSocketAddress) ctx.channel().remoteAddress();
			MJNSClientCounter.getInstance().decrese(inetAddr.getAddress());
			if (clnt != null) {
				clnt.get_client_close(clnt);
				clnt.close();
				clnt = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ctx.close();
		} catch (Exception e) {
		}
	}

	public static void print(ChannelHandlerContext ctx, String message) {
		InetSocketAddress inetAddr = (InetSocketAddress) ctx.channel().remoteAddress();
		print(inetAddr.getAddress().getHostAddress(), inetAddr.getPort(), message);
	}

	public static void print(String ip, int port, String message) {
		System.out.println(String.format("[%s][%s:%d] %s\r\n", getLocalTime(), ip, port, message));
	}

	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat formatter_doubles = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public static String getLocalTime() {
		return formatter.format(new GregorianCalendar().getTime());
	}

	public static String getLocalTimeDoubles() {
		return formatter_doubles.format(new GregorianCalendar().getTime());
	}
}
