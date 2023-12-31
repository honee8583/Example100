package com.example.example100.notice.controller;

import com.example.example100.error.ErrorResponse;
import com.example.example100.notice.entity.Notice;
import com.example.example100.notice.exception.AlreadyDeletedException;
import com.example.example100.notice.exception.DuplicateNoticeException;
import com.example.example100.notice.exception.NoticeNotFoundException;
import com.example.example100.notice.model.NoticeDeleteInput;
import com.example.example100.notice.model.NoticeDto;
import com.example.example100.notice.model.NoticeInput;
import com.example.example100.notice.repository.NoticeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Notice API", description = "공지사항 API 입니다.")
@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeRepository noticeRepository;

    /**
     * 6. "공지사항입니다." 문구를 리턴하는 api를 작성하시오.
     */
    @Operation(summary = "문제6", description = "Q. \"공지사항입니다.\" 문구를 리턴하는 api를 작성하시오.")
//    @Parameter(name = "str", description = "2번 반복할 문자열")
    @GetMapping("/api/notice")
    public String getNotice() {
        return "공지사항입니다.";
    }

    /**
     * 7. 공지사항 게시판의 내용을 추상화한 모델(게시글ID, 제목, 내용, 등록일)이며
     * 데이터는 (게시글 ID = 1, 제목 = 공지사항입니다, 내용 = 공지사항 내용입니다, 등록일 = 2021-01-31) 리턴
     */
    @Operation(summary = "문제7", description = "Q. 공지사항 게시판의 내용을 추상화한 모델(게시글ID, 제목, 내용, 등록일)이며\n"
            + "데이터는 (게시글 ID = 1, 제목 = 공지사항입니다, 내용 = 공지사항 내용입니다, 등록일 = 2021-01-31) 리턴")
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
    @Operation(summary = "문제8", description = "Q. 공지사항 게시판의 내용을 추상화한 모델(게시글ID, 제목, 내용, 등록일)을 복수형태로 리턴하는 api를 작성하시오.")
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
    @Operation(summary = "문제9", description = "Q. 빈 공지사항 데이터 리스트를 반환하는 api를 작성하시오.")
    @GetMapping("/api/notice4")
    public List<NoticeDto> getNoticeList2() {
        List<NoticeDto> noticeDtos = new ArrayList<>();

        return noticeDtos;
    }

    /**
     * 10. 공지사항 게시판의 목록중 전체 개수 정보에 대한 요청을 처리하는 api를 작성하시오.
     */
    @Operation(summary = "문제10", description = "Q. 공지사항 게시판의 목록중 전체 개수 정보에 대한 요청을 처리하는 api를 작성하시오.")
    @GetMapping("/api/notice/count")
    public Integer noticeCount() {
        return 10;
    }

    /**
     * 11. 공지사항에 글을 등록하기 위한 api를 작성하시오.
     * 제목, 내용을 입력받고 파라미터는 추상화하지 않고 기본데이터 타입 형태로 받는다.
     * 리턴값은 입력된 형태에 게시글 ID(1)을 추가한 모델형태로 반환한다.
     */
    @Operation(summary = "문제11", description = "Q. 공지사항에 글을 등록하기 위한 api를 작성하시오.\n"
            + "- 제목, 내용을 입력받고 파라미터는 추상화하지 않고 기본데이터 타입 형태로 받는다.\n"
            + "- 리턴값은 입력된 형태에 게시글 ID(1)을 추가한 모델형태로 반환한다.")
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
    @Operation(summary = "문제12", description = "Q. 공지사항에 글을 등록하기 위한 api를 작성하시오.\n"
            + "- 파라미터는 추상화한 모델로 입력받는다.")
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
    @Operation(summary = "문제13", description = "Q. 공지사항에 글을 등록하기 위한 api를 작성하시오.\n"
            + "- application/json 형태로 입력받으시오.")
    @PostMapping("/api/notice3")
    public NoticeDto registerNotice3(@RequestBody NoticeDto noticeDto) {
        noticeDto.setId(2L);
        noticeDto.setRegDate(LocalDateTime.now());
        return noticeDto;
    }

    /**
     * 14. 데이터베이스에 공지사항을 등록하는 api를 작성하시오.
     * 데이터베이스는 h2를 사용.
     * 전달된 값을 저장하기 위한 repository와 Entity를 통해서 Database에 저장.
     * id값이 저장된 Entity를 리턴.
     */
    @Operation(summary = "문제14", description = "Q. 데이터베이스에 공지사항을 등록하는 api를 작성하시오.\n"
            + "- 데이터베이스는 h2를 사용.\n"
            + "- 전달된 값을 저장하기 위한 repository와 Entity를 통해서 Database에 저장.\n"
            + "- id값이 저장된 Entity를 리턴.")
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
    @Operation(summary = "문제15", description = "Q. 공지사항을 등록한는 api를 작성하시오.\n"
            + "- 공지사항 조회수와 좋아요수의 초기값은 0으로 저장.\n"
            + "- id값이 포함된 Entity를 리턴.")
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
    @Operation(summary = "문제16", description = "Q. 공지사항 글을 수정하기 위해 상세정보를 요청하는 api를 작성하시오.\n"
            + "- 요청주소의 공지사항 ID는 동적으로 변하게 지정.\n"
            + "- 공지사항이 존재하지 않을 경우 null을 리턴.")
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
    @Operation(summary = "문제17", description = "Q. 공지사항 글을 수정하기 위한 api를 작성하시오.\n"
            + "- json형식으로 제목, 내용을 입력받고 url을 통해 공지사항 ID를 요청.\n"
            + "- 수정일은 현재시간을 저장, 조회수와 좋아요수는 변경x.")
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
    @Operation(summary = "문제18", description = "Q. 공지사항 글을 수정하기 위한 api를 작성하시오.\n"
            + "- 게시글이 존재하지 않을 경우 Exception을 발생시킨다.\n"
            + "- ExceptionHandler를 통해 구현하고 발생하는 예외에 대해서는 400에러, 예외 메시지를 리턴한다.")
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
    @Operation(summary = "문제19", description = "Q. 공지사항 글을 수정하기 위한 api를 작성하시오.\n"
            + "- 존재하지 않는 게시글일경우 예외 발생.\n"
            + "- 제목, 내용, 수정일 수정.")
    @PutMapping("/api/notice3/{id}")
    public void updateNotice3(@PathVariable Long id, @RequestBody NoticeInput noticeInput) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NoticeNotFoundException("존재하지 않는 공지사항입니다."));

        notice.setTitle(noticeInput.getTitle());
        notice.setContent(noticeInput.getContent());
        notice.setUpdateDate(LocalDateTime.now());
        noticeRepository.save(notice);
    }

    /**
     * 20. 공지사항의 조회수를 증가시키는 api를 작성하시오.
     */
    @Operation(summary = "문제20", description = "Q. 공지사항의 조회수를 증가시키는 api를 작성하시오.")
    @PatchMapping("/api/notice/{id}/hits")
    public void increaseNoticeHits(@PathVariable Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NoticeNotFoundException("존재하지 않는 공지사항입니다."));

        notice.setHits(notice.getHits() + 1);
        noticeRepository.save(notice);
    }

    /**
     * 21. 공지사항을 삭제하기 위한 api를 작성하시오.
     */
    @Operation(summary = "문제21", description = "Q. 공지사항을 삭제하기 위한 api를 작성하시오.")
    @DeleteMapping("/api/notice/{id}")
    public void deleteNotice(@PathVariable Long id) {
        Optional<Notice> optionalNotice = noticeRepository.findById(id);
        if (optionalNotice.isPresent()) {
            noticeRepository.delete(optionalNotice.get());
        }
    }

    /**
     * 22. 공지사항을 삭제하기 위한 api를 작성하시오.
     * 공지사항이 존재하지 않을 경우 예외를 발생하시오.
     */
    @Operation(summary = "문제22", description = "Q. 공지사항을 삭제하기 위한 api를 작성하시오.\n"
            + "- 공지사항이 존재하지 않을 경우 예외를 발생하시오.")
    @DeleteMapping("/api/notice2/{id}")
    public void deleteNotice2(@PathVariable Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NoticeNotFoundException("존재하지 않는 게시글입니다."));
        noticeRepository.delete(notice);
    }

    /**
     * 23. 공지사항을 삭제하기 위한 api를 작성하시오.
     * 공지사항 글을 물리적으로 삭제하지 않고 삭제 플래그값을 이용하여 삭제.
     * 삭제시간은 현재시간으로 설정한다.
     * 공지사항이 이미 삭제된 경우에는 200 코드와 "이미 삭제된 글입니다." 문구를 리턴한다.
     */
    @Operation(summary = "문제23", description = "Q. 공지사항을 삭제하기 위한 api를 작성하시오.\n"
            + "- 공지사항 글을 물리적으로 삭제하지 않고 삭제 플래그값을 이용하여 삭제.\n"
            + "- 삭제시간은 현재시간으로 설정한다.\n"
            + "- 공지사항이 이미 삭제된 경우에는 200 코드와 \"이미 삭제된 글입니다.\" 문구를 리턴한다.")
    @DeleteMapping("/api/notice3/{id}")
    public void deleteNotice3(@PathVariable Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NoticeNotFoundException("존재하지 않는 게시글입니다."));

        if (notice.isDeleted()) {
            throw new AlreadyDeletedException("이미 삭제된 글입니다.");
        }

        notice.setDeleted(true);
        notice.setDeletedDate(LocalDateTime.now());
        noticeRepository.save(notice);
    }

    @ExceptionHandler(AlreadyDeletedException.class)
    public ResponseEntity<String> alreadyDeletedExceptionHandler(AlreadyDeletedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
    }

    /**
     * 24. 여러개의 공지사항을 삭제하기 위한 api를 작성하시오.
     * 삭제할 공지사항의 id값들을 추상화한 모델로 입력.
     * application/json 형태로 입력.
     */
    @Operation(summary = "문제24", description = "Q. 여러개의 공지사항을 삭제하기 위한 api를 작성하시오.\n"
            + "- 삭제할 공지사항의 id값들을 추상화한 모델로 입력.\n"
            + "- application/json 형태로 입력.")
    @DeleteMapping("/api/notice")
    public void deleteNotices(@RequestBody NoticeDeleteInput noticeDeleteInput) {
        List<Notice> notices = noticeRepository.findByIdIn(noticeDeleteInput.getIds())
                .orElseThrow(() -> new NoticeNotFoundException("존재하지 않는 게시글입니다."));
        notices.stream().forEach(notice -> {
            notice.setDeleted(true);
            notice.setDeletedDate(LocalDateTime.now());
        });
        noticeRepository.saveAll(notices);
    }

    /**
     * 25. 모든 공지사항 데이터를 삭제하기 위한 api를 작성하시오.
     */
    @Operation(summary = "문제25", description = "Q. 모든 공지사항 데이터를 삭제하기 위한 api를 작성하시오.")
    @DeleteMapping("/api/notice/all")
    public void deleteAllNotices() {
        noticeRepository.deleteAll();
    }

    /**
     * 26. 공지사항을 등록하는 api를 작성하시오.
     * 이슈 : data.sql로 id값을 직접추가해준다면 JPA를 이용해서 데이터를 추가할 때 id값이 1부터 시작해 중복될 수 있음.
     * 해결 : data.sql 파일에서 id값을 지정하지 않는다.
     */
    @Operation(summary = "문제26", description = "Q. 공지사항을 등록하는 api를 작성하시오.\n"
            + "- 이슈 : data.sql로 id값을 직접추가해준다면 JPA를 이용해서 데이터를 추가할 때 id값이 1부터 시작해 중복될 수 있음.\n"
            + "- 해결 : data.sql 파일에서 id값을 지정하지 않는다.")
    @PostMapping("/api/notice/register")
    public void addNotice(@RequestBody NoticeInput noticeInput) {
        Notice notice = Notice.builder()
                .title(noticeInput.getTitle())
                .content(noticeInput.getContent())
                .hits(0)
                .likes(0)
                .regDate(LocalDateTime.now())
                .build();
        noticeRepository.save(notice);
    }

    /**
     * 27. 공지사항을 등록하는 api를 작성하시오.
     * 제목과 내용이 입력되지 않은 경우 400리턴.
     * 예외발생시 에러를 취합하여 콜렉션형태로 리턴.
     */
    @Operation(summary = "문제27", description = "Q. 공지사항을 등록하는 api를 작성하시오.\n"
            + "- 제목과 내용이 입력되지 않은 경우 400리턴.\n"
            + "- 예외발생시 에러를 취합하여 콜렉션형태로 리턴.")
    @PostMapping("/api/notice/register2")
    public ResponseEntity<?> addNotice2(@RequestBody @Valid NoticeInput noticeInput, Errors errors) {
        if (errors.hasErrors()) {
            List<ErrorResponse> errorResponses = new ArrayList<>();
            errors.getAllErrors().stream().forEach(e ->
                    errorResponses.add(ErrorResponse.of((FieldError)e)));
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
        }

        noticeRepository.save(Notice.builder()
                .title(noticeInput.getTitle())
                .content(noticeInput.getContent())
                .hits(0)
                .likes(0)
                .build());
        return ResponseEntity.ok().build();
    }

    /**
     * 28. 공지사항을 등록하는 api를 작성하시오.
     * 제목의 경우 10~100자 사이.
     * 내용의 경우 50~1000자 사이.
     */
    @Operation(summary = "문제28", description = "Q. 공지사항을 등록하는 api를 작성하시오.\n"
            + "- 제목의 경우 10~100자 사이.\n"
            + "- 내용의 경우 50~1000자 사이.")
    @PostMapping("/api/notice/register3")
    public ResponseEntity<?> addNotice3(@RequestBody @Valid NoticeInput noticeInput, Errors errors) {
        if (errors.hasErrors()) {
            List<ErrorResponse> errorResponses = new ArrayList<>();
            errors.getAllErrors().stream().forEach(e ->
                    errorResponses.add(ErrorResponse.of((FieldError)e)));
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
        }

        noticeRepository.save(Notice.builder()
                .title(noticeInput.getTitle())
                .content(noticeInput.getContent())
                .hits(0)
                .likes(0)
                .build());
        return ResponseEntity.ok().build();
    }

    /**
     * 29. 데이터베이스에서 파라미터로 전달된 개수만큼 최근 공지사항을 리턴하는 api를 작성하시오.
     */
    @Operation(summary = "문제29", description = "Q. 데이터베이스에서 파라미터로 전달된 개수만큼 최근 공지사항을 리턴하는 api를 작성하시오.")
    @GetMapping("/api/notices/latest/{size}")
    public Page<Notice> latestNotices(@PathVariable int size) {
        return noticeRepository.findAll(
                PageRequest.of(0, size, Direction.DESC, "regDate"));
    }

    /**
     * 30. 공지사항을 등록한 이후에 바로 동일한 제목과 내용의 공지사항을 등록하는 경우 등록을 막는 api를 작성하시오.
     * 제목, 내용 일치 및 등록일간의 차이가 1분이내일경우 중복으로 판단.
     */
    @Operation(summary = "문제30", description = "Q. 공지사항을 등록한 이후에 바로 동일한 제목과 내용의 공지사항을 등록하는 경우 등록을 막는 api를 작성하시오.\n"
            + "- 제목, 내용 일치 및 등록일간의 차이가 1분이내일경우 중복으로 판단.")
    @PostMapping("/api/notice/register4")
    public void addNotice4(@RequestBody NoticeInput noticeInput) {
        /*
        // 데이터가 많을 경우 불리
        Optional<List<Notice>> notices = noticeRepository.findByTitleAndContentAndRegDateIsGreaterThanEqual(
                noticeInput.getTitle(),
                noticeInput.getContent(),
                LocalDateTime.now().minusMinutes(1));

        if (notices.isPresent()) {
            if (notices.get().size() > 0) {
                throw new DuplicateNoticeException("1분 이내에 등록된 동일한 공지사항이 존재합니다.");
            }
        }
        */

        int count = noticeRepository.countByTitleAndContentAndRegDateIsGreaterThanEqual(
                noticeInput.getTitle(),
                noticeInput.getContent(),
                LocalDateTime.now().minusMinutes(1));

        if (count > 0) {
            throw new DuplicateNoticeException("1분 이내에 등록된 동일한 공지사항이 존재합니다.");
        }

        noticeRepository.save(Notice.builder()
                .title(noticeInput.getTitle())
                .content(noticeInput.getContent())
                .hits(0)
                .likes(0)
                .regDate(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(DuplicateNoticeException.class)
    public ResponseEntity<?> duplicateNoticeExceptionHandler(DuplicateNoticeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
