package com.connection.api.v1.repository.connector.management;

import com.connection.api.v1.model.connector.management.ConnectionTypeCategory;
import com.connection.api.v1.model.connector.management.dto.ConnectionTypeProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConnectionTypeCategoryRepository extends CrudRepository<ConnectionTypeCategory, String> {
    Optional<ConnectionTypeCategory> findByIdAndIsDeleted(String categoryId, boolean b);

    Page<ConnectionTypeCategory> findByNameContainingIgnoreCaseAndTypeAndIsDeleted(@Param("name") String name, @Param("type") String type, @Param("isDeleted") boolean isDeleted, Pageable pageable);

    Page<ConnectionTypeCategory> findByNameContainingIgnoreCaseAndIsDeleted(@Param("name") String name, @Param("isDeleted") boolean isDeleted, Pageable pageable);

    Page<ConnectionTypeCategory> findByTypeAndIsDeleted(@Param("type") String type, @Param("isDeleted") boolean isDeleted, Pageable pageable);


    Page<ConnectionTypeCategory> findByIsDeleted(boolean isDeleted, Pageable pageable);
}
