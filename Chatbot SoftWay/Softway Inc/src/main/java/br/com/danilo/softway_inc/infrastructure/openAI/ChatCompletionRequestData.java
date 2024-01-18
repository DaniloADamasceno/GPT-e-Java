package br.com.danilo.softway_inc.infrastructure.openAI;

// --> Dados da requisição do ChatCompletion
public record ChatCompletionRequestData(String promptSystem, String promptUser) {}
