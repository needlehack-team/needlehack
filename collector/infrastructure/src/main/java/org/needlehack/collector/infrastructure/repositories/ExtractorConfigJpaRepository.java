package org.needlehack.collector.infrastructure.repositories;

import org.needlehack.collector.infrastructure.repositories.model.ExtractorConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExtractorConfigJpaRepository extends JpaRepository<ExtractorConfigEntity, Long> {

    Optional<ExtractorConfigEntity> findByOriginIgnoreCase(String origin);
}