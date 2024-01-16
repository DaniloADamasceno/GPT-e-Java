package br.com.danilo.ecommerce;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.ModelType;

import java.math.BigDecimal;

public class TokenCounter {

    public static void main(String[] args) {
        EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();               // Registro de codificação
        Encoding enc = registry.getEncodingForModel(ModelType.GPT_3_5_TURBO);   // Modelo utilizado para o cálculo
        var qtd = enc.countTokens("Identifique o perfil de compra de cada cliente | Identify the purchase profile of each customer");

        System.out.println("||--------------------------------------------------------------||");

        System.out.println("QTD de Tokens: | QTY of Tokens: " + qtd);
        var priceToken = new BigDecimal(qtd)
                .divide(new BigDecimal("1000"))
                .multiply(new BigDecimal("0.0010"));
        System.out.println("Custo da requisição: R$ | Request cost: R$" + priceToken);

        System.out.println("||--------------------------------------------------------------||");
    }

}
