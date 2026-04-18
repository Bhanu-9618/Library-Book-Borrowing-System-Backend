package edu.icet.ecom.config;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AppConfigTest {

    @Test
    void modelMapperBean_ShouldBeCreated() {
        AppConfig config = new AppConfig();
        ModelMapper mapper = config.modelMapper();
        assertNotNull(mapper);
    }
}
