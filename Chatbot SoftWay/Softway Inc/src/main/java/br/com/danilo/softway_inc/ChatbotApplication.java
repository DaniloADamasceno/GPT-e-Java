package br.com.danilo.softway_inc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatbotApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ChatbotApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("✅ ✅");
        System.out.println();
        System.out.println("✅✅ Banco SOFTWAY iniciado com sucesso! ✅✅ ");
        System.out.println("Desenvolvido por: Danilo A. Damasceno");
        System.out.println("LinkedIn: https://www.linkedin.com/in/daniloadamasceno/");
        System.out.println("GitHub: https://github.com/DaniloADamasceno");
        System.out.println("link para acessar o SWAGGER: http://localhost:8080/swagger-ui.html");
        System.out.println("Link para acessar a API da OpenAI: https://platform.openai.com/signup");
        System.out.println("Link para acessar o ChatBot: http://localhost:8080/chat");
        System.out.println("✅ ✅");
    }
}

