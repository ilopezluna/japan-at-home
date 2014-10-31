package es.japanathome.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.apache.commons.lang.StringUtils.EMPTY;

/**
 * Created with IntelliJ IDEA.
 * User: ignasi
 * Date: 15/06/14
 * Time: 22:34
 */
public class Merchant implements Serializable
{
    private final String code;
    private final String terminal;
    private final String order;
    private final String amount;
    private final String currency;
    private final String transactionType;
    private final String url;
    private final String signature;

    @JsonIgnore
    private final String password;

    private final String responseSignature;

    public Merchant(
            String code,
            String terminal,
            String order,
            String amount,
            String currency,
            String transactionType,
            String url,
            String password,
            String response
    )
    {
        this.code = code;
        this.terminal = terminal;
        this.order = order;
        this.amount = amount;
        this.currency = currency;
        this.transactionType = transactionType;
        this.url = url;
        this.password = password;

        this.signature = toSHA1(getDataToSignForRequest());
        this.responseSignature = toSHA1( getDataToSignOnResponse( response ));
    }

    public String getCode() {
        return code;
    }

    public String getTerminal() {
        return terminal;
    }

    public String getOrder() {
        return order;
    }

    public String getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getUrl() {
        return url;
    }

    public String getSignature() {
        return signature;
    }

    public String getResponseSignature() {
        return responseSignature;
    }

    public String getPassword() {
        return password;
    }

    public String getDataToSignForRequest()
    {
        /**
         * Digest=SHA-1(Ds_Merchant_Amount + Ds_Merchant_Order
         * +Ds_Merchant_MerchantCode + DS_Merchant_Currency
         * +Ds_Merchant_TransactionType + Ds_Merchant_MerchantURL + CLAVE SECRETA)
         */

        return getAmount() + getOrder() + getCode() + getCurrency() + getTransactionType() + getUrl() + getPassword();
    }

    public String getDataToSignOnResponse( String response )
    {
        return amount + order + getCode() + getCurrency() + response + getPassword();
    }

    private String toSHA1(String toSign)
    {
        if (StringUtils.isEmpty( toSign ) ) {
            return EMPTY;
        }

        MessageDigest md;
        try
        {
            md = MessageDigest.getInstance("SHA-1");
            md.update(toSign.getBytes() );

            byte byteData[] = md.digest();

            //convert the byte to hex format method 1
            StringBuilder sb = new StringBuilder();
            for (byte aByteData1 : byteData) {
                sb.append(Integer.toString((aByteData1 & 0xff) + 0x100, 16).substring(1));
            }

            //convert the byte to hex format method 2
            StringBuilder hexString = new StringBuilder();
            for (byte aByteData : byteData) {
                String hex = Integer.toHexString(0xff & aByteData);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return EMPTY;
    }

    @Override
    public String toString() {
        return "Merchant{" +
                "code='" + code + '\'' +
                ", terminal='" + terminal + '\'' +
                ", order='" + order + '\'' +
                ", amount='" + amount + '\'' +
                ", currency='" + currency + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", url='" + url + '\'' +
                ", signature='" + signature + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
