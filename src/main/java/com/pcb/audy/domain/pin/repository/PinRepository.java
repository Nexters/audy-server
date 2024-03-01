package com.pcb.audy.domain.pin.repository;

import com.pcb.audy.domain.course.entity.Course;
import com.pcb.audy.domain.pin.entity.Pin;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinRepository extends JpaRepository<Pin, UUID> {
    void deleteByCourse(Course course);
}
