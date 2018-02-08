package codesquad.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import codesquad.dto.AnswerDto;
import codesquad.dto.QuestionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codesquad.CannotDeleteException;
import codesquad.domain.Answer;
import codesquad.domain.AnswerRepository;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;

@Service("qnaService")
public class QnaService {
    private static final Logger log = LoggerFactory.getLogger(QnaService.class);

    @Resource(name = "questionRepository")
    private QuestionRepository questionRepository;

    @Resource(name = "answerRepository")
    private AnswerRepository answerRepository;

    @Resource(name = "deleteHistoryService")
    private DeleteHistoryService deleteHistoryService;

    public Question create(User loginUser, Question question) {
        question.writeBy(loginUser);
        log.debug("question : {}", question);
        return questionRepository.save(question);
    }

    public Question findById(long id) {
        return questionRepository.findOne(id);
    }

    @Transactional
    public Question updateQuestion(Long id, User loginUser, QuestionDto questionDto) {
        Question question = questionRepository.findOne(id);
        return question.update(loginUser, questionDto);
    }

    @Transactional
    public Question deleteQuestion(User loginUser, Long idx) {
        Question deletedQuestion = questionRepository.findOne(idx);
        return deletedQuestion.delete(loginUser);
    }

    public Iterable<Question> findAll() {
        return questionRepository.findByDeleted(false);
    }

    public List<Question> findAll(Pageable pageable) {
        return questionRepository
                .findAll(pageable)
                .getContent()
                .stream()
                .filter(question -> !question.isDeleted())
                .collect(Collectors.toList());
    }

    public Answer findAnswerById(long idx) {
        return answerRepository.findOne(idx);
    }

    @Transactional
    public Answer addAnswer(User loginUser, long questionId, String contents) {
        Question question = questionRepository.findOne(questionId);
        Answer answer = new Answer(loginUser, contents);
        question.addAnswer(answer);
        return answer;
    }

    @Transactional
    public Answer updateAnswer(User loginUser, long answerId, AnswerDto answerDto) {
        Answer answer = answerRepository.findOne(answerId);
        answer.update(loginUser, answerDto);
        return answer;
    }

    @Transactional
    public Answer deleteAnswer(User loginUser, long id) {
        Answer answer = answerRepository.findOne(id);
        answer.delete(loginUser);
        return answer;
    }
}
