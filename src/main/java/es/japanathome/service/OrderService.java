package es.japanathome.service;

import es.japanathome.domain.Item;
import es.japanathome.domain.Order;
import es.japanathome.domain.Product;
import es.japanathome.domain.Zip;
import es.japanathome.dto.Merchant;
import es.japanathome.repository.OrderRepository;
import es.japanathome.repository.ProductRepository;
import es.japanathome.repository.ZipRepository;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OrderService {

    private final static String ITEMS_EMPTY = "item collection is empty";
    private final static String ITEM_INVALID_QUANTITY = "item invalid quantity";
    private final static String RESTAURANT_IS_NULL = "restaurant is null";
    private final static String ADDRESS_IS_NULL = "address is null";
    private final static String PAYMENT_TYPE_IS_NULL = "paymentType is null";
    private static final String ZIP_IS_NULL = "zip is null";
    private static final String ZIP_IS_NOT_VALID = "zip is not valid";

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Inject
    private OrderRepository orderRepository;

    @Inject
    private ItemService itemService;

    @Inject
    private ProductRepository productRepository;

    @Inject
    private ZipRepository zipRepository;

    @Inject
    private MailService mailService;

    @Inject
    private Environment env;

    private String orderMails;

    @PostConstruct
    public void init()
    {
        this.orderMails = env.getProperty("order.mails");
    }


    public Order create(Order order)
    {
        log.debug("Creating order");
        fillOrder(order);
        log.debug("Saving order: " + order.toString());
        return orderRepository.save( order );
    }

    private void fillOrder(Order order) {

        order.setCreatedAt( LocalDate.now(DateTimeZone.UTC) );
        order.setStatus( Order.Status.CREATED );

        Map<Long, Item> itemsResult = new HashMap<>();

        Map<Long, Item> items = order.getItems();
        for ( Map.Entry<Long, Item> entry : items.entrySet() )
        {
            Product product = productRepository.findOne(entry.getKey());

            Item item = new Item();
            item.setProduct(product);
            item.setQuantity(entry.getValue().getQuantity());

            itemsResult.put( product.getId(), item );
        }

        order.setItems(itemsResult);
    }

    public BigDecimal getPrice(Order order)
    {
        log.debug("Get order price");
        BigDecimal orderPrice = BigDecimal.ZERO;
        for (Map.Entry<Long, Item> itemEntry : order.getItems().entrySet())
        {
            orderPrice = orderPrice.add( itemService.getPrice( itemEntry ) );
        }
        return orderPrice;
    }

    public void validate(Order order)
    {
        if ( CollectionUtils.isEmpty(order.getItems()) )
        {
            throw new ValidationException(ITEMS_EMPTY);
        }
        for (Item item : order.getItems().values())
        {
            if ( item.getQuantity() < 1 )
            {
                throw new ValidationException(ITEM_INVALID_QUANTITY);
            }
        }
        if ( order.getRestaurant() == null )
        {
            throw new ValidationException(RESTAURANT_IS_NULL);
        }
        if ( order.getAddress() == null )
        {
            throw new ValidationException(ADDRESS_IS_NULL);
        }
        if ( order.getPaymentType() == null )
        {
            throw new ValidationException(PAYMENT_TYPE_IS_NULL);
        }
        if ( order.getZip() == null )
        {
            throw new ValidationException(ZIP_IS_NULL);
        }

        Zip zip = zipRepository.findByCode(order.getZip().getCode());
        //TODO hidden feature, fix it!
        order.setZip(zip);

        if (order.getZip() == null)
        {
            throw new ValidationException(ZIP_IS_NOT_VALID);
        }

        if ( !order.getRestaurant().getZips().contains(order.getZip()) )
        {
            throw new ValidationException(ZIP_IS_NOT_VALID);
        }
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findOne(Long id) {
        return orderRepository.findOne(id);
    }

    public void delete(Long id) {
        orderRepository.delete(id);
    }

    public void confirm( Merchant merchant, int responseCode, String signature )
    {
        boolean validated = validateSermepaResponse(merchant, responseCode, signature);
        if ( validated )
        {
            Order order = orderRepository.findByCode( merchant.getOrder() );
            order.setStatus( Order.Status.PAID );
            orderRepository.save( order );

            sendOrderMail(order);
        }
    }

    private boolean validateSermepaResponse(Merchant merchant, int responseCode, String signature)
    {
        String localSignature = merchant.getResponseSignature();
        String remoteSignature = signature.toLowerCase();
        log.debug("Remote signature: " + remoteSignature);
        log.debug("Local signature: " + localSignature);
        log.debug("ResponseCode: " + responseCode);
        log.debug("Merchant: " + merchant);

        return remoteSignature.equals( localSignature ) && isValid(responseCode);
    }


    private void sendOrderMail(Order order)
    {
        String content = order.toString();

        String[] emails = orderMails.split(",");
        for (String email : emails)
        {
            mailService.sendOrderEmail( email, "Pedido!", content);
        }
    }

    private boolean isValid(int responseCode)
    {
        return responseCode > -1 && responseCode < 100; //TODO magic numbers..
    }

    public void saveAndFlush(Order order)
    {
        orderRepository.saveAndFlush(order);
    }
}
