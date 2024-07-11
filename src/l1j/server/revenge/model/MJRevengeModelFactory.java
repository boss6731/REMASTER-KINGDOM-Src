package l1j.server.revenge.model;

public interface MJRevengeModelFactory {
	public static MJRevengeModelFactory winner() {
		return new WinnerFactory();
	}
	
	public static MJRevengeModelFactory loser() {
		return new LoserFactory();
	}
	
	public MJRevengeModel newModel();
	
	
	static class WinnerFactory implements MJRevengeModelFactory{
		private WinnerFactory() {}
		
		@Override
		public MJRevengeModel newModel() {
			return new MJRevengeWinnerModel();
		}
	}
	
	static class LoserFactory implements MJRevengeModelFactory{
		private LoserFactory(){}
		
		@Override
		public MJRevengeModel newModel() {
			return new MJRevengeLoserModel();
		}
	}
}
