package com.pcb.audy.domain.editor.repository;

import com.pcb.audy.domain.course.entity.Course;
import com.pcb.audy.domain.editor.entity.Editor;
import com.pcb.audy.domain.editor.entity.EditorId;
import com.pcb.audy.domain.user.entity.User;
import com.pcb.audy.global.meta.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EditorRepository extends JpaRepository<Editor, EditorId> {
    Editor findByUserAndCourse(User user, Course course);

    Page<Editor> findAllByUserAndRoleOrderByCreateTimestampDesc(
            User user, Role role, Pageable pageable);

    Page<Editor> findAllByUserOrderByCreateTimestampDesc(User user, Pageable pageable);

    Long countByCourse(Course course);
}
