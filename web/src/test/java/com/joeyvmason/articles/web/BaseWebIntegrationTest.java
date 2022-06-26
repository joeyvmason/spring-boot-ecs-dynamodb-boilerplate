package com.joeyvmason.articles.web;

import com.joeyvmason.articles.core.BaseIntegrationTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = BaseWebIntegrationTestConfig.class)
public abstract class BaseWebIntegrationTest extends BaseIntegrationTest {
}
