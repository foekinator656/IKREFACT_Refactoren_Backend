package nl.hsleiden.ipsen2.groep3.bouncer.controller;

import nl.hsleiden.ipsen2.groep3.bouncer.model.Site;
import nl.hsleiden.ipsen2.groep3.bouncer.model.UserAccount;
import nl.hsleiden.ipsen2.groep3.bouncer.repository.SiteRepository;
import nl.hsleiden.ipsen2.groep3.bouncer.security.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * this is the SiteController used for managing the site CRUD
 *
 * @author Youp van Leeuwen
 */

@RestController
@RequestMapping("/site")
public class SiteController {
    private final SiteRepository siteRepository;

    public SiteController (SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    @GetMapping("")
    public ResponseEntity<List<Site>> index () {
        return new ResponseEntity<>(siteRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Site> create (@RequestParam Map<String, String> siteData) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Site site = new Site();
        site.setName(siteData.get("name"));
        site.setUrl(siteData.get("url"));
        site.setCreatedBy((UserAccount) userPrincipal.getUser());

        site = this.siteRepository.save(site);

        return new ResponseEntity<>(site, HttpStatus.CREATED);
    }

    @PutMapping("")
    public ResponseEntity<Site> save (@RequestBody Site site) {
        site = this.siteRepository.save(site);

        return new ResponseEntity<>(site, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Site> show(@PathVariable("id") Long id) {
        Optional<Site> site = this.siteRepository.findById(id);

        if (site.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(site.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long siteId) {
        this.siteRepository.deleteById(siteId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
