package jkammellander.questions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class QuestionHandler {

    // Creating a new ArrayList and HashSet.
    private ArrayList<Question> questions = new ArrayList<>();
    private HashSet<Integer> questionIndex = new HashSet<>();

    // Reading the questions from the CSV file and adding them to the ArrayList.
    public QuestionHandler() throws IOException {
        BufferedReader reader = null;
        String line ="";

        try {
            // Reading the questions from the CSV file and adding them to the ArrayList.
            String file = "src/jkammellander/questions/Questions.csv";
            reader = new BufferedReader(new FileReader(file));

            // Reading the questions from the CSV file and adding them to the ArrayList.
            for (int i = 0; (line = reader.readLine()) != null; i++) {
                String[] row = line.split(",");
                Question question = new Question(row[0],row[1]);
                this.questions.add(question);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Checking if the reader is null and if it is not it closes the reader.
            assert reader != null;
            reader.close();
        }

        // Adding 3 random numbers to the HashSet.
        for (int i = 0; i != 3;) {
            this.questionIndex.add((int)(Math.random()*(this.questions.size()-1))+1);
            if (this.questionIndex.size() == i+1) {
                i++;
            }
        }
    }

    /**
     * This function returns an ArrayList of Question objects
     *
     * @return An ArrayList of Question objects.
     */
    public ArrayList<Question> getQuestions() {
        return questions;
    }

    /**
     * This function returns the questionIndex HashSet
     *
     * @return A HashSet of Integers
     */
    public HashSet<Integer> getQuestionIndex() {
        return questionIndex;
    }
}
