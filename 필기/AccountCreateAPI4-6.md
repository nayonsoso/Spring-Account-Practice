1. 이제 컨트롤러에 대해서 테스트를 진행해보려 한다.

일단 이전에 우리가 해둔 테스트 코드를 보자. : @WebMvcTest(AccountController.class)를 이용해서 진짜로 컨트롤러를 띄우고
컨트롤러가 의존하는 것들을 @MockBean으로 만들어줬다. 그리고 @MockBean으로 만든 필드들을 given & willReturn 을 통해서 가짜로 값을 넣어줬다.
이번에도 같은 것을 할 것인데, 컨트롤러 테스트이므로 특별한 것을 추가했다!
바로 `mockMvc.perform()`메소드이다.

2.
```
mockMvc.perform(post("account")
                .contentType(MediaType.APPLICATION_JSON)
                .content())
```

이렇게 post 방식으로 /account URL에 바로 접근하는게 가능하고, contentType으로 헤더를 채우고, content로 바디를 채울 수 있다.

3. content 내부를 채우는 방법은 그냥 String으로 json 형식에 맞게 작성하는 방법이 있고, jackson이라는 json을 객체로, 객체를 json으로 바꿔주는 클래스를 사용할 수도 있다. ==> 그건 ObjectMapper로 이미 빈으로 등록되어있어서 컨트롤러 테스트 안에서는 @Autowired만 해주면 됨

4. .andExpect(jsonPath("$.accountNumber"))에 있는 jsonPath는 응답에 있는 http 바디를 의미함

5. 이제 서비스에 대해 테스트를 진행해보겠다.
그런데 왜 서비스에 대해 이런식으로 테스트를 진행하는지 하나도 이해가 되지 않습니다.
테스트 코드의 의미가 무엇인지.. 재귀함수의 콜스택을 따라가듯이 신중하게 한줄 한줄 따라가면 좀 이해하려나요..?
무튼 이해가 안되는 것으로 생각하고 이 부분은 그냥 따라치면서 진행해야 할 것 같습니다.
제 생각에 강사님이 처음 스프링 부트의 예제로 너무 어려운 것을 고른 것 같습니다 ㅠ