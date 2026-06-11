//package org.example.trungcapphuongnam.module.daoTao.service.impl;
//
//import lombok.RequiredArgsConstructor;
//import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
//import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhVersionResponse;
//import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinh;
//import org.example.trungcapphuongnam.module.chuongTrinh.mapper.ChuongTrinhMapper;
//import org.example.trungcapphuongnam.module.chuongTrinh.mapper.ChuongTrinhVersionMapper;
//import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhRepository;
//import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhVersionRepository;
//import org.example.trungcapphuongnam.module.daoTao.dto.cauTruc.ChuongTrinhTheoNganhResponse;
//import org.example.trungcapphuongnam.module.daoTao.dto.cauTruc.NganhCauTrucResponse;
//import org.example.trungcapphuongnam.module.daoTao.dto.LoaiChuongTrinhResponse;
//import org.example.trungcapphuongnam.module.daoTao.dto.TrinhDoDaoTaoResponse;
//import org.example.trungcapphuongnam.module.daoTao.entity.Nganh;
//import org.example.trungcapphuongnam.module.daoTao.mapper.LoaiChuongTrinhMapper;
//import org.example.trungcapphuongnam.module.daoTao.mapper.NganhMapper;
//import org.example.trungcapphuongnam.module.daoTao.mapper.TrinhDoDaoTaoMapper;
//import org.example.trungcapphuongnam.module.daoTao.repository.LoaiChuongTrinhRepository;
//import org.example.trungcapphuongnam.module.daoTao.repository.NganhRepository;
//import org.example.trungcapphuongnam.module.daoTao.repository.TrinhDoDaoTaoRepository;
//import org.example.trungcapphuongnam.module.daoTao.service.DaoTaoCauTrucService;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//public class DaoTaoCauTrucServiceImpl implements DaoTaoCauTrucService {
//
//    private final NganhRepository nganhRepository;
//    private final ChuongTrinhRepository chuongTrinhRepository;
//    private final ChuongTrinhVersionRepository chuongTrinhVersionRepository;
//    private final LoaiChuongTrinhRepository loaiChuongTrinhRepository;
//    private final TrinhDoDaoTaoRepository trinhDoDaoTaoRepository;
//
//    private final NganhMapper nganhMapper;
//    private final ChuongTrinhMapper chuongTrinhMapper;
//    private final ChuongTrinhVersionMapper chuongTrinhVersionMapper;
//    private final LoaiChuongTrinhMapper loaiChuongTrinhMapper;
//    private final TrinhDoDaoTaoMapper trinhDoDaoTaoMapper;
//
//    @Override
//    public NganhCauTrucResponse findCauTrucByNganhId(Long nganhId) {
//        Pageable pageable = Pageable.unpaged();
//
//        Nganh nganh = nganhRepository.findById(nganhId)
//                .orElseThrow(() -> new ResourceNotFoundException(
//                        "Ngành không tồn tại: " + nganhId
//                ));
//
//        List<ChuongTrinhTheoNganhResponse> chuongTrinh = chuongTrinhRepository
//                .findByNganhId(nganhId, pageable)
//                .getContent()
//                .stream()
//                .map(item -> buildChuongTrinhTheoNganh(item, pageable))
//                .toList();
//
//        return NganhCauTrucResponse.builder()
//                .nganh(nganhMapper.toResponse(nganh))
//                .chuongTrinh(chuongTrinh)
//                .build();
//    }
//
//    private ChuongTrinhTheoNganhResponse buildChuongTrinhTheoNganh(
//            ChuongTrinh chuongTrinh,
//            Pageable pageable
//    ) {
//        LoaiChuongTrinhResponse loaiChuongTrinh = chuongTrinh.getLoaiChuongTrinhId() == null
//                ? null
//                : loaiChuongTrinhRepository.findById(chuongTrinh.getLoaiChuongTrinhId())
//                .map(loaiChuongTrinhMapper::toResponse)
//                .orElse(null);
//
//        TrinhDoDaoTaoResponse trinhDoDaoTao = chuongTrinh.getTrinhDoId() == null
//                ? null
//                : trinhDoDaoTaoRepository.findById(chuongTrinh.getTrinhDoId())
//                .map(trinhDoDaoTaoMapper::toResponse)
//                .orElse(null);
//
//        List<ChuongTrinhVersionResponse> versions = chuongTrinhVersionRepository
//                .findByChuongTrinhId(chuongTrinh.getId(), pageable)
//                .map(chuongTrinhVersionMapper::toResponse)
//                .getContent();
//
//        return ChuongTrinhTheoNganhResponse.builder()
//                .chuongTrinh(chuongTrinhMapper.toResponse(chuongTrinh))
//                .loaiChuongTrinh(loaiChuongTrinh)
//                .trinhDoDaoTao(trinhDoDaoTao)
//                .versions(versions)
//                .build();
//    }
//}