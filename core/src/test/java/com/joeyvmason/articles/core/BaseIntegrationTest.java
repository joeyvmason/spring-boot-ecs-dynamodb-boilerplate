package com.joeyvmason.articles.core;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = BaseIntegrationTestConfig.class)
public abstract class BaseIntegrationTest {


}
