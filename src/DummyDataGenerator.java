import java.util.Random;

/*
 * Generates values for characteristics of FileDS objects.
 */
public class DummyDataGenerator {
    public Random random;
    String[] lc = {"free","creativecommon","notusable"};
    String[] r = {"low", "medium","high"};
    String[] Users = {"adam","ben","carl","daniel","eric","fatima","george","henry"};
    

    /**
     * Creates DummyDataGenerator object.
     */
    public DummyDataGenerator(){
        random = new Random();
    }
    public DateHolder generateDateHolder(){
        return new DateHolder(random.nextInt(29)+1, random.nextInt(10)+1, 2015 + random.nextInt(7));
    }
    public String generateUserName(){
        return Users[random.nextInt(Users.length)];
    }
    public String generateLicenseType(){
        return lc[random.nextInt(lc.length)];
    }
    public String generateReso(){
        return r[random.nextInt(r.length)];
    }


}
