package l1j.server.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;

import MJFX.UIAdapter.MJUIAdapter;
import l1j.server.MJNetServer.MJNetServerLoadManager;
import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJReadWriteLock;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;

public class AccountService {
	private static final AccountService service = new AccountService();
	public static AccountService service(){
		return service;
	}
	
	private final MJReadWriteLock lock;
	private final HashSet<String> accounts;
	private AccountService(){	
		lock = new MJReadWriteLock();
		accounts = new HashSet<>(256);
	}
	
	public Account.Account onNewAccount(String account, String password, String address, String phone)
			throws AlreadyAccountsException, AddressOverException{
		Account.Account aInfo = null;
		try{
			lock.writeLock();
			if(accounts.contains(account)){
				throw new AlreadyAccountsException();
			}
			if(alreadyAccountDatabase(account)){
				throw new AlreadyAccountsException();
			}
			if(numOfAddress(address) >= MJNetServerLoadManager.NETWORK_ADDRESS2ACCOUNT){
				throw new AddressOverException();
			}
			Account.Account.create(account, password, address, address, phone);
			aInfo = Account.Account.load(account);
			if(aInfo == null){
				throw new AlreadyAccountsException();
			}
			MJUIAdapter.on_create_account(account, address);
			accounts.add(account);
		}finally{
			lock.writeUnlock();
		}
		return aInfo;
	}
	
	public boolean alreadyAccount(final String account){
		try{
			lock.readLock();
			if(accounts.contains(account)){
				return true;
			}
			return alreadyAccountDatabase(account);
		}finally{
			lock.readUnlock();
		}
	}
	
	private boolean alreadyAccountDatabase(final String account){
		MJObjectWrapper<Boolean> wrapper = new MJObjectWrapper<>();
		wrapper.value = false;
		Selector.exec("select login from accounts where login=? limit 1", new SelectorHandler(){

			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, account);
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				wrapper.value = rs.next();
			}
		});
		return wrapper.value;
	}
	
	public int numOfAddress(final String address){
		MJObjectWrapper<Integer> wrapper = new MJObjectWrapper<>();
		wrapper.value = 0;
		Selector.exec("select count(ip) as numOfAddress from accounts where ip=?", new SelectorHandler(){

			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, address);
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				if(rs.next()){
					wrapper.value = rs.getInt("numOfAddress");
				}
			}
		});
		return wrapper.value;
	}
	
	public static class AlreadyAccountsException extends Exception{
		private static final long serialVersionUID = 1L;
	}
	
	public static class AddressOverException extends Exception{
		private static final long serialVersionUID = 1L;
	}
}
