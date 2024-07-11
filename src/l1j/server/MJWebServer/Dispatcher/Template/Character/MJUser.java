package l1j.server.MJWebServer.Dispatcher.Template.Character;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.handler.codec.http.cookie.Cookie;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyRepresentativeService;
import l1j.server.MJWebServer.Dispatcher.my.user.MJMyUserGroup;
import l1j.server.MJWebServer.Dispatcher.my.user.MJMyUserInfo;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;


public class MJUser {
	public static ConcurrentHashMap<String, MJUser> _users = new ConcurrentHashMap<String, MJUser>(1024);
	
	public static String cookiesToItemQuery(Collection<Cookie> cookies)throws UnsupportedEncodingException{
		if(cookies == null)
			return "";
		
		for(Cookie coo : cookies){
			if(coo.name().equalsIgnoreCase("lineage1_itemQuery"))
				return URLDecoder.decode(coo.value(), "utf8");
		}
		return "";
	}
	
	public static MJUser cookiesToUser(Collection<Cookie> collection) throws UnsupportedEncodingException{
		MJUser user = new MJUser();
		if(collection != null){
			String nameTmp;
			String valTmp;
			for(Cookie cookie : collection){
				nameTmp = cookie.name();
				valTmp	= cookie.value();
				if(valTmp == null || valTmp.equalsIgnoreCase(""))
					continue;
				if(nameTmp.equalsIgnoreCase("lineage1_charId")){
					user._charName 	= URLDecoder.decode(valTmp, "euc-kr");
					user._isLogin	= true;
				}else if(nameTmp.equalsIgnoreCase("lineage1_pledgeName"))
					user._pledgeName = URLDecoder.decode(valTmp, "euc-kr");
				else if(nameTmp.equalsIgnoreCase("lineage1_charLevel"))
					user._charLvl = Integer.parseInt(valTmp);
				else if(nameTmp.equalsIgnoreCase("lineage1_charClassId"))
					user._charClassId = Integer.parseInt(valTmp);
				else if(nameTmp.equalsIgnoreCase("lineage1_charGender"))
					user._charGender = Integer.parseInt(valTmp);
				else if(nameTmp.equalsIgnoreCase("lineage1_webLoginState")){
					cookie.setValue("1");
				}
			}
			
			L1PcInstance pc = L1World.getInstance().getPlayer(user._charName);
			if(pc != null) {
				user._ncoin = pc.getAccount().Ncoin_point;
			}
		}
		
		return user;
	}
	
	public static MJUser NewcookiesToUser(Collection<Cookie> collection) throws UnsupportedEncodingException{
		MJUser user = new MJUser();
		if(collection != null){
			String nameTmp;
			String valTmp;
			MJMyUserInfo uInfo;
			for(Cookie cookie : collection){
				nameTmp = cookie.name();
				valTmp	= cookie.value();
				if(valTmp == null || valTmp.equalsIgnoreCase(""))
					continue;

				if(nameTmp.equalsIgnoreCase("authToken")){
					uInfo = MJMyUserGroup.group().get(valTmp);
					if (uInfo != null) {
						user._charName = MJMyRepresentativeService.service().selectRepresentativeCharacter(uInfo.account());
					}
					
					L1PcInstance pc = L1World.getInstance().getPlayer(user._charName);
					if (pc != null) {
						user._pledgeName = pc.getClanname();
						user._charLvl = pc.getLevel();
						user._charClassId = pc.getClassId();
						user._charGender = pc.get_sex();
						user._ncoin = pc.getAccount().Ncoin_point;
						cookie.setValue("1");
						user._isLogin	= true;
					}
				}
			}
		}
		
		return user;
	}

	public String	_pledgeName;
	public String 	_charName;
	public int 	_charLvl;
	public int		_charClassId;
	public int 	_charGender;
	public boolean _isLogin;
	public String	_sessionId;
	public int _ncoin;
	
	public MJUser(){
		_isLogin 	= false;
		_pledgeName = "-";
		_charName	= "貴賓";
		_charLvl	= 0;
		_charClassId= 0;
		_charGender = 1;
		_sessionId	= "";
		_ncoin = 0;
	}
	
	public boolean isLogin(){
		return _isLogin;
	}
	
	public int getClassId(){
		return _charClassId;
	}
	
	public int getGender(){
		return _charGender;
	}
	
	public int getLevel(){
		return _charLvl;
	}
	
	public String getPledgeName(){
		return _pledgeName;
	}
	
	public String getCharName(){
		return _charName;
	}
	
	public String getSessionId(){
		return _sessionId;
	}
	
	public int getNcoin() {
		return _ncoin;
	}
}
