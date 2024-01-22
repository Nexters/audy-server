package com.pcb.audy.domain.pin.entity;

import com.pcb.audy.domain.course.entity.Course;
import com.pcb.audy.domain.model.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "tb_pin")
public class Pin extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pinId;

    private String pinName;
    private String originName;
    private Double latitude;
    private Double longitude;
    private String address;
    private Integer sequence;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "courseId")
    private Course course;

    @Builder
    private Pin(
            Long pinId,
            String pinName,
            String originName,
            Double latitude,
            Double longitude,
            String address,
            Integer sequence,
            Course course) {
        this.pinId = pinId;
        this.pinName = pinName;
        this.originName = originName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.sequence = sequence;
        this.course = course;
    }
}
