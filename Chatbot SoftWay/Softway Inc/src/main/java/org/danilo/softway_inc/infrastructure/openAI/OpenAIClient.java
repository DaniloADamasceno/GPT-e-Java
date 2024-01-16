package org.danilo.softway_inc.infrastructure.openAI;

import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;

// OpenAI API
@Component
public class OpenAIClient {

    private final String tokenKey;
    private final OpenAiService openAiService;

    //! ---------------------------------------------   Constructor   --------------------------------------------------

    public OpenAIClient(@Value("${app.openai.api.key}") OpenAiService openAiService) {
        this.tokenKey = tokenKey;
        this.openAiService = new OpenAiService(tokenKey, Duration.ofSeconds(60));
    }

    //! ---------------------------------------------   Methods   ------------------------------------------------------
    // --> Enviar Requisição Chat Completion
    public String sendRequestToChatCompletion(ChatCompletionRequest data) {
        var request = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(Arrays.asList(
                        new ChatMessage(
                                ChatMessageRole.SYSTEM.value(),
                                data.promptSystem()),
                        new ChatMessage(
                                ChatMessageRole.USER.value(),
                                data.promptUser())))
                .build();

        var secondsToNextAttempt = 5;
        var attempts = 0;
        while (attempts++ != 5) {
            try {
                return openAiService
                        .createChatCompletion(request)
                        .getChoices().get(0)
                        .getMessage().getContent();
            } catch (OpenAiHttpException ex) {
                var errorCode = ex.statusCode;
                switch (errorCode) {
                    case 401 -> throw new RuntimeException("Erro com a chave da API!", ex);
                    case 429, 500, 503 -> {
                        try {
                            Thread.sleep(1000 * secondsToNextAttempt);
                            secondsToNextAttempt *= 2;
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        return new RuntimeException("Erro ao enviar requisição para o OpenAI API | API Down! Attempts ended without success!")
    }
}
