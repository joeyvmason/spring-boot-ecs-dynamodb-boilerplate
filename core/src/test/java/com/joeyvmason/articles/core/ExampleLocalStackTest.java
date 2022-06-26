package com.joeyvmason.articles.core;

import cloud.localstack.awssdkv1.TestUtils;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExampleLocalStackTest extends BaseLocalStackTest {

    @Test
    public void shouldGenerateLocalStackTables() {
        // Tables are in LocalStack but not showing up when we call listTables()
        AmazonDynamoDB client = TestUtils.getClientDynamoDB();

        ListTablesResult tables = client.listTables();
        assertThat(client.listTables().getTableNames()).hasSize(1);
    }

}
