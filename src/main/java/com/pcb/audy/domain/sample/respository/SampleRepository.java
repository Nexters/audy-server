package com.pcb.audy.domain.sample.respository;

import com.pcb.audy.domain.sample.entity.Sample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleRepository extends JpaRepository<Sample, Long> {
    Sample findBySampleId(Long sampleId);
}
