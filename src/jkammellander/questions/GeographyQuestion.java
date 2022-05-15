package jkammellander.questions;

import java.util.HashMap;
import java.util.Map;

public class GeographyQuestion {
    private Map<String, String> questions = new HashMap<>();
    private double question;

    public GeographyQuestion() {
        this.questions.put("Vienna","What is the capital of Austria🏙️?");
        this.questions.put("Poland","In which country is Breslau🏞️?");
        question = Math.random()*((questions.size()-1) +1)+0;
    }

    public String getQuestion() {
        int iterator = 0;
        for (Map.Entry<String,String> entry : questions.entrySet()) {
            if (iterator == question) {
                return entry.getValue();
            }
            iterator++;
        }
        return null;
    }

    public String getAnwser() {
        int iterator = 0;
        for (Map.Entry<String,String> entry : questions.entrySet()) {
            if (iterator == question) {
                return entry.getKey();
            }
            iterator++;
        }
        return null;
    }
}
