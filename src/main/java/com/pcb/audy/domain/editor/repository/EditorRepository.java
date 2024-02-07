package com.pcb.audy.domain.editor.repository;

import com.pcb.audy.domain.course.entity.Course;
import com.pcb.audy.domain.editor.entity.Editor;
import com.pcb.audy.domain.editor.entity.EditorId;
import com.pcb.audy.domain.user.entity.User;
import com.pcb.audy.global.meta.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EditorRepository extends JpaRepository<Editor, EditorId> {
    Editor findByUserAndCourse(User user, Course course);

    List<Editor> findAllByUserAndRoleOrderByCreateTimestampDesc(User user, Role role);
    List<Editor> findAllByUserOrderByCreateTimestampDesc(User user);
}
