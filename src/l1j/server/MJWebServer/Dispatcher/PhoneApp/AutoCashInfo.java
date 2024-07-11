package l1j.server.MJWebServer.Dispatcher.PhoneApp;

public class AutoCashInfo {
	private int cash;
	private int itemId;
	private String itemName;
	private int count;
	public int getCash() {
		return cash;
	}
	public int getItemId() {
		return itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public int getCount() {
		return count;
	}
	public void setCash(int cash) {
		this.cash = cash;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public void setCount(int count) {
		this.count = count;
	}
}