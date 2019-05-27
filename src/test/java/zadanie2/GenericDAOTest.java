package zadanie2;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class GenericDAOTest {
    @Mock
    private Session mockSession;
    @Mock
    private DbLogger mockLogger;
    @Captor
    private ArgumentCaptor<Exception> captor;
    private GenericDAO genericDAO;

    @BeforeEach
    public void setUp(){
        genericDAO = new GenericDAO();
        genericDAO.setDbLogger(mockLogger);
        genericDAO.setSession(mockSession);
    }
    @Test
    public void should_run_procedure_properly() throws SessionOpenException, CommitException {
        genericDAO.save("someObject");

        Mockito.verify(mockSession).open();
        Mockito.verify(mockSession).openTransaction();
        Mockito.verify(mockSession).save(any());
        Mockito.verify(mockSession).commitTransaction();
        Mockito.verify(mockSession).close();
        Mockito.verify(mockSession,times(0)).rollbackTransaction();
        Mockito.verify(mockLogger,times(0)).log(any());
    }
    @Test
    public void should_throw_exception_while_opening() throws SessionOpenException {
        doThrow(SessionOpenException.class).when(mockSession).open();
        assertThrows(SessionOpenException.class,()->genericDAO.save("someObject"));
    }
    @Test
    public void should_rollback_transaction_after_commit_error() throws CommitException, SessionOpenException {
        doThrow(CommitException.class).when(mockSession).commitTransaction();

        genericDAO.save("someObject");

        Mockito.verify(mockLogger).log(captor.capture());
        assertTrue(captor.getValue() instanceof CommitException);
        Mockito.verify(mockSession).rollbackTransaction();
        Mockito.verify(mockSession).close();
    }
}
