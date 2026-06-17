export const danhSachChiTietTuyenSinhMacDinh = [
    {
        slug: 'thong-tin-tuyen-sinh',
        tieuDe: 'Thông tin tuyển sinh',
        moTa: 'Cập nhật thông tin chung về phương thức tuyển sinh, đối tượng xét tuyển và hình thức đăng ký tại Trường Trung cấp Phương Nam.',
        icon: 'fa-solid fa-circle-info',
        thongTinNhanh: [
            'Xét tuyển học bạ THCS hoặc THPT',
            'Tư vấn chọn ngành theo năng lực',
            'Nhận hồ sơ trực tiếp hoặc online',
            'Hỗ trợ nhập học nhanh chóng'
        ],
        noiDungChiTiet: [
            {
                tieuDe: 'Đối tượng tuyển sinh',
                icon: 'fa-solid fa-user-graduate',
                moTa: 'Nhà trường tiếp nhận hồ sơ của người học có nhu cầu theo học chương trình trung cấp chính quy.',
                danhSach: [
                    'Học sinh đã tốt nghiệp THCS.',
                    'Học sinh đã tốt nghiệp THPT.',
                    'Người học có nhu cầu học nghề, chuyển đổi nghề nghiệp.',
                    'Người học muốn vừa học văn hóa vừa học nghề theo định hướng thực hành.'
                ]
            },
            {
                tieuDe: 'Phương thức xét tuyển',
                icon: 'fa-solid fa-clipboard-check',
                moTa: 'Nhà trường áp dụng phương thức xét tuyển đơn giản, phù hợp với nhiều nhóm người học.',
                danhSach: [
                    'Xét tuyển dựa trên học bạ THCS hoặc THPT.',
                    'Tư vấn định hướng ngành học trước khi nhập học.',
                    'Tiếp nhận hồ sơ tại trường hoặc qua kênh đăng ký trực tuyến.',
                    'Thông báo kết quả xét tuyển sau khi hồ sơ hợp lệ.'
                ]
            }
        ],
        trangThaiHienThi: true
    },
    {
        slug: 'ho-so-xet-tuyen',
        tieuDe: 'Hồ sơ xét tuyển',
        moTa: 'Danh mục hồ sơ cần chuẩn bị khi đăng ký xét tuyển vào Trường Trung cấp Phương Nam.',
        icon: 'fa-solid fa-folder-open',
        thongTinNhanh: [
            'Chuẩn bị bản sao giấy tờ cá nhân',
            'Chuẩn bị học bạ hoặc bằng tốt nghiệp',
            'Có thể bổ sung giấy tờ ưu tiên nếu có',
            'Được hỗ trợ kiểm tra hồ sơ trước khi nộp'
        ],
        noiDungChiTiet: [
            {
                tieuDe: 'Giấy tờ bắt buộc',
                icon: 'fa-solid fa-file-lines',
                moTa: 'Học viên cần chuẩn bị các giấy tờ cơ bản để hoàn tất hồ sơ xét tuyển.',
                danhSach: [
                    'Phiếu đăng ký xét tuyển theo mẫu của trường.',
                    'Bản sao công chứng bằng tốt nghiệp THCS hoặc THPT.',
                    'Bản sao công chứng học bạ THCS hoặc THPT.',
                    'Bản sao CCCD/CMND.',
                    'Ảnh 3x4 theo yêu cầu hồ sơ.'
                ]
            },
            {
                tieuDe: 'Lưu ý khi nộp hồ sơ',
                icon: 'fa-solid fa-circle-check',
                moTa: 'Học viên nên kiểm tra kỹ thông tin trước khi nộp để tránh phải bổ sung nhiều lần.',
                danhSach: [
                    'Thông tin cá nhân trên giấy tờ phải thống nhất.',
                    'Bản sao công chứng còn rõ nội dung, không bị rách hoặc mờ.',
                    'Ghi đúng số điện thoại để nhà trường liên hệ khi cần.',
                    'Nên giữ lại một bản hồ sơ photo để đối chiếu.'
                ]
            }
        ],
        trangThaiHienThi: true
    },
    {
        slug: 'lich-tuyen-sinh',
        tieuDe: 'Lịch tuyển sinh',
        moTa: 'Các mốc thời gian tuyển sinh dự kiến trong năm học để học viên chủ động chuẩn bị hồ sơ.',
        icon: 'fa-solid fa-calendar-days',
        thongTinNhanh: [
            'Tuyển sinh nhiều đợt trong năm',
            'Nhận hồ sơ trực tiếp và online',
            'Có đợt bổ sung cho hồ sơ nộp muộn',
            'Thời gian có thể cập nhật theo kế hoạch nhà trường'
        ],
        noiDungChiTiet: [
            {
                tieuDe: 'Các đợt tuyển sinh dự kiến',
                icon: 'fa-solid fa-calendar-check',
                moTa: 'Nhà trường tiếp nhận hồ sơ theo nhiều đợt tuyển sinh trong năm.',
                danhSach: [
                    'Đợt 1: Nhận hồ sơ và xét tuyển đầu năm.',
                    'Đợt 2: Nhận hồ sơ và xét tuyển giữa năm.',
                    'Đợt 3: Nhận hồ sơ và xét tuyển trước năm học mới.',
                    'Đợt bổ sung: Tiếp nhận hồ sơ nếu còn chỉ tiêu.'
                ]
            },
            {
                tieuDe: 'Thông báo kết quả',
                icon: 'fa-solid fa-bell',
                moTa: 'Sau khi tiếp nhận hồ sơ hợp lệ, bộ phận tuyển sinh sẽ liên hệ học viên để thông báo kết quả.',
                danhSach: [
                    'Thông báo qua số điện thoại đã đăng ký.',
                    'Hướng dẫn bổ sung hồ sơ nếu còn thiếu.',
                    'Hướng dẫn hoàn tất thủ tục nhập học.',
                    'Thông báo lịch sinh hoạt đầu khóa nếu có.'
                ]
            }
        ],
        trangThaiHienThi: true
    },
    {
        slug: 'hoc-phi-chinh-sach',
        tieuDe: 'Học phí & chính sách',
        moTa: 'Thông tin về học phí, học bổng và các chính sách hỗ trợ người học tại Trường Trung cấp Phương Nam.',
        icon: 'fa-solid fa-hand-holding-dollar',
        thongTinNhanh: [
            'Học phí công khai, minh bạch',
            'Có chính sách hỗ trợ theo từng đối tượng',
            'Có học bổng khuyến khích học tập',
            'Có thể được tư vấn phương án đóng học phí phù hợp'
        ],
        noiDungChiTiet: [
            {
                tieuDe: 'Thông tin học phí',
                icon: 'fa-solid fa-money-bill-wave',
                moTa: 'Mức học phí được công bố theo từng ngành học, khóa học và kế hoạch đào tạo của nhà trường.',
                danhSach: [
                    'Học phí được thông báo rõ trước khi nhập học.',
                    'Mức học phí có thể khác nhau theo ngành đào tạo.',
                    'Học viên được tư vấn đầy đủ trước khi hoàn tất thủ tục nhập học.',
                    'Các khoản thu được thực hiện theo quy định của nhà trường.'
                ]
            },
            {
                tieuDe: 'Học bổng và miễn giảm',
                icon: 'fa-solid fa-award',
                moTa: 'Nhà trường có các chính sách hỗ trợ nhằm khuyến khích học viên học tập.',
                danhSach: [
                    'Học bổng cho học viên có kết quả học tập tốt.',
                    'Chính sách hỗ trợ học viên có hoàn cảnh khó khăn.',
                    'Chính sách hỗ trợ theo từng chương trình tuyển sinh.',
                    'Ưu tiên xét hỗ trợ theo hồ sơ và điều kiện thực tế.'
                ]
            }
        ],
        trangThaiHienThi: true
    },
    {
        slug: 'hoi-dap-tuyen-sinh',
        tieuDe: 'Hỏi đáp tuyển sinh',
        moTa: 'Giải đáp các câu hỏi thường gặp khi học viên và phụ huynh tìm hiểu thông tin tuyển sinh.',
        icon: 'fa-solid fa-circle-question',
        thongTinNhanh: [
            'Giải đáp điều kiện xét tuyển',
            'Giải đáp hồ sơ cần chuẩn bị',
            'Giải đáp thời gian nhập học',
            'Hỗ trợ liên hệ tư vấn trực tiếp'
        ],
        noiDungChiTiet: [
            {
                tieuDe: 'Câu hỏi thường gặp',
                icon: 'fa-solid fa-question',
                moTa: 'Một số câu hỏi phổ biến khi học viên đăng ký xét tuyển.',
                danhSach: [
                    'Trường xét tuyển theo hình thức nào?',
                    'Chưa có bằng tốt nghiệp chính thức có nộp hồ sơ được không?',
                    'Có thể đăng ký tư vấn trước khi nộp hồ sơ không?',
                    'Hồ sơ nộp muộn có được xét tuyển không?',
                    'Sau khi trúng tuyển cần làm gì?'
                ]
            },
            {
                tieuDe: 'Kênh hỗ trợ tuyển sinh',
                icon: 'fa-solid fa-phone-volume',
                moTa: 'Học viên và phụ huynh có thể liên hệ nhà trường qua các kênh tư vấn chính thức.',
                danhSach: [
                    'Liên hệ trực tiếp bộ phận tuyển sinh tại trường.',
                    'Gọi điện thoại đến số hotline tuyển sinh.',
                    'Gửi thông tin qua form liên hệ trên website.',
                    'Theo dõi thông báo mới trên trang tin tức và tuyển sinh.'
                ]
            }
        ],
        trangThaiHienThi: true
    }
]