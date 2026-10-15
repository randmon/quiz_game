package api;

import domain.Quiz;
import domain.questions.Question;
import exception.QuizGameException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static api.ApiModels.*;

@Service
public class GameService {
    private static final String DEFAULT_QUIZ = "javaQuestions.txt";

    private final Map<String, GameSession> games = new ConcurrentHashMap<>();
    private final AtomicLong nextGameId = new AtomicLong(1);

    public GameCreatedResponse startGame(GameStartRequest request) {
        String quizResource = request == null || request.quiz() == null || request.quiz().isBlank()
                ? DEFAULT_QUIZ
                : request.quiz().trim();
        String gameId = String.valueOf(nextGameId.getAndIncrement());
        GameSession session = new GameSession(gameId, new Quiz(quizResource));
        games.put(gameId, session);
        return new GameCreatedResponse(gameId, session.title(), session.currentQuestion());
    }

    public QuestionResponse getCurrentQuestion(String gameId) {
        return getSession(gameId).currentQuestion();
    }

    public AnswerResponse submitAnswer(String gameId, String answer) {
        return getSession(gameId).submitAnswer(answer);
    }

    public GameResultResponse getResult(String gameId) {
        return getSession(gameId).result();
    }

    public GameStatusResponse getStatus(String gameId) {
        return getSession(gameId).status();
    }

    private GameSession getSession(String gameId) {
        GameSession session = games.get(gameId);
        if (session == null) {
            throw new QuizGameException("Unknown game id: " + gameId);
        }
        return session;
    }

    private static final class GameSession {
        private final String gameId;
        private final Quiz quiz;
        private Question currentQuestion;
        private List<String> options = List.of();
        private boolean finished;

        private GameSession(String gameId, Quiz quiz) {
            this.gameId = gameId;
            this.quiz = quiz;
            advance();
        }

        private String title() {
            return quiz.getTitle();
        }

        private synchronized QuestionResponse currentQuestion() {
            return toQuestionResponse();
        }

        private synchronized AnswerResponse submitAnswer(String answer) {
            if (finished) {
                throw new QuizGameException("Game is already finished: " + gameId);
            }

            String correctAnswer = quiz.getCorrectAnswer();
            boolean correct = quiz.answer(answer);
            if (quiz.hasQuestionsLeft()) {
                advance();
                return new AnswerResponse(
                        correct,
                        "next_question",
                        correct ? null : correctAnswer,
                        toQuestionResponse(),
                        null);
            }

            finished = true;
            return new AnswerResponse(
                    correct,
                    "completed",
                    correct ? null : correctAnswer,
                    null,
                    result());
        }

        private synchronized GameResultResponse result() {
            return new GameResultResponse(gameId, quiz.getPoints(), quiz.getTotalAmount(), finished);
        }

        private synchronized GameStatusResponse status() {
            return new GameStatusResponse(gameId, quiz.getTitle(), quiz.getPoints(),
                    quiz.getTotalAmount(), finished, finished ? null : toQuestionResponse());
        }

        private void advance() {
            currentQuestion = quiz.getNextQuestion();
            options = currentQuestion.getType().equals("m")
                    ? new ArrayList<>(currentQuestion.getAnswerList())
                    : List.of();
        }

        private QuestionResponse toQuestionResponse() {
            return new QuestionResponse(
                    currentQuestion.getType(),
                    currentQuestion.getPrompt(),
                    options);
        }
    }
}
