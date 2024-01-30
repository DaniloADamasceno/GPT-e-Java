package br.com.danilo.softway_inc.domain.service;

import br.com.danilo.softway_inc.infrastructure.openAI.ChatCompletionRequestData;
import br.com.danilo.softway_inc.infrastructure.openAI.OpenAIClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatbotService {

    private OpenAIClient openAIClient;

    //! ---------------------------------------------   CONSTRUCTOR   --------------------------------------------------
    public ChatbotService(OpenAIClient openAIClient) {
        this.openAIClient = openAIClient;
    }

    //! ---------------------------------------------   PUBLIC METHODS   -----------------------------------------------
    public String answerQuestion(String question) {
        var promptSystem = "Você é um Chatbot que responde perguntas sobre a Softway Inc., " +
                           "uma empresa de tecnologia com foco em esportes. você deve responder somente perguntas relacionadas a Softway Inc. e esportes.";
        var data = new ChatCompletionRequestData(promptSystem, question);
        return openAIClient.sendRequestToChatCompletion(data);
    }

    // -> Carregar Histórico
    public List<String> loadHistory() {
        return openAIClient.uploadMessageHistory();
    }

    // -> Limpar Conversa
    public void clearConversation() {
        openAIClient.clearThread();
    }
}
