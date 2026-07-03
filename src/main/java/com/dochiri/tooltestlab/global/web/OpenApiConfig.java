package com.dochiri.tooltestlab.global.web;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tool Test Lab API")
                        .description("""
                                Tool Test Lab 서비스 API 명세입니다.

                                ## 인증
                                - 회원 가입 후 로그인한 세션으로 인증이 필요한 API를 호출할 수 있습니다.
                                - 로그인하지 않은 상태에서 인증 필요 API를 호출하면 401 오류가 발생합니다.

                                ## 오류 응답
                                모든 오류는 [RFC 9457 Problem Detail](https://www.rfc-editor.org/rfc/rfc9457) 형식으로 반환됩니다.
                                """)
                        .version("0.0.1")
                        .contact(new Contact()
                                .name("dochiri0916")
                                .email("dochiri0916@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("http://localhost:18080").description("로컬 개발 서버")));
    }
}
