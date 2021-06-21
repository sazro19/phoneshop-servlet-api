package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.security.DefaultDosProtectionService;
import com.es.phoneshop.model.product.security.DosProtectionService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DefaultDosProtectionServiceTest {
    private DosProtectionService dosProtectionService;

    @Before
    public void setup() {
        dosProtectionService = DefaultDosProtectionService.getInstance();
    }

    @Test
    public void testIsAllowed() {
        String ip = "1";
        final int MAX = 20;

        int count = 0;
        while (count <= MAX) {
            assertTrue(dosProtectionService.isAllowed(ip));
            count++;
        }

        assertFalse(dosProtectionService.isAllowed(ip));
    }
}


