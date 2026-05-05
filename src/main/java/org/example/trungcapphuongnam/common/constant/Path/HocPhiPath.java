package org.example.trungcapphuongnam.common.constant.Path;

import org.example.trungcapphuongnam.common.constant.ApiPath;

public final class HocPhiPath {
    private HocPhiPath() {}

    public static final String BASE = ApiPath.API + "/hoc-phi";

    public static final String HOC_PHI = BASE + "/hoc-phi";
    public static final String GIAO_DICH_HOC_PHI = BASE + "/giao-dich-hoc-phi";

    public static final String ID = "/{id}";
}
