package com.example.capstoneproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue("Admin")
public class Admin extends Users{

}
