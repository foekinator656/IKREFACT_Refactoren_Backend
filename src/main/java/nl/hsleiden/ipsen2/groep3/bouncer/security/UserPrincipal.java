package nl.hsleiden.ipsen2.groep3.bouncer.security;

import nl.hsleiden.ipsen2.groep3.bouncer.model.Role;
import nl.hsleiden.ipsen2.groep3.bouncer.model.User;
import nl.hsleiden.ipsen2.groep3.bouncer.model.UserAccount;
import nl.hsleiden.ipsen2.groep3.bouncer.model.Worker;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserPrincipal implements UserDetails {
    private final User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        if (this.user.isGrantedMinimum(Role.MODERATOR)) {
            return ((UserAccount) user).getPassword();
        }

        return null;
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Return the correct user object based on its role.
     * The object can then be casted and used.
     *
     * @return user object, must be casted in to correct type!
     */
    public Object getUser () {
        if (this.user.isGrantedMinimum(Role.MODERATOR)) {
            return this.getUserAccount();
        }

        return this.getWorker();
    }

    private UserAccount getUserAccount () {
        return (UserAccount) this.user;
    }

    private Worker getWorker () {
        return (Worker) this.user;
    }
}
