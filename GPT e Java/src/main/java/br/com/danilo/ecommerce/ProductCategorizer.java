package br.com.danilo.ecommerce;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;
import java.util.Arrays;
import java.util.Scanner;

// --> Categorizador de Produtos
public class ProductCategorizer {

    public static void main(String[] args) {

        // --> Entrada do usuário para as categorias válidas
        var keyboardReader = new Scanner(System.in);

        System.out.println("Dígite as Categorias validas: | Enter the valid Categories:");
        String validCategories = keyboardReader.nextLine();

        while (true) {
            // --> Entrada do usúario para os produtos
            System.out.println("Dígite os Produtos: | Enter the Products:");
            String products = keyboardReader.nextLine();

            var userRepresentation = keyboardReader.nextLine();
            var systemRepresentation = """
                                               "Você é um categorizador de produtos e deve responder o nome da categoria de cada produto
                                                               
                                               Escolha uma das categorias abaixo:
                                               """ + validCategories + """
                                                     
                                               ##### Exemplo de resposta #####
                                               Pergunta: Alicate
                                               Resposta: Ferramentas
                                                                   
                                               Pergunta: Bola de Futebol
                                               Resposta: Esportes
                                                                   
                                               Pergunta: Prato
                                               Resposta: Cozinha
                                                                   
                                                                   
                                               ##### Regras a serem seguidas #####
                                               1. Cada produto deve ser categorizado em apenas uma categoria
                                               2. Caso o usuário pergunte algo que não seja de categorização de produto, o sistema deve responder com a mensagem: 
                                               "Não posso te ajudar com esta solicitação, meu papel e ajudar a responder sobre categorização de produtos"    
                                               """;

            triggerRequest(userRepresentation, systemRepresentation);
        }
    }

    public static void triggerRequest(String userRepresentation, String systemRepresentation) {

        var tokenKey = System.getenv("OPENAI_API_KEY");                                        // --> Variável de ambiente com o Token da API OpenAI
        var service = new OpenAiService(tokenKey, Duration.ofSeconds(30));                   // --> Token da API OpenAI

        var completionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")                                                               // --> Modelo de GPT que se utiliza -> GPT-3
                .messages(Arrays.asList(
                        new ChatMessage(ChatMessageRole.USER.value(), userRepresentation),      // --> para cada mensagem, é necessário informar o papel do usuário
                        new ChatMessage(ChatMessageRole.SYSTEM.value(), systemRepresentation)   // --> para cada mensagem, é necessário informar o papel do sistema
                ))
//                .n(1)                                                                                    // --> Número de respostas que se deseja obter
                .build();
        service.
                createChatCompletion(completionRequest).
                getChoices().                                                                               // --> Obter as Respostas da API
                forEach(completion -> {
            System.out.println(completion.getMessage().getContent());
            System.out.println("<<----------  ----------  ----------  ---------->>");
        });                    // --> Imprimir as respostas
    }
}
