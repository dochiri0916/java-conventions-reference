package com.dochiri.tooltestlab.domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import com.dochiri.tooltestlab.cart.domain.model.Cart;
import com.dochiri.tooltestlab.cart.domain.model.CartItem;
import com.dochiri.tooltestlab.cart.domain.model.CartItems;
import com.dochiri.tooltestlab.category.domain.model.Category;
import com.dochiri.tooltestlab.category.domain.model.CategoryName;
import com.dochiri.tooltestlab.member.domain.model.Email;
import com.dochiri.tooltestlab.member.domain.model.Member;
import com.dochiri.tooltestlab.member.domain.model.MemberName;
import com.dochiri.tooltestlab.member.domain.model.EncodedPassword;
import com.dochiri.tooltestlab.order.domain.event.OrderCancelled;
import com.dochiri.tooltestlab.order.domain.event.OrderPlaced;
import com.dochiri.tooltestlab.order.domain.model.OrderLine;
import com.dochiri.tooltestlab.order.domain.model.Quantity;
import com.dochiri.tooltestlab.order.domain.model.ShippingAddress;
import com.dochiri.tooltestlab.product.domain.model.Product;
import com.dochiri.tooltestlab.product.domain.model.ProductDescription;
import com.dochiri.tooltestlab.review.domain.model.Rating;
import com.dochiri.tooltestlab.review.domain.model.Review;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressFBWarnings(value = "RV", justification = "반환값 무시가 의도된 예외 검증 테스트")
class AggregateNullValidationTest {

    @Test
    @DisplayName("Category null 필드 검증")
    void categoryNullFields() {
        // given
        var name = new CategoryName("전자기기");
        var id = com.dochiri.tooltestlab.category.domain.model.CategoryId.generate();
        // when & then
        assertThatThrownBy(() -> new Category(null, name))
                .isInstanceOf(com.dochiri.tooltestlab.category.domain.exception.InvalidCategoryIdException.class);
        assertThatThrownBy(() -> new Category(id, null))
                .isInstanceOf(com.dochiri.tooltestlab.category.domain.exception.InvalidCategoryNameException.class);
    }

    @Test
    @DisplayName("Member null 필드 검증")
    void memberNullFields() {
        // given
        var id = com.dochiri.tooltestlab.member.domain.model.MemberId.generate();
        var email = new Email("test@test.com");
        var password = new EncodedPassword("enc");
        var name = new MemberName("홍길동");
        var status = com.dochiri.tooltestlab.member.domain.model.MemberStatus.ACTIVE;
        // when & then
        assertThatThrownBy(() -> new Member(null, email, password, name, status))
                .isInstanceOf(com.dochiri.tooltestlab.member.domain.exception.InvalidMemberIdException.class);
        assertThatThrownBy(() -> new Member(id, null, password, name, status))
                .isInstanceOf(com.dochiri.tooltestlab.member.domain.exception.InvalidEmailException.class);
        assertThatThrownBy(() -> new Member(id, email, null, name, status))
                .isInstanceOf(com.dochiri.tooltestlab.member.domain.exception.InvalidPasswordException.class);
        assertThatThrownBy(() -> new Member(id, email, password, null, status))
                .isInstanceOf(com.dochiri.tooltestlab.member.domain.exception.InvalidMemberNameException.class);
        assertThatThrownBy(() -> new Member(id, email, password, name, null))
                .isInstanceOf(com.dochiri.tooltestlab.member.domain.exception.InvalidMemberException.class);
    }

    @Test
    @DisplayName("Product null 필드 검증")
    void productNullFields() {
        // given
        var id = com.dochiri.tooltestlab.product.domain.model.ProductId.generate();
        var name = new com.dochiri.tooltestlab.product.domain.model.ProductName("상품");
        var desc = new ProductDescription("설명");
        var price = com.dochiri.tooltestlab.product.domain.model.Money.of(1000);
        var stock = com.dochiri.tooltestlab.product.domain.model.Stock.from(10);
        var status = com.dochiri.tooltestlab.product.domain.model.ProductStatus.ON_SALE;
        // when & then
        assertThatThrownBy(() -> new Product(null, name, desc, price, stock, status))
                .isInstanceOf(com.dochiri.tooltestlab.product.domain.exception.InvalidProductIdException.class);
        assertThatThrownBy(() -> new Product(id, null, desc, price, stock, status))
                .isInstanceOf(com.dochiri.tooltestlab.product.domain.exception.InvalidProductNameException.class);
        assertThatThrownBy(() -> new Product(id, name, null, price, stock, status))
                .isInstanceOf(com.dochiri.tooltestlab.product.domain.exception.InvalidProductException.class);
        assertThatThrownBy(() -> new Product(id, name, desc, null, stock, status))
                .isInstanceOf(com.dochiri.tooltestlab.product.domain.exception.InvalidMoneyException.class);
        assertThatThrownBy(() -> new Product(id, name, desc, price, null, status))
                .isInstanceOf(com.dochiri.tooltestlab.product.domain.exception.InvalidStockException.class);
        assertThatThrownBy(() -> new Product(id, name, desc, price, stock, null))
                .isInstanceOf(com.dochiri.tooltestlab.product.domain.exception.InvalidProductException.class);
    }

