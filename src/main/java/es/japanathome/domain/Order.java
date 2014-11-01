package es.japanathome.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import es.japanathome.domain.util.CustomLocalDateSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

/**
 * A Order.
 */
@Entity
@Table(name = "T_ORDER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Order implements Serializable {

    public enum Status { CREATED, PAID, DELIVERING, DELIVERED }

    public enum PaymentType { CASH, ONLINE }

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @Column(name = "created_on", nullable = false)
    private LocalDate createdOn;

    @Column(name = "address")
    private String address;

    @Column(name = "code")
    private String code;

    @Column(name = "payment_type")
    private PaymentType paymentType;

    @Column(name = "status")
    private Status status;

    @ManyToOne
    private Restaurant restaurant;

    @ManyToOne
    private Zip zip;

    @OneToMany( cascade={ CascadeType.ALL }, fetch=FetchType.EAGER )
    private Map<Long, Item> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Zip getZip() {
        return zip;
    }

    public void setZip(Zip zip) {
        this.zip = zip;
    }

    public Map<Long, Item> getItems() {
        return items;
    }

    public void setItems(Map<Long, Item> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Order order = (Order) o;

        return !(id != null ? !id.equals(order.id) : order.id != null);

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", createdOn=" + createdOn +
                ", address='" + address + '\'' +
                ", code='" + code + '\'' +
                ", paymentType=" + paymentType +
                ", status=" + status +
                ", restaurant=" + restaurant +
                ", zip=" + zip +
                ", items=" + items +
                '}';
    }
}
