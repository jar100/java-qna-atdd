package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static codesquad.domain.QuestionTest.QNA1;
import static codesquad.domain.QuestionTest.QNA2;
import static codesquad.domain.QuestionTest.newQuestion;
import static codesquad.domain.UserTest.JAVAJIGI;
import static codesquad.domain.UserTest.SANJIGI;
import static codesquad.domain.UserTest.newUser;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LogManager.getLogger(ApiQuestionAcceptanceTest.class);
    private static final String URL = "/api/questions";

    private static String location;
    private static Question question;

    @Before
    public void setUp() {
        question = newQuestion(JAVAJIGI);
        location = createResource(URL,question);
    }

    @Test
    public void create() {
        Question dbQuestion = basicAuthTemplate().getForObject(location, Question.class);
        softly.assertThat(dbQuestion).isNotNull();
    }

    @Test
    public void update() {
        Question question = newQuestion(JAVAJIGI);
        String location = createResource(URL,question);
        Question updateQuestion = newQuestion("홍홍홍", "사사사");

        ResponseEntity<String> responseEntity =
                basicAuthTemplate().exchange(location, HttpMethod.PUT, createHttpEntity(updateQuestion), String.class);
        softly.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    public void update_no_login() {
        Question question = newQuestion(JAVAJIGI);
        String location = createResource(URL,question);
        Question updateQuestion = newQuestion("홍홍홍", "사사사");

        ResponseEntity<String> responseEntity =
                template().exchange(location, HttpMethod.PUT, createHttpEntity(updateQuestion), String.class);

        softly.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        log.debug("error message : {}", responseEntity.getBody());

    }


    @Test
    public void update_other_login() {
        Question question = newQuestion(JAVAJIGI);
        String location = createResource(URL,question);
        Question updateQuestion = newQuestion("홍홍홍", "사사사");

        ResponseEntity<String> responseEntity =
                basicAuthTemplate(SANJIGI).exchange(location, HttpMethod.PUT, createHttpEntity(updateQuestion), String.class);

        softly.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        log.debug("error message : {}", responseEntity.getBody());

    }

}
