package org.h4x0r.util;

public class FileUtils {
    public static String removeTailFileSeparator(String str){
        if (str == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer(str);
        if (sb.charAt(sb.length()-1) == '\\' || sb.charAt(sb.length()-1) == '/') {
            sb.deleteCharAt(sb.length()-1);
        }
        return sb.toString();
    }
}
