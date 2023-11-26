# 테스트 데이터 초기화에 @Transactional 사용하는 것에 대한 생각

얼마 전에 2개의 핫한 컨텐츠가 공유되었다.  

- [존경하는 재민님의 유튜브 - 테스트에서 @Transactional 을 사용해야 할까?](https://www.youtube.com/watch?v=PDhN6aiF7QQ)
- [존경하는 토비님의 페이스북](https://www.facebook.com/tobyilee/posts/pfbid037KmQz4TbwBfgkAXc8JjMjipMesF9iuTTWvMtUKirr3742cGfvVrq4Aft33CGmLWSl)

2개의 컨텐츠에서 테스트 데이터 초기화에 @Transactional 사용하는 것에 대해 **서로 다른 의견을 내신 것**이다. 

![intro](./images/intro.png)

마침 페이스북에 태깅되기도 했고 (ㅠㅠ)  
과거에 라이브 방송에서도 "향로님은 반대한다" 라고 언급되기도 했었다.  
(반대하는 것은 사실이기도 하고..)    
  
내 생각을 정리해야지 해야지 하다가, 마침 이번주에 시간이 되어서 정리하게 되었다.  
  
## 1. Spring Team은?

내 의견을 정리하기 전에,  
먼저 Spring Team 의 코드를 살펴보자.  
  
인프라스트럭쳐 계층 (데이터베이스) 테스트를 작성하는 팀의 코드를 보면 될 것 같아서 Spring Data JPA 팀의 코드를 찾아보면,  
아래와 같이 `@Transactional` 을 테스트 코드에서 사용하고 있는 것을 볼 수 있다.  

![spring-team](./images/spring-team.png)

- [spring-data-jpa/UserRepositoryTests](https://github.com/spring-projects/spring-data-jpa/blob/main/spring-data-jpa/src/test/java/org/springframework/data/jpa/repository/UserRepositoryTests.java)

다른 테스트 코드들도 찾아보면 `@Transactional` 을 사용한 코드들이 꽤 있다.  
모든 테스트 코드가 `@Transactional` 을 사용하고 있는 것은 아니지만,  
그래도 스프링 팀에서도 권장하는 방법이라는 것을 알 수 있다.

## 2. 반대하는 이유

그럼 나는 왜 반대할까?  
테스트 코드에서 `@Transactional` 을 테스트 데이터 초기화 용도로 사용할때 발생하는 문제점들을 알아보자.

> 모든 코드는 [Github](https://github.com/jojoldu/spring-boot-kotlin-test-in-action/tree/master/src/test/kotlin/com/jojoldu/testinaction/service/teacher) 에 있다.

### 1. 의도치 않은 트랜잭션 적용

너무 유명한 사례인, "의도치 않은 트랜잭션 적용" 이 있다.  
예를 들어 아래 코드는 
- 실제 코드에서는 `@Transacational` 이 누락되어있으며
- 테스트 코드에서는 데이터 초기화를 위해 `@Transacational` 이 포함되어있다.

![code1](./images/code1.png)

이럴 경우 테스트 컨텍스트에서는 트랜잭션이 있어 테스트는 통과한다.  

![result1_1](./images/result1_1.png)

반면 트랜잭션이 없으니 당연하지만 **실제 실행시에는 오류가 발생**한다.  

![result1_2](./images/result1_2.png)

물론 요즘의 대부분의 스프링에서의 개발은 Service 클래스에 `@Transacational(readOnly=true)` 를 기본적으로 선언해서 이렇게 ORM 에서 발생할만한 여지를 최소화하고 있다.  
  
그래서 팀의 규칙만 잘 정한다면 해당 이슈가 발생할만한 여지가 거의 없다.  
그렇지만, 실제 환경과 테스트 환경의 불일치로 정확한 테스트가 되지 않고 놓치는 부분이 발생할 수 있다는 것 역시 사실이다.

### 2. 트랜잭션 전파 속성을 조절한 테스트 롤백 실패

또 다른 경우로는 트랜잭션 전파 레벨을 수정해서 **새로운 트랜잭션이 필요한 경우 롤백이 되지 않는 것**이 있다.  
  
예를 들어 다음과 같이 기본 전파 레벨인 `REQUIRED` 가 아닌 `REQUIRES_NEW` 가 필요한 비즈니스 상황이 있다고 가정해보자.  

```kotlin
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun saveAllNew (teachers: List<Teacher>) : Int {
        return teacherRepository.saveAll(teachers).size
    }
```

정상적으로 롤백 되었는지 확인하기 위해 **다음 테스트에서 데이터베이스가 비어있는지** 확인해본다.  



### 3. 비동기 메서드 테스트 롤백 실패

```kotlin
@TestMethodOrder(
    MethodOrderer.OrderAnnotation::class)
@SpringBootTest // 트랜잭션 제거
class OriginalTest3 {

    @Autowired
    private lateinit var cleanUp: CleanUp

    @Autowired
    private lateinit var teacherService: NoTxTeacherService

    @Autowired
    private lateinit var teacherRepository: TeacherRepository

    // 명시적 초기화
    @AfterEach
    fun tearDown() {
        cleanUp.all()
    }

    @Test
    @Order(1)
    fun `비동기로 저장된다`() {
        // given
        val email = "jojoldu@gmail.com"
        val teacher = Teacher(name = "jojoldu", email = email)
        teacher.addStudent(Student(name = "John", email = "John@gmail.com", teacher = teacher))
        teacher.addStudent(Student(name = "Jane", email = "Jane@gmail.com", teacher = teacher))

        // when
        val futureResult = teacherService.asyncSave(teacher)
        val result = futureResult.get()

        assertThat(result).isEqualTo(email)
    }

    @Test
    @Order(2)
    fun `롤백 검증`() {
        val count = teacherRepository.count()
        println("DB에서 사라지지 않은 데이터: $count")

        assertThat(count).isEqualTo(0)
    }
}
```

![result]
### 4. TransactionalEventListener 동작 실패


## 마무리

요약하자면 개발자가 별도의 실수 없이 일반적인 트랜잭션 방법을 적용해서 코드를 작성했다면, 테스트에서 `@Transactionl` 을 사용하는 것에 문제가 없으나,  
트랜잭션과 관련된 구현이 들어간 코드에서는 테스트에서 `@Transactional` 을 사용하면 문제가 발생할 수 있다.  
  
앞서 소개했던 것처럼 스프링 팀에서도 `@Transactional` 을 테스트 코드에서 사용하고 있다.  
그래서 테스트 코드에서 `@Transactional` 을 사용하는 것이 안티 패턴이라고 말할 수는 없다.  
다만, 테스트 코드에서 `@Transactional` 을 사용할 때는 위에서 설명한 것처럼 주의해야 한다.  
  
나는 **팀의 구성원 모두가 실수할 수 없을만큼 쉬운 방법들**을 팀의 Ground Rule 기준으로 둔다.  
AA 상황에서는 XX로 해야하고,  
BB 상황에서는 YY로 해야한다 등의 규칙을 만들면   
팀의 Ground Rule 이 복잡해지고, 제대로 이해하지 못할 수도 있다.  
그래서 최대한 간단하게, 모두가 이해할 수 있는 수준으로 설정해왔다.  
  
팀 마다 추구하는 방향성이 다르지만,  
나 같은 경우는 위와 같은 기준으로 항상 **테스트 코드에서 `@Transactional` 을 사용하지 않는 것을 권장한다**.  

> 같은 이유로 `@DataJpaTest` 를 권장하지 않는다.  
> 내부적으로 `@Transactional` 을 선언하고 있기 때문이다.

## 번외 - 테스트 데이터 초기화

테스트 데이터 초기화를 위해 `@Transactional` 을 사용하지 않는다면, 그럼 어떻게 하나?  
매번 테스트에 사용된 테이블들을 하나씩 찾아보면서 초기화해야하는 것인가?  

- 모든 테이블간에 FK 제약 조건을 사용하지 않는다.
  - 제약 조건이 있으면 테이블의 크기가 커질수록 Online DDL 등을 사용하기 어려워 테이블 변경이 어렵다.
  - 테이블 간의 관계를 애플리케이션에서 관리한다.
  - 데이터에 대한 접근은 ORM 등의 계층에서 일원화한다.

이런 환경을 항상 구축하고 있어서, **테스트 데이터 초기화는 굉장히 쉽다**.  
JPA로 관리되는 모든 Entity들의 연관 테이블을 `truncate` 하면 되기 때문이다.


> 근데 스프링 팀도, 토비님도, 영한님도 `@Transactional` 를 테스트 데이터 초기화에 사용하는 데,   
> 이것이 더 메인 스트림 아닌가? 하는 생각을 종종 한다.  
> 그래서 이 글은 나중에 다시 한번 쓰게 될지도 모른다.