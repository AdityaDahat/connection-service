package com.connection.api.v1.repository.connector.management;

import com.connection.api.v1.model.connector.management.PipelineTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PipelineTemplateRepository extends JpaRepository<PipelineTemplate, String> {
    Optional<PipelineTemplate> findByNameAndIsDeleted(String name, boolean isDeleted);

    Optional<PipelineTemplate> findByIdAndIsDeleted(String id, boolean isDeleted);

    Page<PipelineTemplate> findAllByIsDeleted(boolean isDeleted, Pageable pageable);

    Page<PipelineTemplate> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<PipelineTemplate> findAllByIsDeleted(boolean isDeleted);

}
