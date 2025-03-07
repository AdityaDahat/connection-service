package com.connection.api.v1.repository.connector.objects;

import com.connection.api.v1.model.connector.objects.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, String> {
    Optional<Connection> findByIdAndIsDeleted(String connectionId, boolean b);
    @Query("SELECT c FROM Connection c WHERE c.projectId = :projectId AND c.isDeleted = :isDeleted AND JSONB_EXTRACT_PATH_TEXT(c.properties, 'customerId') = :customerId AND c.connectionTypeId = :connectionTypeId")
    Optional<Connection> findByProjectIdAndIsDeletedAndCustomerIdAndConnectionTypeId(
            String projectId,
            boolean isDeleted,
            long customerId,
            String connectionTypeId);
}
