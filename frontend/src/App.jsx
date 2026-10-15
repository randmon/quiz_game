import { useEffect, useState } from "react";

async function request(path, options) {
    const response = await fetch(path, {
        headers: {"Content-Type": "application/json"},
        ...options,
    });
    const body = await response.json();
    if (!response.ok) {
        throw new Error(body.error || "The request could not be completed.");
    }
    return body;
}

function App() {
    const [game, setGame] = useState(null);
    const [question, setQuestion] = useState(null);
    const [answerText, setAnswerText] = useState("");
    const [selectedOption, setSelectedOption] = useState(null);
    const [answerResult, setAnswerResult] = useState(null);
    const [phase, setPhase] = useState("loading");
    const [error, setError] = useState("");

    useEffect(() => {
        let active = true;
        const quiz = new URLSearchParams(window.location.search).get("quiz");

        request("/api/v1/games", {
            method: "POST",
            body: JSON.stringify(quiz ? {quiz} : {}),
        })
            .then((createdGame) => {
                if (!active) return;
                setGame(createdGame);
                setQuestion(createdGame.question);
                document.title = createdGame.title || "Quiz game";
                setPhase("question");
            })
            .catch((requestError) => {
                if (!active) return;
                setError(requestError.message);
                setPhase("error");
            });

        return () => {
            active = false;
        };
    }, []);

    async function submitAnswer(answer, option = null) {
        if (phase !== "question" || !answer.trim()) return;

        setError("");
        setSelectedOption(option);
        setPhase("submitting");
        try {
            const result = await request(`/api/v1/games/${game.gameId}/answer`, {
                method: "POST",
                body: JSON.stringify({answer}),
            });
            setAnswerResult(result);
            setPhase("answered");
        } catch (requestError) {
            setSelectedOption(null);
            setError(requestError.message);
            setPhase("question");
        }
    }

    function continueQuiz() {
        if (answerResult?.status === "completed") {
            setPhase("gameOver");
            return;
        }
        if (!answerResult?.nextQuestion) return;
        setQuestion(answerResult.nextQuestion);
        setAnswerResult(null);
        setAnswerText("");
        setSelectedOption(null);
        setPhase("question");
    }

    if (phase === "loading") {
        return <main className="quiz-shell vb"><p className="status-message">Loading quiz…</p></main>;
    }

    if (phase === "error") {
        return (
            <main className="quiz-shell wrongVB">
                <section className="message-panel">
                    <h1 className="prompt">Unable to start quiz</h1>
                    <p className="correctLabel">{error}</p>
                    <p className="correctLabel">Check that the quiz API is running, then reload this page.</p>
                </section>
            </main>
        );
    }

    if (phase === "gameOver") {
        return (
            <main className="quiz-shell gameoverVB">
                <section className="message-panel">
                    <h1 className="prompt">Game over</h1>
                    <p className="prompt">Points: {answerResult.result.score}/{answerResult.result.total}</p>
                    <button className="continueButton" onClick={() => window.location.reload()}>
                        Play again
                    </button>
                </section>
            </main>
        );
    }

    const answered = phase === "answered";
    const questionPanelClass = answered
        ? answerResult.correct ? "correctVB" : "wrongVB"
        : "vb";

    return (
        <main className={`quiz-shell ${questionPanelClass}`}>
            <section className="question-panel">
                <h1 className="prompt">{question.prompt}</h1>

                {question.type === "m" ? (
                    <div className="answers">
                        {question.options.map((option) => {
                            const isCorrect = answered
                                && (answerResult.correct ? option === selectedOption : option === answerResult.correctAnswer);
                            const isSelectedWrong = answered && !answerResult.correct && option === selectedOption;
                            return (
                                <button
                                    className={`answer-button${isCorrect ? " rightAnswer" : ""}${isSelectedWrong ? " selectedWrong" : ""}`}
                                    disabled={phase !== "question"}
                                    key={option}
                                    onClick={() => submitAnswer(option, option)}
                                >
                                    {option}
                                </button>
                            );
                        })}
                    </div>
                ) : (
                    <form
                        className="written-answer"
                        onSubmit={(event) => {
                            event.preventDefault();
                            submitAnswer(answerText);
                        }}
                    >
                        <input
                            autoComplete="off"
                            disabled={phase !== "question"}
                            onChange={(event) => setAnswerText(event.target.value)}
                            value={answerText}
                        />
                        <button disabled={phase !== "question" || !answerText.trim()} type="submit">
                            Submit
                        </button>
                        {answered && !answerResult.correct && (
                            <p className="correctLabel">Correct answer: {answerResult.correctAnswer}</p>
                        )}
                    </form>
                )}
            </section>

            {error && <p className="inline-error" role="alert">{error}</p>}
            {answered && (
                <button className="continueButton" onClick={continueQuiz}>
                    Continue
                </button>
            )}
            {phase === "submitting" && <p className="status-message">Checking answer…</p>}
        </main>
    );
}

export default App;
