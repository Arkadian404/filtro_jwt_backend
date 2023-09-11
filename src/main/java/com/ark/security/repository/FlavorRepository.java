package com.ark.security.repository;

import com.ark.security.models.Flavor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FlavorRepository extends JpaRepository<Flavor, Integer> {

    @Modifying
    @Query("""
    update Flavor f set f.status=false where f.id=:id
""")
    void softDeleteById(@Param("id") int id);

}
