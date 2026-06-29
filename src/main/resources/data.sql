-- ============================================================
-- Seed Data — 애플리케이션 시작 시 H2 메모리 DB에 자동 적재
-- ============================================================

-- ── Categories ──────────────────────────────────────────────
INSERT INTO categories (id, version, public_id, name) VALUES (1, 0, 'cat01elec0000000000000000000', '전자기기');
INSERT INTO categories (id, version, public_id, name) VALUES (2, 0, 'cat02cloth000000000000000000', '의류');
INSERT INTO categories (id, version, public_id, name) VALUES (3, 0, 'cat03books000000000000000000', '도서');
INSERT INTO categories (id, version, public_id, name) VALUES (4, 0, 'cat04food0000000000000000000', '식품');

-- ── Products ────────────────────────────────────────────────
INSERT INTO products (id, version, public_id, name, description, price_amount, stock, status) VALUES
(1, 0, 'prod01notebook000000000000000', '노트북',     '고성능 노트북',       1500000, 10,  'ON_SALE'),
(2, 0, 'prod02earphone00000000000000', '무선 이어폰', '노이즈 캔슬링 이어폰', 250000,  50,  'ON_SALE'),
(3, 0, 'prod03smartphone000000000000', '스마트폰',    '최신형 스마트폰',      1200000, 30,  'ON_SALE'),
(4, 0, 'prod04sweatshirt000000000000', '맨투맨',      '편안한 맨투맨',        49000,   100, 'ON_SALE'),
(5, 0, 'prod05jeans00000000000000000', '청바지',      '슬림핏 청바지',        79000,   80,  'ON_SALE'),
(6, 0, 'prod06cleancode0000000000000', '클린 코드',   '로버트 마틴 저',       35000,   200, 'ON_SALE'),
(7, 0, 'prod07dddstart00000000000000', 'DDD START!',  '최범균 저',            28000,   150, 'ON_SALE');

-- ── Product ↔ Category 매핑 ─────────────────────────────────
INSERT INTO product_category (product_id, category_id) VALUES
(1, 1), (2, 1), (3, 1),   -- 전자기기: 노트북, 무선 이어폰, 스마트폰
(4, 2), (5, 2),            -- 의류: 맨투맨, 청바지
(6, 3), (7, 3);            -- 도서: 클린 코드, DDD START!
