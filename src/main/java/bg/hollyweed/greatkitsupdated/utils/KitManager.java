package bg.hollyweed.greatkitsupdated.utils;

import java.util.ArrayList;
import java.util.List;

public class KitManager
{
    private static List<Kit> kits;
    
    public static List<Kit> getKits() {
        return KitManager.kits;
    }
    
    static {
        KitManager.kits = new ArrayList<Kit>();
    }
}
