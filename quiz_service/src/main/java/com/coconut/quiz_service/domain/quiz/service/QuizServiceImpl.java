package com.coconut.quiz_service.domain.quiz.service;


import com.coconut.global.dto.ListReqDto;
import com.coconut.quiz_service.domain.jobposting.domain.JobPosting;
import com.coconut.quiz_service.domain.jobposting.repository.JobPostingRepository;
import com.coconut.quiz_service.domain.quiz.domain.Answer;
import com.coconut.quiz_service.domain.quiz.domain.Quiz;
import com.coconut.quiz_service.domain.quiz.domain.mapper.AnswerMapper;
import com.coconut.quiz_service.domain.quiz.dto.*;
import com.coconut.quiz_service.domain.quiz.domain.mapper.QuizMapper;
import com.coconut.quiz_service.domain.quiz.repository.AnswerRepository;
import com.coconut.quiz_service.domain.quiz.repository.QuizRepository;
import com.coconut.quiz_service.domain.quiz.service.interfaces.OpenAiService;
import com.coconut.quiz_service.domain.quiz.service.interfaces.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class QuizServiceImpl implements QuizService {

  private final OpenAiService openAiService;

  private final QuizMapper quizMapper;

  private final AnswerMapper answerMapper;

  private final QuizRepository quizRepository;

  private final JobPostingRepository jobPostingRepository;

  private final AnswerRepository answerRepository;
  
  @Override
  public QuizDto generateQuiz() {
    QuizDto result = openAiService.generateQuiz();
    Quiz quiz = quizMapper.from(result);
    quizRepository.save(quiz);

    result.updateQuizId(quiz.getQuiz_id());
    return result;
  }

  @Transactional
  @Override
  public QuizDto insertExternalQuiz(QuizDto dto) {
    JobPosting jobPosting = jobPostingRepository.findById(dto.getJobPostingId())
            .orElseThrow(() -> new EntityNotFoundException());

    Quiz quiz = quizMapper.from(dto, jobPosting);
    quizRepository.save(quiz);

    dto.updateQuizId(quiz.getQuiz_id());
    return dto;
  }

  @Override
  public List<QuizDto> mapQuizToJobPosting(QuizToJobPostingDto dto) {
    JobPosting jobPosting = jobPostingRepository.findById(dto.getJobPostingId())
            .orElseThrow(() -> new EntityNotFoundException());

    List<Quiz> quizzes = quizRepository.findAllById(dto.getQuizIds());

    List<QuizDto> result = quizzes.stream()
            .peek((quiz) -> quiz.updateJobPosting(jobPosting))
            .map(QuizDto::from)
            .toList();

    return result;
  }


  // 테스트 코드 누락
  @Transactional
  @Override
  public List<QuizDto> findQuizByJobPostingId(long jobPostingId) {
    JobPosting jobPosting = jobPostingRepository.findByIdWithPessimisticWrite(jobPostingId)
            .orElseThrow(() -> new EntityNotFoundException());

    jobPosting.updateViewCount(jobPosting.getViewCount() + 1);

    List<Quiz> quizzes = quizRepository.findAllByJobPosting(jobPosting);

    List<QuizDto> result = quizzes.stream()
            .map(QuizDto::from)
            .toList();

    return result;
  }


  @Transactional
  @Override
  public AnswerDto createAnswer(AnswerCreateReqDto dto) {
    Quiz quiz = quizRepository.findById(dto.getQuizId())
            .orElseThrow(() -> new EntityNotFoundException("일치하는 퀴즈가 없습니다. [id]: " + dto.getQuizId()));

    AnswerDto result = openAiService.generateAnswer(quiz.getQuiz_id(), quiz.getQuiz(), quiz.getKeywords(), dto.getAnswer());

    Answer answer = answerMapper.from(dto, result.getScore());
    answerRepository.save(answer);

    return result;
  }

  @Override
  public List<QuizDto> getQuizList(ListReqDto dto) {
    Pageable pageable = dto.toPageable();
    Page<Quiz> quizList = quizRepository.findAll(pageable);

    return quizList.getContent().stream()
            .map(QuizDto::from)
            .toList();

  }

  @Override
  public void deleteAllQuiz() {
    quizRepository.deleteAll();
  }
}
