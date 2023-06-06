package com.berryinkstamp.berrybackendservice.models;

import com.berryinkstamp.berrybackendservice.enums.RoleName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "ADMINS")
public class Admin extends AbstractAuditingEntity<Admin> implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @JsonIgnore
   private String password;

   private String name;

   private String email;

   private String phoneNumber;

   @Column(name = "enabled", columnDefinition="BOOLEAN DEFAULT false")
   private boolean enabled;

   @Column(name = "activated", columnDefinition="BOOLEAN DEFAULT false")
   private boolean activated;

   private LocalDateTime lastLoginDate;

   private LocalDateTime lastPasswordResetDate;

   @OneToOne(fetch = FetchType.EAGER)
   private Permission permission;

   @JsonIgnore
   @Enumerated(EnumType.STRING)
   private RoleName role = RoleName.ROLE_ADMIN;

   @JsonProperty(value = "roles")
   public List<RoleName> getRolesOutput() {
      return List.of(role);
   }

   @JsonIgnore
   public boolean isSuperAdmin() {
      return role == RoleName.ROLE_SUPER_ADMIN;
   }

   @JsonIgnore
   public boolean isAdmin() {
      return role == RoleName.ROLE_ADMIN;
   }

}
