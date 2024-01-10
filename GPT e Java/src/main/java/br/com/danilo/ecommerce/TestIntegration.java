package br.com.danilo.ecommerce;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.util.Arrays;

public class TestIntegration {

    public static void main(String[] args) {
        System.out.println("""
                Teste de Integração GPT e Java| Test integration GPT and Java!
                                
                GPT-3 é uma ferramenta de linguagem que usa aprendizado de máquina para gerar texto que parece ter sido escrito por um humano.
                o link para a Biblioteca OpenAI GPT-3.5 Turbo com o java é: "https://github.com/TheoKanning/openai-java"



                """);

        var userRepresentation = " Gere 5 produtos";
        var systemRepresentation = " Você é um gerador de produtos ficticios para um ecommerce e deve gerar apenas os nomes dos produtos";

        // OpenAI Service

        var tokenKey = System.getenv("OPENAI_API_KEY");        // --> Variável de ambiente com o Token da API OpenAI
        var service = new OpenAiService(tokenKey);                          // --> Token da API OpenAI
        var completionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")                             // --> Modelo de GPT que se utiliza -> GPT-3
                .messages(Arrays.asList(
                        new ChatMessage(ChatMessageRole.USER.value(), userRepresentation),      // --> para cada mensagem, é necessário informar o papel do usuário
                        new ChatMessage(ChatMessageRole.SYSTEM.value(), systemRepresentation)   // --> para cada mensagem, é necessário informar o papel do sistema
                ))
                .build();
        service.createChatCompletion(completionRequest).
                getChoices().                                   // --> Obter as Respostas da API
                forEach(completion -> System.out.println(completion.getMessage().getContent()));     // --> Imprimir as respostas
    }
}
