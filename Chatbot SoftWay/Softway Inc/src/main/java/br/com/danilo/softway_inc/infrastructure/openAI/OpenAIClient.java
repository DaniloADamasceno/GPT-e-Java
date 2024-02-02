package br.com.danilo.softway_inc.infrastructure.openAI;

import br.com.danilo.softway_inc.domain.DataShippingCalculations;
import br.com.danilo.softway_inc.domain.service.ShippingCalculator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.chat.ChatFunction;
import com.theokanning.openai.completion.chat.ChatFunctionCall;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.messages.Message;
import com.theokanning.openai.messages.MessageRequest;
import com.theokanning.openai.runs.Run;
import com.theokanning.openai.runs.RunCreateRequest;
import com.theokanning.openai.runs.SubmitToolOutputRequestItem;
import com.theokanning.openai.runs.SubmitToolOutputsRequest;
import com.theokanning.openai.service.FunctionExecutor;
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
    private final String model = "gpt-3.5-turbo-1106";     // --> Modelo do GPT
    private final ShippingCalculator shippingCalculator;   // --> Calculadora de Frete

    //! ---------------------------------------------   Constructor   --------------------------------------------------
    public OpenAIClient(@Value("${app.openai.api.key}") String tokenKey,
                        @Value("${app.openai.assistant-id}") String assistantID,
                        ShippingCalculator shippingCalculator) {
        this.tokenKey = tokenKey;
        this.openAiService = new OpenAiService(tokenKey, Duration.ofSeconds(60));
        this.assistantID = assistantID;
        this.shippingCalculator = shippingCalculator;
    }


    //! ---------------------------------------------   Methods   ------------------------------------------------------

    //%% -- -- --  --> Enviar Requisição para o Chat
    public String sendRequestToChatCompletion(ChatCompletionRequestData dados) {
        var messageRequest = MessageRequest                        // --> Cria uma requisição de mensagem (Representa a mensagem enviada pelo usuário)
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


        //%% -- -- --  --> Cria uma requisição de Run (Representa uma execução do modelo GPT-3.5-Turbo)
        var runRequest = RunCreateRequest
                .builder()
                .model(this.model)
                .assistantId(assistantID)
                .build();                                             // --> Cria uma Run na API da OpenAI
        var run = openAiService
                .createRun(threadID, runRequest);


        //%% -- -- --  --> Verifica se a Run foi concluída e se alguma função precisará ser chamada
        var concluded = false;                                        // --> Variável para verificar se a Run foi concluída
        var needCallFunction = false;                                 // --> Variável para verificar se a função de cálculo de frete precisa ser chamada
        try {
            while (!concluded && !needCallFunction) {                  // --> Enquanto a Run não estiver concluída e a função de cálculo de frete não precisar ser chamada
                Thread.sleep(1000 * 10);                        // --> Espera 10 segundos
                run = openAiService.retrieveRun(threadID, run.getId());             // --> Atualiza a Run
                concluded = run.getStatus().equalsIgnoreCase("completed");    // --> Verifica se a Run foi concluída
                needCallFunction = run.getRequiredAction() != null;                       // --> Verifica se a função de cálculo de frete precisa ser chamada
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        //%% -- -- --  --> Verifica se a Função de Cálculo de Frete precisa ser chamada
        if (needCallFunction) {                                        // --> Se a função de cálculo de frete precisar ser chamada
            var shippingPrice = callFunctionShipping(run);    // --> Chama a função de cálculo de frete
            var submitRequest = SubmitToolOutputsRequest             // --> Cria uma requisição de submissão de saída da ferramenta
                    .builder()
                    .toolOutputs(Arrays.asList(
                            new SubmitToolOutputRequestItem(
                                    run
                                            .getRequiredAction()
                                            .getSubmitToolOutputs()
                                            .getToolCalls()
                                            .get(0)
                                            .getId(),
                                    shippingPrice)
                    ))
                    .build();
            openAiService.submitToolOutputs(
                    threadID, run.getId(), submitRequest);       // --> Submete a requisição de submissão de saída da ferramenta


            //%% -- -- --  --> Verifica se a Run foi concluída
            var numberAttempts = 0;                                                          // --> Número de tentativas
            try {
                while (!concluded) {                                                          // --> Enquanto a Run não estiver concluída
                    Thread.sleep(1000 * 10);                                            // --> Espera 10 segundos
                    run = openAiService.retrieveRun(threadID, run.getId());             // --> Atualiza a Run
                    concluded = run.getStatus().equalsIgnoreCase("completed");    // --> Verifica se a Run foi concluída
                    if (numberAttempts++ == 5) {
                        throw new RuntimeException("API OpenAI não respondeu, tente mais tarde | OpenAI API unresponsive, try later");
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


            //%% -- -- -- --> Lista de Mensagens da Thread
            var listMessages = openAiService.listMessages(threadID);  // --> Lista as mensagens da Thread
            return listMessages
                    .getData()                                                                 // --> Pega os dados da lista de mensagens
                    .stream()                                                                   // --> Transforma em Stream
                    .sorted(Comparator.comparingInt(
                            Message::getCreatedAt).reversed())                       // --> Ordena as mensagens pela data de criação de forma reversa
                    .findFirst().get().getContent().get(0).getText().getValue()                 // --> Pega a primeira mensagem da lista
                    .replaceAll("\\\u3010.*?\\\u3011", "");                    // --> Remove os  Caracteres do final da mensagem
        }


        //%% -- -- -- --> Chamar a Função de Cálculo de Frete
        private Object callFunctionShipping (Run run){
            try {
                var function = run.getRequiredAction().getSubmitToolOutputs().getToolCalls().get(0).getFunction();
                var functionShippingCalculator = ChatFunction.builder()
                        .name("ShippingCalculator")
                        .executor(DataShippingCalculations.class, calculations -> ShippingCalculator.computeShippingCost(calculations))
                        .build();

                var functionExecutor = new FunctionExecutor(Arrays.asList(functionShippingCalculator));
                var functionCall = new ChatFunctionCall(function.getName(), new ObjectMapper().readTree(function.getArguments()));
                return functionExecutor.execute(functionCall).toString();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


        //%% -- -- -- --> Carregar Histórico de Conversa
        public List<String> uploadMessageHistory () {
            var messages = new ArrayList<String>();

            if (this.threadID != null) {
                messages.addAll(
                        openAiService
                                .listMessages(this.threadID)
                                .getData()                         // --> Pega os dados da lista de mensagens
                                .stream()                          // --> Transforma em Stream
                                .sorted(Comparator.comparingInt(Message::getCreatedAt)) // --> Ordena as mensagens pela data de criação
                                .map(mapMessage -> mapMessage.getContent().get(0).getText().getValue()) // --> Pega a mensagem e adiciona na lista
                                .collect(Collectors.toList())   // --> Transforma em lista
                );
            }

            return messages;
        }

        //%% -- -- -- --> Limpar Thread
        public void clearThread () {
            if (this.threadID != null) {
                openAiService.deleteThread(this.threadID); // --> Deleta a Thread
                this.threadID = null;
            }
        }