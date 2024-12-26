package com.example.oner.service;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class SlackService {

    @Value("${slack.token}")
    private String slackToken;

    @Value("${slack.channel}")
    private String slackChannel;

    public void sendMessage(String message) throws IOException, SlackApiException {
        Slack slack = Slack.getInstance();
        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel(slackChannel)
                .text(message)
                .build();

        ChatPostMessageResponse response = slack.methods(slackToken).chatPostMessage(request);

        if (!response.isOk()) {
            throw new RuntimeException("Error sending message to Slack: " + response.getError());
        }
    }
}
