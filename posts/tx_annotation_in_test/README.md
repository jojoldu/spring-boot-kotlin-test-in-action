# @Transactional 을 테스트 데이터 초기화에 사용하는 것은 어떨까?


![intro](./images/intro.png)

[facebook](https://www.facebook.com/tobyilee/posts/pfbid037KmQz4TbwBfgkAXc8JjMjipMesF9iuTTWvMtUKirr3742cGfvVrq4Aft33CGmLWSl)

Spring Data JPA 팀에서도 아래와 같이 `@Transactional` 을 테스트 코드에서 사용하고 있다.  

![spring-team](./images/spring-team.png)

- [spring-data-jpa/UserRepositoryTests](https://github.com/spring-projects/spring-data-jpa/blob/main/spring-data-jpa/src/test/java/org/springframework/data/jpa/repository/UserRepositoryTests.java)


## 이유

### 1. 의도치 않은 트랜잭션 적용

### 2. 테스트 클래스 내부에서의 격리 실패

테스트 클래스에 트랜잭션이 잡혀서, 
테스트 클래스 내부에서는 서로 격리된 테스트가 불가능하다.

테스트 메소드마다 트랜잭션 선언해서 해결 가능

### 3. 트랜잭션 전파 속성을 조절한 메서드나 비동기 메서드 테스트 시 롤백 실패

### 4. TransactionalEventListener 동작 실패

### 5. JPA랑 같이 사용할 때 로그에 쿼리가 안보이는 현상

## 마무리

요약하자면 개발자가 별도의 실수 없이 일반적인 트랜잭션 방법을 적용해서 코드를 작성했다면, 테스트에서 `@Transactionl` 을 사용하는 것에 문제가 없으나,  
그렇지 않다면 테스트에서 `@Transactional` 을 사용하면 의도치 않게 작동된다.  

앞서 소개했던 것처럼 스프링 팀에서도 `@Transactional` 을 테스트 코드에서 사용하고 있다.  
그래서 테스트 코드에서 `@Transactional` 을 사용하는 것이 안티패턴이라고 말할 수는 없다.  
다만, 테스트 코드에서 `@Transactional` 을 사용할 때는 위에서 설명한 것처럼 주의해야 한다.  
  
나는 **팀의 구성원 모두가 실수할 수 없을만큼 쉬운 방법들**을 팀의 Ground Rule 설정할때의 기준으로 둔다.  
AA 상황에서는 XX로 해야하고,  
BB 상황에서는 YY로 해야한다 등의 규칙을 만들면   
팀의 Ground Rule 이 복잡해지고, 제대로 이해하지 못할 수도 있다.  
그래서 최대한 간단하게, 모두가 이해할 수 있는 수준으로 설정한다.  
  
팀 마다 추구하는 방향성이 다르지만,  
나 같은 경우는 위와 같은 기준으로 항상 테스트 코드에서 `@Transactional` 을 사용하지 않는 것을 권장한다.