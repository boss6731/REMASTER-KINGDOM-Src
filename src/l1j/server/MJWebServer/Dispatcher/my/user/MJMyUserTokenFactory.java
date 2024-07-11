package l1j.server.MJWebServer.Dispatcher.my.user;

import java.util.Base64;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.google.gson.JsonObject;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.MJMyHeaderSetter;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyResource;

public class MJMyUserTokenFactory {
	private MJMyUserTokenFactory(){}
	
	public static String createAppToken(){
		return UUID.randomUUID().toString();
	}
	
	public static String createAuthToken(String appToken, String account, String password){
		try{
			byte[] authTokenKey = MJMyResource.construct().auth().authTokenKeyBytes();
			JsonObject obj = new JsonObject();
			obj.addProperty("appToken", appToken);
			obj.addProperty("account", account);
			obj.addProperty("password", password);
			obj.addProperty("authTimeMillis", System.currentTimeMillis());
			String json = obj.toString();
			Mac sha256 = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(authTokenKey, "HmacSHA256");
			sha256.init(secret_key);
			return Base64.getEncoder().encodeToString(sha256.doFinal(json.getBytes(MJEncoding.UTF8)));
		}catch(Exception e){
			e.printStackTrace();
		}
		return MJString.EmptyString;
	}
	
	public static MJMyHeaderSetter loginSetter(final String authToken) {
		return new MJMyHeaderSetter() {
			@Override
			public void onHeaderSet(HttpResponse response) {
				if(!MJString.isNullOrEmpty(authToken)){
					Cookie cookie = new DefaultCookie("authToken", authToken);
					cookie.setMaxAge(MJMyResource.construct().auth().authExpirationMillis() / 1000L);
					cookie.setPath("/");
					response.headers().set(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode(cookie));
				}
			}
		};
	}
}
