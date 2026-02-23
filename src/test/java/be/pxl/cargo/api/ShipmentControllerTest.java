package be.pxl.cargo.api;

import be.pxl.cargo.api.request.CreateCargoRequest;
import be.pxl.cargo.domain.Location;
import be.pxl.cargo.exceptions.ResourceNotFoundException;
import be.pxl.cargo.service.CargoService;
import be.pxl.cargo.service.ShipmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShipmentController.class)
public class ShipmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ShipmentService shipmentService;

    @MockitoBean
    private CargoService cargoService;

    @BeforeEach
    public void setup() {
        CreateCargoRequest createCargoRequest = new CreateCargoRequest("123", 150, Location.CITY_B, Location.SEA_PORT_Z);

        cargoService.createCargo(createCargoRequest);
    }

    @Test
    void shipmentController_postShipments_addValidCargoToValidShipment_ShouldReturn200Ok() throws Exception {
        mockMvc.perform(put("/shipments/1/cargo/123"))
                .andExpect(status().isOk());
    }

    @Test
    void shipmentController_postShipments_addInvalidCargo_ShouldReturnNotFound() throws Exception {
        doThrow(new ResourceNotFoundException())
                .when(shipmentService)
                .addCargoToShipment(900L, "invalid_cargo");

        // Valid cargo works
        mockMvc.perform(put("/shipments/1/cargo/123"))
                .andExpect(status().isOk());

        // Invalid throws
        mockMvc.perform(put("/shipments/900/cargo/invalid_cargo"))
                .andExpect(status().isNotFound());
    }
}
