import com.serviceImplementation.Wallet.WalletApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = WalletApplication.class)
public class CoordinatesControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Test
    @WithMockUser(username = "krithi", password = "krithi", roles = "USER")
    public void testMyEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v3/Coordinates/coord-test"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Coordinates"));
    }

    @Test
    @WithMockUser(username = "krithi", password = "krithi", roles = "USER")
    public void testCalculateDistance() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v3/Coordinates/distance"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
