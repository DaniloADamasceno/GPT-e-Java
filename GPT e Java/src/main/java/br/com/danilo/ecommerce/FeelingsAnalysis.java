package br.com.danilo.ecommerce;


import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Collectors;

// --> Analisador de Sentimentos de Avaliações de Produtos
public class FeelingsAnalysis {

    public static void main(String[] args) {
        try {
            var promptSystem = """
                    Você é um analisador de sentimentos de avaliações de produtos.
                    Escreva um parágrafo com até 50 palavras resumindo as avaliações e depois atribua qual o sentimento geral para o produto.
                    Identifique também 3 pontos fortes e 3 pontos fracos identificados a partir das avaliações.
                                    
                    #### Formato de saída
                    Nome do produto:
                    Resumo das avaliações: [resuma em até 50 palavras]
                    Sentimento geral: [deve ser: POSITIVO, NEUTRO ou NEGATIVO]
                    Pontos fortes: [3 bullets points]
                    Pontos fracos: [3 bullets points]
                    """;

            var productDirectory = Path.of("src/main/resources/customerReviews/");    // --> Diretório dos produtos
            var userReviewFiles = Files
                    .walk(productDirectory, 1)
                    .filter(path -> path.toString().endsWith(".txt"))
                    .collect(Collectors.toList());

            for (Path file : userReviewFiles) {
                System.out.println(" Iniciando análise do produto: | Starting product analysis: " + file.getFileName()); // --> Imprime o nome do arquivo

                var promptUser = loadFileReviews(file);
                String modelOpenAI = "gpt-3.5-turbo";                                     // --> Modelo do OpenAI a ser utilizado

                var request = ChatCompletionRequest
                        .builder()
                        .model(modelOpenAI)                                               // --> Modelo do OpenAI a ser utilizado
                        .messages(Arrays.asList(
                                new ChatMessage(
                                        ChatMessageRole.SYSTEM.value(),
                                        promptSystem),
                                new ChatMessage(
                                        ChatMessageRole.USER.value(),
                                        promptUser)))
                        .build();

                var keyToken = System.getenv("OPENAI_API_KEY");
                var serviceOpenAI = new OpenAiService(keyToken, Duration.ofSeconds(60));

                var answers = serviceOpenAI
                        .createChatCompletion(request)
                        .getChoices().get(0).getMessage().getContent();

                saveCustomerAnalysis(file.getFileName().toString().                   // --> Salvar somente o nome do arquivo
                                replace(".txt", ""),                     // --> Remover o .txt do nome do arquivo
                        answers);

                System.out.println(" Analise Finalizada | Analysis Finished" + "\n");
            }
        } catch (Exception errorWhenParsingFiles) {
            System.out.println("Erro ao analisar o produto! | Error analyzing product!");
        }
    }

    // --> Carrega os clientes do arquivo
    private static String loadFileReviews(Path file) {
        try {
            return Files.readAllLines(file).toString();
        } catch (Exception errorLoadFile) {
            throw new RuntimeException("Erro ao carregar o arquivo! | Error loading file!", errorLoadFile);
        }
    }

    // --> Salva a análise do produto
    private static void saveCustomerAnalysis(String file, String analise) {
        try {
            var path = Path.of("src/main/resources/savedCustomerReviews" + file + ".txt");
            Files.writeString(path, analise, StandardOpenOption.CREATE_NEW);
        } catch (Exception errorSaveAnalysis) {
            throw new RuntimeException("Erro ao salvar o arquivo! | Error saving file!", errorSaveAnalysis);
        }
    }

}