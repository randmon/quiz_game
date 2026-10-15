package api;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public final class ApiModels {
    private ApiModels() {
    }

    public record GameStartRequest(String quiz) {
    }

    public record AnswerRequest(@NotBlank String answer) {
    }

    public record GameCreatedResponse(String gameId, String title, QuestionResponse question) {
    }

    public record QuestionResponse(String type, String prompt, List<String> options) {
    }

    public record AnswerResponse(
            boolean correct,
            String status,
            String correctAnswer,
            QuestionResponse nextQuestion,
            GameResultResponse result) {
    }

    public record GameResultResponse(String gameId, int score, int total, boolean completed) {
    }

    public record GameStatusResponse(
            String gameId,
            String title,
            int score,
            int total,
            boolean finished,
            QuestionResponse currentQuestion) {
    }

    public record ErrorResponse(String error) {
    }
}
