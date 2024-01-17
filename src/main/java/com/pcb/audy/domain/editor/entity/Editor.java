package com.pcb.audy.domain.editor.entity;

import com.pcb.audy.domain.course.entity.Course;
import com.pcb.audy.domain.model.BaseEntity;
import com.pcb.audy.domain.user.entity.User;
import com.pcb.audy.global.meta.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(EditorId.class)
@Table(name = "tb_editor")
public class Editor extends BaseEntity {
    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "courseId")
    private Course course;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    private Editor(User user, Course course, Role role) {
        this.user = user;
        this.course = course;
        this.role = role;
    }
}
