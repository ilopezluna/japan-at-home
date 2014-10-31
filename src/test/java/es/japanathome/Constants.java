package es.japanathome;

import es.japanathome.domain.Item;
import es.japanathome.domain.Product;
import es.japanathome.domain.Restaurant;
import es.japanathome.domain.Tag;

import java.math.BigDecimal;

/**
 * Created by ignasi on 14/10/14.
 */
public class Constants {

    public static final String PRODUCT_DEFAULT_NAME = "name";
    public static final BigDecimal PRODUCT_DEFAULT_PRICE = new BigDecimal("1.1");

    public static final String RESTAURANT_DEFAULT_NAME = "name";

    public static final String TAG_DEFAULT_NAME = "name";

    public static final Integer ITEM_DEFAULT_QUANTITY = 1;

    public static final String DEFAULT_ADDRESS = "default address";

    public final static String DS_MERCHANT_CODE = "201920191";
    public final static String DS_MERCHANT_TERMINAL = "1";
    public final static String DS_MERCHANT_ORDER = "29292929";
    public final static String DS_MERCHANT_AMOUNT = "1235";
    public final static String DS_MERCHANT_CURRENCY = "978";
    public final static String DS_TRANSACTION_TYPE = "0";
    public final static String PASSWORD = "h2u282kMks01923kmqpo";
    public final static String DATA_TO_SIGN = "1235292929292019201919780h2u282kMks01923kmqpo";
    public final static String SIGNATURE = "02004012fbec5f547eb9728b575d4587b64df953";

    public static Restaurant buildRestaurant()
    {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(RESTAURANT_DEFAULT_NAME);
        restaurant.setShortName(RESTAURANT_DEFAULT_NAME);
        restaurant.setPrice(BigDecimal.TEN);
        return restaurant;
    }

    public static Product buildProduct(Restaurant restaurant, Tag tag)
    {
        Product product = new Product();
        product.setName(PRODUCT_DEFAULT_NAME);
        product.setPrice(PRODUCT_DEFAULT_PRICE);
        product.setRestaurant(restaurant);
        product.setTag(tag);
        return  product;
    }

    public static Tag buildTag()
    {
        Tag tag = new Tag();
        tag.setName(TAG_DEFAULT_NAME);
        return tag;
    }

    public static Item buildItem(Product product)
    {
        Item item = new Item();
        item.setProduct(product);
        item.setQuantity(ITEM_DEFAULT_QUANTITY);
        return item;
    }

}
