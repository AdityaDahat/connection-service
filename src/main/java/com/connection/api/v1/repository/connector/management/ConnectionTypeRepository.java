package com.connection.api.v1.repository.connector.management;

import com.connection.api.v1.model.connector.management.ConnectionType;
import com.connection.api.v1.model.connector.management.dto.ConnectionTypeProjection;
import com.connection.security.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConnectionTypeRepository extends CrudRepository<ConnectionType , String> {


    Optional<ConnectionType> findByNameAndIsDeleted(String name, boolean isDeleted);

    Optional<ConnectionType> findByIdAndIsDeleted(String connectionTypeId, boolean b);

    Page<ConnectionType> findAllByIsDeletedAndType(boolean b, String connectionObjectType, Pageable pageable);

    Page<ConnectionType> findAllByIsDeleted(boolean b, Pageable pageable);

    List<ConnectionType> findAllByIdInAndIsDeleted(List<String> id, boolean isDeleted);
//    @Query(value = """
//    SELECT c.name AS name,
//           c.short_name AS shortName,
//           c.is_private AS isPrivate,
//           c.status AS status,
//           c.is_global_connection_type AS isGlobalConnectionType,
//           c.is_deleted AS isDeleted,
//           td.provider AS provider,
//           td.type AS type,
//           td.authentication_mode AS authenticationMode,
//           td.connection_object_type AS connectionObjectType,
//           td.additional_info AS additionalInfo
//    FROM connection_type c
//    LEFT JOIN type_detail td ON c.id = td.connection_type_id
//    WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))
//""",
//            countQuery = """
//    SELECT COUNT(*)
//    FROM connection_type c
//    LEFT JOIN type_detail td ON c.id = td.connection_type_id
//    WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))
//""",
//            nativeQuery = true)
    Page<ConnectionTypeProjection> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);

//    List<ConnectionType> findByCategoryIdAndIsDeleted(String connectionTypeCategoryId, boolean b);

    List<ConnectionType> findByIsDeleted(boolean b);
}
