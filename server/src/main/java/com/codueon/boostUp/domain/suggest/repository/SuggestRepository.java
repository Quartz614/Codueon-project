package com.codueon.boostUp.domain.suggest.repository;

import com.codueon.boostUp.domain.suggest.entity.Suggest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SuggestRepository extends JpaRepository<Suggest, Long> , CustomSuggestRepository{
    List<Suggest> findAllById(Long lessonId);
    Optional<Suggest> findByIdAndLessonIdAndMemberId(Long lessonId, Long suggestId, Long memberId);
    Optional<Suggest> findByLessonIdAndMemberId(Long lessonId, Long memberId);
    List<Suggest> findAllByLessonIdAndMemberId(Long lessonId, Long memberId);
}
