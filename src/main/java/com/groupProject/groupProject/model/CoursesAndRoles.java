package com.groupProject.groupProject.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "roles_courses")
@NoArgsConstructor
@AllArgsConstructor
public class CoursesAndRoles {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "role_id")
    private Long roleId;



}
