package codesquad.web;

import codesquad.domain.Answer;
import codesquad.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static codesquad.domain.UserTest.SANJIGI;

public class ApiAnswerAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LogManager.getLogger(ApiQuestionAcceptanceTest.class);
    private static final String URL = "/api/questions/1/answers";


    //create
    @Test
    public void create() {
        String contents = "나는댓글이다";
        ResponseEntity<Void> response = basicAuthTemplate().postForEntity(URL, contents, Void.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String location = response.getHeaders().getLocation().getPath();

        Answer dbAnswer = basicAuthTemplate().getForObject(location, Answer.class);
        softly.assertThat(dbAnswer).isNotNull();
    }


    @Test
    public void update() {
        String contents = "나는댓글이다";
        ResponseEntity<Void> response = basicAuthTemplate().postForEntity(URL, contents, Void.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        // update
        String location = response.getHeaders().getLocation().getPath();

        String updateCountents = "수정한 댓글입니다.";
        ResponseEntity<String> responseEntity =
                basicAuthTemplate().exchange(location, HttpMethod.PUT, createHttpEntity(updateCountents), String.class);


        softly.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void update_no_login() {
        String contents = "나는댓글이다";
        ResponseEntity<Void> response = basicAuthTemplate().postForEntity(URL, contents, Void.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String location = response.getHeaders().getLocation().getPath();
        String updateCountents = "수정한 댓글입니다.";

        ResponseEntity<String> responseEntity =
                template().exchange(location, HttpMethod.PUT, createHttpEntity(updateCountents), String.class);

        softly.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        log.debug("error message : {}", responseEntity.getBody());
    }

    @Test
    public void update_no_otherUser() throws Exception {
        //update_no
        String contents = "나는댓글이다";
        ResponseEntity<Void> response = basicAuthTemplate().postForEntity(URL, contents, Void.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String location = response.getHeaders().getLocation().getPath();
        String updateCountents = "수정한 댓글입니다.";

        ResponseEntity<String> responseEntity =
                basicAuthTemplate(SANJIGI).exchange(location, HttpMethod.PUT, createHttpEntity(updateCountents), String.class);

        softly.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        log.debug("error message : {}", responseEntity.getBody());
    }


}
