package com.codueon.boostUp.domain.suggest.contoller;

import com.codueon.boostUp.domain.suggest.dto.PostSuggest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SuggestPostTest extends SuggestControllerTest{

    @Test
    @DisplayName("신청 프로세스 1 - 과외 신청")
    void createSuggest() throws Exception{

        Long lessonId = 1L;

        PostSuggest post = PostSuggest.builder()
                .days("월, 수, 금")
                .languages("Java")
                .requests("누워서 수업 들어도 되나요?")
                .build();

        String content = gson.toJson(post);

        doNothing().when(suggestService).createSuggest(Mockito.any(), Mockito.anyLong(), Mockito.anyLong());

        ResultActions actions =
                mockMvc.perform(
                        post("/lesson/{lesson-id}/suggest", lessonId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)

                );

        actions.andExpect(status().isCreated())
                .andReturn();

    }


}