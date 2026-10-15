package api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        classes = application.QuizApiApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GameControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate http;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void rootReturnsHealthStatus() throws Exception {
        ResponseEntity<String> response = http.getForEntity(url("/"), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("UP", objectMapper.readTree(response.getBody()).get("status").asText());
    }

    @Test
    void startsGameWithoutExposingCorrectAnswer() throws Exception {
        ResponseEntity<String> response = post("/api/v1/games", Map.of("quiz", "test-quiz.txt"));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        JsonNode body = objectMapper.readTree(response.getBody());
        assertNotNull(body.get("gameId"));
        assertEquals("What is 2 + 2?", body.at("/question/prompt").asText());
        assertEquals("m", body.at("/question/type").asText());
        assertTrue(body.at("/question/options").toString().contains("\"4\""));
        assertFalse(response.getBody().contains("correctAnswer"));
    }

    @Test
    void submitsWrittenAndMultipleChoiceAnswersAndReturnsFinalResult() throws Exception {
        String gameId = startGame();

        JsonNode writtenQuestion = objectMapper.readTree(
                post("/api/v1/games/" + gameId + "/answer", Map.of("answer", "4")).getBody());
        assertTrue(writtenQuestion.get("correct").asBoolean());
        assertEquals("What is the capital of France?", writtenQuestion.at("/nextQuestion/prompt").asText());

        JsonNode multipleChoiceQuestion = objectMapper.readTree(
                post("/api/v1/games/" + gameId + "/answer", Map.of("answer", "Paris")).getBody());
        assertTrue(multipleChoiceQuestion.get("correct").asBoolean());
        assertEquals("m", multipleChoiceQuestion.at("/nextQuestion/type").asText());

        JsonNode result = objectMapper.readTree(
                post("/api/v1/games/" + gameId + "/answer", Map.of("answer", "Mars")).getBody());
        assertEquals("completed", result.get("status").asText());
        assertTrue(result.at("/result/completed").asBoolean());
        assertEquals(3, result.at("/result/score").asInt());
    }

    @Test
    void retrievesQuestionAndResultAndScoresAnswersOnTheServer() throws Exception {
        String gameId = startGame();

        ResponseEntity<String> currentQuestion =
                http.getForEntity(url("/api/v1/games/" + gameId + "/question"), String.class);
        assertEquals(HttpStatus.OK, currentQuestion.getStatusCode());
        assertEquals("What is 2 + 2?", objectMapper.readTree(currentQuestion.getBody()).get("prompt").asText());

        JsonNode wrongAnswer = objectMapper.readTree(
                post("/api/v1/games/" + gameId + "/answer", Map.of("answer", "not 4")).getBody());
        assertFalse(wrongAnswer.get("correct").asBoolean());
        assertEquals("4", wrongAnswer.get("correctAnswer").asText());
        post("/api/v1/games/" + gameId + "/answer", Map.of("answer", "wrong answer"));
        post("/api/v1/games/" + gameId + "/answer", Map.of("answer", "Mars"));

        ResponseEntity<String> result =
                http.getForEntity(url("/api/v1/games/" + gameId + "/result"), String.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        JsonNode resultBody = objectMapper.readTree(result.getBody());
        assertTrue(resultBody.get("completed").asBoolean());
        assertEquals(1, resultBody.get("score").asInt());
    }

    @Test
    void returnsErrorsForUnknownGamesInvalidAnswersAndFinishedGames() throws Exception {
        ResponseEntity<String> unknown = http.getForEntity(url("/api/v1/games/missing/question"), String.class);
        assertEquals(HttpStatus.NOT_FOUND, unknown.getStatusCode());

        String gameId = startGame();
        ResponseEntity<String> invalid = post("/api/v1/games/" + gameId + "/answer", Map.of());
        assertEquals(HttpStatus.BAD_REQUEST, invalid.getStatusCode());

        post("/api/v1/games/" + gameId + "/answer", Map.of("answer", "4"));
        post("/api/v1/games/" + gameId + "/answer", Map.of("answer", "Paris"));
        post("/api/v1/games/" + gameId + "/answer", Map.of("answer", "Mars"));
        ResponseEntity<String> finished = post("/api/v1/games/" + gameId + "/answer", Map.of("answer", "Mars"));
        assertEquals(HttpStatus.BAD_REQUEST, finished.getStatusCode());
    }

    private String startGame() throws Exception {
        ResponseEntity<String> response = post("/api/v1/games", Map.of("quiz", "test-quiz.txt"));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        return objectMapper.readTree(response.getBody()).get("gameId").asText();
    }

    private ResponseEntity<String> post(String path, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return http.exchange(url(path), HttpMethod.POST, new HttpEntity<>(body, headers), String.class);
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }
}
