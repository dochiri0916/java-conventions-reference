package com.dochiri.tooltestlab.adapter;

import com.dochiri.tooltestlab.category.domain.model.Category;
import com.dochiri.tooltestlab.member.application.port.out.MemberRepositoryPort;
import com.dochiri.tooltestlab.member.domain.model.*;
import com.dochiri.tooltestlab.product.application.port.out.ProductRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PersistenceAdapterIntegrationTest {

    @Autowired
    private MemberRepositoryPort memberRepositoryPort;

    @Autowired
    private ProductRepositoryPort productRepositoryPort;

    @Test
    @DisplayName("회원을 publicId로 조회할 수 있다")
    void findByPublicId() {
            // given
        var member = Member.create(new Email("persist-test@example.com"),
                new Password("password123"), new MemberName("테스터"));
        var saved = memberRepositoryPort.save(member);

            // when
        var found = memberRepositoryPort.findByPublicId(saved.id().value());

            // then
        assertThat(found).isPresent();
        assertThat(found.get().id().value()).isEqualTo(saved.id().value());
    }

    @Test
    @DisplayName("카테고리를 publicId로 조회할 수 있다")
    void findCategoryByPublicId() {
            // given
        var category = Category.create(new com.dochiri.tooltestlab.category.domain.model.CategoryName("카테고리-테스트"));
        var saved = productRepositoryPort.saveCategory(category);

            // when
        var found = productRepositoryPort.findCategoryByPublicId(saved.id().value());

            // then
        assertThat(found).isPresent();
        assertThat(found.get().id().value()).isEqualTo(saved.id().value());
    }
}
