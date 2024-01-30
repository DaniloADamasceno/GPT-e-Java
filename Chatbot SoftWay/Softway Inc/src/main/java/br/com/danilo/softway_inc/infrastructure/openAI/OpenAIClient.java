package br.com.danilo.softway_inc.infrastructure.openAI;

import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.messages.Message;
import com.theokanning.openai.messages.MessageRequest;
import com.theokanning.openai.runs.RunCreateRequest;
import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.threads.ThreadRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OpenAIClient {

    //! ---------------------------------------------   Attributes   ---------------------------------------------------
    private final String tokenKey;                         // --> Chave da API
    private final String assistantID;                      // --> Chave do Assistente
    private String threadID;                               // --> ID da Thread
    private final OpenAiService openAiService;             // --> Serviço OpenAI
    private final String model = "gpt-3.5-turbo";          // --> Modelo do GPT

    //! ---------------------------------------------   Constructor   --------------------------------------------------
    public OpenAIClient(@Value("${app.openai.api.key}") String tokenKey,
                        @Value("${app.openai.assistant-id}") String assistantID) {
        this.tokenKey = tokenKey;
        this.openAiService = new OpenAiService(tokenKey, Duration.ofSeconds(60));
        this.assistantID = assistantID;
    }

    //! ---------------------------------------------   Methods   ------------------------------------------------------

    /**
     * Método responsável por enviar uma requisição para a API da OpenAI
     * @param dados
     * @return
     */
    public String sendRequestToChatCompletion(ChatCompletionRequestData dados) {
        var messageRequest = MessageRequest                         // --> Cria uma requisição de mensagem (Representa a mensagem enviada pelo usuário)
                .builder()
                .role(ChatMessageRole.USER.value())
                .content(dados.promptUser())
                .build();

        if (this.threadID == null) {                                               // --> Se não houver uma ThreadID criada
            var threadRequest = ThreadRequest                       // --> Cria uma requisição de Thread (Representa a conversa entre o usuário e o assistente)
                    .builder()
                    .messages(Arrays.asList(messageRequest))
                    .build();

            var thread = openAiService.createThread(threadRequest);
            this.threadID = thread.getId();
        } else {
            openAiService.createMessage(this.threadID, messageRequest);             // --> Cria uma mensagem na Thread caso ela não exista
        }

        var runRequest = RunCreateRequest                         // --> Cria uma requisição de Run (Representa uma execução do modelo GPT-3.5-Turbo)
                .builder()
                .assistantId(assistantID)
                .build();                                                          // --> Cria uma Run na API da OpenAI
        var run = openAiService
                .createRun(threadID, runRequest);

        try {
            while (!run.getStatus().equalsIgnoreCase("completed")) {    // --> Enquanto a Run não estiver completa
                Thread.sleep(1000 * 10);                                      // --> Espera 10 segundos
                run = openAiService.retrieveRun(threadID, run.getId());       // --> Atualiza a Run
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        var listMessages = openAiService.listMessages(threadID);                // --> Lista as mensagens da Thread
        var assistantResponse = listMessages
                .getData()                                                          // --> Pega os dados da lista de mensagens
                .stream()                                                           // --> Transforma em Stream
                .sorted(Comparator.comparingInt(Message::getCreatedAt).reversed())  // --> Ordena as mensagens pela data de criação de forma reversa
                .findFirst().get().getContent().get(0).getText().getValue();        // --> Pega a primeira mensagem da lista

        return assistantResponse;
    }

    //-> Carregar Histórico de Conversa
    public List<String> uploadMessageHistory() {
        var messages = new ArrayList<String>();

        if (this.threadID != null) {
            messages.addAll(
                    openAiService
                            .listMessages(this.threadID)
                            .getData()                         // --> Pega os dados da lista de mensagens
                            .stream()                          // --> Transforma em Stream
                            .sorted(Comparator.comparingInt(Message::getCreatedAt)) // --> Ordena as mensagens pela data de criação
                            .map(mapMessage -> mapMessage.getContent().get(0).getText().getValue()) // --> Pega a mensagem e adiciona na lista
                            .collect(Collectors.toList()) // --> Transforma em lista
            );
        }

        return messages;
    }
    // -> Limpar Thread
    public void clearThread() {
        if(this.threadID != null) {
            openAiService.deleteThread(this.threadID); // --> Deleta a Thread
            this.threadID = null;
        }
    }
}


