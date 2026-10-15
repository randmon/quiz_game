package api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static api.ApiModels.*;

@RestController
@RequestMapping("/api/v1/games")
public class GameController {
    private final GameService games;

    public GameController(GameService games) {
        this.games = games;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GameCreatedResponse startGame(@RequestBody(required = false) GameStartRequest request) {
        return games.startGame(request);
    }

    @GetMapping("/{gameId}/question")
    public QuestionResponse getCurrentQuestion(@PathVariable String gameId) {
        return games.getCurrentQuestion(gameId);
    }

    @PostMapping("/{gameId}/answer")
    public AnswerResponse submitAnswer(
            @PathVariable String gameId,
            @Valid @RequestBody AnswerRequest request) {
        return games.submitAnswer(gameId, request.answer());
    }

    @GetMapping("/{gameId}/result")
    public GameResultResponse getResult(@PathVariable String gameId) {
        return games.getResult(gameId);
    }

    @GetMapping("/{gameId}")
    public GameStatusResponse getStatus(@PathVariable String gameId) {
        return games.getStatus(gameId);
    }
}
