package org.alixar.daw2.dario.dwese_ticket_logger_webapp.dwese_ticket_logger_webapp.repositories;

import org.alixar.daw2.dario.dwese_ticket_logger_webapp.dwese_ticket_logger_webapp.entities.Province;
import org.alixar.daw2.dario.dwese_ticket_logger_webapp.dwese_ticket_logger_webapp.entities.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProvinceRepository extends JpaRepository<Province, Long> {

    @Query("SELECT COUNT(r) > 0 FROM Region r WHERE r.code = :code AND r.id != :id")
    boolean existsProvinceByCodeAndNotId(@Param("code") String code, @Param("id") Long id);

    @Query("SELECT COUNT(r) > 0 FROM Region r WHERE r.code = :code")
    boolean existsProvinceByCode(@Param("code") String code);
}