    @Test
    @DisplayName("Review null 필드 검증")
    void reviewNullFields() {
        // given
        var id = com.dochiri.tooltestlab.review.domain.model.ReviewId.generate();
        var memberId = new com.dochiri.tooltestlab.member.domain.model.MemberId("m1");
        var productId = new com.dochiri.tooltestlab.product.domain.model.ProductId("p1");
        var rating = new Rating(5);
        var content = new com.dochiri.tooltestlab.review.domain.model.ReviewContent("좋아요");
        // when & then
        assertThatThrownBy(() -> new Review(null, memberId, productId, rating, content))
                .isInstanceOf(com.dochiri.tooltestlab.review.domain.exception.InvalidReviewIdException.class);
        assertThatThrownBy(() -> new Review(id, null, productId, rating, content))
                .isInstanceOf(com.dochiri.tooltestlab.member.domain.exception.InvalidMemberIdException.class);
        assertThatThrownBy(() -> new Review(id, memberId, null, rating, content))
                .isInstanceOf(com.dochiri.tooltestlab.product.domain.exception.InvalidProductIdException.class);
        assertThatThrownBy(() -> new Review(id, memberId, productId, null, content))
                .isInstanceOf(com.dochiri.tooltestlab.review.domain.exception.InvalidRatingException.class);
        assertThatThrownBy(() -> new Review(id, memberId, productId, rating, null))
                .isInstanceOf(com.dochiri.tooltestlab.review.domain.exception.InvalidReviewContentException.class);
    }

    @Test
    @DisplayName("Order null 필드 검증")
    void orderNullFields() {
        // given
        var id = com.dochiri.tooltestlab.order.domain.model.OrderId.generate();
        var memberId = new com.dochiri.tooltestlab.member.domain.model.MemberId("m1");
        var address = new ShippingAddress(
                new com.dochiri.tooltestlab.order.domain.model.RecipientName("홍길동"),
                new com.dochiri.tooltestlab.order.domain.model.Phone("01012345678"),
                new com.dochiri.tooltestlab.order.domain.model.StreetAddress("서울시"),
                new com.dochiri.tooltestlab.order.domain.model.ZipCode("12345"));
        var items = getOrderLines();
        var totalAmount = items.totalAmount();
        // when & then
        assertThatThrownBy(() -> new com.dochiri.tooltestlab.order.domain.model.Order(
                null, memberId, items, com.dochiri.tooltestlab.order.domain.model.OrderStatus.PENDING,
                address, totalAmount))
                .isInstanceOf(com.dochiri.tooltestlab.order.domain.exception.InvalidOrderIdException.class);
        assertThatThrownBy(() -> new com.dochiri.tooltestlab.order.domain.model.Order(
                id, null, items, com.dochiri.tooltestlab.order.domain.model.OrderStatus.PENDING,
                address, totalAmount))
                .isInstanceOf(com.dochiri.tooltestlab.member.domain.exception.InvalidMemberIdException.class);
        assertThatThrownBy(() -> new com.dochiri.tooltestlab.order.domain.model.Order(
                id, memberId, null, com.dochiri.tooltestlab.order.domain.model.OrderStatus.PENDING,
                address, totalAmount))
                .isInstanceOf(com.dochiri.tooltestlab.order.domain.exception.InvalidOrderLinesException.class);
        assertThatThrownBy(() -> new com.dochiri.tooltestlab.order.domain.model.Order(
                id, memberId, items, null, address, totalAmount))
                .isInstanceOf(com.dochiri.tooltestlab.order.domain.exception.InvalidOrderStatusException.class);
        assertThatThrownBy(() -> new com.dochiri.tooltestlab.order.domain.model.Order(
                id, memberId, items, com.dochiri.tooltestlab.order.domain.model.OrderStatus.PENDING,
                null, totalAmount))
                .isInstanceOf(com.dochiri.tooltestlab.order.domain.exception.InvalidShippingAddressException.class);
        assertThatThrownBy(() -> new com.dochiri.tooltestlab.order.domain.model.Order(
                id, memberId, items, com.dochiri.tooltestlab.order.domain.model.OrderStatus.PENDING,
                address, null))
                .isInstanceOf(com.dochiri.tooltestlab.product.domain.exception.InvalidMoneyException.class);
    }

