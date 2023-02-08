package com.codueon.boostUp.domain.lesson.controller;

import com.codueon.boostUp.domain.dto.MultiResponseDto;
import com.codueon.boostUp.domain.lesson.dto.get.GetLesson;
import com.codueon.boostUp.domain.lesson.dto.get.GetLessonCurriculum;
import com.codueon.boostUp.domain.lesson.dto.get.GetLessonInfo;
import com.codueon.boostUp.domain.lesson.dto.get.GetMainPageLesson;
import com.codueon.boostUp.domain.lesson.dto.patch.PatchLessonCurriculum;
import com.codueon.boostUp.domain.lesson.dto.patch.PostLessonDetailEdit;
import com.codueon.boostUp.domain.lesson.dto.patch.PostLessonInfoEdit;
import com.codueon.boostUp.domain.lesson.dto.post.PostLesson;
import com.codueon.boostUp.domain.lesson.dto.post.PostSearchLesson;
import com.codueon.boostUp.domain.lesson.dto.utils.WrapLessonId;
import com.codueon.boostUp.domain.lesson.service.LessonService;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lesson")
public class LessonController {
    private final LessonService lessonService;

    /**
     * 과외 등록 컨트롤러
     * @param postLesson 과외 등록
     * @param profileImage 프로필 이미지
     * @param careerImage 경력 이미지
     * @return ResponseEntity
     * @throws Exception
     * @author Quartz614
     */
    @PostMapping(value = "/registration")
    public ResponseEntity<?> postLesson(@RequestPart(value = "data") @Valid PostLesson postLesson,
                                        Authentication authentication,
                                        @RequestPart(required = false, value = "profileImage") MultipartFile profileImage,
                                        @RequestPart(required = false, value = "careerImage") List<MultipartFile> careerImage) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);
        lessonService.createLessonS3(postLesson, memberId, profileImage, careerImage);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 과외 요약 정보 수정 컨트롤러
     * @param lessonId 과외 식별자
     * @param postLessonInfoEdit 수정 과외 요약 정보
     * @param profileImage 프로필 이미지
     * @return ResponseEntity
     * @author Quatz614
     */
    @SneakyThrows
    @PostMapping("/{lesson-id}/modification")
    public ResponseEntity updateLesson(@PathVariable("lesson-id") Long lessonId,
                                       @RequestPart(value = "data") @Valid PostLessonInfoEdit postLessonInfoEdit,
                                       Authentication authentication,
                                       @RequestPart(required = false, value = "profileImage") MultipartFile profileImage) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        lessonService.updateLessonInfoS3(lessonId, postLessonInfoEdit, profileImage);
        return ResponseEntity.ok().build();
    }

    /**
     * 과외 상세 정보 수정 컨트롤러
     * @param lessonId 과외 식별자
     * @param postLessonDetailEdit 수정 과외 상세 정보
     * @param careerImage 경력 이미지
     * @return ResponseEntity
     * @author Quartz614
     */
    @SneakyThrows
    @PostMapping("/{lesson-id}/detailInfo/modification")
    public ResponseEntity updateLessonDetail(@PathVariable("lesson-id") Long lessonId,
                                             @RequestPart(value = "data") @Valid PostLessonDetailEdit postLessonDetailEdit,
                                             @RequestPart(required = false, value = "careerImage") List<MultipartFile> careerImage) {
        lessonService.updateLessonDetailS3(lessonId, postLessonDetailEdit, careerImage);
        return ResponseEntity.ok().build();
    }

    /**
     * 과외 진행 방식 정보 수정 컨트롤러
     * @param lessonId 과외 식별자
     * @param patchLessonCurriculum 과외 진행 방식 정보
     * @return ResponseEntity
     * @author Quartz614
     */
    @PatchMapping("/{lesson-id}/curriculum/modification")
    public ResponseEntity updateCurriculum(@PathVariable("lesson-id") Long lessonId,
                                           @RequestBody PatchLessonCurriculum patchLessonCurriculum) {
        lessonService.updateCurriculum(lessonId, patchLessonCurriculum);
        return ResponseEntity.ok().build();
    }

    /**
     * 과외 삭제 컨트롤러
     * @param lessonId 과외 식별자
     * @return ResponseEntity
     * @author Quartz614
     */
    @DeleteMapping(value = "/{lesson-id}")
    public ResponseEntity deleteLesson(@PathVariable("lesson-id") Long lessonId,
                                       Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);
        lessonService.deleteLessonS3(memberId, lessonId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 등록한 과외 마이페이지 조회 컨트롤러
     *
     * @return ResponseEntity
     * @author Quartz614
     */
    @GetMapping(value = "/tutor")
    public ResponseEntity<WrapLessonId> getLessonMypage(Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);

        return ResponseEntity.ok().body(new WrapLessonId(lessonService.getLessonMypage(memberId)));
    }

    /**
     * 메인페이지 과외 전체 조회 컨트롤러 메서드
     * @param pageable 페이지 정보
     * @return Page(GetMainPageLesson)
     * @author mozzi327
     */
    @GetMapping
    public ResponseEntity<?> getMainPageLessonInfos(Pageable pageable,
                                                    Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);

        Page<GetMainPageLesson> response = lessonService.getMainPageLessons(memberId, pageable);
        return ResponseEntity.ok().body(new MultiResponseDto<>(response));
    }

    /**
     * 메인페이지 상세 검색 컨트롤러 메서드
     * @param postSearchLesson 상세 검색 정보
     * @param pageable 페이지 정보
     * @return Page(GetMainPageLesson)
     * @author mozzi327
     */
    @PostMapping("/search")
    public ResponseEntity<?> getDetailSearchForLesson(@RequestBody @Valid PostSearchLesson postSearchLesson,
                                                      Pageable pageable,
                                                      Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);

        Page<GetMainPageLesson> response = lessonService.getDetailSearchLessons(memberId, postSearchLesson, pageable);
        return ResponseEntity.ok().body(new MultiResponseDto<>(response));
    }

    /**
     * 메인페이지 언어 별 과외 조회 컨트롤러 메서드
     * @param languageId 사용 언어 식별자
     * @param pageable 페이지 정보
     * @return Page(GetMainPageLesson)
     * @author mozzi327
     */
    @GetMapping("/language/{language-id}")
    public ResponseEntity<?> getLessonByLanguage(@PathVariable("language-id") Integer languageId,
                                                 Pageable pageable,
                                                 Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);

        Page<GetMainPageLesson> response = lessonService.getMainPageLessonsAboutLanguage(memberId, languageId, pageable);
        return ResponseEntity.ok().body(new MultiResponseDto<>(response));
    }

    /**
     * 과외 상세 페이지 요약 정보 조회 컨트롤러 메서드
     * @param lessonId 과외 식별자
     * @return GetLesson
     * @author mozzi327
     */
    @GetMapping("/{lesson-id}")
    public ResponseEntity<?> getLesson(@PathVariable("lesson-id") Long lessonId,
                                       Authentication authentication) {

        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);

        GetLesson response = lessonService.getDetailLesson(lessonId, memberId);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 과외 상세 페이지 상세 정보 조회 컨트롤러 메서드
     * @param lessonId
     * @return GetLessonInfo
     * @author mozzi327
     */
    @GetMapping("/{lesson-id}/detailInfo")
    public ResponseEntity<?> getLessonInfo(@PathVariable("lesson-id") Long lessonId) {
        GetLessonInfo response = lessonService.getDetailLessonInfo(lessonId);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 과외 상세 페이지 커리큘럼 정보 조회 컨트롤러 메서드
     * @param lessonId
     * @return GetLessonCurriculum
     * @author mozzi327
     */
    @GetMapping("/{lesson-id}/curriculum")
    public ResponseEntity<?> getCurriculum(@PathVariable("lesson-id") Long lessonId) {
        GetLessonCurriculum response = lessonService.getDetailLessonCurriculum(lessonId);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 로그인 확인 메서드
     * @param token 토큰 정보
     * @return Long
     * @author mozzi327
     */
    private Long getMemberIdIfExistToken(JwtAuthenticationToken token) {
        if (token == null) return null;
        else return token.getId();
    }
}
