package database;

import exception.QuizFileException;
import domain.Quiz;
import domain.questions.MultipleChoice;
import domain.questions.Question;
import domain.questions.WrittenAnswer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private final String resourcePath;

    public FileManager(String resourcePath) {
        if (resourcePath == null || resourcePath.isBlank()) {
            throw new IllegalArgumentException("Quiz resource path cannot be empty");
        }
        this.resourcePath = resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath;
    }

    public List<Question> loadQuestions(Quiz quiz) {
        InputStream stream = FileManager.class.getClassLoader().getResourceAsStream(resourcePath);
        if (stream == null) {
            throw new QuizFileException("Quiz resource '" + resourcePath + "' was not found on the classpath");
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            ParsedQuiz parsedQuiz = parse(resourcePath, reader);
            quiz.setTitle(parsedQuiz.title());
            return parsedQuiz.questions();
        } catch (IOException e) {
            throw new QuizFileException("Could not read quiz resource '" + resourcePath + "': " + e.getMessage());
        }
    }

    static ParsedQuiz parse(String resourcePath, Reader source) throws IOException {
        BufferedReader reader = source instanceof BufferedReader bufferedReader
                ? bufferedReader
                : new BufferedReader(source);
        List<Question> questions = new ArrayList<>();
        String title = null;
        String line;
        int lineNumber = 0;

        while ((line = reader.readLine()) != null) {
            lineNumber++;
            if (line.isBlank() || line.startsWith("//")) {
                continue;
            }

            if (title == null) {
                title = line;
                continue;
            }

            String[] fields = line.split("\\|", -1);
            if (fields[0].isBlank()) {
                throw invalidLine(resourcePath, lineNumber, "question type is missing");
            }

            switch (fields[0]) {
                case "w" -> {
                    if (fields.length != 3) {
                        throw invalidLine(resourcePath, lineNumber, "written questions must have exactly 3 fields");
                    }
                    requireField(resourcePath, fields[1], lineNumber, "question");
                    requireField(resourcePath, fields[2], lineNumber, "correct answer");
                    questions.add(new WrittenAnswer(fields[1], fields[2]));
                }
                case "m" -> {
                    if (fields.length < 4) {
                        throw invalidLine(resourcePath, lineNumber,
                                "multiple-choice questions need a question, correct answer, and at least one incorrect answer");
                    }
                    requireField(resourcePath, fields[1], lineNumber, "question");
                    requireField(resourcePath, fields[2], lineNumber, "correct answer");
                    MultipleChoice question = new MultipleChoice(fields[1], fields[2]);
                    question.addPossibleAnswer(fields[2]);
                    for (int i = 3; i < fields.length; i++) {
                        requireField(resourcePath, fields[i], lineNumber, "answer option");
                        question.addPossibleAnswer(fields[i]);
                    }
                    questions.add(question);
                }
                default -> throw invalidLine(resourcePath, lineNumber,
                        "invalid question type '" + fields[0] + "' (expected 'w' or 'm')");
            }
        }

        if (title == null) {
            throw new QuizFileException("Quiz resource '" + resourcePath + "' is empty; expected a title");
        }
        if (questions.isEmpty()) {
            throw new QuizFileException("Quiz resource '" + resourcePath + "' contains a title but no questions");
        }

        return new ParsedQuiz(title, questions);
    }

    private static QuizFileException invalidLine(String resourcePath, int lineNumber, String message) {
        return new QuizFileException(
                String.format("Invalid quiz resource '%s' at line %d: %s", resourcePath, lineNumber, message)
        );
    }

    private static void requireField(String resourcePath, String value, int lineNumber, String fieldName) {
        if (value.isBlank()) {
            throw invalidLine(resourcePath, lineNumber, fieldName + " cannot be empty");
        }
    }

    record ParsedQuiz(String title, List<Question> questions) {
    }
}
