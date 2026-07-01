package com.dochiri.tooltestlab;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ToolTestLabApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("컨텍스트 로드")
    void contextLoads() {
        // given
        var context = applicationContext;

        // when
        var beanCount = context.getBeanDefinitionCount();

        // then
        assertThat(beanCount).isPositive();
    }

}
