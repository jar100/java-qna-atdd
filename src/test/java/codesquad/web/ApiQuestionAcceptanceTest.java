package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static codesquad.domain.QuestionTest.newQuestion;
import static codesquad.domain.UserTest.newUser;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LogManager.getLogger(ApiQuestionAcceptanceTest.class);
    private static final String URL = "/api/questions";

    @Test
    public void create() {
        User newUser = newUser("testUser1");
        Question question = newQuestion(newUser);
        ResponseEntity<Void> response = basicAuthTemplate().postForEntity(URL, question, Void.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String location = response.getHeaders().getLocation().getPath();

        Question dbQuestion = basicAuthTemplate().getForObject(location, Question.class);
        softly.assertThat(dbQuestion).isNotNull();
    }

    @Test
    public void update_no_login() {
        // 로그인해서 qusertion 생성
        User newUser = newUser("testUser1");
        Question question = newQuestion("가나다라", "마바사");
        ResponseEntity<Void> response = basicAuthTemplate().postForEntity(URL, question, Void.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String location = response.getHeaders().getLocation().getPath();
        //
        Question updateQuestion = newQuestion("홍홍홍", "사사사");


        ResponseEntity<String> responseEntity =
                template().exchange(location, HttpMethod.PUT, createHttpEntity(updateQuestion), String.class);

        softly.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        log.debug("error message : {}", responseEntity.getBody());

    }


    @Test
    public void update_other_login() {
        // 로그인해서 qusertion 생성
        User newUser = newUser("testUser1");
        Question question = newQuestion("가나다라", "마바사");
        ResponseEntity<Void> response = basicAuthTemplate().postForEntity(URL, question, Void.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String location = response.getHeaders().getLocation().getPath();
        //
        Question updateQuestion = newQuestion("홍홍홍", "사사사");


        ResponseEntity<String> responseEntity =
                basicAuthTemplate(newUser).exchange(location, HttpMethod.PUT, createHttpEntity(updateQuestion), String.class);

        softly.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        log.debug("error message : {}", responseEntity.getBody());

    }

    @Test
    public void update() {
        // 로그인해서 qusertion 생성
        User newUser = newUser("testUser1");
        Question question = newQuestion("가나다라", "마바사");
        ResponseEntity<Void> response = basicAuthTemplate().postForEntity(URL, question, Void.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String location = response.getHeaders().getLocation().getPath();
        //
        Question updateQuestion = newQuestion("홍홍홍", "사사사");

        ResponseEntity<String> responseEntity =
                basicAuthTemplate().exchange(location, HttpMethod.PUT, createHttpEntity(updateQuestion), String.class);

        softly.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

}
