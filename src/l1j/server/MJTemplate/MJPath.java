package l1j.server.MJTemplate;

import java.io.File;

public class MJPath {
	/**
	 * ������ݬ������?�� {@link MJString#DirectorySeparatorChar} ?��?�������ӡ�
	 * @param paths �����ݬ�����
	 * @return ��?���������ݬ��
	 **/
	public static String combine(String... paths) {
		StringBuilder sb = new StringBuilder(256);
		int length = paths.length - 1;
		for (int i = 0; i < length; ++i) {
			String path = paths[i];
			char c = path.charAt(path.length() - 1);
			if (MJString.isDirectorySeparatorChar(c)) {
				sb.append(path);
			} else {
				sb.append(path).append(MJString.DirectorySeparatorChar);
			}
		}
		sb.append(paths[length]);
		return sb.toString();
	}




	/**
	 * �������ݬ����???��������٣��
	 * @param path �����ݬ��
	 * @return ??��������٣���𶡰.����
	 **/
	public static String getExtension(String path) {
		if (MJString.isNullOrEmpty(path))
			return MJString.EmptyString;

		int indexof = path.lastIndexOf('.');
		return indexof == -1 ? MJString.EmptyString : path.substring(indexof + 1, path.length());
	}




	/**
	 * �������ݬ����???������������٣������٣��
	 * @param path �����ݬ��
	 * @return ��������٣������٣
	 **/
	public static String getFileName(String path) {
		if (MJString.isNullOrEmpty(path))
			return MJString.EmptyString;

		return new File(path).getName();
	}




	/**
	 *
	 * �������ݬ����???��������������٣������٣��
	 * @param path �����ݬ��
	 * @return ����������٣������٣
	 **/
	public static String getFileNameWithoutExtension(String path) {
		String fileName = getFileName(path);
		if (MJString.isNullOrEmpty(fileName))
			return MJString.EmptyString;

		int indexof = fileName.lastIndexOf('.');
		return indexof == -1 ? fileName : fileName.substring(0, indexof);
	}

