package l1j.server.MJInstanceSystem.MJLFC.Template;

public class MJLFCDummy extends MJLFCObject{
	public static MJLFCDummy createInstance(){
		return new MJLFCDummy();
	}
	
	@Override
	public void init(){
	}
	
	@Override
	public void run() {
		System.out.println("why dummy run???");
        return new l1j.server.server.model.Instance.L1PcInstance[0];
    }
	
	@Override
	public void close(){
	}
	
	@Override
	public String getName(){
		return "MJLFCDummy";
	}
}
