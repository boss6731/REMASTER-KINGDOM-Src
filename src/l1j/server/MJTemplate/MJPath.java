package l1j.server.MJTemplate;

import java.io.File;

public class MJPath {
	/**
	 * 경로 문자열 사이에 {@link MJString#DirectorySeparatorChar}를 삽입하고 병합하여 경로를 만들어낸다.
	 * @param pathes 경로문자열들
	 * @return 병합된 경로 문자열을 반한한다.
	 **/
	public static String combine(String...pathes){
		StringBuilder sb = new StringBuilder(256);
		int length = pathes.length - 1;
		for(int i=0; i<length; ++i){
			String path = pathes[i];
			char c = path.charAt(path.length() - 1);
			if(MJString.isDirectorySeparatorChar(c)){
				sb.append(path);
			}else{
				sb.append(path).append(MJString.DirectorySeparatorChar);
			}
		}		
		sb.append(pathes[length]);
		return sb.toString();
	}
	
	
	
	
	/**
	 * 경로 문자열에서 확장자를 찾아 반환한다.
	 * @param path 경로 문자열
	 * @return 찾아낸 확장자(.은 제거된다.)
	 **/
	public static String getExtension(String path){
		if(MJString.isNullOrEmpty(path))
			return MJString.EmptyString;

		int indexof = path.lastIndexOf('.');
		return indexof == -1 ? MJString.EmptyString : path.substring(indexof + 1, path.length());
	}
	
	
	
	
	/**
	 * 경로 문자열에서 확장자를 포함한 파일명을 찾아 반환한다.
	 * @param path 경로 문자열
	 * @return 확장자를 포함한 파일명
	 **/
	public static String getFileName(String path){
		if(MJString.isNullOrEmpty(path))
			return MJString.EmptyString;
		
		return new File(path).getName();
	}
	
	
	
	
	/**
	 * 경로 문자열에서 확장자를 제외한 파일명을 찾아 반환한다.
	 * @param path 경로 문자열
	 * @return 확장자를 제외한 파일명
	 **/
	public static String getFileNameWithoutExtension(String path){
		String fileName = getFileName(path);
		if(MJString.isNullOrEmpty(fileName))
			return MJString.EmptyString;
		
		int indexof = fileName.lastIndexOf('.');
		return indexof == -1 ? fileName : fileName.substring(0, indexof);
	}
}	
