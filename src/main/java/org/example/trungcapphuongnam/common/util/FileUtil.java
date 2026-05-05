package org.example.trungcapphuongnam.common.util;
//dùng cho update file
public class FileUtil {

    public static String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}