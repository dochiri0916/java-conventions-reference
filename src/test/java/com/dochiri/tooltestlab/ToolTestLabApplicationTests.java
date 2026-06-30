package com.dochiri.tooltestlab;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ToolTestLabApplicationTests {

    @Test
    @DisplayName("컨텍스트 로드")
    void contextLoads() {
        // given

        // when

        // then
        assertThat(true).isTrue();
    }

}
