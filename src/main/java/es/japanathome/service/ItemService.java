package es.japanathome.service;

import es.japanathome.domain.Item;
import es.japanathome.domain.Product;
import es.japanathome.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Map;

@Service
@Transactional
public class ItemService
{

    private final Logger log = LoggerFactory.getLogger(ItemService.class);

    @Inject
    private ProductRepository productRepository;

    public BigDecimal getPrice(Map.Entry<Long, Item> itemEntry)
    {
        Product product = productRepository.getOne(itemEntry.getKey());
        Item item = itemEntry.getValue();
        Integer quantity = item.getQuantity();

        return product.getPrice().multiply( new BigDecimal( quantity ) );
    }

}
