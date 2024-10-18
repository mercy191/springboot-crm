package com.example.springboot_crm.controller;

import com.example.springboot_crm.dto.SellerDTO;
import com.example.springboot_crm.dto.SellerWithTotalAmountDTO;
import com.example.springboot_crm.exception.ResourceNotFoundException;
import com.example.springboot_crm.service.SellerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SellerController.class)
public class SellerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SellerService sellerService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Test getAllSellers - Valid input returns 200")
    public void test_whenValidInput_thenGetAllSellers_andReturns200() throws Exception {

        List<SellerDTO> sellers = Arrays.asList(
                new SellerDTO(1L, "Seller One", "sellerone@example.com", LocalDateTime.of(2024, 1, 1, 0, 0)),
                new SellerDTO(2L, "Seller Two", "sellertwo@example.com", LocalDateTime.of(2024, 1, 1, 1, 1))
        );

        when(sellerService.getAllSellers()).thenReturn(sellers);

        mockMvc.perform(get("/api/springboot_crm/sellers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Seller One"))
                .andExpect(jsonPath("$[0].contactInfo").value("sellerone@example.com"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Seller Two"))
                .andExpect(jsonPath("$[1].contactInfo").value("sellertwo@example.com"));
    }

    @Test
    @DisplayName("Test getAllSellers - Valid input returns 404")
    public void test_whenValidInput_theGetAllSellers_andReturns404() throws Exception{

        when(sellerService.getAllSellers())
                .thenThrow(new ResourceNotFoundException("No sellers found"));

        mockMvc.perform(get("/api/springboot_crm/sellers")).
                andExpect(status().isNotFound());
    }



    @Test
    @DisplayName("Test getSellerById - Valid input returns 200")
    public void test_whenValidPathVariable_thenGetSellerById_andReturns200() throws Exception {

        SellerDTO seller = new SellerDTO(1L, "Seller One", "sellerone@example.com", LocalDateTime.of(2024, 1, 1, 0, 0));

        when(sellerService.getSellerById(1L)).thenReturn(seller);

        mockMvc.perform(get("/api/springboot_crm/sellers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Seller One"));
    }

    @Test
    @DisplayName("Test getSellerById - Valid input returns 404")
    public void test_whenValidPathVariable_thenGetSellerById_andReturns404() throws Exception {

        when(sellerService.getSellerById(1L))
                .thenThrow(new ResourceNotFoundException("Seller not found with id 1"));

        mockMvc.perform(get("/api/springboot_crm/sellers/1"))
                .andExpect(status().isNotFound());
    }



    @Test
    @DisplayName("Test getMostProductiveSeller - Valid input returns 200")
    public void test_whenValidInput_thenGetMostProductiveSeller_andReturns200() throws Exception {
        SellerWithTotalAmountDTO seller = new SellerWithTotalAmountDTO(
                new SellerDTO(1L, "Top Seller", "topseller@example.com",
                        LocalDateTime.of(2024, 1, 1, 0, 0)),
                BigDecimal.valueOf(5000));
        LocalDateTime start = LocalDateTime.now().minusYears(1);
        LocalDateTime end = LocalDateTime.now();

        when(sellerService.getMostProductiveSeller(start, end)).thenReturn(seller);

        mockMvc.perform(get("/api/springboot_crm/sellers/mostProductive")
                        .param("start", start.toString())
                        .param("end", end.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Top Seller"))
                .andExpect(jsonPath("$.contactInfo").value("topseller@example.com"))
                .andExpect(jsonPath("$.totalTransactionAmount").value(5000));
    }

    @Test
    @DisplayName("Test getMostProductiveSeller - Valid input returns 404")
    public void test_whenValidInput_thenGetMostProductiveSeller_andReturns404() throws Exception {

        LocalDateTime start = LocalDateTime.now().minusYears(1);
        LocalDateTime end = LocalDateTime.now();

        when(sellerService.getMostProductiveSeller(start, end))
                .thenThrow(new ResourceNotFoundException("No most productive seller found"));

        mockMvc.perform(get("/api/springboot_crm/sellers/mostProductive")
                .param("start", start.toString())
                .param("end", end.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test getMostProductiveSeller - Missing parameter returns 400")
    public void test_whenMissingRequestParameter_thenNotGetMostProductiveSeller_andReturns400() throws Exception {

        mockMvc.perform(get("/api/springboot_crm/sellers/mostProductive"))
                .andExpect(status().isBadRequest());
    }



    @Test
    @DisplayName("Test getSellersWithTotalAmountLessThan - Valid input returns 200")
    public void test_whenValidInput_thenGetSellersWithTotalAmountLessThan_andReturns200() throws Exception {
        List<SellerWithTotalAmountDTO> sellers = Arrays.asList(
                new SellerWithTotalAmountDTO(new SellerDTO(
                        1L, "Seller One", "sellerone@example.com",
                        LocalDateTime.of(2024, 1, 1, 0, 0)
                ), BigDecimal.valueOf(3000)
                ),
                new SellerWithTotalAmountDTO(new SellerDTO(
                        2L, "Seller Two", "sellertwo@example.com",
                        LocalDateTime.of(2024, 1, 1, 1, 0)
                ), BigDecimal.valueOf(1500))
        );
        LocalDateTime start = LocalDateTime.now().minusYears(1);
        LocalDateTime end = LocalDateTime.now();

        when(sellerService.getSellersWithTotalAmountLessThan(BigDecimal.valueOf(4000), start, end)).thenReturn(sellers);

        mockMvc.perform(get("/api/springboot_crm/sellers/withTotalAmountLessThan")
                        .param("amount", "4000")
                        .param("start", start.toString())
                        .param("end", end.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Seller One"))
                .andExpect(jsonPath("$[0].contactInfo").value("sellerone@example.com"))
                .andExpect(jsonPath("$[0].totalTransactionAmount").value(BigDecimal.valueOf(3000)))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Seller Two"))
                .andExpect(jsonPath("$[1].contactInfo").value("sellertwo@example.com"))
                .andExpect(jsonPath("$[1].totalTransactionAmount").value(BigDecimal.valueOf(1500)));
    }

    @Test
    @DisplayName("Test getMostProductiveSeller - Valid input returns 404")
    public void test_whenValidInput_thenGetSellersWithTotalAmountLessThan_andReturns404() throws Exception {

        LocalDateTime start = LocalDateTime.now().minusYears(1);
        LocalDateTime end = LocalDateTime.now();

        when(sellerService.getSellersWithTotalAmountLessThan(BigDecimal.valueOf(4000), start, end))
                .thenThrow(new ResourceNotFoundException("No sellers found with total transaction amount less than 4000"));

        mockMvc.perform(get("/api/springboot_crm/sellers/mostProductive")
                        .param("amount", "4000")
                        .param("start", start.toString())
                        .param("end", end.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test getSellersWithTotalAmountLessThan - Missing parameter returns 400")
    public void test_whenMissingRequestParameter_thenNotGetSellersWithTotalAmountLessThan_andReturns400() throws Exception {

        mockMvc.perform(get("/api/springboot_crm/sellers/withTotalAmountLessThan"))
                .andExpect(status().isBadRequest());
    }



    @Test
    @DisplayName("Test createSeller - Valid input returns 201")
    public void test_whenValidInput_thenCreateSeller_andReturns201() throws Exception {

        SellerDTO seller = new SellerDTO(null, "New Seller", "newseller@example.com", null);
        SellerDTO createdSeller = new SellerDTO(1L, "New Seller", "newseller@example.com", LocalDateTime.now());

        when(sellerService.createSeller(any(SellerDTO.class))).thenReturn(createdSeller);

        mockMvc.perform(post("/api/springboot_crm/sellers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seller)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Seller"))
                .andExpect(jsonPath("$.contactInfo").value("newseller@example.com"));
    }

    @Test
    @DisplayName("Test createSeller - Not valid request body returns 400")
    public void test_whenNotValidRequestBody_thenNotCreateSeller_andReturns400() throws Exception {

        SellerDTO invalidSeller = new SellerDTO(null, null, null, null);

        mockMvc.perform(post("/api/springboot_crm/sellers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidSeller)))
                .andExpect(status().isBadRequest());
    }



    @Test
    @DisplayName("Test updateSeller - Valid path variable returns 200")
    public void test_whenValidPathVariable_thenUpdateSeller_andReturns200() throws Exception {
        LocalDateTime time = LocalDateTime.now();
        SellerDTO seller = new SellerDTO(1L, "Old Seller", "oldseller@example.com", time);
        SellerDTO updatedSeller = new SellerDTO(1L, "Updated Seller", "updatedseller@example.com", time);

        when(sellerService.updateSeller(eq(1L), any(SellerDTO.class))).thenReturn(updatedSeller);

        mockMvc.perform(put("/api/springboot_crm/sellers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seller)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Seller"))
                .andExpect(jsonPath("$.contactInfo").value("updatedseller@example.com"));
    }

    @Test
    @DisplayName("Test updateSeller - Not Valid variable returns 404")
    public void test_whenNotValidPathVariable_thenUpdateSeller_andReturns404() throws Exception {

        SellerDTO updatedSeller = new SellerDTO(null, "Seller", "seller@example.com", null);

        when(sellerService.updateSeller(1L, updatedSeller))
                .thenThrow(new ResourceNotFoundException("Seller not found with id 1"));

        mockMvc.perform(put("/api/springboot_crm/sellers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedSeller)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test updateSeller - Not valid request body returns 400")
    public void test_whenNotValidRequestBody_thenNotUpdateSeller_andReturns400() throws Exception {

        SellerDTO invalidSeller = new SellerDTO(null, null, null, null);

        mockMvc.perform(put("/api/springboot_crm/sellers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidSeller)))
                .andExpect(status().isBadRequest());
    }



    @Test
    @DisplayName("Test deleteSeller - Valid path variable returns 204")
    public void test_whenValidPathVariable_thenDeleteSeller_andReturns204() throws Exception {

        doNothing().when(sellerService).deleteSeller(1L);

        mockMvc.perform(delete("/api/springboot_crm/sellers/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Test deleteSeller - Not valid path variable returns 404")
    public void test_whenNotValidPathVariable_thenDeleteSeller_andReturns404() throws Exception {

        doThrow(new ResourceNotFoundException("Seller not found with id 1"))
                .when(sellerService).deleteSeller(1L);

        mockMvc.perform(delete("/api/springboot_crm/sellers/1"))
                .andExpect(status().isNotFound());
    }

}

