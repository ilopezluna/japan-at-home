package es.japanathome.service.util;


import es.japanathome.dto.Merchant;

import java.math.BigDecimal;

import static org.apache.commons.lang.StringUtils.EMPTY;

/**
 * Created with IntelliJ IDEA.
 * User: ignasi
 * Date: 22/06/14
 * Time: 13:14
 */
public final class SermepaUtils
{
    private transient final static String DS_MERCHANT_TERMINAL = "1";
    private transient final static String DS_MERCHANT_CURRENCY = "978";
    private transient final static String DS_TRANSACTION_TYPE = "0";

    public static Merchant getMerchantForRequest(final String url, final String code, final String key, final String orderCode, final BigDecimal orderPrice)
    {
        /**
         * Sermepa doesn't allow decimal values,
         */
        String amount = String.valueOf( orderPrice.multiply(new BigDecimal(100)).intValue() );
        return getMerchant(EMPTY, url, code, key, orderCode, amount);
    }

    public static Merchant getMerchantForResponse(String response, String url, String code, String key, String orderCode, String amount)
    {

        return getMerchant(response, url, code, key, orderCode, amount);
    }

    private static Merchant getMerchant(String response, String url, String code, String key, String orderCode, String amount)
    {
        final Merchant merchant = new Merchant(
                code,
                DS_MERCHANT_TERMINAL,
                orderCode,
                amount,
                DS_MERCHANT_CURRENCY,
                DS_TRANSACTION_TYPE,
                url,
                key,
                response
        );

        return merchant;
    }
}
