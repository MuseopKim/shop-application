## 상품 전시 시스템

> 카테고리 / 브랜드 별 상품 및 가격 정보를 제공하는 시스템

## 개발환경

- Java 17
- Spring Boot 3.4.2
- H2 Database
- Caffeine Cache
- JUnit 5, Mockito

## 실행 방법

### git clone

```shell
git clone https://github.com/MuseopKim/shop-application.git
```

### Application 실행

```shell
./gradlew clean bootRun
```

### Test 실행

```shell
./gradlew test
```

## 기능

- [x] 카테고리 별 최저 가격 브랜드, 가격, 총액 조회
- [x] 단일 브랜드 전체 카테고리 상품 구매 시 최저 가격 브랜드, 총액 조회
- [x] 특정 카테고리 최저, 최고 가격 브랜드 및 가격 조회
- [x] 브랜드 관리
    - [x] 브랜드 등록
    - [x] 브랜드 수정
    - [x] 브랜드 삭제
- [x] 상품 관리
    - [x] 상품 등록
    - [x] 상품 수정
    - [x] 상품 삭제

## Database Tables

<img width="595" alt="Image" src="https://github.com/user-attachments/assets/ab7afe10-178c-4f54-8638-bfb3b20f3b0a" />

```sql
-- 브랜드
CREATE TABLE brand
(
    id         BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '-',
    name       VARCHAR(100) NOT NULL COMMENT '브랜드명',
    created_at DATETIME     NOT NULL COMMENT '생성일',
    updated_at DATETIME     NOT NULL COMMENT '수정일'
);

-- 상품
CREATE TABLE product
(
    id         BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '-',
    brand_id   BIGINT       NOT NULL,
    category   VARCHAR(50)  NOT NULL,
    name       VARCHAR(100) NOT NULL,
    price      INTEGER      NOT NULL,
    created_at DATETIME     NOT NULL COMMENT '생성일',
    updated_at DATETIME     NOT NULL COMMENT '수정일'
);

CREATE INDEX `idx_product_category_price_brand` ON product (`category`, `price`, `brand_id`);
CREATE INDEX `idx_product_brand_id` ON product (`brand_id`);
```

### 설계 고려사항

- 카테고리, 브랜드 별 가격 조회가 빈번하게 발생할 것으로 예상하여 복합 인덱스`(category, price, brand_id)` 구성
- 브랜드 별 상품 조회를 위한 `brand_id` 인덱스 구성
- 조회 성능을 고려하여 ETL, 배치 작업을 통해 별도의 `집계 테이블`(카테고리별 최저 / 최고가, 브랜드별 총액 등) 구성을 고려 하였으나,
  캐시 사용으로 대체
- 프로젝트 요구 기능 내용을 반영하여 카테고리는 별도 테이블을 두지 않고 Enum으로 관리 (고도화 시점에 별도 테이블 분리 고려)

## 가격 집계 정보 조회 해결 전략

### Code Architecture

```text
Presentation Layer -> Business Layer -> Implementation Layer -> Persistence Layer
   (Controller)         (Service)         (Implementation)        (Repository)
```

<img width="641" alt="Image" src="https://github.com/user-attachments/assets/3a50c668-c75e-491f-a46f-b242324af116" />

```text
Business Layer
└─ ProductPriceService
     - 브랜드 / 카테고리 별 가격 분석 흐름 제어
     - Implementation Layer의 각 컴포넌트가 협력하며 집계 결과 도출

Implementation Layer
└─ ProductReader
     - 브랜드 / 카테고리 기준 가격 집계(최저 / 최고 가격) 정보 조회
└─ ProductPriceAggregator
     - 조회된 가격 정보를 다양한 관점에서 조합하는 책임
     - 조합의 결과를 전용 Aggregation 객체로 변환
└─ Aggregation
     └─ CategoryPriceAggregation
           - 카테고리 별 최저 / 최고 가격 집계 정보
           - 전체 카테고리 가격 합산
     └─ BrandCategoryPriceAggregation
           - 브랜드 별 전체 카테고리 가격 집계 정보
           - 브랜드 별 총액 계산 및 최저가 브랜드 등 필요 정보 반환
```

## 조회 성능 개선 전략

- 요청마다 모든 상품 가격 집계가 필요한 고비용성을 고려하여 캐시 사용한 성능 최적화
- 캐시는 별도 인프라 구성을 하지 않기 위해 Local Cache(`Caffeine Cache`)로 대체 (고도화 시 Redis와 같은 글로벌 캐시 사용 고려)

### Cache Eviction

- 데이터 정합성 고려하여 상품 변경(등록 / 수정 / 삭제)시 캐시 무효화
- Cache-Aside 패턴으로 다음 요청 시 새로운 데이터로 캐시 재생성
- 실시간 무효화 실패 케이스 고려하여 기본 캐시 유지 기간(TTL) 적용

### 이벤트 기반 관리

- 이벤트 발행을 통한 느슨한 결합 구조
- 스프링이 제공하는 `ApplicationEventPublisher`, `EventListener` 활용
- `@TransactionalEventListener` 적용하여 등록 / 수정 / 삭제 트랜잭션과 격리

## API Specification

