package org.alixar.daw2.dario.dwese_ticket_logger_webapp.dwese_ticket_logger_webapp.repositories;

import org.alixar.daw2.dario.dwese_ticket_logger_webapp.dwese_ticket_logger_webapp.entities.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {

    @Query("SELECT COUNT(r) > 0 FROM Region r WHERE r.code = :code AND r.id != :id")
    boolean existsRegionByCodeAndNotId(@Param("code") String code, @Param("id") Long id);

    @Query("SELECT COUNT(r) > 0 FROM Region r WHERE r.code = :code")
    boolean existsRegionByCode(@Param("code") String code);
}
