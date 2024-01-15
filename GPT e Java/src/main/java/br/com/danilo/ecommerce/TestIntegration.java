package br.com.danilo.ecommerce;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;
import java.util.Arrays;

public class TestIntegration {

    public static void main(String[] args) {

        var userRepresentation = " Ferramentas";
        var systemRepresentation = """
                 Teste de Integração GPT e Java| Test integration GPT and Java!
                                
                GPT-3.5 Turbo é uma ferramenta de linguagem que usa aprendizado de máquina para gerar texto que parece ter sido escrito por um humano.
                o link para a Biblioteca OpenAI GPT-3.5 Turbo com o java é: "https://github.com/TheoKanning/openai-java"
                                
                Você é um gerador de produtos ficticios para um ecommerce e deve gerar apenas os nomes dos produtos
                                
                Escolha uma das categorias abaixo:
                1. Ferramentas Elétricas
                2. Ferramentas Hidráulicas
                3. Ferramentas Pneumáticas
                4. Ferramentais Robóticas
                5. Ferramentas Agrícolas
                6. Ferramentas de Corte
                7. Ferramentas de Medição
                8. Ferramentas de Fixação
                9. Ferramentas de Aperto
                10. Ferramentas de Jardinagem
                                
                                
                ##### Exemplo de resposta #####
                                
                Pergunta: Ferramentas Elétricas
                Resposta: Furadeira, Serra Circular, Serra Tico-Tico, Esmerilhadeira, Lixadeira, Tupia, Plaina, Martelete,
                 Parafusadeira, Lixadeira Orbital
                                
                Pergunta: Ferramentas Hidráulicas
                Resposta: Bomba Hidráulica, Macaco Hidráulico, Prensa Hidráulica, Cilindro Hidráulico, Válvula Hidráulica,
                 Mangueira Hidráulica, Conexão Hidráulica, Motor Hidráulico, Comando Hidráulico, Unidade Hidráulica
                 
                Pergunta: Ferramentas Pneumáticas
                Resposta: Compressor de Ar Comprimido, Pistola de Pintura, Pistola de Ar, Pistola de Limpeza, Pistola de Jateamento,
                 Pistola de Pintura
                 
                Pergunta: Ferramentais Robóticas
                Resposta: Robô Industrial, Robô Colaborativo, Robô de Solda, Robô de Pintura, Robô de Paletização, Robô de Corte,
                 Robô de Montagem, Robô de Embalagem, Robô de Inspeção, Robô de Polimento
                 
                Pergunta: Ferramentas Agrícolas
                Resposta: Trator Agrícola, Colheitadeira, Plantadeira, Pulverizador, Grade Aradora, Roçadeira, Ensiladeira,
                 Semeadeira, Distribuidor de Adubo, Carreta Agrícola
                 
                Pergunta: Ferramentas de Corte
                Resposta: Serra Circular, Serra Tico-Tico, Serra Mármore, Serra Fita, Serra de Bancada, Serra de Esquadria,
                 Serra de Mesa, Serra de Fita, Serra de Corte, Serra de Fita
                 
                Pergunta: Ferramentas de Medição
                Resposta: Trena, Paquímetro, Micrômetro, Nível, Esquadro, Transferidor, Régua, Compasso, Nível a Laser,
                 Medidor de Distância
                 
                Pergunta: Ferramentas de Fixação
                Resposta: Parafuso, Porca, Arruela, Rebite, Prego, Grampo, Abraçadeira, Presilha, Braçadeira, Grampo
                                
                Pergunta: Ferramentas de Aperto
                Resposta: Alicate, Chave de Fenda, Chave Phillips, Chave de Boca, Chave Allen, Chave Torx, Chave Estrela,
                 Chave Combinada, Chave Inglesa, Chave de Grifo
                 
                Pergunta: Ferramentas de Jardinagem
                Resposta: Cortador de Grama, Roçadeira, Aparador de Cerca Viva, Soprador de Folhas, Pulverizador, Motosserra,
                 Motopoda, Perfurador de Solo, Triturador de Galhos, Aspirador de Folhas
                """;



        var tokenKey = System.getenv("OPENAI_API_KEY");                                        // --> Variável de ambiente com o Token da API OpenAI
        var service = new OpenAiService(tokenKey, Duration.ofSeconds(45));                   // --> Token da API OpenAI + Tempo de espera para a resposta da API
        var completionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")                                                               // --> Modelo de GPT que se utiliza -> GPT-3
                .messages(Arrays.asList(
                        new ChatMessage(ChatMessageRole.USER.value(), userRepresentation),      // --> para cada mensagem, é necessário informar o papel do usuário
                        new ChatMessage(ChatMessageRole.SYSTEM.value(), systemRepresentation)   // --> para cada mensagem, é necessário informar o papel do sistema
                ))
                .build();
        service.createChatCompletion(completionRequest).
                getChoices().                                                                               // --> Obter as Respostas da API
                forEach(completion -> System.out.println(completion.getMessage().getContent()));     // --> Imprimir as respostas
    }


}
