package com.cg.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
@Accessors(chain = true)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    @OneToMany(targetEntity = User.class, fetch = FetchType.EAGER)
    private List<User> users;

}
