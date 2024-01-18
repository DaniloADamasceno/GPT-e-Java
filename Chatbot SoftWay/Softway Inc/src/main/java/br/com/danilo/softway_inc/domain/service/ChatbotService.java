package br.com.danilo.softway_inc.domain.service;

import br.com.danilo.softway_inc.infrastructure.openAI.ChatCompletionRequestData;
import br.com.danilo.softway_inc.infrastructure.openAI.OpenAIClient;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import io.reactivex.Flowable;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class ChatbotService {

    private OpenAIClient openAIClient;

    //! ---------------------------------------------   CONSTRUCTOR   --------------------------------------------------
    public ChatbotService(OpenAIClient openAIClient) {
        this.openAIClient = openAIClient;
    }

    //! ---------------------------------------------   PUBLIC METHODS   -----------------------------------------------
    @SneakyThrows
    public Flowable<ChatCompletionChunk> answerQuestion(String question) {
        var promptSystem = "Você é um Chatbot que responde perguntas sobre a Softway Inc., " +
                           "uma empresa de tecnologia com foco em esportes. você deve responder somente perguntas relacionadas a Softway Inc. e esportes.";
        var data = new ChatCompletionRequestData(promptSystem, question);
        return openAIClient.sendRequestToChatCompletion(data);
        //var chatCompletionRequestData = new ChatCompletionRequestData(promptSystem, promptUser);
        //var chatCompletionResponse = openAIClient.completeChat(chatCompletionRequestData);
        //var answer = chatCompletionResponse.choices().get(0).text();
        //return answer;
    }
}
