

public class GlobalData {

	private static String data;
	private static String path;
	public static boolean isPath = false; 
	
	
	
	public static String getPath() {
		return path;
	}

	public static void setPath(String path) {
		GlobalData.path = path;
	}

	public static String getData() {
		return data;
	}

	public static void setData(String data) {
		GlobalData.data = data;
	}
	

}
