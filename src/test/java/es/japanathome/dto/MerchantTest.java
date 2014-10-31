package es.japanathome.dto;

import org.junit.Test;

import static es.japanathome.Constants.*;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;

public class MerchantTest {

    private final Merchant MERCHANT = new Merchant(
            DS_MERCHANT_CODE,
            DS_MERCHANT_TERMINAL,
            DS_MERCHANT_ORDER,
            DS_MERCHANT_AMOUNT,
            DS_MERCHANT_CURRENCY,
            DS_TRANSACTION_TYPE,
            EMPTY,
            PASSWORD,
            EMPTY);

    @Test
    public void testGetDataToSign() throws Exception
    {
        assertThat(MERCHANT.getDataToSignForRequest()).isEqualTo(DATA_TO_SIGN);
    }

    @Test
    public void testGetSignature() throws Exception
    {
        assertThat(MERCHANT.getSignature()).isEqualTo(SIGNATURE);
    }

    @Test
    public void testGetSignatureOfResponse() throws Exception
    {

        Merchant merchant = new Merchant("329994727", "1", "542988177621","2980", "978", "0", "https://japan-at-home-v5.herokuapp.com/app/rest/order", "qwertyasdf0123456789", "0000");
        assertThat(merchant.getResponseSignature()).isEqualTo("2b8967e5af93188e8ac2c7556543c879ac846a96");
    }
}