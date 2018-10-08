package RajuJanne.vp;

/**
 * @author Janne Rajuvaara, NTK17SP
 */

public class Solver {
    int _weeks = 2;
    int _workers = 10;
    int _offdays = 4;
    int _maxStint = 6; //ei pakollinen
    int _normalOff = 2; //arkena vapaalla
    int _specialOff = 5; //vkl vapaalla

    public Solver()
    {

    }

    public Solver(int weeks, int workers, int offdays, int maxstint, int normaloff, int specialoff)
    {
        _weeks = weeks;
        _workers = workers;
        _offdays = offdays;
        _maxStint = maxstint;
        _normalOff = normaloff;
        _specialOff = specialoff;
    }

    void Run()
    {
        Calendar c = new Calendar(_weeks, _workers);
        c.seedCalendar(_offdays);
    }

    public int getDayErrors(Calendar c, int d)
    { // palauttaa päivän lomailijoiden määrän ja halutun määrän erotuksen
        int[][] cal = c.get_calendar();
        int val = 0;
        for (int i = 0; i<cal.length; i++)
        {
            // jokaisen työntekijän i päivä x käydään läpi, ja vapaapäivän löytyessä nostetaan val arvoa yhdellä
            if (cal[i][d] == 1) val++;
        }
        if (d%5 == 0 || d%6 == 0) // päivä on jaollinen 5 tai 6 = lauantai tai sunnuntai
        {
            val = val - _specialOff;
        }
        else
        {
            val = val - _normalOff;
        }
        return val;
    }

    public int getWorkerErrors(Calendar c, int w)
    {
        int[][] cal = c.get_calendar();
        int val = 0;
        for (int i = 0; i < cal[i].length ;i++)
        {
            if (cal[w][1] == 1) {
                val++;
            }
        }
        return val - _offdays;
    }
}