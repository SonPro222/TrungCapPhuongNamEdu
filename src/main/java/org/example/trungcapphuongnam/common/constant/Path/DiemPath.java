package org.example.trungcapphuongnam.common.constant.Path;

import org.example.trungcapphuongnam.common.constant.ApiPath;

public final class DiemPath {
    private  DiemPath() {}
    public static final String BASE = ApiPath.API + "/diem";

    public static final String BAI_KIEM_TRA = BASE + "/bai-kiem-tra";
    public static final String BAI_NOP_SINH_VIEN = BASE + "/bai-nop-sinh-vien";
    public static final String CAU_HINH_DANH_GIA = BASE + "/cau-hinh-danh-gia";
    public static final String DIEM_CHI_TIET = BASE + "/diem-chi-tiet";
    public static final String KET_QUA_LOP_HOC_PHAN = BASE + "/ket-qua-lop-hoc-phan";
    public static final String KET_QUA_MON_HOC = BASE + "/ket-qua-mon-hoc";

    public static final String ID = "/{id}";
}
