package org.example.trungcapphuongnam.common.constant.Path;

import org.example.trungcapphuongnam.common.constant.ApiPath;

public final class ThiPath {
    private ThiPath() {}

    public static final String BASE = ApiPath.API + "/thi";

    public static final String LICH_THI = BASE + "/lich-thi";
    public static final String SINH_VIEN_DU_THI = BASE + "/sinh-vien-du-thi";
    public static final String DE_THI = BASE + "/de-thi";
    public static final String BAI_THI_SINH_VIEN = BASE + "/bai-thi-sinh-vien";

    public static final String ID = "/{id}";
}
