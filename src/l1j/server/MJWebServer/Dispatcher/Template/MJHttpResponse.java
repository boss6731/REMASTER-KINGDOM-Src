package l1j.server.MJWebServer.Dispatcher.Template;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.Config;
import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJWebServer.Codec.MJHttpModel;
import l1j.server.MJWebServer.Codec.MJWSHandler;
import l1j.server.MJWebServer.Dispatcher.MJHttpClosedException;
import l1j.server.MJWebServer.Dispatcher.Template.Character.MJUser;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public abstract class MJHttpResponse implements MJHttpModel {
	protected MJHttpRequest m_request;
	protected boolean m_application_check;

	protected MJHttpResponse(MJHttpRequest request) {
		this(request, true);
	}
	
	protected MJHttpResponse(MJHttpRequest request, boolean application_check){
		m_request = request;
		m_application_check = application_check;
		try {
			_user = MJUser.NewcookiesToUser(request.get_cookies());
			
			if(_user != null) {
				if(m_application_check) {
					_player = L1World.getInstance().getPlayer(_user.getCharName());
				}
			}
		} catch (UnsupportedEncodingException e) {
			_user = null;
			e.printStackTrace();
		}
	}

	/*protected MJHttpResponse(MJHttpRequest request, boolean application_check) {
		m_request = request;
		m_application_check = application_check;
		try {
			_user = MJUser.cookiesToUser(request.get_cookies());

			if (_user != null) {
				if (m_application_check) {
					_player = L1World.getInstance().getPlayer(_user.getCharName());
				}
			}
		} catch (UnsupportedEncodingException e) {
			_user = null;
			e.printStackTrace();
		}
	}*/

	protected MJUser _user;
	protected L1PcInstance _player;

	public boolean is_application_check() {
		return m_application_check;
	}

	@Override
	public HttpResponse getResponse() throws MJHttpClosedException {
		return get_response();
	}

	public abstract HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException;

	public HttpResponse create_empty(HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(m_request.protocolVersion(), status);
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);
		return response;
	}

	public HttpResponse create_response(HttpResponseStatus status, String document) throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		return create_response(status, document, false);
	}

	public HttpResponse create_response(HttpResponseStatus status, String document, boolean itemSearch) throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		// TODO: 如果不是 PC，用網頁瀏覽器訪問時進行阻止

		if (Config.Web.webServerParking && m_application_check && !itemSearch) {
			if (_player == null) {
				// throw new MJHttpClosedException(); // 由於應用程序的原因被註釋
				// return create_response(status, load_file_string("./appcenter/D-dos.html").getBytes(MJEncoding.UTF8));
			}
		}

		return create_response(status, document.getBytes(MJEncoding.UTF8));
	}

	public HttpResponse create_response(HttpResponseStatus status, byte[] buff) {
		ByteBuf response_buff = Unpooled.wrappedBuffer(buff);
		FullHttpResponse response = new DefaultFullHttpResponse(m_request.protocolVersion(), status, response_buff);
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response_buff.readableBytes());
		return response;
	}

	public HttpResponse notFound(String message) throws MJHttpClosedException {
		HttpResponse response = create_response(HttpResponseStatus.NOT_FOUND, message);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
		return response;
	}

	protected void append_log(String message) {
		MJWSHandler.print(m_request.get_ctx(), message);
	}

	public String load_file_string(String path) {
		byte[] buff = load_file(path);
		return new String(buff, MJEncoding.UTF8);
	}

	public byte[] load_file(String path) {
		byte[] buff = null;
		InputStream is = null;
		try {
			File f = new File(path);
			if (!f.isFile()) {
				return null;
			}

			if (!f.exists()) {
				append_log(String.format("未找到文件。 %s", path));
				return null;
			}
			buff = new byte[(int) f.length()];
			is = new FileInputStream(path);
			is.read(buff, 0, buff.length);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return buff;
	}
}
