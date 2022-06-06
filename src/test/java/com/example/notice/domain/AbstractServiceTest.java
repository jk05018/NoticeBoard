package com.example.notice.domain;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@TestPropertySource(properties = {
	"spring.jpa.properties.hibernate.show_sql=true",
	"spring.jpa.properties.hibernate.format_sql=true",
	"logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE"
})
public abstract class AbstractServiceTest {
}
