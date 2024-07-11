package l1j.server.MJCharacterActionSystem;

import l1j.server.server.model.Instance.L1PcInstance;

public abstract class AbstractActionHandler {
	protected L1PcInstance owner;

	public abstract void handle();
	public abstract boolean validation();
	public abstract int getRegistIndex();
	public abstract long getInterval();
	public abstract boolean empty();
	public void register(){
		if(owner != null)
			owner.registerActionHandler(getRegistIndex(), this);
	}
	
	public void unregister(){
		if(owner != null) {
			//owner.setTelDelay(200);
			owner.unreigsterActionHandler(getRegistIndex());
		}
	}
	
	public void dispose(){
		owner = null;
	}
	
	public String get_owner_name(){
		return owner.getName();
	}
}
