# JPA와 @Transactional 을 테스트 코드에 사용할 때 주의하기


[](https://www.facebook.com/tobyilee/posts/pfbid037KmQz4TbwBfgkAXc8JjMjipMesF9iuTTWvMtUKirr3742cGfvVrq4Aft33CGmLWSl)

Spring Data JPA 팀에서도 아래와 같이 `@Transactional` 을 테스트 코드에서 사용하고 있다.  

![spring-team](./images/spring-team.png)

- [spring-data-jpa/UserRepositoryTests](https://github.com/spring-projects/spring-data-jpa/blob/main/spring-data-jpa/src/test/java/org/springframework/data/jpa/repository/UserRepositoryTests.java)

