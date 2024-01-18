package br.com.danilo.softway_inc.infrastructure.openAI;

import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import io.reactivex.Flowable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;

@Component
public class OpenAIClient {

    //! ---------------------------------------------   Attributes   ---------------------------------------------------
    private final String tokenKey;                                              // --> Chave da API
    private final OpenAiService openAiService;                                  // --> Serviço OpenAI
    private final String model = "gpt-3.5-turbo";                               // --> Modelo do GPT

    //! ---------------------------------------------   Constructor   --------------------------------------------------
    public OpenAIClient(@Value("${app.openai.api.key}") String tokenKey) {
        this.tokenKey = tokenKey;
        this.openAiService = new OpenAiService(tokenKey, Duration.ofSeconds(60));
    }

    //! ---------------------------------------------   Methods   ------------------------------------------------------

    // --> Enviar Requisição Chat Completion
    public Flowable<ChatCompletionChunk> sendRequestToChatCompletion(ChatCompletionRequestData data) throws InterruptedException {
        var request = ChatCompletionRequest
                .builder()
                .model(model)
                .messages(Arrays.asList(
                        new ChatMessage(
                                ChatMessageRole.SYSTEM.value(),
                                data.promptSystem()),
                        new ChatMessage(
                                ChatMessageRole.USER.value(),
                                data.promptUser())))
                .stream(true)                                            // --> Para que a API retorne uma resposta Token a Token
                .build();

        var secondsToNextAttempt = 5;                                           // --> Segundos para a próxima tentativa
        var attempts = 0;                                                       // --> Tentativas
        while (attempts++ != 5) {
            try {
                return openAiService.streamChatCompletion(request);             // --> Envia a requisição para a API da OpenAI
            } catch (OpenAiHttpException errorOpenAI) {
                var errorCode = errorOpenAI.statusCode;                     // --> Código do erro retornado pela API da OpenAI
                switch (errorCode) {
                    case 401, 403 -> throw new RuntimeException("Erro " + errorCode + ": Chave de API inválida! " +
                                                                "| Error " + errorCode + ": Invalid API key!", errorOpenAI);
                    case 500, 502, 503, 504 -> {
                        System.out.println("Erro " + errorCode + ": Erro interno do servidor! | Error " + errorCode + ": Internal server error!");
                        Thread.sleep(60000);                                // --> Espera 1 minuto para tentar novamente
                        attempts *= 2;                                            // --> Dobra o número de tentativas
                    }
                    case 429 -> {
                        System.out.println("Erro " + errorCode + " :Limite de requisições excedido! Aguarde 60 segundos até a próxima tentativa" +
                                           "| Error " + errorCode + ": Request limit exceeded! Wait 60 seconds for the next attempt");
                        Thread.sleep(60000);                                // --> Espera 1 minuto para tentar novamente
                        attempts *= 2;                                            // --> Dobra o número de tentativas

                    }
                }
            }
        }
        throw new RuntimeException("API da OpenAI indisponível!, Tentativas Excedidas | OpenAI API unavailable!, Exceeded Attempts");
    }

}
