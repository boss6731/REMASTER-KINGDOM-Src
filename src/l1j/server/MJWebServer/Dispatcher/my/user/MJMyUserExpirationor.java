package l1j.server.MJWebServer.Dispatcher.my.user;

class MJMyUserExpirationor implements Runnable{
	private MJMyUserInfo uInfo;
	MJMyUserExpirationor(MJMyUserInfo uInfo){
		this.uInfo = uInfo;
	}
	
	@Override
	public void run(){
		MJMyUserGroup.group().removeInternal(uInfo.authToken());
		uInfo.fireExpirationor();
	}
}
