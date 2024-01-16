package br.com.danilo.ecommerce;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.ModelType;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;

// --> Identificador de Perfil de Compra de Clientes
public class ProfileIdentifier {

    public static void main(String[] args) {
        var promptSystem = """
                Identifique o perfil de compra de cada cliente.
                                
                A resposta deve ser:
                                
                Cliente - descreva o perfil do cliente em três palavras
                """;

        var client = loadCustomersFromFile();                              // --> Carrega os clientes do arquivo
        String modelOpenAI = "gpt-3.5-turbo";                                     // --> Modelo do OpenAI a ser utilizado
        var tokens = countTokens(promptSystem);                              // --> Contador de Tokens
        var expectedResponseSize = 2048;                                          // --> Tamanho esperado da resposta


        //? ------------------------------------  TESTE MODEL OPENAI  --------------------------------------------------
        System.out.println("Quantidade de Tokens: | Number of Tokens:" + tokens);
        System.out.println("Modelo OpenAI: | Model OpenAI: " + modelOpenAI + "\n");
        System.out.println("||- ---------------------------------------------------------------- -||" + "\n" + "\n");

        if (tokens > 4096) {                // verificador de quantidade de tokens para escolha do melhor modelo do OpenAI
            modelOpenAI = "gpt-3.5-turbo-16k";
        }
        //? ------------------------------------------------------------------------------------------------------------

        var request = ChatCompletionRequest
                .builder()
                .model(modelOpenAI)                                                 // --> Modelo do OpenAI a ser utilizado
                .maxTokens(expectedResponseSize)                          // --> Tamanho esperado da resposta
                .messages(Arrays.asList(
                        new ChatMessage(
                                ChatMessageRole.SYSTEM.value(),
                                promptSystem),
                        new ChatMessage(
                                ChatMessageRole.SYSTEM.value(),
                                client)))
                .build();

        var keyToken = System.getenv("OPENAI_API_KEY");
        var serviceOpenAI = new OpenAiService(keyToken, Duration.ofSeconds(30));

        System.out.println(
                serviceOpenAI
                        .createChatCompletion(request)
                        .getChoices().get(0).getMessage().getContent());
    }

    // --> Carrega os clientes do arquivo
    private static String loadCustomersFromFile() {
        try {
            var path = Path.of(ClassLoader
                    .getSystemResource("src/main/resources/shoppingList/lista_de_compras_10_clientes.csv")
                    .toURI());
            return Files.readAllLines(path).toString();
        } catch (Exception errorLoadFile) {
            throw new RuntimeException("Erro ao carregar o arquivo! | Error loading file!", errorLoadFile);
        }
    }

    // --> Contador de Tokens
    private static int countTokens(String prompt) {
        EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();               // Registro de codificação
        Encoding enc = registry.getEncodingForModel(ModelType.GPT_3_5_TURBO);   // Modelo utilizado para o cálculo
        return enc.countTokens(prompt);
    }

}
