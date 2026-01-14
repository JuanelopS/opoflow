package dev.jugapi.opoflow.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question {

    private String prompt;
    private List<Option> options;
    private OppositionTopic topic;

    public Question(String prompt, List<Option> options, OppositionTopic topic) {
        this.prompt = prompt;
        this.options = new ArrayList<>(options);  // this makes the list mutable so that the order of the responses can be randomized.
        this.topic = topic;
    }

    public String getPrompt() {
        return prompt;
    }

    public List<Option> getOptions() {
        return options;
    }

    public OppositionTopic getTopic() {
        return topic;
    }

    public void shuffleOptions() {
        Collections.shuffle(this.options);
    }
}
