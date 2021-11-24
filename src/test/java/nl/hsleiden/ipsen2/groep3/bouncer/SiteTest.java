package nl.hsleiden.ipsen2.groep3.bouncer;

import nl.hsleiden.ipsen2.groep3.bouncer.model.AuthenticationRequest;
import nl.hsleiden.ipsen2.groep3.bouncer.model.AuthenticationResponse;
import nl.hsleiden.ipsen2.groep3.bouncer.model.Site;
import nl.hsleiden.ipsen2.groep3.bouncer.repository.SiteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SiteTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private SiteRepository siteRepository;
    private PrepareRequest prepareRequest;

    private String jwt;

    @BeforeEach
    public void login() {
        AuthenticationRequest user = new AuthenticationRequest("johndoe@gmail.com", "12345678");
        this.prepareRequest = new PrepareRequest();

        try {
            MvcResult result = mvc.perform(post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(prepareRequest.getContentAsString(user)))
                    .andExpect(status().isOk())
                    .andReturn();

            String content = result.getResponse().getContentAsString();
            AuthenticationResponse response = prepareRequest.mapper.readValue(content, AuthenticationResponse.class);

            this.jwt = response.getJwt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenSite_whenSiteHasNoID_thenStatus201() {
        try {
            Site site = new Site();
            site.setUrl("http://www.totallyARealSite.DoesNotExcist");
            site.setName(UUID.randomUUID().toString());

            mvc.perform(post("/site")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(prepareRequest.getContentAsString(site))
                            .header("AUTHORIZATION", "Bearer " + this.jwt))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenSite_whenSiteHasID_thenStatus201() {
        try {
            Site site = createSite();
            site.setName(UUID.randomUUID().toString());

            MvcResult result = mvc.perform(put("/site")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(prepareRequest.getContentAsString(site))
                            .header("AUTHORIZATION", "Bearer " + this.jwt))
                    .andExpect(status().isOk())
                    .andReturn();

            String content = result.getResponse().getContentAsString();
            Site updatedSite = prepareRequest.mapper.readValue(content, Site.class);

            assertThat(updatedSite.getName()).isNotEqualTo(site.getName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenSiteID_whenSiteFound_thenStatus200() {
        try {
            Site site = createSite();

            mvc.perform(get("/site/" + site.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("AUTHORIZATION", "Bearer " + this.jwt))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenSiteID_whenSiteNotFound_thenStatus404() {
        try {
            Site site = createSite();

            mvc.perform(get("/site/" + (site.getId() + 10))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("AUTHORIZATION", "Bearer " + this.jwt))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Site createSite() {
        Site site = new Site();
        site.setUrl("http://www.totallyARealSite.DoesNotExcist");
        site.setName(UUID.randomUUID().toString());

        site = siteRepository.save(site);

        return site;
    }
}
