package com.hospitality.api.infraestructure.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuestRepository extends CrudRepository<GuestEntity, Long> {

    @Query("SELECT g FROM GuestEntity g WHERE LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(g.document) LIKE LOWER(CONCAT('%', :document, '%')) " +
            "OR LOWER(g.phone) LIKE LOWER(CONCAT('%', :phone, '%'))")
    Optional<List<GuestEntity>> findByNameOrDocumentOrPhone(@Param("name") String name,
                                            @Param("document") String document,
                                            @Param("phone") String phone);
}
