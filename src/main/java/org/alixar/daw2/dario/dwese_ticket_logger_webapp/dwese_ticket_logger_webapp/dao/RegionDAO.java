package org.alixar.daw2.dario.dwese_ticket_logger_webapp.dwese_ticket_logger_webapp.dao;


import org.alixar.daw2.dario.dwese_ticket_logger_webapp.dwese_ticket_logger_webapp.entities.Region;
import java.sql.SQLException;
import java.util.List;
public interface RegionDAO {

    List<Region> listAllRegions() throws SQLException;

    void insertRegion(Region region) throws SQLException;
    void updateRegion(Region region) throws SQLException;
    void deleteRegion(int id) throws SQLException;

    Region getRegionById(int id) throws SQLException;
    boolean existsRegionByCode(String code) throws SQLException;
    boolean existsRegionByCodeAndNotId(String code, int id) throws SQLException;}