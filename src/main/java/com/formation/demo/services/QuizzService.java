package com.formation.demo.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.formation.demo.entities.Answer;
import com.formation.demo.entities.Options;
import com.formation.demo.entities.Question;
import com.formation.demo.entities.Quizz;
import com.formation.demo.entities.QuizSubmission;
import com.formation.demo.repository.QuizSubmissionRepository;
import com.formation.demo.repository.QuizzRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizzService {

    private final QuizzRepository quizzRepository;
    private final QuizSubmissionRepository quizzAnswerRepository;

    // creation de quizz
    public Quizz createQuizz(Quizz quizz) {
        quizz.setActive(true);
        if (quizz.getCreatedAt() == null) {
            quizz.setCreatedAt(LocalDateTime.now());
        }
        quizz.setUpdatedAt(LocalDateTime.now());
        return quizzRepository.save(quizz);
    }

    // Lancer un quizz

    public QuizSubmission startQuizz(String quizzId, String userId) {
        // Verifier si l'utilisateur a deja commence le quizz
        QuizSubmission submission = QuizSubmission.builder()
                .quizzId(quizzId)
                .userId(userId)
                .startedAt(LocalDateTime.now())
                .build();
        return quizzAnswerRepository.save(submission);
    }

    /* Soumettre réponses */
    public QuizSubmission submitQuiz(
            String quizId,
            String userId,
            QuizSubmission answers) {
        Quizz quiz = quizzRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz introuvable"));
        System.out.println("Quizz Id" + quiz.getId() + " userId Id:" + userId);

        int score = calculateScore(quiz, answers.getAnswers());
        QuizSubmission submission = new QuizSubmission();
        // QuizSubmission existingSubmission = submission.get(submission.size() - 1);
        submission.setAnswers(answers.getAnswers());
        submission.setScore(score);
        submission.setPassed(score >= quiz.getMinimumScore());
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setPassed(score >= quiz.getMinimumScore());
        submission.setQuizzId(quizId);
        submission.setUserId(userId);

        return quizzAnswerRepository.save(submission);
    }

    private int calculateScore(Quizz quiz, List<Answer> answers) {
        int total = 0;

        for (Question q : quiz.getQuestions()) {

            Answer answer = answers.stream()
                    .filter(a -> a.getQuestionId().equals(String.valueOf(q.getId())))
                    .findFirst()
                    .orElse(null);

            if (answer == null)
                continue;

            // Index des options correctes
            Set<Integer> correctIndexes = new HashSet<>();
            List<Options> options = q.getOptions();

            for (int i = 0; i < options.size(); i++) {
                if (options.get(i).isCorrect()) {
                    correctIndexes.add(i);
                }
            }

            // Index sélectionnés par l'utilisateur
            Set<Integer> selectedIndexes = answer.getSelectedOptionIds().stream()
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());

            if (correctIndexes.equals(selectedIndexes)) {
                total += q.getScore();
            }
        }
        return total;
    }

    // get seance by user and seance id
    public Optional<Quizz> getQuizzBySeanceAndPromotion(String seanceId, String promotionId) {
        return quizzRepository.findBySeanceIdAndPromotionId(seanceId, promotionId);
    }

    private Quizz resolveLatestQuizForSubmission(String quizIdOrSeanceId) {
        Optional<Quizz> quizById = quizzRepository.findById(quizIdOrSeanceId);
        if (quizById.isPresent()) {
            String seanceId = quizById.get().getSeanceId();
            if (seanceId != null) {
                Optional<Quizz> latestBySeance = selectLatestQuizz(quizzRepository.findBySeanceId(seanceId));
                if (latestBySeance.isPresent()) {
                    return latestBySeance.get();
                }
            }
            return quizById.get();
        }

        return selectLatestQuizz(quizzRepository.findBySeanceId(quizIdOrSeanceId))
                .orElseThrow(() -> new RuntimeException("Quiz introuvable"));
    }

    private Optional<Quizz> selectLatestQuizz(List<Quizz> quizzes) {
        return quizzes.stream()
                .max(Comparator.comparing(this::resolveQuizDate));
    }

    private LocalDateTime resolveQuizDate(Quizz quiz) {
        if (quiz.getUpdatedAt() != null) {
            return quiz.getUpdatedAt();
        }
        if (quiz.getCreatedAt() != null) {
            return quiz.getCreatedAt();
        }
        if (quiz.getStaredAt() != null) {
            return quiz.getStaredAt();
        }
        return LocalDateTime.MIN;
    }

    public boolean checkExistQuizzBySeanceId(String seanceId) {
        return quizzRepository.existsBySeanceId(seanceId);
    }

    public boolean hasUserSubmittedQuizz(String seanceId, String userId) {
        Quizz quizz = quizzRepository.findBySeanceId(seanceId).stream().findFirst().orElse(null);
        if (quizz == null) {
            System.out.println("No quizz found for seanceId: " + seanceId);
            return true;
        }
        Optional<QuizSubmission> submission = quizzAnswerRepository
                .findTopByQuizzIdAndUserIdOrderBySubmittedAtDesc(quizz.getId(), userId);
        if (checkExistQuizzBySeanceId(userId)) {
            return true;
        }
        return submission.isPresent() && submission.get().getSubmittedAt() != null;
    }

    public Quizz getQuizzBySeanceId(String seanceId) {
        Quizz quizz = null;
        List<Quizz> quizzs = new ArrayList<>();
        for (Quizz q : quizzRepository.findAll()) {
            if (q.getSeanceId().equals(seanceId)) {
                quizzs.add(q);
            }
        }
        if (quizzs.size() > 0) {
            quizz = quizzs.get(quizzs.size() - 1);
        }
        if (quizz == null) {
            throw new RuntimeException("No quizz found for seanceId: " + seanceId);
        }
        return quizz;
    }

    public QuizSubmission getUserQuizSubmission(String quizzId, String userId) {
        Optional<QuizSubmission> submission = quizzAnswerRepository
                .findTopByQuizzIdAndUserIdOrderBySubmittedAtDesc(quizzId, userId);
        return submission.orElse(null);
    }

}
