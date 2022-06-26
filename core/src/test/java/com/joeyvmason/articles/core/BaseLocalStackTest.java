package com.joeyvmason.articles.core;

import cloud.localstack.docker.LocalstackDockerExtension;
import cloud.localstack.docker.annotation.LocalstackDockerProperties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.io.*;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;


@SpringBootTest
@ContextConfiguration(classes = BaseIntegrationTestConfig.class)
@ExtendWith(LocalstackDockerExtension.class)
@LocalstackDockerProperties
public abstract class BaseLocalStackTest {
    private static final Logger LOG = LoggerFactory.getLogger(BaseLocalStackTest.class);

    @BeforeAll
    public static void beforeAll() throws Exception {
        LOG.info("Setting up");

        File location = Paths.get("", "../.cdk").toFile();

        runCommand(location, "AWS_REGION=us-east-1 AWS_ACCOUNT_ID=000000000000 cdklocal bootstrap -v");
        runCommand(location, "AWS_REGION=us-east-1 AWS_ACCOUNT_ID=000000000000 cdklocal deploy DynamoStack -v");

        LOG.info("Finished setting up");
    }

    public static void runCommand(File whereToRun, String command) throws Exception {
        ProcessBuilder builder = new ProcessBuilder();
        builder.directory(whereToRun);
        builder.command("sh", "-c", command);

        Process process = builder.start();

        OutputStream outputStream = process.getOutputStream();
        InputStream inputStream = process.getInputStream();
        InputStream errorStream = process.getErrorStream();

        printStream(inputStream);
        printStream(errorStream);

        boolean isFinished = process.waitFor(30, TimeUnit.SECONDS);
        outputStream.flush();
        outputStream.close();

        if(!isFinished) {
            process.destroyForcibly();
        }
    }

    private static void printStream(InputStream inputStream) throws IOException {
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while((line = bufferedReader.readLine()) != null) {
                LOG.info(line);
            }

        }
    }

}
