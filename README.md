# GPT e Java: Integração com a aplicação da OpenAi

<div align="center">


 </div>

***
</br>
</br>

![GitHub repo size](https://img.shields.io/github/repo-size/DaniloADamasceno/GPT-e-Java?style=for-the-badge)
![GitHub language count](https://img.shields.io/github/languages/count/DaniloADamasceno/GPT-e-Java?style=for-the-badge)

[![wakatime](https://wakatime.com/badge/user/e7f2e494-878d-4290-9a2b-cc473da48b8a/project/018cea71-5844-4ee7-af07-369572eee690.svg)](https://wakatime.com/badge/user/e7f2e494-878d-4290-9a2b-cc473da48b8a/project/018cea71-5844-4ee7-af07-369572eee690)

<!-- Imagens do aplicativo / Programa ou videos  -->
<div align="center">

![java OpenAi 2](https://github.com/DaniloADamasceno/Image-Bank/assets/71226047/f0543921-d696-4d0c-981a-a69bc5952d4b)

![Java OpenAI](https://github.com/DaniloADamasceno/Image-Bank/assets/71226047/d9d55f3f-c6bc-4a42-868a-44ee111f0e82)

![ChatBot GPT](https://github.com/DaniloADamasceno/Image-Bank/assets/71226047/75b41e39-b745-4c41-9002-208d3cc0f141)

 </div>

</br>
</br>

## Descrição do Projeto

</br>

 Este é um projeto de integração com **Java e OpenAI** desenvolvido com Maven e Java.
 projeto consiste em criar um  aplicação utilizando Java e algumas bibliotecas que vão facilitar nosso objetivo de integrar a aplicação com essa API e usar os recursos dessa ferramenta.

Implementação de prompt engineering (engenharia de prompts) e prompt template (template de prompts) para otimizar essas mensagens enviadas na hora de integrar a nossa aplicação com a API.

Utilização da versão 3.5 Turbo.

- Integração do Java com a API da OpenAI
- Engenharia de Prompt e template
- Modelos GPT e seus Custos
- Contagem de Tokens
- Categorizador de Produtos
- Identificador de perfil de chatGPT a ser utilizado (3.5 Turbo, 3.5 Turbo 16k ...etc)
- categorizador de descrição do usuário sobre o produto
- Tratamento de Erros
  
## VARIAÇÕES E TIPOS DO PROJETO

### Contador de Tokens

* Contados de Tokens e seus seus custos ao usuário ao fazer requisições a API do chatGPT

### Categorizador de Custos

* Categorizador de produtos onde ha uma separação e uma categorização de produtos automática

### Identificador de Perfil

* Identificador de perfil e escolha da melhor API a ser utilizada para o sistema com base nos custos de operação 

### Analise de Reviews

* Analizador de reviews de usuários, onde vários usuários fazem suas analises e a API lê todas e faz uma analise geral do produto, classificando as analises em POSITIVAS, NEGATIVAS ou NEUTRAS e também classificado 3 pontos positivos e 3 pontos negativos do produto com base nas analises dos usuários 

***
## MODO CHATBOT

</br>

A proposta é desenvolver um assistente virtual para responder às dúvidas comuns que as clientes possam ter durante a navegação. Desenvolveremos esse projeto e as respostas que o chatbot vai devolver serão todas geradas pela Inteligência Artificial da OpenAI.

### Knowledge Retrieval

* Função que faz a leitura de arquivos externo do sistema e retorna a resposta para o usuário com base na pergunta feita

### Function Calling

* Função que faz a chamada de uma função do sistema e retorna a resposta para o usuário com base na pergunta feita

</br>
</br>

## Pré-requisitos

Antes de começar, certifique-se de ter as seguintes ferramentas e tecnologias instaladas em seu ambiente de desenvolvimento:

- Ter experiência com a linguagem Java e seus recursos básicos, como classes, objetos, interfaces, herança, polimorfismo, exceções, coleções e generics

- Ter conhecimento de SQL e acesso a um banco de dados relacional, como MySQL, PostgreSQL ou H2

- Ter uma IDE de desenvolvimento, como Eclipse, IntelliJ IDEA ou Visual Studio Code

- Ter um gerenciador de dependências, como Maven ou Gradle

**Java**: A linguagem de programação Java para desenvolvimento backend.

**Maven**: Gerenciador de Dependências.

## Instalação

Siga estas etapas para configurar e executar o projeto:

1. Clone o repositório do GitHub:

```bash
git clone https://github.com/DaniloADamasceno/GPT-e-Java
```

1. Crie uma conta para utilização da OpenAI: **https://openai.com/product**
2. Baixe a Biblioteca do OpenAI para Java no github: **https://github.com/TheoKanning/openai-java** e siga as instruções de utilização no Readme
3. Ao adicionar a Dependência da biblioteca ao maven uma alteração devera ser feita no ARTIFACTID para somente **service** e adicionar o numero da versão para **0.18.2** como o exemplo a baixo:

```bash
<artifactId>service</artifactId>
<version>0.18.2</version>
```

4. Adicione as dependências do Maven para o projeto:

```bash
<dependencies>
        <!--*#			Spring Boot Web		-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!--*#			Spring Boot Thymeleaf		-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>

        <!--*#          Spring Boot WebFlux        -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>

        <!--*#          Lombok        -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!--*#          Spring Boot Starter Test        -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <!--*#          OpenAi API        -->
        <dependency>
            <groupId>com.theokanning.openai-gpt3-java</groupId>
            <artifactId>service</artifactId>
            <version>0.18.2</version>
        </dependency>
        <!--*#          Contador e Tokens        -->
        <dependency>
            <groupId>com.knuddels</groupId>
            <artifactId>jtokkit</artifactId>
            <version>0.6.1</version>
        </dependency>
        <!--*#          Spring Boot DevTools        -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>

        <!--*#          Reactor Test        -->
        <dependency>
            <groupId>io.projectreactor</groupId>
            <artifactId>reactor-test</artifactId>
            <scope>test</scope>
        </dependency>
        <!--*#			Swagger		    -->
        <dependency>
            <groupId>io.swagger.core.v3</groupId>
            <artifactId>swagger-models</artifactId>
            <version>2.2.8</version>
        </dependency>
        <!--*#        Swagger         -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-ui</artifactId>
            <version>1.6.15</version>
        </dependency>

    </dependencies>
  ```

5. Adicione no playground do OpenAI a seguinte configuração para o seu projeto:

```bash
{
  "model": "gpt-3.5-turbo",
  "max_tokens": 100,
  "temperature": 0.7,
  "top_p": 1,
  "frequency_penalty": 0,
  "presence_penalty": 0,
  "stop": ["\n", "Human:", "AI:"]
}
```

6. Adicione no playground do OpenAI na area de **add Function** a função de calculo de frete para o seu projeto:

```bash
{
  "name": "calculate Shipping",
  "parameters": {
    "type": "object",
    "properties": {
      "quantityOfProducts": {
        "type": "number",
        "description": "Quantidade de produtos"
      },
      "uf": {
        "type": "string",
        "description": "UF da cidade do cliente",
        "enum": [
          "AC",
          "AL",
          "AP",
          "AM",
          "BA",
          "CE",
          "DF",
          "ES",
          "GO",
          "MA",
          "MT",
          "MS",
          "MG",
          "PA",
          "PB",
          "PR",
          "PE",
          "PI",
          "RJ",
          "RN",
          "RS",
          "RO",
          "RR",
          "SC",
          "SP",
          "SE",
          "TO"
        ]
      }
    },
    "required": [
      "quantityProducts",
      "StatesUF"
    ]
  },
  "description": "Calcula o frete de acordo com a quantidade de produtos e a UF do cliente"
}
```

7. Adicione no playground do OpenAI na area de **Files** os dois arquivos da pasta **resources/softway** do projeto:

* informacoes.md
* politicas.md

</br>

### Contribuição

Este é um projeto de código aberto e estou aberto a contribuições da comunidade.
Se desejar contribuir para este projeto, siga estas etapas:

1. Faça um fork do repositório.
2. Crie um branch para sua feature ou correção de bug: *git checkout -b minha-feature*.
3. Faça suas alterações e adicione suas melhorias.
4. Commit suas alterações: *git commit -m 'Adicionando nova feature'*.
5. Envie para o branch principal do repositório original: *git push origin main*.
6. Abra um pull request no GitHub.



## Stack utilizada

<!-- **Front-end:** ![]() -->

**Back-end:**     ![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white "Badge Java") 
![ChatGPT](https://img.shields.io/badge/chatGPT-74aa9c?style=for-the-badge&logo=openai&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)

<!-- ![SpringBoot](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white "Badge Spring Boot") -->

## Autores

- [@Danilo A. Damasceno](https://github.com/DaniloADamasceno/)

</br>
</br>
</br>

***

## Atualizações de Projeto e suas Versões

#### **Atualização**: 2.2.0
* Desenvolvimento do projeto ChatBot.
* BACK-END em desenvolvimento.
* --> **Desenvolvimento das Funções Knowledge Retrieval e Function Calling**
</br>

### **Atualização**: 2.1.0
* Desenvolvimento do projeto ChatBot.
* BACK-END em desenvolvimento.
* --> **DESENVOLVIMENTO INICIAL**
</br>

### **Atualização**: 1.1.0
* Desenvolvimento do projeto.
* BACK-END em desenvolvimento.
* --> **TRATAMENTO DE ERROS**
</br>

### **Atualização**: 1.0.0 :
* Desenvolvimento Inicial do projeto.
* BACK-END em desenvolvimento.
* --> **PROJETO EM DESENVOLVIMENTO.**
</br>
