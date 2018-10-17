package JTR;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Janne Rajuvaara, NTK17SP
 */

/*
    public int weeks = 2;
    public int workers = 10;
    public int offdays = 4;
    public int maxStint = 6; //ei pakollinen
    public int normalOff = 2; //arkena vapaalla
    public int specialOff = 5; //vkl vapaalla

*/

public class Calendar {

    private int [][] _calendar;
    int offdays;
    int normalOff;
    int specialOff;
    
    public Calendar(int we, int wo, int od, int no, int so)
    {
        // luodaan työntekijöiden array, joista jokaiselle annetaan arvoksi päivien array
        _calendar = new int[wo][we * 7];
        // sijoitetaan jokaisen alkion arvoksi 0, eli työpäivä. ks. seedCalendar()
        for (int i = 0; i < _calendar.length; i++) {
            for (int j = 0; j < _calendar[i].length; j++) {
                _calendar[i][j] = 0;
            }
        }
        offdays = od;
        normalOff = no;
        specialOff = so;
    }
    
    public int[][] get_calendar () {
        return _calendar;
    }
    
    public void seedCalendar(int x) { // arvotaan jokaiselle tekijälle x määrä vapaapäiviä
        for (int i = 0; i < _calendar.length; i++) {
            int j = 0;
            while (j < x)
            {
                Random r = new Random();
                int arvottu = r.ints(0, (_calendar[i].length)).findFirst().getAsInt();
                
                if(_calendar[i][arvottu] != 1) 
                {
                    _calendar[i][arvottu] = 1;
                    j++;
                }
            }
        }
    }
    
    public int getDayErrors(int d)
    { // palauttaa päivän lomailijoiden määrän ja halutun määrän erotuksen
        //int[][] cal = c.get_calendar();
        int val = 0;
        for (int i = 0; i<_calendar.length; i++)
        {
            // jokaisen työntekijän i päivä x käydään läpi, ja vapaapäivän löytyessä nostetaan val arvoa yhdellä
            if (_calendar[i][d] == 1) val++;
        }
        if (d < 7) {
            if (d%5 == 0 || d%6 == 0) // päivä on jaollinen 5 tai 6 = lauantai tai sunnuntai
                {
                    val = val - specialOff;
                }  
        }
        else if ((d-5)% 7 == 0 || (d-6)% 7 == 0) {
            // päivä miinus 5 tai 6 on jaollinen 7:llä = toinen ja sitä seuraavat viikonloput
            {
                val = val - specialOff;
            }   
        }
        else  
        {
            val = val - normalOff;
        }
        return val;
    }
    
    // Periaatteessa työntekijöillä ei pitäisi voida olla tilannetta, jossa vapaapäiviä on merkitty väärä määrä,
    // sillä seedauksen yhteydessä kaikille arvotaan x määrä vapaapäiviä.
    public int getWorkerErrors(int w)
    {
        //int[][] cal = c.get_calendar();
        int val = 0;
        for (int i = 0; i < _calendar[i].length ;i++)
        {
            if (_calendar[w][1] == 1) {
                val++;
            }
        }
        return val - offdays;
    }
    
    public void ToString() {
        for (int[] _calendar1 : _calendar) {
            System.out.println(Arrays.toString(_calendar1));
        }
    }
}
