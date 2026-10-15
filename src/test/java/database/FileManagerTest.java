package database;

import exception.QuizFileException;
import domain.Quiz;
import domain.questions.MultipleChoice;
import domain.questions.WrittenAnswer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileManagerTest {
    @Test
    void loadsPackagedSampleQuizFromClasspath() {
        Quiz quiz = new Quiz("javaQuestions.txt");

        assertEquals("Java Quiz", quiz.getTitle());
        assertEquals(11, quiz.getTotalAmount());
    }

    @Test
    void preservesWrittenAndMultipleChoiceQuestionFormats() throws IOException {
        FileManager.ParsedQuiz quiz = parse("""
                // Comment lines remain supported.
                Test quiz

                m|Prompt?|Correct|Incorrect
                w|Written prompt?|Answer
                """);

        assertEquals("Test quiz", quiz.title());
        assertEquals(2, quiz.questions().size());
        MultipleChoice multipleChoice = assertInstanceOf(MultipleChoice.class, quiz.questions().getFirst());
        assertEquals("Prompt?", multipleChoice.getPrompt());
        assertTrue(multipleChoice.getAnswerList().containsAll(List.of("Correct", "Incorrect")));

        WrittenAnswer writtenAnswer = assertInstanceOf(WrittenAnswer.class, quiz.questions().get(1));
        assertEquals("Written prompt?", writtenAnswer.getPrompt());
    }

    @Test
    void reportsMissingClasspathResource() {
        QuizFileException error = assertThrows(
                QuizFileException.class,
                () -> new Quiz("quizzes/does-not-exist.txt"));

        assertTrue(error.getMessage().contains("was not found on the classpath"));
    }

    @Test
    void reportsQuizWithoutTitle() {
        QuizFileException error = assertThrows(
                QuizFileException.class,
                () -> parse("// comments and blank lines are ignored\n\n"));

        assertTrue(error.getMessage().contains("expected a title"));
    }

    @Test
    void reportsQuizWithoutQuestions() {
        QuizFileException error = assertThrows(
                QuizFileException.class,
                () -> parse("Empty quiz\n"));

        assertTrue(error.getMessage().contains("no questions"));
    }

    @Test
    void reportsMissingFieldsWithLineNumber() {
        QuizFileException error = assertThrows(
                QuizFileException.class,
                () -> parse("Missing field\nw|Prompt|\n"));

        assertTrue(error.getMessage().contains("line 2"));
        assertTrue(error.getMessage().contains("correct answer cannot be empty"));
    }

    @Test
    void reportsMalformedQuestionWithLineNumber() {
        QuizFileException error = assertThrows(
                QuizFileException.class,
                () -> parse("Malformed question\nw|Prompt|Answer|Unexpected\n"));

        assertTrue(error.getMessage().contains("line 2"));
        assertTrue(error.getMessage().contains("exactly 3 fields"));
    }

    @Test
    void reportsInvalidQuestionTypeWithLineNumber() {
        QuizFileException error = assertThrows(
                QuizFileException.class,
                () -> parse("Invalid type\nx|Prompt|Answer\n"));

        assertTrue(error.getMessage().contains("line 2"));
        assertTrue(error.getMessage().contains("invalid question type 'x'"));
    }

    private static FileManager.ParsedQuiz parse(String contents) throws IOException {
        return FileManager.parse("test quiz", new StringReader(contents));
    }
}
