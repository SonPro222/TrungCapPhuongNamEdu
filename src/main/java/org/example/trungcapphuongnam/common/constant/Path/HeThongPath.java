package org.example.trungcapphuongnam.common.constant.Path;

import org.example.trungcapphuongnam.common.constant.ApiPath;

public final class HeThongPath {
    private HeThongPath() {}

    public static final String BASE = ApiPath.API + "/he-thong";

    public static final String NHAN_VIEN = BASE + "/nhan-vien";
    public static final String NHAT_KY_DANG_NHAP = BASE + "/nhat-ky-dang-nhap";
    public static final String NHAT_KY_HE_THONG = BASE + "/nhat-ky-he-thong";
    public static final String QUYEN = BASE + "/quyen";
    public static final String TAI_KHOAN = BASE + "/tai-khoan";
    public static final String TAI_KHOAN_VAI_TRO = BASE + "/tai-khoan-vai-tro";
    public static final String VAI_TRO = BASE + "/vai-tro";
    public static final String VAI_TRO_QUYEN = BASE + "/vai-tro-quyen";

    public static final String ID = "/{id}";
}