    @Test
    @DisplayName("OrderLine null 필드 검증")
    void orderLineNullFields() {
        // given
        var productId = new com.dochiri.tooltestlab.product.domain.model.ProductId("p1");
        var productName = new com.dochiri.tooltestlab.product.domain.model.ProductName("상품");
        var quantity = new Quantity(1);
        var unitPrice = com.dochiri.tooltestlab.product.domain.model.Money.of(1000);
        // when & then
        assertThatThrownBy(() -> new OrderLine(null, productName, quantity, unitPrice))
                .isInstanceOf(com.dochiri.tooltestlab.product.domain.exception.InvalidProductIdException.class);
        assertThatThrownBy(() -> new OrderLine(productId, null, quantity, unitPrice))
                .isInstanceOf(com.dochiri.tooltestlab.product.domain.exception.InvalidProductNameException.class);
        assertThatThrownBy(() -> new OrderLine(productId, productName, null, unitPrice))
                .isInstanceOf(com.dochiri.tooltestlab.order.domain.exception.InvalidQuantityException.class);
        assertThatThrownBy(() -> new OrderLine(productId, productName, quantity, null))
                .isInstanceOf(com.dochiri.tooltestlab.product.domain.exception.InvalidMoneyException.class);
    }

    @Test
    @DisplayName("ShippingAddress null 필드 검증")
    void shippingAddrNullFields() {
        // given
        var recipient = new com.dochiri.tooltestlab.order.domain.model.RecipientName("홍길동");
        var phone = new com.dochiri.tooltestlab.order.domain.model.Phone("01012345678");
        var addr = new com.dochiri.tooltestlab.order.domain.model.StreetAddress("서울시");
        var zip = new com.dochiri.tooltestlab.order.domain.model.ZipCode("12345");
        // when & then
        assertThatThrownBy(() -> new ShippingAddress(null, phone, addr, zip))
                .isInstanceOf(com.dochiri.tooltestlab.order.domain.exception.InvalidShippingAddressException.class);
        assertThatThrownBy(() -> new ShippingAddress(recipient, null, addr, zip))
                .isInstanceOf(com.dochiri.tooltestlab.order.domain.exception.InvalidShippingAddressException.class);
        assertThatThrownBy(() -> new ShippingAddress(recipient, phone, null, zip))
                .isInstanceOf(com.dochiri.tooltestlab.order.domain.exception.InvalidShippingAddressException.class);
        assertThatThrownBy(() -> new ShippingAddress(recipient, phone, addr, null))
                .isInstanceOf(com.dochiri.tooltestlab.order.domain.exception.InvalidShippingAddressException.class);
    }

    @Test
    @DisplayName("OrderPlaced null 필드 검증")
    void orderPlacedNullFields() {
        // given
        var orderId = com.dochiri.tooltestlab.order.domain.model.OrderId.generate();
        var memberId = new com.dochiri.tooltestlab.member.domain.model.MemberId("m1");
        var items = new com.dochiri.tooltestlab.order.domain.model.OrderPlacedItems(List.of());
        // when & then
        assertThatThrownBy(() -> new OrderPlaced(null, memberId, items))
                .isInstanceOf(com.dochiri.tooltestlab.order.domain.exception.InvalidOrderIdException.class);
        assertThatThrownBy(() -> new OrderPlaced(orderId, null, items))
                .isInstanceOf(com.dochiri.tooltestlab.member.domain.exception.InvalidMemberIdException.class);
        assertThatThrownBy(() -> new OrderPlaced(orderId, memberId, null))
                .isInstanceOf(com.dochiri.tooltestlab.order.domain.exception.InvalidOrderLinesException.class);
    }

    @Test
    @DisplayName("OrderCancelled null 필드 검증")
    void orderCancelledNullFields() {
        // given
        var orderId = com.dochiri.tooltestlab.order.domain.model.OrderId.generate();
        var items = new com.dochiri.tooltestlab.order.domain.model.OrderCancelledItems(List.of());
        // when & then
        assertThatThrownBy(() -> new OrderCancelled(null, items))
                .isInstanceOf(com.dochiri.tooltestlab.order.domain.exception.InvalidOrderIdException.class);
        assertThatThrownBy(() -> new OrderCancelled(orderId, null))
                .isInstanceOf(com.dochiri.tooltestlab.order.domain.exception.InvalidOrderLinesException.class);
    }

