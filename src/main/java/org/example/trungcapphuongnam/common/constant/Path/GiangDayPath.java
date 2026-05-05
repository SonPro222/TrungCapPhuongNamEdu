package org.example.trungcapphuongnam.common.constant.Path;

import org.example.trungcapphuongnam.common.constant.ApiPath;

public final class GiangDayPath {
    private GiangDayPath() {}

    public static final String BASE = ApiPath.API + "/giang-day";

    public static final String GIAO_VIEN = BASE + "/giao-vien";
    public static final String PHONG_HOC = BASE + "/phong-hoc";
    public static final String CA_HOC = BASE + "/ca-hoc";
    public static final String LOP_HOC_PHAN = BASE + "/lop-hoc-phan";
    public static final String PHAN_CONG_GIANG_DAY = BASE + "/phan-cong-giang-day";
    public static final String SINH_VIEN_LOP_HOC_PHAN = BASE + "/sinh-vien-lop-hoc-phan";
    public static final String LICH_HOC = BASE + "/lich-hoc";
    public static final String YEU_CAU_DOI_LICH = BASE + "/yeu-cau-doi-lich";
    public static final String DIEM_DANH = BASE + "/diem-danh";

    public static final String ID = "/{id}";
}
