package com.kalaari.integrationtest;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.listeners.MockCreationListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalaari.KalaariApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = { "classpath:/schema-kalaari.sql" })
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = KalaariApplication.class)
public abstract class IntegrationTestsBase {

    protected static List<Object> createdMocks = new LinkedList<>();

    /* REGISTER LISTENER TO ADD EACH MOCK DONE EVER */
    static {
        Mockito.framework().addListener((MockCreationListener) (mock, settings) -> createdMocks.add(mock));
    }

    /* MISC BEANS START */
    @Autowired
    protected TestRestTemplate restTemplate;
    protected ObjectMapper objectMapper = new ObjectMapper();

    @After
    public void printMocksOfThisTest() {
        printAllMocks();
        log.info(" ============================================= ");
    }

    /* PRINT OUT ALL EXISTING MOCKS */
    private void printAllMocks() {
        log.info("Current Mocks before reset:=> ");
        for (Object createdMock : createdMocks) {
            log.info("MOCK: " + String.valueOf(Mockito.mockingDetails(createdMock).getMock()));
        }
    }
}
