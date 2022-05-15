package jkammellander.questions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class QuestionHandler {

    private ArrayList<Question> questions = new ArrayList<>();
    private HashSet<Integer> questionIndex = new HashSet<>();

    public QuestionHandler() throws IOException {
        BufferedReader reader = null;
        String line ="";

        try {
            String file = "src\\jkammellander\\questions\\Questions.csv";
            reader = new BufferedReader(new FileReader(file));
            for (int i = 0; (line = reader.readLine()) != null; i++) {
                String[] row = line.split(",");
                Question question = new Question(row[0],row[1]);
                this.questions.add(question);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert reader != null;
            reader.close();
        }

        for (int i = 0; i != 3;) {
            this.questionIndex.add((int)(Math.random()*(this.questions.size()-1))+1);
            if (this.questionIndex.size() == i+1) {
                i++;
            }
        }
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public HashSet<Integer> getQuestionIndex() {
        return questionIndex;
    }
}
