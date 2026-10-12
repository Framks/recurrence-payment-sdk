package com.recurrence.pagbank.service;

import com.recurrence.contract.CardService;
import com.recurrence.contract.domain.commons.Card;
import com.recurrence.contract.domain.request.TokenizeCardRequest;
import com.recurrence.contract.infra.RequestOptions;
import com.recurrence.core.BaseService;
import com.recurrence.core.ProviderContext;
import com.recurrence.core.RequestValidator;
import com.recurrence.exception.RecurrenceSerializationException;
import com.recurrence.pagbank.domain.card.PagBankPublicKey;
import com.recurrence.pagbank.mapper.PagBankCardMapper;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static com.recurrence.infrastructure.MethodRequest.GET;
import static com.recurrence.pagbank.constant.Constant.Operation.CARD_ENCRYPT;
import static com.recurrence.pagbank.constant.Constant.Operation.CARD_PUBLIC_KEY;
import static com.recurrence.pagbank.constant.Constant.PATH_PUBLIC_KEYS;

/**
 * Criptografa o cartão com a chave pública da conta (RSA/PKCS#1), como o
 * {@code PagSeguro.encryptCard} do SDK JS.
 *
 * <p>Usa a chave de {@code ProviderConfig.publicKey} (ou
 * {@code RECURRENCE_PAGBANK_PUBLIC_KEY}) quando informada. Sem ela, a chave é
 * consultada em {@code GET /public-keys} na primeira chamada e reaproveitada
 * enquanto o provedor existir; se a conta ainda não tiver chave, a consulta falha
 * com {@code RecurrenceApiException}: gere a chave no PagBank antes de tokenizar.</p>
 */
public class PagBankCardService extends BaseService implements CardService {

    private static final String CIPHER = "RSA/ECB/PKCS1Padding";

    private volatile PublicKey publicKey;

    public PagBankCardService(ProviderContext context) {
        super(context);
    }

    @Override
    public Card tokenize(TokenizeCardRequest card, RequestOptions requestOptions) {
        RequestValidator.validate(card);
        String encryptedCard = encrypt(PagBankCardMapper.toPayload(card), publicKey(requestOptions));
        return PagBankCardMapper.toContract(encryptedCard, card);
    }

    private PublicKey publicKey(RequestOptions requestOptions) {
        PublicKey key = publicKey;
        if (key == null) {
            synchronized (this) {
                key = publicKey;
                if (key == null) {
                    String configured = context.config().getPublicKey();
                    String encoded = configured != null ? configured : execute(
                            CARD_PUBLIC_KEY,
                            request(GET, PATH_PUBLIC_KEYS, requestOptions).build(),
                            PagBankPublicKey.class,
                            PagBankPublicKey::publicKey
                    );
                    key = decode(encoded);
                    publicKey = key;
                }
            }
        }
        return key;
    }

    private PublicKey decode(String encoded) {
        try {
            return KeyFactory.getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(encoded)));
        } catch (GeneralSecurityException | IllegalArgumentException | NullPointerException e) {
            throw new RecurrenceSerializationException(
                    context.provider() + " " + CARD_PUBLIC_KEY + ": invalid public key", e);
        }
    }

    private String encrypt(String payload, PublicKey key) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(payload.getBytes(StandardCharsets.UTF_8)));
        } catch (GeneralSecurityException e) {
            throw new RecurrenceSerializationException(
                    context.provider() + " " + CARD_ENCRYPT + ": failed to encrypt card", e);
        }
    }
}
