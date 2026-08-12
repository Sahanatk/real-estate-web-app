    package com.example.myOwnRealtorWebsite.model;

    import com.fasterxml.jackson.annotation.JsonProperty;
    import jakarta.persistence.*;
    import jakarta.validation.constraints.*;
    import lombok.*;
    import org.jspecify.annotations.NonNull;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import java.util.Collection;
    import java.util.List;

    @Entity
    @Table(name = "User")
    @Data
    @NoArgsConstructor
    public class User implements UserDetails {
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Role getRole() {
            return role;
        }

        public void setRole(Role role) {
            this.role = role;
        }

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "user_id")
        private Long id;

        @NotBlank(message = "Email is required")
        @Email(message = "Please provide a valid email address")
        @Column(unique = true,nullable = false)
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private String password;

        @NotBlank(message = "Full name is required")
        @Size(min = 2, max = 50, message = "Full name must be between 2 and 50 characters")
        @Column(name = "full_name", unique = true)
        private String fullName;

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
        private String phone;

        @NotNull(message = "Role is required")
        @Enumerated(EnumType.STRING)
        private Role role; //Admin,Agent,Buyer

        @Override
        @org.jspecify.annotations.NonNull
        public Collection<? extends GrantedAuthority> getAuthorities() {
            if (role == null) {
                return List.of(new SimpleGrantedAuthority("ROLE_USER")); // default fallback
            }
            return List.of(new SimpleGrantedAuthority("ROLE_" + role.name().toUpperCase()));
        }

        @Override
        @NonNull
        public String getUsername() {
            return email;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public boolean isAccountNonExpired() {
            return UserDetails.super.isAccountNonExpired();
        }

        @Override
        public boolean isAccountNonLocked() {
            return UserDetails.super.isAccountNonLocked();
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return UserDetails.super.isCredentialsNonExpired();
        }

        @Override
        public boolean isEnabled() {
            return UserDetails.super.isEnabled();
        }

        //@OneToOne(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
       // private Agent agentProfile;

        /*@OneToMany(mappedBy = "agent",cascade = CascadeType.ALL)*/
        // private List<Property> listings;

        public enum Role {
            ADMIN,AGENT,BUYER,SELLER,USER
        }

    }
