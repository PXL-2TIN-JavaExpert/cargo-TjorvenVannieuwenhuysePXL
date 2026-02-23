package be.pxl.cargo.api;

import be.pxl.cargo.api.request.CreateCargoRequest;
import be.pxl.cargo.domain.Location;
import be.pxl.cargo.service.CargoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CargoController.class)
public class CargoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CargoService cargoService;

    @Test
    void cargoController_postCargos_ValidBody_ShouldReturn201Created() throws Exception {
        CreateCargoRequest createCargoRequest = new CreateCargoRequest("123", 150, Location.CITY_B, Location.SEA_PORT_Z);

        mockMvc.perform(post("/cargos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCargoRequest)
                )
        ).andExpect(status().isCreated());
    }

    @Test
    void cargoController_postCargos_InvalidBody_TooLight_ShouldReturn400BadRequest() throws Exception {
        CreateCargoRequest createCargoRequest = new CreateCargoRequest("123", 3, Location.CITY_B, Location.SEA_PORT_Z);

        mockMvc.perform(post("/cargos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCargoRequest)
                )
        ).andExpect(status().isBadRequest());
    }
}
