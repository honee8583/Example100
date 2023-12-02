package com.example.example100.notice.controller;

import com.example.example100.notice.entity.Notice;
import com.example.example100.notice.exception.NoticeNotFoundException;
import com.example.example100.notice.model.NoticeDto;
import com.example.example100.notice.model.NoticeInput;
import com.example.example100.notice.repository.NoticeRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeRepository noticeRepository;

    /**
     * 6. "공지사항입니다." 문구를 리턴하는 api를 작성하시오.
     */
    @GetMapping("/api/notice")
    public String getNotice() {
        return "공지사항입니다.";
    }

    /**
     * 7. 공지사항 게시판의 내용을 추상화한 모델(게시글ID, 제목, 내용, 등록일)이며
     * 데이터는 (게시글 ID = 1, 제목 = 공지사항입니다, 내용 = 공지사항 내용입니다, 등록일 = 2021-01-31) 리턴
     */
    @GetMapping("/api/notice2")
    public NoticeDto getNotice2() {
        return NoticeDto.builder()
                .id(1L)
                .title("공지사항입니다.")
                .content("공지사항 내용입니다.")
                .regDate(LocalDateTime.of(2021, 1, 31, 17, 17))
                .build();
    }

    /**
     * 8. 공지사항 게시판의 내용을 추상화한 모델(게시글ID, 제목, 내용, 등록일)을 복수형태로 리턴하는 api를 작성하시오.
     */
    @GetMapping("/api/notice3")
    public List<NoticeDto> getNoticeList1() {
        List<NoticeDto> noticeDtos = new ArrayList<>();
        NoticeDto noticeDto1 = NoticeDto.builder()
                .id(1L)
                .title("공지사항입니다.")
                .content("공지사항 내용입니다.")
                .regDate(LocalDateTime.of(2021, 1, 31, 17, 17))
                .build();
        NoticeDto noticeDto2 = NoticeDto.builder()
                .id(2L)
                .title("공지사항입니다.")
                .content("공지사항 내용입니다.")
                .regDate(LocalDateTime.of(2021, 2, 1, 17, 17))
                .build();
        noticeDtos.add(noticeDto1);
        noticeDtos.add(noticeDto2);
        return noticeDtos;
    }

    /**
     * 9. 빈 공지사항 데이터 리스트를 반환하는 api를 작성하시오.
     */
    @GetMapping("/api/notice4")
    public List<NoticeDto> getNoticeList2() {
        List<NoticeDto> noticeDtos = new ArrayList<>();

        return noticeDtos;
    }

    /**
     * 10. 공지사항 게시판의 목록중 전체 개수 정보에 대한 요청을 처리하는 api를 작성하시오.
     */
    @GetMapping("/api/notice/count")
    public Integer noticeCount() {
        return 10;
    }

    /**
     * 11. 공지사항에 글을 등록하기 위한 api를 작성하시오.
     * 제목, 내용을 입력받고 파라미터는 추상화하지 않고 기본데이터 타입 형태로 받는다.
     * 리턴값은 입력된 형태에 게시글 ID(1)을 추가한 모델형태로 반환한다.
     */
    @PostMapping("/api/notice")
    public NoticeDto registerNotice(@RequestParam String title, @RequestParam String content) {
        return NoticeDto.builder()
                .id(1L)
                .title(title)
                .content(content)
                .regDate(LocalDateTime.now())
                .build();
    }

    /**
     * 12. 공지사항에 글을 등록하기 위한 api를 작성하시오.
     * 파라미터는 추상화한 모델로 입력받는다.
     */
    @PostMapping("/api/notice2")
    public NoticeDto registerNotice2(NoticeDto noticeDto) {
        noticeDto.setId(1L);
        noticeDto.setRegDate(LocalDateTime.now());
        return noticeDto;
    }

    /**
     * 13. 공지사항에 글을 등록하기 위한 api를 작성하시오.
     * application/json 형태로 입력받으시오.
     */
    @PostMapping("/api/notice3")
    public NoticeDto registerNotice3(@RequestBody NoticeDto noticeDto) {
        noticeDto.setId(2L);
        noticeDto.setRegDate(LocalDateTime.now());
        return noticeDto;
    }

    /**
     * 14. 데이터베이스에 공지사항을 등록한는 api를 작성하시오.
     * 데이터베이스는 h2를 사용.
     * 전달된 값을 저장하기 위한 repository와 Entity를 통해서 Database에 저장.
     * id값이 저장된 Entity를 리턴.
     */
    @PostMapping("/api/notice4")
    public Notice registerNotice4(@RequestBody NoticeInput noticeInput) {
        Notice notice = Notice.builder()
                .title(noticeInput.getTitle())
                .content(noticeInput.getContent())
                .regDate(LocalDateTime.now())
                .build();

        return noticeRepository.save(notice);
    }

    /**
     * 15. 공지사항을 등록한는 api를 작성하시오.
     * 공지사항 조회수와 좋아요수의 초기값은 0으로 저장.
     * id값이 포함된 Entity를 리턴.
     */
    @PostMapping("/api/notice5")
    public Notice registerNotice5(@RequestBody NoticeInput noticeInput) {
        Notice notice = Notice.builder()
                .title(noticeInput.getTitle())
                .content(noticeInput.getContent())
                .regDate(LocalDateTime.now())
                .hits(0)
                .likes(0)
                .build();

        return noticeRepository.save(notice);
    }

    /**
     * 16. 공지사항 글을 수정하기 위해 상세정보를 요청하는 api를 작성하시오.
     * 요청주소의 공지사항 ID는 동적으로 변하게 지정.
     * 공지사항이 존재하지 않을 경우 null을 리턴.
     */
    @GetMapping("/api/notice/{id}")
    public Notice getNotice(@PathVariable Long id) {
        Optional<Notice> optionalNotice = noticeRepository.findById(id);
        if (optionalNotice.isPresent()) {
            return optionalNotice.get();
        }
        return null;
    }

    /**
     * 17. 공지사항 글을 수정하기 위한 api를 작성하시오.
     * json형식으로 제목, 내용을 입력받고 url을 통해 공지사항 ID를 요청.
     * 수정일은 현재시간을 저장, 조회수와 좋아요수는 변경x.
     */
    @PutMapping("/api/notice/{id}")
    public void updateNotice(@PathVariable Long id, @RequestBody NoticeInput noticeInput) {
        Optional<Notice> optionalNotice = noticeRepository.findById(id);
        if (optionalNotice.isPresent()) {
            Notice notice = optionalNotice.get();
            notice.setTitle(noticeInput.getTitle());
            notice.setContent(noticeInput.getContent());
            notice.setUpdateDate(LocalDateTime.now());
            noticeRepository.save(notice);
        }
    }

    /**
     * 18. 공지사항 글을 수정하기 위한 api를 작성하시오.
     * 게시글이 존재하지 않을 경우 Exception을 발생시킨다.
     * ExceptionHandler를 통해 구현하고 발생하는 예외에 대해서는 400에러, 예외 메시지를 리턴한다.
     */
    @PutMapping("/api/notice2/{id}")
    public void updateNotice2(@PathVariable Long id, @RequestBody NoticeInput noticeInput) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NoticeNotFoundException("존재하지 않는 공지사항입니다."));

        notice.setTitle(noticeInput.getTitle());
        notice.setContent(noticeInput.getContent());
        notice.setUpdateDate(LocalDateTime.now());
        noticeRepository.save(notice);
    }

    @ExceptionHandler(NoticeNotFoundException.class)
    public ResponseEntity<String> NoticeNotFoundExceptionHandler(NoticeNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * 19. 공지사항 글을 수정하기 위한 api를 작성하시오.
     * 존재하지 않는 게시글일경우 예외 발생.
     * 제목, 내용, 수정일 수정.
     */
    @PutMapping("/api/notice3/{id}")
    public void updateNotice3(@PathVariable Long id, @RequestBody NoticeInput noticeInput) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NoticeNotFoundException("존재하지 않는 공지사항입니다."));

        notice.setTitle(noticeInput.getTitle());
        notice.setContent(noticeInput.getContent());
        notice.setUpdateDate(LocalDateTime.now());
        noticeRepository.save(notice);
    }
}
