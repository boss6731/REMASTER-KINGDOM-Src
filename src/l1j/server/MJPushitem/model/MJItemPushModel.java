package l1j.server.MJPushitem.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.MJTemplate.MJTime;

public class MJItemPushModel {
	private int pushid;
	private int itemid;
	private int count;
	private int enchantlevel;
	private long expiredate;
	private long enabledate;
	private boolean useitem_at_once;
	private String subject;
	private String contents;
	private String weburl;
	private String imagenum;
	private int minlevel;
	private int maxlevel;
	
	public static MJItemPushModel newInstance(){
		return new MJItemPushModel();
	}
	
	public static MJItemPushModel readToDatabase(ResultSet rs) throws SQLException {
		MJItemPushModel info = newInstance();
		info.pushid = rs.getInt("push_id");
		info.itemid = rs.getInt("item_id");
		info.count = rs.getInt("item_count");
		info.enchantlevel = rs.getInt("enchantlevel");
		info.enabledate = MJTime.convertLong(rs.getTimestamp("enabledate")) / 1000;
		info.expiredate = MJTime.convertLong(rs.getTimestamp("expiredate")) / 1000;
		info.useitem_at_once = rs.getString("useitem_at_once").equalsIgnoreCase("true") ? true : false;
		info.subject = rs.getString("subject");
		info.contents = rs.getString("contents");
		info.weburl = rs.getString("weburl");
		info.minlevel = rs.getInt("minlevel");
		info.maxlevel = rs.getInt("maxlevel");
		info.imagenum = rs.getString("imagenum");
		return info;
	}
	
	public int getItemId(){
		return itemid;
	}
	public void setItemId(int i){
		itemid = i;
	}
	
	public int getItemCount(){
		return count;
	}
	public void setItemCount(int i){
		count = i;
	}
	
	public int getEnchantlevel() {
		return enchantlevel;
	}

	public void setEnchantlevel(int enchantlevel) {
		this.enchantlevel = enchantlevel;
	}
	
	public int getpushid() {
		return pushid;
	}
	public void setpushid(int pushid) {
		this.pushid = pushid;
	}
	public long getExpiredate() {
		return expiredate;
	}
	public void setExpiredate(long expiredate) {
		this.expiredate = expiredate;
	}
	public long getEnabledate() {
		return enabledate;
	}
	public void setEnabledate(long enabledate) {
		this.enabledate = enabledate;
	}
	public boolean isUseitem_at_once() {
		return useitem_at_once;
	}
	public void setUseitem_at_once(boolean useitem_at_once) {
		this.useitem_at_once = useitem_at_once;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getWeburl() {
		return weburl;
	}
	public void setWeburl(String weburl) {
		this.weburl = weburl;
	}
	public int getMinlevel() {
		return minlevel;
	}
	public void setMinlevel(int minlevel) {
		this.minlevel = minlevel;
	}
	public int getMaxlevel() {
		return maxlevel;
	}
	public void setMaxlevel(int maxlevel) {
		this.maxlevel = maxlevel;
	}
	
	public String getImagenum() {
		return imagenum;
	}

	public void setImagenum(String imagenum) {
		this.imagenum = imagenum;
	}
}
