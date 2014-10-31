package es.japanathome.service.util;

import es.japanathome.dto.Merchant;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.math.BigDecimal;

import static es.japanathome.Constants.*;
import static org.assertj.core.api.Assertions.assertThat;

public class SermepaUtilsTest {

    @Test
    public void testGetMerchant() throws Exception {
        Merchant merchant = SermepaUtils.getMerchantForRequest(StringUtils.EMPTY, DS_MERCHANT_CODE, PASSWORD, DS_MERCHANT_ORDER, new BigDecimal("12.35"));
        assertThat(merchant.getDataToSignForRequest()).isEqualTo(DATA_TO_SIGN);
        assertThat(merchant.getSignature()).isEqualTo(SIGNATURE);
    }
}