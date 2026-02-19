package com.thacbao.neki.model;

import com.thacbao.neki.model.base.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_similarity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSimilarity extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id_1", nullable = false)
    private Product product1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id_2", nullable = false)
    private Product product2;

    @Column(name = "score", nullable = false)
    private Double score;

}
