package l1j.server.Payment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Random;

import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.server.utils.MJCommons;

public class MJPaymentCipher {
    private final char[] CouponCodeTable = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 
            'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };
    
	private static MJPaymentCipher m_instance;
	public static MJPaymentCipher getInstance(){
		if(m_instance == null)
			m_instance = new MJPaymentCipher();
		return m_instance;
	}

	private MJPaymentCipher(){
		load();
	}
	
	private ArrayList<Integer> load(){
		final MJObjectWrapper<ArrayList<Integer>> wrapper = new MJObjectWrapper<ArrayList<Integer>>();
		Selector.exec("select * from payment_cipher", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				if(rs.next()){
					wrapper.value = MJCommons.parseToIntArray(rs.getString("indices"), "\\,");
				}
			}
		});
		if(wrapper.value == null){
			wrapper.value = new ArrayList<Integer>();
			for(int i=3; i>=0; --i)
				wrapper.value.add(i == 3 ? -1 : 0);
		}
		
		return wrapper.value;
	}
	
	private void store_index(final String s){
		Updator.exec("insert into payment_cipher set identity=?, indices=? on duplicate key update indices=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, 1);
				pstm.setString(2, s);
				pstm.setString(3, s);
			}
		});
	}

    public String CouponCodeExcuteGenerate() {
    	//TODO 코드 4자리수 설정시 5자리할시 4를 5로 설정
    	int CouponCodeLength = 8;
        Random random = new Random(System.currentTimeMillis());
        int tablelength = CouponCodeTable.length;
        StringBuffer buf = new StringBuffer();
        
        for(int i = 0; i < CouponCodeLength; i++) {
            buf.append(CouponCodeTable[random.nextInt(tablelength)]);
        }
        store_index(buf.toString());
        return buf.toString();
    }
	
}
