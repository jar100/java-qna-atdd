package codesquad.web;

import codesquad.domain.QuestionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;


public class QuestionAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LogManager.getLogger(QuestionAcceptanceTest.class);


    /*질문 CRUD 기능을 구현한다.
    모든 사용자는 질문을 볼 수 있다.
    로그인한 사용자에 대해서만 질문이 가능하다.
    자신이 작성한 질문에 대해서만 수정/삭제가 가능하다.

    질문 CRUD 기능을 ATDD 기반으로 구현한다.
    Question 코드에 대한 단위 테스트를 구현한다.
    프로덕션 코드와 테스트 코드의 중복을 제거한다.
    QuestionAcceptanceTest, UserAcceptanceTest에서 발생하는 중복 코드를 제거한다.
*/

    @Autowired
    QuestionRepository questionRepository;

    @Test
    public void createFormOk() throws Exception {
        ResponseEntity<String> response = basicAuthTemplate().getForEntity("/questions/form", String.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        log.debug("body : {}", response.getBody());
    }

    @Test
    public void createFormFailed() throws Exception {
        ResponseEntity<String> response = template().getForEntity("/questions/form", String.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void create() {

    }

    @Test
    public void listTest() throws Exception {
        ResponseEntity<String> response = template().getForEntity("/", String.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        log.debug("body : {}", response.getBody());
        softly.assertThat(response.getBody()).contains(defaltQuestion().getTitle());

    }

    @Test
    public void showFormOkTest() throws Exception {
        ResponseEntity<String> response = template().getForEntity("/questions/1", String.class);
        log.debug("body : {}", response.getBody());
        softly.assertThat(response.getBody()).contains(defaltQuestion().getContents());
    }


    @Test
    public void updateFormNoTest() throws Exception {
        //TODO 수정폼 못넘어가는 테스트 코드작성
    }

    @Test
    public void updateFormOkTest() throws Exception {
        //TODO 수정폼 넘어가는 테스트 코드작성
    }

    @Test
    public void update() throws  Exception {
        //TODO 업데이트 ok
    }

    @Test
    public void update_no() throws  Exception {
        //TODO 업데이트 no
    }

    @Test
    public void deleted() throws  Exception {
        //TODO 삭제 ok
    }

    @Test
    public void deleted_no() throws  Exception {
        //TODO 삭제 no
    }
}
