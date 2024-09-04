package ar.fdelbo.jpos.service;

import ar.fdelbo.jpos.exception.JPosException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.q2.iso.QMUX;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class JposServiceTest {

    private JposService jposService;
    private QMUX qmux;

    @BeforeEach
    void setup() {
        qmux = mock(QMUX.class);
        jposService = new JposService(1000L, qmux);
    }

    @Test
    void performRequest_whenHappyPath_returnsResponse() throws ISOException {
        //Given
        final var request = new ISOMsg();
        when(qmux.request(any(ISOMsg.class), anyLong())).thenReturn(new ISOMsg());

        //When
        final var response = jposService.performRequest(request);

        //Then
        assertNotNull(response);
        verify(qmux, times(1)).request(any(), anyLong());
        verifyNoMoreInteractions(qmux);
    }

    @Test
    void performRequest_whenQMuxFails_jposExceptionIsExpected() throws ISOException {
        //Given
        final var request = new ISOMsg();
        when(qmux.request(any(ISOMsg.class), anyLong())).thenThrow(new RuntimeException("QMUX Error!"));

        //When
        Executable executable = () -> jposService.performRequest(request);

        //Then
        final var exception = assertThrows(JPosException.class, executable, "Exception is expected due to QMUX fail");
        assertEquals("jPOS error while performing request to the network", exception.getMessage());
        verify(qmux, times(1)).request(any(), anyLong());
        verifyNoMoreInteractions(qmux);
    }
}
