package com.rajeswari.urlshortener.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Base62EncoderTest {

    @Test
    void shouldEncodeSingleDigitNumbers() {
        assertEquals("1", Base62Encoder.encode(1));
        assertEquals("9", Base62Encoder.encode(9));
        assertEquals("f", Base62Encoder.encode(15));
    }

    @Test
    void shouldEncodeLowercaseRange() {
        assertEquals("a", Base62Encoder.encode(10));
        assertEquals("z", Base62Encoder.encode(35));
    }

    @Test
    void shouldEncodeUppercaseRange() {
        assertEquals("A", Base62Encoder.encode(36));
        assertEquals("Z", Base62Encoder.encode(61));
    }

    @Test
    void shouldEncodeMultipleDigits() {
        assertEquals("10", Base62Encoder.encode(62));
        assertEquals("11", Base62Encoder.encode(63));
    }

    @Test
    void shouldEncodeBoundaryValues() {
        assertEquals("0", Base62Encoder.encode(0));
        assertEquals("Z", Base62Encoder.encode(61));
        assertEquals("10", Base62Encoder.encode(62));
        assertEquals("ZZ", Base62Encoder.encode(3843));
    }
}