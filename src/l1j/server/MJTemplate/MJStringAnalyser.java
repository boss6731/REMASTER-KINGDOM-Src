package l1j.server.MJTemplate;

public class MJStringAnalyser {
	public static MJStringAnalyser execute(String source_string){
		return new MJStringAnalyser(source_string).do_analysis();
	}
	
	private String m_source_string;
	private int m_source_length;
	private int m_kor_length;
	private int m_alpha_length;
	private int m_special_length;
	private int m_invalid_length;
	private int m_bytes_length;
	private MJStringAnalyser(String source_string){
		m_source_string = MJString.isNullOrEmpty(source_string) ? MJString.EmptyString : source_string;
		m_source_length = m_source_string.length();
	}
	
	public String get_source_string(){
		return m_source_string;
	}
	
	public int get_source_length(){
		return m_source_length;
	}
	
	public int get_kor_length(){
		return m_kor_length;
	}
	
	public int get_alpha_length(){
		return m_alpha_length;
	}
	
	public int get_special_length(){
		return m_special_length;
	}
	
	public int get_invalid_length(){
		return m_invalid_length;
	}
	
	public int get_bytes_length(){
		return m_bytes_length;
	}
	
	public MJStringAnalyser do_analysis(){
		for(int i=m_source_length - 1; i>=0; --i){
			int c = m_source_string.charAt(i);
			if(MJString.is_syllables_kor(c)){
				m_bytes_length += 2;
				++m_kor_length;
			}else if(MJString.is_lower_alpha(c) || MJString.is_upper_alpha(c)){
				++m_bytes_length;
				++m_kor_length;
			}else{
				if(MJString.is_special_character(c)){
					++m_special_length;
				}else{
					++m_invalid_length;
				}
				m_bytes_length += (c >= 0x100 ? 2 : 1);
			}
		}
		return this;
	}
}
