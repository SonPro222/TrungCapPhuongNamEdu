//package org.example.trungcapphuongnam.module.daoTao.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.example.trungcapphuongnam.common.constant.Path.DaoTaoPath;
//import org.example.trungcapphuongnam.common.response.ApiResponse;
//import org.example.trungcapphuongnam.module.daoTao.dto.cauTruc.NganhCauTrucResponse;
//import org.example.trungcapphuongnam.module.daoTao.service.DaoTaoCauTrucService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping(DaoTaoPath.NGANH)
//@RequiredArgsConstructor
//public class DaoTaoCauTrucController {
//
//    private final DaoTaoCauTrucService service;
//
//    @GetMapping("/{id}/cau-truc")
//    public ResponseEntity<ApiResponse<NganhCauTrucResponse>> findCauTrucByNganhId(
//            @PathVariable Long id
//    ) {
//        return ResponseEntity.ok(
//                ApiResponse.ok(service.findCauTrucByNganhId(id))
//        );
//    }
//}