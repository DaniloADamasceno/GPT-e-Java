package br.com.danilo.softway_inc.infrastructure.openAI;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.ModelType;
import org.springframework.stereotype.Component;


// --> Contador de Tokens
@Component
public class TokenCounter {

    private final Encoding encoding;

    public TokenCounter() {
        var registry = Encodings.newDefaultEncodingRegistry();
        this.encoding = registry.getEncodingForModel(ModelType.GPT_3_5_TURBO);
    }

    public int contarTokens(String message) {
        return encoding.countTokens(message);
    }

}
