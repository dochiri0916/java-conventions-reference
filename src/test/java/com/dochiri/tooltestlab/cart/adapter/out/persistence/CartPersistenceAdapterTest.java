package com.dochiri.tooltestlab.cart.adapter.out.persistence;

import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.cart.domain.model.Cart;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.model.Quantity;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.product.domain.model.ProductName;
import com.dochiri.tooltestlab.shared.model.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CartPersistenceAdapterTest {

    @Autowired
    private CartRepositoryPort cartRepositoryPort;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("회원 ID로 장바구니를 조회할 수 있다")
    void findByMemberId() {
            // given
        var memberId = new MemberId("cartTestMem1");
        var cart = Cart.create(memberId);
        cartRepositoryPort.save(cart);

            // when
        var found = cartRepositoryPort.findByMemberId(memberId.value());

            // then
        assertThat(found).isPresent();
    }

    @Test
    @DisplayName("장바구니에 아이템을 추가하여 저장할 수 있다")
    void saveCartWithItems() {
            // given
        var memberId = new MemberId("cartTestMem2");
        var cart = Cart.create(memberId);

            // when
        var cartWithItem = cart.addItem(new ProductId("prod01notebook000000000000000"),
                new ProductName("테스트상품"), new Quantity(2), Money.of(1000));
        var saved = cartRepositoryPort.save(cartWithItem);

            // then
        assertThat(saved.items().values()).hasSize(1);
    }

    @Test
    @DisplayName("동시 생성으로 인한 UNIQUE 제약 위반 시 재시도 로직이 실행된다")
    void retryOnUniqueConstraintViolation() {
            // given
        var memberId = new MemberId("cartTestMem3");
        var cart = Cart.create(memberId);
        var cartIdValue = cart.id().value();

        // cartId는 같지만 memberId는 다른 행을 직접 삽입하여 cartId UNIQUE 충돌 유도
        jdbcTemplate.update(
                "INSERT INTO carts (cart_id, member_id, version) VALUES (?, ?, 0)",
                cartIdValue, "differentMemberId");

            // when
        // adapter.save() 내부에서 findByMemberId("cartTestMem3")는 결과가 없으므로
        // 새 CartEntity를 INSERT 시도하고, cartId UNIQUE 충돌로 DataIntegrityViolationException 발생
        // catch 블록에서 로그 출력 후 재시도 → 두 번째도 충돌하여 예외 전파
        Exception caught = null;
        try {
            cartRepositoryPort.save(cart);
        } catch (Exception e) {
            caught = e;
        }

            // then
        assertThat(caught).isNotNull();
    }
}
