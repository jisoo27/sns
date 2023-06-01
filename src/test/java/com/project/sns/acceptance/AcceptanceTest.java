package com.project.sns.acceptance;

import com.project.sns.util.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = DEFINED_PORT)
public class AcceptanceTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        databaseCleanup.execute();
    }
}
