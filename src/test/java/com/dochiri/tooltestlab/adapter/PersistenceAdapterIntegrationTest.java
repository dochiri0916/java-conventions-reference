package com.dochiri.tooltestlab.adapter;

import com.dochiri.tooltestlab.catalog.category.domain.model.Category;
import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryName;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.domain.model.Product;
import com.dochiri.tooltestlab.member.application.port.out.MemberRepositoryPort;
import com.dochiri.tooltestlab.member.domain.model.Email;
import com.dochiri.tooltestlab.member.domain.model.EncodedPassword;
import com.dochiri.tooltestlab.member.domain.model.Member;
import com.dochiri.tooltestlab.member.domain.model.MemberName;
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
    @DisplayName("회원을 memberId로 조회할 수 있다")
    void findByMemberId() {
        // given
        var member = Member.register(new Email("persist-test@example.com"),
                new EncodedPassword("password123"), new MemberName("테스터"));
        var saved = memberRepositoryPort.save(member);

            // when
        var found = memberRepositoryPort.findByMemberId(saved.id().value());

            // then
        assertThat(found).isPresent();
        assertThat(found.get().id().value()).isEqualTo(saved.id().value());
    }

    @Test
    @DisplayName("기존 상품을 카테고리와 함께 업데이트할 수 있다")
    void updateProductWithCategories() {
        // given
        var product = Product.create("상품명", "설명", 10000, 10);
        var category = Category.create(new CategoryName("카테고리"));
        var saved = productRepositoryPort.save(product, List.of(category));

        // when
        var updated = productRepositoryPort.save(saved, List.of(category));

        // then
        assertThat(updated.id()).isEqualTo(saved.id());
    }

    @Test
    @DisplayName("카테고리를 publicId로 조회할 수 있다")
    void findCategoryByPublicId() {
            // given
        var category = Category.create(new CategoryName("카테고리-테스트"));
        var saved = productRepositoryPort.saveCategory(category);

            // when
        var found = productRepositoryPort.findCategoryByPublicId(saved.id().value());

            // then
        assertThat(found).isPresent();
        assertThat(found.get().id().value()).isEqualTo(saved.id().value());
    }
}
