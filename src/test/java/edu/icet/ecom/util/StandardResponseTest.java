package edu.icet.ecom.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StandardResponseTest {

    @Test
    void testStandardResponse_GettersAndSetters() {
        StandardResponse response = new StandardResponse();
        response.setCode(200);
        response.setMessage("Success");
        response.setData("Test Data");

        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertEquals("Test Data", response.getData());
    }

    @Test
    void testStandardResponse_Constructor() {
        StandardResponse response = new StandardResponse(201, "Created", null);
        assertEquals(201, response.getCode());
        assertEquals("Created", response.getMessage());
        assertNull(response.getData());
    }
}
