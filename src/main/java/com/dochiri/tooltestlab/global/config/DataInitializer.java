package com.dochiri.tooltestlab.global.config;

import com.dochiri.tooltestlab.category.domain.model.Category;
import com.dochiri.tooltestlab.member.application.command.RegisterMemberCommand;
import com.dochiri.tooltestlab.member.application.port.in.RegisterMemberUseCase;
import com.dochiri.tooltestlab.product.application.command.RegisterProductCommand;
import com.dochiri.tooltestlab.product.application.port.in.RegisterProductUseCase;
import com.dochiri.tooltestlab.product.application.port.out.ProductRepositoryPort;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 개발용 초기 데이터 주입.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepositoryPort productRepositoryPort;
    private final RegisterProductUseCase registerProductUseCase;
    private final RegisterMemberUseCase registerMemberUseCase;

    public DataInitializer(ProductRepositoryPort productRepositoryPort,
                           RegisterProductUseCase registerProductUseCase,
                           RegisterMemberUseCase registerMemberUseCase) {
        this.productRepositoryPort = productRepositoryPort;
        this.registerProductUseCase = registerProductUseCase;
        this.registerMemberUseCase = registerMemberUseCase;
    }

    @Override
    public void run(String... args) {
        // 카테고리 (idempotent: 중복 생성 시 무시)
        Category electronics = saveCategoryIfAbsent("전자기기");
        Category clothing = saveCategoryIfAbsent("의류");
        Category books = saveCategoryIfAbsent("도서");
        Category food = saveCategoryIfAbsent("식품");

        // 상품
        List<String> elecIds = List.of(electronics.id().value());
        registerProductUseCase.register(new RegisterProductCommand("노트북", "고성능 노트북", 1_500_000, 10, elecIds));
        registerProductUseCase.register(new RegisterProductCommand("무선 이어폰", "노이즈 캔슬링 이어폰", 250_000, 50, elecIds));
        registerProductUseCase.register(new RegisterProductCommand("스마트폰", "최신형 스마트폰", 1_200_000, 30, elecIds));

        List<String> clothIds = List.of(clothing.id().value());
        registerProductUseCase.register(new RegisterProductCommand("맨투맨", "편안한 맨투맨", 49_000, 100, clothIds));
        registerProductUseCase.register(new RegisterProductCommand("청바지", "슬림핏 청바지", 79_000, 80, clothIds));

        List<String> bookIds = List.of(books.id().value());
        registerProductUseCase.register(new RegisterProductCommand("클린 코드", "로버트 마틴 저", 35_000, 200, bookIds));
        registerProductUseCase.register(new RegisterProductCommand("DDD START!", "최범균 저", 28_000, 150, bookIds));

        // 테스트 회원
        try {
            registerMemberUseCase.register(new RegisterMemberCommand("admin@test.com", "1234", "관리자"));
        } catch (Exception ignored) {
            // already exists
        }

        System.out.println("[DataInitializer] 초기 데이터 주입 완료");
    }

    private Category saveCategoryIfAbsent(String name) {
        List<Category> existing = productRepositoryPort.findAllCategories();
        return existing.stream()
                .filter(c -> c.name().equals(name))
                .findFirst()
                .orElseGet(() -> productRepositoryPort.saveCategory(Category.create(name)));
    }
}
