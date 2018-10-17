package JTR;

/**
 *
 * @author Janne Rajuvaara, NTK17SP
 */

public class Solver {
    int _weeks = 2;
    int _workers = 10;
    int _offdays = 4;
    int _maxStint = 6; //ei pakollinen
    int _normalOff = 2; //arkena vapaalla
    int _specialOff = 5; //vkl vapaalla
    Calendar c;
    
    public Solver ()
    {
        
    }
    
    public Solver (int we, int wo, int od, int no, int so)
    {
        _weeks = we;
        _workers = wo;
        _offdays = od;
        _normalOff = no;
        _specialOff = so;
    }
    
    public void Initialize()
    {        
        c = new Calendar(_weeks, _workers, _offdays, _normalOff, _specialOff);
        c.seedCalendar(_offdays);
    }
    public void Run()
    {
        Initialize();
        c.ToString();
    }
}
