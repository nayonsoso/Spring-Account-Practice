<눈여겨볼만한 점>
0. @RequestBody 어노테이션으로 json 요청, 응답으로 바로 클래스로 받을 수 있음
1. 계좌 생성을 위한 dto 패키지를 따로 만들고, 그 안에 CreateAccount 클래스를 만든 다음, 그 안에 Request, Response 클래스를 static으로 만듦 -> 계좌를 생성할 때 요청과 응답이 어떻게 오는지를 한눈에 볼 수 있었음
2. 요청을 받을 때의 객체가 (=CreateAccount.Request) 유효한지 검증하기 위해서 @RequestBody @Valid 어노테이션을 달아줌
3. @Valid의 설정을 위해서 CreateAccount.Request클래스 안에서 어노테이션을 달아줌 @NotNull / @Min(1) 이런 식으로!
4. AccountUserRepository에서 그저 JpaRepository를 extends하기만 했는데, findById 같은 sql 이 실행될법한 메소드가 알아서 실행되는게 놀라움
5. JPA의 findById의 리턴값이 Optional이라는 것이 새롭고, 그 안에 값이 없으면 orElseThrow안의 예외가 던져짐, 그리고 orElseThrow안에 람다 함수식이 올 수 있다는 것도 새로움
6. 커스텀 Exception을 담기 위해서 exception 패키지를 만들고, 그 안에 클래스로 커스텀 예외를 넣어준다는게 신기 & 대부분의 커스텀 예외는 RuntimeExcpetion을 구현한다는데 그 이유도 모르겠고 그렇다는 것도 신기
7. 커스텀 예외를 위한 커스텀 에러코드 열거체를 만들었는데, 이를 type 패키지에 넣는다는게 새로움 & ErrorCode 열거체 안에서 desciption이라는 변수를 따로만들 수 있고, 그 Enum위에 @AllArgsConstructor을 선언하여 desciption을 인자로 하는 ErrorCode 생성자가 만들어질 수있다는  것도 신기!! 그리고 Enum에 Getter을 할 수있다는 것도 신기함
8. JpaRepository는 언제나 신기함.. 특히 조금 복잡할 수 있는 메소드 (=findFirstByOrderById Desc) 도 JPA의 형식에 맞으므로 알아서 구현된다는게 신기하다.
9. findFirstByOrderByIdDesc의 리턴값이 Optional인데, 여기에 .map 함수를 적용할 수 있구나! 아마 사용법은 stream에서와 똑같이 람다식의 매개변수에 뭔가를 해서 그 결과를 리턴하는 것 같음 & 그리고 map에 이어서 OrElse를 할 수 있다는것도 신기!!
10. 그리고 서비스에있는 createAcount 함수가 accountRepository.save()를 리턴했는데, 그러면 accountRepository.save()가 저장을 한 Account를 리턴한다는 거잖아! 왕신기
11. 그리고 accountRepository.save() 안에 어마어마하게 긴 Account.builer().build()가 들어갈 수 있다는 것도 신기하다. 아마 setter을 계속 선언하는 건 너무 기니까 builder로 간단하게 만든거겠지?
12. 아 그리고 domain 패키지에있는 AccountUser안의 createdAt과 updatedAt위에 각각 @CreatedDate, @LastModifiedDate어노테이션이 선언되어 있는데 이 어노테이션을 제대로 실행하기 위해서는 AccountUser 클래스에 @EntityListeners(AuditingEntityListener.class)를 선언해줘야하고, 이 어노테이션을 유효하게 만들기 위해 config패키지에 JpaAuditingConfiguration.class를 만들고 그 위에 @Configuration와 @EnableJpaAuditing를 적어주기만해도 된다는게 신기! 어떤원리일까??
13. 그리고 createdAt, updatedAt, Id는 대부분의 도메인에 만들어줘야 한다고 하셨음! 이거 기억해야 할 듯~~