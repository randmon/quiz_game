package model.database;

import model.DomainException;
import model.domain.Quiz;
import model.domain.questions.MultipleChoice;
import model.domain.questions.Question;
import model.domain.questions.WrittenAnswer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FileManager {
    private String path;

    public FileManager(String path) {
        this.path = path;
    }

    private List<List<String>> load() {
        List<List<String>> result = new ArrayList<>();
        File file = new File(path);

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.startsWith("//")) {
                    String[] params = line.split("\\|");
                    ArrayList<String> l = new ArrayList<>(Arrays.asList(params));
                    result.add(l);
                }
            }
            scanner.close();
        } catch (FileNotFoundException fnf) {
            throw new DomainException(path + " doesn't exist");
        } catch (Exception e) {
            throw new DomainException("Error found reading " + path);
        }
        return result;
    }

    public List<Question> loadQuestions(Quiz quiz) {
        List<Question> questions = new ArrayList<>();
        List<List<String>> lines = load();
        quiz.setTitle(lines.get(0).get(0));
        lines.remove(0);
        for (List<String> l : lines) {
            switch (l.get(0)) {
                case "m" :
                    MultipleChoice q = new MultipleChoice(l.get(1), l.get(2));
                    q.addPossibleAnswer(l.get(2)); //First answer in document is always correct answer
                    for (String s : l.subList(3, l.size())) {
                        q.addPossibleAnswer(s);
                    }
                    questions.add(q);
                    break;
                case "w":
                    WrittenAnswer w = new WrittenAnswer(l.get(1), l.get(2));
                    questions.add(w);
            }
        }
        return questions;
    }
}
