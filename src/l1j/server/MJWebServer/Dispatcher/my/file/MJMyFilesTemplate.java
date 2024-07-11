package l1j.server.MJWebServer.Dispatcher.my.file;

import java.util.ArrayList;

import l1j.server.MJTemplate.MJString;

class MJMyFilesTemplate {
	ArrayList<MJMyFilesDirectoryInfo> directories;
	
	static class MJMyFilesDirectoryInfo{
		String uri;
		int maxAge;
		boolean needLogin;
		ArrayList<MJMyFilesFileInfo> effectiveFiles;
		MJMyFilesDirectoryInfo(){
			uri = MJString.EmptyString;
			maxAge = 0;
			needLogin = false;
		}
	}
	
	static class MJMyFilesFileInfo{
		String fileName;
		int maxAge;
		boolean needLogin;
		MJMyFilesFileInfo(){
			fileName = MJString.EmptyString;
			maxAge = 0;
			needLogin = false;
		}
	}
}
