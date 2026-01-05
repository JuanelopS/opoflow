package dev.jugapi.opoflow.model;

import java.util.List;

public class Question {

    private String prompt;
    private List<Option> options;
    private OppositionTopic topic;

    public Question(String prompt, List<Option> options, OppositionTopic topic) {
        this.prompt = prompt;
        this.options = options;
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

}
