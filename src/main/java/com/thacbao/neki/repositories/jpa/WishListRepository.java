package com.thacbao.neki.repositories.jpa;

import com.thacbao.neki.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<Wishlist, Integer> {

    @Query("""
    select w from Wishlist w
    join fetch w.products p
    where w.user.id = :userId
""")
    Optional<Wishlist> findByUserIdFetchProducts(Integer userId);

    boolean existsByUserIdAndProducts_Id(Integer userId, Integer productId);
}
