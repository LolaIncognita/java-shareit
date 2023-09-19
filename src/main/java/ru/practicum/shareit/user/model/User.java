package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @NotNull
    @Email
    @Column(unique = true)
    private String email;
}