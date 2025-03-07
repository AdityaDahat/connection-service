package com.connection.api.v1.repository.connector.management;

import com.connection.api.v1.model.connector.management.ConnectionDefinition;
import com.connection.api.v1.model.connector.management.dto.ConnectionTypeProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionDefinitionRepository extends JpaRepository<ConnectionDefinition, String> {

    //    @Query(value = "SELECT * FROM connection_definition WHERE name = :name AND mode = :mode AND is_deleted = :isDeleted", nativeQuery = true)
    Optional<ConnectionDefinition> findByNameAndModeAndIsDeleted(@Param("name") String name, @Param("mode") String mode, @Param("isDeleted") boolean isDeleted);

    //    @Query(value = "SELECT * FROM connection_definition WHERE id = :id AND is_deleted = :isDeleted", nativeQuery = true)
    Optional<ConnectionDefinition> findByIdAndIsDeleted(@Param("id") String id, @Param("isDeleted") boolean isDeleted);

    //    @Query(value = "SELECT * FROM connection_definition WHERE id = :id AND connection_type_id = :connectionTypeId AND mode = :mode AND name = :name AND is_deleted = :isDeleted", nativeQuery = true)
    Optional<ConnectionDefinition> findByIdAndConnectionTypeIdAndModeAndNameAndIsDeleted(@Param("id") String id,
                                                                                         @Param("connectionTypeId") String connectionTypeId,
                                                                                         @Param("mode") String mode,
                                                                                         @Param("name") String name,
                                                                                         @Param("isDeleted") boolean isDeleted);

    @Query(value = "SELECT * FROM connection_definition WHERE connection_type_id = :connectionTypeId AND is_deleted = :isDeleted",
            countQuery = "SELECT COUNT(*) FROM connection_definition WHERE connection_type_id = :connectionTypeId AND is_deleted = :isDeleted",
            nativeQuery = true)
    Page<ConnectionDefinition> findAllByConnectionTypeIdAndIsDeleted(@Param("connectionTypeId") String connectionTypeId,
                                                                     @Param("isDeleted") boolean isDeleted,
                                                                     Pageable pageable);

    //    @Query(value = "SELECT * FROM connection_definition_tbl WHERE is_deleted = :isDeleted",
//            countQuery = "SELECT COUNT(*) FROM connection_definition_tbl WHERE is_deleted = :isDeleted",
//            nativeQuery = true)
    Page<ConnectionDefinition> findAllByIsDeleted(@Param("isDeleted") boolean isDeleted, Pageable pageable);


    Optional<ConnectionDefinition> findByIdAndObjectTypeInAndIsDeleted(String id, List<String> objectTypes,
                                                                       boolean isDeleted);

    @Query(value = "SELECT * FROM connection_definition " +
            "WHERE (name ILIKE CONCAT('%', :connectionDefName, '%')) " +
            "OR connection_type_id = :connectionTypeId " +
            "OR object_type = :objectType",
            nativeQuery = true)
    List<ConnectionDefinition> getConnectionDefinitionDetails(
            @Param("connectionDefName") String connectionDefName,
            @Param("connectionTypeId") String connectionTypeId,
            @Param("objectType") String objectType);


    List<ConnectionDefinition> findByNameContainingIgnoreCaseAndConnectionTypeIdAndObjectType(@Param("name") String name, @Param("connectionTypeId") String connectionTypeId, @Param("objectType") String objectType);

}
