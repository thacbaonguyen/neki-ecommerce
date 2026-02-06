package com.thacbao.neki.model;

import com.thacbao.neki.model.base.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id", nullable = false)
    private SubCategory subCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "base_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "sale_price", precision = 10, scale = 2)
    private BigDecimal salePrice;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('Men', 'Women', 'Unisex', 'Kids')")
    private Gender gender = Gender.UNISEX;

    @Builder.Default
    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Builder.Default
    @Column(name = "is_new")
    private Boolean isNew = false;

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    // SEO fields
    @Column(name = "meta_title", length = 255)
    private String metaTitle;

    @Column(name = "meta_description", length = 500)
    private String metaDescription;

    @Column(name = "meta_keywords", length = 500)
    private String metaKeywords;

    // Average rating (denormalized for performance)
    @Builder.Default
    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating = BigDecimal.ZERO;

    // Total reviews count (denormalized)
    @Builder.Default
    @Column(name = "review_count")
    private Integer reviewCount = 0;

    // Total sold (denormalized)
    @Builder.Default
    @Column(name = "total_sold")
    private Integer totalSold = 0;

    // View count for popularity
    @Builder.Default
    @Column(name = "view_count")
    private Long viewCount = 0L;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "product_collections",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "collection_id")
    )
    private Set<Collection> collections = new HashSet<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "product_topics",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id")
    )
    private Set<Topic> topics = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductImage> images = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductVariant> variants = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<Review> reviews = new HashSet<>();

    @Builder.Default
    @ManyToMany(mappedBy = "products")
    private Set<Wishlist> wishlists = new HashSet<>();

    public BigDecimal getCurrentPrice() {
        return salePrice != null && salePrice.compareTo(BigDecimal.ZERO) > 0
                ? salePrice : basePrice;
    }

    public boolean isOnSale() {
        return salePrice != null && salePrice.compareTo(BigDecimal.ZERO) > 0
                && salePrice.compareTo(basePrice) < 0;
    }

    public BigDecimal getDiscountPercentage() {
        if (!isOnSale()) return BigDecimal.ZERO;
        return basePrice.subtract(salePrice)
                .divide(basePrice, 2, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }


    public enum Gender {
        MEN("Men"),
        WOMEN("Women"),
        UNISEX("Unisex"),
        KIDS("Kids");

        private final String value;

        Gender(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}