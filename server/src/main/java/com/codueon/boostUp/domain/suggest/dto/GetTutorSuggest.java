package com.codueon.boostUp.domain.suggest.dto;

import com.codueon.boostUp.domain.suggest.entity.Suggest;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetTutorSuggest {

    private Long suggestId;

    private Long lessonId;

    private String name;

    private String days;

    private String languages;

    private String requests;

    private String status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Builder
    @QueryProjection
    public GetTutorSuggest(Suggest suggest,
                           Long lessonId,
                           String name) {
        this.suggestId = suggest.getId();
        this.lessonId = lessonId;
        this.name = name;
        this.days = suggest.getDays();
        this.languages = suggest.getLanguages();
        this.requests = suggest.getRequests();
        this.status = suggest.getStatus().getStatus();
        this.startTime = suggest.getStartTime();
        this.endTime = suggest.getEndTime();
    }
}