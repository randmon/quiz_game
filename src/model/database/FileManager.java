package model.database;

import model.DomainException;
import model.domain.questions.MultipleChoice;
import model.domain.questions.Question;

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
                String[] params = line.split("\\|");
                ArrayList<String> l = new ArrayList<>(Arrays.asList(params));
                result.add(l);
            }
            System.out.println("Successfully loaded " + result.size() + " questions from " + path);
            scanner.close();
        } catch (FileNotFoundException fnf) {
            throw new DomainException(path + " doesn't exist");
        } catch (Exception e) {
            throw new DomainException("Error found reading " + path);
        }
        return result;
    }

    public List<Question> loadQuestions() {
        List<Question> questions = new ArrayList<>();
        List<List<String>> lines = load();
        for (List<String> l : lines) {
            switch (l.get(0)) {
                case "m" :
                    MultipleChoice q = new MultipleChoice(l.get(1), l.get(2));
                    for (String s : l.subList(3, l.size())) {
                        q.addPossibleAnswer(s);
                    }
                    questions.add(q);
                    break;
            }
        }
        return questions;
    }
}
