package com.pcb.audy.domain.editor.repository;

import com.pcb.audy.domain.course.entity.Course;
import com.pcb.audy.domain.editor.entity.Editor;
import com.pcb.audy.domain.editor.entity.EditorId;
import com.pcb.audy.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EditorRepository extends JpaRepository<Editor, EditorId> {
    Editor findByUserAndCourse(User user, Course course);
}
