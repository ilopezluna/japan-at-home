package es.japanathome.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A Restaurant.
 */
@Entity
@Table(name = "T_RESTAURANT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Restaurant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "description")
    private String description;

    @Column(name = "address")
    private String address;

    @Column(name = "price", precision=10, scale=2)
    private BigDecimal price;

    @Column(name = "min_price", precision=10, scale=2)
    private BigDecimal minPrice;

    @Column(name = "status")
    private Integer status;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "close_at")
    private String closeAt;

    @Column(name = "open_at")
    private String openAt;

    @Column(name = "day_closed")
    private String dayClosed;

    @Column(name = "average_delivery_time")
    private String averageDeliveryTime;

    @Column(name = "logo")
    private String logo;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Zip> zips = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCloseAt() {
        return closeAt;
    }

    public void setCloseAt(String closeAt) {
        this.closeAt = closeAt;
    }

    public String getOpenAt() {
        return openAt;
    }

    public void setOpenAt(String openAt) {
        this.openAt = openAt;
    }

    public String getDayClosed() {
        return dayClosed;
    }

    public void setDayClosed(String dayClosed) {
        this.dayClosed = dayClosed;
    }

    public String getAverageDeliveryTime() {
        return averageDeliveryTime;
    }

    public void setAverageDeliveryTime(String averageDeliveryTime) {
        this.averageDeliveryTime = averageDeliveryTime;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Set<Zip> getZips() {
        return zips;
    }

    public void setZips(Set<Zip> zips) {
        this.zips = zips;
    }

    public Zip getZip(String code)
    {
        for (Zip zip : zips)
        {
            if ( zip.getCode().equals(code) )
            {
                return zip;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Restaurant restaurant = (Restaurant) o;

        if (id != null ? !id.equals(restaurant.id) : restaurant.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", shortName='" + shortName + "'" +
                ", description='" + description + "'" +
                ", address='" + address + "'" +
                ", price='" + price + "'" +
                ", minPrice='" + minPrice + "'" +
                ", status='" + status + "'" +
                ", telephone='" + telephone + "'" +
                ", closeAt='" + closeAt + "'" +
                ", openAt='" + openAt + "'" +
                ", dayClosed='" + dayClosed + "'" +
                ", averageDeliveryTime='" + averageDeliveryTime + "'" +
                ", logo='" + logo + "'" +
                '}';
    }
}