    @Test
    @DisplayName("Cart null 필드 검증")
    void cartNullFields() {
        // given
        var id = com.dochiri.tooltestlab.cart.domain.model.CartId.generate();
        var memberId = new com.dochiri.tooltestlab.member.domain.model.MemberId("m1");
        var items = CartItems.empty();
        // when & then
        assertThatThrownBy(() -> new Cart(null, memberId, items))
                .isInstanceOf(com.dochiri.tooltestlab.cart.domain.exception.InvalidCartIdException.class);
        assertThatThrownBy(() -> new Cart(id, null, items))
                .isInstanceOf(com.dochiri.tooltestlab.member.domain.exception.InvalidMemberIdException.class);
        assertThatThrownBy(() -> new Cart(id, memberId, null))
                .isInstanceOf(com.dochiri.tooltestlab.cart.domain.exception.InvalidCartException.class);
    }

    @Test
    @DisplayName("CartItem null 필드 검증")
    void cartItemNullFields() {
        // given
        var productId = new com.dochiri.tooltestlab.product.domain.model.ProductId("p1");
        var productName = new com.dochiri.tooltestlab.product.domain.model.ProductName("상품");
        var quantity = new Quantity(1);
        var unitPrice = com.dochiri.tooltestlab.product.domain.model.Money.of(1000);
        // when & then
        assertThatThrownBy(() -> new CartItem(null, productName, quantity, unitPrice))
                .isInstanceOf(com.dochiri.tooltestlab.product.domain.exception.InvalidProductIdException.class);
        assertThatThrownBy(() -> new CartItem(productId, null, quantity, unitPrice))
                .isInstanceOf(com.dochiri.tooltestlab.product.domain.exception.InvalidProductNameException.class);
        assertThatThrownBy(() -> new CartItem(productId, productName, null, unitPrice))
                .isInstanceOf(com.dochiri.tooltestlab.order.domain.exception.InvalidQuantityException.class);
        assertThatThrownBy(() -> new CartItem(productId, productName, quantity, null))
                .isInstanceOf(com.dochiri.tooltestlab.product.domain.exception.InvalidMoneyException.class);
    }

    @Test
    @DisplayName("Cart.addItem null 필드 검증")
    void cartAddItemNullFields() {
        // given
        var cart = Cart.create(new com.dochiri.tooltestlab.member.domain.model.MemberId("m1"));
        var productId = new com.dochiri.tooltestlab.product.domain.model.ProductId("p1");
        var productName = new com.dochiri.tooltestlab.product.domain.model.ProductName("상품");
        var quantity = new Quantity(1);
        var unitPrice = com.dochiri.tooltestlab.product.domain.model.Money.of(1000);
        // when & then
        assertThatThrownBy(() -> cart.addItem(null, productName, quantity, unitPrice))
                .isInstanceOf(com.dochiri.tooltestlab.product.domain.exception.InvalidProductIdException.class);
        assertThatThrownBy(() -> cart.addItem(productId, null, quantity, unitPrice))
                .isInstanceOf(com.dochiri.tooltestlab.product.domain.exception.InvalidProductNameException.class);
        assertThatThrownBy(() -> cart.addItem(productId, productName, null, unitPrice))
                .isInstanceOf(com.dochiri.tooltestlab.order.domain.exception.InvalidQuantityException.class);
        assertThatThrownBy(() -> cart.addItem(productId, productName, quantity, null))
                .isInstanceOf(com.dochiri.tooltestlab.product.domain.exception.InvalidMoneyException.class);
    }

    @Test
    @DisplayName("CartItems.replace null 검증")
    void cartItemsReplaceNull() {
        // given
        var items = CartItems.from(List.of(new CartItem(new com.dochiri.tooltestlab.product.domain.model.ProductId("p1"),
                new com.dochiri.tooltestlab.product.domain.model.ProductName("상품"),
                new Quantity(1), com.dochiri.tooltestlab.product.domain.model.Money.of(1000))));
        var oldItem = items.values().get(0);
        var newItem = new CartItem(new com.dochiri.tooltestlab.product.domain.model.ProductId("p2"),
                new com.dochiri.tooltestlab.product.domain.model.ProductName("상품2"),
                new Quantity(1), com.dochiri.tooltestlab.product.domain.model.Money.of(1000));
        // when & then
        assertThatThrownBy(() -> items.replace(null, newItem))
                .isInstanceOf(com.dochiri.tooltestlab.cart.domain.exception.InvalidCartItemException.class);
        assertThatThrownBy(() -> items.replace(oldItem, null))
                .isInstanceOf(com.dochiri.tooltestlab.cart.domain.exception.InvalidCartItemException.class);
    }

    private static com.dochiri.tooltestlab.order.domain.model.OrderLines getOrderLines() {
        return com.dochiri.tooltestlab.order.domain.model.OrderLines.from(List.of(
                new OrderLine(new com.dochiri.tooltestlab.product.domain.model.ProductId("p1"),
                        new com.dochiri.tooltestlab.product.domain.model.ProductName("상품"),
                        new Quantity(1), com.dochiri.tooltestlab.product.domain.model.Money.of(1000))));
    }
}
