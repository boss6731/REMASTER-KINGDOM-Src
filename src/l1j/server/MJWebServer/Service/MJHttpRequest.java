package l1j.server.MJWebServer.Service;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Codec.MJWSHandler;

public class MJHttpRequest implements HttpRequest{
	private static final int INIT_COOKIE = 1;
	private static final int INIT_PARAMS = 2;
	private static final int INIT_MULTI_PART = 4;
	private static final int INIT_POST_DATA = 8;
	
	private static final HttpDataFactory m_factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);

	protected HttpRequest m_request;
	protected ChannelHandlerContext m_ctx;
	
	private int m_bit;
	
	protected Collection<Cookie> m_cookies;
	protected Map<String, List<String>> m_parameters;
	protected Map<String, String> m_post_datas;
	protected String m_request_uri;
	
	protected boolean m_is_multipart;
	protected byte[] m_body;
	
	public MJHttpRequest(HttpRequest request, ChannelHandlerContext ctx){
		m_request = request;
		m_ctx = ctx;
		m_bit = 0;
		
		m_cookies = null;
		m_parameters = null;
		m_request_uri = MJString.EmptyString;
	}
	@Override
	public HttpHeaders headers() {
		return m_request.headers();
	}
	@Override
	public HttpRequest setProtocolVersion(HttpVersion version) {
		m_request.setProtocolVersion(version);
		return this;
	}
	@Override
	public HttpVersion getProtocolVersion() {
		return protocolVersion();
	}
	@Override
	public HttpVersion protocolVersion() {
		return m_request.protocolVersion();
	}
	@Override
	public HttpRequest setMethod(HttpMethod method) {
		m_request.setMethod(method);
		return this;
	}
	@Override
	public HttpRequest setUri(String uri) {
		m_request.setUri(uri);
		return this;
	}
	@Override
	public DecoderResult getDecoderResult() {
		return m_request.decoderResult();
	}
	@Override
	public void setDecoderResult(DecoderResult result) {
		m_request.setDecoderResult(result);
	}
	@Override
	public DecoderResult decoderResult() {
		return m_request.decoderResult();
	}

	@Override
	public HttpMethod getMethod() {
		return method();
	}

	@Override
	public HttpMethod method() {
		return m_request.method();
	}
	@Override
	public String getUri() {
		return uri();
	}
	@Override
	public String uri() {
		return m_request.uri();
	}

	public ChannelHandlerContext get_ctx(){
		return m_ctx;
	}
	public SocketAddress get_remote_address() {
		return m_ctx.channel().remoteAddress();
	}
	
	public String get_remote_address_string(){
		InetSocketAddress address = (InetSocketAddress)get_remote_address();
		return address.getAddress().getHostAddress();
	}
	
	public int get_remote_address_port(){
		InetSocketAddress address = (InetSocketAddress)get_remote_address();
		return address.getPort();
	}

	public SocketAddress get_local_address() {
		return m_ctx.channel().localAddress();
	}

	public String get_request_uri() {
		if((m_bit & INIT_PARAMS) == 0){
			parse_parameters();
		}
		return m_request_uri;
	}

	
	public List<String> read_parameters(String key){
		Map<String, List<String>> parameters = get_parameters();
		return parameters == null ? new ArrayList<>(0) : get_parameters().get(key);
	}
	
	public String read_parameters_at_once(String key){
		List<String> list = read_parameters(key);
		return list == null || list.size() <= 0 ? MJString.EmptyString : list.get(0);
	}

	public Map<String, List<String>> get_parameters() {
		if((m_bit & INIT_PARAMS) == 0){
			parse_parameters();
		}
		return m_parameters;
	}
	private void parse_parameters(){
		m_bit |= INIT_PARAMS;
		QueryStringDecoder decoder = new QueryStringDecoder(uri());
		m_request_uri = decoder.path();
		Map<String, List<String>> params = decoder.parameters();
		if(method() == HttpMethod.POST){
			m_parameters = new HashMap<String, List<String>>();
			m_parameters.putAll(params);	
		}else{
			m_parameters = params;
		}
	}
	public Collection<Cookie> get_cookies() {
		if((m_bit & INIT_COOKIE) == 0){
			m_bit |= INIT_COOKIE;
			String cookie_header = headers().get(HttpHeaderNames.COOKIE);
			if(MJString.isNullOrEmpty(cookie_header)){
				m_cookies = Collections.emptySet();
			}else{
				m_cookies = ServerCookieDecoder.LAX.decode(cookie_header);
			}
		}
		return m_cookies;
	}
	public boolean is_multipart() {
		if((m_bit & INIT_MULTI_PART) == 0){
			m_is_multipart = false;
			m_bit |= INIT_MULTI_PART;
			if(method() == HttpMethod.POST){
				m_is_multipart = HttpPostRequestDecoder.isMultipart(m_request);
			}
		}
		return m_is_multipart;
	}
	public void set_body(byte[] body){
		m_body = body;
	}
	public byte[] get_body() {
		return m_body;
	}

	public Map<String, String> get_post_datas(){
		if((m_bit & INIT_POST_DATA) == 0){
			parse_post_datas();
		}
		return m_post_datas;
	}
	private void parse_post_datas() {
		m_bit |= INIT_POST_DATA; // 設置初始化標誌位
		m_post_datas = new HashMap<String, String>(); // 初始化存放 POST 數據的 Map
		HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(m_factory, m_request); // 創建解碼器來處理 POST 請求
		try {
			for (InterfaceHttpData data : decoder.getBodyHttpDatas()) { // 遍歷所有 POST 數據
				HttpDataType data_type = data.getHttpDataType(); // 獲取數據類型
				if (HttpDataType.Attribute == data_type) { // 如果數據類型是 Attribute
					try {
						Attribute attribute = (Attribute)data;
						m_post_datas.put(attribute.getName(), attribute.getValue()); // 將屬性名稱和值存入地圖
					} catch (Exception e) {
						e.printStackTrace(); // 打印異常堆棧信息
						MJWSHandler.print(m_ctx, String.format("Throws BODY Attribute - %s", data.getHttpDataType().name())); // 打印錯誤信息
					}
				} else {
					MJWSHandler.print(m_ctx, String.format("invalid BODY data - %s : %s", data_type.name(), data)); // 打印無效數據類型的信息
				}
			}
		} finally {
			try {
				decoder.destroy(); // 銷毀解碼器
			} catch (Exception e) {
				// 忽略異常
			}
		}
	}
	public boolean is_keep_alive(){
		return HttpUtil.isKeepAlive(m_request);
	}
	
	public String connectionFlow() {
		return new StringBuilder(64)
				.append("[")
				.append(get_remote_address().toString())
				.append("] -> [")
				.append(get_local_address().toString())
				.append("]")
				.toString();
	}


	private String toString = MJString.EmptyString;
	@Override
	public String toString(){
		if(MJString.isNullOrEmpty(toString)){
			HttpHeaders headers = m_request.headers();
			toString = new StringBuilder(1024)
					.append("[NettyHttpRequest]").append(connectionFlow()).append("\r\n")
					.append(" -uri: ").append(uri()).append("\r\n")
					.append(" -requestUri: ").append(get_request_uri()).append("\r\n")
					.append(" -method: ").append(method()).append("\r\n")
					.append(" -headers: ").append("\r\n").append(MJString.join("\r\n", "   ", headers, headers.size())).append("\r\n")
					.append(" -cookies : ").append("\r\n").append(MJString.join("\r\n", get_cookies())).append("\r\n")
					.append(" -parameters: ").append("\r\n").append(MJString.join("\r\n", "   ", get_parameters())).append("\r\n")
					.append(" -body: ").append("\r\n").append(MJString.join("\r\n", "   ", get_post_datas())).append("\r\n")
					.toString();
		}
		return toString;
	}
}