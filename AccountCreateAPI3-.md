0. 서비스에서 createAccount를 실행하면, 새로 계좌를 만들고 이를 save한 것을 컨트롤러에 리턴하는 구조이다.
   이때, jpa의 save 메소드는 결과로 저장한 엔티티를 리턴하는데, 엔티티는 일반 클래스와는 다른 성격을 가지고 있다.
   그런데 엔티티를 계층간에 주고받고 하면서 추가적인 쿼리를 날리려 하다 보면, 트랜젝션이 없기 때문에 오류가 발생할 수 있다.
   그리고 컨트롤러로 응답할 때, Account객체 만 필요한 것일 수도 있고 더 많은 정보가 필요한 것일 수 있는데,
   엔티티는 DB와 일대일 대칭하는데에 사용되기 때문에 엔티티를 바꿔줄 수도 없는 노릇!
   따라서 서비스와 컨트로러 사이를 이동할 dto를 새로 만들어주는게 좋다.
>> 이렇게 엔티티보다는 단순하지만, 유사한 DTO를 만드는게 되게 자주 쓰이는 방법이라고 함!!

1. 근데 이때, Account를 save하고 ACcount를 리턴하는건 맞으니까.. 이걸 그대로 AccountDto로 바꾸고 싶음!
   => AccountDto에 static 함수 fromEntity를 만들어주면 됨! (이것도 자주 쓰이는 방법)
   그리고 그 내부에서 account.get~~()를 뺴온 다음 바로 AccountDto.builer().~~() 안에 넣어주면 됨!
   예시 ::
```
public static AccountDto fromEntity(Account account){
        return AccountDto.builder()
                .userId(account.getAccountUser().getId())
                .accountNumber(account.getAccountNumber())
                .registeredAt(account.getRegisteredAt())
                .ubregisteredAt(account.getUnregisteredAt())
                .build();
    }
```

2. 강사님은 계속 한번 쓰이는 변수는 의미가 없다면서 return문을 엄청 길게 만드는데, 그 이유가 중간에 다른 로직이 끼어들 수 있다는 것 때문! (변동 가능성이 높아진다)

3. AccountDto가 컨트롤러와 서비스간의 dto였다면, 이를 응답으로 바꾸기 위해서 다시 같은 원리로,
   CreateAccount.Response 클래스 안에 static으로 from 함수를 만들어주면 됨!

4. 힘들게 만든 계좌 생성 API를 테스트해보려 한다. >> 항상 하던대로 지구본 눌러서 해도 되지만,
   test 폴더 바로 아래에 http 폴더를 만들어서 account라는 이름의 http request 파일을 만들어서 하는 방법도 있음! (이게 더 편리)

5. 그런데 계좌 생성을 하기 위해서 /account를 입력했는데 오류 발생 -> 유저가 아무도 저장되어있지 않기 때문에 userId로 검색을 했을 때 에러 뜬 것임. 따라서 처음 요구조건에 맞게 미리 데이터를 저장해보자. -> resource 폴더 밑에 data.sql 파일을 만든다 -> sql 문으로 accountUser을 넣어준다.

6. 이때 sql 문을 쓰면서 주의할 점은, 우리가 대문자로 선언한건 소문자로 바꿔주고, 카멜 표기법으로 합성어 시작을 대문자로 만들어준건 _소문자로 바꿔야 한다는 점이다 e.g : AccountUser => account_user

7. 또하나 주의할 점은, application.yml 파일에서 jpa: defer-datasource-initialization: true를 선언해야 한다는 점이다. (데이터 초기값 입력을 테이블 생성 이후로 미루겠다는 의미)
