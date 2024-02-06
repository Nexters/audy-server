package com.pcb.audy.domain.course.entity;

import com.pcb.audy.domain.editor.entity.Editor;
import com.pcb.audy.domain.model.BaseEntity;
import com.pcb.audy.domain.pin.entity.Pin;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_course")
public class Course extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    private String courseName;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Pin> pinList = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Editor> editorList = new ArrayList<>();

    @Builder
    private Course(Long courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;
    }
}
