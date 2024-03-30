package com.coconut.quiz_spring.domain.quiz.service;


import com.coconut.quiz_spring.domain.jobposting.domain.JobPosting;
import com.coconut.quiz_spring.domain.jobposting.repository.JobPostingRepository;
import com.coconut.quiz_spring.domain.quiz.domain.Quiz;
import com.coconut.quiz_spring.domain.quiz.dto.AnswerCreateReqDto;
import com.coconut.quiz_spring.domain.quiz.dto.AnswerDto;
import com.coconut.quiz_spring.domain.quiz.dto.QuizDto;
import com.coconut.quiz_spring.domain.quiz.domain.mapper.QuizMapper;
import com.coconut.quiz_spring.domain.quiz.dto.QuizToJobPostingDto;
import com.coconut.quiz_spring.domain.quiz.repository.QuizRepository;
import com.coconut.quiz_spring.domain.quiz.service.interfaces.OpenAiService;
import com.coconut.quiz_spring.domain.quiz.service.interfaces.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  private final QuizRepository quizRepository;

  private final JobPostingRepository jobPostingRepository;

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
  @Override
  public List<QuizDto> findQuizByJobPostingId(long jobPostingId) {
    JobPosting jobPosting = jobPostingRepository.findById(jobPostingId)
            .orElseThrow(() -> new EntityNotFoundException());

    List<Quiz> quizzes = quizRepository.findAllByJobPosting(jobPosting);

    List<QuizDto> result = quizzes.stream()
            .map(QuizDto::from)
            .toList();

    return result;
  }


  @Override
  public AnswerDto createAnswer(AnswerCreateReqDto dto) {
    Quiz quiz = quizRepository.findById(dto.getQuizId())
            .orElseThrow(() -> new EntityNotFoundException("일치하는 퀴즈가 없습니다. [id]: " + dto.getQuizId()));

    AnswerDto answer = openAiService.generateAnswer(quiz.getQuiz(), quiz.getKeywords(), dto.getAnswer());
    return answer;
  }
}
