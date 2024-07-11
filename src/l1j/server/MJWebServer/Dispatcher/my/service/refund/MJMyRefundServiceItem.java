package l1j.server.MJWebServer.Dispatcher.my.service.refund;

import java.text.DecimalFormat;

import MJNCoinSystem.MJNCoinRefundInfo;
import l1j.server.MJTemplate.MJString;

public class MJMyRefundServiceItem {
	static MJMyRefundServiceItem newInstance(MJNCoinRefundInfo nInfo){
		DecimalFormat _decF = new DecimalFormat("#,###");
		MJMyRefundServiceItem item = new MJMyRefundServiceItem();
		item.refundId = nInfo.get_refund_id();
		item.category = nInfo.get_is_refund() ? 1 : 0;
		item.categoryTitle = MJMyRefundCategory.findCategory(item.category).categoryTitle();
		item.refundName = nInfo.get_refund_name();
		item.characterName = nInfo.get_character_name();
		item.accountName = nInfo.get_account_name();
		item.bankName = nInfo.get_bank_name();
		item.bankAccountNumber = nInfo.get_bank_account_number();
		item.ncoin = _decF.format(nInfo.get_ncoin_value());
		item.lastModified = MJString.convert_datetime_millis(nInfo.get_generate_date());
		return item;
	}
	
	private int refundId;
	private int category;
	private String categoryTitle;
	private String refundName;
	private String characterName;
	private String accountName;
	private String bankName;
	private String bankAccountNumber;
	private String ncoin;
	private long lastModified;
	MJMyRefundServiceItem(){
		refundId = -1;
		category = -1;
		categoryTitle = MJString.EmptyString;
		lastModified = 0L;
	}
	
	public int refundId(){
		return refundId;
	}
	
	public int category(){
		return category;
	}
	
	public String categoryTitle(){
		return categoryTitle;
	}
	
	public String refundName(){
		return refundName;
	}
	
	public String characterName(){
		return characterName;
	}
	
	public String accountName(){
		return accountName;
	}
	
	public String bankName(){
		return bankName;
	}
	
	public String bankAccountNumber(){
		return bankAccountNumber;
	}
	
	public String ncoin(){
		return ncoin;
	}
	
	public long lastModified(){
		return lastModified;
	}
}
