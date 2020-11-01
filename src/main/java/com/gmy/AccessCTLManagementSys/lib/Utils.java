package com.gmy.AccessCTLManagementSys.lib;

import com.sun.jna.Platform;
import org.springframework.boot.system.ApplicationHome;

import java.io.File;
import java.io.UnsupportedEncodingException;

public class Utils {
	public Utils() {

	}

	//加载海康HCNetSDK.dll文件的路径
	public static final String loadLibrary=HCNetSDKPath.DLL_PATH;
	public static final String loadLibrary1 =HCNetSDKPath.DLL_PATH1;

	public static class HCNetSDKPath {
		public static String DLL_PATH, DLL_PATH1;
		/*下面这个是加载dll文件的 ，也就是上面的第3步（做了第3步可以不要这个static里面的内容，但是用这个把第3步换成工具类加载更加的方便后续的维护，所以我们把第3步的加载路径换成：
          HCNetSDK INSTANCE = (HCNetSDK) Native.loadLibrary(HCNetDeviceConUtil.loadLibrary,
                HCNetSDK.class);
    */
		static {
//			String path = (HCNetSDKPath.class.getResource("/win64/dhnetsdk.dll").getPath()).replaceAll("%20", " ").substring(1).replace("/",
//					"\\");
//			String path1 = (HCNetSDKPath.class.getResource("/win64/dhconfigsdk.dll").getPath()).replaceAll("%20", " ").substring(1).replace("/",
//					"\\");
//			try {
//				DLL_PATH = java.net.URLDecoder.decode(path, "utf-8");
//				DLL_PATH1 = java.net.URLDecoder.decode(path1, "utf-8");
//				System.out.println(DLL_PATH);
//				System.out.println(DLL_PATH1);
//			} catch (UnsupportedEncodingException e) {
//				System.out.println("sdasdfaddfadf");
//				e.printStackTrace();
//			}
			ApplicationHome h = new ApplicationHome(HCNetSDKPath.class);
			File jarF = h.getSource();
			String p = jarF.getParentFile().toString();
			System.out.println(p);
			DLL_PATH = p + "\\win64\\dhnetsdk.dll";
			DLL_PATH1 = p + "\\win64\\dhconfigsdk.dll";
		}
	}
	
	// 获取操作平台信息
	public static String getOsPrefix() {
		String arch = System.getProperty("os.arch").toLowerCase();
		final String name = System.getProperty("os.name");
		String osPrefix;
		switch(Platform.getOSType()) {
			case Platform.WINDOWS: {
				if ("i386".equals(arch))
	                arch = "x86";
	            osPrefix = "win32-" + arch;
			}
            break;
			case Platform.LINUX: {
				if ("x86".equals(arch)) {
	                arch = "i386";
	            }
	            else if ("x86_64".equals(arch)) {
	                arch = "amd64";
	            }
	            osPrefix = "linux-" + arch;
			}			       
	        break;
			default: {
	            osPrefix = name.toLowerCase();
	            if ("x86".equals(arch)) {
	                arch = "i386";
	            }
	            if ("x86_64".equals(arch)) {
	                arch = "amd64";
	            }
	            int space = osPrefix.indexOf(" ");
	            if (space != -1) {
	                osPrefix = osPrefix.substring(0, space);
	            }
	            osPrefix += "-" + arch;
			}
	        break;
	       
		}

		return osPrefix;
	}	
	
    public static String getOsName() {
    	String osName = "";
		String osPrefix = getOsPrefix();
		if(osPrefix.toLowerCase().startsWith("win32-x86")
				||osPrefix.toLowerCase().startsWith("win32-amd64") ) {
			osName = "win";
		} else if(osPrefix.toLowerCase().startsWith("linux-i386")
				|| osPrefix.toLowerCase().startsWith("linux-amd64")) {
			osName = "linux";
		}
		
		return osName;
    }
    
    // 获取加载库
	public static String getLoadLibrary(String library) {
		if (isChecking()) {
			return null;
		}
		
		String loadLibrary = "";
		String osPrefix = getOsPrefix();
		if(osPrefix.toLowerCase().startsWith("win32-x86")) {
			loadLibrary = "./src/main/resources/libs/win32/";
		} else if(osPrefix.toLowerCase().startsWith("win32-amd64") ) {
			loadLibrary = "./target/classes/win64/";
		} else if(osPrefix.toLowerCase().startsWith("linux-i386")) {
			loadLibrary = "";
		}else if(osPrefix.toLowerCase().startsWith("linux-amd64")) {
			loadLibrary = "";
		}
		
		System.out.printf("[Load %s Path : %s]\n", library, loadLibrary + library);
		return loadLibrary + library;
	}
	
	private static boolean checking = false;
	public static void setChecking() {
		checking = true;
	}
	public static void clearChecking() {
		checking = false;
	}
	public static boolean isChecking() {
		return checking;
	}
}
