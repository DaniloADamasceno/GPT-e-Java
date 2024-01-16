package br.com.danilo.ecommerce;

import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// --> Analisador de Sentimentos de Avaliações de Produtos
public class FeelingsAnalysisErrorHandling {

    public static void main(String[] args) throws InterruptedException {
        var arquivosDeAvaliacoes = uploadReviewsFiles();

        for (var arquivo : arquivosDeAvaliacoes) {
            System.out.println("Iniciando analise do produto: " + arquivo.getFileName());

            var resposta = sendRequest(arquivo);
            saveCustomerAnalysis(arquivo, resposta);

            System.out.println("Analise finalizada");
        }
    }

    private static String sendRequest(Path file) throws InterruptedException {
        var keyToken = System.getenv("OPENAI_API_KEY");
        var openAiService = new OpenAiService(keyToken, Duration.ofSeconds(60));

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
        var promptUsuario = loadFileReviews(file);

        var request = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(Arrays.asList(
                        new ChatMessage(
                                ChatMessageRole.SYSTEM.value(),
                                promptSystem),
                        new ChatMessage(
                                ChatMessageRole.USER.value(),
                                promptUsuario)))
                .build();

        var accessAttempts = 0;
        var attempts = 0;
        while (accessAttempts++ != 5) {                                           // --> Tenta 5 vezes
            try {
                return openAiService
                        .createChatCompletion(request)
                        .getChoices().get(0).getMessage().getContent();
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
    // --> Carrega os arquivos de avaliações
    private static List<Path> uploadReviewsFiles() {
        try {
            var reviewsDirectory = Path.of("src/main/resources/reviewsDirectory/");
            return Files
                    .walk(reviewsDirectory, 1)
                    .filter(path -> path.toString().endsWith(".txt"))
                    .collect(Collectors.toList());
        } catch (Exception errorUploadReviewsFiles) {
            throw new RuntimeException("Erro ao carregar os arquivos de avaliações! | Error loading evaluation files!", errorUploadReviewsFiles);
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
    private static void saveCustomerAnalysis(Path file, String review) {
        try {
            var productName = file
                    .getFileName()
                    .toString()
                    .replace("avaliacoes-", "")
                    .replace(".txt", "");
            var path = Path.of("src/main/resources/savedCustomerReviews" + productName + ".txt");
            Files.writeString(path, review, StandardOpenOption.CREATE_NEW);
        } catch (Exception errorSaveAnalysis) {
            throw new RuntimeException("Erro ao salvar o arquivo! | Error saving file!", errorSaveAnalysis);
        }
    }
}
