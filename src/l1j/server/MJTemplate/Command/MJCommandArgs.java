package l1j.server.MJTemplate.Command;

import l1j.server.MJTemplate.Exceptions.MJCommandArgsIndexException;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ChatPacket;

public class MJCommandArgs {
	private L1PcInstance 	_owner;
	private String[]		_args;
	private int				_idx;
	public MJCommandArgs setOwner(L1PcInstance pc){
		_owner = pc;
		return this;
	}
	
	public MJCommandArgs setParam(String param){
		_args 	= param.split(" ");
		_idx	= 0;
		return this;
	}
	
	public L1PcInstance getOwner(){
		return _owner;
	}
	
	public int nextInt() throws MJCommandArgsIndexException{
		isValidRange();
		return Integer.parseInt(_args[_idx++]);
	}
	
	public String nextString() throws MJCommandArgsIndexException{
		isValidRange();
		return _args[_idx++];
	}
	
	private void isValidRange() throws MJCommandArgsIndexException{
		if(_args.length <= _idx)
			throw new MJCommandArgsIndexException(_args, _idx);
	}
	
	public MJCommandArgs undo(){
		--_idx;
		return this;
	}
	
	public boolean isRange(){
		return _idx < _args.length;
	}

	/**
	 * 發送通知消息給所有者。
	 * @param message 要發送的消息
	 */
	public void notify(String message) {
		if (_owner != null) {
			if (_owner.getName().equalsIgnoreCase("網路管理員")) {
				_owner.sendPackets(message);
			} else {
				_owner.sendPackets(new S_ChatPacket(_owner, message));
			}
		}
	}
	
	public void dispose(){
		_args 	= null;
		_owner 	= null;
	}
}
