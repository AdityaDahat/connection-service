package com.connection.api.v1.repository.connector.objects;

import com.connection.api.v1.model.connector.objects.ConnectionToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConnectionTokenRepository extends JpaRepository<ConnectionToken , String> {

    Optional<ConnectionToken> findByIdAndConnectionTypeId(String connectionTokenId, String connectionTypeId);
}
