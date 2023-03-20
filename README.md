# 프로젝트 설명
### JPA를 활용한 게시판 프로젝트

# 게시판관리
- [X] 회원인 경우 게시글을 작성/수정/삭제할 수 있어야 한다.
- [X] 게시판 사용자는 게시글 목록을 볼 수 있다.
- [X] 게시글 검색을 할 수 있다.
- [X] 해시태그 검색을 할 수 있다.
- [X] 회원인 경우에만 게시글 상세보기를 확인할 수 있다.

# 게시글댓글 관리
- [X] 회원인 경우 댓글을 생성/삭제할 수 있어야 한다.
- [X] 시큐리티를 통해 인증/인가

# 개발 추가요건
- [X] 단위테스트
- [X] 통합테스트

# 사용한 기술 & 개발 환경
- <b>Spring Boot 2.7.5</b>
- <b>Java 17</b>
- <b>JPA</b>
- <b>Thymeleaf</b>
- <b>H2 DB</b>
  - 테스트, 개발 환경에서 H2 DB를 사용하였습니다.
- <b>MySql</b>
  - 실제 배포를 할 때 MySql 사용하였습니다.
- <b>Rest Repositories HAL Explorer</b>

# Project Structure
```
 ─ src
    ├─ config // 설정 관련 (시큐리티, 타임리프 설정)
    ├─ controller // 컨트롤러
    ├─ domain // 도메인 (게시글, 댓글, 유저)
    │  └─ constant // Enum 타입
    ├─ dto // DTO
    │  ├─ request // 요청 dto
    │  ├─ response // 응답 dto
    │  └─ security // 시큐리티 응답 dto
    ├─ repository 
    │  └─ querydsl // 동적쿼리 
    └─ service // 서비스 (비즈니스 로직)
```

# ERD
![image](https://user-images.githubusercontent.com/81161819/226256512-1068b09f-7827-4129-ae72-030715953d94.png)

# USE-CASE
![image](https://user-images.githubusercontent.com/81161819/226270456-e237c89e-0070-4377-b78c-79e5455d17ca.png)

# API
## Article
- 게시글 목록 보기 `(GET /articles)`
- 게시글 상세보기 `(GET /articles/{articleId})`
- 해시태그 검색 페이지 `(GET /articles/search-hashtag)`
- 게시글 작성 페이지 `(GET /articles/form)`
- 게시글 저장 `(POST /articles/form)`
- 게시글 수정 페이지 `(GET /articles/{articleId}/form)`
- 게시글 수정 `(POST /articles/{articleId}/form)`
- 게시글 삭제 `(POST /articles/{articleId}/delete)`

## ArticleComment
- 댓글 저장 `(POST /comments/postNewArticleComment)`
- 댓글 삭제 `(POST /comments/{commentId}/delete)`

# ISSUE, WORKFLOW
## Mockaroo 적용
JPA 연결 테스트를 하기위해 간단하게 테스트 코드를 먼저 작성하였고, 이 작성한 코드는 db에 데이터가 있어야 합니다.
테스트 데이터를 생성해주는 `mockaroo`를 사용했습니다. 
* https://www.mockaroo.com/
```
insert into article (title, content, hashtag, created_by, modified_by, created_at, modified_at) values
('임의로', '생성된', '#해시태그', '2021-05-30 23:53:46', '2021-03-10 08:48:50'),
('임의로', '생성된', '#해시태그', '2021-05-30 23:53:46', '2021-03-10 08:48:50') ...
```

## Spring Data REST, HAL Explorer 
<a href="https://github.com/jistol/sample/tree/master/ex-springdata-rest-sample">`Spring Data Rest`</a>는 Spring Data의 서브 프로젝트로 `Repository` 설정만으로 REST API 서버를 구성해주는 기능입니다.

사용자는 Entity 클래스와 Repository 인터페이스만 작성하면 나머지 CRUD 작업은 모두 알아서 RESTful하게 생성됩니다.

Repository 위에다가 `RepositoryRestResource` 어노테이션을 붙입니다.
### Data REST Example
```java
@RepositoryRestResource
public interface ArticleRepository extends JpaRepository<Article, Long> {
}
```

- 특이한 점은 테스트 코드를 통해 응답 데이터의 타입을 확인하면 `application/hal+json`으로 나오게 됩니다.
```java
    void 테스트() throws Exception {
        mvc.perform(get("/api/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));

    }
```

## QueryDsl를 도입
기존에 `mybatis`에서 동적쿼리를 작성하였는데, JPA에서는 어떻게 구현해야할 지 어려움이 있었습니다.

JPA에서는 다른 방법도 있었지만, QueryDsl을 사용하는게 가장 효율적이라고 생각이 들어 사용하게 되었습니다.

<a>공식문서</a>에서는 `QuerydslpredicateExecutor` 라는 인터페이스를 소개하고 있습니다.
```java
public interface QuerydslPredicateExecutor<T> {
    Optional<T> findById(Predicate predicate);
    Iterable<T> findAll(Predicate predicate); 
    long count(Predicate predicate);
    boolean exists(Predicate predicate); 
    // … more functionality omitted.
}
```
`QuerydslPredicateExcutor` 인터페이스의 사용 방법은 간단합니다.
### `QuerydslPredicateExcutor` Example
```java
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>
```
엔티티 안에 있는 모든 필드에 대한 기본 검색기능을 추가해줍니다.

대소문자를 구분하지 않습니다.

하지만 <b>부분 검색</b>이 안 된다는 단점이 있었습니다.

이 부분을 `QuerydslBinderCustomizer` 통해 설정을 할 수 있습니다.
### `QuerydslBinderCustomizer` Example
```java
@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>, 
        QuerydslBinderCustomizer<QArticle> {

    @Override // 인터페이스 파일이라 이 안에서 원래 구현을 넣을 수 없지만, Java8부터 가능
    default void customize(QuerydslBindings bindings, QArticle root){
           /* 검색 기능을 사용자가 원하는 바에 맞게 구현 */
    }
}

```

## thymeleaf3 decoupled logic 도입
decoupled (분리된) logic은 thymeleaf 기능 중 하나입니다. 

HTML과 XML 템플릿에서 순수 마크업과 로직이 들어간 부분을 분리시키는 기능입니다. 

디자이너가 이해하기 쉽기에 유용하다고 볼 수 있습니다. 

`decoupled logic`을 사용하기 위해서는 yml 파일에 설정을 추가해두고, `TymeleafConfig`를 구현하면 됩니다.

### Decoupled Logic Example
- 이렇게 설정을 하면 `th:` 이런 타임리프 코드들을 th.xml파일로 옮겨주어, html 파일에는 순수한 html 파일이 이루어지게 됩니다. 
```yaml
  thymeleaf3.decoupled-logic: true
```

```java
@Configuration
public class ThymeleafConfig {
    @Bean
    public SpringResourceTemplateResolver thymeleafTemplateResolver(
            SpringResourceTemplateResolver defaultTemplateResolver,
            Thymeleaf3Properties thymeleaf3Properties
    ) {
        defaultTemplateResolver.setUseDecoupledLogic(thymeleaf3Properties.isDecoupledLogic());

        return defaultTemplateResolver;
    }
    @RequiredArgsConstructor
    @Getter
    @ConstructorBinding
    @ConfigurationProperties("spring.thymeleaf3")
    public static class Thymeleaf3Properties {
        /**
         * Use Thymeleaf 3 Decoupled Logic
         */
        private final boolean decoupledLogic;
    }
}
```

# 데모 페이지
* https://project-board.herokuapp.com/
