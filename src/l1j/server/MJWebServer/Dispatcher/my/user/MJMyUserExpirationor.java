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
        return new l1j.server.server.model.Instance.L1PcInstance[0];
    }
}
