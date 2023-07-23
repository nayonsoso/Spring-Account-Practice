package com.example.account.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfiguration {
}
// 이 클래스 자체가 자동으로 빈으로 등록이 되어서 @EntityListeners(AuditingEntityListener.class)
// 어노테이션을 가능하게 만들어 줌