package RajuJanne.vp;

import java.util.Arrays;

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
    int _multip = 1;
    Calendar c;

    public Solver ()
    {

    }

    public Solver (int multip)
    {
        _weeks *= multip;
        _workers *= multip;
        _offdays *= multip;
        _normalOff *= multip;
        _specialOff *= multip;
        _multip = multip;
        // mitä vittua stintille tehää häh
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
        c = new Calendar(_weeks, _workers, _offdays, _normalOff, _specialOff, _maxStint);
        c.seedCalendar(_offdays);
        /*
        c.getStintBreakpoints();
        c.getWorkerErrors();
        c.getDayErrors();
        c.calculateDayOffDifferentials();
         */
    }
    public void Run()
    {
        // Ei tarkista vielä, että jokaisella työntekijällä on yksi vapaa viikonloppu, tai että työputket eivät ole liian pitkiä
        Boolean stop = false;
        do {
            // annetaan Solve metodille 50 yritystä aikaa selvittää paskansa, tai alotetaan alusta.
            Initialize();
            for (int i = 0; i < 25 * _multip; i++) {
                //c.runDayFunctions();
                //c.getStintBreakpoints();
                //c.fixStints();
                if(c.isSolved()) { stop = true;  break; }
                else  {
                    c.swapWorstDays();
                    //c.fixStints();
                }
                if(c.isSolved()) { stop = true;  break; }
                else  {
                    //c.swapWorstDays();
                    c.fixStints();
                }
            }
            if (stop) {
                System.out.println("0 = working, 1 = day off");
                c.ToString();
                System.out.println();
                c.getStintBreakpoints();
                //c.fixStints();
                System.out.println("Debug print for work stints, empty array means no excessive stints found for the worker.");
                System.out.println(c.get_breakpoints());
                break;
            }
        }while (true);
    }
}