### 표준 응답 형식

- 모든 API는 다음과 같은 표준 응답 형식을 따릅니다

##### 요청 성공

```json
{
  "code": "SUCCESS",
  "message": "성공",
  "payload": {
    // response payload
  }
}
```

##### 요청 실패

```json
{
  "code": "BRAND_NOT_EXIST",
  "message": "브랜드가 존재하지 않습니다"
}
```

### 1. 카테고리 별 최저 가격 브랜드 조회

##### Request

```bash
curl -X GET http://localhost:8080/categories/minimum-prices \
   -H 'Accept: application/json'
```

#### Response

```json
{
  "code": "SUCCESS",
  "message": "성공",
  "payload": {
    "minimumPrice": {
      "brandName": "D",
      "categories": [
        {
          "category": "CAP",
          "price": 1500
        },
        {
          "category": "TOP",
          "price": 10100
        },
        {
          "category": "SOCKS",
          "price": 2400
        },
        {
          "category": "ACCESSORIES",
          "price": 2000
        },
        {
          "category": "SNEAKERS",
          "price": 9500
        },
        {
          "category": "OUTER",
          "price": 5100
        },
        {
          "category": "PANTS",
          "price": 3000
        },
        {
          "category": "BAG",
          "price": 2500
        }
      ],
      "totalPrice": 36100
    }
  }
}
```

### 2. 단일 브랜드 전체 카테고리 상품 구매 시 최저 가격 브랜드, 총액 조회

##### Request

```bash
curl -X GET http://localhost:8080/brands/minimum-total-price \
    -H 'Accept: application/json'
```

#### Response

```json
{
  "code": "SUCCESS",
  "message": "성공",
  "payload": {
    "minimumPrice": {
      "brandName": "D",
      "categories": [
        {
          "category": "CAP",
          "price": 1500
        },
        {
          "category": "TOP",
          "price": 10100
        },
        {
          "category": "SOCKS",
          "price": 2400
        },
        {
          "category": "ACCESSORIES",
          "price": 2000
        },
        {
          "category": "SNEAKERS",
          "price": 9500
        },
        {
          "category": "OUTER",
          "price": 5100
        },
        {
          "category": "PANTS",
          "price": 3000
        },
        {
          "category": "BAG",
          "price": 2500
        }
      ],
      "totalPrice": 36100
    }
  }
}
```

### 3. 특정 카테고리 최저, 최고 가격 브랜드 및 가격 조회

```bash
curl -X GET http://localhost:8080/categories/pants/price-ranges \
-H 'Content-Type: application/json'
```

```json
{
  "code": "SUCCESS",
  "message": "성공",
  "payload": {
    "category": "PANTS",
    "minimumPrices": [
      {
        "brandName": "D",
        "price": 3000
      }
    ],
    "maximumPrices": [
      {
        "brandName": "A",
        "price": 4200
      }
    ]
  }
}
```

### 4. 브랜드 등록

##### Request

```bash
curl -X POST http://localhost:8080/brands \
    -H 'Content-Type: application/json' \
    -d '{
        "name": "브랜드A"
    }'
```

##### Response

```json
{
  "code": "SUCCESS",
  "message": "성공",
  "payload": {
    "id": 10,
    "name": "브랜드A"
  }
}
```

### 5. 브랜드 수정

##### Request

```bash
curl -X PUT http://localhost:8080/brands/1 \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "브랜드ABC"
  }'
```

##### Response

```json
{
  "code": "SUCCESS",
  "message": "성공",
  "payload": {
    "id": 1,
    "name": "브랜드ABC"
  }
}
```

### 6. 브랜드 삭제

##### Request

```bash
curl -X DELETE http://localhost:8080/brands/1
```

##### Response

```json
{
  "code": "SUCCESS",
  "message": "성공",
  "payload": true
}
```

### 7. 상품 등록

##### Request

```bash
curl -X POST http://localhost:8080/products \
  -H 'Content-Type: application/json' \
  -d '{
      "brandId": 1,
      "category": "TOP",
      "name": "기본 티셔츠",
      "price": 10000
  }'
```

##### Response

```json
{
  "code": "SUCCESS",
  "message": "성공",
  "payload": {
    "id": 73,
    "brandId": 1,
    "brandName": "브랜드ABC",
    "category": "TOP",
    "name": "기본 티셔츠",
    "price": 10000
  }
}
```

### 8. 상품 수정

##### Request

```bash
curl -X PUT http://localhost:8080/products/1 \
  -H 'Content-Type: application/json' \
  -d '{
      "brandId": 1,
      "category": "TOP",
      "name": "프리미엄 티셔츠",
      "price": 20000
  }'
```

##### Response

```json
{
  "code": "SUCCESS",
  "message": "성공",
  "payload": {
    "id": 73,
    "brandId": 1,
    "brandName": "브랜드ABC",
    "category": "TOP",
    "name": "프리미엄 티셔츠",
    "price": 20000
  }
}
```

### 9. 상품 삭제

##### Request

```bash
curl -X DELETE http://localhost:8080/products/1
```

##### Response

```json
{
  "code": "SUCCESS",
  "message": "성공",
  "payload": true
}
```
