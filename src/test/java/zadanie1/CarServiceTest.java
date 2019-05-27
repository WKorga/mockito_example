package zadanie1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(MockitoExtension.class)
public class CarServiceTest {
    private HashMap<String, Long> mileage;
    private Car nonEmptyCar;
    private Car emptyCar;
    @BeforeEach
    public void setUp(){
        mileage=new HashMap<>();
        mileage.put("2014", 1001L);
        mileage.put("2015", 1003L);
        mileage.put("2016", 1005L);
        mileage.put("2017", 1007L);
        mileage.put("2018", 1011L);
        mileage.put("2019", 1013L);
        nonEmptyCar = new Car();
        nonEmptyCar.setId(1L);
        nonEmptyCar.setYearMileage(mileage);
        emptyCar = new Car();
        emptyCar.setId(2L);
        emptyCar.setYearMileage(new HashMap<>());
    }
    @Test
    public void should_return_valid_mileage_value(){
        CarDAO mockDAO = Mockito.mock(CarDAO.class);
        Mockito.when(mockDAO.findById(Mockito.anyLong())).thenReturn(nonEmptyCar);
        CarService carService = new CarService();
        carService.setEntityManager(mockDAO);
        assertEquals(3023,carService.findMileageBetweenYears(1L,"2016","2018"));
    }
    @Test
    public void should_return_zero_for_empty_list(){
        CarDAO mockDAO = Mockito.mock(CarDAO.class);
        Mockito.when(mockDAO.findById(Mockito.anyLong())).thenReturn(new Car());
        CarService carService = new CarService();
        carService.setEntityManager(mockDAO);
        assertEquals(3023,carService.findMileageBetweenYears(1L,"2016","2018"));
    }
}
