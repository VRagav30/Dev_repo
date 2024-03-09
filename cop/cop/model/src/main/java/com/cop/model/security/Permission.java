package com.cop.model.security;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

/**
 * @author RESHMA
 */
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name="\"permission\"")
public class Permission {
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String permissionId;

    @NonNull
    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "PERMISSION_ROLE",
    joinColumns = {@JoinColumn(name = "PERMISSION_ID")},
    inverseJoinColumns = {@JoinColumn(name = "ROLE_ID")}
    )
    private Set<Role> roles;

    @Override
    public int hashCode() {
        if (permissionId != null) {
            return permissionId.hashCode();
        } else if (name != null) {
            return name.hashCode();
        }

        return 0;
    }

    @Override
    public boolean equals(Object another) {
        if (another == null || !(another instanceof Permission))
            return false;

        Permission anotherPermission = (Permission) another;

        return (anotherPermission.permissionId != null && (anotherPermission.permissionId == this.permissionId))
                || (anotherPermission.permissionId == null && anotherPermission.name != null && (anotherPermission.name.equals(this.name)));
    }

}
