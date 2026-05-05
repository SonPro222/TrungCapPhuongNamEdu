package org.example.trungcapphuongnam.common.constant.Path;

import org.example.trungcapphuongnam.common.constant.ApiPath;

public final class SinhVienPath {
    private SinhVienPath() {}

    public static final String BASE = ApiPath.API + "/sinh-vien";

    public static final String SINH_VIEN = BASE;
    public static final String SINH_VIEN_CHUONG_TRINH = BASE + "/chuong-trinh";
    public static final String MIEN_TRU_BAO_LUU = BASE + "/mien-tru-bao-luu";

    public static final String ID = "/{id}";
}
