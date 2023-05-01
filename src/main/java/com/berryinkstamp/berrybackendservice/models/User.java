package com.berryinkstamp.berrybackendservice.models;

import com.berryinkstamp.berrybackendservice.enums.Gender;
import com.berryinkstamp.berrybackendservice.enums.RoleName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@Entity
@Table(name = "USERS")
public class User extends AbstractAuditingEntity<User> implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @JsonIgnore
   private String password;

   private String name;

   private String email;

   private String phoneNumber;

   @Enumerated(EnumType.STRING)
   private Gender gender;

   private String username;

   @Column(name = "enabled", columnDefinition="BOOLEAN DEFAULT false")
   private boolean enabled;

   @Column(name = "activated", columnDefinition="BOOLEAN DEFAULT false")
   private boolean activated;

   private LocalDateTime lastLoginDate;

   private LocalDateTime lastPasswordResetDate;


   @JsonIgnore
   @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
   @JoinTable(
           name = "USER_ROLES",
           joinColumns = { @JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
           inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
   private Set<Role> roles = new HashSet<>();

   @OneToOne(fetch = FetchType.EAGER)
   private MailSetting mailSetting;

   @OneToOne(fetch = FetchType.EAGER)
   private Address address;

   @OneToOne
   @JoinColumn(name = "printer_profile_id", referencedColumnName = "id")
   private Profile printerProfile;

   @OneToOne
   @JoinColumn(name = "designer_profile_id", referencedColumnName = "id")
   private Profile designerProfile;



   @JsonProperty(value = "roles")
   private List<RoleName> getRolesOutput() {
      return roles.stream().map(Role::getName).collect(Collectors.toList());
   }

}
