package org.example.trungcapphuongnam.common.constant.Path;

import org.example.trungcapphuongnam.common.constant.ApiPath;

public final class DaoTaoPath {
    private  DaoTaoPath() {}
    public static final String BASE = ApiPath.API + "/dao-tao";

    public static final String KHOA_DAO_TAO = BASE + "/khoa-dao-tao";
    public static final String KHUNG_KY = BASE + "/khung-ky";
    public static final String LOAI_CHUONG_TRINH = BASE + "/loai-chuong-trinh";
    public static final String LOP_HANH_CHINH = BASE + "/lop-hanh-chinh";
    public static final String NGANH = BASE + "/nganh";
    public static final String TRINH_DO_DAO_TAO = BASE + "/trinh-do-dao-tao";

    public static final String ID = "/{id}";
}
