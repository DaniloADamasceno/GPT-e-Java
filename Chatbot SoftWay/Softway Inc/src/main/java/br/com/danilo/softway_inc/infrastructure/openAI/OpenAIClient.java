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
import java.util.Arrays;
import java.util.Comparator;

@Component
public class OpenAIClient {

    //! ---------------------------------------------   Attributes   ---------------------------------------------------
    private final String tokenKey;                                              // --> Chave da API
    private final String assistantID;                                           // --> Chave do Assistente
    private String threadID;                                                    // --> ID da Thread
    private final OpenAiService openAiService;                                  // --> Serviço OpenAI
    private final String model = "gpt-3.5-turbo";                               // --> Modelo do GPT

    //! ---------------------------------------------   Constructor   --------------------------------------------------
    public OpenAIClient(@Value("${app.openai.api.key}") String tokenKey,
                        @Value("${app.openai.api.assistentID}") String assistantID) {
        this.tokenKey = tokenKey;
        this.openAiService = new OpenAiService(tokenKey, Duration.ofSeconds(60));
        this.assistantID = assistantID;
    }

    //! ---------------------------------------------   Methods   ------------------------------------------------------

    // --> Enviar Requisição Chat Completion
    public String sendRequestToChatCompletion(ChatCompletionRequestData data) {

        var messageRequest = MessageRequest                       // --> Cria uma requisição de mensagem (Representa a mensagem enviada pelo usuário)
                .builder()
                .role(ChatMessageRole.USER.value())
                .content(data.promptUser())
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

        while (!run.getStatus().equals("complete")) {                              // --> Enquanto a Run não estiver completa
            try {
                Thread.sleep(1000 * 10);                                     // --> Espera 10 segundos
                run = openAiService.retrieveRun(threadID, run.getId());      // --> Atualiza a Run
            } catch (InterruptedException errorRunService) {
                errorRunService.printStackTrace();
            }
        }

        var listMessages = openAiService
                .listMessages(threadID);                                           // --> Lista as mensagens da Thread

        var assistantResponse = listMessages
            .getData()                                                             // --> Pega os dados da lista de mensagens
            .stream()                                                              // --> Transforma em Stream
            .sorted(Comparator.comparingInt(Message::getCreatedAt).reversed())   // --> Ordena as mensagens pela data de criação de forma reversa
            .findFirst().get().getContent().get(0).getText().getValue();        // --> Pega a primeira mensagem da lista

return assistantResponse;
    }

}

//        var request = ChatCompletionRequest
//                .builder()
//                .model(model)
//                .messages(Arrays.asList(
//                        new ChatMessage(
//                                ChatMessageRole.SYSTEM.value(),
//                                data.promptSystem()),
//                        new ChatMessage(
//                                ChatMessageRole.USER.value(),
//                                data.promptUser())))
//                .stream(true)                                            // --> Para que a API retorne uma resposta Token a Token
//                .build();
//
//        var secondsToNextAttempt = 5;                                           // --> Segundos para a próxima tentativa
//        var attempts = 0;                                                       // --> Tentativas
//        while (attempts++ != 5) {
//            try {
//                return openAiService.streamChatCompletion(request);             // --> Envia a requisição para a API da OpenAI
//            } catch (OpenAiHttpException errorOpenAI) {
//                var errorCode = errorOpenAI.statusCode;                         // --> Código do erro retornado pela API da OpenAI
//                switch (errorCode) {
//                    case 401, 403 -> throw new RuntimeException("Erro " + errorCode + ": Chave de API inválida! " +
//                                                                "| Error " + errorCode + ": Invalid API key!", errorOpenAI);
//                    case 500, 502, 503, 504 -> {
//                        System.out.println("Erro " + errorCode + ": Erro interno do servidor! | Error " + errorCode + ": Internal server error!");
//                        Thread.sleep(60000);                                // --> Espera 1 minuto para tentar novamente
//                        attempts *= 2;                                            // --> Dobra o número de tentativas
//                    }
//                    case 429 -> {
//                        System.out.println("Erro " + errorCode + " :Limite de requisições excedido! Aguarde 60 segundos até a próxima tentativa" +
//                                           "| Error " + errorCode + ": Request limit exceeded! Wait 60 seconds for the next attempt");
//                        Thread.sleep(60000);                                // --> Espera 1 minuto para tentar novamente
//                        attempts *= 2;                                            // --> Dobra o número de tentativas
//
//                    }
//                }
//            }
//        }
//        throw new RuntimeException("API da OpenAI indisponível!, Tentativas Excedidas | OpenAI API unavailable!, Exceeded Attempts");
//    }


